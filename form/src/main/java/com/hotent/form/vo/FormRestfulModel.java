package com.hotent.form.vo;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("表单服务")
public class FormRestfulModel {
	@ApiModelProperty(name="boid",notes="boid")
	protected String boid;
	@ApiModelProperty(name="defId",notes="defId")
	protected String defId;
	@ApiModelProperty(name="boData",notes="boDataJson数据")
	protected ObjectNode boData;
	@ApiModelProperty(name="saveType",notes="保存方式")
	protected String saveType;
	@ApiModelProperty(name="bocode",notes="bo别名")
	protected String code;
	@ApiModelProperty(name="formkey",notes="表单key")
	protected String formkey;
	@ApiModelProperty(name="userId",notes="用户ID")
	protected String userId;
	@ApiModelProperty(name="flowKey",notes="流程key")
	protected String flowKey;
    @ApiModelProperty(name="flowDefId",notes="流程定义id")
    protected String flowDefId;
	@ApiModelProperty(name="nodeId",notes="节点ID")
	protected String nodeId;
	@ApiModelProperty(name="nextNodeId",notes="下一个节点ID")
	protected String nextNodeId;
	@ApiModelProperty(name="parentFlowKey",notes="parentFlowKey")
	protected String parentFlowKey;
	@ApiModelProperty(name="permissionType",notes="权限类型")
	protected int permissionType;
	@ApiModelProperty(name="isGlobalPermission",notes="是否获取全局表单权限")
	protected boolean isGlobalPermission;
	

	public int getPermissionType() {
		return permissionType;
	}
	public void setPermissionType(int permissionType) {
		this.permissionType = permissionType;
	}
	public String getParentFlowKey() {
		return parentFlowKey;
	}
	public void setParentFlowKey(String parentFlowKey) {
		this.parentFlowKey = parentFlowKey;
	}
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public String getNextNodeId() {
		return nextNodeId;
	}
	public void setNextNodeId(String nextNodeId) {
		this.nextNodeId = nextNodeId;
	}
	public String getFormkey() {
		return formkey;
	}
	public void setFormkey(String formkey) {
		this.formkey = formkey;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFlowKey() {
		return flowKey;
	}
	public void setFlowKey(String flowKey) {
		this.flowKey = flowKey;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getBoid() {
		return boid;
	}
	public void setBoid(String boid) {
		this.boid = boid;
	}
	public String getDefId() {
		return defId;
	}
	public void setDefId(String defId) {
		this.defId = defId;
	}
	public ObjectNode getBoData() {
		return boData;
	}
	public void setBoData(ObjectNode boData) {
		this.boData = boData;
	}
	public String getSaveType() {
		return saveType;
	}
	public void setSaveType(String saveType) {
		this.saveType = saveType;
	}

    public String getFlowDefId() {
        return flowDefId;
    }

    public void setFlowDefId(String flowDefId) {
        this.flowDefId = flowDefId;
    }
	public boolean isGlobalPermission() {
		return isGlobalPermission;
	}
	public void setGlobalPermission(boolean isGlobalPermission) {
		this.isGlobalPermission = isGlobalPermission;
	}
    
}
