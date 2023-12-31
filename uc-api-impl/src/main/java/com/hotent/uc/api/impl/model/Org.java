package com.hotent.uc.api.impl.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hotent.base.entity.BaseModel;
import com.hotent.uc.api.constant.GroupStructEnum;
import com.hotent.uc.api.constant.GroupTypeConstant;
import com.hotent.uc.api.model.IGroup;
import com.hotent.uc.api.model.IdentityType;

/**
 * 类 {@code Org} 组织架构 实体对象
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月5日
 */
public class Org extends BaseModel<Org> implements IGroup{

	private static final long serialVersionUID = -7706199321187360467L;

	/**
	* 主键
	*/
	protected String id; 
	
	/**
	* 组织名称
	*/
	protected String name; 
	
	/**
	* 上级组织ID
	*/
	protected String parentId; 
	
	/**
	* 组织编号
	*/
	protected String code; 
	
	/**
	* 组织级别
	*/
	protected String grade; 
	
	/**
	 * 维度Id
	 */
	protected String demId;

    /**
     * 组织排序
     */
	protected Long orderNo; 
	
	/**
	 * 上级组织名称
	 */
	protected  String parentOrgName;
	
	/**
	 * 是否主组织
	 */
	private int isMaster=0;
	
	/**
	 * 组织路径
	 */
	protected String path;
	
	/**
	 * 组织路径名
	 */
	protected String pathName;

	/**
	 * 是否有子节点   否0  是1  
	 */
	protected int isIsParent = 0;

    /**
     * 微信组织Id
     */
	protected  String wxOrgId;

    /**
     * 用户id列表
     */
    protected List<String> userList = new ArrayList<String>();

    public String getWxOrgId() {
        return wxOrgId;
    }

    public void setWxOrgId(String wxOrgId) {
        this.wxOrgId = wxOrgId;
    }

    public String getPathName() {
		return pathName;
	}

	public boolean isIsParent() {
		return isIsParent==1;
	}

	@JsonIgnore
	public void setIsParent(int isIsParent) {
		this.isIsParent = isIsParent;
	}
	
	@JsonProperty("isParent")
	public void setIsParentBoolean(boolean isParent) {
		this.isIsParent = isParent?1:0;
	}

	public void setPathName(String pathName) {
		this.pathName = pathName;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setParentOrgName(String parentOrgName) {
		this.parentOrgName = parentOrgName;
	}

	public String getParentOrgName() {
		return this.parentOrgName;
	}
	
	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}
	
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
	
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentId() {
		return this.parentId;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getDemId() {
		return demId;
	}

	public void setDemId(String demId) {
		this.demId = demId;
	}

	public String getCode() {
		return this.code;
	}
	
	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getGrade() {
		return this.grade;
	}
	
	public List<String> getUserList() {
		return userList;
	}

	public void setUserList(List<String> userList) {
		this.userList = userList;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("name", this.name) 
		.append("parentId", this.parentId) 
		.append("code", this.code) 
		.append("grade", this.grade)
		.append("demId",this.demId)
		
		.append("path",this.path)
		.append("pathName",this.pathName)
		.toString();
	}

	public String getIdentityType() {
		return IdentityType.GROUP;
	}

	public String getGroupId() {
		return this.id;
	}

	public String getGroupCode() {
		return this.code;
	}

	public Long getOrderNo() {
		return this.orderNo;
	}

	public String getGroupType() {
		return GroupTypeConstant.ORG.key();
	}

	public GroupStructEnum getStruct() {
		return GroupStructEnum.TREE;
	}

	public String getPath() {
		return this.path;
	}

	public Map<String, Object> getParams() {
		return null;
	}

	public int getIsMaster() {
		return isMaster;
	}

	public void setIsMaster(int isMaster) {
		this.isMaster = isMaster;
	}
}