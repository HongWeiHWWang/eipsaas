package com.hotent.base.sqlbuilder.service.impl;

/**
 * 描述：TODO
 * 包名：com.hotent.platform.bpm.sqlbuilder
 * 文件名：AbstractHandlerSqlBuilder.java
 * 作者：User-mailto:liyj@jee-soft.cn
 * 日期2014-7-16-下午6:23:30
 *  2014广州宏天软件有限公司版权所有
 * 
 */

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.hotent.base.sqlbuilder.ISqlBuilder;
import com.hotent.base.sqlbuilder.MySqlSqlBuilder;
import com.hotent.base.sqlbuilder.OracleSqlBuilder;
import com.hotent.base.sqlbuilder.SqlBuilderModel;
import com.hotent.base.sqlbuilder.SqlServerSqlBuilder;
import com.hotent.base.sqlbuilder.service.ISqlBuilderService;


@Service("sqlBuilderService")
public class SqlBuilderService implements ISqlBuilderService{
	protected  Map<String,ISqlBuilder> builderMap=new HashMap<String, ISqlBuilder>() ;


	@Autowired
	ApplicationContext context;
	public void setBuilderMap(Map<String, ISqlBuilder> builderMap) {
		this.builderMap = builderMap;
	}

	@Override
	public String getSql(SqlBuilderModel model) {
		ISqlBuilder builder=getSqlBuilder(model);
		if(builder!=null){
			return builder.getSql();
		}
		return "";
	}

	@Override
	public ISqlBuilder getSqlBuilder(SqlBuilderModel model){
		String dbType=model.getDbType();
		ISqlBuilder builder=builderMap.get(dbType);
		if(builder==null){
			if("mysql".equals(dbType)){
				builder=new MySqlSqlBuilder();
			}else if("oracle".equals(dbType)){
				builder=new OracleSqlBuilder();
			}else{
				builder=new SqlServerSqlBuilder();
			}
		}
		builder.setModel(model);

		return builder;
	}
}
