package com.hotent.redis.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import com.hotent.base.cache.impl.AbstractValueAdaptingCache;
import com.hotent.base.cache.setting.SecondaryCacheSetting;
import com.hotent.base.cache.support.NullValue;
import com.hotent.base.util.BeanUtils;
import com.hotent.redis.support.AwaitThreadContainer;
import com.hotent.redis.support.Lock;
import com.hotent.redis.util.RedisHelper;
import com.hotent.redis.util.ThreadTaskUtils;

/**
 * cache的redis实现
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年6月17日
 */
@SuppressWarnings("unchecked")
public class RedisCache extends AbstractValueAdaptingCache{
	protected static final Logger logger = LoggerFactory.getLogger(RedisCache.class);
	
	/**
     * 刷新缓存重试次数
     */
    private static final int RETRY_COUNT = 20;
    /**
     * 刷新缓存等待时间，单位毫秒
     */
    private static final long WAIT_TIME = 20;
    /**
     * 等待线程容器
     */
    private AwaitThreadContainer container = new AwaitThreadContainer();
	/**
	 * redis 客户端
	 */
	private RedisTemplate<String, Object> redisTemplate;

	/**
	 * 缓存有效时间,毫秒
	 */
	private long expiration;

	/**
	 * 缓存主动在失效前强制刷新缓存的时间
	 * 单位：毫秒
	 */
	private long preloadTime = 0;

	/**
	 * 是否强制刷新（执行被缓存的方法），默认是false
	 */
	private boolean forceRefresh = false;

	/**
	 * 是否使用缓存名称作为 redis key 前缀
	 */
	private boolean usePrefix;

	/**
	 * 是否允许为NULL
	 */
	private final boolean allowNullValues;
	
	private final TimeUnit timeUnit;

	/**
	 * 非空值和null值之间的时间倍率，默认是1。allowNullValue=true才有效
	 * <p>
	 * 如配置缓存的有效时间是200秒，倍率这设置成10，
	 * 那么当缓存value为null时，缓存的有效时间将是20秒，非空时为200秒
	 * </p>
	 */
	private final int magnification;

	public RedisCache(String name, RedisTemplate<String, Object> redisTemplate, SecondaryCacheSetting secondaryCacheSetting) {
		super(name);
		this.redisTemplate = redisTemplate;
		this.expiration = secondaryCacheSetting.getExpiration();
		this.preloadTime = secondaryCacheSetting.getPreloadTime();
		this.forceRefresh = secondaryCacheSetting.isForceRefresh();
		this.usePrefix = secondaryCacheSetting.isUsePrefix();
		this.allowNullValues = secondaryCacheSetting.isAllowNullValue();
		this.magnification = secondaryCacheSetting.getMagnification();
		timeUnit = secondaryCacheSetting.getTimeUnit();
	}

	@Override
	public RedisTemplate<String, Object> getNativeCache() {
		return this.redisTemplate;
	}

	/**
	 * 获取 RedisCacheKey
	 *
	 * @param key 缓存key
	 * @return RedisCacheKey
	 */
	public RedisCacheKey getRedisCacheKey(Object key) {
		return new RedisCacheKey(key, redisTemplate.getKeySerializer())
				.cacheName(getName()).usePrefix(usePrefix);
	}

	@Override
	public Object get(Object key) {
		RedisCacheKey redisCacheKey = getRedisCacheKey(key);
		logger.debug("redis缓存 key= {} 查询redis缓存", redisCacheKey.getKey());
		return redisTemplate.opsForValue().get(redisCacheKey.getKey());
	}

	@Override
	public <T> T get(Object key, Callable<T> valueLoader) {
		RedisCacheKey redisCacheKey = getRedisCacheKey(key);
		logger.debug("redis缓存 key= {} 查询redis缓存如果没有命中，从数据库获取数据", redisCacheKey.getKey());
		// 先获取缓存，如果有直接返回
		Object result = redisTemplate.opsForValue().get(redisCacheKey.getKey());
		if (result != null || redisTemplate.hasKey(redisCacheKey.getKey())) {
			// 刷新缓存
			refreshCache(redisCacheKey, valueLoader, result);
			return (T) fromStoreValue(result);
		}
		// 执行缓存方法
		return executeCacheMethod(redisCacheKey, valueLoader);
	}

	@Override
	public void put(Object key, Object value) {
		RedisCacheKey redisCacheKey = getRedisCacheKey(key);
		logger.debug("redis缓存 key= {} put缓存", redisCacheKey.getKey());
		putValue(redisCacheKey, value);
	}

	@Override
	public Object putIfAbsent(Object key, Object value) {
		logger.debug("redis缓存 key= {} putIfAbsent缓存", getRedisCacheKey(key).getKey());
		Object reult = get(key);
		if (reult != null) {
			return reult;
		}
		put(key, value);
		return null;
	}

