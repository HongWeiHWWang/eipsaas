package com.hotent.bpm.engine.inst;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.bpm.api.constant.NodeStatus;
import com.hotent.bpm.api.service.BpmProStatusService;
import com.hotent.bpm.persistence.manager.BpmProStatusManager;


@Service
public class DefaultBpmProStatusService implements BpmProStatusService {
	
	@Resource
	BpmProStatusManager bpmProStatusManager;  

	@Override
	public void createOrUpd(String instId, String bpmnDefId, String nodeId,
			String nodeName, NodeStatus nodeStatus) {
		bpmProStatusManager.createOrUpd(instId, bpmnDefId, nodeId, nodeName, nodeStatus);

	}

}
