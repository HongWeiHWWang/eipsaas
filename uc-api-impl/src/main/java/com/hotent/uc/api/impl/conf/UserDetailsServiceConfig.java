package com.hotent.uc.api.impl.conf;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.hotent.base.annotation.IgnoreOnAssembly;
import com.hotent.base.feign.UCFeignService;
import com.hotent.uc.api.impl.service.UCCachingUserDetailsService;
import com.hotent.uc.api.impl.service.UCRestUserDetailsServiceImpl;

/**
 * 类 {@code UserDetailsServiceConfig} 用户详情服务
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月5日
 */
@Configuration
@IgnoreOnAssembly
public class UserDetailsServiceConfig {
	@Resource
	UserCache userCache;
	
	/**
	 * 配置获取用户详情的服务
     * @param ucFeignService uc模块的服务接口
	 * @return 用户详情的服务
	 */
	@Primary
	@Bean
	public UserDetailsService configUserDetailsService(UCFeignService ucFeignService) {
		UCRestUserDetailsServiceImpl ucRestUserDetailsServiceImpl = new UCRestUserDetailsServiceImpl(ucFeignService);
		UCCachingUserDetailsService detailsService = new UCCachingUserDetailsService(ucRestUserDetailsServiceImpl);
		detailsService.setUserCache(userCache);
		return detailsService;
	}
}
