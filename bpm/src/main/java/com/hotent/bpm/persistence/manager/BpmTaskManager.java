package com.hotent.bpm.persistence.manager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.manager.BaseManager;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.bpm.api.model.delegate.BpmDelegateTask;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;
import com.hotent.bpm.persistence.model.DefaultBpmTask;
import com.hotent.bpm.persistence.model.ResultMessage;
import com.hotent.uc.api.model.IGroup;
import com.hotent.uc.api.model.IUser;

public interface BpmTaskManager  extends BaseManager<DefaultBpmTask>{

    /**
     * 取回委托流程
     * @param user,taskId
     * @return
     */
    CommonResult<String>  retrieveBpmTask(IUser user,String taskId);

	/**
	 * 根据bpmn的任务ID获取任务。
	 * @param relateTaskId
	 * @return  BpmTask
	 */
	DefaultBpmTask getByRelateTaskId(String relateTaskId);


	/**
	 * 根据任务Id获取任务。
	 * <pre>
	 * 这个任务包括任务的执行人名称和任务的候选人。
	 * </pre>
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	DefaultBpmTask getByTaskId(String taskId) throws Exception;

	/**
	 * 根据关联任务Id删除记录。
	 * @param relateTaskId
	 * void
	 */
	void delByRelateTaskId(String relateTaskId);

	/**
	 * 获取某用户的所有待办列表
	 * @param userId
	 * @return
	 * List&lt;DefaultBpmTask>
	 */
	PageList<DefaultBpmTask> getByUserId(String userId);

	/**
	 * 获取某用户的所有待办列表
	 * @param userId
	 * @return
	 * List&lt;DefaultBpmTask>
	 */
	PageList<DefaultBpmTask> getByUserId(String userId,PageBean page);


	List<DefaultBpmTask> getAllByUserId(String userId,QueryFilter<DefaultBpmTask> queryFilter);

	/**
	 * 获取某用户的所有待办列表，并按条件进行过滤
	 * @param userId
	 * @return
	 * List&lt;DefaultBpmTask>
	 */
	PageList<DefaultBpmTask> getByUserId(String userId,QueryFilter<DefaultBpmTask> queryFilter);

    /**
     * 获取某用户领导的所有待办列表，并按条件进行过滤
     * @param userId
     * @return
     * List&lt;DefaultBpmTask>
     */
    PageList<DefaultBpmTask> getLeaderByUserId(String userId,QueryFilter queryFilter);

	/**
	 * 获取指定用户的待办数量
	 * <p>待办数量按照流程分类及每个分类下的数量返回</p>
	 * @param userId
	 * @return
	 */
	List<Map<String,Object>> getCountByUserId(String userId);
	/**
	 * 获取指定用户的待办数量
	 * @return
	 */
	Long getTodoCountByUserId(String userId);

	/**
	  * 对任务分配用户。
	  * @param task
	  * void
	 * @throws Exception
	  */
	 void assignUser(BpmDelegateTask delegateTask, List<BpmIdentity> identityList) throws Exception;


	 /**
	  * 代理任务给某用户。
	  * @param taskId	任务ID
	  * @param toUser 	指定用户
	  * void
	  */
	 void delegate(String taskId,String toUser);


	 /**
	 * 根据流程实例ID获取任务列表。
	 * @param instId
	 * @return
	 * List&lt;? extends BpmTask>
	 */
	 List<DefaultBpmTask> getByInstId(String instId);

	 /**
	  * 根据流程实例ID和节点ID获得任务列表
	  * @param instId
	  * @param nodeId
	  * @return
	  * List<DefaultBpmTask>
	  */
	 List<DefaultBpmTask> getByExeIdAndNodeId(String instId,String nodeId);

	 /**
	  * 根据executeId 查询相关的任务。
	  * @param executeId
	  * @param nodeId
	  * @return
	  */
	 List<DefaultBpmTask> getByExecuteAndNodeId(String executeId,String nodeId);

	/**
	 * 根据流程实例Id和用户获取任务列表。
	 * @param instId
	 * @param userId
	 * @return
	 * List&lt;? extends BpmTask>
	 */
	 List<DefaultBpmTask> getByInstUser(String instId,String userId);


	 /**
	  * 获取任务的可用的执行人。
	  * @param taskId
	  * @return
	  * List&lt;BpmIdentity>
	  */
	 List<BpmIdentity> getIdentitysByTaskId(String taskId);

	 /**
	  * 补签用户。
	  * @param taskId		任务ID
	  * @param aryUsers		会签人员ID
	  * @return ResultMessage
	 * @throws Exception
	  */
	 ResultMessage addSignTask(String taskId,String[] aryUsers) throws Exception ;

	 /**
	  *      二次顺签 并签用户。
	  * @param taskId		任务ID
	  * @param aryUsers		会签人员ID
	  * @return ResultMessage
	 * @throws Exception
	  */
	 ResultMessage addCustomSignTask(String taskId,String[] aryUsers) throws Exception ;

