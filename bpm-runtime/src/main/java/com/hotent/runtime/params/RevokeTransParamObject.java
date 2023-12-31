package com.hotent.runtime.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 并行审批任务撤销参数对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2019年10月9日
 */
@ApiModel(value="撤销流程参数对象")
public class RevokeTransParamObject {
	
	@ApiModelProperty(name="instanceId",notes="流程实例id",required=false)
	private String instanceId;
	
	@ApiModelProperty(name="nodeId",notes="流程节点id",required=false)
	private String nodeId;
	
	@ApiModelProperty(name="parentTaskId",notes="父任务id",required=true)
	private String parentTaskId;
	
	@ApiModelProperty(name="taskId",notes="任务id",required=true)
	private String taskId;
	
	@ApiModelProperty(name="messageType",notes="消息通知类型，默认邮件通知，inner（内部消息），mail（邮件），sms（短信），多个之单使用英文逗号隔开",allowableValues="mail,inner,sms",required=true)
	private String messageType;
	
	@ApiModelProperty(name="cause",notes="原因")
	private String cause;

	public String getMessageType() {
		return messageType;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public String getParentTaskId() {
		return parentTaskId;
	}

	public void setParentTaskId(String parentTaskId) {
		this.parentTaskId = parentTaskId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

}
