package com.hotent.runtime.params;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 获取流程图（状态）参数
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(value="获取流程图（状态）参数")
public class BpmImageParamObject {
	
	@ApiModelProperty(name="defId",notes="流程定义id")
	private String defId;
	
	@ApiModelProperty(name="bpmnInstId",notes="BPMN流程实例ID")
	private String bpmnInstId;
	
	@ApiModelProperty(name="procInstId",notes="流程实例id")
	private String procInstId;
	
	@ApiModelProperty(name="taskId",notes="任务id")
	private String taskId;

	@ApiModelProperty(name="parentProcInstId",notes="父流程实例id")
	private String parentProcInstId;
	
	@ApiModelProperty(name="subNodeId",notes="子流程的节点id")
	private String subNodeId;
	
	
	public String getDefId() {
		return defId;
	}

	public void setDefId(String defId) {
		this.defId = defId;
	}

	public String getBpmnInstId() {
		return bpmnInstId;
	}

	public void setBpmnInstId(String bpmnInstId) {
		this.bpmnInstId = bpmnInstId;
	}

	public String getProcInstId() {
		return procInstId;
	}

	public void setProcInstId(String procInstId) {
		this.procInstId = procInstId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getParentProcInstId() {
		return parentProcInstId;
	}

	public void setParentProcInstId(String parentProcInstId) {
		this.parentProcInstId = parentProcInstId;
	}

	public String getSubNodeId() {
		return subNodeId; 
	}

	public void setSubNodeId(String subNodeId) {
		this.subNodeId = subNodeId;
	}
	
	
}
