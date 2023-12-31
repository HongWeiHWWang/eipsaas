package com.hotent.bpm.persistence.manager;

import java.util.List;

import com.hotent.base.manager.BaseManager;
import com.hotent.bpm.api.model.delegate.BpmDelegateTask;
import com.hotent.bpm.persistence.model.BpmAgentCondition;

public interface BpmAgentConditionManager extends BaseManager<BpmAgentCondition>{

	/**
	 * 检查条件代理设置的条件，如果通过返回true，不通过返回false
	 * @param delegateTask 
	 * @param con
	 * @param busData
	 * @param vars
	 * @return
	 */
	boolean checkCondition(BpmDelegateTask delegateTask, BpmAgentCondition con);

	void removeBySettingId(String pk);

	List<BpmAgentCondition> getBySettingId(String id);
	
}
