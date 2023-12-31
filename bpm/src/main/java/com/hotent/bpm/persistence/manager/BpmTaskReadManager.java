package com.hotent.bpm.persistence.manager;

import java.util.List;

import com.hotent.base.manager.BaseManager;
import com.hotent.bpm.persistence.model.BpmTaskRead;

public interface BpmTaskReadManager extends BaseManager<BpmTaskRead>{
	
	
	/**
	  * 根据流程实例列表删除任务。
	  * @param instList 
	  * void
	  */
	 void delByInstList(List<String> instList);

}
