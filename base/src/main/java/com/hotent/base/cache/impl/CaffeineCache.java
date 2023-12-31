package com.hotent.base.cache.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.hotent.base.cache.setting.FirstCacheSetting;
import com.hotent.base.cache.support.ExpireMode;
import com.hotent.base.cache.support.NullValue;
import com.hotent.base.util.BeanUtils;

/**
 * 缓存的Caffeine实现
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月9日
 */
public class CaffeineCache extends AbstractValueAdaptingCache {
	protected static final Logger logger = LoggerFactory.getLogger(CaffeineCache.class);

	/**
	 * 缓存对象
	 */
	private final Cache<Object, Object> cache;

	/**
	 * 使用name和{@link FirstCacheSetting}创建一个 {@link CaffeineCache} 实例
	 *
	 * @param name              缓存名称
	 * @param firstCacheSetting 一级缓存配置 {@link FirstCacheSetting}
	 */
	public CaffeineCache(String name, FirstCacheSetting firstCacheSetting) {
		super(name);
		this.cache = getCache(firstCacheSetting);
	}

	@Override
	public Cache<Object, Object> getNativeCache() {
		return this.cache;
	}

	@Override
	public Object get(Object key) {
		logger.debug("caffeine缓存 key={} 获取缓存", toJsonString(key));

		if (this.cache instanceof LoadingCache) {
			return ((LoadingCache<Object, Object>) this.cache).get(key);
		}
		return cache.getIfPresent(key);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(Object key, Callable<T> valueLoader) {
		logger.debug("caffeine缓存 key={} 获取缓存， 如果没有命中就走库加载缓存", toJsonString(key));

		Object result = this.cache.get(key, k -> loaderValue(key, valueLoader));
		// 如果不允许存NULL值 直接删除NULL值缓存
		boolean isEvict = !isAllowNullValues() && (result == null || result instanceof NullValue);
		if (isEvict) {
			evict(key);
		}
		return (T) fromStoreValue(result);
	}

	@Override
	public void put(Object key, Object value) {
		// 允许存NULL值
		if (isAllowNullValues()) {
			logger.debug("caffeine缓存 key={} put缓存", toJsonString(key));
			this.cache.put(key, toStoreValue(value));
			return;
		}

		// 不允许存NULL值
		if (value != null && !(value instanceof NullValue)) {
			logger.debug("caffeine缓存 key={} put缓存", toJsonString(key));
			this.cache.put(key, toStoreValue(value));
		}
		logger.debug("缓存值为NULL并且不允许存NULL值，不缓存数据");
	}

	@Override
	public Object putIfAbsent(Object key, Object value) {
		logger.debug("caffeine缓存 key={} putIfAbsent 缓存", toJsonString(key));
		boolean flag = !isAllowNullValues() && (value == null || value instanceof NullValue);
		if (flag) {
			return null;
		}
		Object result = this.cache.get(key, k -> toStoreValue(value));
		return fromStoreValue(result);
	}

	@Override
	public void evict(Object key) {
		logger.debug("caffeine缓存 key={} 清除缓存", toJsonString(key));
		this.cache.invalidate(key);
	}

	@Override
	public void clear() {
		logger.debug("caffeine缓存 key={} 清空缓存");
		this.cache.invalidateAll();
	}

	/**
	 * 加载数据
	 */
	private <T> Object loaderValue(Object key, Callable<T> valueLoader) {
		try {
			T t = valueLoader.call();
			logger.debug("caffeine缓存 key={} 从库加载缓存", toJsonString(key));
			return toStoreValue(t);
		} catch (Exception e) {
			throw new LoaderCacheValueException(key, e);
		}
	}

	/**
	 * 根据配置获取本地缓存对象
	 *
	 * @param firstCacheSetting 一级缓存配置
	 * @return {@link Cache}
	 */
	private static Cache<Object, Object> getCache(FirstCacheSetting firstCacheSetting) {
		// 根据配置创建Caffeine builder
		Caffeine<Object, Object> builder = Caffeine.newBuilder();
		builder.initialCapacity(firstCacheSetting.getInitialCapacity());
		builder.maximumSize(firstCacheSetting.getMaximumSize());
		if (ExpireMode.WRITE.equals(firstCacheSetting.getExpireMode())) {
			builder.expireAfterWrite(firstCacheSetting.getExpireTime(), firstCacheSetting.getTimeUnit());
		} else if (ExpireMode.ACCESS.equals(firstCacheSetting.getExpireMode())) {
			builder.expireAfterAccess(firstCacheSetting.getExpireTime(), firstCacheSetting.getTimeUnit());
		}
		// 根据Caffeine builder创建 Cache 对象
		return builder.build();
	}

	@Override
	public boolean isAllowNullValues() {
		return false;
	}

	
	@Override
	public void evictAll(Iterable<? extends Object> keys) {
		if(BeanUtils.isEmpty(keys)) {
			return;
		}
		this.cache.invalidateAll(keys);
	}

	@Override
	public Map<String, String> getAll(Iterable<String> keys, String type) {
		Map<String, String> map = new HashMap<String, String>();
		if (BeanUtils.isEmpty(keys))
			return map;
		HashMap<String, List<Object>> newMap = new HashMap<String, List<Object>>();
		for (Map.Entry<String, List<Object>> entry : newMap.entrySet()) {
			String key = entry.getKey();
			// CaffeineCache不能缓存国际化资源，将key作为值返回
			map.put(key, key);
		}
		return map;
	}

	@Override
	public void putAll(Map<String, Map<String, String>> map) {
		logger.warn("在CaffeineCache中不能对国际化资源进行缓存的读写处理。");
	}

	@Override
	public void hdel(String key, String field) {
		logger.warn("在CaffeineCache中不能对国际化资源进行缓存的读写处理。");
	}
}
