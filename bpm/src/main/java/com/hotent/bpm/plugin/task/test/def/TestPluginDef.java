package com.hotent.bpm.plugin.task.test.def;

import com.hotent.bpm.plugin.core.plugindef.AbstractBpmTaskPluginDef;
import com.hotent.bpm.plugin.task.test.entity.TestPluginEntity;

public class TestPluginDef extends AbstractBpmTaskPluginDef {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2787890606261585685L;
	private TestPluginEntity testPluginEntity;
	
	
	
	public TestPluginEntity getTestPluginEntity() {
		return testPluginEntity;
	}

	public void setTestPluginEntity(TestPluginEntity testPluginEntity) {
		this.testPluginEntity = testPluginEntity;
	}

}
