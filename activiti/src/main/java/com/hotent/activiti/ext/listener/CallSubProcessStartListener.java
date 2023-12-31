package com.hotent.activiti.ext.listener;

import com.hotent.base.util.AppUtil;
import com.hotent.bpm.api.constant.EventType;
import com.hotent.bpm.api.constant.ScriptType;
import com.hotent.bpm.api.event.CallSubProcessStartEvent;
import com.hotent.bpm.api.model.delegate.BpmDelegateExecution;
import com.hotent.bpm.listener.CallSubProcessEndEventListener;
import com.hotent.bpm.listener.CallSubProcessStartEventListener;

/**
 * 外部子流程节点启动事件监听器。
 * <pre>
 * 1.	子流程执行顺序。
 *  	1.流程流转到外部子流程节点。
 *  	2.首先执行节点开始监听器，CallSubProcessStartListener。
 *  	3.再触发子流程的开始监听事件。
 *  	4.子流程执行完毕，会触发结束监听事件。
 *  	5.触发子流程节点结束监听器。
 * 	2.起始节点事件做的操作。
 * 		1.构造外部子流程变量 【outPassVars】 ，用于外部子流程和内部子流程做数据传递。
 * 		2.清除之前的堆栈任务。
 * 		3.清除之前子流程产生的子流程流程实例id列表。
 * 		4.修改子流程节点的状态为正在运行。
 * </pre>
 * @author ray
 *
 */
public class CallSubProcessStartListener  extends AbstractExecutionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6025609008447338532L;

	@Override
	public EventType getBeforeTriggerEventType() {
		return EventType.START_EVENT;
	}

	@Override
	public EventType getAfterTriggerEventType() {
		return EventType.START_POST_EVENT;
	}
	

	@Override
	public void beforePluginExecute(BpmDelegateExecution bpmDelegateExecution) {
		CallSubProcessStartEvent ev=new CallSubProcessStartEvent(bpmDelegateExecution);
		//AppUtil.getBean(CallSubProcessStartEventListener.class);
		AppUtil.publishEvent(ev);
	}

	@Override
	public void triggerExecute(BpmDelegateExecution bpmDelegateExecution) {
		
	}

	@Override
	public void afterPluginExecute(BpmDelegateExecution bpmDelegateExecution) {
		
	}

	@Override
	protected ScriptType getScriptType() {
		return ScriptType.START;
	}

	

	

}
