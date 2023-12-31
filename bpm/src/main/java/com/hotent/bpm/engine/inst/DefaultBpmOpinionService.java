package com.hotent.bpm.engine.inst;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.bpm.api.model.process.task.BpmTaskOpinion;
import com.hotent.bpm.api.service.BpmOpinionService;
import com.hotent.bpm.persistence.manager.BpmCheckOpinionManager;
import com.hotent.bpm.persistence.model.DefaultBpmCheckOpinion;

@Service
public class DefaultBpmOpinionService implements BpmOpinionService {
	
	@Resource
	BpmCheckOpinionManager bpmCheckOpinionManager;  

	@Override
	public List<BpmTaskOpinion> getTaskOpinions(String procInstId) {
		List<DefaultBpmCheckOpinion> defaultBpmCheckOpinions = bpmCheckOpinionManager.getByInstId(procInstId);
		
		return convert(defaultBpmCheckOpinions);
	}
	
	private List<BpmTaskOpinion> convert(List<DefaultBpmCheckOpinion> list){
		List<BpmTaskOpinion> rtnList=new ArrayList<BpmTaskOpinion>();
		for(DefaultBpmCheckOpinion opinion:list){
			rtnList.add(opinion);
		}
		return rtnList;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<BpmTaskOpinion> getByInstNodeId(String instId, String nodeId) {
		return (List)bpmCheckOpinionManager.getByInstNodeId(instId, nodeId);
	}

}
