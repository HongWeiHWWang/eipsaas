package com.hotent.runtime.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 抄送转发
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月11日
 */
@ApiModel(value="抄送转发")
public class CopyToParam {
	
	@ApiModelProperty(name="instanceId",notes="流程实例id",required=true,example="100000123")
	private String instanceId;
	
	@ApiModelProperty(name="account",notes="用户账号",required=true)
	private String account;
	
	@ApiModelProperty(name="userId",notes="接收抄送/转发的用户id,逗号隔开",required=true)
	private String userId;
	
	@ApiModelProperty(name="messageType",notes="消息类型 inner(站内消息),mail(邮件),sms(短信),voic(语音)",required=false,allowableValues="inner,mail,sms,voic")
	private String messageType;
	
	@ApiModelProperty(name="opinion",notes="意见",required=true,example="抄送/转发意见")
	private String opinion;
	
	@ApiModelProperty(name="copyToType",notes="抄送/转发  0/1",required=true,allowableValues="0,1")
	private String copyToType;

    @ApiModelProperty(name="taskId",notes="任务id",required=true)
    private String taskId;

    protected String files = ""; /* 附件 */

    protected String selectNodeId = "";//任务节点ID

    public String getSelectNodeId() {
        return selectNodeId;
    }

    public void setSelectNodeId(String selectNodeId) {
        this.selectNodeId = selectNodeId;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
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

	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	public String getCopyToType() {
		return copyToType;
	}

	public void setCopyToType(String copyToType) {
		this.copyToType = copyToType;
	}
	
	
}
