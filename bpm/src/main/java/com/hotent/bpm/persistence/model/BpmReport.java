package com.hotent.bpm.persistence.model;

/**
 * 报表查询结果集实体
 * @author tangxin
 *
 */
public class BpmReport {
	
	//名称
	protected String name;
	
	//值
	protected String value;
	

	public BpmReport() {
		
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
