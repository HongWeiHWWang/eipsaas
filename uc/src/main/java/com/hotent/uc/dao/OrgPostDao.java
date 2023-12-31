package com.hotent.uc.dao;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hotent.base.query.QueryFilter;
import com.hotent.uc.model.OrgPost;
import com.hotent.uc.model.User;

/**
 * 
 * <pre> 
 * 描述：岗位 DAO接口
 * 构建组：x5-bpmx-platform
 * 作者:ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2016-06-30 10:26:10
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface OrgPostDao extends BaseMapper<OrgPost>{

	/**
	 *删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	
	/**
	 * 根据编码获取岗位
	 * @param code
	 * @return
	 */
    OrgPost getByCode(@Param("code") String code);
    
    /**
     * 根据组织id获取岗位信息
     * @param orgId
     * @return
     */
	List<OrgPost> getListByOrgId(@Param("orgId") String orgId);

	/**
	 * 根据条件获取岗位列表
	 * @param queryFilter
	 * @return
	 */
	List<OrgPost> queryInfoList(QueryFilter queryFilter);

	/**
	 * 根据组织id和职务id获取岗位
	 * @param orgId
	 * @param relDefId
	 * @return
	 */
	OrgPost getByOrgIdRelDefId(@Param("orgId") String orgId,@Param("relDefId") String relDefId);
	
	/**
	 * 根据用户账号获取岗位
	 * @param account
	 * @param demId
	 * @return
	 */
	List<OrgPost> getRelListByParam(Map<String,Object> map);

	/**
	 * 通过组织id删除岗位
	 * @param account
	 * @param demId
	 * @return
	 */
	void delByOrgId(@Param("orgId") String orgId,@Param("updateTime")LocalDateTime updateTime);
	
	/**
	 * 根据职务id获取岗位
	 * @param reldefId
	 * @return
	 */
	List<OrgPost> getByReldefId(@Param("relDefId") String relDefId);
	
	/**
	 * 设置主岗位
	 * @param code
	 * @param isCharge
	 */
	void updateRelCharge(@Param("id") String id,@Param("isCharge") Integer isCharge,@Param("updateTime")LocalDateTime updateTime);
	
	/**
	 * 取消主岗位
	 * @param orgId
	 */
	void cancelRelCharge(@Param("orgId") String orgId,@Param("updateTime")LocalDateTime updateTime);
	
	/**
	 * 根据组织id,是否主主岗位属性获取岗位列表
	 * @param orgId
	 * @param isCharge
	 * @return
	 */
	List<OrgPost> getRelChargeByOrgId(@Param("orgId") String orgId,@Param("isCharge") Integer isCharge);
	
	IPage<OrgPost> getOrgPost(IPage<OrgPost> page,@Param(Constants.WRAPPER)Wrapper<OrgPost> convert2Wrapper);

    /**
     * 根据岗位id查询岗位所在的人员，一般只有一位
     * @param postId
     * @return
     */
    List<Map<String,Object>> getFullname(@Param("postId")String postId);

    /**
     * 根据职务id查询岗位以及这些岗位所属的组织全路径
     * @param jobId
     * @return
     */
    List<Map<String,Object>> getPostByJobId(@Param("jobId")String jobId);

    /**
     * 根据用户ID查询用户组织岗位信息
     * @param userId
     * @return
     */
    List<Map<String,Object>> getUserByUserId(@Param("userId")String userId);

    /**
     * 根据用户ID查询用户角色信息
     * @param userId
     * @return
     */
    List<Map<String,Object>> getUserJobByUserId(@Param("userId")String userId);

	OrgPost get(Serializable id);

	/**
	 * 根据code查询记录数
	 * @param code
	 * @return
	 */
	Integer getCountByCode(@Param("code") String code);
}

