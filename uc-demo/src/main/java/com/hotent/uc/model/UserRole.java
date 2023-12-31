package com.hotent.uc.model;

import io.swagger.annotations.ApiModelProperty;

import org.apache.commons.lang.builder.ToStringBuilder;


public class UserRole  extends UcBaseModel  {

	private static final long serialVersionUID = -3390031053071859598L;
	
	/**
	* id_
	*/
	@ApiModelProperty(name="id",notes="id")
	protected String id; 
	
	/**
	* role_id_
	*/
	@ApiModelProperty(name="roleId",notes="角色id")
	protected String roleId; 
	
	/**
	* user_id_
	*/
	@ApiModelProperty(name="userId",notes="用户id")
	protected String userId; 
	
	/**
	 * 以下是扩展字段，用于关联显示。
	 */
	
	//用户名
	@ApiModelProperty(name="fullname",notes="用户名称")
	protected String fullname; 
	// 角色名称
	@ApiModelProperty(name="roleName",notes="角色名称")
	protected String roleName; 
	//角色别名
	@ApiModelProperty(name="alias",notes="角色别名")
	protected  String alias;
	//账号
	@ApiModelProperty(name="account",notes="用户账号")
	protected String account=""; 
	
	public void setAlias(String alias) {
		this.alias = alias;
	}
 
	public String getAlias() {
		return this.alias;
	}
	
	public void setId(String id) {
		this.id = id;
	}
 
	public String getId() {
		return this.id;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
 
	public String getFullname() {
		return this.fullname;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
 
	public String getRoleName() {
		return this.roleName;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	/**
	 * 返回 role_id_
	 * @return
	 */
	public String getRoleId() {
		return this.roleId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	/**
	 * 返回 user_id_
	 * @return
	 */
	public String getUserId() {
		return this.userId;
	}
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("roleId", this.roleId) 
		.append("userId", this.userId)
		.append("updateTime",this.updateTime)
		.append("isDelete",this.isDelete)
		.append("version",this.version)
		.toString();
	}

}
