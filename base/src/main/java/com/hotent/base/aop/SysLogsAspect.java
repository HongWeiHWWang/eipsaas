package com.hotent.base.aop;

import java.lang.reflect.Method;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.constants.JmsConstant;
import com.hotent.base.constants.TenantConstant;
import com.hotent.base.jms.JmsProducer;
import com.hotent.base.util.AuthenticationUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.ExceptionUtil;
import com.hotent.base.util.HttpUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.ThreadMsgUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.base.util.WebUtil;

import io.swagger.annotations.ApiOperation;

/**
 * 系统日志切面
 *
 * @company 广州宏天软件股份有限公司
 * @author liyg
 * @email liygui@jee-soft.cn
 * @date 2018年8月31日
 */
@Aspect
@Component
public class SysLogsAspect {
	private Logger logger = LoggerFactory.getLogger(SysLogsAspect.class);
	@Resource
	JmsProducer jmsProducer;
	@Resource
	AopCacheHelper aopCacheHelper;
	
	private static String moduleType = "base" ;
	
	@Value("${spring.profiles.title:base}") 
	public void setModuleType(String param){
		moduleType = param;
	}
	
	@Around("execution(* *..*Controller.*(..)) && @annotation(io.swagger.annotations.ApiOperation)")
	public Object sysLogs(ProceedingJoinPoint joinPoint) throws Throwable{
		Class<?> targetClass = joinPoint.getTarget().getClass();
		String methodName = joinPoint.getSignature().getName();
		
		Method[] methods = targetClass.getMethods();
		// 当前切中的方法
		Method method = null;
		for (int i = 0; i < methods.length; i++){
			if (methods[i].getName() == methodName){
				method = methods[i];
				break;
			}
		}
		ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
		
		String opeName = apiOperation.value();
		if(StringUtil.isEmpty(opeName)){
			opeName = apiOperation.notes();
		}
		ThreadMsgUtil.addMapMsg("sysLogOpeName", opeName);
		// 执行方法前
		Object proceed = null;
		try {
			HttpServletRequest request = HttpUtil.getRequest();
			if(BeanUtils.isNotEmpty(request)) {
				request.setAttribute("enterController", true);
			}
			proceed = joinPoint.proceed();
			// 当前切中的方法
			
			if(BeanUtils.isEmpty(request)) {
				return proceed;
			}
			// 执行方法后
		    //访问目标方法的参数：
	        Object[] args = joinPoint.getArgs();
			String reqUrl = request.getRequestURI();
			
			try {
				Map<String, String> cacheSettings = aopCacheHelper.getSysLogsSettingStatusMap();
				if(BeanUtils.isEmpty(cacheSettings) || !cacheSettings.containsKey(moduleType)) {
					logger.error("未获取到日志配置中关于模块：{}的日志配置，跳过该模块的日志记录。", moduleType);
					return proceed;
				}
				
				StringBuffer sb = new StringBuffer();
				if(BeanUtils.isNotEmpty(args)){
					for (Object object : args) {
						if(object instanceof ServletRequest || object instanceof ServletResponse) {
							continue;
						}
						try {
							String json = JsonUtil.toJson(object);
							sb.append(json);
						} catch (Exception e) {
							sb.append(object.toString());
						}
					}
				}
				
				String executor = "系统[无用户登录系统]";
				if(StringUtil.isNotEmpty(AuthenticationUtil.getCurrentUserFullname())){
					executor = String.format("%s[%s]",AuthenticationUtil.getCurrentUserFullname(),AuthenticationUtil.getCurrentUsername());  
				}
				ObjectNode objectNode = JsonUtil.getMapper().createObjectNode();
				objectNode.put("id", UniqueIdUtil.getSuid());
				objectNode.put("opeName", opeName);
				objectNode.put("moduleType", moduleType);
				objectNode.put("reqUrl", reqUrl);
				objectNode.put("content", sb.toString());
				objectNode.put("type", "sysLog");
				String tenantId = HttpUtil.getTenantId();
				if(BeanUtils.isEmpty(tenantId)) {
					tenantId = TenantConstant.PLATFORM_TENANT_ID;
				}
				objectNode.put("tenantId", tenantId);
				// 是否开始配置日志
				if(BeanUtils.isNotEmpty(cacheSettings) &&
				   cacheSettings.containsKey(moduleType) &&
				   "1".equals(cacheSettings.get(moduleType))){
					if( "/sso/auth".matches(reqUrl) || "/auth".matches(reqUrl) ){
						executor = (String) request.getAttribute("loginUser");
						objectNode.put("logType", "登录日志");
					}else{
						objectNode.put("logType", "操作日志");
					}
					objectNode.put("executor", executor);
					objectNode.put("ip", WebUtil.getIpAddr(request));
					
					jmsProducer.sendToQueue(JsonUtil.toJson(objectNode),JmsConstant.SYS_LOG_QUEUE);
				}
			} catch (Exception e) {
				logger.error("保存操作日志失败。" + ExceptionUtil.getExceptionMessage(e));
			}
			
		} catch (Throwable e) {
			throw e;
		}
		return proceed;
	}
}
