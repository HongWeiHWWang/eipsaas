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
import com.hotent.bpm.api.constant.DecideType;
import com.hotent.runtime.core.RunTimeTestCase;
import com.hotent.runtime.manager.BpmTaskTransRecordManager;
import com.hotent.runtime.model.BpmTaskTrans;
import com.hotent.runtime.model.BpmTaskTransRecord;

/**
 * BpmTaskTransRecordManager测试
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月27日
 */
public class BpmTaskTransRecordManagerTest extends RunTimeTestCase{
	@Resource
	BpmTaskTransRecordManager bpmTaskTransRecordManager;
	
	@Test
	public void testCurd(){
		String suid = idGenerator.getSuid();
		String instId = "10000000440044";
		BpmTaskTransRecord transRecord = new BpmTaskTransRecord();
		transRecord.setId(suid);
		transRecord.setCreator("系统管理员");
		transRecord.setTaskName("测试任务");
		transRecord.setTaskSubject("测试流程");
		transRecord.setStatus((short)0);//流转中
		transRecord.setTransOwner("1");//流转任务所属人
		transRecord.setTransTime(DateUtil.getCurrentDate());
		transRecord.setDefName("测试流程");
		transRecord.setProcInstId(instId);
		//复制流转任务中的一些数据
		transRecord.setAction(BpmTaskTrans.SIGN_ACTION_BACK);
		transRecord.setTaskId("10000000440033");
		transRecord.setDecideType(DecideType.AGREE.getKey());
		
		// 添加
		bpmTaskTransRecordManager.create(transRecord);
		// 查询
		BpmTaskTransRecord trans = bpmTaskTransRecordManager.get(suid);
		assertEquals(instId, trans.getProcInstId());

		String newInstId = "10000000440045";
		trans.setProcInstId(newInstId);
		// 更新
		bpmTaskTransRecordManager.update(trans);



		String suid2 = UniqueIdUtil.getSuid();
		String suid3 = UniqueIdUtil.getSuid();
		BpmTaskTransRecord bdr1 = trans;
		bdr1.setId(suid2);
		BpmTaskTransRecord bdr2 = trans;
		bdr2.setId(suid3);
		bpmTaskTransRecordManager.create(bdr1);
		bpmTaskTransRecordManager.create(bdr2);

		// 通过ID列表批量查询
		QueryFilter filter = QueryFilter.build()
				 .withQuery(new QueryField("id", Arrays.asList(new String[]{suid2,suid3}), QueryOP.IN));
		PageList<BpmTaskTransRecord> page = bpmTaskTransRecordManager.query(filter);
		List<BpmTaskTransRecord> byIds = page.getRows();
		assertEquals(2, byIds.size());

		QueryFilter queryFilter = QueryFilter.build()
											 .withDefaultPage() /*构建默认分页条件，否则不会做分页查询，则结构中也不会有total返回*/
											 .withQuery(new QueryField("procInstId", newInstId));
		// 通过通用查询条件查询
		PageList<BpmTaskTransRecord> query = bpmTaskTransRecordManager.query(queryFilter);
		assertEquals(1, query.getTotal());

		// 通过指定列查询唯一记录
		BpmTaskTransRecord bab = bpmTaskTransRecordManager.get(suid);
		assertEquals(suid, bab.getId());

		// 查询所有数据
		List<BpmTaskTransRecord> all = bpmTaskTransRecordManager.getAll();
		assertEquals(3, all.size());
		
		// 通过ID删除数据
		bpmTaskTransRecordManager.remove(suid);
		BpmTaskTransRecord sstd = bpmTaskTransRecordManager.get(suid);
		assertTrue(sstd==null);
		
		// 通过ID集合批量删除数据
		bpmTaskTransRecordManager.removeByIds(new String[]{suid, suid2, suid3});
		List<BpmTaskTransRecord> nowAll = bpmTaskTransRecordManager.getAll();
		assertEquals(0, nowAll.size());
	}
}
