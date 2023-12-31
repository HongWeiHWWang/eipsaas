package com.hotent.uc.dao;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hotent.base.constants.SQLConst;
import com.hotent.uc.model.RelAuth;
import com.baomidou.mybatisplus.core.toolkit.Constants;
/**
 * 汇报线权限
 * @author Administrator
 *
 */
public interface RelAuthDao extends BaseMapper<RelAuth>{

	/**
	 *删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	

	/**
	 * 获取所有的汇报线
	 * @param page 
	 * @param wrapper
	 * @return
	 */
	List<RelAuth> getAllRelAuth(IPage<RelAuth> page,@Param(Constants.WRAPPER) Wrapper<RelAuth> wrapper);

	/**
	 * 根据分级节点id和人员id获取分级管理
	 * @param map
	 * @return
	 */
	RelAuth getByRelIdAndUserId(@Param("relId") String relId,@Param("userId") String userId);

	/**
	 * 根据分级节点id删除分级管理员
	 * @param relIds
	 */
	void delByRelId(@Param("relId") String relId,@Param("updateTime") LocalDateTime updateTime);

	/**
	 * 获取当前用户的分级节点布局管理权限
	 * @param userId
	 * @return
	 */
	List<RelAuth> getLayoutRelAuth(@Param("userId") String userId);

	/**
	 * 通过用户获取所有授权的组
	 * @param userId
	 * @return
	 */
	List<RelAuth> getByUserId(@Param("userId") String userId);
	/**
	 * 根据汇报线分类编码和用户账号获取汇报线管理列表
	 * @param typeCode
	 * @param account
	 * @return
	 */
	List<RelAuth> getRelAuthListByTypeAndUser(@Param("typeCode") String typeCode, @Param("account") String account);
	
	/**
	 * 根据汇报线分类编码和用户id获取汇报线管理列表
	 * @param typeCode
	 * @param account
	 * @return
	 */
	List<RelAuth> getRelAuthsByTypeAndUser(@Param("typeId") String typeId, @Param("userId") String userId);
	
	/**
	 * 
	 * @param wrapper
	 * @return
	 */
	List<RelAuth> queryOnSync(@Param(Constants.WRAPPER)Wrapper<RelAuth> wrapper);


	IPage<RelAuth> query(IPage<RelAuth> convert2iPage,@Param(Constants.WRAPPER) Wrapper<RelAuth> wrapper);


	RelAuth get(Serializable id);
	
}

