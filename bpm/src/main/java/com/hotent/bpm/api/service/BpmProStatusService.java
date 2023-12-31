package com.hotent.bpm.api.service;

import com.hotent.bpm.api.constant.NodeStatus;

/**
 * 节点状态服务。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-4-24-下午2:15:28
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface BpmProStatusService {
	
	/**
	 * 更新节点状态。 
	 * @param instId		流程实例ID
	 * @param bpmnDefId		流程定义ID
	 * @param nodeId		流程节点ID
	 * @param nodeName		节点名称
	 * @param nodeStatus 	节点状态。
	 * void
	 * @exception 
	 * @since  1.0.0
	 */
	void createOrUpd(String  instId,String bpmnDefId,String nodeId,String nodeName, NodeStatus nodeStatus);
}
