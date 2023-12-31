package com.hotent.bpm.listener;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.hotent.bpm.engine.task.cmd.DefaultTaskFinishCmd;
import com.hotent.bpm.api.event.NodeNotifyEvent;
import com.hotent.bpm.api.event.NodeNotifyModel;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.exception.WorkFlowException;
import com.hotent.base.feign.PortalFeignService;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.Base64;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.base.util.time.DateFormatUtil;
import com.hotent.base.util.time.TimeUtil;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.cmd.TaskFinishCmd;
import com.hotent.bpm.api.constant.ActionType;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.constant.InterPoseType;
import com.hotent.bpm.api.constant.MultiInstanceType;
import com.hotent.bpm.api.constant.NodeStatus;
import com.hotent.bpm.api.constant.NodeType;
import com.hotent.bpm.api.constant.OpinionStatus;
import com.hotent.bpm.api.constant.ProcessInstanceStatus;
import com.hotent.bpm.api.constant.TaskType;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.event.TaskCompleteEvent;
import com.hotent.bpm.api.model.delegate.BpmDelegateTask;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.model.process.task.BpmTask;
import com.hotent.bpm.api.model.process.task.BpmTaskTurn;
import com.hotent.bpm.api.model.process.task.SkipResult;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.api.service.TaskCommuService;
import com.hotent.bpm.natapi.inst.NatProInstanceService;
import com.hotent.bpm.natapi.task.NatTaskService;
import com.hotent.bpm.persistence.manager.ActExecutionManager;
import com.hotent.bpm.persistence.manager.BpmCheckOpinionManager;
import com.hotent.bpm.persistence.manager.BpmDefinitionManager;
import com.hotent.bpm.persistence.manager.BpmExeStackExecutorManager;
import com.hotent.bpm.persistence.manager.BpmExeStackManager;
import com.hotent.bpm.persistence.manager.BpmInterposeRecoredManager;
import com.hotent.bpm.persistence.manager.BpmProStatusManager;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.persistence.manager.BpmTaskCandidateManager;
import com.hotent.bpm.persistence.manager.BpmTaskDueTimeManager;
import com.hotent.bpm.persistence.manager.BpmTaskManager;
import com.hotent.bpm.persistence.manager.BpmTaskNoticeManager;
import com.hotent.bpm.persistence.manager.BpmTaskTurnManager;
import com.hotent.bpm.persistence.manager.TaskFollowManager;
import com.hotent.bpm.persistence.model.ActExecution;
import com.hotent.bpm.persistence.model.BpmExeStack;
import com.hotent.bpm.persistence.model.BpmExeStackExecutor;
import com.hotent.bpm.persistence.model.BpmInterposeRecored;
import com.hotent.bpm.persistence.model.BpmTaskDueTime;
import com.hotent.bpm.persistence.model.BpmTaskNotice;
import com.hotent.bpm.persistence.model.DefaultBpmCheckOpinion;
import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;
import com.hotent.bpm.persistence.model.DefaultBpmTask;
import com.hotent.bpm.persistence.model.TaskFollow;
import com.hotent.bpm.persistence.util.BpmStackRelationUtil;
import com.hotent.bpm.persistence.util.BpmUtil;
import com.hotent.bpm.persistence.util.ServiceUtil;
import com.hotent.bpm.util.BpmCheckOpinionUtil;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;

@Service
public class TaskCompleteEventListener implements ApplicationListener<TaskCompleteEvent>, Ordered
{

	@Resource
	ActExecutionManager actExecutionManager;
	@Resource
	NatTaskService natTaskService;
	@Resource
	BpmTaskManager bpmTaskManager;
	@Resource
	BpmTaskCandidateManager bpmTaskCandidateManager;
	@Resource
	BpmCheckOpinionManager bpmCheckOpinionManager;
	@Resource
	BpmProStatusManager bpmProStatusManager;
	@Resource
	BpmDefinitionAccessor bpmDefinitionAccessor;
	@Resource
	BpmDefinitionManager bpmDefinitionManager;
	@Resource
	BpmTaskTurnManager bpmTaskTurnManager;
	@Resource
	TaskCommuService taskCommuService;
	@Resource
	NatProInstanceService natProInstanceService;
	@Resource
	BpmExeStackManager bpmExeStackManager;
	@Resource
	BpmProcessInstanceManager bpmProcessInstanceManager;
	@Resource
	BpmExeStackExecutorManager bpmExeStackExecutorManager;
	@Resource
	BpmTaskDueTimeManager bpmTaskDueTimeManager;
	@Resource
	PortalFeignService PortalFeignService;
	@Resource
	TaskFollowManager taskFollowManager;
	@Override
	public int getOrder()
	{
		return 1;
	}

