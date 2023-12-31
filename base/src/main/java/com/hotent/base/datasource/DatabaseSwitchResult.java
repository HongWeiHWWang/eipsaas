package com.hotent.base.datasource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.hotent.base.datasource.impl.DefaultDatabaseContext;
import com.hotent.base.handler.MultiTenantHandler;
import com.hotent.base.interceptor.MasterSlaveAutoRoutingPlugin;
import com.hotent.base.util.AppUtil;

/**
 * 切换数据源的结果
 * <p>
 * 这里实现了AutoCloseable接口，当系统中切换数据源动作在try()中执行时，执行完后会调用close方法，close中切换回原来的数据源。
 * </p>
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月22日
 */
public class DatabaseSwitchResult implements AutoCloseable{
	/**
	 * 切换前的数据源
	 */
	private String previewDsAlias;
	/**
	 * 当前数据源
	 */
	private String currentDsAlias;
	/**
	 * 当前切换数据源的数据库类型
	 */
	private String dbType;
	
	public DatabaseSwitchResult(String previewDsAlias, String currentDsAlias, String dbType) {
		this.setPreviewDsAlias(previewDsAlias);
		this.setCurrentDsAlias(currentDsAlias);
		this.setDbType(dbType);
	}
	
	@Override
	public void close() throws Exception {
		// 清空当前切换的数据源
		DynamicDataSourceContextHolder.clear();
		DefaultDatabaseContext context = AppUtil.getBean(DefaultDatabaseContext.class);
		context.clear();
		JdbcTemplate jdbcTemplate = AppUtil.getBean(JdbcTemplate.class);
		// 设置回默认的数据源
		jdbcTemplate.setDataSource(context.getDataSource());
		// 告诉读写分离路由器：离开外部数据源了
		MasterSlaveAutoRoutingPlugin.removeInExternalDatasource();
		// 告诉多租户：离开外部数据源了
		MultiTenantHandler.removeThreadLocalIgnore();
	}

	public String getPreviewDsAlias() {
		return previewDsAlias;
	}

	public void setPreviewDsAlias(String previewDsAlias) {
		this.previewDsAlias = previewDsAlias;
	}

	public String getCurrentDsAlias() {
		return currentDsAlias;
	}

	public void setCurrentDsAlias(String currentDsAlias) {
		this.currentDsAlias = currentDsAlias;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
}
