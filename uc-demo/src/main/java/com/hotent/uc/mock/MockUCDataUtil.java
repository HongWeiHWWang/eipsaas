package com.hotent.uc.mock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.pagehelper.Page;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.uc.api.model.Group;
import com.hotent.uc.model.Org;
import com.hotent.uc.model.OrgPost;
import com.hotent.uc.model.Role;
import com.hotent.uc.model.User;
import com.hotent.uc.model.UserRole;
import com.hotent.uc.params.user.UserVo;

/**
 * 模拟用户数据 
 * UCFeignService 接口对应的都要实现
 * @author liyanggui
 *
 */
public class MockUCDataUtil {
	
	public static Map<String,User> userMap = new HashMap<String, User>();
	public static List<User> userList = new ArrayList<User>();
	
	public static List<Role> roleList = new ArrayList<Role>();
	
	public static Map<String,List<UserRole>> userRoleMap = new HashMap<String, List<UserRole>>();
	
	static{
		// 密码是 123456
		User user = new User("admin", "系统管理员", "jZae727K08KaOmKSgOaGzww/XVqGr/PKEgIMkjrcbJI=", null);
		user.setId("1");
		user.setEmail("1192668375@qq.com");
		user.setMobile("18316986707");
		user.setStatus(1);
		userMap.put("admin", user);
		userMap.put("1", user);
		userList.add(user);
		
		user = new User("demo", "演示用户", "jZae727K08KaOmKSgOaGzww/XVqGr/PKEgIMkjrcbJI=", null);
		user.setId("2");
		user.setEmail("1192668375@qq.com");
		user.setMobile("18316986707");
		user.setStatus(1);
		userMap.put("demo", user);
		userMap.put("2", user);
		userList.add(user);
		
		Role role = new Role();
		role.setId("1");
		role.setCode("testRoleCode");
		role.setName("演示角色");
		roleList.add(role);
		
		List<UserRole> userRoleList = new ArrayList<UserRole>();
		UserRole userRole = new UserRole();
		userRole.setAccount("demo");
		userRole.setAlias(role.getCode());
		userRole.setUserId("2"); // demo 演示用户的id
		userRole.setRoleId(role.getId());
		userRoleList.add(userRole);
		userRoleMap.put("2", userRoleList);
		userRoleMap.put("demo", userRoleList);
		
		
	}
	
	/**
	 * 根据账号获取用户信息
	 * @param username
	 * @return
	 */
	public static User getUserByAccount(String username) {
		return userMap.get(username);
	}
	
	/**
	 * 根据用户id, 获取用户具有的所有的角色关系
	 * @param userId
	 * @return
	 */
	public static List<UserRole> getUserRoleListByUserId(String userId) {
		if(!userRoleMap.containsKey(userId)){
			return new ArrayList<UserRole>();
		}
		return userRoleMap.get(userId);
	}
	
	/**
	 * 根据账号获取多个用户  
	 * @param accounts 逗号分隔
	 * @return
	 */
	public static List<UserVo> getUserByAccounts(String accounts) {
		List<UserVo> resultUserVo = new ArrayList<UserVo>();
		String[] split = accounts.split(",");
		for (String account : split) {
			User user = userMap.get(account);
			resultUserVo.add(new UserVo(user));
		}
		return resultUserVo;
	}
	
	/**
	 * 获取系统角色 角色列表使用  用户菜单权限授权处理
	 * @return
	 */
	public static PageList<Role> getRoleList() {
		PageList<Role> pageList = new PageList<Role>(roleList);
		return pageList;
	}

	/**
	 * 根据用户userId 获取用户信息
	 * @param userId
	 * @return
	 */
	public static User getUserByUserId(String userId) {
		return userMap.get(userId);
	}
	
	/**
	 * 根据账号或者工号获取用户信息
	 * @param jsonString
	 * @return
	 * @throws IOException 
	 */
	public static UserVo getUserByAccountOrUserNumber(String jsonString) throws IOException {
		JsonNode jsonNode = JsonUtil.toJsonNode(jsonString);
		if(jsonNode.has("account")){
			User user = userMap.get(jsonNode.get("account").asText());
			return new UserVo(user);
		}
		
		if(jsonNode.has("userNumber")){
			User user = userMap.get(jsonNode.get("userNumber").asText());
			return new UserVo(user);
		}
		return null;
	}
	
	/**
	 * 根据组织id获取组织相关的用户  流程表单权限会使用到
	 * @param queryFilter
	 * @return
	 */
	public static Page<User> getUserByOrgId(QueryFilter queryFilter) {
		List<User> resultUserList = new ArrayList<User>();
		Map<String, Object> params = queryFilter.getParams();
		// 根据组织id获取组织相关的用户 , 此处直接模拟返回用户数据
		if(params.containsKey("orgId")){
			resultUserList = userList;
		}
		Page<User> resultPageUser = (Page<User>)resultUserList; 
		return resultPageUser;
	}
	
