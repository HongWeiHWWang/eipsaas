package com.hotent.sys.persistence.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;

import com.hotent.sys.SysTestCase;
import com.hotent.sys.persistence.model.SysLogs;

class SysLogsManagerTest extends SysTestCase{
	
	@Resource
	SysLogsManager sysLogsManager; 
	
	@Test
	void test() {
		SysLogs t = new SysLogs();
		t.setExecutionTime(LocalDateTime.now());
		t.setIp("127.0.0.1");
		t.setOpeContent("jason单元测试");
		t.setOpeName("jason单元测试");
		t.setReqUrl("/oa/test/reqUrl");
		sysLogsManager.create(t);
		
		SysLogs sysLogs = sysLogsManager.get(t.getId());
		
		assertEquals(sysLogs.getId(), t.getId());
		sysLogs.deleteById(sysLogs.getId());
		
	}

}
