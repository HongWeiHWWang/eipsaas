package com.hotent.bpm.plugin.task.taskcopyto.plugin;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.constant.TemplateConstants;
import com.hotent.bpm.api.constant.TemplateConstants.TEMP_VAR;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;
import com.hotent.bpm.api.plugin.core.def.BpmTaskPluginDef;
import com.hotent.bpm.api.plugin.core.session.BpmTaskPluginSession;
import com.hotent.bpm.api.service.BpmCopyToService;
import com.hotent.bpm.plugin.core.runtime.AbstractBpmTaskPlugin;
import com.hotent.bpm.plugin.core.util.UserAssignRuleQueryHelper;
import com.hotent.bpm.plugin.task.taskcopyto.def.TaskCopyToPluginDef;
import com.hotent.bpm.plugin.task.taskcopyto.def.model.CopyToItem;
import com.hotent.bpm.plugin.task.tasknotify.helper.NotifyHelper;
import com.hotent.uc.api.model.IUser;

public class TaskCopyToPlugin extends AbstractBpmTaskPlugin{	
	@Resource
	private BpmCopyToService bpmCopyToService;
	
	@Resource
	private NotifyHelper notifyHelper;
	
	public Void execute(BpmTaskPluginSession pluginSession,
			BpmTaskPluginDef pluginDef) throws Exception {
		
		Map<String,Object> variables = pluginSession.getBpmDelegateTask().getVariables();		
		
		ActionCmd actionCmd = ContextThreadUtil.getActionCmd();
		BpmProcessInstance instance=(BpmProcessInstance) actionCmd.getTransitVars(BpmConstants.PROCESS_INST);
		
		TaskCopyToPluginDef taskCopyToPluginDef = (TaskCopyToPluginDef)pluginDef;
		for(CopyToItem copyToItem:taskCopyToPluginDef.getCopyToItems()){			
			List<IUser> toUsers = UserAssignRuleQueryHelper.queryUsersWithExtract(copyToItem.getUserAssignRules(), pluginSession.getBpmDelegateTask().getVariables());
			bpmCopyToService.copyTo(toUsers, instance,pluginSession.getBpmDelegateTask().getTaskDefinitionKey());
			
			//如果设置了消息类型，表示要发送消息
			//抄送重用“任务完成”的模板，如果需要定义独立的模板类型，请增加常量
			if(copyToItem.getMsgTypes().size()>0){
				
				variables.put(TEMP_VAR.NODE_NAME, pluginSession.getBpmDelegateTask().getName());
				
				notifyHelper.notify(toUsers, copyToItem.getMsgTypes(), TemplateConstants.TYPE_KEY.TASK_COMPLETE,  variables);
			}
		}
		return null;
	}

}
