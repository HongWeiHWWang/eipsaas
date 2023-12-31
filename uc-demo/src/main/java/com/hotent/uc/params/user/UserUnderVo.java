package com.hotent.uc.params.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author liangqf
 * <pre>用户下属关系</pre>
 *
 */
@ApiModel
public class UserUnderVo {
	
	@ApiModelProperty(name="underAccounts",notes="下属用户帐号，多个用英文逗号隔开",required=true)
	private String underAccounts;
	
	@ApiModelProperty(name="orgCode",notes="组织代码",required=true)
	private String orgCode;
	
	@ApiModelProperty(name="account",notes="用户帐号",required=true)
	private String account;

	public String getUnderAccounts() {
		return underAccounts;
	}

	public void setUnderAccounts(String underAccounts) {
		this.underAccounts = underAccounts;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
	public String toString() {
		return "{"
				+ "\""+"underAccounts"+"\""+":"+"\""+this.underAccounts+"\","
				+"\""+"orgCode"+"\""+":"+"\""+this.orgCode+"\","
				+"\""+"account"+"\""+":"+"\""+this.account+"\""
				+ "}";
	}
}
