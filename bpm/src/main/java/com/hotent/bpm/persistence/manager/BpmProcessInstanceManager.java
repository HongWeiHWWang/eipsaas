package com.hotent.bpm.persistence.manager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hotent.base.manager.BaseManager;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.bpm.api.cmd.ProcessInstCmd;
import com.hotent.bpm.api.model.process.def.BpmProcessDef;
import com.hotent.bpm.api.model.process.def.BpmProcessDefExt;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;
import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;
import com.hotent.bpm.persistence.model.ResultMessage;
import com.hotent.uc.api.model.IGroup;

public interface BpmProcessInstanceManager extends BaseManager<DefaultBpmProcessInstance>
{
    /**
     * 流程实例管理点击删除按钮
     * @param instId
     */
    void removeBpm(String instId);

    /**
	 * 构建流程定义标题
	 * 
	 * @param bpmDefinition
	 * @param processInstCmd
	 * @return String
	 * @throws Exception 
	 */
	String getSubject(BpmProcessDef<BpmProcessDefExt> bpmDefinition, ProcessInstCmd processInstCmd, DefaultBpmProcessInstance defaultBpmProcessInstance) throws Exception;

	/**
	 * 根据BPMN流程实例获取流程实例数据。
	 * 
	 * @param bpmnInstId
	 * @return DefaultBpmProcessInstance
	 */
	DefaultBpmProcessInstance getByBpmnInstId(String bpmnInstId);

	DefaultBpmProcessInstance getBpmProcessInstanceHistory(String procInstId);

	DefaultBpmProcessInstance getBpmProcessInstanceHistoryByBpmnInstId(String bpmnInstId);

	/**
	 * 根据用户ID获取流程运行实例。
	 * @param userId
	 * @return
	 */
	IPage<DefaultBpmProcessInstance> getByUserId(String userId);

	/**
	 * 根据用户ID获取分页数据。
	 * @param userId
	 * @param page
	 * @return
	 */
	IPage<DefaultBpmProcessInstance> getByUserId(String userId, PageBean pageBean);

	IPage<DefaultBpmProcessInstance> getByUserId(String userId, QueryFilter queryFiler);

	void updateStatusByInstanceId(String processInstanceId, String status);

	void updateStatusByBpmnInstanceId(String processInstanceId, String status);

	/**
	 * 按用户Id及组列表查找实例
	 * 
	 * @param userId
	 * @param groupList
	 * @return
	 */
	IPage<DefaultBpmProcessInstance> getByUserIdGroupList(String userId, List<IGroup> groupList);

	/**
	 * 按用户Id及组列表查找实例并且分页
	 * 
	 * @param userId
	 * @param groupList
	 * @param page
	 * @return
	 */
	IPage<DefaultBpmProcessInstance> getByUserIdGroupList(String userId, List<IGroup> groupList, PageBean pageBean);

	/**
	 * 按用户Id及组列表查找实例并且分页
	 * 
	 * @param userId
	 * @param groupList
	 * @param page
	 * @return
	 */
	IPage<DefaultBpmProcessInstance> getByUserIdGroupList(String userId, List<IGroup> groupList, QueryFilter queryFilter);

	/**
	 * 按人员查找其参与的流程实例
	 * 
	 * @param usreId
	 * @return List&lt;DefaultBpmProcessInstance>
	 */
	IPage<DefaultBpmProcessInstance> getByAttendUserId(String usreId);

	/**
	 * 按人员查找其参与的流程实例并分页返回结果
	 * 
	 * @param usreId
	 * @param page
	 * @return List&lt;DefaultBpmProcessInstance>
	 */
	PageList<DefaultBpmProcessInstance> getByAttendUserId(String usreId, PageBean pageBean);

	/**
	 * 根据人员查询并返回查询结果。
	 * 
	 * @param usreId
	 * @param queryFilter
	 * @return List&lt;DefaultBpmProcessInstance>
	 */
	IPage<DefaultBpmProcessInstance> getByAttendUserId(String usreId, QueryFilter queryFilter);

	/**
	 * 根据流程定义获取流程实例列表。
	 * 
	 * @param bpmnDefId
	 * @return List&lt;DefaultBpmProcessInstance>
	 */
	List<DefaultBpmProcessInstance> getListByBpmnDefKey(String defKey);
	
	

	/**
	 * 获取我的请求。
	 * 
	 * @param userId
	 * @param queryFilter
	 * @return List&lt;DefaultBpmProcessInstance>
	 */
	IPage<DefaultBpmProcessInstance> getMyRequestByUserId(String userId, QueryFilter queryFilter);

