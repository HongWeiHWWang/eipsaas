package com.hotent.uc.api.impl.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.uc.api.constant.GroupTypeConstant;
import com.hotent.uc.api.impl.model.UserFacade;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserService;

/**
 * 类 {@code UserServiceImpl} 用户服务的实现
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月9日
 */
@Primary
@Service
public class UserServiceImpl implements IUserService {
	@Resource
	UCFeignService ucFeignService;
	@Resource
	UserCacheImpl userCacheImpl;

	// 获得ObjectNode
	private ObjectNode getObjectNode() {
		return JsonUtil.getMapper().createObjectNode();
	}

	public IUser getUserById(String userId)  {
		try {
			UserFacade userfacade = userCacheImpl.getUserFromCacheById(userId);
			if(userfacade!=null) {
				return userfacade;
			}
			CommonResult<JsonNode> ObjectNode = ucFeignService.getUserById(userId);
			if(ObjectNode.getState()){
				userfacade = JsonUtil.toBean(ObjectNode.getValue(), UserFacade.class);
				userCacheImpl.putUserInCache(userfacade);
				return userfacade;
			}else{
				return null;
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public IUser getUserByAccount(String account)  {
		try {
			JsonNode result = ucFeignService.getUser(account, "");
 			if(BeanUtils.isEmpty(result)){
				return null;
			}
			UserFacade bean = JsonUtil.toBean(result, UserFacade.class);
			return bean;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	@Override
	public List<IUser> getUserByAccounts(String accounts)  {
		ObjectNode params=getObjectNode();
		params.put("accounts", accounts);
		try {
			ArrayNode node = ucFeignService.getUserByAccounts(accounts);
			List<IUser> bean = new ArrayList<IUser>();
			for(JsonNode j :node){
				bean.add(JsonUtil.toBean(j, UserFacade.class));
			}
			return bean;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	@Override
	public List<IUser> getUserListByGroup(String groupType, String groupId)  {
		// 此处可以根据不同的groupType去调用真实的实现：如角色下的人，组织下的人
		JsonNode result= JsonUtil.getMapper().createObjectNode();
		if (groupType.equals(GroupTypeConstant.ORG.key())) {
			QueryFilter queryFilter=QueryFilter.build();
			queryFilter.setPageBean(new PageBean(1, Integer.MAX_VALUE));
			queryFilter.addFilter("org_id_", groupId, QueryOP.EQUAL);
			result = ucFeignService.getAllOrgUsers(queryFilter);
		}
		if (groupType.equals(GroupTypeConstant.ROLE.key())) {
			result = ucFeignService.getUsersByRoleCode(groupId);
		}
		if (groupType.equals(GroupTypeConstant.POSITION.key())) {
			result = ucFeignService.getUserByPost(groupId);
		}
		
		if (groupType.equals(GroupTypeConstant.JOB.key())) {
			result = ucFeignService.getUsersByJob(groupId);
		}
		if (BeanUtils.isEmpty(result)) {
			return new ArrayList<>();
		}
		List<UserFacade> list = null;
		try {
			list = (List<UserFacade>) JsonUtil.toBean(!groupType.equals(GroupTypeConstant.ORG.key())?JsonUtil.toJson(result):JsonUtil.toJson(result.get("rows")),new TypeReference<List<UserFacade>>(){});
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} 
		List<IUser> Ilist = new ArrayList<>();
		for (UserFacade user : list) {
			Ilist.add((IUser)user);
		}
		return Ilist;
	}

	@Override
	public List<IUser> getByEmail(String email) {
		ObjectNode params=getObjectNode();
		params.put("email", email);
		List<UserFacade> list = null;
		try {
			
			JsonNode result = ucFeignService.getUserByEmail(email);
			
			list = (List<UserFacade>) JsonUtil.toBean(JsonUtil.toJson(result),new TypeReference<List<UserFacade>>(){});
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		List<IUser> Ilist = new ArrayList<>();
		if(BeanUtils.isNotEmpty(list)) {
			for (UserFacade user : list) {
				Ilist.add((IUser)user);
			}
		}
		return Ilist;
	}
}