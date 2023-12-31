package com.hotent.sys.persistence.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryField;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.sys.SysTestCase;
import com.hotent.sys.persistence.model.SysMethod;
import com.hotent.sys.persistence.param.SysRoleAuthParam;

public class SysMethodManagerTest extends SysTestCase {
	
	@Resource
	SysMethodManager sysMethodManager;
	
	@Resource 
	SysRoleAuthManager sysRoleAuthManager;
	
	
	@Test
	public void testCurd() {
		
		String id = idGenerator.getSuid();

		SysMethod entity = new SysMethod();
		entity.setAlias(id);
		entity.setId(id);
		entity.setMenuAlias("user.list"+id);
		entity.setRequestUrl("/user/list");
		sysMethodManager.create(entity);
		assertEquals(id, entity.getId());
		
		entity.setId(idGenerator.getSuid());
		entity.setAlias(entity.getId());
		sysMethodManager.create(entity);
		
		List<SysMethod> entityList = new ArrayList<SysMethod>();
		List<String> methodsAlias = new ArrayList<String>();
		methodsAlias.add(id);
		for (int i = 0; i < 100; i++) {
			SysMethod sysMethod = new SysMethod();
			String suid = idGenerator.getSuid();
			sysMethod.setAlias(suid);
			sysMethod.setId(suid);
			sysMethod.setMenuAlias("user.list"+suid);
			sysMethod.setRequestUrl("/user/list");
			entityList.add(sysMethod);
			methodsAlias.add(sysMethod.getId());
		}
		sysMethodManager.saveBatch(entityList);
		
		
		SysRoleAuthParam t = new SysRoleAuthParam();
		t.setRoleAlias("roleAlias");
		t.setArrMethodAlias(methodsAlias);
		t.setArrMenuAlias(Arrays.asList("11"));
		t.setDataPermission(new HashMap<String, String>());
		sysRoleAuthManager.create(t);
		
		SysMethod sysMethod = sysMethodManager.get(id);
		assertTrue(BeanUtils.isNotEmpty(sysMethod));
		
		assertTrue(BeanUtils.isNotEmpty(sysMethod));
		
		
		QueryFilter<SysMethod> queryFilter = QueryFilter.<SysMethod>build()
				.withDefaultPage().withQuery(new QueryField("method.alias_", id)); 
		PageList<SysMethod> roleMethods = sysMethodManager.getRoleMethods("roleAlias", queryFilter);
		System.out.println(roleMethods);
		
		QueryFilter<SysMethod> queryFilter2 = QueryFilter.<SysMethod>build();
		roleMethods = sysMethodManager.getRoleMethods("roleAlias", queryFilter2);
		System.out.println(roleMethods);
		
		sysMethodManager.remove(id);
	}

}
