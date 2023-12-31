package com.hotent.bo.context;

import com.hotent.base.util.FormContextThread;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 线程变量管理类。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray	
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-19-下午3:01:39
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@Service
public class FormContextThreadUtil implements FormContextThread{
		
	/**
	 * 通讯线程变量，用于主流程和外部子流程成进行通讯。
	 */
	private static ThreadLocal<Map<String,Object>> commuVars=new ThreadLocal<Map<String,Object>>();
	
	
	/**
	 * 设置通讯流程变量。
	 * @param commuVars_ 
	 * void
	 */
	public static void setCommuVars(Map<String,Object> commuVars_){
		commuVars.set(commuVars_);
	}

	/**
	 * 返回通讯变量。 
	 * @return 
	 * Map<String,Object>
	 */
	public static Map<String,Object> getCommuVars(){
		if(commuVars.get()==null){
			setCommuVars(new HashMap<String, Object>());
		}
		return commuVars.get();
	}
	/**
	 * 获取值。如果不存在获取默认值
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static Object getCommuVar(String key ,Object defaultValue){
		if(commuVars.get()==null){
			setCommuVars(new HashMap<String, Object>());
		}
		Map<String,Object> map = commuVars.get();
		if(map.containsKey(key))return map.get(key);
		
		return defaultValue;
	}
	
	/**
	 * 添加键和值。
	 * @param key
	 * @param value
	 */
	public static void putCommonVars(String key,Object value){
		Map<String,Object> vars= getCommuVars();
		vars.put(key, value);
	}
	
	public static void cleanCommuVars(){
		commuVars.remove();
	}
	
	
	/**
	 * 清除所有的线程变量。 
	 * void
	 */
	public void cleanAll(){
		commuVars.remove();
	}
}
