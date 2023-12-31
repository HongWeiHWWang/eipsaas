package com.hotent.runtime.params;



import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 撤销我流转出去的任务参数类
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月13日
 */
@ApiModel(value="撤销我流转出去的任务参数类")
public class WithDrawParam {
	
	@ApiModelProperty(name="taskId",notes="任务id",required=true)
	protected String taskId;
	
	@ApiModelProperty(name="opinion",notes="意见")
	private String opinion;
	
	@ApiModelProperty(name="notifyType",notes="通知相关人员消息类型，inner（内部消息），mail（邮件），sms（短信），voic（电话语音），多个之单使用英文逗号隔开",allowableValues="mail,inner,sms,voic")
	protected String notifyType;

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

	public String getNotifyType() {
		return notifyType;
	}

	public void setNotifyType(String notifyType) {
		this.notifyType = notifyType;
	}
}
