package com.hotent.bpm.persistence.dao;
import java.util.List;
import java.util.Map;

import com.hotent.bpm.api.constant.OpinionStatus;
import com.hotent.bpm.defxml.entity.activiti.In;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.bpm.persistence.model.DefaultBpmCheckOpinion;



public interface BpmCheckOpinionDao extends BaseMapper<DefaultBpmCheckOpinion> {

    /**
     * 取回委托流程
     * @param params
     */
    void retrieveBpmTask(Map<String,Object> params);

	/**
	 * 根据任务ID查询审批意见（只有一条记录）
	 * @param taskId
	 * @return  BpmCheckOpinion
	 */
	DefaultBpmCheckOpinion getByTaskId(@Param("taskId") String taskId);

    /**
     * 根据任务ID和任务key查询审批意见（只有一条记录）
     * @param taskId
     * @return  BpmCheckOpinion
     */
    List<DefaultBpmCheckOpinion> getTaskKeyByTaskId(@Param("taskId") String taskId);

    /**
     * 根据任务节点ID和流程实例ID获取审批历史数据（只有一条记录）
     * @param taskId
     * @return  BpmCheckOpinion
     */
    List<DefaultBpmCheckOpinion> getTaskKeyByNodeId(@Param("nodeId") String nodeId,@Param("instId") String instId);

    /**
     * 根据实例ID，审批状态，带执行人查询审批历史信息（只对应一条）
     * @param
     * @return
     * BpmCheckOpinion
     */
    DefaultBpmCheckOpinion getByTeam(@Param("instId") String instId,@Param("noticeId") String noticeId,@Param("opinionStatus") String opinionStatus,@Param("commuUser") String commuUser);
	
	
	 void archiveHistory(@Param("procInstId") String procInstId);
	
	 /**
	  * 根据实例ID查询意见列表。
	  * @param instIdList
	  * @return 
	  * List&lt;DefaultBpmCheckOpinion>
	  */
	 List<DefaultBpmCheckOpinion> getByInstIds(@Param("list") List<String> instIdList);
	
	 
	 /**
	  * 根据父实例ID查询子实例ID列表。
	  * @param supInstId
	  * @return  List&lt;DefaultBpmCheckOpinion>
	  */
	 List<String> getBySupInstId(@Param("procInstId") String supInstId);
	 
	 /**
	  * 查询父实例ID。
	  * @param instId
	  * @return DefaultBpmCheckOpinion
	  */
	 String getSupInstByInstId(@Param("procInstId") String instId);
	 
	 
	 /**
	  * 根据流程实例列表删除意见数据。
	  * @param instList 
	  * void
	  */
	 void delByInstList(@Param("list") List<String> instList);

	 /**
	  * 根据流程实例，节点ID 获取该节点审批意见
	  * @param instId
	  * @param nodeId
	  * @return
	  */
	List<DefaultBpmCheckOpinion> getByInstNodeId(@Param("instId") String instId,@Param("nodeId") String nodeId);
	
	/**
	 *  更新待审批节点状态。
	 * @param taskId		任务ID
	 * @param status		状态
	 */
	void updStatusByWait(@Param("taskId") String taskId,@Param("status") String status);
	
	
	/**
	 * 获取未审批的意见列表。
	 * @param list
	 * void
	 */
	List<DefaultBpmCheckOpinion> getByInstIdsAndWait(@Param("list") List<String> list);


	DefaultBpmCheckOpinion getByTaskIdAction(@Param("taskId") String taskId,@Param("taskAction") String taskAction);

								 
	List<DefaultBpmCheckOpinion> getByInstNodeIdAgree(@Param("instId") String instId,@Param("nodeId") String nodeId);
	
	void updateOpinion(DefaultBpmCheckOpinion defaultBpmCheckOpinion);

	
	List<String> getBytaskKeys(List<String> taskkeys,String taskId,String instId);

    DefaultBpmCheckOpinion getBpmOpinion(@Param("instId") String instId,@Param("nodeId") String nodeId,@Param("dbType") String dbType);

    /**
     * 根据任务ID修改任务为已阅
     * @param id
     */
    void checkOpinionIsRead(@Param("id") String id);
    
    /**
     * 根据父任务id获取所有撤回的子任务审批记录
     * @param parentTaskId
     * @return
     */
    List<DefaultBpmCheckOpinion> getByRevokeParentTaskId(@Param("parentTaskId") String parentTaskId);

	/**
	 * 更新审批人
	 * @param param
	 */
	void updateQualfieds(DefaultBpmCheckOpinion option);
	

    /**
     * 根据父任务id和指定状态查询审批记录
     * <pre>
     * 状态条件可以为null，为null时只通过父任务id查询数据
     * </pre>
     * @param parentTaskId
     * @param status
     * @return
     */
    List<DefaultBpmCheckOpinion> getByParentTaskIdAndStatus(@Param("parentTaskId") String parentTaskId, @Param("status") String status);
    
    /**
     * 获取实例指定节点的某个审批状态的数据
     * @param instId
     * @param revokeNodeId
     * @param key
     * @return
     */
	List<DefaultBpmCheckOpinion> getByInstNodeIdStatus(@Param("instId") String instId,@Param("nodeId") String nodeId,@Param("status") String status);
}
