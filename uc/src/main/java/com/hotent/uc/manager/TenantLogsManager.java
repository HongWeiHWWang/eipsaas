package com.hotent.uc.manager;

import com.hotent.base.manager.BaseManager;
import com.hotent.uc.model.TenantLogs;

/**
 * 
 * <pre> 
 * 描述：租户操作日志 处理接口
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2020-04-17 14:53:55
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public interface TenantLogsManager extends BaseManager<TenantLogs>{
	/**
	 * 根据租户id删除其下扩展参数值
	 * @param typeId
	 */
	void deleteByTenantId(String tenantId);
}
