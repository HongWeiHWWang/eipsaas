package com.hotent.bpm.api.service;

import java.util.Map;

/**
 * 流程实例的状态接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年7月10日
 */
public interface FlowStatusService {
	/**
	 * 根据流程实例ID获取流程实例的节点状态及其颜色
	 * @param bpmnInstId
	 * @return
	 */
	public Map<String, String> getProcessInstanceStatus(String bpmnInstId);
}
