package com.hotent.sys.persistence.model;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


 /**
 * 系统请求方法的配置 （用于角色权限配置）
 * <pre> 
 * 描述：系统请求方法的配置 （用于角色权限配置） 实体对象
 * 构建组：x7
 * 作者:liyg
 * 邮箱:liygui@jee-soft.cn
 * 日期:2018-06-29 14:23:28
 * 版权：广州宏天软件有限公司
 * </pre>
 */
 @ApiModel(description = "系统请求方法的配置 （用于角色权限配置）") 
 @TableName("portal_sys_method")
public class SysMethod extends BaseModel<SysMethod>{

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(name="id" ,notes="主键")
	@TableId("id_")
	protected String id; 
	
	@ApiModelProperty(name="menuAlias" ,notes="菜单资源别名")
	@TableField("menu_alias_")
	protected String menuAlias; 
	
	@ApiModelProperty(name="alias" ,notes="别名")
	@TableField("alias_")
	protected String alias; 
	
	@ApiModelProperty(name="name" ,notes="请求方法名")
	@TableField("name_")
	protected String name; 
	
	@ApiModelProperty(name="requestUrl" ,notes="请求地址")
	@TableField("request_url_")
	protected String requestUrl; 
	
	@ApiModelProperty(name="path" ,notes="菜单路径")
	@TableField("path_")
	protected String path; 
	
	@TableField(exist=false)
	protected String roleAlias;
	
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
	
	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}
	
	/**
	 * 返回 菜单资源别名
	 * @return
	 */
	public String getMenuAlias() {
		return this.menuAlias;
	}
	
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	/**
	 * 返回 别名
	 * @return
	 */
	public String getAlias() {
		return this.alias;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	
	/**
	 * 返回 请求地址
	 * @return
	 */
	public String getRequestUrl() {
		return this.requestUrl;
	}
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("menuAlias", this.menuAlias) 
		.append("alias", this.alias) 
		.append("requestUrl", this.requestUrl) 
		.toString();
	}
}