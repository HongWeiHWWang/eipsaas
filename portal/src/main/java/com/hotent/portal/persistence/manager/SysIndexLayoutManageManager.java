package com.hotent.portal.persistence.manager;

import java.util.List;

import com.hotent.base.manager.BaseManager;
import com.hotent.portal.model.SysIndexColumn;
import com.hotent.portal.model.SysIndexLayoutManage;



/**
 * 布局管理  Service类
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月11日
 */
public interface SysIndexLayoutManageManager extends BaseManager<SysIndexLayoutManage>{
	
	/**
	 * 获取默认的设计模版
	 * @return
	 */
	public String getDefaultDesignHtml();
	
	/**
	 * 获取布局管理集合
	 * @param filter 通用查询器
	 * @return
	 */
//	public List<SysIndexLayoutManage> getList(QueryFilter filter);
	
	/**
	 * 获得布局实体
	 * @param userId 用户id
	 * @param columnList 首页栏目集合
	 * @param type   类型
	 * @return
	 */
	public SysIndexLayoutManage getLayoutList(String userId, List<SysIndexColumn>  columnList, Short type);
	
	/**
	 * 找自己拥有权限的管理布局
	 * @return
	 */
	public String getMyHasRightsLayout();
	
	/**
	 * 找自己所属子组织没权限但设置默认布局
	 * @return
	 */
//	public String getHasRightsLayout();
	
	/**
	 * 系统管理员的默认布局
	 * @return
	 */
	public String getManagerLayout();
	
	/**
	 * 获取主页管理数据
	 * @param layoutId
	 * @return
	 */
	public String obtainIndexManageData(String layoutId);
	
	/**
	 * 通过orgid和layoutType获取已启用的布局
	 * @param orgId
	 * @param layoutType
	 * @return
	 */
	public SysIndexLayoutManage getEnableByOrgIdAndType(String orgId,Short layoutType);
	
	/**
	 * 判断布局名称是否重复
	 * @param name 布局名称
	 * @return
	 */
	public Boolean isExistName(String name);
	
	/**
	 * 通过组织和布局类型获取实体
	 * @param orgId 组织id
	 * @param layoutType 布局类型
	 * @return
	 */
	public List<SysIndexLayoutManage> getByOrgIdAndLayoutType(String orgId, Short layoutType);
	
	/**
	 * 取消所有默认当前布局类型
	 * @param orgId 	   组织id
	 * @param layoutType 布局类型
	 */
	public void cancelOrgIsDef(String orgId, Short layoutType);
	
	/**
	 * 获取手机的首页布局
	 * @param userId 用户id
	 * @return
	 */
	public String obtainIndexManageMobileData(String layoutId);
	
	/**
	 * 获取pc端布局
	 * @param valueOf 值
	 * @param typePc pc端
	 * @return
	 */
	public SysIndexLayoutManage getByIdAndType(String id, Short type);

	/**
	 * 手机的布局
	 * @return
	 */
	public String getMobileManagerLayout();

	/**
	 * 通过指定组织ID和布局类型获取布局
	 * @param string
	 * @param layoutType
	 * @return
	 */
	public SysIndexLayoutManage getSharedByOrgIdAndType(String orgId,Short layoutType);
	
	/**
	 * 通过指定组织ID数组和布局类型获取布局
	 * @param orgIds
	 * @param layoutType
	 * @return
	 */
	SysIndexLayoutManage getSharedByOrgIds(List<String> orgIds, Short layoutType);

	public void setEnable(String id, Short enable);
}