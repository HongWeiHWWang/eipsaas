package com.hotent.bpm.api.model.process.nodedef.ext.extmodel;

import java.io.Serializable;
import java.util.List;

public class ReceiversModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//收件人(to,bcc,cc)
	private String type="";
	
	/**
	 * 人员接收规则。
	 */
	private List<UserAssignRule> userRules;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<UserAssignRule> getUserRules() {
		return userRules;
	}

	public void setUserRules(List<UserAssignRule> userRules) {
		this.userRules = userRules;
	}
	
	

}
