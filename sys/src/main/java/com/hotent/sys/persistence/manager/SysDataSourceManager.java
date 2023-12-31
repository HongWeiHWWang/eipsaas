package com.hotent.sys.persistence.manager;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.hotent.base.manager.BaseManager;
import com.hotent.sys.persistence.model.SysDataSource;

public interface SysDataSourceManager extends BaseManager<SysDataSource> {

	/**
	 * 根据sysDataSource实例构建数据源实例。
	 * 
	 * @param sysDataSource
	 * @return DataSource
	 */
	DataSource getDsFromSysSource(SysDataSource sysDataSource);

	/**
	 * 根据别名获取数据源。
	 * 
	 * @param alias
	 * @return SysDataSource
	 */
	SysDataSource getByAlias(String alias);

	/**
	 * 
	 * 检验这个ds可用否
	 * 
	 * @param dataSource
	 *            ：数据源
	 * @param dataSourcePool
	 *            ：数据池
	 * @return boolean
	 */
	boolean checkConnection(SysDataSource sysDataSource);

	/**
	 * 获取在数据库同时在bean容器里的数据源
	 * 
	 * @return List<SysDataSource>
	 */
	List<SysDataSource> getSysDataSourcesInBean();


	/**
	 * 从数据库中实例化数据源列表。
	 * 
	 * @return List&lt;DataSource>
	 */
	Map<String, DataSource> getDataSource();

	/**
	 * 获取默认数据源 因为默认数据源是不在sys_data_source表中的
	 */
	SysDataSource getDefaultDataSource();

	/**
	 * 检验别名是否存在
	 * @param alias
	 * @return
	 */
	boolean isAliasExist(String alias);
}
