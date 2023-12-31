package com.hotent.bpm.api.constant;

/**
 * 多实例类型。
 * <pre> 
 * 多实例：
 * 1.单实例。
 * 2.并行。
 * 3.串行。
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-4-18-下午3:39:23
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public enum MultiInstanceType {
	
	NO("no","单实例"),
	PARALLEL("parallel","并行"),
	SEQUENTIAL("sequential","串行");
	
	// 键
	private String key = "";
	// 值
	private String value = "";

	// 构造方法
	private MultiInstanceType(String key, String value) {
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
	public static MultiInstanceType fromKey(String key) {
	for (MultiInstanceType c : MultiInstanceType.values()) {
			if (c.getKey().equalsIgnoreCase(key))
				return c;
		}
		throw new IllegalArgumentException(key);
	}
}
