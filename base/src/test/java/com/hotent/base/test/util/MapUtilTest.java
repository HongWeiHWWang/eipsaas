package com.hotent.base.test.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.hotent.base.util.MapUtil;

/**
 * JsonUtil测试
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月20日
 */
public class MapUtilTest{
	@Test
	public void testMapUtil() throws Exception{
		Map<String,String> map = new HashMap<String, String>();
		map.put(null, "null");
		map.put("user_1", "user_1");
		map.put("user_2", "user_2");
		map.put("menu_1", "menu_1");
		MapUtil.delByStartKey(map, "aa");
		
		Set<String> keySet = map.keySet();
		for (String string : keySet) {
			System.out.println(string);
		}
	}
}
