package com.hotent.runtime.core.persistence.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryField;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.runtime.core.RunTimeTestCase;
import com.hotent.runtime.manager.BpmTestCaseManager;
import com.hotent.runtime.model.BpmTestCase;

/**
 * bpmTestCaseManager测试
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月27日
 */
public class BpmTestCaseManagerTest extends RunTimeTestCase{
	@Resource
	BpmTestCaseManager bpmTestCaseManager;
	
	@Test
	public void testCurd(){
		String suid = idGenerator.getSuid();
		BpmTestCase bpmTestCase = new BpmTestCase();
		bpmTestCase.setId(suid);
		bpmTestCase.setActionType("agree");
		bpmTestCase.setDefKey("cslc");
		bpmTestCase.setName("测试代码");
		bpmTestCase.setStartor("1");
		bpmTestCase.setStartorAccount("admin");
		bpmTestCase.setStartorFullName("系统管理员");
		
		// 添加
		bpmTestCaseManager.create(bpmTestCase);
		// 查询
		BpmTestCase testCase = bpmTestCaseManager.get(suid);
		assertEquals("cslc", testCase.getDefKey());

		String newDefKey = "cslc1";
		testCase.setDefKey(newDefKey);
		// 更新
		bpmTestCaseManager.update(testCase);



		String suid2 = UniqueIdUtil.getSuid();
		String suid3 = UniqueIdUtil.getSuid();
		BpmTestCase bdr1 = testCase;
		bdr1.setId(suid2);
		BpmTestCase bdr2 = testCase;
		bdr2.setId(suid3);
		bpmTestCaseManager.create(bdr1);
		bpmTestCaseManager.create(bdr2);

		// 通过ID列表批量查询
		QueryFilter filter = QueryFilter.build()
				 .withQuery(new QueryField("id", Arrays.asList(new String[]{suid2,suid3}), QueryOP.IN));
		PageList<BpmTestCase> page = bpmTestCaseManager.query(filter);
		List<BpmTestCase> byIds = page.getRows();
		assertEquals(2, byIds.size());

		QueryFilter queryFilter = QueryFilter.build()
											 .withDefaultPage() /*构建默认分页条件，否则不会做分页查询，则结构中也不会有total返回*/
											 .withQuery(new QueryField("defKey", newDefKey));
		// 通过通用查询条件查询
		PageList<BpmTestCase> query = bpmTestCaseManager.query(queryFilter);
		assertEquals(1, query.getTotal());

		// 通过指定列查询唯一记录
		BpmTestCase bab = bpmTestCaseManager.get(suid);
		assertEquals(suid, bab.getId());

		// 查询所有数据
		List<BpmTestCase> all = bpmTestCaseManager.getAll();
		assertEquals(3, all.size());
		
		// 通过ID删除数据
		bpmTestCaseManager.remove(suid);
		BpmTestCase sstd = bpmTestCaseManager.get(suid);
		assertTrue(sstd==null);
		
		// 通过ID集合批量删除数据
		bpmTestCaseManager.removeByIds(new String[]{suid, suid2, suid3});
		List<BpmTestCase> nowAll = bpmTestCaseManager.getAll();
		assertEquals(0, nowAll.size());
	}
}
