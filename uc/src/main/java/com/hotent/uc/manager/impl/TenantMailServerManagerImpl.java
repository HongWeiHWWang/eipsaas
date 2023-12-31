package com.hotent.uc.manager.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.uc.dao.TenantMailServerDao;
import com.hotent.uc.manager.TenantMailServerManager;
import com.hotent.uc.model.TenantMailServer;

/**
 * 
 * <pre> 
 * 描述：租户邮件服务器信息 处理实现类
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2020-04-17 11:01:30
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@Service("tenantMailServerManager")
public class TenantMailServerManagerImpl extends BaseManagerImpl<TenantMailServerDao, TenantMailServer> implements TenantMailServerManager{

	@Override
	public TenantMailServer getByTenantId(String tenantId) {
		return baseMapper.getByTenantId(tenantId);
	}

	@Override
	@Transactional
	public void deleteByTenantId(String tenantId) {
		baseMapper.deleteByTenantId(tenantId);
	}
	
}
