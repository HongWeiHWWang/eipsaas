package com.hotent.portal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.hotent.base.security.MethodAuthService;
import com.hotent.sys.persistence.manager.SysRoleAuthManager;

/**
 * 获取请求方法的授权信息
 * @author liyg
 *
 */
@Service
@Primary
public class SysMethodAuthService implements MethodAuthService {

	@Resource
	SysRoleAuthManager sysRoleAuthManager;
	
	@Override
	public List<HashMap<String, String>> getMethodAuth() {
		List<HashMap<String, String>> sysRoleAuthAll = sysRoleAuthManager.getSysRoleAuthAll();
		List<HashMap<String, String>> result = new ArrayList<HashMap<String,String>>();
		for (HashMap<String, String> map : sysRoleAuthAll) {
			result.add(map);
		}
		return result;
	}
}
