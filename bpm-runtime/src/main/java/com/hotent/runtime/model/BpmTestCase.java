package com.hotent.runtime.model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;


/**
 * 流程的测试用例设置 实体对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
@TableName("bpm_test_case")
@ApiModel(value="流程的测试用例设置 实体对象")
public class BpmTestCase extends BaseModel<BpmTestCase>{
	
	private static final long serialVersionUID = 1L;
	
	@XmlTransient
	@TableId("id_")
	@ApiModelProperty(name="id",notes="主键")
	protected String id; 
	
	@XmlAttribute(name = "name")
	@TableField("name_")
	@ApiModelProperty(name="name",notes="测试用例名称")
	protected String name; 
	
	@XmlAttribute(name = "name")
	@TableField("BO_FORM_DATA_")
	@ApiModelProperty(name="boFormData",notes="表单数据")
	protected String boFormData; 
	
	@XmlAttribute(name = "flowVars")
	@TableField("FLOW_VARS_")
	@ApiModelProperty(name="flowVars",notes="流程变量")
	protected String flowVars; 
	
	@XmlAttribute(name = "startor")
	@TableField("STARTOR_")
	@ApiModelProperty(name="startor",notes="发起人")
	protected String startor; 
	
	@XmlAttribute(name = "actionType")
	@TableField("ACTION_TYPE_")
	@ApiModelProperty(name="actionType",notes="审批动作， 默认动作时 agree")
	protected String actionType; 
	
	@XmlAttribute(name = "bpmDebugger")
	@TableField("BPM_DEBUGGER_")
	@ApiModelProperty(name="bpmDebugger",notes="断点测试设置")
	protected String bpmDebugger; 
	
	@XmlAttribute(name = "defKey")
	@TableField("DEF_KEY_")
	@ApiModelProperty(name="defKey",notes="流程定义key")
	protected String defKey; 
	
	@XmlAttribute(name = "startorAccount")
	@TableField("STARTOR_ACCOUNT_")
	@ApiModelProperty(name="startorAccount",notes="发起人账号")
	protected String startorAccount;
	
	@XmlAttribute(name = "startorFullName")
	@TableField("STARTOR_FULL_NAME_")
	@ApiModelProperty(name="startorFullName",notes="发起人名称")
	protected String startorFullName;
	
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 编号
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 返回 测试用例名称
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	public void setBoFormData(String boFormData) {
		this.boFormData = boFormData;
	}
	
	/**
	 * 返回 表单数据
	 * @return
	 */
	public String getBoFormData() {
		return this.boFormData;
	}
	
	public void setFlowVars(String flowVars) {
		this.flowVars = flowVars;
	}
	
	/**
	 * 返回 流程变量
	 * @return
	 */
	public String getFlowVars() {
		return this.flowVars;
	}
	
	public void setStartor(String startor) {
		this.startor = startor;
	}
	
	/**
	 * 返回 发起人
	 * @return
	 */
	public String getStartor() {
		return this.startor;
	}
	
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	
	/**
	 * 返回 审批动作， 默认动作时 agree
	 * @return
	 */
	public String getActionType() {
		return this.actionType;
	}
	
	public void setBpmDebugger(String bpmDebugger) {
		this.bpmDebugger = bpmDebugger;
	}
	
	/**
	 * 返回 断点测试设置
	 * @return
	 */
	public String getBpmDebugger() {
		return this.bpmDebugger;
	}
	
	public String getDefKey() {
		return defKey;
	}

	public void setDefKey(String defKey) {
		this.defKey = defKey;
	}
	
	public String getStartorAccount() {
		return startorAccount;
	}

	public void setStartorAccount(String startorAccount) {
		this.startorAccount = startorAccount;
	}

	public String getStartorFullName() {
		return startorFullName;
	}

	public void setStartorFullName(String startorFullName) {
		this.startorFullName = startorFullName;
	}
	
	
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("name", this.name) 
		.append("boFormData", this.boFormData) 
		.append("flowVars", this.flowVars) 
		.append("startor", this.startor) 
		.append("actionType", this.actionType) 
		.append("bpmDebugger", this.bpmDebugger) 
		.append("defKey", this.defKey) 
		.toString();
	}
}