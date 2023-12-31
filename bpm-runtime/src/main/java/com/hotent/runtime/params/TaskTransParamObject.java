package com.hotent.runtime.params;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 流转参数对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(value="流转参数对象")
public class TaskTransParamObject {

	@ApiModelProperty(name="instanceId",notes="流程实例")
	private String  instanceId;
	
	@ApiModelProperty(name="taskId",notes="任务ID",required=true)
	private String  taskId;
	
	@ApiModelProperty(name="action",notes="完成后的操作，submit（提交），back（返回）",allowableValues="submit,back")
	private String  action;
	
	@ApiModelProperty(name="decideType",notes="计票策略，agree（同意票）refuse（否决票）",allowableValues="agree,refuse")
	private String  decideType;
	
	@ApiModelProperty(name="voteType",notes="投票类型,amount（绝对票），percent（百分比）",allowableValues="percent,amount")
	private String  voteType;
	
	@ApiModelProperty(name="voteAmount",notes="票数")
	private Short  voteAmount;
	
	@ApiModelProperty(name="signType",notes="会签类型，parallel（并行），seq（串行）",allowableValues="parallel,seq")
	private String  signType;
	
	@ApiModelProperty(name="notifyType",notes="通知类型，inner（内部消息），mail（邮件），sms（短信），多个之单使用英文逗号隔开",allowableValues="inner,mail,sms")
	private String notifyType;
	
	@ApiModelProperty(name="opinion",notes="通知内容",required=true)
	private String opinion;
	
	@ApiModelProperty(name="userIds",notes="流转人员id，多个用英文逗号隔开",required=true)
	private String userIds;
	
	@ApiModelProperty(name="data",notes="表单数据",required=false)
	private String data;

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

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getDecideType() {
		return decideType;
	}

	public void setDecideType(String decideType) {
		this.decideType = decideType;
	}

	public String getVoteType() {
		return voteType;
	}

	public void setVoteType(String voteType) {
		this.voteType = voteType;
	}

	public Short getVoteAmount() {
		return voteAmount;
	}

	public void setVoteAmount(Short voteAmount) {
		this.voteAmount = voteAmount;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
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
	
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
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
		+"\""+"action"+"\""+":"+"\""+this.action+"\","
		+"\""+"decideType"+"\""+":"+"\""+this.decideType+"\","
		+"\""+"voteType"+"\""+":"+"\""+this.voteType+"\","
		+"\""+"voteAmount"+"\""+":"+"\""+this.voteAmount+"\","
		+"\""+"signType"+"\""+":"+"\""+this.signType+"\","
		+"\""+"notifyType"+"\""+":"+"\""+this.notifyType+"\","
		+"\""+"opinion"+"\""+":"+"\""+this.opinion+"\","
		+"\""+"userIds"+"\""+":"+"\""+this.userIds+"\""
		+ "}";
	}

}