	/**
	 * 获取我的请求在各分类下的数量
	 * @param userId
	 * @return
	 */
	List<Map<String,Object>> getMyRequestCount(String userId);
	/**
	 * 获取我的请求的数量
	 * @param userId
	 * @return
	 */
	Long getMyRequestCountByUserId(String userId);

	/**
	 * 获取我的办结。
	 * 
	 * @param userId
	 * @param queryFilter
	 * @return List&lt;DefaultBpmProcessInstance>
	 */
	IPage<DefaultBpmProcessInstance> getMyCompletedByUserId(String userId, QueryFilter queryFilter);

	/**
	 * 获取我发起的草稿。
	 * 
	 * @param userId
	 * @param queryFilter
	 * @return List&lt;DefaultBpmProcessInstance>
	 */
	IPage<DefaultBpmProcessInstance> getDraftsByUserId(String userId, QueryFilter queryFilter);

	/**
	 * 获取已办事宜。
	 * 
	 * @param userId
	 * @param queryFilter
	 * @return List&lt;DefaultBpmProcessInstance>
	 */
	IPage<Map<String, Object>> getHandledByUserId(String userId, QueryFilter queryFilter);
	
	/**
	 * 获取已办事宜。
	 * 
	 * @param userId
	 * @param queryFilter
	 * @return List&lt;DefaultBpmProcessInstance>
	 */
	PageList<Map<String, Object>> getDoneInstList(String userId, QueryFilter queryFilter);
	
	

	/**
	 * 取完成待办各分类下数量
	 * @param userId
	 * @param queryFilter
	 * @return
	 */
	List<Map<String, Object>> getDoneInstCount(String userId, QueryFilter queryFilter);
	
	/**
	 * 获取用户完成待办的数量
	 * @param userId
	 * @return
	 */
	Long getDoneInstCount(String userId);
	/**
	 * 获取办结事宜。
	 * 
	 * @param userId
	 * @param queryFilter
	 * @return List&lt;DefaultBpmProcessInstance>
	 */
	IPage<DefaultBpmProcessInstance> getCompletedByUserId(String userId, QueryFilter queryFilter);

	/**
	 * 更新根据流程定义更新流程实例状态。
	 * 
	 * @param defId
	 * @param isForbidden
	 *            void
	 */
	void updForbiddenByDefKey(String defKey, Integer isForbidden);

	/**
	 * 根据流程实例ID更新流程实例是否禁止。
	 * 
	 * @param instId
	 * @param isForbidden
	 *            void
	 */
	void updForbiddenByInstId(String instId, Integer isForbidden);

	/**
	 * 根据流程定义key删除测试用例。
	 * 
	 * <pre>
	 * 1.根据流程定义KEY找到所有的状态为测试的实例ID.
	 * 2.根据实例ID删除相关数据。
	 * </pre>
	 * 
	 * @param defKey
	 *            void
	 */
    void removeTestInstByDefKey(String defKey,Boolean isBpm);

	/**
	 * 根据父ID获取所有的实例子列表。
	 * 
	 * @param parentId
	 *            父ID。
	 * @param includeSelf
	 *            是否包括当前实例数据。
	 * @return List&lt;String>
	 */
	List<DefaultBpmProcessInstance> getByParentId(String parentId, boolean includeSelf);

	/**
	 * 流程发起人撤销流程实例。
	 * 
	 * <pre>
	 * 	1.根据流程实例ID查找所有的子实例。
	 * 	2.查找相关的任务数据和Execution数据。
	 *  3.保留主Execution。
	 * 	4.创建新任务指向主流程实例。
	 * 
	 * </pre>
	 * 
	 * @param instanceId
	 * @return ResultMessage
	 * @throws Exception 
	 */
	ResultMessage revokeInstance(String instanceId, String informType, String cause) throws Exception;

	/**
	 * 撤回任务
	 * 
	 * <pre>
	 * 	1.根据流程实例ID查找所有的子实例。
	 * 	2.查找相关的任务数据和Execution数据。
	 *  3.保留主Execution。
	 * 	4.创建新任务指向主流程实例。
	 * 
	 * </pre>
	 * 
	 * @param instanceId
	 * @return ResultMessage
	 * @throws Exception 
	 */
	ResultMessage revokeTask(String instId, String informType, String cause) throws Exception;

