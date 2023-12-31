package com.hotent.uc.params.user;

import com.hotent.uc.model.UserRel;
import com.hotent.base.util.BeanUtils;

import io.swagger.annotations.ApiModelProperty;

public class UserRelVo {

	
	@ApiModelProperty(name="alias",notes="关系节点别名(编辑汇报线时必填。新增根据 汇报线分类编码+用户组类型编码+用户组编码 生成。重复则拼id后三位)",required=true)
	private String alias;
	
	@ApiModelProperty(name="value",notes="关系节点值（用户组id，如用户填账号，组织、岗位、角色分别填编码）",required=true)
	private String value;
	
	@ApiModelProperty(name="parentAlias",notes="关系节点父节点别名（如果是顶层节点则节点别名为关系节点所属分类key，所属分类在汇报线分类列表处添加），添加汇报线必填",required=true)
	private String parentAlias;
	
	@ApiModelProperty(name="type",notes="关系节点类型  user:用户;org:组织;post:岗位;role:角色，group：群组",required=true)
	private String type;
	
	@ApiModelProperty(name="status",notes="关系节点状态 0：无效；1：有效（默认为有效）")
	private Integer status;

	@ApiModelProperty(name="id",notes = "关系节点id")
	private String id;

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getParentAlias() {
		return parentAlias;
	}

	public void setParentAlias(String parentAlias) {
		this.parentAlias = parentAlias;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public static UserRel parse(UserRelVo userRelVo){
		UserRel userRel = new UserRel();
		userRel.setGroupType(userRelVo.getType());
		userRel.setValue(userRelVo.getValue());
		userRel.setStatus(BeanUtils.isEmpty(userRelVo.getStatus())?1:userRelVo.getStatus());
		return userRel;
	}
}
