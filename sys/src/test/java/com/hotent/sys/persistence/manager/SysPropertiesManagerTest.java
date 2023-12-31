package com.hotent.sys.persistence.manager;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.hotent.base.util.SQLUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.sys.SysTestCase;
import com.hotent.sys.persistence.model.SysProperties;

/**
 * BoEntManager测试
 * 
 * @company 广州宏天软件股份有限公司
 * @author liyg
 * @date 2018年6月27日
 */
public class SysPropertiesManagerTest extends SysTestCase{
	@Resource
	SysPropertiesManager sysPropertiesManager;
	
	
	@Test
	public void testCurd() throws Exception{
		
		List<SysProperties> oldAll = sysPropertiesManager.list();
		
		String suid = UniqueIdUtil.getSuid();
		
		SysProperties entity = new SysProperties();
		entity.setId(suid);
		entity.setAlias("test");
		entity.setEncrypt(0);
		entity.setName("测试");
		entity.setValue("test");
		sysPropertiesManager.create(entity);
		
		SysProperties sysProperties = sysPropertiesManager.get(entity.getId());
		
		logger.debug("插入数据和查询结果是否成功： "  );
		assertEquals(entity.getId(), sysProperties.getId());
		List<SysProperties> all = sysPropertiesManager.list();
		
		assertEquals(oldAll.size()+1, all.size());
		
		String byAlias = sysPropertiesManager.getByAlias("test");
		assertEquals("test", byAlias);
		
		for (int i = 0; i < 20; i++) {
			String dbType = SQLUtil.getDbType();
			System.out.println(dbType);
		}
		
	}
}
