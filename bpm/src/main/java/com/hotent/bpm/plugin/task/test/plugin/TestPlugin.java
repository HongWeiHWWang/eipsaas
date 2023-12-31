package com.hotent.bpm.plugin.task.test.plugin;

import com.hotent.bpm.api.constant.EventType;
import com.hotent.bpm.api.model.delegate.BpmDelegateTask;
import com.hotent.bpm.api.plugin.core.def.BpmTaskPluginDef;
import com.hotent.bpm.api.plugin.core.session.BpmTaskPluginSession;
import com.hotent.bpm.plugin.core.runtime.AbstractBpmTaskPlugin;
import com.hotent.bpm.plugin.task.test.def.TestPluginDef;
import com.hotent.bpm.plugin.task.test.entity.TestPluginEntity;


public class TestPlugin extends AbstractBpmTaskPlugin{

	@Override
	public Void execute(BpmTaskPluginSession pluginSession,BpmTaskPluginDef pluginDef) throws Exception {
		String type=(pluginSession.getEventType()==EventType.TASK_COMPLETE_EVENT)?"任务完成":"任务创建";
		TestPluginDef testDef = (TestPluginDef) pluginDef;
		TestPluginEntity  entity = testDef.getTestPluginEntity();
		String testMsg = entity.getTestMessage();
		BpmDelegateTask task =pluginSession.getBpmDelegateTask();
		System.err.println(task.getName()+"-"+type+"时执行节点插件。输出配置消息:"+testMsg);
		return null;
	}

}
