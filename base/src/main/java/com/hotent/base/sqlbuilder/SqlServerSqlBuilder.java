package com.hotent.base.sqlbuilder;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;



public class SqlServerSqlBuilder extends AbstractSqlBuilder {

	@Override
	public void handleDbTypeEqualDate(StringBuffer sql, String field, QueryOP op, Object value) {
		if (op == QueryOP.BETWEEN) {
			ObjectNode value1 = (ObjectNode) value;
			String start = JsonUtil.getString( value1,"start","");
			String end = JsonUtil.getString(value1, "end", "");
			if (StringUtil.isNotEmpty(start)) {
				sql.append(" and " + field + " " + ">=" + " '" + start + "'");
			}
			if (StringUtil.isNotEmpty(end)) {
				sql.append(" and " + field + " " + "<=" + " '" + end + "'");
			}
		} else {
			sql.append(" and " + field + " " + op.op() + " '" + value.toString() + "'");
		}

	}

}
