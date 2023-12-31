package com.hotent.uc.manager;

import java.util.List;
import java.util.Map;

import com.hotent.base.manager.BaseManager;
import com.hotent.base.model.CommonResult;
import com.hotent.uc.model.Role;
import com.hotent.uc.params.role.RoleVo;
import com.hotent.uc.params.user.UserVo;

/**
 * 
 * <pre> 
 * 描述：角色管理 处理接口
 * 构建组：x5-bpmx-platform
 * 作者:ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2016-06-30 10:28:04
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface RoleManager extends BaseManager<Role>{
	
	/**
	 * 删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	
	
	
	
   Role getByAlias(String alias);
   
    /**
     * 根据用户ID获取角色列表
     * @param userId
     * @return
     */
    List<Role> getListByUserId(String userId);
   
    /**
     * 根据用户账号获取角色列表
     * @param userId
     * @return
     */
    List<Role> getListByAccount(String account);
	
    /**
	 * 添加角色
	 * @param user
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> addRole(RoleVo role) throws Exception;
	
	/**
	 * 删除角色
	 * @param codes
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> deleteRole(String codes) throws Exception;
	
	/**
	 * 角色更新
	 * @param user
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> updateRole(RoleVo role) throws Exception;
	
	
	
	/**
	 * 获取角色信息
	 * @param json
	 * @return
	 * @throws Exception
	 */
	Role getRole(String code) throws Exception;
	
	/**
	 * 分配用户（按用户）
	 * @param code
	 * @param accounts
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> saveUserRole(String code,String accounts)throws Exception;
	
	/**
	 * 分配用户（按组织）
	 * @param code
	 * @param orgCodes
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> addUserRoleByOrg(String code,String orgCodes)throws Exception;
	
	/**
	 * 角色移除用户
	 * @param code
	 * @param accounts
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> removeUserRole(String code,String accounts)throws Exception;
	
	/**
	 * 获取用户所属角色列表
	 * @param account
	 * @return
	 * @throws Exception
	 */
	List<Role> getRolesByUser(String account)throws Exception;
	
	/**
	 * 获取角色（多个）中的用户
	 * @param codes
	 * @return
	 * @throws Exception
	 */
	List<UserVo> getUsersByRoleCode(String codes)throws Exception;
	
	List<Role> getOrgRoleList(Map<String,Object> params);
	/**
	 * 禁用角色
	 * @param codes
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> forbiddenRoles(String codes)throws Exception;
	/**
	 * 激活角色
	 * @param codes
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> activateRoles(String codes)throws Exception;
	
	/**
	 *  根据时间获取角色数据（数据同步）
	 * @param btime
	 * @param etime
	 * @return
	 * @throws Exception
	 */
	List<Role> getRoleByTime(String btime,String etime) throws Exception ;
	
	/**
	 * 删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removeUserRolePhysical() throws Exception ;
	
	/**
	 * 查询角色编码是否已存在
	 * @param code
	 * @return
	 * @throws Exception
	 */
	CommonResult<Boolean> isCodeExist(String code) throws Exception ;
	/**
	 * 根据角色id删除角色
	 * @param ids
	 * @return
	 * @throws Exception 
	 */
	CommonResult<String> deleteRoleByIds(String ids) throws Exception;

	/**
	 * 根据角色别名获取除这个角色之外的所有角色
	 * @param code
	 * @return
	 */
	List<Role> getOrgRoleListNotCode(String code);

	CommonResult<String> saveUserRoles(String codes, String account);
}
