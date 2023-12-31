package com.hotent.uc.api.impl.model;

import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.hotent.base.entity.BaseModel;
import com.hotent.uc.api.constant.GroupStructEnum;
import com.hotent.uc.api.constant.GroupTypeConstant;
import com.hotent.uc.api.model.IGroup;
import com.hotent.uc.api.model.IdentityType;

/**
 * 类 {@code Role} 角色管理 实体对象
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月5日
 */
public class Role extends BaseModel<Role> implements IGroup{

	private static final long serialVersionUID = 5057944508741881514L;

	/**
	* 主键ID
	*/
	protected String id; 
	
	/**
	* 角色名称
	*/
	protected String name; 
	
	/**
	* 角色别名
	*/
	protected String alias; 
	
	/**
	* 状态 0：禁用，1：启用
	*/
	protected Integer enabled; 
	
	/**
	 * 角色描述
	 */
	protected String description="";
	
	
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
	
	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getAlias() {
		return this.alias;
	}
	
	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public Integer getEnabled() {
		return this.enabled;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("name", this.name) 
		.append("alias", this.alias) 
		.append("enabled", this.enabled) 
		.toString();
	}

	public String getIdentityType() {
		return IdentityType.GROUP;
	}

	public String getGroupId() {
		return this.id;
	}

	public String getGroupCode() {
		return this.alias;
	}

	public Long getOrderNo() {
		return Long.valueOf(1);
	}

	public String getGroupType() {
		return GroupTypeConstant.ROLE.key();
	}

	public GroupStructEnum getStruct() {
		return GroupStructEnum.PLAIN;
	}

	public String getParentId() {
		return "";
	}

	public String getPath() {
		return this.name;
	}

	public Map<String, Object> getParams() {
		return null;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}