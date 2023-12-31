package com.hotent.sys.persistence.manager;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.hotent.base.util.BeanUtils;
import com.hotent.sys.SysTestCase;
import com.hotent.sys.persistence.model.SysAuthUser;
/**
 * 
 * @author jason
 * @date 2020-04-14
 *
 */
public class SysAuthUserManagerTest extends SysTestCase{

	@Resource
	SysAuthUserManager sysAuthUserManager;
	
	@Test
	public void testCRUD() {
		SysAuthUser entity = new SysAuthUser();
		entity.setOwnerId("1");
		entity.setOwnerName("jason");
		entity.setRightType("all");
		entity.setAuthorizeId("1");
		entity.setObjType("test");
		sysAuthUserManager.create(entity);
		SysAuthUser sysAuthUser = sysAuthUserManager.get(entity.getId());
		assertTrue(BeanUtils.isNotEmpty(sysAuthUser));
		
		List<String> authorizeIdsByUserMap = sysAuthUserManager.getAuthorizeIdsByUserMap("test");
		assertTrue(BeanUtils.isEmpty(authorizeIdsByUserMap));
		
		try {
			ArrayNode rights = sysAuthUserManager.getRights(entity.getAuthorizeId(), entity.getObjType());
			assertTrue(BeanUtils.isNotEmpty(rights));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		boolean hasRights = sysAuthUserManager.hasRights(entity.getAuthorizeId());
		assertTrue(!hasRights);
		
		sysAuthUserManager.remove(entity.getId());
		
	}
	
}
