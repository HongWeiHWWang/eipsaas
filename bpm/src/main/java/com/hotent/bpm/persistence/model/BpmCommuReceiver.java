package com.hotent.bpm.persistence.model;
import java.time.LocalDateTime;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

/**
 * 对象功能:任务通知接收人 entity对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zyg
 * 创建时间:2014-08-05 17:47:38
 */
@TableName("bpm_commu_receiver")
public class BpmCommuReceiver extends BaseModel<BpmCommuReceiver>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8890485916062173654L;
	public static String COMMU_NO="no";
	public static String COMMU_RECEIVE="receive";
	public static String COMMU_FEEDBACK="feedback";
	@TableId("id_")
	protected String  id; /*主键*/
	@TableField("commu_id_")
	protected String  commuId; /*通知ID*/
	@TableField("receiver_id_")
	protected String  receiverId; /*接收人ID*/
	@TableField("receiver")
	protected String  receiver; /*接收人*/
	@TableField("status_")
	protected String  status=COMMU_NO; /*状态*/
	@TableField("opinion_")
	protected String  opinion; /*反馈意见*/
	@TableField("receive_time_")
	protected LocalDateTime  receiveTime; /*接收时间*/
	@TableField("feedback_time_")
	protected LocalDateTime  feedbackTime; /*反馈时间*/
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
	public void setCommuId(String commuId) 
	{
		this.commuId = commuId;
	}
	/**
	 * 返回 通知ID
	 * @return
	 */
	public String getCommuId() 
	{
		return this.commuId;
	}
	public void setReceiverId(String receiverId) 
	{
		this.receiverId = receiverId;
	}
	/**
	 * 返回 接收人ID
	 * @return
	 */
	public String getReceiverId() 
	{
		return this.receiverId;
	}
	public void setReceiver(String receiver) 
	{
		this.receiver = receiver;
	}
	/**
	 * 返回 接收人
	 * @return
	 */
	public String getReceiver() 
	{
		return this.receiver;
	}
	public void setStatus(String status) 
	{
		this.status = status;
	}
	/**
	 * 返回 状态
	 * @return
	 */
	public String getStatus() 
	{
		return this.status;
	}
	public void setOpinion(String opinion) 
	{
		this.opinion = opinion;
	}
	/**
	 * 返回 反馈意见
	 * @return
	 */
	public String getOpinion() 
	{
		return this.opinion;
	}
	public void setReceiveTime(LocalDateTime receiveTime) 
	{
		this.receiveTime = receiveTime;
	}
	/**
	 * 返回 接收时间
	 * @return
	 */
	public LocalDateTime getReceiveTime() 
	{
		return this.receiveTime;
	}
	public void setFeedbackTime(LocalDateTime feedbackTime) 
	{
		this.feedbackTime = feedbackTime;
	}
	/**
	 * 返回 反馈时间
	 * @return
	 */
	public LocalDateTime getFeedbackTime() 
	{
		return this.feedbackTime;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("commuId", this.commuId) 
		.append("receiverId", this.receiverId) 
		.append("receiver", this.receiver) 
		.append("status", this.status) 
		.append("opinion", this.opinion) 
		.append("receiveTime", this.receiveTime) 
		.append("feedbackTime", this.feedbackTime) 
		.toString();
	}
}