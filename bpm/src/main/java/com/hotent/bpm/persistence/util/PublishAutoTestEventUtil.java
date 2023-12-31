package com.hotent.bpm.persistence.util;

import java.util.ArrayList;
import java.util.List;

import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.event.AutoTestEvent;
import com.hotent.bpm.api.model.process.task.BpmTask;
import com.hotent.bpm.engine.task.service.DefaultBpmTaskService;
import com.hotent.bpm.listener.AutoTestEventListener;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.persistence.model.AutoTestModel;
import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;
import com.hotent.uc.api.model.IUser;

/**
 * 
 * @author liygui
 * @date 2018-01-16
 *
 */
public class PublishAutoTestEventUtil {
	
	// 根据流程实例获取未处理的任务，发布事件模拟执行任务 instId 顶层流程实例id
	public static void publishAutoTestEvent(String instId){
		
		DefaultBpmTaskService bpmTaskService = (DefaultBpmTaskService) AppUtil.getBean("defaultBpmTaskService");
		BpmProcessInstanceManager bpmProcessInstanceManager= (BpmProcessInstanceManager) AppUtil.getBean("bpmProcessInstanceManager");
		
		List<DefaultBpmProcessInstance> instances = bpmProcessInstanceManager.getByParentId(instId, true);
		if(BeanUtils.isEmpty(instances)) return;
		
		List<BpmTask> tasks = new ArrayList<BpmTask>();
		for (DefaultBpmProcessInstance defaultBpmProcessInstance : instances) {
			if(BeanUtils.isEmpty(defaultBpmProcessInstance)) continue;
			tasks = bpmTaskService.getTasksInstId(defaultBpmProcessInstance.getId());
			if(BeanUtils.isNotEmpty(tasks)) break;
		}
		
		
		if(BeanUtils.isNotEmpty(tasks)){
			 BpmTask bpmTask = tasks.get(0);
			 AutoTestModel autoTestModel = new AutoTestModel();
			 autoTestModel.setTaskId(bpmTask.getTaskId());
			 autoTestModel.setNodeId(bpmTask.getNodeId());
			 autoTestModel.setNodeName(bpmTask.getName());
			 autoTestModel.setSubject(bpmTask.getSubject());
			 autoTestModel.setProcInstId(bpmTask.getProcInstId());
			 autoTestModel.setSkipDebugger((Boolean) ContextThreadUtil.getCommuVar("skipDebugger", false));
			 List<IUser> userList = bpmTaskService.getUsersByTaskId(bpmTask.getTaskId());
			 autoTestModel.setUserList(userList);
			 
			 AutoTestEvent event = new AutoTestEvent(autoTestModel);
			 //AppUtil.getBean(AutoTestEventListener.class);
			 AppUtil.publishEvent(event);
		}
		
	}

}
