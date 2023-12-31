package com.hotent.uc.api.context;

import java.util.Locale;

import com.hotent.uc.api.model.IGroup;
import com.hotent.uc.api.model.IUser;



/**
 * 接口 {@code ICurrentContext} 获取上下文对象数据
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月5日
 */
public interface ICurrentContext {
	
	/**
	 * 当前岗位 {@value}
	 */
	public static final String CURRENT_ORG = "current_org";
	
	/**
	 * 获取当前登录用户
	 * @return 当前登录用户
	 */
	IUser getCurrentUser();
	
	/**
	 * 获取当前执行人
	 * @return 当前执行人
	 */
	String getCurrentUserId();
	/**
	 * 获取当前组织
     * @return 当前组织
	 */
	IGroup getCurrentGroup();
	
	/**
	 * 清理当前用户 
	 */
	void clearCurrentUser();
	
	/**
	 * 设置当前用户
	 * @param user 用户
	 */
	void setCurrentUser(IUser user);
	
	/**
	 * 根据用户帐号设置上下文用户
	 * @param account 帐号
	 */
	void setCurrentUserByAccount(String account);
	/**
	 * 获取当前组织
	 */
	void setCurrentGroup(IGroup group);
	
	/**
	 * 
	 * 获取当前Locale
	 * @return 国际化语言
	 */
	Locale getLocale();
	
	/**
	 * 
	 * 设置上下文local
	 * @param local 国际化语言
	 */
	void setLocale(Locale local );
	
	/**
	 * 
	 * 清除上下文local 
	 */
	void clearLocale();
}