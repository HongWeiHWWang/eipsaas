package com.hotent.uc.params.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class UserPolymerRole {
	@ApiModelProperty(name="code", notes="角色编码", required=true)
	private String code;
	@ApiModelProperty(name="name", notes="角色名称", required=false)
	private String name;
	
	public UserPolymerRole(){}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
