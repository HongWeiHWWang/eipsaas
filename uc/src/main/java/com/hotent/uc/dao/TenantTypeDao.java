package com.hotent.uc.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.uc.model.TenantType;

/**
 * 
 * <pre> 
 * 描述：租户类型 DAO接口
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2020-04-17 10:52:37
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public interface TenantTypeDao extends BaseMapper<TenantType> {
	/**
	 * 根据类型编码获取
	 * @param code
	 * @return
	 */
	TenantType getByCode(@Param("code")String code);
	/**
	 * 获取默认分类
	 * @return
	 */
	TenantType getDefault();
	
	/**
	 * 清空默认分类
	 */
	void setNotDefault();
	
	/**
	 * 根据状态获取
	 * @param status
	 * @return
	 */
	List<TenantType> getByStatus(@Param("status")String status,@Param("authIds")List<String> authIds);
}
