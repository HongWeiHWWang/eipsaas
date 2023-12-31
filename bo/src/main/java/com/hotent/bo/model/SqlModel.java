package com.hotent.bo.model;

/**
 * 用于执行SQL
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月12日
 */
public class SqlModel {
	/**
	 * sql语句
	 */
	private String  sql="";
	
	/**
	 * 更新的值
	 */
	private Object[] values;
	
	public SqlModel(){}
	
	public SqlModel( String sql, Object[] values) {
		this.sql = sql;
		this.values = values;
	}
	
	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Object[] getValues() {
		return values;
	}

	public void setValues(Object[] values) {
		this.values = values;
	}
}
