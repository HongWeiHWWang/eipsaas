package com.hotent.table.model;

import java.util.List;

/**
 * 索引
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月3日
 */
public interface Index {
	// 索引类型
	/** 位图索引 */
	String INDEX_TYPE_BITMAP = "BITMAP";
	/** B树索引 */
	String INDEX_TYPE_BTREE = "BTREE";
	/** 函数索引 */
	String INDEX_TYPE_FUNCTION = "FUNCTION";
	/** 堆索引 */
	String INDEX_TYPE_HEAP = "HEAP";
	/** 聚合索引 */
	String INDEX_TYPE_CLUSTERED = "CLUSTERED";
	/** 非聚合索引 */
	String INDEX_TYPE_NONCLUSTERED = "NONCLUSTERED";
	/** XML索引 */
	String INDEX_TYPE_XML = "XML";
	/** 空间索引 */
	String INDEX_TYPE_SPATIAL = "SPATIAL";
	/** 常规索引 */
	String INDEX_TYPE_REG = "REGULAR";
	/** 多维块索引 */
	String INDEX_TYPE_DIM = "DIMENSIONBLOCK";
	/** 块索引 */
	String INDEX_TYPE_BLOK = "BLOCK";
	// 表类型
	/** 表 */
	String TABLE_TYPE_TABLE = "TABLE";
	/** 视图 */
	String TABLE_TYPE_VIEW = "VIEW";
	// 索引状态
	/** 有效的 */
	String INDEX_STATUS_VALIDATE = "VALIDATE";
	/** 无效的 */
	String INDEX_STATUS_INVALIDATE = "INVALIDATE";

	/**
	 * 获取表名
	 * 
	 * @return 表名
	 */
	public String getTableName();

	/**
	 * 获取索引名
	 * 
	 * @return 索引名
	 */
	public String getIndexName();

	/**
	 * 是否是唯一索引
	 * 
	 * @return 是否唯一索引
	 */
	public boolean isUnique();

	/**
	 * 获取索引类型
	 * 
	 * @return 索引类型
	 */
	public String getIndexType();

	/**
	 * 获取索引描述
	 * 
	 * @return 索引描述
	 */
	public String getIndexComment();

	/**
	 * 获取字段列表
	 * @return 字段列表
	 */
	public List<String> getColumnList();
	
	/**
	 * 设置表名
	 * @param tableName 表名
	 */
	public void setTableName(String tableName);
	
	/**
	 * 设置表类型
	 * @param tableType 表类型
	 */
	public void setTableType(String tableType);
	
	/**
	 * 设置索引名称
	 * @param indexName 索引名称
	 */
	public void setIndexName(String indexName);
	
	/**
	 * 设置索引类型
	 * @param indexType 索引类型
	 */
	public void setIndexType(String indexType);
	
	/**
	 * 设置是否唯一索引
	 * @param unique 是否唯一索引
	 */
	public void setUnique(boolean unique);
	
	/**
	 * 设置索引状态
	 * @param indexStatus 索引状态
	 */
	public void setIndexStatus(String indexStatus);
	
	/**
	 * 设置索引Ddl
	 * @param indexDdl 索引Ddl
	 */
	public void setIndexDdl(String indexDdl);
	
	/**
	 * 设置列列表
	 * @param columnList 列列表
	 */
	public void setColumnList(List<String> columnList);
	
	/**
	 * 设置是否主键索引
	 * @param b 是否主键索引
	 */
	public void setPkIndex(boolean b);
	
	/**
	 * 设置索引描述
	 * @param indexComment 索引描述
	 */
	public void setIndexComment(String indexComment);
}
