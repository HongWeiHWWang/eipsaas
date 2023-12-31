package com.hotent.bpm.api.plugin.core.runtime;

import java.util.List;

import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.session.BpmUserCalcPluginSession;

public interface UserQueryPlugin extends RunTimePlugin<BpmUserCalcPluginSession, BpmPluginDef, List<BpmIdentity>>{

}
