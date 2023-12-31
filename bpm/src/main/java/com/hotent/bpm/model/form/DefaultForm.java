package com.hotent.bpm.model.form;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;

import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.model.form.FormType;



public class DefaultForm  implements Form ,Serializable{

	private static final long serialVersionUID = 1L;
	
	private String nodeId;
	private String parentFlowKey;
	private String name;
	private FormCategory type;
	private String formValue;
	@XmlAttribute(name = "formType")
	private String formType=FormType.PC.value();
	
	private String formExtraConf;
	
	private String helpFile;
	
	public String getNodeId() {
		return nodeId;
	}
	
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	
	public String getParentFlowKey() {
		if(StringUtil.isEmpty(parentFlowKey)){
			return Form.LOCAL;
		}
		return parentFlowKey;
	}
	
	public void setParentFlowKey(String parentFlowKey) {
		this.parentFlowKey = parentFlowKey;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public FormCategory getType() {
		return type;
	}
	
	public void setType(FormCategory type) {
		this.type = type;
	}
	
	public String getFormValue() {
		return formValue;
	}
	
	public void setFormValue(String formValue) {
		this.formValue = formValue;
	}
	
	
	public void setId(String id){
	}

	public String getId(){
		return "";
	}

	@Override
	public void setFormType(String formType) {
		this.formType=formType;
	}

	@Override
	public String getFormType() {
		return this.formType;
	}

	@Override
	public boolean isFormEmpty() {
		boolean isEmpty=StringUtil.isEmpty(formValue);
		return isEmpty;
	}

	public String getFormExtraConf() {
		return formExtraConf;
	}

	public void setFormExtraConf(String formExtraConf) {
		this.formExtraConf = formExtraConf;
	}

	public String getHelpFile() {
		return helpFile;
	}

	public void setHelpFile(String helpFile) {
		this.helpFile = helpFile;
	}

}
