package com.hotent.bpm.plugin.core.util;

import java.util.ArrayList;
import java.util.List;

import com.hotent.base.util.AppUtil;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserService;

public final class UserConverter {
	/**
	 * 根据BpmIdentity查询并转换
	 * @param bpmIdentities
	 * @param userService
	 * @return 
	 * List<User>
	 * @exception 
	 * @since  1.0.0
	 */
	public static List<IUser> queryAndConvert(List<BpmIdentity> bpmIdentities){
		IUserService userService = AppUtil.getBean(IUserService.class);
		
		List<IUser> userList = new ArrayList<IUser>();
		for(BpmIdentity bpmIdentity:bpmIdentities){
			IUser user = userService.getUserById(bpmIdentity.getId());
			userList.add(user);
		}
		return userList;
	}
}
