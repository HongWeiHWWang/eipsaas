package com.hotent.bpm.plugin.core.session;

import com.hotent.bpm.api.engine.BpmxEngine;
import com.hotent.bpm.api.plugin.core.session.BpmPluginSession;
import com.hotent.uc.api.service.IOrgService;

public abstract class AbstractBpmPluginSession implements BpmPluginSession{
	/**
	 * 工作流服务引擎
	 */
	private BpmxEngine bpmxEngine;
	/**
	 * 组织服务引擎
	 */
	private IOrgService orgEngine;

	public BpmxEngine getBpmxEngine() {
		return bpmxEngine;
	}
	public void setBpmxEngine(BpmxEngine bpmxEngine) {
		this.bpmxEngine = bpmxEngine;
	}
	public IOrgService getOrgEngine() {
		return orgEngine;
	}
	public void setOrgEngine(IOrgService orgEngine) {
		this.orgEngine = orgEngine;
	}

	
}
