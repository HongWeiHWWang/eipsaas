package com.hotent.bpm.api.model.process.def;



/**
 * Restful接口调用插件定义
 * @author heyifan
 */
public class Restful implements java.io.Serializable{
	
	private static final long serialVersionUID = 2734933510879917264L;
	private String url = "";			/*接口地址*/
	private String desc = "";			/*接口描述*/
	private String header = "";			/*接口头部*/
	private int invokeMode = 1;			/*接口调用模式:0:同步，1:异步*/
	private String callTime = "";		/*触发时机：startEvent：流程启动时，endEvent：流程结束时，taskCreate：任务创建时，taskComplete：任务结束时*/
	private String callNodes = "";      /*触发节点：只针对全局restful事件中选择了节点事件的事件*/
	private String params = "";			/*接口参数*/
	private String outPutScript = "";	/*接口返回值处理脚本*/
	private String parentDefKey="";     /*父定义KEY*/
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public int getInvokeMode() {
		return invokeMode;
	}
	public void setInvokeMode(int invokeMode) {
		this.invokeMode = invokeMode;
	}
	public String getCallTime() {
		return callTime;
	}
	public void setCallTime(String callTime) {
		this.callTime = callTime;
	}
	
	public String getCallNodes() {
		return callNodes;
	}
	public void setCallNodes(String callNodes) {
		this.callNodes = callNodes;
	}
	public String getParentDefKey() {
		return parentDefKey;
	}
	public void setParentDefKey(String parentDefKey) {
		this.parentDefKey = parentDefKey;
	}
	public String getParams() {
		return params;
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
