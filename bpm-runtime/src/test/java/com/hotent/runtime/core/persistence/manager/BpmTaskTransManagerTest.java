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
import com.hotent.bpm.api.constant.DecideType;
import com.hotent.runtime.core.RunTimeTestCase;
import com.hotent.runtime.manager.BpmTaskTransManager;
import com.hotent.runtime.model.BpmTaskTrans;

/**
 * bpmTaskTransManager测试
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月27日
 */
public class BpmTaskTransManagerTest extends RunTimeTestCase{
	@Resource
	BpmTaskTransManager bpmTaskTransManager;
	
	@Test
	public void testCurd(){
		String suid = idGenerator.getSuid();
		String instId = "10000000440044";
		BpmTaskTrans bpmTaskTrans = new BpmTaskTrans();
		bpmTaskTrans.setId(suid);
		bpmTaskTrans.setInstanceId(instId);
		bpmTaskTrans.setTaskId("10000000440033");
		bpmTaskTrans.setDecideType(DecideType.AGREE.getKey());
		
		// 添加
		bpmTaskTransManager.create(bpmTaskTrans);
		// 查询
		BpmTaskTrans trans = bpmTaskTransManager.get(suid);
		assertEquals(instId, trans.getInstanceId());

		String newInstId = "10000000440045";
		trans.setInstanceId(newInstId);
		// 更新
		bpmTaskTransManager.update(trans);



		String suid2 = UniqueIdUtil.getSuid();
		String suid3 = UniqueIdUtil.getSuid();
		BpmTaskTrans bdr1 = trans;
		bdr1.setId(suid2);
		BpmTaskTrans bdr2 = trans;
		bdr2.setId(suid3);
		bpmTaskTransManager.create(bdr1);
		bpmTaskTransManager.create(bdr2);

		// 通过ID列表批量查询
		QueryFilter filter = QueryFilter.build()
				 .withQuery(new QueryField("id", Arrays.asList(new String[]{suid2,suid3}), QueryOP.IN));
		PageList<BpmTaskTrans> page = bpmTaskTransManager.query(filter);
		List<BpmTaskTrans> byIds = page.getRows();
		assertEquals(2, byIds.size());

		QueryFilter queryFilter = QueryFilter.build()
											 .withDefaultPage() /*构建默认分页条件，否则不会做分页查询，则结构中也不会有total返回*/
											 .withQuery(new QueryField("instanceId", newInstId));
		// 通过通用查询条件查询
		PageList<BpmTaskTrans> query = bpmTaskTransManager.query(queryFilter);
		assertEquals(1, query.getTotal());

		// 查询所有数据
		List<BpmTaskTrans> all = bpmTaskTransManager.getAll();
		assertEquals(3, all.size());
		
		// 通过ID删除数据
		bpmTaskTransManager.remove(suid);
		BpmTaskTrans sstd = bpmTaskTransManager.get(suid);
		assertTrue(sstd==null);
		
		// 通过ID集合批量删除数据
		bpmTaskTransManager.removeByIds(new String[]{suid, suid2, suid3});
		List<BpmTaskTrans> nowAll = bpmTaskTransManager.getAll();
		assertEquals(0, nowAll.size());
	}
}
