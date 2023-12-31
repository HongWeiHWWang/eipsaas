package com.hotent.portal.model;


import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 系统消息接收实体对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author hugh
 * @email zxh@jee-soft.cn
 * @date 2014-11-17 17:49:50
 */
@ApiModel(description="系统消息接收实体对象")
@TableName("portal_sys_msg_receiver")
public class MessageReceiver extends BaseModel<MessageReceiver>{

	private static final long serialVersionUID = 1L;
	public static String TYPE_USER="user";
	public static String TYPE_GROUP="group";
	
	@ApiModelProperty(name="id", notes="主键")
	@TableId("id_")
	protected String id; 
	
	@ApiModelProperty(name="msgId", notes="消息ID")
	@TableField("msg_id_")
	protected String msgId; 
	
	@ApiModelProperty(name="receiverType", notes="接收者类型")
	@TableField("receiver_type_")
	protected String receiverType; 
	
	@ApiModelProperty(name="receiverId", notes="接收者ID")
	@TableField("receiver_id_")
	protected String receiverId; 
	
	@ApiModelProperty(name="receiver", notes="接收者")
	@TableField("receiver_")
	protected String receiver; 
	
	/**
	 * 设置主键
	 *@param id 主键
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
	 * 设置消息ID
	 * @param msgId 消息ID
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
	 * 设置 接收者类型 
	 * @param receiverType 接收者类型
	 */
	public void setReceiverType(String receiverType) 
	{
		this.receiverType = receiverType;
	}
	
	/**
	 * 返回 接收者类型
	 * @return
	 */
	public String getReceiverType() 
	{
		return this.receiverType;
	}
	
	/**
	 * 设置接收者ID
	 * @param receiverId 接收者ID
	 */
	public void setReceiverId(String receiverId) 
	{
		this.receiverId = receiverId;
	}
	
	/**
	 * 返回 接收者ID
	 * @return
	 */
	public String getReceiverId() 
	{
		return this.receiverId;
	}
	
	/**
	 * 
	 * @param receiver
	 */
	public void setReceiver(String receiver) 
	{
		this.receiver = receiver;
	}
	
	/**
	 * 返回 接收者
	 * @return
	 */
	public String getReceiver() 
	{
		return this.receiver;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("msgId", this.msgId) 
		.append("receiverType", this.receiverType) 
		.append("receiverId", this.receiverId) 
		.append("receiver", this.receiver) 
		.toString();
	}
}