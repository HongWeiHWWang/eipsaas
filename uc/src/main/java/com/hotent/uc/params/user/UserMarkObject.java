package com.hotent.uc.params.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户参数对象
 * @author liangqf
 *
 */
@ApiModel
public class UserMarkObject {
	
	@ApiModelProperty(name="account",notes="用户帐号（两个参数任传其一，两个都有值时，只用account）")
	private String account;
	
	@ApiModelProperty(name="userNumber",notes="工号")
	private String userNumber;


	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getUserNumber() {
		return userNumber;
	}

	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}
	
	
}
