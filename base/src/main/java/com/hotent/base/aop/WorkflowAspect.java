package com.hotent.base.aop;

import java.lang.reflect.Method;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.Workflow;
import com.hotent.base.entity.BaseModel;
import com.hotent.base.exception.BaseException;
import com.hotent.base.exception.WorkFlowException;
import com.hotent.base.feign.BpmRuntimeFeignService;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;

/**
 * 工作流切面处理
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年8月17日
 */
@Aspect
@Component
public class WorkflowAspect {
	@Resource
	BpmRuntimeFeignService bpmRuntimeFeignService;
	
	@SuppressWarnings({ "rawtypes" })
	@Around("execution(* *..*Controller.*(..)) && @annotation(com.hotent.base.annotation.Workflow)")
	public Object workflow(ProceedingJoinPoint joinPoint) throws Throwable{
		Object	returnVal=null;
		Class<?> targetClass = joinPoint.getTarget().getClass();
		String methodName = joinPoint.getSignature().getName();
		Object[] params=joinPoint.getArgs();
		Method[] methods = targetClass.getMethods();
		// 当前切中的方法
		Method method = null;
		// 当前实体类参数
		BaseModel param = null;
		
		for (int i = 0; i < params.length; i++){
			// 获取入参中第一个继承自 BaseModel的参数
			if (params[i] instanceof BaseModel){
				param = (BaseModel)params[i];
				break;
			}
		}
		
		if(BeanUtils.isEmpty(param)) {
			returnVal = joinPoint.proceed();
			return returnVal;
		}
		
		for (int i = 0; i < methods.length; i++){
			if (methods[i].getName() == methodName){
				method = methods[i];
				break;
			}
		}
		Workflow workflow = method.getAnnotation(Workflow.class);
		// 流程定义
		String flowKey = workflow.flowKey();
		// 业务系统编码
		String sysCode = workflow.sysCode();
		// 实例ID回填到实体类的哪个字段
		String instanceIdField = workflow.instanceIdField();
		// 变量keys集合
		String[] varKeys = workflow.varKeys();
		if(StringUtil.isEmpty(flowKey)) {
			return joinPoint.proceed();
		}
		// 构建启动流程的参数
		ObjectNode startFlowParam = JsonUtil.getMapper().createObjectNode();
		startFlowParam.put("flowKey", flowKey);
		String businessKey = param.getPkVal();
		if(StringUtil.isEmpty(businessKey)) {
			// 没有主键时抛出错误
			throw new WorkFlowException("启动流程时，实体对象中的id不能为空");
		}
		startFlowParam.put("businessKey", businessKey);
		startFlowParam.put("formType", "frame");
		if(StringUtil.isNotEmpty(sysCode)) {
			startFlowParam.put("sysCode", sysCode);
		}
		// 构建vars流程变量
		if(BeanUtils.isNotEmpty(varKeys) && varKeys.length > 0) {
			ObjectNode varsObject = JsonUtil.getMapper().createObjectNode();
			JsonNode paramJsonNode = JsonUtil.toJsonNode(param);
			for(String key : varKeys) {
				JsonNode jsonNode = paramJsonNode.get(key);
				varsObject.set(key, jsonNode);
			}
			startFlowParam.set("vars", varsObject);
		}
		
		// 调用接口启动流程
		ObjectNode startFlowResult = bpmRuntimeFeignService.start(startFlowParam);
		
		if(BeanUtils.isNotEmpty(startFlowResult) && startFlowResult.get("state").asBoolean()) {
			String instanceId = startFlowResult.get("instId").asText();
			// 将流程实例ID回填到实体对象中
			BeanUtils.setProperty(param, instanceIdField, instanceId);
			returnVal = joinPoint.proceed();
		}
		else {
			String message = "流程启动失败";
			if(BeanUtils.isNotEmpty(startFlowResult) && BeanUtils.isNotEmpty(startFlowResult.get("message"))) {
				message += ":" + startFlowResult.get("message").asText();
			}
			throw new BaseException(message);
		}
		return returnVal;
	}
}
