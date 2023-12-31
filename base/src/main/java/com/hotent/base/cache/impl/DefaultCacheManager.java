package com.hotent.base.cache.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;
import com.github.benmanes.caffeine.cache.Cache;
import com.hotent.base.cache.ICache;
import com.hotent.base.cache.setting.CacheSetting;

/**
 * 默认的缓存管理器实现类
 * <p>
 * 默认的缓存管理器仅提供一级缓存
 * </p>
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年6月16日
 */
@Service
@ConditionalOnMissingBean(name= {"redisCacheManager"})
public class DefaultCacheManager extends AbstractCacheManager {
	/**
	 * 根据缓存名称在CacheManager中没有找到对应Cache时，通过该方法新建一个对应的Cache实例
	 *
	 * @param name                 缓存名称
	 * @param layeringCacheSetting 缓存配置
	 * @return {@link Cache}
	 */
	protected ICache getMissingCache(String name, CacheSetting cacheSetting) {
		// 创建一级缓存
		CaffeineCache caffeineCache = new CaffeineCache(name, cacheSetting.getFirstCacheSetting());
		return caffeineCache;
	}
}
