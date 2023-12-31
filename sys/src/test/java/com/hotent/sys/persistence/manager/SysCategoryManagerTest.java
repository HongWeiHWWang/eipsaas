package com.hotent.sys.persistence.manager;

import javax.annotation.Resource;

import org.junit.Test;

import com.hotent.base.util.BeanUtils;
import com.hotent.sys.SysTestCase;
import com.hotent.sys.persistence.model.SysCategory;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SysCategoryManagerTest extends SysTestCase{
	
	@Resource
	SysCategoryManager sysCategoryManager;
	
	@Test
	public void testCur(){
		SysCategory sysCategory = new SysCategory();
		sysCategory.setId(idGenerator.getSuid());
		sysCategory.setGroupKey("test");
		sysCategory.setName("测试");
		sysCategory.setFlag(1);
		sysCategory.setSn(12);
		sysCategory.setType(new Short("1"));
		
		sysCategoryManager.create(sysCategory);
		
		SysCategory sysCategory2 = sysCategoryManager.get(sysCategory.getId());
		assertEquals(sysCategory2.getId(),sysCategory.getId());
		
		assertTrue(sysCategoryManager.isKeyExist(null, sysCategory.getGroupKey()));
		
		assertTrue(BeanUtils.isNotEmpty(sysCategoryManager.getByTypeKey(sysCategory.getGroupKey())));
		
		sysCategoryManager.remove(sysCategory.getId());
	}

}
