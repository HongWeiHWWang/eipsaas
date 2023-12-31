package com.hotent.bpm.natapi.task;

import java.util.Collection;
import java.util.Map;

import com.hotent.bpm.api.model.delegate.BpmDelegateTask;

/**
 * 
 * <pre> 
 * 描述：Actviti任务获取实体服务
 * 构建组：x5-bpmx-native-api
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2014-3-18-上午8:53:05
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface NatTaskService {
	/**
	 * 获取任务实例对象
	 * @param taskId
	 * @return 
	 * BpmDelegateTask
	 */
	BpmDelegateTask getByTaskId(String taskId);
	
	/**
	 * 保存任务。
	 * @param task
	 */
	void save(BpmDelegateTask  task);
	
	
	/**
	 * 完成任务
	 * @param taskId
	 * void
	 */
	void completeTask(String taskId);
	
	
	/**
	 * 完成任务并跳至目标节点。
	 * @param actDefId
	 * @param currentNodeId
	 * @param destinationNodeId 
	 * void
	 */
	void completeTask(String taskId,String... destinationNodeId);
	
	
	/**
	 * 仅完成流程任务。
	 * <pre>
	 * 在完成任务时候，将目标节点之后的节点删除，那么流程将不会产生新的任务。
	 * </pre>
	 * @param taskId 
	 * void
	 */
	void completeTaskOnly(String taskId);
	
	
	/**
	 * 锁定流程任务。
	 * @param taskId
	 * @param userId 
	 * void
	 */
	void setAssignee(String taskId,String userId);
	
	
	/**
	 * 获取流程变量。
	 * @param varName
	 * @return 
	 * Object
	 */
	Object getVariable(String taskId, String variableName);
	
	/**
	 * 获取流程任务本地变量。
	 * @param taskId
	 * @param variableName
	 * @return Object
	 */
	Object getVariableLocal(String taskId, String variableName);
	
	/**
	 * 获取流程任务所有变量。
	 * @param taskId
	 * @param variableName
	 * @return Object
	 */
	Map<String,Object> getVariables(String taskId);
	
	/**
	 * 获取流程任务指定变量集合获取流程变量。
	 * @param taskId
	 * @param variableNames
	 * @return Object
	 */
	Map<String,Object> getVariables(String taskId, Collection<String> variableNames);
	
	/**
	 * 根据任务ID获取流程本地变量。
	 * @param taskId
	 * @return 
	 * Map&lt;String,Object>
	 */
	Map<String,Object> getVariablesLocal(String taskId);
	/**
	 * 根据任务ID，变量集合获取流程本地变量。
	 * @param taskId	变量ID
	 * @variableNames 	变量集合名称
	 * @return 
	 * Map&lt;String,Object>
	 */
	Map<String,Object> getVariablesLocal(String taskId, Collection<String> variableNames);
	
	/**
	 * 设置流程变量。
	 * @param taskId
	 * @param variableName
	 * @param value 
	 * void
	 */
	void setVariable(String taskId, String variableName, Object value);
	
	/**
	 * 设置任务本地变量。 
	 * @param taskId
	 * @param variableName
	 * @param value 
	 * void
	 */
	void setVariableLocal(String taskId, String variableName, Object value);
	
	
	/**
	 * 设置流程变量。
	 * @param taskId
	 * @param variables 
	 * void
	 */
	void setVariables(String taskId, Map<String,? extends Object> variables);
	
	/**
	 * 设置本地流程变量。 
	 * @param taskId
	 * @param variables 
	 * void
	 */
	void setVariablesLocal(String taskId, Map<String,? extends Object> variables);
	
	
}
