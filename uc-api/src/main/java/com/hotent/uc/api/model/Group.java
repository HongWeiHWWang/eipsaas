package com.hotent.uc.api.model;

import java.util.Map;

import com.hotent.uc.api.constant.GroupStructEnum;

/**
 * 类 {@code Group} 用户组默认实现类，为了方便IGroup在uc和bpm之间传递
 *
 * @company 广州宏天软件股份有限公司
 * @author wanghb
 * @date 2019年2月26日
 */
public class Group implements IGroup{


	private static final long serialVersionUID = -4406666365832918247L;

    /**
     * 身份类型 比如：用户或用户组
     */
	protected String  identityType;
    /**
     * 用户组ID
     */
	protected String  groupId;
    /**
     * 用户组名称
     */
	protected String  name;
    /**
     * 用户组编码
     */
	protected String  groupCode;
    /**
     * 组织排序
     */
	protected Long  orderNo;
    /**
     * 用户组类型 比如：org,role,pos
     */
	protected String  groupType;
    /**
     * 用户组结构枚举
     */
	protected GroupStructEnum  struct;
    /**
     * 组织上级ID
     */
	protected String  parentId;
    /**
     * 组织路径
     */
	protected String  path;
    /**
     * 用户组参数
     */
	protected Map<String, Object>  params;

	public String getIdentityType() {
		return identityType;
	}

	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public Long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public GroupStructEnum getStruct() {
		return struct;
	}

	public void setStruct(GroupStructEnum struct) {
		this.struct = struct;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	
	
}
