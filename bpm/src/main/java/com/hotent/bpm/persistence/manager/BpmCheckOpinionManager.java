package com.hotent.bpm.persistence.manager;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.manager.BaseManager;
import com.hotent.base.model.CommonResult;
import com.hotent.bpm.api.constant.OpinionStatus;
import com.hotent.bpm.persistence.model.DefaultBpmCheckOpinion;

public interface BpmCheckOpinionManager extends BaseManager<DefaultBpmCheckOpinion>{
	/**
	 * 根据任务ID，查询对应的审批意见（只对应一条）
	 * @param taskId
	 * @return 
	 * BpmCheckOpinion
	 */
	 DefaultBpmCheckOpinion getByTaskId(String taskId);
	 
	 /**
	  * 通过父任务id查询审批记录
	  * @param parentId
	  * @return
	  */
	 List<DefaultBpmCheckOpinion> getByParentId(String parentId);

    /**
     * 根据任务ID和任务key查询审批意见（只有一条记录）
     * @param taskId
     * @return  BpmCheckOpinion
     */
    DefaultBpmCheckOpinion getTaskKeyByTaskId(String taskId);

    /**
     * 根据任务节点ID和流程实例ID获取审批历史数据（只有一条记录）
     * @param nodeId
     * @return  BpmCheckOpinion
     */
    DefaultBpmCheckOpinion getTaskKeyByNodeId(String nodeId,String instId);

    /**
     * 根据实例ID，审批状态，带执行人查询审批历史信息（只对应一条）
     * @param
     * @return
     * BpmCheckOpinion
     */
    DefaultBpmCheckOpinion getByTeam(String instId,String noticeId,OpinionStatus opinionStatus, String commuUser);


    /**
	 * 归档意见历史。
	 * @param instId 
	 * void
	 */
	void archiveHistory(String instId);
	
	
	/**
	 * 根据流程实例ID获取流程意见。
	 * @param instId
	 * @return 
	 * List&lt;DefaultBpmCheckOpinion>
	 */
	List<DefaultBpmCheckOpinion> getByInstId(String instId);
	
	/**
	 * 根据流程实例Id获取表单的意见数据,用于在表单展示意见。
	 * @param instId
	 * @return List&lt;DefaultBpmCheckOpinion>
	 */
	List<DefaultBpmCheckOpinion> getFormOpinionByInstId(String instId);
	
	/**
	 * 根据流程实例取得关联的流程实例ID列表。
	 * @param instId	流程实例ID
	 * @return  List&lt;String>
	 */
	List<String> getListByInstId(String instId);
	
	/**
	  * 根据流程实例列表删除意见数据。
	  * @param instList 
	  * void
	  */
	 void delByInstList(List<String> instList);
	 
	 /**
	  * 向上查询得到顶级的流程实例。
	  * @param instId 流程实例ID
	  * @return  String
	  */
	 String getTopInstId(String instId);
	
	 /**
	 * 根据流程实例，节点 获取 流程意见
	 * @param instId
	 * @param nodeId
	 * @return
	 */
	 List<DefaultBpmCheckOpinion> getByInstNodeId(String instId,String nodeId);
	 
	 
	 /**
	  * 更新待审批节点状态。
	  * @param taskId	任务ID	
	  * @param status	状态
	  */
	void updStatusByWait(String taskId,String status);
	
	/**
	 * 获取未审批的意见列表。
	 * @param list
	 * void
	 */
	List<DefaultBpmCheckOpinion> getByInstIdsAndWait(List<String> list);

	/**
	 * 通过TaskId 和action类型获取意见
	 * @param taskAction 
	 * */
	DefaultBpmCheckOpinion getByTaskIdStatus(String taskId, String taskAction);


	CommonResult<String> updateFlowOpinions(ObjectNode opinions);


	CommonResult<String> delFlowOpinions(String id,  String opinion);

    /**
     * 根据流程实例ID,任务节点获取审批记录
     * @param instId
     * @param nodeId
     * @param dbType
     * @return
     */
    DefaultBpmCheckOpinion getBpmOpinion(String instId, String nodeId,String dbType);

    /**
     * 根据任务ID修改任务为已阅
     * @param id
     */
    void checkOpinionIsRead(String id);

	void updateExtraProps(DefaultBpmCheckOpinion bpmCheckOpinion);
	
	/**
	 * 根据父任务ID，查询对应撤回的子任务审批意见
	 * @param taskId
	 * @return 
	 * BpmCheckOpinion
	 */
	 List<DefaultBpmCheckOpinion> getByRevokeParentTaskId(String parentTaskId);

    /**
     * 取回委托流程
     * @param params
     */
    void retrieveBpmTask(Map<String,Object> params);

	/**
	 * 更新审批人
	 * @param param
	 */
	void updateQualfieds(List<String> instIds,String transfer,String transfered,String transferName,String transferedName);

	List<DefaultBpmCheckOpinion> getByInstNodeIdAgree(String instId,
			String fromNodeId);

	List<DefaultBpmCheckOpinion> getByInstNodeIdStatus(String instId,
			String revokeNodeId, Object object);
	 
}
