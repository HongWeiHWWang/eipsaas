package com.hotent.bpm.plugin.task.restful.plugin;

import java.util.List;

import javax.annotation.Resource;

import com.hotent.base.util.BeanUtils;
import com.hotent.bpm.api.model.process.def.Restful;
import com.hotent.bpm.api.plugin.core.def.BpmTaskPluginDef;
import com.hotent.bpm.api.plugin.core.session.BpmTaskPluginSession;
import com.hotent.bpm.api.service.RestfulService;
import com.hotent.bpm.engine.identity.DefaultBpmIdentityService;
import com.hotent.bpm.persistence.manager.BpmDefinitionManager;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.plugin.core.runtime.AbstractBpmTaskPlugin;
import com.hotent.bpm.plugin.task.restful.def.RestfulInvokePluginDef;
import com.hotent.uc.api.service.IUserService;

/**
 * restful接口调用插件
 * @author heyifan
 *
 */
public class RestfulInvokePlugin extends AbstractBpmTaskPlugin{
	@Resource
	BpmProcessInstanceManager bpmProcessInstanceManager;
	@Resource
	BpmDefinitionManager bpmDefinitionManager;
	@Resource
	IUserService userServiceImpl;
	@Resource
	DefaultBpmIdentityService bpmIdentityService;

	@Resource
	RestfulService restfulService;
	
	@Override
	public Void execute(BpmTaskPluginSession pluginSession,BpmTaskPluginDef pluginDef) {
		RestfulInvokePluginDef restfulPluginDef = (RestfulInvokePluginDef) pluginDef;
		List<Restful> restfuls = restfulPluginDef.getRestfulList();
		return BeanUtils.isNotEmpty(restfuls)?restfulService.taskPluginExecute(pluginSession, pluginDef,restfuls):null;
	}
	
}