package com.hotent.uc.params.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户参数对象
 * @author liangqf
 *
 */
@ApiModel
public class UserPwdObject {
	
	@ApiModelProperty(name="account",notes="用户帐号（账号和工号任传其一，两个都有值时，只用account）")
	private String account;
	
	@ApiModelProperty(name="userNumber",notes="工号")
	private String userNumber;
	
	@ApiModelProperty(name="oldPwd",notes="旧密码（在接口updateUserPsw中不需要传）")
	private String oldPwd;
	
	@ApiModelProperty(name="newPwd",notes="新密码",required=true)
	private String newPwd;

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

	public String getOldPwd() {
		return oldPwd;
	}

	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}
	
	
}
