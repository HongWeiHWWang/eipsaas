package com.hotent.bpm.api.model.delegate;

import java.util.List;
import java.util.Map;

import com.hotent.bpm.api.constant.MultiInstanceType;


/**
 * activiti 的DelegateExecution类的代理类。
 * <pre> 
 * 描述：activiti 的DelegateExecution类的代理类。
 * 构建组：x5-bpmx-native-api
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2013-12-17-下午6:37:29
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface BpmDelegateExecution extends BpmVariable {

	String getId();
	/**
	 * 获取Bpmn流程实例ID（即actInstId）
	 * @return  String
	 */
	String getBpmnInstId();

	String getEventName();

	String getProcessBusinessKey();

	String getBpmnDefId();

	String getParentId();
	
	/**
	 * 获取父线程。
	 * @return  String
	 */
	BpmDelegateExecution getParentExecution();

	/**
	 * 获取节点ID（即nodeId）
	 * @return  String
	 */
	String getNodeId();

	String getNodeName();
	
	String getExecutionEntityId();
	
	boolean isEnded();
	
	/**
	 * 获取上级流程exeId
	 * @return 
	 * String
	 */
	String getSupperExecutionId();
	
	/**
	 * 取得上级线程。 
	 * @return 
	 * BpmDelegateExecution
	 */
	BpmDelegateExecution getSupperExecution();
	
	/**
	 * 获取外部流程变量。
	 * @return 
	 * Map&lt;String,Object>
	 */
	Map<String, Object> getSupperVars();
	
	/**
	 * 获取外部流程的变量。
	 * @param varName
	 * @return 
	 */
	Object getSupperVariable(String varName);

	
	/**
	 * 获取流程是否为多实例。
	 * @return  boolean
	 */
	MultiInstanceType multiInstanceType();
	
	
	/**
	 * 获取外部流程是否为多实例。
	 * @return  boolean
	 */
	MultiInstanceType supperMultiInstanceType();
	
	/**
	 * 取得子的Execution。
	 * @return 
	 * List&lt;BpmDelegateExecution>
	 */
	List<BpmDelegateExecution> getExecutions();

}
