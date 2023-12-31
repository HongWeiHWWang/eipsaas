package com.hotent.bpm.plugin.execution.message.def;

import java.io.Serializable;

public class HtmlSetting extends PlainTextSetting implements Serializable{
	private static final long serialVersionUID = 1L;
	private String subject="";

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
}
