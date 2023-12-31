package com.hotent.system.model.msg;

/**
 * 微信模板参数
 * @author mikel
 */
public class WXTemplateParam {

	public WXTemplateParam(String text) {
		this.value = text;
	}

    public WXTemplateParam(String text,String color) {
        this.value = text;
        this.color = color;
    }
	
	private String value;
	private String color;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
}
