package com.hotent.bpm.api.model.process.inst;

import java.time.LocalDateTime;



/**
 * 对象功能:流程实例 entity对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zyp
 * 创建时间:2014-03-07 15:35:54
 */
public interface BpmProcessInstance {


	/**
	 * 审批结果-未审批=uncheck
	 */
	public final static String RESULT_TYPE_INIT="uncheck";
	/**
	 * 审批结果-审批通过=pass
	 */
	public final static String RESULT_TYPE_PASS="pass";
	/**
	 * 审批结果-审批不通过=refuse
	 */
	public final static String RESULT_TYPE_REFUSE="refuse";
	
	/**
	 * 通过BO保存数据
	 */
	public final static String DATA_MODE_BO="bo";
	/**
	 * 从外部传入数据。
	 */
	public final static String DATA_MODE_EXTERNAL="external";
	/**
	 * 通过Handler计算。
	 */
	public final static String DATA_MODE_INNER="inner";
	
	/**
	 * 流程实例禁止
	 */
	public final static Integer FORBIDDEN_YES=1;
	
	/**
	 * 流程实例允许。
	 */
	public final static Integer FORBIDDEN_NO=0;
	
	/**
	 * 正式。
	 */
	public final static String FORMAL_YES="Y";
	
	/**
	 * 非正式
	 */
	public final static String FORMAL_NO="N";
	
	/**
	 * 返回 流程实例ID
	 * @return
	 */
	String getId() ;
	
	/**
	 * 返回 流程实例标题
	 * @return
	 */
	String getSubject() ;
	
	/**
	 * 返回 流程定义ID
	 * @return
	 */
	String getProcDefId() ;
	
	/**
	 * 返回 BPMN流程定义ID
	 * @return
	 */
	String getBpmnDefId() ;
	
	/**
	 * 返回 流程定义Key
	 * @return
	 */
	String getProcDefKey() ;
	
	/**
	 * 返回 流程名称
	 * @return
	 */
	String getProcDefName() ;
	
	/**
	 * 返回 关联数据业务主键
	 * @return
	 */
	String getBizKey() ;
	
	/**
	 * 返回业务系统编码
	 * @return
	 */
	String getSysCode();
	
	/**
	 * 返回 绑定的表单主键
	 * @return
	 */
	String getFormKey() ;
	
	
	
	/**
	 * 返回 实例状态
	 * @return
	 */
	String getStatus() ;
	
	/**
	 * 返回 实例结束时间
	 * @return
	 */
	LocalDateTime getEndTime() ;
	
	/**
	 * 返回 持续时间(ms)
	 * @return
	 */
	Long getDuration() ;
	
	/**
	 * 返回 所属分类ID
	 * @return
	 */
	String getTypeId() ;
	
	/**
	 * 返回 流程结束后的最终审批结果，agree=同意；refuse=拒绝
	 * @return
	 */
	String getResultType() ;
	
	/**
	 * 返回 BPMN流程实例ID
	 * @return
	 */
	String getBpmnInstId() ;
	
	/**
	 * 返回 创建人ID
	 * @return
	 */
	String getCreateBy() ;
	
	
	/**
	 * 创建人名字。
	 * @return String
	 */
	String getCreator();
	
	/**
	 * 返回 创建时间
	 * @return
	 */
	LocalDateTime getCreateTime() ;
	
	/**
	 * 返回 创建者所属组织ID
	 * @return
	 */
	String getCreateOrgId() ;
	
	/**
	 * 返回 更新人ID
	 * @return
	 */
	String getUpdateBy() ;
	
	/**
	 * 返回 更新时间
	 * @return
	 */
	LocalDateTime getUpdateTime() ;
	
	/**
	 * 返回 是否正式数据
	 * @return
	 */
	String getIsFormmal() ;
	
	/**
	 * 返回 父实例Id
	 * @return
	 */
	String getParentInstId() ;
	
	/**
	 * 是否禁用
	 * @return int
	 */
	int getIsForbidden();
	
	/**
	 * 数据保存模式。
	 * <pre>
	 * bo:bo实例
	 * external:外部传入
	 * inner:通过handler计算。
	 * </pre>
	 * @return String
	 */
	String getDataMode();
	
	/**
	 * 紧急状态
	 * @return
	 */
	String getUrgentState();
	
}