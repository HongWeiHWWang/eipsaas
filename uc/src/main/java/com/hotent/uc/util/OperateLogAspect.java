package com.hotent.uc.util;

import java.io.IOException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.uc.model.OperateLog;

import io.swagger.annotations.ApiOperation;


@Aspect
@Component
public class OperateLogAspect {
   
	private static  List<String> ignoreMethodArr = new  ArrayList<String>();
	static{
		ignoreMethodArr.add("showADButton");
		ignoreMethodArr.add("importZipUser");
		ignoreMethodArr.add("importExcelUser");
		ignoreMethodArr.add("syncADUsers");//AD同步
		ignoreMethodArr.add("oaAsync");//OA同步
		ignoreMethodArr.add("setTrigger");//修改定时计划
	}

	@Around("execution(* com.hotent.uc.**.controller.*Controller.*(..)) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable{
		// 接收到请求，记录请求内容
		Object	returnVal=null;
		Class<?> targetClass = joinPoint.getTarget().getClass();
		String methodName = joinPoint.getSignature().getName();
		if (BeanUtils.isEmpty(methodName) || (methodName.startsWith("get")) || 
			 (methodName.startsWith("is") && methodName.endsWith("Exist") ) || ignoreMethodArr.contains(methodName)){ 
			returnVal = joinPoint.proceed();
			return returnVal;
		}
		

		Object[] params=joinPoint.getArgs();
		if (params.length>0) {
			if (params[0] instanceof QueryFilter) {
				returnVal = joinPoint.proceed();
				return returnVal;
			}
		}
		//方法
		Method[] methods = targetClass.getMethods();
		Method method = null;
		for (int i = 0; i < methods.length; i++){
			if (methods[i].getName() == methodName){
				method = methods[i];
				break;
			}
		}
		ApiOperation annotation = method.getAnnotation(ApiOperation.class);
		//如果方法上没有注解@ApiOperation，返回
		if(annotation==null){
			returnVal = joinPoint.proceed();
			return returnVal;
		}
		String notes = annotation.notes();
		UpdateMethod updateMethod = method.getAnnotation(UpdateMethod.class);
		//此处可能有未实现UpdateCompare接口的，防止转型报错
		try {
			if(updateMethod!=null){
				Class<?> argType = updateMethod.type();
				Object[] argAry = joinPoint.getArgs();
				for (Object obj : argAry) {
					if(obj.getClass().equals(argType)){
						UpdateCompare compare = (UpdateCompare)obj;
						String memo = compare.compare();
						notes+=memo;
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		OperateLog operateLog=new OperateLog();
		operateLog.setId(UniqueIdUtil.getSuid());
		operateLog.setNote(notes);
		operateLog.setReqIp(getIpAddress(request));
		operateLog.setReqType(request.getMethod());
		operateLog.setReqUrl(request.getRequestURI().toString());
		operateLog.setReqParam(convestArrayToString(params));
		operateLog.setOperatorName();
		operateLog.setStartTime(LocalDateTime.now());
		// 记录下请求内容  
		try {
			returnVal = joinPoint.proceed();
			operateLog.setSuccess(1);
			operateLog.setEndTime(LocalDateTime.now());
		    OperateLogUtil.doLogAsync(operateLog);
		} catch (Exception e) {
			operateLog.setSuccess(0);
			operateLog.setFailReason(e.getMessage());
			operateLog.setEndTime(LocalDateTime.now());
		    OperateLogUtil.doLogAsync(operateLog);
			throw e;
		}	
		return returnVal;
	}  
	
    public static String getIpAddress(HttpServletRequest request) { 
    	
        String ip = request.getHeader("x-forwarded-for");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }  
        
        @SuppressWarnings("rawtypes")
		Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            System.err.println(key+"---------"+value);
        }
        return ip;  
    }  

	
	public String convestArrayToString(Object[] arr) throws IOException{
		String [] str = new String[arr.length];
	    if(null != arr){
			for (int i = 0; i < arr.length; i++){
				str[i] = JsonUtil.toJson(arr[i]);
			}
	  }
		return Arrays.toString(str);
	}
}
