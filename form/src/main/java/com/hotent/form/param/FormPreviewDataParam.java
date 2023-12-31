package com.hotent.form.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 表单预览参数
 * @company 广州宏天软件股份有限公司
 * @author:liyg
 * @date:2018年7月18日
 */
@ApiModel(description="表单预览参数")
public class FormPreviewDataParam {
	
	@ApiModelProperty(name="id", notes="表单id")
	private String id;
	
	@ApiModelProperty(name="design", notes="设计源码")
	private String design;

	@ApiModelProperty(name="bos", notes="业务对象 [{}]")
	private String bos;
	
	@ApiModelProperty(name="formType", notes="表单类型 pc/mobile")
	private String formType;
	
	@ApiModelProperty(name="form", notes="表单信息")
	private String form;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDesign() {
		return design;
	}

	public void setDesign(String design) {
		this.design = design;
	}

	public String getBos() {
		return bos;
	}

	public void setBos(String bos) {
		this.bos = bos;
	}
	
	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}
	
}
