package com.hotent.redis.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.junit.Test;

import com.hotent.base.cache.CacheManager;
import com.hotent.base.cache.ICache;
import com.hotent.base.cache.setting.CacheSetting;
import com.hotent.base.cache.setting.SecondaryCacheSetting;
import com.hotent.base.util.StringUtil;
import com.hotent.redis.RedisTestCase;

/**
 * redis单元测试
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2018-06-06 17:42
 */
public class RedisServiceTest extends RedisTestCase{
	@Resource
	CacheManager cacheManager;

	/**
	 * cache的增删改查
	 * @throws InterruptedException 
	 */
	@Test
	public void testCache() throws InterruptedException {
		CacheSetting buildDefault = CacheSetting.buildDefault("测试redis");
		SecondaryCacheSetting secondaryCacheSetting = buildDefault.getSecondaryCacheSetting();
		secondaryCacheSetting.setExpiration(3);
		secondaryCacheSetting.setTimeUnit(TimeUnit.SECONDS);
		ICache cache = cacheManager.getCache("redis:test", buildDefault);
		
		String key = "name";
		String name = "张三";
		String newName = "李四";
		// 添加
		cache.put(key, name);
		// 取值
		String keyVal = cache.get(key, String.class);
		assertEquals(keyVal, name);
		// 更新值
		cache.put(key, newName);
		String newKeyVal = cache.get(key, String.class);
		assertEquals(newKeyVal, newName);
		// 删除
		cache.evict(key);
		assertTrue(StringUtil.isEmpty(cache.get(key, String.class)));
		
		// 指定过期时间
		cache.put(key, name);
		assertTrue(StringUtil.isNotEmpty(cache.get(key, String.class)));
		Thread.sleep(4000);
		assertTrue(StringUtil.isEmpty(cache.get(key, String.class)));
		cache.put(key, name);
		assertEquals(name, cache.get(key, String.class));
		// 清空
		cache.clear();
		assertTrue(StringUtil.isEmpty(cache.get(key, String.class)));
	}

	/**
	 * 批量查询数据
	 */
	@Test
	public void testRedis(){
		ICache cache = cacheManager.getCache("redis:i18n", CacheSetting.buildDefault("测试redis"));
		
		String key = "zxy";
		String enUsVal = "zhaoxiangyun";
		String zhTwVal = "趙祥雲";
		Map<String, Map<String, String>> map = new HashMap<>();
		Map<String, String> map1 = new HashMap<>();
		map1.put("en-US", enUsVal);
		map1.put("zh-TW", zhTwVal);
		map1.put("zh-CN", "赵祥云");
		map.put(key, map1);
		// 批量插入数据
		cache.putAll(map);
		
		// 查询
		String type = "en-US";
		List<String> codelist = new ArrayList<>();
		codelist.add(key);
		Map<String, String> map2 = cache.getAll(codelist,type);
		assertTrue(map2.containsKey(key));
		assertEquals(map2.get(key), enUsVal);
		
		//清除对应缓存
		String field = "zh-TW";
		cache.hdel(key, field);
		
		Map<String, String> batchGet = cache.getAll(codelist, field);
		assertTrue(batchGet.containsKey(key));
		assertEquals(batchGet.get(key), key);
		
		//删除所有批量添加的数据
		cache.clear();
	}
}
