package com.hotent.bpm.plugin.core.session;

import com.hotent.bpm.api.cmd.TaskFinishCmd;
import com.hotent.bpm.api.model.delegate.BpmDelegateTask;
import com.hotent.bpm.api.plugin.core.session.TaskActionPluginSession;

public class DefaultTaskActionPluginSession extends AbstractBpmPluginSession
		implements TaskActionPluginSession {

	private TaskFinishCmd taskFinishCmd;
	private BpmDelegateTask bpmDelegateTask;
	@Override
	public TaskFinishCmd getTaskFinishCmd() {
		return taskFinishCmd;
	}

	public void setTaskFinishCmd(TaskFinishCmd taskFinishCmd) {
		this.taskFinishCmd = taskFinishCmd;
	}

	public BpmDelegateTask getBpmDelegateTask() {
		return bpmDelegateTask;
	}

	public void setBpmDelegateTask(BpmDelegateTask bpmDelegateTask) {
		this.bpmDelegateTask = bpmDelegateTask;
	}

}
