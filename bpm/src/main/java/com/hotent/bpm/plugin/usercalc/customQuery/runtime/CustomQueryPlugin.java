package com.hotent.bpm.plugin.usercalc.customQuery.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.feign.FormFeignService;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.groovy.GroovyScriptEngine;
import com.hotent.base.model.HtJsonNodeFactory;
import com.hotent.base.query.PageList;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.context.BpmContextUtil;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.session.BpmUserCalcPluginSession;
import com.hotent.bpm.model.identity.DefaultBpmIdentity;
import com.hotent.bpm.plugin.core.runtime.AbstractUserCalcPlugin;
import com.hotent.bpm.plugin.usercalc.customQuery.def.CustomQueryPluginDef;

/**
 * 关联数据作为流程审批人的插件
 *
 * @company 广州宏天软件股份有限公司
 * @author zhangxw
 * @email zhangxw@jee-soft.cn
 * @date 2020年3月25日
 */
public class CustomQueryPlugin extends AbstractUserCalcPlugin{

	@Resource
	FormFeignService formFeignService;
	@Resource
	UCFeignService ucFeignService;
	
	@Override
	public List<BpmIdentity> queryByPluginDef(BpmUserCalcPluginSession pluginSession, BpmPluginDef pluginDef) {
		List<BpmIdentity> identityList=new ArrayList<BpmIdentity>();
		CustomQueryPluginDef def = (CustomQueryPluginDef)pluginDef;
		String alias = def.getAlias();
		String valueField = def.getValueField();
		String params = def.getParams();
		if(StringUtil.isEmpty(alias) || StringUtil.isEmpty(valueField)) return identityList;
		try {
			//针对json结构有变的情况下的特殊处理
			if(params.startsWith("\"")){
				params = params.replace("\"[", "[");
				params = params.replace("]\"", "]");
				params = params.replace("\\\"", "\"");
			}
			//针对参数值中带有运算符的特殊处理
			if(params.contains("+\\")){
				params = params.replace("\\\"", "\"");
			}
			String queryData = null;
			if(StringUtil.isNotEmpty(params)){
				GroovyScriptEngine groovyScriptEngine=new GroovyScriptEngine();
				Map<String,Object> varMap = new HashMap<String,Object>();
				ActionCmd cmd = ContextThreadUtil.getActionCmd();
				if(BeanUtils.isNotEmpty(cmd)){
					varMap.putAll(cmd.getVariables());
				}
				Map<String,ObjectNode> boMap=BpmContextUtil.getBoFromContext();
				if(BeanUtils.isNotEmpty(boMap)){
					for (Iterator<Entry<String, ObjectNode>> iterator = boMap.entrySet().iterator(); iterator.hasNext();) {
						Entry<String, ObjectNode> ent = iterator.next();
						varMap.put(ent.getKey(), HtJsonNodeFactory.build().htObjectNode(ent.getValue()));
					}
				}else if(BeanUtils.isNotEmpty(cmd) && BeanUtils.isNotEmpty(cmd.getBusData())){
					try {
						JsonNode dataNode = JsonUtil.toJsonNode(cmd.getBusData());
						Iterator<Entry<String, JsonNode>> it = dataNode.fields();
						while (it.hasNext())
			            {
			                Entry<String, JsonNode> entry = it.next();
			                varMap.put(entry.getKey(), HtJsonNodeFactory.build().htObjectNode((ObjectNode) entry.getValue()));
			            }
					} catch (Exception e) {
						System.out.println("解析流程数据失败："+e.getMessage());
					}
				}
				ArrayNode array = JsonUtil.getMapper().createArrayNode();
				ArrayNode paramArray = (ArrayNode) JsonUtil.toJsonNode(params);
				for (JsonNode jsonNode : paramArray) {
					if(BeanUtils.isNotEmpty(jsonNode.get("value")) && BeanUtils.isNotEmpty(jsonNode.get("field")) && StringUtil.isNotEmpty(jsonNode.get("value").asText())){
						ObjectNode node = JsonUtil.getMapper().createObjectNode();
						node.put("key", jsonNode.get("field").asText());
						String value = jsonNode.get("value").asText();
						if("0".equals(jsonNode.get("valueType").asText())){
							value = groovyScriptEngine.executeString(value, varMap);
						}
						node.put("value", value);
						array.add(node);
					}
				}
				if(BeanUtils.isNotEmpty(array) && array.size()>0){
					queryData = JsonUtil.toJson(array);
				}
			}
			PageList page = formFeignService.doQuery(alias, 1, queryData);
			if(BeanUtils.isNotEmpty(page) && BeanUtils.isNotEmpty(page.getRows())){
				List results = page.getRows();
				String accounts = "";
				for (Object object : results) {
					JsonNode node = JsonUtil.toJsonNode(object);
					if(BeanUtils.isNotEmpty(node.get(valueField)) && StringUtil.isNotEmpty(node.get(valueField).asText())){
						if(StringUtil.isNotEmpty(accounts)){
							accounts += ",";
						}
						accounts += node.get(valueField).asText();
					}
				}
				if(StringUtil.isNotEmpty(accounts)){
					ArrayNode users = ucFeignService.getUserByAccounts(accounts);
					if(BeanUtils.isNotEmpty(users)){
						for (JsonNode userNode : users) {
							BpmIdentity identity = new DefaultBpmIdentity(userNode.get("id").asText(), userNode.get("fullname").asText(), BpmIdentity.TYPE_USER);
							identityList.add(identity);
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("关联查询获取审批人失败："+e.getMessage());
		}
		
		return identityList;
	}
}
