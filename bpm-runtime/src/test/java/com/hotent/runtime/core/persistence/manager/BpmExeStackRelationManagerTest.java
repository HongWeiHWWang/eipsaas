package com.hotent.runtime.core.persistence.manager;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryField;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bpm.persistence.manager.BpmExeStackRelationManager;
import com.hotent.bpm.persistence.model.BpmExeStackRelation;
import com.hotent.runtime.core.RunTimeTestCase;

/**
 * BpmExeStackRelationManager测试
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月27日
 */
public class BpmExeStackRelationManagerTest extends RunTimeTestCase{
	@Resource
	BpmExeStackRelationManager bpmExeStackRelationManager;
	
	@Test
	public void testCurd() throws JsonParseException, JsonMappingException, IOException{
		// 查询所有数据
		List<BpmExeStackRelation> allList = bpmExeStackRelationManager.getAll();
		
		String suid = "11610000000440072";
		String instId = "11610000000440044";
		BpmExeStackRelation bpmExeStackRelation = new BpmExeStackRelation();
		bpmExeStackRelation.setRelationId(suid);
		bpmExeStackRelation.setProcInstId(instId);
		bpmExeStackRelation.setFromStackId("11610000000440060");
		bpmExeStackRelation.setToStackId("11610000000440071");
		bpmExeStackRelation.setFromNodeId("UserTask1");
		bpmExeStackRelation.setFromNodeType("userTask");
		bpmExeStackRelation.setToNodeId("UserTask2");
		bpmExeStackRelation.setToNodeType("userTask");
		
		// 添加
		bpmExeStackRelationManager.create(bpmExeStackRelation);
		// 查询
		BpmExeStackRelation relation = bpmExeStackRelationManager.get(suid);
		assertEquals(instId, relation.getProcInstId());

		String newInstId = "11610000000440045";
		relation.setProcInstId(newInstId);
		// 更新
		bpmExeStackRelationManager.update(relation);



		String suid2 = "116"+UniqueIdUtil.getSuid();
		String suid3 = "116"+UniqueIdUtil.getSuid();
		BpmExeStackRelation bdr1 = JsonUtil.toBean(JsonUtil.toJsonNode(relation), BpmExeStackRelation.class);
		bdr1.setId(suid2);
		BpmExeStackRelation bdr2 = JsonUtil.toBean(JsonUtil.toJsonNode(relation), BpmExeStackRelation.class);
		bdr2.setId(suid3);
		bpmExeStackRelationManager.create(bdr1);
		bpmExeStackRelationManager.create(bdr2);

		// 通过ID列表批量查询
		QueryFilter filter = QueryFilter.<BpmExeStackRelation>build()
				 .withQuery(new QueryField("relationId", Arrays.asList(new String[]{suid2,suid3}), QueryOP.IN));
		PageList<BpmExeStackRelation> page = bpmExeStackRelationManager.query(filter);
		List<BpmExeStackRelation> byIds = page.getRows();
		assertEquals(2, byIds.size());

		QueryFilter queryFilter = QueryFilter.build()
											 .withDefaultPage() /*构建默认分页条件，否则不会做分页查询，则结构中也不会有total返回*/
											 .withQuery(new QueryField("procInstId", newInstId));
		// 通过通用查询条件查询
		PageList<BpmExeStackRelation> query = bpmExeStackRelationManager.query(queryFilter);
		assertEquals(3, query.getTotal());

		// 查询所有数据
		List<BpmExeStackRelation> all = bpmExeStackRelationManager.getAll();
		assertEquals(allList.size()+3, all.size());
		
		// 查询所有数据(分页)
//		PageList<BpmExeStackRelation> allByPage = bpmExeStackRelationManager.getAllByPage(new PageBean(1, 2));
//		assertEquals(3, allByPage.getTotal());
//		assertEquals(2, allByPage.getRows().size());
		
		// 通过ID删除数据
//		bpmExeStackRelationManager.remove(suid);
//		BpmExeStackRelation sstd = bpmExeStackRelationManager.getUnique("relationId", suid);
//		assertTrue(sstd==null);
		
		// 通过ID集合批量删除数据
		bpmExeStackRelationManager.removeByIds(new String[]{suid, suid2, suid3});
		List<BpmExeStackRelation> nowAll = bpmExeStackRelationManager.getAll();
		assertEquals(allList.size(), nowAll.size());
	}
}