	@Override
	public void evict(Object key) {
		RedisCacheKey redisCacheKey = getRedisCacheKey(key);
		logger.info("清除redis缓存 key= {} ", redisCacheKey.getKey());
		redisTemplate.delete(redisCacheKey.getKey());
	}

	@Override
	public void evictAll(Iterable<? extends Object> keys) {

	}

	@Override
	public void clear() {
		// 必须开启了使用缓存名称作为前缀，clear才有效
		if (usePrefix) {
			logger.info("清空redis缓存 ，缓存前缀为{}", getName());

			Set<String> keys = RedisHelper.scan(redisTemplate, getName() + "*");
			if (!CollectionUtils.isEmpty(keys)) {
				redisTemplate.delete(keys);
			}
		}
	}

	@Override
	public boolean isAllowNullValues() {
		return this.allowNullValues;
	}

	/**
	 * 同一个线程循环5次查询缓存，每次等待20毫秒，如果还是没有数据直接去执行被缓存的方法
	 */
	private <T> T executeCacheMethod(RedisCacheKey redisCacheKey, Callable<T> valueLoader) {
		Lock redisLock = new Lock(redisTemplate, redisCacheKey.getKey() + "_sync_lock");
		// 同一个线程循环20次查询缓存，每次等待20毫秒，如果还是没有数据直接去执行被缓存的方法
		for (int i = 0; i < RETRY_COUNT; i++) {
			try {
				// 先取缓存，如果有直接返回，没有再去做拿锁操作
				Object result = redisTemplate.opsForValue().get(redisCacheKey.getKey());
				if (result != null) {
					logger.debug("redis缓存 key= {} 获取到锁后查询查询缓存命中，不需要执行被缓存的方法", redisCacheKey.getKey());
					return (T) fromStoreValue(result);
				}

				// 获取分布式锁去后台查询数据
				if (redisLock.lock()) {
					T t = loaderAndPutValue(redisCacheKey, valueLoader, true);
					logger.debug("redis缓存 key= {} 从数据库获取数据完毕，唤醒所有等待线程", redisCacheKey.getKey());
					// 唤醒线程
					container.signalAll(redisCacheKey.getKey());
					return t;
				}
				// 线程等待
				logger.debug("redis缓存 key= {} 从数据库获取数据未获取到锁，进入等待状态，等待{}毫秒", redisCacheKey.getKey(), WAIT_TIME);
				container.await(redisCacheKey.getKey(), WAIT_TIME);
			} catch (Exception e) {
				container.signalAll(redisCacheKey.getKey());
				throw new LoaderCacheValueException(redisCacheKey.getKey(), e);
			} finally {
				redisLock.unlock();
			}
		}
		logger.debug("redis缓存 key={} 等待{}次，共{}毫秒，任未获取到缓存，直接去执行被缓存的方法", redisCacheKey.getKey(), RETRY_COUNT, RETRY_COUNT * WAIT_TIME, WAIT_TIME);
		return loaderAndPutValue(redisCacheKey, valueLoader, true);
	}

	/**
	 * 加载并将数据放到redis缓存
	 */
	private <T> T loaderAndPutValue(RedisCacheKey key, Callable<T> valueLoader, boolean isLoad) {
		long start = System.currentTimeMillis();
		try {
			// 加载数据
			Object result = putValue(key, valueLoader.call());
			logger.debug("redis缓存 key={} 执行被缓存的方法，并将其放入缓存, 耗时：{}。", key.getKey(), System.currentTimeMillis() - start);
			return (T) fromStoreValue(result);
		} catch (Exception e) {
			throw new LoaderCacheValueException(key.getKey(), e);
		}
	}

	private Object putValue(RedisCacheKey key, Object value) {
		Object result = toStoreValue(value);
		// redis 缓存不允许直接存NULL
		if (result == null) {
			return result;
		}
		// 不允许缓存NULL值，删除缓存
		if (!isAllowNullValues() && result instanceof NullValue) {
			redisTemplate.delete(key.getKey());
			return result;
		}

		// 允许缓存NULL值
		long expirationTime = this.expiration;
		// 允许缓存NULL值且缓存为值为null时需要重新计算缓存时间
		if (isAllowNullValues() && result instanceof NullValue) {
			expirationTime = expirationTime / getMagnification();
		}
		// 将数据放到缓存
		redisTemplate.opsForValue().set(key.getKey(), result, expirationTime, timeUnit);
		return result;
	}

