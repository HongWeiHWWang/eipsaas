package com.hotent.table.operator.impl.h2;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import com.hotent.base.exception.DataBaseException;
import com.hotent.table.model.Column;
import com.hotent.table.model.Table;
import com.hotent.table.operator.impl.BaseTableOperator;

/**
 * H2 数据库表操作的实现
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月25日
 */
public class H2TableOperator extends BaseTableOperator {
	
	@Override
	public void createTable(Table model) throws SQLException {
		List<Column> columnList = model.getColumnList();
		// 建表语句
		StringBuffer sb = new StringBuffer();
		// 主键字段
		String pkColumn = null;
		// 建表开始
		sb.append("CREATE TABLE " + model.getTableName() + " (\n");
		for (int i = 0; i < columnList.size(); i++) {
			// 建字段
			Column cm = columnList.get(i);
			sb.append("    ").append(cm.getFieldName()).append("    ");
			sb.append(getColumnType(cm.getColumnType(), cm.getCharLen(),
					cm.getIntLen(), cm.getDecimalLen()));
			sb.append(" ");
			// 主键
			if (cm.getIsPk()) {
				if (pkColumn == null) {
					pkColumn = cm.getFieldName();
				} else {
					pkColumn += "," + cm.getFieldName();
				}
			}
			// 添加默认值
			String defVal = getDefaultValueSQL(cm);
			if (StringUtils.isNotEmpty(defVal)) {
				sb.append(defVal);
			}

			// 非空
			if (!cm.getIsNull() || cm.getIsPk()) {
				sb.append(" NOT NULL ");
			}

			// 字段注释
			if (cm.getComment() != null && cm.getComment().length() > 0) {
				 sb.append(" COMMENT '" + cm.getComment() + "'");
			}
			sb.append(",\n");
		}
		// 建主键
		if (pkColumn != null) {
			sb.append("    CONSTRAINT PK_").append(model.getTableName())
					.append(" PRIMARY KEY (").append(pkColumn).append(")");
		} else {
			sb = new StringBuffer(sb.substring(0, sb.length() - ",\n".length()));
		}
		sb.append("\n)");
		// 表注释
		if (model.getComment() != null && model.getComment().length() > 0) {
			sb.append(String.format(" COMMENT='%s';", model.getComment()));
		}
		else {
			// 建表结束
			sb.append(";");
		}
		jdbcTemplate.execute(sb.toString());
	}

	@Override
	public String getColumnType(Column column) {
		return getColumnType(column.getColumnType(), column.getCharLen(),
				column.getIntLen(), column.getDecimalLen());
	}

	@Override
	public String getColumnType(String columnType, int charLen, int intLen,
			int decimalLen) {
		if (Column.COLUMN_TYPE_VARCHAR.equals(columnType)) {
			return "VARCHAR(" + charLen + ')';
		} else if (Column.COLUMN_TYPE_NUMBER.equals(columnType)) {
			return "DECIMAL(" + (intLen + decimalLen) + "," + decimalLen + ")";
		} else if (Column.COLUMN_TYPE_DATE.equals(columnType)) {
			return "DATE";
		} else if (Column.COLUMN_TYPE_INT.equals(columnType)) {
			if (intLen > 0 && intLen <= 5) {
				return "SMALLINT";
			} else if (intLen > 5 && intLen <= 10) {
				return "INTEGER";
			} else {
				return "BIGINT";
			}
		} else if (Column.COLUMN_TYPE_CLOB.equals(columnType)) {
			return "CLOB";
		} else {
			return "VARCHAR(50)";
		}
	}

	@Override
	public void dropTable(String tableName) throws SQLException {
		String selSql = "" + "SELECT " + "COUNT(*) AMOUNT " + "FROM "
				+ "INFORMATION_SCHEMA.TABLES  " + "WHERE "
				+ "TABLE_SCHEMA = SCHEMA() " + "AND TABLE_NAME = UPPER('"
				+ tableName + "')";
		int rtn = jdbcTemplate.queryForObject(selSql, Integer.class);
		if (rtn > 0) {
			String sql = "DROP TABLE " + tableName;
			jdbcTemplate.execute(sql);
		}

	}

	@Override
	public void updateTableComment(String tableName, String comment)
			throws SQLException {
		StringBuffer sb = new StringBuffer();
		sb.append("COMMENT ON TABLE ");
		sb.append(tableName);
		sb.append(" IS '");
		sb.append(comment);
		sb.append("'\n");
		jdbcTemplate.execute(sb.toString());
	}

	@Override
	public void addColumn(String tableName, Column model) throws SQLException {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE ").append(tableName);
		sb.append(" ADD ");
		sb.append(model.getFieldName()).append(" ");
		sb.append(getColumnType(model.getColumnType(), model.getCharLen(),
				model.getIntLen(), model.getDecimalLen()));

		// 添加默认值
		// 添加默认值
		String defVal = getDefaultValueSQL(model);
		if (StringUtils.isNotEmpty(defVal)) {
			sb.append(defVal);
		}
		sb.append("\n");
		jdbcTemplate.execute(sb.toString());
		if (model.getComment() != null && model.getComment().length() > 0) {
			jdbcTemplate.execute("COMMENT ON COLUMN " + tableName + "."
					+ model.getFieldName() + " IS '" + model.getComment() + "'");
		}

	}

