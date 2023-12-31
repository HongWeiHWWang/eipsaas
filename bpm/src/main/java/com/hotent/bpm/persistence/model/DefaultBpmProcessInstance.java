package com.hotent.bpm.persistence.model;
import java.time.LocalDateTime;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.JsonNode;
import com.hotent.base.entity.BaseModel;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;



/**
 * 对象功能:流程实例 entity对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zyp
 * 创建时间:2014-03-07 15:35:54
 */
@TableName("bpm_pro_inst")
public class DefaultBpmProcessInstance extends BaseModel<DefaultBpmProcessInstance> implements BpmProcessInstance,Cloneable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7883744782862256060L;
	/*流程实例ID*/
	@TableId("id_")
	protected String  id; 
	/*流程实例标题*/
	@TableField("subject_")
	protected String  subject; 
	/*流程定义ID*/
	@TableField("proc_def_id_")
	protected String  procDefId; 
	/*BPMN流程定义ID*/
	@TableField("bpmn_def_id_")
	protected String  bpmnDefId; 
	/*流程定义Key*/
	@TableField("proc_def_key_")
	protected String  procDefKey; 
	@TableField("proc_def_name_")
	protected String  procDefName; /*流程名称*/
	@TableField("biz_key_")
	protected String  bizKey; /*关联数据业务主键*/
	@TableField("sys_code_")
	protected String  sysCode; /*关联数据系统编码*/
	@TableField("form_key_")
	protected String  formKey; /*绑定的表单主键*/
	//实例状态
	@TableField("status_")
	protected String  status; 
	@TableField("end_time_")
	protected LocalDateTime  endTime; /*实例结束时间*/
	@TableField("duration_")
	protected Long  duration; /*持续时间(ms)*/
	@TableField("type_id_")
	protected String  typeId; /*所属分类ID*/
	@TableField("result_type_")
	protected String  resultType; /*流程结束后的最终审批结果，agree=同意；refuse=拒绝*/
	@TableField("bpmn_inst_id_")
	protected String  bpmnInstId; /*BPMN流程实例ID*/
	@TableField("is_formmal_")
	protected String  isFormmal=FORMAL_YES; /*是否正式数据*/
	@TableField("parent_inst_id_")
	protected String  parentInstId="0"; /*父实例Id*/
	@TableField("super_node_id_")
	protected  String superNodeId;/*父流程节点定义ID*/
	@TableField("urgent_state_")
	protected  String urgentState;/*紧急状态*/
	@TableField("creator_")
	protected  String creator;
	@TableField("create_by_")
	protected String  createBy; /*创建人ID*/
	@TableField("create_time_")
	protected LocalDateTime  createTime; /*创建时间*/
	@TableField("create_org_id_")
	protected String  createOrgId; /*创建者所属组织ID*/
	@TableField("update_by_")
	protected String  updateBy; /*更新人ID*/
	@TableField("update_time_")
	protected LocalDateTime  updateTime; /*更新时间*/
	//是否禁止
	@TableField("is_forbidden_")
	protected int isForbidden=0;


	@TableField(exist=false)
	protected String  createOrgPath; /*创建者所属组织全路径*/
    //是否物理删除（0：否，1：是）
	@TableField("is_dele_")
    protected int isDele=0;
	//支持手机表单。
	@TableField("support_mobile_")
	protected int supportMobile=0;
	//业务数据存储模式。
	@TableField("data_mode_")
	protected String dataMode="";
	//流程分管授权权限对象
	@TableField(exist=false)
  	protected JsonNode authorizeRight; 
  	//任务名称，做查询使用。
	@TableField(exist=false)
  	protected String taskName="";
	//是否为驳回状态下驳回到发起人
	@TableField(exist=false)
    protected Boolean isBackToStart = false;
	
	//是否显示表单修改记录
	@TableField(exist=false)
	protected boolean showModifyRecord = false;
	
	//表单formKey
	@TableField(exist=false)
	protected String bpmFormKey;

	public void setId(String id)
	{
		this.id = id;
	}
	/**
	 * 返回 流程实例ID
	 * @return
	 */
	public String getId() 
	{
		return this.id;
	}
	public void setSuperNodeId(String superNodeId) 
	{
		this.superNodeId = superNodeId;
	}
 
	public String getSuperNodeId() 
	{
		return this.superNodeId;
	}
	
	public void setSubject(String subject) 
	{
		this.subject = subject;
	}
	/**
	 * 返回 流程实例标题
	 * @return
	 */
	public String getSubject() 
	{
		return this.subject;
	}
	public void setProcDefId(String procDefId) 
	{
		this.procDefId = procDefId;
	}
	/**
	 * 返回 流程定义ID
	 * @return
	 */
	public String getProcDefId() 
	{
		return this.procDefId;
	}
	public void setBpmnDefId(String bpmnDefId) 
	{
		this.bpmnDefId = bpmnDefId;
	}
	/**
	 * 返回 BPMN流程定义ID
	 * @return
	 */
	public String getBpmnDefId() 
	{
		return this.bpmnDefId;
	}
	public void setProcDefKey(String procDefKey) 
	{
		this.procDefKey = procDefKey;
	}
	/**
	 * 返回 流程定义Key
	 * @return
	 */
	public String getProcDefKey() 
	{
		return this.procDefKey;
	}
	public void setProcDefName(String procDefName) 
	{
		this.procDefName = procDefName;
	}
	/**
	 * 返回 流程名称
	 * @return
	 */
	public String getProcDefName() 
	{
		return this.procDefName;
	}
	public void setBizKey(String bizKey) 
	{
		this.bizKey = bizKey;
	}
	/**
	 * 返回 关联数据业务主键
	 * @return
	 */
	public String getBizKey() 
	{
		return this.bizKey;
	}
	
	/**
	 * 返回业务系统编码
	 * @return
	 */
	public String getSysCode() {
		return sysCode;
	}
	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}
	
	public void setFormKey(String formKey) 
	{
		this.formKey = formKey;
	}
	/**
	 * 返回 绑定的表单主键
	 * @return
	 */
	public String getFormKey() 
	{
		return this.formKey;
	}
	
	public void setStatus(String status) 
	{
		this.status = status;
	}
	/**
	 * 返回 实例状态
	 * @return
	 */
	public String getStatus() 
	{
		return this.status;
	}
	public void setEndTime(LocalDateTime endTime) 
	{
		this.endTime = endTime;
	}
	/**
	 * 返回 实例结束时间
	 * @return
	 */
	public LocalDateTime getEndTime() 
	{
		return this.endTime;
	}
	public void setDuration(Long duration) 
	{
		this.duration = duration;
	}
	/**
	 * 返回 持续时间(ms)
	 * @return
	 */
	public Long getDuration() 
	{
		return this.duration;
	}
	public void setTypeId(String typeId) 
	{
		this.typeId = typeId;
	}
	/**
	 * 返回 所属分类ID
	 * @return
	 */
	public String getTypeId() 
	{
		return this.typeId;
	}
	public void setResultType(String resultType) 
	{
		this.resultType = resultType;
	}
	/**
	 * 返回 流程结束后的最终审批结果，agree=同意；refuse=拒绝

	 * @return
	 */
	public String getResultType() 
	{
		return this.resultType;
	}
	public void setBpmnInstId(String bpmnInstId) 
	{
		this.bpmnInstId = bpmnInstId;
	}
	/**
	 * 返回 BPMN流程实例ID
	 * @return
	 */
	public String getBpmnInstId() 
	{
		return this.bpmnInstId;
	}
	

	
	public void setIsFormmal(String isFormmal) 
	{
		this.isFormmal = isFormmal;
	}
	/**
	 * 返回 是否正式数据
	 * @return
	 */
	public String getIsFormmal() 
	{
		return this.isFormmal;
	}
	public void setParentInstId(String parentInstId) 
	{
		this.parentInstId = parentInstId;
	}
	/**
	 * 返回 父实例Id
	 * @return
	 */
	public String getParentInstId() 
	{
		if(StringUtil.isEmpty(this.parentInstId)) return "0";
		return this.parentInstId;
	}
	
	public JsonNode getAuthorizeRight()
	{
		return authorizeRight;
	}
	public void setAuthorizeRight(JsonNode authorizeRight)
	{
		this.authorizeRight = authorizeRight;
	}
	
	void setIsForbidden(int isForbidden){
		this.isForbidden=isForbidden;
	}
	
	@Override
	public int getIsForbidden() {
		return this.isForbidden;
	}
	@Override
	public String getDataMode() {
		return this.dataMode;
	}
	
	public void setDataMode(String mode){
		this.dataMode=mode;
	}
	
	public int getSupportMobile() {
		return supportMobile;
	}
	public void setSupportMobile(int supportMobile) {
		this.supportMobile = supportMobile;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getCreateOrgPath() {
		return createOrgPath;
	}
	public void setCreateOrgPath(String createOrgPath) {
		this.createOrgPath = createOrgPath;
	}

    public int getIsDele() {
        return isDele;
    }

    public void setIsDele(int isDele) {
        this.isDele = isDele;
    }

    public Boolean getBackToStart() {
        return isBackToStart;
    }

    public void setBackToStart(Boolean backToStart) {
        isBackToStart = backToStart;
    }
    
	public String getBpmFormKey() {
		return bpmFormKey;
	}
	public void setBpmFormKey(String bpmFormKey) {
		this.bpmFormKey = bpmFormKey;
	}
	public boolean isShowModifyRecord() {
		return showModifyRecord;
	}
	public void setShowModifyRecord(boolean showModifyRecord) {
		this.showModifyRecord = showModifyRecord;
	}
	public String getUrgentState() {
		return urgentState;
	}
	public void setUrgentState(String urgentState) {
		this.urgentState = urgentState;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public LocalDateTime getCreateTime() {
		return createTime;
	}
	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}
	public String getCreateOrgId() {
		return createOrgId;
	}
	public void setCreateOrgId(String createOrgId) {
		this.createOrgId = createOrgId;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public LocalDateTime getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("subject", this.subject) 
		.append("procDefId", this.procDefId) 
		.append("bpmnDefId", this.bpmnDefId) 
		.append("procDefKey", this.procDefKey) 
		.append("procDefName", this.procDefName) 
		.append("bizKey", this.bizKey) 
		.append("sysCode", this.sysCode) 
		.append("formKey", this.formKey) 
		.append("status", this.status) 
		.append("endTime", this.endTime) 
		.append("duration", this.duration) 
		.append("typeId", this.typeId) 
		.append("resultType", this.resultType) 
		.append("bpmnInstId", this.bpmnInstId) 
		.append("isFormmal", this.isFormmal)
        .append("isDele", this.isDele)
        .append("parentInstId", this.parentInstId)
		.toString();
	}
	
	
}