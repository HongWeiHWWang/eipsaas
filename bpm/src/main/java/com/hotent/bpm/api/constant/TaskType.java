package com.hotent.bpm.api.constant;

/**
 * 任务状态。
 * <pre>
 * 普通任务(NORMAL)
 * 代理任务(AGENT)
 * 转交任务(DELIVERTO)
 * 流转源任务(TRANSFORMING ,在列表不可见)	
 * 接收流转任务(TRANSFORMED)
 * 通知任务(COMMU)
 * 被驳回任务(BACK)
 *  
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-2-15-下午11:27:12
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public enum TaskType {
	NORMAL("NORMAL","普通任务"),
	AGENT("AGENT","代理任务"),
	DELIVERTO("DELIVERTO","转办任务"),
	TRANSFORMING("TRANSFORMING","流转源任务"),
	TRANSFORMED("TRANSFORMED","接收流转任务"),
	APPROVELINEING("APPROVELINEING","并行审批源任务"),
	APPROVELINEED("APPROVELINEED","接收并行审批任务"),
	SIGNSEQUENCEING("SIGNSEQUENCEING","顺序签署源任务"),
	SIGNSEQUENCEED("SIGNSEQUENCEED","接收顺序签署任务"),
	SIGNLINEING("SIGNLINEING","并行签署源任务"),
	SIGNLINEED("SIGNLINEED","接收并行签署任务"),
    TRANSFORMEDINQU("TRANSFORMEDINQU","征询任务"),
	COMMU("COMMU","通知任务"),
	BACK("BACK","被驳回任务"),
	ADDSIGN("ADDSIGN","加签"),
	COPYTO("COPYTO","传阅"),
	SHARE("SHARE","共享"),
	BACKSHARE("BACKSHARE","驳回共享"),
	FOLLOW("FOLLOW","跟踪");
	
	// 键
	private String key = "";
	// 值
	private String value = "";

	// 构造方法
	private TaskType(String key, String value) {
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
	public static TaskType fromKey(String key) {
		for (TaskType c : TaskType.values()) {
			if (c.getKey().equalsIgnoreCase(key))
				return c;
		}
		throw new IllegalArgumentException(key);
	}
}
