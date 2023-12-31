package com.hotent.uc.api.impl.model.permission;
import java.util.HashSet;
import java.util.Set;

import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;

/**
 * 类 {@code UsersPermission} 用户权限
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月5日
 */
public class UsersPermission extends AbstarctPermission  {

	@Override
	public String getTitle() {
		return "用户";
	}

	@Override
	public String getType() {
		return "user";
	}



	@Override
	public Set<String> getCurrentProfile() {
		IUser user=ContextUtil.getCurrentUser();
		Set<String> userSet=new HashSet<String>();
		userSet.add(user.getUserId());
		return userSet;
	}



}
