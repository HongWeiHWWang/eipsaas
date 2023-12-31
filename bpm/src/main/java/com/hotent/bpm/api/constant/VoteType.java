package com.hotent.bpm.api.constant;


/**
 * 投票类型。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-13-下午6:03:16
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public enum VoteType {

    
    AMOUNT("amount","票数"),
    PERCENT("percent","百分比");
	// 键
	private String key = "";
	// 值
	private String value = "";

	// 构造方法
	private VoteType(String key, String value) {
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
	public static VoteType fromKey(String key) {
	for (VoteType c : VoteType.values()) {
			if (c.getKey().equalsIgnoreCase(key))
				return c;
		}
		throw new IllegalArgumentException(key);
	}
}