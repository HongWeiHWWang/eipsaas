package com.hotent.redis.service;

import org.springframework.data.redis.core.RedisTemplate;
import com.hotent.base.cache.ICache;
import com.hotent.base.cache.impl.AbstractCacheManager;
import com.hotent.base.cache.setting.CacheSetting;

/**
 * CacheManager的redis实现
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年6月19日
 */
public class RedisCacheManager extends AbstractCacheManager{
	RedisTemplate<String, Object> redisTemplate;
	
	public RedisCacheManager(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	@Override
	protected ICache getMissingCache(String name, CacheSetting cacheSetting) {
		RedisCache redisCache = new RedisCache(name, redisTemplate, cacheSetting.getSecondaryCacheSetting());
		return redisCache;
	}
}
