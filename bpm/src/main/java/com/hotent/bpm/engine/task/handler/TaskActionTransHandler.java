package com.hotent.bpm.engine.task.handler;

import java.util.HashMap;
import java.util.Map;

import com.hotent.base.util.StringUtil;
import org.springframework.stereotype.Component;

import com.hotent.base.util.AppUtil;
import com.hotent.bpm.api.constant.ActionType;
import com.hotent.bpm.api.constant.OpinionStatus;
import com.hotent.bpm.api.event.DoTaskEvent;
import com.hotent.bpm.api.plugin.core.def.TaskActionHandlerDef;
import com.hotent.bpm.api.plugin.core.runtime.TaskActionHandler;
import com.hotent.bpm.api.plugin.core.session.TaskActionPluginSession;
import com.hotent.bpm.engine.task.cmd.DefaultTaskFinishCmd;

/**
 * 流程流转处理器。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-8-3-下午6:33:05
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@Component
public class TaskActionTransHandler implements TaskActionHandler {


	@Override
	public Boolean execute(TaskActionPluginSession pluginSession,
			TaskActionHandlerDef pluginDef) {
		/*
		//已将为同意或反对。
		//取得意见
		//1.如果任务为接收任务。那么会删除这个任务。
		//2.如果接收任务都完成，那么如果为返回。
		 * 判断任务为普通任务标准，字段task_id_是否为空，不为空为普通任务，否则为派生的任务。
		 * 如果父任务为普通任务，那就修改任务为普通任务。
		 * 如果父任务为流转任务，那么更新状态为transformed。
		 * 
		 * 如果为提交。
		 * 删除本任务和父任务。
		 * 如果为父任务为普通任务。
		 * 那么提交父任务。
		 * 如果为派生任务。
		 * 则删除父任务。
		 * 
		*/
		DefaultTaskFinishCmd finishCmd=(DefaultTaskFinishCmd)pluginSession.getTaskFinishCmd();

        String actionName = "";
        if(StringUtil.isNotEmpty(finishCmd.getAddSignAction())){//普通用户任务加签审批动作 agreeTrans（同意流转）opposeTrans（反对流转）
            actionName = finishCmd.getAddSignAction();
        }else{
            actionName = finishCmd.getActionName();
        }

		if("agreeTrans".equals(actionName)){
			actionName=OpinionStatus.AGREE.getKey();
		}
		else{
			actionName=OpinionStatus.OPPOSE.getKey();
		}
		
		String taskId=finishCmd.getTaskId();	
		
		String notifyType=  finishCmd.getNotifyType();
		
		// taskTransService.completeTask(taskId, actionName, notifyType, finishCmd.getApprovalOpinion());
		//该类在bpm下多处用到。但是该类所需的taskTransService，在bpmRuntime下、且taskTransService在bpm模块只有此处用到。
		//再加上taskTransService.completeTask在bpmRuntim下牵连甚广，不适合移动至bpm。此处采用发布事件的方式，
		Map<String, Object> map = new HashMap<>();
		map.put("taskId", taskId);
		map.put("actionName", actionName);
		map.put("notifyType", notifyType);
		map.put("opinion", finishCmd.getApprovalOpinion());
        //普通用户任务加签审批动作 agreeTrans（同意流转）opposeTrans（反对流转）
        map.put("addSignAction", finishCmd.getAddSignAction());
		DoTaskEvent testEvent = new DoTaskEvent(map);
		//AppUtil.getBean("doTaskEventListener");
		AppUtil.publishEvent(testEvent);
		return true;
	}

	@Override
	public boolean isNeedCompleteTask() {
		return false;
	}

	@Override
	public ActionType getActionType() {
		return ActionType.TRANS;
	}

}
