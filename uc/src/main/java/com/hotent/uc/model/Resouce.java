package com.hotent.uc.model;


import org.apache.commons.lang.builder.ToStringBuilder;

import com.hotent.base.entity.BaseModel;



 /**
 * 
 * <pre> 
 * 描述：UC_ROLE_RESOUCE 实体对象
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2018-04-08 16:01:40
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public class Resouce extends BaseModel<Resouce> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 192338854033401774L;

	/**
	* 主键id
	*/
	protected String id; 
	
	/**
	* 角色id
	*/
	protected String roleId; 
	
	/**
	* 资源
	*/
	protected String resouce; 
	
	/**
	* 预留
	*/
	protected String pre; 
	

	
	
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 主键id
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
	
	public void setResouce(String resouce) {
		this.resouce = resouce;
	}
	
	/**
	 * 返回 资源
	 * @return
	 */
	public String getResouce() {
		return this.resouce;
	}
	
	public void setPre(String pre) {
		this.pre = pre;
	}
	
	/**
	 * 返回 预留
	 * @return
	 */
	public String getPre() {
		return this.pre;
	}
	

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("roleId", this.roleId) 
		.append("resouce", this.resouce) 
		.append("pre", this.pre) 
		.toString();
	}
}