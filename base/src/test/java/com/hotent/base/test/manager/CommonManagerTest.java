package com.hotent.base.test.manager;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;

import com.hotent.base.BaseTestCase;
import com.hotent.base.dao.CommonDao;
import com.hotent.base.handler.MultiTenantHandler;
import com.hotent.base.handler.MultiTenantIgnoreResult;
import com.hotent.base.manager.CommonManager;
import com.hotent.base.query.FieldRelation;
import com.hotent.base.query.FieldSort;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryField;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.MapUtil;
import com.hotent.base.util.SQLUtil;

/**
 * 通用查询测试
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月25日
 */
public class CommonManagerTest extends BaseTestCase {
	@Resource
	CommonManager commonManager;
	@Resource
	CommonManager studentManager;
	@Resource
	CommonDao commonDao;

	@Test
	public void testInsert() throws Exception{
		String dbType = SQLUtil.getDbType();
		assertEquals("h2", dbType);
		for(int i=10;i<210;i++){
			String sql= String.format("insert into ex_student (id_,name_,birthday_,sex_,desc_) values ('%s', '%s', '%s', %s, '%s')", i, "同学" + i, LocalDateTime.now(), i%2, "我是小学一年级学生");
			commonManager.execute(sql);
		}
		String sql = String.format("select * from ex_student");
		List<Map<String, Object>> queryByPage = commonManager.query(sql);
		assertEquals(queryByPage.size(), 200);
		
		String sql2 = "select * from ex_student where name_=? and sex_=?";
		List<Map<String, Object>> r2 = commonManager.query(sql2, "同学17", 17%2);
		assertEquals(r2.size(), 1);
		assertEquals(MapUtil.getString(r2.get(0), "ID_"), "17");
		
		String sql3 = "select * from ex_student where name_=#{name} and sex_=#{sex}";
		Map<String, Object> m3 = MapUtil.buildMap("name", "同学27");
		m3.put("sex", 27%2);
		List<Map<String, Object>> r3 = commonManager.query(sql3, m3);
		assertEquals(r3.size(), 1);
		assertEquals(MapUtil.getString(r3.get(0), "ID_"), "27");
		
		String sql4 = "delete from ex_student where id_=?";
		int r4 = commonManager.execute(sql4, 14);
		assertEquals(1,r4);
		
		String sql5 = "select * from ex_student where id_=?";
		List<Map<String, Object>> r5 = commonManager.query(sql5, 14);
		assertEquals(r5.size(), 0);
		
		String sql6 = "update ex_student set name_=#{name} where id_=#{id}";
		Map<String, Object> m6 = MapUtil.buildMap("name", "新的名字");
		m6.put("id", 17);
		int r6 = commonManager.execute(sql6, m6);
		assertEquals(1, r6);
		
		String sql7 = "select * from ex_student where id_=?";
		List<Map<String, Object>> r7 = commonManager.query(sql7, 17);
		assertEquals(r7.size(), 1);
		assertEquals(MapUtil.getString(r7.get(0), "NAME_"), "新的名字");
		
		try(MultiTenantIgnoreResult setThreadLocalIgnore = MultiTenantHandler.setThreadLocalIgnore()){
			List<Map<String, Object>> list = commonManager.query(sql);
			assertEquals(list.size(), 199);
		}

		PageList<Map<String, Object>> pageList = commonManager.query(sql, new PageBean(1, 20, true));
		assertEquals(199, pageList.getTotal());

		QueryFilter<?> queryFilter = QueryFilter.build()
				.withPage(new PageBean(1, 10))
				.withSorter(new FieldSort("ID_"))
				.withQuery(new QueryField("ID_", new String[]{"11", "12", "13"}, QueryOP.IN, FieldRelation.AND))
				.withQuery(new QueryField("NAME_", "同学12", QueryOP.EQUAL, FieldRelation.AND))
				.withQuery(new QueryField("SEX_", 12%2, QueryOP.EQUAL, FieldRelation.AND));

		PageList<Map<String, Object>> queryForListPage = commonManager.query(sql, queryFilter);
		assertEquals(1, queryForListPage.getTotal());

		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime yesterday = now.minusDays(1);
		LocalDateTime tomorrow = now.plusDays(1);
		String yesterdayStr = yesterday.format(df);
		String tomorrowStr = tomorrow.format(df);

		QueryFilter<?> queryFilter2 = QueryFilter.build()
				.withPage(new PageBean(1, 10))
				.withQuery(new QueryField("desc_", "小学一年级", QueryOP.LIKE))
				.withQuery(new QueryField("birthday_", new String[] {yesterdayStr, tomorrowStr}, QueryOP.BETWEEN));
		PageList<Map<String, Object>> queryForList = commonManager.query(sql, queryFilter2);
		assertEquals(199, queryForList.getTotal());
		assertEquals(10, queryForList.getRows().size());
	}
}
