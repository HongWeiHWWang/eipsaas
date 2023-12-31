package com.hotent.uc.dao;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.uc.model.OrgParams;

/**
 * 
 * <pre> 
 * 描述：组织参数 DAO接口
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2016-11-04 11:39:44
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface OrgParamsDao extends BaseMapper<OrgParams>{

	/**
	 *删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	
	/**
	 * 根据组织id获取组织参数列表
	 * @param orgId
	 * @return
	 */
	List<OrgParams> getByOrgId(@Param("orgId") String orgId);

	/**
	 * 根据组织id，参数别名获取组织参数
	 * @param orgId
	 * @param alias
	 * @return
	 */
	OrgParams getByOrgIdAndAlias(@Param("orgId") String orgId,@Param("alias")String alias);

	/**
	 * 根据组织id删除组织参数
	 * @param orgId
	 */
	void removeByOrgId(@Param("orgId") String orgId,@Param("updateTime") LocalDateTime updateTime);

	/**
	 * 根据参数别名删除组织参数
	 * @param alias
	 */
	void removeByAlias(@Param("alias") String alias,@Param("updateTime") LocalDateTime updateTime);
}
