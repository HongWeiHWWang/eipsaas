package com.hotent.uc.manager;

import java.time.LocalDateTime;
import java.util.List;

import com.hotent.base.manager.BaseManager;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.uc.model.UserRole;

/**
 * 
 * <pre> 
 * 描述：用户角色管理 处理接口
 * 构建组：x5-bpmx-platform
 * 作者:ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2016-06-30 10:28:34
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface UserRoleManager extends BaseManager<UserRole>{
	
	
	/**
	 * 删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	
	
	
	/**
	 * 根据用户和角色id 查询 关联关系。
	 * @param roleId
	 * @param userId
	 * @return
	 */
	UserRole getByRoleIdUserId(String roleId,String userId);
	
	/**
	 * 获取用户的角色。
	 * @param userId
	 * @return
	 */
	List<UserRole> getListByUserId(String userId);
	
	/**
	 * 根据角色ID查询关联的用户。
	 * @param roleId
	 * @return
	 */
	List<UserRole> getListByRoleId(String roleId);
	
	/**
	 * 根据角色别名查询关联的用户。
	 * @param roleId
	 * @return
	 */
	List<UserRole> getListByAlias(String alias);
	
	/**
	 * 保存用户与角色的关系(排他性保存，除了传入的角色以外，用户拥有的其他角色会被删除)
	 * @param account 用户账号
	 * @param roleCode 角色代码
	 */
	void saveUserRole(String account, String...roleCodes) throws Exception;
	
	/**
	 * 获取用户的角色列表
	 * @param queryFilter
	 * @return
	 */
	PageList<UserRole> getUserRolePage(QueryFilter queryFilter);



	void removeByUserId(String id, LocalDateTime now);



	void removeByRoleId(String roleId, LocalDateTime now);
}
