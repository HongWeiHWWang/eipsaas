package com.hotent.uc.api.impl.service;

import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.hotent.base.cache.annotation.CacheEvict;
import com.hotent.base.cache.annotation.CachePut;
import com.hotent.base.cache.annotation.Cacheable;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.CacheKeyConst;
import com.hotent.base.util.StringUtil;
import com.hotent.uc.api.impl.model.UserFacade;

/**
 * 类 {@code UserCacheImpl} 用户详情的缓存器
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月9日
 */
@Service
public class UserCacheImpl implements UserCache{
    /**
     * 根据用户账号获取缓存的用户详情
     * @param username 用户账号
     * @return 用户详情
     */
	@Cacheable(value = CacheKeyConst.USER_NAME_CACHENAME, key = "#username")
	public UserDetails getUserFromCache(String username) {
		return null;
	}

    /**
     * 根据用户信息把用户信息添加到缓存里面
     * @param user 用户信息
     */
	public void putUserInCache(UserDetails user) {
		String username = user.getUsername();
		UserCacheImpl bean = AppUtil.getBean(getClass());
		if(user instanceof UserFacade) {
			UserFacade userFacade = (UserFacade)user;
			String id = userFacade.getId();
			bean.putUserIdInCache(id, username);
		}
		bean.putUserDetailsInCache(user);
	}
	
	@CachePut(value = CacheKeyConst.USER_NAME_CACHENAME, key = "#user.username")
	protected UserDetails putUserDetailsInCache(UserDetails user) {
		return user;
	}
	
	@CachePut(value = CacheKeyConst.USER_ID_CACHENAME, key = "#userId")
	protected String putUserIdInCache(String userId, String username) {
		return username;
	}
	
	@Cacheable(value = CacheKeyConst.USER_ID_CACHENAME, key = "#userId")
	protected String getUsernameByUserId(String userId) {
		return null;
	}
	
	/**
	 * 通过用户ID从缓存中获取用户
	 * @param id 用户ID
	 * @return 用户详情
	 */
	public UserFacade getUserFromCacheById(String id) {
		UserCacheImpl bean = AppUtil.getBean(getClass());
		String username = bean.getUsernameByUserId(id);
		if(StringUtil.isNotEmpty(username)) {
			UserDetails userDetails = bean.getUserFromCache(username);
			if(userDetails!=null && userDetails instanceof UserFacade) {
				return (UserFacade)userDetails;
			}
			return null;
		}
		return null;
	}

    /**
     * 根据用户账号删除缓存用户信息
     * @param username 用户账号
     */
	@CacheEvict(value = CacheKeyConst.USER_NAME_CACHENAME, key = "#username")
	public void removeUserFromCache(String username) {}
}
