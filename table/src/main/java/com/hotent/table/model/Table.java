package com.hotent.table.model;

import java.util.List;

/**
 * 表对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月3日
 */
public interface Table {

	/**
	 * 返回表名
	 * 
	 * @return 表名
	 */
	public String getTableName();

	/**
	 * 返回注释
	 * 
	 * @return 注释
	 */
	public String getComment();

	/**
	 * 返回列列表
	 * 
	 * @return 列列表
	 */
	public List<Column> getColumnList();

	/**
	 * 返回主键
	 * 
	 * @return 主键
	 */
	public List<Column> getPrimayKey();

	/**
	 * 设置表名
	 * @param name 表名
	 */
	public void setTableName(String name);

	/**
	 * 设置表注释
	 * @param comment 表注释
	 */
	public void setComment(String comment);

	/**
	 * 设置字段列表
	 * @param columns 字段列表
	 */
	public void setColumnList(List<Column> columns);
	
	/**
	 * 增加字段
	 * @param column 字段
	 */
	public void addColumn(Column column);

}
