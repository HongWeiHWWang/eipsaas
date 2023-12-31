package com.hotent.table.factory;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.jdbc.core.JdbcTemplate;

import com.hotent.base.constants.SQLConst;
import com.hotent.table.operator.IIndexOperator;

/**
 * IndexOperator factorybean，用户创建IIndexOperator对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月25日
 */
public class IndexOperatorFactoryBean implements FactoryBean<IIndexOperator> {

	private IIndexOperator indexOperator;

	private String dbType = SQLConst.DB_MYSQL;

	private JdbcTemplate jdbcTemplate;

	@Override
	public IIndexOperator getObject() throws Exception {
		indexOperator = DatabaseFactory.getIndexOperator(dbType);
		
		indexOperator.setJdbcTemplate(jdbcTemplate);
		return indexOperator;
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
	 * 设置jdbcTemplate
	 * 
	 * @param jdbcTemplate
	 */
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Class<?> getObjectType() {
		return IIndexOperator.class;
	}

	@Override
	public boolean isSingleton() {

		return true;
	}
}
