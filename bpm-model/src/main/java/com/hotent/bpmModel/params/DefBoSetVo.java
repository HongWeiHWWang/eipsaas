package com.hotent.bpmModel.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 流程bo设置保存对象
 * 
 * @company 广州宏天软件有限公司
 * @author wanghb
 * @email wanghb@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(description = "流程bo设置保存对象")
public class DefBoSetVo {

	@ApiModelProperty(name = "json", notes = "流程bo设置的json字符串", required = true)
	protected String json;

	@ApiModelProperty(name = "topDefKey", notes = "是否发布")
	protected String topDefKey;

	@ApiModelProperty(name = "flowId", notes = "流程定义id", required = true)
	protected String flowId;

	@ApiModelProperty(name = "isClearForm", notes = "是否清除表单")
	protected Boolean isClearForm;

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getTopDefKey() {
		return topDefKey;
	}

	public void setTopDefKey(String topDefKey) {
		this.topDefKey = topDefKey;
	}

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public Boolean getIsClearForm() {
		return isClearForm;
	}

	public void setIsClearForm(Boolean isClearForm) {
		this.isClearForm = isClearForm;
	}

}