package com.hotent.bpm.api.plugin.core.context;

import java.io.Serializable;

import org.w3c.dom.Element;

import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.runtime.RunTimePlugin;

public interface PluginContext extends Serializable{
	
	
	public final static String PLUGINCONTEXT="PluginContext";
	
	/**
	 * 插件运行时的Class
	 * @return 
	 * String
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	Class<? extends RunTimePlugin> getPluginClass();
	/**
	 * 返回流程插件定义
	 * @return 
	 * BpmPluginDef
	 * @exception 
	 * @since  1.0.0
	 */
	BpmPluginDef getBpmPluginDef();
	
	/**
	 * 根据XML及XSD解析实现类实例
	 * void
	 * @exception 
	 * @since  1.0.0
	 */
	BpmPluginDef parse(Element element);
	
	
	/**
	 * 获取插件标题。
	 * @return 
	 * String
	 */
	String getTitle();
	
	

	
	
	
	
	
	
	
	
	
	
}
