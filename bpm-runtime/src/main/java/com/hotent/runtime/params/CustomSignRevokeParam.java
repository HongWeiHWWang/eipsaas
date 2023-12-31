package com.hotent.runtime.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 签署并审撤销流程参数对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author liygui
 * @email liygui@jee-soft.cn
 * @date 2020年03月12日
 */
@ApiModel(value="撤销流程参数对象")
public class CustomSignRevokeParam {

    @ApiModelProperty(name="currentTaskIds",notes="要撤回的正在运行的任务ID,多个可以通过逗号分隔,并签时会用到")
    private String currentTaskIds;

	@ApiModelProperty(name="instanceId",notes="流程实例id",required=true)
	private String instanceId;
	
	@ApiModelProperty(name="messageType",notes="消息通知类型，默认邮件通知，inner（内部消息），mail（邮件），sms（短信），多个之单使用英文逗号隔开",allowableValues="mail,inner,sms",required=true)
	private String messageType;
	
	@ApiModelProperty(name="cause",notes="原因")
	private String cause;
	
	@ApiModelProperty(name="targetNodeId",notes="要撤回的节点ID",required=true)
    private String targetNodeId;
	
	@ApiModelProperty(name="targetTaskId",notes="要撤回的节点的已办的任务id",required=true)
    private String targetTaskId;
	
	@ApiModelProperty(name="signType",notes="signType",required=true)
    private String signType;

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getCurrentTaskIds() {
		return currentTaskIds;
	}

	public void setCurrentTaskIds(String currentTaskIds) {
		this.currentTaskIds = currentTaskIds;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getMessageType() {
		return messageType;
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

	public String getTargetNodeId() {
		return targetNodeId;
	}

	public void setTargetNodeId(String targetNodeId) {
		this.targetNodeId = targetNodeId;
	}

	public String getTargetTaskId() {
		return targetTaskId;
	}

	public void setTargetTaskId(String targetTaskId) {
		this.targetTaskId = targetTaskId;
	}

   

}
