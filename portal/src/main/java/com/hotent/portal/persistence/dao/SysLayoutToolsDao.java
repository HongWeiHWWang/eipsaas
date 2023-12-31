package com.hotent.portal.persistence.dao;

import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.portal.model.SysLayoutTools;

/**
 * 布局工具设置 DAO接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月11日
 */
public interface SysLayoutToolsDao extends BaseMapper<SysLayoutTools> {
	
	/**
	 * 
	 * @param layoutId  布局id
	 * @param params    参数map
	 */
	SysLayoutTools getByLayoutID(Map params);
}
