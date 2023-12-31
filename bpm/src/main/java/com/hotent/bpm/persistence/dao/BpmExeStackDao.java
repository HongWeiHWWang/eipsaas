package com.hotent.bpm.persistence.dao;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.bpm.persistence.model.BpmExeStack;


public interface BpmExeStackDao extends BaseMapper<BpmExeStack> {
	
	
	
	
	/**
	 * 根据流程实例Id,节点ID和token获取堆栈对象。 
	 * @param procInstId	流程ID
	 * @param nodeId	节点ID
	 * @param taskToken		令牌
	 * @return  BpmExeStack
	 */
	List<BpmExeStack> getByInstNodeToken(@Param("procInstId") String procInstId,@Param("nodeId") String nodeId,@Param("taskToken") String taskToken);
	
	
	/**
	 * 根据流程实例ID获取发起堆栈数据。
	 * @param instId
	 * @return BpmExeStack
	 */
	BpmExeStack getInitStack(String instId);
	
	
	/**
	 * 根据路径删除堆栈数据。
	 * @param instId	流程实例ID
	 * @param path 		堆栈路径
	 * void
	 */
	void removeByPath(@Param("procInstId") String instId,@Param("nodePath") String path);
	
	/**
	 * 保存一份数据到 bpm_exe_stack_his 表   用户 驳回按流程图执行   驳回撤回操作
	 * 保存之前需要在表bpm_exe_stack_his将该实例中的数据删除
	 * @param instId	流程实例ID
	 * @param path 		堆栈路径
	 * void
	 */
	void stack2HisByPath(@Param("procInstId") String instId,@Param("nodePath") String path);
	
	/**
	 * 驳回的任务进行撤回时   需要回复的堆栈数据
	 * @param instId
	 */
	void his2StackByInstId(@Param("procInstId") String instId);
	
	/**
	 * 删除bpm_exe_stack_his 历史数据
	 * @param instId
	 */
	void removeHisByInstId(@Param("procInstId") String instId);
	
	/**
	 * 根据路径删除任务候选人
	 * @param instId	流程实例ID
	 * @param path 		堆栈路径
	 * void
	 */
	void removeBpmTaskCandidateByPath(@Param("procInstId") String instId,@Param("nodeSet") Set<String> nodeIds);
	
	/**
	 * 根据路径删除任务的执行更改
	 * @param instId	流程实例ID
	 * @param path 		堆栈路径
	 * void
	 */
	void removeBpmTaskTurnByPath(@Param("procInstId") String instId,@Param("nodePath") String path);
	
	/**	
	 * 根据路径删除任务
	 * @param instId	流程实例ID
	 * @param path 		堆栈路径
	 * void
	 */
	void removeBpmTaskByPath(@Param("procInstId") String instId,@Param("nodeSet") Set<String> nodeIds);

	
	/**
	 * 根据路径删除堆栈的关系数据
	 * @param instId	流程实例ID
	 * @param path 		堆栈路径
	 * void
	 */
	void removeBpmExeStackRelationInToStackId(@Param("procInstId") String instId,@Param("nodePath") String path);
	
	/**
	 * 保存 removeBpmExeStackRelationInToStackId方法删除的数据到 bpm_exe_stack_relation_his表中
	 * @param instId
	 * @param path
	 */
	void stackRelation2HisInToStackIdOrFormStackId(@Param("procInstId") String instId,@Param("nodePath") String path);
	
	/**
	 * removeBpmExeStackRelationInToStackId 删除的数据需要从 his表中还原
	 * @param instId
	 */
	void his2StackRelationByInstId(@Param("procInstId") String instId);
	
	/**
	 * 不需要使用时 需要删除  如 在保存数据到his 前 需要删除   
	 * 在流程实例结束时间需要删除
	 * 
	 * @param instId
	 */
	void removeStackRelationHisByInstId(@Param("procInstId") String instId);
	
	/**
	 * 根据路径删除堆栈的关系数据
	 * @param instId	流程实例ID
	 * @param path 		堆栈路径
	 * void
	 */
	void removeBpmExeStackRelationInFromStackId(@Param("procInstId") String instId,@Param("nodePath") String path);
	
	/**
	 * 根据路径删除Activiti的执行运行时
	 * @param instId	流程实例ID
	 * @param path 		堆栈路径
	 * void
	 */
	void removeActRuExeCutionByPath(@Param("procInstId") String instId,@Param("nodePath") String path,@Param("notIncludeExecuteIds") String notIncludeExecuteIds);
	
	/**
	 * 根据路径删除Activiti的执行运行时的任务
	 * @param instId	流程实例ID
	 * @param path 		堆栈路径
	 * void
	 */
	void removeActRuTaskByPath(@Param("procInstId") String instId,@Param("nodePath") String path,@Param("notIncludeExecuteIds") String notIncludeExecuteIds);
	
	/**
	 * 根据路径删除Activiti的执行运行时的变量
	 * @param instId	流程实例ID
	 * @param path 		堆栈路径
	 * void
	 */
	void removeActRuVariableByPath(@Param("procInstId") String instId,@Param("nodePath") String path,@Param("notIncludeExecuteIds") String notIncludeExecuteIds);
	
 
	/**
	 * 按流程图执行多实例退回时调整Bpm任务表
	 * @param rejectAfterExecutionId
	 */
	void multipleInstancesRejectAdjustOnBpmTask(@Param("rejectAfterExecutionId") String rejectAfterExecutionId);
	
	/**
	 * 按流程图执行多实例退回时调整Act任务表
	 * @param rejectAfterExecutionId
	 */
	void multipleInstancesRejectAdjustOnActTask(@Param("rejectAfterExecutionId") String rejectAfterExecutionId);
	
	/**
	 *  按流程图执行多实例退回时调整Act执行表
	 * @param actProcInstanceId
	 */
	void multipleInstancesRejectAdjustOnActExecution(@Param("actProcInstanceId") String actProcInstanceId);
	
	/**
	 * 根据路径删除活动状态
	 * @param instId	流程实例ID
	 * @param path 		堆栈路径
	 * void
	 */
	void removebpmProStatusPath(@Param("procInstId") String instId,@Param("nodePath") String path);
	
	
	List<BpmExeStack> getByIds(String[] ids);
	
	
	/**
	 * 删除堆栈，除了初始的第一个不删除，即parentid为0的不删除
	 * @param instId
	 * @param path
	 */
	void removeExeStackExceptParentId(@Param("procInstId") String instId,@Param("parentId") String parentId);

	/**
	 *   驳回撤回   之前的驳回按直来直往的方式  需要将targetNode 设置为空
	 * @param instId
	 * @param revokeNodeId
	 */
	void updateTagertNode(@Param("procInstId")String instId, @Param("targetNode") String targetNode);
	
	/**
	 * 通过来源taskId获取目标taskId
	 * @param fromTaskId
	 * @return
	 */
	String getToTaskIdByFromTaskId(@Param("fromTaskId")String fromTaskId);

	/**
	 * 获取任务的前置节点
	 * @param taskId
	 * @return
	 */
	String getCurrentTaskFromNodeId(@Param("taskId") String taskId);
	
	
	/**
	 * 
	 * @param instId
	 * @param targetNodePath
	 * @return
	 */
	BpmExeStack getByInstIdAndTargetNodePath(@Param("instId") String instId, @Param("nodePath")String nodePath);


	List<BpmExeStack> getByBpmTaskByPath(@Param("instId") String instId, @Param("nodePath")String nodePath);


	List<BpmExeStack> getHisByInstId(@Param("instId") String instId);
}
