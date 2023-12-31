package com.hotent.base.test.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.junit.Test;

import com.hotent.base.BaseTestCase;
import com.hotent.base.cache.CacheManager;
import com.hotent.base.cache.ICache;
import com.hotent.base.cache.setting.CacheSetting;
import com.hotent.base.cache.setting.FirstCacheSetting;
import com.hotent.base.example.model.Student;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;

/**
 * ICache manager测试
 * 
 * @company 广州宏天软件股份有限公司
 * @author jason
 * @email liyg@jee-soft.cn
 * @date 2020年4月13日
 */
public class MemoryCacheTest extends BaseTestCase{
	private static final String cacheName = "memoryCache:test";
	private static final CacheSetting cacheSetting = CacheSetting.buildDefault("测试默认缓存");
	
	@Resource
	CacheManager cacheManager;
	
	@Test
	public void testCrud() throws InterruptedException{
		FirstCacheSetting firstCacheSetting = cacheSetting.getFirstCacheSetting();
		firstCacheSetting.setExpireTime(3);
		firstCacheSetting.setTimeUnit(TimeUnit.SECONDS);
		ICache cache = cacheManager.getCache(cacheName, cacheSetting);
		cache.put("name", "jason");
		String name = cache.get("name", String.class);
		assertEquals("jason", name);
		
		cache.put("name", "张三");
		assertEquals("张三", cache.get("name", String.class));
		
		cache.evict("name");
		String name2 = cache.get("name", String.class);
		assertTrue(StringUtil.isEmpty(name2));
		
		cache.put("age", 23);
		assertEquals(23, cache.get("age", Integer.class));
		Thread.sleep(4000);
		assertTrue(BeanUtils.isEmpty(cache.get("age", Integer.class)));
		
		String nm1 = "李四";
		Student st1 = new Student("1", nm1, LocalDate.now(), new Short("1"), String.format("我叫%s，小学一年级学生", nm1));
		
		cache.put("st1", st1);
		
		Student student = cache.get("st1", Student.class);
		assertEquals("1", student.getId());
		
		cache.clear();
		assertTrue(BeanUtils.isEmpty(cache.get("st1", Student.class)));
	}
}
