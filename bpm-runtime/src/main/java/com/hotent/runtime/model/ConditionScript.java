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
 * 对象功能:sys_script entity对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:helh
 * 创建时间:2014-05-08 15:19:20
 */
@TableName("BPM_MULTI_SCRIPT")
@ApiModel(value = "ConditionScript",description = "人员脚本")
public class ConditionScript extends BaseModel<ConditionScript> implements Cloneable{

	private static final long serialVersionUID = 1L;
	@XmlTransient
	@TableId("id_")
	@ApiModelProperty(name="id",notes="主键")
	protected String  id;

	@XmlAttribute(name = "className")
	@TableField("CLASS_NAME_")
	@ApiModelProperty(name="className",notes="脚本所在类的类名")
	protected String  className;

	@XmlAttribute(name = "classInsName")
	@TableField("CLASS_INS_NAME_")
	@ApiModelProperty(name="classInsName",notes="类实例名")
	protected String  classInsName;

	@XmlAttribute(name = "methodName")
	@TableField("METHOD_NAME_")
	@ApiModelProperty(name="methodName",notes="方法名")
	protected String  methodName;

	@XmlAttribute(name = "methodDesc")
	@TableField("METHOD_DESC_")
	@ApiModelProperty(name="methodDesc",notes="方法描述")
	protected String  methodDesc;

	@XmlAttribute(name = "returnType")
	@TableField("RETURN_TYPE_")
	@ApiModelProperty(name="returnType",notes="返回值类型")
	protected String  returnType;
	/**
	 *  参数信息
	 * [
	 * 	{
	 * 		"paraName":"arg0",
	 * 		"paraType":"org.activiti.engine.impl.persistence.entity.TaskEntity",
	 * 		"paraDesc":"任务实体",
	 * 		"paraCt":"18"
	 *  }
	 * ]
	 */
	@XmlAttribute(name = "argument")
	@TableField("ARGUMENT_")
	@ApiModelProperty(name="argument",notes="参数信息")
	protected String  argument;		
	
	@XmlAttribute(name = "enable")
	@TableField("ENABLE_")
	@ApiModelProperty(name="enable",notes="是否有效 ，0：否 ， 1：是")
	protected Integer enable=1;	
	
	@XmlAttribute(name = "type")
	@TableField("TYPE_")
	@ApiModelProperty(name="type",notes="脚本类型，条件脚本 1，人员脚本 2")
	protected Integer type;
	 
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getClassInsName() {
		return classInsName;
	}
	public void setClassInsName(String classInsName) {
		this.classInsName = classInsName;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getMethodDesc() {
		return methodDesc;
	}
	public void setMethodDesc(String methodDesc) {
		this.methodDesc = methodDesc;
	}
	public String getReturnType() {
		return returnType;
	}
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	public String getArgument() {
		return argument;
	}
	public void setArgument(String argument) {
		this.argument = argument;
	}
	public Integer getEnable() {
		return enable;
	}
	public void setEnable(Integer enable) {
		this.enable = enable;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("className", this.className) 
		.append("classInsName", this.classInsName) 
		.append("methodName", this.methodName) 
		.append("methodDesc", this.methodDesc) 
		.append("returnType", this.returnType) 
		.append("argument", this.argument) 
		.append("enable", this.enable) 
		.toString();
	}
}
