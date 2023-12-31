package com.hotent.bpm.api.plugin.core.task;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hotent.bpm.api.plugin.core.def.TaskActionHandlerDef;
import com.hotent.bpm.api.plugin.core.runtime.TaskActionHandler;
/**
 * <pre> 
 * 描述：任务动作执行插件配置接口
 * 构建组：x5-bpmx-api
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2014-3-18-下午2:13:58
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@Component
public interface TaskActionHandlerConfig{
	/**
	 * 通过动作名称获取得任务处理器
	 * @param actionName
	 * @return 
	 * TaskActionHandler
	 */
	public TaskActionHandler getTaskActionHandler(String actionType);
	/**
	 * 通过动作名称获得任务定义
	 * @param actionName
	 * @return 
	 * TaskActionHandlerDef
	 */
	public TaskActionHandlerDef getTaskActionHandlerDef(String actionType);
	/**
	 * 初始化任务执行配置 
	 * void
	 */
	public void init();
	
	
	/**
	 * 获取ActionHandlerDef列表。
	 * @return 
	 * List&lt;TaskActionHandlerDef>
	 */
	public Collection<TaskActionHandlerDef> getActionHandlerDefList();
	
	
	/**
	 * 获取全部的按钮定义。
	 * @return 
	 * Collection&lt;TaskActionHandlerDef>
	 */
	public List<? extends TaskActionHandlerDef> getAllActionHandlerDefList();
	
	/**
	 * 获取是否需要初始化的按钮。
	 * @param isInit 
	 * @return
	 */
	List<? extends TaskActionHandlerDef> getActionHandlerDefList(boolean isInit);
	
	
}
