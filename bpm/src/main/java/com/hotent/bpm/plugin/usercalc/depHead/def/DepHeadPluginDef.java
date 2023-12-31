package com.hotent.bpm.plugin.usercalc.depHead.def;

import com.hotent.bpm.plugin.core.plugindef.AbstractUserCalcPluginDef;

public class DepHeadPluginDef extends AbstractUserCalcPluginDef {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7081474125246515565L;
	
	/**
	 * 是否主负责人
	 */
	private boolean mainLeader;

	public boolean isMainLeader() {
		return mainLeader;
	}

	public void setMainLeader(boolean isMainLeader) {
		this.mainLeader = isMainLeader;
	}
	

}