	/**
	 * 刷新缓存数据
	 */
	private <T> void refreshCache(RedisCacheKey redisCacheKey, Callable<T> valueLoader, Object result) {
		Long ttl = redisTemplate.getExpire(redisCacheKey.getKey());
		Long preload = preloadTime;
		// 允许缓存NULL值，则自动刷新时间也要除以倍数
		boolean flag = isAllowNullValues() && (result instanceof NullValue || result == null);
		if (flag) {
			preload = preload / getMagnification();
		}
		if (null != ttl && ttl > 0 && TimeUnit.SECONDS.toMillis(ttl) <= preload) {
			// 判断是否需要强制刷新在开启刷新线程
			if (!getForceRefresh()) {
				logger.debug("redis缓存 key={} 软刷新缓存模式", redisCacheKey.getKey());
				softRefresh(redisCacheKey);
			} else {
				logger.debug("redis缓存 key={} 强刷新缓存模式", redisCacheKey.getKey());
				forceRefresh(redisCacheKey, valueLoader);
			}
		}
	}

	/**
	 * 软刷新，直接修改缓存时间
	 *
	 * @param redisCacheKey {@link RedisCacheKey}
	 */
	private void softRefresh(RedisCacheKey redisCacheKey) {
		// 加一个分布式锁，只放一个请求去刷新缓存
		Lock redisLock = new Lock(redisTemplate, redisCacheKey.getKey() + "_lock");
		try {
			if (redisLock.tryLock()) {
				redisTemplate.expire(redisCacheKey.getKey(), this.expiration, TimeUnit.MILLISECONDS);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			redisLock.unlock();
		}
	}

	/**
	 * 硬刷新（执行被缓存的方法）
	 *
	 * @param redisCacheKey {@link RedisCacheKey}
	 * @param valueLoader   数据加载器
	 */
	private <T> void forceRefresh(RedisCacheKey redisCacheKey, Callable<T> valueLoader) {
		// 尽量少的去开启线程，因为线程池是有限的
		ThreadTaskUtils.run(() -> {
			// 加一个分布式锁，只放一个请求去刷新缓存
			Lock redisLock = new Lock(redisTemplate, redisCacheKey.getKey() + "_lock");
			try {
				if (redisLock.lock()) {
					// 获取锁之后再判断一下过期时间，看是否需要加载数据
					Long ttl = redisTemplate.getExpire(redisCacheKey.getKey());
					if (null != ttl && ttl > 0 && TimeUnit.SECONDS.toMillis(ttl) <= preloadTime) {
						// 加载数据并放到缓存
						loaderAndPutValue(redisCacheKey, valueLoader, false);
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			} finally {
				redisLock.unlock();
			}
		});
	}

	/**
	 * 是否强制刷新（执行被缓存的方法），默认是false
	 *
	 * @return boolean
	 */
	private boolean getForceRefresh() {
		return forceRefresh;
	}

	/**
	 * 非空值和null值之间的时间倍率，默认是1。
	 *
	 * @return int
	 */
	public int getMagnification() {
		return magnification;
	}

	@Override
	public Map<String, String> getAll(Iterable<String> keys, String type) {
		Map<String, String> map = new HashMap<String, String>();
		if (BeanUtils.isEmpty(keys))
			return map;
		HashMap<String, List<Object>> newMap = new HashMap<String, List<Object>>();
		for (String key : keys) {
			RedisCacheKey redisCacheKey = getRedisCacheKey(key);
			List<Object> multiGet = redisTemplate.opsForHash().multiGet(redisCacheKey.getKey(), Arrays.asList(type));
			newMap.put(key, multiGet);
		}
		for (Map.Entry<String, List<Object>> entry : newMap.entrySet()) {
			String key = entry.getKey();
			List<Object> list = entry.getValue();
			if (BeanUtils.isNotEmpty(list) && BeanUtils.isNotEmpty(list.get(0))) {
				map.put(key, String.valueOf(list.get(0)));
			} else {
				// 没找到国际化资源时，将key作为值返回
				map.put(key, key);
			}
		}
		return map;
	}

	@Override
	public void putAll(Map<String, Map<String, String>> map) {
		if(BeanUtils.isEmpty(map)) {
			return;
		}
		try {
			Iterator<Map.Entry<String, Map<String, String>>> it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Map<String, String>> next = it.next();
				String key = next.getKey();
				RedisCacheKey redisCacheKey = getRedisCacheKey(key);
				Map<String, String> value = next.getValue();
				redisTemplate.opsForHash().putAll(redisCacheKey.getKey(), value);
			}
		}
		catch(Exception ex) {
			logger.error("批量存放数据到缓存中时出错了：", ExceptionUtils.getRootCauseMessage(ex));
		}
	}

	@Override
	public void hdel(String key, String field) {
		RedisCacheKey redisCacheKey = getRedisCacheKey(key);
		redisTemplate.opsForHash().delete(redisCacheKey.getKey(), field);
	}
}
