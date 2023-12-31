package com.hotent.bpm.plugin.execution.webservice.def;

import com.hotent.bpm.plugin.core.plugindef.AbstractBpmExecutionPluginDef;

/**
 * 任务脚本节点定义。
 * <pre> 
 * 构建组：x5-bpmx-plugin
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-4-24-下午10:38:06
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class WebServiceNodePluginDef extends AbstractBpmExecutionPluginDef {
	private static final long serialVersionUID = 1L;
	
	private String name = "";
	private String alias = "";
	private String params = "";
	private String outPutScript = "";
	
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getParams() {
		return params;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public String getOutPutScript() {
		return outPutScript;
	}
	public void setOutPutScript(String outPutScript) {
		this.outPutScript = outPutScript;
	}
	
	
}
