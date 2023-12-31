package com.hotent.bpmModel.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 节点配置保存对象
 * 
 * @company 广州宏天软件有限公司
 * @author wanghb
 * @email wanghb@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(description = "节点配置保存对象")
public class NodeConfSaveVo {

	@ApiModelProperty(name = "nodeId", notes = "节点id", required = true)
	protected String nodeId;

	@ApiModelProperty(name = "nodeJson", notes = "节点json")
	protected String nodeJson;

	@ApiModelProperty(name = "defId", notes = "流程定义id", required = true)
	protected String defId;

	@ApiModelProperty(name = "parentFlowKey", notes = "父流程定义id")
	protected String parentFlowKey;

	@ApiModelProperty(name = "eventScriptArray", notes = "事件脚本数组")
	protected String eventScriptArray;

	@ApiModelProperty(name = "condition", notes = "节点分支条件字符串")
	protected String condition;

	@ApiModelProperty(name = "jsonStr", notes = "节点自动任务字符串")
	protected String jsonStr;

	@ApiModelProperty(name = "signRule", notes = "节点会签规则字符串")
	protected String signRule;

	@ApiModelProperty(name = "privilegeList", notes = "节点特权字符串")
	protected String privilegeList;

	public String getEventScriptArray() {
		return eventScriptArray;
	}

	public void setEventScriptArray(String eventScriptArray) {
		this.eventScriptArray = eventScriptArray;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getJsonStr() {
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}

	public String getSignRule() {
		return signRule;
	}

	public void setSignRule(String signRule) {
		this.signRule = signRule;
	}

	public String getPrivilegeList() {
		return privilegeList;
	}

	public void setPrivilegeList(String privilegeList) {
		this.privilegeList = privilegeList;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodeJson() {
		return nodeJson;
	}

	public void setNodeJson(String nodeJson) {
		this.nodeJson = nodeJson;
	}

	public String getDefId() {
		return defId;
	}

	public void setDefId(String defId) {
		this.defId = defId;
	}

	public String getParentFlowKey() {
		return parentFlowKey;
	}

	public void setParentFlowKey(String parentFlowKey) {
		this.parentFlowKey = parentFlowKey;
	}

}