package com.hotent.bpm.api.model.process.def;

import com.hotent.bpm.api.constant.ScriptType;

/**
 * 事件脚本。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-7-17-上午9:32:14
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class EventScript {
	
	private ScriptType scriptType;
	
	private String content="";
	
	public EventScript() {
	}

	public EventScript(ScriptType scriptType, String content) {
		this.scriptType = scriptType;
		this.content = content;
	}

	public ScriptType getScriptType() {
		return scriptType;
	}

	public void setScriptType(ScriptType scriptType) {
		this.scriptType = scriptType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	

}
