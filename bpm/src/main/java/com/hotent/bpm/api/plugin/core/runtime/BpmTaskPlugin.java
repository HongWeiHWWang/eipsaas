package com.hotent.bpm.api.plugin.core.runtime;

import com.hotent.bpm.api.plugin.core.def.BpmTaskPluginDef;
import com.hotent.bpm.api.plugin.core.session.BpmTaskPluginSession;


/**
 * <pre> 
 * 描述：任务类插件运行时
 * 构建组：x5-bpmx-native-api
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2013-12-17-下午6:39:54
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface BpmTaskPlugin extends RunTimePlugin<BpmTaskPluginSession,BpmTaskPluginDef,Void>  {
	
}
