package com.hotent.base.context.impl;

import org.springframework.stereotype.Service;

import com.hotent.base.constants.TenantConstant;
import com.hotent.base.context.BaseContext;

@Service
public class EmptyBaseContext implements BaseContext{
	@Override
	public String getCurrentUserId() {
		return "1";
	}

	@Override
	public String getCurrentOrgId() {
		return "1";
	}

	@Override
	public String getCurrentTenantId() {
		return TenantConstant.PLATFORM_TENANT_ID;
	}

	@Override
	public String getCurrentUserAccout() {
		return null;
	}

	@Override
	public void setTempTenantId(String tenantId) {
		
	}

	@Override
	public void clearTempTenantId() {
		
	}
}
