package com.hotent.activiti.inst.service.impl;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.ProcessEngineImpl;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;

import com.hotent.activiti.cmd.GetExecutionVariableCmd;
import com.hotent.activiti.cmd.GetSuperVariableCmd;
import com.hotent.activiti.def.BpmDefUtil;
import com.hotent.activiti.inst.cmd.ProcessInstanceEndCmd;
import com.hotent.activiti.util.ActivitiUtil;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.api.service.BpmDefinitionService;
import com.hotent.bpm.natapi.inst.NatProInstanceService;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;

@Service
public class ProInstanceServiceImpl implements NatProInstanceService{
	
	@Resource
	RuntimeService runtimeService;
	
	@Resource 
	ProcessEngine processEngine;
	
	@Resource
	BpmDefinitionAccessor bpmDefinitionAccessor;  
	
	@Resource
	BpmDefinitionService bpmDefinitionService ;
	@Resource
	RepositoryService repositoryService;
	

	
	
	@Override
	public String startProcessInstance(String processDefinitionId,
			String businessKey, Map<String, Object> variables,
			String... aryDestination) throws Exception {
		//获取流程定义ID
		String defId= bpmDefinitionService.getDefIdByBpmnDefId(processDefinitionId);
		//获取发起节点。
		BpmNodeDef bpmNodeDef= bpmDefinitionAccessor.getStartEvent(defId);
		String nodeId=bpmNodeDef.getNodeId();
		if(variables.containsKey(BpmConstants.START_DESTINATION) && (Boolean)variables.get(BpmConstants.START_DESTINATION) ){
			nodeId = bpmNodeDef.getOutcomeNodes().get(0).getNodeId();
		}
		//准备
		@SuppressWarnings("unused")
		Map<String,Object> activityMap= BpmDefUtil.prepare(processDefinitionId, nodeId, aryDestination);
		
		String bpmnInstId="";
		try{
			bpmnInstId= startProcessInstance(processDefinitionId,businessKey,variables);
		}
		catch(Exception ex){
			throw new RuntimeException(ex);
		}
		finally{
			//恢复
		//	BpmDefUtil.restore(activityMap);
		}
		return bpmnInstId;
		
	}
	
	
	
	@Override
	public String startProcessInstance(String processDefinitionId, String businessKey, Map<String, Object> variables) {
		try{
			IUser user= ContextUtil.getCurrentUser();
			Authentication.setAuthenticatedUserId(user.getUserId());
			
			ProcessInstance instance = runtimeService.startProcessInstanceById(processDefinitionId,businessKey, variables);
			return instance.getId();
		}
		catch(Exception ex){
			throw new RuntimeException(ex);
		}
		finally{
			Authentication.setAuthenticatedUserId(null);
			
		}
	}
	
	@Override
	public Map<String, Object> getVariables(String processInstanceId) {
		return runtimeService.getVariables(processInstanceId);
	}
	@Override
	public void setVariable(String executionId, String variableName,Object value) {
		 runtimeService.setVariable(executionId, variableName, value);
		
	}
	@Override
	public void setVariableLocal(String executionId, String variableName,Object value) {
		runtimeService.setVariableLocal(executionId, variableName, value);
	}
	@Override
	public void setVariables(String executionId,Map<String, ? extends Object> variables) {
		runtimeService.setVariables(executionId, variables);
	}
	@Override
	public void setVariablesLocal(String executionId,Map<String, ? extends Object> variables) {
		runtimeService.setVariablesLocal(executionId, variables);
	}
	@Override
	public void removeVariable(String executionId, String variableName) {
		runtimeService.removeVariable(executionId, variableName);
	}
	@Override
	public void removeVariableLocal(String executionId, String variableName) {
		runtimeService.removeVariableLocal(executionId, variableName);
		
	}
	@Override
	public void removeVariables(String executionId,Collection<String> variableNames) {
		runtimeService.removeVariables(executionId, variableNames);
	}
	@Override
	public void removeVariablesLocal(String executionId,Collection<String> variableNames) {
		runtimeService.removeVariablesLocal(executionId, variableNames);
	}
	@Override
	public boolean hasVariableLocal(String executionId, String variableName) {
		return runtimeService.hasVariableLocal(executionId, variableName);
	}
	@Override
	public Object getVariableLocal(String executionId, String variableName) {
//		try{
//			return runtimeService.getVariableLocal(executionId, variableName);
//		}
//		catch(Exception ex){
//			log.warn(ex.getMessage());
//			return null;
//		}
		
		CommandExecutor executor=ActivitiUtil.getCommandExecutor();
		GetExecutionVariableCmd cmd=new GetExecutionVariableCmd(executionId,variableName,true);
		return executor.execute(cmd);
	}
	
	@Override
	public boolean hasVariable(String executionId, String variableName) {
		return runtimeService.hasVariable(executionId, variableName);
	}
	@Override
	public Object getVariable(String executionId, String variableName) {
		return runtimeService.getVariable(executionId, variableName);
	}
	@Override
	public Map<String, Object> getVariablesLocal(String executionId,Collection<String> variableNames) {
		return runtimeService.getVariablesLocal(executionId);
	}
	@Override
	public Map<String, Object> getVariablesLocal(String executionId) {
		return runtimeService.getVariablesLocal(executionId);
	}
	@Override
	public Map<String, Object> getVariables(String executionId,Collection<String> variableNames) {
		return runtimeService.getVariables(executionId, variableNames);
	}

	@Override
	public void endProcessInstance(String bpmnInstanceId) {
		ProcessEngineImpl engine = (ProcessEngineImpl)processEngine;
		CommandExecutor cmdExecutor=engine.getProcessEngineConfiguration().getCommandExecutor();
		cmdExecutor.execute(new ProcessInstanceEndCmd(bpmnInstanceId));
	}
	
	@Override
	public void activateProcessInstanceById(String bpmnInstanceId) {
		runtimeService.activateProcessInstanceById(bpmnInstanceId);
	}
	
	@Override
	public void suspendProcessInstanceById(String bpmnInstanceId){
		runtimeService.suspendProcessInstanceById(bpmnInstanceId);
	}


	@Override
	public void deleteProcessInstance(String bpmnInstId, String reason) {
		runtimeService.deleteProcessInstance(bpmnInstId, reason);
	}

	@Override
	public Object getSuperVariable(String bpmnId, String varName) {
		CommandExecutor executor=ActivitiUtil.getCommandExecutor();
		GetSuperVariableCmd cmd=new GetSuperVariableCmd(bpmnId,varName);
		return executor.execute(cmd);
	}


	
	
	
}
