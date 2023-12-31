package com.hotent.bpm.plugin.usercalc.script.runtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.groovy.GroovyScriptEngine;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.context.BpmContextUtil;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.session.BpmUserCalcPluginSession;
import com.hotent.bpm.plugin.core.runtime.AbstractUserCalcPlugin;
import com.hotent.bpm.plugin.usercalc.script.def.ScriptPluginDef;

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
public class ScriptPlugin extends AbstractUserCalcPlugin{
	
	@Resource
	GroovyScriptEngine groovyScriptEngine;

	@SuppressWarnings("unchecked")
	@Override
	public List<BpmIdentity> queryByPluginDef(BpmUserCalcPluginSession pluginSession, BpmPluginDef pluginDef) {
		ScriptPluginDef def=(ScriptPluginDef)pluginDef;
	 
		String script=def.getScript();
		if(StringUtil.isEmpty(script)){
			return Collections.EMPTY_LIST;
		}
		
		//获取流程变量
		Map<String, Object> variables= pluginSession.getVariables();
		
		
		Map<String,ObjectNode > boMap= BpmContextUtil.getBoFromContext();
		if(BeanUtils.isNotEmpty(boMap)){
			variables.putAll(boMap); 
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
