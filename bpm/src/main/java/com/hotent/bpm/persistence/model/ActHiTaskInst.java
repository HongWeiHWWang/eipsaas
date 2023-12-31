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
 * 历史任务
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年7月2日
 */

@ApiModel(value="历史任务")
@TableName("act_hi_taskinst")
public class ActHiTaskInst extends BaseModel<ActHiTaskInst> implements Cloneable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5862742694663016705L;

	@TableId("id_")
	@ApiModelProperty(name="id",notes="主键")
	protected String  id; /*ID_*/
	
	@TableField("proc_def_id_")
	@ApiModelProperty(name="procDefId",notes="流程定义id")
	protected String  procDefId; /*PROC_DEF_ID_*/
	
	@TableField("task_def_key_")
	@ApiModelProperty(name="taskDefKey",notes="任务定义标识（环节ID）")
	protected String  taskDefKey; /*TASK_DEF_KEY_*/
	
	@TableField("proc_inst_id_")
	@ApiModelProperty(name="procInstId",notes="流程实例id")
	protected String  procInstId; /*PROC_INST_ID_*/
	
	@TableField("execution_id_")
	@ApiModelProperty(name="executionId",notes="执行id")
	protected String  executionId; /*EXECUTION_ID_*/
	
	@TableField("name_")
	@ApiModelProperty(name="name",notes="名称")
	protected String  name; /*NAME_*/
	
	@TableField("parent_task_id_")
	@ApiModelProperty(name="parentTaskId",notes="父任务id")
	protected String  parentTaskId; /*PARENT_TASK_ID_*/
	
	@TableField("description_")
	@ApiModelProperty(name="description",notes="描述")
	protected String  description; /*DESCRIPTION_*/
	
	@TableField("owner_")
	@ApiModelProperty(name="owner",notes="被代理人")
	protected String  owner; /*OWNER_*/
	
	@TableField("assignee_")
	@ApiModelProperty(name="assignee",notes="负责人")
	protected String  assignee; /*ASSIGNEE_*/
	
	@TableField("start_time_")
	@ApiModelProperty(name="startTime",notes="开始时间")
	protected LocalDateTime  startTime; /*START_TIME_*/
	
	@TableField("claim_time_")
	@ApiModelProperty(name="claimTime",notes="领取时间")
	protected LocalDateTime  claimTime; /*CLAIM_TIME_*/
	
	@TableField("end_time_")
	@ApiModelProperty(name="endTime",notes="结束时间")
	protected LocalDateTime  endTime; /*END_TIME_*/
	
	@TableField("duration_")
	@ApiModelProperty(name="duration",notes="持续时间")
	protected Long  duration; /*DURATION_*/
	
	@TableField("delete_reason_")
	@ApiModelProperty(name="deleteReason",notes="删除原因")
	protected String  deleteReason; /*DELETE_REASON_*/
	
	@TableField("priority_")
	@ApiModelProperty(name="priority",notes="优先级")
	protected Integer  priority; /*PRIORITY_*/
	
	@TableField("due_date_")
	@ApiModelProperty(name="dueDate",notes="截止时间")
	protected LocalDateTime  dueDate; /*DUE_DATE_*/
	
	@TableField("form_key_")
	@ApiModelProperty(name="formKey",notes="表单标识")
	protected String  formKey; /*FORM_KEY_*/
	public void setId(String id) 
	{
		this.id = id;
	}
	/**
	 * 返回 ID_
	 * @return
	 */
	public String getId() 
	{
		return this.id;
	}
	public void setProcDefId(String procDefId) 
	{
		this.procDefId = procDefId;
	}
	/**
	 * 返回 PROC_DEF_ID_
	 * @return
	 */
	public String getProcDefId() 
	{
		return this.procDefId;
	}
	public void setTaskDefKey(String taskDefKey) 
	{
		this.taskDefKey = taskDefKey;
	}
	/**
	 * 返回 TASK_DEF_KEY_
	 * @return
	 */
	public String getTaskDefKey() 
	{
		return this.taskDefKey;
	}
	public void setProcInstId(String procInstId) 
	{
		this.procInstId = procInstId;
	}
	/**
	 * 返回 PROC_INST_ID_
	 * @return
	 */
	public String getProcInstId() 
	{
		return this.procInstId;
	}
	public void setExecutionId(String executionId) 
	{
		this.executionId = executionId;
	}
	/**
	 * 返回 EXECUTION_ID_
	 * @return
	 */
	public String getExecutionId() 
	{
		return this.executionId;
	}
	public void setName(String name) 
	{
		this.name = name;
	}
	/**
	 * 返回 NAME_
	 * @return
	 */
	public String getName() 
	{
		return this.name;
	}
	public void setParentTaskId(String parentTaskId) 
	{
		this.parentTaskId = parentTaskId;
	}
	/**
	 * 返回 PARENT_TASK_ID_
	 * @return
	 */
	public String getParentTaskId() 
	{
		return this.parentTaskId;
	}
	public void setDescription(String description) 
	{
		this.description = description;
	}
	/**
	 * 返回 DESCRIPTION_
	 * @return
	 */
	public String getDescription() 
	{
		return this.description;
	}
	public void setOwner(String owner) 
	{
		this.owner = owner;
	}
	/**
	 * 返回 OWNER_
	 * @return
	 */
	public String getOwner() 
	{
		return this.owner;
	}
	public void setAssignee(String assignee) 
	{
		this.assignee = assignee;
	}
	/**
	 * 返回 ASSIGNEE_
	 * @return
	 */
	public String getAssignee() 
	{
		return this.assignee;
	}
	public void setStartTime(LocalDateTime startTime) 
	{
		this.startTime = startTime;
	}
	/**
	 * 返回 START_TIME_
	 * @return
	 */
	public LocalDateTime getStartTime() 
	{
		return this.startTime;
	}
	public void setClaimTime(LocalDateTime claimTime) 
	{
		this.claimTime = claimTime;
	}
	/**
	 * 返回 CLAIM_TIME_
	 * @return
	 */
	public LocalDateTime getClaimTime() 
	{
		return this.claimTime;
	}
	public void setEndTime(LocalDateTime endTime) 
	{
		this.endTime = endTime;
	}
	/**
	 * 返回 END_TIME_
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
	 * 返回 DURATION_
	 * @return
	 */
	public Long getDuration() 
	{
		return this.duration;
	}
	public void setDeleteReason(String deleteReason) 
	{
		this.deleteReason = deleteReason;
	}
	/**
	 * 返回 DELETE_REASON_
	 * @return
	 */
	public String getDeleteReason() 
	{
		return this.deleteReason;
	}
	public void setPriority(Integer priority) 
	{
		this.priority = priority;
	}
	/**
	 * 返回 PRIORITY_
	 * @return
	 */
	public Integer getPriority() 
	{
		return this.priority;
	}
	public void setDueDate(LocalDateTime dueDate) 
	{
		this.dueDate = dueDate;
	}
	/**
	 * 返回 DUE_DATE_
	 * @return
	 */
	public LocalDateTime getDueDate() 
	{
		return this.dueDate;
	}
	public void setFormKey(String formKey) 
	{
		this.formKey = formKey;
	}
	/**
	 * 返回 FORM_KEY_
	 * @return
	 */
	public String getFormKey() 
	{
		return this.formKey;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("procDefId", this.procDefId) 
		.append("taskDefKey", this.taskDefKey) 
		.append("procInstId", this.procInstId) 
		.append("executionId", this.executionId) 
		.append("name", this.name) 
		.append("parentTaskId", this.parentTaskId) 
		.append("description", this.description) 
		.append("owner", this.owner) 
		.append("assignee", this.assignee) 
		.append("startTime", this.startTime) 
		.append("claimTime", this.claimTime) 
		.append("endTime", this.endTime) 
		.append("duration", this.duration) 
		.append("deleteReason", this.deleteReason) 
		.append("priority", this.priority) 
		.append("dueDate", this.dueDate) 
		.append("formKey", this.formKey) 
		.toString();
	}
	
	@Override
	public Object clone(){
		 ActHiTaskInst o = null;  
        try {  
            o = (ActHiTaskInst) super.clone();  
        } catch (CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
	    return o;  
	}
}