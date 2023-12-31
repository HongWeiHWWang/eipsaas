package com.hotent.bpm.api.constant;

/**
 * 审批动作类型。
 * 
 * <pre>
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-4-1-下午5:54:18
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public enum ActionType {
	/**审批*/
	APPROVE("approve", "审批"),
	/**驳回*/
	BACK("back", "驳回"),
	/**驳回到发起人*/
	BACK_TO_START("backToStart", "驳回到发起人"),
	/**沟通*/
	COMMU("commu", "沟通"),
	/**撤销*/
	RECOVER("recover", "撤销"),
	/**流转*/
	TRANS("trans", "流转"),
	/**其他*/
	OTHER("other","其他");

	// 键
	private String key = "";
	// 值
	private String value = "";

	// 构造方法
	private ActionType(String key, String value) {
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
	public static ActionType fromKey(String key) {
		for (ActionType c : ActionType.values()) {
			if (c.getKey().equalsIgnoreCase(key))
				return c;
		}
		throw new IllegalArgumentException(key);
	}

}
