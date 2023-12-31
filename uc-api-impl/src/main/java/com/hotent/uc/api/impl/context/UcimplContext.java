package com.hotent.uc.api.impl.context;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hotent.base.annotation.IgnoreOnAssembly;
import com.hotent.base.constants.TenantConstant;
import com.hotent.base.context.BaseContext;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.HttpUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;

/**
 * 微服务部署时 非uc模块将使用该bean
 * @author liyanggui
 *
 */
@Service
@IgnoreOnAssembly
@Primary
public class UcimplContext implements BaseContext {
	
	private ThreadLocal<String> tempTenantId = new ThreadLocal<>();

	public void setTempTenantId(String tenantId) {
		tempTenantId.set(tenantId);
	}

	public void clearTempTenantId() {
		this.tempTenantId.remove();
	}

	@Override
	public String getCurrentUserId() {
		if(authenticationEmpty()) {
			return TenantConstant.PLATFORM_TENANT_ID;
		}
		return ContextUtil.getCurrentUserId();
	}

	@Override
	public String getCurrentUserAccout() {
		if(authenticationEmpty()) {
			return TenantConstant.PLATFORM_TENANT_ID;
		}
		return ContextUtil.getCurrentUser().getAccount();
	}

	@Override
	public String getCurrentOrgId() {
		if(authenticationEmpty()) {
			return TenantConstant.PLATFORM_TENANT_ID;
		}
		return ContextUtil.getCurrentGroupId();
	}

	@Override
	public String getCurrentTenantId() {
		String tenantId = HttpUtil.getTenantId();
		if(BeanUtils.isNotEmpty(tenantId)) {
			return tenantId;
		}
		
		String tempTenantId = this.tempTenantId.get();
        if (StringUtil.isNotEmpty(tempTenantId)) {
			return tempTenantId;
		}
        
		if(authenticationEmpty()) {
			return TenantConstant.PLATFORM_TENANT_ID;
		}
		
		IUser currentUser = ContextUtil.getCurrentUser();
		if(BeanUtils.isEmpty(currentUser)) {
			return TenantConstant.PLATFORM_TENANT_ID;
		}
		
		return currentUser.getTenantId();
	}
	
	/**
	 * 兼容单元测试
	 * @return
	 */
	private boolean authenticationEmpty() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(BeanUtils.isEmpty(authentication)) {
			return true;
		}
		return false;
	}

}
