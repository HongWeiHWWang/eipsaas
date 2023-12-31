package com.hotent.uc.api.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.uc.api.service.IOrgService;
import com.hotent.uc.api.service.IUserGroupService;
import com.hotent.uc.api.service.IUserService;

/**
 * 类 {@code DefaultOrgService} 身份服务的实现
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月5日
 */
@Service("defaultOrgEngine")
public class DefaultOrgService implements IOrgService {
	@Resource
	IUserGroupService userGroupService;
	
	@Resource
	IUserService userService;

	@Override
	public IUserGroupService getUserGroupService() {
		return userGroupService;
	}

	@Override
	public IUserService getUserService() {
		return userService;
	}
}
