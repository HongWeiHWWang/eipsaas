package com.hotent.portal.model;


import java.time.LocalDateTime;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.AutoFillModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 系统信息实体对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author hugh
 * @email zxh@jee-soft.cn
 * @date 2014-11-18 09:03:31
 */
@ApiModel(description="系统信息实体对象")
@TableName("portal_sys_msg")
public class SysMessage extends AutoFillModel<SysMessage>{

	private static final long serialVersionUID = 1L;
	
	public final static Short iS_REPLY_YES = 1; //可以回复
	public final static Short iS_REPLY_NO= 0;   //不可以回复
	
	
	public final static Short iS_PUBLIC_YES= 1;   //是公告
	public final static Short iS_PUBLIC_NO= 0;   //不是公告
	
	public final static String TYPE_BULLETIN="bulletin";//公告类型
	
	@ApiModelProperty(name="id", notes="主键")
	@TableId("id_")
	protected String id;
	
	@ApiModelProperty(name="subject", notes="主题")
	@TableField("subject_")
	protected String subject;
	
	@ApiModelProperty(name="ownerId", notes="发帖人ID")
	@TableField("owner_id_")
	protected String ownerId;
	
	@ApiModelProperty(name="owner", notes="发帖人")
	@TableField("owner_")
	protected String owner; 
	
	@ApiModelProperty(name="messageType", notes="消息类型")
	@TableField("message_type_")
	protected String messageType; 
	
	@ApiModelProperty(name="canReply", notes="是否可以回复")
	@TableField("can_reply_")
	protected Short canReply; 
	
	@ApiModelProperty(name="isPublic", notes="是否公告")
	@TableField("is_public_")
	protected Short isPublic;
	
	@ApiModelProperty(name="content", notes="内容")
	@TableField("content_")
	protected String content;
	
	@ApiModelProperty(name="fileMsg", notes="附件信息")
	@TableField("file_msg_")
	protected String fileMsg;
	
	@ApiModelProperty(name="receiverName", notes="接收人名称")
	@TableField("receiver_name_")
	protected String receiverName;
	
	/*非数据库字段*/
	@ApiModelProperty(name="receiveTime", notes="阅读时间")
	@TableField(exist = false)
	protected LocalDateTime receiveTime;
	
	@ApiModelProperty(name="receiverId", notes="接收人id")
	@TableField(exist = false)
	protected String receiverId;
	
	@ApiModelProperty(name="receiverOrgName", notes="接受组织名称")
	@TableField(exist = false)
	protected String receiverOrgName;
	
	@ApiModelProperty(name="receiverOrgId", notes="接受组织Id")
	@TableField(exist = false)
	protected String receiverOrgId;
	
	@ApiModelProperty(name="rid", notes="收信id")
	@TableField(exist = false)
	protected String rid;
	
	@ApiModelProperty(name="detailUrl", notes="详情地址")
	@TableField(exist = false)
	protected String detailUrl;	
	
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
	 * 设置主题
	 * @param subject 主题
	 */
	public void setSubject(String subject) 
	{
		this.subject = subject;
	}
	
	/**
	 * 返回 主题
	 * @return
	 */
	public String getSubject() 
	{
		return this.subject;
	}
	
	/**
	 * 设置发帖人ID
	 * @param ownerId 发帖人ID
	 */
	public void setOwnerId(String ownerId) 
	{
		this.ownerId = ownerId;
	}
	
	/**
	 * 返回 发帖人ID
	 * @return
	 */
	public String getOwnerId() 
	{
		return this.ownerId;
	}
	
	/**
	 * 设置 发帖人
	 * @param owner  发帖人
	 */
	public void setOwner(String owner) 
	{
		this.owner = owner;
	}
	
	/**
	 * 返回 发帖人
	 * @return
	 */
	public String getOwner() 
	{
		return this.owner;
	}
	
