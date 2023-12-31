package com.hotent.bpm.plugin.core.session;

import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.bpm.api.context.BpmContextUtil;
import com.hotent.bpm.api.model.delegate.BpmDelegateTask;
import com.hotent.bpm.api.plugin.core.session.BpmUserCalcPluginSession;

public class DefaultBpmUserCalcPluginSession extends AbstractBpmPluginSession implements BpmUserCalcPluginSession{
	/**
	 * 任务对象
	 */
	private BpmDelegateTask bpmDelegateTask;
	/**
	 * 具体使用时根据调用情况设置不同的对象进来
	 */
	private Map<String,Object> variables;
	
	
	/**
	 * variables
	 * @return  the variables
	 * @since   1.0.0
	 */
	
	public Map<String, Object> getVariables() {
		return variables;
	}

	/**
	 * @param variables the variables to set
	 */
	public void setVariables(Map<String, Object> variables) {
		this.variables = variables;
	}

	public BpmDelegateTask getBpmDelegateTask() {
		return bpmDelegateTask;
	}

	public void setBpmDelegateTask(BpmDelegateTask bpmDelegateTask) {
		this.bpmDelegateTask = bpmDelegateTask;
	}
	
	
	@Override
	public Map<String,ObjectNode>  getBoMap(){
		return BpmContextUtil.getBoFromContext();

	}

}
