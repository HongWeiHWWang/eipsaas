package com.hotent.table.factory;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.jdbc.core.JdbcTemplate;

import com.hotent.base.constants.SQLConst;
import com.hotent.table.operator.IViewOperator;

/**
 * IViewOperator工厂类
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月10日
 */
public class ViewOperatorFactoryBean implements FactoryBean<IViewOperator>{
	// 表操作
	private IViewOperator viewOperator;
	// 数据库类型
	private String dbType = SQLConst.DB_MYSQL;
	// JdbcTemplate
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public IViewOperator getObject() throws Exception {
		viewOperator = DatabaseFactory.getViewOperator(dbType);
		viewOperator.setJdbcTemplate(jdbcTemplate);
		return viewOperator;
	}
	
	/**
	 * 设置数据库类型
	 * 
	 * @param dbType 数据库类型
	 */
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	
	/**
	 * 设置jdbcTemplate
	 * 
	 * @param jdbcTemplate
	 */
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Class<?> getObjectType() {
		return IViewOperator.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
