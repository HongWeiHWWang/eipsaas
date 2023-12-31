package com.hotent.table.operator.impl.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import com.hotent.table.model.Column;
import com.hotent.table.model.Table;
import com.hotent.table.operator.impl.BaseTableOperator;

/**
 * Mysql数据库表操作的实现
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月25日
 */
public class MySQLTableOperator extends BaseTableOperator {
	@Override
	public void createTable(Table table) throws SQLException {
		List<Column> columnList = table.getColumnList();

		// 建表语句
		StringBuffer sb = new StringBuffer();
		// 主键字段
		String pkColumn = null;
		// 建表开始
		sb.append("CREATE TABLE " + table.getTableName() + " (\n");
		for (int i = 0; i < columnList.size(); i++) {
			// 建字段
			Column cm = columnList.get(i);
			sb.append(cm.getFieldName()).append(" ");
			sb.append(getColumnType(cm.getColumnType(), cm.getCharLen(), cm.getIntLen(), cm.getDecimalLen()));
			sb.append(cm.getIsRequired() == 1 ? " NOT NULL " : " ");
			sb.append(" ");

			String defaultValue = cm.getDefaultValue();

			// 添加默认值。
			if (StringUtils.isNotEmpty(defaultValue)) {
				if(Column.COLUMN_TYPE_DATE.equals(cm.getColumnType())){
					defaultValue = formatTimeDefaultValue(cm,defaultValue);
				}
				if (Column.COLUMN_TYPE_NUMBER.equals(cm.getColumnType()) || Column.COLUMN_TYPE_INT.equals(cm.getColumnType())) {
					sb.append(" default " + defaultValue);
				} else {
					sb.append(" default '" + defaultValue + "' ");
				}
			}
			// 主键
			if (cm.getIsPk()) {
				if (pkColumn == null) {
					pkColumn = cm.getFieldName();
				} else {
					pkColumn += "," + cm.getFieldName();
				}
			}
			// 字段注释
			if (cm.getComment() != null && cm.getComment().length() > 0) {
				sb.append(" COMMENT '" + cm.getComment() + "'");
			}
			sb.append(",\n");
		}
		// 建主键
		if (pkColumn != null)
			sb.append(" PRIMARY KEY (" + pkColumn + ")");

		sb.append("\n)");

		// 表注释
		if (table.getComment() != null && table.getComment().length() > 0) {
			sb.append(" COMMENT='" + table.getComment() + "'");
		}
		// 建表结束
		sb.append(";");

		jdbcTemplate.execute(sb.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hotent.base.db.table.BaseTableOperator#getColumnType(com.hotent.base
	 * .api.table.model.Column)
	 */
	@Override
	public String getColumnType(Column column) {
		return getColumnType(column.getColumnType(), column.getCharLen(), column.getIntLen(), column.getDecimalLen());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hotent.base.db.table.BaseTableOperator#getColumnType(java.lang.String
	 * , int, int, int)
	 */
	@Override
	public String getColumnType(String columnType, int charLen, int intLen, int decimalLen) {
		if (Column.COLUMN_TYPE_VARCHAR.equals(columnType)) {
			return "VARCHAR(" + charLen + ')';
		} else if (Column.COLUMN_TYPE_NUMBER.equals(columnType)) {
			return "DECIMAL(" + (intLen + decimalLen) + "," + decimalLen + ")";
		} else if (Column.COLUMN_TYPE_DATE.equals(columnType)) {
			return "DATETIME NULL";
		} else if (Column.COLUMN_TYPE_INT.equals(columnType)) {
			return "BIGINT(" + intLen + ")";
		} else if (Column.COLUMN_TYPE_CLOB.equals(columnType)) {
			return "TEXT";
		} else {
			return "";
		}
	}

	/*
	 * 
	 * 删除表
	 * 
	 * @see
	 * com.hotent.base.db.table.BaseTableOperator#dropTable(java.lang.String)
	 */
	@Override
	public void dropTable(String tableName) throws SQLException {
		if (!isTableExist(tableName)) {
			return;
		}
		String sql = "drop table " + tableName;
		jdbcTemplate.execute(sql);
	}

	/*
	 * 修改表的注释
	 * 
	 * @see
	 * com.hotent.base.db.table.BaseTableOperator#updateTableComment(java.lang
	 * .String, java.lang.String)
	 */
	@Override
	public void updateTableComment(String tableName, String comment) throws SQLException {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE ").append(tableName).append(" COMMENT '").append(comment).append("';\n");

		jdbcTemplate.execute(sb.toString());
	}

	/*
	 * 添加列
	 * 
	 * @see
	 * com.hotent.base.db.table.BaseTableOperator#addColumn(java.lang.String,
	 * com.hotent.base.api.table.model.Column)
	 */
	@Override
	public void addColumn(String tableName, Column model) throws SQLException {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE ").append(tableName);
		sb.append(" ADD (");
		sb.append(model.getFieldName()).append(" ");
		sb.append(getColumnType(model.getColumnType(), model.getCharLen(), model.getIntLen(), model.getDecimalLen()));
		//因为在已有非空TIMESTAMP字段情况下，再加入非空TIMESTAMP字段会报错。因此新增时间类型的字段，数据库不加必填校验，只在前端页面校验
		if (model.getIsRequired()==1 && (!"date".equals(model.getColumnType()))) {
			sb.append(" NOT NULL ");
		}
		if (model.getComment() != null && model.getComment().length() > 0) {
			sb.append(" COMMENT '" + model.getComment() + "'");
		}
		sb.append(");");

		jdbcTemplate.execute(sb.toString());

	}
	
	/**
	 * 删除列
	 */
	@Override
	public void dropColumn(String tableName, String columnName)
			throws SQLException {
		String sql = "ALTER TABLE " + tableName + " DROP COLUMN  " + columnName;
		jdbcTemplate.execute(sql);
	}

	/*
	 * 修改列
	 * 
	 * @see
	 * com.hotent.base.db.table.BaseTableOperator#updateColumn(java.lang.String,
	 * java.lang.String, com.hotent.base.api.table.model.Column)
	 */
	@Override
	public void updateColumn(String tableName, String columnName, Column column) throws SQLException {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE ").append(tableName).append(" CHANGE " + columnName + " " + column.getFieldName()).append(" ").append(getColumnType(column.getColumnType(), column.getCharLen(), column.getIntLen(), column.getDecimalLen()));
		if (!column.getIsNull())
			sb.append(" NOT NULL ");

		if (column.getComment() != null && column.getComment().length() > 0)
			sb.append(" COMMENT '" + column.getComment() + "'");

		sb.append(";");

		jdbcTemplate.execute(sb.toString());
	}

	/*
	 * 
	 * 添加外键。
	 * 
	 * @see
	 * com.hotent.base.db.table.BaseTableOperator#addForeignKey(java.lang.String
	 * , java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void addForeignKey(String pkTableName, String fkTableName, String pkField, String fkField) {
		String shortTableName = fkTableName.replaceFirst("(?im)W_", "");
		String sql = "ALTER TABLE " + fkTableName + " ADD CONSTRAINT fk_" + shortTableName + " FOREIGN KEY (" + fkField + ") REFERENCES " + pkTableName + " (" + pkField + ") ON DELETE CASCADE";
		jdbcTemplate.execute(sql);
	}

	/*
	 * 刪除外鍵
	 * 
	 * @see
	 * com.hotent.base.db.table.BaseTableOperator#dropForeignKey(java.lang.String
	 * , java.lang.String)
	 */
	@Override
	public void dropForeignKey(String tableName, String keyName) {
		String sql = "ALTER TABLE " + tableName + " DROP FOREIGN KEY " + keyName;

		jdbcTemplate.execute(sql);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hotent.base.db.table.BaseTableOperator#getPKColumns(java.lang.String)
	 */
	@Override
	public List<String> getPKColumns(String tableName) throws SQLException {
		String schema = getSchema();
		String sql = "SELECT k.column_name " + "FROM information_schema.table_constraints t " + "JOIN information_schema.key_column_usage k " + "USING(constraint_name,table_schema,table_name) " + "WHERE t.constraint_type='PRIMARY KEY' " + "AND t.table_schema='" + schema + "' " + "AND t.table_name='" + tableName + "'";
		List<String> columns = jdbcTemplate.query(sql, new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String column = rs.getString(1);
				return column;
			}
		});
		return columns;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hotent.base.db.table.BaseTableOperator#getPKColumns(java.util.List)
	 */
	@Override
	public Map<String, List<String>> getPKColumns(List<String> tableNames) throws SQLException {
		StringBuffer sb = new StringBuffer();
		for (String name : tableNames) {
			sb.append("'");
			sb.append(name);
			sb.append("',");
		}
		sb.deleteCharAt(sb.length() - 1);

		String schema = getSchema();
		String sql = "SELECT t.table_name,k.column_name " + "FROM information_schema.table_constraints t " + "JOIN information_schema.key_column_usage k " + "USING(constraint_name,table_schema,table_name) " + "WHERE t.constraint_type='PRIMARY KEY' " + "AND t.table_schema='" + schema + "' " + "AND t.table_name in (" + sb.toString().toUpperCase() + ")";

		Map<String, List<String>> columnsMap = new HashMap<String, List<String>>();

		List<Map<String, String>> maps = jdbcTemplate.query(sql, new RowMapper<Map<String, String>>() {
			@Override
			public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
				String table = rs.getString(1);
				String column = rs.getString(2);
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", table);
				map.put("column", column);
				return map;
			}
		});

		for (Map<String, String> map : maps) {
			if (columnsMap.containsKey(map.get("name"))) {
				columnsMap.get(map.get("name")).add(map.get("column"));
			} else {
				List<String> cols = new ArrayList<String>();
				cols.add(map.get("column"));
				columnsMap.put(map.get("name"), cols);
			}
		}

		return columnsMap;
	}