	 /**
	  *  撤回 生成待办 是否需要再次创建会签数据
	  * @param taskId
	  * @param aryUsers
	  * @param isCreateSignData
	  * @return
	  * @throws Exception
	  */
	 ResultMessage addCustomSignTask(String taskId,String[] aryUsers,boolean isCreateSignData) throws Exception ;

	 /**
	  * 按用户ID，实例Id 用户组列表查找任务
	  * @param bpmnInstId
	  * @param userId
	  * @param groupList
	  * @return
	  */
	 List<DefaultBpmTask> getByBpmInstIdUserIdGroupList(String bpmnInstId,String userId,List<IGroup> groupList);
	 /**
	  * 锁定任务为某一用户
	  * @param taskId
	  * @param userId
	  * void
	  */
	 void lockTask(String taskId,String userId);
	 /**
	  * 解锁某一任务
	  * @param taskId
	  * void
	  */
	 void unLockTask(String taskId);
	 /**
	  * 设置任务的执行用户
	  * @param taskId
	  * @param assigneeId 任务的执行人Id
	  * void
	  */
	 void assignTask(String taskId,String assigneeId);


	 /**
	  * 根据实例ID删除流程。
	  * @param instList
	  * void
	  */
	 void delByInstList(List<String> instList);

	 /**
	  * 根据父ID删除任务。
	  * @param parentId
	  * void
	  */
	 void delByParentId(String parentId);


	 /**
	  * 根据任务ID获取孩子节点。
	  * @param taskId
	  * @return
	  * List&lt;DefaultBpmTask>
	  */
	 List<DefaultBpmTask> getChildsByTaskId(String taskId);





	 /**
	  * 根据任务实例创建任务。
	  * <pre>
	  * 1.创建Bpm_task记录。
	  * 2.创建ACT_RU_TASK记录。
	  * </pre>
	  * @param bpmProcessInstance
	  * void
	  */
	 void createTask(BpmProcessInstance bpmProcessInstance);
	 /**
	  * 查询有分管授权的列表
	  * @param queryFilter
	  * @return
	 * @throws IOException
	 * @throws Exception
	  */
	 List<DefaultBpmTask> queryList(QueryFilter<DefaultBpmTask> queryFilter) throws IOException, Exception;

	 /**
	  * 转换列表信息数据
	  * @param list
	  * @param isActRight 是否需要设置分管授权
	  * @param isHashRight 是否有权限
	  * @param authorizeRightMap  分管授权的map
	  * @return
	 * @throws Exception
	  */
	 List<DefaultBpmTask> convertInfo(List<DefaultBpmTask> list) throws Exception;

	 /**
	  * 取得未到期的任务
	  * @return
	  */
	 List<DefaultBpmTask> getReminderTask();


	 /**
	  * 根据任务ID终止流程。
	  * <pre>
	  * 1.根据任务ID查询到BPM_TASK记录。
	  * 2.发送通知消息，通知相关人员。
	  * 3.删除bpm_task_candidate对应记录。
	  * 4.删除BPM_TASK记录。
	  * 5.删除act_ru_identitylink记录。
	  * 6.删除act_ru_task记录。
	  * 7.删除act_ru_execution的记录.
	  * 8.标记bpm_pro_inst，bpm_pro_inst_hi为人工终止。
	  * 9.归档bpm_pro_inst。
	  * </pre>
	  * @param taskId
	  * void
	  */
	 void endProcessByTaskId(String taskId,String informType,String cause,String files ) throws Exception ;


	 /**
	  * 根据实例ID列表获取任务列表。
	  * @param instIds
	  * @return
	  * List&lt;DefaultBpmTask>
	  */
	 List<DefaultBpmTask> getByInstList(List<String> instIds);


	 /**
	  * 根据用户ID获取其流转出去的任务。
	  * @param userId			用户ID
	  * @param queryFilter
	  * @return
	  * PageList<DefaultBpmTask>
	  */
	 PageList<DefaultBpmTask> getMyTransTask(String userId,
				QueryFilter<DefaultBpmTask> queryFilter);


	 /**
	 * 如果任务执行人和当前人不是同一个人则，不能处理任务。
	 * <br/>
	 * 当任务执行人为空或者和当前人相同时返回true。
	 * <pre>
	 *  这个主要防止，一个任务由多个候选人处理一个任务的情况。
	 *  比如：
	 *  一个任务有A,B两个候选人:
	 *  1. 如果A在列表中打开此任务并锁定，这个时候B就不用在处理了。
	 *  B点击任务是判断任务执行人是否是自己，不是自己就不能处理了。
	 *
	 *  2.A和B都打开任务，A和B ，A先锁定任务，这个时候B如果在处理任务，就需要
	 *  提醒B用户，告诉B，任务不能由他处理。
	 *
	 * </pre>
	 * @param taskId
	 * @return	0:任务已经处理,1:可以锁定,2:不需要解锁 ,3:可以解锁，
	 * 			4,被其他人锁定,5:这种情况一般是管理员操作，所以不用出锁定按钮。
	 */
	 int canLockTask(String taskId);

