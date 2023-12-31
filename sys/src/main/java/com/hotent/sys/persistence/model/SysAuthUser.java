package com.hotent.sys.persistence.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;




/**
 * 对象功能:流程授权主表明细 Model对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:jason
 * 创建时间:2014-03-05 09:00:53
 */
@TableName("portal_sys_auth_user")
@ApiModel(description="流程授权中用户属性")
public class SysAuthUser extends BaseModel<SysAuthUser>
{
	
	private static final long serialVersionUID = 1L;

	public final static class BPMDEFUSER_RIGHT_TYPE{
		
		/**所有用户 */
		public static final String ALL="all";
//		/**用户 */
//		public static final String USER="user";
//		/**角色*/
//		public static final String ROLE="role";
//		/**组织(本层级)*/
//		public static final String ORG="org";
//		/**岗位*/
//		public static final String POSITION="position";
//		/**组织（包含子组织)*/
//		public static final String GRANT="grant";

	}
	
	public final static class BPMDEFUSER_OBJ_TYPE{
		/**首页栏目权限*/
		public static final String INDEX_COLUMN="indexColumn";
		/**首页工具权限*/
		public static final String INDEX_TOOLS="indexTools";
		/**分类管理权限*/
		public static final String INDICATOR_COLUMN="indicatorColumn";
		/**布局管理权限*/
		public static  String INDEX_MANAGE ="indexManage";
		/**会议室管理权限*/
		public static  String MEETING_ROOM_MANAGE ="meetingRoom";
		/**公告查看权限*/
		public static  String MESSAGE_READ ="messageRead";
	}

	@TableId("id_")
	@ApiModelProperty(name="id", notes="主键")
	protected String id;
	
	@TableField("obj_type_")
	@ApiModelProperty("项目类型")
	protected String objType;
	
	@TableField("authorize_id_")
	@ApiModelProperty("流程分管权限主表ID")
	protected String authorizeId;
	
	@TableField("owner_id_")
	@ApiModelProperty("权限所有者ID")
	protected String ownerId;
	
	@TableField("owner_name_")
	@ApiModelProperty("权限所有者")
	protected String ownerName;
	
	@TableField("right_type_")
	@ApiModelProperty("权限类型")
	protected String rightType;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getObjType() {
		return objType;
	}

	public void setObjType(String objType) {
		this.objType = objType;
	}

	public String getAuthorizeId()
	{
		return authorizeId;
	}

	public void setAuthorizeId(String authorizeId)
	{
		this.authorizeId = authorizeId;
	}

	public String getOwnerId()
	{
		return ownerId;
	}

	public void setOwnerId(String ownerId)
	{
		this.ownerId = ownerId;
	}

	public String getOwnerName()
	{
		return ownerName;
	}

	public void setOwnerName(String ownerName)
	{
		this.ownerName = ownerName;
	}

	public String getRightType()
	{
		return rightType;
	}

	public void setRightType(String rightType)
	{
		this.rightType = rightType;
	}

	

}