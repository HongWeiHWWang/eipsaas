package com.hotent.bpm.api.model.process.inst;

import java.time.LocalDateTime;

/**
 * 对象功能:流程实例抄送 entity对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zyp
 * 创建时间:2014-04-29 17:35:40
 */
public interface BpmProCpto {

	
	/**
	 * 返回 主键
	 * @return
	 */
	String getId() ;
	
	/**
	 * 返回 流程实例ID
	 * @return
	 */
	String getInstId() ;
	
	/**
	 * 返回 ACT实例ID
	 * @return
	 */
	String getBpmnInstId() ;
	
	/**
	 * 返回 节点ID
	 * @return
	 */
	String getNodeId() ;	
	
	
	
	/**
	 * 返回 抄送时间
	 * @return
	 */
	LocalDateTime getCreateTime() ;
	
	/**
	 * 返回 意见
	 * @return
	 */
	String getOpinion() ;
	
	/**
	 * 返回 流程实例标题
	 * @return
	 */
	String getSubject() ;
	
	
	
	/**
	 * 返回 抄送类型(copyto抄送,trans转发)
	 * @return
	 */
	String getType() ;
	
	/**
	 * 返回 流程发起人
	 * @return
	 */
	String getStartorId() ;
	
	/**
	 * 发起人。
	 * @return String
	 */
	String getStartor() ;
		
	/**
	 * 返回 流程分类
	 * @return
	 */
	String getTypeId() ;
	
}