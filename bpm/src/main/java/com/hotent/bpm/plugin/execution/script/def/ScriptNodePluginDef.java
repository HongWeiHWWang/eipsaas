package com.hotent.bpm.plugin.execution.script.def;

import com.hotent.bpm.plugin.core.plugindef.AbstractBpmExecutionPluginDef;

public class ScriptNodePluginDef extends AbstractBpmExecutionPluginDef {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3858139780017277007L;
	private String script="";

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

}
