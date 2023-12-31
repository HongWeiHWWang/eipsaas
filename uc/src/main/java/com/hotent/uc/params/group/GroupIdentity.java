package com.hotent.uc.params.group;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户组
 * @author zhangxw
 *
 */
@ApiModel
public class GroupIdentity {
	
	 /**
	  * 用户。
	  */
	 public final static String TYPE_USER="user";
	 /**
	  * 群组。
	  */
	 public final static String TYPE_GROUP="group";
	 /**
	  * 组织。
	  */
	 public final static String TYPE_ORG="org";
	 /**
	  * 职务。
	  */
	 public final static String TYPE_JOB="job";
	 /**
	  * 岗位。
	  */
	 public final static String TYPE_POS="pos";
	 /**
	  * 角色。
	  */
	 public final static String TYPE_ROLE="role";
	
	@ApiModelProperty(name="id",notes="用户组id（如用户id）")
	private String id;
	
	@ApiModelProperty(name="name",notes="用户组名称（如用户名称）")
	private String name;
	
	@ApiModelProperty(name="code",notes="用户组编码（如用户账号）")
	private String code;
	
	@ApiModelProperty(name="groupType",notes="用户组类型：user（用户）、org（组织）、pos（岗位）、role（角色）")
	private String groupType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	
}
