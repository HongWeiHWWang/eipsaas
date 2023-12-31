package com.hotent.activiti.ext.listener;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.activiti.ext.factory.BpmDelegateFactory;
import com.hotent.base.groovy.GroovyScriptEngine;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.constant.EventType;
import com.hotent.bpm.api.constant.ScriptType;
import com.hotent.bpm.api.context.BpmContextUtil;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.model.delegate.BpmDelegateExecution;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.plugin.core.cmd.ExecutionCommand;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.api.service.BpmDefinitionService;
/**
 * <pre> 
 * 描述：抽象事件监听处理器
 * 构建组：x5-bpmx-activiti
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2014-2-14-上午10:37:10
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public abstract class AbstractExecutionListener implements ExecutionListener{
	
	private static final long serialVersionUID = 5610767582073352010L;
	
	@Resource
	private BpmDefinitionService bpmDefinitionService;
	
	@Resource
	private BpmDefinitionAccessor bpmDefinitionAccessor;
	
	@Resource
	private GroovyScriptEngine groovyScriptEngine;

	private List<ExecutionCommand> executionCommands;
	
	/**
	 * 触发子类执行动作之前，需要执行的插件事件类型。
	 * @return 
	 * EventType
	 */
	public abstract EventType getBeforeTriggerEventType();
	
	/**
	 * 触发子类执行动作之后，需要执行的插件事件类型。
	 * @return 
	 * EventType
	 */
	public abstract EventType getAfterTriggerEventType();
	
	/**
	 * 在所有插件执行之前执行的逻辑
	 * @param bpmDelegateExecution 
	 * void
	 */
	public abstract void beforePluginExecute(BpmDelegateExecution bpmDelegateExecution);
	
	/**
	 * 触发子类的执行操作
	 * @param bpmDelegateExecution 
	 * void
	 */
	public abstract void triggerExecute(BpmDelegateExecution bpmDelegateExecution);		
	
	/**
	 * 在所有插件执行之后执行的逻辑
	 * @param delegateTask 
	 * void
	 * @throws Exception 
	 */	
	public abstract void afterPluginExecute(BpmDelegateExecution bpmDelegateExecution) throws Exception;
	
	@Override
	public void notify(DelegateExecution delegateExecution) throws Exception {
		//转换数据
		BpmDelegateExecution bpmDelegateExecution = BpmDelegateFactory
				.getBpmDelegateExecution(delegateExecution);
		//在插件之前执行逻辑
		beforePluginExecute(bpmDelegateExecution);
		//执行插件（在触发子类执行之前的插件，对应getBeforeTriggerEventType）
		if(executionCommands!=null && getBeforeTriggerEventType()!=null){
			for(ExecutionCommand cmd:executionCommands){
				cmd.execute(getBeforeTriggerEventType(), bpmDelegateExecution);
			}
		}
		//触发子类执行
		triggerExecute(bpmDelegateExecution);
		//执行插件（在触发子类执行之后的插件，对应getAfterTriggerEventType）
		if(executionCommands!=null && getAfterTriggerEventType()!=null){
			for(ExecutionCommand cmd:executionCommands){
				cmd.execute(getAfterTriggerEventType(), bpmDelegateExecution);
			}
		}
		//在插件全部执行完之后执行逻辑
		afterPluginExecute(bpmDelegateExecution);
		//执行脚本
		exeEventScript(bpmDelegateExecution);
	
	}

	/**
	 * 取得事件类型。
	 * @return ScriptType
	 */
	protected abstract ScriptType getScriptType();
	
	/**
	 * 执行事件脚本。
	 * @param bpmDelegateExecution 
	 * void
	 * @throws Exception 
	 */
	private void exeEventScript(BpmDelegateExecution bpmDelegateExecution) throws Exception{
		String bpmnDefId=bpmDelegateExecution.getBpmnDefId();
		String defId =bpmDefinitionService.getDefIdByBpmnDefId(bpmnDefId);
		String nodeId=bpmDelegateExecution.getNodeId();
		if(StringUtil.isEmpty(nodeId)){
			return;
		}
		BpmNodeDef nodeDef= bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);
		
		ScriptType scriptType= getScriptType();
		String script=nodeDef.getScripts().get(scriptType);
		if(StringUtil.isEmpty(script)) return;
	 
		Map<String, Object> vars=bpmDelegateExecution.getVariables();
		//流程实例ID
		
		ActionCmd cmd= ContextThreadUtil.getActionCmd();
		//BO上下文
		Map<String,ObjectNode> boMap= BpmContextUtil.getBoFromContext();
		if(BeanUtils.isNotEmpty(boMap)){
			vars.putAll(boMap); 
		}

		vars.put("nodeDef", nodeDef);
		vars.put("execution", bpmDelegateExecution);
		vars.put("cmd", cmd);
		groovyScriptEngine.execute(script, vars);
	}

	public void setExecutionCommands(List<ExecutionCommand> executionCommands) {
		this.executionCommands = executionCommands;
	}
	
}
