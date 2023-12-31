package com.hotent.runtime.params;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 流程终止参数
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(value="流程终止参数")
@JsonInclude(Include.NON_NULL)
public class DoEndParamObject {
	
	@ApiModelProperty(name="account",notes="用户帐号",required=true,example="admin")
	private String account;
	
	@ApiModelProperty(name="taskId",notes="任务id",required=true)
	private String taskId;
	
	@ApiModelProperty(name="endReason",notes="终止原因",required=true)
	private String endReason;
	
	@ApiModelProperty(name="messageType",notes="消息通知类型 非必填，默认邮件，inner（内部消息），mail（邮件），sms（短信），多个之单使用英文逗号隔开",allowableValues="mail,inner,sms")
	private String messageType;
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

	public String getEndReason() {
		return endReason;
	}

	public void setEndReason(String endReason) {
		this.endReason = endReason;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

}
