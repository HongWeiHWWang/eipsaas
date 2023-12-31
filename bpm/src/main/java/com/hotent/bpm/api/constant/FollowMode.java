package com.hotent.bpm.api.constant;



/**
 * 会签满足会签结束条件后，后续处理类型。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-13-下午6:04:37
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public enum FollowMode {

	/**等待*/
    WAIT("wait","等待"),
    /**完成*/
    COMPLETE("complete","完成");
    
	// 键
	private String key = "";
	// 值
	private String value = "";

	// 构造方法
	private FollowMode(String key, String value) {
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
	public static FollowMode fromKey(String key) {
	for (FollowMode c : FollowMode.values()) {
			if (c.getKey().equalsIgnoreCase(key))
				return c;
		}
		throw new IllegalArgumentException(key);
	}
}