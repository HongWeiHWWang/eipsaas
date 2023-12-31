package com.hotent.bpmModel.core.persistence.manager;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.base.util.time.DateUtil;
import com.hotent.bpm.persistence.manager.BpmTaskReminderManager;
import com.hotent.bpm.persistence.model.BpmTaskReminder;
import com.hotent.bpmModel.core.BpmModelTestCase;

/**
 * 任务催办manager测试
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月25日
 */
public class BpmTaskReminderManagerTest extends BpmModelTestCase{
	@Resource
	BpmTaskReminderManager bpmTaskReminderManager;
	
	@Test
	public void testCurd(){
		String suid1 = UniqueIdUtil.getSuid();
		String taskId = "100001";
		BpmTaskReminder bpmTaskReminder = new BpmTaskReminder();
		bpmTaskReminder.setId(suid1);
		bpmTaskReminder.setTaskId(taskId);
		bpmTaskReminder.setIsSendMsg(1);;
		bpmTaskReminder.setRelDate(DateUtil.getCurrentDate());
		bpmTaskReminder.setDueAction(BpmTaskReminder.TASK_DUE_ACTION_NO_ACTION);
		bpmTaskReminder.setDueDate(DateUtil.getCurrentDate());
		bpmTaskReminder.setMsgSendDate(DateUtil.getCurrentDate());
		bpmTaskReminder.setMsgInterval(2);
		bpmTaskReminder.setMsgCount(1);
		bpmTaskReminder.setMsgType("inner");
		
		//添加任务催办
		bpmTaskReminderManager.create(bpmTaskReminder);
		
		//通过id获取任务催办
		BpmTaskReminder bpmTaskReminder01 = bpmTaskReminderManager.get(suid1);
		assertEquals(suid1, bpmTaskReminder01.getId());
		
		
		// 查询所有数据(分页)
		PageList<BpmTaskReminder> pageList = bpmTaskReminderManager.page(new PageBean(1, 10));
		assertEquals(1, pageList.getTotal());
		assertEquals(1, pageList.getRows().size());
		
		//根据任务id删除催办任务
	    bpmTaskReminderManager.deleteByTaskId(taskId);
		List<BpmTaskReminder> all = bpmTaskReminderManager.getAll();
		assertEquals(0, all.size());
		
		String suid2 = UniqueIdUtil.getSuid();
		BpmTaskReminder bpmTaskReminder2 = bpmTaskReminder;
		bpmTaskReminder2.setId(suid2);
		bpmTaskReminderManager.create(bpmTaskReminder2);
		String suid3 = UniqueIdUtil.getSuid();
		BpmTaskReminder bpmTaskReminder3 = bpmTaskReminder;
		bpmTaskReminder3.setId(suid3);
		bpmTaskReminderManager.create(bpmTaskReminder3);
		//获取需要触发的催办项
		List<BpmTaskReminder> list = bpmTaskReminderManager.getTriggerReminders();
		assertEquals(0, list.size());
		
		List<BpmTaskReminder> all3 = bpmTaskReminderManager.getAll();
		assertEquals(2, all3.size());
		//根据id删除
		bpmTaskReminderManager.remove(suid2);
		List<BpmTaskReminder> all2 = bpmTaskReminderManager.getAll();
		assertEquals(1, all2.size());
		//根据id 批量删除
		bpmTaskReminderManager.removeByIds(new String[]{suid2,suid3});
		
		List<BpmTaskReminder> all0 = bpmTaskReminderManager.getAll();
		assertEquals(0, all0.size());
	}
	
}
