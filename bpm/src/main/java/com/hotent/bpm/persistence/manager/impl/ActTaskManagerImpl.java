package com.hotent.bpm.persistence.manager.impl;

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Resource;

import com.hotent.base.util.BeanUtils;
import io.micrometer.core.instrument.binder.BaseUnits;
import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bpm.api.constant.TaskType;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.persistence.dao.ActTaskDao;
import com.hotent.bpm.persistence.manager.ActExecutionManager;
import com.hotent.bpm.persistence.manager.ActHiTaskInstManager;
import com.hotent.bpm.persistence.manager.ActTaskManager;
import com.hotent.bpm.persistence.manager.BpmTaskManager;
import com.hotent.bpm.persistence.model.ActExecution;
import com.hotent.bpm.persistence.model.ActHiTaskInst;
import com.hotent.bpm.persistence.model.ActTask;
import com.hotent.bpm.persistence.model.DefaultBpmTask;
import org.springframework.transaction.annotation.Transactional;

@Service("actTaskManager")
public class ActTaskManagerImpl extends BaseManagerImpl<ActTaskDao, ActTask> implements ActTaskManager{
	
	@Resource
	ActExecutionManager actExecutionManager;
	
	@Resource
	ActHiTaskInstManager actHiTaskInstManager;
	@Resource
	BpmTaskManager bpmTaskManager ; 
	
	
	/**
	 * 添加用户任务。
	 * <pre>
	 * 实现方式
	 * 1.根据任务Id查询任务实例。
	 * 2.根据这个任务实例创建EXECUTION实例。
	 * 3.创建历史任务实例。
	 * 4.创建BPM_TASK数据实例。
	 * 5.创建BPM_TASK数据实例审批意见。
	 * 6.持久化数据到数据库。
	 * </pre>
	 * @param bpmnTaskId	流程引擎的任务ID。		
	 * @param userId 
	 * void
	 */
	@Override
    @Transactional
	public ActTask createTask(String taskId, String userId) {
		String newTaskId=UniqueIdUtil.getSuid();
		String executionId=UniqueIdUtil.getSuid();
		
		ActTask actTask=super.get(taskId);
		
		ActExecution  actExecution=actExecutionManager.get(actTask.getExecutionId());
		ActHiTaskInst actHiTaskInst=actHiTaskInstManager.get(taskId);
		DefaultBpmTask defaultBpmTask = bpmTaskManager.getByRelateTaskId(taskId);
		
		//任务
		ActTask newActTask=(ActTask)actTask.clone();
		newActTask.setId(newTaskId);
		newActTask.setExecutionId(executionId);
		newActTask.setAssignee(userId);
		newActTask.setOwner(userId);
		//execution
		ActExecution  newActExecution=(ActExecution)actExecution.clone();
		newActExecution.setId(executionId);
		//历史数据 todo 会签并行加签时会报错NULL
        ActHiTaskInst newActHiTask = null;
        if(BeanUtils.isNotEmpty(actHiTaskInst)){
            newActHiTask=(ActHiTaskInst)actHiTaskInst.clone();
            newActHiTask.setId(newTaskId);
            newActHiTask.setExecutionId(executionId);
            newActHiTask.setAssignee(userId);
            newActHiTask.setOwner(userId);
        }

		//DefaultBpmTask
		DefaultBpmTask newBpmTask=(DefaultBpmTask) defaultBpmTask.clone();
		newBpmTask.setId(newTaskId);
		newBpmTask.setTaskId(newTaskId);
		newBpmTask.setExecId(executionId);
		newBpmTask.setAssigneeId(userId);
		newBpmTask.setOwnerId(userId);
	
		
		//添加数据库表。
		actExecutionManager.create(newActExecution);
		super.create(newActTask);
        if(BeanUtils.isNotEmpty(newActHiTask)) {
            actHiTaskInstManager.create(newActHiTask);
        }
		bpmTaskManager.create(newBpmTask);
		
		return newActTask;
		
	}

