//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.03.13 at 05:53:25 下午 CST 
//


package com.hotent.bpm.api.constant;



/**
 * 特权处理模式。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-14-上午9:42:17
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public enum PrivilegeMode {

    ALL("all","所有"),
    DIRECT("direct","直接"),
    ONETICKET("oneticket","一票制"),    
    ALLOW_ADD_SIGN("allowAddSign","允许增加会签");
    
	// 键
	private String key = "";
	// 值
	private String value = "";

	// 构造方法
	private PrivilegeMode(String key, String value) {
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
	public static PrivilegeMode fromKey(String key) {
	for (PrivilegeMode c : PrivilegeMode.values()) {
			if (c.getKey().equalsIgnoreCase(key))
				return c;
		}
		throw new IllegalArgumentException(key);
	}

}
