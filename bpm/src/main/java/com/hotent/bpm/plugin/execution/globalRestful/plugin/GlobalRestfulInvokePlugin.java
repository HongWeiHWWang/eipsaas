package com.hotent.bpm.plugin.execution.globalRestful.plugin;

import java.util.List;

import javax.annotation.Resource;

import com.hotent.base.util.BeanUtils;
import com.hotent.bpm.api.model.process.def.Restful;
import com.hotent.bpm.api.plugin.core.def.BpmExecutionPluginDef;
import com.hotent.bpm.api.plugin.core.session.BpmExecutionPluginSession;
import com.hotent.bpm.api.service.RestfulService;
import com.hotent.bpm.engine.identity.DefaultBpmIdentityService;
import com.hotent.bpm.persistence.manager.BpmDefinitionManager;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.plugin.core.runtime.AbstractBpmExecutionPlugin;
import com.hotent.bpm.plugin.execution.globalRestful.def.GlobalRestfulInvokePluginDef;
import com.hotent.uc.api.service.IUserService;

/**
 * restful接口调用插件
 * @author heyifan
 *
 */
public class GlobalRestfulInvokePlugin extends AbstractBpmExecutionPlugin{
	
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
	public Void execute(BpmExecutionPluginSession pluginSession,
			BpmExecutionPluginDef pluginDef) {
		GlobalRestfulInvokePluginDef restfulPluginDef = (GlobalRestfulInvokePluginDef) pluginDef;
		List<Restful> restfuls = restfulPluginDef.getRestfulList();
		return BeanUtils.isNotEmpty(restfuls)?restfulService.executionPluginExecute(pluginSession, pluginDef,restfuls):null;
	}
	
}