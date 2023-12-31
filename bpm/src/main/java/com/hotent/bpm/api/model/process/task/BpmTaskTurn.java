package com.hotent.bpm.api.model.process.task;

import java.time.LocalDateTime;

/**
 * 对象功能:任务的执行更改 entity对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zyg
 * 创建时间:2014-05-04 16:06:19
 */
public interface BpmTaskTurn {
	
	/**
	 * 状态
	 */
	public static final String STATUS_RUNNING="running";
	public static final String STATUS_FINISH="finish";
	public static final String STATUS_CANCEL="cancel";
	
	/**
	 * 类型
	 */
	public static final String TYPE_AGENT="agent";
	public static final String TYPE_TURN="turn";

	
	/**
	 * 返回 更改ID
	 * @return
	 */
	String getId() ;
	
	/**
	 * 返回 任务ID
	 * @return
	 */
	String getTaskId() ;
	
	/**
	 * 返回 任务名称
	 * @return
	 */
	String getTaskName() ;
	
	/**
	 * 返回 待办事项标题
	 * @return
	 */
	String getTaskSubject() ;
	
	/**
	 * 返回 关联 - 任务节点ID
	 * @return
	 */
	String getNodeId() ;
	
	/**
	 * 返回 关联 - 流程实例ID
	 * @return
	 */
	String getProcInstId() ;
	
	/**
	 * 返回 任务所属人ID
	 * @return
	 */
	String getOwnerId() ;
	
	/**
	 * 返回 任务所属人姓名
	 * @return
	 */
	String getOwnerName() ;
	
	
	/**
	 * 返回 任务承接人ID
	 * @return
	 */
	String getAssigneeId() ;
	
	/**
	 * 返回 任务承接人姓名
	 * @return
	 */
	String getAssigneeName() ;
	
	/**
	 * 返回 状态。running 正在运行；finish 完成；cancel 取消。
	 * @return
	 */
	String getStatus() ;
	
	/**
	 * 返回 更改类型。agent 代理；turn 转办。
	 * @return
	 */
	String getTurnType() ;
	
	/**
	 * 返回 所属分类ID
	 * @return
	 */
	String getTypeId() ;
	
	/**
	 * 返回 创建时间
	 * @return
	 */
	LocalDateTime getCreateTime() ;
	
	/**
	 * 返回 任务完成时间
	 * @return
	 */
	LocalDateTime getFinishTime() ;
	
}