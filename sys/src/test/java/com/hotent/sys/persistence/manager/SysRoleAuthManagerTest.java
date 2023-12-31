package com.hotent.sys.persistence.manager;

import static org.junit.Assert.*;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.hotent.base.util.BeanUtils;
import com.hotent.sys.SysTestCase;
import com.hotent.sys.persistence.model.SysRoleAuth;


public class SysRoleAuthManagerTest extends SysTestCase {
	
	@Resource
	SysRoleAuthManager sysRoleAuthManager;
	
	@Test
	public void testCrud() {
		String id = idGenerator.getSuid();
		SysRoleAuth entity =  new SysRoleAuth();
		entity.setId(id);
		entity.setMenuAlias(id);
		entity.setRoleAlias(id);
		entity.setMethodAlias(id);
		
		sysRoleAuthManager.create(entity  );
		
		assertEquals(id,entity.getId());
		
		entity = sysRoleAuthManager.get(id);
		assertTrue(BeanUtils.isNotEmpty(entity));
		
		entity.setRoleAlias("test"+id);
		sysRoleAuthManager.update(entity);
		
		entity = sysRoleAuthManager.get(id);
		assertEquals("test"+id,entity.getRoleAlias());
		
		sysRoleAuthManager.remove(id);
		entity = sysRoleAuthManager.get(id);
		assertTrue(BeanUtils.isEmpty(entity));
		
		
	}
	
	@Test
	public void testByRoleAlias() {
		String id = idGenerator.getSuid();
		SysRoleAuth entity =  new SysRoleAuth();
		entity.setId(id);
		entity.setMenuAlias(id);
		entity.setRoleAlias(id);
		entity.setMethodAlias(id);
		
		sysRoleAuthManager.create(entity  );
		
		assertEquals(id,entity.getId());
	
		List<SysRoleAuth> sysRoleAuthByRoleAlias = sysRoleAuthManager.getSysRoleAuthByRoleAlias(id);
		assertEquals(1,sysRoleAuthByRoleAlias.size());
		
		
		List<String> menuAliasByRoleAlias = sysRoleAuthManager.getMenuAliasByRoleAlias(id);
		assertEquals(1,menuAliasByRoleAlias.size());
		
		
		List<String> methodAliasByRoleAlias = sysRoleAuthManager.getMethodAliasByRoleAlias(id);
		assertEquals(1,methodAliasByRoleAlias.size());
		
		entity = sysRoleAuthManager.get(id);
		assertTrue(BeanUtils.isNotEmpty(entity));
		
		entity.setRoleAlias("test"+id);
		sysRoleAuthManager.update(entity);
		
		entity = sysRoleAuthManager.get(id);
		assertEquals("test"+id,entity.getRoleAlias());
		
		
		sysRoleAuthManager.removeByRoleAlias(id);
		
		entity = sysRoleAuthManager.get("test"+id);
		assertTrue(BeanUtils.isEmpty(entity));
		
		
	}

}
