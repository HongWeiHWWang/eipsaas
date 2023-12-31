package com.hotent.uc.api.impl.service;

import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.util.Assert;

import com.hotent.uc.api.impl.model.UserFacade;

/**
 * 类 {@code UCCachingUserDetailsService} 实现缓存的用户详情服务
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月9日
 */
public class UCCachingUserDetailsService implements UserDetailsService{

    /**
     * 用户信息缓存
     */
	private UserCache userCache = new NullUserCache();

    /**
     * 用户详情服务
     */
	private final UserDetailsService delegate;

	public UCCachingUserDetailsService(UserDetailsService delegate) {
		this.delegate = delegate;
	}

    /**
     * 获取用户信息缓存
     * @return 用户信息缓存
     */
	public UserCache getUserCache() {
		return userCache;
	}

    /**
     * 设置用户信息缓存
     * @param userCache 用户信息缓存
     */
	public void setUserCache(UserCache userCache) {
		this.userCache = userCache;
	}

    /**
     * 根据用户账号获取用户详情
     * @param username 用户账号
     * @return
     */
	public UserDetails loadUserByUsername(String username) {
		UserFacade user = (UserFacade) userCache.getUserFromCache(username);

		if (user == null) {
			user = (UserFacade) delegate.loadUserByUsername(username);
		}

		Assert.notNull(user, "UserDetailsService " + delegate
				+ " returned null for username " + username + ". "
				+ "This is an interface contract violation");

		userCache.putUserInCache(user);

		return user;
	}
}
