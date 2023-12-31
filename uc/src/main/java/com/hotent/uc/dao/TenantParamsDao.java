package com.hotent.uc.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.uc.model.TenantParams;

/**
 * 
 * <pre> 
 * 描述：UC_TENANT_PARAMS DAO接口
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2020-04-17 14:54:36
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public interface TenantParamsDao extends BaseMapper<TenantParams> {
	
	/**
	 * 根据租户id获取其下扩展参数值
	 * @param typeId
	 */
	List<TenantParams> getByTenantId(@Param("tenantId")String tenantId);
	
	/**
	 * 根据租户id删除其下扩展参数值
	 * @param typeId
	 */
	void deleteByTenantId(@Param("tenantId")String tenantId);
	
	TenantParams getByTenantIdAndCode(@Param("tenantId") String tenantId,@Param("code") String code);
}
