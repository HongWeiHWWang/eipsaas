package com.hotent.bpm.api.model.process.task;

import com.hotent.bpm.api.constant.NodeType;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;

public class NodeDefTransient {
	
	private String nodeId="";
	
	private String name="";
	
	private NodeType type=null;

	private String bpmGatewayStackId="";
	
	
	public NodeDefTransient(){
		
	}
	
	public NodeDefTransient(BpmNodeDef bpmNodeDef){
		this.nodeId=bpmNodeDef.getNodeId();
		this.name=bpmNodeDef.getName();
		this.type=bpmNodeDef.getType();
	}
	

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public NodeType getType() {
		return type;
	}

	public void setType(NodeType type) {
		this.type = type;
	}

	public String getBpmGatewayStackId() {
		return bpmGatewayStackId;
	}

	public void setBpmGatewayStackId(String bpmGatewayStackId) {
		this.bpmGatewayStackId = bpmGatewayStackId;
	}

	@Override
	public boolean equals(Object obj) {
		boolean rtn=(obj instanceof NodeDefTransient);
		if(!rtn) return false;
		NodeDefTransient tmp=(NodeDefTransient)obj;
		return tmp.getNodeId().equals(this.nodeId);
	}
	
	
	
	
	

}
