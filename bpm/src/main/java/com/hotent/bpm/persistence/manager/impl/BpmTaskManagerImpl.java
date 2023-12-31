package com.hotent.bpm.persistence.manager.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.exception.BaseException;
import com.hotent.base.exception.SystemException;
import com.hotent.base.feign.PortalFeignService;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.handler.MultiTenantHandler;
import com.hotent.base.handler.MultiTenantIgnoreResult;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.Direction;
import com.hotent.base.query.FieldSort;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.base.util.time.DateFormatUtil;
import com.hotent.base.util.time.TimeUtil;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.constant.ActionType;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.constant.MultiInstanceType;
import com.hotent.bpm.api.constant.NodeStatus;
import com.hotent.bpm.api.constant.OpinionStatus;
import com.hotent.bpm.api.constant.ProcessInstanceStatus;
import com.hotent.bpm.api.constant.TaskType;
import com.hotent.bpm.api.constant.TemplateConstants;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.event.NoExecutorModel;
import com.hotent.bpm.api.event.NotifyTaskModel;
import com.hotent.bpm.api.event.PushStackEvent;
import com.hotent.bpm.api.event.TaskNotifyEvent;
import com.hotent.bpm.api.helper.identity.BpmIdentityConverter;
import com.hotent.bpm.api.helper.identity.BpmIdentityExtractService;
import com.hotent.bpm.api.model.delegate.BpmDelegateTask;
import com.hotent.bpm.api.model.form.FormType;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.CustomSignNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.SignNodeDef;
import com.hotent.bpm.api.model.process.task.BpmTask;
import com.hotent.bpm.api.model.process.task.BpmTaskTurn;
import com.hotent.bpm.api.service.BpmAgentService;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.api.service.BpmFormService;
import com.hotent.bpm.api.service.BpmIdentityService;
import com.hotent.bpm.api.service.BpmTaskActionService;
import com.hotent.bpm.engine.form.BpmFormFactory;
import com.hotent.bpm.engine.task.cmd.DefaultTaskFinishCmd;
import com.hotent.bpm.helper.identity.DefaultBpmIdentityConverter;
import com.hotent.bpm.listener.TaskNotifyEventListener;
import com.hotent.bpm.model.form.FormModel;
import com.hotent.bpm.model.identity.DefaultBpmIdentity;
import com.hotent.bpm.natapi.inst.NatProInstanceService;
import com.hotent.bpm.natapi.task.NatTaskService;
import com.hotent.bpm.persistence.dao.BpmTaskDao;
import com.hotent.bpm.persistence.manager.ActExecutionManager;
import com.hotent.bpm.persistence.manager.ActTaskManager;
import com.hotent.bpm.persistence.manager.BpmCheckOpinionManager;
import com.hotent.bpm.persistence.manager.BpmCustomSignDataManager;
import com.hotent.bpm.persistence.manager.BpmDefAuthorizeManager;
import com.hotent.bpm.persistence.manager.BpmDefinitionManager;
import com.hotent.bpm.persistence.manager.BpmExeStackManager;
import com.hotent.bpm.persistence.manager.BpmProStatusManager;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.persistence.manager.BpmReadRecordManager;
import com.hotent.bpm.persistence.manager.BpmSecretaryManageManager;
import com.hotent.bpm.persistence.manager.BpmSignDataManager;
import com.hotent.bpm.persistence.manager.BpmTaskCandidateManager;
import com.hotent.bpm.persistence.manager.BpmTaskManager;
import com.hotent.bpm.persistence.manager.BpmTaskTurnManager;
import com.hotent.bpm.persistence.model.ActTask;
import com.hotent.bpm.persistence.model.BpmCustomSignData;
import com.hotent.bpm.persistence.model.BpmDefAuthorizeType.BPMDEFAUTHORIZE_RIGHT_TYPE;
import com.hotent.bpm.persistence.model.BpmExeStack;
import com.hotent.bpm.persistence.model.BpmReadRecord;
import com.hotent.bpm.persistence.model.BpmSecretaryManage;
import com.hotent.bpm.persistence.model.BpmSignData;
import com.hotent.bpm.persistence.model.DefaultBpmCheckOpinion;
import com.hotent.bpm.persistence.model.DefaultBpmDefinition;
import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;
import com.hotent.bpm.persistence.model.DefaultBpmTask;
import com.hotent.bpm.persistence.model.DefaultBpmTaskCandidate;
import com.hotent.bpm.persistence.model.PushStackModel;
import com.hotent.bpm.persistence.model.ResultMessage;
import com.hotent.bpm.persistence.util.BpmUtil;
import com.hotent.bpm.util.BpmCheckOpinionUtil;
import com.hotent.bpm.util.MessageUtil;
import com.hotent.bpm.util.PortalDataUtil;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IGroup;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.model.IdentityType;
import com.hotent.uc.api.service.IUserGroupService;
import com.hotent.uc.api.service.IUserService;

@Service("bpmTaskManager")
public class BpmTaskManagerImpl extends BaseManagerImpl<BpmTaskDao, DefaultBpmTask> implements BpmTaskManager {

	@Resource
	BpmTaskCandidateManager bpmTaskCandidateManager;
	@Resource
	IUserGroupService defaultUserGroupService;
	@Resource
	IUserService userServiceImpl;
	@Resource
	ActTaskManager actTaskManager;
	@Resource
	BpmDefinitionManager bpmDefinitionManager;
	@Resource
	BpmDefinitionAccessor bpmDefinitionAccessor;
	@Resource
	BpmSignDataManager bpmSignDataManager;
	@Resource
	NatProInstanceService natProInstanceService;
	@Resource
	BpmIdentityExtractService bpmIdentityExtractService;
	@Resource
	BpmAgentService bpmAgentService;
	@Resource
	BpmTaskTurnManager bpmTaskTurnManager;
	@Resource
	BpmDefAuthorizeManager bpmDefAuthorizeManager;
	@Resource
	BpmIdentityService bpmIdentityService;
	@Resource
	NatTaskService natTaskService;
	@Resource
	ActExecutionManager actExecutionManager;
	@Resource
	BpmCheckOpinionManager bpmCheckOpinionManager;
	@Resource
	BpmProcessInstanceManager bpmProcessInstanceManager;
	@Resource
	BpmProStatusManager bpmProStatusManager;
	@Resource
	PortalFeignService PortalFeignService;
	@Resource
	BpmSecretaryManageManager bpmSecretaryManageManager;
	@Resource
	BpmCustomSignDataManager bpmCustomSignDataManager;
	@Resource
	BpmTaskActionService bpmTaskActionService;
	@Resource
	DefaultBpmIdentityConverter defaultBpmIdentityConverter;
	@Resource
	BpmExeStackManager bpmExeStackManager;
	@Resource
	BpmReadRecordManager bpmReadRecordManager;

	private Map<String, Object> actRightMap;


	@Override
    @Transactional
	public CommonResult<String> retrieveBpmTask(IUser user, String taskId) {
		BpmTaskTurn bpmTaskTurn = bpmTaskTurnManager.getByTaskId(taskId);
		if (BeanUtils.isNotEmpty(bpmTaskTurn)) {
			if ("finish".equals(bpmTaskTurn.getStatus())) {
				return new CommonResult<String>(false, "收回失败，任务已处理");
			}
			DefaultBpmTask defaultBpmTask = super.get(taskId);
			String defId = defaultBpmTask.getProcDefId();
			DefaultBpmDefinition def = bpmDefinitionManager.getById(defId);
	        String isReadRevoke = def.getIsReadRevoke();
	        // 不允许已阅撤回
	        if("false".equals(isReadRevoke)) {
	        	List<BpmReadRecord> bpmReadRecord = bpmReadRecordManager.getByTaskIdandrecord(taskId, bpmTaskTurn.getAssigneeId());
	        	if(BeanUtils.isNotEmpty(bpmReadRecord)) {
	        		return new CommonResult<String>(false, "当前流程不允许已阅撤回");
	        	}
	    	}

			Map<String, Object> map = new HashMap<>();
			map.put("assigneeId", user.getUserId());
			map.put("assigneeName", user.getFullname());
			map.put("taskId", taskId);
			baseMapper.retrieveBpmTask(map);
			bpmTaskTurnManager.delByTaskId(taskId);
			bpmCheckOpinionManager.retrieveBpmTask(map);
			return new CommonResult<String>(true, "收回成功");
		} else {
			return new CommonResult<String>(false, "任务不存在，请联系管理员");
		}

	}

	@Override
	public DefaultBpmTask getByRelateTaskId(String relateTaskId) {
		return baseMapper.getByRelateTaskId(relateTaskId);
	}

	@Override
    @Transactional
	public void delByRelateTaskId(String relateTaskId) {
		bpmTaskCandidateManager.removeByTaskId(relateTaskId);
		baseMapper.removeByTaskId(relateTaskId);
	}

	@Override
	public PageList<DefaultBpmTask> getByUserId(String userId) {
		// 自动查询待办的总条数时性能比较慢，所以指定不查询总条数
		PageBean pageBean = new PageBean();
		pageBean.setShowTotal(false);
		IPage<DefaultBpmTask> result = baseMapper.getByUserId(convert2IPage(pageBean), convertGroupList(userId), convert2Wrapper(QueryFilter.build(), currentModelClass()));
		// 没有查询条件，所以直接查询待办总数
		List<Map<String, Object>> countByUserId = getCountByUserId(userId);
		result.setTotal(getTotalCount(countByUserId));
		return new PageList<DefaultBpmTask>(result);
	}

