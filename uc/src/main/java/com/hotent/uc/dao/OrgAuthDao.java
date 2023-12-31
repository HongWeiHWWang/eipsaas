package com.hotent.uc.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hotent.uc.model.OrgAuth;
import com.hotent.uc.model.User;

/**
 * 
 * <pre> 
 * 描述：分级组织管理 DAO接口
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2017-07-20 14:30:29
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface OrgAuthDao extends BaseMapper<OrgAuth>{
	

	/**
	 *删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	

	/**
	 * 获取所有的分级组织
	 * @param params
	 * @return
	 */
	IPage<OrgAuth> getAllOrgAuth(IPage<OrgAuth> page,@Param(Constants.WRAPPER)Wrapper<OrgAuth> convert2Wrapper);

	/**
	 * 根据组织id和人员id获取分级管理
	 * @param map
	 * @return
	 */
	OrgAuth getByOrgIdAndUserId(@Param("orgId") String orgId,@Param("userId") String userId);

	/**
	 * 根据组织id和人员id获取分级管理
	 * @param map
	 * @return
	 */
	List<OrgAuth> getListByOrgIdAndUserId(@Param("orgId") String orgId,@Param("userId") String userId);


	/**
	 * 根据组织id删除分级管理员
	 * @param orgIds
	 */
	void delByOrgId(@Param("orgId") String orgId,@Param("updateTime") LocalDateTime updateTime);

	/**
	 * 获取当前用户的组织布局管理权限
	 * @param userId
	 * @return
	 */
	List<OrgAuth> getLayoutOrgAuth(@Param("userId") String userId);

	/**
	 * 通过用户获取所有授权的组
	 * @param userId
	 * @return
	 */
	List<OrgAuth> getByUserId(@Param("userId") String userId);
	/**
	 * 根据维度编码和用户账号获取分级组织管理列表
	 * @param demCode
	 * @param account
	 * @return
	 */
	List<OrgAuth> getOrgAuthListByDemAndUser(@Param("demCode") String demCode, @Param("account") String account);
	
	/**
	 * 
	 * @param wrapper
	 * @return
	 */
	List<OrgAuth> queryOnSync(@Param(Constants.WRAPPER)Wrapper<OrgAuth> wrapper);


	IPage<OrgAuth> query(IPage<OrgAuth> convert2iPage,@Param(Constants.WRAPPER) Wrapper<OrgAuth> convert2Wrapper);
	
}

