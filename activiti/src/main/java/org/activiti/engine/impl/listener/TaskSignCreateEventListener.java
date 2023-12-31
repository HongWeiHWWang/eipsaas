package org.activiti.engine.impl.listener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.delegate.DelegateTask;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotent.base.calendar.ICalendarService;
import com.hotent.base.exception.WorkFlowException;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.base.util.time.TimeUtil;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.cmd.BaseActionCmd;
import com.hotent.bpm.api.cmd.TaskFinishCmd;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.constant.EventType;
import com.hotent.bpm.api.constant.ExtractType;
import com.hotent.bpm.api.constant.MultiInstanceType;
import com.hotent.bpm.api.constant.NodeStatus;
import com.hotent.bpm.api.constant.TaskType;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.event.TaskSignCreateEvent;
import com.hotent.bpm.api.helper.identity.BpmIdentityExtractService;
import com.hotent.bpm.api.model.delegate.BpmDelegateTask;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.def.BpmProcessDef;
import com.hotent.bpm.api.model.process.def.BpmProcessDefExt;
import com.hotent.bpm.api.model.process.def.NodeProperties;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.CustomSignNodeDef;
import com.hotent.bpm.api.model.process.task.BpmTask;
import com.hotent.bpm.api.plugin.core.factory.BpmPluginSessionFactory;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.engine.task.cmd.DefaultTaskFinishCmd;
import com.hotent.bpm.persistence.manager.BpmCheckOpinionManager;
import com.hotent.bpm.persistence.manager.BpmCustomSignDataManager;
import com.hotent.bpm.persistence.manager.BpmExeStackManager;
import com.hotent.bpm.persistence.manager.BpmProStatusManager;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.persistence.manager.BpmSignDataManager;
import com.hotent.bpm.persistence.manager.BpmTaskDueTimeManager;
import com.hotent.bpm.persistence.manager.BpmTaskManager;
import com.hotent.bpm.persistence.model.BpmSignData;
import com.hotent.bpm.persistence.model.BpmTaskDueTime;
import com.hotent.bpm.persistence.model.DefaultBpmCheckOpinion;
import com.hotent.bpm.persistence.model.DefaultBpmProcessDefExt;
import com.hotent.bpm.persistence.model.DefaultBpmTask;
import com.hotent.bpm.persistence.util.BpmUtil;
import com.hotent.bpm.util.BpmCheckOpinionUtil;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserService;

