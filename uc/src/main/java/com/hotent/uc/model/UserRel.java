package com.hotent.uc.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotent.base.model.Tree;


/**
* 
* <pre> 
* 描述：用户关系 实体对象
* 构建组：x5-bpmx-platform
* 作者:liygui
* 邮箱:liygui@jee-soft.cn
* 日期:2017-06-12 09:21:48
* 版权：广州宏天软件有限公司
* </pre>
*/
@TableName("UC_USER_REL")
@ApiModel(description="用户关系")
public class UserRel extends UcBaseModel<UserRel> implements Tree{
	
	private final static Map<String, String> typeNameyMap = new HashMap<String, String>();  
	static { 
		typeNameyMap.put("user", "用户");
		typeNameyMap.put("org", "组织");
		typeNameyMap.put("pos", "岗位");
		typeNameyMap.put("role", "角色");
		typeNameyMap.put("job", "职务");
		typeNameyMap.put("group", "群组");
	}

	public static final String GROUP_USER = "user";
	public static final String GROUP_POS = "pos";
	public static final String GROUP_ROLE = "role";
	public static final String GROUP_ORG = "org";
	public static final String GROUP_JOB = "job";
	public static final String GROUP_GROUP = "group";
	public static final String FA_USERS = " fa fa-users ";
	public static final String FA_USER = " fa fa-user ";
	public static final String FA_POS = " fa fa-bookmark ";
	public static final String FA_ORG = " fa fa-sitemap ";
	public static final String FA_ROLE = " fa fa-filter ";
	public static final String FA_GROUP = " fa fa-group ";
	public static final String FA_JOB = " fa fa-idcard ";
	
	private static final long serialVersionUID = 1L;

	/**
	* 主键
	*/
	@TableId("ID_")
	@ApiModelProperty(name="id",notes="用户关系id")
	protected String id; 
	
	/**
	* 用户组名称
	*/
	@TableField("NAME_")
	@ApiModelProperty(name="name",notes="用户组名称")
	protected String name; 
	
	/**
	* 用户组id
	*/
	@TableField("VALUE_")
	@ApiModelProperty(name="value",notes="用户组id")
	protected String value; 
	
	/**
	* 用户组别名
	*/
	@TableField("ALIAS_")
	@ApiModelProperty(name="alias",notes="用户关系节点别名")
	protected String alias;
	
	/**
	 * 用户级别( 当一个汇报线上的用户有多个上级时， 此时可通过级别区分 )
	 */
	@TableField("LEVEL_")
	@ApiModelProperty(name="level",notes="用户关系节点级别")
	protected String level;
	
	/**
	* 父id
	*/
	@TableField("PARENT_ID_")
	@ApiModelProperty(name="parentId",notes="用户关系节点父节点")
	protected String parentId; 
	
	/**
	 * 群组名称
	 */
	@TableField("GROUP_TYPE_")
	@ApiModelProperty(name="groupType",notes="用户关系节点类型（用户组类型）")
	protected String groupType;
	
	
	/**
	 * 关系节点id路径，包含层级关系  
	 */
	@TableField("PATH_")
	@ApiModelProperty(name="path",notes="用户关系节点路径")
	protected String path;
	
	/**
	 * 节点状态 1：正常，0：失效
	 */
	@TableField("STATUS_")
	@ApiModelProperty(name="status",notes="节点状态 1：正常，0：失效")
	protected int status;
	
	/**
	* 分类
	*/
	@TableField("TYPE_ID_")
	@ApiModelProperty(name="typeId",notes="汇报线分类id")
	protected String typeId; 
	
	@TableField(exist=false)
	@ApiModelProperty(name="icon",notes="节点图标")
	protected String icon;

	@TableField(exist=false)
	protected List<UserRel> children = new ArrayList<UserRel>();
	
	
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 用户id 主要用于构建树
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
	
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	/**
	 * 返回 PARENT_USER_ID_
	 * @return
	 */
	public String getParentId() {
		return this.parentId;
	}
	
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public String getGroupType() {
		return groupType;
	}
	
	public String getGroupTypeName(String groupType) {
		return typeNameyMap.get(groupType);
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 返回 关系节点路径
	 * @return
	 */
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	
	
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	
	/**
	 * 返回 分类
	 * @return
	 */
	public String getTypeId() {
		return this.typeId;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("name", this.name)
		.append("value", this.value)
		.append("alias", this.alias)
		.append("level", this.level)
		.append("parentId", this.parentId) 
		.append("groupType", this.groupType) 
		.append("status", this.status) 
		.append("typeId", this.typeId) 
		.append("path", this.path)
		.append("isDelete",this.isDelete)
		.append("version",this.version)
		.toString();
	}

	@Override
	@JsonIgnore
	public String getText() {
		return this.name;
	}

	

	@Override
	public void setIsParent(String isParent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List getChildren() {
		// TODO Auto-generated method stub
		return children;
	}

	@Override
	public void setChildren(List children) {
		this.children = children;
	}
	

//	@Override
//	public String getText() {
//		return this.name;
//	}
//
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	@Override
//	public List getChildren() {
//		return children;
//	}
//
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	@Override
//	public void setChildren(List children) {
//		this.children = children;
//	}
//	
//	public String getIcon(){
//		if(this.getParentId()=="-1"){
//			return SysUserRel.FA_USERS;
//		}else{
//			if(SysUserRel.GROUP_USER.equals(this.groupType)){
//				return SysUserRel.FA_USER;
//			}else if(SysUserRel.GROUP_ORG.equals(this.groupType)){
//				return SysUserRel.FA_ORG;
//			}else if(SysUserRel.GROUP_POS.equals(this.groupType)){
//				return SysUserRel.FA_POS;
//			}else if(SysUserRel.GROUP_ROLE.equals(this.groupType)){
//				return SysUserRel.FA_ROLE;
//			}else{
//				return SysUserRel.FA_GROUP;
//			}
//		}
//	}
}
