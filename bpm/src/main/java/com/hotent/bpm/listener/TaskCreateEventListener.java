package com.hotent.bpm.listener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.hotent.base.util.*;
import com.hotent.bpm.api.event.NodeNotifyEvent;
import com.hotent.bpm.api.event.NodeNotifyModel;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.exception.WorkFlowException;
import com.hotent.base.feign.PortalFeignService;
import com.hotent.base.util.time.DateFormatUtil;
import com.hotent.base.util.time.TimeUtil;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.cmd.TaskFinishCmd;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.constant.NodeStatus;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.event.NoExecutorModel;
import com.hotent.bpm.api.event.TaskCreateEvent;
import com.hotent.bpm.api.helper.identity.BpmIdentityExtractService;
import com.hotent.bpm.api.model.delegate.BpmDelegateTask;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.def.BpmProcessDef;
import com.hotent.bpm.api.model.process.def.BpmProcessDefExt;
import com.hotent.bpm.api.model.process.def.NodeProperties;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.CustomSignNodeDef;
import com.hotent.bpm.api.plugin.core.factory.BpmPluginSessionFactory;
import com.hotent.bpm.api.service.BpmAgentService;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.exception.NoTaskUserException;
import com.hotent.bpm.model.identity.DefaultBpmIdentity;
import com.hotent.bpm.persistence.manager.ActTaskManager;
import com.hotent.bpm.persistence.manager.BpmCheckOpinionManager;
import com.hotent.bpm.persistence.manager.BpmExeStackManager;
import com.hotent.bpm.persistence.manager.BpmProStatusManager;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.persistence.manager.BpmTaskDueTimeManager;
import com.hotent.bpm.persistence.manager.BpmTaskManager;
import com.hotent.bpm.persistence.model.BpmTaskDueTime;
import com.hotent.bpm.persistence.model.DefaultBpmCheckOpinion;
import com.hotent.bpm.persistence.model.DefaultBpmProcessDefExt;
import com.hotent.bpm.persistence.model.DefaultBpmTask;
import com.hotent.bpm.util.BpmCheckOpinionUtil;
import com.hotent.bpm.util.BpmUtil;
import com.hotent.uc.api.service.IUserService;

