package com.hotent.sys.persistence.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;

import com.hotent.sys.SysTestCase;
import com.hotent.sys.persistence.model.SysLogsSettings;

class SysLogsSettingManagerTest  extends SysTestCase{
	
	@Resource
	SysLogsSettingsManager sysLogsSettingsManager;
	
	@Test
	void test() {
		
		SysLogsSettings t = new SysLogsSettings();
		t.setModuleType("portal-eureka");
		t.setRemark("是否记录门户模块的操作日志");
		t.setSaveDays(30);
		t.setStatus("0");
		sysLogsSettingsManager.create(t);
		
		SysLogsSettings sysLogsSettings = sysLogsSettingsManager.get(t.getId());
		assertEquals(sysLogsSettings.getId(), t.getId());
		sysLogsSettings.setStatus("1");
		sysLogsSettingsManager.update(sysLogsSettings);
		sysLogsSettings = sysLogsSettingsManager.get(t.getId());
		assertEquals("1", sysLogsSettings.getStatus());
	}

}
