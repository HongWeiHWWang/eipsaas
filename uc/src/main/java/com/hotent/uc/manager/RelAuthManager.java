package com.hotent.uc.manager;

import java.util.List;
import java.util.Map;

import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.uc.model.RelAuth;
import com.hotent.base.manager.BaseManager;
import com.hotent.base.model.CommonResult;
import com.hotent.uc.params.common.OrgExportObject;
import com.hotent.uc.params.org.RelAuthVo;

/**
 * 汇报线权限
 * @author Administrator
 *
 */
public interface RelAuthManager extends   BaseManager<RelAuth>{
	
	/**
	 * 删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	
	
	
	/**
	 * 获取所有的汇报线权限
	 * @param queryFilter
	 * @return
	 */
	List<RelAuth> getAllRelAuth(QueryFilter queryFilter);
	
	/**
	 * 根据组织id和人员id获取分级管理
	 * @param map
	 * @return
	 */
	RelAuth getByRelIdAndUserId(String orgId,String userId);
	/**
	 * 根据用户id获取获取当前用户的组织布局管理权限
	 * @param userId
	 * @return
	 */
	List<RelAuth> getLayoutRelAuth(String userId);
	
	/**
	 * 通过用户获取所有授权的组
	 * @param userId
	 * @return
	 */
	List<RelAuth> getByUserId(String userId);
	
	/**
	 * 分级组织添加管理员
	 * @param RelAuthVo
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> addRelAuth(RelAuthVo relAuthVo) throws Exception;
	
	/**
	 * 分级组织添加管理员（多个）
	 * @param code
	 * @param accounts
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> addRelAuths(String code,String accounts) throws Exception;
	
	/**
	 * 更新组织分级
	 * @param RelAuthVo
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> updateRelAuth(RelAuthVo relAuthVo) throws Exception;
	
	/**
	 * 删除组织分级管理员
	 * @param relCode
	 * @param accounts
	 * @return
	 * @throws Exception 
	 */
	CommonResult<String> delRelAuth(String relCode,String accounts) throws Exception;
	
	/**
	 * 获取组织分级
	 * @param account
	 * @param orgCode
	 * @return
	 * @throws Exception 
	 */
	RelAuth getRelAuth(String account,String relCode) throws Exception;
	
	
	
	/**
	 * 
	 * @param typeId
	 * @param userId
	 * @return
	 */
	List<RelAuth> getRelAuthsByTypeAndUser(String typeId, String userId)throws Exception;
	
	/**
	 * 根据时间获取组织数据（数据同步）
	 * @param exportObject
	 * @return
	 * @throws Exception
	 */
	List<RelAuth> getRelAuthByTime(OrgExportObject exportObject) throws Exception ;
	
	/**
	 * 通过汇报线节点id删除汇报线权限管理
	 * @param relId
	 */
	void delByRelId(String relId);
	
	/**
	 * 根据条件查询出汇报线权限权限
	 * @param filter
	 * @return
	 */
	PageList<RelAuth> queryRelAuth(QueryFilter filter);
}
