package com.hotent.bpm.api.constant;

/**
 * 
 * <pre> 
 * 描述：事件类型,如节点创建事件,节点结束事件,任务创建事件,任务结束事件等
 * 现在一般供插件开发使用，在各个Bpmn Listener的实现中设置。
 * 构建组：x5-bpmx-api
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2014-2-13-下午6:14:33
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public enum EventType {
	/**流程启动（流程实例已经生成，但辅助操作未做）*/
	START_EVENT("startEvent","流程启动"),	
	/**流程启动辅助操作完成后*/
	START_POST_EVENT("postStartEvent","流程启动辅助操作完成后"),
	/**流程结束之前*/
	END_EVENT("endEvent","流程结束之前"),													
	/**流程结束之后*/
	END_POST_EVENT("postEndEvent","流程结束之后"),								
	/**任务创建（任务已生成，但未分配人员）*/
	TASK_CREATE_EVENT("taskCreate","任务创建（任务已生成，但未分配人员）"),								
	/**任务创建之后（已分配人员）*/
	TASK_POST_CREATE_EVENT("postTaskCreate","任务创建之后（已分配人员）"),
	/**任务完成*/
	TASK_COMPLETE_EVENT("taskComplete","任务完成"),
	/**任务完成之后（执行完工作流本身辅助操作）*/
	TASK_POST_COMPLETE_EVENT("postTaskComplete","任务完成之后（执行完工作流本身辅助操作）"),
	/**会签任务创建*/
	TASK_SIGN_CREATE_EVENT("taskSignCreate","会签任务创建"),
	/**会签任务创建之后（已分配人员）*/
	TASK_SIGN_POST_CREATE_EVENT("postTaskSignCreate","会签任务创建之后（已分配人员）"),
	/**自动任务完成*/
	AUTO_TASK_EVENT("autoTaskEvent","自动任务完成");
	
	// 键
	private String key = "";
	// 值
	private String value = "";

	// 构造方法
	private EventType(String key, String value) {
		this.key = key;
		this.value = value;
	}

	// =====getting and setting=====
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return key;
	}

	/**
	 * 通过key获取对象
	 * 
	 * @param key
	 * @return
	 */
	public static EventType fromKey(String key) {
	for (EventType c : EventType.values()) {
			if (c.getKey().equalsIgnoreCase(key))
				return c;
		}
		throw new IllegalArgumentException(key);
	}

}
