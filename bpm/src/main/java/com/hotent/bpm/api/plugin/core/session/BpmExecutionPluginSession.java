/**
 * 描述：TODO
 * 包名：com.hotent.bpm.plugin.core.session
 * 文件名：BpmExecutionPluginSession.java
 * 作者：win-mailto:chensx@jee-soft.cn
 * 日期2014-2-23-下午8:56:43
 *  2014广州宏天软件有限公司版权所有
 * 
 */
package com.hotent.bpm.api.plugin.core.session;

import com.hotent.bpm.api.constant.EventType;
import com.hotent.bpm.api.model.delegate.BpmDelegateExecution;

/**
 * <pre> 
 * 描述：支持执行类插件执行的会话数据
 * 构建组：x5-bpmx-api
 * 作者：Winston Yan
 * 邮箱：yancm@jee-soft.cn
 * 日期：2014-2-23-下午8:56:43
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface BpmExecutionPluginSession extends BpmPluginSession{
	public BpmDelegateExecution getBpmDelegateExecution();
	
	public EventType getEventType();
}
