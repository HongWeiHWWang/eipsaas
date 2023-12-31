package com.hotent.uc.dao;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.uc.model.TenantManage;

/**
 * 
 * <pre> 
 * 描述：租户管理 DAO接口
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2020-04-17 10:56:07
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public interface TenantManageDao extends BaseMapper<TenantManage> {
	/**
	 * 根据编码获取租户
	 * @param tenantId
	 * @return
	 */
	TenantManage getByCode(@Param("code")String code);
	
	/**
	 * 根据租户类型获取租户列表
	 * @param tenantId
	 * @return
	 */
	List<TenantManage> getByTypeId(@Param("typeId")String typeId);
	
	/**
	 * 根据租户类型删除类型下的租户
	 * @param typeId
	 */
	void deleteByTypeId(@Param("typeId")String typeId);
	
	/**
	 * 根据域名获取租户
	 * @param domain
	 * @return
	 */
	TenantManage getByDomain(@Param("domain") String domain);
	
	/**
	 * 根据状态获取
	 * @param typeId
	 * @param status
	 * @return
	 */
	List<TenantManage> getByStatus(@Param("typeId")String typeId,@Param("status")String status);

	/**
	 * 查询租户列表（包含租户类型名称）
	 * @param page
	 * @param wrapper
	 * @return
	 */
	IPage<TenantManage> queryWithType(IPage<TenantManage> page, @Param(Constants.WRAPPER) Wrapper<TenantManage> wrapper);
}
