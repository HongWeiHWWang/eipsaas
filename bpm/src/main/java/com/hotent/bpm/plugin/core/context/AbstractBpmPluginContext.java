package com.hotent.bpm.plugin.core.context;

import org.w3c.dom.Element;

import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.plugin.core.context.BpmPluginContext;
import com.hotent.bpm.api.plugin.core.context.PluginParse;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;

/**
 * 插件抽象类。
 * <pre> 
 * 1.任务插件继承自它。
 * 2.执行插件继承自它。
 * 构建组：x5-bpmx-plugin-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-7-21-下午10:15:23
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public abstract class AbstractBpmPluginContext implements BpmPluginContext,PluginParse{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7352108173349317104L;
	private BpmPluginDef bpmPluginDef;

	@Override
	public BpmPluginDef getBpmPluginDef() {
		return bpmPluginDef;
	}

	public void setBpmPluginDef(BpmPluginDef bpmPluginDef) {
		this.bpmPluginDef = bpmPluginDef;
	}
	
	
	protected abstract BpmPluginDef parseJson(String pluginJson);
	
	
	protected abstract BpmPluginDef parseElement(Element element);
	
	/**
	 * 父类实现将解析的插件定义设置到前实例。
	 */
	public BpmPluginDef parse(Element element) {
		BpmPluginDef def=parseElement(element);
		this.setBpmPluginDef(def);
		return def;
	}
	
	
	@Override
	public void parse(String pluginDefJson) {
		BpmPluginDef bpmPluginDef=parseJson(pluginDefJson);
		setBpmPluginDef(bpmPluginDef);
	}
	
	
	@Override
	public String getType() {
		return StringUtil.lowerFirst(this.getClass().getSimpleName().replaceAll(BpmPluginContext.PLUGINCONTEXT, ""));
	}
	

}
