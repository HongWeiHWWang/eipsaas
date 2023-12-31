package com.hotent.form.generator.impl;

import java.sql.Connection;
import javax.sql.DataSource;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;

/**
 * 扩展的动态数据源配置
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月30日
 */
public class DynamicDataSourceConfig extends DataSourceConfig{
	// 数据库类型
	private String dbType;
	// 数据源
	private DataSource dataSource;
	
	public DynamicDataSourceConfig(String dbType, DataSource dataSource) {
		this.dbType = dbType;
		this.dataSource = dataSource;
	}
	
	@Override
	public DbType getDbType() {
		return DbType.getDbType(this.dbType);
	}

	@Override
	public Connection getConn() {
		try{
			return dataSource.getConnection();
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
