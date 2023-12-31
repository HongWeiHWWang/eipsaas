package com.hotent.bpm.plugin.usercalc.customQuery.def;

import com.hotent.bpm.plugin.core.plugindef.AbstractUserCalcPluginDef;

/**
 * 关联数据作为流程审批人的定义
 *
 * @company 广州宏天软件股份有限公司
 * @author zhangxw
 * @email zhangxw@jee-soft.cn
 * @date 2020年3月25日
 */

public class CustomQueryPluginDef extends AbstractUserCalcPluginDef {
	private static final long serialVersionUID = -4275133246687703477L;
	/**
	 * 关联查询别名
	 */
	private String alias;
	/**
	 * 关联查询名称
	 */
	private String name;
	
	/**
	 * 关联查询描述
	 */
	private String description;
	
	/**
	 * 关联查询取值字段
	 */
	private String valueField;
	
	/**
	 * 关联参数
	 */
	private String params = "";
	
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getValueField() {
		return valueField;
	}
	public void setValueField(String valueField) {
		this.valueField = valueField;
	}
	
}
