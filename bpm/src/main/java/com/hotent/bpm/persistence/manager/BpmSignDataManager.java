package com.hotent.bpm.persistence.manager;

import java.util.List;

import com.hotent.base.manager.BaseManager;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.task.BpmTask;
import com.hotent.bpm.persistence.model.BpmSignData;

public interface BpmSignDataManager extends BaseManager<BpmSignData>{
	
	
	/**
	 * 根据实例ID获取会签结果。
	 * @param instId
	 * @param nodeId
	 * @return  List&lt;BpmSignData>
	 */
	List<BpmSignData> getVoteByExecuteNode(String executeId,String nodeId,Integer isActive);

	/**
	 * 添加会签数据。
	 * @param defId
	 * @param instId
	 * @param actInstId
	 * @param nodeId
	 * @param taskId
	 * @param qualifiedId
	 * @param qualifiedName
	 * @param index 
	 * @param signType 数据类型 
	 * void
	 */
	void addSignData(String defId,String instId,String actInstId,String executeId,String nodeId,
			String taskId,String qualifiedId,String qualifiedName,Short index,String signType);
	
	
	/**
	 * 根据流程实例ID和节点ID删除数据。
	 * @param actExecuteId
	 * @param nodeId 
	 * void
	 */
	void removeByNotActive(String actExecuteId,String nodeId);
	
	/**
	 * 更新会签为为已处理状态。
	 * @param actExecuteId
	 * @param nodeId
	 */
	void updByNotActive(String actExecuteId,String nodeId);
	
	
	/**
	 * 获取Sgin数据。
	 * @param bpmTask
	 * @param bpmIdentity
	 * @return  BpmSignData
	 */
	BpmSignData getSignData(BpmTask bpmTask,String executeId, BpmIdentity bpmIdentity);
	
	
	/**
	* 根据流程实例列表删除任务。
	* @param instList 
	* void
	*/
	void delByInstList(List<String> instList) ;
	
	
	/**
	 * 投票接口方法。
	 * @param executeId		流程运行实例ID。
	 * @param nodeId		流程节点ID
	 * @param index			投票顺序
	 * @param actionName	投票动作
	 */
	void vote(String executeId,String nodeId,Short index,String actionName);
	
	/**
	 * 根据流程实例id和用户id获取会签数据
	 * @param instancId
	 * @param userId
	 * @param taskId
	 * @return
	 */
	BpmSignData getByInstanIdAndUserId(String instancId,String userId,String taskId);
	
	/**
	 * 获取未审批的会签人员
	 * 删除未审批的
	 * 激活已投票的
	 *
	 * @param instId
	 * @param nodeId
	 * @return
	 */
	List<BpmIdentity> getByInstanIdAndNodeIdAndNo(String instId, String targetNode);

}
