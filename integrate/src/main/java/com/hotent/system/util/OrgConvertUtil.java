package com.hotent.system.util;



import com.fasterxml.jackson.databind.JsonNode;
import com.hotent.base.util.BeanUtils;
import com.hotent.system.model.WxOrg;
import com.hotent.system.model.WxUser;


public class OrgConvertUtil {

	public static WxUser userToWxUser(JsonNode user){
		WxUser wxUser = new WxUser();
		String email= (user.get("email")==null||user.get("email").isNull())? "" : user.get("email").asText();
		String sex= (user.get("sex") == null ||user.get("sex").isNull()) ? "" : user.get("sex").asText();
		String mobile=user.get("mobile").asText();
		String fullname=user.get("fullname").asText();
		String account=user.get("account").asText();
		//没有主组织，则挂在根部门下
		String deptStr =BeanUtils.isEmpty(user.get("department"))||user.get("department").isEmpty() ? "1" : user.get("department").asText();
		String[] department = deptStr.split(",");
		
		wxUser.setDepartment(department);
		wxUser.setEmail(email);
		wxUser.setGender(sex);
		wxUser.setMobile(mobile);
		wxUser.setName(fullname);
		wxUser.setUserid(account);
		return wxUser;
	}
	
	public static WxOrg sysOrgToWxOrg(JsonNode org){
		WxOrg wxorg = new WxOrg();
		wxorg.setId(org.get("id").asText());
		wxorg.setParentid(org.get("parentId").asText());
		wxorg.setName(org.get("name").asText());
		wxorg.setOrder((org.get("orderNo")==null || org.get("orderNo").isNull())?"":org.get("orderNo").asText());
		return wxorg;
	}


}
