package com.hotent.bpm.api.plugin.core.context;

import java.util.List;

import com.hotent.bpm.api.constant.EventType;

/**
 * 插件上下文接口。
 * @author ray
 *
 */
public interface BpmPluginContext extends PluginContext{
	/**
	 * 返回该插件关联的事件集合
	 * @return 
	 * List<EventType>
	 * @exception 
	 * @since  1.0.0
	 */
	public List<EventType> getEventTypes();	
	
	public int getOrder();
}
