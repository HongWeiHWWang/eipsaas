package com.hotent.bpm.persistence.manager;

import com.hotent.base.manager.BaseManager;
import com.hotent.bpm.persistence.model.BpmTaskCommu;

public interface BpmTaskCommuManager extends BaseManager<BpmTaskCommu>{
	
	BpmTaskCommu getByTaskId(String taskId);

    BpmTaskCommu getByInstId(String instId);
}
