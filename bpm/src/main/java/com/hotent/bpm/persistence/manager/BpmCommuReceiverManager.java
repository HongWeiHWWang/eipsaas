package com.hotent.bpm.persistence.manager;

import java.util.List;

import com.hotent.base.manager.BaseManager;
import com.hotent.bpm.persistence.model.BpmCommuReceiver;

public interface BpmCommuReceiverManager extends BaseManager<BpmCommuReceiver>{

	BpmCommuReceiver getByCommuUser(String commuId, String receiverId);

    BpmCommuReceiver getByCommuId(String commuId);
	/**
	 * 通过沟通ID获取所有的接收人 如果 status == null 获取所有
	 * @param commuId
	 * @param status
	 * 	COMMU_NO="no";             
		COMMU_RECEIVE="receive";   
		COMMU_FEEDBACK="feedback"; 
	 * @return
	 */
	public List<BpmCommuReceiver> getByCommuStatus(String commuId,String status);
	/**检查是否已经沟通过*/
	boolean checkHasCommued(String commuId, String userId);
}
