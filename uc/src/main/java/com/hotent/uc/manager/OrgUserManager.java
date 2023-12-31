package com.hotent.uc.manager;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hotent.base.manager.BaseManager;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.QueryFilter;
import com.hotent.uc.model.OrgUser;
import com.hotent.uc.params.user.UserPolymerOrgPos;

/**
 * 
 * <pre> 
 * 描述：用户组织关系 处理接口
 * 构建组：x5-bpmx-platform
 * 作者:ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2016-06-30 10:27:31
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface OrgUserManager extends BaseManager<OrgUser>{
	
	/**
	 * 删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	
	
	
	int updateUserPost(String id, String relId);
	
	/**
	 * 根据组织用户和关系id查找关联关系。
	 * @param orgId
	 * @param userId
	 * @param relId
	 * @return
	 */
	OrgUser getOrgUser(String orgId,String userId,String relId);
    
	/**
	 * 根据用户和组织ID获取关联关系。
	 * @param orgId
	 * @param userId
	 * @return
	 */
	List<OrgUser> getListByOrgIdUserId(String orgId, String userId);
	
	/**
	 * 获取指定用户的主岗位/主组织
	 * <p>优先获取默认维度的主岗位/主组织，没有时获取其他维度的。</p>
	 * @param userId
	 * @return
	 */
	OrgUser getMainPostOrOrgByUserId(String userId);
	
	/**
	 * 获取用户的主岗位组织关系
	 * @return
	 */
	List<OrgUser> getOrgUserMaster(String userId,String demId);
	
    int removeByOrgIdUserId(String orgId,String userId);
    
    /**
     * 设置主组织关系。
     * @param id		主键
     * @return
     * @throws SQLException 
     */
	void setMaster(String ... id) throws SQLException;
	
	/**
	 * 设置部门负责人，同一个用户只能是某个组织的主负责人或负责人
	 * 不能既是主负责人又是负责人
	 * @param userId 用户id
	 * * @param isCharge false为取消操作，true为设置操作
	 * @param orgId 组织id
	 * @throws SQLException 
	 */
	void setCharge(String userId,Boolean isCharge, String orgId) throws SQLException;
	
	/**
	 * 根据queryfilter查询部门或岗位下的人员。
	 * @param queryFilter
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public IPage<HashMap<String,Object>> getUserByGroup(QueryFilter queryFilter) ;
	/**
	 * 根据组织ID获取组织的负责人组织关系
	 * @param orgId  组织ID
	 * @param isMain 是否获取主负责人(只有一个)
	 * @return
	 */
	public List<OrgUser> getChargesByOrgId(String orgId,Boolean isMain);
	
	/**
	 * 检测所有岗位的有效性
	 */
	void syncValidOrgUser();
	
	/**
	 * 根据组织id删除用户组织关系
	 * @param orgId
	 */
	void delByOrgId(String orgId);
	
	/**
	 * 获取组织人员关系
	 * map中可以传orgId,userId,relId(岗位id),isMaster,isCharge
	 * @param map
	 * @return
	 */
	List<OrgUser> getByParms(Map<String,Object> params);
	
	/**
	 * 保存用户所属组织、岗位(排他性保存，除了传入的组织和岗位，其他数据会被删除)
	 * @param account
	 * @param orgPoses
	 * @throws Exception
	 */
	void saveOrgUser(String account, List<UserPolymerOrgPos> orgsPoses) throws Exception;
	
	/**
	 * 检查岗位是否在设置的有效期内
	 * @param date
	 */
	void checkIsInActiveTime(LocalDateTime date);
	
	/**
	 * 
	 * @param filter
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	IPage getUserOrgPage(QueryFilter filter);

    /**
     * 根据组织id和用户id删除组织下的用户，并岗位id为空
     */
    CommonResult<String> deleteOrgById(String orgId, String userId) throws Exception;

	/**
	 * 设置主岗位
	 * @param ids
	 */
	void setMasterByIds(String ... ids);

	/**
	 * 获取用户id岗位总数
	 * @param queryFilter
	 * @return
	 */
	List getUserByGroupList(QueryFilter queryFilter);



	List<Map<String, Object>> getUserNumByOrgCode(Map<String, Object> map);



	void updateUserOrgByPostId(String id, String orgId);



	List getOrgUserData(QueryFilter queryFilter);



	void removeByUserId(String id, LocalDateTime now);



	Object getChargesByOrgId(String orgId, int i);



	List<OrgUser> getByOrgCodeAndroleCode(String orgCode, String roleCode);



	List<OrgUser> getByPostCodeAndOrgCode(String orgCode, String postCode);
}
