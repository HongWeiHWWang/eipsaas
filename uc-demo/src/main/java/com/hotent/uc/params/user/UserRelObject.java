package com.hotent.uc.params.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户组织关系参数对象
 * @author liangqf
 *
 */
@ApiModel
public class UserRelObject {
	
	@ApiModelProperty(name="account",notes="登录帐号（account、userNumber任传其一）")
	private String account;
	
	@ApiModelProperty(name="userNumber",notes="工号")
	private String userNumber;
	
	@ApiModelProperty(name="isMain",notes="是否主组织/是否主负责人")
	private Boolean isMain;
	
	@ApiModelProperty(name="demCode",notes="维度别名")
	private String demCode;
	
	@ApiModelProperty(name="level",notes="级别")
	private String level;

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

	public String getDemCode() {
		return demCode;
	}

	public void setDemCode(String demCode) {
		this.demCode = demCode;
	}

	public Boolean getIsMain() {
		return isMain;
	}

	public void setIsMain(Boolean isMain) {
		this.isMain = isMain;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

}
