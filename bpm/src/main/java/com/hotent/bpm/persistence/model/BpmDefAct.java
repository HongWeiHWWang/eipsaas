package com.hotent.bpm.persistence.model;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;


/**
 * 对象功能:流程授权主表明细 Model对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:xucx
 * 创建时间:2014-03-05 10:04:50
 */
@TableName("bpm_def_act")
public class BpmDefAct extends BaseModel<BpmDefAct>
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2102357407773602509L;

	// id
	@TableId("id_")
	protected String id;
	
	// 流程分管权限主表ID
	@TableField("authorize_id_")
	protected String authorizeId;
	
	//流程类型(流程或流程分类)
	@TableField("type_")
	protected String type;
	
	// 流程KEY或分类id
	@TableField("def_key_")
	protected String defKey;
	
	// 流程名称或分类名称
	@TableField("def_name_")
	protected String defName;
	
	// 流程权限内容
	@TableField("right_content_")
	protected String rightContent;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getAuthorizeId()
	{
		return authorizeId;
	}

	public void setAuthorizeId(String authorizeId)
	{
		this.authorizeId = authorizeId;
	}

	public String getDefKey()
	{
		return defKey;
	}

	public void setDefKey(String defKey)
	{
		this.defKey = defKey;
	}

	public String getDefName()
	{
		return defName;
	}

	public void setDefName(String defName)
	{
		this.defName = defName;
	}

	public String getRightContent()
	{
		return rightContent;
	}

	public void setRightContent(String rightContent)
	{
		this.rightContent = rightContent;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	

}