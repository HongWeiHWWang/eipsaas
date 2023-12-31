package com.hotent.activiti.ext.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.constant.MultiInstanceType;
import com.hotent.bpm.api.model.delegate.BpmDelegateTask;
import com.hotent.bpm.api.model.identity.BpmIdentity;

/**
 * 任务代理类，提供给bpm_core进行调用。
 * <pre> 
 * 描述：这个类是DELEGATETASK的代理类。
 * 构建组：x5-bpmx-activiti
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2013-12-18-上午10:38:59
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class BpmDelegateTaskImpl implements BpmDelegateTask {
	
	private List<BpmIdentity> list=new ArrayList<BpmIdentity>();
	
	private TaskEntity task=null;
	
	public void setDelegateTask(DelegateTask delegateTask){
		this.task=(TaskEntity)delegateTask;
	}
	
	
	
	

	@Override
	public Map<String, Object> getVariables() {
		
		return task.getVariables();
	}

	@Override
	public Map<String, Object> getVariablesLocal() {
		return task.getVariablesLocal();
	}

	@Override
	public Object getVariable(String variableName) {
		
		return task.getVariable(variableName);
	}

	@Override
	public Object getVariableLocal(String variableName) {
		
		return task.getVariableLocal(variableName);
	}

	@Override
	public Set<String> getVariableNames() {
		
		return task.getVariableNames();
	}

	@Override
	public Set<String> getVariableNamesLocal() {
		
		return task.getVariableNamesLocal();
	}

	@Override
	public void setVariable(String variableName, Object value) {
		task.setVariable(variableName, value);
		
	}

	@Override
	public Object setVariableLocal(String variableName, Object value) {
		return task.setVariableLocal(variableName, value);
	}

	@Override
	public void setVariables(Map<String, ? extends Object> variables) {
		task.setVariables(variables);
		
	}

	@Override
	public void setVariablesLocal(Map<String, ? extends Object> variables) {
		task.setVariablesLocal(variables);
		
	}

	@Override
	public boolean hasVariables() {
		return task.hasVariables();
	}

	@Override
	public boolean hasVariablesLocal() {
		return task.hasVariablesLocal();
	}

	@Override
	public boolean hasVariable(String variableName) {
		return task.hasVariable(variableName);
	}

	@Override
	public boolean hasVariableLocal(String variableName) {
		return task.hasVariableLocal(variableName);
	}

	@Override
	public void createVariableLocal(String variableName, Object value) {
		 task.createVariableLocal(variableName, value);
		
	}

	@Override
	public void removeVariable(String variableName) {
		task.removeVariable(variableName);
		
	}

	@Override
	public void removeVariableLocal(String variableName) {
		task.removeVariableLocal(variableName);
		
	}

	@Override
	public void removeVariables(Collection<String> variableNames) {
		task.removeVariables(variableNames);
		
	}

	@Override
	public void removeVariablesLocal(Collection<String> variableNames) {
		task.removeVariablesLocal(variableNames);
	}

	@Override
	public void removeVariables() {
		task.removeVariables();
		
	}

	@Override
	public void removeVariablesLocal() {
		task.removeVariablesLocal();
		
	}
	

	

	@Override
	public String getId() {
		
		return task.getId();
	}

	@Override
	public String getName() {
		return task.getName();
		
	}

	@Override
	public void setName(String name) {
		task.setName(name);
	}

	@Override
	public String getDescription() {
		return task.getDescription();
	}

	@Override
	public void setDescription(String description) {
		task.setDescription(description);
		
	}

	@Override
	public int getPriority() {
		return task.getPriority();
	}

	@Override
	public void setPriority(int priority) {
		task.setPriority(priority);
	}

	@Override
	public String getProcessInstanceId() {
		return this.task.getProcessInstanceId();
	}

	@Override
	public String getExecutionId() {
		
		return task.getExecutionId();
	}

	@Override
	public String getBpmnDefId() {
		
		return task.getProcessDefinitionId();
	}

	@Override
	public LocalDateTime getCreateTime() {
		if(BeanUtils.isNotEmpty(task.getCreateTime())){
			Instant instant = task.getCreateTime().toInstant();
		    ZoneId zoneId = ZoneId.systemDefault();
		    return instant.atZone(zoneId).toLocalDateTime();
		}else{
			return null;
		}
	}

	@Override
	public String getTaskDefinitionKey() {
		return task.getTaskDefinitionKey();
	}

	@Override
	public String getEventName() {
		return task.getEventName();
	}	
	
	//挂起状态。
	@Override
	public int getSuspensionState(){
		return task.getSuspensionState();
	}
	

	

	@Override
	public LocalDateTime getDueDate() {
		if(BeanUtils.isNotEmpty(task.getDueDate())){
			Instant instant = task.getDueDate().toInstant();
		    ZoneId zoneId = ZoneId.systemDefault();
		    return instant.atZone(zoneId).toLocalDateTime();
		}else{
			return null;
		}
	}

	@Override
	public void setDueDate(LocalDateTime dueDate) {
		if(BeanUtils.isNotEmpty(dueDate)){
			 ZoneId zoneId = ZoneId.systemDefault();
		     ZonedDateTime zdt = dueDate.atZone(zoneId);
		     task.setDueDate(Date.from(zdt.toInstant()));
		}else{
			task.setDueDate(null);
		}
	}
	
	@Override
	public String getOwner() {
		return task.getOwner();
	}

	@Override
	public void setOwner(String owner) {
		task.setOwner(owner);
		
	}

	@Override
	public String getAssignee() {
		return task.getAssignee();
	}

	@Override
	public void setAssignee(String assignee) {
		task.setAssignee(assignee);
		
	}
	

	@Override
	public void cleanExecutor() {
		list.clear();
		
	}

	@Override
	public void addExecutor(BpmIdentity bpmIdentity) {
		boolean isExist=isExecutorExist(bpmIdentity);
		if(isExist) return ;
		list.add(bpmIdentity);
		
	}

	@Override
	public void addExecutors(List<BpmIdentity> bpmIdentitys) {
		for(BpmIdentity bpmIdentity:bpmIdentitys){
			addExecutor(bpmIdentity);
		}		
	}

	@Override
	public boolean isExecutorExist(BpmIdentity bpmIndentity) {
		for(BpmIdentity obj:list){
			if(obj.equals(bpmIndentity)){
				return true;
			}
		}
		return false;
	}

	@Override
	public List<BpmIdentity> getExecutors() {
		return this.list;
	}

	@Override
	public void delExecutor(BpmIdentity bpmIndentity) {
		list.remove(bpmIndentity);
		
	}

	@Override
	public String getSupperExecutionId() {
		
		return task.getExecution().getSuperExecutionId();
	}

	@Override
	public Map<String, Object> getSupperVars() {
		if(task.getExecution().getSuperExecution()!=null){
			return task.getExecution().getSuperExecution().getVariables();
		}
		return null;
	}

	@Override
	public Object getSupperVariable(String varName) {
		ExecutionEntity ent= task.getExecution().getSuperExecution();
		if(ent!=null){
			return ent.getVariable(varName);
		}
		return null;
	}

	@Override
	public MultiInstanceType supperMultiInstanceType() {
		if(task.getExecution().getSuperExecution()!=null){
			String multiInstance = (String) task.getExecution().getSuperExecution()
					.getActivity().getProperty(BpmConstants.MULTI_INSTANCE);
			if(StringUtil.isEmpty(multiInstance)){
				return MultiInstanceType.NO;
			}
			return MultiInstanceType.fromKey(multiInstance);
		}
		return MultiInstanceType.NO;
	}

	@Override
	public MultiInstanceType multiInstanceType() {
		String multiInstance = (String) task.getExecution().getActivity().getProperty(BpmConstants.MULTI_INSTANCE);
		if(StringUtil.isEmpty(multiInstance)){
			return MultiInstanceType.NO;
		}
		return MultiInstanceType.fromKey(multiInstance);
	}

	@Override
	public Object getExecutionLocalVariable(String name) {
		return task.getExecution().getVariableLocal(name);
	}

	@Override
	public void setExecutionLocalVariable(String name, Object obj) {
		task.getExecution().setVariableLocal(name, obj);
	}


	@Override
	public Object getProxyObj() {
		return this.task;
	}





	public boolean isInExtSubFlow() {
		if(task.getExecution().getSuperExecution()!=null){
			return true;
		}
		return false;
	}





	@Override
	public String getParentExecuteId() {
		return  task.getExecution().getParentId();
	}





	@Override
	public String getParentExecuteId(int level) {
		int i=0;
		ExecutionEntity ent= task.getExecution();
		while(i<level){
			ent=ent.getParent();
			if(ent==null){
				throw new RuntimeException("指定级别的父节点找不到");
			}
			i++;
		}
		return ent.getId();
	}





	@Override
	public Boolean isNotEmpty() {
		return BeanUtils.isNotEmpty(task);
	}
	@Override
	public boolean isTaskEmpty() {
		return BeanUtils.isEmpty(task);
	}

	
	
}
