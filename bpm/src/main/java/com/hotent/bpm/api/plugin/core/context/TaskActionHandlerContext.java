package com.hotent.bpm.api.plugin.core.context;

import java.util.Map;

import com.hotent.bpm.api.plugin.core.def.TaskActionHandlerDef;
/**
 * 
 * <pre> 
 * 描述：任务操作句柄上下文对象
 * 构建组：x5-bpmx-api
 * 作者：winston yan
 * 邮箱:yancm@jee-soft.cn
 * 日期:2014-3-19-下午3:26:46
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface TaskActionHandlerContext  {
	/**
	 * 获得任务操作句柄实例的Map集合
	 * @return 
	 * Map<String,TaskActionHandler>
	 */
	public Map<String,String> getTaskActionHandlers();
	/**
	 * 获得任务操作定义实例的Map集合
	 * @return 
	 * Map<String,TaskActionHandlerDef>
	 */
	public Map<String,TaskActionHandlerDef> getTaskActionHandlerDefs();
}
