package com.hotent.sys.persistence.manager;

import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;

import com.hotent.base.util.BeanUtils;
import com.hotent.sys.SysTestCase;
import com.hotent.sys.persistence.model.SysIdentity;

public class SysIdentityManagerTest extends SysTestCase{

	@Resource
	SysIdentityManager sysIdentityManager;
	
	@Test
	public void testCur() throws UnsupportedEncodingException {
		
		SysIdentity sysIdentity = new SysIdentity();
		sysIdentity.setId(idGenerator.getSuid());
		sysIdentity.setAlias("testAlias");
		sysIdentity.setName("测试");
		sysIdentity.setRegulation("{yyyy}{MM}{dd}{NO}");
		sysIdentity.setGenType(new Short("1"));
		sysIdentity.setNoLength(5);
		sysIdentity.setCurDate("20180705");
		sysIdentity.setInitValue(1);
		sysIdentity.setCurValue(0);
		sysIdentity.setStep(new Short("1"));
		sysIdentityManager.create(sysIdentity);
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("id", sysIdentity.getId());
		params.put("alias", sysIdentity.getAlias());
		
		sysIdentityManager.isAliasExisted(params);
		
		System.out.println(sysIdentityManager.getCurIdByAlias("testAlias"));
		
		System.out.println(sysIdentityManager.nextId("testAlias"));
		
		System.out.println(sysIdentityManager.getPreviewIden("testAlias"));
		
		sysIdentityManager.remove(sysIdentity.getId());
		
		assertTrue(BeanUtils.isEmpty(sysIdentityManager.get(sysIdentity.getId())));
		
	}

}
