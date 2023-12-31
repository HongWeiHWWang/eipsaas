package com.hotent.bpm.api.constant;

/**
 * 设计器类型。
 * 
 * <pre>
 *  web：web设计器 
 *  flash:flash设计器。 
 *  eclipse:eclipse客户端设计出来的。
 *  
 * 描述：设计器类型。
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-2-8-上午11:51:07
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public enum DesignerType {
	/**web设计器*/
	WEB("web","web设计器"),
	/**eclipse客户端*/
	ECLIPSE("eclipse","eclipse客户端"),
	/**flash设计器*/
	FLASH("flash","flash设计器");

	// 键
	private String key = "";
	// 值
	private String value = "";

	// 构造方法
	private DesignerType(String key, String value) {
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
	public static DesignerType fromKey(String key) {
		for (DesignerType c : DesignerType.values()) {
			if (c.getKey().equalsIgnoreCase(key))
				return c;
		}
		throw new IllegalArgumentException(key);
	}

}
