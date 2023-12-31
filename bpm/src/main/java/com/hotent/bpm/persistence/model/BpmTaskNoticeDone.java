package com.hotent.bpm.persistence.model;

import java.time.LocalDateTime;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 知会任务已办
 * 
 * <pre>
 *  
 * 描述：知会任务已办 实体对象
 * 构建组：x7
 * 作者:zhaoxy
 * 邮箱:zhaoxy@jee-soft.cn
 * 日期:2019-04-09 15:35:22
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@TableName("bpm_task_notice_done")
@ApiModel(value = "BpmTaskNoticeDone", description = "知会任务已办")
public class BpmTaskNoticeDone extends BaseModel<BpmTaskNoticeDone> {

	private static final long serialVersionUID = 1L;
	@TableId("id_")
	@ApiModelProperty(value = "ID")
	protected String id;

	@TableField("proc_def_id_")
	@ApiModelProperty(value = "流程定义ID")
	protected String procDefId;

	@TableField("sup_inst_id_")
	@ApiModelProperty(value = "父流程实例ID")
	protected String supInstId;

	@TableField("proc_inst_id_")
	@ApiModelProperty(value = "流程实例ID")
	protected String procInstId;

	@TableField("task_id_")
	@ApiModelProperty(value = "任务ID")
	protected String taskId;

	@TableField("task_key_")
	@ApiModelProperty(value = "任务定义Key")
	protected String taskKey;

	@TableField("task_name_")
	@ApiModelProperty(value = "任务名称")
	protected String taskName;

	@TableField("token_")
	@ApiModelProperty(value = "任务令牌")
	protected String token;

	@TableField("qualfieds_")
	@ApiModelProperty(value = "有审批资格用户ID串")
	protected String qualfieds;

	@TableField("qualfied_names_")
	@ApiModelProperty(value = "有审批资格用户名称串")
	protected String qualfiedNames;

	@TableField("auditor_")
	@ApiModelProperty(value = "执行人ID")
	protected String auditor;

	@TableField("auditor_name_")
	@ApiModelProperty(value = "执行人名")
	protected String auditorName;

	@TableField("opinion_")
	@ApiModelProperty(value = "审批意见")
	protected String opinion;

	@TableField("status_")
	@ApiModelProperty(value = "知会任务已办状态")
	protected String status;

	@TableField("form_def_id_")
	@ApiModelProperty(value = "表单定义ID")
	protected String formDefId;

	@TableField("form_name_")
	@ApiModelProperty(value = "表单名")
	protected String formName;

	@TableField("assign_time_")
	@ApiModelProperty(value = "任务分配用户时间")
	protected java.util.Date assignTime;

	@TableField("complete_time_")
	@ApiModelProperty(value = "结束时间")
	protected java.util.Date completeTime;

	@TableField("dur_ms_")
	@ApiModelProperty(value = "持续时间(ms)")
	protected Integer durMs;

	@TableField("files_")
	@ApiModelProperty(value = "附件")
	protected String files;

	@TableField("interpose_")
	@ApiModelProperty(value = "是否干预")
	protected Short interpose;

	@TableField("subject_")
	@ApiModelProperty(value = "流程标题")
	protected String subject;

	@TableField("proc_def_name_")
	@ApiModelProperty(value = "流程名称")
	protected String procDefName;

	@TableField("bpm_task_notice_id_")
	@ApiModelProperty(value = "知会待办主键ID")
	protected String bpmTaskNoticeId;

	@TableField("support_mobile_")
	@ApiModelProperty(value = "支持手机")
	protected Integer supportMobile;

	@TableField("create_time_")
	@ApiModelProperty(value = "创建时间")
	protected LocalDateTime createTime = LocalDateTime.now();
	
	@TableField(exist=false)
	@ApiModelProperty(value = "分类id")
	protected String typeId;

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public BpmTaskNoticeDone() {
	};

