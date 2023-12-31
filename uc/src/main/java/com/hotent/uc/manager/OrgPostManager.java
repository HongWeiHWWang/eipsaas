package com.hotent.uc.manager;

import java.util.List;
import java.util.Map;

import com.hotent.base.manager.BaseManager;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.uc.model.OrgPost;

/**
 * 
 * <pre> 
 * 描述：组织关联关系 处理接口
 * 构建组：x5-bpmx-platform
 * 作者:ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2016-06-30 10:26:10
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface OrgPostManager extends   BaseManager<OrgPost>{
	
	
	/**
	 * 删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	
	
	OrgPost getByCode(String code);
	
	/**
	 * 根据组织ID获取岗位列表
	 * @param orgId
	 * @return
	 */
	List<OrgPost> getListByOrgId(String orgId);
	
	
	List<OrgPost> queryInfoList(QueryFilter queryFilter);
	
	/**
	 * 根据组织ID和职务ID获取岗位定义
	 * @param orgId
	 * @param relDefId
	 * @return
	 */
	OrgPost getByOrgIdRelDefId(String orgId,String relDefId);
	
	/**
	 * 根据职务ID获取岗位列表
	 * @param relDefId 职务ID
	 * @return
	 */
	List<OrgPost> getByRelDefId(String relDefId);
	
	/**
	 * 根据用户ID获取对应的岗位列表
	 * @param userId
	 * @return
	 */
	List<OrgPost> getListByUserId(String userId,String demId);
	
	/**
	 * 根据用户账号获取对应的岗位列表
	 * @param account
	 * @return
	 */
	List<OrgPost> getListByAccount(String account,String demId);
	
	/**
	 * 设置/取消主岗位
	 * @param id
	 * @param isCharge
	 * @return
	 */
	boolean setRelCharge(String id,boolean isCharge);
	
	/**
	 * 取消组织中的主岗位
	 * @param id
	 * @return
	 */
	boolean cancelRelCharge(String orgId);
	
	/**
	 * 获取组织中的主(非主)岗位
	 * @param orgId
	 * @param isCharge
	 * @return
	 */
	List<OrgPost> getRelCharge(String orgId,Boolean isCharge);
	
	/**
	 * 根据组织id删除岗位
	 * @param orgId
	 */
	void delByOrgId(String orgId);
	
	/**
	 * 获取组织岗位
	 * @param filter
	 * @return
	 */
	PageList<OrgPost> getOrgPost(QueryFilter filter);

    /**
     * 根据岗位id查询岗位所在的人员，一般只有一位
     * @param postId
     * @return
     */
    List<Map<String,Object>> getFullname(String postId);

    /**
     * 根据职务id查询岗位以及这些岗位所属的组织全路径
     * @param jobId
     * @return
     */
    List<Map<String,Object>> getPostByJobId(String jobId);

    /**
     * 根据用户ID查询用户组织岗位角色信息
     * @param userId
     * @return
     */
    List<Map<String,Object>> getUserByUserId(String userId);


	List<OrgPost> getByReldefId(String jobId);

	Integer getCountByCode(String code);
}
