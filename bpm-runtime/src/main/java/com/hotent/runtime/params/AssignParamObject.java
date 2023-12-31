package com.hotent.runtime.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 任务转办、加签参数对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(value="任务转办、加签参数对象")
public class AssignParamObject {
	
	@ApiModelProperty(name="account",notes="用户帐号",example="admin")
	private String account;
	
	@ApiModelProperty(name="taskId",notes="任务id")
	private String taskId;
	
	@ApiModelProperty(name="messageType",notes="消息通知类型 非必填，默认邮件，inner（内部消息），mail（邮件），sms（短信），多个之单使用英文逗号隔开",allowableValues="mail,inner,sms")
	private String messageType = "mail";
	
	@ApiModelProperty(name="opinion",notes="意见")
	private String opinion;
	
	@ApiModelProperty(name="userId",notes="转办、加签用户id，多个id之间用逗号分隔")
	private String userId;

    protected String files = ""; /* 附件 */

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
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

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
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

}
