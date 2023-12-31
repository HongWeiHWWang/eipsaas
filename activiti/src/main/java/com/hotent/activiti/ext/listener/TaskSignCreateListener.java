package com.hotent.activiti.ext.listener;

import org.activiti.engine.impl.listener.TaskSignCreateEventListener;

import com.hotent.base.util.AppUtil;
import com.hotent.bpm.api.constant.EventType;
import com.hotent.bpm.api.constant.ScriptType;
import com.hotent.bpm.api.event.TaskSignCreateEvent;
import com.hotent.bpm.api.model.delegate.BpmDelegateTask;

/**
 * 会签任务创建监听器。
 * <pre> 
 * 构建组：x5-bpmx-activiti
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-30-下午10:21:16
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class TaskSignCreateListener extends AbstractTaskListener  {
	
	
	/**
	 * serialVersionUID
	 * @since 1.0.0
	 */
	private static final long serialVersionUID = 5830546066306214153L;
	
	

	@Override
	public EventType getBeforeTriggerEventType() {
		return EventType.TASK_SIGN_CREATE_EVENT;
	}

	@Override
	public EventType getAfterTriggerEventType() {
		return EventType.TASK_SIGN_POST_CREATE_EVENT;
	}

	@Override
	public void beforePluginExecute(BpmDelegateTask delegateTask) {

	}

	@Override
	public void triggerExecute(BpmDelegateTask delegateTask) {
		TaskSignCreateEvent ev=new TaskSignCreateEvent(delegateTask);
		//AppUtil.getBean(TaskSignCreateEventListener.class);
		AppUtil.publishEvent(ev);
	}

	@Override
	public void afterPluginExecute(BpmDelegateTask delegateTask) {
		
	}

	@Override
	protected ScriptType getScriptType() {
		return ScriptType.CREATE;
	}
}