	// 根据用户ID或账号获取用户所属的组,这里不对组类别进行区分，返回统一的用户组列表，可能包括角色，部门等
	private Map<String, String> convertGroupList(String userId) {
		List<IGroup> list = defaultUserGroupService.getGroupsByUserIdOrAccount(userId);
		Map<String, String> map = new HashMap<String, String>();
		map.put("user", userId);
		if (BeanUtils.isEmpty(list))
			return map;
		for (IGroup group : list) {
			String type = group.getGroupType();
			if (map.containsKey(type)) {
				String groupId = map.get(type);
				groupId += ",'" + group.getGroupId() + "'";
				map.put(type, groupId);
			} else {
				map.put(type, "'" + group.getGroupId() + "'");
			}
		}
		return map;
	}

	@Override
	public PageList<DefaultBpmTask> getByUserId(String userId, QueryFilter<DefaultBpmTask> queryFilter) {
		Class<DefaultBpmTask> currentModelClass = currentModelClass();
		Wrapper<DefaultBpmTask> convert2Wrapper = convert2Wrapper(queryFilter, currentModelClass);
		PageBean pageBean = queryFilter.getPageBean();
		// 自动查询待办的总条数时性能比较慢，所以指定不查询总条数
		pageBean.setShowTotal(false);
		Map<String, String> map = convertGroupList(userId);
		IPage<DefaultBpmTask> list = baseMapper.getByUserId(convert2IPage(pageBean), map, convert2Wrapper);

		// 1.查询总数不需要排序（排序会导致查询慢）
		queryFilter.setSorter(new ArrayList<>());

		// 2.重新生成wrapper对象
		Wrapper<DefaultBpmTask> countWrapper = convert2Wrapper(queryFilter, currentModelClass);

		// 3.查询待办总数
		Long total = baseMapper.getCountByUserIdWithWhere(map, countWrapper);

		list.setTotal(total);
		return new PageList<>(list);
	}

    @Override
    public PageList<DefaultBpmTask> getLeaderByUserId(String userId, QueryFilter queryFilter) {
        Map<String, Object> leadersRigths = bpmSecretaryManageManager.getLeadersRigthMapBySecretaryId(userId,BpmSecretaryManage.RIGHT_TASK,false);
        Map<String, Object> groupMap = new HashMap<String, Object>();
        if (BeanUtils.isNotEmpty(queryFilter) && queryFilter.getParams() != null) {
            groupMap = queryFilter.getParams();
        }
        groupMap.put("rightMap", leadersRigths);
        groupMap.put("userId", ContextUtil.getCurrentUserId());
        queryFilter.setParams(new HashMap<>());
        Wrapper<DefaultBpmTask> convert2Wrapper = convert2Wrapper(queryFilter, currentModelClass());
        IPage<DefaultBpmTask> list = baseMapper.getLeaderByUserId(convert2IPage(queryFilter.getPageBean()),groupMap,convert2Wrapper);
        if (BeanUtils.isEmpty(list)) {
            return new PageList<DefaultBpmTask>();
        }
        UCFeignService ucFeignService = AppUtil.getBean(UCFeignService.class);
        ArrayNode users = ucFeignService
                .getUserByIdsOrAccounts(StringUtil.join(new ArrayList<>(leadersRigths.keySet()), ","));
        if (BeanUtils.isEmpty(users) || users.isEmpty()){
        	throw new BaseException("无秘书审批权限！");
		}
        Map<String, String> userNameMap = new HashMap<>();
        for (JsonNode jsonNode : users) {
            ObjectNode objectNode = (ObjectNode) jsonNode;
            userNameMap.put(objectNode.get("id").asText(), objectNode.get("fullname").asText());
        }
        for (DefaultBpmTask task : list.getRecords()) {
            if (StringUtil.isNotEmpty(task.getLeaderIds()) && !userId.equals(task.getLeaderIds())) {
                String[] ids = task.getLeaderIds().split(",");
                Set<String> idSet = new HashSet<>(Arrays.asList(ids));
                // 如果当前用户在任务的候选人里边，不进行标记，优先当前用户处理
                if (idSet.contains(userId)) {
                    continue;
                }
                List<BpmIdentity> lIdentities = new ArrayList<>();
                for (String id : ids) {
                    lIdentities.add(new DefaultBpmIdentity(id, userNameMap.get(id), BpmIdentity.TYPE_USER));
                }
                if (TaskType.BACK.getKey().equals(task.getStatus())) {
                    task.setStatus(TaskType.BACKSHARE.getKey());
                } else {
                    task.setStatus(TaskType.SHARE.getKey());
                }
                task.setIdentityList(lIdentities);
            }
        }
        return new PageList<>(list);
    }

	@Override
	public List<Map<String,Object>> getCountByUserId(String userId) {
		Map<String, String> map = convertGroupList(userId);
		return baseMapper.getCountByUserId(map);
	}
	
	@Override
    public List<Map<String,Object>> getLeaderCountByUserId(String userId) {
        Map<String, Object> leadersRigths = bpmSecretaryManageManager.getLeadersRigthMapBySecretaryId(userId,BpmSecretaryManage.RIGHT_TASK,false);
        Map<String, Object> groupMap = new HashMap<String, Object>();
        groupMap.put("rightMap", leadersRigths);
        return baseMapper.getLeaderCountByUserId(groupMap);
    }
	
	
	@Override
	public Long getTodoCountByUserId(String userId) {
		Map<String, String> map = convertGroupList(userId);
		return baseMapper.getTodoCountByUserId(map);
	};

	// 将每种分类的待办相加，返回总待办条数
	private long getTotalCount(List<Map<String, Object>> list) {
		long total = 0L;
		for(Map<String, Object> map : list) {
			Object count = map.get("count");
			if(count!=null && count instanceof Long) {
				total += (Long)count;
			}
		}
		return total;
	}

	@Override
	public PageList<DefaultBpmTask> getByUserId(String userId, PageBean pageBean) {
		// 自动查询待办的总条数时性能比较慢，所以指定不查询总条数
		pageBean.setShowTotal(false);
		PageList<DefaultBpmTask> pageList = new PageList<>(baseMapper.getByUserId(convert2IPage(pageBean), convertGroupList(userId), null));
		// 没有查询条件，所以直接查询待办总数
		List<Map<String, Object>> countByUserId = getCountByUserId(userId);
		pageList.setTotal(getTotalCount(countByUserId));
		return pageList;
	}

	@Override
	public List<DefaultBpmTask> getAllByUserId(String userId, QueryFilter<DefaultBpmTask> queryFilter) {
		Class<DefaultBpmTask> currentModelClass = currentModelClass();
		Wrapper<DefaultBpmTask> convert2Wrapper = convert2Wrapper(queryFilter, currentModelClass);
		PageBean pageBean = queryFilter.getPageBean();
		// 自动查询待办的总条数时性能比较慢，所以指定不查询总条数
		pageBean.setShowTotal(false);
		Map<String, String> map = convertGroupList(userId);
		IPage<DefaultBpmTask> list = baseMapper.getByUserId(convert2IPage(pageBean), map, convert2Wrapper);
		return list.getRecords();
	}

	@Transactional
	private void updateExtraPropData(DefaultBpmTask bpmTask, ActionCmd cmd) {
		try {
			if (StringUtil.isEmpty(cmd.getBusData())) {
				return;
			}
			BpmFormService bpmFormService = BpmFormFactory.getFormService(FormType.PC);
			FormModel formModel = bpmFormService.getByDefId(bpmTask.getProcDefId(), bpmTask.getNodeId(),
					(BpmProcessInstance) cmd.getTransitVars(BpmConstants.PROCESS_INST), true);
			if (BeanUtils.isEmpty(formModel) || StringUtil.isEmpty(formModel.getFormExtraConf())) {
				return;
			}
			boolean isUpdatePropValue = false;
			ObjectNode busData = (ObjectNode) JsonUtil.toJsonNode(cmd.getBusData());
			ObjectNode objectNode = (ObjectNode) JsonUtil.toJsonNode(formModel.getFormExtraConf());
			for (Iterator<Entry<String, JsonNode>> iterator = objectNode.fields(); iterator.hasNext();) {
				Entry<String, JsonNode> node = iterator.next();
				ObjectNode conf = (ObjectNode) node.getValue();
				String extraValue = "";
				if (BeanUtils.isEmpty(conf) || !conf.hasNonNull("field")) {
					continue;
				}
				String[] split = conf.get("field").asText().split("\\.");
				if (split.length < 2) {
					continue;
				}
				String bodefCode = split[0];
				String attrName = split[1];
				String subFiledName = split.length == 3 ? split[2] : "";
				int subIndex = conf.hasNonNull("subIndex") ? conf.get("subIndex").asInt() : 1;

				if (!busData.hasNonNull(bodefCode) || !busData.get(bodefCode).hasNonNull(attrName)) {
					continue;
				}
				ObjectNode entData = (ObjectNode) busData.get(bodefCode);

				if (StringUtil.isEmpty(subFiledName)) {
					extraValue = entData.get(attrName).asText();
				} else {
					ArrayNode subDatas = (ArrayNode) entData.get(attrName);
					if (BeanUtils.isNotEmpty(subDatas) && subDatas.size() >= subIndex) {
						ObjectNode subData = (ObjectNode) subDatas.get(subIndex - 1);
						if (subData.hasNonNull(subFiledName)) {
							extraValue = subData.get(subFiledName).asText();
						}
					}
				}
				if (StringUtil.isNotEmpty(extraValue)) {
					isUpdatePropValue = true;
					setExtarPropValua(bpmTask, node.getKey(), extraValue);
				}
			}
			if (isUpdatePropValue) {
				//this.updateExtraProps(bpmTask);
			}
		} catch (Exception e) {
			throw new SystemException("更新待办业务扩展字段失败:" + e.getMessage());
		}
	}

