package com.hotent.uc.manager;

import com.hotent.base.manager.BaseManager;
import com.hotent.uc.model.TenantMailServer;

/**
 * 
 * <pre> 
 * 描述：租户邮件服务器信息 处理接口
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2020-04-17 11:01:30
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public interface TenantMailServerManager extends BaseManager<TenantMailServer>{
	/**
	 * 获取租户邮件服务器
	 * @param tenantId
	 * @return
	 */
	TenantMailServer getByTenantId(String tenantId);
	
	/**
	 * 根据租户id删除其邮件服务器数据
	 * @param typeId
	 */
	void deleteByTenantId(String tenantId);
}
