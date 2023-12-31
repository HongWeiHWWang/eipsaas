package com.hotent.uc.manager;

import java.util.List;
import java.util.Map;

import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.uc.model.OrgAuth;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hotent.base.manager.BaseManager;
import com.hotent.base.model.CommonResult;
import com.hotent.uc.params.common.OrgExportObject;
import com.hotent.uc.params.org.OrgAuthVo;

/**
 * 
 * <pre> 
 * 描述：分级组织管理 处理接口
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2017-07-20 14:30:29
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface OrgAuthManager extends   BaseManager<OrgAuth>{
	
	/**
	 * 删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	
	
	/**
	 * 获取所有的分级组织
	 * @param queryFilter
	 * @return
	 */
	IPage<OrgAuth> getAllOrgAuth(QueryFilter<OrgAuth> queryFilter);
	
	/**
	 * 根据组织id和人员id获取分级管理
	 * @param map
	 * @return
	 */
	OrgAuth getByOrgIdAndUserId(String orgId,String userId);

	/**
	 * 根据组织id和人员id获取分级管理
	 * @param map
	 * @return
	 */
	List<OrgAuth> getListByOrgIdAndUserId(String orgId,String userId);

	/**
	 * 根据用户id获取获取当前用户的组织布局管理权限
	 * @param userId
	 * @return
	 */
	List<OrgAuth> getLayoutOrgAuth(String userId);
	
	/**
	 * 通过用户获取所有授权的组
	 * @param userId
	 * @return
	 */
	List<OrgAuth> getByUserId(String userId);
	
	/**
	 * 添加组织分级
	 * @param orgAuthVo
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> addOrgAuth(OrgAuthVo orgAuthVo) throws Exception;
	
	/**
	 * 更新组织分级
	 * @param orgAuthVo
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> updateOrgAuth(OrgAuthVo orgAuthVo) throws Exception;
	
	/**
	 * 删除组织分级
	 * @param account
	 * @param orgCode
	 * @return
	 * @throws Exception 
	 */
	CommonResult<String> delOrgAuth(String account,String orgCode) throws Exception;
	
	/**
	 * 获取组织分级
	 * @param account
	 * @param orgCode
	 * @return
	 * @throws Exception 
	 */
	OrgAuth getOrgAuth(String account,String orgCode) throws Exception;
	
	/**
	 * 获取所有的分级组织管理
	 * @param params
	 * @return
	 */
	//List<OrgAuth> getAllOrgAuth(Map<String,Object> params);
	
	/**
	 * 
	 * @param demCode
	 * @param account
	 * @return
	 */
	List<OrgAuth> getOrgAuthListByDemAndUser(String demCode, String account)throws Exception;
	
	/**
	 * 根据时间获取组织数据（数据同步）
	 * @param exportObject
	 * @return
	 * @throws Exception
	 */
	List<OrgAuth> getOrgAuthByTime(OrgExportObject exportObject) throws Exception ;
	
	/**
	 * 通过组织id删除分级组织管理
	 * @param orgId
	 */
	void delByOrgId(String orgId);
	
	/**
	 * 根据条件查询出分级组织权限
	 * @param filter
	 * @return
	 */
	PageList<OrgAuth> queryOrgAuth(QueryFilter<OrgAuth> filter);
}
