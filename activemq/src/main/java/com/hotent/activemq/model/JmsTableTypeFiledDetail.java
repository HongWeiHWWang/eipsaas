package com.hotent.activemq.model;

import java.io.Serializable;

/**
 * JMS消息
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年10月9日
 */
public class JmsTableTypeFiledDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3363346153708470650L;
	/**
	 *  实体表名
	 */
	protected String tableName;
	/**
	 * 实体主键名
	 */
	protected String pkFiledName;
	/**
	 * 实体分类id字段名
	 */
	protected String typeIdFiledName;
	
	/**
	 * 实体分类名称字段名
	 */
	protected String typeNameFiledName;
	
	
	public JmsTableTypeFiledDetail(String tableName,String pkFiledName, String typeIdFiledName, String typeNameFiledName) {
		super();
		this.tableName = tableName;
		this.typeIdFiledName = typeIdFiledName;
		this.pkFiledName = pkFiledName;
		this.typeNameFiledName = typeNameFiledName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTypeIdFiledName() {
		return typeIdFiledName;
	}

	public void setTypeIdFiledName(String typeIdFiledName) {
		this.typeIdFiledName = typeIdFiledName;
	}

	public String getTypeNameFiledName() {
		return typeNameFiledName;
	}

	public void setTypeNameFiledName(String typeNameFiledName) {
		this.typeNameFiledName = typeNameFiledName;
	}

	public String getPkFiledName() {
		return pkFiledName;
	}

	public void setPkFiledName(String pkFiledName) {
		this.pkFiledName = pkFiledName;
	}

	
}
