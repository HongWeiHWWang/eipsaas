package com.hotent.form.persistence.manager.impl;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.constants.SQLConst;
import com.hotent.base.datasource.DatabaseContext;
import com.hotent.base.datasource.DatabaseSwitchResult;
import com.hotent.base.groovy.GroovyScriptEngine;
import com.hotent.base.handler.MultiTenantHandler;
import com.hotent.base.manager.CommonManager;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.query.FieldRelation;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.sqlbuilder.SqlBuilderModel;
import com.hotent.base.sqlbuilder.service.ISqlBuilderService;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.form.datatrans.ITypeConvert;
import com.hotent.form.datatrans.ResultTransform;
import com.hotent.form.model.CustomQuery;
import com.hotent.form.persistence.dao.CustomQueryDao;
import com.hotent.form.persistence.manager.CustomQueryManager;
import com.hotent.form.util.CustomUtil;
import com.hotent.form.vo.CustomQueryControllerVo;
import com.hotent.table.meta.impl.BaseTableMeta;
import com.hotent.table.operator.IViewOperator;
import com.hotent.table.util.MetaDataUtil;

/**
 * 自定义查询
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月14日
 */
@Service("customQueryManager")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CustomQueryManagerImpl extends BaseManagerImpl<CustomQueryDao, CustomQuery> implements CustomQueryManager {
	@Resource
	CommonManager commonManager;
	@Resource
	ISqlBuilderService sqlBuilderService;
	@Resource
	GroovyScriptEngine groovyScriptEngine;
	@Resource
	MultiTenantHandler tenantHandler;
	@Resource
	DatabaseContext databaseContext;

	@Override
	public PageList getData(CustomQuery customQuery, String queryData, String dsType, int pageNo, int pageSize) throws IOException{
		String sql = getSql(customQuery, queryData, dsType);
		sql=sql.replaceAll("\"", "");
		PageList pageList = null;
		List list = null;
		if(customQuery.getNeedPage()==1){
			pageNo=pageNo<=0?1:pageNo;
			pageSize=pageSize<=0?10:pageSize;
			PageBean page = new PageBean(pageNo, pageSize);
			pageList = commonManager.query(sql, page);
		}
		else{
			list = commonManager.query(sql);
			pageList = new PageList(list);
		}


		// 格式化
		ResultTransform.transform(pageList.getRows(), new ITypeConvert() {
			@Override
			public Object processValue(Object obj) {
				Map<String, Object> map = (Map<String, Object>) obj;
				for (String key : map.keySet()) {
					if (BeanUtils.isEmpty(map.get(key)))
						continue;
					if (!Date.class.isAssignableFrom(map.get(key).getClass()) && !java.util.Date.class.isAssignableFrom(map.get(key).getClass()))
						continue;
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					try {
						map.put(key, sdf.format(map.get(key)));
					} catch (Exception e) {
					}
				}

				return map;
			}
		});
		return pageList;
	}

	/**
	 * 把customQuery根据一些参数翻译成SqlBuilderModel
	 * 
	 * @param customQuery
	 * @param queryData
	 *            ：页面传来的参数：JSONARRAY格式
	 * @param dsType
	 *            ：数据源的类型
	 * @return SqlBuilderModel
	 * @throws IOException 
	 * @exception
	 * @since 1.0.0
	 */
	private SqlBuilderModel buildSqlBuilderModel(CustomQuery customQuery, String queryData, String dsType) throws IOException {
		SqlBuilderModel sqlBuilderModel = new SqlBuilderModel();

		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtil.isNotEmpty(queryData)) {
			ArrayNode jArray = (ArrayNode) JsonUtil.toJsonNode(queryData);
			for (int i = 0; i < jArray.size(); i++) {
				ObjectNode jSONObject = (ObjectNode) JsonUtil.toJsonNode(jArray.get(i));
				if (BeanUtils.isNotEmpty(jSONObject.get("value"))) {
					params.put(jSONObject.get("key").asText(), jSONObject.get("value").asText());
				}
			}
		}

		// setting
		sqlBuilderModel.setDbType(dsType);
		sqlBuilderModel.setFromName(customQuery.getObjName());
		sqlBuilderModel.setResultField((ArrayNode) JsonUtil.toJsonNode(customQuery.getResultfield()));// 返回字段

		ArrayNode conditionField = JsonUtil.getMapper().createArrayNode();// 条件字段
		ArrayNode confilJA = (ArrayNode) JsonUtil.toJsonNode(customQuery.getConditionfield());
		try {
			ArrayNode queryDataArry = (ArrayNode) JsonUtil.toJsonNode(queryData);
			if(BeanUtils.isNotEmpty(queryDataArry)&&queryDataArry.size()>0){
				ArrayNode extArray = JsonUtil.getMapper().createArrayNode();
				for (JsonNode quryNode : queryDataArry) {
					boolean isIn = false;
					for (JsonNode confNode : confilJA) {
						if(quryNode.get("key").asText().equals(confNode.get("field").asText())
								||quryNode.get("key").asText().equals(confNode.get("comment").asText())){
							isIn = true;
							break;
						}
						if(!isIn){
							extArray.add(quryNode);
						}
					}
				}
				if(extArray.size()>0){
					getMoreConditionField(customQuery.getResultfield(), (ObjectNode)JsonUtil.toJsonNode(queryDataArry.get(0)), confilJA);
				}
			}
			if(BeanUtils.isNotEmpty(queryDataArry) && queryDataArry.size()>0){
				for (Object object1 : queryDataArry) {
					ObjectNode qObj = (ObjectNode) JsonUtil.toJsonNode(object1);
					boolean isIn = false;
					for (Object object2 : confilJA) {
						ObjectNode confObj = (ObjectNode) JsonUtil.toJsonNode(object2);
						if(qObj.get("key").asText().equals(confObj.get("field").asText())||
								qObj.get("key").asText().equals(confObj.get("comment").asText())){
							isIn = true;
						}
					}
					if(!isIn){
						getMoreConditionField(customQuery.getResultfield(), qObj, confilJA);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < confilJA.size(); i++) {
			ObjectNode jObject = (ObjectNode) JsonUtil.toJsonNode(confilJA.get(i));

			String field = jObject.get("field").asText();
			String defaultType = jObject.get("defaultType").asText();
			String defaultValue = jObject.get("defaultValue").asText();
			String dbType = jObject.get("dbType").asText();
			String condition = jObject.get("condition").asText();
			Object value = null;

			ObjectNode jsonObject = JsonUtil.getMapper().createObjectNode();
			jsonObject.put("field", field);
			jsonObject.put("op", condition);
			jsonObject.put("dbType", dbType);

			value = CustomUtil.buildValue(field, defaultType, defaultValue, params);
			// 为空就不加入构建条件
			if (BeanUtils.isEmpty(value)) continue;
			// 是空字符也不构建	
			if ((value instanceof String) && (StringUtil.isEmpty(value.toString()) || value.toString().equals("|"))) {
				continue;
			}

			if (condition.equals(QueryOP.BETWEEN.toString())) {// between的值特殊处理
				value = CustomUtil.handleDateBetweenValue(value);
			}

			jsonObject.set("value", JsonUtil.toJsonNode(value));

			conditionField.add(jsonObject);
		}
		sqlBuilderModel.setConditionField(conditionField);

		sqlBuilderModel.setSortField((ArrayNode)JsonUtil.toJsonNode(customQuery.getSortfield()));// 排序字段

		return sqlBuilderModel;
	}

	private void getMoreConditionField(String resultField,ObjectNode qObj,ArrayNode confilJA) throws IOException{
		ArrayNode resultFields = (ArrayNode) JsonUtil.toJsonNode(resultField);
		for (Object object : resultFields) {
			ObjectNode obj = (ObjectNode) JsonUtil.toJsonNode(object);
			if(obj.get("comment").asText().equals(qObj.get("key").asText())){
				ObjectNode newCondi = JsonUtil.getMapper().createObjectNode();
				newCondi.put("field", obj.get("field").asText());
				newCondi.put("comment", obj.get("comment").asText());
				newCondi.put("condition", "LK");
				newCondi.put("dbType", "varchar");
				newCondi.put("defaultType", 2);
				newCondi.put("defaultValue", qObj.get("value").asText());
				confilJA.add(newCondi);
				break;
			}
		}
	}

	private String getSql(CustomQuery customQuery, String queryData, String dsType) throws IOException {
		String sql="";
		if(customQuery.getSqlBuildType()==1){
			Map<String,Object> params=new HashMap<String, Object>();
			if(StringUtil.isNotEmpty(queryData)){
				ArrayNode json=(ArrayNode) JsonUtil.toJsonNode(queryData);
				Map<String,Object> tmp=new HashMap<String, Object>();
				boolean isEmpty = true;
				for(JsonNode jsonObj :json){
					tmp.put(jsonObj.get("key").asText(), jsonObj.get("value").asText());
					isEmpty = false;
				}
				if(!isEmpty){
					params.put("map", tmp);
					params.putAll(tmp);
				}
			}
			sql=groovyScriptEngine.executeString(customQuery.getDiySql(), params);
		}
		else{
			SqlBuilderModel model = buildSqlBuilderModel(customQuery, queryData, dsType);
			sql= sqlBuilderService.getSql(model);
		}
		return sql;
	}

	@Override
	public CustomQuery getByAlias(String alias) {
		QueryFilter queryFilter =QueryFilter.build();
		queryFilter.addFilter("alias_", alias, QueryOP.EQUAL,FieldRelation.AND);
		PageList<CustomQuery> list = query(queryFilter);
		List<CustomQuery> customQueries=list.getRows();
		if (customQueries == null || customQueries.isEmpty())
			return null;
		return customQueries.get(0);
	}

	@Override
	public ArrayNode getTableOrViewByDsName(CustomQueryControllerVo vo) throws Exception {

		ArrayNode jsonArray = JsonUtil.getMapper().createArrayNode();
		if ("1".equals(vo.getIsTable())) {
			Map<String, String> map;
			try(DatabaseSwitchResult dResult = databaseContext.setDataSource(vo.getDsalias())){
				BaseTableMeta baseTableMeta = MetaDataUtil.getBaseTableMetaAfterSetDT(dResult.getDbType());// 获取表操作元
				map = baseTableMeta.getTablesByName(vo.getObjName());
			}
			String tenantCode = tenantHandler.getTenantCode();
			List<String> ignoreTableNames = tenantHandler.getIgnoreTableNames();
			Pattern regex = null;
			if(StringUtil.isNotEmpty(tenantCode)) {
				String exp = String.format("^%s(%s)_.*$", SQLConst.CUSTOMER_TABLE_PREFIX.toLowerCase(), tenantCode.toLowerCase());
				regex = Pattern.compile(exp);
			}

			for (String key : map.keySet()) {
				// 1.通用的物理表：判断该物理表是否需要忽略，忽略的情况下不返回给当前租户
				if(ignoreTableNames.stream().anyMatch((e) -> e.equalsIgnoreCase(key))) {
					continue;
				}

				// 2.数据建模生成的物理表，按照所归属的租户过滤
				if(key.toLowerCase().startsWith(SQLConst.CUSTOMER_TABLE_PREFIX.toLowerCase()) && regex!=null) {
					Matcher matcher = regex.matcher(key.toLowerCase());
					if(matcher==null || !matcher.find()) {
						continue;
					}
				}

				ObjectNode jsonObject = JsonUtil.getMapper().createObjectNode();
				jsonObject.put("name", key.toString());
				jsonObject.put("comment", map.get(key).toString());
				jsonArray.add(jsonObject);
			}
		} else {
			PageList<String> viewNames;
			try(DatabaseSwitchResult dResult = databaseContext.setDataSource(vo.getDsalias())){
				IViewOperator iViewOperator = MetaDataUtil.getIViewOperatorAfterSetDT(dResult.getDbType());
				viewNames = iViewOperator.getViews(vo.getObjName());
			}
			for (String name : viewNames.getRows()) {
				ObjectNode jsonObject = JsonUtil.getMapper().createObjectNode();
				jsonObject.put("name", name);
				jsonObject.put("comment", name);
				jsonArray.add(jsonObject);
			}
		}
		return jsonArray;
	}
}
