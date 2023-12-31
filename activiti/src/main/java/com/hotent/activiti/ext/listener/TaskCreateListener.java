package com.hotent.activiti.ext.listener;

import javax.annotation.Resource;

import com.hotent.base.util.AppUtil;
import com.hotent.bpm.api.constant.EventType;
import com.hotent.bpm.api.constant.ScriptType;
import com.hotent.bpm.api.event.TaskCreateEvent;
import com.hotent.bpm.api.model.delegate.BpmDelegateTask;
import com.hotent.bpm.api.service.BpmTaskActionService;
import com.hotent.bpm.listener.TaskCompleteEventListener;
import com.hotent.bpm.listener.TaskCreateEventListener;


/**
 * 任务创建的监听器。
 * <pre>
 * 主要处理人员的分配。
 * </pre>
 * @author ray
 *
 */
public class TaskCreateListener extends AbstractTaskListener {
	
	
	@Resource
	private BpmTaskActionService bpmTaskActionService;
	
	
	/**
	 * serialVersionUID
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -7836822392037648008L;
	
	@Override
	public EventType getBeforeTriggerEventType() {
		return EventType.TASK_CREATE_EVENT;
	}

	@Override
	public EventType getAfterTriggerEventType() {
		return EventType.TASK_POST_CREATE_EVENT;
	}

	@Override
	public void beforePluginExecute(BpmDelegateTask delegateTask) throws Exception {
		bpmTaskActionService.create(delegateTask);
	}

	@Override
	public void triggerExecute(BpmDelegateTask delegateTask) {
		TaskCreateEvent startEvent=new TaskCreateEvent(delegateTask);
		//todo 添加license4eip7 包后会导致新增用户任务1的任务新增2条 重复调用了
		//AppUtil.getBean(TaskCreateEventListener.class);
		AppUtil.publishEvent(startEvent);
	}	
	
	@Override
	public void afterPluginExecute(BpmDelegateTask delegateTask) {
		
	}

	@Override
	protected ScriptType getScriptType() {
		return ScriptType.CREATE;
	}

	
	
}
