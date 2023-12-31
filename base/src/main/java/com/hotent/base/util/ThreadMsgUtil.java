package com.hotent.base.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息Util工具类,在线程变量中添加消息,消息使用list存放
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月26日
 */
public class ThreadMsgUtil {
	private static ThreadLocal<List<String>> localMsg = new ThreadLocal<List<String>>();
	
	private static ThreadLocal<Map<String,String>> localMapMsg = new ThreadLocal<Map<String,String>>();
	
	//流程消息的key
	public static final String MSG_FLOW_ERROR = "msg_flow_error";
	
	/**
	 * 添加消息
	 * 
	 * @param msg
	 */
	public static void addMsg(String msg) {
		List<String> list = localMsg.get();
		if (BeanUtils.isEmpty(list)) {
			list = new ArrayList<String>();
			list.add(msg);
			localMsg.set(list);
		} else {
			list.add(msg);
		}
	}

	/**
	 * 获取消息数据，并直接清除消息中的数据
	 * 
	 * @return
	 */
	public static List<String> getMsg() {
		return getMsg(true);
	}

	/**
	 * 获取消息数据
	 * 
	 * @param clean
	 * @return
	 */
	public static List<String> getMsg(boolean clean) {
		List<String> list = localMsg.get();
		if (clean) {

			localMsg.remove();
		}
		return list;
	}

	/**
	 * 返回流程消息
	 * 
	 * @return
	 */
	public static String getMessage() {

		return getMessage(true);
	}

	/**
	 * 获取消息
	 * 
	 * @param clean
	 * @return
	 */
	public static String getMessage(boolean clean) {
		List<String> list = getMsg(clean);
		String str = "";
		if (BeanUtils.isEmpty(list)) {
			return str;
		}
		for (String msg : list) {
			str += msg + "\r\n";
		}
		return str;
	}

	/**
	 * 清除消息
	 */
	public static void clean() {
		localMsg.remove();
	}
	
	/**
	 * 添加线程消息
	 * @param key
	 * @param msg
	 */
	public static void addMapMsg(String key,String msg){
		Map<String, String> map = localMapMsg.get();
		if(BeanUtils.isEmpty(map)){
			map = new HashMap<String, String>();
		}
		map.put(key, msg);
		localMapMsg.set(map);
	}
	
	public static String getMapMsg(String key){
		Map<String, String> map = localMapMsg.get();
		if(BeanUtils.isEmpty(map)){
			return null;
		}
		if(map.containsKey(key)){
			return map.get(key);
		}
		return null;
	}
	
	public static String getMapMsg(String key,boolean clean){
		Map<String, String> map = localMapMsg.get();
		if(BeanUtils.isEmpty(map)){
			return null;
		}
		if(map.containsKey(key)){
			String msg = map.get(key);
			if(clean) {
				map.remove(key);
			}
			return msg;
		}
		return null;
	}
	
	public static void cleanMapMsg(){
		localMapMsg.remove();
	}
}
