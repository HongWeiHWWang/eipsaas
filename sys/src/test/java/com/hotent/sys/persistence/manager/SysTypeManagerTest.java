package com.hotent.sys.persistence.manager;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;

import org.junit.Test;

import com.hotent.base.util.BeanUtils;
import com.hotent.sys.SysTestCase;
import com.hotent.sys.persistence.model.SysType;
import static org.junit.Assert.assertTrue;

public class SysTypeManagerTest extends SysTestCase{

	@Resource
	SysTypeManager sysTypeManager;
	
	@Test
	public void testCur() throws UnsupportedEncodingException {
		SysType sysType = new SysType();
		sysType.setId(idGenerator.getSuid());
		sysType.setTypeGroupKey("DIC");
		sysType.setName("test");
		sysType.setTypeKey("test");
		sysType.setStruType(new Short("1"));
		sysType.setParentId("-1");
		sysType.setDepth(1);
		sysType.setPath("11.11");
		sysType.setSn(121212);
		sysType.setOwnerId("0");
		sysTypeManager.create(sysType);
		
		SysType sysType2 = sysTypeManager.get(sysType.getId());
		
		assertTrue(BeanUtils.isNotEmpty(sysType2));
		
		assertTrue(BeanUtils.isNotEmpty(sysTypeManager.getByParentId("0")));
		
		//assertTrue(BeanUtils.isNotEmpty(sysTypeManager.getInitSysType(1,"2")));
		
		assertTrue(BeanUtils.isNotEmpty(sysTypeManager.getInitSysType(0,sysType.getId())));
		
		assertTrue((sysTypeManager.isKeyExist(null, sysType.getTypeGroupKey(), sysType.getTypeKey())));
		
		assertTrue(BeanUtils.isNotEmpty(sysTypeManager.getByGroupKey(sysType.getTypeGroupKey())));
		
		assertTrue(BeanUtils.isNotEmpty(sysTypeManager.getPrivByPartId("-1","0")));
		
		sysTypeManager.updSn(sysType.getId(),121);
		
		assertTrue(BeanUtils.isEmpty(sysTypeManager.getChildByTypeKey(sysType.getTypeKey())));
		
		assertTrue(BeanUtils.isNotEmpty(sysTypeManager.getByKey(sysType.getTypeKey())));
		
		assertTrue(BeanUtils.isNotEmpty(sysTypeManager.getXmlByKey(sysType.getTypeKey())));
		
		assertTrue(BeanUtils.isNotEmpty(sysTypeManager.getByTypeKeyAndGroupKey(sysType.getTypeGroupKey(),sysType.getTypeKey())));

		sysTypeManager.delByIds(sysType.getId());
	}	

}
