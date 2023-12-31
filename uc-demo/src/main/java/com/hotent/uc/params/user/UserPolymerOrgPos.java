package com.hotent.uc.params.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class UserPolymerOrgPos {
	/**
	 * 类型：组织
	 */
	public static final String TYPE_ORG = "org";
	/**
	 * 类型：岗位
	 */
	public static final String TYPE_POS = "pos";
	
	@ApiModelProperty(name="type", notes="类型", allowableValues="org,pos", required=true)
	private String type;
	@ApiModelProperty(name="orgCode", notes="组织代码", required=true)
	private String orgCode;
	@ApiModelProperty(name="posCode", notes="岗位代码", required=true)
	private String posCode;
	@ApiModelProperty(name="name", notes="组织/岗位名称", required=false)
	private String name;
	@ApiModelProperty(name="isMain", notes="是否主组织/岗位", required=false)
	private Boolean isMain;
	
	public UserPolymerOrgPos(){}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getIsMain() {
		return isMain;
	}

	public void setIsMain(Boolean isMain) {
		this.isMain = isMain;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getPosCode() {
		return posCode;
	}

	public void setPosCode(String posCode) {
		this.posCode = posCode;
	}
}
