package com.hotent.table.meta.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import com.hotent.base.util.BeanUtils;
import com.hotent.table.colmap.MySQLColumnMap;
import com.hotent.table.colmap.PostgreSQLColumnMap;
import com.hotent.table.model.Column;
import com.hotent.table.model.Table;
import com.hotent.table.model.impl.DefaultTable;

/**
 * postgreSQL 表元数据的实现类
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月25日
 */
public class PostgreSQLTableMeta extends BaseTableMeta {

	private final String SQL_GET_COLUMNS = "SELECT 	TABLE_NAME, COLUMN_NAME, tmp.column_comment, tmp.is_primary, IS_NULLABLE, DATA_TYPE, udt_name, CHARACTER_MAXIMUM_LENGTH LENGTH, NUMERIC_PRECISION PRECISIONS, NUMERIC_SCALE SCALE FROM 	information_schema. COLUMNS C INNER JOIN ( 	SELECT 	A .attname, col_description (A .attrelid, A .attnum) AS column_comment, ( 	CASE WHEN ( SELECT COUNT (*) FROM pg_constraint  WHERE conrelid = A .attrelid AND conkey [ 1 ]= attnum  AND contype = 'p'  ) > 0 THEN  'Y'  ELSE  'N'  END ) AS is_primary 	FROM  pg_attribute A 	INNER JOIN pg_class b ON A .attrelid = b.oid 	WHERE 		b.relname = '%s' 	AND A .attstattarget = '-1' ) tmp ON C . COLUMN_NAME = tmp.attname WHERE 	table_schema = 'public' AND TABLE_NAME = '%s'";

	private final String SQL_GET_COLUMNS_BATCH = "SELECT 	TABLE_NAME, COLUMN_NAME, tmp.column_comment, tmp.is_primary, IS_NULLABLE, DATA_TYPE, udt_name, CHARACTER_MAXIMUM_LENGTH LENGTH, NUMERIC_PRECISION PRECISIONS, NUMERIC_SCALE SCALE FROM 	information_schema. COLUMNS C INNER JOIN ( 	SELECT 	A .attname, col_description (A .attrelid, A .attnum) AS column_comment, ( 	CASE WHEN ( SELECT COUNT (*) FROM pg_constraint  WHERE conrelid = A .attrelid AND conkey [ 1 ]= attnum  AND contype = 'p'  ) > 0 THEN  'Y'  ELSE  'N'  END ) AS is_primary 	FROM  pg_attribute A 	INNER JOIN pg_class b ON A .attrelid = b.oid 	WHERE 		b.relname in (%s) 	AND A .attstattarget = '-1' ) tmp ON C . COLUMN_NAME = tmp.attname WHERE 	table_schema = 'public' AND TABLE_NAME in (%s)";

	private final String sqlComment = "select relname as table_name,cast(obj_description(relfilenode,'pg_class') as varchar) as table_comment from pg_class c where relname ='%s' ";

	private final String sqlAllTable = "select relname as table_name,cast(obj_description(relfilenode,'pg_class') as varchar) as table_comment from pg_class c " + 
			"where relname in (select tablename from pg_tables where schemaname='public' and position('_2' in tablename)=0 %s) ORDER BY oid";

	@Override
	public Table getTableByName(String tableName) {
		Table model = getTableModel(tableName);
		// 获取列对象
		List<Column> columnList = getColumnsByTableName(tableName);
		model.setColumnList(columnList);
		return model;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, String> getTablesByName(String tableName) {
		String whereClause = "";
		if (StringUtils.isNotEmpty(tableName)) {
			whereClause = String.format(" AND tablename LIKE '%%%s%%'", tableName) ;
		}
		String sql = String.format(sqlAllTable, whereClause);
		List list = jdbcTemplate.query(sql, 
				new RowMapper<Map<String, String>>() {
					@Override
					public Map<String, String> mapRow(ResultSet rs, int row)
							throws SQLException {
						String tableName = rs.getString("table_name");
						String comments = rs.getString("table_comment");
						Map<String, String> map = new HashMap<String, String>();
						map.put("name", tableName);
						map.put("comments", comments);
						return map;
					}
				});
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, String> tmp = (Map<String, String>) list.get(i);
			String name = tmp.get("name");
			String comments = tmp.get("comments");
			comments = getComments(comments, name);
			map.put(name, comments);
		}

		return map;
	}

	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, String> getTablesByName(List<String> names) {
		String whereClause = "";
		if (BeanUtils.isNotEmpty(names)) {
			StringBuffer sb = new StringBuffer();
			for (String name : names) {
				sb.append("'");
				sb.append(name);
				sb.append("',");
			}
			sb.deleteCharAt(sb.length() - 1);
			whereClause = String.format(" and tablename in (%s)", sb.toString().toLowerCase());
		}
		
		String sql = String.format(sqlAllTable, whereClause);
		
		List list = jdbcTemplate.query(sql,
				new RowMapper<Map<String, String>>() {
					@Override
					public Map<String, String> mapRow(ResultSet rs, int row)
							throws SQLException {
						String tableName = rs.getString("table_name");
						String comments = rs.getString("table_comment");
						Map<String, String> map = new HashMap<String, String>();
						map.put("tableName", tableName);
						map.put("tableComment", comments);
						return map;
					}
				});
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, String> tmp = (Map<String, String>) list.get(i);
			String name = tmp.get("tableName");
			String comments = tmp.get("tableComment");
			map.put(name, comments);
		}

