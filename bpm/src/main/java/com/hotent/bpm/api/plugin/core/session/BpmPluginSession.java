package com.hotent.bpm.api.plugin.core.session;

import com.hotent.bpm.api.engine.BpmxEngine;
import com.hotent.uc.api.service.IOrgService;


public interface BpmPluginSession {
	public BpmxEngine getBpmxEngine();
	
	public IOrgService getOrgEngine();
}
