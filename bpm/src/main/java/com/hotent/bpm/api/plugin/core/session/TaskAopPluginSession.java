package com.hotent.bpm.api.plugin.core.session;

import com.hotent.bpm.api.cmd.TaskFinishCmd;

public interface TaskAopPluginSession extends BpmPluginSession {
	public TaskFinishCmd getTaskFinishCmd();
}
