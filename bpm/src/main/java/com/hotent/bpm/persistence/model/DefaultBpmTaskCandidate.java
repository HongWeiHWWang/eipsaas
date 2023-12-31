package com.hotent.bpm.persistence.model;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bpm.api.model.process.task.BpmTaskCandidate;

/**
 * 对象功能:任务候选人 entity对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zyp
 * 创建时间:2014-02-12 18:04:14
 */
@TableName("bpm_task_candidate")
public class DefaultBpmTaskCandidate extends BaseModel<DefaultBpmTaskCandidate> implements BpmTaskCandidate,Cloneable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1382539058756263334L;
	
	@TableId("id_")
	protected String  id; /*主键*/
	
	@TableField("task_id_")
	protected String  taskId; /*任务ID*/
	
	@TableField("type_")
	protected String  type; /*候选人类型*/
	
	@TableField("executor_")
	protected String  executor; /*执行人ID*/
	
	@TableField("proc_inst_id_")
	protected String  procInstId; /*流程实例ID*/
	
	//非数据库字段 执行人名称
	@TableField(exist=false)
	protected transient String  executors;
	
	
	public DefaultBpmTaskCandidate(){
		
	}
	
	public DefaultBpmTaskCandidate(String taskId,String type,String executor,String procInstId){
		this.id=UniqueIdUtil.getSuid();
		this.taskId=taskId;
		this.type=type;
		this.executor=executor;
		this.procInstId=procInstId;
	}
	
	
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
	public void setType(String type) 
	{
		this.type = type;
	}
	/**
	 * 返回 候选人类型
	 * @return
	 */
	public String getType() 
	{
		return this.type;
	}
	public void setExecutor(String executor) 
	{
		this.executor = executor;
	}
	/**
	 * 返回 执行人ID
	 * @return
	 */
	public String getExecutor() 
	{
		return this.executor;
	}
	public void setProcInstId(String procInstId) 
	{
		this.procInstId = procInstId;
	}
	/**
	 * 返回 流程实例ID
	 * @return
	 */
	public String getProcInstId() 
	{
		return this.procInstId;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("taskId", this.taskId) 
		.append("type", this.type) 
		.append("executor", this.executor) 
		.append("actInstId", this.procInstId) 
		.toString();
	}

	public String getExecutors() {
		return executors;
	}

	public void setExecutors(String executors) {
		this.executors = executors;
	}
	
}