package com.hotent.runtime.params;

import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 流程执行相关对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(description="流程执行相关对象")
public class FlowParamObject {
	
	@ApiModelProperty(name="account",notes="发起人帐号/审批人帐号")
	private String account;
	
	@ApiModelProperty(name="defId",notes="流程定义id")
	private String defId;
	
	@ApiModelProperty(name="flowKey",notes="流程key")
	private String flowKey;
	
	@ApiModelProperty(name="subject",notes="流程标题")
	private String subject;
	
	@ApiModelProperty(name="procInstId",notes="流程实例id")
	private String procInstId;
	
	@ApiModelProperty(name="vars",notes="流程变量")
	private Map<String,String> vars;
	
	@ApiModelProperty(name="data",notes="bo业务数据，以base64加密后的密文")
	private String data;
	
	@ApiModelProperty(name="businessKey",notes="业务主键KEY")
	private String businessKey;
	
	@ApiModelProperty(name="taskId",notes="任务id")
	private String taskId;
	
	@ApiModelProperty(name="toUserId",notes="转办任务用户id")
	private String toUserId;
	
	@ApiModelProperty(name="addSignTaskUserId",notes="加签任务用户id")
	private String addSignTaskUserId;
	
	@ApiModelProperty(name="actionName",
			notes="审批动作,agree（审批）abandon（弃权）oppose（反对）agreeTrans（同意流转）opposeTrans（反对流转）commu（沟通反馈）reject（驳回）backToStart（驳回指定节点）"
			,allowableValues="agree,abandon,oppose,agreeTrans,opposeTrans,commu,reject,backToStart")
	private String actionName;
	
	@ApiModelProperty(name="opinion",notes="意见")
	private String opinion;
	
	@ApiModelProperty(name="directHandlerSign",notes="会签时是否直接审批通过",allowableValues="true,false")
	private boolean directHandlerSign;
	
	@ApiModelProperty(name="backHandMode",notes="驳回模式 ,direct :直来直往,normal: 按照流程图方式驳回",allowableValues="direct,normal")
	private String backHandMode;
	
	@ApiModelProperty(name="jumpType",notes=" 跳转方式 free : 自由跳转 ,select : 选择跳转,reject :驳回",allowableValues="free,select,reject")
	private String jumpType;
	
	@ApiModelProperty(name="nodeUsers",notes="节点用户，以base64加密[{nodeId:\"userTask1\",executors:[{id:\"\",name:\"\"},..]}]后的数据")
	private String nodeUsers;
	
	@ApiModelProperty(name="endReason",notes="终止原因")
	private String endReason;
	
	@ApiModelProperty(name="messageType",notes="消息通知类型 非必填，默认邮件，inner（内部消息），mail（邮件），sms（短信），多个之单使用英文逗号隔开",allowableValues="mail,inner,sms")
	private String messageType;
	
	@ApiModelProperty(name="destination",notes="跳转的目标节点，传入节点id")
	private String destination;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getDefId() {
		return defId;
	}

	public void setDefId(String defId) {
		this.defId = defId;
	}

	public String getFlowKey() {
		return flowKey;
	}

	public void setFlowKey(String flowKey) {
		this.flowKey = flowKey;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getProcInstId() {
		return procInstId;
	}

	public void setProcInstId(String procInstId) {
		this.procInstId = procInstId;
	}

	public Map<String, String> getVars() {
		return vars;
	}

	public void setVars(Map<String, String> vars) {
		this.vars = vars;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
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

	public boolean getDirectHandlerSign() {
		return directHandlerSign;
	}

	public void setDirectHandlerSign(boolean directHandlerSign) {
		this.directHandlerSign = directHandlerSign;
	}

	public String getBackHandMode() {
		return backHandMode;
	}

	public void setBackHandMode(String backHandMode) {
		this.backHandMode = backHandMode;
	}

	public String getJumpType() {
		return jumpType;
	}

	public void setJumpType(String jumpType) {
		this.jumpType = jumpType;
	}

	public String getNodeUsers() {
		return nodeUsers;
	}

	public void setNodeUsers(String nodeUsers) {
		this.nodeUsers = nodeUsers;
	}

	public String getEndReason() {
		return endReason;
	}

	public void setEndReason(String endReason) {
		this.endReason = endReason;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public String getAddSignTaskUserId() {
		return addSignTaskUserId;
	}

	public void setAddSignTaskUserId(String addSignTaskUserId) {
		this.addSignTaskUserId = addSignTaskUserId;
	}

}
