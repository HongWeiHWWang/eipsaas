package com.hotent.table.operator;

import java.sql.SQLException;
import java.util.List;

import com.hotent.table.meta.IDbType;
import com.hotent.table.model.Index;

/**
 * 索引操作接口
 * 
 * @开发公司：广州宏天软件股份有限公司
 * @作者：heyifan
 * @邮箱：heyf@jee-soft.cn
 * @创建时间：2018年4月3日
 */
public interface IIndexOperator extends IDbType {
	
	/**
	 * 创建索引
	 * @param index 索引对象
	 * @throws SQLException
	 */
	void createIndex(Index index) throws SQLException;

	/**
	 * 根据表名和索引名，删除表名和索引名对应的索引.所有实现使用精确匹配方式。
	 * 
	 * @param tableName
	 *            表名
	 * @param indexName
	 *            索引名
	 */
	void dropIndex(String tableName, String indexName);

	/**
	 * 根据表名和索引名，取得表名和索引名对应的索引.所有实现使用精确匹配方式。
	 * 
	 * @param tableName
	 *            表名
	 * @param indexName
	 *            索引名
	 * @return 索引
	 * @throws SQLException
	 */
	Index getIndex(String tableName, String indexName) throws SQLException;

	/**
	 * 通过索引名查询索引
	 * @param indexName
	 * @return 索引列表
	 * @throws SQLException
	 */
	List<Index> getIndexByName(String indexName) throws SQLException;

	/**
	 * 根据表名，取得表名对应的索引列表.所有实现使用精确匹配方式。
	 * 
	 * @param tableName
	 *            表名
	 * @return 索引列表
	 * @throws SQLException
	 */
	List<Index> getIndexsByTable(String tableName) throws SQLException;

	/**
	 * 重建索引
	 * 
	 * @param tableName
	 *            表名
	 * @param indexName
	 *            索引名
	 */
	void rebuildIndex(String tableName, String indexName);
}
