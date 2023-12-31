package com.hotent.runtime.params;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 并行审批参数对象
 * @author zhangxw
 *
 */
@ApiModel(value="并行审批参数对象")
public class TaskApproveLineParam {
	
	@ApiModelProperty(name="instanceId",notes="流程实例")
	private String  instanceId;
	
	@ApiModelProperty(name="taskId",notes="任务ID",required=true)
	private String  taskId;
	
	@ApiModelProperty(name="notifyType",notes="通知类型，inner（内部消息），mail（邮件），sms（短信），多个之单使用英文逗号隔开",allowableValues="inner,mail,sms")
	private String notifyType;
	
	@ApiModelProperty(name="opinion",notes="通知内容",required=true)
	private String opinion;
	
	@ApiModelProperty(name="userIds",notes="流转人员id，多个用英文逗号隔开",required=true)
	private String userIds;

    protected String files = ""; /* 附件 */

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getNotifyType() {
		return notifyType;
	}

	public void setNotifyType(String notifyType) {
		this.notifyType = notifyType;
	}

	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}
	
	public String toString() {
//		return new ToStringBuilder(this)
//		.append("instanceId", this.instanceId) 
//		.append("taskId", this.taskId) 
//		.append("action", this.action) 
//		.append("decideType", this.decideType) 
//		.append("voteType", this.voteType) 
//		.append("voteAmount", this.voteAmount) 
//		.append("signType", this.signType) 
//		.append("notifyType", this.notifyType) 
//		.append("opinion", this.opinion) 
//		.append("userIds", this.userIds) 
//		.toString();
		
		return "{"
		+ "\""+"instanceId"+"\""+":"+"\""+this.instanceId+"\","
		+ "\""+"taskId"+"\""+":"+"\""+this.taskId+"\","
		+"\""+"notifyType"+"\""+":"+"\""+this.notifyType+"\","
		+"\""+"opinion"+"\""+":"+"\""+this.opinion+"\","
		+"\""+"userIds"+"\""+":"+"\""+this.userIds+"\""
		+ "}";
	}

}
