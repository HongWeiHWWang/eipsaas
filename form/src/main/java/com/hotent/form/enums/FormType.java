package com.hotent.form.enums;

/**
 * 表单类型
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月13日
 */
public enum FormType {
	PC("pc"), 
	MOBILE("mobile");

	private final String value;

	FormType(String v) {
		value = v;
	}

	public String value() {
		return value;
	}

	public static FormType fromValue(String v) {
		for (FormType c : FormType.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}

}
