package com.hotent.uc.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * 
 * <pre> 
 * 描述：分级组织管理 实体对象
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2017-07-20 14:30:28
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@TableName("UC_ORG_AUTH")
@ApiModel(description="级组织管理")
public class OrgAuth extends UcBaseModel<OrgAuth>  {

	private static final long serialVersionUID = 6155180992784105371L;
	
	/**
	* ID_
	*/
	@TableId("ID_")
	@ApiModelProperty(name="id",notes="分级组织id")
	protected String id; 
	
	/**
	* 分级组织管理员id
	*/
	@TableField("USER_ID_")
	@ApiModelProperty(name="userId",notes="分级组织管理员id")
	protected String userId; 
	
	/**
	* 对应管理组织id
	*/
	@TableField("ORG_ID_")
	@ApiModelProperty(name="orgId",notes="对应管理组织id")
	protected String orgId; 
	
	/**
     * 对应维度id
     */
	@TableField("DEM_ID_")
    @ApiModelProperty(name="demId",notes="对应维度id")
    protected String demId;

    /**
     * 对应维度code
     */
	@TableField(exist=false)
    @ApiModelProperty(name="demCode",notes="对应维度code")
    protected String demCode;

    /**
	* 组织管理权限
	*/
	@TableField("ORG_PERMS_")
	@ApiModelProperty(name="orgPerms",notes="组织管理权限")
	protected String orgPerms; 
	
	/**
	* 用户管理权限
	*/
	@TableField("USER_PERMS_")
	@ApiModelProperty(name="userPerms",notes="用户管理权限")
	protected String userPerms; 
	
	/**
	* 岗位管理权限
	*/
	@TableField("POS_PERMS_")
	@ApiModelProperty(name="posPerms",notes="岗位管理权限")
	protected String posPerms; 
	
	/**
	* 分级管理员权限
	*/
	@TableField("ORGAUTH_PERMS_")
	@ApiModelProperty(name="orgauthPerms",notes="分级管理员权限")
	protected String orgauthPerms; 
	/**
	 * 布局管理权限
	 */
	@TableField("LAYOUT_PERMS_")
	@ApiModelProperty(name="layoutPerms",notes="布局管理权限")
	protected String layoutPerms; 
	
	
	/**
	 * 分级管理员名称
	 */
	@TableField(exist=false)
	@ApiModelProperty(name="userName",notes="分级管理员名称")
	protected String userName;
	
	/**
	 * 对应组织
	 */
	@TableField(exist=false)
	@ApiModelProperty(name="orgName",notes="对应组织名称")
	protected String orgName;
	
	/**
	 * 对应维度名称
	 */
	@TableField(exist=false)
	@ApiModelProperty(name="demName",notes="对应维度名称")
	protected String demName;
	
	/**
	 * 组织路径
	 */
	@TableField(exist=false)
	@ApiModelProperty(name="orgPath",notes="组织路径")
	protected String orgPath;
	
	/**
	 * 用户账号
	 */
	@TableField(exist=false)
	@ApiModelProperty(name="userAccount",notes="用户账号")
	protected String userAccount;

    public String getDemCode() {
        return demCode;
    }

    public void setDemCode(String demCode) {
        this.demCode = demCode;
    }

    public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 ID_
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getDemName() {
		return demName;
	}

	public void setDemName(String demName) {
		this.demName = demName;
	}

	/**
	 * 返回 分级组织管理员id
	 * @return
	 */
	public String getUserId() {
		return this.userId;
	}
	
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	/**
	 * 返回 对应管理组织id
	 * @return
	 */
	public String getOrgId() {
		return this.orgId;
	}
	
	public void setDemId(String demId) {
		this.demId = demId;
	}
	
	/**
	 * 返回 对应维度id
	 * @return
	 */
	public String getDemId() {
		return this.demId;
	}
	
	public void setOrgPerms(String orgPerms) {
		this.orgPerms = orgPerms;
	}
	
	/**
	 * 返回 组织管理权限
	 * @return
	 */
	public String getOrgPerms() {
		return this.orgPerms;
	}
	
	public void setUserPerms(String userPerms) {
		this.userPerms = userPerms;
	}
	
	/**
	 * 返回 用户管理权限
	 * @return
	 */
	public String getUserPerms() {
		return this.userPerms;
	}
	
	public void setPosPerms(String posPerms) {
		this.posPerms = posPerms;
	}
	
	/**
	 * 返回 岗位管理权限
	 * @return
	 */
	public String getPosPerms() {
		return this.posPerms;
	}
	
	public void setOrgauthPerms(String orgauthPerms) {
		this.orgauthPerms = orgauthPerms;
	}
	
	public String getOrgPath() {
		return orgPath;
	}

	public void setOrgPath(String orgPath) {
		this.orgPath = orgPath;
	}

	/**
	 * 返回 分级管理员权限
	 * @return
	 */
	public String getOrgauthPerms() {
		return this.orgauthPerms;
	}
	
	public String getLayoutPerms() {
		return layoutPerms;
	}

	public void setLayoutPerms(String layoutPerms) {
		this.layoutPerms = layoutPerms;
	}
	

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("userId", this.userId) 
		.append("orgId", this.orgId) 
		.append("demId", this.demId) 
		.append("orgPerms", this.orgPerms) 
		.append("userPerms", this.userPerms) 
		.append("posPerms", this.posPerms) 
		.append("orgauthPerms", this.orgauthPerms) 
		.append("userName",this.userName)
		.append("orgName",this.orgName)
		.append("demName",this.demName)
		.append("isDelete",this.isDelete)
		.append("version",this.version)
		.toString();
	}

}
