package com.hotent.bpm.api.constant;


/**
 * 流程干预动作类型表
 * <pre>
 * 作者：wanghb
 * 日期:2019-41-3-下午5:54:18
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 *
 */
public enum InterPoseType {

	DEL_PATH("del_path","删除节点"),
	
	ADD_PATH("add_path","加签"),

	MODIFY_PATH("modify_path","修改节点执行人"),
	
	DEL_OPINION("del_opinion","删除审批意见"),
	
	MODIFY_OPINION("modify_opinion","修改意见"),

	MODIFY_DATA("modify_data","修改表单数据"),

	INTERPOSE_AGREE("agree","通过"),
	
	INTERPOSE_JEJECT("jeject","驳回"),
	
	INTERPOSE_TRANS("trans","转交"),
	
	INTERPOSE_END("end","作废实例"),
	
	INTERPOSE_DEL_INST("del_inst","删除实例"),
	
	INTERPOSE_COMMU("commu","沟通"),
	
	/**
	 * 流程移交
	 */
	FLOW_TURNOVER("flow_turnover","流程移交");
	// 键
	private String key = "";
	// 值
	private String value = "";

	// 构造方法
	private InterPoseType(String key, String value) {
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
	public static InterPoseType fromKey(String key) {
	for (InterPoseType c : InterPoseType.values()) {
			if (c.getKey().equalsIgnoreCase(key))
				return c;
		}
		throw new IllegalArgumentException(key);
	}
}
