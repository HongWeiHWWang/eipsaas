package com.hotent.base.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.cache.annotation.CacheEvict;
import com.hotent.base.conf.SsoConfig;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.exception.BaseException;
import com.hotent.base.exception.CertificateException;
import com.hotent.base.exception.ServerRejectException;
import com.hotent.base.feign.PortalFeignService;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.jwt.JwtAuthenticationRequest;
import com.hotent.base.jwt.JwtAuthenticationResponse;
import com.hotent.base.jwt.JwtTokenHandler;
import com.hotent.base.model.CommonResult;
import com.hotent.base.service.PropertyService;
import com.hotent.base.service.PwdStrategyService;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.FluentUtil;
import com.hotent.base.util.HttpUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.MapUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.XmlUtil;
import com.hotent.uc.api.model.IUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags="认证接口")
@ApiGroup(group= {ApiGroupConsts.GROUP_BPM,ApiGroupConsts.GROUP_FORM,ApiGroupConsts.GROUP_PORTAL,ApiGroupConsts.GROUP_UC,ApiGroupConsts.GROUP_OA})
public class AuthenticationRestController{
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationRestController.class);
	@Value("${jwt.header:'Authorization'}")
	private String tokenHeader;
	@Value("${jwt.single:false}")
	private boolean single;
	@Value("${jwt.expiration:604800}")
	private Long expiration;
	@Resource
	AuthenticationManager authenticationManager;
	@Resource
	JwtTokenHandler jwtTokenHandler;
	@Resource
	UserDetailsService userDetailsService;
	@Resource
	SsoConfig ssoConfig;
	@Value("${system.mode.demo:false}")
	protected boolean demoMode;
	@Resource
	UCFeignService uCFeignService;
	@Resource
	PortalFeignService portalFeignService;
	@Resource
	PropertyService propertyService;
	
	/**
	 * 删除缓存的用户详情
	 * <p>该方法没有方法体，通过注解在切面中删除缓存数据</p>
	 * @param userAccount
	 */
	private void deleteUserDetailsCache(String userAccount) {
		AuthenticationRestController bean = AppUtil.getBean(getClass());
		bean.delUserDetailsCache(userAccount);
	}
	
	@CacheEvict(value = "user:details", key = "#userAccount")
	protected void delUserDetailsCache(String userAccount) {}

	@RequestMapping(value = "/auth", method = RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "登录系统", httpMethod = "POST", notes = "登录系统")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException, CertificateException {
		String reqAccount = authenticationRequest.getUsername();
		String reqPassword = authenticationRequest.getPassword(); 
		//清除用户缓存
		this.deleteUserDetailsCache(reqAccount);
		String errorMsg = "";
		try {
			authenticate(reqAccount, reqPassword);
		} catch (Exception e) {
			errorMsg = "账号或密码错误";
			if(BeanUtils.isNotEmpty(e.getCause()) && e.getCause() instanceof CertificateException){
				CertificateException ce = (CertificateException) e.getCause();
				errorMsg = ce.getDetailMessage();
			}
			if(e instanceof LockedException) {
				errorMsg = "账号被禁用或离职";
			}
		}
		if (StringUtil.isNotEmpty(errorMsg)) {
			throw new  BaseException(errorMsg);
		}
		// 当前切中的方法
		HttpServletRequest request = HttpUtil.getRequest();
		boolean isMobile = HttpUtil.isMobile(request);
		// Reload password post-security so we can generate the token
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String token = jwtTokenHandler.generateToken(userDetails);
		String userName = userDetails.getUsername();
		String account = "";
		String userId = "";
		boolean loginStatus = true;
		Map<String,Object> userAttrs = new HashMap<String, Object>();
		if(userDetails instanceof IUser) {
			IUser user = ((IUser)userDetails);
			userName = user.getFullname();
			account = user.getAccount();
			userId = user.getUserId();
			request.setAttribute("loginUser", String.format("%s[%s]",userName,account));
			//校验密码策略
			loginStatus = checkUser(user, reqPassword);
			userAttrs.put("tenantId", user.getTenantId());
		}
		//处理单用户登录
		handleSingleLogin(isMobile, MapUtil.getString(userAttrs, "tenantId"), account, token, expiration.intValue());
		return ResponseEntity.ok(new JwtAuthenticationResponse(token, userName, account, userId, expiration, loginStatus, userAttrs));
	}

	// cas验证ticket并获取当前登录用户账号
	private String getUserNameWithCas(String ticket, String service) throws IOException {
		String casUserDetail = "";
		try{
			casUserDetail = FluentUtil.get(String.format("%s/p3/serviceValidate?ticket=%s&service=%s", ssoConfig.getCasUrl(), ticket, service), "");
		}catch(Exception e){
			e.printStackTrace();
			throw new BaseException("获取cas认证信息失败");
		}
		String json = XmlUtil.toJson(casUserDetail);
		JsonNode jsonNode = JsonUtil.toJsonNode(json);
		String username = jsonNode.get("authenticationSuccess").get("user").asText();
		return username;
	}

	// oauth验证code并获取当前登录用户账号
	private String getUserNameWithOauth(String code, String service) {
		String userName = null;
		String oauthTokenUrl = ssoConfig.getOauthTokenUrl();
		String stufix = String.format("&code=%s&redirect_uri=%s", code, service);
		try {
			String header = ssoConfig.getOauthBasicHeader();
			String tokenResult = FluentUtil.post(oauthTokenUrl + stufix, header, null);
			JsonNode jsonNode = JsonUtil.toJsonNode(tokenResult);
			if(jsonNode!=null && jsonNode.isObject()) {
				String token = jsonNode.get("value").asText();
				String oauthCheckUrl = ssoConfig.getOauthCheckUrl();
				String checkResult = FluentUtil.post(oauthCheckUrl + token, null, null);
				JsonNode checkJNode = JsonUtil.toJsonNode(checkResult);
				if(checkJNode!=null && checkJNode.isObject()) {
					userName = checkJNode.get("user_name").asText();
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new BaseException("获取oauth认证信息失败");
		}
		return userName;
	}

	@RequestMapping(value = "/sso/auth", method = RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "登录系统-单点登录", httpMethod = "GET", notes = "登录系统-单点登录")
	public ResponseEntity<?> ssoAuth(@RequestParam Optional<String> ticket, @RequestParam Optional<String> code, @RequestParam Optional<String> ssoMode, @RequestParam String service) throws AuthenticationException, ClientProtocolException, IOException {
		Assert.isTrue(ssoConfig.isEnable(), "当前服务未开启单点登录");
		String username = null;
		String mode = ssoConfig.getMode();
		if(ssoMode.isPresent()) {
			mode = ssoMode.get();
		}
		// 使用cas认证
		if(ticket.isPresent() && SsoConfig.MODE_CAS.equals(mode)) {
			username = getUserNameWithCas(ticket.get(), service);
		}
		// 使用oauth认证
		else if(code.isPresent() && SsoConfig.MODE_OAUTH.equals(mode)) {
			username = getUserNameWithOauth(code.get(), service);
		}
		// 使用jwt认证
		else if(code.isPresent() && SsoConfig.MODE_JWT.equals(mode)){
			username = jwtTokenHandler.getUsernameFromToken(ticket.get());
		}
		else {
			throw new ServerRejectException("单点登录模式匹配异常");
		}

		//清除用户缓存
		this.deleteUserDetailsCache(username);
		
		// 当前切中的方法
		HttpServletRequest request = HttpUtil.getRequest();
		boolean isMobile = HttpUtil.isMobile(request);
		// Reload password post-security so we can generate the token
		final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		final String token = jwtTokenHandler.generateToken(userDetails);
		String userName = userDetails.getUsername();
		String account = "";
		String userId = "";
		Map<String,Object> userAttrs = new HashMap<String, Object>();
		if(userDetails instanceof IUser) {
			IUser user = ((IUser)userDetails);
			userName = user.getFullname();
			account = user.getAccount();
			userId = user.getUserId();
			request.setAttribute("loginUser", String.format("%s[%s]",userName,account));
			userAttrs.put("tenantId", user.getTenantId());
		}
		//获取超时时间
		logger.debug("通过单点认证登录成功。");
		//处理单用户登录
		if(!(code.isPresent() && SsoConfig.MODE_JWT.equals(mode))){
			handleSingleLogin(isMobile, MapUtil.getString(userAttrs, "tenantId"), userName, token, expiration.intValue());
		}
		// Return the token
		return ResponseEntity.ok(new JwtAuthenticationResponse(token, userName, account, userId, expiration,userAttrs));
	}

	@RequestMapping(value = "/sso/weixin", method = RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "企业微信应用进入手机端-单点登录", httpMethod = "GET", notes = "企业微信应用进入手机端-单点登录")
	public ResponseEntity<?> ssoWeixin( @RequestParam Optional<String> code) throws AuthenticationException, ClientProtocolException, IOException {
		String username = null;
		String resultJson = HttpUtil.sendHttpsRequest(portalFeignService.getUserInfoUrl("weChatWork",code.get()), "", "POST");
		logger.error("企业微信登录返回结果："+resultJson);
		ObjectNode result=null;
		try{
			result = (ObjectNode) JsonUtil.toJsonNode(resultJson);
		}catch (Exception e){
			logger.error(e.getMessage());
		}
		String errcode = result.get("errcode").asText();
		if("0".equals(errcode)) {
			username = result.get("UserId").asText();
			try {
				//清除用户缓存
				this.deleteUserDetailsCache(username);
				
				// 当前切中的方法
				HttpServletRequest request = HttpUtil.getRequest();
				boolean isMobile = HttpUtil.isMobile(request);
				// Reload password post-security so we can generate the token
				final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				final String token = jwtTokenHandler.generateToken(userDetails);
				String userName = userDetails.getUsername();
				String account = "";
				String userId = "";
				String tenantId = "";
				if (userDetails instanceof IUser) {
					IUser user = ((IUser) userDetails);
					userName = user.getFullname();
					account = user.getAccount();
					userId = user.getUserId();
					tenantId = user.getTenantId();
					request.setAttribute("loginUser", String.format("%s[%s]", userName, account));
				}
				logger.debug("通过单点认证登录成功。");
				//处理单用户登录
				handleSingleLogin(isMobile, tenantId, userName, token, expiration.intValue());
				// Return the token
				return ResponseEntity.ok(new JwtAuthenticationResponse(token, userName, account, userId));
			} catch (Exception e) {
				throw new RuntimeException("企业微信登录失败 ,用户账号:"+ username);
			}
		}
		throw new RuntimeException("企业微信登录失败 ： " + result.get("errmsg").asText());
	}

	@RequestMapping(value = "/sso/weixinPublic", method = RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "微信公众号进入手机端", httpMethod = "GET", notes = "微信公众号进入手机端")
	public ResponseEntity<?> weixinPublic( @RequestParam Optional<String> code) throws AuthenticationException, ClientProtocolException, IOException {
		String resultJson = HttpUtil.sendHttpsRequest(portalFeignService.getUserInfoUrl("weChatOffAcc",code.get()), "", "POST");
		ObjectNode result=null;
		try{
			result = (ObjectNode) JsonUtil.toJsonNode(resultJson);
		}catch (Exception e){
			logger.error(e.getMessage());
		}
		if(result.has("openid")) {
			String openid = result.get("openid").asText();
			CommonResult<JsonNode> r = uCFeignService.getUserByOpenId(openid);
			if(r.getState()) {
				JsonNode node = r.getValue();
				if(StringUtil.isNotEmpty(openid) && BeanUtils.isEmpty(node)) {
					return ResponseEntity.ok(new JwtAuthenticationResponse(openid));
				}
				String account = node.get("account").asText();
				//清除用户缓存
				this.deleteUserDetailsCache(account);
				// 当前切中的方法
				HttpServletRequest request = HttpUtil.getRequest();
				boolean isMobile = HttpUtil.isMobile(request);
				// Reload password post-security so we can generate the token
				final UserDetails userDetails = userDetailsService.loadUserByUsername(account);
				final String token = jwtTokenHandler.generateToken(userDetails);
				String userName = userDetails.getUsername();
				String userId = "";
				String tenantId = "";
				if (userDetails instanceof IUser) {
					IUser user = ((IUser) userDetails);
					userName = user.getFullname();
					userId = user.getUserId();
					tenantId = user.getTenantId();
					request.setAttribute("loginUser", String.format("%s[%s]", userName, account));
				}
				//获取超时时间
				String overTime=propertyService.getProperty("overTime","30");
				//处理单用户登录
				handleSingleLogin(isMobile, tenantId, userName, token, expiration.intValue());
				// Return the token
				return ResponseEntity.ok(new JwtAuthenticationResponse(token, userName, account, userId));
			}else {
				if(StringUtil.isNotEmpty(openid)) {
					return ResponseEntity.ok(new JwtAuthenticationResponse(openid));
				}
			}
		}
		throw new RuntimeException("微信登录失败 ： " + result.get("errmsg").asText());
	}


	@RequestMapping(value = "/sso/dingTalk", method = RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "钉钉进入手机端", httpMethod = "GET", notes = "微信公众号进入手机端")
	public ResponseEntity<?> dingTalk( @RequestParam Optional<String> code) throws AuthenticationException, ClientProtocolException, IOException {
		String resultJson = HttpUtil.sendHttpsRequest(portalFeignService.getUserInfoUrl("dingtalk",code.get()), "", "GET");
		ObjectNode result=null;
		try{
			result = (ObjectNode) JsonUtil.toJsonNode(resultJson);
		}catch (Exception e){
			logger.error(e.getMessage());
		}
		if(result.has("userid")) {
			String openid = result.get("userid").asText();
			final UserDetails userDetails = userDetailsService.loadUserByUsername(openid);
			if(BeanUtils.isNotEmpty(userDetails)) {
				String account = openid;
				//清除用户缓存
				this.deleteUserDetailsCache(account);
				// Reload password post-security so we can generate the token
				// 当前切中的方法
				HttpServletRequest request = HttpUtil.getRequest();
				boolean isMobile = HttpUtil.isMobile(request);
				final String token = jwtTokenHandler.generateToken(userDetails);
				String userName = userDetails.getUsername();
				String userId = "";
				String tenantId = "";
				if (userDetails instanceof IUser) {
					IUser user = ((IUser) userDetails);
					userName = user.getFullname();
					userId = user.getUserId();
					tenantId = user.getTenantId();
					request.setAttribute("loginUser", String.format("%s[%s]", userName, account));
				}
				//处理单用户登录
				handleSingleLogin(isMobile, tenantId, userName, token, expiration.intValue());
				// Return the token
				return ResponseEntity.ok(new JwtAuthenticationResponse(token, userName, account, userId));
			}else {
				throw new RuntimeException("钉钉登录失败！"+openid+"账号不存在");
			}
		}
		throw new RuntimeException("钉钉登录失败 ： " + result.get("errmsg").asText());
	}



	/**
	 * 
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/sso/info", method = RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "单点登录配置", httpMethod = "GET", notes = "单点登录配置")
	public ResponseEntity<Map<String,Object>> isUseCas(HttpServletRequest request, HttpServletResponse response) throws IOException{
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("enable", ssoConfig.isEnable());
		map.put("ssoUrl", ssoConfig.getSsoUrl());
		map.put("ssoLogoutUrl", ssoConfig.getSsoLogoutUrl());
		return ResponseEntity.ok(map);
	}

	@RequestMapping(value = "${jwt.route.refresh:/refresh}", method = RequestMethod.GET)
	@ApiOperation(value = "刷新token", httpMethod = "GET", notes = "刷新token")
	public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
		String authToken = request.getHeader(tokenHeader);
		final String token = authToken.substring(7);
		String tenantId = jwtTokenHandler.getTenantIdFromToken(token);
		String account = jwtTokenHandler.getUsernameFromToken(token);
		String refreshedToken = jwtTokenHandler.refreshToken(token);
		boolean isMobile = HttpUtil.isMobile(request);
		// 处理单用户登录 更新单用户登录的token
		handleSingleLogin(isMobile, tenantId, account, refreshedToken, expiration.intValue());
		return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken, "", "", ""));
	}

	/**
	 * Authenticates the user. If something is wrong, an {@link AuthenticationException} will be thrown
	 */
	private void authenticate(String username, String password) throws AuthenticationException, CertificateException {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
	}

	private boolean checkUser(IUser user,String password) {
		if(!user.isAdmin()) {
			//非系统管理员
			PwdStrategyService service=AppUtil.getBean(PwdStrategyService.class);
			JsonNode json = service.getJsonDefault();
			if(BeanUtils.isNotEmpty(json)) {
				// 初始化密码
				String initPwd = json.get("initPwd").asText();
				// 密码策略
				int pwdRule = json.get("pwdRule").asInt();
				// 密码长度
				int pwdLength = json.get("pwdLength").asInt();
				// 密码可用时长
				int duration = json.get("duration").asInt();
				// 启用策略	0:停用，1:启用
				int enable = json.get("enable").asInt();

				if(enable == 1) {
					if(password.equals(initPwd)) {
						return false;
					}
					if(password.length() < pwdLength) {
						return false;
					}

					if(pwdRule!=1) {
						if(pwdRule == 2) {//必须包含数字、字母
							String regex ="^(?![a-zA-z]+$)(?!\\d+$)(?![!@#$%^&*]+$)[a-zA-Z\\d!@#$%^&*]+$";
							boolean result=password.matches(regex);
							if(!result) {
								return false;
							}
						}else if(pwdRule == 3) {//必须包含数字、字母、特殊字符
							String regex = "^(?=.*?[A-Za-z])(?=.*?\\d)(?=.*?[~!@#$%^&*()_+`\\-={}:\";'<>?,.\\/])[a-zA-Z\\d~!@#$%^&*()_+`\\-={}:\";'<>?,.\\/]*$";
							boolean result=password.matches(regex);
							if(!result) {
								return false;
							}
						}else if(pwdRule == 4) {//必须包含数字、大小字母、特殊字符
							String regex = "^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?\\d)(?=.*?[~!@#$%^&*()_+`\\-={}:\";'<>?,.\\/])[a-zA-Z\\d~!@#$%^&*()_+`\\-={}:\";'<>?,.\\/]*$";
							boolean result=password.matches(regex);
							if(!result) {
								return false;
							}
						}
					}

					//密码策略时间
					LocalDateTime pwdCreateTime = user.getPwdCreateTime();
					if(BeanUtils.isNotEmpty(pwdCreateTime)) {
						LocalDateTime currenTime = LocalDateTime.now();
						int size = (int)(currenTime.toLocalDate().toEpochDay() - pwdCreateTime.toLocalDate().toEpochDay());
						if(size > duration) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * 处理单用户登录
	 * @param isMobile
	 * @param username
	 * @param token
	 */
	private void handleSingleLogin(boolean isMobile,String tenantId, String username,String token,int expiration){
		//如果是单用户登录
		if(single){
			String userAgent = isMobile ? "mobile" : "pc";
			// 以当前登录设备、租户ID、用户账号为key将token存放到缓存中
			jwtTokenHandler.putTokenInCache(userAgent, tenantId, username, expiration, token);
		}
	}
}