package com.hotent.mail.model;



import java.time.LocalDateTime;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * 外部邮件最近联系 实体对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月6日
 */
@ApiModel(description="外部邮件最近联系 实体对象")
@TableName("portal_sys_mail_linkman")
public class MailLinkman extends BaseModel<MailLinkman>{
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(name="id", notes="主键")
	@TableId("ID")
	protected String id; 

	@ApiModelProperty(name="userId", notes="用户ID")
	@TableField("USERID")
	protected String userId; 
	
	@ApiModelProperty(name="mailId", notes="邮件ID")
	@TableField("MAILID")
	protected String mailId; 

	@ApiModelProperty(name="sendTime", notes="送送时间")
	@TableField("SENDTIME")
	protected LocalDateTime sendTime; 

	@ApiModelProperty(name="linkName", notes="联系人名称")
	@TableField("LINKNAME")
	protected String linkName; 
	
	@ApiModelProperty(name="phone", notes="联系人电话")
	@TableField("PHONE")
	protected String phone;
	
	@ApiModelProperty(name="linkAddress", notes="联系人地址")
	@TableField("LINKADDRESS")
	protected String linkAddress; 

	@ApiModelProperty(name="sendTimes", notes="发送次数")
	@TableField("SENDTIMES")
	protected Long sendTimes;
	
	@ApiModelProperty(name="org", notes="部门名称")
	@TableField(exist=false)
	protected String orgName; 
	
	
	/**
	 * 设置主键
	 * @param id 主键
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 主键
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * 设置用户id
	 * @param userId 用户id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	/**
	 * 返回 用户ID
	 * @return
	 */
	public String getUserId() {
		return this.userId;
	}
	
	/**
	 * 设置邮件id 
	 * @param mailId 邮件id
	 */
	public void setMailId(String mailId) {
		this.mailId = mailId;
	}
	
	/**
	 * 返回 邮件ID
	 * @return
	 */
	public String getMailId() {
		return this.mailId;
	}
	
	/**
	 * 设置发送时间
	 * @param sendTime 发送时间
	 */
	public void setSendTime(LocalDateTime sendTime) {
		this.sendTime = sendTime;
	}
	
	/**
	 * 返回 送送时间
	 * @return
	 */
	public LocalDateTime getSendTime() {
		return this.sendTime;
	}
	
	/**
	 * 设置联系人名称
	 * @param linkName 联系人名称
	 */
	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}
	
	/**
	 * 返回 联系人名称
	 * @return
	 */
	public String getLinkName() {
		return this.linkName;
	}
	
	/**
	 *设置联系人地址
	 * @param linkAddress 联系人地址
	 */
	public void setLinkAddress(String linkAddress) {
		this.linkAddress = linkAddress;
	}
	
	/**
	 * 返回 联系人地址
	 * @return
	 */
	public String getLinkAddress() {
		return this.linkAddress;
	}
	
	/**
	 * 设置发送次数
	 * @param sendTimes 发送次数
	 */
	public void setSendTimes(Long sendTimes) {
		this.sendTimes = sendTimes;
	}
	
	/**
	 * 返回 发送次数
	 * @return
	 */
	public Long getSendTimes() {
		return this.sendTimes;
	}

	public String getPhone() {
		return phone;
	}

	/**
	 * 返回联系电话
	 * @param phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getOrgName() {
		return orgName;
	}
	
	/**
	 * 部门名称
	 * @param orgName
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("LINKID", this.id) 
		.append("USERID", this.userId) 
		.append("MAILID", this.mailId) 
		.append("SENDTIME", this.sendTime) 
		.append("LINKNAME", this.linkName) 
		.append("LINKADDRESS", this.linkAddress) 
		.append("SENDTIMES", this.sendTimes)
		.append("PHONE",this.phone)
		.toString();
	}
}