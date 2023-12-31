package com.hotent.bpm.natapi.inst;

import java.util.Collection;
import java.util.Map;

/**
 * 
 * <pre>
 * 描述：流程实例服务
 * 构建组：x5-bpmx-native-api
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2013-12-20-下午8:34:36
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface NatProInstanceService {
	/**
	 * 启动流程实例并返回流程实例ID
	 * 
	 * @param processDefinitionId	流程定义ID
	 * @param businessKey	流程业务主键ID
	 * @param variables		变量
	 * @return String
	 */
	String startProcessInstance(String processDefinitionId, String businessKey, Map<String, Object> variables);
	
	
	/**
	 * 启动流程并可以指定分支路径。
	 * @param processDefinitionId
	 * @param businessKey
	 * @param variables
	 * @param aryDestination
	 * @return  String
	 * @throws Exception 
	 */
	String startProcessInstance(String processDefinitionId, String businessKey, Map<String, Object> variables,String... aryDestination) throws Exception; 

	/**
	 * 取得流程实例对应的变量列表
	 * 
	 * @param bpmInstId
	 *            流程引擎实例ID
	 * @return Map<String,Object>
	 * @exception
	 * @since 1.0.0
	 */
	Map<String, Object> getVariables(String bpmInstId);

	/**
	 * 根据executionId设置流程变量。
	 * @param executionId
	 * @param variableName
	 * @param value 
	 * void
	 */
	void setVariable(String executionId, String variableName, Object value);

	/**
	 * 设置本地变量。
	 * @param executionId
	 * @param variableName
	 * @param value 
	 * void
	 */
	void setVariableLocal(String executionId, String variableName, Object value);

	void setVariables(String executionId, Map<String, ? extends Object> variables);

	void setVariablesLocal(String executionId, Map<String, ? extends Object> variables);

	void removeVariable(String executionId, String variableName);

	void removeVariableLocal(String executionId, String variableName);

	void removeVariables(String executionId, Collection<String> variableNames);

	void removeVariablesLocal(String executionId, Collection<String> variableNames);

	boolean hasVariableLocal(String executionId, String variableName);

	Object getVariableLocal(String executionId, String variableName);

	boolean hasVariable(String executionId, String variableName);

	Object getVariable(String executionId, String variableName);

	Map<String, Object> getVariablesLocal(String executionId, Collection<String> variableNames);

	Map<String, Object> getVariablesLocal(String executionId);

	Map<String, Object> getVariables(String executionId, Collection<String> variableNames);
	
	void endProcessInstance(String bpmnInstanceId);
	
	void activateProcessInstanceById(String bpmnInstanceId);
	
	void suspendProcessInstanceById(String bpmnInstanceId);
	
	/**
	 * 删除流程实例。
	 * @param bpmnInstId
	 * @param reason 
	 * void
	 */
	void deleteProcessInstance(String bpmnInstId,String reason);
	
	
	/**
	 * 根据流程实例ID和流程变量名称获取上级变量数据。
	 * @param bpmnId
	 * @param varName
	 * @return  Object
	 */
	Object  getSuperVariable(String bpmnId,String varName);
	
	
}