	@Override
	public void updateColumn(String tableName, String columnName, Column model)
			throws SQLException {
		// 修改列名
		if (!columnName.equals(model.getFieldName())) {
			StringBuffer modifyName = new StringBuffer("ALTER TABLE ")
					.append(tableName);
			modifyName.append(" ALTER COLUMN ").append(columnName)
					.append(" RENAME TO ").append(model.getFieldName());
			jdbcTemplate.execute(modifyName.toString());
		}
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE ").append(tableName);
		sb.append(" ALTER COLUMN ").append(model.getFieldName());
		sb.append(getColumnType(model.getColumnType(), model.getCharLen(),
				model.getIntLen(), model.getDecimalLen()));
		if (!model.getIsNull()) {
			sb.append(" NOT NULL ");
		}
		jdbcTemplate.execute(sb.toString());
		// 修改注释
		if (model.getComment() != null && model.getComment().length() > 0) {
			jdbcTemplate.execute("COMMENT ON COLUMN " + tableName + "."
					+ model.getFieldName() + " IS'" + model.getComment() + "'");
		}
	}
	
	@Override
	public void addForeignKey(String pkTableName, String fkTableName,
			String pkField, String fkField) {
		String shortTableName = fkTableName.replaceFirst("(?im)W_", "");
		String sql = " ALTER TABLE " + fkTableName + " ADD CONSTRAINT FK_"
				+ shortTableName + " FOREIGN KEY (" + fkField + ") REFERENCES "
				+ pkTableName + " (" + pkField + ") ON DELETE CASCADE";
		jdbcTemplate.execute(sql);
	}

	@Override
	public void dropForeignKey(String tableName, String keyName) {
		String sql = "ALTER   TABLE   " + tableName + "   DROP   CONSTRAINT  "
				+ keyName;
		jdbcTemplate.execute(sql);
	}

	@Override
	public List<String> getPKColumns(String tableName) throws SQLException {
		String sql = "";
		List<String> columns = jdbcTemplate.query(sql, new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String column = rs.getString(1);
				return column;
			}
		});
		return columns;
	}

	@Override
	public Map<String, List<String>> getPKColumns(List<String> tableNames)
			throws SQLException {
		StringBuffer sb = new StringBuffer();
		for (String name : tableNames) {
			sb.append("'");
			sb.append(name);
			sb.append("',");
		}
		sb.deleteCharAt(sb.length() - 1);

		String sql = "";

		Map<String, List<String>> columnsMap = new HashMap<String, List<String>>();

		List<Map<String, String>> maps = jdbcTemplate.query(sql,
				new RowMapper<Map<String, String>>() {
					@Override
					public Map<String, String> mapRow(ResultSet rs, int rowNum)
							throws SQLException {
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

	@Override
	public boolean isTableExist(String tableName) {
		String sql = "" + "SELECT " + "COUNT(*) AMOUNT " + "FROM "
				+ "INFORMATION_SCHEMA.TABLES  " + "WHERE "
				+ "TABLE_SCHEMA = SCHEMA() " + "AND TABLE_NAME = UPPER('"
				+ tableName + "')";
		return jdbcTemplate.queryForObject(sql, Integer.class) > 0 ? true
				: false;
	}

	/**
	 * 获取默认
	 * 
	 * @param cm
	 * @return
	 */
	private String getDefaultValueSQL(Column cm) {
		String sql = null;
		if (StringUtils.isNotEmpty(cm.getDefaultValue())) {
			if (Column.COLUMN_TYPE_INT.equalsIgnoreCase(cm.getColumnType())) {
				sql = " DEFAULT " + cm.getDefaultValue() + " ";
			} else if (Column.COLUMN_TYPE_NUMBER.equalsIgnoreCase(cm
					.getColumnType())) {
				sql = " DEFAULT " + cm.getDefaultValue() + " ";
			} else if (Column.COLUMN_TYPE_VARCHAR.equalsIgnoreCase(cm
					.getColumnType())) {
				sql = " DEFAULT '" + cm.getDefaultValue() + "' ";
			} else if (Column.COLUMN_TYPE_CLOB.equalsIgnoreCase(cm
					.getColumnType())) {
				sql = " DEFAULT '" + cm.getDefaultValue() + "' ";
			} else if (Column.COLUMN_TYPE_DATE.equalsIgnoreCase(cm
					.getColumnType())) {
				sql = " DEFAULT " + cm.getDefaultValue() + " ";
			} else {
				sql = " DEFAULT " + cm.getDefaultValue() + " ";
			}
		}
		return sql;
	}

	@Override
	public boolean isExsitPartition(String tableName, String partition) {
		return false;
	}

	@Override
	public void createPartition(String tableName, String partition) {
		throw new DataBaseException("H2数据库不支持创建表分区");
	}

	@Override
	public boolean supportPartition(String tableName) {
		return false;
	}

	@Override
	public void dropColumn(String tableName, String columnName) throws SQLException {
		String sql = "ALTER TABLE " + tableName + " DROP COLUMN  " + columnName;
		jdbcTemplate.execute(sql);
	}
}
