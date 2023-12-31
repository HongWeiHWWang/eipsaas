package com.hotent.bpm.plugin.usercalc.samenode.def;

import com.hotent.bpm.plugin.core.plugindef.AbstractUserCalcPluginDef;

public class SameNodePluginDef  extends AbstractUserCalcPluginDef {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4044850069996657293L;
	private String nodeId="";

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	
	

}
