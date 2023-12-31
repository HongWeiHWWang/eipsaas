package com.hotent.bpm.persistence.manager;

import java.util.List;

import com.hotent.base.manager.BaseManager;
import com.hotent.bpm.persistence.model.ActExecution;

public interface ActExecutionManager extends BaseManager<ActExecution>{
	
	
	void delByInstList(List<String> bpmInstList);

	List<String> getByParentsId(String id);

	void delActiveByInstList(List<String> includeBpmnIdList);
	
}
