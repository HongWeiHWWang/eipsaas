package com.hotent.portal.model;



import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 对象权限表 Model对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月11日
 */
@ApiModel(description="对象权限表 Model对象")
public class SysObjRights{
	public static  String RIGHT_TYPE_INDEX_COLUMN ="indexColumn";
	/**
	 * indexManage  首页布局管理授权
	 */
	public static  String RIGHT_TYPE_INDEX_MANAGE ="indexManage";
	public static  String RIGHT_TYPE_POPUP_MSG ="popupMsg";
	/**
	 * bulletin  公告管理授权
	 */
	public static  String RIGHT_TYPE_SYS_BULLETIN = "bulletin";
	
	@ApiModelProperty(name="id", notes="主键")
	protected Long  id;

	@ApiModelProperty(name="objType", notes="对象类型")
	protected String  objType;

	@ApiModelProperty(name="objectId", notes="权限对象ID")
	protected Long  objectId;
	
	@ApiModelProperty(name="ownerId", notes="授权人ID")
	protected Long  ownerId;

	@ApiModelProperty(name="owner", notes="授权人")
	protected String  owner;
	
	@ApiModelProperty(name="rightType", notes="权限类型")
	protected String  rightType;
	
	/**
	 * 设置主键
	 * @param id 主键
	 */
	public void setId(Long id){
		this.id = id;
	}
	
	/**
	 * 返回 主键
	 * @return
	 */
	public Long getId() {
		return this.id;
	}
	
	/**
	 * 设置对象类型
	 * @param objType 对象类型
	 */
	public void setObjType(String objType){
		this.objType = objType;
	}
	
	/**
	 * 返回 对象类型
	 * @return
	 */
	public String getObjType() {
		return this.objType;
	}
	
	/**
	 * 设置权限对象ID 
	 * @param objectId 权限对象ID
	 */
	public void setObjectId(Long objectId){
		this.objectId = objectId;
	}
	
	/**
	 * 返回 权限对象ID
	 * @return
	 */
	public Long getObjectId() {
		return this.objectId;
	}
	
	/**
	 * 设置授权人ID
	 * @param ownerId 授权人ID
	 */
	public void setOwnerId(Long ownerId){
		this.ownerId = ownerId;
	}
	
	/**
	 * 返回 授权人ID
	 * @return
	 */
	public Long getOwnerId() {
		return this.ownerId;
	}
	
	/**
	 * 设置授权人
	 * @param owner
	 */
	public void setOwner(String owner){
		this.owner = owner;
	}
	
	/**
	 * 返回 授权人
	 * @return
	 */
	public String getOwner() {
		return this.owner;
	}
	
	/**
	 * 设置权限类型
	 * @param rightType 权限类型
	 */
	public void setRightType(String rightType){
		this.rightType = rightType;
	}
	
	/**
	 * 返回 权限类型
	 * @return
	 */
	public String getRightType() {
		return this.rightType;
	}
	

   	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof SysObjRights)) 
		{
			return false;
		}
		SysObjRights rhs = (SysObjRights) object;
		return new EqualsBuilder()
		.append(this.id, rhs.id)
		.append(this.objType, rhs.objType)
		.append(this.objectId, rhs.objectId)
		.append(this.ownerId, rhs.ownerId)
		.append(this.owner, rhs.owner)
		.append(this.rightType, rhs.rightType)
		.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.id) 
		.append(this.objType) 
		.append(this.objectId) 
		.append(this.ownerId) 
		.append(this.owner) 
		.append(this.rightType) 
		.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("objType", this.objType) 
		.append("objectId", this.objectId) 
		.append("ownerId", this.ownerId) 
		.append("owner", this.owner) 
		.append("rightType", this.rightType) 
		.toString();
	}
   
  

}