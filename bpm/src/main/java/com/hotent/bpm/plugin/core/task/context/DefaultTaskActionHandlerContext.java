package com.hotent.bpm.plugin.core.task.context;

import java.util.HashMap;
import java.util.Map;

import com.hotent.bpm.api.plugin.core.context.TaskActionHandlerContext;
import com.hotent.bpm.api.plugin.core.def.TaskActionHandlerDef;

/**
 * 任务处理容器。
 * <pre> 
 * 构建组：x5-bpmx-plugin-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-7-8-下午8:46:06
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class DefaultTaskActionHandlerContext implements TaskActionHandlerContext {
	
	private Map<String, String> taskActionHandlers = new HashMap<String, String>();
	private Map<String, TaskActionHandlerDef> taskActionHandlerDefs = new HashMap<String, TaskActionHandlerDef>();
	
	@Override
	public Map<String, String> getTaskActionHandlers() {
		return taskActionHandlers;
	}

	@Override
	public Map<String, TaskActionHandlerDef> getTaskActionHandlerDefs() {
		return taskActionHandlerDefs;
	}

}
