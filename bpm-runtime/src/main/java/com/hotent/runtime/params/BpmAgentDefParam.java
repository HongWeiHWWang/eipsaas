package com.hotent.runtime.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 代理指定流程对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(value="代理指定流程对象")
public class BpmAgentDefParam {
	
	@ApiModelProperty(name="id",notes="主键")
	protected String  id;
	
	@ApiModelProperty(name="settingId",notes="流程代理设置ID")
	protected String  settingId;
	
	@ApiModelProperty(name="flowKey",notes="流程定义KEY",required=true)
	protected String  flowKey;
	
	@ApiModelProperty(name="flowName",notes="流程名称",required=true)
	protected String flowName="";
	
	@ApiModelProperty(name="nodeId",notes="节点定义ID(为空的情况,如果指定ID,那么代理只在这些ID的任务生效)")
	protected String  nodeId;
	
	@ApiModelProperty(name="nodeName",notes="节点名称")
	protected String  nodeName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSettingId() {
		return settingId;
	}

	public void setSettingId(String settingId) {
		this.settingId = settingId;
	}

	public String getFlowKey() {
		return flowKey;
	}

	public void setFlowKey(String flowKey) {
		this.flowKey = flowKey;
	}

	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	
	public String toString() {
//		return new ToStringBuilder(this)
//		.append("id", this.id) 
//		.append("settingId", this.settingId) 
//		.append("flowKey", this.flowKey) 
//		.append("flowName", this.flowName) 
//		.append("nodeId", this.nodeId) 
//		.append("nodeName", this.nodeName) 
//		.toString();
		return "{"
		+ "\""+"id"+"\""+":"+"\""+this.id+"\","
		+ "\""+"settingId"+"\""+":"+"\""+this.settingId+"\","
		+"\""+"flowKey"+"\""+":"+"\""+this.flowKey+"\","
		+"\""+"flowName"+"\""+":"+"\""+this.flowName+"\","
		+"\""+"nodeId"+"\""+":"+"\""+this.nodeId+"\","
		+"\""+"agentName"+"\""+":"+"\""+this.nodeName+"\""
		+ "}";
	}

}
