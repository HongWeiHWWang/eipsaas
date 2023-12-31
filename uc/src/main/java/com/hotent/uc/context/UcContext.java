package com.hotent.uc.context;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hotent.base.constants.TenantConstant;
import com.hotent.base.context.BaseContext;
import com.hotent.base.exception.BaseException;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.HttpUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.uc.manager.TenantManageManager;
import com.hotent.uc.model.TenantManage;
import com.hotent.uc.model.User;
import com.hotent.uc.util.ContextUtil;

/**
 * uc 和 uc-api-impl 中各自有一个UCContext
 * 
 * @author liyanggui
 *
 */
@Service
@Primary
public class UcContext implements BaseContext {

	private ThreadLocal<String> tempTenantId = new ThreadLocal<>();

	public void setTempTenantId(String tenantId) {
		tempTenantId.set(tenantId);
	}

	public void clearTempTenantId() {
		this.tempTenantId.remove();
	}

	@Override
	public String getCurrentUserId() {
		if (authenticationEmpty()) {
			return TenantConstant.PLATFORM_TENANT_ID;
		}
		return ContextUtil.getCurrentUserId();
	}

	@Override
	public String getCurrentUserAccout() {
		if (authenticationEmpty()) {
			return TenantConstant.PLATFORM_TENANT_ID;
		}
		return ContextUtil.getCurrentUser().getAccount();
	}

	@Override
	public String getCurrentOrgId() {
		if (authenticationEmpty()) {
			return TenantConstant.PLATFORM_TENANT_ID;
		}
		return ContextUtil.getCurrentGroupId();
	}

	@Override
	public String getCurrentTenantId() {
		String tenantId = HttpUtil.getTenantId();
		if (BeanUtils.isNotEmpty(tenantId)) {
			return tenantId;
		}
		String tempTenantId = this.tempTenantId.get();
        if (StringUtil.isNotEmpty(tempTenantId)) {
			return tempTenantId;
		}
        
		if (authenticationEmpty()) {
			return TenantConstant.PLATFORM_TENANT_ID;
		}	
		User currentUser = ContextUtil.getCurrentUser();
		if (BeanUtils.isEmpty(currentUser)) {
			String tenantCode = HttpUtil.getRequest().getHeader("Tenant-Code");
			TenantManageManager tenantManageManager = AppUtil.getBean(TenantManageManager.class);
			TenantManage tenantManage = tenantManageManager.getByCode(tenantCode);
			if (BeanUtils.isNotEmpty(tenantManage)) {
				String status = tenantManage.getStatus();
				if(TenantManage.STATUS_DRAFT.equals(status)) {
					throw new BaseException("草稿状态的租户不允许登录");
				}
				return tenantManage.getId();
			}
			return TenantConstant.PLATFORM_TENANT_ID;
		}
		return ContextUtil.getCurrentUser().getTenantId();
	}

	/**
	 * 兼容单元测试
	 * 
	 * @return
	 */
	private boolean authenticationEmpty() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (BeanUtils.isEmpty(authentication)) {
			return true;
		}
		return false;
	}
}
