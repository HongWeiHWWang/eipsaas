package com.hotent.uc.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hotent.uc.model.Org;

/**
 * 
 * <pre> 
 * 描述：组织架构 DAO接口
 * 构建组：x5-bpmx-platform
 * 作者:ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2016-06-28 15:13:03
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface OrgDao extends BaseMapper<Org>{
	

	/**
	 *删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	
 
	/**
	 * 根据Code取定义对象。
	 * @param code
	 * @return
	 */
	Org getByCode(@Param("code") String code);
	
	/**
	 * 通过用户ID获取组织ID、是否主组织Map
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getOrgIdMapByUserId(@Param("userId") String userId);
	
	/**
	 * 通过父组织ID集合查询所有子组织ID
	 * @param parentIds
	 * @return
	 */
	List<String> getSubOrgByIds(@Param("parentIds")Set<String> parentIds);

	/**
	 * 根据用户ID获取组织列表
	 * @param userId
	 * @return
	 */
	List<Org> getOrgListByUserId(@Param("userId") String userId);
	
	/**
	 * 获取用户主组织
	 * @param userId
	 * @param demId
	 * @return
	 */
	List<Org> getMainOrgListByUser(@Param("userId") String userId,@Param("demId") String demId);

	/**
	 * 根据父组织id获取其下子组织（包含父组织）
	 * @param parentId
	 * @return
	 */
	List<Org> getByParentId(@Param("pid") String pid);

	/**
	 * 根据组织名称获取组织列表
	 * @param orgName
	 * @return
	 */
	List<Org> getByOrgName(@Param("orgName") String orgName);

	/**
	 * 根据路径名获取组织
	 * @param orgName
	 * @return
	 */
	List<Org> getByPathName(@Param("pathName") String pathName);

	/**
	 * 根据父级id以及维度id获取组织
	 * @param orgName
	 * @return
	 */
	List<Org> getByParentAndDem(@Param(Constants.WRAPPER)Wrapper<Org> wrapper);

	/**
	 * 根据子级查询父级
	 * @param demId
	 * @param sonId
	 * @return
	 */
	Org getByDemIdAndSonId(@Param("demId") String demId,@Param("sonId") String sonId);

	/**
	 * 根据维度ID获取组织列表
	 * @param demId
	 * @return
	 */
	List<Org> getOrgListByDemId(@Param("demId") String demId);
	
	/**
	 * 获取用户所属（主）组织
	 * @param map
	 * @return
	 */
	List<Org> getUserOrg(Map<String,Object> map);
	
	/**
	 * 关联维度获取组织
	 * @param queryFilter
	 * @return
	 */
	List<Org> getOrgInnerDem(Map<String,Object> params);
	
	/**
	 * 通过账号获取所属组织
	 * @param account
	 * @return
	 */
	List<Org> getOrgsByAccount(String account);
	
	/**
	 * 通过账号获取所属组织，
	 * @param account
	 * @return
	 */
	List<Org> justGetOrgsByAccount(String account);
	/**
	 * 通过用户组获取所属部门，
	 * @param userIds
	 * @return
	 */
	List<Map<String,String>> getPathNames(@Param("userIds")List<String> userIds);
	
	/**
	 * 根据父组织路径，获取所有子组织id（包含自己）
	 * @param pathMap
	 * @return
	 */
	List<String> getChildrenIds(@Param("pathMap") Map<String, String> pathMap);
	
	/**
	 * 根据用户id 查询主组织
	 * @param userId
	 * @return
	 */
	List<Org> getOrgMaster(@Param("account")String account);


	Org get(Serializable id);

	/**
	 * 根据code查询记录数
	 * @param code
	 * @return
	 */
	Integer getCountByCode(@Param("code") String code);

}

