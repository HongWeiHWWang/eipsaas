package com.hotent.mail.model;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hotent.base.entity.BaseModel;
import com.hotent.base.model.Tree;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 邮箱设置实体类
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月6日
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description="邮箱设置实体类")
@TableName(value="portal_sys_mail_setting",resultMap="MailSetting")
public class MailSetting extends BaseModel<MailSetting> implements Tree{
	private static final long serialVersionUID = 1L;
	
	public final static String SMTP_PROTOCAL = "smtp";
	public final static String POP3_PROTOCAL = "pop3";
	public final static String IMAP_PROTOCAL = "imap";
	
	public final static String EXCHANGE_MAIL_TYPE = "exchange";
	public final static String IMAP_MAIL_TYPE = "imap";
	public final static String POP3_MAIL_TYPE = "pop3";
	
	// 是
	public final static int ENABLE = 1 ;
	// 否
	public final static int DISABLE = 0 ;
	
	@ApiModelProperty(name="id", notes="主键")
	@TableId("ID_")
	protected String id; 
	
	@ApiModelProperty(name="sendHost", notes="邮件服务器的IP")
	@TableField("SEND_HOST_")
	protected String sendHost;
	
	@ApiModelProperty(name="sendPort", notes="邮件服务器的端口")
	@TableField("SEND_PORT_")
	protected String sendPort;

	@ApiModelProperty(name="receiveHost", notes="邮件接收服务器的IP")
	@TableField("RECEIVE_HOST_")
	protected String receiveHost;
	
	@ApiModelProperty(name="receivePort", notes="邮件接收服务器的端口")
	@TableField("RECEIVE_PORT_")
	protected String receivePort;
	
	@ApiModelProperty(name="protocal", notes="邮件接收服务器的协议")
	@TableField("PROTOCAL_")
	protected String protocal;

	@ApiModelProperty(name="smtpHost", notes="smt主机")
	@TableField("SMTP_HOST_")
	protected String smtpHost;
	
	@ApiModelProperty(name="smtpPort", notes="smt端口")
	@TableField("SMTP_PORT_")
	protected String smtpPort;
	
	@ApiModelProperty(name="popHost", notes="pop主机")
	@TableField("POP_HOST_")
	protected String popHost;
	
	@ApiModelProperty(name="popPort", notes="pop端口")
	@TableField("POP_PORT_")
	protected String popPort;
	
	@ApiModelProperty(name="imapHost", notes="imap主机")
	@TableField("IMAP_HOST_")
	protected String imapHost;
	
	@ApiModelProperty(name="imapPort", notes="imap端口")
	@TableField("IMAP_PORT_")
	protected String imapPort;

	@ApiModelProperty(name="SSL", notes="是否是SSL(true:是  false:不是)", allowableValues="true,false")
	@TableField("USE_SSL_")
	protected Boolean SSL = false ;

	@ApiModelProperty(name="validate", notes="是否需要身份验证(true:需要  false:不需要)", allowableValues="true,false")
	@TableField("IS_VALIDATE_")
	protected Boolean validate = true ;

	@ApiModelProperty(name="mailAddress", notes="登陆邮件服务器的用户名")
	@TableField("MAIL_ADDRESS_")
	protected String mailAddress;
	
	@ApiModelProperty(name="password", notes="登陆邮件服务器的密码")
	@TableField("MAIL_PASS_")
	protected String password;

	@ApiModelProperty(name="nickName", notes="用户昵称")
	@TableField("USER_NAME_")
	protected String nickName ;

	@ApiModelProperty(name="isHandleAttach", notes="是否收取附件(true:是  false:否)", allowableValues="true,false")
	@TableField("IS_HANDLE_ATTACH_")
	protected Boolean isHandleAttach = true;
	
	@ApiModelProperty(name="isDeleteRemote", notes="是否删除服务器上的邮件(true:删除  false:不删除)", allowableValues="true,false")
	@TableField("IS_DELETE_REMOTE_")
	protected Boolean isDeleteRemote = false ;
	
	@ApiModelProperty(name="userId", notes="用户ID")
	@TableField("USER_ID_")
	protected String userId; 
	
	@ApiModelProperty(name="isDefault", notes="是否默认()")
	@TableField("IS_DEFAULT_")
	protected Short isDefault; 

	@ApiModelProperty(name="mailType", notes="接收邮件服务器类型")
	@TableField("MAIL_TYPE_")
	protected String mailType; 
	
	@ApiModelProperty(name="lastMessageId", notes="上一条消息ID")
	@TableField("LAST_MESSAGE_ID_")
	protected String lastMessageId;
	
	@ApiModelProperty(name="lastReceiveTime", notes="上一次收件时间")
	@TableField("LAST_RECEIVE_TIME_")
	protected LocalDateTime lastReceiveTime;
	
