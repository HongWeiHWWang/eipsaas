package com.hotent.bpm.api.constant;

/**
 * 流程节点类型
 * <pre> 
 * 描述：流程节点类型
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-2-26-下午5:03:05
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public enum NodeType {
	
	START("start","开始节点"),
	END("end","结束节点"),
	USERTASK("userTask","用户任务节点"),
	SIGNTASK("signTask","会签任务节点"),
	CUSTOMSIGNTASK("customSignTask","签署并审任务节点"),
	SUBPROCESS("subProcess","子流程"),
	CALLACTIVITY("callActivity","外部子流程"),
	EXCLUSIVEGATEWAY("exclusiveGateway","分支网关"),
	PARALLELGATEWAY("parallelGateway","同步网关"),
	INCLUSIVEGATEWAY("inclusiveGateway","条件网关"),
	SUBSTARTGATEWAY("subStartGateway","内嵌子流程开始网关"),
	SUBENDGATEWAY("subEndGateway","内嵌子流程结束网关"),
	SUBMULTISTARTGATEWAY("subMultiStartGateway","多实例内嵌子流程开始网关"),
	
	SERVICETASK("serviceTask","服务任务节点");
	
	// 键
	private String key = "";
	// 值
	private String value = "";

	// 构造方法
	private NodeType(String key, String value) {
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

	/**
	 * 通过key获取对象
	 * 
	 * @param key
	 * @return
	 */
	public static NodeType fromKey(String key) {
	for (NodeType c : NodeType.values()) {
			if (c.getKey().equalsIgnoreCase(key))
				return c;
		}
		throw new IllegalArgumentException(key);
	}

}
