package com.hotent.sys.persistence.manager;

import java.util.HashMap;
import java.util.List;

import com.hotent.base.manager.BaseManager;
import com.hotent.sys.persistence.model.SysRoleAuth;
import com.hotent.sys.persistence.param.SysRoleAuthParam;

/**
 * 
 * <pre> 
 * 描述：角色权限配置 处理接口
 * 构建组：x6
 * 作者:liyg
 * 邮箱:liygui@jee-soft.cn
 * 日期:2018-06-29 14:27:46
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface SysRoleAuthManager extends BaseManager<SysRoleAuth>{
	
	/**
	 * 根据角色别名获取角色权限信息
	 * @param roleAlias
	 * @return
	 */
	List<SysRoleAuth> getSysRoleAuthByRoleAlias(String roleAlias);
	
	/**
	 * 根据角色别名获取菜单资源权限
	 * @param roleAlias
	 * @return
	 */
	List<String> getMenuAliasByRoleAlias(String roleAlias);
	
	/**
	 * 根据角色别名获取 后台请求地址权限
	 * @param roleAlias
	 * @return
	 */
	List<String> getMethodAliasByRoleAlias(String roleAlias);
	
	/**
	 * 删除角色授权
	 * @param roleAlias
	 */
	void removeByRoleAlias(String roleAlias);
	
	/**
	 * 批量删除角色授权
	 * @param aryroleAlias
	 */
	void removeByArrRoleAlias(String[] aryroleAlias);
	
	/**
	 * 新增角色授权信息
	 * 1. 先清除原来的授权信息  根据角色别名清除
	 * 2. 再添加授权记录
	 * @param sysRoleAuthParam
	 */
	void create(SysRoleAuthParam sysRoleAuthParam);
	
	/**
	 * 获取后台请求方法与角色的关系
	 * @return
	 */
	List<HashMap<String,String>> getSysRoleAuthAll();

	/**
	 * 权限复制（原角色权限复制给新的角色）
	 * @param oldCode 原角色别名
	 * @param newCodes 权限复制的角色别名
     */
	void createCopy(String oldCode,String[] newCodes);
	
	/**
	 * 保存接口权限
	 * @param sysRoleAuthParam
	 */
	void saveRoleMethods(SysRoleAuthParam sysRoleAuthParam);
	
	/**
	 * 删除角色接口权限
	 * @param roleAlias
	 * @param methodAlias
	 */
	void removeRoleMethods(String roleAlias, String[] methodAlias);
}
