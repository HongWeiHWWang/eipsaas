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
@TableName("bpm_def_auth_type")
public class BpmDefAuthorizeType extends BaseModel<BpmDefAuthorizeType>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3439559591673686503L;

	public final static class BPMDEFAUTHORIZE_RIGHT_TYPE{
		
		/**流程授权启动类型*/
		public static final String START="start";
		/**流程授权定义类型*/
		public static final String MANAGEMENT="management";
		/**流程授权任务类型*/
		public static final String TASK="task";
		/**流程授权实例类型*/
		public static final String INSTANCE="instance";
		
	}
	
	
	// id
	@TableId("id_")
	protected String id;
	
	//流程授权定义ID
	@TableField("authorize_id_")
	protected String authorizeId;
	
	//流程授权说明
	@TableField("authorize_type_")
	protected String authorizeType;

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

	public String getAuthorizeType()
	{
		return authorizeType;
	}

	public void setAuthorizeType(String authorizeType)
	{
		this.authorizeType = authorizeType;
	}

	
	
}