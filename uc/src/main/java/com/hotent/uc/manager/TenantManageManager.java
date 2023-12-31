package com.hotent.uc.manager;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hotent.base.manager.BaseManager;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.uc.model.TenantManage;

/**
 * 
 * <pre> 
 * 描述：租户管理 处理接口
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2020-04-17 10:56:07
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public interface TenantManageManager extends BaseManager<TenantManage>{
	/**
	 * 根据编码获取租户
	 * @param tenantId
	 * @return
	 */
	TenantManage getByCode(String code);
	
	/**
	 * 根据状态获取
	 * @param typeId
	 * @param status
	 * @return
	 */
	List<TenantManage> getByStatus(String typeId,String status);
	
	/**
	 * 根据租户类型获取租户列表
	 * @param tenantId
	 * @return
	 */
	List<TenantManage> getByTypeId(String typeId);
	
	/**
	 * 根据租户类型删除类型下的租户
	 * @param typeId
	 */
	void deleteByTypeId(String typeId);
	
	/**
	 * 根据访问域名获取租户
	 * @param domain
	 * @return
	 */
	TenantManage getByDomain(String domain);

	/**
	 * 查询租户列表（包含租户类型名称）
	 * @param queryFilter
	 * @return
	 */
	PageList queryWithType(QueryFilter queryFilter);
}
