package com.hotent.uc.params.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import com.hotent.uc.model.Demension;
import com.hotent.uc.model.Org;
import com.hotent.uc.model.OrgAuth;
import com.hotent.uc.model.OrgJob;
import com.hotent.uc.model.OrgParams;
import com.hotent.uc.model.OrgPost;
import com.hotent.uc.model.OrgRole;
import com.hotent.uc.model.OrgUser;
import com.hotent.uc.model.Params;
import com.hotent.uc.model.Role;
import com.hotent.uc.model.User;
import com.hotent.uc.model.UserGroup;
import com.hotent.uc.model.UserParams;
import com.hotent.uc.model.UserRel;
import com.hotent.uc.model.UserRole;
import com.hotent.uc.model.UserUnder;

/**
 * 副本数据同步获取返回值类
 * @author zhangxw
 *
 */
@ApiModel
public class DataSyncVo {
	@ApiModelProperty(name="userList",notes="用户列表")
	private List<User> userList;
	@ApiModelProperty(name="demList",notes="维度列表")
	private List<Demension> demList;
	@ApiModelProperty(name="orgList",notes="组织列表")
	private List<Org> orgList;
	@ApiModelProperty(name="orgAuthList",notes="获取分级组织数据")
	private List<OrgAuth> orgAuthList;
	@ApiModelProperty(name="jobList",notes="职务列表")
	private List<OrgJob> jobList;
	@ApiModelProperty(name="postList",notes="岗位列表")
	private List<OrgPost> postList;
	@ApiModelProperty(name="orgUserList",notes="用户组织关系列表")
	private List<OrgUser> orgUserList;
	@ApiModelProperty(name="underList",notes="组织中用户下属列表")
	private List<UserUnder> underList;
	@ApiModelProperty(name="orgRoleList",notes="组织角色列表")
	private List<OrgRole> orgRoleList;
	
	@ApiModelProperty(name="paramsList",notes="用户组织参数列表")
	private List<Params> paramsList;
	@ApiModelProperty(name="userParamList",notes="用户参数列表")
	private List<UserParams> userParamList;
	@ApiModelProperty(name="orgParamList",notes="组织参数列表")
	private List<OrgParams> orgParamList;
	
	@ApiModelProperty(name="roleList",notes="角色列表")
	private List<Role> roleList;
	@ApiModelProperty(name="userRoleList",notes="用户角色关系列表")
	private List<UserRole> userRoleList;
	
	@ApiModelProperty(name="groupList",notes="获取群组数据")
	private List<UserGroup> groupList;
	
	@ApiModelProperty(name="userRelList",notes="汇报线节点列表")
	private List<UserRel> userRelList;
	public List<User> getUserList() {
		return userList;
	}
	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
	public List<Demension> getDemList() {
		return demList;
	}
	public void setDemList(List<Demension> demList) {
		this.demList = demList;
	}
	public List<Org> getOrgList() {
		return orgList;
	}
	public void setOrgList(List<Org> orgList) {
		this.orgList = orgList;
	}
	public List<OrgAuth> getOrgAuthList() {
		return orgAuthList;
	}
	public void setOrgAuthList(List<OrgAuth> orgAuthList) {
		this.orgAuthList = orgAuthList;
	}
	public List<OrgJob> getJobList() {
		return jobList;
	}
	public void setJobList(List<OrgJob> jobList) {
		this.jobList = jobList;
	}
	public List<OrgPost> getPostList() {
		return postList;
	}
	public void setPostList(List<OrgPost> postList) {
		this.postList = postList;
	}
	public List<OrgUser> getOrgUserList() {
		return orgUserList;
	}
	public void setOrgUserList(List<OrgUser> orgUserList) {
		this.orgUserList = orgUserList;
	}
	public List<UserUnder> getUnderList() {
		return underList;
	}
	public void setUnderList(List<UserUnder> underList) {
		this.underList = underList;
	}
	public List<OrgRole> getOrgRoleList() {
		return orgRoleList;
	}
	public void setOrgRoleList(List<OrgRole> orgRoleList) {
		this.orgRoleList = orgRoleList;
	}
	public List<Params> getParamsList() {
		return paramsList;
	}
	public void setParamsList(List<Params> paramsList) {
		this.paramsList = paramsList;
	}
	public List<UserParams> getUserParamList() {
		return userParamList;
	}
	public void setUserParamList(List<UserParams> userParamList) {
		this.userParamList = userParamList;
	}
	public List<OrgParams> getOrgParamList() {
		return orgParamList;
	}
	public void setOrgParamList(List<OrgParams> orgParamList) {
		this.orgParamList = orgParamList;
	}
	public List<Role> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}
	public List<UserRole> getUserRoleList() {
		return userRoleList;
	}
	public void setUserRoleList(List<UserRole> userRoleList) {
		this.userRoleList = userRoleList;
	}
	public List<UserGroup> getGroupList() {
		return groupList;
	}
	public void setGroupList(List<UserGroup> groupList) {
		this.groupList = groupList;
	}
	public List<UserRel> getUserRelList() {
		return userRelList;
	}
	public void setUserRelList(List<UserRel> userRelList) {
		this.userRelList = userRelList;
	}
	
}
