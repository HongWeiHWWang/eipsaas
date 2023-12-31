package com.hotent.activiti.ext.listener;

import com.hotent.base.util.AppUtil;
import com.hotent.bpm.api.constant.EventType;
import com.hotent.bpm.api.constant.ScriptType;
import com.hotent.bpm.api.event.CallSubProcessEndEvent;
import com.hotent.bpm.api.model.delegate.BpmDelegateExecution;
import com.hotent.bpm.listener.CallSubProcessEndEventListener;

/**
 * 外部子流程结束事件监听器。
 * <pre>
 * 	1.用于修改子流程节点的状态为正在结束。
 *  2.在子流程调用任务结束后，将人员变量删除。
 * </pre>
 * @author ray
 *
 */
public class CallSubProcessEndListener extends AbstractExecutionListener  {

	/**
	 * serialVersionUID
	 * @since 1.0.0
	 */
	private static final long serialVersionUID = 8833743502289179836L;

	@Override
	public EventType getBeforeTriggerEventType() {
		return EventType.END_EVENT;
	}

	@Override
	public EventType getAfterTriggerEventType() {
		return EventType.END_POST_EVENT;
	}
	

	@Override
	public void beforePluginExecute(BpmDelegateExecution bpmDelegateExecution) {
	}

	@Override
	public void triggerExecute(BpmDelegateExecution bpmDelegateExecution) {
	}

	@Override
	public void afterPluginExecute(BpmDelegateExecution bpmDelegateExecution) {
		CallSubProcessEndEvent ev=new CallSubProcessEndEvent(bpmDelegateExecution);
		//AppUtil.getBean(CallSubProcessEndEventListener.class);
		AppUtil.publishEvent(ev);
	}

	@Override
	protected ScriptType getScriptType() {
		return ScriptType.END;
	}


}
