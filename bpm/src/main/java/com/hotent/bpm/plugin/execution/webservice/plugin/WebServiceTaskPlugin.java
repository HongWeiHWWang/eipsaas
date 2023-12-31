package com.hotent.bpm.plugin.execution.webservice.plugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.exception.WorkFlowException;
import com.hotent.base.groovy.GroovyScriptEngine;
import com.hotent.base.service.InvokeResult;
import com.hotent.base.service.ServiceClient;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.context.BpmContextUtil;
import com.hotent.bpm.api.plugin.core.def.BpmExecutionPluginDef;
import com.hotent.bpm.api.plugin.core.session.BpmExecutionPluginSession;
import com.hotent.bpm.plugin.core.runtime.AbstractBpmExecutionPlugin;
import com.hotent.bpm.plugin.execution.webservice.def.WebServiceNodePluginDef;

public class WebServiceTaskPlugin  extends AbstractBpmExecutionPlugin{
	@Resource
	GroovyScriptEngine groovyScriptEngine;
	
	private ServiceClient serviceClient;
	
	@Override
	public Void execute(BpmExecutionPluginSession pluginSession, BpmExecutionPluginDef pluginDef) throws IOException {
		WebServiceNodePluginDef webServiceDef = (WebServiceNodePluginDef) pluginDef;
		//脚本需要的变量
		Map<String, Object> variables = pluginSession.getBpmDelegateExecution().getVariables();
		Map<String,ObjectNode> boMap= BpmContextUtil.getBoFromContext();
		if(BeanUtils.isNotEmpty(boMap)){
			variables.putAll(boMap); 
		}
		variables.putAll(getConfParam(webServiceDef, variables));
		if(serviceClient==null){
			serviceClient = AppUtil.getBean(ServiceClient.class);
		}
		InvokeResult invokeResult = serviceClient.invoke(webServiceDef.getAlias(), variables);
		handleResult(invokeResult,pluginSession,webServiceDef,variables);
		return null;
	}
	
	/***
	 * 拼装请求参数
	 * @return
	 * @throws IOException 
	 */
	private Map<String,Object> getConfParam(WebServiceNodePluginDef webServiceDef,Map<String, Object> variables) throws IOException {
		Map<String,Object> p = new HashMap<String, Object>();

		String inputStr =  webServiceDef.getParams(); 
		ArrayNode params = (ArrayNode) JsonUtil.toJsonNode(inputStr);
		if(BeanUtils.isEmpty(params))return p;
		try {
			for (int i = 0; i < params.size(); i++) {
				ObjectNode param = (ObjectNode) JsonUtil.toJsonNode(params.get(i));
				String bindType = param.get("bindType").asText();
				String bindValue = param.get("bindValue").asText();
				String key = param.get("name").asText();
				Object value ;
				
				if("var".equals(bindType)){
					value = variables.get(bindValue);
				}else if("bo".equals(bindType)){
					String [] boStr = bindValue.split("\\.");
					ObjectNode bo =(ObjectNode) variables.get(boStr[0]);
					value = bo.get(boStr[1]);
				}else{
					value = groovyScriptEngine.executeObject(bindValue, variables);
				}
			  p.put(key, value);
			}
		} catch (Exception e) {
			throw new RuntimeException("webService 节点解析 输入参数异常！    详细信息："+inputStr,e);
		}
		return p;
	}
	/**
	 * 处理结果
	 * @param invokeResult
	 * @param pluginSession
	 * @param webServiceDef
	 * @param variables 
	 * @throws IOException 
	 */
	private void handleResult(InvokeResult invokeResult, BpmExecutionPluginSession pluginSession,WebServiceNodePluginDef webServiceDef, Map<String, Object> variables) throws IOException {
		if(invokeResult.isFault()){
			throw new WorkFlowException("webService 调用异常！");
		}
		String script = webServiceDef.getOutPutScript();
		if(StringUtil.isEmpty(script)) return;
		String jsonStr = invokeResult.getJson();
		
		variables.put("invokeResult", invokeResult);
		variables.put("pluginSession", pluginSession);
		if(!invokeResult.isVoid() && StringUtil.isNotEmpty(jsonStr))
			variables.put("data", JsonUtil.toJsonNode(jsonStr));
		
		groovyScriptEngine.execute(script,variables);
	}
}