package com.hotent.mail.api;



import javax.mail.Part;

import com.hotent.mail.model.Mail;

import microsoft.exchange.webservices.data.property.complex.AttachmentCollection;


/**
 * <pre> 
 * 描述：邮件附件处理接口
 * 构建组：x5-base-core
 * 作者：gjh
 * 邮箱:guojh@jee-soft.cn
 * 日期:2014-10-30-下午3:28:34
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface AttacheHandler {

	/**
	 * 邮件附件处理接口方法
	 * @param part
	 * @param mail 邮件对象
	 * <p>示例：</p>
	 * <pre>
	 * public void handle(Part part, Mail mail){
	 * 	//获得文件名
	String filename=MimeUtility.decodeText(part.getFileName());
	String basePath = System.getProperty("user.dir")+File.separator;
	String filePath = basePath + filename;
	System.out.println(filePath);
	File file = new File(basePath);
	if (!file.exists()) file.mkdirs();
	//将附件流保存到本地
	InputStream is = part.getInputStream() ;
	FileOutputStream fos = new FileOutputStream(filePath);
	byte[] bs = new byte[512];
	int n = 0;
	while ((n = is.read(bs)) != -1) {
		fos.write(bs, 0, n);
	}
	is.close();
	fos.close();
	//将附件的文件名及存放路径存入Mail对象
	mail.getMailAttachments().add(new MailAttachment(filename, filePath));
	 * }
	 * </pre>
	 * @see	Mail
	 */
	void handle(Part part, Mail mail);
	
	/**
	 * 保存exchange的附件
	 * @param attachments
	 * @param mail
	 */
	void handle(AttachmentCollection attachments, Mail mail);
	
	/**
	 * 根据传入的邮件唯一标识ID，判断是否对此邮件进行下载。
	 * @param messageId	同一个邮箱中的邮件唯一标识ID
	 * @return	true：下载；false：不下载
	 * <p>示例：</p>
	 * <pre>
	 * public Boolean isDownlad(String UID) {
	 * 	Mail mail = getFromDB("admin@jee-soft.cn", UID);
	 * 	if(mail!=null) return false ;
	 * 	return true;
	 * }
	 * </pre>
	 */
	Boolean isDownlad(String messageId);
}
