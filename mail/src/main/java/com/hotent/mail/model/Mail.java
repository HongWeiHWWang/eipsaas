package com.hotent.mail.model;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 邮件实体类
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月6日
 */
@ApiModel(description="邮件实体类")
@TableName("portal_sys_mail")
public class Mail extends BaseModel<Mail>{
	private static final long serialVersionUID = 1L;
	//已读
	public static Short Mail_IsRead=1;
	//未读
	public static Short Mail_IsNotRead=0;
	//已回复
	public static Short Mail_IsReplay=1;
	//未回复
	public static Short Mail_IsNotReplay=0;
	/**
	 * 收件箱
	 */
	public static Short Mail_InBox=1;
	/**
	 * 发件箱
	 */
	public static Short Mail_OutBox=2;
	/**
	 * 草稿箱
	 */
	public static Short Mail_DraftBox=3;
	/**
	 * 垃圾箱
	 */
	public static Short Mail_DumpBox=4;
	
	@ApiModelProperty(name="id", notes="主键")
	@TableId("ID_")
	protected String id; 
	
	@ApiModelProperty(name="subject", notes="邮件主题")
	@TableField("TITLE_")
	protected String subject;

	@ApiModelProperty(name="content", notes="邮件内容")
	@TableField("CONTENT_")
	protected String content;

	@ApiModelProperty(name="senderAddress", notes="发件人地址")
	@TableField("SENDER_ADDRESSES_")
	protected String senderAddress;

	@ApiModelProperty(name="senderName", notes="发件人地址别名")
	@TableField("SENDER_NAME_")
	protected String senderName ;

	@ApiModelProperty(name="receiverAddresses", notes="收件人地址")
	@TableField("RECEIVER_ADDRESSES_")
	protected String receiverAddresses;

	@ApiModelProperty(name="receiverName", notes="收件人地址别名")
	@TableField("RECEIVER_NAMES_")
	protected String receiverName ;
	
	@ApiModelProperty(name="copyToAddresses", notes="抄送人地址")
	@TableField("CC_ADDRESSES_")
	protected String copyToAddresses;

	@ApiModelProperty(name="copyToName", notes="抄送人别名")
	@TableField("CC_NAMES_")
	protected String copyToName ;

	@ApiModelProperty(name="bccName", notes="暗送人显示名")
	@TableField("BCC_NAMES_")
	protected String bccName ;

	@ApiModelProperty(name="bcCAddresses", notes="暗送人地址")
	@TableField("BCC_ADDRESSES_")
	protected String bcCAddresses;

	@ApiModelProperty(name="messageId", notes="每种邮箱中，邮件的唯一ID")
	@TableField("MESSAGE_ID_")
	protected String messageId ;
	
	@ApiModelProperty(name="type", notes="邮件类型(1:收件箱  2:发件箱  3:草稿箱  4:垃圾箱)", allowableValues="0,1,2,3,4")
	@TableField("TYPE_")
	protected Short type; 
	
	@ApiModelProperty(name="userId", notes="用户ID")
	@TableField("USER_ID_")
	protected String userId; 
	
	@ApiModelProperty(name="isReply", notes="是否回复(0:未回复  1:已回复)", allowableValues="0,1")
	@TableField("IS_REPLY_")
	protected Short isReply; 

	@ApiModelProperty(name="sendDate", notes="发送时间")
	@TableField("SEND_DATE_")
	protected LocalDateTime sendDate ;
	
	@ApiModelProperty(name="fileIds", notes="附件ID")
	@TableField("FILE_IDS_")
	protected String fileIds; 
	
	@ApiModelProperty(name="isRead", notes="是否已读(0:未读  1:已读)")
	@TableField("IS_READ_")
	protected Short isRead; 
	
	@ApiModelProperty(name="setId", notes="SET_ID_")
	@TableField("SET_ID_")
	protected String setId; 
	
	/**
	 * 根据发件人邮箱地址是否存在邮箱联系人中(0:存在，1：未存在)
	 */
	@TableField(exist = false)
	protected String isExitedMan;
	
	/**
	 * 邮件附件
	 */
	@TableField(exist = false)
	protected List<MailAttachment> attachments = new ArrayList<MailAttachment>();
	
	// 首页工具使用
	@TableField(exist = false)
	protected String detailUrl;
	
