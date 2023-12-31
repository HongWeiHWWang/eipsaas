package com.hotent.bpm.plugin.usercalc.hrScript.def;

import com.hotent.bpm.plugin.core.plugindef.AbstractUserCalcPluginDef;

/**
 * 流程脚本插件。
 * <pre> 
 * 构建组：x5-bpmx-plugin
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-6-16-下午2:01:05
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class HrScriptPluginDef extends AbstractUserCalcPluginDef{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5455807382612179694L;
	private String script="";
	private String description="";
	private String scriptId = "";
	private String params = "";
	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getScriptId() {
		return scriptId;
	}

	public void setScriptId(String scriptId) {
		this.scriptId = scriptId;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

}
