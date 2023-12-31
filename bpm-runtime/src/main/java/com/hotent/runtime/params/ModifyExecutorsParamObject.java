package com.hotent.runtime.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 修改任务执行人
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(value="修改任务执行人")
public class ModifyExecutorsParamObject {
	
	@ApiModelProperty(name="taskId",notes="任务id",required=true)
	private String taskId;
	
	@ApiModelProperty(name="userIds",notes="用户ID集",required=true)
	private String[] userIds;
	
	@ApiModelProperty(name="messageType",notes="消息通知类型，默认邮件通知，inner（内部消息），mail（邮件），sms（短信），多个之单使用英文逗号隔开",allowableValues="mail,inner,sms",required=true)
	private String messageType;
	
	@ApiModelProperty(name="cause",notes="原因",required=true)
	private String cause;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String[] getUserIds() {
		return userIds;
	}

	public void setUserIds(String[] userIds) {
		this.userIds = userIds;
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

}
