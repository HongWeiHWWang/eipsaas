package com.hotent.portal.persistence.manager.impl;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.portal.model.SysLayoutSetting;
import com.hotent.portal.persistence.dao.SysLayoutSettingDao;
import com.hotent.portal.persistence.manager.SysLayoutSettingManager;

/**
 * 布局设置 处理实现类
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月11日
 */
@Service("sysLayoutSettingManager")
public class SysLayoutSettingManagerImpl extends BaseManagerImpl<SysLayoutSettingDao, SysLayoutSetting> implements SysLayoutSettingManager{
	
	@Override
	public SysLayoutSetting getByLayoutId(String layoutId) {
		return baseMapper.getByLayoutId(layoutId);
	}
}
