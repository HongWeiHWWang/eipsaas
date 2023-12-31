package com.hotent.bpmModel.core.persistence.manager;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.base.util.time.DateUtil;
import com.hotent.bpm.persistence.manager.BpmReminderHistoryManager;
import com.hotent.bpm.persistence.model.BpmReminderHistory;
import com.hotent.bpmModel.core.BpmModelTestCase;

/**
 * 催办历史manager测试
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月25日
 */
public class BpmReminderHistoryManagerTest extends BpmModelTestCase{
	@Resource
	BpmReminderHistoryManager bpmReminderHistoryManager;
	
	@Test
	public void testCurd(){
		String suid1 = UniqueIdUtil.getSuid();
		BpmReminderHistory bpmReminderHistory = new BpmReminderHistory();
		bpmReminderHistory.setId(suid1);
		bpmReminderHistory.setUserId("1");
		bpmReminderHistory.setInstId("100001");
		bpmReminderHistory.setIsntName("测试");
		bpmReminderHistory.setNodeId("UserTask1");
		bpmReminderHistory.setNodeName("用户任务1");
		bpmReminderHistory.setExecuteDate(DateUtil.getCurrentDate());
		bpmReminderHistory.setRemindType("Warning");
		bpmReminderHistory.setNote("测试催办任务");
		
		//添加催办历史
		bpmReminderHistoryManager.create(bpmReminderHistory);
		
		//通过id获取催办历史
		BpmReminderHistory bpmReminderHistory01 = bpmReminderHistoryManager.get(suid1);
		assertEquals(suid1, bpmReminderHistory01.getId());
		
		
		// 查询所有数据(分页)
		PageList<BpmReminderHistory> pageList = bpmReminderHistoryManager.page(new PageBean(1, 10));
		assertEquals(1, pageList.getTotal());
		assertEquals(1, pageList.getRows().size());
		
		String suid2 = UniqueIdUtil.getSuid();
		BpmReminderHistory bpmReminderHistory2 = bpmReminderHistory;
		bpmReminderHistory2.setId(suid2);
		bpmReminderHistoryManager.create(bpmReminderHistory2);
		String suid3 = UniqueIdUtil.getSuid();
		BpmReminderHistory bpmReminderHistory3 = bpmReminderHistory;
		bpmReminderHistory3.setId(suid3);
		bpmReminderHistoryManager.create(bpmReminderHistory3);
		
		List<BpmReminderHistory> all = bpmReminderHistoryManager.getAll();
		assertEquals(3, all.size());
		//根据id删除
		bpmReminderHistoryManager.remove(suid1);
		
		List<BpmReminderHistory> all2 = bpmReminderHistoryManager.getAll();
		assertEquals(2, all2.size());
		//根据id 批量删除
		bpmReminderHistoryManager.removeByIds(new String[]{suid2,suid3});
		
		List<BpmReminderHistory> all0 = bpmReminderHistoryManager.getAll();
		assertEquals(0, all0.size());
	}
	
}
