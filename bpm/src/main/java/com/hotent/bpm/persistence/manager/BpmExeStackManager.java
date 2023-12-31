package com.hotent.bpm.persistence.manager;

import java.util.List;
import java.util.Set;

import com.hotent.base.manager.BaseManager;
import com.hotent.bpm.api.constant.MultiInstanceType;
import com.hotent.bpm.api.model.delegate.BpmDelegateExecution;
import com.hotent.bpm.api.model.delegate.BpmDelegateTask;
import com.hotent.bpm.api.model.process.task.BpmTask;
import com.hotent.bpm.persistence.model.BpmExeStack;

public interface BpmExeStackManager extends BaseManager<BpmExeStack>{
	
	
	
	/**
	 * 获取发起的堆栈数据。
	 * @param instId
	 * @return  BpmExeStack
	 */
	BpmExeStack getInitStack(String instId);
	
	
	/**
	 * 获取堆栈数据。
	 * @param instId
	 * @param nodeId
	 * @param token
	 * @return BpmExeStack
	 */
	BpmExeStack getStack(String instId,String nodeId,String token);
	
	
	/**
	 * 获取上一步堆栈数据。
	 * <pre>
	 * 1.查询当前的堆栈数据。
	 * 2.查找上一个堆栈的数据。
	 * </pre>
	 * @param instId	实例ID
	 * @param nodeId	节点ID
	 * @param token 	token
	 * void
	 */
	BpmExeStack getPrevStack(String instId,String nodeId,String token);
	
	
	/**
	 * 退出堆栈。
	 * <pre>
	 * 	1.找到当前节点。
	 * 	2.如果目标节点为空，处理模式可以不管。
	 *  2.如果目标节点不为空，设置处理模式。
	 *  	1.处理模式为normal
	 *  		删除目标节点之后的堆栈数据，设置目标节点堆栈handleMode为normal.
	 *  	2.处理模式为direct
	 *  		堆栈不做修改，设置目标节点堆栈的handleMode为direct.
	 * </pre>
	 * @param instId
	 * @param currentNode
	 * @param token
	 * @param handleMode
	 * @param targetNode 
	 * void
	 */
	void popStack(String instId,String currentNode,String currentToken, String handleMode,String destinationNode,String destinationToken);
	
	
	/**
	 * 退出堆栈到开始节点。
	 * @param instId
	 * @param handleMode 
	 * void
	 */
	void popStartStack(String instId,String currrentNode, String handleMode);
	
	
	/**
	 * 加入堆栈。
	 * @param task 
	 * void
	 * @throws Exception 
	 */
	void pushStack(BpmDelegateTask task) throws Exception;
	
	/**
	 * 加入堆栈
	 * @param execution
	 * @throws Exception
	 */
	void pushStack(BpmDelegateExecution execution) throws Exception;
	
	/**
	 * 加入堆栈数据
	 * @param defId
	 * @param token
	 * @param instId
	 * @param nodeId
	 * @param instType
	 * @param bpmTask
	 * @throws Exception
	 */
	void pushStack(String defId, String token, String instId,
			String nodeId, MultiInstanceType instType, BpmTask bpmTask) throws Exception;

	/***
	 * 获取某节点之前的堆栈信息列表
	 * @param procInstId
	 * @param nodeId
	 * @return
	 */
	List<BpmExeStack> getPreStacksByInstIdNodeId(String procInstId, String nodeId);
	
	/**
	 * 通过来源taskId获取目标taskId
	 * @param fromTaskId
	 * @return
	 */
	String getToTaskIdByFromTaskId(String fromTaskId);
	
	/**
	 * 获取任务的前置节点
	 * @param id
	 * @return
	 */
	String getCurrentTaskFromNodeId(String id);


	void removeActRuExeCutionByPath(String instId, String targetNodePath, String notIncludeExecuteIds);


	void removeBpmTaskCandidateByPath(String instId, Set<String> nodeIds);


	void removeBpmTaskByPath(String instId, Set<String> nodeIds);


	BpmExeStack getByInstIdAndTargetNodePath(String instId, String replace);


	void removeHisByInstId(String instId);


	void removeStackRelationHisByInstId(String instId);


	void stackRelation2HisInToStackIdOrFormStackId(String instId, String targetNodePath);


	void stack2HisByPath(String instId, String targetNodePath);


	void removeBpmExeStackRelationInToStackId(String instId, String targetNodePath);


	void removeBpmExeStackRelationInFromStackId(String instId, String targetNodePath);


	void removeByPath(String instId, String targetNodePath);


	void multipleInstancesRejectAdjustOnActTask(String rejectSingleExecutionId);


	void multipleInstancesRejectAdjustOnActExecution(String actProcInstanceId);


	void multipleInstancesRejectAdjustOnBpmTask(String rejectAfterExecutionId);


	void his2StackByInstId(String instId);


	void his2StackRelationByInstId(String instId);


	void updateTagertNode(String instId, String revokeNodeId);


	List<BpmExeStack> getByBpmTaskByPath(String instId, String targetNodePath);
	
	List<BpmExeStack>  getHisByInstId(String instId);
}