	public void onApplicationEvent(TaskCompleteEvent event)
	{
		// ## 数据获取和准备
		// 事件对象
		BpmDelegateTask delegateTask = (BpmDelegateTask) event.getSource();
		String currentNodeId = delegateTask.getTaskDefinitionKey();
		String currentExecutionId = delegateTask.getExecutionId();
		String instId = (String) delegateTask.getVariable(BpmConstants.PROCESS_INST_ID);

		// 任务完成数据
		TaskFinishCmd cmd = (TaskFinishCmd) ContextThreadUtil.getActionCmd();
		// 退回时的目标节点
		String rejectTargetNodeId = cmd.getDestination();
		// 节点状态
		NodeStatus nodeStatus = getNodeStatus(cmd);
		String bpmnTaskId = cmd.getTaskId();
		DefaultBpmTask bpmTask = bpmTaskManager.getByRelateTaskId(bpmnTaskId);
		BpmDelegateTask task = natTaskService.getByTaskId(bpmTask.getTaskId());
		// 解决嵌入子流程时有一条先完成时会导致另一条的执行ID变化了，需要更新
		if (!task.getExecutionId().equals(bpmTask.getExecId()))
		{
			bpmTask.setExecId(task.getExecutionId());
			bpmTaskManager.update((DefaultBpmTask) bpmTask);
		}

		ActionCmd actionCmd = ContextThreadUtil.getActionCmd();
		String skipType = (String) actionCmd.getTransitVars().get(BpmConstants.BPM_SKIP_TYPE);
		// ## 操作
		//执行流程跟踪任务
		try {
			followTask(cmd,bpmTask.getNodeId());
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		// 删除候选人和任务
		delCandidateAndTask(bpmnTaskId);
		// 更新审批意见
		try {
			updateCheckOpinion(cmd, skipType);
		} catch (Exception e2) {
			throw new RuntimeException(e2.getMessage());
		}
		// 更新节点状态
		updateProcStatus(instId, delegateTask, nodeStatus, actionCmd);
		// 更新转办代理任务。
		updTaskTurnComplte(cmd.getTaskId());
		// 删除沟通任务相关数据。
		updTaskCommuComplete(bpmTask.getId());
		// 更新堆栈数据。
		updStack(bpmTask, actionCmd);
		// 更新流程实例。
		updProcessInstance(cmd);
		BpmNodeDef bpmNodeDef = null;
		// 更新审批剩余时间
		try {
			updDueTime(bpmTask);
			bpmNodeDef = bpmDefinitionAccessor.getBpmNodeDef(bpmTask.getProcDefId(), bpmTask.getNodeId());
			//如果是普通的用户任务，则执行自动抄送。会签任务在会签结果确定后再进行自动抄送，此处不抄送
			if (NodeType.USERTASK.getKey().equals(bpmNodeDef.getType().getKey())) {
				BpmUtil.autoTrans(bpmNodeDef, instId, bpmTask.getId());
			}
		} catch (Exception e1) {
			throw new WorkFlowException(ExceptionUtils.getRootCauseMessage(e1));
		}
		// 如果是驳回按流程图执行，则删除之后的所有任务和堆栈关系
		if (actionCmd != null ){
			Object isBackCancelBoject = actionCmd.getTransitVars(BpmExeStack.HAND_MODE_NORMAL_IS_CANCLE_NODE_PATH_TASK);
			// 是否是按流图执行且退回到发起人
			if (isBackCancelBoject != null){
				String notIncludeExecuteIds = "";
				// 当前执行
				ActExecution currentExecution = actExecutionManager.get(currentExecutionId);
				String processInstanceId = delegateTask.getProcessInstanceId();

				// 多实例子流程驳回时需要特殊处理
				Object objToken = natProInstanceService.getVariable(currentExecutionId, BpmConstants.TOKEN_NAME);
				String currentToken = objToken != null ? objToken.toString() : null;
				// 是否是多实例子流程内部之间的退回
				boolean isMultiInnerReject = false;
				
				// 多实例子流程
				if (currentToken != null && !StringUtil.isEmpty(currentToken) && StringUtil.isNotZeroEmpty(currentToken))
				{
					isMultiInnerReject = true;
					// 取上级的执行线程
					currentExecution = actExecutionManager.get(currentExecution.getParentId());

					notIncludeExecuteIds = currentExecutionId + "," + currentExecution.getId();

					// 是否为多实例子流程内部退回到主流程
					boolean isHaveMultiGateway = false;
					try {
						isHaveMultiGateway = BpmStackRelationUtil.isHaveMultiGatewayByBetweenNode(instId, rejectTargetNodeId, currentNodeId);
					} catch (Exception e) {
						throw new WorkFlowException(ExceptionUtils.getRootCauseMessage(e));
					}

					if (isHaveMultiGateway){
						isMultiInnerReject = false;
						// 多实例子流程按流程图走且退到主流程
						String rejectAfterExecutionId = currentExecution.getId();
						cmd.addTransitVars("rejectAfterExecutionId", rejectAfterExecutionId);
					} 


					currentExecution = actExecutionManager.get(currentExecution.getParentId());
					notIncludeExecuteIds = notIncludeExecuteIds + "," + currentExecution.getId();

				} 
				else
				{
					currentExecution.setParentId(processInstanceId);
					actExecutionManager.update(currentExecution);

					// 是否为子流程或者并行网关内退回到主流程
					boolean isHaveGateway = false;
					try {
						isHaveGateway = BpmStackRelationUtil.isHaveAndOrGateway(instId, currentNodeId, "pre");
					} catch (Exception e) {
						throw new WorkFlowException(ExceptionUtils.getRootCauseMessage(e));
					}
					if (isHaveGateway)
					{
						String rejectAfterExecutionId = currentExecution.getId();
						cmd.addTransitVars("rejectSingleExecutionId", rejectAfterExecutionId);
					}
				}

				String targetNodePath = (String) actionCmd.getTransitVars(BpmExeStack.HAND_MODE_NORMAL_TARGET_NODE_PATH)+"%";
			
				Map<String, BpmNodeDef> betweenNodes = com.hotent.bpm.util.BpmUtil.getDeleteTaskNodes(bpmNodeDef, cmd.getDestination());
				if (!isMultiInnerReject){
					Object rejectAfterExecutionId = actionCmd.getTransitVars("rejectSingleExecutionId");
					if(rejectAfterExecutionId==null || StringUtil.isEmpty(rejectAfterExecutionId.toString())){
						if (notIncludeExecuteIds.equals("")){
							notIncludeExecuteIds = currentExecutionId;
						}
						// 删除Activiti的执行时表,当前主线程不可删除
						bpmExeStackManager.removeActRuExeCutionByPath(instId, targetNodePath, notIncludeExecuteIds);
					}
				} 
				bpmExeStackManager.removeBpmTaskCandidateByPath(instId, betweenNodes.keySet());
				bpmExeStackManager.removeBpmTaskByPath(instId, betweenNodes.keySet());
				// 多实例子流程同部间驳回的特殊处理，主要解决堆栈没有删除会导致重复问题，保需要删除堆栈和堆栈关系
				handlebpmExeStack(instId,targetNodePath);
				actionCmd.getTransitVars().remove(BpmExeStack.HAND_MODE_NORMAL_IS_CANCLE_NODE_PATH_TASK);
			}else{
				//处理动作
				String actionName = cmd.getActionName();
				//如果是驳回：reject或backToStart
				if(StringUtil.isNotEmpty(actionName) && ("reject".equals(actionName) || "backToStart".equals(actionName))){
					String handMode = (String) actionCmd.getTransitVars(BpmConstants.BACK_HAND_MODE);
					//如果驳回的模式是直来直往：direct
					if(StringUtil.isNotEmpty(handMode) && handMode.equals(BpmExeStack.HAND_MODE_DIRECT)){
						// 当前执行
						ActExecution currentExecution = actExecutionManager.get(currentExecutionId);
						List<BpmNodeDef> listBpmNodeDef = null;
						try {
							listBpmNodeDef = BpmStackRelationUtil.getHistoryListBpmNodeDef(instId,currentExecution.getActId(), "pre");
						} catch (Exception e) {
							throw new WorkFlowException(ExceptionUtils.getRootCauseMessage(e));
						}
						
						//如果是同步网关
						if(BeanUtils.isNotEmpty(listBpmNodeDef) && (listBpmNodeDef.get(0).getType().equals(NodeType.PARALLELGATEWAY) ||
								listBpmNodeDef.get(0).getType().equals(NodeType.INCLUSIVEGATEWAY))){
							//当前记录为并行并且父ID等于流程实例ID
							if(currentExecution.getIsConcurrent().toString().equals("1") ){
								cmd.addTransitVars("rejectDirectExecutionId", currentExecution.getId());
								cmd.addTransitVars("rejectDirectParentId", currentExecution.getParentId());
							}
						}
					}
				}
				
			}
			NodeNotifyModel model = new NodeNotifyModel(bpmTask.getProcDefId(), bpmTask.getNodeId());
			model.setTask(task);
			model.setTiming("complete");
			NodeNotifyEvent ev = new NodeNotifyEvent(model);
			AppUtil.publishEvent(ev);

		}

	}
	
	private void handlebpmExeStack(String instId, String targetNodePath) {
		// An撤回时需要用到
		TaskFinishCmd cmd = (TaskFinishCmd) ContextThreadUtil.getActionCmd();
		
		BpmExeStack bpmExeStack = bpmExeStackManager.getByInstIdAndTargetNodePath(instId,targetNodePath.replace("%", ""));
		cmd.addTransitVars(BpmConstants.PARENT_STACK, bpmExeStack);
		ContextThreadUtil.putCommonVars(BpmConstants.PARENT_STACK, bpmExeStack);
		
		// 保存一份数据到 bpm_exe_stack_his 和 bpm_exe_stack_relation_his 表中
		// 先删除历史数据
		bpmExeStackManager.removeHisByInstId(instId);
		bpmExeStackManager.removeStackRelationHisByInstId(instId);
		
		// 备份将要删除的堆栈数据  在驳回撤回时 恢复数据
		bpmExeStackManager.stackRelation2HisInToStackIdOrFormStackId(instId, targetNodePath);
		bpmExeStackManager.stack2HisByPath(instId, targetNodePath);
		
		// 删除堆栈关系
		bpmExeStackManager.removeBpmExeStackRelationInToStackId(instId, targetNodePath);
		bpmExeStackManager.removeBpmExeStackRelationInFromStackId(instId, targetNodePath);
		// 删除堆栈
		bpmExeStackManager.removeByPath(instId, targetNodePath);
	}

	/**
	 * 流程跟踪任务
	 * @param cmd
	 * @throws Exception 
	 */
	public void followTask(TaskFinishCmd cmd,String nodeId) throws Exception {
		// 判断是否是跟踪任务
		if (cmd.getActionName().equals("agree") || cmd.getActionName().equals("reject")) {
			QueryFilter build = QueryFilter.build();
			build.addFilter("PRO_INST_", cmd.getInstId(), QueryOP.EQUAL);
			build.addFilter("TASK_ID", nodeId, QueryOP.LIKE);
			PageList<TaskFollow> list = taskFollowManager.query(build);
			if (list.getRows().size() > 0) {
				List<TaskFollow> rows = list.getRows();
				DefaultBpmTask task=(DefaultBpmTask) cmd.getTransitVars(BpmConstants.BPM_TASK);
				BpmTaskNoticeManager noticeManager = AppUtil.getBean(BpmTaskNoticeManager.class);
				for (TaskFollow follow : rows) {
					BpmTaskNotice taskNotice = new BpmTaskNotice(task.getName(), task.getSubject(), task.getProcInstId(), task.getProcDefId(), task.getProcDefName(), "", "",TaskType.FOLLOW.getKey(),task.getSupportMobile(),"","",null,task.getId(),null);
					taskNotice.setId(UniqueIdUtil.getSuid());
					taskNotice.setTaskId(task.getId());
					taskNotice.setNodeId(nodeId);
					if(StringUtil.isNotEmpty(follow.getCreatorId())){
						String creatorId = follow.getCreatorId();
						IUser user = ServiceUtil.getUserById(creatorId);
						if(BeanUtils.isNotEmpty(user)){
							taskNotice.setOwnerName(user.getFullname());
							taskNotice.setAssigneeName(user.getFullname());
						}
						taskNotice.setOwnerId(creatorId);
						taskNotice.setAssigneeId(creatorId);
					}
                    taskNotice.setIsRead(0);
					noticeManager.create(taskNotice);
				}
			}
		}
	}

	private void updDueTime(BpmTask bpmTask) throws Exception {
		BpmTaskDueTime bpmTaskDueTime = bpmTaskDueTimeManager.getByTaskId(bpmTask.getId());
		if(BeanUtils.isEmpty(bpmTaskDueTime))return;
		int remainingTime=0;
		if("caltime".equals(bpmTaskDueTime.getDateType())){
			// getSecondDiff 秒
			remainingTime = TimeUtil.getSecondDiff(LocalDateTime.now(), bpmTaskDueTime.getStartTime())/60;
		}else{
			// getWorkTimeByUser 毫秒
			ObjectNode params=JsonUtil.getMapper().createObjectNode();
			params.put("userId", bpmTaskDueTime.getUserId());
			params.put("startTime", DateFormatUtil.formaDatetTime(bpmTaskDueTime.getStartTime()));
			params.put("endTime", DateFormatUtil.formaDatetTime(LocalDateTime.now()));
			remainingTime =(int) (PortalFeignService.getWorkTimeByUser(params)/60000);
		}
		remainingTime = bpmTaskDueTime.getDueTime() - remainingTime;
		if(remainingTime<=0){
			remainingTime = 0;
		}
		bpmTaskDueTime.setRemainingTime(remainingTime);
		bpmTaskDueTimeManager.update(bpmTaskDueTime);
	}

	/**
	 * 更新流程实例状态。
	 * 
	 * @param cmd
	 *            void
	 */
	private void updProcessInstance(TaskFinishCmd cmd)
	{
		DefaultBpmProcessInstance bpmProcessInstance = (DefaultBpmProcessInstance) cmd.getTransitVars(BpmConstants.PROCESS_INST);
		ProcessInstanceStatus status = getInstStatus(cmd);

		if (!status.getKey().equals(bpmProcessInstance.getStatus())){
			bpmProcessInstance.setStatus(status.getKey());
			bpmProcessInstanceManager.update(bpmProcessInstance);
		}
		//流程实例结束跟踪流程实例删除
		if(ProcessInstanceStatus.STATUS_END.getKey().equals(status.getKey())) {
			taskFollowManager.remove(bpmProcessInstance.getId());
		}
	}

	/**
	 * 更新堆栈数据。 结束时
	 * 
	 * @param bpmTask
	 *            void
	 * @param cmd
	 */
	private void updStack(BpmTask bpmTask, ActionCmd cmd)
	{
		if (!(cmd instanceof TaskFinishCmd)) return;
		TaskFinishCmd finishCmd = (TaskFinishCmd) cmd;
		
		// 在已办中撤回 不需要记录 
		Object isDoneUnused = finishCmd.getTransitVars("IsDoneUnused");
		Object recordStack = finishCmd.getTransitVars(BpmConstants.RECORD_STACK);
		if(BeanUtils.isNotEmpty(isDoneUnused) && recordStack ==null ) {
			return ;
		}
		
		if (!ActionType.APPROVE.equals(finishCmd.getActionType())) 	return;
		Object tokenObj = natProInstanceService.getVariable(bpmTask.getExecId(), BpmConstants.TOKEN_NAME);
		String token = tokenObj == null ? null : tokenObj.toString();

		if (StringUtil.isZeroEmpty(token))
		{
			BpmDelegateTask task = natTaskService.getByTaskId(bpmTask.getTaskId());
			token = task.getVariable(BpmConstants.TOKEN_NAME) != null ? task.getVariable(BpmConstants.TOKEN_NAME).toString() : null;
		}

		BpmExeStack bpmExeStack = bpmExeStackManager.getStack(bpmTask.getProcInstId(), bpmTask.getNodeId(), null);
		if (bpmExeStack == null)
			// 当驳回到并行之前的节点时不需要token
			bpmExeStack = bpmExeStackManager.getStack(bpmTask.getProcInstId(), bpmTask.getNodeId(), null);
		// TODO delete 兼容错误数据 旧流程任务没有堆栈信息，
		if (bpmExeStack == null)
			return;

		bpmExeStack.setEndTime(LocalDateTime.now());

		bpmExeStackManager.update(bpmExeStack);
		// 更新堆栈执行人信息。
		BpmExeStackExecutor executor = bpmExeStackExecutorManager.getByTaskId(bpmTask.getId());
		// 将变量设置临时变量。
		cmd.addTransitVars(BpmConstants.PARENT_STACK, bpmExeStack);
		ContextThreadUtil.putCommonVars(BpmConstants.PARENT_STACK, bpmExeStack);
		// 驳回至该节点时。executor 通过taskId 取不到
		if (executor == null)
		{
			List<BpmExeStackExecutor> executorList = bpmExeStackExecutorManager.getByStackId(bpmExeStack.getId());
			if (executorList.size() == 1)
				executor = executorList.get(0);
			else
				return;
		}
		// 设置任务执行人
		executor.setAssigneeId(ContextUtil.getCurrentUserId());

		executor.setEndTime(LocalDateTime.now());
		// 是否干预
		if (finishCmd.isInterpose())
		{
			executor.setStatus(2);
		} else
		{
			executor.setStatus(1);
		}
		bpmExeStackExecutorManager.update(executor);
	}

	/**
	 * 更新沟通任务， 主要是删除沟通任务。
	 * 
	 * @param taskId
	 *            void
	 */
	private void updTaskCommuComplete(String taskId)
	{
		taskCommuService.finishTask(taskId);
	}

	/**
	 * 更新转办代理任务为完成。
	 * 
	 * @param taskId
	 *            void
	 */
	private void updTaskTurnComplte(String taskId)
	{
		IUser user = ContextUtil.getCurrentUser();
		bpmTaskTurnManager.updComplete(taskId, user);
	}

	@SuppressWarnings("incomplete-switch")
	private ProcessInstanceStatus getInstStatus(TaskFinishCmd cmd)
	{
		ProcessInstanceStatus status = ProcessInstanceStatus.STATUS_RUNNING;
		String action = cmd.getActionName();
		switch (cmd.getActionType())
		{
		case APPROVE:
			status = ProcessInstanceStatus.STATUS_RUNNING;
			break;
		case BACK_TO_START:
			status = ProcessInstanceStatus.STATUS_BACK_TOSTART;
			break;
		case BACK:
			if ("toStart".equals(action))
			{
				status = ProcessInstanceStatus.STATUS_BACK_TOSTART;
			} else
			{
				status = ProcessInstanceStatus.STATUS_BACK;
			}
			break;
		case RECOVER:
			if ("toStart".equals(action))
			{
				status = ProcessInstanceStatus.STATUS_REVOKE_TOSTART;
			} else
			{
				status = ProcessInstanceStatus.STATUS_REVOKE;
			}
			break;
		}
		return status;
	}

	@SuppressWarnings("incomplete-switch")
	private NodeStatus getNodeStatus(TaskFinishCmd cmd){
		NodeStatus nodeStatus = NodeStatus.AGREE;
		String action = cmd.getActionName();
		// 在已办中撤回
		Object isDoneUnused = cmd.getTransitVars("IsDoneUnused");
		if (isDoneUnused != null){
			nodeStatus = NodeStatus.RECOVER;
			return nodeStatus;
		}
		switch (cmd.getActionType()){
			case APPROVE:
				nodeStatus = NodeStatus.fromKey(cmd.getActionName());
				break;
			case BACK_TO_START:
				nodeStatus = NodeStatus.BACK_TO_START;
				break;
			case BACK:
				if ("toStart".equals(action) || "backToStart".equals(action)){
					nodeStatus = NodeStatus.BACK_TO_START;
				} 
				else{
					nodeStatus = NodeStatus.BACK;
				}
				break;
			case RECOVER:
				if ("toStart".equals(action)){
					nodeStatus = NodeStatus.RECOVER_TO_START;
				} 
				else{
					nodeStatus = NodeStatus.RECOVER;
				}
				break;
		}
		return nodeStatus;
	}

	/**
	 * 更新审批意见
	 * 
	 * @param cmd
	 * @param skipType
	 *            void
	 * @throws Exception 
	 */
	private void updateCheckOpinion(TaskFinishCmd cmd, String skipType) throws Exception
	{
		boolean isSkip = BeanUtils.isNotEmpty(skipType);
		DefaultBpmTask bpmTask = (DefaultBpmTask)cmd.getTransitVars(BpmConstants.BPM_TASK);
		DefaultBpmCheckOpinion bpmCheckOpinion = bpmCheckOpinionManager.getByTaskId(cmd.getTaskId());
		if (bpmCheckOpinion == null) 	return;
		BpmCheckOpinionUtil.updateExtraPropCheckOpinion(bpmCheckOpinion,bpmTask);
		if (StringUtil.isNotEmpty(cmd.getInterPoseOpinion())) {
			bpmCheckOpinionManager.remove(bpmCheckOpinion.getId());
			BpmInterposeRecored bpmInterposeRecored = new BpmInterposeRecored(bpmTask.getProcInstId(), cmd.getInterPoseOpinion(), InterPoseType.fromKey(cmd.getActionName()) , (String) cmd.getTransitVars(BpmConstants.BPM_OPINION_IS_DONE));
			BpmInterposeRecoredManager manager = AppUtil.getBean(BpmInterposeRecoredManager.class);
			bpmInterposeRecored.setTaskName(bpmTask.getName());
			bpmInterposeRecored.setFiles(cmd.getFiles());
			manager.create(bpmInterposeRecored);
			return ;
		}
		IUser user = ContextUtil.getCurrentUser();
		String status = getStatus(cmd, isSkip);

		bpmCheckOpinion.setStatus(status);
		bpmCheckOpinion.setCompleteTime(LocalDateTime.now());
		long durMs =TimeUtil.getTime(bpmCheckOpinion.getCompleteTime(), bpmCheckOpinion.getCreateTime());
		bpmCheckOpinion.setDurMs(durMs);
		if (isSkip && SkipResult.SKIP_EMPTY_USER.equals(skipType)){
			bpmCheckOpinion.setAuditor("");
			bpmCheckOpinion.setAuditorName("");
		}else if(SkipResult.SKIP_APPROVER.equals(skipType)){
			bpmCheckOpinion.setAuditor(String.valueOf(ContextThreadUtil.getCommuVar(SkipResult.SKIP_APPROVER_AUDITOR,"")));
			bpmCheckOpinion.setAuditorName(String.valueOf(ContextThreadUtil.getCommuVar(SkipResult.SKIP_APPROVER_AUDITORNAME, "")));
		} 
		else{
			String userId = BpmConstants.SYSTEM_USER_ID;
			String userName = BpmConstants.SYSTEM_USER_NAME;
			if (user != null){
				userId = user.getUserId();
				userName = user.getFullname();
			}
			bpmCheckOpinion.setAuditor(userId);
			bpmCheckOpinion.setAuditorName(userName);
		}

		String opinion = cmd.getApprovalOpinion();
		if (isSkip){
			if (SkipResult.SKIP_FIRST.equals(skipType)){
				opinion = "跳过第一个任务节点";
			} 
			else if (SkipResult.SKIP_EMPTY_USER.equals(skipType)){
				opinion = "执行人为空";
			} 
			else if (SkipResult.SKIP_SAME_USER.equals(skipType)){
				opinion = "和上一个节点执行人相同跳过!";
			}else if( SkipResult.SKIP_APPROVER.equals(skipType) ){
				opinion = "审批跳过";
			}
			bpmCheckOpinion.setSkipType(skipType);
			//把跳过的审批意见延后一秒。以免和上一个审批意见的完成时间在同一秒，导致不好排序
			bpmCheckOpinion.setCompleteTime(TimeUtil.getLocalDateTimeByMills(TimeUtil.getNextTime(0,1,TimeUtil.getCurrentTimeMillis())));
		}
		// 干预的情况。
		if (cmd.isInterpose())
		{
			bpmCheckOpinion.setInterpose(1);
		}

		/**
		 * 设置意见标识。
		 */
		bpmCheckOpinion.setFormName(cmd.getOpinionIdentity());

		bpmCheckOpinion.setOpinion(opinion);
		bpmCheckOpinion.setFiles(cmd.getFiles());//附件
        bpmCheckOpinion.setZfiles(cmd.getZfiles());//正文
		if (StringUtil.isNotEmpty(cmd.getAgentLeaderId()) && !"0".equals(cmd.getAgentLeaderId())) {
			bpmCheckOpinion.setAgentLeaderId(cmd.getAgentLeaderId());
		}
		if (StringUtil.isNotEmpty(cmd.getBusData())) {
			try {
				bpmCheckOpinion.setFormData(Base64.getBase64(cmd.getBusData()));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		cmd.addVariable(BpmConstants.PRE_BPM_CHECK_OPINION_ID, bpmCheckOpinion.getId());
		
		bpmCheckOpinionManager.update(bpmCheckOpinion);
		
	}

	/**
	 * 获取意见状态。
	 * 
	 * @param cmd
	 * @param isSkip
	 * @return String
	 */
	private String getStatus(TaskFinishCmd cmd, boolean isSkip)
	{
		if (isSkip){
			return OpinionStatus.SKIP.getKey();
		}
		// 在已办中撤回
		Object isDoneUnused = cmd.getTransitVars("IsDoneUnused");
		//转办任务处理
		try {
			// 1.判断该任务是否处于转办中
			BpmTaskTurn bpmTaskTurn = bpmTaskTurnManager.getByTaskId(cmd.getTaskId());
			if (bpmTaskTurn != null) {
				String status=cmd.getActionName();
				if(status.equals("agree")) {
					return OpinionStatus.DELIVERTO_AGREE.getKey();
				}else if(status.equals("oppose")) {
					return OpinionStatus.DELIVERTO_OPPOSE.getKey();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (isDoneUnused != null){
			// 撤回取消
			return OpinionStatus.SIGN_RECOVER_CANCEL.getKey();
		}
		return cmd.getActionName();

	}

	/**
	 * 删除候选人，删除任务。
	 * 
	 * @param taskId
	 *            void
	 */
	private void delCandidateAndTask(String taskId)
	{
		bpmTaskCandidateManager.removeByTaskId(taskId);
		bpmTaskManager.remove(taskId);
	}

	/**
	 * 更新流程状态
	 * 
	 * @param delegateTask
	 * @param nodeStatus
	 *            void
	 */
	private void updateProcStatus(String instId, BpmDelegateTask delegateTask, NodeStatus nodeStatus,ActionCmd actionCmd)
	{
		String bpmnDefId = delegateTask.getBpmnDefId();
		String nodeId = delegateTask.getTaskDefinitionKey();

		MultiInstanceType type = delegateTask.multiInstanceType();

		if (MultiInstanceType.NO.equals(type))
		{
			bpmProStatusManager.createOrUpd(instId, bpmnDefId, nodeId, delegateTask.getName(), nodeStatus);
		}
		String targetNodePath = (String) actionCmd.getTransitVars(BpmExeStack.HAND_MODE_NORMAL_TARGET_NODE_PATH)+"%";
		List<BpmExeStack> bpmExeStacks=bpmExeStackManager.getByBpmTaskByPath(instId, targetNodePath);
		//更新流程相关实例状态
		if(nodeStatus.getKey().equals(NodeStatus.BACK_TO_START.getKey())||nodeStatus.getKey().equals(NodeStatus.BACK.getKey())){
			if (actionCmd != null){
				String backHandMode = (String) actionCmd.getTransitVars().get(BpmConstants.BACK_HAND_MODE);
				if(StringUtil.isEmpty(backHandMode)){
					throw new RuntimeException("backHandMode不能为空");
				}
				if(backHandMode.equals("normal")){
					List<String> instList=new ArrayList<String>();
					instList.add(instId);
					List<DefaultBpmCheckOpinion> checkOpinions = bpmCheckOpinionManager.getByInstIdsAndWait(instList);
					for(DefaultBpmCheckOpinion checkOpinion:checkOpinions){
					    //查询待办任务是否为征询状态
                        BpmTask bpmTask = bpmTaskManager.get(checkOpinion.getTaskId());
                        if(BeanUtils.isNotEmpty(bpmTask)){
                            if("TRANSFORMEDINQU".equals(bpmTask.getStatus())){
                                //删除任务
                                bpmTaskManager.remove(checkOpinion.getTaskId());
                            }
                        }
                        for (BpmExeStack bpmExeStack : bpmExeStacks) {
							if(bpmTask.getNodeId().equals(bpmExeStack.getNodeId())){
								// 更新意见状态为驳回取消。
		                        checkOpinion.setStatus(OpinionStatus.SIGN_BACK_CANCEL.getKey());
		                        checkOpinion.setCompleteTime(LocalDateTime.now());
		                        //普通用户任务加签后驳回重新审批
                                if(BeanUtils.isNotEmpty(((DefaultTaskFinishCmd) actionCmd).getRejectTaskId())){
                                    checkOpinion.setStatus(OpinionStatus.REJECT.getKey());
                                    checkOpinion.setAuditor(bpmTask.getAssigneeId());
                                    checkOpinion.setOpinion(((DefaultTaskFinishCmd) actionCmd).getApprovalOpinion());
                                    checkOpinion.setAuditorName(((DefaultBpmTask) bpmTask).getAssigneeName());
                                }
		                        bpmCheckOpinionManager.update(checkOpinion);
							}
						}
						
                        //bpmCheckOpinionManager.updStatusByWait(checkOpinion.getTaskId(),OpinionStatus.SIGN_BACK_CANCEL.getKey());
					}
					// 节点的代办状态更新为驳回取消
					bpmProStatusManager.updStatusByInstList(instList, NodeStatus.SIGN_BACK_CANCLE);
				}
			}
		}
		
	}
	
	
}
