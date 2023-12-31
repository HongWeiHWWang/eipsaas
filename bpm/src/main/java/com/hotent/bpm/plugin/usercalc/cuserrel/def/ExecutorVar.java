package com.hotent.bpm.plugin.usercalc.cuserrel.def;

import java.io.Serializable;

public class ExecutorVar implements Serializable{
	private static final long serialVersionUID = 1L;
	public static final String SOURCE_BO ="BO";
	public static final String SOURCE_FLOW_VAR ="flowVar";
	
	public static final String EXECUTOR_TYPE_USER ="user";
	public static final String EXECUTOR_TYPE_GROUP ="group";
	
	
	/**来自BO还是流程变量*/
	private String source="";
	
	/** **/
	private String name="";
	
	/** group/user/fixed**/
	private String executorType ="";
	
	/***  account/userId**/
	private String userValType="";
	
	/**groupKey/groupId**/
	private String groupValType="";
	private String dimension="";
	
	/**在规则设置的时候，提供值与改对象匹配*/
	private String value;
	public ExecutorVar(){
		
	}

	public ExecutorVar(String source, String name, String executorType, String userValType, String groupValType, String dimension) {
		this.source = source;
		this.name = name;
		this.executorType = executorType;
		this.userValType = userValType;
		this.groupValType = groupValType;
		this.dimension = dimension;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExecutorType() {
		return executorType;
	}

	public void setExecutorType(String executorType) {
		this.executorType = executorType;
	}

	public String getUserValType() {
		return userValType;
	}

	public void setUserValType(String userValType) {
		this.userValType = userValType;
	}

	public String getGroupValType() {
		return groupValType;
	}

	public void setGroupValType(String groupValType) {
		this.groupValType = groupValType;
	}

	public String getDimension() {
		return dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
	
}
