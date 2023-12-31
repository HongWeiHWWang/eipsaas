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
import com.hotent.table.colmap.OracleColumnMap;
import com.hotent.table.model.Column;
import com.hotent.table.model.Table;
import com.hotent.table.model.impl.DefaultTable;

/**
 * Oracle 表元数据的实现类
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月25日
 */
public class OracleTableMeta extends BaseTableMeta {
	/**
	 * 取得注释
	 */
	private String sqlTableComment = "select TABLE_NAME,DECODE(COMMENTS,null,TABLE_NAME,comments) comments from user_tab_comments  where table_type='TABLE' AND table_name ='%s'";

	/**
	 * 取得列表
	 */
	private final String SQL_GET_COLUMNS = "SELECT "
			+ " 	A.TABLE_NAME TABLE_NAME, "
			+ " 	A.COLUMN_NAME NAME, "
			+ " 	A.DATA_TYPE TYPENAME, "
			+ " 	A.DATA_LENGTH LENGTH,  "
			+ " 	A.DATA_PRECISION PRECISION, "
			+ " 	A.DATA_SCALE SCALE, "
			+ " 	A.DATA_DEFAULT, "
			+ " 	A.NULLABLE,  "
			+ " 	DECODE(B.COMMENTS,NULL,A.COLUMN_NAME,B.COMMENTS) DESCRIPTION, "
			+ " 	( " + "   	  SELECT " + "   	    COUNT(*) " + "   	  FROM  "
			+ "   	    USER_CONSTRAINTS CONS, "
			+ "    	   USER_CONS_COLUMNS CONS_C  " + "    	 WHERE  "
			+ "    	   CONS.CONSTRAINT_NAME=CONS_C.CONSTRAINT_NAME "
			+ "    	   AND CONS.CONSTRAINT_TYPE='P' "
			+ "    	   AND CONS.TABLE_NAME=B.TABLE_NAME "
			+ "     	  AND CONS_C.COLUMN_NAME=A.COLUMN_NAME "
			+ "  	 ) AS IS_PK " + " FROM  " + " 	 USER_TAB_COLUMNS A, "
			+ " 	USER_COL_COMMENTS B  " + " WHERE  "
			+ " 	A.COLUMN_NAME=B.COLUMN_NAME "
			+ " 	AND A.TABLE_NAME = B.TABLE_NAME " + " 	AND A.TABLE_NAME='%s' "
			+ " ORDER BY " + "  	A.COLUMN_ID";
	/**
	 * 取得列表
	 */
	private final String SQL_GET_COLUMNS_BATCH = "SELECT "
			+ " 	A.TABLE_NAME TABLE_NAME, "
			+ " 	A.COLUMN_NAME NAME, "
			+ " 	A.DATA_TYPE TYPENAME, "
			+ " 	A.DATA_LENGTH LENGTH,  "
			+ " 	A.DATA_PRECISION PRECISION, "
			+ " 	A.DATA_SCALE SCALE, "
			+ " 	A.DATA_DEFAULT, "
			+ " 	A.NULLABLE,  "
			+ " 	DECODE(B.COMMENTS,NULL,A.COLUMN_NAME,B.COMMENTS) DESCRIPTION, "
			+ " 	( " + "   	  SELECT " + "   	    COUNT(*) " + "   	  FROM  "
			+ "   	    USER_CONSTRAINTS CONS, "
			+ "    	   USER_CONS_COLUMNS CONS_C  " + "    	 WHERE  "
			+ "    	   CONS.CONSTRAINT_NAME=CONS_C.CONSTRAINT_NAME "
			+ "    	   AND CONS.CONSTRAINT_TYPE='P' "
			+ "    	   AND CONS.TABLE_NAME=B.TABLE_NAME "
			+ "     	  AND CONS_C.COLUMN_NAME=A.COLUMN_NAME "
			+ "  	 ) AS IS_PK " + " FROM  " + " 	USER_TAB_COLUMNS A, "
			+ " 	USER_COL_COMMENTS B  " + " WHERE  "
			+ " 	A.COLUMN_NAME=B.COLUMN_NAME "
			+ " 	AND A.TABLE_NAME = B.TABLE_NAME ";

	/**
	 * 取得数据库所有表
	 */
	private String sqlAllTables = "select TABLE_NAME,DECODE(COMMENTS,null,TABLE_NAME,comments) comments from user_tab_comments where table_type='TABLE'  ";

