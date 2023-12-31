package com.hotent.uc.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hotent.uc.model.TenantAuth;

/**
 * 
 * <pre> 
 * 描述：租户管理员 DAO接口
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2020-04-17 10:55:39
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public interface TenantAuthDao extends BaseMapper<TenantAuth> {
	/**
	 * 根据租户类型或者租户id获取管理员
	 * @param page
	 * @param typeId
	 * @param tenantId
	 * @return
	 */
	List<TenantAuth> getByTypeAndTenantId(@Param("typeId")String typeId, @Param("tenantId")String tenantId);
	
	/**
	 * 根据租户类型删除其下管理员
	 * @param typeId
	 */
	void deleteByTypeId(@Param("typeId")String typeId);
	
	/**
	 * 根据租户id删除其下管理员
	 * @param typeId
	 */
	void deleteByTenantId(@Param("tenantId")String tenantId);
	
	/**
	 * 根据用户获取
	 * @param typeId
	 * @param tenantId
	 * @param userId
	 * @return
	 */
	List<TenantAuth> getByUserId(@Param("typeId")String typeId, @Param("tenantId")String tenantId, @Param("userId")String userId);
	
	/**
	 * 根据租户类型或租户id获取租户管理员授权分页数据
	 * @param iPage
	 * @param convert2Wrapper
	 * @return
	 */
	IPage<TenantAuth> queryByTypeAndTenantId(IPage<TenantAuth> iPage, @Param(Constants.WRAPPER)Wrapper<TenantAuth> convert2Wrapper);

    /**
     * 根据用户ID删除租户管理员
     * @param userId
     * @return
     */
    void delByUserId(@Param("userId")String userId);
}
