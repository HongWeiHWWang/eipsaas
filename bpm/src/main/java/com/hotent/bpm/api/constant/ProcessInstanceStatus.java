package com.hotent.bpm.api.constant;
/**
 * 流程实例的状态。
 * 
 * <pre>
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-4-1-下午5:54:18
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public enum ProcessInstanceStatus {
	/**草稿*/
	STATUS_DRAFT("draft", "草稿"),
	/**运行中*/
	STATUS_RUNNING("running", "运行中"),
	/**结束*/
	STATUS_END("end", "结束"),
	/**人工结束*/
	STATUS_MANUAL_END("manualend", "人工结束"),
	
	STATUS_BACK_TOSTART("backToStart", "驳回到发起人"),
	
	STATUS_BACK("back", "驳回"),
	
	STATUS_REVOKE("revoke", "撤销"),
	
	STATUS_REVOKE_TOSTART("revokeToStart", "撤销");

	// 键
	private String key = "";
	// 值
	private String value = "";

	// 构造方法
	private ProcessInstanceStatus(String key, String value) {
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
	public static ProcessInstanceStatus fromKey(String key) {
		for (ProcessInstanceStatus c : ProcessInstanceStatus.values()) {
			if (c.getKey().equalsIgnoreCase(key))
				return c;
		}
		throw new IllegalArgumentException(key);
	}
	
	public static void main(String[] args) {
		System.out.println(ProcessInstanceStatus.STATUS_DRAFT.getKey().equals("draft"));
	}

}
