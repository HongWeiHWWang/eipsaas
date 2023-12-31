package com.hotent.base.sqlbuilder.service;

import com.hotent.base.sqlbuilder.ISqlBuilder;
import com.hotent.base.sqlbuilder.SqlBuilderModel;

public interface ISqlBuilderService {
	
	String getSql(SqlBuilderModel model);

	ISqlBuilder getSqlBuilder(SqlBuilderModel model);
	
}
