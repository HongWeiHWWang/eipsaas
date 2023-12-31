package org.activiti.engine.impl.el;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.Condition;
import org.apache.commons.lang.exception.ExceptionUtils;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.exception.WorkFlowException;
import com.hotent.base.groovy.GroovyScriptEngine;
import com.hotent.base.model.HtJsonNodeFactory;
import com.hotent.base.model.HtObjectNode;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.cmd.ProcessInstCmd;
import com.hotent.bpm.api.cmd.TaskFinishCmd;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.context.BpmContextUtil;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.plugin.core.execution.sign.SignResult;
import com.hotent.bpm.engine.task.cmd.DefaultTaskFinishCmd;
import com.hotent.bpm.exception.BusinessException;
/**
 * GroovyEngine条件。
 * 
 * <pre>
 *用于分支网关和条件网关。
 * 1.在表达式中如何使用BO.
 * 		例如 有一个订单bo，叫order。
 * 		使用方法如下return order.getInt("amount")>1;
 * 2.如果这个流程属于一个子流程，那么这个时候如果有一个条件分支我们如何处理呢？
 *  	可以判断当前的bo实例是否包含某个bo。
 *  	例如：
 *  	if(boMap.containsKey("order")){
 *  
 *  	}
 * 
 * 构建组：x5-bpmx-activiti
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-10-22-下午10:39:41
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class GroovyCondition implements Condition {
	/**
	 * serialVersionUID
	 * @since 1.0.0
	 */
	private static final long serialVersionUID =  -5577703954744892854L;

	private String script="";
	
	public GroovyCondition(String condition){
		this.script=condition;
	} 
	

	public boolean evaluate(DelegateExecution execution) {
		Map<String,Object> maps=execution.getVariables();
		//添加execution。
		maps.put(VariableScopeElResolver.EXECUTION_KEY, execution);
		
		ActionCmd cmd=ContextThreadUtil.getActionCmd();
		if(cmd instanceof TaskFinishCmd){
			TaskFinishCmd taskCmd=(TaskFinishCmd)cmd;
			//如果cmd包含会签结果的变量。则表明这个是一个已经完成了的会签任务。则用会签节点的投票结果，作为该节点的actionName
			SignResult signResult = (SignResult) cmd.getTransitVars(BpmConstants.TASK_SIGN_RESULT);
			if (BeanUtils.isEmpty(signResult) || taskCmd.getActionName().equals(signResult.getNodeStatus().getKey())) {
				maps.put("taskCmd", taskCmd);
			}else {
				try {
					DefaultTaskFinishCmd defaultTaskFinishCmd = (DefaultTaskFinishCmd) taskCmd;
					DefaultTaskFinishCmd clone = defaultTaskFinishCmd.clone();
					clone.setActionName(signResult.getNodeStatus().getKey());
					maps.put("taskCmd", clone);
				} catch (Exception e) {
					e.printStackTrace();
					maps.put("taskCmd", taskCmd);
				}
			}
		}else if (cmd instanceof ProcessInstCmd ) {
			maps.put("taskCmd", cmd);
		}
		Map<String,ObjectNode> boMap= BpmContextUtil.getBoFromContext();
		
		if(BeanUtils.isNotEmpty(boMap)){
			if(BeanUtils.isNotEmpty(boMap)){
				Map<String, HtObjectNode> newMap =new HashMap<>();
				for (Iterator<Entry<String, ObjectNode>> iterator = boMap.entrySet().iterator(); iterator.hasNext();) {
					Entry<String, ObjectNode> next = iterator.next();
					ObjectNode obj= next.getValue();
					if (obj.hasNonNull("data") && (obj.get("data") instanceof ObjectNode)) {
						obj = (ObjectNode) obj.get("data");
					}
					HtObjectNode htObjectNode = HtJsonNodeFactory.build().htObjectNode(obj);
					newMap.put(next.getKey(),htObjectNode);
					maps.put(next.getKey(),htObjectNode);
				}
				maps.putAll(newMap); 
			}
		}
		
		maps.put("boMap", boMap);
		
		GroovyScriptEngine engine=AppUtil.getBean(GroovyScriptEngine.class);
		String newScript = replaceSpecialChar(script);
		try {
			return engine.executeBoolean(newScript, maps);
		}catch(BusinessException e){
			throw new WorkFlowException(ExceptionUtils.getRootCauseMessage(e));
		}catch (Exception e) { 
			StringBuffer message = new  StringBuffer("条件脚本解析异常！请联系管理员。");
			message.append("<br/><br/>节点："+execution.getCurrentActivityName()+"——"+execution.getCurrentActivityId());
			message.append("<br/><br/>脚本："+script);
			message.append("<br/><br/>流程变量："+maps.toString()); 
			throw new WorkFlowException(message.toString());
		}
	}
	
	private String replaceSpecialChar(String str){
		if(StringUtil.isEmpty(str)) return ""; 
		str = str.trim();
		if(str.startsWith("${")) return  str.substring(2, str.length()-1);
		return str;
	}


	@Override
	public boolean evaluate(String arg0, DelegateExecution arg1) {
		return evaluate(arg1);
	}
}
