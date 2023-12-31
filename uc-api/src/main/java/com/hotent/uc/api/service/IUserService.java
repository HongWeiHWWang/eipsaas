package com.hotent.uc.api.service;

import java.util.List;

import com.hotent.uc.api.model.IUser;

/**
 * 接口 {@code IUserService} 用户服务
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月5日
 */
public interface IUserService{
	
	/**
	 * 根据用户ID获取用户的对象
	 * @param userId 用户ID
	 * @return 用户的对象
	 */
	IUser getUserById(String userId);
	
	
	/**
	 * 根据用户帐号获取用户对象
	 * @param account 用户帐号
	 * @return 用户对象
	 */
	IUser getUserByAccount(String account);
	
	
	/**
	 * 根据组织id和组织类型获取用户列表
	 * <pre>
	 * 	根据组织类型获取该组织下的人员：
	 *  比如：给定角色ID和类型为角色，获取这个角色下的人员列表
	 * </pre>
	 * @param groupId		组织列表
	 * @param groupType		组织类型
	 * @return 用户列表
	 */
	List<IUser> getUserListByGroup(String groupType, String groupId);
 
	/**
	 * 返回用户集合
	 * @param email 邮箱地址
	 * @return 用户集合
	 */
	List<IUser> getByEmail(String email);
	
	/**
	 * 根据多个用户帐号返回用户信息
     * @param accounts 多个用户帐号
     * @return 用户信息
	 */
	List<IUser> getUserByAccounts(String accounts);
	
}
