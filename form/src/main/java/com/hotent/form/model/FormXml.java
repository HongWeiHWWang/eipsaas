package com.hotent.form.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * 此类用于表单的导入导出。
 *
 */
@XmlRootElement(name = "bpmForms")
@XmlAccessorType(XmlAccessType.FIELD)
public class FormXml {
	

	@XmlElement(name = "bpmForm", type = Form.class)
	private Form bpmForm;
	
	@XmlElement(name = "bpmFormDef", type = FormMeta.class)
	private FormMeta bpmFormDef;
	
	@XmlElement(name="boCodes")
	private List<String> boCodes;
	

	public List<String> getBoCodes() {
		return boCodes;
	}


	public void setBoCodes(List<String> boCodes){
		this.boCodes = boCodes;
	}


	public Form getBpmForm() {
		return bpmForm;
	}


	public void setBpmForm(Form bpmForm){
		this.bpmForm = bpmForm;
	}


	public FormMeta getBpmFormDef(){
		return bpmFormDef;
	}


	public void setBpmFormDef(FormMeta bpmFormDef){
		this.bpmFormDef = bpmFormDef;
	}
	

}