	@Override
    @Transactional
	public void assignUser(BpmDelegateTask delegateTask, List<BpmIdentity> identityList) throws Exception {

		// 修改任务执行人。
		DefaultBpmTask bpmTask = getByRelateTaskId(delegateTask.getId());

		ActionCmd cmd = ContextThreadUtil.getActionCmd();
		if(cmd.getTransitVars("IsDoneUnused") != null) {
			cmd.addTransitVars(BpmConstants.CREATE_BPM_TASK, bpmTask);
		}
		// 更新表单扩展属性
		updateExtraPropData(bpmTask, cmd);
		// 设置当前执行人是否为空。
		bpmTask.setIdentityEmpty(BeanUtils.isEmpty(identityList));

		if (BeanUtils.isEmpty(identityList)) {
			ContextThreadUtil.addTask(bpmTask);
			// 没有执行人时抛出事件。
			NoExecutorModel noExcutor = NoExecutorModel.getNoExecutorModel(bpmTask.getTaskId(), bpmTask.getBpmnInstId(),
					bpmTask.getSubject(), bpmTask.getNodeId(), bpmTask.getName(), bpmTask.getBpmnDefId());
			BpmUtil.publishNoExecutorEvent(noExcutor);
			return;
		}
		// 设置任务执行人列表。
		bpmTask.setIdentityList(identityList);

		Map<String, Object> vars = delegateTask.getVariables();

		ActionType actionType = ActionType.APPROVE;
		String actionName = "start";
		String opinion = "";
		if (cmd instanceof DefaultTaskFinishCmd) {
			DefaultTaskFinishCmd finishCmd = (DefaultTaskFinishCmd) cmd;
			actionType = finishCmd.getActionType();
			actionName = finishCmd.getActionName();
			opinion = finishCmd.getApprovalOpinion();
		}
		// 将用户抽取出来。
		List<IUser> userList = bpmIdentityExtractService.extractUser(identityList);

		NotifyTaskModel model = NotifyTaskModel.getNotifyModel(bpmTask.getTaskId(), bpmTask.getBpmnInstId(),
				bpmTask.getProcInstId(), bpmTask.getSubject(), bpmTask.getNodeId(), bpmTask.getName(),
				bpmTask.getBpmnDefId(), vars, userList, actionType, actionName, opinion);

		if (identityList.size() == 1) {
			BpmIdentity identity = identityList.get(0);
			if (BpmIdentity.TYPE_USER.equals(identity.getType())) {
				handTask(bpmTask, delegateTask, vars, model);
			} else {
				// 加到候选人
				bpmTaskCandidateManager.addCandidate(bpmTask, identityList);
			}
		} else {
			bpmTaskCandidateManager.addCandidate(bpmTask, identityList);
		}
		// 添加流程任务对象到流程的线程变量。
		ContextThreadUtil.addTask(bpmTask);

		// 当用户组的人员为空时抛出事件。
		publishIdentityListWhenEmpty(bpmTask, identityList, userList);
		// 撤销时不通知执行人
		// 跳过任务时不做通知
		BpmUtil.setTaskSkip(bpmTask);
		if (!ActionType.RECOVER.equals(actionType) && !bpmTask.getSkipResult().isSkipTask()) {
			// 发布通知事件。
			publishNotifyEvent(model);
		}

		//有执行人并且是当前用户。或者任务没有执行人的时候。把当前任务id放入线程变量中。以便驳回撤回的时候可以直接处理
		if ((StringUtil.isNotEmpty(bpmTask.getAssigneeId()) && ContextUtil.getCurrentUserId().equals(bpmTask.getAssigneeId())) || "0".equals(bpmTask.getAssigneeId())) {
			ActionCmd actionCmd = ContextThreadUtil.getActionCmd();
            actionCmd.addTransitVars("revokeTaskId", bpmTask.getId());
		}
	}

	/**
	 * 根据配置设置待办表对应属性的值
	 */
	private void setExtarPropValua(DefaultBpmTask bpmTask, String prop, String value) {
		switch (prop) {
		case "prop1":
			bpmTask.setProp1(value);
			break;
		case "prop2":
			bpmTask.setProp2(value);
			break;
		case "prop3":
			bpmTask.setProp3(value);
			break;
		case "prop4":
			bpmTask.setProp4(value);
			break;
		case "prop5":
			bpmTask.setProp5(value);
			break;
		case "prop6":
			bpmTask.setProp6(value);
			break;
		default:
			break;
		}
	}

	/**
	 * 发布任务通知事件。
	 *
	 * @param model void
	 */
	private void publishNotifyEvent(NotifyTaskModel model) {
		if (model.getActionType() == ActionType.RECOVER)
			return;
		TaskNotifyEvent ev = new TaskNotifyEvent(model);
		//AppUtil.getBean(TaskNotifyEventListener.class);
		AppUtil.publishEvent(ev);
	}

	/**
	 * 处理任务。 同时处理代理产生。
	 *
	 * @param bpmTask
	 * @param delegateTask
	 * @param vars
	 * @param model        void
	 */
    @Transactional
	private void handTask(DefaultBpmTask bpmTask, BpmDelegateTask delegateTask, Map<String, Object> vars,
			NotifyTaskModel model) {
		BpmIdentity identity = bpmTask.getIdentityList().get(0);
		// 获取任务代理人。
		IUser agent = getAgent(identity, delegateTask, vars);

		// 未获取代理人
		if (agent == null) {
			bpmTask.setAssigneeId(identity.getId());
			bpmTask.setAssigneeName(identity.getName());
			delegateTask.setAssignee(identity.getId());
		} else {
			IUser delegator = userServiceImpl.getUserById(identity.getId());

			bpmTask.setAssigneeId(agent.getUserId());
			bpmTask.setAssigneeName(agent.getFullname());
			bpmTask.setStatus(TaskType.AGENT.name());
			delegateTask.setAssignee(agent.getUserId());
			// 添加代理
			bpmTaskTurnManager.add(bpmTask, delegator, agent, "代理任务默认转办", BpmTaskTurn.TYPE_AGENT);
			model.setAgent(true);
			model.setAgent(agent);
			model.setDelegator(delegator);
		}

		delegateTask.setOwner(identity.getId());
		bpmTask.setOwnerId(identity.getId());
		bpmTask.setOwnerName(identity.getName());
		// 会签串行任务状态处理
		MultiInstanceType instanceType = delegateTask.multiInstanceType();
		if (BeanUtils.isNotEmpty(instanceType) && MultiInstanceType.SEQUENTIAL.equals(instanceType)) {// 会签串行，判断是否为加签任务
			BpmSignData signData = bpmSignDataManager.getByInstanIdAndUserId(bpmTask.getProcInstId(),
					bpmTask.getOwnerId(), bpmTask.getTaskId());
			if (BeanUtils.isNotEmpty(signData) && BpmSignData.TYPE_ADDSIGN.equals(signData.getType())) {
				bpmTask.setStatus(TaskType.ADDSIGN.getKey());
			}
		}

		update(bpmTask);
	}

	/**
	 * 获取代理人。
	 *
	 * @param identity
	 * @param delegateTask
	 * @param vars
	 * @return User
	 */
	private IUser getAgent(BpmIdentity identity, BpmDelegateTask delegateTask, Map<String, Object> vars) {
		ActionCmd cmd = ContextThreadUtil.getActionCmd();
		IUser agent = null;
		// 只有审批产生的任务才计算代理，驳回和撤销的任务不再代理。
		if (cmd instanceof DefaultTaskFinishCmd) {
			DefaultTaskFinishCmd finishCmd = (DefaultTaskFinishCmd) cmd;
			if (ActionType.APPROVE.equals(finishCmd.getActionType())) {
				// 获取任务代理人。
				agent = bpmAgentService.getAgent(identity.getId(), delegateTask, vars);
			}
		} else {
			agent = bpmAgentService.getAgent(identity.getId(), delegateTask, vars);
		}
		return agent;
	}

	/**
	 * 当用户组的人员为空时抛出事件。
	 *
	 * @param bpmTask
	 * @param list    void
	 */
	private void publishIdentityListWhenEmpty(BpmTask bpmTask, List<BpmIdentity> list, List<IUser> users) {
		if (BeanUtils.isNotEmpty(users))
			return;
		NoExecutorModel noExcutor = NoExecutorModel.getNoExecutorModel(bpmTask.getTaskId(), bpmTask.getBpmnInstId(),
				bpmTask.getSubject(), bpmTask.getNodeId(), bpmTask.getName(), bpmTask.getBpmnDefId());
		noExcutor.setIdentifyList(list);
		BpmUtil.publishNoExecutorEvent(noExcutor);
	}

	@Override
	public List<DefaultBpmTask> getByInstId(String instId) {
		return baseMapper.getByInstId(instId);
	}

	@Override
	public List<DefaultBpmTask> getByExeIdAndNodeId(String instId, String nodeId) {
		return baseMapper.getByExeIdAndNodeId(instId, nodeId);
	}

	@Override
	public List<DefaultBpmTask> getByInstUser(String instId, String userId) {
		return baseMapper.getByInstUser(instId, userId);
	}

	/**
	 * 根据任务获取所有人。
	 */
	@Override
	public List<BpmIdentity> getIdentitysByTaskId(String taskId) {
		return Collections.emptyList();
	}

