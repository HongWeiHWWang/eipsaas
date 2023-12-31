package com.hotent.bpm.plugin.core.factory;

import com.hotent.base.util.AppUtil;
import com.hotent.bpm.api.constant.AopType;
import com.hotent.bpm.api.constant.EventType;
import com.hotent.bpm.api.plugin.core.context.BpmPluginContext;
import com.hotent.bpm.api.plugin.core.context.ProcessInstAopPluginContext;
import com.hotent.bpm.api.plugin.core.factory.BpmPluginFactory;
import com.hotent.bpm.api.plugin.core.runtime.BpmExecutionPlugin;
import com.hotent.bpm.api.plugin.core.runtime.BpmTaskPlugin;
import com.hotent.bpm.api.plugin.core.runtime.ProcessInstAopPlugin;


public class DefaultBpmPluginFactory implements BpmPluginFactory{

	
	@Override
	public BpmExecutionPlugin buildExecutionPlugin(
			BpmPluginContext bpmPluginContext, EventType eventType) {
		if(bpmPluginContext.getEventTypes().contains(eventType)){
			try {				
				BpmExecutionPlugin bpmExecutionPlugin = (BpmExecutionPlugin)AppUtil.getBean(bpmPluginContext.getPluginClass());
				return bpmExecutionPlugin;
			} catch (Exception e) {
				
			}
		}
		return null;
	}

	
	@Override
	public BpmTaskPlugin buildTaskPlugin(
			BpmPluginContext bpmPluginContext, EventType eventType) {
		if(bpmPluginContext.getEventTypes().contains(eventType)){
			try {				
				BpmTaskPlugin bpmTaskPlugin = (BpmTaskPlugin)AppUtil.getBean(bpmPluginContext.getPluginClass());
				return bpmTaskPlugin;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public ProcessInstAopPlugin buildProcessInstAopPlugin(
			ProcessInstAopPluginContext processInstAopPluginContext,
			AopType aopType) {
		if(processInstAopPluginContext.getAopType().equals(aopType)){
			try {
				ProcessInstAopPlugin processInstAopPlugin = (ProcessInstAopPlugin)AppUtil.getBean(processInstAopPluginContext.getPluginClass());
				return processInstAopPlugin;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}


}
