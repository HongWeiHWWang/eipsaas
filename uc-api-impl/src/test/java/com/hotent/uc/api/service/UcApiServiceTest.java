package com.hotent.uc.api.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.hotent.uc.api.UcApiTestCase;
import com.hotent.uc.api.model.IUser;

/**
 * UcApiServiceTest 接口测试
 * @company 广州宏天软件有限公司
 * @author wanghb
 * @email wanghb@jee-soft.cn
 * @date 2018年6月29日
 */
public class UcApiServiceTest extends UcApiTestCase{
	@Resource
	IUserService userService;
	
	@Test
	public void testCurd() throws Exception{//cleaner9101001
		
		IUser user = userService.getUserById("1");
		assertEquals("1", user.getUserId());
		
		IUser user1 = userService.getUserByAccount("zngleiqh");
		assertEquals("zngleiqh", user1.getAccount());
		
		List<IUser> users = userService.getByEmail("yz@cmstest.com");
		System.out.println(users.size());
		
	}
	
}
