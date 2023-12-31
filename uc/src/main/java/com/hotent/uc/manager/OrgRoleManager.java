package com.hotent.uc.manager;

import java.sql.SQLException;

import com.hotent.uc.model.OrgRole;
import com.hotent.uc.model.Role;
import com.hotent.base.manager.BaseManager;

/**
 * 
 * <pre> 
 * 描述：组织角色管理 处理接口
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2017-12-25 10:25:20
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface OrgRoleManager extends   BaseManager<OrgRole>{
	
	/**
	 * 删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	
	
	
	/**
	 * 添加组织角色
	 * @param orgId
	 * @param roleIds
	 * @param isInherit
	 * @throws SQLException 
	 */
	void addOrgRole(String orgId,Role role,int isInherit) throws SQLException;
	
	void delByOrgIdAndRoleId(String orgId,String roleId);
	
}