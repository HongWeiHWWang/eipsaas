package com.hotent.mail.persistence.manager;

import java.time.LocalDateTime;
import java.util.List;

import com.hotent.base.manager.BaseManager;
import com.hotent.base.query.QueryFilter;
import com.hotent.mail.model.MailSetting;


/**
 * 外部邮件用户设置 处理接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月6日
 */
public interface MailSettingManager extends BaseManager<MailSetting>{
	
	/**
	 * 测试外部邮箱
	 * @param MailSetting 邮箱配置实体类
	 * @param isOriginPwd 是否为原始密码
	 * @throws Exception
	 */
	public void testConnection(MailSetting mailSetting, boolean isOriginPwd) throws Exception;
	
	/**
	 * 测试外部邮箱
	 * @param setId	外部邮箱设置ID
	 * @throws Exception
	 */
	void testConnection(String setId) throws Exception;
	
	/**
	 * 保存邮箱设置
	 * 
	 * @param mailSetting	邮箱对象
	 * @param isOriginPwd	是否为原始密码
	 * @param userAccount	用户账号
	 * @throws Exception
	 */
	void saveSetting(MailSetting mailSetting, boolean isOriginPwd, String userAccount) throws Exception;
	/**
	 * 设置默认邮箱
	 * @param MailSetting 邮箱配置实体类
	 * @param currentUserId 当前用户id
	 * @throws Exception
	 */
	public void setDefault(MailSetting mailSetting, String currentUserId)throws Exception;
	
	/**
	 * 验证设置的邮箱地址的唯一性
	 * @param MailSetting 邮箱配置实体类
	 * @return			    是否存在这个邮箱
	 * @throws Exception
	 */
	public boolean isExistMail(MailSetting mailSetting)throws Exception;
	
	/**
	 * 根据邮箱地址返回相应的邮箱配置实体
	 * @param address 邮箱地址
	 * @return		     返回邮箱配置实体类
	 */
	public MailSetting getMailByAddress(String address);
	
	/**
	 * 获取用户的默认邮箱
	 * @param userId 用户id
	 * @return 		  返回邮箱配置实体类
	 */
	public MailSetting getByIsDefault(String userId);
	
	/**
	 * 获取当前用户的邮箱列表
	 * @param userId 用户id
	 * @return		  获取邮箱列表
	 */
	public List<MailSetting> getMailByUserId(String userId);
	
	/**
	 * 获取当前用户的邮箱分页列表
	 * @param queryFilter 参数构造查询
	 * @return			     获取邮箱列表
	 */
	public List<MailSetting> getAllByUserId(QueryFilter queryFilter);

	/**
	 * 获取用户的邮件数
	 * @param userId 用户id
	 * @return   	  返回邮件数量
	 */
	public int getCountByUserId(String userId);
	
	/**
	 * 删除邮箱
	 * @param lAryId 邮箱id
	 */
	public void delAllByIds(String[] lAryId);
	
	/**
	 * 更新邮箱的最近同步的邮件ID
	 * <pre>
	 * POP3、IMAP邮箱使用上一封邮件ID来做增量同步，exchange邮箱使用上一封邮件的收件时间来做增量同步。
	 * </pre>
	 * @param id				邮箱设置主键
	 * @param messageId			上一封邮件ID
	 * @param lastReceiveTime	上一封邮件收件时间
	 */
	void updateLastEnvelop(String id, String messageId, LocalDateTime lastReceiveTime);
}
