package com.hotent.bpm.api.model.process.task;

import java.time.LocalDateTime;

import com.hotent.base.model.ExtraProp;

/**
 * 对象功能:流程审批意见 entity对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zyp
 * 创建时间:2014-03-21 17:07:23
 */
public interface BpmTaskOpinion extends ExtraProp{
	
	/**
	 * 主键ID
	 * @return  String
	 */
	String getId() ;
	
	/**
	 * 流程定义ID 
	 * @return  String
	 */
	String getProcDefId();
	
	/**
	 * 外部子流程父流程实例。 
	 * @return  String
	 */
	String getSupInstId();
	/**
	 * 当前流程实例ID。 
	 * @return  String
	 */
	String getProcInstId();
	
	/**
	 * 任务ID 
	 * @return  String
	 */
	String getTaskId() ;
	/**
	 * 任务节点
	 * @return  String
	 */
	String getTaskKey() ;
	/**
	 * 任务节点名称
	 * @return  String
	 */
	String getTaskName() ;
	
	/**
	 * 令牌
	 * @return  String
	 */
	String getToken() ;
	/**
	 * 有资格的审批人。 
	 * @return  String
	 */
	String getQualfieds(); 
	/**
	 * 有资格的审批人。 
	 * @return  String
	 */
	String getQualfiedNames(); 
	/**
	 * 审批人。 
	 * @return  String
	 */
	String getAuditor() ;
	/**
	 * 审批人。 
	 * @return  String
	 */
	String getAuditorName();
	/**
	 * 意见。 
	 * @return  String
	 */
	String getOpinion() ;
	/**
	 * 意见状态。 
	 * @return  String
	 */
	String getStatus() ;
	/**
	 * 表单ID。 
	 * @return  String
	 */
	String getFormDefId() ;
	/**
	 * 表单名称。 
	 * @return  String
	 */
	String getFormName();
	
	/**
	 * 创建时间。
	 * @return   LocalDateTime
	 */
	LocalDateTime getCreateTime();
	
	/**
	 * 分配时间。这个用任务产生后，没有分配人员，在分配时的事件。
	 * @return   LocalDateTime
	 */
	LocalDateTime getAssignTime() ;
	
	/**
	 * 任务完成时间。
	 * @return  LocalDateTime
	 */
	LocalDateTime getCompleteTime();
	/**
	 * 审批时长。
	 * @return Long
	 */
	Long getDurMs();

	/**
	 * 取得附件数据。
	 * @return  String
	 */
	String getFiles();
	
	
   String getAgentLeaderId();

   void setAgentLeaderId(String agentLeaderId) ;
   
   /**
    * 获取父任务id，只针对并行审批任务
    * @return
    */
   String getParentTaskId();
   
   /**
    * 获取signType
    * @return
    */
   String getSignType();
   
   /**
    * 获取审批跳过类型
    * @return
    */
   String getSkipType();
}