	/**
	 * 获取角色对应的用户数据 
	 * 
	 * @param codes 角色别名  逗号分隔
	 * @return
	 */
	public static List<UserVo> getUsersByRoleCode(String codes) {
		List<UserVo> userVoList = new ArrayList<UserVo>();
		for (User user : userList) {
			userVoList.add(new UserVo(user));
		}
		return userVoList;
	}
	
	/**
	 * 通过岗位获取用户 
	 * @param postCode 岗位别名 逗号分隔
	 * @return
	 */
	public static List<UserVo> getUserByPost(String postCode) {
		List<UserVo> userVoList = new ArrayList<UserVo>();
		for (User user : userList) {
			userVoList.add(new UserVo(user));
		}
		return userVoList;
	}
	
	/**
	 * 获取职务（多个）下的所有人员
	 * @param codes 职务别名 逗号分隔
	 * @return
	 */
	public static List<UserVo> getUsersByJob(String codes) {
		List<UserVo> userVoList = new ArrayList<UserVo>();
		for (User user : userList) {
			userVoList.add(new UserVo(user));
		}
		return userVoList;
	}
	
	/**
	 * 根据邮件查询所有用户
	 * @param email
	 * @return
	 */
	public static List<User> getByUserEmail(String email) {
		return userList;
	}
	/**
	 * 根据组织ID获取组织的负责人组织关系
	 * @ApiParam(name = "orgId", value = "组织id", required = true) @RequestParam String orgId,
	 *	@ApiParam(name = "isMain", value = "是否主组织", required = false) @RequestParam(required=false) Boolean isMain,
	 *	@ApiParam(name = "demCode", value = "维度编码（不传则为默认维度）", required = false) @RequestParam(required=false) Boolean demCode
	 * @param orgId
	 * @param isMain
	 * @param demCode
	 * @return
	 */
	public static List<User> getChargesByOrdId(String orgId, Boolean isMain,
			Boolean demCode) {
		return userList;
	}
	
	/**
	 * 获取用户的所有组织信息
	 * @param userId
	 * @return
	 */
	public static List<Org> getOrgListByUserId(String userId) {
		return new ArrayList<Org>();
	}
	
	/**
	 * 根据传入的用户id集合，获取用户的权限集合
	 * 此处只返回候选人类型为用户的
	 * 用户组的根据情况自行扩张
	 * @param ids
	 * @return
	 */
	public static Map<String, Map<String, String>> getUserRightMapByIds(
			Set<String> ids) {
		Map<String, Map<String, String>> resultMap = new HashMap<String, Map<String,String>>();
		for (String userId : ids) {
			Map<String, String> temp = new HashMap<String, String>();
			temp.put("user",  StringUtil.convertListToSingleQuotesString(ids));
			resultMap.put(userId,  temp);
		}
		return resultMap;
	}

	public static List<UserVo> getUserByUserIds(String ids) {
		String[] userIds = ids.split(",");
		List<UserVo> userVo = new ArrayList<UserVo>();  
		for (String id : userIds) {
			userVo.add(new UserVo(userMap.get(id)));
		}
		return userVo;
	}
	
	/**
	 * 用户选择器对话框
	 * @param queryFilter
	 * @return
	 */
	public static PageList<User> queryByType(QueryFilter queryFilter) {
		PageList<User> pageUser = new PageList<User>(userList);
		return pageUser;
	}

	/**
	 * 获取用户信息前端显示
	 * @param account
	 * @return
	 */
	public static Map<String, Object> getUserDetailByAccountOrId(String account) {
		return new HashMap<String, Object>();
	}
	
	/**
	 * 获取用户的角色
	 * @param account
	 * @return
	 */
	public static List<Role> getRolesByAccount(String account) {
		if(userRoleMap.containsKey(account)){
			List<UserRole> list = userRoleMap.get(account);
			Set<String> roleAlias = new HashSet<String>();
			for (UserRole userRole : list) {
				roleAlias.add(userRole.getAlias());
			}
			List<Role> result = new ArrayList<Role>();
			for (Role role : roleList) {
				if(roleAlias.contains(role.getCode())){
					result.add(role);
				}
			}
			return result;
		}
		return new ArrayList<Role>();
	}
	
	/**
	 * 根据用户账号获取所属岗位列表
	 * @param account
	 * @return
	 */
	public static List<OrgPost> getOrgPostByUserAccount(String account) {
		return new ArrayList<OrgPost>();
	}
	
	/**
	 * 获取用户主组织
	 * @param userId
	 * @param orElse
	 * @return
	 */
	public static Org getMainGroup(String userId, String orElse) {
		return null;
	}
	
	/**
	 * 根据用户id组获取组织全路径
	 * @param userIds
	 * @return
	 */
	public static List<Map<String, String>> getPathNames(List<String> userIds) {
		return new ArrayList<Map<String,String>>();
	}

	/**
	 * 根据用户id和用户组类型获取其相关用户组(角色，组织，岗位，职务)
	 * @param userId
	 * @param type
	 * @return
	 */
	public static List<Group> getGroupsByUserId(String userId, String type) {
		return new ArrayList<Group>();
	}
	
	

}
