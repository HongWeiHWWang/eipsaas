package com.hotent.bpm.persistence.manager;

import com.hotent.base.manager.BaseManager;
import com.hotent.bpm.persistence.model.BpmAgentSetting;
import com.hotent.bpm.persistence.model.ResultMessage;

public interface BpmAgentSettingManager extends BaseManager<BpmAgentSetting>{
	
	
	/**
	 * 根据授权人和流程定义ID获取流程代理设定。
	 * @param authId
	 * @param flowKey
	 * @return BpmAgentSetting
	 */
	BpmAgentSetting getSettingByFlowAndAuthidAndDate(String authId,String flowKey);
	
	/**
	 * 检查代理是否和已设置的代理是否有冲突
	 * @param setting
	 * @return 
	 * ResultMessage
	 */
	ResultMessage checkConflict(BpmAgentSetting setting);

	/**
	 * 通过id获取流程代理设定。（包含流程和 代理条件设定）
	 * @param entityId
	 * @return
	 */
	BpmAgentSetting getById(String entityId);
}
