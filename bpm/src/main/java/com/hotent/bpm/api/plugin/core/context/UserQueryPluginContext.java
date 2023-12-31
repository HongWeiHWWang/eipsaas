/**
 * 描述：TODO
 * 包名：com.hotent.runtime.api.plugin.core.context
 * 文件名：UserQueryPluginContext.java
 * 作者：win-mailto:chensx@jee-soft.cn
 * 日期2014-3-30-下午6:37:23
 *  2014广州宏天软件有限公司版权所有
 * 
 */
package com.hotent.bpm.api.plugin.core.context;

import com.hotent.bpm.api.plugin.core.runtime.RunTimePlugin;

/**
 * <pre> 
 * 描述：TODO
 * 构建组：x5-bpmx-api
 * 作者：Winston Yan
 * 邮箱：yancm@jee-soft.cn
 * 日期：2014-3-30-下午6:37:23
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface UserQueryPluginContext  {
	@SuppressWarnings("rawtypes")
	public Class<? extends RunTimePlugin> getUserQueryPluginClass();
}
