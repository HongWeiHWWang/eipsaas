package com.hotent.table.operator.impl.postgresql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import com.hotent.base.util.StringUtil;
import com.hotent.table.model.Index;
import com.hotent.table.model.impl.DefaultIndex;
import com.hotent.table.operator.impl.BaseIndexOperator;

/**
 * postgreSQL 索引操作的实现
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月25日
 */
public class PostgreSQLIndexOperator extends BaseIndexOperator {
	// 批量操作的
	protected int BATCH_SIZE = 100;

	@Override
	public void createIndex(Index index) throws SQLException {
		String sql = genIndexDDL(index);
		jdbcTemplate.execute(sql);
		index.setIndexDdl(sql);
	}

	/**
	 * 生成Index对象对应的DDL语句
	 * 
	 * @param index
	 * @return
	 */
	private String genIndexDDL(Index index) {
		StringBuffer ddl = new StringBuffer();
		ddl.append("CREATE");
		if(StringUtil.isNotEmpty(index.getIndexType())){
			ddl.append(" "+index.getIndexType()+" ");
		}
		ddl.append(" INDEX");
		ddl.append(" " + index.getIndexName());
		ddl.append(" ON " + index.getTableName());
		ddl.append("(");
		for (String column : index.getColumnList()) {
			ddl.append(column + ",");
		}
		if (!StringUtils.isEmpty(index.getIndexComment())) {
			ddl.append("COMMENT '" + index.getIndexComment() + "'");
		}
		ddl.replace(ddl.length() - 1, ddl.length(), ")");
		return ddl.toString();
	}

	@Override
	public void dropIndex(String tableName, String indexName) {
		String sql = "drop index " + indexName + " on " + tableName;
		jdbcTemplate.execute(sql);
	}

	@Override
	public void rebuildIndex(String tableName, String indexName) {
		String sql = "SHOW CREATE TABLE " + tableName;
		List<String> ddls = jdbcTemplate.query(sql, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("Create Table");
			}
		});
		String ddl = ddls.get(0);
		
		Pattern pattern = Pattern.compile("ENGINE\\s*=\\s*\\S+", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(ddl);
		if (matcher.find()) {
			String str = matcher.group();
			String sql_ = "ALTER TABLE " + tableName + " " + str;
			jdbcTemplate.execute(sql_);
			//System.out.println("sql_-------------->:"+ddl);
		}
	}

	@Override
	public List<Index> getIndexByName(String indexName) throws SQLException {
		List<String> allTableNames = getAllTableNames();
		List<Index> allIndexs = new ArrayList<Index>();
		for(String tableName:allTableNames){
			allIndexs.addAll(getIndexsByTable(tableName));
		}
		
		List<Index> indexs = new ArrayList<Index>();
		for(Index index :allIndexs){
			if(index.getIndexName().contains(indexName)){
				indexs.add(index);
			}
		}
		return indexs;
	}

	@Override
	public List<Index> getIndexsByTable(String tableName) throws SQLException {
		String sql = new String("show index from ?tableName;");
		sql = sql.replace("?tableName", tableName);
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		List<String> indexNames = new ArrayList<String>();
		List<Index> indexs = new ArrayList<Index>();
		for (Map<String, Object> row : rows) {
			String indexName = row.get("Key_name") + "";
			if (!indexNames.contains(indexName)) {
				indexNames.add(indexName);
				indexs.add(getIndex(tableName, indexName));
			}
		}

		return indexs;
	}

	@Override
	public Index getIndex(String tableName, String indexName) throws SQLException {
		String sql = new String("show index from ?tableName where key_name='?indexName';");
		sql = sql.replace("?tableName", tableName);
		sql = sql.replace("?indexName", indexName);
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

		Index index = null;
		List<String> columnList = new ArrayList<String>();
		for (Map<String, Object> row : rows) {
			/*
			 * for(String key:row.keySet()){ System.out.println(key+":"+row.get(key)); }
			 */
			if (index == null) {
				index = new DefaultIndex();
				index.setPkIndex(indexName.equals("PRIMARY"));
				index.setIndexComment(row.get("Comment") + "");
				index.setIndexName(row.get("Key_name") + "");
				index.setIndexType(row.get("Index_type") + "");
				index.setTableName(tableName);
				index.setUnique((row.get("Non_unique") + "").equals("0"));
			}
			columnList.add(row.get("Column_name") + "");
		}
		if(index!=null){
			index.setColumnList(columnList);
		}
		return index;
	}

	private List<String> getAllTableNames() {
		List<String> tableNames = new ArrayList<String>();

		String sql = "show tables;";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		for (Map<String, Object> row : rows) {
			for (Object object : row.values()) {
				tableNames.add(object+"");
			}
		}

		return tableNames;
	}
}
