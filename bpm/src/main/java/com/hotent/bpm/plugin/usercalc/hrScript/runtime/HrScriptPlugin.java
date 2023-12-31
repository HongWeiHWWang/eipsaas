package com.hotent.bpm.plugin.usercalc.hrScript.runtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.groovy.GroovyScriptEngine;
import com.hotent.base.model.HtJsonNodeFactory;
import com.hotent.base.model.HtObjectNode;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.context.BpmContextUtil;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.session.BpmUserCalcPluginSession;
import com.hotent.bpm.api.service.BoDataService;
import com.hotent.bpm.engine.task.cmd.DefaultTaskFinishCmd;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;
import com.hotent.bpm.plugin.core.runtime.AbstractUserCalcPlugin;
import com.hotent.bpm.plugin.usercalc.hrScript.def.HrScriptPluginDef;

/**
 * 人员脚本获取插件。
 * <pre> 
 * 构建组：x5-bpmx-plugin
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-6-16-下午3:06:18
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class HrScriptPlugin extends AbstractUserCalcPlugin{
	@Resource
	GroovyScriptEngine groovyScriptEngine;
	@Resource
	BoDataService boDataService;
	@Resource
	BpmProcessInstanceManager bpmProcessInstanceManager;
	@SuppressWarnings("unchecked")
	@Override
	public List<BpmIdentity> queryByPluginDef(BpmUserCalcPluginSession pluginSession, BpmPluginDef pluginDef) throws Exception {
		HrScriptPluginDef def=(HrScriptPluginDef)pluginDef;
	 
		String script=def.getScript();
		if(StringUtil.isEmpty(script)){
			return Collections.EMPTY_LIST;
		}
		//获取流程变量
		Map<String, Object> variables= pluginSession.getVariables();
		Map<String,ObjectNode > boMap= BpmContextUtil.getBoFromContext();
		
		ActionCmd actionCmd = ContextThreadUtil.getActionCmd();
		if(BeanUtils.isEmpty(actionCmd) && variables.containsKey("instanceId_") ){
			String instanceId = (String) variables.get("instanceId_");
			DefaultBpmProcessInstance bpmProcessInstance = bpmProcessInstanceManager.get(instanceId);
			List<ObjectNode> boDatas = boDataService.getDataByInst(bpmProcessInstance);
			boMap=new HashMap<String, ObjectNode>();
			
			for(ObjectNode data:boDatas){
				String code="";
				if(data.hasNonNull("boDefAlias")){
					code = data.get("boDefAlias").asText();
				}else  {
					code=data.get("boDef").get("alias").asText();
				}
				boMap.put(code, data);
			}
			
			DefaultTaskFinishCmd taskCmd = new DefaultTaskFinishCmd();
			taskCmd.setVariables(variables);
			
			
			ContextThreadUtil.setActionCmd(taskCmd);
			
		} 
		
		if(BeanUtils.isNotEmpty(boMap)){
			Map<String, HtObjectNode> newMap =new HashMap<>();
			for (Iterator<Entry<String, ObjectNode>> iterator = boMap.entrySet().iterator(); iterator.hasNext();) {
				Entry<String, ObjectNode> next = iterator.next();
				newMap.put(next.getKey(),HtJsonNodeFactory.build().htObjectNode(next.getValue()));
			}
			variables.putAll(newMap); 
		}
		 
		Set<BpmIdentity> set=(Set<BpmIdentity>) groovyScriptEngine.executeObject(script,variables);
		List<BpmIdentity> list=new ArrayList<BpmIdentity>();
		
		list.addAll(set);
		return list;
	}

	@Override
	public boolean supportPreView() {
		return false;
	}

}
