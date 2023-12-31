package com.hotent.sys.persistence.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 角色权限配置
 * 
 * <pre>
 *  
 * 描述：角色权限配置 实体对象
 * 构建组：x7
 * 作者:liyg
 * 邮箱:liygui@jee-soft.cn
 * 日期:2018-06-29 14:27:46
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@ApiModel(description = "角色权限配置")
@TableName("portal_sys_role_auth")
public class SysRoleAuth extends BaseModel<SysRoleAuth> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(name = "id", notes = "主键")
	@TableId("id_")
	protected String id;

	@ApiModelProperty(name = "roleAlias", notes = "角色别名")
	@TableField("role_alias_")
	protected String roleAlias;

	@ApiModelProperty(name = "menuAlias", notes = "菜单别名")
	@TableField("menu_alias_")
	protected String menuAlias;

	@ApiModelProperty(name = "methodAlias", notes = "请求方法别名")
	@TableField("method_alias_")
	protected String methodAlias;

	@ApiModelProperty(name = "dataPermission", notes = "数据权限设置json")
	@TableField("data_permission_")
	protected String dataPermission;

	@ApiModelProperty(name = "methodRequestUrl", notes = "请求方法地址")
	@TableField(exist=false)
	private String methodRequestUrl;

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 返回 主键
	 * 
	 * @return
	 */
	public String getId() {
		return this.id;
	}

	public void setRoleAlias(String roleAlias) {
		this.roleAlias = roleAlias;
	}

	/**
	 * 返回 角色别名
	 * 
	 * @return
	 */
	public String getRoleAlias() {
		return this.roleAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

	/**
	 * 返回 菜单别名
	 * 
	 * @return
	 */
	public String getMenuAlias() {
		return this.menuAlias;
	}

	public void setMethodAlias(String methodAlias) {
		this.methodAlias = methodAlias;
	}

	/**
	 * 返回 请求方法别名
	 * 
	 * @return
	 */
	public String getMethodAlias() {
		return this.methodAlias;
	}

	public String getDataPermission() {
		return dataPermission;
	}

	public void setDataPermission(String dataPermission) {
		this.dataPermission = dataPermission;
	}

	public String getMethodRequestUrl() {
		return methodRequestUrl;
	}

	public void setMethodRequestUrl(String methodRequestUrl) {
		this.methodRequestUrl = methodRequestUrl;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append("roleAlias", this.roleAlias)
				.append("menuAlias", this.menuAlias).append("methodAlias", this.methodAlias).toString();
	}
}