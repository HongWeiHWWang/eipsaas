package com.hotent.bpm.plugin.core.factory;

import java.util.Map;

import javax.annotation.Resource;

import com.hotent.bpm.api.cmd.ProcessInstCmd;
import com.hotent.bpm.api.cmd.TaskFinishCmd;
import com.hotent.bpm.api.engine.BpmxEngine;
import com.hotent.bpm.api.model.delegate.BpmDelegateExecution;
import com.hotent.bpm.api.model.delegate.BpmDelegateTask;
import com.hotent.bpm.api.plugin.core.factory.BpmPluginSessionFactory;
import com.hotent.bpm.api.plugin.core.session.BpmExecutionPluginSession;
import com.hotent.bpm.api.plugin.core.session.BpmTaskPluginSession;
import com.hotent.bpm.api.plugin.core.session.BpmUserCalcPluginSession;
import com.hotent.bpm.api.plugin.core.session.ExecutionActionPluginSession;
import com.hotent.bpm.api.plugin.core.session.ProcessInstAopPluginSession;
import com.hotent.bpm.api.plugin.core.session.TaskActionPluginSession;
import com.hotent.bpm.api.plugin.core.session.TaskAopPluginSession;
import com.hotent.bpm.plugin.core.session.DefaultBpmExecutionPluginSession;
import com.hotent.bpm.plugin.core.session.DefaultBpmTaskPluginSession;
import com.hotent.bpm.plugin.core.session.DefaultBpmUserCalcPluginSession;
import com.hotent.bpm.plugin.core.session.DefaultExecutionActionPluginSession;
import com.hotent.bpm.plugin.core.session.DefaultProcessInstAopPluginSession;
import com.hotent.bpm.plugin.core.session.DefaultTaskActionPluginSession;
import com.hotent.bpm.plugin.core.session.DefaultTaskAopPluginSession;
import com.hotent.uc.api.service.IOrgService;

public class DefaultBpmPluginSessionFactory implements BpmPluginSessionFactory {	
	@Resource
	BpmxEngine bpmxEngine;
	
	@Resource(name="defaultOrgEngine")
	IOrgService defaultOrgEngine;
	
	@Override
	public BpmTaskPluginSession buildBpmTaskPluginSession(
			BpmDelegateTask bpmDelegateTask) {
		DefaultBpmTaskPluginSession bpmTaskPluginSession = new DefaultBpmTaskPluginSession();
		bpmTaskPluginSession.setBpmxEngine(bpmxEngine);
		bpmTaskPluginSession.setOrgEngine(defaultOrgEngine);
		bpmTaskPluginSession.setBpmDelegateTask(bpmDelegateTask);
		return bpmTaskPluginSession;
	}

	@Override
	public BpmExecutionPluginSession buildBpmExecutionPluginSession(
			BpmDelegateExecution bpmDelegateExecution) {
		DefaultBpmExecutionPluginSession bpmExecutionPluginSession = new DefaultBpmExecutionPluginSession();
		bpmExecutionPluginSession.setBpmxEngine(bpmxEngine);
		bpmExecutionPluginSession.setOrgEngine(defaultOrgEngine);
		bpmExecutionPluginSession.setBpmDelegateExecution(bpmDelegateExecution);
		return bpmExecutionPluginSession;
	}

	@Override
	public BpmUserCalcPluginSession buildBpmUserCalcPluginSession(Map<String, Object> variables){
		DefaultBpmUserCalcPluginSession defaultBpmUserCalcPluginSession = new DefaultBpmUserCalcPluginSession();
		defaultBpmUserCalcPluginSession.setBpmxEngine(bpmxEngine);
		defaultBpmUserCalcPluginSession.setOrgEngine(defaultOrgEngine);
		defaultBpmUserCalcPluginSession.setVariables(variables);		
		return defaultBpmUserCalcPluginSession;		
	}
	
	@Override
	public BpmUserCalcPluginSession buildBpmUserCalcPluginSession(
			BpmDelegateTask bpmDelegateTask) {
		DefaultBpmUserCalcPluginSession defaultBpmUserCalcPluginSession = new DefaultBpmUserCalcPluginSession();
		defaultBpmUserCalcPluginSession.setBpmxEngine(bpmxEngine);
		defaultBpmUserCalcPluginSession.setOrgEngine(defaultOrgEngine);
		defaultBpmUserCalcPluginSession.setVariables(bpmDelegateTask.getVariables());
		defaultBpmUserCalcPluginSession.setBpmDelegateTask(bpmDelegateTask);
		return defaultBpmUserCalcPluginSession;
	}

	@Override
	public ProcessInstAopPluginSession buildProcessInstAopPluginSession(
			ProcessInstCmd processInstCmd) {
		DefaultProcessInstAopPluginSession processInstAopPluginSession = new DefaultProcessInstAopPluginSession();
		processInstAopPluginSession.setBpmxEngine(bpmxEngine);
		processInstAopPluginSession.setOrgEngine(defaultOrgEngine);
		processInstAopPluginSession.setProcessInstCmd(processInstCmd);
		return processInstAopPluginSession;
	}

	@Override
	public TaskAopPluginSession buildTaskAopPluginSession(
			TaskFinishCmd taskFinishCmd) {
		DefaultTaskAopPluginSession taskAopPluginSession = new DefaultTaskAopPluginSession();
		taskAopPluginSession.setBpmxEngine(bpmxEngine);
		taskAopPluginSession.setOrgEngine(defaultOrgEngine);
		taskAopPluginSession.setTaskFinishCmd(taskFinishCmd);
		return taskAopPluginSession;
	}

	@Override
	public TaskActionPluginSession buildTaskActionPluginSession(BpmDelegateTask bpmDelegateTask,
			TaskFinishCmd taskFinishCmd) {
		DefaultTaskActionPluginSession taskActionPluginSession = new DefaultTaskActionPluginSession();
		taskActionPluginSession.setBpmxEngine(bpmxEngine);
		taskActionPluginSession.setOrgEngine(defaultOrgEngine);
		taskActionPluginSession.setTaskFinishCmd(taskFinishCmd);		
		taskActionPluginSession.setBpmDelegateTask(bpmDelegateTask);
		return taskActionPluginSession;
	}

	@Override
	public ExecutionActionPluginSession buildExecutionActionPluginSession(
			BpmDelegateExecution bpmDelegateExecution,
			TaskFinishCmd taskFinishCmd) {
		DefaultExecutionActionPluginSession pluginSession = new DefaultExecutionActionPluginSession();
		pluginSession.setBpmxEngine(bpmxEngine);
		pluginSession.setOrgEngine(defaultOrgEngine);
		pluginSession.setBpmDelegateExecution(bpmDelegateExecution);
		pluginSession.setTaskFinishCmd(taskFinishCmd);
		return pluginSession;
	}

	

	
	
	
}
