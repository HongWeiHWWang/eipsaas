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
 * 系统信息读取实体对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author hugh
 * @email zxh@jee-soft.cn
 * @date 2014-11-17 17:49:50
 */
@ApiModel(description="系统信息读取实体对象")
@TableName("portal_sys_msg_read")
public class MessageRead extends BaseModel<MessageRead>{
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(name="id", notes="主键")
	@TableId("ID_")
	protected String id; 
	
	@ApiModelProperty(name="msgId", notes="消息ID")
	@TableField("msg_id_")
	protected String msgId; 
	
	@ApiModelProperty(name="receiverId", notes="消息接收人ID")
	@TableField("receiver_id_")
	protected String receiverId; 
	
	@ApiModelProperty(name="receiver", notes="消息接收人")
	@TableField("receiver_")
	protected String receiver; 
	
	@ApiModelProperty(name="receiverTime", notes="接收时间")
	@TableField("receiver_time_")
	protected LocalDateTime receiverTime; 
	
	/**
	 * 设置主键
	 * @param id 主键
	 */
	public void setId(String id) 
	{
		this.id = id;
	}
	
	/**
	 * 返回 主键
	 * @return
	 */
	public String getId() 
	{
		return this.id;
	}
	
	/**
	 * 
	 * @param msgId
	 */
	public void setMsgId(String msgId) 
	{
		this.msgId = msgId;
	}
	
	/**
	 * 返回 消息ID
	 * @return
	 */
	public String getMsgId() 
	{
		return this.msgId;
	}
	
	/**
	 * 设置消息接收人ID
	 * @param receiverId 消息接收人ID
	 */
	public void setReceiverId(String receiverId) 
	{
		this.receiverId = receiverId;
	}
	
	/**
	 * 返回 消息接收人ID
	 * @return
	 */
	public String getReceiverId() 
	{
		return this.receiverId;
	}
	
	/**
	 * 设置消息接收人
	 * @param receiver 消息接收人
	 */
	public void setReceiver(String receiver) 
	{
		this.receiver = receiver;
	}
	
	/**
	 * 返回 消息接收人
	 * @return
	 */
	public String getReceiver() 
	{
		return this.receiver;
	}
	
	/**
	 * 设置接收时间
	 * @param receiverTime 接收时间
	 */
	public void setReceiverTime(LocalDateTime receiverTime) 
	{
		this.receiverTime = receiverTime;
	}
	
	/**
	 * 返回 接收时间
	 * @return
	 */
	public LocalDateTime getReceiverTime() 
	{
		return this.receiverTime;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("msgId", this.msgId) 
		.append("receiverId", this.receiverId) 
		.append("receiver", this.receiver) 
		.append("receiverTime", this.receiverTime) 
		.toString();
	}
}