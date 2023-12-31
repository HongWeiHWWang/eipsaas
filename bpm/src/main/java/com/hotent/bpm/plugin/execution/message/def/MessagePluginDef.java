package com.hotent.bpm.plugin.execution.message.def;

import com.hotent.bpm.plugin.core.plugindef.AbstractBpmExecutionPluginDef;

public class MessagePluginDef extends AbstractBpmExecutionPluginDef  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1209724089698541571L;

	/**
	 * 外部class。
	 */
	private String externalClass="";
	
	/**
	 * 简单TEXT消息设定。
	 */
	private PlainTextSetting plainTextSetting;
	
	/**
	 * 富文本消息设定。
	 */
	private HtmlSetting htmlSetting;
	

	public String getExternalClass() {
		return externalClass;
	}

	public void setExternalClass(String externalClass) {
		this.externalClass = externalClass;
	}

	public PlainTextSetting getPlainTextSetting() {
		return plainTextSetting;
	}

	public void setPlainTextSetting(PlainTextSetting plainTextSetting) {
		this.plainTextSetting = plainTextSetting;
	}

	public HtmlSetting getHtmlSetting() {
		return htmlSetting;
	}

	public void setHtmlSetting(HtmlSetting htmlSetting) {
		this.htmlSetting = htmlSetting;
	}
	
}
