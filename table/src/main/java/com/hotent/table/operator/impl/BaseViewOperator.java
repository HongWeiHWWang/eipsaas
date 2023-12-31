package com.hotent.table.operator.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.hotent.base.manager.CommonManager;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.util.AppUtil;
import com.hotent.table.meta.impl.BaseDbType;
import com.hotent.table.model.Column;
import com.hotent.table.model.Table;
import com.hotent.table.model.impl.DefaultColumn;
import com.hotent.table.model.impl.DefaultTable;
import com.hotent.table.operator.IViewOperator;

/**
 * 获取视图信息基类
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月10日
 */
public abstract class BaseViewOperator extends BaseDbType implements IViewOperator{
	
	/**
	 * 获取数据类型
	 * 
	 * @param type
	 *            字段类型
	 * @return 数据类型
	 */
	public abstract String getType(String type);

	/**
	 * 根据视图名称获取model。
	 * 
	 * @param viewName
	 *            视图名
	 * @return 表模型
	 * @throws SQLException
	 */
	public Table getModelByViewName(String viewName) throws SQLException {
		ResultSet rs = null;
		Table table = new DefaultTable();
		table.setTableName(viewName);
		table.setComment(viewName);
		try (Connection conn = jdbcTemplate.getDataSource().getConnection();Statement stmt = (Statement) conn.createStatement();){
			
			stmt.setMaxRows(1);
			rs = stmt.executeQuery("select * from " + viewName); 
			ResultSetMetaData metadata = rs.getMetaData();
			int count = metadata.getColumnCount();
			// 从第二条记录开始
			for (int i = 1; i <= count; i++) {
				Column column = new DefaultColumn();
				String columnName = metadata.getColumnName(i);
				String typeName = metadata.getColumnTypeName(i);
				String dataType = getType(typeName);
				column.setFieldName(columnName);
				column.setColumnType(dataType);
				column.setComment(columnName);
				table.addColumn(column);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
		}
		return table;
	}

	/**
	 * 获取查询的列表
	 * @param sql			sql
	 * @param pageBean		分页对象
	 * @param elementType	返回数据类型	
	 * @return				数据
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected <T> PageList<T> getForList(String sql, PageBean pageBean, Class<T> elementType) throws Exception {
		if (pageBean != null) {
			CommonManager commonManager = AppUtil.getBean(CommonManager.class);
			PageList<T> result = (PageList<T>) commonManager.query(sql, pageBean);
			return result;
		} else {
			List<T> queryForList = this.jdbcTemplate.queryForList(sql, elementType);
			return new PageList<T>(queryForList);
		}
	}

	/**
	 * 获取查询的列表
	 * @param sql		sql
	 * @param pageBean	分页对象
	 * @param rowMapper	结果mapper
	 * @return			数据
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected <T> PageList<T> getForList(String sql, PageBean pageBean, RowMapper<T> rowMapper) throws Exception {
		if (pageBean != null) {
			CommonManager commonManager = AppUtil.getBean(CommonManager.class);
			PageList<T> result = (PageList<T>) commonManager.query(sql, pageBean);
			return result;
		} else {
			List<T> queryForList = this.jdbcTemplate.query(sql, rowMapper);
			return new PageList<T>(queryForList);
		}
	}

}
