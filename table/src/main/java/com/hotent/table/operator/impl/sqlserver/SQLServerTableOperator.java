package com.hotent.table.operator.impl.sqlserver;

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
 * SQLServer 数据库表操作的实现
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月25日
 */
public class SQLServerTableOperator extends BaseTableOperator {
	
	/*
	 * 创建表 (non-Javadoc)
	 * 
	 * @see
	 * com.hotent.base.db.table.BaseTableOperator#createTable(com.hotent.base
	 * .api.table.model.Table)
	 */
	@Override
	public void createTable(Table model) throws SQLException {
		List<Column> columnList = model.getColumnList();

		// 建表语句
		StringBuffer createTableSql = new StringBuffer();
		// 主键字段
		String pkColumn = null;

		// List<String> fkList = new ArrayList<String>();
		// 例注释
		List<String> columnCommentList = new ArrayList<String>();
		// 建表开始
		createTableSql.append("CREATE TABLE " + model.getTableName() + " (\n");
		for (int i = 0; i < columnList.size(); i++) {
			// 建字段
			Column cm = columnList.get(i);
			createTableSql.append("    ").append(cm.getFieldName()).append("    ");
			createTableSql.append(getColumnType(cm.getColumnType(),
					cm.getCharLen(), cm.getIntLen(), cm.getDecimalLen()));
			createTableSql.append(" ");

			// alter table Table_2 add a datetime default getdate() not null ;
			if (StringUtils.isNotEmpty(cm.getDefaultValue())) {
				if(Column.COLUMN_TYPE_NUMBER.equals(cm.getColumnType())||Column.COLUMN_TYPE_INT.equals(cm.getColumnType())){
					createTableSql.append(" DEFAULT " + cm.getDefaultValue());
				}else{
					createTableSql.append(" DEFAULT '" + cm.getDefaultValue()+"' ");
				}
				
			}

			// 非空
			if (cm.getIsRequired() == 1) {
				createTableSql.append(" NOT NULL ");
			}
			// 主键
			if (cm.getIsPk()) {
				if (pkColumn == null) {
					pkColumn = cm.getFieldName();
				} else {
					pkColumn += "," + cm.getFieldName();
				}
			}
			// if (cm.getIsFk()) {
			// fkList.add("    FOREIGN KEY (" + cm.getName() + ") REFERENCES " +
			// cm.getFkRefTable() + "(" + cm.getFkRefColumn() + ")");
			//
			// }
			// 字段注释
			if (cm.getComment() != null && cm.getComment().length() > 0) {
				StringBuffer comment = new StringBuffer(
						"EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'");
				comment.append(cm.getComment())
						.append("' ,@level0type=N'SCHEMA', @level0name=N'dbo', @level1type=N'TABLE', @level1name=N'")
						.append(model.getTableName())
						.append("', @level2type=N'COLUMN', @level2name=N'")
						.append(cm.getFieldName()).append("'");
				columnCommentList.add(comment.toString());
			}
			createTableSql.append(",\n");
		}
		// 建主键
		if (pkColumn != null) {
			createTableSql.append("    CONSTRAINT PK_").append(model.getTableName())
					.append(" PRIMARY KEY (").append(pkColumn).append(")");
		}
		// 建外键
		// for (String fk : fkList) {
		// createTableSql.append(",\n" + fk);
		// }

		// 建表结束
		createTableSql.append("\n)");
		jdbcTemplate.execute(createTableSql.toString());

		// 表注释
		if (model.getComment() != null && model.getComment().length() > 0) {
			StringBuffer tableComment = new StringBuffer(
					"EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'");
			tableComment
					.append(model.getComment())
					.append("' ,@level0type=N'SCHEMA', @level0name=N'dbo', @level1type=N'TABLE', @level1name=N'")
					.append(model.getTableName()).append("'");
			jdbcTemplate.execute(tableComment.toString());
		}
		for (String columnComment : columnCommentList) {
			jdbcTemplate.execute(columnComment);
		}

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
		return getColumnType(column.getColumnType(), column.getCharLen(),
				column.getIntLen(), column.getDecimalLen());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hotent.base.db.table.BaseTableOperator#getColumnType(java.lang.String
	 * , int, int, int)
	 */
	@Override
	public String getColumnType(String columnType, int charLen, int intLen,
			int decimalLen) {
		if (Column.COLUMN_TYPE_VARCHAR.equals(columnType)) {
			return "VARCHAR(" + charLen + ')';
		} else if (Column.COLUMN_TYPE_NUMBER.equals(columnType)) {
			return "NUMERIC(" + (intLen + decimalLen) + "," + decimalLen + ")";
		} else if (Column.COLUMN_TYPE_DATE.equals(columnType)) {
			return "DATETIME";
		} else if (Column.COLUMN_TYPE_INT.equals(columnType)) {
			return "NUMERIC(" + intLen + ")";
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
		String sql = "IF OBJECT_ID(N'" + tableName
				+ "', N'U') IS NOT NULL  DROP TABLE " + tableName;
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
	public void updateTableComment(String tableName, String comment)
			throws SQLException {
		// 假如不存在表的注释 ,会抛出异常
		StringBuffer commentSql = new StringBuffer(
				"EXEC sys.sp_updateextendedproperty @name=N'MS_Description', @value=N'");
		commentSql
				.append(comment)
				.append("' ,@level0type=N'SCHEMA', @level0name=N'dbo', @level1type=N'TABLE', @level1name=N'")
				.append(tableName).append("'");

		jdbcTemplate.execute(commentSql.toString());

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
		StringBuffer alterSql = new StringBuffer();
		alterSql.append("ALTER TABLE ").append(tableName);
		alterSql.append(" ADD ");
		alterSql.append(model.getFieldName()).append(" ");
		alterSql.append(getColumnType(model.getColumnType(),
				model.getCharLen(), model.getIntLen(), model.getDecimalLen()));
		// 设置默认值
		if (StringUtils.isNotEmpty(model.getDefaultValue())) {
			alterSql.append(" DEFAULT " + model.getDefaultValue());
		}

		// if (!model.getIsNull()) {
		// alterSql.append(" NOT NULL ");
		// }
		alterSql.append("\n");
		jdbcTemplate.execute(alterSql.toString());
		if (model.getComment() != null && model.getComment().length() > 0) {
			StringBuffer comment = new StringBuffer(
					"EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'");
			comment.append(model.getComment())
					.append("' ,@level0type=N'SCHEMA', @level0name=N'dbo', @level1type=N'TABLE', @level1name=N'")
					.append(tableName)
					.append("', @level2type=N'COLUMN', @level2name=N'")
					.append(model.getFieldName()).append("'");
			jdbcTemplate.execute(comment.toString());
		}
	}

	/*
	 * 修改列
	 * 
	 * @see
	 * com.hotent.base.db.table.BaseTableOperator#updateColumn(java.lang.String,
	 * java.lang.String, com.hotent.base.api.table.model.Column)
	 */
	@Override
	public void updateColumn(String tableName, String columnName, Column model)
			throws SQLException {
		// 修改列名
		if (!columnName.equals(model.getFieldName())) {
			StringBuffer modifyName = new StringBuffer("EXEC sp_rename '");
			modifyName.append(tableName).append(".[").append(columnName)
					.append("]','").append(model.getFieldName())
					.append("', 'COLUMN'");
			jdbcTemplate.execute(modifyName.toString());
		}

		// 修改列的大小,此处不修改列的类型,若修改列的类型则在前面部分已抛出异常
		StringBuffer alterSql = new StringBuffer();
		alterSql.append("ALTER TABLE ").append(tableName);
		alterSql.append(" ALTER COLUMN " + model.getFieldName()).append(" ");
		alterSql.append(getColumnType(model.getColumnType(),
				model.getCharLen(), model.getIntLen(), model.getDecimalLen()));
		if (!model.getIsNull()) {
			alterSql.append(" NOT NULL ");
		}

		jdbcTemplate.execute(alterSql.toString());

		// 修改注释
		if (model.getComment() != null && model.getComment().length() > 0) {
			// 更新字段注释假如不存在该列的注释 ,会抛出异常
			StringBuffer comment = new StringBuffer(
					"EXEC sys.sp_updateextendedproperty @name=N'MS_Description', @value=N'");
			comment.append(model.getComment())
					.append("' ,@level0type=N'SCHEMA', @level0name=N'dbo', @level1type=N'TABLE', @level1name=N'")
					.append(tableName)
					.append("', @level2type=N'COLUMN', @level2name=N'")
					.append(model.getFieldName()).append("'");
			jdbcTemplate.execute(comment.toString());
		}
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
	public void addForeignKey(String pkTableName, String fkTableName,
			String pkField, String fkField) {
		String shortTableName = fkTableName.replaceFirst("(?im)W_", "");
		String sql = "  ALTER TABLE " + fkTableName + " ADD CONSTRAINT fk_"
				+ shortTableName + " FOREIGN KEY (" + fkField + ") REFERENCES "
				+ pkTableName + " (" + pkField + ")   ON DELETE CASCADE";
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
		String sql = "ALTER   TABLE   " + tableName + "   DROP   CONSTRAINT  "
				+ keyName;
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
		String sql = "SELECT C.COLUMN_NAME COLUMN_NAME FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS PK ,INFORMATION_SCHEMA.KEY_COLUMN_USAGE C "
				+ "WHERE 	PK.TABLE_NAME = '%S' "
				+ "AND	CONSTRAINT_TYPE = 'PRIMARY KEY' "
				+ "AND	C.TABLE_NAME = PK.TABLE_NAME "
				+ "AND	C.CONSTRAINT_NAME = PK.CONSTRAINT_NAME ";
		sql = String.format(sql, tableName);

		List<String> columns = jdbcTemplate.query(sql, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub
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
	public Map<String, List<String>> getPKColumns(List<String> tableNames)
			throws SQLException {
		StringBuffer sb = new StringBuffer();
		for (String name : tableNames) {
			sb.append("'");
			sb.append(name);
			sb.append("',");
		}
		sb.deleteCharAt(sb.length() - 1);

		String sql = "SELECT C.COLUMN_NAME COLUMN_NAME FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS PK ,INFORMATION_SCHEMA.KEY_COLUMN_USAGE C "
				+ "WHERE 	PK.TABLE_NAME in( %S )"
				+ "AND	CONSTRAINT_TYPE = 'PRIMARY KEY' "
				+ "AND	C.TABLE_NAME = PK.TABLE_NAME "
				+ "AND	C.CONSTRAINT_NAME = PK.CONSTRAINT_NAME ";
		sql = String.format(sql, sb.toString());

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

	/*
	 * 检查表是否存在
	 * 
	 * @see
	 * com.hotent.base.db.table.BaseTableOperator#isTableExist(java.lang.String)
	 */
	@Override
	public boolean isTableExist(String tableName) {
		String sql = "select count(1) from sysobjects where name='"
				+ tableName.toUpperCase() + "'";
		return jdbcTemplate.queryForObject(sql, Integer.class) > 0 ? true
				: false;

	}

	@Override
	public boolean isExsitPartition(String tableName, String partition) {
		return false;
	}

	@Override
	public void createPartition(String tableName, String partition) {
		
	}


	@Override
	public boolean supportPartition(String tableName) {
		return false;
	}

	@Override
	public void dropColumn(String tableName, String columnName)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
