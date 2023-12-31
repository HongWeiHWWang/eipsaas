package com.hotent.table.operator.impl.db2;

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
import com.hotent.base.util.BeanUtils;
import com.hotent.table.colmap.DB2ColumnMap;
import com.hotent.table.model.Column;
import com.hotent.table.model.Table;
import com.hotent.table.model.impl.DefaultTable;
import com.hotent.table.operator.IViewOperator;
import com.hotent.table.operator.impl.BaseViewOperator;

/**
 * DB2 视图操作的实现类
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月25日
 */
public class DB2ViewOperator extends BaseViewOperator implements IViewOperator {

	private static final String SQL_GET_ALL_VIEW = "SELECT " + "VIEWNAME "
			+ "FROM " + "SYSCAT.VIEWS " + "WHERE  "
			+ "VIEWSCHEMA IN (SELECT CURRENT SQLID FROM SYSIBM.DUAL) ";

	private final String SQL_GET_COLUMNS_BATCH = "SELECT "
			+ "TABNAME TAB_NAME, " + "COLNAME COL_NAME, "
			+ "TYPENAME COL_TYPE, " + "REMARKS COL_COMMENT, "
			+ "NULLS IS_NULLABLE, " + "LENGTH LENGTH, " + "SCALE SCALE, "
			+ "KEYSEQ  " + "FROM  " + "SYSCAT.COLUMNS " + "WHERE  "
			+ "TABSCHEMA IN (SELECT CURRENT SQLID FROM SYSIBM.DUAL) ";

	private static final String SQL_GET_COLUMNS = "SELECT "
			+ "TABNAME TAB_NAME, " + "COLNAME COL_NAME, "
			+ "TYPENAME COL_TYPE, " + "REMARKS COL_COMMENT, "
			+ "NULLS IS_NULLABLE, " + "LENGTH LENGTH, " + "SCALE SCALE, "
			+ "KEYSEQ  " + "FROM  " + "SYSCAT.COLUMNS " + "WHERE  "
			+ "TABSCHEMA IN (SELECT CURRENT SQLID FROM SYSIBM.DUAL) "
			+ "AND UPPER(TABNAME) = UPPER('%s') ";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hotent.base.api.db.IViewOperator#createOrRep(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void createOrRep(String viewName, String sql) throws Exception {
		// TODO
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hotent.base.api.db.IViewOperator#getViews(java.lang.String)
	 */
	@Override
	public PageList<String> getViews(String viewName) throws Exception {
		return getViews(viewName, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hotent.base.api.db.IViewOperator#getViews(java.lang.String,
	 * com.hotent.base.api.Page)
	 */
	@Override
	public PageList<String> getViews(String viewName, PageBean pageBean) throws Exception {
		String sql = SQL_GET_ALL_VIEW;
		if (StringUtils.isNotEmpty(viewName))
			sql += " and UPPER(view_name) like '" + viewName.toUpperCase()
					+ "%'";
		RowMapper<String> rowMapper = new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("VIEWNAME");
			}
		};

		return super.getForList(sql, pageBean, rowMapper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hotent.base.api.db.IViewOperator#getViewsByName(java.lang.String,
	 * com.hotent.base.api.Page)
	 */
	@Override
	public PageList<Table> getViewsByName(String viewName, PageBean pageBean)
			throws Exception {
		String sql = SQL_GET_ALL_VIEW;
		if (StringUtils.isNotEmpty(viewName))
			sql += " AND UPPER(VIEWNAME) LIKE '%" + viewName.toUpperCase()
					+ "%'";

		PageList<Table> tableModels = getForList(sql, pageBean, tableModelRowMapper);

		List<String> tableNames = new ArrayList<String>();
		// get all table names
		for (Table model : tableModels.getRows()) {
			tableNames.add(model.getTableName());
		}
		// batch get table columns
		Map<String, List<Column>> tableColumnsMap = getColumnsByTableName(tableNames);
		// extract table columns from paraTypeMap by table name;
		for (Entry<String, List<Column>> entry : tableColumnsMap.entrySet()) {
			// set TableModel's columns
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
			sql += " AND UPPER(TABNAME) IN (" + buf.toString().toUpperCase()
					+ ") ";
		}
		
		List<Column> columnModels=jdbcTemplate.query(sql, new DB2ColumnMap());
	
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hotent.base.db.table.BaseViewOperator#getType(java.lang.String)
	 */
	@Override
	public String getType(String type) {
		String dbtype = type.toLowerCase();
		if (dbtype.endsWith("bigint") || dbtype.endsWith("decimal")
				|| dbtype.endsWith("double") || dbtype.endsWith("integer")
				|| dbtype.endsWith("real") || dbtype.endsWith("smallint")) {
			return Column.COLUMN_TYPE_NUMBER;
		} else if (dbtype.endsWith("blob") || dbtype.endsWith("clob")
				|| dbtype.endsWith("dbclob") || dbtype.endsWith("graphic")
				|| dbtype.endsWith("long vargraphic")
				|| dbtype.endsWith("vargraphic") || dbtype.endsWith("xml")) {
			return Column.COLUMN_TYPE_CLOB;
		} else if (dbtype.endsWith("character")
				|| dbtype.endsWith("long varchar")
				|| dbtype.endsWith("varchar")) {
			return Column.COLUMN_TYPE_VARCHAR;
		} else if (dbtype.endsWith("date") || dbtype.endsWith("time")
				|| dbtype.endsWith("timestamp")) {
			return Column.COLUMN_TYPE_DATE;
		} else {
			return Column.COLUMN_TYPE_VARCHAR;
		}
	}

	RowMapper<Table> tableModelRowMapper = new RowMapper<Table>() {
		@Override
		public Table mapRow(ResultSet rs, int row) throws SQLException {
			Table tableModel = new DefaultTable();
			String tabName = rs.getString("VIEWNAME");
			tableModel.setTableName(tabName);
			tableModel.setComment(tabName);
			return tableModel;
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hotent.base.db.table.BaseViewOperator#getModelByViewName(java.lang
	 * .String)
	 */
	@Override
	public Table getModelByViewName(String viewName) throws SQLException {
		String sql = SQL_GET_ALL_VIEW;
		sql += " AND UPPER(VIEWNAME) = '" + viewName.toUpperCase() + "'";
		// TableModel tableModel= (TableModel) jdbcTemplate.queryForObject(sql,
		// null, tableModelRowMapper);
		Table tableModel = null;
		List<Table> tableModels = jdbcTemplate.query(sql, tableModelRowMapper);
		if (BeanUtils.isEmpty(tableModels)) {
			return null;
		} else {
			tableModel = tableModels.get(0);
		}
		// 获取列对象
		List<Column> columnList = getColumnsByTableName(viewName);
		tableModel.setColumnList(columnList);
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
		
		List<Column> list = jdbcTemplate.query(sql, 
				new DB2ColumnMap());
		return list;
	}
}
