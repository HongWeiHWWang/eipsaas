package com.hotent.uc.params.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author liangqf
 * <pre>
 * 用户组织关系视图
 * </pre>
 *
 */
@ApiModel
public class OrgUserVo {
	
	@ApiModelProperty(name="account",notes="用户帐号",required=true)
	private String account;
	
	@ApiModelProperty(name="orgCode",notes="组织代码",required=true)
	private String orgCode;
	
	@ApiModelProperty(name="isMaster",notes="是否主组织 0：不是；1：是（默认）",allowableValues="1,0")
	private int isMaster;
	
	@ApiModelProperty(name="isCharge",notes="是否负责人 0：不是（默认）；1：负责人；2：主负责人",allowableValues="0,1,2")
	private int isCharge;
	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public int getIsMaster() {
		return isMaster;
	}

	public void setIsMaster(int isMaster) {
		this.isMaster = isMaster;
	}

	public int getIsCharge() {
		return isCharge;
	}

	public void setIsCharge(int isCharge) {
		this.isCharge = isCharge;
	}
	
	public String toString() {
		return "{"
				+ "\""+"account"+"\""+":"+"\""+this.account+"\","
				+"\""+"orgCode"+"\""+":"+"\""+this.orgCode+"\","
				+"\""+"isMaster"+"\""+":"+"\""+this.isMaster+"\","
				+"\""+"isCharge"+"\""+":"+"\""+this.isCharge+"\""
				+ "}";
	}

}
