package com.hotent.bpm.api.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hotent.base.jms.JmsActor;
import com.hotent.bpm.api.constant.ActionType;
import com.hotent.uc.api.model.IUser;

/**
 * 任务通知对象。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-5-8-下午3:57:56
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class NotifyTaskModel {
	
	/**
	 * 任务ID
	 */
	private String taskId="";
	
	/**
	 * act流程实例ID
	 */
	private String bpmnInstId="";
	
	/**
	 * 流程实例ID
	 */
	private String procInstId="";
	
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
	 * 流程变量
	 */
	Map<String, Object> vars=new HashMap<String, Object>();
	
	/**
	 * 任务接收人。
	 */
	private List<IUser> identitys=new ArrayList<IUser>();

	private List<JmsActor> jmsActors = new ArrayList<>();
	
	/**
	 * 动作类型
	 */
	private ActionType actionType;
	
	/**
	 * 操作名称
	 */
	private String actionName="";
	
	/**
	 * 流程意见。
	 */
	private String opinion="";
	
	private boolean isAgent=false;
	
	/**
	 * 代理人
	 */
	private IUser agent;
	
	/**
	 * 委托人
	 */
	private IUser delegator;
	
	

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

	public Map<String, Object> getVars() {
		return vars;
	}

	public void setVars(Map<String, Object> vars) {
		this.vars = vars;
	}

	public List<IUser> getIdentitys() {
		return identitys;
	}

	public void setIdentitys(List<IUser> identitys) {
		this.identitys = identitys;
	}

	public ActionType getActionType() {
		return actionType;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
	
	
	
	
	public boolean isAgent() {
		return isAgent;
	}

	public void setAgent(boolean isAgent) {
		this.isAgent = isAgent;
	}



	public IUser getAgent() {
		return agent;
	}

	public void setAgent(IUser agent) {
		this.agent = agent;
	}

	public IUser getDelegator() {
		return delegator;
	}

	public void setDelegator(IUser delegator) {
		this.delegator = delegator;
	}
	
	public NotifyTaskModel addVars(String name,String value){
		vars.put(name, value);
		return this;
	}

	public String getProcInstId() {
		return procInstId;
	}

	public void setProcInstId(String procInstId) {
		this.procInstId = procInstId;
	}

	public List<JmsActor> getJmsActors() {
		return jmsActors;
	}

	public void setJmsActors(List<JmsActor> jmsActors) {
		this.jmsActors = jmsActors;
	}

	/**
	 * 获取任务通知的对象。
	 * @param taskId
	 * @param bpmnInstId
	 * @param subject
	 * @param nodeId
	 * @param nodeName
	 * @param bpmnDefId
	 * @param vars
	 * @param identitys
	 * @param actionType
	 * @param actionName
	 * @param opinion
	 * @return  NotifyTaskModel
	 */
	public static NotifyTaskModel getNotifyModel(String taskId,String bpmnInstId,String procInstId,String subject,
			String nodeId,String nodeName,String bpmnDefId,Map<String, Object> vars,
			List<IUser> identitys,ActionType actionType,String actionName,String opinion){
		
		NotifyTaskModel model=new NotifyTaskModel();
		model.setTaskId(taskId);
		model.setBpmnInstId(bpmnInstId);
		model.setSubject(subject);
		model.setNodeId(nodeId);
		model.setNodeName(nodeName);
		model.setProcInstId(procInstId);
		model.setBpmnDefId(bpmnDefId);
		model.setVars(vars);
		model.setActionType(actionType);
		model.setActionName(actionName);
		model.setOpinion(opinion);
		model.setIdentitys(identitys);
		
		return model;
	}
	
	
	
	
	
}
