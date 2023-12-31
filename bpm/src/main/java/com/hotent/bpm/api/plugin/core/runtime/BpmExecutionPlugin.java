package com.hotent.bpm.api.plugin.core.runtime;

import com.hotent.bpm.api.plugin.core.def.BpmExecutionPluginDef;
import com.hotent.bpm.api.plugin.core.session.BpmExecutionPluginSession;

/**
 * 
 * <pre> 
 * 描述：执行类插件运行时
 * 构建组：x5-bpmx-native-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2013-12-18-下午3:13:55
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface BpmExecutionPlugin  extends RunTimePlugin<BpmExecutionPluginSession,BpmExecutionPluginDef,Void> {
	
}