/**
 * 任务创建时的动作。
 * 
 * <pre>
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-25-下午10:02:23
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@Service
@Transactional
public class TaskCreateEventListener implements ApplicationListener<TaskCreateEvent>, Ordered {
	@Resource
	BpmTaskManager bpmTaskManager;
	@Resource
	BpmCheckOpinionManager bpmCheckOpinionManager;
	@Resource
	BpmProStatusManager bpmProStatusManager;
	@Resource
	BpmAgentService bpmAgentService;
	@Resource
	BpmExeStackManager bpmExeStackManager;
	@Resource
	BpmDefinitionAccessor bpmDefinitionAccessor;
	@Resource
	PortalFeignService PortalFeignService;
	@Resource
	BpmTaskDueTimeManager bpmTaskDueTimeManager;
	@Resource
	IUserService userServiceImpl;
	@Resource
	BpmPluginSessionFactory bpmPluginSessionFactory;
	@Resource
	BpmIdentityExtractService bpmIdentityExtractService;
	@Resource
	BpmProcessInstanceManager instanceManager;
	@Resource
	ActTaskManager actTaskManager;

	@Override
	public int getOrder() {
		return 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onApplicationEvent(TaskCreateEvent ev) {
		try {
			BpmDelegateTask delegateTask = (BpmDelegateTask) ev.getSource();
			String instId = (String) delegateTask.getVariable(BpmConstants.PROCESS_INST_ID);
			String subject = (String) delegateTask.getVariable(BpmConstants.SUBJECT);
			String nodeId = delegateTask.getTaskDefinitionKey();
			ActionCmd taskCmd = ContextThreadUtil.getActionCmd();

			List<BpmIdentity> identityList = new ArrayList<BpmIdentity>();
			// 从流程变量中获取是否设置了人员，如果设置则从流程变量中获取。
			Map<String, List<BpmIdentity>> nodeUsers = (Map<String, List<BpmIdentity>>) taskCmd
					.getTransitVars(BpmConstants.BPM_NODE_USERS);
			if (BeanUtils.isEmpty(nodeUsers)) { // (BpmConstants.BPM_NODE_USERS)
				Map<String, Object> variables = taskCmd.getVariables();
				String nodeUserStr = (String) variables.get(BpmConstants.BPM_NODE_USERS);
				if (StringUtil.isNotEmpty(nodeUserStr)) {
					JsonNode jsonNode = JsonUtil.toJsonNode(nodeUserStr);
					if (jsonNode.has(nodeId)) {
						ArrayNode jsonNode2 = (ArrayNode) jsonNode.get(nodeId);
						for (JsonNode jsonNode3 : jsonNode2) {
							DefaultBpmIdentity identity = JsonUtil.toBean(jsonNode3, DefaultBpmIdentity.class);
							identityList.add(identity);
						}
					}
				}
			}
			// 正常跳转指定的执行人
			if (taskCmd.getTransitVars(BpmConstants.BPM_NEXT_NODE_USERS) != null) {
				identityList = (List<BpmIdentity>) taskCmd.getTransitVars(BpmConstants.BPM_NEXT_NODE_USERS);
			}
			// 已有指定执行人
			if (nodeUsers != null && nodeUsers.containsKey(nodeId)) {
				identityList = nodeUsers.get(nodeId);
			}
			// 先从任务的Excutors中获取，如果获取不到再从CMD中获取。
			if (BeanUtils.isEmpty(identityList)) {
				identityList = delegateTask.getExecutors();
			}
			// 如果在上下文指定了人员，则取上下文的人员。
			if (BeanUtils.isEmpty(identityList)) {
				Map<String, List<BpmIdentity>> identityMap = taskCmd.getBpmIdentities();
				identityList = identityMap.get(nodeId);
			}
            
			BpmProcessInstance instance = (BpmProcessInstance) taskCmd.getTransitVars(BpmConstants.PROCESS_INST);
            if (BeanUtils.isEmpty(instance)) {
            	instance = instanceManager.get(instId);
			}
			// boolean isAllowEmptyIdentity = BpmUtil.isAllowEmptyIdentity(instance,
			// nodeId);//判断是否允许执行人为空，先去掉
			boolean isAllowEmptyIdentity = BpmUtil.isAllowEmptyIdentity(instance, nodeId);
			// 判断执行人为空时跳过任务
			// 如果不允许执行人为空，并且人员为空的情况抛出异常。
			if (isAllowEmptyIdentity == false && BeanUtils.isEmpty(identityList)) {
				NoExecutorModel noExcutor = NoExecutorModel.getNoExecutorModel(delegateTask.getId(),
						delegateTask.getProcessInstanceId(), subject, delegateTask.getTaskDefinitionKey(),
						delegateTask.getName(), delegateTask.getBpmnDefId());

				BpmUtil.publishNoExecutorEvent(noExcutor);
                String msg ="【"+delegateTask.getName() +"】没有任务执行人";
				ThreadMsgUtil.addMapMsg(ThreadMsgUtil.MSG_FLOW_ERROR, msg);
				throw new NoTaskUserException(msg);
			}

			// 分配人员
			bpmTaskManager.assignUser(delegateTask, identityList);

			// 添加意见
			addOpinion(delegateTask, identityList);

			// 修改节点状态节点为待审批。
			bpmProStatusManager.createOrUpd(instId, delegateTask.getBpmnDefId(), nodeId, delegateTask.getName(),
					NodeStatus.PENDING);
			// 加入堆栈数据。
			bpmExeStackManager.pushStack(delegateTask);

			setDueTime(delegateTask, identityList, instance);

			NodeNotifyModel model = new NodeNotifyModel(instance.getProcDefId(), nodeId);
			model.setTask(delegateTask);
			model.setTiming("create");
			NodeNotifyEvent eve = new NodeNotifyEvent(model);
			AppUtil.publishEvent(eve);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new WorkFlowException(ExceptionUtils.getRootCauseMessage(e));
		}
	}

	private void setDueTime(BpmDelegateTask delegateTask, List<BpmIdentity> identityList, BpmProcessInstance instance)
			throws Exception {
		DefaultBpmTask bpmTask = bpmTaskManager.getByRelateTaskId(delegateTask.getId());
		if (BeanUtils.isEmpty(bpmTask))
			return;
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
					ObjectNode params = JsonUtil.getMapper().createObjectNode();
					params.put("userId", identityList.get(0).getId());
					if (BeanUtils.isNotEmpty(bpmTask.getCreateTime())) {
						params.put("startTime", DateFormatUtil.formaDatetTime(bpmTask.getCreateTime()));
					}
					params.put("time", dueTimeMin);
					String dueTimeStr = PortalFeignService.getEndTimeByUser(params);
					dueTime = DateFormatUtil.parse(dueTimeStr);
				}
			}
		}

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

		if (StringUtil.isNotZeroEmpty(instance.getParentInstId())) {
			ContextThreadUtil.addTask(bpmTask);
		}

	}

	/**
	 * 添加意见。
	 * 
	 * @param delegateTask
	 * @param identityList void
	 */
	private void addOpinion(BpmDelegateTask delegateTask, List<BpmIdentity> identityList) {
		String ids = "";
		String names = "";
		if (BeanUtils.isNotEmpty(identityList)) {
			ids = BpmCheckOpinionUtil.getIdentityIds(identityList);
			names = BpmCheckOpinionUtil.getIdentityNames(identityList);
		}

		String instId = (String) delegateTask.getVariable(BpmConstants.PROCESS_INST_ID);
		DefaultBpmCheckOpinion bpmCheckOpinion = BpmCheckOpinionUtil.buildBpmCheckOpinion(delegateTask, instId);
		bpmCheckOpinion.setQualfieds(ids);
		bpmCheckOpinion.setQualfiedNames(names);
		
		ActionCmd cmd = ContextThreadUtil.getActionCmd();
		setSignType(bpmCheckOpinion,cmd);
		//记录当前审批记录，是由哪个任务完成产生的，作为撤回依据
        if (cmd instanceof TaskFinishCmd) {
        	bpmCheckOpinion.setParentTaskId(((TaskFinishCmd)cmd).getTaskId());
		}
		bpmCheckOpinionManager.create(bpmCheckOpinion);
	}

	private void setSignType(DefaultBpmCheckOpinion bpmCheckOpinion,ActionCmd cmd) {
		Object opinionSignType = cmd.getTransitVars(BpmConstants.OPINION_SIGN_TYPE);
		if(BeanUtils.isNotEmpty(opinionSignType)) {
			bpmCheckOpinion.setSignType(CustomSignNodeDef.BEFORE_SIGN);
		}else {
			Map<String, Object> variables = cmd.getVariables();
			if (variables.containsKey(BpmConstants.PRE_BPM_CHECK_OPINION_ID)
					&& BeanUtils.isNotEmpty(variables.get(BpmConstants.PRE_BPM_CHECK_OPINION_ID))) {
				String id = String.valueOf(variables.get(BpmConstants.PRE_BPM_CHECK_OPINION_ID));
				DefaultBpmCheckOpinion defaultBpmCheckOpinion = bpmCheckOpinionManager.get(id);
				if (BeanUtils.isNotEmpty(defaultBpmCheckOpinion)
						&& StringUtil.isNotEmpty(defaultBpmCheckOpinion.getSignType())) {
					bpmCheckOpinion.setSignType(CustomSignNodeDef.AFTER_SIGN);
				}
			}
		}
	}

}
