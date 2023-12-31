package com.hotent.portal.persistence.dao;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.portal.model.SysIndexMyLayout;

public interface SysIndexMyLayoutDao extends BaseMapper<SysIndexMyLayout> {
	/**
	 * 返回我的布局实体
	 * @param userId 用户id
	 * @return
	 */
	SysIndexMyLayout getByUserId(String userId);
	
	/**
	 * 通过用户ID删除布局
	 * @param userId	用户ID
	 */
	void removeByUserId(String userId);

	void updateValid(@Param("type")int type,@Param("userId") String userId);

	void setValid(String id);
}
