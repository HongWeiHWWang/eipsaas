package com.hotent.form.vo;

public class FormRigthParam {

	
	protected String flowKey;//父流程定义key
	
	protected String formKey;//表单key
	
	protected String nodeId;//节点id
	
	protected int type=1;//类型
	
	protected String parentflowKey;//父流程定义
	
	protected String permission;//权限字符串

    protected String isCheckOpinion;//是否隐藏审批记录

    public String getIsCheckOpinion() {
        return isCheckOpinion;
    }

    public void setIsCheckOpinion(String isCheckOpinion) {
        this.isCheckOpinion = isCheckOpinion;
    }

    public String getFlowKey() {
		return flowKey;
	}

	public void setFlowKey(String flowKey) {
		this.flowKey = flowKey;
	}

	public String getFormKey() {
		return formKey;
	}

	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getParentflowKey() {
		return parentflowKey;
	}

	public void setParentflowKey(String parentflowKey) {
		this.parentflowKey = parentflowKey;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	
	
}
