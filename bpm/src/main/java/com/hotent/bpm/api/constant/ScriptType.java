

package com.hotent.bpm.api.constant;


/**
 * 脚本类型
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-7-16-下午11:39:48
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public enum ScriptType {

    START("start","开始脚本"),
    END("end","结束脚本"),
    CREATE("create","创建脚本"),
    COMPLETE("complete","结束脚本");
    
	// 键
	private String key = "";
	// 值
	private String value = "";

	// 构造方法
	private ScriptType(String key, String value) {
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
	public static ScriptType fromKey(String key) {
	for (ScriptType c : ScriptType.values()) {
			if (c.getKey().equalsIgnoreCase(key))
				return c;
		}
		throw new IllegalArgumentException(key);
	}

}
