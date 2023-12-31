package com.hotent.uc.model;

import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

import org.apache.commons.lang.builder.ToStringBuilder;

public class OrgUser extends UcBaseModel  {

	private static final long serialVersionUID = 2541505317450158908L;
	/**
	 * 主关系
	 */
	public static final Integer MASTER_YES=1;
	
	/**
	 * 非主关系
	 */
	public static final Integer MASTER_NO=0;
	
	
	/**
	* id_
	*/
	@ApiModelProperty(name="id",notes="用户组织关系id")
	protected String id; 
	
	/**
	* org_id_
	*/
	@ApiModelProperty(name="orgId",notes="组织id")
	protected String orgId; 
	
	/**
	* user_id_
	*/
	@ApiModelProperty(name="userId",notes="用户id")
	protected String userId; 
	
	/**
	* 0:非主部门，1：主部门
	*/
	@ApiModelProperty(name="isMaster",notes="0:非主组织，1：主组织")
	protected Integer isMaster; 
	
	/**
	 * 0： 非负责人 ， 1: 负责人， 2 部门的主负责人
	 */
	@ApiModelProperty(name="isCharge",notes="0： 非负责人 ， 1: 负责人， 2 部门的主负责人")
	protected Integer isCharge=0;
	
	/**
	* rel_id_
	*/
	@ApiModelProperty(name="relId",notes="岗位id")
	protected String relId; 
	
	/**
	 * 开始生效日期
	 */
	@ApiModelProperty(name="startDate",notes="开始生效日期")
	protected LocalDateTime startDate; 
	
	/**
	 * 结束日期
	 */
	@ApiModelProperty(name="endDate",notes="结束日期")
	protected LocalDateTime endDate; 
	
	/**
	 * 是否生效
	 */
	@ApiModelProperty(name="isRelActive",notes="是否生效")
	protected int isRelActive = 1;
	
	/*扩展字段，仅用于关联查询时使用*/
	@ApiModelProperty(name="orgCode",notes="组织代码", hidden=true)
	protected String orgCode;
	@ApiModelProperty(name="posCode",notes="岗位代码", hidden=true)
	protected String posCode;
	
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 id_
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	/**
	 * 返回 org_id_
	 * @return
	 */
	public String getOrgId() {
		return this.orgId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	/**
	 * 返回 user_id_
	 * @return
	 */
	public String getUserId() {
		return this.userId;
	}
	
	public void setIsMaster(Integer isMaster) {
		this.isMaster = isMaster;
	}
	
	/**
	 * 返回 0:非主部门，1：主部门
	 * @return
	 */
	public Integer getIsMaster() {
		return this.isMaster;
	}
	
	public Integer getIsCharge() {
		return isCharge;
	}

	public void setIsCharge(Integer isCharge) {
		this.isCharge = isCharge;
	}

	public void setRelId(String relId) {
		this.relId = relId;
	}
	
	/**
	 * 返回 rel_id_
	 * @return
	 */
	public String getRelId() {
		return this.relId;
	}
	
	/**
	 * 返回 开始生效日期
	 * @return
	 */
	public LocalDateTime getStartDate() {
		return startDate;
	}

	
	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	/**
	 * 返回 结束日期
	 * @return
	 */
	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	/**
	 * 返回 是否生效
	 * @return
	 */
	public int getIsRelActive() {
		return isRelActive;
	}

	public void setIsRelActive(int isRelActive) {
		this.isRelActive = isRelActive;
	}
	
	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getPosCode() {
		return posCode;
	}

	public void setPosCode(String posCode) {
		this.posCode = posCode;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("orgId", this.orgId) 
		.append("userId", this.userId) 
		.append("isMaster", this.isMaster) 
		.append("relId", this.relId) 
		.append("startDate", this.startDate) 
		.append("endDate", this.endDate) 
		.append("isRelActive", this.isRelActive) 
		.append("updateTime",this.updateTime)
		.append("isDelete",this.isDelete)
		.append("version",this.version)
		.toString();
	}

}
