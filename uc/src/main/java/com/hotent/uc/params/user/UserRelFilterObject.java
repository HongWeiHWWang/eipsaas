package com.hotent.uc.params.user;

import io.swagger.annotations.ApiModelProperty;

public class UserRelFilterObject {

	@ApiModelProperty(name="account",notes="用户账号",required=false)
	private String account;
	
	@ApiModelProperty(name="userId",notes="用户id",required=false)
	private String userId;
	
	@ApiModelProperty(name="typeCode",notes="汇报线分类编码",required=true)
	private String typeCode;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	
	
}
