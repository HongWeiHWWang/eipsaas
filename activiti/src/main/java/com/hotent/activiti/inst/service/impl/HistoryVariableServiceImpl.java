package com.hotent.activiti.inst.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricVariableInstance;

import com.hotent.bpm.natapi.inst.NatHistoryVariableService;

public class HistoryVariableServiceImpl implements NatHistoryVariableService {
	
	@Resource
	HistoryService historyService;

	@Override
	public Object getByBpmnInstIdVarName(String bpmnInstId, String name) {
		
		HistoricVariableInstance ent= historyService.createHistoricVariableInstanceQuery()
				.processInstanceId(bpmnInstId).variableName(name).singleResult();
		return ent.getValue();
	}

	@Override
	public Map<String, Object> getByBpmnInstId(String bpmnInstId) {
		List<HistoricVariableInstance> list= historyService.createHistoricVariableInstanceQuery().processInstanceId(bpmnInstId).list();
		Map<String,Object> map=new HashMap<String, Object>();
		for(int i=0;i<list.size();i++){
			HistoricVariableInstance inst=list.get(i);
			map.put(inst.getVariableName(), inst.getValue());
		}
		return map;
	}
	
	

}
