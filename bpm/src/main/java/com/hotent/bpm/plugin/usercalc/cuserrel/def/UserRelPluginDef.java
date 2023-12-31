package com.hotent.bpm.plugin.usercalc.cuserrel.def;

import com.hotent.bpm.plugin.core.plugindef.AbstractUserCalcPluginDef;

public class UserRelPluginDef extends AbstractUserCalcPluginDef {
	
//	<userRel xmlns="http://www.jee-soft.cn/bpm/plugins/userCalc/userRel"
//		    source="start" relationKey="grade" relationParty="xiaji" logicCal="or" extract="no">
//		    <members account="" userName=""/>
//		    <var name=""/>  
//		</userRel>
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4206793565483702827L;

	/**
	 * 用户类型。
	 */
	private String source="";
	
	/**
	 * 关系类型key。
	 */
	private String relationKey="";
	
	/**
	 * 关系类型名称
	 */
	private String relationName="";
	
	/**
	 * 关系方。
	 */
	private String relationParty="";
	
	
	private String relationPartyName="";
	/**
	 * 用户变量。
	 */
	private ExecutorVar executorVar;
	
	/**
	 * 帐号。
	 */
	private String account="";
	
	/**
	 * 用户名。
	 */
	private String userName="";
	
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getRelationKey() {
		return relationKey;
	}
	public void setRelationKey(String relationKey) {
		this.relationKey = relationKey;
	}
	public String getRelationParty() {
		return relationParty;
	}
	public void setRelationParty(String relationParty) {
		this.relationParty = relationParty;
	}

	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRelationName() {
		return relationName;
	}
	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}
	public String getRelationPartyName() {
		return relationPartyName;
	}
	public void setRelationPartyName(String relationPartyName) {
		this.relationPartyName = relationPartyName;
	}
	public ExecutorVar getVar() {
		return executorVar;
	}
	public void setExecutorVar(ExecutorVar executorVar) {
		this.executorVar = executorVar;
	}
	
	

}
