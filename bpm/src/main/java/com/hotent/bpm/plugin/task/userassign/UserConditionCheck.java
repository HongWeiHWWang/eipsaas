package com.hotent.bpm.plugin.task.userassign;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.groovy.GroovyScriptEngine;
import com.hotent.base.model.HtJsonNodeFactory;
import com.hotent.base.model.HtObjectNode;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.time.TimeUtil;
import com.hotent.bpm.api.context.BpmContextUtil;
import com.hotent.bpm.api.helper.identity.IConditionCheck;
import com.hotent.bpm.api.plugin.core.session.BpmUserCalcPluginSession;
import com.hotent.bpm.exception.UserCalcException;
import com.hotent.bpm.plugin.usercalc.UserCalcHelper;
import com.hotent.bpm.plugin.usercalc.cuserrel.def.ExecutorVar;


/**
 * <pre> 
 * 描述：人员条件规则设置计算
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-7-8-下午2:47:50
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */

@Service
public class UserConditionCheck implements IConditionCheck {
	@Resource
	GroovyScriptEngine groovyScriptEngine;

	@Override
	public boolean check(String condition, String mode,BpmUserCalcPluginSession session) {
		try { 
			if(StringUtil.isEmpty(condition))return true;
			
			ObjectNode conditionObj =  (ObjectNode) JsonUtil.toJsonNode(condition);
			if (!conditionObj.hasNonNull("condition")) {
				return true;
			}
			
			return groovyScriptEngine.executeBoolean(calConditions((ArrayNode)conditionObj.get("condition"),session,conditionObj.get("junction").asText()), null);
		
		} catch (Exception e) {
			e.printStackTrace();
			throw new UserCalcException("人员条件表达式解析异常！" +e.getMessage());
		}
	}
	/***
	 *  计算每条规则的Boolean 值 ,返回类似：  (true&&false||true)  的字符串
	 *  让 脚本根据算术优先级进行运算返回结果。
	 * @param conditionList
	 * @param session
	 * @return
	 * @throws Exception
	 */
	private String calConditions(ArrayNode conditionList,BpmUserCalcPluginSession session,String compareType) throws Exception{
		if(conditionList.size()==0) return "true";
		
		StringBuffer sb = new StringBuffer();
		sb.append("(");
		for(int i =0; i< conditionList.size();i++){
			ObjectNode conditionParam = (ObjectNode) conditionList.get(i);
			
			if(i!= 0) 
				sb.append(getCompType(compareType));//运算类型
			
			//如果含子项
			if(conditionParam.findValue("condition") != null){
				ArrayNode subConditions = (ArrayNode) conditionParam.get("condition");
				sb.append(calConditions(subConditions,session,conditionParam.get("junction").asText()));
			}//如果单一表达式
			else{
				if(calculate(conditionParam,session))  sb.append("true");
				else sb.append("false");
			}
		}
		sb.append(")");
		
		return sb.toString();
	}
	
	private String getCompType(String compareType) {
		
		if(compareType.equals("and")) {
			return "&&";
		}
		else {
			return "||";  
		}   
		
	}
	/*计算单一条件 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean calculate(ObjectNode condition, BpmUserCalcPluginSession session) throws Exception{
		int ruleType =condition.get("ruleType").asInt();
		// 脚本类型 的计算
		if(ruleType == 2){
			String script = condition.get("script").asText();
			Map variables = session.getVariables();
			
			Map<String,ObjectNode> boMap= BpmContextUtil.getBoFromContext();
			
			if(BeanUtils.isNotEmpty(boMap)){
				Map<String, HtObjectNode> newMap =new HashMap<>();
				for (Iterator<Entry<String, ObjectNode>> iterator = boMap.entrySet().iterator(); iterator.hasNext();) {
					Entry<String, ObjectNode> next = iterator.next();
					newMap.put(next.getKey(),HtJsonNodeFactory.build().htObjectNode(next.getValue()));
				}
				variables.putAll(newMap); 
			}
			
			return groovyScriptEngine.executeBoolean(script,variables);
		}
		if(BeanUtils.isEmpty(condition.get("expression"))){
		    return true;
        }
		String expression = condition.get("expression").asText();

		// 条件类型的 计算 
		ObjectNode  executorVarJson = (ObjectNode) condition.get("executorVar");
		ExecutorVar executorVar = JsonUtil.toBean(executorVarJson, ExecutorVar.class);
		
		String executorVarValue = executorVar.getValue();
		List<String> keys = UserCalcHelper.calcVarValue(executorVar, session,false);
		
		//流程变量，Bo
		String variable = StringUtil.join(keys.toArray(new String[]{})).trim();
		
		if("notEquals".equals(expression)) 
			return !executorVarValue.equals(variable);
		
		if("equals".equals(expression)) 
			return executorVarValue.equals(variable);
		
		if("contains".equals(expression)) 
			return executorVarValue.contains(variable);
		
		if("notContains".equals(expression)) 
			return !executorVarValue.contains(variable);
		
		
		String dataType = condition.get("dataType").asText();
		boolean isDate = "date".equals(dataType);
	
		//数字，日期的       > <  >= <=大小判断
		{
			if(isDate){
				LocalDateTime date = TimeUtil.getDateTimeByTimeString(executorVarValue);
				LocalDate valueData = TimeUtil.getDateTimeByTimeString(variable).toLocalDate();
				int diff =  TimeUtil.getSecondDiff(valueData.atStartOfDay(),date);
				
				if(">".equals(expression)){
					return diff > 0;
				}else if("<".equals(expression)){
					return diff < 0;
				}else if("<=".equals(expression)){
					return diff <= 0;
				}
				else if(">=".equals(expression)){
					return diff >= 0;
				}
			}
			
			// 如果不是日期就为数字类型，直接 拼装条件表达式，让脚本执行
			return groovyScriptEngine.executeBoolean(executorVarValue+expression+variable,session.getVariables());
		}
		
	}
	
}


