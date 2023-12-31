package com.hotent.bpm.api.model.process.task;
import java.time.LocalDateTime;
import java.util.List;

import com.hotent.base.model.ExtraProp;
import com.hotent.bpm.api.constant.TaskType;
import com.hotent.bpm.api.model.identity.BpmIdentity;



/**
 * 对象功能:流程任务 entity对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zyp
 * 创建时间:2014-02-12 18:04:14
 */
public interface BpmTask extends ExtraProp  {

	
	/**
	 * 返回 任务ID
	 * @return
	 */
	public String getId() ;
	
	/**
	 * 返回 任务名称
	 * @return
	 */
	public String getName() ;
	
	/**
	 * 返回 待办事项标题
	 * @return
	 */
	public String getSubject() ;
	
	/**
	 * 返回 关联的任务ID
	 * @return
	 */
	public String getTaskId() ;
	
	/**
	 * 返回 关联 - 节点执行ID
	 * @return
	 */
	public String getExecId() ;
	
	/**
	 * 返回 关联 - 节点执行ID
	 * @return
	 */
	public void setExecId(String execId) ;
	
	/**
	 * 返回 关联 - 任务节点ID
	 * @return
	 */
	public String getNodeId() ;
	
	/**
	 * 返回 关联 - 流程实例ID
	 * @return
	 */
	public String getProcInstId() ;
	
	
	
	/**
	 * 返回 关联 - 流程定义ID
	 * @return
	 */
	public String getProcDefId() ;
	
	/**
	 * 返回 关联 - 流程业务主键
	 * @return
	 */
	public String getProcDefKey() ;
	
	/**
	 * 返回 关联 - 流程名称
	 * @return
	 */
	public String getProcDefName() ;
	
	/**
	 * 返回 任务所属人ID
	 * @return
	 */
	public String getOwnerId() ;
	
	public void setOwnerId(String ownerId);
	
	public void setOwnerName(String ownerName);
	
	public String getOwnerName();
	
	public void setAssigneeName(String assigneeName);
	
	/**
	 * 返回 任务执行人ID
	 * @return
	 */
	public String getAssigneeId();
	
	public void setAssigneeId(String assigneeId);
	
	/**
	 * 返回 任务状态。
	 * @see TaskType
	 * @return
	 */
	public String getStatus() ;
	
	public void setStatus(String status);
	
	/**
	 * 返回 任务优先级
	 * @return
	 */
	public Long getPriority() ;
	
	/**
	 * 返回 任务创建时间
	 * @return
	 */
	public LocalDateTime getCreateTime() ;
	
	/**
	 * 返回 任务到期时间
	 * @return
	 */
	public LocalDateTime getDueTime() ;
	
	/**
	 * 返回 是否挂起(0正常,1挂起)
	 * @return
	 */
	public Short getSuspendState() ;
	
	/**
	 * 
	 * 增加任务的父ID
	 * @return  String
	 * @exception 
	 * @since  1.0.0
	 */
	public String getParentId();
	
	/**
	 * bpmn流程实例ID。
	 * @return 
	 * String
	 */
	public String getBpmnInstId();
	
	/**
	 * 获取流程定义ID
	 * @return String
	 */
	public String getBpmnDefId();
	
	
	/**
	 * 获取执行人是否为空。
	 * @return  boolean
	 */
	boolean isIdentityEmpty();
	
	/**
	 * 取得任务执行者列表,这里只用于传递数据。
	 * @return 
	 * List&lt;BpmIdentity>
	 */
	List<BpmIdentity> getIdentityList();
	
	/**
	 * 设置跳转结果。
	 * @param skipResult 
	 * void
	 */
	void setSkipResult(SkipResult skipResult);
	
	/**
	 * 返回跳过任务结果。
	 * @return 
	 * SkipResult
	 */
	SkipResult getSkipResult();
	
	
	void setIsGateWay(boolean isGateway);
	
	boolean isGateWay();
	
	

	
}