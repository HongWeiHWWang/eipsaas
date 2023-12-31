package com.hotent.uc.dao;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.uc.model.TenantLogs;

/**
 * 
 * <pre> 
 * 描述：租户管理操作日志 DAO接口
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2020-04-17 14:53:55
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public interface TenantLogsDao extends BaseMapper<TenantLogs> {
	/**
	 * 根据租户id删除其下管理员
	 * @param typeId
	 */
	void deleteByTenantId(@Param("tenantId")String tenantId);
}
