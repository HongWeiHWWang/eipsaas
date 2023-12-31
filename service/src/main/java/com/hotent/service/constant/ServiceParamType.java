package com.hotent.service.constant;

/**
 * 接口调用参数的类型枚举
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月3日
 */
public enum ServiceParamType {
	String("string", "字符串"),
	Number("number", "数字"),
	Date("date", "日期");
	
	private String key;
	private String label;
	
	private ServiceParamType(String key, String label) {
		this.key = key;
		this.label = label;
	}
	
	public String key() {
		return key;
	}
	
	public String label() {
		return label;
	}
}
