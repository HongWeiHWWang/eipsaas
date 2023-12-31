package com.hotent.portal.persistence.dao;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.portal.model.SysMessage;

/**
 * 系统信息DAO接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author hugh
 * @email zxh@jee-soft.cn
 * @date 2014-11-18 09:03:31
 */
public interface SysMessageDao extends BaseMapper<SysMessage> {
	
	/**
	 * 获取信息通过用户id
	 * @param queryFilter 通用查询器
	 * @return
	 */
	List<SysMessage> getMsgByUserId(Map<String,Object> params);
	
	
	/**
	 * 获取未读信息通过用户id
	 * @param userId 用户id
	 * @param isPublish 是否发布
	 * @return
	 */
	List<SysMessage> getNotReadMsgByUserId(@Param("receiverId")String userId,@Param("isPublish") int isPublish);
	
	/**
	 * 获取一条DB2数据库未读信息通过用户id
	 * @param userId 用户id
	 * @return
	 */
	SysMessage getOneNotReadMsgByUserIdDb2(String userId);
	
	/**
	 * 获取一条mssql数据库未读信息通过用户id
	 * @param userId 用户id
	 * @return
	 */
	SysMessage getOneNotReadMsgByUserIdMssql(String userId);
	
	/**
	 * 获取一条mysql数据库未读信息通过用户id
	 * @param userId 用户id
	 * @return
	 */
	SysMessage getOneNotReadMsgByUserIdMysql(String userId);
	
	/**
	 * 获取一条oracl数据库未读信息通过用户id
	 * @param userId 用户id
	 * @return
	 */
	SysMessage getOneNotReadMsgByUserIdOracl(String userId);
	
	/**
	 * 获取未读信息数量
	 * @param userId 用户id
	 * @return
	 */
	List<SysMessage> getNotReadMsgNum(String userId);
	
	/**
	 * 获取信息数量
	 * @param receiverId 接收id
	 * @return
	 */
	List<SysMessage> getMsgSize(String receiverId);
}
