package com.hotent.uc.model;
import io.swagger.annotations.ApiModelProperty;


import org.apache.commons.lang.builder.ToStringBuilder;


 /**
 * 
 * <pre> 
 * 描述：组织角色管理 实体对象
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2017-12-25 10:25:19
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public class OrgRole extends UcBaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2142601465361046779L;

	/**
	* 主键
	*/
	@ApiModelProperty(name="id",notes="组织角色id")
	protected String id; 
	
	/**
	* 角色id
	*/
	@ApiModelProperty(name="roleId",notes="角色id")
	protected String roleId; 
	
	/**
	* 角色id
	*/
	@ApiModelProperty(name="roleName",notes="角色名称")
	protected String roleName; 
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}


	/**
	* 组织id
	*/
	@ApiModelProperty(name="orgId",notes="组织id")
	protected String orgId; 
	
	/**
	* 子组织是否可以继承,0:不可1:可以
	*/
	@ApiModelProperty(name="isInherit",notes="子组织是否可以继承,0:不可1:可以")
	protected Integer isInherit; 
	
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 主键
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	/**
	 * 返回 角色id
	 * @return
	 */
	public String getRoleId() {
		return this.roleId;
	}
	
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	/**
	 * 返回 组织id
	 * @return
	 */
	public String getOrgId() {
		return this.orgId;
	}
	
	public void setIsInherit(Integer isInherit) {
		this.isInherit = isInherit;
	}
	
	/**
	 * 返回 子组织是否可以继承,0:不可1:可以
	 * @return
	 */
	public Integer getIsInherit() {
		return this.isInherit;
	}
	
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("roleId", this.roleId) 
		.append("orgId", this.orgId) 
		.append("isInherit", this.isInherit) 
		.append("updateTime", this.updateTime) 
		.append("isDelete", this.isDelete) 
		.append("version", this.version) 
		.toString();
	}
}