package com.hotent.base.sqlbuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.model.Column;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;


/**
 * 描述：TODO
 * 包名：com.hotent.base.db.sql
 * 文件名：DefaultSqlBuilder.java
 * 作者：User-mailto:liyj@jee-soft.cn
 * 日期2014-7-16-下午3:33:48
 *  2014广州宏天软件有限公司版权所有
 * 
 */



/**
 * <pre>
 * 描述：TODO
 * 构建组：x5-base-db
 * 作者：lyj
 * 邮箱:liyj@jee-soft.cn
 * 日期:2014-7-16-下午3:33:48
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public abstract class AbstractSqlBuilder implements ISqlBuilder {
	protected SqlBuilderModel sqlBuilderModel;

	public AbstractSqlBuilder() {
		super();
	}

	@Override
	public void setModel(SqlBuilderModel model) {
		this.sqlBuilderModel = model;
	}

	public String analyzeResultField() {
		StringBuffer sql = new StringBuffer();

		ArrayNode resultField = sqlBuilderModel.getResultField();
		if (resultField == null|| resultField.size()==0) {
			sql.append("* ");
		} else {
			for (int i = 0; i < resultField.size(); i++) {
				JsonNode jo = resultField.get(i);
				String field = jo.get("field").asText();
				String aggFuncOp = jo.get("AggFuncOp").asText();

				sql.append(aggFuncOp);
				sql.append("(" + field + ") ");// 拼装成eg：SELECT
												// count(id_),(dsalias_)
				if (!StringUtil.isEmpty(field)) {
					sql.append(" as " + field + " ");
				}

				if (i < resultField.size() - 1) {// 不是最后一个元素
					sql.append(",");
				}
			}
		}

		return sql.toString();

	}

	/**
	 * 
	 * @exception
	 * @since 1.0.0
	 */
	public String analyzeConditionField() {
        boolean isOr = false;//是否为或者查询
		StringBuffer sql = new StringBuffer();
		ArrayNode conditionField = sqlBuilderModel.getConditionField();

		if (conditionField == null ) {
			return sql.toString();
		}

		for (int i = 0; i < conditionField.size(); i++) {
			ObjectNode jo = (ObjectNode) conditionField.get(i);
			Object value = jo.get("value");
			if(value!=null){
				value=((JsonNode) value).asText();
			}
			if (JsonUtil.getString(jo, "isScript").equals("1")) {// 脚本加上去就行
				sql.append(value);
				continue;
			}
			String field = jo.get("field").asText();
			String dbType = jo.get("dbType").asText();

			QueryOP op = QueryOP.getByVal(jo.get("op").asText());
			if (QueryOP.IS_NULL .equals(op)) {
				sql.append(" and " + field + " is null ");
				continue;
			}

			if (QueryOP.NOTNULL.equals(op)) {
				sql.append(" and " + field + " is not null ");
				continue;
			}

			if ( QueryOP.IN.equals(op)) {
				String v = "";
				String[] vals = value.toString().split(",");
				for (int j = 0; j < vals.length; j++) {
					v += "'" + vals[j] + "'";
					if (j != vals.length - 1) {
						v += ",";
					}
				}
				sql.append(" and " + field + " " + op.op() + "(" + v + ") ");
				continue;
			}

			if (dbType.equals(Column.COLUMN_TYPE_VARCHAR) || dbType.equals(Column.COLUMN_TYPE_CLOB)) {
				if (QueryOP.EQUAL.equals(op)  || QueryOP.NOT_EQUAL.equals(op)) {
                    String relation = "and";
                    if(isOr){
                        if(jo.get("relation").asText().equals("null")){
                            relation = "and";
                        }else{
                            relation = jo.get("relation").asText();
                        }
                    }else{
                        isOr = true;
                    }
					sql.append(" "+relation+" " + field + op.op() + "'" + value.toString() + "' ");
				} else if (QueryOP.LIKE.equals(op)) {
				    String relation = "and";
				    if(isOr){
                        if(jo.get("relation").asText().equals("null")){
                            relation = "and";
                        }else{
                            relation = jo.get("relation").asText();
                        }
                    }else{
                        isOr = true;
                    }
					sql.append(" "+relation+" " + field + " like '%" + value.toString() + "%' ");
				} else if (QueryOP.RIGHT_LIKE.equals(op)) {
					sql.append(" and " + field + " like '" + value.toString() + "%' ");
				} else if (QueryOP.LEFT_LIKE.equals(op)) {
					sql.append(" and " + field + " like '%" + value.toString() + "' ");
				} else if (QueryOP.EQUAL_IGNORE_CASE.equals(op)) {
					sql.append(" and upper(" + field + ") " + op.op() + "'" + value.toString().toUpperCase() + "' ");
				}
			} else if (dbType.equals(Column.COLUMN_TYPE_INT) || dbType.equals(Column.COLUMN_TYPE_NUMBER)) {
				if (QueryOP.BETWEEN.equals(op)) {
					
					ObjectNode value1 = (ObjectNode) value;
					String start = JsonUtil.getString( value1,"start","");
					String end = JsonUtil.getString(value1, "end", "");
					if (StringUtil.isNotEmpty(start)) {
						sql.append(" and " + field + " " + ">=" + "'" + start + "' ");
					}
					if (StringUtil.isNotEmpty(end)) {
						sql.append(" and " + field + " " + "<=" + "'" + end + "' ");
					}
				} else {
					sql.append(" and " + field + op.op() + "'" + value.toString() + "' ");
				}
			} else if (dbType.equals(Column.COLUMN_TYPE_DATE)) {
				handleDbTypeEqualDate(sql, field, op, value);
			}
		}

		return sql.toString();

	}

	/**
	 * 
	 * 处理字段类型等于date的时候
	 * 
	 * @param field
	 * @param op
	 * @param value
	 *            void
	 * @exception
	 * @since 1.0.0
	 */
	public abstract void handleDbTypeEqualDate(StringBuffer sql, String field, QueryOP op, Object value);

	public String analyzeSortField() {
		StringBuffer sql = new StringBuffer();

		ArrayNode sortField = sqlBuilderModel.getSortField();

		if (sortField == null || sortField.size()==0) {
			return "";
		}
		sql.append("ORDER BY  ");
		for (int i = 0; i < sortField.size(); i++) {
			JsonNode jo = sortField.get(i);
			String field = jo.get("field").asText();
			String sortType = jo.get("sortType").asText();
			sql.append(field + " " + sortType);
			if (i < sortField.size() - 1) {// 不是最后一个元素
				sql.append(",");
			}
		}
		return sql.toString();
	}

	@Override
	public String getSql() {
		StringBuffer sql = new StringBuffer("select ");

		sql.append(analyzeResultField());
		sql.append("from " + sqlBuilderModel.getFromName() + " ");
		sql.append(" where 1=1 ");
		sql.append(analyzeConditionField());
		sql.append(analyzeSortField());

		return sql.toString();
	}

}
