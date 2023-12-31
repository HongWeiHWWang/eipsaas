package com.hotent.job.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 任务参数对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月7日
 */
@ApiModel(description="任务参数对象")
public class ParameterObj {
	public static final String TYPE_INT = "int";
	public static final String TYPE_LONG = "long";
	public static final String TYPE_FLOAT = "float";
	public static final String TYPE_BOOLEAN = "boolean";

	@ApiModelProperty(name="type", notes="参数数据类型(int long float boolean)")
	private String type = "";

	@ApiModelProperty(name="name", notes="参数名称")
	private String name = "";

	@ApiModelProperty(name="value", notes="参数值")
	private String value = "";
	
	/**
	 * 返回参数类型
	 * @return
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * 设置参数类型
	 * @param type 类型
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * 返回参数名称
	 * @return 名称
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 设置参数名称
	 * @param name 名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 返回参数值
	 * @return
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * ]设置参数值
	 * @param value 参数值
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
