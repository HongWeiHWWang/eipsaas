package com.hotent.table.conf;

import javax.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import com.hotent.base.datasource.DatabaseContext;
import com.hotent.table.factory.DatabaseFactory;
import com.hotent.table.meta.impl.BaseTableMeta;
import com.hotent.table.operator.IIndexOperator;
import com.hotent.table.operator.ITableOperator;
import com.hotent.table.operator.IViewOperator;

@Configuration
public class DatabaseFactoryConfig {
	@Resource
	JdbcTemplate jdbcTemplate;
	@Resource
	DatabaseContext databaseContext;

	@Bean
	public ITableOperator initTableOperator() throws Exception{
		ITableOperator tableOperator = DatabaseFactory.getTableOperator(databaseContext.getDbType());
		tableOperator.setJdbcTemplate(jdbcTemplate);
		return tableOperator;
	}

	@Bean
	public BaseTableMeta initBaseTableMeta() throws Exception{
		BaseTableMeta tableMetaByDbType = DatabaseFactory.getTableMetaByDbType(databaseContext.getDbType());
		tableMetaByDbType.setJdbcTemplate(jdbcTemplate);
		return tableMetaByDbType;
	}

	@Bean
	public IIndexOperator initIIndexOperator() throws Exception{
		IIndexOperator indexOperator = DatabaseFactory.getIndexOperator(databaseContext.getDbType());
		indexOperator.setJdbcTemplate(jdbcTemplate);
		return indexOperator;
	}

	@Bean
	public IViewOperator initIViewOperator() throws Exception{
		IViewOperator viewOperator = DatabaseFactory.getViewOperator(databaseContext.getDbType());
		viewOperator.setJdbcTemplate(jdbcTemplate);
		return viewOperator;
	}
}
