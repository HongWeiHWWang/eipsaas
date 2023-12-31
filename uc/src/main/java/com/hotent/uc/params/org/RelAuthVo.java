package com.hotent.uc.params.org;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author liangqf
 *<pre>分级汇报线视图</pre>
 */
public class RelAuthVo {
	
	@ApiModelProperty(name="account",notes="用户帐号",required=true)
	private String account;
	
	@ApiModelProperty(name="newAccount",notes="用户帐号,新增不传。编辑的时候，会替换之前用户账号",required=false)
	private String newAccount;
	
	@ApiModelProperty(name="relCode",notes="汇报线节点代码",required=true)
	private String relCode;
	
	@ApiModelProperty(name="typeCode",notes="汇报线分类编码",required=true)
	private String typeCode;
	

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getNewAccount() {
		return newAccount;
	}

	public void setNewAccount(String newAccount) {
		this.newAccount = newAccount;
	}

	
	
	public String getRelCode() {
		return relCode;
	}

	public void setRelCode(String relCode) {
		this.relCode = relCode;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String toString() {
		return "{"
				+ "\""+"account"+"\""+":"+"\""+this.account+"\","
				+"\""+"relCode"+"\""+":"+"\""+this.relCode+"\","
				+"\""+"typeCode"+"\""+":"+"\""+this.typeCode+"\","
				+ "}";
	}
	
}
