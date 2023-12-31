package com.hotent.bpm.persistence.util;

import java.util.List;

import org.springframework.util.Assert;

import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.helper.identity.BpmIdentityExtractService;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserService;

public class ServiceUtil {
	/**
	 * 通过userId获取用户
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public static IUser getUserById(String userId) throws Exception{
		if (StringUtil.isEmpty(userId))
		{
			throw new RuntimeException("必须传入用户ID(userId)!");
		}
		IUserService userServiceImpl=AppUtil.getBean(IUserService.class);
		IUser user = userServiceImpl.getUserById(userId);
		if (BeanUtils.isEmpty(user))
		{
			throw new Exception("该用户不存在,请确认传入的userId是否存在");
		}
		return user;
	}
	
	/**
	 * 根据用户账户获取用户
	 * 
	 * @param account
	 * @return
	 * @throws Exception
	 */
	public static IUser getUserByAccount(String account)
	{
		Assert.isTrue(StringUtil.isNotEmpty(account), "必须传入用户账号");
		IUserService userServiceImpl=AppUtil.getBean(IUserService.class);
		IUser user = userServiceImpl.getUserByAccount(account);
		Assert.isTrue(BeanUtils.isNotEmpty(user), String.format("账号为：%s的用户不存在", account));
		return user;
	}

	/**
	 * 根据流程处理人抽取用户列表
	 * @param bpmIdentities
	 * @return
	 * @throws Exception
	 */
	public static List<IUser> extractUser(List<BpmIdentity> bpmIdentities) throws Exception{
		if (BeanUtils.isEmpty(bpmIdentities))
		{
			throw new RuntimeException("必须传入流程处理人(bpmIdentities)!");
		}
		BpmIdentityExtractService bpmIdentityExtractService = AppUtil.getBean(BpmIdentityExtractService.class);
		List<IUser> extractUser = bpmIdentityExtractService.extractUser(bpmIdentities);
		// 去重复
		BeanUtils.removeDuplicate(extractUser);
		return extractUser;
	}

	public static void setCurrentUser(String account) throws Exception{
		ContextUtil.setCurrentUserByAccount(account);
	}
}