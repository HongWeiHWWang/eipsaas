package com.hotent.bpm.api.model.process.task;



/**
 * 对象功能:任务候选人 entity对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zyp
 * 创建时间:2014-02-12 18:04:14
 */
public interface BpmTaskCandidate {

	
	/**
	 * 返回 主键
	 * @return
	 */
	public String getId() ;
	
	/**
	 * 返回 任务ID
	 * @return
	 */
	public String getTaskId() ;
	
	/**
	 * 返回 候选人类型
	 * @return
	 */
	public String getType() ;
	
	/**
	 * 返回 执行人ID
	 * @return
	 */
	public String getExecutor() ;
	
	/**
	 * 返回 流程实例ID
	 * @return
	 */
	public String getProcInstId() ;
	
}