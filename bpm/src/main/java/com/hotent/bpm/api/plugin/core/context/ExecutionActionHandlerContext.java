/**
 * 描述：TODO
 * 包名：com.hotent.runtime.api.plugin.core.context
 * 文件名：ExecutionActionHandlerContext.java
 * 作者：win-mailto:chensx@jee-soft.cn
 * 日期2014-4-8-下午4:25:30
 *  2014广州宏天软件有限公司版权所有
 * 
 */
package com.hotent.bpm.api.plugin.core.context;

import java.util.Map;

import com.hotent.bpm.api.plugin.core.def.ExecutionActionHandlerDef;
import com.hotent.bpm.api.plugin.core.runtime.ExecutionActionHandler;

/**
 * <pre> 
 * 描述：TODO
 * 构建组：x5-bpmx-api
 * 作者：Winston Yan
 * 邮箱：yancm@jee-soft.cn
 * 日期：2014-4-8-下午4:25:30
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface ExecutionActionHandlerContext {
	/**
	 * 获得任务操作句柄实例的Map集合
	 * @return 
	 * Map<String,ExecutionActionHandler>
	 * @exception 
	 * @since  1.0.0
	 */
	public Map<String,ExecutionActionHandler> getExecutionActionHandlers();
	/**
	 * 获得任务操作定义实例的Map集合
	 * @return 
	 * Map<String,ExecutionActionHandlerDef>
	 * @exception 
	 * @since  1.0.0
	 */
	public Map<String,ExecutionActionHandlerDef> getExecutionActionHandlerDefs();
}
