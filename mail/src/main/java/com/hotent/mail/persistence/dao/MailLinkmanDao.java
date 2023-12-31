package com.hotent.mail.persistence.dao;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.mail.model.MailLinkman;

/**
 * 外部邮件最近联系 DAO接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月6日
 */
public interface MailLinkmanDao extends BaseMapper<MailLinkman> {
	
	/**
	 * 根据联系人邮箱地址找到相应的联系人实体
	 * @param  params 参数
	 * @return		     返回邮箱联系人		
	 */
	public MailLinkman findLinkMan(Map params);
	
	/**
	 * 根据dm数据库查询当前用户id查询前20条最近联系人
	 * @param params 参数集合
	 * @return		 返回邮箱联系人实体集合
	 */
	public List<MailLinkman> getAllByUserIdDm(Map params);
	
	/**
	 * 根据oracl数据库查询当前用户id查询前20条最近联系人
	 * @param params 参数集合
	 * @return		 返回邮箱联系人实体集合
	 */
	public List<MailLinkman> getAllByUserIdOracl(Map params);
	
	/**
	 * 根据db2数据库查询当前用户id查询前20条最近联系人
	 * @param params 参数集合
	 * @return		 返回邮箱联系人实体集合
	 */
	public List<MailLinkman> getAllByUserIdDb2(Map params);
	
	/**
	 * 根据mysql数据库查询当前用户id查询前20条最近联系人
	 * @param params 参数集合
	 * @return		 返回邮箱联系人实体集合
	 */
	public List<MailLinkman> getAllByUserIdMysql(Map params);
	
	/**
	 * 根据mssql数据库查询当前用户id查询前20条最近联系人
	 * @param params 参数集合
	 * @return		 返回邮箱联系人实体集合
	 */
	public List<MailLinkman> getAllByUserIdMssql(Map params);
	
	/**
	 * 根据h2数据库查询当前用户id查询前20条最近联系人
	 * @param params 参数集合
	 * @return		 返回邮箱联系人实体集合
	 */
	public List<MailLinkman> getAllByUserIdH2(Map params);
	
	/**
	 * 根据联系人名称或邮箱地址查询
	 * @param query
	 * @return
	 */
	public List<MailLinkman> queryByAll(@Param("query")String query);
}
