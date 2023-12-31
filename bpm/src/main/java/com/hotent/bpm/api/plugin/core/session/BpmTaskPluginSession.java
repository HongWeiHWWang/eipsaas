/**
 * 描述：TODO
 * 包名：com.hotent.bpm.plugin.core.session
 * 文件名：BpmTaskPluginSession.java
 * 作者：win-mailto:chensx@jee-soft.cn
 * 日期2014-2-23-下午8:51:34
 *  2014广州宏天软件有限公司版权所有
 * 
 */
package com.hotent.bpm.api.plugin.core.session;

import com.hotent.bpm.api.constant.EventType;
import com.hotent.bpm.api.model.delegate.BpmDelegateTask;

/**
 * <pre> 
 * 描述：支持任务类插件执行的会话数据
 * 构建组：x5-bpmx-api
 * 作者：Winston Yan
 * 邮箱：yancm@jee-soft.cn
 * 日期：2014-2-23-下午8:51:34
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface BpmTaskPluginSession extends BpmPluginSession{
	/**
	 * 获得BpmDelegateTask对象
	 * @return 
	 * BpmDelegateTask
	 * @exception 
	 * @since  1.0.0
	 */
	public BpmDelegateTask getBpmDelegateTask();
	
	public EventType getEventType();
}
