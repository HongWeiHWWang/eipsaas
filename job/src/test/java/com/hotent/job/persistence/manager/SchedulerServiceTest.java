package com.hotent.job.persistence.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;

import com.hotent.job.JobTestCase;
import com.hotent.job.model.SchedulerVo;


public class SchedulerServiceTest extends JobTestCase {

	@Resource
	SchedulerService ss;

	@Test
	public void testManage() throws Exception {
		String jobName = "test";
		String className = "com.hotent.job.MyJob";
		String json = null;
		String des = "1";
		// 添加任务
		boolean addJob = ss.addJob(jobName, className, json, des);
		assertTrue(addJob);

		// 添加触发器
		String tn = "触发器";
		String paramJson = "{\"type\":\"1\",\"timeInterval\":\"2018-06-14 05:10:15\"}";
		ss.addTrigger(jobName, tn, paramJson);

		// 通过触发器名称获得触发器
		Trigger t = ss.getTrigger(tn);
		assertEquals(tn, t.getKey().getName());

		// 获取任务列表
		List<SchedulerVo> list = ss.getJobList();
		assertEquals(1, list.size());

		// 根据任务名称获取触发器
		List<Trigger> triggersByJob = ss.getTriggersByJob(jobName);
		assertEquals(1, triggersByJob.size());

		//取得触发器的状态
		List<Trigger> list2 = new ArrayList<>();
		list2.add(t);
		HashMap<String, TriggerState> triggerStatus = ss.getTriggerStatus(list2);
		System.out.println(triggerStatus.get(t.getKey().getName()));

		//停止或暂停触发器
		ss.toggleTriggerRun(tn);
		List<Trigger> st = new ArrayList<>();
		st.add(t);
		HashMap<String, TriggerState> ts = ss.getTriggerStatus(list2);
		System.out.println(ts.get(t.getKey().getName()));

		//判断任务是否存在
		boolean jobExists = ss.isJobExists(jobName);
		assertTrue(jobExists);

		//判断触发器是否存在
		boolean c = ss.isTriggerExists(tn);
		assertTrue(c);

		//删除任务
		ss.delJob(jobName);
		List<SchedulerVo> jt = ss.getJobList();
		assertEquals(0, jt.size());

		//刪除触发器
		ss.delTrigger(tn);
		Trigger trigger = ss.getTrigger(tn);
		assertTrue(trigger==null);

		//启动
		ss.start();
		//是否启动
		boolean b = ss.isStarted();
		assertTrue(b);

		//是否挂起
		boolean mode = ss.isInStandbyMode();
		assertFalse(mode);

		//关闭
		ss.shutdown();
		boolean ms = ss.isStarted();
		assertTrue(ms);

	}
}
