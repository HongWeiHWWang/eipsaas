package com.hotent.mail.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 邮件附件实体类 外
 * 
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月15日
 */
@ApiModel(description = "邮件附件实体类")
public class MailAtt {

	@ApiModelProperty(name = "fileId", notes = "附件id")
	String fileId;

	@ApiModelProperty(name = "fileName", notes = "附件名称")
	String fileName;
	
	/**
	 * 附件无参构造
	 */
	public MailAtt() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 附件有参构造
	 * 
	 * @param fileId
	 *            附件id
	 * @param fileName
	 *            附件名称
	 */
	public MailAtt(String fileId, String fileName) {
		super();
		this.fileId = fileId;
		this.fileName = fileName;
	}

	/**
	 * 返回附件id
	 * 
	 * @return
	 */
	public String getFileId() {
		return fileId;
	}

	/**
	 * 设置附件id
	 * 
	 * @param fileId
	 *            附件id
	 */
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	/**
	 * 返回附件名称
	 * 
	 * @return
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * 设置附件名称
	 * 
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MailAtt [fileId=" + fileId + ", fileName=" + fileName + "]";
	}

}
