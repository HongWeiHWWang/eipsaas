/**
 * 对象功能:流程分管授权限用户中间表明细 Dao类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:xucx
 * 创建时间:2014-03-05 10:10:53
 */
package com.hotent.sys.persistence.dao;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.sys.persistence.model.SysAuthUser;

public interface SysAuthUserDao extends BaseMapper<SysAuthUser>{

	/**
	 * 获取所有的授权的对象用户
	 * @param params
	 * @return
	 */
	public  List<SysAuthUser> getAll(Map<String,Object> params);
	
	/**
	 * 根据授权ID删除流程用户子表的权限信息
	 * @param typeId
	 * @param objType
	 * @return
	 */
	public void delByAuthorizeId(@Param("authorizeId") String authorizeId,@Param("objType") String objType);
	/**
	 * 获取与用户相关的授权的项目ID
	 * @param userRightMap
	 * @param objType
	 * @return
	 */
	public  List<String>  getAuthorizeIdsByUserMap(@Param("userRightMap") Map<String, String> userRightMap,@Param("objType") String objType);
	/**
	 * 获取用户权限对某模块数据是否有权限
	 *@param userRightMap
	 * @param authorizeId
	 */
	public List<String> getAuthByAuthorizeId(@Param("userRightMap") Map<String, String> userRightMap,@Param("authorizeId") String authorizeId);
}