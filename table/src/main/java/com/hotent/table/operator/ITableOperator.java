package com.hotent.table.operator;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.hotent.table.meta.IDbType;
import com.hotent.table.model.Column;
import com.hotent.table.model.Table;

/**
 * 数据表操作接口。 对每一个数据库写一个实现，实现对不同数据库的统一操作
 * 
 * @开发公司：广州宏天软件股份有限公司
 * @作者：heyifan
 * @邮箱：heyf@jee-soft.cn
 * @创建时间：2018年4月3日
 */
public interface ITableOperator extends IDbType {
	/**
	 * 获取字段类型
	 * 
	 * @param columnType		字段类型，类型静态常量在{@link Column}中
	 * @param charLen           字符串长度
	 * @param intLen            整型长度
	 * @param decimalLen        小数长度
	 * @return 字段类型
	 */
	String getColumnType(String columnType, int charLen, int intLen,int decimalLen);

	/**
	 * 获取字段类型
	 * 
	 * @param column
	 *            字段
	 * @return 字段类型
	 */
	String getColumnType(Column column);

	/**
	 * 根据Table创建表
	 * 
	 * @param table
	 *            表
	 * @throws SQLException
	 */
	void createTable(Table table) throws SQLException;

	/**
	 * 根据表名删除表
	 * 
	 * @param tableName 表名
	 */
	void dropTable(String tableName) throws SQLException;

	/**
	 * 修改表的注释
	 * 
	 * @param tableName            表名
	 * @param comment			        表注释
	 * @throws SQLException
	 */
	void updateTableComment(String tableName, String comment) throws SQLException;
	
	/**
	 * 添加外键
	 * 
	 * @param pkTableName
	 *            主键表
	 * @param fkTableName
	 *            外键表
	 * @param pkField
	 *            主键
	 * @param fkField
	 *            外键
	 */
	void addForeignKey(String pkTableName, String fkTableName,
			String pkField, String fkField);

	/**
	 * 删除外键
	 * 
	 * @param tableName 表名
	 * @param keyName 外键名
	 */
	void dropForeignKey(String tableName, String keyName);

	/**
	 * 在表中添加列
	 * @param tableName            表名
	 * @param column               字段
	 * @throws SQLException
	 */
	void addColumn(String tableName, Column column) throws SQLException;

	/**
	 * 在表中删除列
	 * @param tableName 表名
	 * @param columnName 字段名
	 * @throws SQLException
	 */
	void dropColumn(String tableName, String columnName) throws SQLException;

	/**
	 * 更新列
	 * <pre>
	 * 可以修改列名，字段类型，字段是否非空，字段的注释。
	 * </pre>
	 * @param tableName
	 *            表名
	 * @param columnName
	 *            列名
	 * @param column
	 *            字段
	 * @throws SQLException
	 */
	void updateColumn(String tableName, String columnName, Column column) throws SQLException;

	/**
	 * 根据表名，取得相应的主键的列表
	 * 
	 * @param tableName
	 *            表名
	 * @return 主键列表
	 * @throws SQLException
	 */
	List<String> getPKColumns(String tableName) throws SQLException;

	/**
	 * 根据表名列表，取得相应的主键列表
	 * <pre>
	 * 返回Map格式结果，Map中:key=表名，value=相应的主键的列列表
	 * </pre>
	 * 
	 * @param tableNames
	 *            表名列表
	 * @return 表名、主键列列表
	 * @throws SQLException
	 */
	Map<String, List<String>> getPKColumns(List<String> tableNames) throws SQLException;

	/**
	 * 判断表是否存在
	 * 
	 * @param tableName 表名
	 * @return 表是否存在
	 */
	boolean isTableExist(String tableName);

	/**
	 * 判断指定表的分区是否存在
	 * @param tableName 表名
	 * @param partition 分区
	 * @return 表分区是否存在
	 */
	boolean isExsitPartition(String tableName,String partition);

	/**
	 * 创建分区
	 * @param tableName 表名
	 * @param partition 分区
	 */
	void createPartition(String tableName,String partition);

	/**
	 * 是否支持分区
	 * <pre>
	 * 判断表是否支持创建分区
	 * </pre>
	 * @param tableName 表名
	 * @return 是否支持分区
	 */
	boolean supportPartition(String tableName);
}
