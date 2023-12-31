package com.hotent.bpm.persistence.manager;

import java.util.List;

import com.hotent.base.manager.BaseManager;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.persistence.model.ActExecution;
import com.hotent.bpm.persistence.model.ActTask;

public interface ActTaskManager extends BaseManager<ActTask>{
	
	/**
	 * 添加用户任务。
	 * <pre>
	 * 实现方式
	 * 1.根据任务Id查询任务实例。
	 * 2.根据这个任务实例创建EXECUTION实例。
	 * 3.创建历史任务实例。
	 * 4.创建BPM_TASK数据实例。
	 * 5.持久化数据到数据库。
	 * </pre>
	 * @param bpmnTaskId	流程引擎的任务ID。		
	 * @param userId 
	 * void
	 */
	ActTask createTask(String bpmnTaskId,String userId);
	
	
	/**
	 * 根据ActExecution创建任务。
	 * @param task
	 * @return  ActTask
	 */
	ActTask createTask(ActExecution actExecution,BpmProcessInstance processInstance, BpmNodeDef bpmNodeDef);


	List<ActTask> getByInstId(String actProcInstanceId);
	
	
}
