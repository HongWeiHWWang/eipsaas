package com.hotent.uc.dao;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hotent.uc.model.OrgUser;

/**
 * 
 * <pre> 
 * 描述：用户组织关系 DAO接口
 * 构建组：x5-bpmx-platform
 * 作者:ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2016-06-30 10:27:31
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface OrgUserDao extends BaseMapper<OrgUser>{

	/**
	 *删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	
	/**
	 * 
	 * @param id
	 * @param relId
	 * @return
	 */
	int updateUserPost(@Param("id") String id,@Param("relId") String relId,@Param("updateTime")LocalDateTime updateTime);
	
	/**
	 * 
	 * @param orgId
	 * @param userId
	 * @return
	 */
	int removeByOrgIdUserId(@Param("orgId") String orgId,@Param("userId") String userId,@Param("updateTime")LocalDateTime updateTime);
	
	/**
	 * 获取指定用户的主岗位/主组织
	 * <p>按照默认维度主岗位、主组织的优先级排序</p>
	 * @param userId
	 * @return
	 */
	List<OrgUser> getMainPostOrOrgByUserId(String userId);
	
	/**
	 * 获取用户的主岗位组织关系
	 * @return
	 */
	List<OrgUser> getOrgUserMaster(@Param("userId") String userId,@Param("demId") String demId);
	
	/**
	 * 设置主部门
	 * @param id
	 * @return
	 */
	boolean setMaster(@Param("id") String id);
	
	/**
     * 取消主部门。
     * @param userId
     * @param demId
     * @return
     */
    boolean cancelUserMasterOrg(@Param("userId") String userId,@Param("demId") String demId,@Param("updateTime")LocalDateTime updateTime);

    /**
     * 根据用户ID取消用户主部门。
     * @param userId
     * @return
     */
    boolean cancelUserMasterOrgByUserId(@Param("userId") String userId,@Param("updateTime")LocalDateTime updateTime);
	
	/**
	 * 根据组织和关系id获取用户列表。
	 * @param convert2iPage 
	 * @param queryFilter
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	IPage<HashMap<String,Object>> getUserByGroup(IPage<OrgUser> convert2iPage,@Param(Constants.WRAPPER) Wrapper<OrgUser> wrapper);

    /**
     * 根据组织id查询组织下面的人员数量
     * @param params
     * @return
     */
    List getUserNumByOrgId(Map<String,Object> params) ;

    List getUserNumByOrgCode(Map<String,Object> params);
	/**
	 * 根据用户ids删除该用户的组织
	 * @param ids
	 */
	void removeByUserId(@Param("userId") String userId,@Param("updateTime")LocalDateTime updateTime);
	
	/**
	 * 取消orgId部门的主负责人
	 * @param orgId
	 */
	void updateCancleMainCharge(@Param("orgId") String orgId,@Param("updateTime")LocalDateTime updateTime);
	/**
	 * 根据组织ID获取组织的负责人组织关系
	 * @param orgId  组织ID
	 * @param isMain 是否获取主负责人
	 * @return
	 */
	public List<OrgUser> getChargesByOrgId(@Param("orgId") String orgId,@Param("isMain")  Integer isMain);
	/**
	 * 根据组织id删除组织人员关系
	 * 将删除本组织及其子组织下的人员关系
	 * @param orgIds
	 */
	void delByOrgId(@Param("orgId") String orgId,@Param("updateTime")LocalDateTime updateTime);
	
	/**
	 * 获取组织人员关系
	 * map中可以传orgId,userId,relId(岗位id),isMaster,isCharge
	 * @param map
	 * @return
	 */
	List<OrgUser> getByParms(Map<String,Object> map);
	
	/**
	 * 获取组织人员关系
	 * map中可以传orgId,userId,relId(岗位id),isMaster,isCharge
	 * @param map
	 * @return
	 */
	List<OrgUser> queryParms(Map<String,Object> map);
	
	/**
	 * 根据组织编码、角色编码获取
	 * @param orgCode
	 * @param roleCode
	 * @return
	 */
	List<OrgUser> getByOrgCodeAndroleCode(@Param("orgCode") String orgCode,@Param("roleCode") String roleCode);
	
	
	/**
	 * 根据组织编码、岗位编码获取
	 * @param orgCode
	 * @param roleCode
	 * @return
	 */
	List<OrgUser> getByPostCodeAndOrgCode(@Param("orgCode") String orgCode,@Param("postCode") String postCode);
	
	/**
	 * 设置人员主岗位
	 * @param id
	 */
	void updateUserMasterOrg(@Param("id") String id,@Param("updateTime")LocalDateTime updateTime);
	
	/**
	 * 将部门的主负责人取消， 设置为负责人
	 * @param orgId
	 */
	void cancleMainCharge(@Param("orgId") String orgId,@Param("updateTime")LocalDateTime updateTime);
	
	/**
	 * 通过用户账号获取其所属组织和岗位(包括逻辑删除的数据)
	 * @param account
	 * @return
	 */
	List<OrgUser> getByAccount(String account);
   /**
    * 检查岗位是否在设置的有效期内
    * @param updateTime
    * @param endDate
    */
	void checkIsInActiveTime(@Param("startDate")LocalDateTime startDate,@Param("endDate")LocalDateTime endDate);
	
	/**
	 * 根据组织获取相关数据列表。
	 * @param queryFilter
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List getOrgUserData(@Param(Constants.WRAPPER)Wrapper<OrgUser> wrapper) ;
	
	/**
	 * 获取组织用户
	 * @param convert2iPage 
	 * @param queryFilter
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	IPage getUserOrgPage(IPage<OrgUser> convert2iPage,@Param(Constants.WRAPPER) Wrapper<OrgUser> wrapper);

    /**
     * 根据组织id和用户id删除组织下的用户，并岗位id为空
     * @param params
     */
	void deleteOrgById(Map<String, Object> params);
	

	/**
	 * 更新岗位下用户的所属组织id
	 * @param postId
	 * @param orgId
	 */
	void updateUserOrgByPostId(@Param("postId") String postId,@Param("orgId")String orgId);

	/**
	 * 获取用户id岗位总数
	 * @param wrapper
	 * @return
	 */
	List getUserByGroupList(@Param(Constants.WRAPPER)Wrapper<OrgUser> wrapper);
	
}
