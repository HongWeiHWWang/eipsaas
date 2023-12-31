package com.hotent.portal.model;
import java.time.LocalDateTime;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


 /**
 * portal_message_log
 * <pre> 
 * 描述：portal_message_log 实体对象
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2019-05-30 21:34:36
 * 版权：广州宏天软件有限公司
 * </pre>
 */
 @ApiModel(value = "MessageLog",description = "portal_message_log")
 @TableName("portal_message_log")
public class MessageLog extends BaseModel<MessageLog>{

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="ID_")
	@TableId("ID_")
	protected String id; 
	
	@ApiModelProperty(value="标题")
	@TableField("SUBJECT_")
	protected String subject; 
	
	@ApiModelProperty(value="消息类型")
	@TableField("TYPE_")
	protected String type; 
	
	@ApiModelProperty(value="发送人ID")
	@TableField("SENDER_ID_")
	protected String senderId; 
	
	@ApiModelProperty(value="发送人姓名")
	@TableField("SENDER_NAME_")
	protected String senderName; 
	
	@ApiModelProperty(value="消息接收人")
	@TableField("RECEIVERS_")
	protected String receivers; 
	
	@ApiModelProperty(value="是否发送成功")
	@TableField("IS_SUCCESS_")
	protected Boolean isSuccess; 
	
	@ApiModelProperty(value="调用结果")
	@TableField("EXCEPTION_")
	protected String exception;
	
	@ApiModelProperty(value="创建时间")
	@TableField("CREATE_TIME_")
	protected LocalDateTime createTime;
	
	@ApiModelProperty(value="消息内容")
	@TableField("CONTENT_")
	protected String content; 
	
	@ApiModelProperty(value="消息VO")
	@TableField("MSG_VO_")
	protected String msgVo; 
	
	@ApiModelProperty(value="重试次数")
	@TableField("RETRY_COUNT_")
	protected Integer retryCount; 
	
	
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 ID_
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	/**
	 * 返回 标题
	 * @return
	 */
	public String getSubject() {
		return this.subject;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * 返回 消息类型
	 * @return
	 */
	public String getType() {
		return this.type;
	}
	
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	
	/**
	 * 返回 是否发送成功
	 * @return
	 */
	public Boolean getIsSuccess() {
		return this.isSuccess;
	}
	
	public void setException(String exception) {
		this.exception = exception;
	}
	
	/**
	 * 返回 调用结果
	 * @return
	 */
	public String getException() {
		return this.exception;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * 返回 消息内容
	 * @return
	 */
	public String getContent() {
		return this.content;
	}
	
	public void setMsgVo(String msgVo) {
		this.msgVo = msgVo;
	}
	
	/**
	 * 返回 消息VO
	 * @return
	 */
	public String getMsgVo() {
		return this.msgVo;
	}
	
	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}
	
	/**
	 * 返回 重试次数
	 * @return
	 */
	public Integer getRetryCount() {
		return this.retryCount;
	}
	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getReceivers() {
		return receivers;
	}

	public void setReceivers(String receivers) {
		this.receivers = receivers;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("subject", this.subject) 
		.append("type", this.type) 
		.append("senderId", this.senderId) 
		.append("senderName", this.senderName) 
		.append("receivers", this.receivers) 
		.append("isSuccess", this.isSuccess) 
		.append("exception", this.exception) 
		.append("createTime", this.createTime) 
		.append("content", this.content) 
		.append("msgVo", this.msgVo) 
		.append("retryCount", this.retryCount) 
		.toString();
	}
}