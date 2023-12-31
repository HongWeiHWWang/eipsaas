package com.hotent.bpm.persistence.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

@TableName("bpm_report_list")
public class BpmReportList extends BaseModel<BpmReportList>{
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 3070533538675608081L;

	//主键
	@TableId("id_")
	protected String id;
	
	//标题
	@TableField("name_")
	protected String name;
	
	//创建人名称
	@TableField("create_name_")
	protected String createName;
	
	//主组织名称
	@TableField("create_org_name_")
	protected String orgName;
	
	//是否发布 0:未发布 1：已发布
	@TableField("type_")
	protected String type = "0";
	

	@TableField("create_by_")
	protected String createBy;
	
	@TableField("create_org_id_")
	protected String createOrgId ;
	
	public BpmReportList() {
		
	}

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getCreateOrgId() {
		return createOrgId;
	}

	public void setCreateOrgId(String createOrgId) {
		this.createOrgId = createOrgId;
	}
	
}
