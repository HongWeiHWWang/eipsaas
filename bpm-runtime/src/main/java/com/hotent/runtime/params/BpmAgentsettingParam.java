package com.hotent.runtime.params;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 流程代理设置对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(value="流程代理设置对象")
public class BpmAgentsettingParam {
	
	@ApiModelProperty(name="id",notes="流程代理设置对象id")
	protected String id;
	
	@ApiModelProperty(name="subject",notes="标题 ",required=true)
	protected String subject;
	
	@ApiModelProperty(name="authId",notes="授权人ID",required=true)
	protected String authId;
	
	@ApiModelProperty(name="authName",notes="授权人姓名",required=true)
	protected String authName;
	
	@ApiModelProperty(name="startDate",notes="代理开始生效时间，日期格式yyyy-MM-dd",required=true)
	protected String startDate;
	
	@ApiModelProperty(name="endDate",notes="代理结束日期，日期格式yyyy-MM-dd",required=true)
	protected String endDate;
	
	@ApiModelProperty(name="isEnabled",notes="是否有效",allowableValues="Y,N",required=true)
	protected String isEnabled;
	
	@ApiModelProperty(name="agentId",notes="代理人ID，当为条件代理（type=3）时可不填，另外两种情况必填")
	protected String agentId;
	
	@ApiModelProperty(name="agent",notes="代理人，当为条件代理（type=3）时可不填，另外两种情况必填")
	protected String agent;
	
	@ApiModelProperty(name="flowKey",notes="流程定义KEY(条件代理有效type=3)")
	protected String flowKey;
	
	@ApiModelProperty(name="type",notes="代理类型(1,全权代理,2,部分代理,3.条件代理)",allowableValues="1,2,3",required=true)
	protected Short type;
	
	@ApiModelProperty(name="createBy",notes="创建人ID")
	protected String createBy;
		
	@ApiModelProperty(name="createOrgId",notes="创建者所属组织ID")
	protected String createOrgId;
	
	@ApiModelProperty(name="updateBy",notes="更新人ID")
	protected String updateBy;
	
	@ApiModelProperty(name="conditions",notes="流程代理条件对象，当为条件代理（type=3）时必填")
	protected List<BpmAgentConditionParam> conditions;
	
	@ApiModelProperty(name="bpmDefs",notes="代理指定流程对象，当为部分代理（type=2）时必填")
	protected List<BpmAgentDefParam> bpmDefs;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public String getAuthName() {
		return authName;
	}

	public void setAuthName(String authName) {
		this.authName = authName;
	}

	public String getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getFlowKey() {
		return flowKey;
	}

	public void setFlowKey(String flowKey) {
		this.flowKey = flowKey;
	}

	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getCreateOrgId() {
		return createOrgId;
	}

	public void setCreateOrgId(String createOrgId) {
		this.createOrgId = createOrgId;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public List<BpmAgentConditionParam> getConditions() {
		return conditions;
	}

	public void setConditions(List<BpmAgentConditionParam> conditions) {
		this.conditions = conditions;
	}

	public List<BpmAgentDefParam> getBpmDefs() {
		return bpmDefs;
	}

	public void setBpmDefs(List<BpmAgentDefParam> bpmDefs) {
		this.bpmDefs = bpmDefs;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String toString() {
//		return new ToStringBuilder(this)
//		.append("id", this.id) 
//		.append("subject", this.subject) 
//		.append("authName", this.authName) 
//		.append("authId", this.authId) 
//		.append("startDate", this.startDate) 
//		.append("endDate", this.endDate) 
//		.append("isEnabled", this.isEnabled) 
//		.append("agent", this.agent) 
//		.append("agentId", this.agentId) 
//		.append("flowKey", this.flowKey) 
//		.append("type", this.type) 
//		.append("createBy", this.createBy)
//		.append("createTime", this.createTime) 
//		.append("createOrgId", this.createOrgId) 
//		.append("updateBy", this.updateBy) 
//		.append("updateTime", this.updateTime) 
//		.toString();
		return "{"
		+ "\""+"id"+"\""+":"+"\""+this.id+"\","
		+ "\""+"subject"+"\""+":"+"\""+this.subject+"\","
		+"\""+"authName"+"\""+":"+"\""+this.authName+"\","
		+"\""+"authId"+"\""+":"+"\""+this.authId+"\","
		+"\""+"startDate"+"\""+":"+"\""+this.startDate+"\","
		+"\""+"endDate"+"\""+":"+"\""+this.endDate+"\","
		+"\""+"isEnabled"+"\""+":"+"\""+this.isEnabled+"\","
		+"\""+"agent"+"\""+":"+"\""+this.agent+"\","
		+"\""+"agentId"+"\""+":"+"\""+this.agentId+"\","
		+"\""+"flowKey"+"\""+":"+"\""+this.flowKey+"\","
		+"\""+"type"+"\""+":"+"\""+this.type+"\","
		+"\""+"createBy"+"\""+":"+"\""+this.createBy+"\","
		+"\""+"createOrgId"+"\""+":"+"\""+this.createOrgId+"\","
		+"\""+"updateBy"+"\""+":"+"\""+this.updateBy+"\","
		+ "}";
	}
	
}
