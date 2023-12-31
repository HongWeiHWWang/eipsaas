package com.hotent.table.operator.impl.sqlserver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.table.colmap.SQLServerColumnMap;
import com.hotent.table.model.Column;
import com.hotent.table.model.Table;
import com.hotent.table.model.impl.DefaultTable;
import com.hotent.table.operator.IViewOperator;
import com.hotent.table.operator.impl.BaseViewOperator;

/**
 * SQLServer 视图操作的实现类
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月25日
 */
public class SQLServerViewOperator extends BaseViewOperator implements
		IViewOperator {

	private final String sqlAllView = "select name from sysobjects where xtype='V'";

	private final String SQL_GET_COLUMNS_BATCH = "SELECT "
			+ "B.NAME TABLE_NAME,A.NAME NAME, C.NAME TYPENAME, A.MAX_LENGTH LENGTH, A.IS_NULLABLE IS_NULLABLE,A.PRECISION PRECISION,A.SCALE SCALE,  "
			+ "(SELECT COUNT(*) FROM SYS.IDENTITY_COLUMNS WHERE SYS.IDENTITY_COLUMNS.OBJECT_ID = A.OBJECT_ID AND A.COLUMN_ID = SYS.IDENTITY_COLUMNS.COLUMN_ID) AS AUTOGEN, "
			+ "(SELECT CAST(VALUE AS VARCHAR) FROM SYS.EXTENDED_PROPERTIES WHERE SYS.EXTENDED_PROPERTIES.MAJOR_ID = A.OBJECT_ID AND SYS.EXTENDED_PROPERTIES.MINOR_ID = A.COLUMN_ID) AS DESCRIPTION, "
			+ " 0 AS IS_PK " + "FROM  "
			+ "SYS.COLUMNS A, SYS.VIEWS B, SYS.TYPES C " + "WHERE  "
			+ "A.OBJECT_ID = B.OBJECT_ID  "
			+ "AND A.SYSTEM_TYPE_ID=C.SYSTEM_TYPE_ID "
			+ "AND C.NAME<>'SYSNAME' ";

	@Override
	public void createOrRep(String viewName, String sql) throws Exception {
	
		String sql_drop_view = "IF EXISTS (SELECT * FROM sysobjects WHERE xtype='V'"
				+ " AND name = '" + viewName + "')" + " DROP VIEW " + viewName;

		String viewSql = "CREATE VIEW " + viewName + " AS " + sql;
		jdbcTemplate.execute(sql_drop_view);
		jdbcTemplate.execute(viewSql);
	}

	@Override
	public PageList<String> getViews(String viewName) throws SQLException {
		String sql = sqlAllView;
		if (StringUtils.isNotEmpty(viewName))
			sql += " and name like '" + viewName + "%'";
		return new PageList<String>(this.jdbcTemplate.queryForList(sql, String.class));
	}

	@Override
	public PageList<String> getViews(String viewName, PageBean pageBean)
			throws SQLException, Exception {
		String sql = sqlAllView;
		if (StringUtils.isNotEmpty(viewName))
			sql += " AND NAME LIKE '" + viewName + "%'";
		return super.getForList(sql, pageBean, String.class);
	}

	@Override
	public PageList<Table> getViewsByName(String viewName, PageBean pageBean)
			throws Exception {
		String sql = sqlAllView;
		if (StringUtils.isNotEmpty(viewName)) {
			sql += " AND NAME LIKE '" + viewName + "%'";
		}

		RowMapper<Table> rowMapper = new RowMapper<Table>() {
			@Override
			public Table mapRow(ResultSet rs, int row) throws SQLException {
				Table tableModel = new DefaultTable();
				tableModel.setTableName(rs.getString("NAME"));
				tableModel.setComment(tableModel.getTableName());
				return tableModel;
			}
		};
		PageList<Table> tableModels = getForList(sql, pageBean, rowMapper);
		// 获取列对象
		List<String> tableNames = new ArrayList<String>();
		// get all table names
		for (Table model : tableModels.getRows()) {
			tableNames.add(model.getTableName());
		}
		// batch get table columns
		Map<String, List<Column>> tableColumnsMap = getColumnsByTableName(tableNames);
		// extract table columns from paraTypeMap by table name;
		for (Entry<String, List<Column>> entry : tableColumnsMap.entrySet()) {
			// set Table's columns
			for (Table model : tableModels.getRows()) {
				if (model.getTableName().equalsIgnoreCase(entry.getKey())) {
					model.setColumnList(entry.getValue());
				}
			}
		}
		return tableModels;
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
			sql += " AND B.NAME IN (" + buf.toString() + ") ";
		}
		List<Column> columnModels= jdbcTemplate.query(sql, new SQLServerColumnMap());
		
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
	public String getType(String type) {
		type = type.toLowerCase();
		if (type.indexOf("number") > -1)
			return Column.COLUMN_TYPE_NUMBER;
		else if (type.indexOf("date") > -1) {
			return Column.COLUMN_TYPE_DATE;
		} else if (type.indexOf("char") > -1) {
			return Column.COLUMN_TYPE_VARCHAR;
		}
		return Column.COLUMN_TYPE_VARCHAR;
	}

	@Override
	public Table getModelByViewName(String viewName) throws SQLException {
		return super.getModelByViewName(viewName);
	}
}
