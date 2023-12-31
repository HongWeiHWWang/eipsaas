package com.hotent.uc.dao;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.uc.model.TenantMailServer;

/**
 * 
 * <pre> 
 * 描述：租户邮件服务器 DAO接口
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2020-04-17 11:01:30
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public interface TenantMailServerDao extends BaseMapper<TenantMailServer> {
	/**
	 * 获取租户邮件服务器
	 * @param tenantId
	 * @return
	 */
	TenantMailServer getByTenantId(@Param("tenantId")String tenantId);
	
	/**
	 * 根据租户id删除其邮件服务器数据
	 * @param typeId
	 */
	void deleteByTenantId(@Param("tenantId")String tenantId);
}
