package com.hotent.bpm.plugin.usercalc.cusers.def;

import com.hotent.bpm.plugin.core.plugindef.AbstractUserCalcPluginDef;
import com.hotent.bpm.plugin.usercalc.cuserrel.def.ExecutorVar;

public class CusersPluginDef extends AbstractUserCalcPluginDef {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2982105166695296725L;

	/**
	 * 来源
	 */
	private String source="";
	
	/**
	 * 变量
	 */
	private ExecutorVar executorVar;
	
	/**
	 * 帐号，使用逗号分隔。
	 */
	private String account="";
	
	/**
	 * 用户名，使用逗号分隔。
	 */
	private String userName="";

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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public ExecutorVar getVar() {
		return executorVar;
	}

	public void setExecutorVar(ExecutorVar executorVar) {
		this.executorVar = executorVar;
	}
	
}
