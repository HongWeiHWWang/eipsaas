package com.hotent.system.persistence.manager.impl;


import java.io.IOException;

import javax.annotation.Resource;

import com.hotent.base.util.StringUtil;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.HttpUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.system.consts.DingTalkConsts;
import com.hotent.system.consts.WeChatWorkConsts;
import com.hotent.system.model.WxUser;
import com.hotent.system.persistence.manager.IUserService;
import com.hotent.system.util.DingTalkHttpUtil;
import com.hotent.system.util.OrgConvertUtil;

/**
 * <pre>
 * 阿里钉钉，用户同步类
 * 日期：2019-12-03
 * @author Pangquan
 * </pre>
 */
@Component
public class DtUserService implements IUserService {

	@Resource
	UCFeignService userManager;

	@Override
	public void create(ObjectNode user) throws IOException {
		String mobile=user.get("mobile").asText();
		String fullname=user.get("fullname").asText();
		if(StringUtil.isEmpty(mobile))
			throw new RuntimeException(fullname+"添加钉钉通讯录失败 ：没有填写手机信息 ");

		if( this.queryUser(user)==true) {
			return;
		}

		WxUser wxUser = OrgConvertUtil.userToWxUser(user);
		if(BeanUtils.isEmpty(wxUser.getDepartment())) return;

		String resultJson = DingTalkHttpUtil.sendHttpsRequest(DingTalkConsts.getCreateUserUrl(), wxUser.toString(), "POST");
		JsonNode result = JsonUtil.toJsonNode(resultJson);

		String errcode = result.get("errcode").asText();
		if("0".equals(errcode)){
			return;
		}
		// 表示已经存在
		if("60102".equals(errcode)){
			return;
		}
		throw new RuntimeException("添加["+fullname+"]到钉钉通讯录失败 ： "+resultJson);
	}


	public boolean queryUser(ObjectNode user) throws IOException {
		String account=user.get("account").asText();
		//获取企业微信用户，判断是否存在
		String getUserUrl= DingTalkConsts.getUserUrl() + account;
		String resultJsonUser = HttpUtil.sendHttpsRequest(getUserUrl, "", "GET");
		JsonNode userJson = JsonUtil.toJsonNode(resultJsonUser);

		if("0".equals(userJson.get("errcode").asText())){
			return true;
		}
		return false;
	}
	@Override
	public void update(ObjectNode user) throws IOException {


	}

	@Override
	public void delete(String userId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll(String userIds) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addAll(JsonNode users) throws IOException {
		for (JsonNode user : users){
			ObjectNode node= (ObjectNode) user;
			//SysUser u =(SysUser) user;
			// u.getHasSyncToWx() ==1
			String mobile=user.get("mobile").asText();
			if(StringUtil.isEmpty(mobile))
				continue;

			this.create(node);
		}

	}

	@Override
	public void syncUser(String[] lAryId) throws IOException {
		ArrayNode userList = JsonUtil.getMapper().createArrayNode();
//		iwxOrgService.syncAllOrg();//不同步组织结构，把用户都挂在顶级的组织上
		if(BeanUtils.isNotEmpty(lAryId)){
			for (int i = 0; i < lAryId.length; i++) {
				ObjectNode user = (ObjectNode) userManager.getUserById(lAryId[i]).getValue();
				if(user != null) userList.add(user);
			}
		}else{
			userList = (ArrayNode) userManager.getAllUser();
		}
		addAll(userList);

	}

}