	/**
	 * 判断根据流程实是否例撤销到发起人。
	 * 
	 * @param instanceId
	 * @return boolean
	 * @throws Exception 
	 */
	ResultMessage canRevokeToStart(String instanceId) throws Exception;

	/**
	 * 是否可以追回。
	 * 
	 * <pre>
	 * 1.根据
	 * </pre>
	 * 
	 * @param instanceId
	 * @return ResultMessage
	 */
	ResultMessage canRevoke(String instanceId, String nodeId);

	/**
	 * 按用户的授权内容去查询列表
	 * 
	 * @param queryFilter
	 * @return
	 * @throws IOException 
	 */
	List<DefaultBpmProcessInstance> queryList(QueryFilter queryFilter) throws IOException;

	/**
	 * 根据流程实例ID查询顶级的流程实例。
	 * <pre>
	 *  根据父实例向上查找，只到找到父实例为0的实例为止。
	 * </pre>
	 * @param instance
	 * @return BpmProcessInstance
	 */
	BpmProcessInstance getTopBpmProcessInstance(String instanceId);

	/**
	 * 根据流程实例查询顶级的流程实例。
	 * 
	 * @param instance
	 * @return BpmProcessInstance
	 */
	BpmProcessInstance getTopBpmProcessInstance(BpmProcessInstance instance);

	/**
	 * 根据流程定义ID获取实例列表。
	 * 
	 * @param defId
	 * @return List&lt;DefaultBpmProcessInstance>
	 */
	List<DefaultBpmProcessInstance> getListByDefId(String defId);
 
	/**
	 * 根据父流程实例ID和节点定义ID查子流程实例ＩＤ
	 * @Title: getBpmnByParentIdAndSuperNodeId 
	 * @Description: TODO
	 * @param parentInstId
	 * @param superNodeId
	 * @return
	 * @return: BpmProcessInstance
	 */
	List<BpmProcessInstance> getBpmProcessByParentIdAndSuperNodeId(String parentInstId,String superNodeId);
	
	List<BpmProcessInstance> getHiBpmProcessByParentIdAndSuperNodeId(String parentInstId,String superNodeId);
	
 
	/**
	 * 根据业务主键获取流程实例数据。
	 * @param businessKey
	 * @return
	 */
	DefaultBpmProcessInstance getByBusinessKey(String businessKey) ;
    
    
	/**
	 * 获取已参加会议
	 * @param queryFilter
	 * @return
	 */
	PageList<DefaultBpmProcessInstance> getMyHandledMeeting(QueryFilter queryFilter);

    /**
     * 查询实体对象
     * @param queryFilter	通用查询对象
     * @return				分页结果
     */
    public PageList<DefaultBpmProcessInstance> queryByuserId(QueryFilter queryFilter) ;
    
    /**
     * 根据流程实例ID获取流程实例信息
     * @param auditor
     * @param queryFilter
     * @return
     */
    IPage<DefaultBpmProcessInstance> getById(String auditor,QueryFilter queryFilter);

    /**
     * 根据流程实例ID恢复实例数据
     * @param id
     */
    void restore(String id);

    /**
     * 根据流程实例ID获取任务ID
     * @param instId
     * @return
     */
    List<String> getBpmTaskIdByInstId(String instId);

    /**
     * 根据流程实例ID删除知会待办记录数据
     * @param instId
     */
    void deleteNotice(String instId);

    /**
     * 根据流程实例ID查询最新的一条审批记录任务ID
     * @param instId
     * @return
     */
    List<String> getNodeIdByInstId(String instId);

    /**
     * 根据实例id物理删除流程运行数据
     * @param processInstId
     */
	void physicsRemove(String processInstId);

    /**
	 * 获取已办事宜。
	 * 
	 * @param userId
	 * @param queryFilter
	 * @return List&lt;DefaultBpmProcessInstance>
	 */
	List<Map<String,Object>> getFlowFieldList(QueryFilter queryFilter);


    /**
     * 获取通知任务执行人。
     *
     * <pre>
     * 获取任务的执行人，获取任务的候选人。
     * </pre>
     *
     * @param includeIdList
     *            流程实例ID列表。
     * @return List&lt;User>
     * @throws IOException 
     * @throws Exception 
     */
    Object getNotifyUsers(List<String> includeIdList) ;

	Object getNodeApprovalUsers(List<String> includeIdList) throws Exception;
	
	List<DefaultBpmProcessInstance> getListByRightMap(Map<String, String> userRightMap);

}
