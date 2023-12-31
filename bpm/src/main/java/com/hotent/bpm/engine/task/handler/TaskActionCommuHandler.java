package com.hotent.bpm.engine.task.handler;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.hotent.bpm.engine.task.cmd.DefaultTaskFinishCmd;
import com.hotent.bpm.api.constant.ActionType;
import com.hotent.bpm.api.plugin.core.def.TaskActionHandlerDef;
import com.hotent.bpm.api.plugin.core.runtime.TaskActionHandler;
import com.hotent.bpm.api.plugin.core.session.TaskActionPluginSession;
import com.hotent.bpm.api.service.TaskCommuService;

/**
 * 沟通退回处理。
 * @author yongguo
 *
 */
@Component
public class TaskActionCommuHandler implements TaskActionHandler{
	
	@Resource
	TaskCommuService taskCommuService;
	

	@Override
	public Boolean execute(TaskActionPluginSession pluginSession,
			TaskActionHandlerDef pluginDef) throws Exception {
		
		DefaultTaskFinishCmd finishCmd=(DefaultTaskFinishCmd)pluginSession.getTaskFinishCmd();
		
		String taskId=(String) finishCmd.getTaskId(); 
		
		String notifyType=finishCmd.getNotifyType();
		
		String opinion=finishCmd.getApprovalOpinion();
		
		taskCommuService.completeTask(taskId, notifyType, opinion);
		
		return true;
	}

	@Override
	public boolean isNeedCompleteTask() {
		return false;
	}

	@Override
	public ActionType getActionType() {
		return ActionType.COMMU;
	}

}
