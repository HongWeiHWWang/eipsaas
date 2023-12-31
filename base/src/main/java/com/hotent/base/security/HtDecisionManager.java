package com.hotent.base.security;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;

import com.hotent.base.constants.SystemConstants;
import com.hotent.base.util.AuthenticationUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.PlatformConsts;
import com.hotent.base.util.StringUtil;

public class HtDecisionManager implements AccessDecisionManager {


	@SuppressWarnings("unchecked")
	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {
		try {
			if (BeanUtils.isEmpty(configAttributes))
				return;

			HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
			String requestHeader = request.getHeader("Referer");
			if (SystemConstants.REFERER_FEIGN.equals(requestHeader)) {
				return;
			}
			Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) authentication
					.getAuthorities();

			for (GrantedAuthority grantedAuthority : authorities) {
				// 超级管理员
				if (PlatformConsts.ROLE_SUPER.equals(grantedAuthority.getAuthority())) {
					return;
				}
			}

			for (ConfigAttribute configAttribute : configAttributes) {
				if(BeanUtils.isEmpty(configAttribute)) continue;
				String configVal = configAttribute.toString();
				// 匿名资源允许访问
				if(PlatformConsts.PERMIT_All.equals(configVal)) {
					return;
				}
				// 受权限控制的资源
				else if(PlatformConsts.AUTHENTICATED.equals(configVal)) {
					// 匿名访问时抛出 401异常
					if(AuthenticationUtil.isAnonymous(authentication)) {
						throw new InsufficientAuthenticationException("需要提供jwt授权码");
					}
					else {
						// 如果该资源未加入权限管理列表，则允许访问
						String attribute = configAttribute.getAttribute();
						if(StringUtil.isEmpty(attribute)) {
							return;
						}
					}
				}
			}
			
			for (GrantedAuthority grantedAuthority : authorities) {
				for (ConfigAttribute configAttribute : configAttributes) {
					if (grantedAuthority.getAuthority().equals(
							configAttribute.getAttribute())) {
						// 有权限
						return;
					}
				}
			}

			throw new AccessDeniedException("您没有权限， 请联系系统管理员");
			
		}
		finally {
			// 完成权限校验后，清理线程变量中的权限数据。
			HtInvocationSecurityMetadataSourceService.clearMapThreadLocal();
		}
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}
}
