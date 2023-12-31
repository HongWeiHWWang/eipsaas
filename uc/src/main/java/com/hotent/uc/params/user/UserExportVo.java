package com.hotent.uc.params.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 用户参数对象
 * @author liangqf
 *
 */
@ApiModel
public class UserExportVo {
	@ApiModelProperty(name="account",notes="用户帐号（多个用“,”号分隔，账号和工号任传其一，两个都有值时，只用account）")
	private String account;
	@ApiModelProperty(name="userNumber",notes="工号（多个用“,”号分隔）")
	private String userNumber;
	@ApiModelProperty(name="isOrg",notes="是否导出组织相关数据（包括维度、组织、职务、岗位已经之间的关系表数据）。默认为true",required=true,value="true")
	private boolean isOrg ;
	
	@ApiModelProperty(name="isRole",notes="是否导出角色以及用户角色关系数据。默认为true",required=true,value="true")
	private boolean isRole;

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

	public boolean isOrg() {
		return isOrg;
	}

	public void setOrg(boolean isOrg) {
		this.isOrg = isOrg;
	}

	public boolean isRole() {
		return isRole;
	}

	public void setRole(boolean isRole) {
		this.isRole = isRole;
	}
	

}
