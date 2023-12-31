package com.hotent.bpm.api.service;


import java.util.List;

import com.hotent.bpm.api.constant.EventType;
import com.hotent.bpm.api.model.process.def.Restful;
import com.hotent.bpm.api.model.process.task.BpmTask;
import com.hotent.bpm.api.plugin.core.def.BpmExecutionPluginDef;
import com.hotent.bpm.api.plugin.core.def.BpmTaskPluginDef;
import com.hotent.bpm.api.plugin.core.session.BpmExecutionPluginSession;
import com.hotent.bpm.api.plugin.core.session.BpmTaskPluginSession;

/**
 * 沟通任务。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2017-11-14-上午9:58:05
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface RestfulService {
	
	/**
	 * 节点事件执行
	 * @param pluginSession
	 * @param pluginDef
	 */
	Void taskPluginExecute(BpmTaskPluginSession pluginSession,BpmTaskPluginDef pluginDef,List<Restful> restfuls);
	/**
	 * 节点事件执行
	 * @param pluginSession
	 * @param pluginDef
	 * @param restfuls
	 * @return
	 */
	Void executionPluginExecute(BpmExecutionPluginSession pluginSession,BpmExecutionPluginDef pluginDef,List<Restful> restfuls);
	/**
	 * 外部任务事件（沟通、转办等）执行
	 * @param task
	 * @param restfuls
	 * @param eventType
	 * @return
	 */
	Void outTaskPluginExecute(BpmTask task, List<Restful> restfuls,EventType eventType);
}
