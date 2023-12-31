package com.hotent.bpm.api.constant;

/**
 * 投票结果。
 * <pre>
 * no：未投票
 * agree:赞成
 * oppose:赞成
 * abandon:弃权 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-31-下午5:45:27
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public enum VoteResult {

	
	NO("no","未投票"),
	AGREE("agree","赞成"),
	OPPOSE("oppose","赞成"),
	ABANDON("abandon","弃权");
	
	// 键
	private String key = "";
	// 值
	private String value = "";

	// 构造方法
	private VoteResult(String key, String value) {
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
	public static VoteResult fromKey(String key) {
	for (VoteResult c : VoteResult.values()) {
			if (c.getKey().equalsIgnoreCase(key))
				return c;
		}
		throw new IllegalArgumentException(key);
	}
}
