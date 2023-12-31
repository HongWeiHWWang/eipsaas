package com.hotent.base.context;

/**
 * 当前登录用户的上下文数据
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月9日
 */
public interface BaseContext {
	/**
	 * 获取当前登录用户ID
	 * @return
	 */
	String getCurrentUserId();
	
	/**
	 * 获取当前登录用户账号
	 * @return
	 */
	String getCurrentUserAccout();
	
	/**
	 * 获取当前登录用户所属组织ID
	 * @return
	 */
	String getCurrentOrgId();
	/**
	 * 获取当前登录用户所在租户ID
	 * @return
	 */
	String getCurrentTenantId();
	
    void setTempTenantId(String tenantId);
    
    void clearTempTenantId();
}
