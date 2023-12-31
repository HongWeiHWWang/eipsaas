package com.hotent.uc.params.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 副本数据同步获取参数类
 * @author zhangxw
 *
 */
@ApiModel
public class DataSyncObject {

	@ApiModelProperty(name="btime",notes="开始时间")
	private String btime;
	@ApiModelProperty(name="etime",notes="结束时间")
	private String etime;
	
	@ApiModelProperty(name="isUser",notes="获取用户数据")
	private Boolean isUser;
	@ApiModelProperty(name="isDem",notes="获取维度数据")
	private Boolean isDem;
	@ApiModelProperty(name="isOrg",notes="获取组织数据")
	private Boolean isOrg;
	@ApiModelProperty(name="isOrgAuth",notes="获取分级组织数据")
	private Boolean isOrgAuth;
	@ApiModelProperty(name="isJob",notes="获取职务数据")
	private Boolean isJob;
	@ApiModelProperty(name="isPost",notes="获取岗位数据")
	private Boolean isPost;
	@ApiModelProperty(name="isOrgUser",notes="获取用户组织关系数据")
	private Boolean isOrgUser;
	@ApiModelProperty(name="isUnder",notes="获取组织中用户下属数据")
	private Boolean isUnder;
	@ApiModelProperty(name="isOrgRole",notes="获取组织角色数据")
	private Boolean isOrgRole;
	
	@ApiModelProperty(name="isParams",notes="获取用户组织参数数据")
	private Boolean isParams;
	@ApiModelProperty(name="isUserParams",notes="获取用户参数数据")
	private Boolean isUserParams;
	@ApiModelProperty(name="isOrgParams",notes="获取组织参数数据")
	private Boolean isOrgParams;
	
	@ApiModelProperty(name="isRole",notes="获取角色数据")
	private Boolean isRole;
	@ApiModelProperty(name="isUserRole",notes="获取用户角色关系数据")
	private Boolean isUserRole;
	
	@ApiModelProperty(name="isGroup",notes="获取群组数据")
	private Boolean isGroup;
	
	@ApiModelProperty(name="isRelType",notes="获取汇报分类数据")
	private Boolean isRelType;
	@ApiModelProperty(name="isUserRel",notes="获取汇报线节点数据")
	private Boolean isUserRel;
	@ApiModelProperty(name="demCodes",notes="维度编码（多个用“,”号隔开）")
	private String demCodes;
	@ApiModelProperty(name="orgCodes",notes="组织编码（多个用“,”号隔开）")
	private String orgCodes;
	@ApiModelProperty(name="postCodes",notes="岗位编码（多个用“,”号隔开）")
	private String postCodes;
	@ApiModelProperty(name="jobCodes",notes="职务编码（多个用“,”号隔开）")
	private String jobCodes;
	
	public String getBtime() {
		return btime;
	}
	public void setBtime(String btime) {
		this.btime = btime;
	}
	public String getEtime() {
		return etime;
	}
	public void setEtime(String etime) {
		this.etime = etime;
	}
	public Boolean getIsUser() {
		return isUser;
	}
	public void setIsUser(Boolean isUser) {
		this.isUser = isUser;
	}
	public Boolean getIsDem() {
		return isDem;
	}
	public void setIsDem(Boolean isDem) {
		this.isDem = isDem;
	}
	public Boolean getIsOrg() {
		return isOrg;
	}
	public void setIsOrg(Boolean isOrg) {
		this.isOrg = isOrg;
	}
	public Boolean getIsOrgAuth() {
		return isOrgAuth;
	}
	public void setIsOrgAuth(Boolean isOrgAuth) {
		this.isOrgAuth = isOrgAuth;
	}
	public Boolean getIsJob() {
		return isJob;
	}
	public void setIsJob(Boolean isJob) {
		this.isJob = isJob;
	}
	public Boolean getIsPost() {
		return isPost;
	}
	public void setIsPost(Boolean isPost) {
		this.isPost = isPost;
	}
	public Boolean getIsOrgUser() {
		return isOrgUser;
	}
	public void setIsOrgUser(Boolean isOrgUser) {
		this.isOrgUser = isOrgUser;
	}
	public Boolean getIsUnder() {
		return isUnder;
	}
	public void setIsUnder(Boolean isUnder) {
		this.isUnder = isUnder;
	}
	public Boolean getIsOrgRole() {
		return isOrgRole;
	}
	public void setIsOrgRole(Boolean isOrgRole) {
		this.isOrgRole = isOrgRole;
	}
	public Boolean getIsParams() {
		return isParams;
	}
	public void setIsParams(Boolean isParams) {
		this.isParams = isParams;
	}
	public Boolean getIsUserParams() {
		return isUserParams;
	}
	public void setIsUserParams(Boolean isUserParams) {
		this.isUserParams = isUserParams;
	}
	public Boolean getIsOrgParams() {
		return isOrgParams;
	}
	public void setIsOrgParams(Boolean isOrgParams) {
		this.isOrgParams = isOrgParams;
	}
	public Boolean getIsRole() {
		return isRole;
	}
	public void setIsRole(Boolean isRole) {
		this.isRole = isRole;
	}
	public Boolean getIsUserRole() {
		return isUserRole;
	}
	public void setIsUserRole(Boolean isUserRole) {
		this.isUserRole = isUserRole;
	}
	public Boolean getIsGroup() {
		return isGroup;
	}
	public void setIsGroup(Boolean isGroup) {
		this.isGroup = isGroup;
	}
	public Boolean getIsRelType() {
		return isRelType;
	}
	public void setIsRelType(Boolean isRelType) {
		this.isRelType = isRelType;
	}
	public Boolean getIsUserRel() {
		return isUserRel;
	}
	public void setIsUserRel(Boolean isUserRel) {
		this.isUserRel = isUserRel;
	}
	public String getDemCodes() {
		return demCodes;
	}
	public void setDemCodes(String demCodes) {
		this.demCodes = demCodes;
	}
	public String getOrgCodes() {
		return orgCodes;
	}
	public void setOrgCodes(String orgCodes) {
		this.orgCodes = orgCodes;
	}
	public String getPostCodes() {
		return postCodes;
	}
	public void setPostCodes(String postCodes) {
		this.postCodes = postCodes;
	}
	public String getJobCodes() {
		return jobCodes;
	}
	public void setJobCodes(String jobCodes) {
		this.jobCodes = jobCodes;
	}
}
