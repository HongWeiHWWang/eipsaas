package com.hotent.uc.params.org;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hotent.uc.model.OrgAuth;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author liangqf
 *<pre>分级组织视图</pre>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrgAuthVo{

	@ApiModelProperty(name="id",notes="组织分级管理ID",required=true)
	private String id;

	@ApiModelProperty(name="account",notes="用户帐号",required=true)
	private String account;
	
	@ApiModelProperty(name="newAccount",notes="用户帐号,新增不传。编辑的时候，会替换之前用户账号",required=false)
	private String newAccount;
	
	@ApiModelProperty(name="orgCode",notes="组织代码",required=true)
	private String orgCode;
	
	@ApiModelProperty(name="demCode",notes="维度别名",required=true)
	private String demCode;
	
	@ApiModelProperty(name="orgPerms",notes="组织管理权限，add（增加），delete（删除），edit（编辑）",example="add,delete,edit")
	private String orgPerms;
	
	@ApiModelProperty(name="userPerms",notes="用户管理权限，add（增加），delete（删除），edit（编辑）",example="add,delete,edit")
	private String userPerms;
	
	@ApiModelProperty(name="posPerms",notes="岗位管理权限，add（增加），delete（删除），edit（编辑）",example="add,delete,edit")
	private String posPerms;
	
	@ApiModelProperty(name="orgauthPerms",notes="分级管理权限，add（增加），delete（删除），edit（编辑）",example="add,delete,edit")
	private String orgauthPerms;
	
	@ApiModelProperty(name="layoutPerms",notes="布局管理权限，add（增加），delete（删除），edit（编辑）",example="add,delete,edit")
	private String layoutPerms;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
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

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getDemCode() {
		return demCode;
	}

	public void setDemCode(String demCode) {
		this.demCode = demCode;
	}

	public String getOrgPerms() {
		return orgPerms;
	}

	public void setOrgPerms(String orgPerms) {
		this.orgPerms = orgPerms;
	}

	public String getUserPerms() {
		return userPerms;
	}

	public void setUserPerms(String userPerms) {
		this.userPerms = userPerms;
	}

	public String getPosPerms() {
		return posPerms;
	}

	public void setPosPerms(String posPerms) {
		this.posPerms = posPerms;
	}

	public String getOrgauthPerms() {
		return orgauthPerms;
	}

	public void setOrgauthPerms(String orgauthPerms) {
		this.orgauthPerms = orgauthPerms;
	}

	public String getLayoutPerms() {
		return layoutPerms;
	}

	public void setLayoutPerms(String layoutPerms) {
		this.layoutPerms = layoutPerms;
	}
	
	public String toString() {
		return "{"
				+ "\""+"id"+"\""+":"+"\""+this.id+"\","
				+ "\""+"account"+"\""+":"+"\""+this.account+"\","
				+"\""+"orgCode"+"\""+":"+"\""+this.orgCode+"\","
				+"\""+"demCode"+"\""+":"+"\""+this.demCode+"\","
				+"\""+"posPerms"+"\""+":"+"\""+this.posPerms+"\","
				+"\""+"userPerms"+"\""+":"+"\""+this.userPerms+"\","
				+"\""+"orgauthPerms"+"\""+":"+"\""+this.orgauthPerms+"\","
				+"\""+"orgPerms"+"\""+":"+"\""+this.orgPerms+"\","
				+"\""+"layoutPerms"+"\""+":"+"\""+this.layoutPerms+"\""
				+ "}";
	}
	
	public static OrgAuth parse(OrgAuthVo orgAuthVo){
		OrgAuth orgAuth = new OrgAuth();
		orgAuth.setOrgPerms(orgAuthVo.getOrgPerms());
		orgAuth.setUserPerms(orgAuthVo.getUserPerms());
		orgAuth.setPosPerms(orgAuthVo.getPosPerms());
		orgAuth.setLayoutPerms(orgAuthVo.getLayoutPerms());
		orgAuth.setOrgauthPerms(orgAuthVo.getOrgauthPerms());
		return orgAuth;
	}

}
