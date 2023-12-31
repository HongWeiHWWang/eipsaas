package com.hotent.system.model;

import java.util.Date;

/**
 * token对象。
 * @author ray
 *
 */
public class TokenModel {

	/**
	 * 通讯录accesstoken最后更新时间。
	 */
	private Date lastUpdTime=new Date();

	/**
	 * 应用accesstoken最后更新时间。
	 */
	private Date agentTokenlastUpdTime=new Date();
	/**
	 * 通讯录accesstoken 数据。
	 */
	private String token="";

	/**
	 * 应用Token
	 */
	private String agentToken="";
	/**
	 * 通讯录accesstoken是否初始化。
	 */
	private boolean isInit=false;
	/**
	 * 应用Token是否初始化
	 */
	private boolean isAgentInit=false;

	public TokenModel(){}

	/**
	 * 通讯录accesstoken过期时间。
	 */
	private int exprieIn=7200;

	/**
	 * 应用Token过期时间
	 */
	private int agentexprieIn=7200;


	public int getAgentexprieIn() {
		return agentexprieIn;
	}

	public void setAgentexprieIn(int agentexprieIn) {
		this.agentexprieIn = agentexprieIn;
	}

	public Date getLastUpdTime() {
		return lastUpdTime;
	}

	public void setLastUpdTime(Date lastUpdTime) {
		this.lastUpdTime = lastUpdTime;
	}


	public Date getAgentTokenlastUpdTime() {
		return agentTokenlastUpdTime;
	}

	public void setAgentTokenlastUpdTime(Date agentTokenlastUpdTime) {
		this.agentTokenlastUpdTime = agentTokenlastUpdTime;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}



	public String getAgentToken() {
		return agentToken;
	}

	public void setAgentToken(String agentToken) {
		this.agentToken = agentToken;
	}

	public boolean isInit() {
		return isInit;
	}

	public void setInit(boolean isInit) {
		this.isInit = isInit;
	}


	public boolean isAgentInit() {
		return isAgentInit;
	}

	public void setAgentInit(boolean isAgentInit) {
		this.isAgentInit = isAgentInit;
	}

	public int getExprieIn() {
		return exprieIn;
	}

	public void setExprieIn(int exprieIn) {
		this.exprieIn = exprieIn;
	}

	/**
	 * 是否已经过期。
	 * @return
	 */
	public boolean isExpire(Date updtime,int exprieIn){
		long t=(new Date().getTime() - updtime.getTime())/1000;
		long time=exprieIn-t;
		if(time<60){
			return true;
		}
		return false;
	}

	/**
	 * 设置token。
	 * @param token
	 * @param expire
	 */
	public void setCorpToken(String token,int expire){
		this.token=token;
		this.exprieIn=expire;
		this.isInit=true;
		this.lastUpdTime=new Date();
	}

	/**
	 * 设置应用token。
	 * @param token
	 * @param expire
	 */
	public void setAgentToken(String token,int expire){
		this.agentToken=token;
		this.agentexprieIn=expire;
		this.isAgentInit=true;
		this.agentTokenlastUpdTime=new Date();
	}

}