	/*
	 * 检查表是否存在
	 * 
	 * @see
	 * com.hotent.base.db.table.BaseTableOperator#isTableExist(java.lang.String)
	 */
	@Override
	public boolean isTableExist(String tableName) {
		String schema = getSchema();
		String sql = "select count(1) from information_schema.TABLES t where t.TABLE_SCHEMA='" + schema + "' and table_name ='" + tableName.toUpperCase() + "'";
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class) > 0 ? true : false;
	}

	/**
	 * 取得当前连接的Schema
	 * 
	 * @return
	 */
	private String getSchema() {
		String schema = null;
		Connection conn = null;
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			schema = conn.getCatalog();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return schema;
	}

	@Override
	public boolean isExsitPartition(String tableName, String partition) {
		partition=replaceLineThrough(partition);
		
		String sql="select count(*) from information_schema.partitions  where table_schema = schema() "
				+ "and table_name=? and partition_name =?";
		
		String[] args=new String[2];
		args[0]=tableName;
		args[1]="P_" + partition;
		Integer rtn= jdbcTemplate.queryForObject(sql, args, Integer.class);
		return rtn>0;
	}

	@Override
	public void createPartition(String tableName, String partition) {
		partition=replaceLineThrough(partition);
		String sql="alter table "+tableName+" add partition (partition P_"+partition+" values in ('"+partition+"')) ";
		jdbcTemplate.update(sql);
	}
	


	@Override
	public boolean supportPartition(String tableName) {
		String sql="select count(*) from information_schema.partitions  where table_schema = schema() "
				+ "and table_name=? ";
		
		Integer rtn= jdbcTemplate.queryForObject(sql, Integer.class, tableName);
		return rtn>0;
	}
	
	//针对字段是日期类型，但是格式是时间格式HH:mm:ss的默认值，转换为日期，防止生成表时失败
	private String formatTimeDefaultValue(Column cm,String defaultValue){
		//TODO 检查时间格式是否创建表会失败
		return defaultValue;
	}
}
