package com.hotent.mail.persistence.dao;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotent.mail.model.Mail;


/**
 * 外部邮件 DAO接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月6日
 */
public interface MailDao extends BaseMapper<Mail> {
	
	@Select("select * from portal_sys_mail ${ew.customSqlSegment}")
	List<Mail> getAll(@Param(Constants.WRAPPER) Wrapper<Mail> wrapper);
	
	/**
	 * 发件人列表
	 * @param senderAddress 发送人邮箱地址
	 * @return 				返回邮件实体类集合
	 */
	public List<Mail> getListBySender(String senderAddress);

	/**
	 * 获取分类邮件（收件箱，发件箱，草稿箱，垃圾箱)
	 * @param  参数
	 * @return 返回邮件实体集合
	 */
	public List<Mail> getFolderList(Map params);
	

	/**
	 * 更新邮箱分类类型
	 * @param mailId 邮箱id 
	 * @param types 邮箱分类类型
	 * @return		返回更新邮箱分类类型数量
	 */
	public int updateTypes(Map params);
	/**
	 * 根据邮箱和邮箱类型得到邮件数量
	 *
	 * @return	       返回邮箱类型邮件数量
	 */
	public int getFolderCount(Map params);
	
	/**
	 *  获得用户默认邮箱的邮件列表
	 * @param params 查询参数
	 * @return	返回邮箱集合实体类
	 */
	public List<Mail> getDefaultMailList(Map params);	
	
	/**
	 * 判断是否存在当前邮件uid
	 * @param uid   邮件uid
	 * @param setId 设置Id
	 * @return	返回boolean判断
	 */
	public boolean getByEmailId(String uid,String setId);
	
	/**
	 * 根据邮件uid删除本地邮件
	 * @param uid  邮件uid
	 */
	public void delByEmailid(String uid);
	
	/**
	 * 根据用户ID得到最新已发的邮件
	 * @param userId 用户Id
	 * @param pb	  分页
	 * @return 		  返回邮箱实体类集合
	 */
	public List<Mail> getMailByUserId(String userId, Page pb);
	
	/**
	 * 通过setId删除
	 * @param setId 设置id
	 */
	public void delBySetId(String setId);	
	
	/**
	 * 
	 * 根据发送次数获取收件人邮件名称集
	 * @param userId 用户ID
	 * @return       返回邮箱实体类集合
	 */
	public List<Mail> getReceiveraddesses(String userId);
	
	/**
	 * 邮箱变成已读
	 * @param id
	 */
	public void isRead(String id);
	
}
