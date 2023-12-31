package com.hotent.sys.persistence.dao;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.sys.persistence.model.SysDataSource;


public interface SysDataSourceDao extends BaseMapper<SysDataSource> {
	
	/**
	 * 获取动态数据源列表。
	 * @param enabled
	 * @param isStart
	 * @return
	 */
	List<SysDataSource> getDataSource(boolean enabled,boolean isStart);
	
	/**
	 * 根据别名获取数据源。
	 * @param alias
	 * @return
	 */
	SysDataSource getByAlias(String alias);
}