	@SuppressWarnings("unchecked")
	@Override
    @Transactional
	public ResultMessage addSignTask(String taskId, String[] aryUsers) throws Exception {
		if (aryUsers == null || aryUsers.length == 0) {
			throw new BaseException("没有指定执行人!");
		}

		BpmTask bpmTask = super.get(taskId);
		String bpmnTaskId = bpmTask.getTaskId();

		ActTask actTask = actTaskManager.get(bpmnTaskId);

		BpmNodeDef nodeDef = bpmDefinitionAccessor.getBpmNodeDef(bpmTask.getProcDefId(), actTask.getTaskDefKey());

		if (!(nodeDef instanceof SignNodeDef))
			throw new BaseException("当前节点不是会签节点!");

		SignNodeDef signNodeDef = (SignNodeDef) nodeDef;

		String actInstId = actTask.getProcInstId();
		String executionId = actTask.getExecutionId();
		String nodeId = nodeDef.getNodeId();
		String instId = bpmTask.getProcInstId();

		List<BpmSignData> signDataList = bpmSignDataManager.getVoteByExecuteNode(bpmTask.getExecId(), nodeId, 1);
		if (BeanUtils.isEmpty(signDataList)) {
			throw new BaseException("没有会签数据!");
		}

		List<BpmIdentity> users = getCanAddUsers(signDataList, aryUsers);
		if (BeanUtils.isEmpty(users)) {
			throw new BaseException("指定的人员已存在!");
		}

		int userAmount = users.size();

		Integer nrOfInstances = (Integer) natProInstanceService.getVariable(executionId,
				BpmConstants.NUMBER_OF_INSTANCES);

		if (nrOfInstances != null) {
			natProInstanceService.setVariable(executionId, BpmConstants.NUMBER_OF_INSTANCES,
					nrOfInstances + userAmount);
		}

		List<Map<String, String>> taskIds = new ArrayList<Map<String, String>>();
		// 并行
		if (signNodeDef.isParallel()) {
			Integer loopCounter = nrOfInstances - 1;
			// 添加活动的线程个数
			Integer nrOfActiveInstances = (Integer) natProInstanceService.getVariable(executionId,
					BpmConstants.NUMBER_OF_ACTIVE_INSTANCES);
			natProInstanceService.setVariable(executionId, BpmConstants.NUMBER_OF_ACTIVE_INSTANCES,
					nrOfActiveInstances + userAmount);
			for (int i = 0; i < userAmount; i++) {
				BpmIdentity bpmIdentity = users.get(i);
				// 创建流程引擎任务。
				ActTask newActTask = actTaskManager.createTask(taskId, bpmIdentity.getId());
				// 添加审核意见
				DefaultBpmTask signBpmTask = super.get(newActTask.getId());
				signBpmTask.setStatus(TaskType.ADDSIGN.getKey());
				signBpmTask.setExecId(bpmTask.getExecId());
				super.update(signBpmTask);
				this.addSignCheckOpinion(signBpmTask, OpinionStatus.AWAITING_CHECK, bpmIdentity.getId());
				Map<String, String> map = new HashMap<String, String>();
				map.put("userId", bpmIdentity.getId());
				map.put("taskId", signBpmTask.getTaskId());
				taskIds.add(map);

				String newExecutionId = newActTask.getExecutionId();
				Integer index = (Integer) (loopCounter + i + 1);

				natProInstanceService.setVariableLocal(newExecutionId, BpmConstants.NUMBER_OF_LOOPCOUNTER, index);
				natProInstanceService.setVariableLocal(newExecutionId, BpmConstants.ASIGNEE, bpmIdentity);

				bpmSignDataManager.addSignData(bpmTask.getProcDefId(), instId, actInstId, bpmTask.getExecId(), nodeId,
						signBpmTask.getTaskId(), bpmIdentity.getId(), bpmIdentity.getName(), index.shortValue(),
						BpmSignData.TYPE_ADDSIGN);
			}
		}
		// 串行。
		else {
			String varName = BpmConstants.SIGN_USERIDS + nodeId;
			List<BpmIdentity> addList = new ArrayList<BpmIdentity>();

			for (int i = 0; i < userAmount; i++) {
				Integer index = nrOfInstances + i;
				BpmIdentity bpmIdentity = users.get(i);
				bpmSignDataManager.addSignData(bpmTask.getProcDefId(), instId, actInstId, bpmTask.getExecId(), nodeId,
						bpmTask.getTaskId(), bpmIdentity.getId(), bpmIdentity.getName(), index.shortValue(),
						BpmSignData.TYPE_ADDSIGN);
				addList.add(bpmIdentity);
				Map<String, String> map = new HashMap<String, String>();
				map.put("userId", bpmIdentity.getId());
				map.put("taskId", bpmTask.getTaskId());
				taskIds.add(map);
			}
			// 修改串行的流程变量。
			List<BpmIdentity> list = (List<BpmIdentity>) natProInstanceService.getVariable(executionId, varName);
			list.addAll(addList);
			natProInstanceService.setVariable(executionId, varName, list);
		}

		ResultMessage rtnMessage = ResultMessage.getSuccess("加签成功!");
		rtnMessage.addVariable("actTask", actTask);
		rtnMessage.addVariable("users", users);
		rtnMessage.addVariable("taskIds", taskIds);

		return rtnMessage;
	}

	/**
	 * 添加加签任务意见。
	 *
	 * @param bpmTask
	 * @param opinionStatus void
	 * @param toUser        增加任务的那个人
	 */
    @Transactional
	private void addSignCheckOpinion(DefaultBpmTask bpmTask, OpinionStatus opinionStatus, String toUser) {

		// 如果是流转中的人添加意见，则办理人为那个人
		IUser user = BpmUtil.getUser(toUser);
		List<BpmIdentity> identityList = new ArrayList<BpmIdentity>();
		BpmIdentity bpmIdentity = new DefaultBpmIdentity();
		bpmIdentity.setType(IdentityType.USER);
		bpmIdentity.setId(user.getUserId());
		bpmIdentity.setName(user.getFullname());
		identityList.add(bpmIdentity);
		String signType = null;
		String status = bpmTask.getStatus();
		if(TaskType.SIGNLINEED.getKey().equals(status)) {
			signType = CustomSignNodeDef.SIGNTYPE_PARALLEL;
		}
		if(TaskType.SIGNSEQUENCEED.getKey().equals(status)) {
			signType = CustomSignNodeDef.SIGNTYPE_SEQUENTIAL;
		}
		if(TaskType.APPROVELINEED.getKey().equals(status)) {
			signType = CustomSignNodeDef.SIGNTYPE_PARALLELAPPROVE;
		}

		DefaultBpmCheckOpinion checkOpinion = new DefaultBpmCheckOpinion();
		checkOpinion.setId(UniqueIdUtil.getSuid());
		checkOpinion.setProcDefId(bpmTask.getBpmnDefId());
		checkOpinion.setProcInstId(bpmTask.getProcInstId());
		checkOpinion.setSignType(signType);
		checkOpinion.setTaskId(bpmTask.getTaskId());
		checkOpinion.setTaskKey(bpmTask.getNodeId());
		checkOpinion.setTaskName(bpmTask.getName());
		checkOpinion.setStatus(opinionStatus.getKey());

		checkOpinion.setCreateTime(LocalDateTime.now());
		checkOpinion.setQualfieds(BpmCheckOpinionUtil.getIdentityIds(identityList));
		checkOpinion.setQualfiedNames(user.getFullname());
		checkOpinion.setAuditor(user.getUserId());
		checkOpinion.setAuditorName(user.getFullname());

		bpmCheckOpinionManager.create(checkOpinion);
	}

	/**
	 * 获取可以进行补签的人员。
	 *
	 * @param list
	 * @param aryUsers
	 * @return List&lt;BpmIdentity>
	 */
	private List<BpmIdentity> getCanAddUsers(List<BpmSignData> list, String[] aryUsers) {
		List<BpmIdentity> rtnList = new ArrayList<BpmIdentity>();
		List<String> userList = new ArrayList<String>();

		for (BpmSignData signData : list) {
			// 会签任务审批后 撤回 允许将自己加签回来
			if ("no".equals(signData.getVoteResult())) {
				userList.add(signData.getQualifiedId());
			}
		}

		for (String userId : aryUsers) {
			if (userList.contains(userId))
				continue;
			IUser user = userServiceImpl.getUserById(userId);
			BpmIdentity identity = DefaultBpmIdentity.getIdentityByUserId(userId, user.getFullname());
			rtnList.add(identity);
		}
		return rtnList;
	}

	/**
	 * 按用户ID，实例Id 用户组列表查找任务
	 *
	 * @param bpmnInstId
	 * @param userId
	 * @param groupList
	 * @return
	 */
	public List<DefaultBpmTask> getByBpmInstIdUserIdGroupList(String bpmnInstId, String userId,
			List<IGroup> groupList) {
		return baseMapper.getByBpmInstIdUserIdGroupList(bpmnInstId, userId, groupList);
	}

	@Override
    @Transactional
	public void lockTask(String taskId, String userId) {
		DefaultBpmCheckOpinion opinion = new DefaultBpmCheckOpinion();
		DefaultBpmTask defaultBpmTask = super.get(taskId);
		opinion.setAuditor(userId);
		opinion.setAuditorName(ContextUtil.getCurrentUser().getFullname());
		opinion.setQualfiedNames(ContextUtil.getCurrentUser().getFullname());
		opinion.setStatus(OpinionStatus.LOCK_TASK.getKey());
		opinion.setOpinion(OpinionStatus.LOCK_TASK.getValue()+"任务");
		opinion.setProcDefId(defaultBpmTask.getProcDefId());
		opinion.setProcInstId(defaultBpmTask.getProcInstId());
		opinion.setTaskName(defaultBpmTask.getName());
		opinion.setCreateTime(LocalDateTime.now());
		opinion.setCompleteTime(LocalDateTime.now());
		bpmCheckOpinionManager.create(opinion);
		baseMapper.updateAssigneeOwnerId(taskId, userId, userId);
	}

	@Override
    @Transactional
	public void unLockTask(String taskId) {
		// 设置其值为0，即表示为空，用0值方便数据库索引建立及提交查询速度
		baseMapper.updateAssigneeOwnerId(taskId, BpmConstants.EmptyUser, BpmConstants.EmptyUser);
	}

