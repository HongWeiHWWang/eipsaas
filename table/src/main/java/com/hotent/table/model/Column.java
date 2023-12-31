package com.hotent.table.model;

/**
 * 列对象，用于产生数据库列
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月3日
 */
public interface Column {

	/** 字符串 */
	String COLUMN_TYPE_VARCHAR = "varchar";
	/** 大文本 */
	String COLUMN_TYPE_CLOB = "clob";
	/** 数字 */
	String COLUMN_TYPE_NUMBER = "number";
	/** 整型 */
	String COLUMN_TYPE_INT = "int";
	/** 日期 */
	String COLUMN_TYPE_DATE = "date";

	/**
	 * 获取列名
	 * 
	 * @return 列名
	 */
	public String getFieldName();

	/**
	 * 获取列注释
	 * 
	 * @return 列注释
	 */
	public String getComment();

	/**
	 * 获取是否主键
	 * 
	 * @return 是否主键
	 */
	public boolean getIsPk();

	/**
	 * 获取是否可为空
	 * 
	 * @return 是否可为空
	 */
	public boolean getIsNull();

	/**
	 * 获取列类型
	 * 
	 * @return 列类型
	 */
	public String getColumnType();
	
	/**
	 * 获取列实际类型
	 * 
	 * @return 列实际类型
	 */
	public String getFcolumnType();

	/**
	 * 获取字符串长度
	 * 
	 * @return 字符串长度
	 */
	public int getCharLen();

	/**
	 * 获取整数位长度
	 * 
	 * @return 整数位长度
	 */
	public int getIntLen();

	/**
	 * 获取小数位长度
	 * 
	 * @return 小数位长度
	 */
	public int getDecimalLen();

	/**
	 * 获取默认值
	 * 
	 * @return 默认值
	 */
	public String getDefaultValue();

	/**
	 * 获取表名
	 * 
	 * @return 表名
	 */
	public String getTableName();

	/**
	 * 设置列名
	 * 
	 * @param name 列名
	 */
	public void setFieldName(String name);

	/**
	 * 设置列类型
	 * 
	 * @param columnType 列类型
	 */
	public void setColumnType(String columnType);
	/**
	 * 设置类实际类型
	 * 
	 * @param columnType 列实际类型
	 */
	public void setFcolumnType(String columnType);

	/**
	 * 设置列注释
	 * 
	 * @param comment 列注释
	 */
	public void setComment(String comment);

	/**
	 * 设置是否为空
	 * 
	 * @param isNull 是否为空
	 */
	public void setIsNull(boolean isNull);

	/**
	 * 设置是否是主键
	 * 
	 * @param isPk 是否主键
	 */
	public void setIsPk(boolean isPk);

	/**
	 * 设置字符串长度
	 * 
	 * @param charLen 字符串长度
	 */
	public void setCharLen(int charLen);

	/**
	 * 设置整数的长度
	 * 
	 * @param intLen 整数的长度
	 */
	public void setIntLen(int intLen);

	/**
	 * 设置小数长度
	 * 
	 * @param decimalLen 小数长度
	 */
	public void setDecimalLen(int decimalLen);

	/**
	 * 设置默认值
	 * 
	 * @param defaultValue 默认值
	 */
	public void setDefaultValue(String defaultValue);

	/**
	 * 设置表名
	 * 
	 * @param tableName 表名
	 */
	public void setTableName(String tableName);
	
	/**
	 * 获取是否必填
	 * 
	 * @return 是否必填
	 */
	int getIsRequired();
	
	/**
	 * 设置是否必填
	 * 
	 * @param isRequired 是否必填
	 */
	void setIsRequired(int isRequired);

}
