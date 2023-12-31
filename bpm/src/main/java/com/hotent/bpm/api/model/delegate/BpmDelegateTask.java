package com.hotent.bpm.api.model.delegate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.hotent.bpm.api.constant.MultiInstanceType;
import com.hotent.bpm.api.model.identity.BpmIdentity;
/**
 * <pre> 
 * 描述：流程代理任务
 * 构建组：x5-bpmx-native-api
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2013-12-17-下午6:36:07
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface BpmDelegateTask extends BpmVariable {

	/**
	 * 任务ID。 
	 * @return  String
	 */
	String getId();

	/**
	 * 任务名称。
	 * @return String
	 */
	String getName();

	/**
	 * 设置任务名称。
	 * @param name 
	 * void
	 */
	void setName(String name);
	
	/**
	 * 获取任务描述
	 * @return  String
	 */
	String getDescription();

	/**
	 * 设置任务描述。
	 * @param description 
	 * void
	 */
	void setDescription(String description);
	
	/**
	 * 返回代理对象。
	 * @return
	 */
	Object getProxyObj();
	
	/**
	 * 获取优先级。
	 * @return  int
	 */
	int getPriority();

	/**
	 * 设置优先级。
	 * @param priority 
	 * void
	 */
	void setPriority(int priority);

	/**
	 * 获取流程实例ID
	 * @return  String
	 */
	String getProcessInstanceId();

	/**
	 * 取得EXECUTIONID。
	 * @return  String
	 */
	String getExecutionId();

	/**
	 * 流程定义ID。
	 * @return  String
	 */
	String getBpmnDefId();

	/**
	 * 任务创建时间。
	 * @return Date
	 */
	LocalDateTime getCreateTime();

	/**
	 * 任务ID
	 * @return  String
	 */
	String getTaskDefinitionKey();

	/**
	 * 事件名称。
	 * @return  String
	 */
	String getEventName();
	
	/**
	 * 挂起状态。
	 * @return int
	 */
	int getSuspensionState();

	/**
	 * 获取到期时间。
	 * @return  Date
	 */
	LocalDateTime getDueDate();

	/**
	 * 设置任务到期时间。
	 * @param dueDate  void
	 */
	void setDueDate(LocalDateTime dueDate);
	
	/**
	 * 获取上级流程exeId
	 * @return  String
	 */
	String getSupperExecutionId();
	
	/**
	 * 获取外部流程变量。
	 * @return  Map&lt;String,Object>
	 */
	Map<String, Object> getSupperVars();
	
	/**
	 * 获取外部流程的变量。
	 * @param varName
	 * @return Object
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
	
	
	
	 /** The {@link User.getId() userId} of the person responsible for this task. */
	String getOwner();
	  
	  /** The {@link User.getId() userId} of the person responsible for this task.*/
	void setOwner(String owner);
	  
	  /** The {@link User.getId() userId} of the person to which this task is delegated. */
	String getAssignee();
	  
	  /** The {@link User.getId() userId} of the person to which this task is delegated. */
	void setAssignee(String assignee);
	
	/**
	 * 清除用户
	 * void
	 */
	void cleanExecutor();
	
	/**
	 * 添加执行人
	 * @param bpmIdentity 
	 * void
	 */
	void addExecutor(BpmIdentity  bpmIdentity);
	/**
	 * 添加执行人集合
	 * @param bpmIdentitys 
	 * void
	 */
	void addExecutors(List<BpmIdentity>  bpmIdentitys);

	/**
	 * 人员是否存在
	 * @param bpmIndentity
	 * @return  boolean
	 */
	boolean isExecutorExist(BpmIdentity  bpmIndentity);
	
	/**
	 * 获取执行人列表。
	 * @return  List&lt;BpmIdentity>
	 */
	List<BpmIdentity> getExecutors();
	
	/**
	 * 删除执行人。 
	 * @param bpmIndentity 
	 * void
	 */
	void delExecutor(BpmIdentity  bpmIndentity);

	/**
	 * 取得本地线程的变量。
	 * @param name
	 * @return  Object
	 */
	Object getExecutionLocalVariable(String name);
	
	/**
	 * 设置本地线程的变量。
	 * @param name
	 * @param obj 
	 * void
	 */
	void setExecutionLocalVariable(String name,Object obj);
	
	/**
	 * 判断是否在子流程内部
	 * @return
	 */
	boolean isInExtSubFlow();
	
	/**
	 * 获取上级的executeId。
	 * @return
	 */
	String getParentExecuteId();
	
	/**
	 * 获取上几级的父executeId。
	 * @param level
	 * @return
	 */
	String getParentExecuteId(int level);

	/**
	 * 判断task是否为空
	 * @return
	 */
	Boolean isNotEmpty();
	
	boolean isTaskEmpty();
	

}
