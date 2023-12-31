package com.hotent.uc.params.org;

import java.util.List;
import java.util.Map;

import com.hotent.uc.model.Org;
import com.hotent.uc.model.OrgPost;
import com.hotent.uc.model.OrgUser;

public class OaTransVo {
	/**
	 * 组织列表
	 */
	public List<Org> orgList;
	/**
	 * 岗位列表
	 */
	public List<OrgPost> orgPostList;
	/**
	 * 用户、组织、岗位关系
	 */
	public Map<String,List<OrgUser>> orgUserList;
	/**
	 * 组织对应ID
	 */
	public Map<String,String> orgChangeIdMap;
	/**
	 * 岗位对应ID
	 */
	public Map<String,String> postChangeIdMap;
	
	public List<Org> getOrgList() {
		return orgList;
	}
	public void setOrgList(List<Org> orgList) {
		this.orgList = orgList;
	}
	public List<OrgPost> getOrgPostList() {
		return orgPostList;
	}
	public void setOrgPostList(List<OrgPost> orgPostList) {
		this.orgPostList = orgPostList;
	}
	public Map<String, List<OrgUser>> getOrgUserList() {
		return orgUserList;
	}
	public void setOrgUserList(Map<String, List<OrgUser>> orgUserList) {
		this.orgUserList = orgUserList;
	}
	public Map<String, String> getOrgChangeIdMap() {
		return orgChangeIdMap;
	}
	public void setOrgChangeIdMap(Map<String, String> orgChangeIdMap) {
		this.orgChangeIdMap = orgChangeIdMap;
	}
	public Map<String, String> getPostChangeIdMap() {
		return postChangeIdMap;
	}
	public void setPostChangeIdMap(Map<String, String> postChangeIdMap) {
		this.postChangeIdMap = postChangeIdMap;
	}
	
}
