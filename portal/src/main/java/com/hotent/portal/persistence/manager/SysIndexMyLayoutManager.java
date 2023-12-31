package com.hotent.portal.persistence.manager;

import java.util.List;

import com.hotent.base.manager.BaseManager;
import com.hotent.portal.model.SysIndexColumn;
import com.hotent.portal.model.SysIndexMyLayout;


/**
 * 系统我的首页布局 处理接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月11日
 */
public interface SysIndexMyLayoutManager extends BaseManager<SysIndexMyLayout>{
	
	/**
	 * 保存布局实体
	 * @param html		  模板
	 * @param designHtml 设计模版
	 */
	void save(String html, String designHtml, String userId);
	
	/**
	 * 返回我的布局实体
	 * @param userId     用户id
	 * @param columnList 首页栏目集合
	 * @return
	 */
	SysIndexMyLayout getLayoutList(String userId, List<SysIndexColumn> columnList);
	
	/**
	 * 获取我的大首页数据
	 * @param userId 用户id
	 * @return
	 */
	String obtainMyIndexData(String userId);
	
	/**
	 * 根据当前用户id获取我的布局
	 * @param currentUserId 当前用户id
	 * @return
	 */
	SysIndexMyLayout getByUser(String currentUserId);
	
	/**
	 * 获取首页我的数据
	 * @param layoutId 布局id
	 * @return
	 */
	String obtainIndexMyData(String layoutId);
	
	/**
	 * 删除指定用户的首页布局
	 * @param userId 用户ID
	 */
	void removeByUserId(String userId);

	void setValid(String id);
}
