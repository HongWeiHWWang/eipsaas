package com.hotent.uc.params.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;




/**
 * 用户参数对象
 * @author liangqf
 *
 */
@ApiModel
public class UserImportVo {
	
	@ApiModelProperty(name="isNewCode",notes="是否生成新编码。当编码在系统中已存在且对应名称不同时：true,编码加后缀生成新的编码导入；false：不导入数据及相关连数据。默认为true",required=true,value="true")
	private boolean isNewCode;
	
	@ApiModelProperty(name="isCover",notes="是否覆盖更新。如果编码和名称一样，则默认为同一条数据：true，将已导入数据为准，更新其他字段，false，不更新除关联字段以外的字段。默认为true",required=true,value="true")
	private boolean isCover;
	
	@ApiModelProperty(name="isOrg",notes="是否导入组织相关数据（包括维度、组织、职务、岗位已经之间的关系表数据）。默认为true",required=true,value="true")
	private boolean isOrg ;
	
	@ApiModelProperty(name="isRole",notes="是否导入角色以及用户角色关系数据。默认为true",required=true,value="true")
	private boolean isRole;

	public boolean isNewCode() {
		return isNewCode;
	}

	public void setNewCode(boolean isNewCode) {
		this.isNewCode = isNewCode;
	}

	public boolean isCover() {
		return isCover;
	}

	public void setCover(boolean isCover) {
		this.isCover = isCover;
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
