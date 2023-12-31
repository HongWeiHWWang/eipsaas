package com.hotent.platform.webservice.impl;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface ThirdBizSysService {
	@WebMethod
	String authLoginUser(@WebParam(name = "account") String account, 
			@WebParam(name = "password") String password,
			@WebParam(name = "sysName") String sysName);
	@WebMethod
	String modPassword(@WebParam(name = "account") String account, 
			@WebParam(name = "oldPassword") String oldPassword,
			@WebParam(name = "newPassword") String newPassword, 
			@WebParam(name = "sysName") String sysName);

	@WebMethod
	String sysBusinessOperate(@WebParam(name = "business_code") String business_code, 
			@WebParam(name = "business_name") String business_name,
			@WebParam(name = "business_type") String business_type, 
			@WebParam(name = "leftNode") String leftNode,
			@WebParam(name = "parent_code") String parent_code,
			@WebParam(name = "remarks") String remarks,
			@WebParam(name = "operatetype") String operatetype);

	@WebMethod
	String sysBusinessUsersOperate(@WebParam(name = "account") String account, 
			@WebParam(name = "businessCodes") String businessCodes);

	@WebMethod
	String sysBusinessUsersOperateforRole(@WebParam(name = "accounts") String accounts, 
			@WebParam(name = "addBusinessCodes") String addBusinessCodes,
			@WebParam(name = "delBusinessCodes") String delBusinessCodes);

	@WebMethod
	String sysUserOperate(@WebParam(name = "account") String account, 
			@WebParam(name = "mobile") String mobile,
			@WebParam(name = "fullname") String fullname, 
			@WebParam(name = "email") String email,
			@WebParam(name = "operatetype") String operatetype);
}