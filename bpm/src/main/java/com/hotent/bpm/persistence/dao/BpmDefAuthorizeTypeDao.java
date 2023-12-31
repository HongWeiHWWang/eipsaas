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
import com.hotent.bpm.persistence.model.BpmDefAuthorizeType;



public interface BpmDefAuthorizeTypeDao extends BaseMapper<BpmDefAuthorizeType>{
	
	/**
	 * 获取授权的类型列表
	 * @param params
	 * @return
	 */
	public  List<BpmDefAuthorizeType> getAll(Map<String,Object> params);
	
	/**
	 * 根据授权ID删除流程授权的权限信息
	 * @param typeId
	 * @return
	 */
	public void delByAuthorizeId(@Param("authorizeId") String authorizeId);
}