	@Override
    @Transactional
	public void assignTask(String taskId, String assigneeId) {
		// 修改执行人发布事件。
		DefaultBpmTask bpmTask = super.get(taskId);
		baseMapper.updateAssigneeById(taskId, assigneeId);
		natTaskService.setAssignee(bpmTask.getTaskId(), assigneeId);

	}

	@Override
    @Transactional
	public void delByInstList(List<String> instList) {
		baseMapper.delByInstList(instList);
	}

	@Override
    @Transactional
	public void delByParentId(String parentId) {
		List<String> ids = baseMapper.getIdsByParentId(parentId);
		if (BeanUtils.isNotEmpty(ids) && ids.size() > 0) {
			this.removeByIds(ids.toArray(new String[ids.size()]));
		}
	}

	@Override
	public List<DefaultBpmTask> getChildsByTaskId(String taskId) {
		List<DefaultBpmTask> list = baseMapper.getByParentId(taskId);
		List<DefaultBpmTask> rtnList = new ArrayList<DefaultBpmTask>();
		for (DefaultBpmTask task : list) {
			getByParentId(task, rtnList);
		}
		return rtnList;
	}

	private void getByParentId(DefaultBpmTask task, List<DefaultBpmTask> rtnList) {
		rtnList.add(task);
		List<DefaultBpmTask> list = baseMapper.getByParentId(task.getId());
		if (BeanUtils.isEmpty(list))
			return;

		for (DefaultBpmTask tmp : list) {
			getByParentId(tmp, rtnList);
		}
	}

