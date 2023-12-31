package com.hotent.form.persistence.manager.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.annotation.Resource;

import com.hotent.base.query.*;
import com.hotent.form.model.CombinateDialog;
import com.hotent.form.persistence.manager.CombinateDialogManager;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.datasource.DatabaseContext;
import com.hotent.base.datasource.DatabaseSwitchResult;
import com.hotent.base.groovy.GroovyScriptEngine;
import com.hotent.base.manager.CommonManager;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.sqlbuilder.ISqlBuilder;
import com.hotent.base.sqlbuilder.SqlBuilderModel;
import com.hotent.base.sqlbuilder.service.ISqlBuilderService;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.MapUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.time.DateFormatUtil;
import com.hotent.bo.model.BoAttribute;
import com.hotent.bo.model.BoEnt;
import com.hotent.bo.persistence.manager.BoAttributeManager;
import com.hotent.bo.persistence.manager.BoEntManager;
import com.hotent.form.model.CustomDialog;
import com.hotent.form.persistence.dao.CustomDialogDao;
import com.hotent.form.persistence.manager.CustomDialogManager;

/**
 * 自定义对话框
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月14日
 */
@Service("customDialogManager")
@SuppressWarnings({ "rawtypes", "unchecked" ,"deprecation"})
public class CustomDialogManagerImpl extends BaseManagerImpl<CustomDialogDao, CustomDialog> implements CustomDialogManager {
	@Resource
	CombinateDialogManager combinateDialogManager;
	@Resource
	ISqlBuilderService sqlBuilderService;
	@Resource
	BoEntManager boEntManager;
	@Resource
	BoAttributeManager boAttributeManager;
	@Resource
	GroovyScriptEngine groovyScriptEngine;
	@Resource
	DatabaseContext databaseContext;
	@Resource
	CommonManager commonManager;

