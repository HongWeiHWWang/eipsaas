package com.hotent.runtime.model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;


 /**
 * 并行签署
 * <pre> 
 * 描述：并行签署 实体对象
 * 构建组：x7
 * 作者:jason
 * 邮箱:liygui@jee-soft.cn
 * 日期:2019-10-14 10:34:11
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
 @TableName("bpm_task_sign_line")
 @ApiModel(value = "BpmTaskSignLine",description = "并行签署") 
public class BpmTaskSignLine extends BaseModel<BpmTaskSignLine>{

	private static final long serialVersionUID = 1L;
	
	@XmlTransient
	@TableId("ID_")
	@ApiModelProperty(value="主键")
	protected String id; 
	
	@XmlAttribute(name = "instanceId")
	@TableField("INSTANCE_ID_")
	@ApiModelProperty(value="流程实例")
	protected String instanceId; 
	
	@XmlAttribute(name = "nodeId")
	@TableField("NODE_ID_")
	@ApiModelProperty(value="节点id")
	protected String nodeId; 
	
	@XmlAttribute(name = "taskId")
	@TableField("TASK_ID_")
	@ApiModelProperty(value="任务ID")
	protected String taskId; 
	
	@Deprecated
	@XmlAttribute(name = "parentTaskId")
	@TableField("PARENT_TASK_ID_")
	@ApiModelProperty(value="父任务id")
	protected String parentTaskId; 
	
	@XmlAttribute(name = "path")
	@TableField("PATH_")
	@ApiModelProperty(value="路径")
	protected String path; 
	
	@XmlAttribute(name = "status")
	@TableField("STATUS_")
	@ApiModelProperty(value="状态")
	protected String status; 
	
	@XmlAttribute(name = "executor")
	@TableField("EXECUTOR_")
	@ApiModelProperty(value="执行人")
	protected String executor; 
	
	@XmlAttribute(name = "action")
	@TableField("ACTION_")
	@ApiModelProperty(value="并行签署后的动作 submit提交 back返回 ")
	protected String action; 
	
	@XmlAttribute(name = "isRead")
	@TableField("IS_READ_")
	@ApiModelProperty(value="阅读状态，0：待阅，1：已阅")
	protected Integer isRead = 0;
	
	public Integer getIsRead() {
		return isRead;
	}
	
	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}
	
	public boolean isRead() {
		return isRead==1;
	}
	public boolean isNotRead() {
		return !isRead();
	}
	
	
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 主键
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	
	/**
	 * 返回 流程实例
	 * @return
	 */
	public String getInstanceId() {
		return this.instanceId;
	}
	
	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
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
	
	public void setParentTaskId(String parentTaskId) {
		this.parentTaskId = parentTaskId;
	}
	
	/**
	 * 返回 PARENT_TASK_ID_
	 * @return
	 */
	public String getParentTaskId() {
		return this.parentTaskId;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * 返回 路径
	 * @return
	 */
	public String getPath() {
		return this.path;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * 返回 STATUS_
	 * @return
	 */
	public String getStatus() {
		return this.status;
	}
	
	public void setExecutor(String executor) {
		this.executor = executor;
	}
	
	/**
	 * 返回 执行人
	 * @return
	 */
	public String getExecutor() {
		return this.executor;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	/**
	 * 返回 并行签署后的动作 submit提交 back返回 
	 * @return
	 */
	public String getAction() {
		return this.action;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("instanceId", this.instanceId) 
		.append("taskId", this.taskId) 
		.append("parentTaskId", this.parentTaskId) 
		.append("path", this.path) 
		.append("status", this.status) 
		.append("executor", this.executor) 
		.append("action", this.action) 
		.toString();
	}
}