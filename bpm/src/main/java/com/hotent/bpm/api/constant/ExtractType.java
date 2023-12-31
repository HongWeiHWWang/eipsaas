package com.hotent.bpm.api.constant;

/**
 * 人员抽取类型。
 * <pre> 
 * 描述：人员抽取类型。
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2013-12-18-下午2:26:27
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public enum ExtractType {
	/**不抽取*/
	EXACT_NOEXACT("no","不抽取"),
	/**抽取用户*/
	EXACT_EXACT_USER("extract","抽取用户"),	
	/**延迟抽取*/
	EXACT_EXACT_DELAY("secondExtract","延迟抽取"),		
	/**组用户抽取*/
	EXACT_GROUP_USER("usergroup","组用户抽取");			
	
	// 键
	private String key = "";
	// 值
	private String value = "";

	// 构造方法
	private ExtractType(String key, String value) {
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
	public static ExtractType fromKey(String key) {
	for (ExtractType c : ExtractType.values()) {
			if (c.getKey().equalsIgnoreCase(key))
				return c;
		}
		throw new IllegalArgumentException(key);
	}	
}
