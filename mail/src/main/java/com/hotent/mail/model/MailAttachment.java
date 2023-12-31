package com.hotent.mail.model;



import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 邮件附件类
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月6日
 */
@ApiModel(description="邮件附件类")
@TableName("portal_sys_mail_attachement")
public class MailAttachment extends BaseModel<MailAttachment> {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(name="id", notes="主键,文件ID")
	@TableId("FILEID")
	protected String id; 

	@ApiModelProperty(name="fileName", notes="文件名")
	@TableField("FILENAME")
	protected String fileName;

	@ApiModelProperty(name="filePath", notes="文件路径")
	@TableField("FILEPATH")
	protected String filePath;

	@ApiModelProperty(name="mailId", notes="邮件ID")
	@TableField("MAILID")
	protected String mailId;
	
	@ApiModelProperty(name="fileBlob", notes="文件数据流")
	@TableField(exist=false)
	protected byte[] fileBlob;
	
	/**
	 * 邮件附件类构造方法
	 */
	public MailAttachment(){}
	
	/**
	 * 邮件附件类构造方法
	 * @param fileName	文件名
	 * @param filePath	文件全路径
	 */
	public MailAttachment(String fileName, String filePath) {
		this.fileName = fileName;
		this.filePath = filePath;
	}
	
	/**
	 * 邮件附件类构造方法
	 * @param fileName	文件名
	 * @param fileBlob	文件字节流
	 */
	public MailAttachment(String fileName, byte[] fileBlob) {
		this.fileName = fileName;
		this.fileBlob = fileBlob;
	}
	
	/**
	 * 获取文件名
	 * @return
	 */
	public String getFileName() {
		return fileName;
	}
	
	/**
	 * 设置文件名
	 * @param fileName 文件名
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * 获取文件路径
	 * @return
	 */
	public String getFilePath() {
		return filePath;
	}
	
	/**
	 * 设置文件路径
	 * @param filePath 文件路径
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	/**
	 * 获取文件数据流
	 * @return
	 */
	public byte[] getFileBlob() {
		return fileBlob;
	}
	
	/**
	 * 设置文件数据流
	 * @param fileBlob 文件数据流
	 */
	public void setFileBlob(byte[] fileBlob) {
		this.fileBlob = fileBlob;
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
	 * @param id 主键id 
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取邮箱id
	 * @return
	 */
	public String getMailId() {
		return mailId;
	}
	
	/**
	 * 设置邮箱id
	 * @param mailId 邮箱id
	 */
	public void setMailId(String mailId) {
		this.mailId = mailId;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("fileName", this.fileName) 
		.append("filePath", this.filePath) 
		.append("mailId", this.mailId) 
		.toString();
	}
}
