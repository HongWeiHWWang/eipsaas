package com.hotent.form.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.hotent.form.model.FormDataTemplate;

/**
 * 表单预览参数
 * @company 广州宏天软件股份有限公司
 * @author:zhangxw
 * @date:2019年7月24日
 */
@ApiModel(description="业务数据模板相关信息")
public class BpmDataTemplateInfoVo extends FormDataTemplate{
	
	@ApiModelProperty(name="formId", notes="表单id")
	private String formId;
	@ApiModelProperty(name="pkField", notes="业务主键字段")
	private String pkField;

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getPkField() {
		return pkField;
	}

	public void setPkField(String pkField) {
		this.pkField = pkField;
	}
	
}
