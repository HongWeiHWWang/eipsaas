package com.hotent.mail.persistence.manager;

import java.util.List;

import com.hotent.base.manager.BaseManager;
import com.hotent.mail.model.MailAttachment;


/**
 * 外部邮件附件表 处理接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月6日
 */
public interface MailAttachmentManager extends BaseManager<MailAttachment>{
	
	/**
	 * 根据邮箱ID获取邮箱附件
	 * @param mailId 邮箱ID
	 * @return		   返回邮箱附件实体集合
	 */
	List<MailAttachment> getByMailId(String mailId);

	/**
	 * 更新附件文件路径
	 * @param fileName 文件名称
	 * @param mailId   邮箱ID
	 * @param filePath 文件路径
	 */
	void updateFilePath(String fileName, String mailId, String filePath);

	/**
	 * 根据OutMail实体的fileIds，构建OutMailAttachment列表
	 * @param fileIds 附件ids 
	 * @return 		     返回邮箱附件实体集合
	 */
	List<MailAttachment> getByOutMailFileIds(String fileIds)throws Exception;
	 
	/**
	 * 根据邮件ID删除附件
	 * @param mailId 邮箱ID
	 */
	void delByMailId(String mailId);
	
}
