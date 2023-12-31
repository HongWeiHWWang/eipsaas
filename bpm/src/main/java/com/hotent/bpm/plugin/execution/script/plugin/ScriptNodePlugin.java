package com.hotent.bpm.plugin.execution.script.plugin;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.groovy.GroovyScriptEngine;
import com.hotent.base.util.BeanUtils;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.context.BpmContextUtil;
import com.hotent.bpm.api.model.delegate.BpmDelegateExecution;
import com.hotent.bpm.api.plugin.core.def.BpmExecutionPluginDef;
import com.hotent.bpm.api.plugin.core.session.BpmExecutionPluginSession;
import com.hotent.bpm.plugin.core.runtime.AbstractBpmExecutionPlugin;
import com.hotent.bpm.plugin.execution.script.def.ScriptNodePluginDef;

/**
 * 脚本节点插件运行时。
 * @author ray
 *
 */
public class ScriptNodePlugin extends AbstractBpmExecutionPlugin{
	
	@Resource
	GroovyScriptEngine groovyScriptEngine  ;

	public Void execute(BpmExecutionPluginSession pluginSession,
			BpmExecutionPluginDef pluginDef) {
		ScriptNodePluginDef nodeDef=(ScriptNodePluginDef)pluginDef;
		BpmDelegateExecution execution= pluginSession.getBpmDelegateExecution();
		
		Map<String, Object> vars=new HashMap<String, Object>();
		vars.put(BpmConstants.BPMN_EXECUTION_ID, execution.getId());
		vars.put(BpmConstants.BPMN_INST_ID, execution.getBpmnInstId());
		vars.putAll( execution.getVariables());
		vars.put("execution",execution);
		//从上下文获取bo实体数据。
		Map<String,ObjectNode> boDatas= BpmContextUtil.getBoFromContext();
		if(BeanUtils.isNotEmpty(boDatas)){
			vars.putAll(boDatas);
		}
		
		
		String script=nodeDef.getScript();
		groovyScriptEngine.execute(script, vars);
		return null;
	}

}
