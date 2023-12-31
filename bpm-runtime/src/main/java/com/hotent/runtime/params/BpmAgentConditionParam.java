package com.hotent.runtime.params;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 流程代理条件对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(value="流程代理条件对象")
public class BpmAgentConditionParam {
	
	@ApiModelProperty(name="id",notes="条件对象id")
	protected String  id;
	
	@ApiModelProperty(name="settingId",notes="流程代理设置ID")
	protected String  settingId;
	
	@ApiModelProperty(name="conditionDesc",notes="条件描述")
	protected String  conditionDesc;
	
	@ApiModelProperty(name="condition",notes="条件规则，base64加密的密文",required=true)
	protected String  condition;
	
	@ApiModelProperty(name="agentId",notes="代理人ID",required=true)
	protected String  agentId;
	
	@ApiModelProperty(name="agentName",notes="代理人",required=true)
	protected String  agentName;

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

	public String getConditionDesc() {
		return conditionDesc;
	}

	public void setConditionDesc(String conditionDesc) {
		this.conditionDesc = conditionDesc;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	
	public String toString() {
//		return new ToStringBuilder(this)
//		.append("id", this.id) 
//		.append("settingId", this.settingId) 
//		.append("condition", this.condition) 
//		.append("conditionDesc", this.conditionDesc) 
//		.append("agentId", this.agentId) 
//		.append("agentName", this.agentName) 
//		.toString();
		return "{"
				+ "\""+"id"+"\""+":"+"\""+this.id+"\","
				+ "\""+"settingId"+"\""+":"+"\""+this.settingId+"\","
				+"\""+"condition"+"\""+":"+"\""+this.condition+"\","
				+"\""+"conditionDesc"+"\""+":"+"\""+this.conditionDesc+"\","
				+"\""+"agentId"+"\""+":"+"\""+this.agentId+"\","
				+"\""+"agentName"+"\""+":"+"\""+this.agentName+"\""
				+ "}";
	}

}
