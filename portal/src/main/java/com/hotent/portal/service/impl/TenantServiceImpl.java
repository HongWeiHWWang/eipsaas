package com.hotent.portal.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.hotent.base.constants.TenantConstant;
import com.hotent.base.util.TenantUtil;
import com.hotent.portal.service.TenantService;

/**
 * <pre> 
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2018-06-06 14:20
 * </pre>
 */
@Service
public class TenantServiceImpl implements TenantService {
	
	private Log logger = LogFactory.getLog(TenantServiceImpl.class);

	@Resource
	JdbcTemplate jdbcTemplate;
	
	@Override
	public void initData(String tenantId) {
		logger.debug("开始初始化租户的数据");
		List<String> tableNames = TenantConstant.INIT_PORTAL_DATA_TABLE_NAMES;
		TenantUtil.initData(tenantId, tableNames);
	}

}
