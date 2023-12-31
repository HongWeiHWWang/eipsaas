package com.hotent.bpm.api.model.process.def;

import java.io.Serializable;

public class ExtProperty implements Serializable{
	private static final long serialVersionUID = 3548176703566804438L;
	private String name="";
	private String value="";
	
	public ExtProperty(){
		
	}
	
	
	public ExtProperty(String name,String value){
		this.name=name;
		this.value=value;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

}