	/**
	 * 设置消息类型
	 * @param messageType 消息类型
	 */
	public void setMessageType(String messageType) 
	{
		this.messageType = messageType;
	}
	
	/**
	 * 返回 消息类型
	 * @return
	 */
	public String getMessageType() 
	{
		return this.messageType;
	}
	
	/**
	 * 设置 是否可以回复
	 * @param canReply 是否可以回复
	 */
	public void setCanReply(Short canReply) 
	{
		this.canReply = canReply;
	}
	
	/**
	 * 返回 是否可以回复
	 * @return
	 */
	public Short getCanReply() 
	{
		return this.canReply;
	}
	
	/**
	 * 设置 是否公告
	 * @param isPublic 是否公告
	 */
	public void setIsPublic(Short isPublic) 
	{
		this.isPublic = isPublic;
	}
	
	/**
	 * 返回 是否公告
	 * @return
	 */
	public Short getIsPublic() 
	{
		return this.isPublic;
	}
	
	/**
	 * 设置 内容
	 * @param content 内容
	 */
	public void setContent(String content) 
	{
		this.content = content;
	}
	
	/**
	 * 返回 内容
	 * @return
	 */
	public String getContent() 
	{
		return this.content;
	}
	
	/**
	 * 设置 附件信息
	 * @param fileMsg 附件信息
	 */
	public void setFileMsg(String fileMsg) 
	{
		this.fileMsg = fileMsg;
	}
	
	/**
	 * 返回 附件信息
	 * @return
	 */
	public String getFileMsg() 
	{
		return this.fileMsg;
	}
	
	/**
	 * 返回阅读时间
	 * @return 
	 */
	public LocalDateTime getReceiveTime() {
		return receiveTime;
	}
	
	/**
	 * 设置阅读时间
	 * @param receiveTime 阅读时间
	 */
	public void setReceiveTime(LocalDateTime receiveTime) {
		this.receiveTime = receiveTime;
	}
	
	/**
	 * 返回接收人名称
	 * @return
	 */
	public String getReceiverName() {
		return receiverName;
	}
	
	/**
	 * 设置接收人名称
	 * @param receiverName 接收人名称
	 */
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	
	/**
	 * 返回接受组织名称
	 * @return
	 */
	public String getReceiverOrgName() {
		return receiverOrgName;
	}
	
	/**
	 * 设置接受组织名称
	 * @param receiverOrgName 接受组织名称
	 */
	public void setReceiverOrgName(String receiverOrgName) {
		this.receiverOrgName = receiverOrgName;
	}
	
	/**
	 * 返回接收人id
	 * @return
	 */
	public String getReceiverId() {
		return receiverId;
	}
	
	/**
	 * 设置接收人id
	 * @param receiverId 接收人id
	 */
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	
	/**
	 * 返回接受组织Id
	 * @return
	 */
	public String getReceiverOrgId() {
		return receiverOrgId;
	}
	
	/**
	 * 设置接受组织Id
	 * @param receiverOrgId 接受组织Id
	 */
	public void setReceiverOrgId(String receiverOrgId) {
		this.receiverOrgId = receiverOrgId;
	}
	
	/**
	 * 返回收信id
	 * @return
	 */
	public String getRid() {
		return rid;
	}
	
	/**
	 * 设置收信id
	 * @param rid 收信id
	 */
	public void setRid(String rid) {
		this.rid = rid;
	}
	
	/**
	 * 返回详情地址
	 * @return
	 */
	public String getDetailUrl() {
		return detailUrl;
	}
	
	/**
	 * 设置详情地址
	 * @param detailUrl 详情地址
	 */
	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("subject", this.subject) 
		.append("ownerId", this.ownerId) 
		.append("owner", this.owner) 
		.append("messageType", this.messageType)  
		.append("canReply", this.canReply) 
		.append("isPublic", this.isPublic) 
		.append("content", this.content) 
		.append("fileMsg", this.fileMsg) 
		.toString();
	}
}