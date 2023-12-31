package com.hotent.bpm.plugin.execution.message.context;

import java.util.Map;

/**
 * 获取外部数据接口，这里用于消息插件中获取外部数据时使用。
 * <pre> 
 * 构建组：x5-bpmx-plugin
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-5-10-下午4:09:54
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface IExternalData {
	
	/**
	 * 获取外部数据。 
	 * @param bpmDefId			流程定义ID
	 * @param bpmnInstId		BPMN流程实例ID
	 * @param instId			流程实例ID
	 * @param nodeId			当前节点ID
	 * @param executionId		BPMN EXECUTIONID		
	 * @return  Map&lt;String,Object>
	 */
	Map<String,Object> getData(String bpmDefId,String bpmnInstId,
			String instId,String nodeId,String executionId);

}
