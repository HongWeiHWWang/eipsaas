package com.hotent.portal.persistence.dao;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.portal.model.SysIndexLayoutManage;



/**
 * 布局管理 Dao类
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月11日
 */
public interface SysIndexLayoutManageDao extends BaseMapper<SysIndexLayoutManage> {
	/**
	 * 通过orgid和layoutType获取已启用的布局
	 * @param orgId
	 * @param layoutType
	 * @return
	 */
	SysIndexLayoutManage getEnableByOrgIdAndType(Map<String,Object> params);
	
	/**
	 * 获取当前布局
	 * @param params 参数
	 * @return		   返回当前布局集合
	 */
	List<SysIndexLayoutManage> getByUserIdFilter(Map<String,Object> params);
	
	/**
	 * 找自己所属子组织的没权限但设置默认布局
	 * @param orgIds 组织id
	 * @param isDef  是否属于
	 * @return
	 */
	List<SysIndexLayoutManage> getManageLayout(Map<String,Object> params);
	
	/**
	 * 根据组织id设置默认布局
	 * @param orgId 组织id
	 */
	public void updateIsDef(String orgId);
	
	/**
	 * 判断布局名称是否重复
	 * @param name 布局名称
	 * @return     返回判断结果
	 */
	Integer isExistName(String name);
	/**
	 * 通过组织id和布局类型获取实体
	 * @param orgId 组织id
	 * @param layoutType 布局类型
	 * @return 返回布局管理
	 */
	List<SysIndexLayoutManage> getByOrgIdAndLayoutType(Map params);
	/**
	 * 取消当前组织当前布局类型的所有默认布局
	 * @param orgId 组织id
	 * @param layoutType 布局类型
	 */
	void cancelOrgIsDef(Map params);
	/**
	 * 查该布局是不是组织的布局
	 * @param id 组织id
	 * @param layoutType 布局类型
	 * @param layoutId 布局id
	 * @return 返回布局管理
	 */
	SysIndexLayoutManage getByOrgIdAndLayoutTypeAndLayoutId(String orgId, short layoutType, String layoutId);
	/**
	 * 获取pc布局
	 * @param id 主键
	 * @param type 类型
	 * @return 返回布局管理
	 */
	SysIndexLayoutManage getByIdAndType(Map params);

	List<SysIndexLayoutManage> getSysDefaultLayout(Map<String, List<String>> map);

	SysIndexLayoutManage getSharedByOrgIdAndType(Map<String,Object> map);
	
	/**
	 * 通过组织ID数组和布局类型获取布局
	 * @param orgIds
	 * @param layoutType
	 * @return
	 */
	List<SysIndexLayoutManage> getSharedByOrgIds(@Param("orgIds")List<String> orgIds, @Param("layoutType")Short layoutType);
}
