package com.hotent.runtime.constant;

/**
 * 
 * @author liyanggui
 *
 */
public enum SignLineStatus {

	/**
	 * 审批中
	 */
	INAPPROVAL("inApproval","审批中"),
	HALF("half","二次并签"),
	RETRACTED("retracted","被撤回"),
	/**
	 * An ,Ann 撤回
	 */
	WITHDRAWALOFAPPROVAL("withdrawalOfApproval","撤回审批"),
	COMPLETE("complete","完成"), ;

	// 键
	private String key = "";
	// 值
	private String value = "";

	// 构造方法
	private SignLineStatus(String key, String value) {
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
