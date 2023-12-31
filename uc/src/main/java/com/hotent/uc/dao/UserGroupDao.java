package com.hotent.uc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.uc.model.UserGroup;

/**
 * 
 * <pre> 
 * 描述：群组管理  DAO接口
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2017-11-27 17:55:17
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface UserGroupDao extends BaseMapper<UserGroup>{

	/**
	 *删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	
	/**
	 * 根据code取定义对象。
	 * @param code
	 * @return
	 */
	UserGroup getByCode(@Param("code") String code);
	
	/**
	 * 通过自定sql获取群组列表
	 * @param whereSql
	 * @return
	 */
	List<UserGroup> getByWhereSql(@Param("whereSql") String whereSql);
}
