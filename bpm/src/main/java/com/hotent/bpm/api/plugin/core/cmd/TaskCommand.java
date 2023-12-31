package com.hotent.bpm.api.plugin.core.cmd;

import com.hotent.bpm.api.constant.EventType;
import com.hotent.bpm.api.model.delegate.BpmDelegateTask;

public interface TaskCommand {
	public void execute(EventType eventType, BpmDelegateTask bpmDelegateTask) throws Exception;
}
