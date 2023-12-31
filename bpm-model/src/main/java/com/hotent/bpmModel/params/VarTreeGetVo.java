package com.hotent.bpmModel.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 变量查询对象
 * 
 * @company 广州宏天软件有限公司
 * @author wanghb
 * @email wanghb@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(description = "变量查询对象")
public class VarTreeGetVo {

	@ApiModelProperty(name = "nodeId", notes = "节点id")
	protected String nodeId;

	@ApiModelProperty(name = "defId", notes = "流程定义id", required = true)
	protected String defId;

	@ApiModelProperty(name = "parentFlowKey", notes = "父流程定义id")
	protected String parentFlowKey;

	@ApiModelProperty(name = "flowKey", notes = "流程定义key")
	protected String flowKey;

	@ApiModelProperty(name = "removeSub", notes = "是否剔除子表")
	protected boolean removeSub;

	@ApiModelProperty(name = "removeMain", notes = "是否剔除主表")
	protected boolean removeMain;

	@ApiModelProperty(name = "includeBpmConstants", notes = "是否包含流程变量")
	protected boolean includeBpmConstants;

    @ApiModelProperty(name = "bpmForm", notes = "是否包含表单变量")
    protected boolean bpmForm = true;

    @ApiModelProperty(name = "urgent", notes = "是否包含催办的催办人和被催办人")
    protected boolean urgent = false;

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public boolean isBpmForm() {
        return bpmForm;
    }

    public void setBpmForm(boolean bpmForm) {
        this.bpmForm = bpmForm;
    }

    public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
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

	public String getFlowKey() {
		return flowKey;
	}

	public void setFlowKey(String flowKey) {
		this.flowKey = flowKey;
	}

	public boolean getRemoveSub() {
		return removeSub;
	}

	public void setRemoveSub(boolean removeSub) {
		this.removeSub = removeSub;
	}

	public boolean getRemoveMain() {
		return removeMain;
	}

	public void setRemoveMain(boolean removeMain) {
		this.removeMain = removeMain;
	}

	public boolean getIncludeBpmConstants() {
		return includeBpmConstants;
	}

	public void setIncludeBpmConstants(boolean includeBpmConstants) {
		this.includeBpmConstants = includeBpmConstants;
	}

}