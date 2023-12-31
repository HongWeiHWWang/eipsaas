package com.hotent.mail.persistence.manager;

import java.security.NoSuchProviderException;
import java.util.List;

import javax.mail.MessagingException;

import com.hotent.base.manager.BaseManager;
import com.hotent.base.query.QueryFilter;
import com.hotent.mail.model.Mail;
import com.hotent.mail.model.MailAttachment;
import com.hotent.mail.model.MailSetting;

/**
 * 外部邮件 处理接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月6日
 */
public interface MailManager extends BaseManager<Mail>{
	/**
	 * 保存邮件至垃圾箱
	 * @param lAryId 邮件
	 */
	void addDump(String[] lAryId);
	
	/**
	 * 浏览邮件
	 * @param Mail 邮件
	 * @throws NoSuchProviderException
	 * @throws MessagingException
	 * @throws Exception 
	 */
	void emailRead(Mail Mail)throws Exception;
	
	/**
	 * 根据邮箱设定获取邮件列表。
	 * @param MailSetting 邮箱配置实体
	 * @param uidList	  uid集合
	 * @return			    返回邮件实体集合
	 * @throws Exception
	 */
	List<Mail> getMailListBySetting(MailSetting mailSetting) throws Exception;
	
	/**
	 * 同步远程邮件，进行选择性下载
	 * @param list  邮件集合
	 * @param setId 设置id
	 * @param currentUserId 当前用户id
	 * @throws Exception
	 */
	void saveMail(List<Mail> list, String setId, String currentUserId) throws Exception;
	
	/**
	 * 邮箱树形列表的json数据
	 * @param userId 用户id
	 * @return		  返回邮件集合
	 * @throws Exception
	 */
	List<MailSetting> getMailTreeData(String userId) throws Exception;
	
	/**
	 * 获取邮箱的分类邮件，如收件箱，发件箱，草稿箱
	 * @param queryFilter 构造查询函数
	 * @return			     返回邮件集合
	 */
	List<Mail> getFolderList(QueryFilter queryFilter);
	
	/**
	 * 得到用户默认邮箱中的邮件列表
	 * @param queryFilter 构造查询函数
	 * @return			     返回邮件集合	
	 */
	List<Mail> getDefaultMailList(QueryFilter queryFilter);

	/**
	 * 发送邮件,保存邮件信息至本地,添加/更新最近联系人
	 * @param Mail	  邮箱
	 * @param userId 用户id
	 * @param mailId 邮件id
	 * @param isReply 是否回复
	 * @param context 邮件内容
	 * @param basePath 基本路径
	 * @return		     返回外部邮件id
	 * @throws Exception
	 */
	String sendMail(Mail Mail, String userId, String mailId, int isReply, String context, String basePath) throws Exception;
	
	/**
	 * 得到用于回复页面显示信息
	 * @param mailId 邮件id
	 * @return		  返回邮件
	 */
	Mail getMailReply(String mailId);

	/**
	 * 通过setId删除
	 * @param setId 设置id
	 */
	void delBySetId(String setId);

	/**
	 * 返回附件下载地址
	 * @param entity 邮件附件
	 * @return		   邮件附件文件路径
	 * @throws Exception 
	 */
	String mailAttachementFilePath(MailAttachment entity) throws Exception;
	
	/**
	 * 通过获取名称
	 * @param email
	 * @return 		返回名称
	 */
    String getNameByEmail(String email);
    
    /**
     * 发送exchange邮箱邮件
     * @param mailSetting 邮箱配置实体类
     * @param mail	               邮箱
     * @throws Exception
     */
	void sendExchangeMail(MailSetting mailSetting, Mail mail) throws Exception;
	
	void isRead(String id);
	
}
