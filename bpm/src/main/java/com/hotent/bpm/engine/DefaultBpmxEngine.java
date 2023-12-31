package com.hotent.bpm.engine;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.bpm.api.engine.BpmxEngine;
import com.hotent.bpm.api.service.BpmDefinitionService;
import com.hotent.bpm.api.service.BpmHistoryService;
import com.hotent.bpm.api.service.BpmInstService;
import com.hotent.bpm.api.service.BpmOpinionService;
import com.hotent.bpm.api.service.BpmTaskService;

@Service
public class DefaultBpmxEngine implements BpmxEngine {

	@Resource
	private BpmTaskService bpmTaskService;

	@Resource
	private BpmDefinitionService bpmDefinitionService;
	
	@Resource
	private BpmInstService bpmInstService;
	
	@Resource
	private BpmHistoryService bpmHistoryService;
	
	@Resource
	BpmOpinionService bpmOpinionService ; 
	
	@Override
	public String getName() {
		return "x5";
	}

	@Override
	public BpmTaskService getBpmTaskService() {
		return bpmTaskService;
	}

	@Override
	public BpmDefinitionService getBpmDefinitionService() {
		return bpmDefinitionService;
	}

	@Override
	public BpmInstService getBpmInstService() {
		return bpmInstService;
	}

	@Override
	public BpmHistoryService getBpmHistoryService() {
		return bpmHistoryService;
	}

	@Override
	public BpmOpinionService getBpmOpinionService() {
		return bpmOpinionService;
	}
	
}
