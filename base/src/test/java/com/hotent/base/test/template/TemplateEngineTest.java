package com.hotent.base.test.template;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import com.hotent.base.BaseTestCase;
import com.hotent.base.template.TemplateEngine;

public class TemplateEngineTest extends BaseTestCase{
	@Resource
	TemplateEngine templateEngine;
	
	@Test
	public void testTemplate() throws Exception {
		String tableName = "testTable";
		String name = "张三";
		String dateRange = "2018-2020";
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tableIdCode", tableName);
		map.put("baseHref", "http://");
		map.put("name", name);
		map.put("showPageSize", true);
		map.put("showExplain", true);
		map.put("dateRange", dateRange);
		
		String template = "Hello ${name}!";
		String r = templateEngine.parseByTemplate(template, map);
		assertTrue(r.equals(String.format("Hello %s!", name)));
		String template2 = "${name}";
		String r2 = templateEngine.parseByTemplate(template2, map);
		assertTrue(r2.equals(name));
		
		String m = templateEngine.parseByTempName("test.ftl", map);
		assertTrue(m.contains(tableName));
		assertTrue(m.contains(name));
		assertTrue(m.contains(dateRange));
	}
}
