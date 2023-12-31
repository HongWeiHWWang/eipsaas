package com.hotent.base.sqlbuilder;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.model.Column;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;





/**
 * <pre>
 * 描述：TODO
 * 构建组：x5-base-db
 * 作者：lyj
 * 邮箱:liyj@jee-soft.cn
 * 日期:2014-7-16-下午4:33:40
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class MySqlSqlBuilder extends AbstractSqlBuilder {
	public MySqlSqlBuilder() {
		super();
	}

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

	public static void main(String[] args) {
		// String fromName = "tbtest";
		// JSONArray resultField =
		// JSONArray.fromObject("[{\"field\":\"birthday\",\"comment\":\"生日\",\"AggFuncOp\":\"\"},{\"field\":\"name\",\"comment\":\"名称\",\"AggFuncOp\":\"\"}]");
		ArrayNode conditionField = JsonUtil.getMapper().createArrayNode();
		ObjectNode jo =  JsonUtil.getMapper().createObjectNode();
		jo.put("field", "birthday");
		jo.put("op", QueryOP.BETWEEN+"");
		jo.put("dbType", Column.COLUMN_TYPE_DATE);
		ObjectNode value =  JsonUtil.getMapper().createObjectNode();
		value.put("start", "2000-1-1");
		value.put("end", "2016-1-1");
		jo.set("value", value);
		conditionField.add(jo);
		// JSONArray sortField = new JSONArray();
		// SqlBuilder builder = new MySqlSqlBuilder(fromName, resultField,
		// conditionField, sortField);
		// System.out.println(builder.getSql());
	}
}
