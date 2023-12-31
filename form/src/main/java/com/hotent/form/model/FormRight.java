package com.hotent.form.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 表单权限 实体对象
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月7日
 */
@ApiModel("表单权限 实体对象")
@TableName("form_right")
@XmlAccessorType(XmlAccessType.FIELD)
public class FormRight extends BaseModel<FormRight>{
	private static final long serialVersionUID = 1L;

	/**
	* 主键
	*/
	@ApiModelProperty(name="id", notes="主键")
	@TableId("id_")
	protected String id; 
	
	@XmlAttribute(name = "formKey")
	@ApiModelProperty(name="formKey", notes="表单KEY")
	@TableField("form_key_")
	protected String formKey; 
	
	/**
	* 流程key
	*/
	@XmlAttribute(name = "flowKey")
	@ApiModelProperty(name="flowKey", notes="流程key")
	@TableField("flow_key_")
	protected String flowKey; 
	
	/**
	* 节点ID
	*/
	@XmlAttribute(name = "nodeId")
	@ApiModelProperty(name="nodeId", notes="节点ID")
	@TableField("node_id_")
	protected String nodeId; 
	
	/**
	* 父流程定义
	*/
	@XmlAttribute(name = "parentFlowKey")
	@ApiModelProperty(name="parentFlowKey", notes="父流程定义")
	@TableField("parent_flow_key_")
	protected String parentFlowKey; 
	
	/**
	* 权限字段
	*/
	@XmlElement(name = "permission")
	@ApiModelProperty(name="permission", notes="权限字段")
	@TableField("permission_")
	protected String permission; 
	
	/**
	 * 权限类型 
	 */
	@XmlAttribute(name = "permissionType")
	@ApiModelProperty(name="permissionType", notes="权限类型(1:流程权限2:实例权限)", allowableValues="1,2")
	@TableField("permission_type_")
	protected int permissionType=0;

    @XmlElement(name = "isCheckOpinion")
    @ApiModelProperty(name="isCheckOpinion", notes="是否隐藏审批记录")
    @TableField("is_check_opinion")
    protected String isCheckOpinion;//是否隐藏审批记录

    public String getIsCheckOpinion() {
        return isCheckOpinion;
    }

    public void setIsCheckOpinion(String isCheckOpinion) {
        this.isCheckOpinion = isCheckOpinion;
    }
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 主键
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}
	
	/**
	 * 返回 form_key_
	 * @return
	 */
	public String getFormKey() {
		return this.formKey;
	}
	
	public void setFlowKey(String flowKey) {
		this.flowKey = flowKey;
	}
	
	/**
	 * 返回 流程key
	 * @return
	 */
	public String getFlowKey() {
		return this.flowKey;
	}
	
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	
	/**
	 * 返回 节点ID
	 * @return
	 */
	public String getNodeId() {
		return this.nodeId;
	}
	
	public void setParentFlowKey(String parentFlowKey) {
		this.parentFlowKey = parentFlowKey;
	}
	
	/**
	 * 返回 父流程定义
	 * @return
	 */
	public String getParentFlowKey() {
		return this.parentFlowKey;
	}
	
	public void setPermission(String permission) {
		this.permission = permission;
	}
	
	/**
	 * 返回 权限字段
	 * @return
	 */
	public String getPermission() {
		return this.permission;
	}
	public int getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(int permissionType) {
		this.permissionType = permissionType;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("formKey", this.formKey) 
		.append("flowKey", this.flowKey) 
		.append("nodeId", this.nodeId) 
		.append("parentFlowKey", this.parentFlowKey) 
		.append("permission", this.permission) 
		.toString();
	}
}