	public BpmTaskNoticeDone(String taskName, String procDefId, String procInstId, String auditor, String auditorName,
			String status, String subject, String procDefName, String qualfiedNames, String bpmTaskNoticeId,
			String taskId, String taskKey, Integer supportMobile) {
		super();
		this.taskId = taskId;
		this.taskName = taskName;
		this.procDefId = procDefId;
		this.procInstId = procInstId;
		this.auditor = auditor;
		this.auditorName = auditorName;
		this.status = status;
		this.subject = subject;
		this.procDefName = procDefName;
		this.qualfiedNames = qualfiedNames;
		this.bpmTaskNoticeId = bpmTaskNoticeId;
		this.taskKey = taskKey;
		this.supportMobile = supportMobile;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getProcDefName() {
		return procDefName;
	}

	public void setProcDefName(String procDefName) {
		this.procDefName = procDefName;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getSupportMobile() {
		return supportMobile;
	}

	public void setSupportMobile(Integer supportMobile) {
		this.supportMobile = supportMobile;
	}

	/**
	 * 返回 ID
	 * 
	 * @return
	 */
	public String getId() {
		return this.id;
	}

	public void setProcDefId(String procDefId) {
		this.procDefId = procDefId;
	}

	/**
	 * 返回 流程定义ID
	 * 
	 * @return
	 */
	public String getProcDefId() {
		return this.procDefId;
	}

	public void setSupInstId(String supInstId) {
		this.supInstId = supInstId;
	}

	/**
	 * 返回 父流程实例ID
	 * 
	 * @return
	 */
	public String getSupInstId() {
		return this.supInstId;
	}

	public void setProcInstId(String procInstId) {
		this.procInstId = procInstId;
	}

	/**
	 * 返回 流程实例ID
	 * 
	 * @return
	 */
	public String getProcInstId() {
		return this.procInstId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
	 * 返回 任务ID
	 * 
	 * @return
	 */
	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskKey(String taskKey) {
		this.taskKey = taskKey;
	}

	/**
	 * 返回 任务定义Key
	 * 
	 * @return
	 */
	public String getTaskKey() {
		return this.taskKey;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	/**
	 * 返回 任务名称
	 * 
	 * @return
	 */
	public String getTaskName() {
		return this.taskName;
	}

	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * 返回 任务令牌
	 * 
	 * @return
	 */
	public String getToken() {
		return this.token;
	}

	public void setQualfieds(String qualfieds) {
		this.qualfieds = qualfieds;
	}

	/**
	 * 返回 有审批资格用户ID串
	 * 
	 * @return
	 */
	public String getQualfieds() {
		return this.qualfieds;
	}

	public void setQualfiedNames(String qualfiedNames) {
		this.qualfiedNames = qualfiedNames;
	}

	/**
	 * 返回 有审批资格用户名称串
	 * 
	 * @return
	 */
	public String getQualfiedNames() {
		return this.qualfiedNames;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	/**
	 * 返回 执行人ID
	 * 
	 * @return
	 */
	public String getAuditor() {
		return this.auditor;
	}

	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}

	/**
	 * 返回 执行人名
	 * 
	 * @return
	 */
	public String getAuditorName() {
		return this.auditorName;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	/**
	 * 返回 审批意见
	 * 
	 * @return
	 */
	public String getOpinion() {
		return this.opinion;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 返回 知会任务已办状态
	 * 
	 * @return
	 */
	public String getStatus() {
		return this.status;
	}

	public void setFormDefId(String formDefId) {
		this.formDefId = formDefId;
	}

	/**
	 * 返回 表单定义ID
	 * 
	 * @return
	 */
	public String getFormDefId() {
		return this.formDefId;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	/**
	 * 返回 表单名
	 * 
	 * @return
	 */
	public String getFormName() {
		return this.formName;
	}

	public void setAssignTime(java.util.Date assignTime) {
		this.assignTime = assignTime;
	}

	/**
	 * 返回 任务分配用户时间
	 * 
	 * @return
	 */
	public java.util.Date getAssignTime() {
		return this.assignTime;
	}

	public void setCompleteTime(java.util.Date completeTime) {
		this.completeTime = completeTime;
	}

	/**
	 * 返回 结束时间
	 * 
	 * @return
	 */
	public java.util.Date getCompleteTime() {
		return this.completeTime;
	}

	public void setDurMs(Integer durMs) {
		this.durMs = durMs;
	}

	/**
	 * 返回 持续时间(ms)
	 * 
	 * @return
	 */
	public Integer getDurMs() {
		return this.durMs;
	}

	public void setFiles(String files) {
		this.files = files;
	}

	/**
	 * 返回 附件
	 * 
	 * @return
	 */
	public String getFiles() {
		return this.files;
	}

	public void setInterpose(Short interpose) {
		this.interpose = interpose;
	}

	/**
	 * 返回 是否干预
	 * 
	 * @return
	 */
	public Short getInterpose() {
		return this.interpose;
	}

	public String getBpmTaskNoticeId() {
		return bpmTaskNoticeId;
	}

	public void setBpmTaskNoticeId(String bpmTaskNoticeId) {
		this.bpmTaskNoticeId = bpmTaskNoticeId;
	}
	
	
	

	public LocalDateTime getCreatetime() {
		return createTime;
	}

	public void setCreatetime(LocalDateTime createtime) {
		this.createTime = createtime;
	}

	public String getTypeid() {
		return typeId;
	}

	public void setTypeid(String typeid) {
		this.typeId = typeid;
	}

	/**
	 * @see Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append("procDefId", this.procDefId)
				.append("supInstId", this.supInstId).append("procInstId", this.procInstId).append("taskId", this.taskId)
				.append("taskKey", this.taskKey).append("taskName", this.taskName).append("token", this.token)
				.append("qualfieds", this.qualfieds).append("qualfiedNames", this.qualfiedNames)
				.append("auditor", this.auditor).append("auditorName", this.auditorName).append("opinion", this.opinion)
				.append("status", this.status).append("formDefId", this.formDefId).append("formName", this.formName)
				.append("createTime", this.createTime).append("assignTime", this.assignTime)
				.append("completeTime", this.completeTime).append("durMs", this.durMs).append("files", this.files)
				.append("interpose", this.interpose).append("bpmTaskNoticeId", this.bpmTaskNoticeId)
				.append("supportMobile", this.supportMobile).toString();
	}
}