	@Override
    @Transactional
	public void createTask(BpmProcessInstance instance) {
		ActTask actTask = new ActTask();
		actTask.setId(UniqueIdUtil.getSuid());
		actTask.setRev(1);
		actTask.setExecutionId(instance.getBpmnInstId());
		actTask.setProcInstId(instance.getBpmnInstId());

		actTask.setAssignee(instance.getCreateBy());

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DefaultBpmTask> queryList(QueryFilter queryFilter) throws Exception {
		// 增加分管授权查询判断
		IUser user = ContextUtil.getCurrentUser();
		String userId = user.getUserId();

		boolean isAdmin = user.isAdmin();

		queryFilter.addParams("isAdmin", isAdmin ? 1 : 0);

		if (!isAdmin) {
			actRightMap = bpmDefAuthorizeManager.getActRightByUserId(userId, BPMDEFAUTHORIZE_RIGHT_TYPE.TASK, true,
					true);
			// 获得分管授权与用户相关的信息集合的流程KEY
			String defKeys = (String) actRightMap.get("defKeys");
			if (StringUtil.isNotEmpty(defKeys)) {
				queryFilter.addParams("defKeys", defKeys);
			}
		}

		// 查询列表
		List<DefaultBpmTask> list = (List<DefaultBpmTask>) this.query(queryFilter);

		return convertInfo(list);
	}

	public List<DefaultBpmTask> convertInfo(List<DefaultBpmTask> list) throws Exception {
		if (list == null)
			return list;

		for (DefaultBpmTask bpmTask : list) {
//			//处理用户的数据
//			if(StringUtil.isNotZeroEmpty(bpmTask.getOwnerId()))
//				bpmTask.setOwnerName(getUserFullName(bpmTask.getOwnerId()));
//			//处理执行人用户的数据
//			if(StringUtil.isNotZeroEmpty(bpmTask.getAssigneeId()))
//				bpmTask.setAssigneeName(getUserFullName(bpmTask.getAssigneeId()));
//			//处理用户的数据
//			if(StringUtil.isNotZeroEmpty(bpmTask.getCreatorId()))
//				bpmTask.setCreator(getUserFullName(bpmTask.getCreatorId()));

			List<BpmIdentity> identityList = bpmIdentityService.queryListByBpmTask(bpmTask);
			bpmTask.setIdentityList(identityList);

			// 计算已用时间
			if (bpmTask.getDueTaskTime() > 0) {
				int dueUseTaskTime = -1;
				if ("caltime".equals(bpmTask.getDueDateType())) {
					// getSecondDiff 秒
					dueUseTaskTime = TimeUtil.getSecondDiff(LocalDateTime.now(), bpmTask.getCreateTime()) / 60;
				} else {
					List<IUser> extractUser = bpmIdentityExtractService.extractUser(identityList);
					if (BeanUtils.isNotEmpty(extractUser) && extractUser.size() == 1) {
						IUser iUser = extractUser.get(0);
						// getWorkTimeByUser 毫秒
						ObjectNode params = JsonUtil.getMapper().createObjectNode();
						params.put("userId", iUser.getUserId());
						params.put("startTime", DateFormatUtil.formaDatetTime(bpmTask.getCreateTime()));
						params.put("endTime", DateFormatUtil.formaDatetTime(LocalDateTime.now()));
						dueUseTaskTime = (int) (PortalFeignService.getWorkTimeByUser(params) / 60000);
					}
				}
				bpmTask.setDueUseTaskTime(dueUseTaskTime);
			}

		}
		return list;
	}

	@Override
	public List<DefaultBpmTask> getReminderTask() {
		return baseMapper.getReminderTask();
	}

	/**
	 * 根据任务ID终止流程。
	 *
	 * <pre>
	 * 1.根据任务ID查询到BPM_TASK记录。
	 * 2.发送通知消息，通知相关人员。
	 * 	如果传入了通知类型，那么就是用传入的通知类型，如果没有则获取节点的通知类型。
	 * 3.删除bpm_task_candidate对应记录。
	 * 4.删除BPM_TASK记录。
	 * 5.删除act_ru_identitylink记录。
	 * 6.删除act_ru_task记录。
	 * 7.删除act_ru_execution的记录.
	 * 	这里需要查询是否为外部子流程。如果为外部子流程，则需要删除主流程的Execution信息。
	 * 8.标记bpm_pro_inst，bpm_pro_inst_hi为人工终止。
	 * 9.在bpm_check_opinion 将对应的状态更新为人工终止。
	 * </pre>
	 *
	 * @param taskId void
	 * @throws Exception
	 */
	@Override
    @Transactional
	public void endProcessByTaskId(String taskId, String informType, String cause, String files) throws Exception {
		BpmTask bpmTask = super.get(taskId);

		String InstId = bpmTask.getProcInstId();

		String topInstId = bpmCheckOpinionManager.getTopInstId(InstId);

		List<String> instList = bpmCheckOpinionManager.getListByInstId(topInstId);

		// 删除流程数据。
		actExecutionManager.delByInstList(instList);
		// 删除关联的实例。
		actExecutionManager.remove(InstId);
		// 删除候选人和任务
		// this.delByRelateTaskId(taskId);
		// 删除候选人数据
		bpmTaskCandidateManager.delByInstList(instList);
		// 任务转办代理
		bpmTaskTurnManager.delByInstList(instList);
		// 会签数据
		bpmSignDataManager.delByInstList(instList);
		// 再删除任务
		baseMapper.delByInstList(instList);
		// 删除会签任务

		// 更新实例的状态
		DefaultBpmProcessInstance instance = bpmProcessInstanceManager.get(InstId);
		instance.setStatus(ProcessInstanceStatus.STATUS_MANUAL_END.getKey());
		instance.setEndTime(LocalDateTime.now());
		instance.setDuration(TimeUtil.getTime(LocalDateTime.now(), instance.getCreateTime()));
		bpmProcessInstanceManager.update(instance);
		// bpmProcessInstanceManager.updateStatusByInstanceId(InstId,ProcessInstanceStatus.STATUS_MANUAL_END.getKey());
		// 更新正在审批的审批意见的状态
		List<DefaultBpmCheckOpinion> litCheckOpinions = bpmCheckOpinionManager.getByInstId(InstId);
		if (litCheckOpinions != null) {
			for (DefaultBpmCheckOpinion defaultBpmCheckOpinion : litCheckOpinions) {
				if (!defaultBpmCheckOpinion.getStatus().equals("awaiting_check"))
					continue;
				defaultBpmCheckOpinion.setCompleteTime(LocalDateTime.now());
				defaultBpmCheckOpinion.setAuditor(
						BeanUtils.isNotEmpty(ContextUtil.getCurrentUser()) ? ContextUtil.getCurrentUser().getUserId()
								: "-1");
				defaultBpmCheckOpinion.setAuditorName(
						BeanUtils.isNotEmpty(ContextUtil.getCurrentUser()) ? ContextUtil.getCurrentUser().getFullname()
								: "系统执行人");
				defaultBpmCheckOpinion.setStatus(OpinionStatus.MANUAL_END.getKey());
				defaultBpmCheckOpinion
						.setDurMs(TimeUtil.getTime(LocalDateTime.now(), defaultBpmCheckOpinion.getCreateTime()));
				String opinion = StringUtil.isNotEmpty(defaultBpmCheckOpinion.getOpinion())
						? defaultBpmCheckOpinion.getOpinion() + "|" + cause
						: cause;
				defaultBpmCheckOpinion.setOpinion(opinion);
				defaultBpmCheckOpinion.setFiles(files);
				bpmCheckOpinionManager.update(defaultBpmCheckOpinion);
				// 更新节点状态
				NodeStatus nanualEndStatus = NodeStatus.MANUAL_END;
				bpmProStatusManager.createOrUpd(instance.getId(), instance.getBpmnDefId(),
						defaultBpmCheckOpinion.getTaskKey(), defaultBpmCheckOpinion.getTaskName(), nanualEndStatus);

			}
		}
		// bpmCheckOpinionManager.updStatusByWait(InstId,null,
		// OpinionStatus.MANUAL_END.getKey());

		// 4.通知相关人员。
		// 执行过该任务的相关人员 暂时只通知发起人
		BpmProcessInstance processInstance = bpmProcessInstanceManager.get(topInstId);
		List<IUser> userList = new ArrayList<IUser>();
		if (topInstId != null) {
			IUser user = BpmUtil.getUser(processInstance.getCreateBy(), processInstance.getCreator());
			userList.add(user);
		}

		Map<String, Object> vars = getVars(bpmTask, cause);
		// 发送通知
		MessageUtil.sendMsg(TemplateConstants.TYPE_KEY.BPM_END_PROCESS, informType, userList, vars);

	}

	private Map<String, Object> getVars(BpmTask task, String cause) {
		String baseUrl = PortalDataUtil.getPropertyByAlias(TemplateConstants.TEMP_VAR.BASE_URL);
		IUser user = ContextUtil.getCurrentUser();
		Map<String, Object> map = new HashMap<String, Object>();
		// 处理人
		map.put(TemplateConstants.TEMP_VAR.DELEGATE, BeanUtils.isNotEmpty(user) ? user.getFullname() : "系统执行人");
		map.put(TemplateConstants.TEMP_VAR.NODE_NAME, task.getName());
		// 任务标题
		map.put(TemplateConstants.TEMP_VAR.TASK_SUBJECT, task.getSubject());
		map.put(TemplateConstants.TEMP_VAR.INST_SUBJECT, task.getSubject());

		map.put(TemplateConstants.TEMP_VAR.BASE_URL, baseUrl);
		// 意见
		map.put(TemplateConstants.TEMP_VAR.CAUSE, cause);
		// 流程实例Id
		map.put(TemplateConstants.TEMP_VAR.INST_ID, task.getProcInstId());

		return map;
	}

	@Override
	public List<DefaultBpmTask> getByInstList(List<String> instIds) {
		return baseMapper.getByInstList(instIds);
	}

	@Override
	public PageList<DefaultBpmTask> getMyTransTask(String userId, QueryFilter queryFilter) {
		if (BeanUtils.isEmpty(queryFilter)) {
			queryFilter = QueryFilter.build().withDefaultPage();
		}
		queryFilter.withParam("userId", userId);
		return baseMapper.getMyTransTask(convert2IPage(queryFilter.getPageBean()),queryFilter.getParams());
	}

	@Override
    @Transactional
	public void delegate(String taskId, String toUser) {
		DefaultBpmTask bpmTask = super.get(taskId);
		bpmTask.setAssigneeId(toUser);
		bpmTask.setStatus(TaskType.DELIVERTO.name());
		super.update(bpmTask);
		natTaskService.setAssignee(bpmTask.getTaskId(), toUser);

	}

	// 0:任务已经处理,1:可以锁定,2:不需要解锁 ,3:可以解锁，4,被其他人锁定,5:这种情况一般是管理员操作，所以不用出锁定按钮。
	@Override
	public int canLockTask(String taskId) {
		return canLockTask(taskId, "");
	}

	// 0:任务已经处理,1:可以锁定,2:不需要解锁 ,3:可以解锁，4,被其他人锁定,5:这种情况一般是管理员操作，所以不用出锁定按钮。
	@Override
	public int canLockTask(String taskId, String curUserId) {
		IUser currentUser = ContextUtil.getCurrentUser();
		if (StringUtil.isZeroEmpty(curUserId)) {
			curUserId = currentUser.getUserId();
		}
		BpmTask task = super.get(taskId);
		if (task == null) {
			return 0;
		}
		if (currentUser.isAdmin()) {
			return 5;
		}
		String assigneeId = task.getAssigneeId();

		List<DefaultBpmTaskCandidate> list = bpmTaskCandidateManager.queryByTaskId(taskId);

		// 任务执行人为空则可以进行锁定。
		if (BpmConstants.EmptyUser.equals(assigneeId)) {
			if (BeanUtils.isEmpty(list) || currentUser.isAdmin()) {
				return 1;
			}
			// 判断是否有候选人
			boolean isExist = isInCandidate(list, curUserId);
			return (isExist) ? 1 : 5;
		}
		// 执行人和当前人一致。
		else if (curUserId.equals(assigneeId)) {
			// 判断是否有候选人
			// 不存在候选人，则不需要解锁。
			if (BeanUtils.isEmpty(list)) {
				return 2;
			}
			// 存在候选人， 并且是超级管理员
			else if (currentUser.isAdmin()) {
				return 3;
			}
			// 存在候选人，则可以解锁。
			else {
				boolean isExist = isInCandidate(list, curUserId);
				return (isExist) ? 3 : 5;
			}
		}
		// 被其他人锁定。
		return 4;
	}

	@Override
	public boolean canAccessTask(String taskId, String userId) {
		BpmTask task = (BpmTask) this.get(taskId);
		if (task.getAssigneeId().equals(userId))
			return true;
		// 判断候选人
		List<DefaultBpmTaskCandidate> candidateList = bpmTaskCandidateManager.queryByTaskId(taskId);

		return isInCandidate(candidateList, userId);
	}

	/**
	 * 人是否在候选人中。
	 *
	 * @param candidateList
	 * @param userId
	 * @return
	 */
	private boolean isInCandidate(List<DefaultBpmTaskCandidate> candidateList, String userId) {
		List<IGroup> groups = defaultUserGroupService.getGroupsByUserIdOrAccount(userId);
		Map<String, IGroup> groupMap = groupListToMap(groups);

		for (DefaultBpmTaskCandidate candidate : candidateList) {
			String executor = candidate.getExecutor();
			if (BpmIdentity.TYPE_USER.equals(candidate.getType())) {
				if (userId.equals(executor)) {
					return true;
				}
			} else {
				if (groupMap.containsKey(executor)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 将组织转换成为map。
	 *
	 * @param groups
	 * @return
	 */
	private Map<String, IGroup> groupListToMap(List<IGroup> groups) {
		Map<String, IGroup> map = new HashMap<String, IGroup>();
		if (BeanUtils.isNotEmpty(groups)) {
			for (IGroup group : groups) {
				map.put(group.getGroupId(), group);
			}
		}
		return map;

	}

	@Override
	public DefaultBpmTask getByTaskId(String taskId) throws Exception {

		DefaultBpmTask task = super.get(taskId);
		if (BeanUtils.isEmpty(task)) {
			return null;
		}
		List<DefaultBpmTask> tasks = new ArrayList<DefaultBpmTask>();
		tasks.add(task);
		convertInfo(tasks);

		return tasks.get(0);
	}

	@Override
    @Transactional
	public void setTaskExecutors(String taskId, String[] userIds, String notifyType, String opinion) throws Exception {
		DefaultBpmTask task = super.get(taskId);
		bpmTaskCandidateManager.removeByTaskId(taskId);
		// 修改意见中的审核人
		DefaultBpmCheckOpinion bpmCheckOpinion = bpmCheckOpinionManager.getByTaskId(taskId);
		List<BpmIdentity> identityList = new ArrayList<BpmIdentity>();
		BpmIdentityConverter bpmIdentityConverter = AppUtil.getBean(BpmIdentityConverter.class);
		String userRealName = "";
		int index = 0;
		for (String userId : userIds) {
			index++;
			IUser user = userServiceImpl.getUserById(userId);
			BpmIdentity bpmIdentity = bpmIdentityConverter.convertUser(user);
			bpmIdentity.setType(IdentityType.USER);
			identityList.add(bpmIdentity);
			userRealName += user.getFullname();
			if (index != userIds.length)
				userRealName += ",";

		}
		bpmCheckOpinion.setQualfiedNames(userRealName);
		bpmCheckOpinion.setQualfieds(BpmCheckOpinionUtil.getIdentityIds(identityList));
		bpmCheckOpinionManager.update(bpmCheckOpinion);

		if (BeanUtils.isEmpty(userIds)) {
			task.setAssigneeId(BpmConstants.EmptyUser);
		} else if (userIds.length == 1) {
			task.setAssigneeId(userIds[0]);
			task.setAssigneeName(userRealName);
		} else {
			task.setAssigneeId(BpmConstants.EmptyUser);
			task.setAssigneeName("");
			List<BpmIdentity> list = new ArrayList<BpmIdentity>();
			for (String userId : userIds) {
				DefaultBpmIdentity identity = new DefaultBpmIdentity();
				identity.setType(BpmIdentity.TYPE_USER);
				identity.setId(userId);
				list.add(identity);
			}
			// 添加候选人。
			bpmTaskCandidateManager.addCandidate(taskId, list);
		}
		super.update(task);

		// 发送消息。
		sendMsg(task, userIds, notifyType, opinion);
	}

	/**
	 * 发送消息。
	 *
	 * @param task
	 * @param userIds
	 * @param notifyType
	 * @param opinion
	 * @throws Exception
	 */
    @Transactional
	private void sendMsg(BpmTask task, String[] userIds, String notifyType, String opinion) throws Exception {
		if (BeanUtils.isEmpty(userIds))
			return;
		String baseUrl = PortalDataUtil.getPropertyByAlias(TemplateConstants.TEMP_VAR.BASE_URL);

		// 发送通知。
		NotifyTaskModel model = new NotifyTaskModel();
		List<IUser> userList = new ArrayList<IUser>();
		for (String userId : userIds) {
			IUser user = userServiceImpl.getUserById(userId);
			userList.add(user);
		}
		model.setIdentitys(userList);
		model.setOpinion(opinion);
		model.addVars(TemplateConstants.TEMP_VAR.BASE_URL, baseUrl)
				.addVars(TemplateConstants.TEMP_VAR.TASK_SUBJECT, task.getSubject())
				.addVars(TemplateConstants.TEMP_VAR.TASK_ID, task.getId())
				.addVars(TemplateConstants.TEMP_VAR.CAUSE, opinion);

		MessageUtil.send(model, notifyType, "bpmTaskSetExecutors");
	}

	@Override
    @Transactional
	public void updateTaskPriority(String taskId, Long priority) {
		baseMapper.updateTaskPriority(taskId, priority);

	}

	@Override
	public List<DefaultBpmTask> getByExecuteAndNodeId(String executeId, String nodeId) {
		return baseMapper.getByExecuteAndNodeId(executeId, nodeId);
	}

	@Override
    @Transactional
	public void unLockTask(String taskId, String userId) {
		baseMapper.updateAssigneeOwnerId(taskId, userId, userId);
	}

	@SuppressWarnings("unchecked")
	@Override
    @Transactional
	public ResultMessage addCustomSignTask(String taskId, String[] aryUsers, boolean isCreateSignData)
			throws Exception {
		if (aryUsers == null || aryUsers.length == 0) {
			throw new BaseException("没有指定执行人!");
		}

		BpmTask bpmTask = super.get(taskId);
		String bpmnTaskId = bpmTask.getTaskId();

		ActTask actTask = actTaskManager.get(bpmnTaskId);

		BpmNodeDef nodeDef = bpmDefinitionAccessor.getBpmNodeDef(bpmTask.getProcDefId(), actTask.getTaskDefKey());

		if (!(nodeDef instanceof CustomSignNodeDef))
			throw new BaseException("当前节点不是签署节点!");

		CustomSignNodeDef signNodeDef = (CustomSignNodeDef) nodeDef;

		String executionId = actTask.getExecutionId();
		String nodeId = nodeDef.getNodeId();

		List<BpmIdentity> users = getCanAddUsers(Arrays.asList(), aryUsers);
		if (BeanUtils.isEmpty(users)) {
			throw new BaseException("指定的人员已存在!");
		}

		int userAmount = users.size();

		Integer nrOfInstances = (Integer) natProInstanceService.getVariable(executionId,
				BpmConstants.NUMBER_OF_INSTANCES);

		if (nrOfInstances != null) {
			natProInstanceService.setVariable(executionId, BpmConstants.NUMBER_OF_INSTANCES,
					nrOfInstances + userAmount);
		}

		List<Map<String, String>> taskIds = new ArrayList<Map<String, String>>();


		String customStatus = BpmCustomSignData.STATUS_COMPLETE;

		// 并行 并签
		if (signNodeDef.isParallel()) {
			Integer loopCounter = nrOfInstances - 1;
			// 获取多实例的token的最大值
			Integer baseToken = (Integer) nrOfInstances+99;
			// 添加活动的线程个数
			Integer nrOfActiveInstances = (Integer) natProInstanceService.getVariable(executionId,
					BpmConstants.NUMBER_OF_ACTIVE_INSTANCES);
			natProInstanceService.setVariable(executionId, BpmConstants.NUMBER_OF_ACTIVE_INSTANCES,
					nrOfActiveInstances + userAmount);

			// 记录堆栈数据
			DefaultTaskFinishCmd defaultTaskFinishCmd = new DefaultTaskFinishCmd();
			ContextThreadUtil.setActionCmd(defaultTaskFinishCmd);
			BpmExeStack stack = bpmExeStackManager.getStack(bpmTask.getProcInstId(), nodeId, String.valueOf(baseToken));
			defaultTaskFinishCmd.addTransitVars(BpmConstants.PARENT_STACK, stack);

			for (int i = 0; i < userAmount; i++) {
				BpmIdentity bpmIdentity = users.get(i);
				// 创建流程引擎任务。
				ActTask newActTask = actTaskManager.createTask(taskId, bpmIdentity.getId());
				// 添加审核意见
				DefaultBpmTask signBpmTask = super.get(newActTask.getId());
				signBpmTask.setOwnerName(bpmIdentity.getName());
				signBpmTask.setAssigneeName(bpmIdentity.getName());
				signBpmTask.setExecId(bpmTask.getExecId());
				super.update(signBpmTask);
				this.addSignCheckOpinion(signBpmTask, OpinionStatus.AWAITING_CHECK, bpmIdentity.getId());
				Map<String, String> map = new HashMap<String, String>();
				map.put("userId", bpmIdentity.getId());
				map.put("taskId", signBpmTask.getTaskId());
				taskIds.add(map);
				ContextThreadUtil.putCommonVars(BpmConstants.CREATE_BPM_TASK+ContextUtil.getCurrentUserId(), signBpmTask);

				String newExecutionId = newActTask.getExecutionId();
				Integer index = (Integer) (loopCounter + i + 1);

				natProInstanceService.setVariableLocal(newExecutionId, BpmConstants.NUMBER_OF_LOOPCOUNTER, index);
				natProInstanceService.setVariableLocal(newExecutionId, BpmConstants.ASIGNEE, bpmIdentity);
				natProInstanceService.setVariableLocal(newExecutionId, BpmConstants.TOKEN_NAME, baseToken+i+1);

				// 添加签署数据
				if (isCreateSignData) {
					bpmCustomSignDataManager.addCustomSignData(signBpmTask, bpmnTaskId);
				}
				// 二次签署添加堆栈数据

				PushStackEvent pushStackEvent = new PushStackEvent(new PushStackModel(newActTask,signBpmTask));
				AppUtil.publishEvent(pushStackEvent);


			}
		}
		// 串行。 顺签
		else {
			customStatus = BpmCustomSignData.STATUS_COMPLETE;
			// 修改串行的流程变量。
			String varName = BpmConstants.SIGN_USERIDS + nodeId;
			List<BpmIdentity> list = (List<BpmIdentity>) natProInstanceService.getVariable(executionId, varName);
			Integer loopCounter = (Integer) natProInstanceService.getVariable(executionId,
					BpmConstants.NUMBER_OF_LOOPCOUNTER);
			for (int i = 0; i < userAmount; i++) {
				BpmIdentity bpmIdentity = users.get(i);
				// 需要在当前审批用户都添加
				list.add(loopCounter + 1+i, bpmIdentity);
				Map<String, String> map = new HashMap<String, String>();
				map.put("userId", bpmIdentity.getId());
				map.put("taskId", bpmTask.getTaskId());
				taskIds.add(map);


			}
			natProInstanceService.setVariable(executionId, varName, list);
		}

		ResultMessage rtnMessage = ResultMessage.getSuccess("签署成功!");
		rtnMessage.addVariable("actTask", actTask);
		rtnMessage.addVariable("users", users);
		rtnMessage.addVariable("taskIds", taskIds);
		rtnMessage.addVariable("customStatus", customStatus);

		return rtnMessage;
	}

	@Override
    @Transactional
	public ResultMessage addCustomSignTask(String taskId, String[] aryUsers) throws Exception {
		return addCustomSignTask(taskId, aryUsers, true);
	}

	@Override
    @Transactional
	public void approvalTaskRevoke(String runningTaskId, String customSignTaskId) throws Exception {
		// 更新原来的审批意见 An 撤回自己 撤回
		BpmCheckOpinionUtil.updateCheckRevoker(customSignTaskId);

		// 添加An任务
		this.addCustomSignTask(runningTaskId, new String[] { ContextUtil.getCurrentUserId() }, false);

		String createTaskId = getNewCreateTaskId();

		// 更新bpm_custom_signdata 状态 将完成状态更新为撤回审批中
		bpmCustomSignDataManager.updateStatusByTaskId(customSignTaskId, BpmCustomSignData.STATUS_COMPLETE,
				BpmCustomSignData.STATUS_WITHDRAW_APPROVAL,createTaskId);

	}



	@Override
    @Transactional
	public void sequentialTaskRevoke(String runingTaskId, String customSignTaskId) throws Exception {

		// 修改流程变量
		Integer loopcounter = (Integer) natTaskService.getVariable(runingTaskId, BpmConstants.NUMBER_OF_LOOPCOUNTER);
		if(loopcounter==0) {
			throw new BaseException("没有可撤回的任务");
		}
		natTaskService.setVariable(runingTaskId, BpmConstants.NUMBER_OF_LOOPCOUNTER, loopcounter-2);
		DefaultTaskFinishCmd cmd = getCmd(runingTaskId, "agree", OpinionStatus.RETRACTED.getValue());
		bpmTaskActionService.finishTask(cmd);
		String newCreateTaskId = getNewCreateTaskId(cmd);
		// bpmCustomSignDataManager.removeCascadeByTaskId(customSignTaskId);
		// 更新bpm_custom_signdata 状态 将完成状态更新为撤回审批中
		bpmCustomSignDataManager.updateStatusByTaskId(customSignTaskId,BpmCustomSignData.STATUS_COMPLETE,
						BpmCustomSignData.STATUS_WITHDRAW_APPROVAL,newCreateTaskId);
	}

	/**
	 * 当前任务审批后（finishTask） 产生新任务的任务id
	 * @param cmd
	 * @return
	 */
	private String getNewCreateTaskId(DefaultTaskFinishCmd cmd) {
		Object transitVars = cmd.getTransitVars(BpmConstants.CREATE_BPM_TASK);
		String newCreateTaskId = null;
		if(BeanUtils.isNotEmpty(transitVars)) {
			BpmTask bpmTask = (BpmTask) transitVars;
			newCreateTaskId = bpmTask.getId();
		}
		return newCreateTaskId;
	}

	/**
	 * 加签的新任务的任务id
	 * @return
	 */
	private String getNewCreateTaskId() {
		String createTaskId = null;
		Object commuVar = ContextThreadUtil.getCommuVar(BpmConstants.CREATE_BPM_TASK+ContextUtil.getCurrentUserId(), null);
		if(BeanUtils.isNotEmpty(commuVar)) {
			createTaskId = ((BpmTask)commuVar).getId();
		}
		return createTaskId;
	}


	// 审批命令
	private DefaultTaskFinishCmd getCmd(String taskId, String actionName, String opinion) {
		DefaultTaskFinishCmd cmd = new DefaultTaskFinishCmd();
		cmd.setTaskId(taskId);
		cmd.setActionName(actionName);
		// 已办中撤消
		cmd.addTransitVars("IsDoneUnused", true);
		// 设置表单意见。
		cmd.setApprovalOpinion(opinion);
		cmd.setDataMode(ActionCmd.DATA_MODE_BO);
		return cmd;
	}


	@Override
    @Transactional
	public void approvalTaskARevoke(String instId, String runningTaskId, String customSignTaskId ,String destNodeId) throws Exception {
		BpmCheckOpinionUtil.updateCheckRevoker(customSignTaskId);
		// A1 ... An 被撤回
		//bpmCustomSignDataManager.updateStatusByInstId(instId, Arrays.asList(BpmCustomSignData.STATUS_APPROVAL,BpmCustomSignData.STATUS_WITHDRAW_APPROVAL), BpmCustomSignData.STATUS_RETRACTED);
		bpmCustomSignDataManager.removeByInstId(instId);
		DefaultTaskFinishCmd taskCmd = getCmd(runningTaskId,"reject","");
		setRejctCmd(taskCmd,"normal",destNodeId);
		// 这时候的撤回需要记录堆栈
		taskCmd.addTransitVars(BpmConstants.RECORD_STACK, true);
		// 用于设置A的审批已经中signType 为BeforeSign
		taskCmd.addTransitVars(BpmConstants.OPINION_SIGN_TYPE, CustomSignNodeDef.BEFORE_SIGN);
		bpmTaskActionService.finishTask(taskCmd);


	}

	private void setRejctCmd(DefaultTaskFinishCmd cmd, String backHandMode, String destNodeId) {
		cmd.setDestination(destNodeId);
		// 设置流程驳回时跳转模式。
		cmd.addTransitVars(BpmConstants.BACK_HAND_MODE, backHandMode);
	}


	@Override
    @Transactional
	public void sequentialTaskARevoke(String instanceId, String taskId, String destNodeId)  throws Exception {
		bpmCustomSignDataManager.removeByInstId(instanceId);
		DefaultTaskFinishCmd taskCmd = getCmd(taskId,"reject","");
		setRejctCmd(taskCmd,"normal",destNodeId);
		// 这时候的撤回需要记录堆栈
		taskCmd.addTransitVars(BpmConstants.RECORD_STACK, true);
		// 用于设置A的审批已经中signType 为BeforeSign
		taskCmd.addTransitVars(BpmConstants.OPINION_SIGN_TYPE, CustomSignNodeDef.BEFORE_SIGN);
		bpmTaskActionService.finishTask(taskCmd);
	}

	@Override
    @Transactional
	public void taskAnRevoke(String instanceId, String runningTaskId, String targetNodeId, String targetTaskId) throws Exception {
		// 设置为reject 会清空堆栈信息
		// 此处只能用agree  如果用reject  并签 A  发起A1 A2,A1 发起A11 A12，A11审批后撤回会有问题
		DefaultTaskFinishCmd taskCmd = getCmd(runningTaskId,"agree","");
		setRejctCmd(taskCmd,"normal",targetNodeId);
		setCmdBpmIdentity(taskCmd,targetNodeId);
		// 这时候的撤回需要记录堆栈
		taskCmd.addTransitVars(BpmConstants.RECORD_STACK, true);
		// 如果下一个任务是会签任务 撤回时需要撤回所有的
		taskCmd.addTransitVars(BpmConstants.B_TASKS_REVOKE, true);
		bpmTaskActionService.finishTask(taskCmd);
		String newCreateTaskId = getNewCreateTaskId(taskCmd);
		bpmCustomSignDataManager.updateStatusByTaskId(targetTaskId, BpmCustomSignData.STATUS_COMPLETE, BpmCustomSignData.STATUS_WITHDRAW_APPROVAL, newCreateTaskId);
		// 原来的审批意见更新为被撤回
		BpmCheckOpinionUtil.updateCheckRevoker(targetTaskId);
	}

	/**
	 * 设置当前用户作为处理人
	 * @param taskCmd
	 * @param targetNodeId
	 */
    @Transactional
	private void setCmdBpmIdentity(DefaultTaskFinishCmd taskCmd,String targetNodeId) {
		List<BpmIdentity> list = new ArrayList<BpmIdentity>();
		BpmIdentity bpmIdentity = defaultBpmIdentityConverter.convertUser(ContextUtil.getCurrentUser());
		list.add(bpmIdentity );
		Map<String, List<BpmIdentity>> nodeIdentityMap = new HashMap<String, List<BpmIdentity>>();
		nodeIdentityMap.put(targetNodeId, list);
		taskCmd.setBpmIdentities(nodeIdentityMap);
	}

	@Override
    @Transactional
	public void parallaelARevoke(String instanceId, String targetNodeId, String currentTaskIds) throws Exception {
		List<String> taskIds = Arrays.asList(currentTaskIds.split(","));

		String firstTaskId = taskIds.get(0);
		for (String revokeTaskId : taskIds) {
			if(revokeTaskId.equals(firstTaskId)) continue;
			DefaultTaskFinishCmd taskCmd = getCmd(revokeTaskId,"agree","");
			bpmTaskActionService.finishTask(taskCmd);
		}

		List<BpmCustomSignData> signDatas = bpmCustomSignDataManager.getByInstIdAndStatus(instanceId, Arrays.asList(BpmCustomSignData.STATUS_APPROVAL,BpmCustomSignData.STATUS_WITHDRAW_APPROVAL));
		String action = "agree";
		String destNodeId = "";
		DefaultTaskFinishCmd taskCmd = getCmd(firstTaskId,action,"");
		if(BeanUtils.isNotEmpty(signDatas) && signDatas.size()==1 &&  signDatas.get(0).getTaskId().equals(firstTaskId)) {
			action = "reject";
			destNodeId = targetNodeId;
			// 这时候的撤回需要记录堆栈
			taskCmd.addTransitVars(BpmConstants.RECORD_STACK, true);
			// 用于设置A的审批已经中signType 为BeforeSign
			taskCmd.addTransitVars(BpmConstants.OPINION_SIGN_TYPE, CustomSignNodeDef.BEFORE_SIGN);
		}
		taskCmd.setActionName(action);

		setRejctCmd(taskCmd,"normal",destNodeId);
		bpmTaskActionService.finishTask(taskCmd);

		// 产生A 任务 删除signdata数据
		if("reject".equals(action)) {
			bpmCustomSignDataManager.removeByInstId(instanceId);
		}

	}

	@Override
    @Transactional
	public void parallelRevoke(String currentTaskIds, String targetTaskId) throws Exception {
		List<String> taskIds = Arrays.asList(currentTaskIds.split(","));
		for (String revokeTaskId : taskIds) {
			// 被撤回
			bpmCustomSignDataManager.updateStatusByTaskId(revokeTaskId, Arrays.asList(BpmCustomSignData.STATUS_APPROVAL,BpmCustomSignData.STATUS_WITHDRAW_APPROVAL), BpmCustomSignData.STATUS_RETRACTED, null);
		}

		List<BpmCustomSignData> signDatas = bpmCustomSignDataManager.getParallelSonByTaskId(targetTaskId);

		if(BeanUtils.isEmpty(signDatas)) {
			// 产生A11
			this.addCustomSignTask(taskIds.get(0), new String[] { ContextUtil.getCurrentUserId() },false);
			String newCreateTaskId = getNewCreateTaskId();
			bpmCustomSignDataManager.updateStatusByTaskId(targetTaskId, BpmCustomSignData.STATUS_COMPLETE, BpmCustomSignData.STATUS_WITHDRAW_APPROVAL, newCreateTaskId);
		}

		for (String revokeTaskId : taskIds) {
			DefaultTaskFinishCmd taskCmd = getCmd(revokeTaskId,"agree","");
			bpmTaskActionService.finishTask(taskCmd);
		}

	}

	@Override
    @Transactional
	public void updateOwner(Map<String, Object> ownerMap) {
		baseMapper.updateOwner(ownerMap);
	}

	@Override
    @Transactional
	public void updateAssignee(Map<String, Object> assigneeMap) {
		baseMapper.updateAssignee(assigneeMap);
	}

	public PageList<DefaultBpmTask> query(QueryFilter<DefaultBpmTask> queryFilter) {
		queryFilter.withSorter(new FieldSort("task.create_time_",Direction.DESC));
		Map<String, Object> params = queryFilter.getParams();
		if (BeanUtils.isNotEmpty(params) && (int)params.get("isAdmin") == 0) {
			String defKeys = (String) params.get("defKeys");
			if (BeanUtils.isEmpty(defKeys)) {
				return new PageList<>();
			}else {
				queryFilter.addFilter("proc_def_key_", defKeys, QueryOP.IN);
			}
		}
		queryFilter.setParams(new HashMap<>());
		return new PageList<>(baseMapper.customQuery(convert2IPage(queryFilter.getPageBean()),
					convert2Wrapper(queryFilter, currentModelClass())));
	}

	@Override
	public List<ObjectNode> getTaskListByTenantId(String tenantId) throws Exception{
		List<ObjectNode> dataObjects = new ArrayList<ObjectNode>();
		try(MultiTenantIgnoreResult setThreadLocalIgnore = MultiTenantHandler.setThreadLocalIgnore()){
			List<DefaultBpmTask> taskList = baseMapper.getTaskByTenantId(tenantId);
			if(BeanUtils.isNotEmpty(taskList)) {
				for(DefaultBpmTask list:taskList) {
					ObjectNode tasNode = (ObjectNode)JsonUtil.toJsonNode(list);
					dataObjects.add(tasNode);
				}
			}
			return dataObjects;
		} catch (Exception e) {
			throw new BaseException(e.getMessage());
		}
	}

}
