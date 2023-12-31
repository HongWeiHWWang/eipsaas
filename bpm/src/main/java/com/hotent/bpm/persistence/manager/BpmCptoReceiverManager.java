package com.hotent.bpm.persistence.manager;

import com.hotent.base.manager.BaseManager;
import com.hotent.bpm.persistence.model.BpmCptoReceiver;

public interface BpmCptoReceiverManager extends BaseManager<BpmCptoReceiver>{

	BpmCptoReceiver getByCopyToId(String copyToId,String userId); 
	
}
