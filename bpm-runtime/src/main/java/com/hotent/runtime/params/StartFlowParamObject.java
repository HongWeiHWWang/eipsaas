package com.hotent.runtime.params;

import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 流程启动参数
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(value="流程启动参数")
public class StartFlowParamObject {
	
	@ApiModelProperty(name="account",notes="发起人帐号",example="admin")
	private String account;
	
	@ApiModelProperty(name="defId",notes="流程定义id，流程定义id与流程key必填其中一个")
	private String defId;
	
	@ApiModelProperty(name="flowKey",notes="流程key，流程定义id与流程key必填其中一个")
	private String flowKey;
	
	@ApiModelProperty(name="subject",notes="流程标题，不填则按流程定义中设置的标题规则生成")
	private String subject;
	
	@ApiModelProperty(name="proInstId",notes="流程实例id")
	private String proInstId;
	
	@ApiModelProperty(name="vars",notes="流程变量，变量名：变量值，如{\"var1\":\"val1\",\"var2\":\"val2\"...}")
	private Map<String,String> vars;
	
	@ApiModelProperty(name="data",notes="bo业务数据，以base64加密后的密文")
	private String data;
	
	@ApiModelProperty(name="businessKey",notes="业务主键KEY，只对URL表单形式有效")
	private String businessKey;
	
	@ApiModelProperty(name="sysCode",notes="业务系统编码，只对URL表单形式有效")
	private String sysCode;
	
	@ApiModelProperty(name="formType",notes="表单类型（inner,frame）")
	private String formType;
	
	@ApiModelProperty(name="nodeUsers",notes="下一节点人员")
	private String nodeUsers;
	
	@ApiModelProperty(name="isSendNodeUsers",notes="是否自由选择人员作为下一节点执行人")
	private int isSendNodeUsers;
	
	@ApiModelProperty(name="destination",notes="跳转目标节点")
	private String destination;
	
    @ApiModelProperty(name="expression",notes="审批意见")
    private String expression;
    
    @ApiModelProperty(name="startOrgId",notes="发起人组织id")
    private String startOrgId;
    
    @ApiModelProperty(name="urgentStateValue",notes="紧急状态的值")
    private ObjectNode urgentStateValue;
    
    @ApiModelProperty(name="agentLeaderId",notes="被代理的领导id")
    private String agentLeaderId;

    @ApiModelProperty(name="isApproval",notes="是否从待办审批页面点的保存")
    private Boolean isApproval = false;
    /**
     * 支持手机表单。
     */
    @ApiModelProperty(name="supportMobile",notes="是否支持手机表单 0：否，1：是")
    protected int supportMobile=0;
    
    @ApiModelProperty(name="taskId",notes="任务id")
    private String taskId;

    public int getSupportMobile() {
        return supportMobile;
    }

    public void setSupportMobile(int supportMobile) {
        this.supportMobile = supportMobile;
    }

    public Boolean getApproval() {
        return isApproval;
    }

    public void setApproval(Boolean approval) {
        isApproval = approval;
    }

    public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public int getIsSendNodeUsers() {
		return isSendNodeUsers;
	}

	public void setIsSendNodeUsers(int isSendNodeUsers) {
		this.isSendNodeUsers = isSendNodeUsers;
	}

	public String getNodeUsers() {
		return nodeUsers;
	}

	public void setNodeUsers(String nodeUsers) {
		this.nodeUsers = nodeUsers;
	}

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

	public String getProInstId() {
		return proInstId;
	}

	public void setProInstId(String proInstId) {
		this.proInstId = proInstId;
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

	public String getSysCode() {
		return sysCode;
	}

	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}

	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getStartOrgId() {
		return startOrgId;
	}

	public void setStartOrgId(String startOrgId) {
		this.startOrgId = startOrgId;
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

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	
}
