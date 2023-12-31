/**
 * 描述：TODO
 * 包名：com.hotent.runtime.api.plugin.core.def
 * 文件名：ExecutionActionHandlerDef.java
 * 作者：win-mailto:chensx@jee-soft.cn
 * 日期2014-4-8-下午4:12:08
 *  2014广州宏天软件有限公司版权所有
 * 
 */
package com.hotent.bpm.api.plugin.core.def;

/**
 * <pre> 
 * 描述：TODO
 * 构建组：x5-bpmx-api
 * 作者：Winston Yan
 * 邮箱：yancm@jee-soft.cn
 * 日期：2014-4-8-下午4:12:08
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface ExecutionActionHandlerDef extends BpmPluginDef{
	/**
	 * 获得描述
	 * @return 
	 * String
	 * @exception 
	 * @since  1.0.0
	 */
	public String getDescription();
	
	/**
	 * 获得操作类的类名
	 * @return 
	 * String
	 * @exception 
	 * @since  1.0.0
	 */
	public String getHandlerClass();
	
	/**
	 * 获得操作名称，如agree、end、back等
	 * @return 
	 * String
	 * @exception 
	 * @since  1.0.0
	 */
	public String getName();
}
