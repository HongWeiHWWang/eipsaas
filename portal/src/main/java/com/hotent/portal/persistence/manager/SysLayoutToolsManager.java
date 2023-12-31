package com.hotent.portal.persistence.manager;


import com.hotent.base.manager.BaseManager;
import com.hotent.portal.model.SysLayoutTools;


/**
 * 布局工具设置 处理接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月11日
 */
public interface SysLayoutToolsManager extends BaseManager<SysLayoutTools>{
	
	/**
	 * 返回布局工具
	 * @param layoutId 布局id
	 * @param toolsType 工具；类型
	 * @return
	 */
	SysLayoutTools getByLayoutID(String layoutId, String toolsType);
	
	/**
	 * 不同类型查出不同工具
	 * @param layoutId 布局id
	 * @param tools    工具类型
	 * @return
	 */
//	List<SysIndexTools> queryTools(String layoutId, String tools);
}
