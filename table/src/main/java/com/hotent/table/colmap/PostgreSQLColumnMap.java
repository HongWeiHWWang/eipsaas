package com.hotent.table.colmap;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.hotent.base.util.StringUtil;
import com.hotent.table.model.Column;
import com.hotent.table.model.impl.DefaultColumn;

import io.jsonwebtoken.lang.Assert;

/**
 * PostgreSQL列的元数据到columnmodel的映射
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年6月24日
 */
public class PostgreSQLColumnMap implements RowMapper<Column> {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public Column mapRow(ResultSet rs, int row) throws SQLException {
		Column column = new DefaultColumn();
		String table_name = rs.getString("table_name");
		String name = rs.getString("column_name");
		String is_nullable = rs.getString("is_nullable");
		String data_type = rs.getString("data_type");
		String length = rs.getString("length");
		String precisions = rs.getString("precisions");
		String scale = rs.getString("scale");
		String column_comment = rs.getString("column_comment");
		// Postgresql的字段原始类型
		String udt_name = rs.getString("udt_name");
		String is_primary = rs.getString("is_primary");
		int iLength = 0;
		int iPrecisions = 0;
		int iScale = 0;
		try {
			iLength = StringUtils.isEmpty(length) ? 0 : Integer.parseInt(length);
			iPrecisions = StringUtils.isEmpty(precisions) ? 0 : Integer.parseInt(precisions);
			iScale = StringUtils.isEmpty(scale) ? 0 : Integer.parseInt(scale);
		} catch (NumberFormatException e) {}

		column.setFieldName(name);
		column.setTableName(table_name);
		column.setComment(StringUtil.isEmpty(column_comment) ? name : column_comment);
		column.setFcolumnType(udt_name);
		if("Y".equalsIgnoreCase(is_primary)) {
			column.setIsPk(true);
		}
		boolean isNull = "YES".equalsIgnoreCase(is_nullable);
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
	private void setType(String dbtype, int length, int precision, int scale,
			Column columnModel) {
		Assert.isTrue(StringUtil.isNotEmpty(dbtype), "解析PostgreSQL的列类型时，获取到的列类型为空.");
		dbtype = dbtype.toLowerCase();
		switch(dbtype) {
			case "bigserial":
			case "serial":
			case "smallint":
			case "integer":
			case "int":
			case "bigint":
				columnModel.setColumnType(Column.COLUMN_TYPE_NUMBER);
				columnModel.setIntLen(precision);
				columnModel.setDecimalLen(0);
				break;
			case "decimal":
			case "numeric":
			case "float":
			case "real":
			case "double precision":
				columnModel.setColumnType(Column.COLUMN_TYPE_NUMBER);
				columnModel.setIntLen(precision);
				columnModel.setDecimalLen(scale);
				break;
			case "bit":
			case "bit varying":
			case "varbit":
			case "boolean":
			case "char":
			case "character":
			case "varchar":
			case "character varying":
				columnModel.setColumnType(Column.COLUMN_TYPE_VARCHAR);
				columnModel.setCharLen(length);
				break;
			case "text":
			case "bytea":
				columnModel.setColumnType(Column.COLUMN_TYPE_CLOB);
				columnModel.setCharLen(65535);
				break;
			case "date":
			case "datetime":
			case "time without time zone":
			case "time with time zone":
			case "timestamp without time zone":
			case "timestamp with time zone":
				columnModel.setColumnType(Column.COLUMN_TYPE_DATE);
				break;
			default:
				logger.warn("处理PostgreSQL数据库的列类型时有未识别的列数据类型：{}", dbtype);
				break;
		}
	}
}
