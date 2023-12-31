package com.hotent.bpm.engine.def.impl.handler;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hotent.bpm.api.plugin.core.context.PluginContext;
import com.hotent.bpm.api.plugin.core.context.PluginParse;


/**
 * 插件容器类，用户根据类型获取指定插件。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-7-22-上午9:05:18
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class PluginContextContainer {
	
	
	private List<PluginContext> pluginList;
	
	public void setPluginList(List<PluginContext> pluginList){
		this.pluginList=pluginList;
	}
	
	public List<PluginContext> getPluginList(){
		return this.pluginList;
	}
	
	
	public PluginParse getPluginParse(String pluginType){
		for(PluginContext ctx:pluginList){
			if(!(ctx  instanceof PluginParse)) continue ; 
			String type=((PluginParse)ctx).getType();
			if(!type.equals(pluginType)) continue;
			return (PluginParse)ctx;
		}
		return null;
	}
	
	
}
