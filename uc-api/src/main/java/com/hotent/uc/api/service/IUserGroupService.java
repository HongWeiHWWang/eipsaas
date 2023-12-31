package com.hotent.uc.api.service;

import java.util.List;
import java.util.Map;

import com.hotent.uc.api.model.GroupType;
import com.hotent.uc.api.model.IGroup;


/**
 * 接口 {@code IUserGroupService} 用户组服务
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月5日
 */
public interface IUserGroupService{
	

	/**
	 * 根据用户ID和组类别获取该用户所在的用户组
	 * <pre>
	 *  例如：张三在可以有多个岗位，这里就获取岗位他的岗位列表
	 * </pre>
	 * @param groupType		用户组类型
	 * @param userId		用户ID
	 * @return 该用户所在的用户组
	 */
	List<IGroup>  getGroupsByUserIdOrAccount(String groupType,String userId);
	
	
	/**
	 * 根据用户ID或账号获取用户所属的组,这里不对组类别进行区分，返回统一的用户组列表，可能包括角色，部门等
	 * @param userId 用户ID
	 * @return 用户组列表，可能包括角色，部门等
	 */
	List<IGroup> getGroupsByUserIdOrAccount(String userId);
	
	
	/**
	 * 根据组织ID和类型获取用户组对象
	 * @param groupType 组织类型
	 * @param code		组织别名
	 * @return 用户组对象
	 */
	IGroup getGroupByIdOrCode(String groupType,String code);
	
	/**
	 * 返回当前的用户组类型
	 * <pre>
	 *  返回平台支持的组织类型：
	 *  比如：
	 *  role：角色
	 *  pos: 岗位
	 *  org: 部门
	 * </pre>
	 * @return 当前的用户组类型
	 */
	List<GroupType> getGroupTypes();
	
	/**
	 * 根据用户id或者账号获取用户的所有用户组，并转换成map
	 * @param id 用户id
	 * @return 用户的所有用户组，并转换成map
	 */
	Map<String, List<IGroup>> getGroupsMapUserIdOrAccount(String id);
	
}
