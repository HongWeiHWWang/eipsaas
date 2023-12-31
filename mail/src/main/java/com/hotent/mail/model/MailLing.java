package com.hotent.mail.model;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 邮件实体 外
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月15日
 */
@ApiModel(description = "邮件实体")
public class MailLing {

	@ApiModelProperty(name = "to", notes = "收件人")
	String to;

	@ApiModelProperty(name = "cc", notes = "抄送人")
	String cc;

	@ApiModelProperty(name = "bcc", notes = "密送人")
	String bcc;

	@ApiModelProperty(name = "from", notes = "发件人")
	String from;

	@ApiModelProperty(name = "subject", notes = "主题")
	String subject;

	@ApiModelProperty(name = "content", notes = "内容")
	String content;

	@ApiModelProperty(name = "attachments", notes = "邮件附件")
	List<MailAtt> attachments;

	public MailLing(String to, String cc, String bcc, String from, String subject, String content) {
		this(to, cc, bcc, from, subject, content, null);
	}
	
	
	/**
	 * 构造无参对象
	 */
	public MailLing() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 邮件实体 外
	 * 
	 * @param to
	 *            收件人
	 * @param cc
	 *            抄送人
	 * @param bcc
	 *            密送人
	 * @param from
	 *            发件人
	 * @param subject
	 *            主题
	 * @param content
	 *            内容
	 * @param attachments
	 *            邮件附件
	 */
	public MailLing(String to, String cc, String bcc, String from, String subject, String content,
			List<MailAtt> attachments) {
		super();
		this.to = to;
		this.cc = cc;
		this.bcc = bcc;
		this.from = from;
		this.subject = subject;
		this.content = content;
		this.attachments = attachments;
	}

	/**
	 * 返回收件人
	 * 
	 * @return
	 */
	public String getTo() {
		return to;
	}

	/**
	 * 设置收件人
	 * 
	 * @param to
	 *            收件人
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * 返回抄送人
	 * 
	 * @return
	 */
	public String getCc() {
		return cc;
	}

	/**
	 * 设置抄送人
	 * 
	 * @param cc
	 *            抄送人
	 */
	public void setCc(String cc) {
		this.cc = cc;
	}

	/**
	 * 返回密送人
	 * 
	 * @return
	 */
	public String getBcc() {
		return bcc;
	}

	/**
	 * 设置密送人
	 * 
	 * @param bcc
	 */
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	/**
	 * 返回发送人
	 * 
	 * @return
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * 设置发送人
	 * 
	 * @param
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * 返回主题
	 * 
	 * @return
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * 设置主题
	 * 
	 * @param subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * 返回内容
	 * 
	 * @return
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置内容
	 * 
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 返回附件集合
	 * 
	 * @return
	 */
	public List<MailAtt> getAttachments() {
		return attachments;
	}

	/**
	 * 设置附件集合
	 * 
	 * @param attachments
	 *            附加
	 */
	public void setAttachments(List<MailAtt> attachments) {
		this.attachments = attachments;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MailLing [to=" + to + ", cc=" + cc + ", bcc=" + bcc + ", from=" + from + ", subject=" + subject
				+ ", content=" + content + ", attachments=" + attachments + "]";
	}

}
