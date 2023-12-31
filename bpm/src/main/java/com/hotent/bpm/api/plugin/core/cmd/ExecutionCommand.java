package com.hotent.bpm.api.plugin.core.cmd;

import com.hotent.bpm.api.constant.EventType;
import com.hotent.bpm.api.model.delegate.BpmDelegateExecution;

public interface ExecutionCommand {
	public void execute(EventType eventType, BpmDelegateExecution execution) throws Exception;
}
