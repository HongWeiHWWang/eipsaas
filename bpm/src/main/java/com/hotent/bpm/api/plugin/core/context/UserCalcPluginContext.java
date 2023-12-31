package com.hotent.bpm.api.plugin.core.context;



/**
 * 用户插件XML。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-7-21-下午9:45:45
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface UserCalcPluginContext extends PluginContext{
	
	
	/**
	 * 获取人员描述。
	 * <pre>
	 * 用户：A,B
	 * </pre>
	 * @param bpmPluginDef
	 * @return 
	 * String
	 */
	String getDescription();
	
	

	
	
	
	
	
	 
	
}
