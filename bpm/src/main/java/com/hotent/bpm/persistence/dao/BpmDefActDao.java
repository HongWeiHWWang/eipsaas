/**
 * 对象功能:流程分管授权限用户中间表明细 Dao类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:xucx
 * 创建时间:2014-03-05 10:10:53
 */
package com.hotent.bpm.persistence.dao;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.bpm.persistence.model.BpmDefAct;


public interface BpmDefActDao extends BaseMapper<BpmDefAct>{
	
	
	/**
	 * 获取所有的授权的流程
	 * @param params
	 * @return
	 */
	public  List<BpmDefAct> getAll(Map<String,Object> params);
	
	/**
	 * 根据授权ID删除流程授权的权限信息
	 * @param typeId
	 * @return
	 */
	public void delByAuthorizeId(@Param("authorizeId") String authorizeId);
	
	/**
	 * 根据流程相关信息删除流程授权的权限信息
	 * @param params
	 * @return
	 */
	public void delByMap(Map<String,Object> params);
	
	/**
	 * 获取与用户相关的授权的流程
	 * @param params
	 * @return
	 */
	public  List<BpmDefAct>  getActRightByUserMap(@Param("userRightMap") Map<String, String> userRightMap,@Param("authorizeType") String authorizeType,@Param("defKey") String defKey);
	
	/**
	 * 根据授权ID获取所有的流程授权信息
	 * @param authorizeId
	 * @return
	 */
	public  List<BpmDefAct> getByAuthorizeId(@Param("authorizeId") String authorizeId);
}