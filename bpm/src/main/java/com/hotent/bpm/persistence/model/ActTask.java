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
 * 任务
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年7月2日
 */

@ApiModel(value="任务")
@TableName("act_ru_task")
public class ActTask extends BaseModel<ActTask> implements Cloneable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2922429794833482809L;

	@TableId("id_")
	@ApiModelProperty(name="id",notes="主键")
	protected String  id; /*ID_*/

    @TableField("rev_")
	@ApiModelProperty(name="rev",notes="乐观锁")
	protected Integer  rev; /*REV_*/

    @TableField("execution_id_")
	@ApiModelProperty(name="executionId",notes="执行id")
	protected String  executionId; /*EXECUTION_ID_*/

    @TableField("proc_inst_id_")
	@ApiModelProperty(name="procInstId",notes="流程实例id")
	protected String  procInstId; /*PROC_INST_ID_*/

    @TableField("proc_def_id_")
	@ApiModelProperty(name="procDefId",notes="流程定义id")
	protected String  procDefId; /*PROC_DEF_ID_*/

    @TableField("name_")
	@ApiModelProperty(name="name",notes="任务名称")
	protected String  name; /*NAME_*/

    @TableField("parent_task_id_")
	@ApiModelProperty(name="parentTaskId",notes="父任务id")
	protected String  parentTaskId; /*PARENT_TASK_ID_*/

    @TableField("description_")
	@ApiModelProperty(name="description",notes="描述")
	protected String  description; /*DESCRIPTION_*/

    @TableField("task_def_key_")
	@ApiModelProperty(name="taskDefKey",notes="任务定义标识（环节ID）")
	protected String  taskDefKey; /*TASK_DEF_KEY_*/

    @TableField("owner_")
	@ApiModelProperty(name="owner",notes="被代理人")
	protected String  owner; /*OWNER_*/

    @TableField("assignee_")
	@ApiModelProperty(name="assignee",notes="负责人")
	protected String  assignee; /*ASSIGNEE_*/

    @TableField("delegation_")
	@ApiModelProperty(name="delegation",notes="委托状态 PENDING委托中，RESOLVED已处理")
	protected String  delegation; /*DELEGATION_*/

    @TableField("priority_")
	@ApiModelProperty(name="priority",notes="优先级")
	protected Integer  priority; /*PRIORITY_*/

    @TableField("create_time_")
	@ApiModelProperty(name="createTime",notes="创建时间")
	protected LocalDateTime  createTime; /*CREATE_TIME_*/

    @TableField("due_date_")
	@ApiModelProperty(name="dueDate",notes="截止时间")
	protected java.util.Date  dueDate; /*DUE_DATE_*/

    @TableField("suspension_state_")
	@ApiModelProperty(name="suspensionState",notes="暂停状态 1激活 2暂停")
	protected Integer  suspensionState; /*SUSPENSION_STATE_*/
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
	public void setRev(Integer rev) 
	{
		this.rev = rev;
	}
	/**
	 * 返回 REV_
	 * @return
	 */
	public Integer getRev() 
	{
		return this.rev;
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
	public void setDelegation(String delegation) 
	{
		this.delegation = delegation;
	}
	/**
	 * 返回 DELEGATION_
	 * @return
	 */
	public String getDelegation() 
	{
		return this.delegation;
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
	
	public void setDueDate(java.util.Date dueDate) 
	{
		this.dueDate = dueDate;
	}
	/**
	 * 返回 DUE_DATE_
	 * @return
	 */
	public java.util.Date getDueDate() 
	{
		return this.dueDate;
	}
	public void setSuspensionState(Integer suspensionState) 
	{
		this.suspensionState = suspensionState;
	}
	
	/**
	 * 返回 SUSPENSION_STATE_
	 * @return
	 */
	public Integer getSuspensionState() 
	{
		return this.suspensionState;
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
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("rev", this.rev) 
		.append("executionId", this.executionId) 
		.append("procInstId", this.procInstId) 
		.append("procDefId", this.procDefId) 
		.append("name", this.name) 
		.append("parentTaskId", this.parentTaskId) 
		.append("description", this.description) 
		.append("taskDefKey", this.taskDefKey) 
		.append("owner", this.owner) 
		.append("assignee", this.assignee) 
		.append("delegation", this.delegation) 
		.append("priority", this.priority) 
		.append("createTime", this.createTime) 
		.append("dueDate", this.dueDate) 
		.append("suspensionState", this.suspensionState) 
		.toString();
	}
	
	@Override
	public Object clone(){
		 ActTask o = null;  
        try {  
            o = (ActTask) super.clone();  
        } catch (CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
	    return o;  
	}
	
}