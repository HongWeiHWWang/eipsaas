package com.hotent.uc.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.uc.model.TenantIgnoreMenu;

/**
 * 
 * <pre> 
 * 描述：租户禁用菜单 DAO接口
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2020-04-20 17:00:53
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public interface TenantIgnoreMenuDao extends BaseMapper<TenantIgnoreMenu> {
	/**
	 * 根据租户id获取其被禁用菜单
	 * @param tenantId
	 * @return
	 */
	List<TenantIgnoreMenu> getByTenantId(@Param("tenantId")String tenantId);
	
	/**
	 * 根据租户id删除其下管理员
	 * @param typeId
	 */
	void deleteByTenantId(@Param("tenantId")String tenantId);
}
