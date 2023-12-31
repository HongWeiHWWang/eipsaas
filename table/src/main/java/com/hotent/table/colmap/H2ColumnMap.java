package com.hotent.table.colmap;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import com.hotent.table.model.Column;
import com.hotent.table.model.impl.DefaultColumn;

/**
 * H2 列的元数据到columnmodel的映射
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月25日
 */
public class H2ColumnMap implements RowMapper<Column> {

	@Override
	public Column mapRow(ResultSet rs, int row) throws SQLException {
		Column column = new DefaultColumn();

		String name = rs.getString("COLUMN_NAME");
		String is_nullable = rs.getString("IS_NULLABLE");
		String data_type = rs.getString("TYPE_NAME");
		String length = rs.getString("LENGTH");
		String precisions = rs.getString("PRECISIONS");
		String scale = rs.getString("SCALE");
		String column_list = rs.getString("COLUMN_LIST");
		String column_comment = rs.getString("REMARKS");
		String table_name = rs.getString("TABLE_NAME");
		int iLength;
		try {
			iLength = StringUtils.isEmpty(length) ? 0 : Integer.parseInt(length);
		} catch (NumberFormatException e) {
			iLength = 0;
		}
		int iPrecisions = StringUtils.isEmpty(precisions) ? 0 : Integer.parseInt(precisions);
		int iScale = StringUtils.isEmpty(scale) ? 0 : Integer.parseInt(scale);

		column.setFieldName(name);
		column.setTableName(table_name);
		column.setComment(column_comment);

		boolean isPkColumn = false;
		if (StringUtils.isNotEmpty(column_list)) {
			String[] pkColumns = column_list.split(",");
			for (String pkColumn : pkColumns) {
				if (name.trim().equalsIgnoreCase(pkColumn.trim())) {
					isPkColumn = true;
					break;
				}
			}
		}
		column.setIsPk(isPkColumn);

		boolean isNull = is_nullable.equals("YES");
		column.setIsNull(isNull);

		setType(data_type, iLength, iPrecisions, iScale, column);

		return column;
	}

	/**
	 * 设置列类型
	 * 
	 * @param dbtype
	 * @param length
	 * @param precision
	 * @param scale
	 * @param columnModel
	 */
	private void setType(String dbtype, int length, int precision, int scale, Column columnModel) {
		dbtype = dbtype.toUpperCase();
		if (dbtype.equals("BIGINT")) { // int
			columnModel.setColumnType(Column.COLUMN_TYPE_INT);
			columnModel.setIntLen(19);
			columnModel.setDecimalLen(0);
			return;
		} else if (dbtype.equals("INT8")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_INT);
			columnModel.setIntLen(19);
			columnModel.setDecimalLen(0);
			return;
		} else if (dbtype.equals("INT")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_INT);
			columnModel.setIntLen(10);
			columnModel.setDecimalLen(0);
			return;
		} else if (dbtype.equals("INTEGER")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_INT);
			columnModel.setIntLen(10);
			columnModel.setDecimalLen(0);
			return;
		} else if (dbtype.equals("MEDIUMINT")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_INT);
			columnModel.setIntLen(7);
			columnModel.setDecimalLen(0);
			return;
		} else if (dbtype.equals("INT4")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_INT);
			columnModel.setIntLen(5);
			columnModel.setDecimalLen(0);
			return;
		} else if (dbtype.equals("SIGNED")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_INT);
			columnModel.setIntLen(3);
			columnModel.setDecimalLen(0);
			return;
		} else if (dbtype.equals("TINYINT")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_INT);
			columnModel.setIntLen(2);
			columnModel.setDecimalLen(0);
			return;
		} else if (dbtype.equals("SMALLINT")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_INT);
			columnModel.setIntLen(5);
			columnModel.setDecimalLen(0);
			return;
		} else if (dbtype.equals("INT2")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_INT);
			columnModel.setIntLen(5);
			columnModel.setDecimalLen(0);
			return;
		} else if (dbtype.equals("YEAR")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_INT);
			columnModel.setIntLen(5);
			columnModel.setDecimalLen(0);
			return;
		} else if (dbtype.equals("IDENTITY")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_INT);
			columnModel.setIntLen(5);
			columnModel.setDecimalLen(0);
			return;
		} else if (dbtype.equals("DECIMAL")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_NUMBER);
			columnModel.setIntLen(precision - scale);
			columnModel.setDecimalLen(scale);
			return;
		} else if (dbtype.equals("DOUBLE")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_NUMBER);
			return;
		} else if (dbtype.equals("FLOAT")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_NUMBER);
			return;
		} else if (dbtype.equals("FLOAT4")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_NUMBER);
			return;
		} else if (dbtype.equals("FLOAT8")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_NUMBER);
			return;
		} else if (dbtype.equals("REAL")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_NUMBER);
			return;
		} else if (dbtype.equals("TIME")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_DATE);
			return;
		} else if (dbtype.equals("DATE")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_DATE);
			return;
		} else if (dbtype.equals("DATETIME")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_DATE);
			return;
		} else if (dbtype.equals("SMALLDATETIME")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_DATE);
			return;
		} else if (dbtype.equals("TIMESTAMP")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_DATE);
			return;
		} else if (dbtype.equals("BINARY")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_CLOB);
			return;
		} else if (dbtype.equals("VARBINARY")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_CLOB);
			return;
		} else if (dbtype.equals("LONGVARBINARY")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_CLOB);
			return;
		} else if (dbtype.equals("RAW")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_CLOB);
			return;
		} else if (dbtype.equals("BYTEA")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_CLOB);
			return;
		} else if (dbtype.equals("TINYBLOB")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_CLOB);
			return;
		} else if (dbtype.equals("MEDIUMBLOB")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_CLOB);
			return;
		} else if (dbtype.equals("LONGBLOB")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_CLOB);
			return;
		} else if (dbtype.equals("IMAGE")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_CLOB);
			return;
		} else if (dbtype.equals("OID")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_CLOB);
			return;
		} else if (dbtype.equals("CLOB")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_CLOB);
			return;
		} else if (dbtype.equals("TINYTEXT")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_CLOB);
			return;
		} else if (dbtype.equals("TEXT")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_CLOB);
			return;
		} else if (dbtype.equals("MEDIUMTEXT")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_CLOB);
			return;
		} else if (dbtype.equals("LONGTEXT")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_CLOB);
			return;
		} else if (dbtype.equals("NTEXT")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_CLOB);
			return;
		} else if (dbtype.equals("NCLOB")) {
			columnModel.setColumnType(Column.COLUMN_TYPE_CLOB);
			return;
		} else {
			columnModel.setColumnType(Column.COLUMN_TYPE_VARCHAR);
			columnModel.setCharLen(length);
		}
	}

}
