package com.hotent.uc.api.impl.model;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.hotent.base.entity.BaseModel;
import com.hotent.uc.api.constant.GroupStructEnum;
import com.hotent.uc.api.constant.GroupTypeConstant;
import com.hotent.uc.api.model.IGroup;
import com.hotent.uc.api.model.IdentityType;

 /**
 * 类 {@code OrgRel} 组织关联关系 实体对象
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月5日
 */
public class OrgRel extends BaseModel<OrgRel> implements IGroup{

	private static final long serialVersionUID = 4780392866955361679L;

	/**
	* 主键ID
	*/
	protected String id; 
	
	/**
	* 组织ID
	*/
	protected String orgId; 
	
	/**
	* rel_def_id_
	*/
	protected String relDefId; 
	
	/**
	* rel_name_
	*/
	protected String name; 
	
	/**
	* rel_code_
	*/
	protected String code;
     /**
      * 组织名称
      */
	protected String orgName;
     /**
      * 角色名称
      */
	protected String jobName; 
	
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgName() {
		return this.orgName;
	}
	
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobName() {
		return this.jobName;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}
	
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgId() {
		return this.orgId;
	}
	
	public void setRelDefId(String relDefId) {
		this.relDefId = relDefId;
	}

	public String getRelDefId() {
		return this.relDefId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("orgId", this.orgId) 
		.append("relDefId", this.relDefId) 
		.append("name", this.name) 
		.append("code", this.code) 
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
		return Long.valueOf(0);
	}

	public String getGroupType() {
		return GroupTypeConstant.POSITION.key();
	}

	public GroupStructEnum getStruct() {
		return GroupStructEnum.PLAIN;
	}

	public String getParentId() {
		return orgId;
	}

	public String getPath() {
		return null;
	}

	public Map<String, Object> getParams() {
		return null;
	}

	@Override
	public String getName() {
		return this.name;
	}
}