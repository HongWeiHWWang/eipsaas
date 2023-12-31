package com.hotent.sys.persistence.manager;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;

import com.hotent.sys.SysTestCase;
import com.hotent.sys.persistence.model.SysDataSourceDef;

class SysDataSourceDefManagerTest extends SysTestCase {

	@Resource
	SysDataSourceDefManager mananager;
	
	@Test
	void test() {
		SysDataSourceDef entity = new SysDataSourceDef();
		entity.setName("jason测试");
		mananager.save(entity);
		
	}

}
