package com.hotent.bpm.api.model.process.nodedef;

import com.hotent.bpm.model.form.Form;


/**
 * 节点和表单定义。
 * @author ray
 *
 */
public class BpmNodeForm {
	
	/**
	 * 节点定义
	 */
	private BpmNodeDef bpmNodeDef=null;
	
	/**
	 * 表单定义。
	 */
	private Form form=null;
	
	public BpmNodeForm() {
	}
	
	

	public BpmNodeForm(BpmNodeDef bpmNodeDef, Form form) {
		this.bpmNodeDef = bpmNodeDef;
		this.form = form;
	}



	public BpmNodeDef getBpmNodeDef() {
		return bpmNodeDef;
	}

	public void setBpmNodeDef(BpmNodeDef bpmNodeDef) {
		this.bpmNodeDef = bpmNodeDef;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}
	
	
	

	
	
}
