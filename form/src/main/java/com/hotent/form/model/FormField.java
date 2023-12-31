/**
 * 
 */
package com.hotent.form.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户任务表单字段信息表 entity对象
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月6日
 */
@ApiModel("表单字段信息")
@TableName("form_field")
public class FormField extends BaseModel<FormField> {
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(name="id", notes="主键")
	@TableId("id_")
	protected String id;
	
	@ApiModelProperty(name="name", notes="字段名称")
	@TableField("name_")
	protected String name;
	
	@ApiModelProperty(name="desc", notes="字段描述")
	@TableField("desc_")
	protected String desc;
	
	@ApiModelProperty(name="type", notes="字段的类型")
	@TableField("type_")
	protected String type;
	
	@ApiModelProperty(name="groupId", notes="分组ID")
	@TableField("group_id_")
	protected String groupId = "0";
	
	@ApiModelProperty(name="formId", notes="表单ID")
	@TableField("form_id_")
	protected String formId;
	
	@ApiModelProperty(name="boDefId", notes="bo定义ID")
	@TableField("bo_def_id_")
	protected String boDefId;
	
	@ApiModelProperty(name="entId", notes="表ID")
	@TableField("ent_id_")
	protected String entId;
	
	@ApiModelProperty(name="boAttrId", notes="BO属性ID")
	@TableField("bo_attr_id_")
	protected String boAttrId;
	
	@ApiModelProperty(name="calculation", notes="运算表达式")
	@TableField("calculation_")
	protected String calculation;
	
	@ApiModelProperty(name="ctrlType", notes="控件类型")
	@TableField("ctrl_type_")
	protected String ctrlType;
	
	@ApiModelProperty(name="validRule", notes="验证规则")
	@TableField("valid_rule_")
	protected String validRule; 
	
	@ApiModelProperty(name="option", notes="表单配置选项")
	@TableField("option_")
	protected String option;
	
	@ApiModelProperty(name="sn", notes="排序")
	@TableField("sn_")
	protected Integer sn;

	@ApiModelProperty(name="status", notes = "状态")
	@TableField(exist = false)
	protected String status;
	
	@ApiModelProperty(name="showFlowField", notes="是否显示流程字段")
	@TableField(exist=false)
	protected boolean showFlowField=false;

	@ApiModelProperty(name="showFlowField", notes="映射对应的实体名称")
	@TableField(exist=false)
	protected String entName="";

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the groupId
	 */
	public String getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return the formId
	 */
	public String getFormId() {
		return formId;
	}

	/**
	 * @param formId the formId to set
	 */
	public void setFormId(String formId) {
		this.formId = formId;
	}

	/**
	 * @return the boDefId
	 */
	public String getBoDefId() {
		return boDefId;
	}

	/**
	 * @param boDefId the boDefId to set
	 */
	public void setBoDefId(String boDefId) {
		this.boDefId = boDefId;
	}

	/**
	 * @return the entId
	 */
	public String getEntId() {
		return entId;
	}

	/**
	 * @param entId the entId to set
	 */
	public void setEntId(String entId) {
		this.entId = entId;
	}

	/**
	 * @return the boAttrId
	 */
	public String getBoAttrId() {
		return boAttrId;
	}

	/**
	 * @param boAttrId the boAttrId to set
	 */
	public void setBoAttrId(String boAttrId) {
		this.boAttrId = boAttrId;
	}

	/**
	 * @return the calculation
	 */
	public String getCalculation() {
		return calculation;
	}

	/**
	 * @param calculation the calculation to set
	 */
	public void setCalculation(String calculation) {
		this.calculation = calculation;
	}

	/**
	 * @return the ctrlType
	 */
	public String getCtrlType() {
		return ctrlType;
	}

	/**
	 * @param ctrlType the ctrlType to set
	 */
	public void setCtrlType(String ctrlType) {
		this.ctrlType = ctrlType;
	}

	/**
	 * @return the validRule
	 */
	public String getValidRule() {
		return validRule;
	}

	/**
	 * @param validRule the validRule to set
	 */
	public void setValidRule(String validRule) {
		this.validRule = validRule;
	}

	/**
	 * @return the option
	 */
	public String getOption() {
		return option;
	}

	/**
	 * @param option the option to set
	 */
	public void setOption(String option) {
		this.option = option;
	}

	/**
	 * @return the sn
	 */
	public Integer getSn() {
		return sn;
	}

	/**
	 * @param sn the sn to set
	 */
	public void setSn(Integer sn) {
		this.sn = sn;
	}

	/**
	 * @return the entName
	 */
	public String getEntName() {
		return entName;
	}

	/**
	 * @param entName the entName to set
	 */
	public void setEntName(String entName) {
		this.entName = entName;
	}

	public boolean isShowFlowField() {
		return showFlowField;
	}

	public void setShowFlowField(boolean showFlowField) {
		this.showFlowField = showFlowField;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append("id", this.id)
				.append("name", this.name)
				.append("desc", this.desc)
				.append("type", this.type)
				.append("groupId", this.groupId)
				.append("formId", this.formId)
				.append("boDefId", this.boDefId)
				.append("entId", this.entId)
				.append("boAttrId", this.boAttrId)
				.append("calculation", this.calculation)
				.append("ctrlType", this.ctrlType)
				.append("validRule", this.validRule)
				.append("option", this.option)
				.append("sn", this.sn)
				.toString();
	}
}
