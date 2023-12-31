package com.hotent.table.meta;

import java.util.List;
import java.util.Map;

import com.hotent.table.model.Table;

/**
 * 表元数据
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月25日
 */
public interface ITableMeta {

	/**
	 * 根据表名，取得表模型。此处对表名进行非模糊匹配。
	 * 
	 * @param tableName
	 *            表名
	 * @return 表模型
	 */
	Table getTableByName(String tableName);

	/**
	 * 根据表名，使用模糊匹配，查询系统中的表
	 * <pre>
	 * 返回Map格式结果，key=表名，value=表注解
	 * </pre>
	 * 
	 * @param tableName 表名
	 * @return 表名、表注解
	 */
	Map<String, String> getTablesByName(String tableName);

	/**
	 * 根据表名,使用模糊匹配，查询系统中的表
	 * 
	 * @param tableName 表名
	 * @return 表集合
	 * @throws Exception
	 */
	List<Table> getTableModelByName(String tableName) throws Exception;

	/**
	 * 根据表名查询系统中的表
	 * <pre>
	 * 返回的Map中：key=表名，value=表注解
	 * </pre>
	 * 
	 * @param tableName 表名列表
	 * @return 表名、表注解
	 */
	Map<String, String> getTablesByName(List<String> names);
	
	/**
	 * 查询所有的表的SQL语句
	 * @return SQL语句
	 */
	String getAllTableSql();
}
