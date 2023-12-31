package com.hotent.portal.persistence.manager.impl;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.portal.model.SysIndexLayout;
import com.hotent.portal.persistence.dao.SysIndexLayoutDao;
import com.hotent.portal.persistence.manager.SysIndexLayoutManager;

/**
 * 系统主页布局 处理实现类
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月11日
 */
@Service("sysIndexLayoutManager")
public class SysIndexLayoutManagerImpl extends BaseManagerImpl<SysIndexLayoutDao, SysIndexLayout> implements SysIndexLayoutManager{
}
