package com.hotent.bpm.plugin.core.runtime;

import com.hotent.base.util.AppUtil;
import com.hotent.bpm.api.plugin.core.factory.BpmPluginSessionFactory;
import com.hotent.bpm.api.plugin.core.runtime.BpmTaskPlugin;
/**
 * 任务插件运行时类抽象列。
 * <pre> 
 * 任务插件运行时需要继承这个类。
 * 构建组：x5-bpmx-api
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2014-2-19-下午3:55:31
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public abstract class AbstractBpmTaskPlugin implements BpmTaskPlugin{
	BpmPluginSessionFactory bpmPluginSessionFactory;

	public BpmPluginSessionFactory getBpmPluginSessionFactory() {
		if(bpmPluginSessionFactory==null)
			this.bpmPluginSessionFactory = AppUtil.getBean(BpmPluginSessionFactory.class);
		return this.bpmPluginSessionFactory;
	}
}
