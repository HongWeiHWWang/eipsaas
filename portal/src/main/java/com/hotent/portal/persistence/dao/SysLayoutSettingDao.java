package com.hotent.portal.persistence.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.portal.model.SysLayoutSetting;

/**
 * 布局设置 DAO接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月11日
 */
public interface SysLayoutSettingDao extends BaseMapper<SysLayoutSetting> {
	
	/**
	 * 通过布局Id获取布局设置
	 * @param layoutId 布局id
	 * @return		         返回布局设置
	 */
	SysLayoutSetting getByLayoutId(String layoutId);
}
