package com.hotent.sys.persistence.manager;

import static org.junit.Assert.assertTrue;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;

import com.hotent.base.util.BeanUtils;
import com.hotent.sys.SysTestCase;
import com.hotent.sys.persistence.model.SysDataSource;

class SysDataSourceManagerTest extends SysTestCase{
	
	@Resource
	SysDataSourceManager sysDataSourceManager;
	
	@Test
	void testCreateSysDataSource() {
		SysDataSource t = new SysDataSource();
		t.setAlias("jason");
		t.setClassPath("com.alibaba.druid.pool.DruidDataSource");
		t.setDbType("mysql");
		t.setSettingJson("[{\"name\":\"username\",\"comment\":\"username\",\"type\":\"java.lang.String\",\"baseAttr\":\"1\",\"default\":\"root\",\"value\":\"sa\"},{\"name\":\"password\",\"comment\":\"password\",\"type\":\"java.lang.String\",\"baseAttr\":\"1\",\"default\":\"root\",\"value\":\"root\"},{\"isAdd\":true,\"name\":\"url\",\"comment\":\"url\",\"type\":\"java.lang.String\",\"baseAttr\":\"1\",\"default\":\"\",\"value\":\"jdbc:sqlserver://192.168.1.10:1433;SelectMethod=cursor;databaseName=x5.8\"},{\"name\":\"initialSize\",\"comment\":\"initialSize\",\"type\":\"int\",\"baseAttr\":\"1\",\"default\":\"10\",\"value\":\"10\"},{\"name\":\"maxActive\",\"comment\":\"maxActive\",\"type\":\"int\",\"baseAttr\":\"1\",\"default\":\"100\",\"value\":\"100\"},{\"name\":\"minIdle\",\"comment\":\"minIdle\",\"type\":\"int\",\"baseAttr\":\"1\",\"default\":\"10\",\"value\":\"10\"},{\"name\":\"maxWait\",\"comment\":\"maxWait\",\"type\":\"long\",\"baseAttr\":\"1\",\"default\":\"60000\",\"value\":\"60000\"},{\"name\":\"validationQuery\",\"comment\":\"validationQuery\",\"type\":\"java.lang.String\",\"baseAttr\":\"1\",\"default\":\"select 1 from dual\",\"value\":\"select 1\"},{\"name\":\"testOnBorrow\",\"comment\":\"testOnBorrow\",\"type\":\"boolean\",\"baseAttr\":\"1\",\"default\":\"false\",\"value\":\"false\"},{\"name\":\"testOnReturn\",\"comment\":\"testOnReturn\",\"type\":\"boolean\",\"baseAttr\":\"1\",\"default\":\"false\",\"value\":\"false\"},{\"name\":\"testWhileIdle\",\"comment\":\"testWhileIdle\",\"type\":\"boolean\",\"baseAttr\":\"1\",\"default\":\"true\",\"value\":\"true\"},{\"name\":\"poolPreparedStatements\",\"comment\":\"poolPreparedStatements\",\"type\":\"boolean\",\"baseAttr\":\"1\",\"default\":\"true\",\"value\":\"true\"},{\"name\":\"maxPoolPreparedStatementPerConnectionSize\",\"comment\":\"maxPoolPreparedStatementPerConnectionSize\",\"type\":\"int\",\"baseAttr\":\"1\",\"default\":\"20\",\"value\":\"20\"},{\"name\":\"filters\",\"comment\":\"filters\",\"type\":\"java.util.List\",\"baseAttr\":\"1\",\"default\":\"stat\",\"value\":\"stat\"},{\"name\":\"timeBetweenEvictionRunsMillis\",\"comment\":\"timeBetweenEvictionRunsMillis\",\"type\":\"long\",\"baseAttr\":\"1\",\"default\":\"60000\",\"value\":\"60000\"},{\"name\":\"minEvictableIdleTimeMillis\",\"comment\":\"minEvictableIdleTimeMillis\",\"type\":\"long\",\"baseAttr\":\"1\",\"default\":\"300000\",\"value\":\"300000\"},{\"name\":\"removeAbandoned\",\"comment\":\"removeAbandoned\",\"type\":\"boolean\",\"baseAttr\":\"1\",\"default\":\"true\",\"value\":\"true\"},{\"name\":\"removeAbandonedTimeoutMillis\",\"comment\":\"removeAbandonedTimeoutMillis\",\"type\":\"long\",\"baseAttr\":\"1\",\"default\":\"60000\",\"value\":\"60000\"},{\"name\":\"logAbandoned\",\"comment\":\"logAbandoned\",\"type\":\"boolean\",\"baseAttr\":\"1\",\"default\":\"true\",\"value\":\"true\"},{\"isAdd\":true,\"name\":\"breakAfterAcquireFailure\",\"comment\":\"breakAfterAcquireFailure\",\"type\":\"boolean\",\"baseAttr\":\"1\",\"default\":\"true\",\"value\":\"true\"}]");
		t.setName("jason测试");
		t.setInitMethod(null);
		t.setEnabled(true);
		t.setInitOnStart(false);
		sysDataSourceManager.create(t);
		
		testUpdateSysDataSource(t.getId());
		
		testIsAliasExist(t.getAlias());
		
		testGetByAlias(t.getAlias());
	}

	void testUpdateSysDataSource(String id) {
		SysDataSource sysDataSource = sysDataSourceManager.get(id);
		assertTrue("jason测试".equals(sysDataSource.getName()));
		assertTrue(BeanUtils.isEmpty(sysDataSource.getInitMethod()));
		sysDataSource.setName("jason测试更新");
		sysDataSourceManager.update(sysDataSource);
		sysDataSource = sysDataSourceManager.get(id);
		assertTrue("jason测试更新".equals(sysDataSource.getName()));
	}
	
	void testIsAliasExist(String alias) {
		boolean aliasExist = sysDataSourceManager.isAliasExist(alias);
		assertTrue(aliasExist);
	}
	
	void testGetByAlias(String alias) {
		SysDataSource byAlias = sysDataSourceManager.getByAlias(alias);
		assertTrue(BeanUtils.isNotEmpty(byAlias));
	}


	@Test
	void testGetDsFromSysSource() {
	}

	@Test
	void testCheckConnection() {
	}

	@Test
	void testGetSysDataSourcesInBean() {
	}

	
	@Test
	void testGetDataSource() {
	}

	@Test
	void testGetDefaultDataSource() {
	}

	@Test
	void testSetDbContextDataSource() {
	}

	@Test
	void testGetDbType() {
	}

	
}
