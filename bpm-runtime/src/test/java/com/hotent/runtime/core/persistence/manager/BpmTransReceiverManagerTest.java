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
import com.hotent.base.util.time.DateUtil;
import com.hotent.runtime.core.RunTimeTestCase;
import com.hotent.runtime.manager.BpmTransReceiverManager;
import com.hotent.runtime.model.BpmTransReceiver;

/**
 * bpmTransReceiverManager测试
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月27日
 */
public class BpmTransReceiverManagerTest extends RunTimeTestCase{
	@Resource
	BpmTransReceiverManager bpmTransReceiverManager;
	
	@Test
	public void testCurd(){
		String suid = idGenerator.getSuid();
		String recordid = "10000000440044";
		BpmTransReceiver bpmTransReceiver = new BpmTransReceiver();
		bpmTransReceiver.setId(suid);
		bpmTransReceiver.setCheckTime(DateUtil.getCurrentDate());
		bpmTransReceiver.setReceiver("admin");
		bpmTransReceiver.setReceiverId("1");
		bpmTransReceiver.setOpinion("流转回复意见");
		bpmTransReceiver.setTransRecordid(recordid);
		bpmTransReceiver.setStatus(Short.valueOf("1"));
		
		// 添加
		bpmTransReceiverManager.create(bpmTransReceiver);
		// 查询
		BpmTransReceiver transReceiver = bpmTransReceiverManager.get(suid);
		assertEquals(recordid, transReceiver.getTransRecordid());

		String newRecordid = "10000000440045";
		transReceiver.setTransRecordid(newRecordid);;
		// 更新
		bpmTransReceiverManager.update(transReceiver);



		String suid2 = UniqueIdUtil.getSuid();
		String suid3 = UniqueIdUtil.getSuid();
		BpmTransReceiver bdr1 = transReceiver;
		bdr1.setId(suid2);
		BpmTransReceiver bdr2 = transReceiver;
		bdr2.setId(suid3);
		bpmTransReceiverManager.create(bdr1);
		bpmTransReceiverManager.create(bdr2);

		// 通过ID列表批量查询
		QueryFilter filter = QueryFilter.build()
				 .withQuery(new QueryField("id", Arrays.asList(new String[]{suid2,suid3}), QueryOP.IN));
		PageList<BpmTransReceiver> page = bpmTransReceiverManager.query(filter);
		List<BpmTransReceiver> byIds = page.getRows();
		assertEquals(2, byIds.size());

		QueryFilter queryFilter = QueryFilter.build()
											 .withDefaultPage() /*构建默认分页条件，否则不会做分页查询，则结构中也不会有total返回*/
											 .withQuery(new QueryField("transRecordid", newRecordid));
		// 通过通用查询条件查询
		PageList<BpmTransReceiver> query = bpmTransReceiverManager.query(queryFilter);
		assertEquals(1, query.getTotal());

		// 通过指定列查询唯一记录
		BpmTransReceiver bab = bpmTransReceiverManager.get(suid);
		assertEquals(suid, bab.getId());

		// 查询所有数据
		List<BpmTransReceiver> all = bpmTransReceiverManager.getAll();
		assertEquals(3, all.size());
		
		// 通过ID删除数据
		bpmTransReceiverManager.remove(suid);
		BpmTransReceiver sstd = bpmTransReceiverManager.get(suid);
		assertTrue(sstd==null);
		
		// 通过ID集合批量删除数据
		bpmTransReceiverManager.removeByIds(new String[]{suid, suid2, suid3});
		List<BpmTransReceiver> nowAll = bpmTransReceiverManager.getAll();
		assertEquals(0, nowAll.size());
	}
}
