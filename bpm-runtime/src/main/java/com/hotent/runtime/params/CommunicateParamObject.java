package com.hotent.runtime.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 任务沟通参数
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(value="任务沟、转办等通用参数类")
public class CommunicateParamObject {
	@ApiModelProperty(name="account",notes="用户帐号",required=true)
	private String account;
	@ApiModelProperty(name="taskId",notes="任务ID",required=true)
	private String taskId;
	@ApiModelProperty(name="opinion",notes="原因")
	private String opinion;
	@ApiModelProperty(name="userId",notes="用户id(多个账号之间用英文逗号分隔)", required=true)
	private String userId;
	@ApiModelProperty(name="messageType",notes="消息通知类型 (非必填)，默认为邮件，inner（内部消息），mail（邮件），sms（短信），多个之间使用英文逗号隔开",allowableValues="mail,inner,sms")
	private String messageType = "mail";
	@ApiModelProperty(name="instId",notes="实例ID",required=true)
	private String instId;
	@ApiModelProperty(name="files",notes="附件(多个附件之间用英文逗号分隔)", required=true)
	private String files;
	@ApiModelProperty(name="defId",notes="流程定义id",required=true)
	private String defId;

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	public String getFiles() {
		return files;
	}

	public void setFiles(String files) {
		this.files = files;
	}

	public String getDefId() {
		return defId;
	}

	public void setDefId(String defId) {
		this.defId = defId;
	}

	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getOpinion() {
		return opinion;
	}
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
}