	@ApiModelProperty(name="parentId", notes="父节点id")
	@TableField(exist=false)
	protected String parentId;
	
	@ApiModelProperty(name="isParent", notes="是否父类")
	@TableField(exist=false)
	protected String isParent;

	@ApiModelProperty(name="isLeaf", notes="是否叶子结点,主要用于数据库保存(0:否  1:是)", allowableValues="0,1")
	@TableField(exist=false)
	protected Integer isLeaf;
	
	@ApiModelProperty(name="open", notes="是否打开(true:打开  false:不打开)", allowableValues="true,false")
	@TableField(exist=false)
	protected String open="true";

	@ApiModelProperty(name="types", notes="邮件类型(1:收件箱  2:发件箱  3:草稿箱  4:垃圾箱)", allowableValues="1,2,3,4")
	@TableField(exist=false)
	protected Integer types;
	
	@TableField(exist=false)
	protected List<MailSetting> children=new ArrayList<>();
	
	/**
	 * 获取主键id
	 * @return
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * 设置主键
	 * @param id 主键
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 获取SSL
	 * @return
	 */
	public Boolean getSSL() {
		return SSL;
	}
	
	/**
	 * 设置SSL
	 * @param SSL SSL参数
	 */
	public void setSSL(Boolean SSL) {
		this.SSL = SSL;
	}
	
	/**
	 * 获取身份验证
	 * @return
	 */
	public Boolean getValidate() {
		return validate;
	}
	
	/**
	 * 设置身份验证
	 * @param validate 身份验证
	 */
	public void setValidate(Boolean validate) {
		this.validate = validate;
	}
	
	/**
	 * 获取邮件服务器的用户名
	 * @return
	 */
	public String getMailAddress() {
		return mailAddress;
	}
	
