package com.hotent.activiti.ext.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.constant.MultiInstanceType;
import com.hotent.bpm.api.model.delegate.BpmDelegateExecution;

/**
 * 流程执行实例的代理实例，代理DelegateExecution这个对象。
 * <pre> 
 * 描述：这个提供给bpm_core调用。
 * 构建组：x5-bpmx-activiti
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2013-12-18-上午10:41:16
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class BpmDelegateExecutionImpl implements BpmDelegateExecution {
	
	private DelegateExecution delegateExecution=null;	
	
	public void setDelegateExecution(DelegateExecution delegateExecution){
		this.delegateExecution=delegateExecution;
	}

	@Override
	public Map<String, Object> getVariables() {
		
		return delegateExecution.getVariables();
	}

	@Override
	public Map<String, Object> getVariablesLocal() {
		return delegateExecution.getVariablesLocal();
	}

	@Override
	public Object getVariable(String variableName) {
		return delegateExecution.getVariable(variableName);
	}

	@Override
	public Object getVariableLocal(String variableName) {
		return delegateExecution.getVariableLocal(variableName);
	}

	@Override
	public Set<String> getVariableNames() {
		return delegateExecution.getVariableNames();
	}

	@Override
	public Set<String> getVariableNamesLocal() {
		return delegateExecution.getVariableNamesLocal();
	}

	@Override
	public void setVariable(String variableName, Object value) {
		delegateExecution.setVariable(variableName, value);
	}

	@Override
	public Object setVariableLocal(String variableName, Object value) {
		return delegateExecution.setVariableLocal(variableName, value);
	}

	@Override
	public void setVariables(Map<String, ? extends Object> variables) {
		delegateExecution.setVariables(variables);

	}

	@Override
	public void setVariablesLocal(Map<String, ? extends Object> variables) {
		delegateExecution.setVariablesLocal(variables);

	}

	@Override
	public boolean hasVariables() {
		return delegateExecution.hasVariables();
	}

	@Override
	public boolean hasVariablesLocal() {
		return delegateExecution.hasVariablesLocal();
	}

	@Override
	public boolean hasVariable(String variableName) {
		return delegateExecution.hasVariable(variableName);
	}

	@Override
	public boolean hasVariableLocal(String variableName) {
		return delegateExecution.hasVariableLocal(variableName);
	}

	@Override
	public void createVariableLocal(String variableName, Object value) {
		delegateExecution.createVariableLocal(variableName, value) ;

	}

	@Override
	public void removeVariable(String variableName) {
		delegateExecution.removeVariable(variableName);

	}

	@Override
	public void removeVariableLocal(String variableName) {
		delegateExecution.removeVariableLocal(variableName);

	}

	@Override
	public void removeVariables(Collection<String> variableNames) {
		delegateExecution.removeVariables(variableNames);

	}

	@Override
	public void removeVariablesLocal(Collection<String> variableNames) {
		delegateExecution.removeVariablesLocal(variableNames);

	}

	@Override
	public void removeVariables() {
		delegateExecution.removeVariables();

	}

	@Override
	public void removeVariablesLocal() {
		delegateExecution.removeVariablesLocal();

	}

	@Override
	public String getId() {
		return delegateExecution.getId();
	}

	@Override
	public String getBpmnInstId() {
		return delegateExecution.getProcessInstanceId();
	}

	@Override
	public String getEventName() {
		return delegateExecution.getEventName();
	}

	@Override
	public String getProcessBusinessKey() {
		return delegateExecution.getProcessBusinessKey();
	}

	@Override
	public String getBpmnDefId() {
		return delegateExecution.getProcessDefinitionId();
	}

	@Override
	public String getParentId() {
		return delegateExecution.getParentId();
	}

	@Override
	public String getNodeId() {
		return delegateExecution.getCurrentActivityId();
	}

	@Override
	public String getNodeName() {
		return delegateExecution.getCurrentActivityName();
	}

	@Override
	public String getExecutionEntityId() {
		ExecutionEntity ent=(ExecutionEntity)delegateExecution;
		return ent.getId();
	}

	@Override
	public boolean isEnded() {
		ExecutionEntity ent=(ExecutionEntity)delegateExecution;
		return ent.isEnded();
	}

	@Override
	public String getSupperExecutionId() {
		ExecutionEntity ent=(ExecutionEntity)delegateExecution;
		
		return ent.getSuperExecutionId();
	}

	@Override
	public Map<String, Object> getSupperVars() {
		ExecutionEntity ent=(ExecutionEntity)delegateExecution;
		if(ent.getSuperExecution()!=null){
			return ent.getSuperExecution().getVariables();
		}
		return null;
	}

	@Override
	public Object getSupperVariable(String varName) {
		ExecutionEntity ent=(ExecutionEntity)delegateExecution;
		if(ent.getSuperExecution()!=null){
			return ent.getSuperExecution().getVariable(varName);
		}
		return null;
	}

	@Override
	public MultiInstanceType supperMultiInstanceType() {
		ExecutionEntity ent=(ExecutionEntity)delegateExecution;
		
		ExecutionEntity superEnt=ent.getSuperExecution();
		if(superEnt==null) return MultiInstanceType.NO;
		
		if(superEnt.getActivity()==null) return MultiInstanceType.NO;
	
		String multiInstance = (String) ent.getSuperExecution()
				.getActivity().getProperty(BpmConstants.MULTI_INSTANCE);
		if(StringUtil.isEmpty(multiInstance)){
			return MultiInstanceType.NO;
		}
		return MultiInstanceType.fromKey(multiInstance);
		
	}

	@Override
	public MultiInstanceType multiInstanceType() {
		ExecutionEntity ent=(ExecutionEntity)delegateExecution;
		if(ent.getActivity()==null) return MultiInstanceType.NO;
		String multiInstance = (String) ent.getActivity().getProperty(BpmConstants.MULTI_INSTANCE);
		if(StringUtil.isEmpty(multiInstance)){
			return MultiInstanceType.NO;
		}
		return MultiInstanceType.fromKey(multiInstance);
	}

	@Override
	public BpmDelegateExecution getParentExecution() {
		ExecutionEntity ent=(ExecutionEntity)delegateExecution;
		ExecutionEntity parentEnt= ent.getParent();
		if(parentEnt==null) return null;
		BpmDelegateExecutionImpl executionImpl=new BpmDelegateExecutionImpl();
		executionImpl.setDelegateExecution(parentEnt);
		return (BpmDelegateExecution) executionImpl;
	}

	@Override
	public BpmDelegateExecution getSupperExecution() {
		ExecutionEntity ent=(ExecutionEntity)delegateExecution;
		ExecutionEntity superEnt= ent.getSuperExecution();
		if(superEnt==null) return null;
		BpmDelegateExecutionImpl executionImpl=new BpmDelegateExecutionImpl();
		executionImpl.setDelegateExecution(superEnt);
		return (BpmDelegateExecution) executionImpl;
	}

	@Override
	public List<BpmDelegateExecution> getExecutions() {
		ExecutionEntity ent=(ExecutionEntity)delegateExecution;
		List<ExecutionEntity> list= ent.getExecutions();
		if(list==null) return null;
		List<BpmDelegateExecution> rtnList=new ArrayList<BpmDelegateExecution>();
		for(ExecutionEntity entity:list){
			BpmDelegateExecutionImpl executionImpl=new BpmDelegateExecutionImpl();
			executionImpl.setDelegateExecution(entity);
			rtnList.add(executionImpl);
		}
		return rtnList;
	}



}
