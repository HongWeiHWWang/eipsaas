package com.hotent.bpm.api.plugin.core.session;

import com.hotent.bpm.api.cmd.ProcessInstCmd;

public interface ProcessInstAopPluginSession extends BpmPluginSession {
	public ProcessInstCmd getProcessInstCmd();
}
