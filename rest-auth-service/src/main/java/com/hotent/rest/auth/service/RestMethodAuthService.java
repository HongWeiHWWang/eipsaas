package com.hotent.rest.auth.service;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.hotent.base.feign.PortalFeignService;
import com.hotent.base.security.MethodAuthService;

/**
 * 
 * 其他服务模块需要引用这个包  这个包是从portal 服务模块中获取url的认证信息
 * @author liyg 
 * @Primary  不能删除
 */
@Service
@Primary
public class RestMethodAuthService implements MethodAuthService
{
	private static Logger logger = LoggerFactory.getLogger(RestMethodAuthService.class);
	@Autowired
	PortalFeignService portalFeignService;
	@Override
	public List<HashMap<String, String>> getMethodAuth() {
		List<HashMap<String, String>> methodRoleAuth = portalFeignService.getMethodRoleAuth();
		logger.debug(" portalFeign " + methodRoleAuth );
		return methodRoleAuth;
	}
}
