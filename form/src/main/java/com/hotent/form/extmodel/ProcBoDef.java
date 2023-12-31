package com.hotent.form.extmodel;


import java.io.Serializable;

/**
 * 流程的bo定义
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月13日
 */
public class ProcBoDef implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * BO的名称。
	 */
	private String name="";
	/**
	 * BO的KEY，在流程定义条件等地方使用。
	 */
	private String key="";
	
	/**
	 * 是否必须。
	 */
	private boolean isRequired=false;
	
	/**
	 * 父流程key。
	 */
	private String parentDefKey="local_";
	
	/**
	 * 实例数据保存模式。
	 */
	private String saveMode="database";
	
	
	public ProcBoDef(){}
	
	
	
	public ProcBoDef(String name,String key){
		this.name=name;
		this.key=key;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	

	public boolean isRequired() {
		return isRequired;
	}

	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}



	public String getParentDefKey() {
		return parentDefKey;
	}


	public void setParentDefKey(String parentDefKey) {
		this.parentDefKey = parentDefKey;
	}

	public String getSaveMode() {
		return saveMode;
	}

	public void setSaveMode(String saveMode) {
		this.saveMode = saveMode;
	}
	
	

}
