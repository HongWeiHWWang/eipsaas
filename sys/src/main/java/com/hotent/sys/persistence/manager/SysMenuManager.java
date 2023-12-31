package com.hotent.sys.persistence.manager;

import java.util.List;
import java.util.Map;

import com.hotent.base.manager.BaseManager;
import com.hotent.sys.persistence.model.SysMenu;

/**
 * 
 * <pre> 
 * 描述：系统菜单 处理接口
 * 构建组：x6
 * 作者:liyg
 * 邮箱:liygui@jee-soft.cn
 * 日期:2018-06-29 09:34:15
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface SysMenuManager extends BaseManager<SysMenu>{
	/**
	 * 判断是否已经存在
	 * @param alias
	 * @return
	 */
	boolean isExistByAlias(String alias);
	
	/**
	 * 根据菜单别名获取
	 * @param alias
	 * @return
	 */
	SysMenu getByAlias(String alias);
	
	/**
	 * 根据资源id递归删除资源数据。
	 * @param resId
	 */
	void removeByResId(String resId);
	
	/**
	 * 获得当前用户的菜单
	 * 
	 * 1. 需要在添加菜单时清空 缓存，
	 * 2. 需要在添加授权信息时清空缓存
	 * @return
	 */
	List<SysMenu> getCurrentUserMenu() throws Exception;
	
	/**
	 * 通过菜单别名来过滤数据(例如：传入manage_menu，则只返回manage_menu的下级菜单，会递归下级的下级菜单)，返回的数据会转换为nest类型。
	 * @param menuAlias	目录别名
	 * @param lists		原数组
	 * @return
	 */
	List<SysMenu> filterByMenuAlias(String menuAlias, List<SysMenu> lists);

	List<Map<String, Object>> getAllMenuRoleAlias(String roleAlias);

	List<SysMenu> getMenuByRoleAlias(String roleAlias);
	
	/**
	 * 获取租户的所有菜单
	 * @param ignoreAlias
	 * @return
	 */
	List<SysMenu> getAllByTenant(String ignoreAlias);
	
	/**
	 * 删除缓存
	 */
	void delUserMenuCache();
}
