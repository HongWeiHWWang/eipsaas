package com.hotent.activiti.ext.listener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.commons.lang.exception.ExceptionUtils;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.activiti.ext.factory.BpmDelegateFactory;
import com.hotent.base.exception.BaseException;
import com.hotent.base.exception.WorkFlowException;
import com.hotent.base.groovy.GroovyScriptEngine;
import com.hotent.base.model.HtJsonNodeFactory;
import com.hotent.base.model.HtObjectNode;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.ExceptionUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.constant.EventType;
import com.hotent.bpm.api.constant.ScriptType;
import com.hotent.bpm.api.context.BpmContextUtil;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.model.delegate.BpmDelegateExecution;
import com.hotent.bpm.api.model.delegate.BpmDelegateTask;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.plugin.core.cmd.ExecutionCommand;
import com.hotent.bpm.api.plugin.core.cmd.TaskCommand;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.api.service.BpmDefinitionService;
import com.hotent.bpm.exception.BusinessException;

/**
 * <pre> 
 * 描述：任务监听器
 * 构建组：x5-bpmx-activiti
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2014-2-14-上午10:42:09
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public abstract class AbstractTaskListener implements TaskListener{
	
	private static final long serialVersionUID = -296298349312307694L;
	
	@Resource
	BpmDefinitionService bpmDefinitionService;
	
	@Resource
	BpmDefinitionAccessor bpmDefinitionAccessor;
	
	@Resource
	GroovyScriptEngine groovyScriptEngine;
	
	
	
	private List<TaskCommand> taskCommands;
	
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
	 * @param delegateTask 
	 * void
	 * @throws Exception 
	 */
	public abstract void beforePluginExecute(BpmDelegateTask delegateTask) throws Exception;
	
	/**
	 * 触发子类的执行操作
	 * @param delegateTask 
	 * void
	 */
	public abstract void triggerExecute(BpmDelegateTask delegateTask);	
	
	/**
	 * 在所有插件执行之后执行的逻辑
	 * @param delegateTask 
	 * void
	 */
	public abstract void afterPluginExecute(BpmDelegateTask delegateTask);
	
	@Override
	public void notify(DelegateTask delegateTask) {
		//转换数据		
		BpmDelegateTask task= BpmDelegateFactory.getBpmDelegateTask(delegateTask);		
		//在插件之前执行逻辑
		try {
			beforePluginExecute(task);
		} catch (Exception e1) {
			throw new RuntimeException(e1);
		}
		//执行插件（在触发子类执行之前的插件，对应getBeforeTriggerEventType）
		if(taskCommands!=null && getBeforeTriggerEventType()!=null){
			for(TaskCommand cmd:taskCommands){
				try {
					cmd.execute(getBeforeTriggerEventType(),task);
				} catch (Exception e) {
					throw new BaseException(ExceptionUtil.getExceptionMessage(e));
				}
			}	
		}
		
		//执行全局节点插件（在触发子类执行之前的插件，对应getBeforeTriggerEventType）
		if(executionCommands!=null && getBeforeTriggerEventType()!=null){
			ActionCmd taskCmd = ContextThreadUtil.getActionCmd();
			taskCmd.getVariables().put("restful_task", task);
			for(ExecutionCommand cmd:executionCommands){
				BpmDelegateExecution  exection = BpmDelegateFactory.getBpmDelegateExecution(delegateTask.getExecution());
				if(BeanUtils.isNotEmpty(exection)){
					try {
						cmd.execute(getBeforeTriggerEventType(),BpmDelegateFactory.getBpmDelegateExecution(delegateTask.getExecution()));
					} catch (Exception e) {
						throw new BaseException(ExceptionUtil.getExceptionMessage(e));
					}
				}
			}	
		}
		//触发子类执行
		triggerExecute(task);	
		
		//执行插件（在触发子类执行之后的插件，对应getAfterTriggerEventType）
		if(taskCommands!=null && getAfterTriggerEventType()!=null){
			for(TaskCommand cmd:taskCommands){
				try {
					cmd.execute(getAfterTriggerEventType(),task);
				} catch (Exception e) {
					throw new BaseException(ExceptionUtil.getExceptionMessage(e));
				}
			}	
		}
		
		//在插件全部执行完之后执行逻辑
		afterPluginExecute(task);
		//执行事件脚本。
		try {
			exeEventScript(task);
		} catch (Exception e) {
			throw new BaseException(ExceptionUtil.getExceptionMessage(e));
		}
	}
	
	/**
	 * 脚本类型。
	 * @return  ScriptType
	 */
	protected abstract ScriptType getScriptType();
	
	private void exeEventScript(BpmDelegateTask delegateTask) throws Exception{
		String bpmnDefId=delegateTask.getBpmnDefId();
		String defId =bpmDefinitionService.getDefIdByBpmnDefId(bpmnDefId);
		String nodeId=delegateTask.getTaskDefinitionKey();
		BpmNodeDef nodeDef= bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);
		
		ScriptType scriptType= getScriptType();
		String script=nodeDef.getScripts().get(scriptType);
		if(StringUtil.isEmpty(script)) return;
		
	 
		Map<String, Object> vars=delegateTask.getVariables();
		ActionCmd cmd= ContextThreadUtil.getActionCmd();
		Map<String,ObjectNode> boMap= BpmContextUtil.getBoFromContext();
		if(BeanUtils.isNotEmpty(boMap)){
			Map<String, HtObjectNode> newMap =new HashMap<>();
			for (Iterator<Entry<String, ObjectNode>> iterator = boMap.entrySet().iterator(); iterator.hasNext();) {
				Entry<String, ObjectNode> next = iterator.next();
				newMap.put(next.getKey(),HtJsonNodeFactory.build().htObjectNode(next.getValue()));
			}
			vars.putAll(newMap); 
		}
		vars.put("nodeDef", nodeDef);
		vars.put("task", delegateTask);
		vars.put("cmd", cmd);
		try {
			groovyScriptEngine.execute(script, vars);
		} catch (BusinessException e) {
			throw new WorkFlowException(ExceptionUtils.getRootCauseMessage(e));
		} catch (Exception e) {
			StringBuffer sb = new StringBuffer();
			sb.append("<br/><br/>流程在节点："+nodeDef.getName()+"("+nodeDef.getNodeId()+")执行"+scriptType.getValue()+"时出现异常情况！");
			sb.append("<br/>请联系管理员！");
			sb.append("<br/>可能原因为："+e.getMessage());
			sb.append("<br/>执行脚本为："+script);
			sb.append("脚本变量："+vars.toString());
			throw new WorkFlowException(sb.toString());
		}
		
	}
	

	public List<TaskCommand> getTaskCommands() {
		return taskCommands;
	}

	public void setTaskCommands(List<TaskCommand> taskCommands) {
		this.taskCommands = taskCommands;
	}

	public List<ExecutionCommand> getExecutionCommands() {
		return executionCommands;
	}

	public void setExecutionCommands(List<ExecutionCommand> executionCommands) {
		this.executionCommands = executionCommands;
	}
	
}
