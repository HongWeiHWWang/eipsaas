package com.hotent.form.vo;

import com.hotent.form.model.Form;
import com.hotent.form.model.FormMeta;

public class BpmFormVo {
	
	private FormMeta bpmFormDef;
	private Form bpmForm;
	
	private String newForm = "";
	
	public FormMeta getBpmFormDef() {
		return bpmFormDef;
	}
	public void setBpmFormDef(FormMeta bpmFormDef) {
		this.bpmFormDef = bpmFormDef;
	}
	public Form getBpmForm() {
		return bpmForm;
	}
	public void setBpmForm(Form bpmForm) {
		this.bpmForm = bpmForm;
	}
	public String getNewForm() {
		return newForm;
	}
	public void setNewForm(String newForm) {
		this.newForm = newForm;
	}
	
	

}
