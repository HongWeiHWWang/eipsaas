package com.hotent.bpm.plugin.core.session;

import com.hotent.bpm.api.cmd.ProcessInstCmd;
import com.hotent.bpm.api.plugin.core.session.ProcessInstAopPluginSession;

public class DefaultProcessInstAopPluginSession extends
		AbstractBpmPluginSession implements ProcessInstAopPluginSession {

	private ProcessInstCmd processInstCmd;
	
	@Override
	public ProcessInstCmd getProcessInstCmd() {
		return processInstCmd;
	}

	public void setProcessInstCmd(ProcessInstCmd processInstCmd) {
		this.processInstCmd = processInstCmd;
	}

}
