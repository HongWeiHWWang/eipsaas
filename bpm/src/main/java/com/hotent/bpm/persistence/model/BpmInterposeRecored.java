package com.hotent.bpm.persistence.model;
import java.time.LocalDateTime;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;
import com.hotent.bpm.api.constant.InterPoseType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


 /**
 * 流程干预记录表
 * <pre> 
 * 描述：流程干预记录表 实体对象
 * 构建组：x7
 * 作者:heyifan
 * 邮箱:heyf@jee-soft.cn
 * 日期:2019-01-04 00:51:42
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@TableName("bpm_interpose_recored")
 @ApiModel(value = "BpmInterposeRecored",description = "流程干预记录表") 
public class BpmInterposeRecored extends BaseModel<BpmInterposeRecored>{

	private static final long serialVersionUID = 1L;
	
	@TableId("id_")
	@ApiModelProperty(value="意见ID")
	protected String id; 
	
	@TableField("proc_def_id_")
	@ApiModelProperty(value="流程定义ID")
	protected String procDefId; 
	
	@TableField("proc_inst_id_")
	@ApiModelProperty(value="流程实例ID")
	protected String procInstId; 
	
	@TableField("task_id_")
	@ApiModelProperty(value="任务ID")
	protected String taskId; 
	
	@TableField("task_name_")
	@ApiModelProperty(value="任务名称")
	protected String taskName; 
	
	@TableField("auditor_")
	@ApiModelProperty(value="执行人ID")
	protected String auditor; 
	
	@TableField("auditor_name_")
	@ApiModelProperty(value="执行人名")
	protected String auditorName; 
	
	@TableField("opinion_")
	@ApiModelProperty(value="OPINION_")
	protected String opinion; 
	
	@TableField("status_")
	@ApiModelProperty(value="审批状态")
	protected String status; 
	
	@TableField("dur_ms_")
	@ApiModelProperty(value="持续时间(ms)")
	protected Integer durMs; 
	
	@TableField("files_")
	@ApiModelProperty(value="意见附件字符串")
	protected String files; 
	
	@TableField("is_done_")
	@ApiModelProperty(value="IS_DONE_")
	protected String isDone; 
	
	@TableField("complete_time_")
	@ApiModelProperty(value="结束时间")
	protected LocalDateTime completeTime; 
	
	@TableField("create_time_")
	@ApiModelProperty(value="创建时间")
	protected LocalDateTime createTime = LocalDateTime.now(); 
	
	@TableField(exist=false)
	protected boolean  adminInterPose = true;
	
	
	
	public void setId(String id) {
		this.id = id;
	}
	
	
	public BpmInterposeRecored() {
		super();
	}

	public BpmInterposeRecored(String procInstId, String opinion, InterPoseType type, String isDone) {
		super();
		this.procInstId = procInstId;
		this.opinion = opinion;
		this.status = type.getKey();
		this.isDone = isDone;
	}



	/**
	 * 返回 意见ID
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
	 * @return
	 */
	public String getProcDefId() {
		return this.procDefId;
	}
	
	public void setProcInstId(String procInstId) {
		this.procInstId = procInstId;
	}
	
	/**
	 * 返回 流程实例ID
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
	 * @return
	 */
	public String getTaskId() {
		return this.taskId;
	}
	
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	/**
	 * 返回 任务名称
	 * @return
	 */
	public String getTaskName() {
		return this.taskName;
	}
	
	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}
	
	/**
	 * 返回 执行人ID
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
	 * @return
	 */
	public String getAuditorName() {
		return this.auditorName;
	}
	
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
	
	/**
	 * 返回 OPINION_
	 * @return
	 */
	public String getOpinion() {
		return this.opinion;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * 返回 审批状态。start=发起流程；awaiting_check=待审批；agree=同意；against=反对；return=驳回；abandon=弃权；retrieve=追回
	 * @return
	 */
	public String getStatus() {
		return this.status;
	}
	

	public void setDurMs(Integer durMs) {
		this.durMs = durMs;
	}
	
	/**
	 * 返回 持续时间(ms)
	 * @return
	 */
	public Integer getDurMs() {
		return this.durMs;
	}
	
	public void setFiles(String files) {
		this.files = files;
	}
	
	/**
	 * 返回 FILES_
	 * @return
	 */
	public String getFiles() {
		return this.files;
	}
	
	
	public void setIsDone(String isDone) {
		this.isDone = isDone;
	}
	
	/**
	 * 返回 IS_DONE_
	 * @return
	 */
	public String getIsDone() {
		return this.isDone;
	}
	


	public LocalDateTime getCompleteTime() {
		return completeTime;
	}


	public void setCompleteTime(LocalDateTime localDateTime) {
		this.completeTime = localDateTime;
	}


	public boolean isAdminInterPose() {
		return adminInterPose;
	}


	public LocalDateTime getCreateTime() {
		return createTime;
	}


	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}


	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("procDefId", this.procDefId) 
		.append("procInstId", this.procInstId) 
		.append("taskId", this.taskId) 
		.append("taskName", this.taskName) 
		.append("auditor", this.auditor) 
		.append("auditorName", this.auditorName) 
		.append("opinion", this.opinion) 
		.append("status", this.status) 
		.append("durMs", this.durMs) 
		.append("files", this.files) 
		.append("isDone", this.isDone) 
		.toString();
	}
}