package com.hotent.bpm.plugin.execution.message.def;

import java.util.ArrayList;
import java.util.List;

import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.UserAssignRule;

public class PlainTextSetting {
	
	/**
	 * 消息类型
	 */
	private String msgType="";
	
	/**
	 * 模版内容。
	 */
	private String content="";
	
	/**
	 * 用户规则列表。
	 */
	private List<UserAssignRule> ruleList=new ArrayList<UserAssignRule>();
	
	public String getMsgType() {
		return msgType;
	}


	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public List<UserAssignRule> getRuleList() {
		return ruleList;
	}


	public void setRuleList(List<UserAssignRule> ruleList) {
		this.ruleList = ruleList;
	}


	

}