	/**
	 * 根据表名查询列表，如果表名为空则去系统所有的数据库表。
	 */
	@Override
	public Map<String, String> getTablesByName(String tableName) {
		String sql = sqlAllTables;
		if (StringUtils.isNotEmpty(tableName))
			sql = sqlAllTables + " and  lower(table_name) like '%"
					+ tableName.toLowerCase() + "%'";
		
		List<Map<String, String>> list = jdbcTemplate.query(sql,
				new RowMapper<Map<String, String>>() {
					@Override
					public Map<String, String> mapRow(ResultSet rs, int row)
							throws SQLException {
						String tableName = rs.getString("table_name");
						String comments = rs.getString("comments");
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
			map.put(name, comments);
		}

		return map;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, String> getTablesByName(List<String> names) {
		StringBuffer sb = new StringBuffer();
		for (String name : names) {
			sb.append("'");
			sb.append(name);
			sb.append("',");
		}
		sb.deleteCharAt(sb.length() - 1);
		String sql = sqlAllTables + " and  lower(table_name) in ("
				+ sb.toString().toLowerCase() + ")";

	
		List list = jdbcTemplate.query(sql, 
				new RowMapper<Map<String, String>>() {
					@Override
					public Map<String, String> mapRow(ResultSet rs, int row)
							throws SQLException {
						String tableName = rs.getString("TABLE_NAME");
						String comments = rs.getString("COMMENTS");
						Map<String, String> map = new HashMap<String, String>();
						map.put("NAME", tableName);
						map.put("COMMENTS", comments);
						return map;
					}
				});
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, String> tmp = (Map<String, String>) list.get(i);
			String name = tmp.get("NAME");
			String comments = tmp.get("COMMENTS");
			map.put(name, comments);
		}

		return map;
	}

	/**
	 * 获取表对象
	 */
	@Override
	public Table getTableByName(String tableName) {
		tableName = tableName.toUpperCase();
		Table model = getTable(tableName);
		// 获取列对象
		List<Column> columnList = getColumnsByTableName(tableName);
		model.setColumnList(columnList);
		return model;
	}

	/**
	 * 根据表名获取tableModel。
	 * 
	 * @param tableName
	 * @return
	 */
	private Table getTable(final String tableName) {
		String sql = String.format(sqlTableComment, tableName);
		Table tableModel = (Table) jdbcTemplate.queryForObject(sql, 
				new RowMapper<Table>() {

					@Override
					public Table mapRow(ResultSet rs, int row)
							throws SQLException {
						Table tableModel = new DefaultTable();
						tableModel.setTableName(tableName);
						tableModel.setComment(rs.getString("comments"));
						return tableModel;
					}
				});
		if (BeanUtils.isEmpty(tableModel))
			tableModel = new DefaultTable();

		return tableModel;
	}

	/**
	 * 根据表名获取列
	 * 
	 * @param tableName
	 * @return
	 */
	private List<Column> getColumnsByTableName(String tableName) {
		String sql = String.format(SQL_GET_COLUMNS, tableName);
		List<Column> columnList = jdbcTemplate.query(sql, 
				new OracleColumnMap());
		return columnList;
	}

	/**
	 * 根据表名获取列。此方法使用批量查询方式。
	 * 
	 * @param tableName
	 * @return
	 */
	private Map<String, List<Column>> getColumnsByTableName(
			List<String> tableNames) {
		String sql = SQL_GET_COLUMNS_BATCH;
		Map<String, List<Column>> map = new HashMap<String, List<Column>>();
		if (tableNames != null && tableNames.size() == 0) {
			return map;
		} else {
			StringBuffer buf = new StringBuffer();
			for (String str : tableNames) {
				buf.append("'" + str + "',");
			}
			buf.deleteCharAt(buf.length() - 1);
			sql += " AND A.TABLE_NAME IN (" + buf.toString() + ") ";
		}

		
		List<Column> columnModels = jdbcTemplate.query(sql, new OracleColumnMap());

		for (Column columnModel : columnModels) {
			String tableName = columnModel.getTableName();
			if (map.containsKey(tableName)) {
				map.get(tableName).add(columnModel);
			} else {
				List<Column> cols = new ArrayList<Column>();
				cols.add(columnModel);
				map.put(tableName, cols);
			}
		}
		return map;
	}

	@Override
	public List<Table> getTableModelByName(String tableName) throws Exception {
		String sql = sqlAllTables;

		if (StringUtils.isNotEmpty(tableName)) {
			sql += " AND  LOWER(table_name) LIKE '%" + tableName.toLowerCase()
					+ "%'";
		}
		RowMapper<Table> rowMapper = new RowMapper<Table>() {
			@Override
			public Table mapRow(ResultSet rs, int row) throws SQLException {
				Table tableModel = new DefaultTable();
				tableModel.setTableName(rs.getString("TABLE_NAME"));
				tableModel.setComment(rs.getString("COMMENTS"));
				return tableModel;
			}
		};
		
		List<Table> tableModels = jdbcTemplate.query(sql,  rowMapper);
		List<String> tableNames = new ArrayList<String>();
		// get all table names
		for (Table model : tableModels) {
			tableNames.add(model.getTableName());
		}
		// batch get table columns
		Map<String, List<Column>> tableColumnsMap = getColumnsByTableName(tableNames);
		// extract table columns from paraTypeMap by table name;
		for (Entry<String, List<Column>> entry : tableColumnsMap.entrySet()) {
			// set Table's columns
			for (Table model : tableModels) {
				if (model.getTableName().equalsIgnoreCase(entry.getKey())) {
					model.setColumnList(entry.getValue());
				}
			}
		}

		return tableModels;
	}

	@Override
	public String getAllTableSql() {
		return this.sqlAllTables;
	}

}