	/**
	 * 设置登陆邮件服务器的用户名
	 * @param mailAddress 邮件服务器用户名
	 */
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}
	
	/**
	 * 获取登陆邮件服务器的密码
	 * @return
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * 设置登陆邮件服务器的密码
	 * @param password 登陆邮件服务器密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * 获取邮件接收服务器协议
	 * @return
	 */
	public String getProtocal() {
		return protocal;
	}
	
	/**
	 * 设置邮件接收服务器协议
	 * @param protocal 邮件协议
	 */
	public void setProtocal(String protocal) {
		this.protocal = protocal;
	}
	
	/**
	 * 获取邮件接收服务器的IP
	 * @return
	 */
	public String getReceiveHost() {
		return receiveHost;
	}
	
	/**
	 * 设置邮件接收服务器的IP
	 * @param receiveHost 邮件接收服务器的IP
	 */
	public void setReceiveHost(String receiveHost) {
		this.receiveHost = receiveHost;
	}
	
	/**
	 * 获取邮件接收服务器的端口
	 * @return
	 */
	public String getReceivePort() {
		return receivePort;
	}
	
	/**
	 * 设置邮件接收服务器的端口
	 * @param receivePort 邮件接收服务器的端口
	 */
	public void setReceivePort(String receivePort) {
		this.receivePort = receivePort;
	}
	
	/**
	 * 获取是否收取附件
	 * @return
	 */
	public Boolean getIsHandleAttach() {
		return isHandleAttach;
	}
	
	/**
	 * 设置是否收取附件
	 * @param isHandleAttach 是否收取附件
	 */
	public void setIsHandleAttach(Boolean isHandleAttach) {
		this.isHandleAttach = isHandleAttach;
	}
	
	/**
	 * 获取邮件服务器的IP
	 * @return
	 */
	public String getSendHost() {
		return sendHost;
	}
	
	/**
	 * 设置邮件服务器的IP
	 * @param sendHost 邮件服务器的IP
	 */
	public void setSendHost(String sendHost) {
		this.sendHost = sendHost;
	}
	
	/**
	 * 获取邮件服务器的端口
	 * @return
	 */
	public String getSendPort() {
		return sendPort;
	}
	
	/**
	 * 设置邮件服务器的端口
	 * @param sendPort 邮件服务器的端口
	 */
	public void setSendPort(String sendPort) {
		this.sendPort = sendPort;
	}
	
	/**
	 * 获取是否删除服务器上的邮件
	 * @return
	 */
	public Boolean getIsDeleteRemote() {
		return isDeleteRemote;
	}
	
	/**
	 * 设置是否删除服务器上的邮件
	 * @param isDeleteRemote 是否删除服务器上的邮件
	 */
	public void setIsDeleteRemote(Boolean isDeleteRemote) {
		this.isDeleteRemote = isDeleteRemote;
	}
	
	/**
	 * 获取用户昵称
	 * @return
	 */
	public String getNickName() {
		return nickName;
	}
	
	/**
	 * 设置用户昵称
	 * @param nickName  用户昵称
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	/**
	 * 获取用户ID
	 * @return
	 */
	public String getUserId() {
		return userId;
	}
	
	/**
	 * 设置用户ID
	 * @param userId 用户ID
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	/**
	 * 获取是否默认
	 * @return
	 */
	public Short getIsDefault() {
		return isDefault;
	}
	
	/**
	 * 设置是否默认
	 * @param isDefault 是否默认
	 */
	public void setIsDefault(Short isDefault) {
		this.isDefault = isDefault;
	}
	
	/**
	 * 获取接收邮件服务器类型
	 * @return
	 */
	public String getMailType() {
		return mailType;
	}
	
	/**
	 * 设置接收邮件服务器类型
	 * @param mailType 接收邮件服务器类型
	 */
	public void setMailType(String mailType) {
		this.mailType = mailType;
	}
	
	/**
	 * 获取smtp主机
	 * @return
	 */
	public String getSmtpHost() {
		return smtpHost;
	}
	
	/**
	 * 设置smtp主机
	 * @param smtpHost smtp主机
	 */
	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}
	
	/**
	 * 获取smtp端口
	 * @return
	 */
	public String getSmtpPort() {
		return smtpPort;
	}
	
	/**
	 * 设置smtp端口
	 * @param smtpPort smtp端口
	 */
	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}
	
	/**
	 * 获取pop主机
	 * @return
	 */
	public String getPopHost() {
		return popHost;
	}
	
	/**
	 * 设置pop主机
	 * @param popHost pop主机
	 */
	public void setPopHost(String popHost) {
		this.popHost = popHost;
	}
	
	/**
	 * 获取pop端口
	 * @return
	 */
	public String getPopPort() {
		return popPort;
	}
	
	/**
	 * 设置pop端口
	 * @param popPort  pop端口
	 */
	public void setPopPort(String popPort) {
		this.popPort = popPort;
	}
	
	/**
	 * 获取imap主机
	 * @return
	 */
	public String getImapHost() {
		return imapHost;
	}
	
	/**
	 * 设置imap主机
	 * @param imapHost imap主机
	 */
	public void setImapHost(String imapHost) {
		this.imapHost = imapHost;
	}
	
	/**
	 * 获取imap端口
	 * @return
	 */
	public String getImapPort() {
		return imapPort;
	}
	
	/**
	 * 设置imap端口
	 * @param imapPort imap端口
	 */
	public void setImapPort(String imapPort) {
		this.imapPort = imapPort;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getParentId() {
		return parentId;
	}
	
	/**
	 * 
	 * @param parentId
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	/**
	 * 获取是否父类
	 * @return
	 */
	public String getIsParent() {
		return isParent;
	}
	
	/**
	 * 设置是否父类
	 * @param isParent 是否父类
	 */
	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}
	
	/**
	 * 获取是否叶子结点
	 * @return
	 */
	public Integer getIsLeaf() {
		return isLeaf;
	}
	
	/**
	 * 设置是否叶子结点
	 * @param isLeaf 是否叶子结点
	 */
	public void setIsLeaf(Integer isLeaf) {
		this.isLeaf = isLeaf;
	}
	
	/**
	 * 获取是否打开
	 * @return
	 */
	public String getOpen() {
		return open;
	}
	
	/**
	 * 设置是否打开
	 * @param open 是否打开
	 */
	public void setOpen(String open) {
		this.open = open;
	}
	
	/**
	 * 获取邮件类型
	 * @return
	 */
	public Integer getTypes() {
		return types;
	}
	
	/**
	 * 设置邮件类型
	 * @param types 邮件类型
	 */
	public void setTypes(Integer types) {
		this.types = types;
	}
	
	public String getLastMessageId() {
		return lastMessageId;
	}

	public void setLastMessageId(String lastMessageId) {
		this.lastMessageId = lastMessageId;
	}

	public LocalDateTime getLastReceiveTime() {
		return lastReceiveTime;
	}

	public void setLastReceiveTime(LocalDateTime lastReceiveTime) {
		this.lastReceiveTime = lastReceiveTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((SSL == null) ? 0 : SSL.hashCode());
		result = prime * result
				+ ((isDeleteRemote == null) ? 0 : isDeleteRemote.hashCode());
		result = prime * result
				+ ((isHandleAttach == null) ? 0 : isHandleAttach.hashCode());
		result = prime * result
				+ ((mailAddress == null) ? 0 : mailAddress.hashCode());
		result = prime * result
				+ ((nickName == null) ? 0 : nickName.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((protocal == null) ? 0 : protocal.hashCode());
		result = prime * result
				+ ((receiveHost == null) ? 0 : receiveHost.hashCode());
		result = prime * result
				+ ((receivePort == null) ? 0 : receivePort.hashCode());
		result = prime * result
				+ ((sendHost == null) ? 0 : sendHost.hashCode());
		result = prime * result
				+ ((sendPort == null) ? 0 : sendPort.hashCode());
		result = prime * result
				+ ((smtpHost == null) ? 0 : smtpHost.hashCode());
		result = prime * result
				+ ((smtpPort == null) ? 0 : smtpPort.hashCode());
		result = prime * result
				+ ((popHost == null) ? 0 : popHost.hashCode());
		result = prime * result
				+ ((popPort == null) ? 0 : popPort.hashCode());
		result = prime * result
				+ ((imapHost == null) ? 0 : imapHost.hashCode());
		result = prime * result
				+ ((imapPort == null) ? 0 : imapPort.hashCode());
		result = prime * result
				+ ((validate == null) ? 0 : validate.hashCode());
		result = prime * result
				+ ((userId == null) ? 0 : userId.hashCode());
		result = prime * result
				+ ((isDefault == null) ? 0 : isDefault.hashCode());
		result = prime * result
				+ ((mailType == null) ? 0 : mailType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof MailSetting))
			return false;
		MailSetting other = (MailSetting) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (SSL == null) {
			if (other.SSL != null)
				return false;
		} else if (!SSL.equals(other.SSL))
			return false;
		if (isDeleteRemote == null) {
			if (other.isDeleteRemote != null)
				return false;
		} else if (!isDeleteRemote.equals(other.isDeleteRemote))
			return false;
		if (isHandleAttach == null) {
			if (other.isHandleAttach != null)
				return false;
		} else if (!isHandleAttach.equals(other.isHandleAttach))
			return false;
		if (mailAddress == null) {
			if (other.mailAddress != null)
				return false;
		} else if (!mailAddress.equals(other.mailAddress))
			return false;
		if (nickName == null) {
			if (other.nickName != null)
				return false;
		} else if (!nickName.equals(other.nickName))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (protocal == null) {
			if (other.protocal != null)
				return false;
		} else if (!protocal.equals(other.protocal))
			return false;
		if (receiveHost == null) {
			if (other.receiveHost != null)
				return false;
		} else if (!receiveHost.equals(other.receiveHost))
			return false;
		if (receivePort == null) {
			if (other.receivePort != null)
				return false;
		} else if (!receivePort.equals(other.receivePort))
			return false;
		if (sendHost == null) {
			if (other.sendHost != null)
				return false;
		} else if (!sendHost.equals(other.sendHost))
			return false;
		if (sendPort == null) {
			if (other.sendPort != null)
				return false;
		} else if (!sendPort.equals(other.sendPort))
			return false;
		if (imapHost == null) {
			if (other.imapHost != null)
				return false;
		} else if (!imapHost.equals(other.imapHost))
			return false;
		if (imapPort == null) {
			if (other.imapPort != null)
				return false;
		} else if (!imapPort.equals(other.imapPort))
			return false;
		
		if (smtpHost == null) {
			if (other.smtpHost != null)
				return false;
		} else if (!smtpHost.equals(other.smtpHost))
			return false;
		if (smtpPort == null) {
			if (other.smtpPort != null)
				return false;
		} else if (!smtpPort.equals(other.smtpPort))
			return false;
		if (popHost == null) {
			if (other.popHost != null)
				return false;
		} else if (!popHost.equals(other.popHost))
			return false;
		if (popPort == null) {
			if (other.popPort != null)
				return false;
		} else if (!popPort.equals(other.popPort))
			return false;
		if (validate == null) {
			if (other.validate != null)
				return false;
		} else if (!validate.equals(other.validate))
			return false;
		
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (isDefault == null) {
			if (other.isDefault != null)
				return false;
		} else if (!isDefault.equals(other.isDefault))
			return false;
		if (mailType == null) {
			if (other.mailType != null)
				return false;
		} else if (!mailType.equals(other.mailType))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MailSeting [id=" + id + ",sendHost=" + sendHost + ", sendPort=" + sendPort
				+ ", receiveHost=" + receiveHost + ", receivePort="
				+ receivePort+ ", imapHost=" + imapHost + ", imapPort="
				+ imapPort+ ", smtpHost=" + smtpHost + ", smtpPort="
				+ smtpPort+ ", popHost=" + popHost + ", popPort="
				+ popPort + ", protocal=" + protocal + ", SSL=" + SSL
				+ ", validate=" + validate + ", mailAddress=" + mailAddress
				+ ", password=" + password + ", nickName=" + nickName
				+ ", isHandleAttach=" + isHandleAttach + ", isDeleteRemote="
				+ isDeleteRemote + ",userId=" + userId + ",isDefault=" + isDefault + ",mailType=" + mailType + "]";
	}

	@Override
	public String getText() {
		return null;
	}

	@Override
	public List getChildren() {
		return this.children;
	}

	@Override
	public void setChildren(List children) {
		this.children=children;
	}
}