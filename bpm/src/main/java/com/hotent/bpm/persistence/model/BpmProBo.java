package com.hotent.bpm.persistence.model;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

/**
 * 对象功能:流程跟业务定义之间的关系
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zyg
 * 创建时间:2014-08-13 11:16:12
 */
@SuppressWarnings("serial")
@TableName("bpm_pro_bo")
public class BpmProBo extends BaseModel<BpmProBo>{
	
	@TableId("id_")
	protected String  id; /*主键*/
	
	@TableField("process_id_")
	protected String  processId; /*流程定义ID*/
	
	@TableField("process_key_")
	protected String  processKey; /*流程定义KEY*/
	
	@TableField("bo_code_")
	protected String  boCode; /*业务对象 标识code*/
	
	@TableField("bo_name_")
	protected String  boName; /*业务对象 名称*/
	
	@TableField("creator_id_")
	protected String  creatorId; /*创建者ID*/
	
	@TableField("create_time_")
	protected Date  createTime; /*创建时间*/
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getProcessId()
	{
		return processId;
	}
	public void setProcessId(String processId)
	{
		this.processId = processId;
	}
	public String getProcessKey()
	{
		return processKey;
	}
	public void setProcessKey(String processKey)
	{
		this.processKey = processKey;
	}
	public String getBoCode()
	{
		return boCode;
	}
	public void setBoCode(String boCode)
	{
		this.boCode = boCode;
	}
	public String getBoName()
	{
		return boName;
	}
	public void setBoName(String boName)
	{
		this.boName = boName;
	}
	public String getCreatorId()
	{
		return creatorId;
	}
	public void setCreatorId(String creatorId)
	{
		this.creatorId = creatorId;
	}
	
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof BpmProBo)){//地址比较
			return this==obj;
		}
		BpmProBo bpb = (BpmProBo) obj;
		if(bpb.getId().equals(this.id)){
			return true;
		}
		if(bpb.getBoCode().equals(this.boCode)&&(bpb.getProcessId().equals(this.processId)||bpb.getProcessKey().equals(this.processKey))){
			return true;
		}
		return false;
	}

}