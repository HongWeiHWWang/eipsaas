package com.hotent.bpm.plugin.execution.procnotify.def;

import java.util.HashMap;
import java.util.Map;

import com.hotent.bpm.api.constant.EventType;
import com.hotent.bpm.plugin.core.plugindef.AbstractBpmExecutionPluginDef;
import com.hotent.bpm.plugin.task.tasknotify.def.model.NotifyVo;

public class ProcNotifyPluginDef extends AbstractBpmExecutionPluginDef{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2102596766855882203L;
	/**
	 * 信息配置集合
	 */
	Map<EventType,NotifyVo> notifyVoMap = new HashMap<EventType,NotifyVo>();	
	
	public void addNotifyVo(EventType eventType,NotifyVo notifyVo){
		notifyVo.setEventType(eventType);
		notifyVoMap.put(eventType, notifyVo);
	}
	
	

	public Map<EventType, NotifyVo> getNotifyVoMap() {
		return notifyVoMap;
	}
}
