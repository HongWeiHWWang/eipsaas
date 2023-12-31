package com.hotent.bpm.plugin.usercalc.grouprel.def;

import com.hotent.bpm.plugin.core.plugindef.AbstractUserCalcPluginDef;
import com.hotent.bpm.plugin.usercalc.cuserrel.def.ExecutorVar;

public class GroupRelPluginDef extends AbstractUserCalcPluginDef {
	
//	<?xml version="1.0" encoding="UTF-8"?>
//	<groupRel xmlns="http://www.jee-soft.cn/bpm/plugins/userCalc/groupRel"
//	    groupType="" source="" logicCal="" extract="">
//	    <groupVar name=""/>
//	    <groups groupKey="" groupName=""/>
//	    <userVar name=""/>
//	    
//	</groupRel>
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2880593630939375496L;
	/**
	 * 来源。
	 */
	private String source="";
	/**
	 * 组类型
	 */
	private String groupType="";
	
	/**
	 * 组类型名称。
	 */
	private String groupTypeName="";
	/**
	 * 支持关系，不支持就返回组。
	 */
	private boolean supportRelation=false;
	
	/**
	 * 关系类型
	 */
	private String relationType="";
	
	/**
	 * 关系类型名称。
	 */
	private String relationTypeName="";
	
	/**
	 * 变量
	 */
	private ExecutorVar executorVar;
	
	/**
	 * 分组key。
	 */
	private String groupKey="" ;
	
	/**
	 * 分组名称。
	 */
	private String groupName="";

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getGroupTypeName() {
		return groupTypeName;
	}

	public void setGroupTypeName(String groupTypeName) {
		this.groupTypeName = groupTypeName;
	}

	public boolean isSupportRelation() {
		return supportRelation;
	}

	public void setSupportRelation(boolean supportRelation) {
		this.supportRelation = supportRelation;
	}

	public String getRelationType() {
		return relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	public String getRelationTypeName() {
		return relationTypeName;
	}

	public void setRelationTypeName(String relationTypeName) {
		this.relationTypeName = relationTypeName;
	}

	public String getGroupKey() {
		return groupKey;
	}

	public void setGroupKey(String groupKey) {
		this.groupKey = groupKey;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public ExecutorVar getVar() {
		return executorVar;
	}

	public void setExecutorVar(ExecutorVar executorVar) {
		this.executorVar = executorVar;
	}
	
	
	

}
