package com.hotent.uc.manager;

import java.util.List;

import com.hotent.base.manager.BaseManager;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.uc.model.TenantAuth;
import com.hotent.uc.params.tenant.TenantAuthAddObject;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * <pre> 
 * 描述：租户类型管理员 处理接口
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2020-04-17 10:55:39
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public interface TenantAuthManager extends BaseManager<TenantAuth>{
	/**
	 * 根据租户类型或者租户id获取管理员
	 * @param page
	 * @param typeId
	 * @param tenantId
	 * @return
	 */
	List<TenantAuth> getByTypeAndTenantId(String typeId,String tenantId);
	
	/**
	 * 根据用户获取
	 * @param typeId
	 * @param tenantId
	 * @param userId
	 * @return
	 */
	List<TenantAuth> getByUserId(String typeId,String tenantId,String userId);
	
	/**
	 * 根据租户类型删除其下管理员
	 * @param typeId
	 */
	void deleteByTypeId(String typeId);
	
	/**
	 * 根据租户id删除其下管理员
	 * @param typeId
	 */
	void deleteByTenantId(String tenantId);
	
	/**
	 * 设置租户管理员
	 * @param authAddObject
	 * @return
	 * @throws Exception 
	 */
	CommonResult<String> saveTenantAuth(TenantAuthAddObject authAddObject) throws Exception;
	
	/**
	 * 删除租户管理员
	 * @param typeId
	 * @param tenantId
	 * @param userIds
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> removeTenantAuth(String typeId,String tenantId,String userIds) throws Exception;
	
	/**
	 * 根据租户类型或租户id获取租户管理员授权分页数据
	 * @param queryFilter
	 * @return
	 */
	PageList<TenantAuth> queryByTypeAndTenantId(QueryFilter queryFilter);

    /**
     * 根据用户ID删除租户管理员
     * @param userId
     * @return
     */
    void delByUserId(@Param("userId")String userId);
}
