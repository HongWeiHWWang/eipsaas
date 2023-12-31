package com.hotent.uc.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.uc.api.constant.GroupStructEnum;
import com.hotent.uc.api.constant.GroupTypeConstant;
import com.hotent.uc.api.model.IGroup;
import com.hotent.uc.api.model.IdentityType;


/**
 * 
 * <pre> 
 * 描述：角色管理 实体对象
 * 构建组：x5-bpmx-platform
 * 作者:ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2016-06-30 10:28:04
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@TableName("UC_ROLE")
@ApiModel(description="角色管理")
public class Role extends UcBaseModel<Role> implements IGroup {

	private static final long serialVersionUID = 2892592641529037008L;
	/**
	* id_
	*/
	@TableId("ID_")
	@ApiModelProperty(name="id",notes="角色id")
	protected String id; 
	
	/**
	* 角色名称
	*/
	@TableField("NAME_")
	@ApiModelProperty(name="name",notes="角色名称")
	protected String name; 
	
	/**
	* 角色别名
	*/
	@TableField("CODE_")
	@ApiModelProperty(name="code",notes="角色编码")
	protected String code; 
	
	/**
	* 0：禁用，1：启用
	*/
	@TableField("ENABLED_")
	@ApiModelProperty(name="enabled",notes="0：禁用，1：启用")
	protected Integer enabled; 
	
	/**
	 * 角色描述
	 */
	@TableField("DESCRIPTION_")
	@ApiModelProperty(name="description",notes="角色描述")
	protected String description="";
	
	
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 id_
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 返回 角色名称
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	
	/**
	 * 返回角色编码
	 * @return
	 */
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}
	
	/**
	 * 返回 0：禁用，1：启用
	 * @return
	 */
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
		.append("alias", this.code) 
		.append("enabled", this.enabled) 
		.append("isDelete",this.isDelete)
		.append("version",this.version)
		.toString();
	}

	public String getGroupId() {
		return this.id;
	}

	public String getGroupCode() {
		return this.code;
	}

	public Long getOrderNo() {
		return Long.valueOf(1);
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

	@Override
	public String getIdentityType() {
		return IdentityType.GROUP;
	}

	@Override
	public String getGroupType() {
		return GroupTypeConstant.ROLE.key();
	}

	@Override
	public GroupStructEnum getStruct() {
		return null;
	}
}
