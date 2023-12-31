package com.hotent.bpmModel.params;

import com.hotent.bpm.model.var.DefaultBpmVariableDef;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 流程变量请求对象
 * 
 * @company 广州宏天软件有限公司
 * @author wanghb
 * @email wanghb@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(description = "流程变量请求对象")
public class BpmVariableDefVo {

	@ApiModelProperty(name = "variableDef", notes = "节点规则对象", required = true)
	protected DefaultBpmVariableDef variableDef;

	@ApiModelProperty(name = "isAdd", notes = "是否新增", required = true)
	protected Boolean isAdd;

	@ApiModelProperty(name = "defId", notes = "流程定义id", required = true)
	protected String defId;

	@ApiModelProperty(name = "isRequired", notes = "是否必须", required = true)
	protected Boolean isRequired;

	public DefaultBpmVariableDef getVariableDef() {
		return variableDef;
	}

	public void setVariableDef(DefaultBpmVariableDef variableDef) {
		this.variableDef = variableDef;
	}

	public Boolean getIsAdd() {
		return isAdd;
	}

	public void setIsAdd(Boolean isAdd) {
		this.isAdd = isAdd;
	}

	public String getDefId() {
		return defId;
	}

	public void setDefId(String defId) {
		this.defId = defId;
	}

	public Boolean getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(Boolean isRequired) {
		this.isRequired = isRequired;
	}

}