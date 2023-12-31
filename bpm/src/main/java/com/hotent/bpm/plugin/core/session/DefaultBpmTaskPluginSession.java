package com.hotent.bpm.plugin.core.session;

import com.hotent.bpm.api.constant.EventType;
import com.hotent.bpm.api.model.delegate.BpmDelegateTask;
import com.hotent.bpm.api.plugin.core.session.BpmTaskPluginSession;

public class DefaultBpmTaskPluginSession extends AbstractBpmPluginSession implements BpmTaskPluginSession {
	private BpmDelegateTask bpmDelegateTask;
	
	private EventType eventType;
	
	public BpmDelegateTask getBpmDelegateTask() {
		return bpmDelegateTask;
	}
	public void setBpmDelegateTask(BpmDelegateTask bpmDelegateTask) {
		this.bpmDelegateTask = bpmDelegateTask;
	}
	public EventType getEventType() {
		return eventType;
	}
	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

}
