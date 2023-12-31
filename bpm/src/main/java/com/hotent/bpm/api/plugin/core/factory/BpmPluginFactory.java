package com.hotent.bpm.api.plugin.core.factory;

import com.hotent.bpm.api.constant.AopType;
import com.hotent.bpm.api.constant.EventType;
import com.hotent.bpm.api.plugin.core.context.BpmPluginContext;
import com.hotent.bpm.api.plugin.core.context.ProcessInstAopPluginContext;
import com.hotent.bpm.api.plugin.core.runtime.BpmExecutionPlugin;
import com.hotent.bpm.api.plugin.core.runtime.BpmTaskPlugin;
import com.hotent.bpm.api.plugin.core.runtime.ProcessInstAopPlugin;
/**
 * 
 * <pre> 
 * 描述：插件工厂
 * 构建组：x5-bpmx-api
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2014-2-19-下午5:15:44
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface BpmPluginFactory {
	/**
	 * 返回流程的插件实例
	 * @param pluginDefs
	 * @param eventType
	 * @return 
	 * List<AbstractBpmPlugin>
	 * @exception 
	 * @since  1.0.0
	 */
	public BpmExecutionPlugin buildExecutionPlugin(BpmPluginContext bpmPluginContext,EventType eventType);
	
	/**
	 * 获取节点插件实例
	 * @param pluginDefs
	 * @param eventType
	 * @return 
	 * List<BpmPlugin>
	 * @exception 
	 * @since  1.0.0
	 */
	public BpmTaskPlugin buildTaskPlugin(BpmPluginContext bpmPluginContext,EventType eventType);	

	public ProcessInstAopPlugin buildProcessInstAopPlugin(ProcessInstAopPluginContext processInstAopPluginContext,AopType aopType);
	
	
}
