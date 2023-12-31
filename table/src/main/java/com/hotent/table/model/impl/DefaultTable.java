package com.hotent.table.model.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.hotent.table.model.Column;
import com.hotent.table.model.Table;

/**
 * 默认表对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月25日
 */
public class DefaultTable implements Table {
	// 表名
	private String name = "";
	// 表注释
	private String comment = "";
	// 列列表
	private List<Column> columnList = new ArrayList<Column>();

	public String getTableName() {
		return name;
	}

	public String getComment() {
		if (StringUtils.isNotEmpty(comment))
			comment = comment.replace("'", "''");
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * 添加列对象。
	 * 
	 * @param model
	 */
	public void addColumn(Column model) {
		this.columnList.add(model);
	}

	public List<Column> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<Column> columnList) {
		this.columnList = columnList;
	}

	public List<Column> getPrimayKey() {
		List<Column> pks = new ArrayList<Column>();
		for (Column column : columnList) {
			if (column.getIsPk())
				pks.add(column);
		}
		return pks;
	}

	@Override
	public String toString() {
		return "TableModel [name=" + name + ", comment=" + comment + ", columnList=" + columnList + "]";
	}

	@Override
	public void setTableName(String name) {
		this.name = name;

	}
}
