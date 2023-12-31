package com.hotent.bpm.plugin.core.plugindef;

/**
 * 
 * <pre> 
 * 描述：抽象的插件定义基类
 * 构建组：x5-bpmx-api
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2014-2-22-下午1:53:57
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public abstract class AbstractBpmPluginDef {
	/**
	 * 插件名称
	 */
	protected String pluginName;
	
	public String getPluginName() {
		return pluginName;
	}
	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}

}
