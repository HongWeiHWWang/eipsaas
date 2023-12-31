package com.hotent.bpm.api.constant;

public enum TaskActionType {
	AGREE("agree","同意"),
	BACK("back","驳回"),
	END("end","结束");
	
	// 键
	private String key = "";
	// 值
	private String value = "";

	// 构造方法
	private TaskActionType(String key, String value) {
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
	public static TaskActionType fromKey(String key) {
	for (TaskActionType c : TaskActionType.values()) {
			if (c.getKey().equalsIgnoreCase(key))
				return c;
		}
		throw new IllegalArgumentException(key);
	}
}
