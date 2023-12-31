package com.hotent.bpm.persistence.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;




/**
 * 对象功能:流程授权主表明细 Model对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:xucx
 * 创建时间:2014-03-05 09:00:53
 */
@TableName("bpm_def_user")
public class BpmDefUser extends BaseModel<BpmDefUser>
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1681575503521426740L;

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
		/**流程定义权限*/
		public static final String BPM_DEF="bpmDef";
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


	
	// id
	@TableId("id_")
	protected String id;
	
	// 项目类型
	@TableField("obj_type_")
	protected String objType;
	
	// 流程分管权限主表ID
	@TableField("authorize_id_")
	protected String authorizeId;
	
	// 权限所有者ID
	@TableField("owner_id_")
	protected String ownerId;
	
	// 权限所有者
	@TableField("owner_name_")
	protected String ownerName;
	
	// 权限类型
	@TableField("right_type_")
	protected String rightType;
	
	//权限部门ID
	@TableField("auth_org_")
	protected String authOrg;
		
	//权限部门名称
	@TableField("auth_org_name_")
	protected String authOrgName;

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

	public String getAuthOrg() {
		return authOrg;
	}

	public void setAuthOrg(String authOrg) {
		this.authOrg = authOrg;
	}

	public String getAuthOrgName() {
		return authOrgName;
	}

	public void setAuthOrgName(String authOrgName) {
		this.authOrgName = authOrgName;
	}

}