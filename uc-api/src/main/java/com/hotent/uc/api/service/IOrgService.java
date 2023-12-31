package com.hotent.uc.api.service;

/**
 * 接口 {@code IOrgService} 身份服务
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月5日
 */
public interface IOrgService {

	/**
	 * 获取用户组关联Service
	 * @return 用户组关联Service
	 */
	IUserGroupService getUserGroupService();


	/**
	 * 获取用户Service接口
	 * @return 用户Service接口
	 */
	IUserService getUserService();
}
