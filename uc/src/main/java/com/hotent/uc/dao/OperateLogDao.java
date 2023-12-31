package com.hotent.uc.dao;


import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.uc.model.OperateLog;

/**
 *  DAO 接口类
 *
 */
public interface OperateLogDao extends BaseMapper<OperateLog>{
	

	/**
	 *删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	
  
	void removePhysicalById(@Param("id") String id);

}