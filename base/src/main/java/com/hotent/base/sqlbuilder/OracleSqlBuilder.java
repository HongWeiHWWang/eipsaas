package com.hotent.base.sqlbuilder;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;



/**
 * <pre>
 * 描述：TODO
 * 构建组：x5-base-db
 * 作者：lyj
 * 邮箱:liyj@jee-soft.cn
 * 日期:2014-7-16-下午5:05:36
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class OracleSqlBuilder extends AbstractSqlBuilder {

	public OracleSqlBuilder() {
		super();
	}

	@Override
	// TODO 日期格式已写死- -
	public void handleDbTypeEqualDate(StringBuffer sql, String field, QueryOP op, Object value) {
		if (op == QueryOP.BETWEEN) {
			ObjectNode value1 = (ObjectNode) value;
			String start = JsonUtil.getString( value1,"start","");
			String end = JsonUtil.getString(value1, "end", "");
			if (StringUtil.isNotEmpty(start)) {
				sql.append(" and " + field + " " + ">=" + " to_date('" + start + "','yyyy-mm-dd hh24:mi:ss')");
			}
			if (StringUtil.isNotEmpty(end)) {
				sql.append(" and " + field + " " + "<=" + " to_date('" + end + "','yyyy-mm-dd hh24:mi:ss')");
			}
		} else {
			sql.append(" and " + field + " " + op.op() + " to_date('" + value.toString() + "','yyyy-mm-dd hh24:mi:ss')");
		}
	}



}
