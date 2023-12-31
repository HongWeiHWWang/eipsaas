package com.hotent.uc.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 
 * <pre> 
 * 描述：群组管理  实体对象
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2017-11-27 17:55:16
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@TableName("UC_USER_GROUP")
@ApiModel(description="群组管理")
public class UserGroup extends UcBaseModel<UserGroup> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	* 编号
	*/
	@TableId("ID_")
	@ApiModelProperty(name="description",notes="群组id")
	protected String id; 
	
	/**
	* 名称
	*/
	@TableField("NAME_")
	@ApiModelProperty(name="description",notes="群组名称")
	protected String name; 
	
	/**
	* 群里的人或组
	*/
	@TableField("JSON_")
	@ApiModelProperty(name="json",notes="群里的人或组（页面保存的json数据）")
	protected String json; 
	
	/**
	* 别名
	*/
	@TableField("CODE_")
	@ApiModelProperty(name="code",notes="群里编码")
	protected String code; 
	
	/**
	 * 用户id
	 */
	@TableField("USER_ID_")
	@ApiModelProperty(name="userId",notes="用户id")
	protected String userId; 
	
	/**
	 * 组织id
	 */
	@TableField("ORG_ID_")
	@ApiModelProperty(name="orgId",notes="组织id")
	protected String orgId; 
	
	/**
	 * 角色id
	 */
	@TableField("ROLE_ID_")
	@ApiModelProperty(name="roleId",notes="角色id")
	protected String roleId; 
	
	/**
	 * 岗位id
	 */
	@TableField("POS_ID_")
	@ApiModelProperty(name="posId",notes="岗位id")
	protected String posId; 
	
	/**
	 * 描述
	 */
	@TableField("DESCRIPTION_")
	@ApiModelProperty(name="description",notes="群组描述")
	protected String description;
	
	/**
	 * 管理员（默认为创建人）
	 */
	@TableField(exist=false)
	@ApiModelProperty(name="creator",notes="管理员（默认为创建人）")
	protected String creator;
	
	
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 编号
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 返回 名称
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	
	/**
	 * 返回 用户id
	 * @return
	 */
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 返回 组织id
	 * @return
	 */
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	/**
	 * 返回 角色id
	 * @return
	 */
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	/**
	 * 返回 岗位id
	 * @return
	 */
	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}

	public void setJson(String json) {
		this.json = json;
	}
	
	/**
	 * 返回 群里的人或组
	 * @return
	 */
	public String getJson() {
		return this.json;
	}
	
	/**
	 * 返回群组编码
	 * @return
	 */
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 返回 描述
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("name", this.name) 
		.append("json", this.json) 
		.append("code", this.code)
		.append("userId", this.userId)
		.append("orgId", this.orgId)
		.append("roleId", this.roleId)
		.append("posId", this.posId)
		.append("description", this.description)
		.append("creator", this.creator)
		.append("isDelete",this.isDelete)
		.append("version",this.version)
		.toString();
	}
}
