package com.hotent.table.factory;

import javax.annotation.Resource;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.jdbc.core.JdbcTemplate;
import com.hotent.base.constants.SQLConst;
import com.hotent.table.operator.ITableOperator;

/**
 * TableOperator factorybean，用户创建ITableOperator对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月25日
 */
public class TableOperatorFactoryBean implements FactoryBean<ITableOperator> {
	// 表操作
	private ITableOperator tableOperator;
	// 数据库类型
	private String dbType = SQLConst.DB_MYSQL;
	@Resource
	private JdbcTemplate jdbcTemplate ;

	@Override
	public ITableOperator getObject() throws Exception {
		tableOperator = DatabaseFactory.getTableOperator(dbType);
		tableOperator.setJdbcTemplate(jdbcTemplate);
		return tableOperator;
	}

	/**
	 * 设置数据库类型
	 * 
	 * @param dbType
	 */
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	@Override
	public Class<?> getObjectType() {
		return ITableOperator.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
