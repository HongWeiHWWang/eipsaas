package com.hotent.bpm.api.context;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.hotent.base.util.ContextThread;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.model.process.task.BpmTask;

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
public class ContextThreadUtil implements ContextThread{
		
	/**
	 * cmd 线程变量。
	 * <pre>
	 * 用于穿越方法获取cmd对象。
	 * </pre>
	 */
	private static ThreadLocal<ActionCmd> actionCmdLocal=new ThreadLocal< ActionCmd>();
	
	/**
	 * 通讯线程变量，用于主流程和外部子流程成进行通讯。
	 */
	private static ThreadLocal<Map<String,Object>> commuVars=new ThreadLocal<Map<String,Object>>();
	
	
	/**
	 * 任务map，键放流程实例ID
	 */
	private static ThreadLocal<Map<String,Set<BpmTask>>> tasksMap=new ThreadLocal<Map<String,Set<BpmTask>>>();
	
		
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
	 * 设置命令变量。 
	 * @param cmd 
	 * void
	 */
	public static void setActionCmd(ActionCmd cmd){
		actionCmdLocal.set(cmd);
	}
	
	/**
	 * 获取命令变量。
	 * @return 
	 * ActionCmd
	 */
	public static ActionCmd getActionCmd(){
		return actionCmdLocal.get();
	}
	
	
	/**
	 * 往线程变量中添加任务。
	 * @param task 
	 * void
	 */
	public static void addTask(BpmTask task){
		Map<String,Set<BpmTask>> map= tasksMap.get();
		if(map==null){
			map=new HashMap<String, Set<BpmTask>>();
			Set<BpmTask> set=new HashSet<BpmTask>();
			set.add(task);
			map.put(task.getProcInstId(), set);
			
			tasksMap.set(map);
		}
		else{
			//包含此流程实例
			if(map.containsKey(task.getProcInstId())){
				Set<BpmTask> set=map.get(task.getProcInstId());
				if(set.contains(task)){
					set.remove(task);
					set.add(task);
				}
				else{
					set.add(task);
				}
			}
			else{
				Set<BpmTask> set=new HashSet<BpmTask>();
				set.add(task);
				map.put(task.getProcInstId(), set);
			}
		}
	}
	
	
	/**
	 * 根据流程实例ID获取
	 * @param instId
	 * @return 
	 * Set&lt;BpmTask>
	 */
	public static Set<BpmTask> getByInstId(String instId){
		Map<String,Set<BpmTask>> map= tasksMap.get();
		if(map==null) return null;
		if(map.get(instId)==null) return null;
		
		return map.get(instId);
	}
	
	
	/**
	 * 清除任务实例map。
	 * void
	 */
	public static void clearTaskMap(){
		tasksMap.remove();
	}

	/**
	 * 根据流程实例删除任务实例。 
	 * @param instId 
	 * void
	 */
	public static void clearTaskByInstId(String instId){
		Map<String,Set<BpmTask>> map= tasksMap.get();
		if(map==null) return;
		map.remove(instId);
	}
	
	/**
	 * 清除所有的线程变量。 
	 * void
	 */
	public void cleanAll(){
		actionCmdLocal.remove();
		commuVars.remove();
		tasksMap.remove();
	}
}
