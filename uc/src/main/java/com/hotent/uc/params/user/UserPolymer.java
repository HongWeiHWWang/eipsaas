package com.hotent.uc.params.user;

import java.util.List;

import com.hotent.base.util.AppUtil;
import com.hotent.uc.manager.UserManager;
import com.hotent.uc.model.User;
import com.hotent.uc.util.OperateLogUtil;
import com.hotent.uc.util.UpdateCompare;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 新增用户和编辑用户的聚合类
 * @author heyifan
 * @date 2018年1月17日
 */
@ApiModel
public class UserPolymer implements UpdateCompare {
	private UserVo user;
	
	private List<UserPolymerOrgPos> orgsPoses;
	
	private List<UserPolymerRole> roles;
	@ApiModelProperty(name="adding", notes="是否新增数据(默认为false)", required=false)
	private Boolean adding = false;

	public UserVo getUser() {
		return user;
	}

	public void setUser(UserVo user) {
		this.user = user;
	}

	public List<UserPolymerOrgPos> getOrgsPoses() {
		return orgsPoses;
	}

	public void setOrgsPoses(List<UserPolymerOrgPos> orgsPoses) {
		this.orgsPoses = orgsPoses;
	}

	public List<UserPolymerRole> getRoles() {
		return roles;
	}

	public void setRoles(List<UserPolymerRole> roles) {
		this.roles = roles;
	}

	public Boolean getAdding() {
		return adding;
	}

	public void setAdding(Boolean adding) {
		this.adding = adding;
	}

	@Override
	public String compare() throws Exception {
	    UserManager service =	AppUtil.getBean(UserManager.class);
	    User oldVo=service.getByAccount(this.getUser().getAccount());
	    UserVo newVo=this.getUser();
	    newVo.setVersion(null);
	    newVo.setParams(null);
		return OperateLogUtil.compare(newVo,UserVo.changeVo(oldVo));
	}

}