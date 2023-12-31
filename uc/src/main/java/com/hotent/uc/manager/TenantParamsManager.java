package com.hotent.uc.manager;

import java.util.List;

import com.hotent.base.manager.BaseManager;
import com.hotent.base.model.CommonResult;
import com.hotent.uc.model.TenantParams;
import com.hotent.uc.params.params.ParamObject;

/**
 * 
 * <pre> 
 * 描述：租户扩展参数值 处理接口
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2020-04-17 14:54:36
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public interface TenantParamsManager extends BaseManager<TenantParams>{
	/**
	 * 根据租户id获取其下扩展参数值
	 * @param typeId
	 */
	List<TenantParams> getByTenantId(String tenantId);
	
	/**
	 * 根据租户id删除其下扩展参数值
	 * @param typeId
	 */
	void deleteByTenantId(String tenantId);
	
	/**
	 * 保存租户扩展参数值
	 * @param tenantId
	 * @param params
	 * @return
	 */
	CommonResult<String> saveUserParams(String tenantId,List<ParamObject> params);
}
