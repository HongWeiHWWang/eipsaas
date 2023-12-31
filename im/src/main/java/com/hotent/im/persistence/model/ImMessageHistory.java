package com.hotent.im.persistence.model;
import java.time.LocalDateTime;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.hotent.base.model.BaseModel;


 /**
 * 
 * <pre> 
 * 描述：聊天消息历史表 实体对象
 * 构建组：x5-bpmx-platform
 * 作者:heyifan
 * 邮箱:heyf@jee-soft.cn
 * 日期:2017-11-17 14:45:52
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public class ImMessageHistory extends BaseModel<String>{
	
	/**
	* ID
	*/
	protected String id; 
	
	/**
	* 消息类型
	*/
	protected String type; 
	
	/**
	* 消息发送者
	*/
	protected String from; 
	
	/**
	* 消息id
	*/
	protected String messageId; 
	
	/**
	 *会话编号 
	 */
	protected String sessionCode;
	
	/**
	* 发送时间
	*/
	protected Long sendTime; 
	
	/**
	* 消息内容
	*/
	protected String content; 
	
	/**
	 *  名称
	 */
	protected String fromName;
	
	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getSessionCode() {
		return sessionCode;
	}

	public void setSessionCode(String sessionCode) {
		this.sessionCode = sessionCode;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 ID
	 * @return
	 */
	public String getId() {
		return this.id;
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
	
	public void setFrom(String from) {
		this.from = from;
	}
	
	/**
	 * 返回 消息发送者
	 * @return
	 */
	public String getFrom() {
		return this.from;
	}
	
	
	public void setSendTime(Long localDateTime) {
		this.sendTime = localDateTime;
	}
	
	/**
	 * 返回 发送时间
	 * @return
	 */
	public Long getSendTime() {
		return this.sendTime;
	}
	
	
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("type", this.type) 
		.append("from", this.from) 
		.append("messageId", this.messageId) 
		.append("sendTime", this.sendTime) 
		.append("content", this.content) 
		.toString();
	}
}