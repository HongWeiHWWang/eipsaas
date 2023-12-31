package com.hotent.base.aop;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.FieldAuth;
import com.hotent.base.feign.FormFeignService;
import com.hotent.base.feign.PortalFeignService;
import com.hotent.base.query.PageList;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;

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
public class FieldAuthAspect {
	@Resource
	FormFeignService formFeignService;
	@Resource
	PortalFeignService portalFeignService;
	
	private final String FieldAuthPonit = "execution(* *..*Controller.*(..)) && @annotation(com.hotent.base.annotation.FieldAuth)";
	
	/**
     * 后置返回通知
     * 这里需要注意的是:
     * 如果参数中的第一个参数为JoinPoint，则第二个参数为返回值的信息
     * 如果参数中的第一个参数不为JoinPoint，则第一个参数为returning中对应的参数
     * returning 限定了只有目标方法返回值与通知方法相应参数类型时才能执行后置返回通知，否则不执行，对于returning对应的通知方法参数为Object类型将匹配任何目标返回值
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
     */
    @AfterReturning(value=FieldAuthPonit, returning = "keys")
    public Object doAfterReturningAdvice1(JoinPoint joinPoint, Object keys) throws Exception {
    	MethodInvocationProceedingJoinPoint methodPoint = (MethodInvocationProceedingJoinPoint)joinPoint;
    	Field proxy = methodPoint.getClass().getDeclaredField("methodInvocation");
    	proxy.setAccessible(true);
    	ReflectiveMethodInvocation j = (ReflectiveMethodInvocation) proxy.get(methodPoint);
    	Method method = j.getMethod();
    	String className = method.getAnnotation(FieldAuth.class).value();
        System.out.println("第一个后置返回通知的返回值：" + keys);
        Class cls = Class.forName(className);
        String simpleName = cls.getSimpleName();
        ObjectNode node = formFeignService.getByClassName(simpleName);
        if(BeanUtils.isEmpty(node)) {
        	return keys;
        }
        ObjectNode rightJson = portalFeignService.calcAllPermssion(node.get("fieldList").asText());
        Map<String, Object> map = JsonUtil.toMap(JsonUtil.toJson(rightJson));
        if(keys instanceof PageList){
        	PageList<Object> pageList = (PageList<Object>)keys;
        	if(pageList.getRows().size()>0){
        		List<Object> rows = pageList.getRows();
        		List<Object> result = new ArrayList<Object>();
            	for (Object object : rows) {
    				ObjectNode objectNode = (ObjectNode) JsonUtil.toJsonNode(object);
    				Set<Entry<String, Object>> entrySet = map.entrySet();
    				for (Entry<String, Object> entry : entrySet) {
    					String key = entry.getKey();
    					boolean flag = (boolean) entry.getValue();
    					if(!flag){
    						String converKey = converKey(key);
    						if(objectNode.has(converKey)){
    							objectNode.put(converKey, "无权限");
    						}
    					}
    				}
    				result.add(objectNode);
    			}
            	pageList.setRows(result);
        	}
        	return pageList;
        }
        
        return keys;
    }
    
    /**
     * 
     * @param key  DEF_ID_
     * @return defId
     */
    private String converKey(String key){
    	if(key.indexOf("_")==-1){
    		return key;
    	}
    	String[] split = key.split("_");
    	if(split.length==1){
    		return split[0].toLowerCase();
    	}
    	
    	StringBuffer sb = new StringBuffer(split[0].toLowerCase());
    	for (int i = 1; i < split.length; i++) {
    		String first = StringUtil.toFirst(split[i], true);
    		sb.append(first);
		}
    	
    	return sb.toString();
    }
}
