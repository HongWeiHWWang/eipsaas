package com.hotent.uc.service.impl;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.hotent.base.util.JsonUtil;
import com.hotent.uc.model.User;
import com.hotent.uc.service.UserDetailsFacade;

@Service
public class UserDetailsFacadeImpl implements UserDetailsFacade{

	private static final long serialVersionUID = 1L;

	@Override
	public UserDetails loadUserDetails(Collection<GrantedAuthority> authorities, Object obj) throws Exception {
		// valid();
		JsonNode jsonNode = JsonUtil.toJsonNode(obj);
		String id = jsonNode.get("id").asText();
		String account = jsonNode.get("account").asText();
		String pwd = jsonNode.get("password").asText();
		int status = jsonNode.get("status").asInt();
		String fullname = jsonNode.get("fullname").asText();
		String email = jsonNode.get("email").asText();
		String mobile = jsonNode.get("mobile").asText();
        String weixin = jsonNode.get("weixin").asText();
		//  设置用户的组织id  以及 下级组织id
		JsonNode jsonNode2 = jsonNode.get("attributes");
		Map<String, String> attributes = JsonUtil.toMap(JsonUtil.toJson(jsonNode2));
		
		User user = new User(account, fullname, pwd, authorities);
		user.setId(id);
		user.setStatus(status);
		user.setAttributes(attributes);
		user.isEnabled();
		user.setMobile(mobile);
		user.setEmail(email);
		user.setWeixin(weixin);
		
		return user;
	}
}
