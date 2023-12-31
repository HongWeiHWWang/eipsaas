package com.hotent.base.cache.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import com.github.benmanes.caffeine.cache.Cache;
import com.hotent.base.cache.CacheManager;
import com.hotent.base.cache.ICache;
import com.hotent.base.cache.setting.CacheSetting;

/**
 * CacheManager的抽象实现类
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年6月19日
 */
public abstract class AbstractCacheManager implements CacheManager{
	private Logger logger = LoggerFactory.getLogger(DefaultCacheManager.class);
	/**
	 * 缓存容器
	 * 外层key是cache_name
	 * 里层key是[一级缓存有效时间-二级缓存有效时间-二级缓存自动刷新时间]
	 */
	private final ConcurrentMap<String, ConcurrentMap<String, ICache>> cacheContainer = new ConcurrentHashMap<>(16);

	/**
	 * 缓存名称容器
	 */
	private volatile Set<String> cacheNames = new LinkedHashSet<>();

	/**
	 * CacheManager 容器
	 */
	static Set<CacheManager> cacheManagers = new LinkedHashSet<>();

	/**
	 * 根据缓存名称在CacheManager中没有找到对应Cache时，通过该方法新建一个对应的Cache实例
	 *
	 * @param name                 缓存名称
	 * @param layeringCacheSetting 缓存配置
	 * @return {@link Cache}
	 */
	protected abstract ICache getMissingCache(String name, CacheSetting cacheSetting);

	@Override
	public Collection<ICache> getCache(String name) {
		ConcurrentMap<String, ICache> cacheMap = this.cacheContainer.get(name);
		if (CollectionUtils.isEmpty(cacheMap)) {
			return Collections.emptyList();
		}
		return cacheMap.values();
	}

	@Override
	public ICache getCache(String name, CacheSetting cacheSetting) {
		// 第一次获取缓存Cache，如果有直接返回,如果没有加锁往容器里里面放Cache
		ConcurrentMap<String, ICache> cacheMap = this.cacheContainer.get(name);
		if (!CollectionUtils.isEmpty(cacheMap)) {
			if (cacheMap.size() > 1) {
				//logger.warn("缓存名称为 {} 的缓存,存在两个不同的过期时间配置，请一定注意保证缓存的key唯一性，否则会出现缓存过期时间错乱的情况", name);
			}
			ICache cache = cacheMap.get(cacheSetting.getInternalKey());
			if (cache != null) {
				return cache;
			}
		}

		// 第二次获取缓存Cache，加锁往容器里里面放Cache
		synchronized (this.cacheContainer) {
			cacheMap = this.cacheContainer.get(name);
			if (!CollectionUtils.isEmpty(cacheMap)) {
				// 从容器中获取缓存
				ICache cache = cacheMap.get(cacheSetting.getInternalKey());
				if (cache != null) {
					return cache;
				}
			} else {
				cacheMap = new ConcurrentHashMap<>(16);
				cacheContainer.put(name, cacheMap);
				// 更新缓存名称
				updateCacheNames(name);
			}

			// 新建一个Cache对象
			ICache cache = getMissingCache(name, cacheSetting);
			if (cache != null) {
				// 装饰Cache对象
				cache = decorateCache(cache);
				// 将新的Cache对象放到容器
				cacheMap.put(cacheSetting.getInternalKey(), cache);
				if (cacheMap.size() > 1) {
					logger.warn("缓存名称为 {} 的缓存,存在两个不同的过期时间配置，请一定注意保证缓存的key唯一性，否则会出现缓存过期时间错乱的情况", name);
				}
			}

			return cache;
		}
	}

	@Override
	public Collection<String> getCacheNames() {
		return this.cacheNames;
	}

	/**
	 * 更新缓存名称容器
	 *
	 * @param name 需要添加的缓存名称
	 */
	private void updateCacheNames(String name) {
		cacheNames.add(name);
	}


	/**
	 * 获取Cache对象的装饰示例
	 *
	 * @param cache 需要添加到CacheManager的Cache实例
	 * @return 装饰过后的Cache实例
	 */
	protected ICache decorateCache(ICache cache) {
		return cache;
	}

	/**
	 * 获取缓存容器
	 *
	 * @return 返回缓存容器
	 */
	public ConcurrentMap<String, ConcurrentMap<String, ICache>> getCacheContainer() {
		return cacheContainer;
	}
}
