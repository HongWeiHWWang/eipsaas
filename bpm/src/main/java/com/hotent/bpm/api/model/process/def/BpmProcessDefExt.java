package com.hotent.bpm.api.model.process.def;

import java.io.Serializable;
import java.util.List;

import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.ProcBoDef;
import com.hotent.bpm.api.plugin.core.context.BpmPluginContext;
import com.hotent.bpm.api.plugin.core.context.ProcessInstAopPluginContext;

/**
 * 流程定义扩展数据。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-4-21-下午2:15:36
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface BpmProcessDefExt extends Serializable{
	
	/**
	 * 流程定义扩展插件。
	 * @return  List&lt;BpmPluginContext>
	 */
	public List<BpmPluginContext> getBpmPluginContexts();
	
	/**
	 * 根据插件实现类获取指定的插件。
	 * 比如：
	 * BpmPluginContext ctx=getBpmPluginContext(UserAssignPluginContext.class);
	 * 
	 * @param clazz
	 * @return
	 */
	BpmPluginContext getBpmPluginContext(Class<?> clazz);
	/**
	 * 流程AOP插件。
	 * @return  List&lt;ProcessInstAopPluginContext>
	 */
	public List<ProcessInstAopPluginContext> getProcessInstAopPluginContexts();
	
	/**
	 * 流程定义扩展属性。
	 * @return  BpmDefExtProperties
	 */
	public BpmDefExtProperties getExtProperties();
	
	/**
	 * 获取流程定义的BO定义列表。
	 * @return
	 */
	List<ProcBoDef> getBoDefList();
	
	/**
	 * 获取流程变量定义列表。
	 * @return
	 */
	List<BpmVariableDef> getVariableList();
	
}
