package com.hotent.bpm.persistence.model;
import java.time.LocalDateTime;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

/**
 * 对象功能:抄送接收人 entity对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zyg
 * 创建时间:2014-10-13 22:24:23
 */
@TableName("bpm_cpto_receiver")
public class BpmCptoReceiver extends BaseModel<BpmCptoReceiver>{

	private static final long serialVersionUID = 6100940225730158847L;
	@TableId("id_")
	protected String  id; /*主键*/
	@TableField("cpto_id_")
	protected String  cptoId; /*关联ID*/
	@TableField("receiver_id_")
	protected String  receiverId; /*接收人ID*/
	@TableField("receiver_")
	protected String  receiver; /*接收人*/
	@TableField("is_read_")
	protected Short  isRead=0; /*已读*/
	@TableField("read_time_")
	protected LocalDateTime  readTime; /*读取时间*/
	@TableField("create_time_")
	protected LocalDateTime createTime = LocalDateTime.now();

	
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
	public void setCptoId(String cptoId) 
	{
		this.cptoId = cptoId;
	}
	/**
	 * 返回 关联ID
	 * @return
	 */
	public String getCptoId() 
	{
		return this.cptoId;
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
	public void setIsRead(Short isRead) 
	{
		this.isRead = isRead;
	}
	/**
	 * 返回 已读
	 * @return
	 */
	public Short getIsRead() 
	{
		return this.isRead;
	}
	public void setReadTime(LocalDateTime readTime) 
	{
		this.readTime = readTime;
	}
	/**
	 * 返回 读取时间
	 * @return
	 */
	public LocalDateTime getReadTime() 
	{
		return this.readTime;
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
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("cptoId", this.cptoId) 
		.append("receiverId", this.receiverId) 
		.append("receiver", this.receiver) 
		.append("isRead", this.isRead) 
		.append("readTime", this.readTime) 
		.toString();
	}
}