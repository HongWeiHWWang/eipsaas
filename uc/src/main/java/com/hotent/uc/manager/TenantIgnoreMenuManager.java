package com.hotent.uc.manager;

import java.util.List;

import com.hotent.base.manager.BaseManager;
import com.hotent.base.model.CommonResult;
import com.hotent.uc.model.TenantIgnoreMenu;

/**
 * 
 * <pre> 
 * 描述：租户禁用菜单 处理接口
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2020-04-20 17:00:53
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public interface TenantIgnoreMenuManager extends BaseManager<TenantIgnoreMenu>{
	/**
	 * 根据租户id获取其被禁用菜单
	 * @param tenantId
	 * @return
	 */
	List<TenantIgnoreMenu> getByTenantId(String tenantId);
	
	/**
	 * 根据租户id删除其下管理员
	 * @param typeId
	 */
	void deleteByTenantId(String tenantId);
	
	/**
	 * 保存租户禁用菜单信息
	 * @param tenantId
	 * @param ignoreMenus
	 * @return
	 */
	CommonResult<String> saveByTenantId(String tenantId,List<String> ignoreMenus);
}
