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
 * 顺序签署人员
 * <pre> 
 * 描述：顺序签署人员 实体对象
 * 构建组：x7
 * 作者:jason
 * 邮箱:liygui@jee-soft.cn
 * 日期:2019-10-09 10:40:32
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
 @TableName("bpm_task_sign_sequence")
 @ApiModel(value = "BpmTaskSignSequence",description = "顺序签署人员") 
public class BpmTaskSignSequence extends BaseModel<BpmTaskSignSequence>{

	private static final long serialVersionUID = 1L;
	
	@XmlTransient
	@TableId("id_")
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
	
	@XmlAttribute(name = "nextTaskId")
	@TableField("NEXT_TASK_ID_")
	@ApiModelProperty(value="下一任务ID")
	protected String nextTaskId; 
	
	@XmlAttribute(name = "parentId")
	@TableField("PARENT_ID_")
	@ApiModelProperty(value="父id")
	protected String parentId; 
	
	@XmlAttribute(name = "status")
	@TableField("STATUS_")
	@ApiModelProperty(value="状态")
	protected String status; 
	
	@XmlAttribute(name = "path")
	@TableField("PATH_")
	@ApiModelProperty(value="路径")
	protected String path; 
	
	@XmlAttribute(name = "seq")
	@TableField("SEQ_")
	@ApiModelProperty(value="顺序")
	protected Short seq; 
	
	@XmlAttribute(name = "executor")
	@TableField("EXECUTOR_")
	@ApiModelProperty(value="执行人")
	protected String executor;
	
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
	
	public String getNextTaskId() {
		return nextTaskId;
	}

	public void setNextTaskId(String nextTaskId) {
		this.nextTaskId = nextTaskId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	/**
	 * 返回 PARENT_ID_
	 * @return
	 */
	public String getParentId() {
		return this.parentId;
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
	
	public void setSeq(Short seq) {
		this.seq = seq;
	}
	
	/**
	 * 返回 顺序
	 * @return
	 */
	public Short getSeq() {
		return this.seq;
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
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("instanceId", this.instanceId) 
		.append("taskId", this.taskId) 
		.append("parentId", this.parentId) 
		.append("status", this.status) 
		.append("path", this.path) 
		.append("seq", this.seq) 
		.append("executor", this.executor) 
		.toString();
	}
}