	public String getUID() {
		return messageId;
	}
	public void setUID(String uID) {
		messageId = uID;
	}
	
	/**
	 * 获取邮件发送地址
	 * @return
	 */
	public String getSenderAddress() {
		return senderAddress;
	}
	
	/**
	 * 设置邮件发送地址
	 * @param senderAddress  邮件发送地址
	 */
	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}
	
	/**
	 * 获取邮件主题
	 * @return
	 */
	public String getSubject() {
		return subject;
	}
	
	/**
	 * 设置邮件主题
	 * @param subject  邮件主题
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	/**
	 * 获取邮件主体内容
	 * @return
	 */
	public String getContent() {
		return content;
	}
	
	/**
	 * 设置邮件主体内容
	 * @param content  邮件主体内容
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * 获取邮件暗送人地址
	 * @return
	 */
	public String getBcCAddresses() {
		return bcCAddresses;
	}
	
	/**
	 * 设置邮件暗送人地址
	 * @param bcCAddresses  邮件暗送人地址
	 */
	public void setBcCAddresses(String bcCAddresses) {
		this.bcCAddresses = bcCAddresses;
	}
	
	/**
	 * 获取邮件附件
	 * @return
	 */
	public List<MailAttachment> getMailAttachments() {
		return attachments;
	}
	
	/**
	 * 设置邮件附件
	 * @param attachments  附件
	 */
	public void setMailAttachments(List<MailAttachment> attachments) {
		this.attachments = attachments;
	}
	
	/**
	 * 获取发送时间
	 * @return
	 */
	public LocalDateTime getSendDate() {
		return sendDate;
	}
	
	/**
	 * 设置发送时间
	 * @param sendDate  发送时间
	 */
	public void setSendDate(LocalDateTime sendDate) {
		this.sendDate = sendDate;
	}
	
	/**
	 * 获取发件人地址别名
	 * @return
	 */
	public String getSenderName() {
		return senderName;
	}
	
	/**
	 * 设置发件人地址别名
	 * @param senderName  发件人地址别名
	 */
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	
	/**
	 * 获取收件人地址别名
	 * @return
	 */
	public String getReceiverName() {
		return receiverName;
	}
	
	/**
	 * 设置收件人地址别名
	 * @param receiverName  收件人地址别名
	 */
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	
	/**
	 * 获取抄送人地址别名
	 * @return
	 */
	public String getCopyToName() {
		return copyToName;
	}
	
	/**
	 * 设置抄送人地址别名
	 * @param copyToName  抄送人地址别名
	 */
	public void setCopyToName(String copyToName) {
		this.copyToName = copyToName;
	}
	
	/**
	 * 获取暗送人显示名
	 * @return
	 */
	public String getBccName() {
		return bccName;
	}
	
	/**
	 * 设置暗送人显示名
	 * @param bccName  暗送人显示名
	 */
	public void setBccName(String bccName) {
		this.bccName = bccName;
	}
	
	/**
	 * 获取抄送人地址
	 * @return
	 */
	public String getCopyToAddresses() {
		return copyToAddresses;
	}
	
	/**
	 * 设置抄送人地址
	 * @param copyToAddresses  抄送人地址
	 */
	public void setCopyToAddresses(String copyToAddresses) {
		this.copyToAddresses = copyToAddresses;
	}
	
	/**
	 * 获取收件人地址
	 * @return
	 */
	public String getReceiverAddresses() {
		return receiverAddresses;
	}
	
	/**
	 * 设置收件人地址
	 * @param receiverAddresses  收件人地址
	 */
	public void setReceiverAddresses(String receiverAddresses) {
		this.receiverAddresses = receiverAddresses;
	}
	
	/**
	 * 获取每种邮箱中，邮件的唯一ID
	 * @return
	 */
	public String getMessageId() {
		return messageId;
	}
	
	/**
	 * 设置每种邮箱中，邮件的唯一ID
	 * @param messageId  每种邮箱中，邮件的唯一ID
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	
	/**
	 * 获取主键id
	 * @return
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * 设置主键id
	 * @param id  主键id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 获取邮箱类型
	 * @return
	 */
	public Short getType() {
		return type;
	}
	
	/**
	 * 设置邮箱类型
	 * @param type  类型
	 */
	public void setType(Short type) {
		this.type = type;
	}
	
	/**
	 * 获取用户id
	 * @return
	 */
	public String getUserId() {
		return userId;
	}
	
	/**
	 * 设置用户id
	 * @param userId 用户id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	/**
	 * 获取是否回复
	 * @return
	 */
	public Short getIsReply() {
		return isReply;
	}
	
	/**
	 * 设置是否回复
	 * @param isReply  是否回复
	 */
	public void setIsReply(Short isReply) {
		this.isReply = isReply;
	}
	
	/**
	 * 获取附件id
	 * @return
	 */
	public String getFileIds() {
		return fileIds;
	}
	
	/**
	 * 设置附件id
	 * @param fileIds  附件id
	 */
	public void setFileIds(String fileIds) {
		this.fileIds = fileIds;
	}
	
	/**
	 * 获取是否已读
	 * @return
	 */
	public Short getIsRead() {
		return isRead;
	}
	
	/**
	 * 设置是否已读
	 * @param isRead
	 */
	public void setIsRead(Short isRead) {
		this.isRead = isRead;
	}
	
	/**
	 * 获取SET_ID_
	 * @return
	 */
	public String getSetId() {
		return setId;
	}
	
	/**
	 * 设置SET_ID_
	 * @param setId
	 */
	public void setSetId(String setId) {
		this.setId = setId;
	}
	
	/**
	 * 获取首页工具使用
	 * @return
	 */
	public String getDetailUrl() {
		return detailUrl;
	}
	
	/**
	 * 设置主页工具使用
	 * @param detailUrl  主页工具使用
	 */
	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}
	
	/**
	 * 获取是否存在于邮箱联系人列表
	 * @return
	 */
	public String getIsExitedMan() {
		return isExitedMan;
	}
	
	/**
	 * 设置是否存在于邮箱联系人列表
	 * @param isExitedMan
	 */
	public void setIsExitedMan(String isExitedMan) {
		this.isExitedMan = isExitedMan;
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.id) 
		.append(this.subject) 
		.append(this.content) 
		.append(this.senderAddress) 
		.append(this.senderName) 
		.append(this.receiverAddresses) 
		.append(this.receiverName) 
		.append(this.copyToAddresses) 
		.append(this.copyToName) 
		.append(this.bcCAddresses) 
		.append(this.bccName) 
		.append(this.sendDate) 
		.append(this.fileIds) 
		.append(this.isRead) 
		.append(this.isReply) 
		.append(this.messageId) 
		.append(this.type) 
		.append(this.userId)
		.toHashCode();
	}
	
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("subject", this.subject) 
		.append("content", this.content) 
		.append("senderAddress", this.senderAddress) 
		.append("senderName", this.senderName) 
		.append("receiverAddresses", this.receiverAddresses) 
		.append("receiverName", this.receiverName) 
		.append("copyToAddresses", this.copyToAddresses) 
		.append("bccName", this.bccName) 
		.append("bcCAddresses", this.bcCAddresses) 
		.append("copyToName", this.copyToName) 
		.append("messageId", this.messageId) 
		.append("type", this.type) 
		.append("userId", this.userId) 
		.append("isReply", this.isReply) 
		.append("sendDate", this.sendDate) 
		.append("fileIds", this.fileIds) 
		.append("isRead", this.isRead) 
		.append("setId", this.setId) 
		.toString();
	}
	
	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof Mail)) 
		{
			return false;
		}
		Mail rhs = (Mail) object;
		return new EqualsBuilder()
		.append(this.id, rhs.id)
		.append(this.subject, rhs.subject)
		.append(this.content, rhs.content)
		.append(this.senderAddress, rhs.senderAddress)
		.append(this.senderName, rhs.senderName)
		.append(this.receiverAddresses, rhs.receiverAddresses)
		.append(this.receiverName, rhs.receiverName)
		.append(this.copyToAddresses, rhs.copyToAddresses)
		.append(this.copyToName, rhs.copyToName)
		.append(this.bcCAddresses, rhs.bcCAddresses)
		.append(this.bccName, rhs.bccName)
		.append(this.sendDate, rhs.sendDate)
		.append(this.fileIds, rhs.fileIds)
		.append(this.isRead, rhs.isRead)
		.append(this.isReply, rhs.isReply)
		.append(this.messageId, rhs.messageId)
		.append(this.type, rhs.type)
		.append(this.userId, rhs.userId)
		.isEquals();
	}

	
	
}