/**
 * 处理TaskSignCreateEvent会签任务创建监听器。 1.添加流程意见。 2.分配用户。 3.添加会签数据。 4.修改节点状态。
 * 5.更新会签数据
 * 
 * <pre>
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-31-下午3:30:01
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@Service
@Transactional
public class TaskSignCreateEventListener implements ApplicationListener<TaskSignCreateEvent>, Ordered {

	@Resource
	BpmTaskManager bpmTaskManager;
	@Resource
	BpmSignDataManager bpmSignDataManager;
	@Resource
	BpmCheckOpinionManager bpmCheckOpinionManager;
	@Resource
	BpmProStatusManager bpmProStatusManager;
	@Resource
	BpmExeStackManager bpmExeStackManager;
	@Resource
	BpmDefinitionAccessor bpmDefinitionAccessor;
	@Resource
	ICalendarService iCalendarService;
	@Resource
	BpmTaskDueTimeManager bpmTaskDueTimeManager;
	@Resource
	BpmIdentityExtractService extractService;
	@Resource
	IUserService userServiceImpl;
	@Resource
	BpmPluginSessionFactory bpmPluginSessionFactory;
	@Resource
	BpmIdentityExtractService bpmIdentityExtractService;
	@Resource
	BpmProcessInstanceManager bpmProcessInstanceManager;
	@Resource
	BpmCustomSignDataManager bpmCustomSignDataManager;

	@Override
	public int getOrder() {
		return 1;
	}

	@Override
	public void onApplicationEvent(TaskSignCreateEvent event) {
		try {
			BpmDelegateTask delegateTask = (BpmDelegateTask) event.getSource();

			DelegateTask actTask = (DelegateTask) delegateTask.getProxyObj();

			BpmTask task = BpmUtil.convertTask(delegateTask);
			ActionCmd cmd = ContextThreadUtil.getActionCmd();
			// 设置上级的executeid
			task.setExecId(actTask.getExecution().getParentId());
			if (MultiInstanceType.PARALLEL.equals(delegateTask.multiInstanceType())) {
				task.setExecId(delegateTask.getParentExecuteId(2));
			}
			String instId = task.getProcInstId();
			String nodeId = task.getNodeId();
			BpmNodeDef bpmNodeDef = bpmDefinitionAccessor.getBpmNodeDef(task.getProcDefId(), nodeId);
			boolean isCommonSign = true;
			String signType = null;
			if (bpmNodeDef instanceof CustomSignNodeDef) {
				CustomSignNodeDef customSignNodeDef = (CustomSignNodeDef) bpmNodeDef;
				signType = customSignNodeDef.getSignType();
				if (StringUtil.isNotEmpty(signType)) {
					isCommonSign = false;
				}
				// 并行签署
				if (CustomSignNodeDef.SIGNTYPE_PARALLEL.equals(signType)) {
					task.setStatus(TaskType.SIGNLINEED.getKey());
				}
				// 顺序签署
				if (CustomSignNodeDef.SIGNTYPE_SEQUENTIAL.equals(signType)) {
					task.setStatus(TaskType.SIGNSEQUENCEED.getKey());
				}
				// 并行审批
				if (CustomSignNodeDef.SIGNTYPE_PARALLELAPPROVE.equals(signType)) {
					task.setStatus(TaskType.APPROVELINEED.getKey());
				}

			}

			bpmTaskManager.create((DefaultBpmTask) task);
	
			BpmIdentity taskExecutor = (BpmIdentity) delegateTask.getVariable(BpmConstants.ASIGNEE);

			// 分配执行人。
			List<BpmIdentity> identityList = new ArrayList<BpmIdentity>();
			identityList.add(taskExecutor);

			// 二次抽取
			if (taskExecutor != null && ExtractType.EXACT_EXACT_DELAY.equals(taskExecutor.getExtractType())) {
				identityList = extractService.extractBpmIdentity(identityList);
			}

			try {
				bpmTaskManager.assignUser(delegateTask, identityList);
			} catch (Exception e2) {
				throw new WorkFlowException(ExceptionUtils.getRootCauseMessage(e2));
			}
			
			// 添加意见
			updatePreOpinion(isCommonSign);
			addOpinion(delegateTask, identityList, instId, signType,isCommonSign);
			// 期限设置
			try {
				setDueTime(delegateTask, identityList);
			} catch (Exception e2) {
				throw new WorkFlowException(ExceptionUtils.getRootCauseMessage(e2));
			}

			// 修改节点状态节点为待审批。
			bpmProStatusManager.createOrUpd(instId, delegateTask.getBpmnDefId(), delegateTask.getTaskDefinitionKey(),
					delegateTask.getName(), NodeStatus.PENDING);

			// 添加会签数据。
			Integer loopCounter = (Integer) delegateTask.getVariable(BpmConstants.NUMBER_OF_LOOPCOUNTER);
			Integer token_ = 0;
			if (loopCounter == null) {
				loopCounter = 0;
			}
			token_ = loopCounter;
			// 会签任务 创建堆栈时， 不作为网关节点去创建堆栈信息 SubProcessMultiInstanceStartEventListener

			// 流程变量名称。
			String resultVarName = BpmConstants.SIGN_RESULT + delegateTask.getTaskDefinitionKey();

			MultiInstanceType instanceType = delegateTask.multiInstanceType();
			// 并行会签
			if (MultiInstanceType.PARALLEL.equals(instanceType)) {
				// 清除之前的会签结果
				delegateTask.removeVariable(resultVarName);
				// 并行往上查找两级。
				String executeId = delegateTask.getParentExecuteId(2);
				if (isCommonSign) {
					addSignData(task, executeId, loopCounter);
				} else if(cmd.getTransitVars("IsDoneUnused") == null){
					bpmCustomSignDataManager.addCustomSignData(task, null);
				}

			} else {
				delegateTask.setVariableLocal(BpmConstants.TOKEN_NAME, token_);
				// 串行一次加入, 会签驳回撤回时不需要再次添加会签数据
				if (loopCounter == 0 || (cmd.getTransitVars("IsDoneUnused") != null
						&& (Boolean) cmd.getTransitVars("IsDoneUnused"))) {
					// 清除之前的会签结果
					delegateTask.removeVariable(resultVarName);

					String executeId = actTask.getExecution().getParentId();
					if (isCommonSign) {
						addSignData(task, executeId);
					}
				}
				if (!isCommonSign && cmd.getTransitVars("IsDoneUnused") == null) {
					String preTaskId = null;
					if (cmd instanceof DefaultTaskFinishCmd && loopCounter > 0) {
						DefaultTaskFinishCmd taskFinishCmd = (DefaultTaskFinishCmd) cmd;
						preTaskId = taskFinishCmd.getTaskId();
					}
					bpmCustomSignDataManager.addCustomSignData(task, preTaskId);
				}
				
			}
			
			cmd.addTransitVars("SubProcessMultiStartOrEndEvent", null);

			// 加入堆栈数据。
			try {
				bpmExeStackManager.pushStack(delegateTask);
			} catch (Exception e1) {
				throw new WorkFlowException(ExceptionUtils.getRootCauseMessage(e1));
			}

			// 会签创建任务调用restful接口事件
			try {
				BpmIdentityExtractService bpmIdentityExtractService = AppUtil.getBean(BpmIdentityExtractService.class);
				List<IUser> users = bpmIdentityExtractService.extractUser(identityList);
				if (BeanUtils.isNotEmpty(users)) {
					DefaultBpmTask defaultTask = BpmUtil.convertTask((DefaultBpmTask) task, task.getParentId(),
							TaskType.NORMAL, users.get(0));
					BpmUtil.restfulPluginExecut(defaultTask, EventType.TASK_CREATE_EVENT);
				} else {
					BpmUtil.restfulPluginExecut((DefaultBpmTask) task, EventType.TASK_CREATE_EVENT);
				}
			} catch (Exception e) {
				System.out.println("会签restful接口调用失败：" + e.getMessage());
			}
		} catch (Exception e3) {
			throw new WorkFlowException(ExceptionUtils.getRootCauseMessage(e3));
		}
	}

	/**
	 * 进入待办时 当前任务未签署任务需要
	 * 
	 * @param taskId
	 */
	private void updatePreOpinion(boolean isCommonSign) {
		ActionCmd cmd = ContextThreadUtil.getActionCmd();
		Map<String, Object> variables = cmd.getVariables();
		if (variables.containsKey(BpmConstants.PRE_BPM_CHECK_OPINION_ID)
				&& BeanUtils.isNotEmpty(variables.get(BpmConstants.PRE_BPM_CHECK_OPINION_ID))) {
			String id = String.valueOf(variables.get(BpmConstants.PRE_BPM_CHECK_OPINION_ID));
			DefaultBpmCheckOpinion defaultBpmCheckOpinion = bpmCheckOpinionManager.get(id);
			if (BeanUtils.isNotEmpty(defaultBpmCheckOpinion)
					&& StringUtil.isEmpty(defaultBpmCheckOpinion.getSignType()) && !isCommonSign ) {
				defaultBpmCheckOpinion.setSignType(CustomSignNodeDef.BEFORE_SIGN);
				bpmCheckOpinionManager.update(defaultBpmCheckOpinion);
			}
		}

	}


	/**
	 * 添加审批意见。
	 * 
	 * @param delegateTask
	 * @param identityList
	 * @param instId       void
	 * @exception @since 1.0.0
	 */
	private void addOpinion(BpmDelegateTask delegateTask, List<BpmIdentity> identityList, String instId,
			String signType,boolean isCommonSign) {
		DefaultBpmCheckOpinion opinion = BpmCheckOpinionUtil.buildBpmCheckOpinion(delegateTask, instId);
		opinion.setSignType(signType);
		ActionCmd cmd = ContextThreadUtil.getActionCmd();
		if(isCommonSign) {
			Map<String, Object> variables = cmd.getVariables();
			if (variables.containsKey(BpmConstants.PRE_BPM_CHECK_OPINION_ID)
					&& BeanUtils.isNotEmpty(variables.get(BpmConstants.PRE_BPM_CHECK_OPINION_ID))) {
				String id = String.valueOf(variables.get(BpmConstants.PRE_BPM_CHECK_OPINION_ID));
				DefaultBpmCheckOpinion defaultBpmCheckOpinion = bpmCheckOpinionManager.get(id);
				if (BeanUtils.isNotEmpty(defaultBpmCheckOpinion)
						&& StringUtil.isNotEmpty(defaultBpmCheckOpinion.getSignType())) {
					opinion.setSignType(CustomSignNodeDef.AFTER_SIGN);
				}
			}

		}
		if (cmd instanceof TaskFinishCmd) {
			opinion.setParentTaskId(((TaskFinishCmd)cmd).getTaskId());
		}
		String ids = BpmCheckOpinionUtil.getIdentityIds(identityList);
		String names = BpmCheckOpinionUtil.getIdentityNames(identityList);
		opinion.setQualfieds(ids);
		opinion.setQualfiedNames(names);
		bpmCheckOpinionManager.create(opinion);
	}

	/**
	 * 添加会签数据。
	 * 
	 * @param bpmTask
	 * @param taskExecutor void
	 */
	private void addSignData(BpmTask bpmTask, String executeId, Integer index) {
		String nodeId = bpmTask.getNodeId();

		BaseActionCmd actionCmd = (BaseActionCmd) ContextThreadUtil.getActionCmd();
		List<BpmIdentity> idList = actionCmd.getBpmIdentities().get(nodeId);
		if (BeanUtils.isEmpty(idList))
			return;

		// 生成投票的数据
		BpmIdentity bpmIdentity = idList.get(index);
		BpmSignData signData = bpmSignDataManager.getSignData(bpmTask, executeId, bpmIdentity);

		signData.setIndex(index.shortValue());
		bpmSignDataManager.create(signData);

	}

	private void addSignData(BpmTask bpmTask, String executeId) {
		String nodeId = bpmTask.getNodeId();

		BaseActionCmd actionCmd = (BaseActionCmd) ContextThreadUtil.getActionCmd();
		List<BpmIdentity> idList = actionCmd.getBpmIdentities().get(nodeId);
		if (BeanUtils.isEmpty(idList))
			return;

		// 生成投票的数据
		for (short i = 0; i < idList.size(); i++) {
			BpmIdentity bpmIdentity = idList.get(i);
			BpmSignData signData = bpmSignDataManager.getSignData(bpmTask, executeId, bpmIdentity);
			signData.setIndex(i);
			bpmSignDataManager.create(signData);
		}

	}

	private void setDueTime(BpmDelegateTask delegateTask, List<BpmIdentity> identityList) throws Exception {
		DefaultBpmTask bpmTask = bpmTaskManager.getByRelateTaskId(delegateTask.getId());
		BpmNodeDef bpmNodeDef = bpmDefinitionAccessor.getBpmNodeDef(bpmTask.getProcDefId(), bpmTask.getNodeId());
		BpmProcessDef<BpmProcessDefExt> bpmProcessDef = bpmDefinitionAccessor.getBpmProcessDef(bpmTask.getProcDefId());
		DefaultBpmProcessDefExt defExt = (DefaultBpmProcessDefExt) bpmProcessDef.getProcessDefExt();
		// 设置任务到期时间
		NodeProperties nodeProperties = bpmNodeDef.getLocalProperties();
		LocalDateTime dueTime = null;
		String userId = "", userName = "";

		int dueTimeMin = 0;
		String dateTpye = "";
		if (nodeProperties.getDueTime() != 0) {
			dueTimeMin = nodeProperties.getDueTime();
			dateTpye = nodeProperties.getDateType();
		} else {
			dueTimeMin = defExt.getExtProperties().getDueTime();
			dateTpye = defExt.getExtProperties().getDateType();
		}

		if (dueTimeMin == 0)
			return;

		if ("caltime".equals(dateTpye)) {
			dueTime = TimeUtil.getLocalDateTimeByMills(
					TimeUtil.getNextTime(TimeUtil.MINUTE, dueTimeMin, TimeUtil.getTimeMillis(bpmTask.getCreateTime())));
		} else {
			// 设置第一个执行人的工作日
			if (BeanUtils.isNotEmpty(identityList)) {
				BpmIdentity bpmIdentity = identityList.get(0);
				if (BpmIdentity.TYPE_USER.equals(bpmIdentity.getType())) {
					userId = bpmIdentity.getId();
					userName = bpmIdentity.getName();
					dueTime = iCalendarService.getEndTimeByUser(identityList.get(0).getId(), bpmTask.getCreateTime(),
							dueTimeMin);
				}
			}
		}
		bpmTask.setDueTime(dueTime);
		bpmTaskManager.update(bpmTask);

		BpmTaskDueTime bpmTaskDueTime = new BpmTaskDueTime();
		bpmTaskDueTime.setId(UniqueIdUtil.getSuid());
		bpmTaskDueTime.setDateType(dateTpye);
		bpmTaskDueTime.setDueTime(dueTimeMin);
		bpmTaskDueTime.setRemainingTime(dueTimeMin);
		bpmTaskDueTime.setExpirationDate(dueTime);
		bpmTaskDueTime.setInstId(bpmTask.getProcInstId());
		bpmTaskDueTime.setTaskId(bpmTask.getTaskId());
		bpmTaskDueTime.setStartTime(bpmTask.getCreateTime());
		bpmTaskDueTime.setUserId(userId);
		bpmTaskDueTime.setUserName(userName);
		bpmTaskDueTime.setIsNew((short) 1);
		bpmTaskDueTime.setStatus((short) 0);

		bpmTaskDueTimeManager.create(bpmTaskDueTime);

	}

}