	 int canLockTask(String taskId,String userId);
	 /**
	  * 判断某个人任务的执行人或这候选人。
	  * @param taskId
	  * @param userId
	  * @return
	  */
	 boolean canAccessTask(String taskId,String userId);


	 /**
	  * 设置任务执行人。
	  * <pre>
	  * 1.执行人为空的情况。
	  * 	清空任务的执行人，删除候选人。
	  * 2.执行人为一个的情况。
	  * 	设置任务的执行人，清空候选人。
	  * 3.执行为多个的情况。
	  * 	设置任务执行人为空，添加候选人。
	  * </pre>
	  * @param taskId
	  * @param userIds
	  */
	 void setTaskExecutors(String taskId, String[] userIds,String notifyType,String opinion) throws Exception;

	/**
	 * 设置任务的紧急程度
	 * @param id
	 * @param long1
	 */
	void updateTaskPriority(String id, Long long1);

	void unLockTask(String taskId, String userId);

	/**
	 * <pre>
	 * 并批撤回情况
	 *
	 * B 未产生待办时,  An 撤回
	 * 撤回前：
	 * A1 未处理
	 * An 已处理
	 *
	 * 撤回后
	 * A1 未处理
	 * An 被撤回
	 * An 未处理
	 *
	 * 实现逻辑：
	 * 	1.添加审批意见 An 被撤回
	 *  2.添加An补签任务
	 *  3.更新 bpm_custom_signdata  taskId  status  撤回审批中
	 * </pre>
	 *
	 *
	 * @param runningTask
	 * @param customSignTaskId
	 */
	void approvalTaskRevoke(String runningTaskId,String customSignTaskId) throws Exception;

	/**
	 * <pre>
	 * 并行审批A 撤回
	 * 撤回条件： A1 ... An 全部未处理 A允许撤回
	 * 实现逻辑：
	 * 1. A1 ... An 待办更新为 被撤回
	 * A 更新为撤回
	 * 2. 使用驳回A节点的方式实现
	 * </pre>
	 * @param instId
	 * @param runningTaskId
	 * @param customSignTaskId 已审批的A任务的id
	 * @param destNodeId
	 */

	void approvalTaskARevoke(String instId, String runningTaskId, String customSignTaskId, String destNodeId) throws Exception ;

	/**
	 * <pre>
	 * 串签 An-1 撤回An
	 * 1. An 完成审批  被撤回 finshTask(An)
	 * 2. 修改流程变量loopcounter
	 * </pre>
	 */
	void sequentialTaskRevoke(String runingTaskId,String customSignTaskId) throws Exception;

	/**
	 * 串签 A 撤回 A1..An中的一个
	 * @param instanceId
	 * @param taskId   正在运行的任务id
	 * @param targetNodeId 撤回目标节点id
	 */
	void sequentialTaskARevoke(String instanceId, String taskId, String targetNodeId)  throws Exception;


	/**
	 * <pre>
	 * 顺序签署,并批，并签
	 * B待办产生未处理时， An 撤回
	 * 1. 产生An 的任务
	 * 2.根据targetTaskId 修改  bpm_custom_signdata 被撤回审批中 并且更新taskId
	 * </pre>
	 * @param instanceId
	 * @param runningTaskId 正在运行的任务id
	 * @param targetNodeId 撤回目标节点id
	 * @param targetTaskId 撤回已办的任务id
	 */
	void taskAnRevoke(String instanceId, String runningTaskId, String targetNodeId, String targetTaskId)  throws Exception;

	/**
	 * <pre>
	 * 并签
	 * A 撤回 A1 ... An
	 * 如果都撤回 即没有未处理的任务则产生A任务
	 *
	 * 1. 更新custom_signdata数据状态未 被撤回
	 * 2. 判断是否产生A任务
	 * 3. 如果产生A任务 清除数据
	 * </pre>
	 * @param instanceId
	 * @param targetNodeId 撤回目标节点id
	 * @param currentTaskIds 撤回待办的任务id
	 */
	void parallaelARevoke(String instanceId, String targetNodeId, String currentTaskIds)  throws Exception ;

	/**
	 * <pre>
	 * 并签
	 * A11 撤回A111,A112,A113
	 * 如果撤回后  A11 下级没有待办则产生A11 待办
	 * </pre>
	 * @param currentTaskIds 需要撤回的任务id
	 * @param targetTaskId
	 */
	void parallelRevoke(String currentTaskIds, String targetTaskId)  throws Exception;

	void updateOwner(Map<String, Object> ownerMap);

	void updateAssignee(Map<String, Object> assigneeMap);

	List<ObjectNode> getTaskListByTenantId(String tenantId) throws Exception;

	List<Map<String, Object>> getLeaderCountByUserId(String userId);

}

