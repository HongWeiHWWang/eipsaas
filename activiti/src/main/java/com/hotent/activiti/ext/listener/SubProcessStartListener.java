package com.hotent.activiti.ext.listener;

import java.util.List;

import org.activiti.engine.impl.event.SubProcessStartEvent;
import org.activiti.engine.impl.listener.SubProcessStartEventListener;

import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.constant.EventType;
import com.hotent.bpm.api.constant.MultiInstanceType;
import com.hotent.bpm.api.constant.ScriptType;
import com.hotent.bpm.api.model.delegate.BpmDelegateExecution;
import com.hotent.bpm.model.def.SubProcessStartOrEndEventModel;

/**
 * 内嵌子流程进入的监听器。
 * 
 * @author Administrator
 *
 */
public class SubProcessStartListener extends AbstractExecutionListener
{

	/**
	 * serialVersionUID
	 * 
	 * @since 1.0.0
	 */
	private static final long serialVersionUID = -5954786939295460739L;

	@Override
	public EventType getBeforeTriggerEventType()
	{
		return EventType.START_EVENT;
	}

	@Override
	public EventType getAfterTriggerEventType(){
		return EventType.START_POST_EVENT;
	}

	@Override
	public void beforePluginExecute(BpmDelegateExecution bpmDelegateExecution){
		MultiInstanceType instType = bpmDelegateExecution.multiInstanceType();
		if (MultiInstanceType.NO.equals(instType)) return;

		Integer looper = (Integer) bpmDelegateExecution.getVariableLocal(BpmConstants.NUMBER_OF_LOOPCOUNTER);

		if (looper == null || MultiInstanceType.PARALLEL.equals(instType)) 	return;
		// 设置token。
		setToken(bpmDelegateExecution, looper);

	}

	private void setToken(BpmDelegateExecution bpmDelegateExecution, Integer looper){
		BpmDelegateExecution parent = bpmDelegateExecution.getParentExecution().getParentExecution();
		BpmDelegateExecution parentTmp = null;
		if(BeanUtils.isNotEmpty(parent)){
			parentTmp = parent.getParentExecution();
		}
		String parentToken = "";
		if (parentTmp != null){
			parentToken = (String) parentTmp.getVariableLocal(BpmConstants.TOKEN_NAME);
		}

		if (looper == 1 && parent!=null){
			List<BpmDelegateExecution> ents = parent.getExecutions();
			BpmDelegateExecution execution = ents.get(0);
			String tmp = "";
			if (StringUtil.isEmpty(parentToken)){
				tmp = BpmConstants.TOKEN_PRE + "0";
			} else{
				tmp = parentToken + "_0";
			}
			execution.getExecutions().get(0).setVariableLocal(BpmConstants.TOKEN_NAME, tmp);
		}

		String val = "";
		if (StringUtil.isEmpty(parentToken)){
			val = BpmConstants.TOKEN_PRE + looper;
		} else{
			val = parentToken += "_" + looper;
		}
		bpmDelegateExecution.setVariableLocal(BpmConstants.TOKEN_NAME, val);
	}

	@Override
	public void triggerExecute(BpmDelegateExecution bpmDelegateExecution)
	{
		MultiInstanceType instType = bpmDelegateExecution.multiInstanceType();
		if (!MultiInstanceType.PARALLEL.equals(instType)){
			// 记录内嵌以子流程到线程变量当中，目的是为了记录子流程开始节点的堆栈
			SubProcessStartOrEndEventModel model = new SubProcessStartOrEndEventModel();
			model.bpmDelegateExecution = bpmDelegateExecution;
			model.setNoteType("subStartGateway");
			SubProcessStartEvent event = new SubProcessStartEvent(model);
			//AppUtil.getBean(SubProcessStartEventListener.class);
			AppUtil.publishEvent(event);
		}
	}

	@Override
	public void afterPluginExecute(BpmDelegateExecution bpmDelegateExecution)
	{

	}

	@Override
	protected ScriptType getScriptType()
	{
		return ScriptType.START;
	}

}
