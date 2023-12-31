package com.hotent.bpm.plugin.core.session;

import com.hotent.bpm.api.cmd.TaskFinishCmd;
import com.hotent.bpm.api.plugin.core.session.TaskAopPluginSession;

public class DefaultTaskAopPluginSession extends AbstractBpmPluginSession
		implements TaskAopPluginSession {

	private TaskFinishCmd taskFinishCmd;
	
	@Override
	public TaskFinishCmd getTaskFinishCmd() {
		return taskFinishCmd;
	}

	public void setTaskFinishCmd(TaskFinishCmd taskFinishCmd) {
		this.taskFinishCmd = taskFinishCmd;
	}

	
}
