package com.hotent.uc.ws.impl;

import javax.annotation.Resource;
import javax.jws.WebService;

import org.apache.commons.lang.exception.ExceptionUtils;

import com.hotent.platform.webservice.impl.ThirdBizSysService;
import com.hotent.uc.ws.WebserviceUserHandler;
import com.hotent.uc.ws.WsFacadeUser;
import com.hotent.uc.ws.WsResult;

@WebService(targetNamespace="http://impl.webservice.platform.hotent.com/",endpointInterface = "com.hotent.platform.webservice.impl.ThirdBizSysService")
public class ThirdBizSysServiceImpl implements ThirdBizSysService{
	@Resource
	WebserviceUserHandler webserviceUserHandler;
	
	@Override
	public String authLoginUser(String account, String password, String sysName) {
		return WsResult.build(false, "接口未实现").toString();
	}

	@Override
	public String modPassword(String account, String oldPassword, String newPassword, String sysName) {
		return WsResult.build(false, "接口未实现").toString();
	}

	@Override
	public String sysBusinessOperate(String business_code, String business_name, String business_type, String leftNode,
			String parent_code, String remarks, String operatetype) {
		return WsResult.build(false, "接口未实现").toString();
	}

	@Override
	public String sysBusinessUsersOperate(String account, String businessCodes) {
		return WsResult.build(false, "接口未实现").toString();
	}

	@Override
	public String sysBusinessUsersOperateforRole(String accounts, String addBusinessCodes, String delBusinessCodes) {
		return WsResult.build(false, "接口未实现").toString();
	}

	@Override
	public String sysUserOperate(String account, String mobile, String fullname, String email, String operatetype) {
		try {
			webserviceUserHandler.sysUserOperate(new WsFacadeUser(account, mobile, fullname, email, operatetype));
			return WsResult.build().toString();
		}
		catch(Exception e) {
			return WsResult.build(false, ExceptionUtils.getRootCauseMessage(e)).toString();
		}
	}
}