	@Override
    @Transactional
	public ActTask createTask(ActExecution actExecution,BpmProcessInstance instance, BpmNodeDef bpmNodeDef) {
		//创建ACT_RU_TASK任务
		ActTask actTask=createActTask(actExecution, bpmNodeDef,instance);
		//历史任务
		createHiActTask(actTask);
		//创建BPM_TASK
		createBpmTask(actTask,instance);
		
		return actTask;
	}
	
	/**
	 * 创建ACT_RU_TASK记录。
	 * @param actExecution
	 * @param bpmNodeDef
	 * @param assigneeId
	 * @return  ActTask
	 */
	private ActTask createActTask(ActExecution actExecution,BpmNodeDef bpmNodeDef,BpmProcessInstance instance){
		//创建ACT_RU_TASK任务
		ActTask actTask=new ActTask();

		actTask.setId(UniqueIdUtil.getSuid());
		actTask.setRev(1);
		actTask.setExecutionId(actExecution.getId());
		actTask.setProcInstId(actExecution.getProcInstId());
		actTask.setProcDefId(actExecution.getProcDefId());
		actTask.setName(bpmNodeDef.getName());
		actTask.setTaskDefKey(bpmNodeDef.getNodeId());
		actTask.setOwner(instance.getCreateBy());
		actTask.setAssignee(instance.getCreateBy());
		actTask.setPriority(50);
		actTask.setCreateTime(LocalDateTime.now());
		actTask.setSuspensionState(1);
		
		super.create(actTask);
		
		
		return actTask;
	}

	/**
	 * 创建历史任务。
	 * @param actTask 
	 * void
	 */
	private void createHiActTask(ActTask actTask){

		ActHiTaskInst taskInst=new ActHiTaskInst();
		taskInst.setId(actTask.getId());
		taskInst.setProcDefId(actTask.getProcDefId());
		taskInst.setTaskDefKey(actTask.getTaskDefKey());
		taskInst.setProcInstId(actTask.getProcInstId());
		taskInst.setExecutionId(actTask.getExecutionId());
		taskInst.setName(actTask.getName());
		taskInst.setParentTaskId(actTask.getParentTaskId());
		taskInst.setOwner(actTask.getOwner());
		taskInst.setAssignee(actTask.getAssignee());
		taskInst.setStartTime(actTask.getCreateTime());
		taskInst.setPriority(actTask.getPriority());
		actHiTaskInstManager.create(taskInst);
	}
	
	/**
	 * 创建BPM_TASK记录。
	 * @param actTask
	 * @param instance 
	 * void
	 */
	private void createBpmTask(ActTask actTask,BpmProcessInstance instance){
		DefaultBpmTask bpmTask=new DefaultBpmTask();
		
		bpmTask.setId(actTask.getId());
		bpmTask.setName(actTask.getName());
		bpmTask.setSubject(instance.getSubject());
		bpmTask.setTaskId(actTask.getId());
		bpmTask.setExecId(actTask.getExecutionId());
		bpmTask.setNodeId(actTask.getTaskDefKey());
		bpmTask.setProcInstId(instance.getId());
		bpmTask.setProcDefId(instance.getProcDefId());
		bpmTask.setProcDefName(instance.getProcDefName());
		bpmTask.setOwnerId(actTask.getOwner());
		bpmTask.setAssigneeId(actTask.getAssignee());
		bpmTask.setStatus(TaskType.NORMAL.name());
		bpmTask.setPriority(50L);
		bpmTask.setCreateTime(actTask.getCreateTime());
		bpmTask.setSuspendState((short)0);
		bpmTask.setBpmnInstId(actTask.getProcInstId());
		bpmTask.setBpmnDefId(actTask.getProcDefId());
		bpmTask.setTypeId(instance.getTypeId());
		bpmTask.setProcDefKey(instance.getProcDefKey());
		bpmTaskManager.create(bpmTask);
		
	}

	@Override
	public List<ActTask> getByInstId(String actProcInstanceId) {
		return baseMapper.getByInstId(actProcInstanceId);
	}
	
}
