package com.hotent.base.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hotent.base.conf.JwtConfig;
import com.hotent.base.jwt.JwtTokenHandler;
import com.hotent.base.model.CommonResult;
import com.hotent.base.service.PropertyService;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.AuthenticationUtil;
import com.hotent.base.util.Base64;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.BpmDefCache;
import com.hotent.base.util.ContextThread;
import com.hotent.base.util.EncryptUtil;
import com.hotent.base.util.FormContextThread;
import com.hotent.base.util.HttpUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.SecurityUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.ThreadMsgUtil;

public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private String encryKey;
	private UserDetailsService userDetailsService;
	private JwtTokenHandler jwtTokenHandler;
	private String tokenHeader;

	public void setEncryKey(String encryKey) {
		this.encryKey = encryKey;
	}

	public JwtAuthorizationTokenFilter(UserDetailsService userDetailsService, JwtTokenHandler jwtTokenHandler, String tokenHeader) {
		this.userDetailsService = userDetailsService;
		this.jwtTokenHandler = jwtTokenHandler;
		this.tokenHeader = tokenHeader;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		ContextThread contextThread = AppUtil.getBean(ContextThread.class);
		if(BeanUtils.isNotEmpty(contextThread)) {
			contextThread.cleanAll();
		}
		//TODO 为了提升流程发起时的性能，缓存的流程定义不做清理，通过设定较短的缓存过期时间来解决缓存的刷新问题
//        BpmDefCache bpmDefCache = AppUtil.getBean(BpmDefCache.class);
//		if(BeanUtils.isNotEmpty(bpmDefCache)){
//            bpmDefCache.cleanAll();
//        }
		FormContextThread formContextThread = AppUtil.getBean(FormContextThread.class);
		if(BeanUtils.isNotEmpty(formContextThread)) {
			formContextThread.cleanAll();
		}
		
		ThreadMsgUtil.clean();
		ThreadMsgUtil.cleanMapMsg();
		
		final String requestHeader = request.getHeader(this.tokenHeader);

		String username = null;
		String authToken = null;
		if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
			authToken = requestHeader.substring(7);
			try {
				username = jwtTokenHandler.getUsernameFromToken(authToken);
			} catch(Exception e) {
				logger.warn("the token valid exception", e);
				send401Error(response, e.getMessage());
				return;
			}
		}
		else if(requestHeader != null && requestHeader.startsWith("Basic ")) {
			String basicToken = requestHeader.substring(6);
			String userPwd = Base64.getFromBase64(basicToken);
			String[] arys = userPwd.split(":");
			if(arys.length==2) {
				try {
					String pwd = "";
					try {
						pwd = EncryptUtil.decrypt(arys[1]);
					} catch (Exception e) {}
					if( "admin".equals(arys[0]) && pwd.equals(encryKey) ){
						SecurityUtil.login(request, arys[0], "", true);
					}else{
						SecurityUtil.login(request, arys[0], arys[1], false);
					}
				}
				catch(Exception e) {
					logger.error("用户认证错误", e);
					send401Error(response, e.getMessage());
					return;
				}
			}
		}
		else {
			logger.warn("couldn't find bearer string, will ignore the header");
		}

		logger.debug("checking authentication for user '{}'", username);
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			logger.debug("security context was null, so authorizating user");

			// It is not compelling necessary to load the use details from the database. You could also store the information
			// in the token and read it from it. It's up to you ;)
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

			// For simple validation it is completely sufficient to just check the token integrity. You don't have to call
			// the database compellingly. Again it's up to you ;)
			if (jwtTokenHandler.validateToken(authToken, userDetails)) {
				//处理单用户登录
				try {
					handleSingleLogin(request, username, authToken, userDetails);
				} catch (Exception e) {
					logger.warn("the token valid exception", e);
					send401Error(response, e.getMessage());
					return;
				}
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
				AuthenticationUtil.setAuthentication(authentication);
			}
		}

		chain.doFilter(request, response);
		
		AuthenticationUtil.removeAll();
		
	}
	
	// 中止请求并返回401错误
	private void send401Error(HttpServletResponse response, String message) throws IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		CommonResult<String> result = new CommonResult<>(false, message);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		PrintWriter writer = response.getWriter();
		writer.print(JsonUtil.toJson(result));
		writer.flush();
	}
	
	/**
	 * 处理单用户登录
	 * @param isMobile
	 * @param username
	 * @param token
	 * @throws Exception 
	 */
	private void handleSingleLogin(HttpServletRequest request,String username,String token,UserDetails userDetails) throws Exception{
		//如果是单用户登录
		JwtConfig jwtConfig = AppUtil.getBean(JwtConfig.class);
		if(jwtConfig.isSingle()){
			boolean isMobile = HttpUtil.isMobile(request);
			String userAgent = isMobile ? "mobile" : "pc";
			String tenantId = HttpUtil.getTenantId();
			PropertyService propertyService = AppUtil.getBean(PropertyService.class);
			String overTime = propertyService.getProperty("overTime","30");
			int overtime = Integer.valueOf(overTime);
			//分钟转换秒
			overtime = overtime * 60;
			// 从缓存中获取token
			String oldToken = jwtTokenHandler.getTokenFromCache(userAgent, tenantId, username, overtime);
			
			if(StringUtil.isNotEmpty(oldToken)) {
				if(jwtTokenHandler.validateToken(oldToken, userDetails) && !oldToken.equals(token)){
					throw new Exception("当前账号已在另一地方登录，若不是本人操作，请注意账号安全！");
				}
			}
		}
	}
}
