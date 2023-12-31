package com.hotent.uc.api.impl.model.permission;

import java.util.Map;
import java.util.Set;

import com.hotent.uc.api.model.IPermission;

/**
 * 类 {@code EveryOnePermission} 所有人权限
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月5日
 */
public class EveryOnePermission implements IPermission {

	@Override
	public String getTitle() {
		return "所有人";
	}

	@Override
	public String getType() {
		return "everyone";
	}

	

	@Override
	public Set<String> getCurrentProfile() {
		return null;
	}

	@Override
	public boolean hasRight(String json, Map<String, Set<String>> currentMap) {
		return true;
	}

}
