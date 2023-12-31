package com.hotent.mail.persistence.dao;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.mail.model.MailSetting;

/**
 * 外部邮件用户设置 DAO接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月6日
 */
public interface MailSettingDao extends BaseMapper<MailSetting> {
	
	/**
	 * 根据邮箱地址返回相应的邮箱配置实体
	 * @param mailAddress 邮箱地址
	 * @return			     返回邮箱配置实体类
	 */
	public MailSetting getMailByAddress(String address);
	
	/**
	 * 根据当前用户ID获得邮箱列表
	 * @param userId 用户ID
	 * @return		   返回邮箱配置实体类集合
	 */
	public List<MailSetting> getMailByUserId(String userId);
	
	/**
	 * 获得当前用户的默认邮箱
	 * @param userId 用户ID
	 * @return		  返回邮箱配置实体类
	 */
	public MailSetting getByIsDefault(String userId);
	
	/**
	 * 根据当前用户ID获得外部邮箱的分页列表
	 * @param queryFilter
	 * @return
	 */
	public List<MailSetting> getAllByUserId(Map<String, Object> params);
	
	/**
	 * 验证邮箱地址的唯一性
	 * @param address 邮箱地址
	 * @return		     返回邮箱地址被配置的次数
	 */
	public int getCountByAddress(String address);
	
	/**
	 * 更改默认邮箱
	 * @param mail 邮箱
	 * @return	       返回
	 */
	public int updateDefault(MailSetting mail);
	
	/**
	 * 统计当前用户设置的外部邮箱数量
	 * @param userId 用户ID
	 * @return		  返回当前用户外部邮箱数量
	 */
	public int getCountByUserId(String userId);
	
	/**
	 * 更新邮箱的最近同步的邮件ID
	 * <pre>
	 * POP3、IMAP邮箱使用上一封邮件ID来做增量同步，exchange邮箱使用上一封邮件的收件时间来做增量同步。
	 * </pre>
	 * @param id				邮箱设置主键
	 * @param messageId			上一封邮件ID
	 * @param lastReceiveTime	上一封邮件收件时间
	 */
	void updateLastEnvelop(@Param("id")String id, @Param("messageId")String messageId, @Param("receiveTime")LocalDateTime lastReceiveTime);
	
}
