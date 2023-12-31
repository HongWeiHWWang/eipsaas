package com.hotent.uc.api.impl.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.JsonNode;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.util.JsonUtil;
import com.hotent.uc.api.impl.model.UserFacade;

/**
 * 类 {@code UCRestUserDetailsServiceImpl} 通过rest接口获取用户详情的实现类
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月6日
 */
public class UCRestUserDetailsServiceImpl implements UserDetailsService{
	private static final Log logger= LogFactory.getLog(UCRestUserDetailsServiceImpl.class);
	
	private UCFeignService ucFeignService;
	
	public UCRestUserDetailsServiceImpl(UCFeignService ucFeignService) {
		this.ucFeignService = ucFeignService;
		Assert.notNull(this.ucFeignService, "RestInvoker could not be empty.");
	}

    /**
     * 获取用户详情
     * @param username 用户账号
     * @return 用户详情
     * @throws UsernameNotFoundException
     */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			JsonNode result = ucFeignService.loadUserByUsername(username);
			UserFacade user = JsonUtil.toBean(result, UserFacade.class);
			return user;
		} catch (Exception e) {
			logger.error(e);
			throw new UsernameNotFoundException(e.getMessage(), e);
		}
	}
}
