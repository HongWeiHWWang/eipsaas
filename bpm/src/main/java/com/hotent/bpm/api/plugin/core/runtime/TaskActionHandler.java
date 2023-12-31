package com.hotent.bpm.api.plugin.core.runtime;

import com.hotent.bpm.api.constant.ActionType;
import com.hotent.bpm.api.plugin.core.def.TaskActionHandlerDef;
import com.hotent.bpm.api.plugin.core.session.TaskActionPluginSession;

/**
 * 
 * <pre> 
 * 描述：任务的动作执行处理器
 * 构建组：x5-bpmx-api
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2014-3-18-上午11:32:56
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface TaskActionHandler extends RunTimePlugin<TaskActionPluginSession,TaskActionHandlerDef,Boolean>{
	/**
	 * 是否需要完成任务
	 * @return 
	 * boolean
	 * @exception 
	 * @since  1.0.0
	 */
	public boolean isNeedCompleteTask();
	
	public ActionType getActionType();
}
