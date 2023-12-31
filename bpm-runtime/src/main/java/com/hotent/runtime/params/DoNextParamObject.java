package com.hotent.runtime.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 流程向下执行参数
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(value="流程向下执行参数")
@JsonInclude(Include.NON_NULL)
public class DoNextParamObject {
	
	@ApiModelProperty(name="account",notes="审批人帐号",required=true,example="admin")
	private String account;
	
	@ApiModelProperty(name="taskId",notes="任务id",required=true)
	private String taskId;
	
	@ApiModelProperty(name="vars",notes="流程变量，变量名：变量值，如{\"var1\":\"val1\",\"var2\":\"val2\"...}")
	private Map<String,String> vars;
	
	@ApiModelProperty(name="actionName",required=true,
			notes="审批动作,agree（审批）abandon（弃权）oppose（反对）agreeTrans（同意流转）opposeTrans（反对流转）commu（沟通反馈）reject（驳回）backToStart（驳回指定节点）"
			,allowableValues="agree,abandon,oppose,agreeTrans,opposeTrans,commu,reject,backToStart")
	private String actionName;
	
	@ApiModelProperty(name="opinion",notes="意见")
	private String opinion;
	
	@ApiModelProperty(name="data",notes="bo业务数据，以base64加密后的密文")
	private String data;
	
	@ApiModelProperty(name="directHandlerSign",notes="会签时是否直接审批通过",allowableValues="true,false")
	private boolean directHandlerSign;
	
	@ApiModelProperty(name="backHandMode",notes="驳回模式 ,direct :直来直往,normal: 按照流程图方式驳回",allowableValues="direct,normal")
	private String backHandMode = "normal";
	
	@ApiModelProperty(name="jumpType",notes=" 跳转方式 free : 自由跳转 ,select : 选择跳转,reject :驳回",allowableValues="free,select,reject")
	private String jumpType;
	
	@ApiModelProperty(name="nodeUsers",notes="节点用户，以base64加密[{nodeId:\"userTask1\",executors:[{id:\"\",name:\"\"},..]}]后的数据")
	private String nodeUsers;
	
	@ApiModelProperty(name="destination",notes="跳转的目标节点，传入节点id")
	private String destination;
	
	@ApiModelProperty(name="formType",notes="表单类型：inner/frame")
	private String formType;
	
	@ApiModelProperty(name="usersMap",notes="任务处理页面提交时的任务、人员map数据")
	private ObjectNode usersMap;
	
	@ApiModelProperty(name="notifyType",notes="消息同时类型(voice: '语音', mail: '邮件', sms: '短信', inner: '站内消息')，多个用“,”号隔开")
	private String notifyType;
	
	@ApiModelProperty(name="files",notes="附件信息，多个用“,”号隔开\"")
	private String files = "";

    @ApiModelProperty(name="zfiles",notes="正文附件信息，只能单个")
    private String zfiles = "";

	@ApiModelProperty(name="interPoseOpinion",notes="干预原因")
	private String interPoseOpinion;
    @ApiModelProperty(name="instId",notes="实例id",required=true)
    private String instId;
    
    @ApiModelProperty(name="urgentStateValue",notes="紧急状态的值")
    private ObjectNode urgentStateValue;
    
    @ApiModelProperty(name="agentLeaderId",notes="被代理的领导id")
    private String agentLeaderId;

    //普通用户任务加签审批动作 agreeTrans（同意流转）opposeTrans（反对流转）
    private String addSignAction;

    //普通用户任务加签后任务ID的父任务ID
    private String rejectTaskId;

    public String getRejectTaskId() {
        return rejectTaskId;
    }

    public void setRejectTaskId(String rejectTaskId) {
        this.rejectTaskId = rejectTaskId;
    }

    public String getAddSignAction() {
        return addSignAction;
    }

    public void setAddSignAction(String addSignAction) {
        this.addSignAction = addSignAction;
    }

    public String getZfiles() {
        return zfiles;
    }

    public void setZfiles(String zfiles) {
        this.zfiles = zfiles;
    }

    public String getInstId() {
        return instId;
    }

    public void setInstId(String instId) {
        this.instId = instId;
    }

    public String getFiles() {
		return files;
	}

	public void setFiles(String files) {
		this.files = files;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Map<String, String> getVars() {
		return vars;
	}

	public void setVars(Map<String, String> vars) {
		this.vars = vars;
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

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
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

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}

	public ObjectNode getUsersMap() {
		return usersMap;
	}

	public void setUsersMap(ObjectNode usersMap) {
		this.usersMap = usersMap;
	}

	public String getNotifyType() {
		return notifyType;
	}

	public void setNotifyType(String notifyType) {
		this.notifyType = notifyType;
	}

	public String getInterPoseOpinion() {
		return interPoseOpinion;
	}

	public void setInterPoseOpinion(String interPoseOpinion) {
		this.interPoseOpinion = interPoseOpinion;
	}

	public ObjectNode getUrgentStateValue() {
		return urgentStateValue;
	}

	public void setUrgentStateValue(ObjectNode urgentStateValue) {
		this.urgentStateValue = urgentStateValue;
	}

	public String getAgentLeaderId() {
		return agentLeaderId;
	}

	public void setAgentLeaderId(String agentLeaderId) {
		this.agentLeaderId = agentLeaderId;
	}
	
}