	@Override
	public CustomDialog getByAlias(String alias) {
		CustomDialog byAlias = baseMapper.getByAlias(alias);
		if(byAlias == null || byAlias.getStyle() == 1){
			return byAlias;
		}
		String displayfield = byAlias.getDisplayfield();
		String resultfield = byAlias.getResultfield();
		String conditionfield = byAlias.getConditionfield();
		try {
			if(!displayfield.equals("[]")){
				JsonNode jsonNode = JsonUtil.toJsonNode(displayfield);
				String disPlay = getNewField(jsonNode, displayfield);
				byAlias.setDisplayfield(disPlay);
			}

			if(!resultfield.equals("[]")){
				JsonNode jsonNode1 = JsonUtil.toJsonNode(resultfield);
				String resultField = getNewField(jsonNode1, resultfield);
				byAlias.setResultfield(resultField);
			}

			if(!conditionfield.equals("[]")){
				JsonNode jsonNode2 = JsonUtil.toJsonNode(conditionfield);
				String condition = getNewField(jsonNode2, conditionfield);
				byAlias.setConditionfield(condition);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return byAlias;
	}

	private String getNewField(JsonNode jsonNode,String str){
		String resultStr = null;
		for(int i=0;i<jsonNode.size();i++){
			String field = jsonNode.get(i).get("field").asText().toUpperCase();
			if(str.indexOf(jsonNode.get(i).get("field").asText()) != -1){
				String rfield = ":\""+jsonNode.get(i).get("field").asText()+"\"";
				String brfield = ":\""+field+"\"";
				if(resultStr != null){
					resultStr = resultStr.replace(rfield, brfield);
				}else{
					resultStr = str.replace(rfield, brfield);
				}
			}
		}
		return resultStr;
	}

	@Override
    public PageList getListData(CustomDialog customDialog, Map<String, Object> param, PageBean pageBean) throws Exception {
        String sql ="";
		List<BoEnt> boEntList = null;
		List<BoAttribute> attr = null;
		Map attrMap = new HashMap();
        if(customDialog.getSqlBuildType()==1){
            Map<String,Object> params=new HashMap<String, Object>();
            params.put("map", param);
            params.putAll(param);
            sql=groovyScriptEngine.executeString(customDialog.getDiySql(), params);
        }
        else{
        	String dbType = databaseContext.getDbTypeByAlias(customDialog.getDsalias());
    		SqlBuilderModel model = constructSqlBuilderModel(customDialog, param, dbType);
			String fromName = model.getFromName();
			if(StringUtil.isNotEmpty(fromName)){
				boEntList = (List<BoEnt>) boEntManager.getByTableName(fromName);
				if(boEntList.size()>0) {
					for(BoEnt boEnt:boEntList) {
						attr = boAttributeManager.getByBoEnt(boEnt);
						for (BoAttribute attribute : attr) {
							if ("yyyy-MM-dd".equals(attribute.getFormat())) {
								String fieldName = attribute.getFieldName();
								attrMap.put(fieldName.toUpperCase(), fieldName.toUpperCase());
							}
						}
					}
				}
			}
			ISqlBuilder sqlBuilder = sqlBuilderService.getSqlBuilder(model);
            sql = sqlBuilder.getSql();
        }

        List list = null;
		PageList<Map<String, Object>> pageList = null;
        try (DatabaseSwitchResult dResult = databaseContext.setDataSource(customDialog.getDsalias())){
	        if (customDialog.getNeedPage()) {
	            PageBean page = pageBean == null ? new PageBean() : pageBean;
				pageList = commonManager.query(sql, page);
	        } else {
	            list = commonManager.query(sql);
	        }
        }
		if(list == null){
			list = pageList.getRows();
		}
        //大小写兼容处理
        for(int i=0;i<list.size();i++){
            Map<String, Object> m =(Map<String, Object>) list.get(i);
            Map<String, Object> tm = new HashMap<String, Object>();
            for(String k : m.keySet()){
                try {
                    if(BeanUtils.isNotEmpty(m.get(k))&&"java.sql.Timestamp".equals(m.get(k).getClass().getCanonicalName())){
                        Date date = (Date) m.get(k);
                        if(date.getYear()==70&&date.getMonth()==0&&date.getDate()==1){
                            tm.put(k.toUpperCase(),DateFormatUtil.format(LocalDateTime.now(), "HH:mm:ss"));
                        }else if(date.getMinutes()==0&&date.getHours()==0&&date.getSeconds()==0 && k.toUpperCase().equals(attrMap.get(k.toUpperCase()))){
                            tm.put(k.toUpperCase(),DateFormatUtil.parse(date).toLocalDate());
                        }else{
                            tm.put(k.toUpperCase(),DateFormatUtil.parse(date));
                        }
                    }else{
                        tm.put(k.toUpperCase(), m.get(k));
                    }
                } catch (Exception e) {
                    tm.put(k.toUpperCase(), m.get(k));
                }
                list.set(i, tm);
            }
			if (!customDialog.getNeedPage()) {
				pageList = new PageList<>(list);
				pageList.setPageSize(list.size());
				pageList.setPage(1);
			}
        }
        return pageList;
    }

	@Override
	public PageList getCustomDialogData(String alias, QueryFilter filter, String mapParam) throws Exception {
		CustomDialog customDialog = null;
		if (StringUtil.isNotEmpty(alias)){
			customDialog = getByAlias(alias);
		}
		if (customDialog == null) {
			return null;
		}
		Map<String, Object> param=new HashMap<String, Object>();
		if(StringUtil.isNotEmpty(mapParam)){
			mapParam="{"+mapParam+"}";
			Map map=JsonUtil.toMap(mapParam);
			for (Object key : map.keySet()) {
				param.put(key+"",map.get(key));
			}
		}

		List<QueryField> querys=filter.getQuerys();
		if(querys.size()>0){
			for (QueryField queryField : querys) {
				param.put(queryField.getProperty(), queryField.getValue());
			}
			if(querys.size()>1){
				param.put("relation", querys.get(1).getRelation().value());
			}
		}
		PageBean pageBean=filter.getPageBean();
		// 改变当前数据源
		PageList list = getListData(customDialog, param, pageBean);
		if(BeanUtils.isNotEmpty(list)) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (int i = 0; i <list.getRows().size() ; i++) {
				Map<String, Object> map = (HashMap) list.getRows().get(i);
				for (Map.Entry<String, Object> ent : map.entrySet()) {
					if (ent.getValue() instanceof Timestamp) {
						Timestamp times = (Timestamp) ent.getValue();
						ent.setValue(dateFormat.format(times));
					} else if (ent.getValue() instanceof java.util.Date) {
						ent.setValue(dateFormat.format(ent.getValue()));
					}
				}
			}
		}
		return list;
	}

	@Override
	public Map getMobileCustomDialogData(Boolean isCombine, String alias) throws Exception {
		Map mv=new HashMap();
		CustomDialog customDialog = null;
		CustomDialog treeDialog = null;

		if (isCombine) {
			CombinateDialog combine = combinateDialogManager.getByAlias(alias);
			treeDialog = combine.getTreeDialog();
			customDialog = combine.getListDialog();
			mv.put("combineField", combine.getField());
			mv.put("returnField", combine.getListDialog().getResultfield());
			mv.put("isSingle", combine.getListDialog().getSelectNum() == 1);
		} else {
			CustomDialog dialog = getByAlias(alias);
			Boolean isTree = dialog.getStyle() == 1;
			if (isTree) {
				treeDialog = dialog;
				//有疑问
				String disPlayName = JsonUtil.toJsonNode(dialog.getDisplayfield()).get("displayName")+"";
				mv.put("displayName",disPlayName.toUpperCase());
			} else {
				customDialog = dialog;
				mv.put("returnField", dialog.getResultfield());
				mv.put("isSingle", dialog.getSelectNum() == 1);
			}
		}
		// string to object
		if (customDialog != null) {
			JsonNode customDialogNode=JsonUtil.toJsonNode(customDialog);
			Map custDialog = JsonUtil.toMap(customDialogNode+"");
			custDialog.put("conditionfield",JsonUtil.toJsonNode(customDialog.getConditionfield()));
			custDialog.put("displayfield", JsonUtil.toJsonNode(customDialog.getDisplayfield()));
			mv.put("listDialog", JsonUtil.toJsonNode(custDialog));
		}
		if (treeDialog != null) {
			mv.put("treeDialog", treeDialog);
		}


		mv.put("isCombine", isCombine);
		return mv;
	}


	@Override
	public List geTreetData(CustomDialog customDialog, Map<String, Object> param, String dbType) throws IOException {
		String sql = getTreeSql(customDialog, param, dbType);
		List pageList = commonManager.query(sql);
		// 处理是否是节点的问题
		handleIsParent(pageList);
		return pageList;
	}
	
	// 遍历参数中传过来的 可选条件，并返回可选条件的 值和condition
	private Map<String,Object> getFromParam(Map<String, Object> param , String fieldName){
		try {
			Pattern regex = Pattern.compile("Q\\^(.*)\\^(.*)");
			String name = "";
			for (String k : param.keySet()) {
				Matcher regexMatcher = regex.matcher(k);
				if (regexMatcher.matches()) {
					name = regexMatcher.group(1);
					if(fieldName.equals(name)){
						Object object = param.get(k);
						if(BeanUtils.isEmpty(object)){
							return null;
						}
						Map<String, Object> returnMap = MapUtil.buildMap("value", object);
						returnMap.put("condition", regexMatcher.group(2));
						return returnMap;
					}
				}
			}
		} catch (PatternSyntaxException ex) {
			return null;
		}
		return null;
	}
 
	private SqlBuilderModel constructSqlBuilderModel(CustomDialog customDialog, Map<String, Object> param, String dbType) throws IOException {
		// 拼装条件字段
		ArrayNode conditionField = JsonUtil.getMapper().createArrayNode();
		ArrayNode conJA = (ArrayNode) JsonUtil.toJsonNode(customDialog.getConditionfield());
		for (int i = 0; i < conJA.size(); i++) {
			JsonNode jo = conJA.get(i);
			String fieldName = jo.get("field").asText();
			String defaultType = jo.get("defaultType").asText();
			String defaultValue = jo.get("defaultValue").asText();
			String fdbType = jo.get("dbType").asText();
			String condition = jo.get("condition").asText();
			Object value = null;
            if (defaultType.equals("1") || defaultType.equals("3")) {// 页面输入参数传入和用户输入
				value =MapUtil.getIgnoreCase(param,  fieldName);
			} else if (defaultType.equals("2")) {// 固定值
				value = defaultValue;
			//} else if (defaultType.equals("3")) {// 脚本
			//	value = groovyScriptEngine.executeObject(defaultValue, param);
			} else if(defaultType.equals("7")) {//可选条件
				Map<String, Object> fromParam = getFromParam(param, fieldName);
				if(BeanUtils.isEmpty(fromParam)) continue;
				condition = MapUtil.getString(fromParam, "condition");
				value = fromParam.get("value");
			}
			else{// 动态传入
				value = MapUtil.getIgnoreCase(param,fieldName);
			}

			if (BeanUtils.isEmpty(value))
				continue;

			Map jsonObject = new HashMap();
			jsonObject.put("field", fieldName);
			jsonObject.put("op", condition);
			jsonObject.put("dbType", fdbType);
			jsonObject.put("value", value);
            jsonObject.put("relation", param.get("relation"));
			conditionField.add(JsonUtil.toJsonNode(jsonObject));
		}

		// 拼装排序排序
		ArrayNode sortField = (ArrayNode) JsonUtil.toJsonNode(customDialog.getSortfield());
		ObjectNode sortFieldJO = JsonUtil.arrayToObject(sortField, "field");
		String sortFieldStr = MapUtil.getIgnoreCase(param, "sortField", "").toString();
		String orderSeq = MapUtil.getIgnoreCase(param, "orderSeq", "").toString();
		if (StringUtil.isNotEmpty(sortFieldStr) && StringUtil.isNotEmpty(orderSeq)) {
			ObjectNode jo = JsonUtil.getMapper().createObjectNode();
			jo.put("field", sortFieldStr);
			jo.put("sortType", orderSeq);
			sortFieldJO.put(sortFieldStr, jo);
		}

		SqlBuilderModel model = new SqlBuilderModel();
		model.setFromName(customDialog.getObjName());
		model.setDbType(dbType);
		model.setConditionField(conditionField);
		model.setSortField(JsonUtil.objectToArray(sortFieldJO));
		
		return model;
	}

	private void handleIsParent(List list) {
		for (Object obj : list) {
			Map<String, Object> map = (Map<String, Object>) obj;
			for (String key : map.keySet()) {
				if (!"isParent".equals(key) && "ISPARENT".equals(key.trim().toUpperCase())) {
					Object isParent = map.get(key);
					if (isParent != null) {
						if ("true".equals(isParent.toString())) {
							map.put("isParent", true);
						} else {
							map.put("isParent", false);
						}
						map.remove(key);
						break;
					}
				}
			}

		}
	}

	
	private String getTreeSql(CustomDialog customDialog, Map<String, Object> param, String dbType) throws IOException {
		String sql = "";
		
		SqlBuilderModel model = constructSqlBuilderModel(customDialog, param, dbType);

		JsonNode djo = JsonUtil.toJsonNode(customDialog.getDisplayfield());

		// 处理pid的值问题，其实就是把pid添加到sqlbuildermodel的条件构建中
		Map pidJson = new HashMap();
		String pid = getPid(customDialog, param);
		if (StringUtil.isNotEmpty(pid)) {
			pidJson.put("field", djo.get("pid"));
			pidJson.put("op", QueryOP.EQUAL.value());
			pidJson.put("dbType", "varchar");
			pidJson.put("value", pid);
		} else {
			pidJson.put("field", djo.get("pid"));
			pidJson.put("dbType", "varchar");
			pidJson.put("op", QueryOP.IS_NULL);
		}
		model.getConditionField().add(JsonUtil.toJsonNode(pidJson));

		sql = sqlBuilderService.getSql(model);

		sql = sql.replace(customDialog.getObjName(), customDialog.getObjName() + " o ");

		String isParentSql = ", ( case (select count(*)  from " + customDialog.getObjName() + " p where p." + djo.get("pid") + "=o." + djo.get("id") + " and p." + djo.get("id") + "!=p." + djo.get("pid") + ") when 0 then 'false' else 'true' end )isParent ";
		isParentSql=isParentSql.replaceAll("\"", "");
		String[] strs = sql.split("from");

		if (strs[0].contains("*")) {
			strs[0] = strs[0].replace("*", "");
		} else {
			strs[0] += ",";
		}

		String str0 = strs[0] + " * ";
		str0 = str0 + isParentSql;
		sql = str0 + " from " + strs[1];
		sql=sql.replaceAll("\"", "");
		return sql;
	}

	private String getPid(CustomDialog customDialog, Map<String, Object> param) throws IOException {
		String str=customDialog.getDisplayfield();
		
		ObjectNode jo = (ObjectNode) JsonUtil.toJsonNode(str);
		String id = jo.get("id")+"";
		String isScript = JsonUtil.getString(jo, "isScript", "false");
		String pvalue = JsonUtil.getString(jo, "pvalue", "");

		String pidVal = MapUtil.getIgnoreCase(param, id, "").toString();
		if (StringUtil.isNotEmpty(pidVal)) {
			if (isScript != null && isScript.equals("true")) {// 是脚本，开始解释这段脚本
				pidVal = groovyScriptEngine.executeString(pvalue, null).toString();
			} else {
				pidVal = pvalue;
			}
		}
		return pidVal;
	}
}
