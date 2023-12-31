package com.hotent.activiti.ext.identity;

import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;

public interface INodeDef {
	
	BpmNodeDef getNodeDef(String bpmnDefId,String nodeId) throws Exception;

}
