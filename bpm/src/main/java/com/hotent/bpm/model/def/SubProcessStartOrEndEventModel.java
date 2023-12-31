package com.hotent.bpm.model.def;

import com.hotent.bpm.api.model.delegate.BpmDelegateExecution;

/**
 * 内嵌子流程开始或结束事件类型
 * 
 * @author Administrator
 *
 */
public class SubProcessStartOrEndEventModel
{
	// 结束节点时为null
	public BpmDelegateExecution bpmDelegateExecution = null;
	/*
	 * Activiti的流程实例ID
	 */
	protected String processInstanceId =null;
	protected String nodeName = null;
	protected String nodeId = null;
	// subEndGateway,subStartGateway
	protected String noteType = null;

	//是否为多实例的主流程
	protected boolean isMultiMain = false;
	
	public String getProcessInstanceId()
	{
		if (this.bpmDelegateExecution != null && this.processInstanceId == null)
		{
			return this.bpmDelegateExecution.getBpmnInstId();
		}
		return this.processInstanceId;

	}
	public void setProcessInstanceId(String processInstanceId)
	{
		this.processInstanceId=processInstanceId;
	}
	 
	public String getNodeName()
	{
		if (bpmDelegateExecution != null && nodeName == null)
		{
			return bpmDelegateExecution.getNodeName();
		}
		return nodeName;

	}
	public void setNodeName(String nodeName)
	{
		this.nodeName=nodeName;
	}
	 
	public String getNodeId()
	{
		if (bpmDelegateExecution != null && nodeId == null)
		{
			return bpmDelegateExecution.getNodeId();
		}
		return nodeId;

	}
	public void setNodeId(String nodeId)
	{
		this.nodeId=nodeId;
	}
	public String getNoteType()
	{
		 
		return noteType;

	}
	public void setNoteType(String noteType)
	{
		this.noteType=noteType;
	}
}
