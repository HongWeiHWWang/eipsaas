package com.hotent.table.colmap;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import com.hotent.table.model.Column;
import com.hotent.table.model.impl.DefaultColumn;

/**
 * DB2 RowMapper
 * @author Raise
 *
 */
public class DB2ColumnMap implements RowMapper<Column> {
	@Override
	public Column mapRow(ResultSet rs, int rowNum) throws SQLException {
		Column column=new DefaultColumn();
		String tabName=rs.getString("TAB_NAME");
		String colName=rs.getString("COL_NAME");
		String colType = rs.getString("COL_TYPE");
		String colComment = rs.getString("COL_COMMENT");
		String nullable=rs.getString("IS_NULLABLE");
		String length=rs.getString("LENGTH");
		String scale=rs.getString("SCALE");
		String keySeq=rs.getString("KEYSEQ");
		int iLength=string2Int(length,0);
		int iPrecision=iLength;
		int iScale=string2Int(scale,0);
		int iKeySeq=string2Int(keySeq,0);

		column.setTableName(tabName);
		column.setFieldName(colName);
		column.setComment(colComment);
		//is nullable
		column.setIsNull("Y".equalsIgnoreCase(nullable)?true:false);
		//is primary key
		column.setIsPk(iKeySeq>0?true:false);
		//column.setColumnType(colType);
		setType(colType,iLength,iPrecision,iScale,column);
		return column;
	}
	
	/**
	 * String到Int的类型转换，如果转换失败，返回默认值。
	 * @param str 要转换的String类型的值
	 * @param def 默认值
	 * @return
	 */
	private int string2Int(String str,int def){
		if(StringUtils.isEmpty(str))
			return def;	
		try{
			return Integer.parseInt(str);
		}catch (Exception e) {
			e.printStackTrace();
			return def;
		}
	
	}

	/**
	 * 设置列类型
	 * @param dbtype
	 * @param length
	 * @param precision
	 * @param scale
	 * @param column
	 */
	private void setType(String type,int length,int precision,int scale,Column column)
	{
		String dbtype=type.toLowerCase();
		if (dbtype.endsWith("bigint")) {
			column.setColumnType(Column.COLUMN_TYPE_NUMBER);
			column.setIntLen(19);
			column.setDecimalLen(0);
		} else if (dbtype.endsWith("blob")) {
			column.setColumnType(Column.COLUMN_TYPE_CLOB);
		} else if (dbtype.endsWith("character")) {
			column.setColumnType(Column.COLUMN_TYPE_VARCHAR);
			column.setCharLen(length);
			column.setDecimalLen(0);
		} else if (dbtype.endsWith("clob")) {
			column.setColumnType(Column.COLUMN_TYPE_CLOB);
		} else if (dbtype.endsWith("date")) {
			column.setColumnType(Column.COLUMN_TYPE_DATE);
		} else if (dbtype.endsWith("dbclob")) {
			column.setColumnType(Column.COLUMN_TYPE_CLOB);
		} else if (dbtype.endsWith("decimal")) {
			column.setColumnType(Column.COLUMN_TYPE_NUMBER);
			column.setIntLen(precision-scale);
			column.setDecimalLen(scale);
		} else if (dbtype.endsWith("double")) {
			column.setColumnType(Column.COLUMN_TYPE_NUMBER);
			column.setIntLen(precision-scale);
			column.setDecimalLen(scale);
		} else if (dbtype.endsWith("graphic")) {
			column.setColumnType(Column.COLUMN_TYPE_CLOB);
		} else if (dbtype.endsWith("integer")) {
			column.setColumnType(Column.COLUMN_TYPE_NUMBER);
			column.setIntLen(10);
			column.setDecimalLen(0);
		} else if (dbtype.endsWith("long varchar")) {
			column.setColumnType(Column.COLUMN_TYPE_VARCHAR);
			column.setCharLen(length);
		} else if (dbtype.endsWith("long vargraphic")) {
			column.setColumnType(Column.COLUMN_TYPE_CLOB);
		} else if (dbtype.endsWith("real")) {
			column.setColumnType(Column.COLUMN_TYPE_NUMBER);
			column.setIntLen(length);
			column.setDecimalLen(scale);
		} else if (dbtype.endsWith("smallint")) {
			column.setColumnType(Column.COLUMN_TYPE_NUMBER);
			column.setIntLen(5);
			column.setDecimalLen(0);
		} else if (dbtype.endsWith("time")) {
			column.setColumnType(Column.COLUMN_TYPE_DATE);
		} else if (dbtype.endsWith("timestamp")) {
			column.setColumnType(Column.COLUMN_TYPE_DATE);
		} else if (dbtype.endsWith("varchar")) {
			column.setColumnType(Column.COLUMN_TYPE_VARCHAR);
			column.setCharLen(length);
		} else if (dbtype.endsWith("vargraphic")) {
			column.setColumnType(Column.COLUMN_TYPE_CLOB);
		} else if (dbtype.endsWith("xml")) {
			column.setColumnType(Column.COLUMN_TYPE_CLOB);
		}
	}

}
