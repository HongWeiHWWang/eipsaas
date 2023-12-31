package com.hotent.table.factory;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.jdbc.core.JdbcTemplate;

import com.hotent.base.constants.SQLConst;
import com.hotent.table.meta.impl.BaseTableMeta;

/**
 * TableOperator factorybean，用户创建ITableOperator对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月25日
 */
public class TableMetaFactoryBean implements FactoryBean<BaseTableMeta> {

	private BaseTableMeta tableMeta;

	// 数据库类型
	private String dbType = SQLConst.DB_MYSQL;
	// 数据源
	private JdbcTemplate jdbcTemplate;

	@Override
	public BaseTableMeta getObject() throws Exception {
	
		tableMeta = DatabaseFactory.getTableMetaByDbType(dbType);
		tableMeta.setJdbcTemplate(jdbcTemplate);
		return tableMeta;
	}

	/**
	 * 设置数据库类型
	 * 
	 * @param dbType
	 */
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	/**
	 * @param 设置数据源
	 */
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public Class<?> getObjectType() {
		return BaseTableMeta.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
