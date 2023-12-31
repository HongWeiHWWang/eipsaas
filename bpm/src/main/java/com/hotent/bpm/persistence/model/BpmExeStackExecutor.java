package com.hotent.bpm.persistence.model;
import java.time.LocalDateTime;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

/**
 * 对象功能:堆栈任务执行人 entity对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zyg
 * 创建时间:2015-01-18 10:49:19
 */
@TableName("bpm_exe_stack_executor")
public class BpmExeStackExecutor extends BaseModel<BpmExeStackExecutor>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7115152242946907951L;
	@TableId("id_")
	protected String  id; /*主键*/
	@TableField("stack_id_")
	protected String  stackId; /*堆栈ID*/
	@TableField("task_id_")
	protected String  taskId; /*任务ID*/	
	@TableField("assignee_id_")
	protected String  assigneeId; /*执行人*/
	@TableField("create_time_")
	protected LocalDateTime  createTime; /*创建时间*/
	@TableField("end_time_")
	protected LocalDateTime  endTime; /*结束时间*/
	@TableField("status_")
	protected Integer  status=0; /*状态(0,初始状态,1,完成)*/
	
	
	
	public void setId(String id) 
	{
		this.id = id;
	}
	/**
	 * 返回 主键
	 * @return
	 */
	public String getId() 
	{
		return this.id;
	}
	public void setStackId(String stackId) 
	{
		this.stackId = stackId;
	}
	/**
	 * 返回 堆栈ID
	 * @return
	 */
	public String getStackId() 
	{
		return this.stackId;
	}
	public void setTaskId(String taskId) 
	{
		this.taskId = taskId;
	}
	/**
	 * 返回 任务ID
	 * @return
	 */
	public String getTaskId() 
	{
		return this.taskId;
	}
	public void setAssigneeId(String assigneeId) 
	{
		this.assigneeId = assigneeId;
	}
	/**
	 * 返回 执行人
	 * @return
	 */
	public String getAssigneeId() 
	{
		return this.assigneeId;
	}
	
	public void setEndTime(LocalDateTime endTime) 
	{
		this.endTime = endTime;
	}
	/**
	 * 返回 结束时间
	 * @return
	 */
	public LocalDateTime getEndTime() 
	{
		return this.endTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
		.append("stackId", this.stackId) 
		.append("taskId", this.taskId) 
		.append("assigneeId", this.assigneeId) 
		.append("createTime", this.createTime) 
		.append("endTime", this.endTime) 
		.toString();
	}
}