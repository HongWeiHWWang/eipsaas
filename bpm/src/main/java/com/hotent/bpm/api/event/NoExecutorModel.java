package com.hotent.bpm.api.event;

import java.util.ArrayList;
import java.util.List;

import com.hotent.bpm.api.model.identity.BpmIdentity;

/**
 * 没有找到执行人通知。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-5-8-下午5:19:42
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class NoExecutorModel {
	
	/**
	 * 任务ID
	 */
	private String taskId="";
	
	/**
	 * 流程实例ID
	 */
	private String bpmnInstId="";
	
	/**
	 * 任务标题
	 */
	private String subject="";
	
	/**
	 * 节点ID.
	 */
	private String nodeId="";
	
	/**
	 * 节点名称。
	 */
	private String nodeName="";
	
	/**
	 * 流程定义ID
	 */
	private String bpmnDefId="";
	
	/**
	 * 没有定义执行人。
	 */
	private List<BpmIdentity> identifyList=new ArrayList<BpmIdentity>();

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getBpmnInstId() {
		return bpmnInstId;
	}

	public void setBpmnInstId(String bpmnInstId) {
		this.bpmnInstId = bpmnInstId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
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

	public String getBpmnDefId() {
		return bpmnDefId;
	}

	public void setBpmnDefId(String bpmnDefId) {
		this.bpmnDefId = bpmnDefId;
	}

	public List<BpmIdentity> getIdentifyList() {
		return identifyList;
	}

	public void setIdentifyList(List<BpmIdentity> identifyList) {
		this.identifyList = identifyList;
	}
	
	/**
	 * 获取没有执行人对象。
	 * @param taskId
	 * @param bpmnInstId
	 * @param subject
	 * @param nodeId
	 * @param nodeName
	 * @param bpmnDefId
	 * @return NoExecutorModel
	 */
	public static NoExecutorModel getNoExecutorModel( String taskId,String bpmnInstId,
			String subject,String nodeId,String nodeName,String bpmnDefId){
		
		NoExecutorModel model=new NoExecutorModel();
		model.setTaskId(taskId);
		model.setBpmnInstId(bpmnInstId);
		model.setSubject(subject);
		model.setNodeId(nodeId);
		model.setNodeName(nodeName);
		model.setBpmnDefId(bpmnDefId);
		
		return model;
	}
	
}