		return map;
	}

	/**
	 * 根据表名获取tableModel。
	 * 
	 * @param tableName
	 * @return
	 */
	private Table getTableModel(final String tableName) {
		String sql = String.format(sqlComment, tableName);
		Table table = (Table) jdbcTemplate.queryForObject(sql,
				new RowMapper<Table>() {
					@Override
					public Table mapRow(ResultSet rs, int row)
							throws SQLException {
						Table table = new DefaultTable();
						String comments = rs.getString("table_comment");
						comments = getComments(comments, tableName);
						table.setTableName(tableName);
						table.setComment(comments);
						return table;
					}
				});
		if (BeanUtils.isEmpty(table))
			table = new DefaultTable();

		return table;
	}

	/**
	 * 根据表名获取列
	 * 
	 * @param tableName
	 * @return
	 */
	private List<Column> getColumnsByTableName(String tableName) {
		String sql = String.format(SQL_GET_COLUMNS, tableName, tableName);
	
		// sqlColumns语句的column_key包含了column是否为primary
		// key，并在MySqlColumnMap中进行了映射。
		List<Column> list = jdbcTemplate.query(sql,
				new PostgreSQLColumnMap());
		for (Column model : list) {
			model.setTableName(tableName);
		}
		return list;
	}

	/**
	 * 根据表名获取列。此方法使用批量查询方式。
	 * 
	 * @param tableName
	 * @return
	 */
	private Map<String, List<Column>> getColumnsByTableName(
			List<String> tableNames) {
		Map<String, List<Column>> map = new HashMap<String, List<Column>>();
		if (tableNames != null && tableNames.size() == 0) {
			return map;
		}
		StringBuffer buf = new StringBuffer();
		for (String str : tableNames) {
			buf.append("'" + str + "',");
		}
		buf.deleteCharAt(buf.length() - 1);
		String names = buf.toString();
		String sql = String.format(SQL_GET_COLUMNS_BATCH, names, names) ;
		List<Column> Columns = jdbcTemplate.query(sql,new MySQLColumnMap());
		for (Column column : Columns) {
			String tableName = column.getTableName();
			if (map.containsKey(tableName)) {
				map.get(tableName).add(column);
			} else {
				List<Column> cols = new ArrayList<Column>();
				cols.add(column);
				map.put(tableName, cols);
			}
		}
		return map;
	}

	/**
	 * 获取注释
	 * 
	 * @param comments
	 * @param defaultValue
	 * @return
	 */
	public static String getComments(String comments, String defaultValue) {
		if (StringUtils.isEmpty(comments))
			return defaultValue;
		int idx = comments.indexOf("InnoDB free");
		if (idx > -1) 
			comments = StringUtils.remove(comments.substring(0, idx).trim(),
					";");
		if (StringUtils.isEmpty(comments)) 
			comments = defaultValue;
		return comments;
	}

	@Override
	public List<Table> getTableModelByName(String tableName) throws Exception {
		String whereClause = "";
		if (StringUtils.isNotEmpty(tableName)) {
			whereClause = String.format(" AND TABLE_NAME LIKE '%%%s%%'", tableName) ;
		}
		String sql = String.format(sqlAllTable, whereClause);
		RowMapper<Table> rowMapper = new RowMapper<Table>() {
			@Override
			public Table mapRow(ResultSet rs, int row) throws SQLException {
				Table table = new DefaultTable();
				table.setTableName(rs.getString("TABLE_NAME"));
				String comments = rs.getString("TABLE_COMMENT");
				comments = getComments(comments, table.getTableName());
				table.setComment(comments);
				return table;
			}
		};
		List<Table> tables = jdbcTemplate.query (sql,  rowMapper);

		List<String> tableNames = new ArrayList<String>();
		// get all table names
		for (Table model : tables) {
			tableNames.add(model.getTableName());
		}
		// batch get table columns
		Map<String, List<Column>> tableColumnsMap = getColumnsByTableName(tableNames);
		// extract table columns from paraTypeMap by table name;
		for (Entry<String, List<Column>> entry : tableColumnsMap.entrySet()) {
			// set TableModel's columns
			for (Table model : tables) {
				if (model.getTableName().equalsIgnoreCase(entry.getKey())) {
					model.setColumnList(entry.getValue());
				}
			}
		}
		return tables;
	}

	@Override
	public String getAllTableSql() {
		return String.format(this.sqlAllTable, "");
	}
}
