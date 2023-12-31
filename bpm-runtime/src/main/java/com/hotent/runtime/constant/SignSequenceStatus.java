package com.hotent.runtime.constant;
public enum SignSequenceStatus {

	/**
	 * 审批中
	 */
	INAPPROVAL("inApproval","审批中"),
	HALF("half","二次顺签"),
	COMPLETE("complete","完成"),
	WAITINGFORGENERATIONSIGNATURETASK("waitingForGenerationSignatureTask","等待生成签署任务");

	// 键
	private String key = "";
	// 值
	private String value = "";

	// 构造方法
	private SignSequenceStatus(String key, String value) {
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
	
}
