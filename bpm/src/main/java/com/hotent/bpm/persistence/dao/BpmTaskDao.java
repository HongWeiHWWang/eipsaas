package com.hotent.bpm.persistence.dao;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hotent.base.query.PageList;
import com.hotent.bpm.persistence.model.DefaultBpmTask;
import com.hotent.uc.api.model.IGroup;


public interface BpmTaskDao  extends BaseMapper<DefaultBpmTask>{

	/**
	 * 根据bpmn的任务ID获取任务。
	 * @param relateTaskId
	 * @return 
	 * BpmTask
	 */
	DefaultBpmTask getByRelateTaskId(@Param("taskId") String relateTaskId);

	/**
	 * 删除关联任务ID。 
	 * @param taskId 
	 * void
	 */
	void removeByTaskId(@Param("taskId") String taskId);

	/**
	 * 查询我的待办，并且按条件进行组合查询
	 * @param userId	
	 * @param groupMap
	 * @param queryFilter
	 * @return 
	 * List<DefaultBpmTask>
	 */
	IPage<DefaultBpmTask> getByUserId(IPage<DefaultBpmTask> page,@Param("map") Map<String, String> params,@Param(Constants.WRAPPER) Wrapper<DefaultBpmTask> wrapper);

    /**
     * 获取用户领导的待办，并且按条件进行组合查询
     * @param page
     * @param params
     * @param wrapper
     * @return
     */
    IPage<DefaultBpmTask> getLeaderByUserId(IPage<DefaultBpmTask> page,@Param("map") Map<String,Object> params,@Param(Constants.WRAPPER) Wrapper<DefaultBpmTask>  wrapper);

	/**
	 * 查询我的待办总条数，并且按照条件进行组合查询
	 * @param params
	 * @param wrapper
	 * @return
	 */
	Long getCountByUserIdWithWhere(@Param("map") Map<String, String> params,@Param(Constants.WRAPPER) Wrapper<DefaultBpmTask> wrapper);
	
	/**
	 * 获取用户各分类下的待办数量
	 * @param params
	 * @return
	 */
	List<Map<String,Object>> getCountByUserId(@Param("map") Map<String, String> params);
	/**
	 * 获取用户的待办数量
	 * @param map
	 * @return
	 */
	Long getTodoCountByUserId(@Param("map") Map<String, String> map);

	/**
	 * 根据流程实例ID获取任务列表。
	 * @param instId
	 * @return 
	 * List&lt;? extends BpmTask>
	 */
	List<DefaultBpmTask> getByInstId(@Param("instId") String instId);

	List<DefaultBpmTask> getByExeIdAndNodeId(@Param("instId") String instId,@Param("nodeId") String nodeId);	 

	/**
	 * 根据流程实例Id和用户获取任务列表。
	 * @param instId
	 * @param userId
	 * @return 
	 * List&lt;? extends BpmTask>
	 */
	List<DefaultBpmTask> getByInstUser(@Param("instId") String instId,@Param("userId") String userId);

	/**
	 * 按用户ID，实例Id 用户组列表查找任务
	 * @param bpmnInstId
	 * @param userId
	 * @param groupList
	 * @return
	 */
	List<DefaultBpmTask> getByBpmInstIdUserIdGroupList(@Param("bpmnInstId") String bpmnInstId,@Param("userId") String userId,@Param("groupList") List<IGroup> groupList);

	/**
	 * 通过ID更新执行Id
	 * @param taskId bpm_task表的主键
	 * @param userId 用户ID 
	 * void
	 */
	void updateAssigneeById(@Param("id") String taskId,@Param("assigneeId") String userId);
	/**
	 * 通过任务ID更新其所属人、执行人
	 * @param taskId
	 * @param ownerId
	 * @param assigneeId 
	 * void
	 */
	void updateAssigneeOwnerId(@Param("id") String taskId,@Param("ownerId") String ownerId,@Param("assigneeId") String assigneeId);

	/**
	 * 根据流程实例列表删除任务。
	 * @param instList 
	 * void
	 */
	void delByInstList(@Param("list") List<String> instList);

	/**
	 * 根据父ID删除任务。
	 * @param parentId 
	 * void
	 */
	void delByParentId(@Param("parentId") String parentId);
	/**
	 * 根据父ID获取任务列表。
	 * @param parentId
	 * @return 
	 * List&lt;DefaultBpmTask>
	 */
	List<DefaultBpmTask> getByParentId(@Param("parentId") String parentId);

	/**
	 * 根据父ID获取任务ID集合
	 * @param parentId
	 * @return
	 */
	List<String> getIdsByParentId(@Param("parentId") String parentId);

	/**
	 * 根据用户ID获取流转任务。
	 * @param userId
	 * @param queryFilter
	 * @return 
	 * PageList&lt;DefaultBpmTask>
	 */
	//	 List<DefaultBpmTask> getTransByUserId(String userId, QueryFilter queryFilter);

	/**
	 * 取得未到期的任务
	 * @return
	 */
	List<DefaultBpmTask> getReminderTask();


	/**
	 * 根据实例ID列表获取任务列表。
	 * @param instIds
	 * @return 
	 * List&lt;DefaultBpmTask>
	 */
	List<DefaultBpmTask> getByInstList(@Param("list") List<String> instIds);

	/**
	 * 根据用户ID获取其流转出去的任务列表。
	 * @param userId
	 * @param queryFilter
	 * @return 
	 * PageList&lt;DefaultBpmTask>
	 */
	PageList<DefaultBpmTask> getMyTransTask(IPage<DefaultBpmTask> page,@Param("map")  Map<String,Object> params);
	
	/**
	 * 修改任务的紧急程度
	 * @param taskId
	 * @param priority
	 */
	void updateTaskPriority(@Param("taskId") String taskId,@Param("priority") Long priority);

	/**
	 * 根据executeId 查询关联的任务，这个主要是在会签场景中使用。
	 * @param executeId		执行ID
	 * @param nodeId		节点ID
	 * @return
	 */
	List<DefaultBpmTask>  getByExecuteAndNodeId(@Param("executeId") String executeId,@Param("nodeId") String nodeId);

	/**
	 * 修改任务执行人
	 * @param params
	 */
	void updateOwner(Map<String,Object> params);

	/**
	 * 修改任务所属人
	 * @param params
	 */
	void updateAssignee(Map<String,Object> params);

	/**
	 * 取回委托流程
	 * @param params
	 */
	void retrieveBpmTask(Map<String,Object> params);

	IPage<DefaultBpmTask> customQuery(IPage<DefaultBpmTask> convert2iPage,@Param(Constants.WRAPPER) Wrapper<DefaultBpmTask> convert2Wrapper);
	
	List<DefaultBpmTask> getTaskByTenantId(@Param("tenantId")String tenantId);

	List<Map<String, Object>> getLeaderCountByUserId(Map<String, Object> groupMap);

}


