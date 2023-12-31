package com.hotent.bpm.engine.task.handler;

import org.springframework.stereotype.Component;

import com.hotent.bpm.api.constant.ActionType;
import com.hotent.bpm.api.plugin.core.def.TaskActionHandlerDef;
import com.hotent.bpm.api.plugin.core.session.TaskActionPluginSession;

@Component
public class TaskActionRecoverHandler extends AbstractTaskActionHandler{

	@Override
	public boolean isNeedCompleteTask() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void preActionHandler(TaskActionPluginSession pluginSession,
			TaskActionHandlerDef def) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterActionHandler(TaskActionPluginSession pluginSession,
			TaskActionHandlerDef def) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ActionType getActionType() {
		
		return null;
	}

}
