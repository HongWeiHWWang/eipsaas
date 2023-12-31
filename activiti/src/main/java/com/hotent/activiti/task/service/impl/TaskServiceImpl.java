package com.hotent.activiti.task.service.impl;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.stereotype.Service;

import com.hotent.activiti.cmd.SetAssigneeCmd;
import com.hotent.activiti.def.BpmDefUtil;
import com.hotent.activiti.ext.factory.BpmDelegateFactory;
import com.hotent.activiti.util.ActivitiUtil;
import com.hotent.bpm.api.model.delegate.BpmDelegateTask;
import com.hotent.bpm.natapi.task.NatTaskService;

@Service
public class TaskServiceImpl implements NatTaskService{
	@Resource
	TaskService taskService;
	
	@Resource
	RepositoryService repositoryService;
	
	@Override
	public BpmDelegateTask getByTaskId(String taskId) {
		TaskEntity task=(TaskEntity)taskService.createTaskQuery().taskId(taskId).singleResult();
		return BpmDelegateFactory.getBpmDelegateTask(task); 
	}
	
	@Override
	public void save(BpmDelegateTask  bpmDelegateTask) {
		TaskEntity task =(TaskEntity) bpmDelegateTask.getProxyObj();
		taskService.saveTask(task);
	}
	
	@Override
	public Object getVariable(String taskId, String variableName) {
		return taskService.getVariable(taskId, variableName);
	}
	@Override
	public Object getVariableLocal(String taskId, String variableName) {
		return taskService.getVariableLocal(taskId, variableName);
	}
	@Override
	public Map<String, Object> getVariables(String taskId) {
		return taskService.getVariables(taskId);
	}
	@Override
	public Map<String, Object> getVariables(String taskId,
			Collection<String> variableNames) {
		return taskService.getVariables(taskId,variableNames);
	}
	@Override
	public Map<String, Object> getVariablesLocal(String taskId) {
		
		return taskService.getVariablesLocal(taskId);
	}
	@Override
	public Map<String, Object> getVariablesLocal(String taskId,
			Collection<String> variableNames) {
		return taskService.getVariablesLocal(taskId,variableNames);
	}
	@Override
	public void completeTask(String taskId) {
		taskService.complete(taskId);
		
	}
	@Override
	public void setVariable(String taskId, String variableName, Object value) {
		taskService.setVariable(taskId, variableName, value);
	}
	@Override
	public void setVariableLocal(String taskId, String variableName,
			Object value) {
		taskService.setVariableLocal(taskId, variableName, value);
		
	}
	@Override
	public void setVariables(String taskId,
			Map<String, ? extends Object> variables) {
		taskService.setVariables(taskId, variables);
		
	}
	@Override
	public void setVariablesLocal(String taskId,
			Map<String, ? extends Object> variables) {
		taskService.setVariablesLocal(taskId, variables);
	}

	@Override
	public void completeTask(String taskId, String... aryDestination) {
		TaskEntity task=(TaskEntity)taskService.createTaskQuery().taskId(taskId).singleResult();
		
		String curNodeId=task.getTaskDefinitionKey();
		String actDefId=task.getProcessDefinitionId();
		
		Map<String,Object> activityMap= BpmDefUtil.prepare(actDefId, curNodeId, aryDestination);
		try{
			taskService.complete(taskId);
		}
		catch(Exception ex){
			throw new RuntimeException(ex);
		}
		finally{
			//恢复
			BpmDefUtil.restore(activityMap);
		}
	}

	@Override
	public void setAssignee(String taskId, String userId) {
		CommandExecutor commandExecutor=ActivitiUtil.getCommandExecutor();
		SetAssigneeCmd cmd=new SetAssigneeCmd(taskId, userId);
		commandExecutor.execute(cmd);
	}


	@Override
	public void completeTaskOnly(String taskId) {
		completeTask(taskId);
	}
}