package com.hotent.runtime.manager.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.activiti.engine.TaskService;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.enums.ResponseErrorEnums;
import com.hotent.base.exception.BaseException;
import com.hotent.base.exception.NotFoundException;
import com.hotent.base.exception.RequiredException;
import com.hotent.base.exception.WorkFlowException;
import com.hotent.base.feign.BpmModelFeignService;
import com.hotent.base.feign.FormFeignService;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.groovy.GroovyScriptEngine;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.Direction;
import com.hotent.base.query.FieldRelation;
import com.hotent.base.query.FieldSort;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryField;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.Base64;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.ThreadMsgUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.base.util.string.StringPool;
import com.hotent.base.util.time.TimeUtil;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.constant.InterPoseType;
import com.hotent.bpm.api.constant.NodeType;
import com.hotent.bpm.api.constant.OpinionStatus;
import com.hotent.bpm.api.constant.PrivilegeMode;
import com.hotent.bpm.api.constant.ProcessInstanceStatus;
import com.hotent.bpm.api.constant.TaskType;
import com.hotent.bpm.api.constant.TemplateConstants;
import com.hotent.bpm.api.context.BpmContextUtil;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.helper.identity.BpmIdentityExtractService;
import com.hotent.bpm.api.helper.identity.UserQueryPluginHelper;
import com.hotent.bpm.api.model.form.FormType;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.def.BpmDefExtProperties;
import com.hotent.bpm.api.model.process.def.BpmDefLayout;
import com.hotent.bpm.api.model.process.def.BpmDefinition;
import com.hotent.bpm.api.model.process.def.BpmNodeLayout;
import com.hotent.bpm.api.model.process.def.BpmProcessDef;
import com.hotent.bpm.api.model.process.def.BpmProcessDefExt;
import com.hotent.bpm.api.model.process.def.BpmVariableDef;
import com.hotent.bpm.api.model.process.def.NodeProperties;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeForm;
import com.hotent.bpm.api.model.process.nodedef.ext.CallActivityNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.SignNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.SubProcessNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.UserTaskNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.Button;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.FormExt;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.ProcBoDef;
import com.hotent.bpm.api.model.process.task.BpmTask;
import com.hotent.bpm.api.model.process.task.BpmTaskOpinion;
import com.hotent.bpm.api.plugin.core.context.BpmPluginContext;
import com.hotent.bpm.api.service.BoDataService;
import com.hotent.bpm.api.service.BpmAgentService;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.api.service.BpmDefinitionService;
import com.hotent.bpm.api.service.BpmFormService;
import com.hotent.bpm.api.service.BpmIdentityService;
import com.hotent.bpm.api.service.BpmInstService;
import com.hotent.bpm.api.service.BpmOpinionService;
import com.hotent.bpm.api.service.BpmTaskActionService;
import com.hotent.bpm.api.service.BpmTaskService;
import com.hotent.bpm.api.service.DiagramService;
import com.hotent.bpm.api.service.SignService;
import com.hotent.bpm.api.service.TaskCommuService;
import com.hotent.bpm.engine.form.BpmFormFactory;
import com.hotent.bpm.engine.inst.DefaultProcessInstCmd;
import com.hotent.bpm.engine.task.cmd.DefaultTaskFinishCmd;
import com.hotent.bpm.exception.ApproveTaskException;
import com.hotent.bpm.listener.BusDataUtil;
import com.hotent.bpm.model.form.BpmForm;
import com.hotent.bpm.model.form.FormCategory;
import com.hotent.bpm.model.form.FormModel;
import com.hotent.bpm.model.identity.DefaultBpmIdentity;
import com.hotent.bpm.model.var.DefaultBpmVariableDef;
import com.hotent.bpm.natapi.inst.NatProInstanceService;
import com.hotent.bpm.natapi.task.NatTaskService;
import com.hotent.bpm.persistence.manager.ActTaskManager;
import com.hotent.bpm.persistence.manager.BpmCheckOpinionManager;
import com.hotent.bpm.persistence.manager.BpmCommuReceiverManager;
import com.hotent.bpm.persistence.manager.BpmDefinitionManager;
import com.hotent.bpm.persistence.manager.BpmExeStackExecutorManager;
import com.hotent.bpm.persistence.manager.BpmExeStackManager;
import com.hotent.bpm.persistence.manager.BpmExeStackRelationManager;
import com.hotent.bpm.persistence.manager.BpmInterposeRecoredManager;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.persistence.manager.BpmReadRecordManager;
import com.hotent.bpm.persistence.manager.BpmTaskCommuManager;
import com.hotent.bpm.persistence.manager.BpmTaskManager;
import com.hotent.bpm.persistence.manager.BpmTaskNoticeDoneManager;
import com.hotent.bpm.persistence.manager.BpmTaskNoticeManager;
import com.hotent.bpm.persistence.manager.BpmTaskTurnManager;
import com.hotent.bpm.persistence.manager.CopyToManager;
import com.hotent.bpm.persistence.model.ActTask;
import com.hotent.bpm.persistence.model.BpmDefAuthorizeType.BPMDEFAUTHORIZE_RIGHT_TYPE;
import com.hotent.bpm.persistence.model.BpmExeStack;
import com.hotent.bpm.persistence.model.BpmExeStackExecutor;
import com.hotent.bpm.persistence.model.BpmExeStackRelation;
import com.hotent.bpm.persistence.model.BpmInterposeRecored;
import com.hotent.bpm.persistence.model.BpmReadRecord;
import com.hotent.bpm.persistence.model.BpmTaskNotice;
import com.hotent.bpm.persistence.model.BpmTaskNoticeDone;
import com.hotent.bpm.persistence.model.CopyTo;
import com.hotent.bpm.persistence.model.DefaultBpmCheckOpinion;
import com.hotent.bpm.persistence.model.DefaultBpmDefinition;
import com.hotent.bpm.persistence.model.DefaultBpmProcessDefExt;
import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;
import com.hotent.bpm.persistence.model.DefaultBpmTask;
import com.hotent.bpm.persistence.model.DefaultBpmTaskTurn;
import com.hotent.bpm.persistence.model.ResultMessage;
import com.hotent.bpm.persistence.util.BpmStackRelationUtil;
import com.hotent.bpm.persistence.util.ServiceUtil;
import com.hotent.bpm.util.BoDataUtil;
import com.hotent.bpm.util.BpmCheckOpinionUtil;
import com.hotent.bpm.util.BpmIdentityUtil;
import com.hotent.bpm.util.BpmUtil;
import com.hotent.runtime.manager.BpmTaskSignSequenceManager;
import com.hotent.runtime.manager.BpmTaskTransManager;
import com.hotent.runtime.manager.BpmTaskTransRecordManager;
import com.hotent.runtime.manager.BpmTransReceiverManager;
import com.hotent.runtime.manager.IFlowManager;
import com.hotent.runtime.manager.TaskTransService;
import com.hotent.runtime.model.BpmTaskTrans;
import com.hotent.runtime.model.BpmTaskTransRecord;
import com.hotent.runtime.model.BpmTransReceiver;
import com.hotent.runtime.params.AssignParamObject;
import com.hotent.runtime.params.CommunicateParamObject;
import com.hotent.runtime.params.DoNextParamObject;
import com.hotent.runtime.params.FlowImageVo;
import com.hotent.runtime.params.FormAndBoVo;
import com.hotent.runtime.params.InstFormAndBoVo;
import com.hotent.runtime.params.IsAllowAddSignObject;
import com.hotent.runtime.params.ModifyExecutorsParamObject;
import com.hotent.runtime.params.RevokeParamObject;
import com.hotent.runtime.params.RevokeSignLineParamObject;
import com.hotent.runtime.params.RevokeTransParamObject;
import com.hotent.runtime.params.SelectDestinationVo;
import com.hotent.runtime.params.StartCmdParam;
import com.hotent.runtime.params.TaskApproveLineParam;
import com.hotent.runtime.params.TaskDetailVo;
import com.hotent.runtime.params.TaskDoNextVo;
import com.hotent.runtime.params.TaskGetVo;
import com.hotent.runtime.params.TaskToAgreeVo;
import com.hotent.runtime.params.TaskToRejectVo;
import com.hotent.runtime.params.TaskTransParamObject;
import com.hotent.runtime.params.TaskjImageVo;
import com.hotent.runtime.params.WithDrawParam;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserService;

@Service("IFlowManager")
public class FlowManagerImpl implements IFlowManager {
	@Resource
	IFlowManager iFlowService;
	@Resource
	BpmDefinitionManager bpmDefinitionManager;
	@Resource
	BpmProcessInstanceManager bpmProcessInstanceManager;
	@Resource
	BpmTaskManager bpmTaskManager;
	@Resource
	BpmDefinitionAccessor bpmDefinitionAccessor;
	@Resource
	CopyToManager copyToManager;
	@Resource
	BpmTaskTurnManager bpmTaskTurnManager;
	@Resource
	BpmTaskTransRecordManager taskTransRecordManager;
	@Resource
	BpmTaskActionService bpmTaskActionService;
	@Resource
	NatTaskService natTaskService;
	@Resource
	SignService signService;
	@Resource
	BpmIdentityExtractService bpmIdentityExtractService;
	@Resource
	BpmDefinitionService bpmDefinitionService;
	@Resource
	IUserService userServiceImpl;
	@Resource
	TaskTransService taskTransService;
	@Resource
	BpmInstService bpmInstService;
	@Resource
	BpmIdentityService bpmIdentityService;
	@Resource
	TaskCommuService taskCommuService;
	@Resource
	DiagramService diagramService;
	@Resource
	BpmTaskService bpmTaskService;
	@Resource
	BpmOpinionService bpmOpinionService;
	@Resource
	BpmExeStackRelationManager relationManager;
	@Resource
	BoDataService boDataService;
	@Resource
	FormFeignService formRestfulService;
	@Resource
	BpmModelFeignService bpmModelFeignService;
	@Resource
	BpmCommuReceiverManager bpmCommuReceiverManager;
	@Resource
	BpmTaskCommuManager bpmTaskCommuManager;
	@Resource
	UCFeignService ucFeignService;
	@Resource
	BpmReadRecordManager bpmReadRecordManager;
	@Resource
	BpmCheckOpinionManager bpmCheckOpinionManager;
	@Resource
	ActTaskManager actTaskManager;
	@Resource
	IUserService userService;
	@Resource
	BpmTaskNoticeManager bpmTaskNoticeManager;
	@Resource
	BpmTaskNoticeDoneManager bpmTaskNoticeDoneManager;
	@Resource
	BpmTaskSignSequenceManager signSequenceManager;
	@Resource
	BpmExeStackManager bpmExeStackManager;
	@Resource
	NatProInstanceService natProInstanceService;
	@Resource
	BpmTaskTransManager bpmTaskTransManager;
	@Resource
	BpmAgentService bpmAgentService;
	@Resource
	protected FormFeignService formService;

	@Override
	@Transactional
	public void delBpmTaskNoticeById(String id) throws Exception {
		BpmTaskNoticeManager noticeManager = AppUtil.getBean(BpmTaskNoticeManager.class);
		BpmTaskNotice bpmTaskNotice = noticeManager.get(id);// 根据主键ID获取传阅任务
		BpmDefinition bpmDefinition = bpmDefinitionManager.getById(bpmTaskNotice.getProcDefId());
		if (bpmTaskNotice.getIsRead() == 1 && "false".equals(bpmDefinition.getIsReadRevoke())) {
			throw new RuntimeException("传阅接收人已阅，无法撤回");
		}
		if (bpmTaskNotice.getIsRead() == 1 && "true".equals(bpmDefinition.getIsReadRevoke())) {
			bpmTaskNoticeDoneManager.delBpmTaskNoticeDoneById(id);// 删除知会已办传阅任务
		}
		bpmTaskNotice.setIsRevoke(1);
		noticeManager.update(bpmTaskNotice);
        //审批记录
        DefaultBpmCheckOpinion defaultBpmCheckOpinion = new DefaultBpmCheckOpinion();
        defaultBpmCheckOpinion.setId(UniqueIdUtil.getSuid());
        defaultBpmCheckOpinion.setProcDefId(bpmTaskNotice.getProcDefId());
        defaultBpmCheckOpinion.setProcInstId(bpmTaskNotice.getProcInstId());
        defaultBpmCheckOpinion.setTaskId(BeanUtils.isEmpty(bpmTaskNotice.getTaskId())?null:bpmTaskNotice.getTaskId());
        defaultBpmCheckOpinion.setTaskKey(null);
        defaultBpmCheckOpinion.setTaskName("传阅任务");
        defaultBpmCheckOpinion.setStatus(OpinionStatus.COPYTO.getKey());
        defaultBpmCheckOpinion.setCreateTime(LocalDateTime.now());
        defaultBpmCheckOpinion.setOpinion("传阅撤回");
        defaultBpmCheckOpinion.setQualfiedNames(bpmTaskNotice.getAssigneeName());
        defaultBpmCheckOpinion.setAuditorName(ContextUtil.getCurrentUser().getFullname());
        defaultBpmCheckOpinion.setCompleteTime(LocalDateTime.now());
        defaultBpmCheckOpinion.setDurMs(TimeUtil.getCurrentTimeMillis() - TimeUtil.getTimeMillis(defaultBpmCheckOpinion.getCreateTime()));
        defaultBpmCheckOpinion.setAuditor(ContextUtil.getCurrentUser().getUserId());
        defaultBpmCheckOpinion.setFiles("");
        defaultBpmCheckOpinion.setIsRead(1);
        bpmCheckOpinionManager.create(defaultBpmCheckOpinion);
	}

    @Override
    public PageList<DefaultBpmTask> getLeaderTodoList(String account, QueryFilter queryFilter) throws Exception {
        IUser user = ServiceUtil.getUserByAccount(account);
        // 查询列表
        try {
            PageList<DefaultBpmTask> list = bpmTaskManager.getLeaderByUserId(user.getUserId(), queryFilter);

            if (BeanUtils.isNotEmpty(list)) {
                Set<String> defKeys = new HashSet<>();
                for (DefaultBpmTask obj : list.getRows()) {
                    defKeys.add(obj.getProcDefKey());
                }
                QueryFilter defQueryFilter = QueryFilter.<DefaultBpmDefinition> build();
                defQueryFilter.addFilter("def_key_", StringUtil.join(defKeys), QueryOP.IN);
                defQueryFilter.addFilter("is_main_", "Y", QueryOP.EQUAL);
                PageList<DefaultBpmDefinition> defQuery = bpmDefinitionManager.query(defQueryFilter);
                for (DefaultBpmDefinition def : defQuery.getRows()) {
                    if (def.getShowUrgentState() == 0) {
                        defKeys.remove(def.getDefKey());
                    }
                }
                for (DefaultBpmTask task : list.getRows()) {
                    if (!defKeys.contains(task.getProcDefKey())) {
                        task.setUrgentStateValue("");
                    }
                }
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("数据库查询出错了！", e);
        }
    }

	@Override
	public PageList<DefaultBpmTask> getTodoList(String account, QueryFilter<DefaultBpmTask> queryFilter) throws Exception {
		IUser user = ServiceUtil.getUserByAccount(account);
		// 查询列表
		try {
			PageList<DefaultBpmTask> list = bpmTaskManager.getByUserId(user.getUserId(), queryFilter);

			if (BeanUtils.isNotEmpty(list)) {
				Set<String> defKeys = new HashSet<>();
				for (DefaultBpmTask obj : list.getRows()) {
					defKeys.add(obj.getProcDefKey());
				}
				QueryFilter<DefaultBpmDefinition> defQueryFilter = QueryFilter.<DefaultBpmDefinition> build();
				defQueryFilter.addFilter("def_key_", StringUtil.join(defKeys), QueryOP.IN);
				defQueryFilter.addFilter("is_main_", "Y", QueryOP.EQUAL);
				PageList<DefaultBpmDefinition> defQuery = bpmDefinitionManager.query(defQueryFilter);
				for (DefaultBpmDefinition def : defQuery.getRows()) {
					if (def.getShowUrgentState() == 0) {
						defKeys.remove(def.getDefKey());
					}
				}
				for (DefaultBpmTask task : list.getRows()) {
					if (!defKeys.contains(task.getProcDefKey())) {
						task.setUrgentStateValue("");
					}
				}
			}
			return list;
		} catch (Exception e) {
			throw new RuntimeException("数据库查询出错了！", e);
		}
	}

	@Override
	public QueryFilter getTodoQueryFilter(QueryFilter queryFilter) throws Exception {
		if (BeanUtils.isNotEmpty(queryFilter.getQuerys()) && queryFilter.getQuerys().size() > 0) {
			List<QueryField> fields = queryFilter.getQuerys();
			for (QueryField field : fields) {
				if ("urgentStateValue".equals(field.getProperty())) {
					field.setGroup("groupUrgent");
					field.setRelation(FieldRelation.AND);
					QueryFilter defFilter = QueryFilter.<DefaultBpmDefinition> build();
					defFilter.addFilter("IS_MAIN_", "Y", QueryOP.EQUAL);
					defFilter.addFilter("SHOW_URGENT_STATE_", "1", QueryOP.EQUAL);
					PageList<DefaultBpmDefinition> query = bpmDefinitionManager.query(defFilter);
					List<String> defKeys = new ArrayList<>();
					if (BeanUtils.isNotEmpty(query.getRows())) {
						for (DefaultBpmDefinition def : query.getRows()) {
							defKeys.add(def.getDefKey());
						}
					}
					queryFilter.addFilter("PROC_DEF_KEY_", defKeys, QueryOP.IN, FieldRelation.AND, "groupUrgent");
					break;
				}
			}
		}
		queryFilter.setGroupRelation(FieldRelation.AND);
		return queryFilter;
	}

	@Override
	public PageList<BpmTaskNotice> getMyNoticeReadList(String account, QueryFilter queryFilter) throws Exception {
		IUser user = ServiceUtil.getUserByAccount(account);
		// 查询列表
		queryFilter.addFilter("bpm_task_notice.STATUS_", "COPYTO", QueryOP.EQUAL, FieldRelation.AND);
		queryFilter.addFilter("bpm_task_notice.OWNER_ID_", user.getUserId(), QueryOP.EQUAL, FieldRelation.AND);
		return bpmTaskNoticeManager.query(queryFilter);
	}

	@Override
	public List<Map<String, Object>> getMyNoticeReadCount(String account, QueryFilter queryFilter) throws Exception {
		IUser user = ServiceUtil.getUserByAccount(account);
		// 查询列表
		queryFilter.addFilter("notice.STATUS_", "COPYTO", QueryOP.EQUAL, FieldRelation.AND);
		queryFilter.addFilter("notice.OWNER_ID_", user.getUserId(), QueryOP.EQUAL, FieldRelation.AND);
		return bpmTaskNoticeManager.getNoticeTodoReadCount(queryFilter);
	}

	@Override
	public PageList<BpmTaskNotice> getNoticeTodoReadList(String account, QueryFilter queryFilter) throws Exception {
		IUser user = ServiceUtil.getUserByAccount(account);
		queryFilter.addFilter("bpm_task_notice.STATUS_", "COPYTO", QueryOP.EQUAL, FieldRelation.AND);
		queryFilter.addFilter("ASSIGNEE_ID_", user.getUserId(), QueryOP.EQUAL, FieldRelation.AND);
		queryFilter.addFilter("IS_READ_", "0", QueryOP.EQUAL, FieldRelation.AND);
		queryFilter.addFilter("IS_REVOKE_", "0", QueryOP.EQUAL, FieldRelation.AND);
		return bpmTaskNoticeManager.query(queryFilter);
	}

	@Override
	public List<Map<String, Object>> getNoticeTodoReadCount(String account, QueryFilter queryFilter) throws Exception {
		IUser user = ServiceUtil.getUserByAccount(account);
		queryFilter.addFilter("notice.STATUS_", "COPYTO", QueryOP.EQUAL, FieldRelation.AND);
		queryFilter.addFilter("ASSIGNEE_ID_", user.getUserId(), QueryOP.EQUAL, FieldRelation.AND);
		queryFilter.addFilter("IS_READ_", "0", QueryOP.EQUAL, FieldRelation.AND);
		queryFilter.addFilter("IS_REVOKE_", "0", QueryOP.EQUAL, FieldRelation.AND);
		return bpmTaskNoticeManager.getNoticeTodoReadCount(queryFilter);
	}

	@Override
	public PageList<BpmTaskNoticeDone> getNoticeDoneReadList(String account, QueryFilter queryFilter) throws Exception {
		IUser user = ServiceUtil.getUserByAccount(account);
		// 查询列表

		queryFilter.addFilter("bpm_task_notice_done.STATUS_", "COPYTO", QueryOP.EQUAL, FieldRelation.AND);
		queryFilter.addFilter("bpm_task_notice_done.AUDITOR_", user.getUserId(), QueryOP.EQUAL, FieldRelation.AND);
		return bpmTaskNoticeDoneManager.query(queryFilter);

	}

	@Override
	public List<Map<String, Object>> getNoticeDoneReadCount(String account, QueryFilter queryFilter) throws Exception {
		IUser user = ServiceUtil.getUserByAccount(account);
		// 查询列表

		queryFilter.addFilter("done.STATUS_", "COPYTO", QueryOP.EQUAL, FieldRelation.AND);
		queryFilter.addFilter("done.AUDITOR_", user.getUserId(), QueryOP.EQUAL, FieldRelation.AND);
		return bpmTaskNoticeDoneManager.getNoticeDoneReadCount(queryFilter);
	}

	@Override
	public PageList<Map<String, Object>> getDoneList(String account, QueryFilter queryFilter, String status)
			throws Exception {

		IUser user = ServiceUtil.getUserByAccount(account);
		IPage<Map<String, Object>> list = bpmProcessInstanceManager.getHandledByUserId(user.getUserId(), queryFilter);
		return new PageList<Map<String, Object>>(list);

	}

	@Override
	public PageList<Map<String, Object>> getDoneInstList(String account, QueryFilter queryFilter, String status)
			throws Exception {
		IUser user = ServiceUtil.getUserByAccount(account);
		return bpmProcessInstanceManager.getDoneInstList(user.getUserId(), queryFilter);
	}

	@Override
	public List<Map<String, Object>> getDoneInstCount(String account, QueryFilter queryFilter, String status)
			throws Exception {
		IUser user = ServiceUtil.getUserByAccount(account);
		return bpmProcessInstanceManager.getDoneInstCount(user.getUserId(), queryFilter);
	}

	@Override
	public PageList<DefaultBpmProcessInstance> getCompletedList(String account, QueryFilter queryFilter)
			throws Exception {
		// 传入的账户
		IUser user = ServiceUtil.getUserByAccount(account);
		IPage<DefaultBpmProcessInstance> list = bpmProcessInstanceManager.getCompletedByUserId(user.getUserId(),
				queryFilter);
		return new PageList<DefaultBpmProcessInstance>(list);

	}

	@Override
	public PageList<DefaultBpmProcessInstance> getMyCompletedList(String account, QueryFilter queryFilter)
			throws Exception {
		// 传入的账户
		IUser user = ServiceUtil.getUserByAccount(account);
		IPage<DefaultBpmProcessInstance> list = bpmProcessInstanceManager.getMyCompletedByUserId(user.getUserId(),
				queryFilter);
		return new PageList<DefaultBpmProcessInstance>(list);

	}

	@Override
	public PageList<DefaultBpmProcessInstance> getMyRequestList(String account, QueryFilter queryFilter)
			throws Exception {
		// 传入的账户
		IUser user = ServiceUtil.getUserByAccount(account);
		// 查询列表
		IPage<DefaultBpmProcessInstance> list = bpmProcessInstanceManager.getMyRequestByUserId(user.getUserId(),
				queryFilter);
		List<DefaultBpmProcessInstance> listInst = new ArrayList<>();
		for (DefaultBpmProcessInstance entity : list.getRecords()) {
			DefaultBpmProcessInstance defaultBpmProcessInstance = new DefaultBpmProcessInstance();
			defaultBpmProcessInstance = entity;
			if ("back".equals(entity.getStatus())) {// 判断我的请求流程实例数据是否是被驳回状态
				// 根据流程定义ID获取流程图所有任务节点
				TaskjImageVo taskNode = iFlowService.taskImage("", entity.getProcDefId());
				if (BeanUtils.isNotEmpty(taskNode)) {
					List<BpmNodeLayout> listLayout = taskNode.getBpmDefLayout().getListLayout();
					// 根据流程实例ID查询最新的一条审批记录任务ID
					List<String> listStr = bpmProcessInstanceManager.getNodeIdByInstId(entity.getParentInstId());
					if (BeanUtils.isNotEmpty(listStr)) {
						if (listLayout.get(1).getNodeId().equals(listStr.get(0))) {
							defaultBpmProcessInstance.setBackToStart(true);
						}
					}
				}
			}
			listInst.add(defaultBpmProcessInstance);
		}
		return new PageList<DefaultBpmProcessInstance>(listInst);
	}

	@Override
	public PageList<DefaultBpmDefinition> getMyFlowList(String account, QueryFilter queryFilter, String typeId)
			throws Exception {
		// 设置这个让调用 ContextUtil.getCurrentUser()方法能够获取到值。
		IUser user = ServiceUtil.getUserByAccount(account);
		queryFilter.withParam("bpmDefAuthorizeRightType", BPMDEFAUTHORIZE_RIGHT_TYPE.START);
		queryFilter.withQuery(new QueryField("is_main_", "Y", QueryOP.EQUAL));
		// 查询列表
		try {
			List<DefaultBpmDefinition> list = bpmDefinitionManager.queryList(queryFilter, user).getRows();
			return new PageList<DefaultBpmDefinition>(list);
		} catch (Exception e) {
			throw new RuntimeException("数据库查询出错了！");
		}
	}

	/**
	 * 根据jsonobject 构建 QueryFilter 。
	 * 
	 * @param jsonObject
	 * @return
	 */
	@SuppressWarnings("unused")
	private QueryFilter buildQueryFilter(ObjectNode jsonObject) {
		String orderField = JsonUtil.getString(jsonObject, "orderField", "create_time_");
		String orderSeq = JsonUtil.getString(jsonObject, "orderSeq", "desc");
		Integer currentPage = JsonUtil.getInt(jsonObject, "currentPage", 1);
		Integer pageSize = JsonUtil.getInt(jsonObject, "pageSize", 20);

		QueryFilter queryFilter = QueryFilter.build().withDefaultPage();
		// 设置分页
		PageBean page = new PageBean(currentPage, pageSize);
		queryFilter.setPageBean(page);
		// 设置排序
		if (StringUtil.isNotEmpty(orderField) && StringUtil.isNotEmpty(orderSeq)) {
			List<FieldSort> fieldSorts = new ArrayList<FieldSort>();
			fieldSorts.add(new FieldSort(orderField, Direction.fromString(orderSeq)));
			queryFilter.setSorter(fieldSorts);
		}

		return queryFilter;
	}

	@Override
	public PageList<DefaultBpmProcessInstance> getMyDraftList(String account, QueryFilter queryFilter)
			throws Exception {
		// 传入的账户
		IUser user = ServiceUtil.getUserByAccount(account);
		// 查询列表
		try {
			IPage<DefaultBpmProcessInstance> list = bpmProcessInstanceManager.getDraftsByUserId(user.getUserId(),
					queryFilter);
			return new PageList<DefaultBpmProcessInstance>(list);
		} catch (Exception e) {
			throw new RuntimeException("数据库查询出错了！");
		}
	}

	@Override
	public PageList<CopyTo> getReceiverCopyTo(String account, QueryFilter queryFilter, String type) throws Exception {
		IUser user = ServiceUtil.getUserByAccount(account);
		// 查询列表
		try {
			List<CopyTo> list = copyToManager.getReceiverCopyTo(user.getUserId(), queryFilter);
			return new PageList<CopyTo>(list);
		} catch (Exception e) {
			throw new RuntimeException("数据库查询出错了！");
		}
	}

	@Override
	public PageList<CopyTo> myCopyTo(String account, QueryFilter queryFilter) throws Exception {
		IUser user = ServiceUtil.getUserByAccount(account);
		// 查询列表
		try {
			List<CopyTo> list = copyToManager.getMyCopyTo(user.getUserId(), queryFilter);
			return new PageList<CopyTo>(list);
		} catch (Exception e) {
			throw new RuntimeException("数据库查询出错了！");
		}
	}

	/**
	 * 根据流程定义ID或流程定义KEY获取流程变量
	 * 
	 * @param json
	 * @return
	 */
	@Override
	public List<BpmVariableDef> getWorkflowVar(String json) throws Exception {
		ObjectNode jsonObject = (ObjectNode) JsonUtil.toJsonNode(json);
		String defId = JsonUtil.getString(jsonObject, "defId");
		String defKey = JsonUtil.getString(jsonObject, "defKey");
		String flowKey = JsonUtil.getString(jsonObject, "flowKey");

		BpmDefinition bpmDefinition = null;
		bpmDefinition = bpmDefinitionManager.getById(defId);
		if (BeanUtils.isEmpty(bpmDefinition)) {
			String key = StringUtil.isNotEmpty(defKey) ? defKey : flowKey;
			if (StringUtil.isNotEmpty(key)) {
				bpmDefinition = bpmDefinitionManager.getMainByDefKey(key, false);
				defId = bpmDefinition.getDefId();
			}
		}
		if (BeanUtils.isEmpty(bpmDefinition)) {
			throw new NullPointerException("找不到对应的流程定义，请检查输入的defId、defKey或flowKey！");
		}
		List<BpmVariableDef> bpmVariableList = new ArrayList<BpmVariableDef>();
		// 全局变量
		BpmProcessDef<BpmProcessDefExt> bpmProcessDefExt = bpmDefinitionAccessor.getBpmProcessDef(defId);
		DefaultBpmProcessDefExt defExt = (DefaultBpmProcessDefExt) bpmProcessDefExt.getProcessDefExt();
		if (defExt.getVariableList() != null)
			bpmVariableList.addAll(defExt.getVariableList());

		// 节点变量
		List<BpmNodeDef> bpmNodeDefList = bpmDefinitionAccessor.getNodesByType(defId, NodeType.USERTASK);
		bpmNodeDefList.addAll(bpmDefinitionAccessor.getNodesByType(defId, NodeType.SIGNTASK));

		for (BpmNodeDef bpmNodeDef : bpmNodeDefList) {
			UserTaskNodeDef taskNodeDef = (UserTaskNodeDef) bpmNodeDef;
			List<BpmVariableDef> nodeVarList = taskNodeDef.getVariableList();
			if (nodeVarList != null)
				bpmVariableList.addAll(nodeVarList);
		}

		return bpmVariableList;

	}

	@Override
	public PageList<DefaultBpmTaskTurn> getDelegate(String account, QueryFilter queryFilter) throws Exception {
		IUser user = ServiceUtil.getUserByAccount(account);
		// 查询列表
		try {
			IPage<DefaultBpmTaskTurn> list = bpmTaskTurnManager.getMyDelegate(user.getUserId(), queryFilter);
			return new PageList<DefaultBpmTaskTurn>(list);
		} catch (Exception e) {
			throw new RuntimeException("数据库查询出错了！");
		}
	}

	@Override
	public List<Map<String, Object>> getDelegateCount(String account, QueryFilter queryFilter) throws Exception {
		IUser user = ServiceUtil.getUserByAccount(account);
		// 查询列表
		try {
			return bpmTaskTurnManager.getMyDelegateCount(user.getUserId());
		} catch (Exception e) {
			throw new RuntimeException("数据库查询出错了！");
		}
	}

	@Override
	public PageList<BpmTaskTransRecord> getMyTrans(String account, QueryFilter queryFilter) throws Exception {
		// 设置这个让调用 ContextUtil.getCurrentUser()方法能够获取到值。
		IUser user = ServiceUtil.getUserByAccount(account);
		// 查询列表
		PageList<BpmTaskTransRecord> pageList = taskTransRecordManager.getMyTransRecord(user.getUserId(), queryFilter);
		return pageList;
	}

	@Override
	@Transactional
	public CommonResult<String> removeDraftById(String id) throws Exception {
		DefaultBpmProcessInstance processeInstance = bpmProcessInstanceManager.get(id);
		if (BeanUtils.isEmpty(processeInstance)) {
			throw new NullPointerException("id为" + id + "的草稿不存在！");
		}
		if (!ProcessInstanceStatus.STATUS_DRAFT.getKey().equals(processeInstance.getStatus())) {
			throw new RuntimeException("该实例不是草稿状态，请不要通过此接口删除！");
		}
		bpmProcessInstanceManager.remove(id);
		return new CommonResult<String>(true, "流程草稿删除成功", "");
	}

	@Override
	@Transactional
	public CommonResult<String> delegate(AssignParamObject assignParamObject) throws Exception {
		IUser user = ContextUtil.getCurrentUser();
		String taskId = assignParamObject.getTaskId();
		String messageType = assignParamObject.getMessageType();
		String userIds = assignParamObject.getUserId();
		String opinion = assignParamObject.getOpinion();
		String files = assignParamObject.getFiles();
		DefaultBpmTask task = bpmTaskManager.get(taskId);
		if (BeanUtils.isEmpty(task)) {
			throw new RuntimeException("任务不存在或已经被处理！");
		}
		if (StringUtil.isEmpty(messageType)) {
			throw new RequiredException(ResponseErrorEnums.REQUIRED_ERROR + ":messageType通知类型必填！");
		}
		String userId = "";
		if (StringUtil.isEmpty(userIds)) {
			throw new RuntimeException("必须传入转办用户id(userId)");
		} else {
			if (user.getUserId().equals(userIds)) {
				throw new RuntimeException("任务转办人不能为自己");
			}
			IUser userById = ServiceUtil.getUserById(userIds);
			if (BeanUtils.isEmpty(userById)) {
				throw new RuntimeException("转办用户id不存在");
			}
			userId = userById.getUserId();
		}
		// 转办
		bpmTaskActionService.delegate(taskId, userId, messageType, opinion, files);
		return new CommonResult<String>(true, "任务转办成功", "");
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	@Transactional
	public CommonResult<String> taskSignUsers(AssignParamObject assignParamObject) throws Exception {
		IUser user = ContextUtil.getCurrentUser();
		String taskId = assignParamObject.getTaskId();
		String userIds = assignParamObject.getUserId();
		String messageType = assignParamObject.getMessageType();
		String addReason = assignParamObject.getOpinion();
		if (StringUtil.isEmpty(userIds)) {
			throw new RuntimeException("必须传入加签用户Id");
		}
		String[] userIdAry = userIds.split(",");
		List<String> userIdList = new ArrayList<String>();
		for (String userId : userIdAry) {
			if (user.getUserId().equals(userId)) {
				throw new RuntimeException(String.format("任务加签人不能为自己:%s", userId));
			}
			IUser userById = ServiceUtil.getUserById(userId);
			if (BeanUtils.isEmpty(userById)) {
				throw new RuntimeException(String.format("加签用户Id(userId):%s不存在", userId));
			}
			userIdList.add(userById.getUserId());
		}
		String[] userIdsAry = new String[userIdList.size()];
		userIdList.toArray(userIdsAry);
		ResultMessage addSignTask = signService.addSignTask(taskId, userIdAry);
		// 加签成功 发送消息
		if (addSignTask.getResult() == ResultMessage.SUCCESS) {
			if (StringUtil.isNotEmpty(messageType)) {
				List<BpmIdentity> bpmIdentities = (List<BpmIdentity>) addSignTask.getVars().get("users");
				List<IUser> users = bpmIdentityExtractService.extractUser(bpmIdentities);
				Map<String, Object> variables = natTaskService.getVariables(taskId);
				variables.put("cause", addReason);
				variables.put("sender", ContextUtil.getCurrentUser().getFullname());
				variables.put("taskSubject", variables.get("subject_"));
				String baseUrl = "";// SysPropertyUtil.getByAlias(TemplateConstants.TEMP_VAR.BASE_URL);
				variables.put("baseUrl", baseUrl);

				List<Map<String, String>> taskIds = (List<Map<String, String>>) addSignTask.getVars().get("taskIds");
				for (IUser iuser : users) {
					String taskid = findTaskId(taskIds, user.getUserId());
					if (StringUtil.isNotEmpty(taskid)) {
						variables.put("taskId", taskid);
						users = new ArrayList<IUser>();
						users.add(user);
						// 发送消息
						signService.sendNotify(users, Arrays.asList(messageType.split(",")),
								TemplateConstants.TYPE_KEY.BPM_ADD_SIGN_TASK, variables);
					}
				}
			}
			return new CommonResult<String>(true, "任务加签成功");
		} else {
			throw new RuntimeException(addSignTask.getCause());
		}
	}

	private String findTaskId(List<Map<String, String>> taskIds, String userId) {
		String taskId = "";
		for (Map<String, String> map : taskIds) {
			String uid = map.get("userId");
			if (userId.equals(uid)) {
				taskId = map.get("taskId");
				break;
			}
		}
		return taskId;
	}

	@Override
	@Transactional
	public CommonResult<String> revokeInstance(RevokeParamObject revokeParamObject) throws Exception {
		String instanceId = revokeParamObject.getInstanceId();
		String messageType = revokeParamObject.getMessageType();
		String cause = revokeParamObject.getCause();
		Boolean isHandRevoke = revokeParamObject.getIsHandRevoke();// 是否从已办中撤回
		ResultMessage result = null;
		if (isHandRevoke) {
			result = revokeTask(revokeParamObject.getTaskId(), instanceId, messageType, cause,
					revokeParamObject.getRevokeNodeId());
		} else {
			result = bpmProcessInstanceManager.revokeInstance(instanceId, messageType, cause);
		}
		if (result.getResult() == 1) {
			ActionCmd actionCmd = ContextThreadUtil.getActionCmd();
			String val = "";
			if (StringUtil.isNotEmpty((String) actionCmd.getTransitVars().get("revokeTaskId"))) {
				val = actionCmd.getTransitVars().get("revokeTaskId").toString();
			}
			return new CommonResult<String>(true, "撤回成功", val);
		} else {
			throw new BaseException(result.getMessage());
		}
	}

	private boolean validTask(String bpmnInstId, BpmNodeDef nodeDef) {
		String nodeId = nodeDef.getNodeId();
		List<ActTask> list = actTaskManager.getByInstId(bpmnInstId);

		for (ActTask task : list) {
			if (nodeId.equals(task.getTaskDefKey())) {
				return false;
			}
		}
		return true;
	}

	// 撤回命令
	private DefaultTaskFinishCmd getCmdFromRecall(String taskId, String actionName, String opinion, String backHandMode,
			String toNodeId, BpmIdentity bpmIdentity) {
		DefaultTaskFinishCmd cmd = new DefaultTaskFinishCmd();
		// 驳回到指定节点
		cmd.setDestination(toNodeId);
		cmd.setTaskId(taskId);
		cmd.setActionName(actionName);
		// 已办中撤消
		cmd.addTransitVars("IsDoneUnused", true);
		// 设置表单意见。
		cmd.setApprovalOpinion(opinion);
		cmd.setDataMode(ActionCmd.DATA_MODE_BO);
		// 设置流程驳回时跳转模式。
		cmd.addTransitVars(BpmConstants.BACK_HAND_MODE, backHandMode);
		// 设置目标节点映射----------------------------------------------------------------------------------------------------
		List<BpmIdentity> list = new ArrayList<BpmIdentity>();
		list.add(bpmIdentity);
		Map<String, List<BpmIdentity>> nodeIdentityMap = new HashMap<String, List<BpmIdentity>>();
		nodeIdentityMap.put(toNodeId, list);
		cmd.setBpmIdentities(nodeIdentityMap);
		return cmd;
	}

	/**
	 * 撤回任务
	 *
	 * @param instId
	 * @param informType
	 * @param cause
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ResultMessage revokeTask(String revokeTaskId, String instId, String informType, String cause,
			String revokeNodeId) throws Exception {
		BpmDefinitionAccessor bpmDefinitionAccessor = (BpmDefinitionAccessor) AppUtil.getBean("bpmDefinitionAccessor");
		BpmExeStackExecutorManager bpmExeStackExecutorManager = AppUtil.getBean(BpmExeStackExecutorManager.class);
		// 找到此流程实例的任务列表，如果有多个任务只要有只有在所有人未处理时才能撤回
		// 1.找出当前流程实例所在节点，当前节点如果是会签不充许撤回
		List<DefaultBpmTask> list = bpmTaskManager.getByInstId(instId);
		if (BeanUtils.isEmpty(list)) {
			throw new BaseException("驳回到外部子流程的操作不支持撤回");
		}

		DefaultBpmTask runningTask = list.get(0);
		//处理同步撤回，会撤回其它分支任务的问题。
        if (list.size() >1) {
			List<DefaultBpmCheckOpinion> byParentId = bpmCheckOpinionManager.getByParentId(revokeTaskId);
			if (BeanUtils.isNotEmpty(byParentId)) {
				Set<String> taskIds = new HashSet<>();
				for (DefaultBpmCheckOpinion defaultBpmCheckOpinion : byParentId) {
					if (defaultBpmCheckOpinion.getStatus().equals(OpinionStatus.AWAITING_CHECK.getKey())) {
						taskIds.add(defaultBpmCheckOpinion.getTaskId());
					}
				}
				for (DefaultBpmTask task : list) {
					if (taskIds.contains(task.getId())) {
						runningTask = task;
						break;
					}
				}
			}
		}
		
		String prcoDefId = runningTask.getProcDefId();
		String nodeId = runningTask.getNodeId();
		String taskId = runningTask.getTaskId();
		BpmNodeDef node = bpmDefinitionAccessor.getBpmNodeDef(prcoDefId, nodeId);
		NodeType type = node.getType();
		BpmProcessInstance instance = bpmProcessInstanceManager.get(instId);
		List<BpmReadRecord> bpmReadRecord = bpmReadRecordManager.getByTaskIds(list);
		if (BeanUtils.isNotEmpty(bpmReadRecord)) {
			// 如果流程定义未设置“允许已阅撤回”，则不允许撤回
			DefaultBpmDefinition def = bpmDefinitionManager.getById(prcoDefId);
			if ("false".equals(def.getIsReadRevoke())) {
				throw new BaseException("任务已被查阅，无法撤回!");
			}
		}

		// 没找到。并且传了实例id、则根据实例id找审批历史
		QueryFilter queryFilter = QueryFilter.<DefaultBpmCheckOpinion> build();
		PageList<DefaultBpmCheckOpinion> query = null;
		if (StringUtil.isNotEmpty(instId)) {
			queryFilter = QueryFilter.<DefaultBpmCheckOpinion> build();
			queryFilter.addFilter("PROC_INST_ID_", instId, QueryOP.EQUAL);
			List<FieldSort> sorter1 = new ArrayList<FieldSort>();
			FieldSort sort1 = new FieldSort("COMPLETE_TIME_", Direction.DESC);
			sorter1.add(sort1);
			queryFilter.setSorter(sorter1);
			query = bpmCheckOpinionManager.query(queryFilter);
		}

		DefaultBpmCheckOpinion checkOpinion = query.getRows().get(0);
		if (OpinionStatus.INQU.getKey().equals(checkOpinion.getStatus())) {
			// 处理征询。1，将征询人的任务。由流转改为正常
			DefaultBpmTask task = bpmTaskManager.get(checkOpinion.getTaskId());
			task.setStatus(TaskType.NORMAL.getKey());
			bpmTaskManager.update(task);
			// 2.删除该任务的相关流转记录。a,删除留转任务。b.删除流转记录。c。删除接收记录
			QueryFilter queryFilter2 = QueryFilter.<BpmTaskTrans> build();
			queryFilter2.addFilter("task_id_", checkOpinion.getTaskId(), QueryOP.EQUAL);
			BpmTaskTransManager bpmTaskTransManager = AppUtil.getBean(BpmTaskTransManager.class);

			PageList<BpmTaskTrans> query2 = bpmTaskTransManager.query(queryFilter2);
			String[] ids = new String[query2.getRows().size()];
			for (int i = 0; i < query2.getRows().size(); i++) {
				ids[i] = query2.getRows().get(i).getId();
			}
			bpmTaskTransManager.removeByIds(ids);

			BpmTaskTransRecordManager bpmTaskTransRecordManager = AppUtil.getBean(BpmTaskTransRecordManager.class);
			QueryFilter queryFilter3 = QueryFilter.<BpmTaskTransRecord> build();
			queryFilter3.addFilter("task_id_", checkOpinion.getTaskId(), QueryOP.EQUAL);
			PageList<BpmTaskTransRecord> query3 = bpmTaskTransRecordManager.query(queryFilter3);
			String[] ids3 = new String[query3.getRows().size()];
			for (int i = 0; i < query3.getRows().size(); i++) {
				ids3[i] = query3.getRows().get(i).getId();
			}
			if (ids3.length > 0) {
				bpmTaskTransRecordManager.removeByIds(ids3);
				// 删除接收人记录
				BpmTransReceiverManager bpmTransReceiverManager = AppUtil.getBean(BpmTransReceiverManager.class);
				QueryFilter queryFilter4 = QueryFilter.<BpmTransReceiver> build();
				queryFilter4.addFilter("TRANS_RECORDID_", ids3, QueryOP.IN);
				PageList<BpmTransReceiver> query4 = bpmTransReceiverManager.query(queryFilter4);
				String[] ids4 = new String[query4.getRows().size()];
				for (int i = 0; i < query4.getRows().size(); i++) {
					ids4[i] = query4.getRows().get(i).getId();
				}
				bpmTransReceiverManager.removeByIds(ids4);
			}

			// 3.找到该任务产生的流转子任务bpm_task.
			List<DefaultBpmTask> childsByTaskId = bpmTaskManager.getChildsByTaskId(checkOpinion.getTaskId());
			String[] childTaskIds = new String[childsByTaskId.size()];
			for (int i = 0; i < childsByTaskId.size(); i++) {
				childTaskIds[i] = childsByTaskId.get(i).getTaskId();
				// 删除流转子任务产生的审批记录
				DefaultBpmCheckOpinion childOpinion = bpmCheckOpinionManager.getByTaskId(childTaskIds[i]);
				if (BeanUtils.isNotEmpty(childOpinion)) {
					bpmCheckOpinionManager.remove(childOpinion.getId());
				}
			}
			// 删除子任务。
			bpmTaskManager.delByParentId(checkOpinion.getTaskId());
			// 4.将子任务对应的审批记录标记为被撤回
			handleRevokeInquCheckOpinion(task, checkOpinion, cause);

			// 修改流转源的审批历史
			checkOpinion.setStatus(OpinionStatus.AWAITING_CHECK.getKey());
			checkOpinion.setOpinion("");
			checkOpinion.setFiles("");
			bpmCheckOpinionManager.update(checkOpinion);
			// 撤回任务时根据任务ID撤回传阅
			if (StringUtil.isNotEmpty(taskId)) {// 要撤回任务的下一个任务的任务ID
				String id = "";// 传阅任务主键ID
				List<BpmTaskNotice> list1 = bpmTaskNoticeManager.getBpmTaskNoticeByTaskId(taskId);
				for (int i = 0; i < list1.size(); i++) {
					id = list1.get(i).getId();
					BpmTaskNoticeManager noticeManager = AppUtil.getBean(BpmTaskNoticeManager.class);
					BpmTaskNotice bpmTaskNotice = noticeManager.get(id);// 根据主键ID获取传阅任务
					BpmDefinition bpmDefinition = bpmDefinitionManager.getById(bpmTaskNotice.getProcDefId());
					if (bpmTaskNotice.getIsRead() == 1 && "false".equals(bpmDefinition.getIsReadRevoke())) {
						break;// 传阅接收人已阅，无法撤回
					} else {
						if (bpmTaskNotice.getIsRead() == 1 && "true".equals(bpmDefinition.getIsReadRevoke())) {
							bpmTaskNoticeDoneManager.delBpmTaskNoticeDoneById(id);// 删除知会已办传阅任务
						}
						bpmTaskNotice.setIsRevoke(1);
						noticeManager.update(bpmTaskNotice);
					}
				}
			}
			if (StringUtil.isNotEmpty(revokeTaskId)) {// 要撤回任务的任务ID
				String id = "";// 传阅任务主键ID
				List<BpmTaskNotice> list1 = bpmTaskNoticeManager.getBpmTaskNoticeByTaskId(revokeTaskId);
				for (int i = 0; i < list1.size(); i++) {
					id = list1.get(i).getId();
					BpmTaskNoticeManager noticeManager = AppUtil.getBean(BpmTaskNoticeManager.class);
					BpmTaskNotice bpmTaskNotice = noticeManager.get(id);// 根据主键ID获取传阅任务
					BpmDefinition bpmDefinition = bpmDefinitionManager.getById(bpmTaskNotice.getProcDefId());
					if (bpmTaskNotice.getIsRead() == 1 && "false".equals(bpmDefinition.getIsReadRevoke())) {
						break;// 传阅接收人已阅，无法撤回
					} else {
						if (bpmTaskNotice.getIsRead() == 1 && "true".equals(bpmDefinition.getIsReadRevoke())) {
							bpmTaskNoticeDoneManager.delBpmTaskNoticeDoneById(id);// 删除知会已办传阅任务
						}
						bpmTaskNotice.setIsRevoke(1);
						noticeManager.update(bpmTaskNotice);
					}
				}
			}
			ResultMessage message = new ResultMessage(ResultMessage.SUCCESS, "撤回成功");
			return message;
		} else if (OpinionStatus.DELIVERTO.getKey().equals(checkOpinion.getStatus())) {
			// 转办撤回
			bpmAgentService.retrieveTask(taskId, informType, cause);
			ResultMessage message = new ResultMessage(ResultMessage.SUCCESS, "撤回成功");
			return message;
		} else {
			/*
			 * 驳回发起人允许 // 验证任务是否已经在发起节点。 boolean rtn =
			 * validTask(instance.getBpmnInstId(), nodeDefs.get(0)); if (!rtn) {
			 * ResultMessage message = new ResultMessage(ResultMessage.FAIL,
			 * "任务已在发起节点,不能再撤销!"); return message; }
			 */

			if (!(type.getKey().equals(NodeType.USERTASK.getKey())
					|| type.getKey().equals(NodeType.SIGNTASK.getKey()))) {
				throw new BaseException("撤回失败，非（用户任务节点和会签节点）不允许撤回");

			}
			//网关内驳回撤回，会导致另一条分支的待办受影响，暂时不允许撤回
			Map<String, BpmNodeDef> inGatewayNodeMap = BpmUtil.getInGatewayNodeMap(bpmDefinitionAccessor.getBpmProcessDef(prcoDefId));
			if (inGatewayNodeMap.containsKey(checkOpinion.getTaskKey())) {
				throw new BaseException("撤回失败，网关内不允许撤回");
			}
			List<DefaultBpmCheckOpinion> listOpinions = new ArrayList<DefaultBpmCheckOpinion>();
			/**
			 * 驳回 撤回 处理 如 用户任务5 驳回发起人 用户任务5 撤回 不需要从堆栈中获取驳回的节点
			 */
			boolean backRevoke = false;
			BpmExeStackExecutor stackExecutor = bpmExeStackExecutorManager.getByTaskId(taskId);
			if (("back".equals(instance.getStatus()) || "backToStart".equals(instance.getStatus())
					|| "revoke".equals(instance.getStatus())) && BeanUtils.isEmpty(stackExecutor)) {
				listOpinions = bpmCheckOpinionManager.getByInstNodeIdStatus(instId, revokeNodeId, null);
				backRevoke = true;
				restoreStackData(instId, revokeNodeId);
			} else {
				// 2.找到所在节点的前继节点

				BpmExeStackRelation relation = relationManager.getByToStackId(stackExecutor.getStackId());
				if (BeanUtils.isNotEmpty(relation) && !relation.getFromNodeType().equals("userTask")
						&& !NodeType.EXCLUSIVEGATEWAY.getKey().equals(relation.getFromNodeType())
						&& !relation.getFromNodeType().equals("signTask")) {
					// 是谁发过来的如果不是用户任务节点，不充许撤回
					// 异常 回滚数据
					throw new BaseException("撤回失败，有网关节点不允许撤回");
				}
				while (NodeType.EXCLUSIVEGATEWAY.getKey().equals(relation.getFromNodeType())) {
					relation = relationManager.getByToStackId(relation.getFromStackId());
				}

				// 4.判断前继节点的处理人是否为当前登录者
				listOpinions = bpmCheckOpinionManager.getByInstNodeIdAgree(instId, relation.getFromNodeId());
				while (BeanUtils.isEmpty(listOpinions) && BeanUtils.isNotEmpty(relation)) {
					String fromStackId = relation.getFromStackId();
					String fromNodeId = relation.getFromNodeId();
					relation = relationManager.getByToStackId(relation.getFromStackId());
					if (BeanUtils.isNotEmpty(relation)) {
						listOpinions = bpmCheckOpinionManager.getByInstNodeIdAgree(instId, relation.getFromNodeId());
					} else {
						BpmExeStack stack = bpmExeStackManager.get(fromStackId);
						if (BeanUtils.isNotEmpty(stack) && "0".equals(stack.getParentId())
								&& StringUtil.isNotEmpty(fromNodeId)) {
							List<DefaultBpmCheckOpinion> fromOpinions = bpmCheckOpinionManager.getByInstNodeId(instId,
									fromNodeId);
							if (BeanUtils.isNotEmpty(fromOpinions)) {
								for (DefaultBpmCheckOpinion opinion : fromOpinions) {
									if (OpinionStatus.SKIP.getKey().equals(opinion.getStatus())) {
										listOpinions.add(opinion);
										relation = relationManager.getByFromStackId(fromStackId);
									}
								}
							}
						}
					}
				}
				revokeNodeId = relation.getFromNodeId();
			}

			boolean isCanRecall = false;
			for (DefaultBpmCheckOpinion defaultBpmCheckOpinion : listOpinions) {
				if (ContextUtil.getCurrentUser().getUserId().equals(defaultBpmCheckOpinion.getAuditor())) {
					isCanRecall = true;
					break;
				}
			}
			if (isCanRecall) {
				BpmIdentity bpmIdentity = DefaultBpmIdentity.getIdentityByUserId(
						ContextUtil.getCurrentUser().getUserId(), ContextUtil.getCurrentUser().getFullname());
				if (StringUtil.isEmpty(cause)) {
					cause = "撤回";
				}
				// 更新原任务状态为撤回
				BpmCheckOpinionUtil.updateCheckRevoker(revokeTaskId);

				// 1. 如果时会签本身节点撤回 使用直来直往
				// 如果当前运行的任务为会签任务 并且 撤回的节点 也是 当前运行任务的节点, 会签并行 则为会签审批撤回
				// 如果会签未串行还是按照常规撤回方式
				if (node.getType().equals(NodeType.SIGNTASK) && node.getNodeId().equals(revokeNodeId)
						&& ((SignNodeDef) node).isParallel()) {
					// 会签审批撤回 使用加签功能实现
					String addSignUserId[] = new String[] { ContextUtil.getCurrentUserId() };
					signService.addSignTask(taskId, addSignUserId);
				} else {
					// 常规撤回
					// 调用驳回方式撤回
					DefaultTaskFinishCmd cmd = getCmdFromRecall(taskId, "reject",
							OpinionStatus.SIGN_RECOVER_CANCEL.getValue(), "normal", revokeNodeId, bpmIdentity);
					if (!backRevoke) {
						// 判断是否允许按流程图执行进行驳回
						List<BpmNodeDef> listBpmNodeDef = BpmStackRelationUtil.getHistoryListBpmNodeDef(instId, nodeId,
								"pre");
						if (node.getType().equals(NodeType.SIGNTASK)) {
							listBpmNodeDef.add(node);
						}
						boolean isCanReject = false;
						List<BpmExeStackRelation> relationList = relationManager.getListByProcInstId(instId);
						for (BpmNodeDef itemNode : listBpmNodeDef) {
							if (!(itemNode.getType().equals(NodeType.USERTASK)
									|| itemNode.getType().equals(NodeType.SIGNTASK)))
								continue;

							boolean isHavePre = BpmStackRelationUtil.isHaveAndOrGateway(instId, node.getNodeId(), "pre",
									relationList);
							boolean isHaveAfter = BpmStackRelationUtil.isHaveAndOrGateway(instId, node.getNodeId(),
									"after", relationList);
							isCanReject = !(isHavePre && isHaveAfter) && revokeNodeId.equals(itemNode.getNodeId());
							if (isCanReject)
								break;

						}
						if (!isCanReject) {
							// 异常 回滚数据
							throw new BaseException("撤回失败，当前节点状态下不允许撤回");
						}
					}

					bpmTaskActionService.finishTask(cmd);
					DefaultBpmProcessInstance processInstance = bpmProcessInstanceManager.get(instId);

					// List<IUser> listUsers = new ArrayList<IUser>();
					// IUser user =
					// userService.getUserById(runningTask.getOwnerId());
					// listUsers.add(user);
					// BpmProcessInstanceManagerImpl.notifyUsers(listUsers,
					// instance, informType,
					// cause);
					// 更新流程实例状态。
					processInstance.setStatus(ProcessInstanceStatus.STATUS_REVOKE.getKey());
					bpmProcessInstanceManager.update(processInstance);
					// 撤回任务时根据任务ID撤回传阅
					if (StringUtil.isNotEmpty(taskId)) {// 要撤回任务的下一个任务的任务ID
						String id = "";// 传阅任务主键ID
						List<BpmTaskNotice> list1 = bpmTaskNoticeManager.getBpmTaskNoticeByTaskId(taskId);
						for (int i = 0; i < list1.size(); i++) {
							id = list1.get(i).getId();
							BpmTaskNoticeManager noticeManager = AppUtil.getBean(BpmTaskNoticeManager.class);
							BpmTaskNotice bpmTaskNotice = noticeManager.get(id);// 根据主键ID获取传阅任务
							BpmDefinition bpmDefinition = bpmDefinitionManager.getById(bpmTaskNotice.getProcDefId());
							if (bpmTaskNotice.getIsRead() == 1 && "false".equals(bpmDefinition.getIsReadRevoke())) {
								break;// 传阅接收人已阅，无法撤回
							} else {
								if (bpmTaskNotice.getIsRead() == 1 && "true".equals(bpmDefinition.getIsReadRevoke())) {
									bpmTaskNoticeDoneManager.delBpmTaskNoticeDoneById(id);// 删除知会已办传阅任务
								}
								bpmTaskNotice.setIsRevoke(1);
								noticeManager.update(bpmTaskNotice);
							}
						}
					}
					if (StringUtil.isNotEmpty(revokeTaskId)) {// 要撤回任务的任务ID
						String id = "";// 传阅任务主键ID
						List<BpmTaskNotice> list1 = bpmTaskNoticeManager.getBpmTaskNoticeByTaskId(revokeTaskId);
						for (int i = 0; i < list1.size(); i++) {
							id = list1.get(i).getId();
							BpmTaskNoticeManager noticeManager = AppUtil.getBean(BpmTaskNoticeManager.class);
							BpmTaskNotice bpmTaskNotice = noticeManager.get(id);// 根据主键ID获取传阅任务
							BpmDefinition bpmDefinition = bpmDefinitionManager.getById(bpmTaskNotice.getProcDefId());
							if (bpmTaskNotice.getIsRead() == 1 && "false".equals(bpmDefinition.getIsReadRevoke())) {
								break;// 传阅接收人已阅，无法撤回
							} else {
								if (bpmTaskNotice.getIsRead() == 1 && "true".equals(bpmDefinition.getIsReadRevoke())) {
									bpmTaskNoticeDoneManager.delBpmTaskNoticeDoneById(id);// 删除知会已办传阅任务
								}
								bpmTaskNotice.setIsRevoke(1);
								noticeManager.update(bpmTaskNotice);
							}
						}
					}

				}

				ResultMessage message = new ResultMessage(ResultMessage.SUCCESS, "撤回成功");
				return message;
			} else {
				// 异常 回滚数据
				throw new BaseException("撤回失败，下个节点任务已被处理，不可撤回！");
			}
		}
	}

	// 处理撤回征询时的审批记录
	private void handleRevokeInquCheckOpinion(DefaultBpmTask bpmTask, DefaultBpmCheckOpinion bpmCheckOpinion,
			String cause) {
		String taskId = bpmCheckOpinion.getTaskId();
		String curUserId = ContextUtil.getCurrentUser().getUserId();
		String curUserName = ContextUtil.getCurrentUser().getFullname();

		// 1.添加一条撤回的审批记录
		BpmCheckOpinionUtil.addCheckOpinion(bpmTask, OpinionStatus.REVOKER, curUserId, cause, true);

		List<DefaultBpmCheckOpinion> childrenOpinions = bpmCheckOpinionManager.getByParentId(taskId);
		for (DefaultBpmCheckOpinion childOpinion : childrenOpinions) {
			// 2.更新流转子任务产生的审批记录
			if (BeanUtils.isNotEmpty(childOpinion)) {
				childOpinion.setStatus(OpinionStatus.SIGN_RECOVER_CANCEL.getKey());
				childOpinion.setCompleteTime(LocalDateTime.now());
				childOpinion.setDurMs(
						TimeUtil.getCurrentTimeMillis() - TimeUtil.getTimeMillis(childOpinion.getCreateTime()));
				childOpinion.setOpinion(cause);
				childOpinion.setAuditor(curUserId);
				childOpinion.setAuditorName(curUserName);
				bpmCheckOpinionManager.update(childOpinion);
			}
		}

		// 3.修改流转源的审批历史为审批中状态
		bpmCheckOpinion.setStatus(OpinionStatus.AWAITING_CHECK.getKey());
		bpmCheckOpinion.setCreateTime(LocalDateTime.now());
		bpmCheckOpinion.setOpinion("");
		bpmCheckOpinionManager.update(bpmCheckOpinion);
	}

	// 驳回撤回时 恢复堆栈数据
	private void restoreStackData(String instId, String revokeNodeId) {
		if (BeanUtils.isNotEmpty(bpmExeStackManager.getHisByInstId(instId))) {
			bpmExeStackManager.his2StackByInstId(instId);
			bpmExeStackManager.his2StackRelationByInstId(instId);
			bpmExeStackManager.updateTagertNode(instId, revokeNodeId);
			bpmExeStackManager.removeHisByInstId(instId);
			relationManager.removeHisByInstId(instId);
		}
	}

	@Override
	public Map<String, List<BpmIdentity>> getNextTaskUsers(String taskId) throws Exception {
		DefaultBpmTask bpmTask = bpmTaskManager.get(taskId);
		if (BeanUtils.isEmpty(bpmTask)) {
			throw new RuntimeException("任务不存在或已被处理！");
		}
		BpmProcessInstance bpmProcessInstance = bpmInstService.getProcessInstance(bpmTask.getProcInstId());
		String defId = bpmProcessInstance.getProcDefId();// 流程定义id
		String nodeId = bpmTask.getNodeId();// 任务节点id
		BpmNodeDef taskNodeDef = bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);
		List<BpmNodeDef> nodes = taskNodeDef.getOutcomeNodes();
		Map<String, List<BpmIdentity>> outcomeUserMap = new HashMap<String, List<BpmIdentity>>();
		List<BpmNodeDef> handlerSelectOutcomeNodes = handlerSelectOutcomeNodes(nodes);
		for (BpmNodeDef bpmNodeDef : handlerSelectOutcomeNodes) {
			List<BpmIdentity> bpmIdentitys = bpmIdentityService.searchByNode(bpmTask.getProcInstId(),
					bpmNodeDef.getNodeId());
			outcomeUserMap.put(bpmNodeDef.getNodeId(), bpmIdentitys);
		}
		return outcomeUserMap;
	}

	// 处理选择路径跳转的分支出口
	private List<BpmNodeDef> handlerSelectOutcomeNodes(List<BpmNodeDef> outcomeNodes) {
		int size = outcomeNodes.size();
		List<BpmNodeDef> returnList = new ArrayList<BpmNodeDef>();
		if (size == 1) {
			BpmNodeDef bpmNodeDef = outcomeNodes.get(0);
			NodeType nodeType = bpmNodeDef.getType();
			// 网关节点
			if (NodeType.EXCLUSIVEGATEWAY.equals(nodeType) || NodeType.INCLUSIVEGATEWAY.equals(nodeType)
					|| NodeType.PARALLELGATEWAY.equals(nodeType)) {
				returnList = bpmNodeDef.getOutcomeNodes();
			}
		}
		if (BeanUtils.isEmpty(returnList)) {
			return outcomeNodes;
		} else {
			return returnList;
		}
	}

	@Override
	@Transactional
	public CommonResult<String> setTaskExecutors(ModifyExecutorsParamObject modifyExecutorsParamObject)
			throws Exception {
		String taskId = modifyExecutorsParamObject.getTaskId();
		String[] userIds = modifyExecutorsParamObject.getUserIds();
		String messageType = modifyExecutorsParamObject.getMessageType();
		String opinion = modifyExecutorsParamObject.getCause();
		if (StringUtil.isEmpty(taskId)) {
			throw new RequiredException(ResponseErrorEnums.REQUIRED_ERROR.getMessage() + ":任务id必填！");
		}
		if (BeanUtils.isEmpty(userIds)) {
			throw new RequiredException(ResponseErrorEnums.REQUIRED_ERROR.getMessage() + ":userIds任务执行人id必填！");
		}
		if (StringUtil.isEmpty(messageType)) {
			throw new RequiredException(ResponseErrorEnums.REQUIRED_ERROR.getMessage() + ":messageType通知方式必填！");
		}
		if (StringUtil.isEmpty(opinion)) {
			throw new RequiredException(ResponseErrorEnums.REQUIRED_ERROR.getMessage() + ":cause通知内容比必填！");
		}
		bpmTaskManager.setTaskExecutors(taskId, userIds, messageType, opinion);
		return new CommonResult<String>(true, "修改任务执行人成功", "");
	}

	@Override
	public Boolean isAllowAddSign(IsAllowAddSignObject isAllowAddSignObject) throws Exception {
		if (StringUtil.isEmpty(isAllowAddSignObject.getTaskId())) {
			throw new RuntimeException("taskId没有设置值");
		}

		if (StringUtil.isEmpty(isAllowAddSignObject.getUserId())
				&& StringUtil.isEmpty(isAllowAddSignObject.getAccount())) {
			throw new RuntimeException("用户id或者用户账号两者不能同时为空");
		}

		String userId = "";
		if (StringUtil.isNotEmpty(isAllowAddSignObject.getUserId())) {
			userId = isAllowAddSignObject.getUserId();
		} else {
			IUser user = ServiceUtil.getUserByAccount(isAllowAddSignObject.getAccount());
			if (BeanUtils.isEmpty(user)) {
				throw new RuntimeException("用户不存在");
			}
			userId = user.getUserId();
		}

		String taskId = isAllowAddSignObject.getTaskId();
		DefaultBpmTask task = bpmTaskManager.get(taskId);

		String defId = task.getProcDefId();
		String nodeId = task.getNodeId();

		BpmNodeDef bpmNodeDef = bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);

		if (!(bpmNodeDef instanceof SignNodeDef)) {
			throw new RuntimeException("该任务不是会签任务");
		}

		NatTaskService natTaskService = (NatTaskService) AppUtil.getBean(NatTaskService.class);
		SignService signService = (SignService) AppUtil.getBean(SignService.class);
		Map<String, Object> variables = natTaskService.getVariables(taskId);
		List<PrivilegeMode> privilege = signService.getPrivilege(userId, (SignNodeDef) bpmNodeDef, variables);
		if (privilege.contains(PrivilegeMode.ALL) || privilege.contains(PrivilegeMode.ALLOW_ADD_SIGN)) {
			return true;
		}
		return false;
	}

	@Override
	@Transactional
	public CommonResult<String> taskToTrans(TaskTransParamObject taskTransParamObject) throws Exception {
		String notifyType = taskTransParamObject.getNotifyType();
		String opinion = taskTransParamObject.getOpinion();
		String userIds = taskTransParamObject.getUserIds();
		String currentUserId = ContextUtil.getCurrentUserId();
		String formData = taskTransParamObject.getData();
		if (StringUtil.isEmpty(userIds)) {
			throw new RequiredException(ResponseErrorEnums.REQUIRED_ERROR.getMessage() + ":userIds流转人员id必填");
		}
		if (StringUtil.isEmpty(opinion)) {
			throw new RequiredException(ResponseErrorEnums.REQUIRED_ERROR.getMessage() + ":opinion流转意见必填");
		}
		String[] userIdArray = userIds.split(",");

		List<IUser> userList = new ArrayList<IUser>();
		for (String id : userIdArray) {
			IUser u = userServiceImpl.getUserById(id);
			if (u != null)
				userList.add(u);
			if (currentUserId.equals(u.getUserId())) {
				throw new RuntimeException("流转人员不能包含本人！");
			}
		}
		ObjectNode transObject = (ObjectNode) JsonUtil.toJsonNode(taskTransParamObject.toString());
		BpmTaskTrans taskTrans = JsonUtil.toBean(transObject, BpmTaskTrans.class);
		
		DefaultBpmTask defaultBpmTask = bpmTaskManager.get(taskTrans.getTaskId());
		if (BeanUtils.isEmpty(defaultBpmTask)) {
			throw new NotFoundException("任务不存在可能已经被处理了");
		}
		String procInstId = defaultBpmTask.getProcInstId();
		DefaultBpmProcessInstance defaultBpmProcessInstance = bpmProcessInstanceManager.get(procInstId);
		// 发起流转时，更新流程实例的状态为运行中，避免先驳回后流转时流程实例状态认为驳回状态的问题。
		bpmProcessInstanceManager.updateStatusByInstanceId(procInstId,ProcessInstanceStatus.STATUS_RUNNING.getKey());
		String busData = Base64.getFromBase64(formData);
		if (StringUtil.isNotEmpty(formData)) {
			BpmProcessDef<BpmProcessDefExt> bpmProcessDef = bpmDefinitionAccessor.getBpmProcessDef(defaultBpmTask.getProcDefId());
			BpmProcessDefExt processDefExt = bpmProcessDef.getProcessDefExt();
			List<ProcBoDef> boDefList = processDefExt.getBoDefList();
			if (BeanUtils.isNotEmpty(boDefList)) {
				DefaultProcessInstCmd processCmd = new DefaultProcessInstCmd();
				processCmd.setBusData(busData);
				BusDataUtil.handSaveBoData(defaultBpmProcessInstance, processCmd);
			}
		}
        String files = taskTransParamObject.getFiles();
        taskTransService.addTransTask(taskTrans, userList, notifyType, opinion, formData,files,false);
		return new CommonResult<String>(true, "流程流转成功", "");
	}

    @Override
    @Transactional
    public CommonResult<String> userTaskToSign(TaskTransParamObject taskTransParamObject) throws Exception {
        String notifyType = taskTransParamObject.getNotifyType();
        String opinion = taskTransParamObject.getOpinion();
        String userIds = taskTransParamObject.getUserIds();
        String currentUserId = ContextUtil.getCurrentUserId();
        String formData = taskTransParamObject.getData();
        if (StringUtil.isEmpty(userIds)) {
            throw new RequiredException(ResponseErrorEnums.REQUIRED_ERROR.getMessage() + ":userIds加签人员id必填");
        }
        if (StringUtil.isEmpty(opinion)) {
            throw new RequiredException(ResponseErrorEnums.REQUIRED_ERROR.getMessage() + ":opinion加签意见必填");
        }
        String[] userIdArray = userIds.split(",");

        List<IUser> userList = new ArrayList<IUser>();
        for (String id : userIdArray) {
            IUser u = userServiceImpl.getUserById(id);
            if (u != null)
                userList.add(u);
            if (currentUserId.equals(u.getUserId())) {
                throw new RuntimeException("加签人员不能包含本人！");
            }
        }
        ObjectNode transObject = (ObjectNode) JsonUtil.toJsonNode(taskTransParamObject.toString());
        BpmTaskTrans taskTrans = JsonUtil.toBean(transObject, BpmTaskTrans.class);
        if (StringUtil.isNotEmpty(formData)) {
            DefaultBpmTask defaultBpmTask = bpmTaskManager.get(taskTrans.getTaskId());
            if (BeanUtils.isEmpty(defaultBpmTask)) {
                throw new NotFoundException("任务不存在可能已经被处理了");
            }
            String procInstId = defaultBpmTask.getProcInstId();
            DefaultBpmProcessInstance defaultBpmProcessInstance = bpmProcessInstanceManager.get(procInstId);
            // 发起加签时，更新流程实例的状态为运行中，避免先驳回后流转时流程实例状态认为驳回状态的问题。
            bpmProcessInstanceManager.updateStatusByInstanceId(procInstId,
                    ProcessInstanceStatus.STATUS_RUNNING.getKey());
            String busData = Base64.getFromBase64(formData);
            DefaultProcessInstCmd processCmd = new DefaultProcessInstCmd();
            processCmd.setBusData(busData);
            BusDataUtil.handSaveBoData(defaultBpmProcessInstance, processCmd);
        }
        String files = taskTransParamObject.getFiles();
        taskTransService.addTransTask(taskTrans, userList, notifyType, opinion, formData,files,true);
        return new CommonResult<String>(true, "任务加签成功", "");
    }

	@Override
	public String getUrlFormByTaskId(String taskId, String formType) throws Exception {
		DefaultBpmTask task = bpmTaskManager.getByTaskId(taskId);
		if (BeanUtils.isEmpty(task)) {
			throw new RuntimeException("任务不存在或已被处理！");
		}
		String defId = task.getProcDefId();
		String nodeId = task.getNodeId();

		BpmProcessInstance bpmProcessInstance = bpmInstService.getProcessInstance(task.getProcInstId());
		BpmFormService bpmFormService = BpmFormFactory.getFormService(FormType.fromValue(formType));
		FormModel formModel = bpmFormService.getByDefId(defId, nodeId, bpmProcessInstance, true);
		if (formModel == null || formModel.isFormEmpty() || FormCategory.INNER.equals(formModel.getType())) {
			return "";
		}
		return formModel.getFormValue();
	}

	@Override
	public String getInstUrlForm(String proInstId, String nodeId, String formType) throws Exception {
		BpmFormService bpmFormService = BpmFormFactory.getFormService(FormType.fromValue(formType));
		DefaultBpmProcessInstance instance = bpmProcessInstanceManager.get(proInstId);
		FormModel formModel = bpmFormService.getInstFormByDefId(instance);
		if (BeanUtils.isNotEmpty(formModel) && StringUtil.isNotEmpty(formModel.getFormId())) {
			if (StringUtil.isNotEmpty(nodeId)) {
				FormModel formModelTmp = bpmFormService.getByDefId(instance.getProcDefId(), nodeId, instance, false);
				if (formModelTmp != null && !formModelTmp.isFormEmpty())
					formModel = formModelTmp;
			}

			if (formModel == null || formModel.isFormEmpty() || FormCategory.INNER.equals(formModel.getType())) {
				return "";
			}
		}
		return formModel.getFormValue();
	}

	@Override
	@Transactional
	public CommonResult<String> communicate(CommunicateParamObject communicateParamObject) throws Exception {
		String curid = ContextUtil.getCurrentUserId();
		String instId = communicateParamObject.getInstId();
		String defId = communicateParamObject.getDefId();
		if (StringUtil.isEmpty(instId)) {
			return new CommonResult<String>(false, "流程实例ID不能为空！");
		}
		String userIds = communicateParamObject.getUserId();
		String communicateReason = communicateParamObject.getOpinion();
		String messageType = communicateParamObject.getMessageType();
		if (StringUtil.isEmpty(userIds)) {
			throw new ApproveTaskException("必须传入沟通用户id");
		}
		String[] userIdAry = userIds.split(",");
		List<IUser> userIdList = new ArrayList<IUser>();
		for (String userId : userIdAry) {
			if (curid.equals(userId)) {
				return new CommonResult<String>(false, "不能对自己发起沟通");
			}
			IUser userByAccount = ServiceUtil.getUserById(userId);
			if (BeanUtils.isEmpty(userByAccount)) {
				throw new RuntimeException(String.format("沟通用户账号(userAccount):%s不存在", userId));
			}
			userIdList.add(userByAccount);
		}
		taskCommuService.addCommuTask(instId, messageType, communicateReason, userIdList,
				communicateParamObject.getFiles(), defId);
		return new CommonResult<String>(true, "沟通成功");
	}

	@Override
	public PageList<DefaultBpmProcessInstance> getMyRequestListAll(String account, QueryFilter queryFilter)
			throws Exception {
		IUser user = ServiceUtil.getUserByAccount(account);
		queryFilter.withQuery(new QueryField("create_by_", user.getUserId(), QueryOP.EQUAL));
		queryFilter.withQuery(new QueryField("status_", "draft", QueryOP.NOT_EQUAL));
		// 查询列表
		try {
			PageList<DefaultBpmProcessInstance> list = (PageList<DefaultBpmProcessInstance>) bpmProcessInstanceManager
					.query(queryFilter);
			return list;
		} catch (Exception e) {
			throw new RuntimeException("数据库查询出错了！");
		}
	}

	@Override
	public PageList<DefaultBpmDefinition> getBpmDefList(String account, QueryFilter queryFilter) throws Exception {
		return (PageList<DefaultBpmDefinition>) bpmDefinitionManager.query(queryFilter);
	}

	@Override
	public PageList<DefaultBpmDefinition> newProcess(String account, QueryFilter queryFilter) throws Exception {
		queryFilter.addFilter("is_main_", "Y", QueryOP.EQUAL, FieldRelation.AND, "a");
		queryFilter.addFilter("status_", "deploy", QueryOP.EQUAL, FieldRelation.AND, "a");
		queryFilter.getParams().put("bpmDefAuthorizeRightType", BPMDEFAUTHORIZE_RIGHT_TYPE.START);

		return (PageList<DefaultBpmDefinition>) bpmDefinitionManager.queryList(queryFilter);
	}

	@Override
	public List<Map<String, Object>> newProcessCount(QueryFilter queryFilter) throws Exception {
		queryFilter.addFilter("is_main_", "Y", QueryOP.EQUAL, FieldRelation.AND, "a");
		queryFilter.addFilter("status_", "deploy", QueryOP.EQUAL, FieldRelation.AND, "a");
		queryFilter.getParams().put("bpmDefAuthorizeRightType", BPMDEFAUTHORIZE_RIGHT_TYPE.START);

		return bpmDefinitionManager.getDefCount(queryFilter);
	}

	@Override
	public PageList<DefaultBpmProcessInstance> myRequest(String account, QueryFilter queryFilter) throws Exception {
		IUser user = ServiceUtil.getUserByAccount(account);
		IPage<DefaultBpmProcessInstance> list = bpmProcessInstanceManager.getMyRequestByUserId(user.getUserId(),
				queryFilter);
		PageList<DefaultBpmProcessInstance> pageList = new PageList<DefaultBpmProcessInstance>(list);
		List<DefaultBpmProcessInstance> listInst = new ArrayList<>();
		for (DefaultBpmProcessInstance entity : pageList.getRows()) {
			DefaultBpmProcessInstance defaultBpmProcessInstance = new DefaultBpmProcessInstance();
			defaultBpmProcessInstance = entity;
			if ("back".equals(entity.getStatus())) {// 判断我的请求流程实例数据是否是被驳回状态
				// 根据流程定义ID获取流程图所有任务节点
				TaskjImageVo taskNode = iFlowService.taskImage("", entity.getProcDefId());
				if (BeanUtils.isNotEmpty(taskNode)) {
					List<BpmNodeLayout> listLayout = taskNode.getBpmDefLayout().getListLayout();
					// 根据流程实例ID查询最新的一条审批记录任务ID
					List<String> listStr = bpmProcessInstanceManager.getNodeIdByInstId(entity.getId());
					if (BeanUtils.isNotEmpty(listStr)) {
						if (listLayout.get(1).getNodeId().equals(listStr.get(0))) {
							defaultBpmProcessInstance.setBackToStart(true);
						}
					}
				}
			}
			listInst.add(defaultBpmProcessInstance);
		}
		pageList.setRows(listInst);
		return pageList;
	}

	@Override
	public List<Map<String, Object>> myRequestCount(String account) throws Exception {
		IUser user = ServiceUtil.getUserByAccount(account);
		return bpmProcessInstanceManager.getMyRequestCount(user.getUserId());
	}

	@Override
	public PageList<DefaultBpmProcessInstance> getInstanceList(String account, QueryFilter queryFilter)
			throws Exception {
		queryFilter.setDefaultSort("createTime", Direction.DESC);
		return (PageList<DefaultBpmProcessInstance>) bpmProcessInstanceManager.query(queryFilter);
	}

	@Override
	public InstFormAndBoVo getInstFormAndBO(String proInstId, String nodeId, String formId, FormType formType, Boolean includData,
			Boolean getStartForm) throws Exception {
		BpmFormService bpmFormService = BpmFormFactory.getFormService(formType);
		InstFormAndBoVo instFormAndBoVo = new InstFormAndBoVo();
		DefaultBpmProcessInstance instance = bpmProcessInstanceManager.get(proInstId);

		if (StringUtil.isNotEmpty(nodeId)) {
			ContextThreadUtil.putCommonVars("nodeId", nodeId);
			ContextThreadUtil.putCommonVars("defId", instance.getProcDefId());
		}

		BpmProcessInstance topInstance = bpmProcessInstanceManager.getTopBpmProcessInstance(instance);

        String topDefKey = "";
        if (StringUtil.isNotZeroEmpty(topInstance.getParentInstId())) {
            topDefKey = topInstance.getProcDefKey();
        }
		FormModel formModel = null;
		if (!getStartForm) {
			formModel = bpmFormService.getByDefId(instance.getProcDefId(), nodeId, instance, false);
		}

		if ((BeanUtils.isEmpty(formModel) && ProcessInstanceStatus.STATUS_DRAFT.getKey().equals(instance.getStatus()))
				|| getStartForm) {
			BpmNodeForm nodeForm = null;
			nodeForm = bpmFormService.getByDefId(instance.getProcDefId());
			formModel = (FormModel) nodeForm.getForm();
		}

		if (BeanUtils.isNotEmpty(formModel)
				&& (StringUtil.isNotEmpty(formModel.getFormId()) || StringUtil.isNotEmpty(formModel.getFormValue()))) {
			if (StringUtil.isNotEmpty(nodeId)) {
				FormModel formModelTmp = bpmFormService.getByDefId(instance.getProcDefId(), nodeId, instance, false);
				if (formModelTmp != null && !formModelTmp.isFormEmpty())
					formModel = formModelTmp;
			}

			if (formModel == null || formModel.isFormEmpty()) {
				instFormAndBoVo.setResult("formEmpty");
				return instFormAndBoVo;
			}
			//表单打印模板
			if(StringUtil.isNotEmpty(formId)) {
				checkPrintForm(formModel, formId);
			}
			instFormAndBoVo.setForm(formModel);
			if (FormCategory.INNER.equals(formModel.getType())) {
				BpmProcessDef<BpmProcessDefExt> procDef = bpmDefinitionAccessor.getBpmProcessDef(topInstance.getProcDefId());
				String doneDataVersion = procDef.getProcessDefExt().getExtProperties().getDoneDataVersion();
				instFormAndBoVo.setDoneDataVersion(StringUtil.isEmpty(doneDataVersion)?"history":doneDataVersion);
				// 获取bo数据
				if (includData) {
					List<ObjectNode> boDatas = boDataService.getDataByInst(instance);
					ObjectNode jsondata = (ObjectNode) BoDataUtil.hanlerData(boDatas);
					instFormAndBoVo.setData(jsondata);
					ObjectNode opinionJson = boDataService.getFormOpinionJson(proInstId);
					instFormAndBoVo.setOpinionList(opinionJson);
				}
				String curUserId = ContextUtil.getCurrentUserId();
				// 获取最外层的权限。
				ObjectNode formRestParams = JsonUtil.getMapper().createObjectNode();
				formRestParams.put("formkey", formModel.getFormKey());
				formRestParams.put("userId", curUserId);
				formRestParams.put("parentFlowKey", topDefKey);
				formRestParams.put("flowKey", topInstance.getProcDefKey());
                String permission = "";
				if("mobile".equals(formType.value())){
                    permission = formRestfulService.getInstPermission(formRestParams);
                }else{
                    formRestParams.put("nodeId", nodeId);
                    permission = formRestfulService.getPermission(formRestParams);
                }
				instFormAndBoVo.setPermission(permission);
			}
		}
		return instFormAndBoVo;
	}
	
	private void checkPrintForm(FormModel formModel, String formId) throws Exception{
		ObjectNode byFormKey = formService.getByFormId(formId);
		if(BeanUtils.isNotEmpty(byFormKey)) {
			String formStr = byFormKey.toString();
			if(BeanUtils.isNotEmpty(formStr)) {
				BpmForm bpmForm = JsonUtil.toBean(formStr, BpmForm.class);
				formModel.setFormHtml(bpmForm.getFormHtml());
				formModel.setFormKey(bpmForm.getFormKey());
				formModel.setFormId(bpmForm.getFormId());
			}
		}
	}

	@Override
	public FormAndBoVo getFormAndBO(StartCmdParam startCmdParam, FormType formType) throws Exception {
		BpmFormService bpmFormService = BpmFormFactory.getFormService(formType);

		FormAndBoVo formAndBoVo = new FormAndBoVo();

		DefaultBpmProcessInstance proInstance = null;

		BpmNodeForm nodeForm = null;
		if (StringUtil.isEmpty(startCmdParam.getProInstId())) {
			nodeForm = bpmFormService.getByDefId(startCmdParam.getDefId());
		} else {
			proInstance = bpmProcessInstanceManager.get(startCmdParam.getProInstId());
			nodeForm = bpmFormService.getByDraft(proInstance);
		}

		if (nodeForm == null) {
			formAndBoVo.setResultMsg("formEmpty");
			return formAndBoVo;
		}
		if (StringUtil.isEmpty(startCmdParam.getDefId())) {
			startCmdParam.setDefId(proInstance.getProcDefId());
		}

		FormModel formModel = (FormModel) nodeForm.getForm();
		//
		formAndBoVo.setForm(formModel);

		BpmNodeDef bpmNodeDef = nodeForm.getBpmNodeDef();

		List<Button> buttons = BpmUtil.getButtons(bpmNodeDef);
		// 按钮
		formAndBoVo.setButtons(buttons);

		if (FormCategory.INNER.equals(formModel.getType())) {
			ObjectNode jsondata = JsonUtil.getMapper().createObjectNode();

			DefaultTaskFinishCmd cmd = new DefaultTaskFinishCmd();
			cmd.setVariables(this.getStartCmd(startCmdParam).getVariables());
			ContextThreadUtil.setActionCmd(cmd);
			// 从草稿发起流程。
			if (StringUtil.isNotEmpty(startCmdParam.getProInstId())) {
				List<ObjectNode> boDatas = boDataService.getDataByInst(proInstance);
				jsondata = BoDataUtil.hanlerData(startCmdParam.getDefId(), boDatas);
			} else {
				List<ObjectNode> boDatas = boDataService.getDataByDefId(startCmdParam.getDefId());
				jsondata = BoDataUtil.hanlerData(startCmdParam.getDefId(), boDatas);
			}
			formAndBoVo.setData(jsondata);

			// 获取流程启动时的表单权限
			ObjectNode formRestParams = JsonUtil.getMapper().createObjectNode();
			formRestParams.put("formkey", formModel.getFormKey());
			formRestParams.put("flowKey", bpmNodeDef.getBpmProcessDef().getDefKey());
			formRestParams.put("nodeId", bpmNodeDef.getNodeId());
			formRestParams.put("nextNodeId", bpmNodeDef.getOutcomeNodes().get(0).getNodeId());
			String permission = formRestfulService.getStartPermission(formRestParams);

			formAndBoVo.setPermission(permission);

		}

		// 获取开始节点的配置信息 用与配置路径跳转
		BpmNodeDef startEvent = bpmDefinitionAccessor.getStartEvent(startCmdParam.getDefId());
		NodeProperties startNodeProp = startEvent.getLocalProperties();

		formAndBoVo.setJumpType(startNodeProp.getJumpType());
		return formAndBoVo;
	}

	@Override
	public DefaultProcessInstCmd getStartCmd(StartCmdParam startCmdParam) throws Exception {
		String proInstId = startCmdParam.getProInstId();
		String defId = startCmdParam.getDefId();
		// 是否由选择人员做为下一节点处理人
		int isSendNodeUsers = startCmdParam.getIsSendNodeUsers();

		BpmDefinition bpmDefinition = bpmDefinitionService.getBpmDefinitionByDefId(defId);

		// TODO 判断流程状态，权限
		String defKey = bpmDefinition.getDefKey();
		// 目标节点
		String destination = startCmdParam.getDestination();
		// 节点执行人 [{nodeId:"userTask1",executors:[{id:"",type:"org,user,pos",
		// name:""},{id:"",type:"org,user,pos",name:""}]}]
		String nodeUsers = startCmdParam.getNodeUsers();
		// 流程表单数据。
		String busData = startCmdParam.getBusData();
		String formType = startCmdParam.getFormType();

		Map<String, List<BpmIdentity>> specUserMap = BpmIdentityUtil.getBpmIdentity(nodeUsers);

		DefaultProcessInstCmd cmd = new DefaultProcessInstCmd();

		cmd.setFlowKey(defKey);
		if (StringUtil.isNotEmpty(proInstId)) {
			cmd.setInstId(proInstId);
		}

		// cmd.setBusinessKey(UniqueIdUtil.getSuid());
		cmd.setDestination(destination);
		cmd.setBusData(busData);
		if (FormCategory.INNER.value().equals(formType)) {
			cmd.setDataMode(ActionCmd.DATA_MODE_BO);
		} else {
			cmd.setDataMode(ActionCmd.DATA_MODE_PK);
		}
		// 指定执行人
		if (BeanUtils.isNotEmpty(specUserMap)) {
			cmd.addTransitVars(BpmConstants.BPM_NODE_USERS, specUserMap);
			if (isSendNodeUsers == 1) {
				cmd.setBpmIdentities(specUserMap);
			}
		}

		Map<String, Object> params = getProcessStartVars(startCmdParam.getReqValue(), defId);

		cmd.setVariables(params);
		// 添加基础变量，防止开始节点设置设置脚本事件用到
		cmd.addVariable(BpmConstants.PROCESS_DEF_ID, defId);
		cmd.addVariable(BpmConstants.BPM_FLOW_KEY, defKey);
		cmd.addVariable(BpmConstants.START_USER, ContextUtil.getCurrentUserId());

		return cmd;
	}

	private Map<String, Object> getProcessStartVars(String reqParams, String defId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		List<BpmVariableDef> list = bpmDefinitionService.getVariableDefs(defId);
		if (BeanUtils.isEmpty(list))
			return params;
		for (BpmVariableDef varDef : list) {
			if (BeanUtils.isEmpty(reqParams))
				return params;
			String reqValue = JsonUtil.toJsonNode(reqParams).get(varDef.getVarKey()).asText();
			if (StringUtil.isEmpty(reqValue) && varDef.getDefaultVal() != null
					&& StringUtil.isNotEmpty(varDef.getDefaultVal().toString())) {
				reqValue = varDef.getDefaultVal().toString();
			}
			if (StringUtil.isNotEmpty(reqValue)) {
				Object convertVal = DefaultBpmVariableDef.getValue(varDef.getDataType(), reqValue);
				params.put(varDef.getVarKey(), convertVal);
			}
		}
		return params;
	}

	@Override
	public SelectDestinationVo selectDestination(String defId) throws Exception {
		BpmNodeDef startEvent = bpmDefinitionAccessor.getStartEvent(defId);

		BpmNodeDef taskNodeDef = bpmDefinitionAccessor.getBpmNodeDef(defId, startEvent.getNodeId());

		NodeProperties nodeProperties = taskNodeDef.getLocalProperties();

		BpmProcessDef<BpmProcessDefExt> bpmProcessDef = bpmDefinitionAccessor.getBpmProcessDef(defId);
		boolean isSkipFirstNode = bpmProcessDef.getProcessDefExt().getExtProperties().isSkipFirstNode();

		String jumpType = "common";
		SelectDestinationVo selectDestinationVo = new SelectDestinationVo();
		if (nodeProperties != null) {
			jumpType = nodeProperties.getJumpType();

			if (jumpType.indexOf("select") > -1) {
				Map<String, Object> outcomeUserMap = new HashMap<>();
				if (isSkipFirstNode) {
					taskNodeDef = taskNodeDef.getOutcomeNodes().get(0);
				}
				List<BpmNodeDef> outcomeNodes = taskNodeDef.getOutcomeNodes();
				List<BpmNodeDef> handlerSelectOutcomeNodes = handlerSelectOutcomeNodes(outcomeNodes);
				for (BpmNodeDef bpmNodeDef : handlerSelectOutcomeNodes) {
					List<BpmIdentity> bpmIdentitys = bpmIdentityService.searchByNodeIdOnStartEvent(defId,
							bpmNodeDef.getNodeId());
					outcomeUserMap.put(bpmNodeDef.getNodeId(),
							bpmIdentityExtractService.extractBpmIdentity(bpmIdentitys));
				}
				selectDestinationVo.setOutcomeUserMap(outcomeUserMap);
				selectDestinationVo.setOutcomeNodes(handlerSelectOutcomeNodes);
			}
			if (jumpType.indexOf("free") > -1) {
				List<BpmNodeDef> allNodeDef = bpmDefinitionAccessor.getAllNodeDef(defId);
				allNodeDef = handlerSelectOutcomeNodes(allNodeDef);
				// 移除开始节点
				List<BpmNodeDef> removeList = new ArrayList<BpmNodeDef>();
				Map<String, Object> allNodeUserMap = new HashMap<>();
				for (BpmNodeDef bpmNodeDef : allNodeDef) {
					NodeType type = bpmNodeDef.getType();
					if (NodeType.START.equals(type) || NodeType.EXCLUSIVEGATEWAY.equals(type)
							|| NodeType.PARALLELGATEWAY.equals(type) || NodeType.END.equals(type)) {
						removeList.add(bpmNodeDef);
					} else if (NodeType.USERTASK.equals(bpmNodeDef.getType())
							|| NodeType.SIGNTASK.equals(bpmNodeDef.getType())) {
						List<BpmIdentity> bpmIdentitys = new ArrayList<BpmIdentity>();
						allNodeUserMap.put(bpmNodeDef.getNodeId(), bpmIdentitys);
					}
				}
				allNodeDef.removeAll(removeList);
				selectDestinationVo.setAllNodeDef(allNodeDef);
				selectDestinationVo.setAllNodeUserMap(allNodeUserMap);
			}
		}
		selectDestinationVo.setJumpType(jumpType);
		return selectDestinationVo;
	}

	@Override
	public FlowImageVo flowImage(String instanceId, String type, String from, String nodeId, String defId)
			throws Exception {
		FlowImageVo flowImageVo = new FlowImageVo();
		if (StringUtil.isEmpty(instanceId) && StringUtil.isEmpty(defId)) {
			return flowImageVo;
		}
		BpmProcessInstance bpmProcessInstance = null;
		List<BpmProcessInstance> bpmProcessInstanceList = new ArrayList<BpmProcessInstance>();
		BpmDefLayout bpmDefLayout = null;
		// 查子流程，则返回子流程的实例id和定义id
		if ("subFlow".equals(type)) {
			String subDefId = "";
			if (StringUtil.isNotEmpty(instanceId)) {
				bpmProcessInstanceList = bpmProcessInstanceManager.getBpmProcessByParentIdAndSuperNodeId(instanceId,
						nodeId);
				if (BeanUtils.isEmpty(bpmProcessInstanceList)) {
					bpmProcessInstanceList = bpmProcessInstanceManager
							.getHiBpmProcessByParentIdAndSuperNodeId(instanceId, nodeId);
				}
			}
			/********* 如果当前流程为子流程时，将instanceId设置为子流程的Id ************/
			if (BeanUtils.isNotEmpty(bpmProcessInstanceList) && bpmProcessInstanceList.size() > 0) {
				bpmProcessInstance = bpmProcessInstanceList.get(0);
				instanceId = bpmProcessInstance.getId();
				subDefId = bpmProcessInstance.getProcDefId();
			} else {// 流程还没有执行到子流程位置的情况。
				if (StringUtil.isEmpty(defId)) {
					BpmProcessInstance parentBpmProcessInstance = bpmInstService.getProcessInstance(instanceId);
					defId = parentBpmProcessInstance.getProcDefId();
				}
				instanceId = "";// 将传入的父实例的流程定义id置空
				CallActivityNodeDef bpmNodeDef = (CallActivityNodeDef) bpmDefinitionAccessor.getBpmNodeDef(defId,
						nodeId);
				String folowKey = bpmNodeDef.getFlowKey();
				BpmDefinition def = bpmDefinitionManager.getMainByDefKey(folowKey, false);
				subDefId = def.getDefId();
			}
			bpmDefLayout = diagramService.getLayoutByDefId(subDefId);
			flowImageVo.setDefId(subDefId);
		} else {
			if (StringUtil.isEmpty(defId)) {
				bpmProcessInstance = bpmInstService.getProcessInstance(instanceId);
				bpmProcessInstanceList.add(bpmProcessInstance);
				defId = bpmProcessInstance.getProcDefId();
			}
			// 流程图layout
			bpmDefLayout = diagramService.getLayoutByDefId(defId);
			flowImageVo.setDefId(defId);
		}
		flowImageVo.setBpmProcessInstance(bpmProcessInstance);
		flowImageVo.setInstanceId(instanceId);
		flowImageVo.setBpmProcessInstanceList(bpmProcessInstanceList);
		if (BeanUtils.isNotEmpty(bpmProcessInstance)) {
			flowImageVo.setParentInstId(bpmProcessInstance.getParentInstId());
		}
		flowImageVo.setBpmDefLayout(bpmDefLayout);
		flowImageVo.setFrom(from);

		return flowImageVo;
	}

	@Override
	public List<ObjectNode> opinionHistory(String instId, String taskId) throws Exception {
		if ((instId == null || instId.isEmpty()) && taskId != null) {
			instId = bpmTaskService.getByTaskId(taskId).getProcInstId();
		}

		List<BpmTaskOpinion> bpmTaskOpinions = bpmOpinionService.getTaskOpinions(instId);

		// 以下要把整理意见格式，要展示出如果上下节点key一致的话要展现在同一个tr中
		List<List<BpmTaskOpinion>> llist = new ArrayList<List<BpmTaskOpinion>>();
		List<BpmTaskOpinion> list = null;
		String preKey = "";
		for (BpmTaskOpinion bto : bpmTaskOpinions) {
			if (StringUtil.isNotEmpty(bto.getTaskKey()) && !bto.getTaskKey().equals(preKey)) {
				list = new ArrayList<BpmTaskOpinion>();
				llist.add(list);
				preKey = bto.getTaskKey();
			}
			list.add(bto);
		}
		List<ObjectNode> opinionList = new ArrayList<ObjectNode>();
		for (List<BpmTaskOpinion> items : llist) {
			for (int i = 0; i < items.size(); i++) {
				ObjectNode obj = (ObjectNode) JsonUtil.toJsonNode(items.get(i));
				if (i == 0) {
					obj.put("isRowSpan", true);
					obj.put("rowSpan", items.size());
				}
				opinionList.add(obj);
			}
		}
		return opinionList;
	}

	@Override
	@Transactional
	public TaskDoNextVo taskDoNext(String taskId) throws Exception {
		DefaultBpmTask task = bpmTaskManager.get(taskId);
		if (task == null)
			throw new RuntimeException("任务不存在，可能已经被处理！");

		TaskDoNextVo taskDoNextVo = new TaskDoNextVo();
		IUser user = ContextUtil.getCurrentUser();
		if (!user.isAdmin()) {
			ObjectNode jsonObj = null;// bpmDefAuthorizeManager.getRight(task.getProcDefKey(),
										// BpmDefAuthorizeType.BPMDEFAUTHORIZE_RIGHT_TYPE.TASK);
			if (jsonObj == null && !ContextUtil.getCurrentUserId().equals(task.getAssigneeId()))
				throw new RuntimeException("没有处理此任务的权限!");
		}
		BpmProcessInstance bpmProcessInstance = bpmInstService.getProcessInstance(task.getProcInstId());
		NodeProperties nodeProperties = BpmUtil.getNodeProperties(bpmProcessInstance, task.getNodeId());
		boolean isPopWin = nodeProperties.isPopWin();
		taskDoNextVo.setNodeId(
				StringUtil.join(Arrays.asList(task.getProcDefKey(), task.getNodeId()), StringPool.UNDERSCORE));
		taskDoNextVo.setTaskId(taskId);
		taskDoNextVo.setPopWin(isPopWin);
		return taskDoNextVo;
	}

	@Override
	@Transactional
	public TaskDoNextVo taskApprove(String taskId) throws Exception {
		DefaultBpmTask task = bpmTaskManager.get(taskId);
		if (task == null)
			throw new RuntimeException("任务不存在，可能已经被处理！");

		boolean isForbindden = bpmInstService.isSuspendByInstId(task.getProcInstId());
		if (isForbindden) {// 流程已经被禁止
			throw new RuntimeException("流程已经被禁止，请联系管理员！");
		}

		TaskDoNextVo taskDoNextVo = new TaskDoNextVo();
		int rtn = bpmTaskManager.canLockTask(taskId);
		// 判断权限
		if (rtn == 4)
			throw new RuntimeException("此任务已经被其他人锁定!");
		// 审批时验证当前人是否有权限访问。
		validTask(taskId);

		BpmProcessInstance bpmProcessInstance = bpmInstService.getProcessInstance(task.getProcInstId());
		NodeProperties nodeProperties = BpmUtil.getNodeProperties(bpmProcessInstance, task.getNodeId());
		boolean isPopWin = nodeProperties.isPopWin();

		taskDoNextVo.setNodeId(
				StringUtil.join(Arrays.asList(task.getProcDefKey(), task.getNodeId()), StringPool.UNDERSCORE));
		taskDoNextVo.setTaskId(taskId);
		taskDoNextVo.setPopWin(isPopWin);
		return taskDoNextVo;
	}

	/**
	 * 判断当前用户是否是当前任务的执行人。
	 * 
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	private void validTask(String taskId) throws Exception {

		IUser defaultUser = ContextUtil.getCurrentUser();
		if (defaultUser.isAdmin())
			return;

		// 先查询自己是否有这个任务；
		boolean mark = false;
		// 获取任务 的人
		List<IUser> users = bpmTaskService.getUsersByTaskId(taskId);
		String userId = ContextUtil.getCurrentUserId();
		for (IUser user : users) {
			if (userId.equals(user.getUserId())) {
				mark = true;
				break;
			}
		}
		if (!mark)
			throw new RuntimeException("您没有执行该操作的权限.");
	}

	@Override
	public TaskjImageVo taskImage(String taskId, String defId) throws Exception {
		String parentInstId = "";
		String procDefId = "";
		String instId = "";
		if (StringUtil.isNotEmpty(taskId)) {
			DefaultBpmTask task = bpmTaskManager.get(taskId);
			// 流程图layout
			BpmProcessInstance bpmProcessInstance = bpmInstService.getProcessInstance(task.getProcInstId());
			parentInstId = bpmProcessInstance.getParentInstId();
			procDefId = bpmProcessInstance.getProcDefId();
			instId = bpmProcessInstance.getId();
		} else if (StringUtil.isNotEmpty(defId)) {
			procDefId = defId;

		}

		BpmDefLayout bpmDefLayout = diagramService.getLayoutByDefId(procDefId);

		return new TaskjImageVo(taskId, bpmDefLayout, instId, parentInstId);
	}

	@Override
	public Object nodeOpinion(String defId, String instId, String nodeId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 获取审批任务意见。
		List<BpmTaskOpinion> opinionList = null;
		if (StringUtil.isNotEmpty(instId)) {
			opinionList = bpmOpinionService.getByInstNodeId(instId, nodeId);
			if (BeanUtils.isNotEmpty(opinionList)) {
				for (BpmTaskOpinion bpmTaskOpinion : opinionList) {
					String qualfieds = bpmTaskOpinion.getQualfieds();
					if (StringUtil.isEmpty(qualfieds)) {
						continue;
					}
					ArrayNode qualfiedsObj = null;
					try {
						qualfiedsObj = (ArrayNode) JsonUtil.toJsonNode(qualfieds);
					} catch (Exception e) {
						continue;
					}
					DefaultBpmCheckOpinion defaultBpmCheckOpinion = (DefaultBpmCheckOpinion) bpmTaskOpinion;
					for (JsonNode jsonNode : qualfiedsObj) {
						ObjectNode identityObj = (ObjectNode) jsonNode;
						DefaultBpmIdentity identity = JsonUtil.toBean(identityObj, DefaultBpmIdentity.class);
						if (!BpmIdentity.TYPE_USER.equals(identity.getType())) {
							List<BpmIdentity> tempList = new ArrayList<>();
							tempList.add(identity);
							List<IUser> extractUser = bpmIdentityExtractService.extractUser(tempList);
							if (BeanUtils.isNotEmpty(extractUser)) {
								List<String> userNames = new ArrayList<>();
								for (IUser iUser : extractUser) {
									userNames.add(iUser.getFullname());
								}
								identityObj.put("users", StringUtil.join(userNames, ","));
							}
						}
					}
					defaultBpmCheckOpinion.setQualfieds(JsonUtil.toJson(qualfiedsObj));
				}
				map.put("hasOpinion", true);
				map.put("data", opinionList);
				return map;
			}
			// 判断流程实例是否结束，为空直接返回空
			map.put("hasOpinion", false);
			DefaultBpmProcessInstance instance = bpmProcessInstanceManager.get(instId);
			if ("end".equals(instance.getStatus()) || "draft".equals(instance.getStatus())) {
				return map;
			}
		}

		map.put("hasOpinion", false);
		// 没有审批意见则获取有审批权限的人
		List<BpmIdentity> userList = new ArrayList<BpmIdentity>();
		if (StringUtil.isEmpty(instId)) {
			userList = bpmIdentityService.searchByNodeIdOnStartEvent(defId, nodeId);
		} else {
			userList = bpmIdentityService.searchByNode(instId, nodeId);
		}

		if (userList.size() > 0) {
			// 找到了执行人
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			for (BpmIdentity identity : userList) {
				Map<String, Object> identityMap = new HashMap<String, Object>();
				identityMap.put("id", identity.getId());
				identityMap.put("name", identity.getName());
				// 这个type可能的值如：
				// user,用户,org,组织，role，角色，pos岗位。这个可以扩充。
				String type = identity.getType().equals(BpmIdentity.TYPE_USER) ? BpmIdentity.TYPE_USER
						: identity.getGroupType();
				if (!BpmIdentity.TYPE_USER.equals(type)) {
					List<BpmIdentity> tempList = new ArrayList<>();
					tempList.add(identity);
					List<IUser> extractUser = bpmIdentityExtractService.extractUser(tempList);
					if (BeanUtils.isNotEmpty(extractUser)) {
						List<String> userNames = new ArrayList<>();
						for (IUser iUser : extractUser) {
							userNames.add(iUser.getFullname());
						}
						identityMap.put("users", StringUtil.join(userNames, ","));
					}
				}
				identityMap.put("type", type);
				list.add(identityMap);
			}
			map.put("data", list);
		}
		return map;
	}

	@Override
	public TaskDetailVo getButtonsBytaskId(String taskId) throws Exception {
		TaskDetailVo taskDetailVo = new TaskDetailVo();
		DefaultBpmTask task = bpmTaskManager.get(taskId);
        BpmProcessInstance bpmProcessInstance = bpmInstService.getProcessInstance(task.getProcInstId());
        List<ObjectNode> bodatas = boDataService.getDataByInst(bpmProcessInstance);
        BpmContextUtil.setBoToContext(bodatas);
		if (BeanUtils.isEmpty(task)) {
			throw new RuntimeException("任务不存在，可能已经被处理了.");
		}
		String defId = task.getProcDefId();
		String nodeId = task.getNodeId();
		BpmNodeDef taskNodeDef = bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);
		List<Button> buttons = null;
		if (BeanUtils.isNotEmpty(taskNodeDef)) {
			buttons = BpmUtil.getButtons(taskNodeDef, task);
		}
		if (BeanUtils.isNotEmpty(buttons)) {
			// 按钮处理，根据groovy脚本去掉定义的按钮。
			handButtons(buttons, task.getTaskId());
			// 按钮
			taskDetailVo.setButtons(buttons);
		}
		return taskDetailVo;
	}

	@Override
	public TaskDetailVo taskDetail(String taskId, String reqValue, FormType formType, String leaderId)
			throws Exception {
		DefaultBpmTask task = bpmTaskManager.get(taskId);
		if (BeanUtils.isEmpty(task)) {
			throw new RuntimeException("任务不存在，可能已经被处理了.");
		}

		Boolean hasPermTodo = false;
		IUser currentUser = ContextUtil.getCurrentUser();
		if (BeanUtils.isEmpty(currentUser)) {
			throw new RuntimeException("请先登录系统");
		}

		if (StringUtil.isNotEmpty(leaderId)) {
			ThreadMsgUtil.addMapMsg("leaderId", leaderId);
		}

		if (currentUser.isAdmin()) {
			hasPermTodo = true;
		}
		// 获取用户
		List<IUser> recievers = new ArrayList<IUser>();
		List<BpmIdentity> queryByBpmTask = bpmIdentityService.queryListByBpmTask(task);
		// 将用户抽取出来。
		recievers = bpmIdentityExtractService.extractUser(queryByBpmTask);
		if (BeanUtils.isNotEmpty(recievers)) {
			for (IUser iUser : recievers) {
				if (currentUser.getUserId().equals(iUser.getUserId()) || leaderId.equals(iUser.getUserId())) {
					hasPermTodo = true;
					break;
				}
			}
		}
		if (!hasPermTodo) {
			throw new RuntimeException("您没有该任务的处理权限");
		}

		TaskDetailVo taskDetailVo = new TaskDetailVo();
		String topDefKey = "";
		BpmProcessInstance bpmProcessInstance = bpmInstService.getProcessInstance(task.getProcInstId());
		if (StringUtil.isNotZeroEmpty(bpmProcessInstance.getParentInstId())) {
			BpmProcessInstance topInstance = bpmProcessInstanceManager.getTopBpmProcessInstance(bpmProcessInstance);
			topDefKey = topInstance.getProcDefKey();
		}

		String defId = task.getProcDefId();
		String nodeId = task.getNodeId();

		BpmFormService bpmFormService = BpmFormFactory.getFormService(formType);
		FormModel formModel = bpmFormService.getByDefId(defId, nodeId, bpmProcessInstance, true);

		if (formModel == null || formModel.isFormEmpty()) {
			taskDetailVo.setResult("formEmpty");
			return taskDetailVo;
		}

		// 表单
		taskDetailVo.setForm(formModel);

		BpmNodeDef taskNodeDef = bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);
		List<Button> buttons = null;
		if (BeanUtils.isNotEmpty(taskNodeDef)) {
			buttons = BpmUtil.getButtons(taskNodeDef, task);
		}
		if (FormCategory.INNER.value().equals(formModel.getType().value())) {
			ContextThreadUtil.putCommonVars("nodeId", nodeId);
			ContextThreadUtil.putCommonVars("defId", defId);
			List<ObjectNode> boDatas = boDataService.getDataByInst(bpmProcessInstance);

			// 跟踪不设置cmd
			if (!"FOLLOW".equals(task.getStatus())) {
				DefaultTaskFinishCmd cmd = new DefaultTaskFinishCmd();
				cmd.setVariables(this.getTaskVars(taskId, null));
				ContextThreadUtil.setActionCmd(cmd);
			}

			// BO数据前置处理
			ObjectNode data = BoDataUtil.hanlerData(bpmProcessInstance, nodeId, boDatas);
			// BO数据
			taskDetailVo.setData(data);

			// 获取意见
			ObjectNode opinionJson = boDataService.getFormOpinionJson(task.getProcInstId());
			taskDetailVo.setOpinionList(opinionJson);
			
			
	        boolean getGlobalPermission = true;
	        ObjectNode formRestParams = JsonUtil.getMapper().createObjectNode();
	        //如果是取节点表单。判断有没有配置全局表单，如全局表单和节点表单不一致。则不获取全局权限
	        if (StringUtil.isNotEmpty(nodeId)) {
	        	BpmProcessDef<BpmProcessDefExt> bpmProcessDef = bpmDefinitionAccessor.getBpmProcessDef(defId);
		        DefaultBpmProcessDefExt defExt = (DefaultBpmProcessDefExt) bpmProcessDef.getProcessDefExt();
		        FormExt globalForm = defExt.getGlobalFormByDefKey(topDefKey, "mobile".equals(formType.value()));
		        if (BeanUtils.isEmpty(globalForm) || !globalForm.getFormValue().equals(formModel.getFormKey())) {
		        	getGlobalPermission = false;
				}
			}
	        formRestParams.put("formkey", formModel.getFormKey());
			formRestParams.put("isGlobalPermission", getGlobalPermission);
			formRestParams.put("flowKey", task.getProcDefKey());
			formRestParams.put("nodeId", nodeId);
			formRestParams.put("parentFlowKey", topDefKey);
			String permission = formRestfulService.getPermission(formRestParams);
			taskDetailVo.setPermission(permission);
		}
		if (BeanUtils.isNotEmpty(buttons)) {
			// 按钮处理，根据groovy脚本去掉定义的按钮。
			handButtons(buttons, task.getTaskId());
			// 处理秘书按钮。目前只能进行签收，驳回，同意操作
			if (StringUtil.isNotZeroEmpty(leaderId)) {
				for (Iterator<Button> iterator = buttons.iterator(); iterator.hasNext();) {
					Button button = iterator.next();
					String btnName = button.getAlias();
					if (!btnName.equals("agree") && !btnName.equals("reject") && !btnName.equals("lockUnlock")) {
						iterator.remove();
					}
				}
			}
			taskDetailVo.setButtons(buttons);
		}
		taskDetailVo.setResult(true);
		// 任务状态为跟踪查看后删除
		if ("FOLLOW".equals(task.getStatus())) {
			bpmTaskManager.remove(taskId);
		}

		return taskDetailVo;
	}

	@Override
	public TaskDetailVo taskDetailMobile(String taskId, String reqValue, FormType formType) throws Exception {
		DefaultBpmTask task = bpmTaskManager.get(taskId);
		if (BeanUtils.isEmpty(task)) {
			task = new DefaultBpmTask();
			BpmTaskNoticeManager noticeManager = AppUtil.getBean(BpmTaskNoticeManager.class);
			BpmTaskNotice bpmTaskNotice = noticeManager.get(taskId);
			if (BeanUtils.isEmpty(bpmTaskNotice)) {
				throw new NotFoundException("此任务已被处理或不存在！");
			}
			task = bpmTaskNotice.convertToBpmTask();
		}
		TaskDetailVo taskDetailVo = new TaskDetailVo();
		String topDefKey = "";
		BpmProcessInstance bpmProcessInstance = bpmInstService.getProcessInstance(task.getProcInstId());
		if (StringUtil.isNotZeroEmpty(bpmProcessInstance.getParentInstId())) {
			BpmProcessInstance topInstance = bpmProcessInstanceManager.getTopBpmProcessInstance(bpmProcessInstance);
			topDefKey = topInstance.getProcDefKey();
		}

		String defId = task.getProcDefId();
		String nodeId = task.getNodeId();

		BpmFormService bpmFormService = BpmFormFactory.getFormService(formType);
		FormModel formModel = bpmFormService.getByDefId(defId, nodeId, bpmProcessInstance, true);

		if (formModel == null || formModel.isFormEmpty()) {
			taskDetailVo.setResult("formEmpty");
			return taskDetailVo;
		}

		// 表单
		taskDetailVo.setForm(formModel);

		BpmNodeDef taskNodeDef = bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);

		List<Button> buttons = null;
		if (BeanUtils.isNotEmpty(taskNodeDef) && !"FOLLOW".equals(task.getStatus())) {
			buttons = BpmUtil.getButtons(taskNodeDef, task);
		}

		if (FormCategory.INNER.value().equals(formModel.getType().value())) {
			ContextThreadUtil.putCommonVars("nodeId", nodeId);
			ContextThreadUtil.putCommonVars("defId", defId);
			List<ObjectNode> boDatas = boDataService.getDataByInst(bpmProcessInstance);

			// 跟踪不设置cmd
			if (!"FOLLOW".equals(task.getStatus()) && !"COPYTO".equals(task.getStatus())
					&& !"COMMU".equals(task.getStatus())) {
				DefaultTaskFinishCmd cmd = new DefaultTaskFinishCmd();
				cmd.setVariables(this.getTaskVars(taskId, null));
				ContextThreadUtil.setActionCmd(cmd);
			}
			;

			// BO数据前置处理
			// ObjectNode data =BoDataUtil.hanlerData(bpmProcessInstance,nodeId,
			// boDatas);
			ObjectNode data = (ObjectNode) BoDataUtil.hanlerData(boDatas);
			// BO数据
			taskDetailVo.setData(data);

			// 获取意见
			ObjectNode opinionJson = boDataService.getFormOpinionJson(task.getProcInstId());
			taskDetailVo.setOpinionList(opinionJson);
			// 流程定义的权限
			if (!"FOLLOW".equals(task.getStatus()) && !"COPYTO".equals(task.getStatus())
					&& !"COMMU".equals(task.getStatus())) {
				ObjectNode formRestParams = JsonUtil.getMapper().createObjectNode();
				formRestParams.put("formkey", formModel.getFormKey());
				formRestParams.put("flowKey", task.getProcDefKey());
				formRestParams.put("nodeId", nodeId);
				formRestParams.put("parentFlowKey", topDefKey);
				String permission = formRestfulService.getPermission(formRestParams);
				taskDetailVo.setPermission(permission);
			} else {
				String curUserId = ContextUtil.getCurrentUserId();
				// 获取最外层的权限。
				ObjectNode formRestParams = JsonUtil.getMapper().createObjectNode();
				formRestParams.put("formkey", formModel.getFormKey());
				formRestParams.put("userId", curUserId);
				formRestParams.put("parentFlowKey", topDefKey);
				String permission = formRestfulService.getInstPermission(formRestParams);
				taskDetailVo.setPermission(permission);
			}
		}
		if (BeanUtils.isNotEmpty(buttons)) {
			// 按钮处理，根据groovy脚本去掉定义的按钮。
			handButtons(buttons, task.getTaskId());
			// 按钮
			taskDetailVo.setButtons(buttons);
		}
		taskDetailVo.setResult(true);
		// 任务状态为跟踪查看后删除
		// if("FOLLOW".equals(task.getStatus())) {
		// bpmTaskManager.remove(taskId);
		// }
		return taskDetailVo;
	}

	/**
	 * 处理按钮是否可以可以显示。
	 * 
	 * @param buttons
	 * @param taskId
	 */
	private void handButtons(List<Button> buttons, String taskId) {
		GroovyScriptEngine scriptEngine = (GroovyScriptEngine) AppUtil.getBean(GroovyScriptEngine.class);

		Map<String, ObjectNode> boMap = BpmContextUtil.getBoFromContext();

		Map<String, Object> variables = new HashMap<String, Object>();

		if (BeanUtils.isNotEmpty(boMap)) {
			variables.putAll(boMap);
		}

		List<Button> removeBtns = new ArrayList<Button>();

		for (Button btn : buttons) {
			String script = btn.getGroovyScript();
			if (StringUtil.isEmpty(script))
				continue;

            boolean rtn = true;
			if(variables.size()!=0){
                rtn = scriptEngine.executeBoolean(script, variables);
            }
			if (rtn == false) {
				removeBtns.add(btn);
			}
		}
		buttons.removeAll(removeBtns);
	}

	/**
	 * 获取合法的流程变量。
	 * 
	 * @param taskId
	 * @param varMap
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> getActVars(String taskId, Map<String, String> varMap) throws Exception {
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		if (StringUtil.isEmpty(taskId))
			return rtnMap;
		DefaultBpmTask bpmTask = bpmTaskManager.get(taskId);
		if (!StringUtil.isZeroEmpty(bpmTask.getParentId())) {
			taskId = bpmTask.getParentId();
		}
		rtnMap = natTaskService.getVariables(taskId);
		if (BeanUtils.isEmpty(varMap)) {
			return rtnMap;
		}
		BpmProcessDef<BpmProcessDefExt> bpmProcessDefExt = bpmDefinitionAccessor
				.getBpmProcessDef(bpmTask.getProcDefId());
		DefaultBpmProcessDefExt defExt = (DefaultBpmProcessDefExt) bpmProcessDefExt.getProcessDefExt();
		List<BpmVariableDef> bpmVariableList = defExt.getVariableList();
		if (bpmVariableList == null)
			bpmVariableList = new ArrayList<BpmVariableDef>();

		if (StringUtil.isNotEmpty(bpmTask.getNodeId())) {
			bpmVariableList.addAll(defExt.getVariableList(bpmTask.getNodeId()));
		}
		for (BpmVariableDef var : bpmVariableList) {
			String varkey = var.getVarKey();
			String val = varMap.get(varkey);
			if (val != null) {
				Object rtnVal = DefaultBpmVariableDef.getValue(var.getDataType(), val);
				rtnMap.put(varkey, rtnVal);
			}
		}
		return rtnMap;
	}

	@Override
	public Map<String, Object> getTaskVars(String taskId, Map<String, String> vars) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtil.isEmpty(taskId))
			return params;
		DefaultBpmTask bpmTask = bpmTaskManager.get(taskId);
		if (!StringUtil.isZeroEmpty(bpmTask.getParentId())) {
			taskId = bpmTask.getParentId();
		}
		params = natTaskService.getVariables(taskId);
		List<BpmVariableDef> list = bpmDefinitionService.getVariableDefs(bpmTask.getProcDefId(), bpmTask.getNodeId());
		for (BpmVariableDef varDef : list) {
			String reqValue = null;
			// 优先获取参数传过来的变量
			if (BeanUtils.isNotEmpty(vars)) {
				reqValue = vars.get(varDef.getVarKey());
			}
			// 参数未传递该变量时，尝试获取变量的默认值
			if (StringUtil.isEmpty(reqValue) && varDef.getDefaultVal() != null
					&& StringUtil.isNotEmpty(varDef.getDefaultVal().toString())) {
				reqValue = varDef.getDefaultVal().toString();
			}
			if (StringUtil.isNotEmpty(reqValue)) {
				Object convertVal = DefaultBpmVariableDef.getValue(varDef.getDataType(), reqValue);
				params.put(varDef.getVarKey(), convertVal);
			}
		}
		return params;
	}

	public Map<String, Object> getVarsFromURLForm(String taskId, String data) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtil.isEmpty(taskId) || StringUtil.isEmpty(data))
			return params;
		JsonNode jsonNode = JsonUtil.toJsonNode(data);
		if (BeanUtils.isEmpty(jsonNode))
			return params;
		DefaultBpmTask bpmTask = bpmTaskManager.get(taskId);
		List<BpmVariableDef> list = bpmDefinitionService.getVariableDefs(bpmTask.getProcDefId());
		if (BeanUtils.isEmpty(list))
			return params;
		for (BpmVariableDef varDef : list) {
			String varKey = varDef.getVarKey();
			String reqValue = null;
			JsonNode varKeyInData = jsonNode.get(varKey);
			if (varKeyInData != null && varKeyInData.isValueNode()) {
				reqValue = varKeyInData.asText();
			}
			if (StringUtil.isNotEmpty(reqValue)) {
				Object convertVal = DefaultBpmVariableDef.getValue(varDef.getDataType(), reqValue);
				params.put(varKey, convertVal);
			}
		}
		return params;
	}

	@Override
	@Transactional
	public CommonResult<String> complete(DoNextParamObject doNextParamObject) throws Exception {
		try {
			// 获取cmd对象。
			DefaultTaskFinishCmd cmd = getCmdFromRequest(doNextParamObject);
			boolean result = bpmTaskActionService.finishTask(cmd);
			return new CommonResult<String>(result, result ? "任务办理成功" : "任务办理失败", "");
		} catch (Exception e) {
			e.printStackTrace();
			String rootCauseMessage = ExceptionUtils.getRootCauseMessage(e);
			if (StringUtil.isNotEmpty(rootCauseMessage) && rootCauseMessage.indexOf("表单数据已被其他用户修改，请重新加载数据。") > -1) {
				rootCauseMessage = "表单数据已被其他用户修改，请重新加载数据。";
			}
			throw new BaseException("任务办理失败:" + rootCauseMessage);
		}
	}

	/**
	 * 从上下文请求获取包装后的cmd对象。
	 * 
	 * @param doNextParamObject
	 * @return DefaultTaskFinishCmd
	 * @throws Exception
	 */
	private DefaultTaskFinishCmd getCmdFromRequest(DoNextParamObject doNextParamObject) throws Exception {

        String taskId = doNextParamObject.getTaskId();

		if (BeanUtils.isNotEmpty(doNextParamObject.getUrgentStateValue())) {
			DefaultBpmTask defaultBpmTask = bpmTaskManager.get(taskId);
			if (BeanUtils.isNotEmpty(defaultBpmTask)) {
				DefaultBpmDefinition mainByDefKey = bpmDefinitionManager
						.getMainByDefKey(defaultBpmTask.getProcDefKey());
				if (BeanUtils.isNotEmpty(mainByDefKey) && mainByDefKey.getShowUrgentState() == 1) {
					DefaultBpmProcessInstance instance = bpmProcessInstanceManager.get(defaultBpmTask.getProcInstId());
					String oldValue = doNextParamObject.getUrgentStateValue().hasNonNull("old")
							? doNextParamObject.getUrgentStateValue().get("old").asText() : "";
					String instanceUrgentState = StringUtil.isNotEmpty(instance.getUrgentState())
							? instance.getUrgentState() : "";
					if (!oldValue.equals(instanceUrgentState)) {
						throw new RuntimeException("紧急状态已被修改，请刷新表单获取最新数据再提交");
					}
					instance.setUrgentState(doNextParamObject.getUrgentStateValue().get("new").asText());
					bpmProcessInstanceManager.update(instance);
				}
			}
		}
		String actionName = doNextParamObject.getActionName();
		String opinion = doNextParamObject.getOpinion();
		String formType = StringUtil.isEmpty(doNextParamObject.getFormType()) ? FormCategory.INNER.value()
				: doNextParamObject.getFormType();
		;
		String data = "";
		if (StringUtil.isNotEmpty(doNextParamObject.getData())) {
			try {
				data = Base64.getFromBase64(doNextParamObject.getData());
			} catch (Exception e) {
			}
		}

		boolean directHandlerSign = doNextParamObject.getDirectHandlerSign();
		String backHandMode = doNextParamObject.getBackHandMode();
		String jumpType = doNextParamObject.getJumpType();
		DefaultTaskFinishCmd cmd = new DefaultTaskFinishCmd();
		if (StringUtil.isNotEmpty(doNextParamObject.getInterPoseOpinion())) {
			cmd.setInterPoseOpinion(doNextParamObject.getInterPoseOpinion());
		}
		// 自由跳转 或者 驳回到指定节点
		if ("free".equals(jumpType) || "select".equals(jumpType) || "reject".equals(actionName)) {
			String destination = doNextParamObject.getDestination();// rejectMode
			if (StringUtil.isNotEmpty(destination)) {
				cmd.setDestination(destination);
			}
		}

		String nodeUsers = doNextParamObject.getNodeUsers();
		if (StringUtil.isNotEmpty(nodeUsers)) {
			Map<String, List<BpmIdentity>> specUserMap = BpmIdentityUtil.getBpmIdentity(nodeUsers);
			if (BeanUtils.isNotEmpty(specUserMap)) {
				cmd.addTransitVars(BpmConstants.BPM_NODE_USERS, specUserMap);
			}
		}
		// 用户手机端驳回 暂时这样处理
		if (StringUtil.isEmpty(cmd.getDestination()) && "reject".equals(actionName)) {
			// 设置驳回上一步
			String destination = getRejectPreDestination(taskId);
			if (StringUtil.isEmpty(destination)) {
				throw new RuntimeException("该节点任务不支持驳回上一步");
			}
			cmd.setDestination(destination);
		}

		if ("common".equals(jumpType)) {
			List<BpmIdentity> identity = BpmIdentityUtil.getNextNodeBpmIdentity(nodeUsers);
			cmd.addTransitVars(BpmConstants.BPM_NEXT_NODE_USERS, identity);
		}
		// 会签任务的直接处理
		if (directHandlerSign) {
			cmd.addTransitVars(BpmConstants.SIGN_DIRECT, "1");
		}
		//是否是普通用户任务加签后驳回
        if(StringUtil.isNotEmpty(doNextParamObject.getRejectTaskId())){
			cmd.setTaskId(doNextParamObject.getRejectTaskId());
		}else if(StringUtil.isNotEmpty(taskId)){
            cmd.setTaskId(taskId);
        }
		cmd.setActionName(actionName);

		// 设置表单意见。
		cmd.setApprovalOpinion(opinion);

		cmd.setAgentLeaderId(doNextParamObject.getAgentLeaderId());

		cmd.setFiles(doNextParamObject.getFiles());
		cmd.setZfiles(doNextParamObject.getZfiles());
		cmd.setAddSignAction(doNextParamObject.getAddSignAction());
		//是否是普通用户任务加签后任务的驳回
		if(StringUtil.isNotEmpty(doNextParamObject.getRejectTaskId())){
		    cmd.setRejectTaskId(doNextParamObject.getRejectTaskId());
		    //驳回方法是否是回到本节点
            if("direct".equals(doNextParamObject.getBackHandMode())){
                cmd.setAddSignTaskId(taskId);
            }
        }
		Map<String, Object> varsFromData = new HashMap<>();
		// 处理表单意见，如果表单的意见存在则覆盖之前的意见。
		if (FormCategory.INNER.value().equals(formType)) {
			BpmUtil.handOpinion(data, cmd);
			cmd.setDataMode(ActionCmd.DATA_MODE_BO);
		} else {
			// URL表单下，如果data有数据，则根据流程定义中的变量设置 来更新流程变量
			if (StringUtil.isNotEmpty(data)) {
				varsFromData = this.getVarsFromURLForm(taskId, data);
			}
			cmd.setDataMode(ActionCmd.DATA_MODE_PK);
		}
		// 请求参数中的流程变量数据
		// Map<String, Object> varsFromVars = this.getTaskVars(taskId,
		// doNextParamObject.getVars());
		Map<String, Object> varsFromVars = getActVars(taskId, doNextParamObject.getVars());
		// 来自URL表单中的数据 会 覆盖掉变量的默认数据
		varsFromVars.putAll(varsFromData);

		cmd.setVariables(varsFromVars);
		// 设置流程驳回时跳转模式。
		cmd.addTransitVars(BpmConstants.BACK_HAND_MODE, backHandMode);

		cmd.setBusData(data);
		// 设置目标节点映射----------------------------------------------------------------------------------------------------
		Map<String, List<BpmIdentity>> nodeIdentityMap = getNodeBpmIdentities(taskId, doNextParamObject.getUsersMap());
		cmd.setBpmIdentities(nodeIdentityMap);
		return cmd;
	}

	/**
	 * 获取驳回上一步的目标节点
	 * 
	 * @param taskId
	 * @return "" 不可驳回 ， 非空返回可驳回到上一步节点的名称
	 * @throws Exception
	 */
	public String getRejectPreDestination(String taskId) throws Exception {

		boolean canRejectPreAct = true;// 是否可以驳回到上一步
		DefaultBpmTask task = bpmTaskManager.get(taskId);

		String defId = task.getProcDefId();
		String nodeId = task.getNodeId();

		boolean canReject = false;

		BpmNodeDef taskNodeDef = bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);
		List<Button> buttons = BpmUtil.getButtons(taskNodeDef, task);
		for (Button button : buttons) {
			if ("reject".equals(button.getAlias()))
				canReject = true;
		}

		if (!canReject)
			return "";

		NodeProperties nodeProperties = taskNodeDef.getLocalProperties();
		String backMode = nodeProperties.getBackMode();
		if (StringUtil.isEmpty(backMode))
			backMode = "normal";
		String procInstId = task.getProcInstId();

		List<BpmNodeDef> listBpmNodeDef = BpmStackRelationUtil.getHistoryListBpmNodeDef(procInstId, task.getNodeId(),
				"pre");
		// 允许直来直往的节点
		List<BpmNodeDef> bpmExeStacksUserNode = new ArrayList<BpmNodeDef>();
		// 允许按流程图执行的节点
		List<BpmNodeDef> bpmExeStacksGoMapUserNode = new ArrayList<BpmNodeDef>();
		List<BpmExeStackRelation> relationList = relationManager.getListByProcInstId(procInstId);
		for (BpmNodeDef node : listBpmNodeDef) {
			if ((node.getType().equals(NodeType.CALLACTIVITY) || node.getType().equals(NodeType.USERTASK)
					|| node.getType().equals(NodeType.SIGNTASK)) && !node.getNodeId().equals(nodeId)) {
				bpmExeStacksUserNode.add(node);

				boolean isHavePre = BpmStackRelationUtil.isHaveAndOrGateway(procInstId, node.getNodeId(), "pre",
						relationList);
				boolean isHaveAfter = BpmStackRelationUtil.isHaveAndOrGateway(procInstId, node.getNodeId(), "after",
						relationList);
				if (!(isHavePre && isHaveAfter)) {
					bpmExeStacksGoMapUserNode.add(node);
				} else {
					List<BpmNodeDef> incomeNodes = node.getIncomeNodes();
					if (BeanUtils.isNotEmpty(incomeNodes)) {
						BpmNodeDef nodeDef = incomeNodes.get(0);
						// 如果是从开始网关进入的用户节点，则允许按流程图驳回
						if (node.getType().equals(NodeType.USERTASK) && (nodeDef.getType().equals(NodeType.START)
								|| nodeDef.getType().equals(NodeType.USERTASK))) {
							bpmExeStacksGoMapUserNode.add(node);
						}
					}
				}
			}
		}
		canRejectPreAct = bpmExeStacksGoMapUserNode.size() > 0 || bpmExeStacksUserNode.size() > 0;

		if (!canRejectPreAct) {
			return "";
		}

		if ("direct".equals(backMode)) {
			return bpmExeStacksUserNode.get(0).getNodeId();
		} else {
			return bpmExeStacksGoMapUserNode.get(0).getNodeId();
		}

	}

	/**
	 * 根据任务节点获取节点的执行人。
	 * 
	 * @param taskId
	 * @param usersMap
	 * @return Map<String,List<BpmIdentity>>
	 * @throws Exception
	 */
	private Map<String, List<BpmIdentity>> getNodeBpmIdentities(String taskId, ObjectNode usersMap) throws Exception {
		Map<String, List<BpmIdentity>> nodeIdentityMap = new HashMap<String, List<BpmIdentity>>();
		if (StringUtil.isEmpty(taskId))
			return nodeIdentityMap;

		BpmTask bpmTask = bpmTaskManager.get(taskId);
		BpmNodeDef taskNodeDef = bpmDefinitionAccessor.getBpmNodeDef(bpmTask.getProcDefId(), bpmTask.getNodeId());

		for (BpmNodeDef nodeDef : taskNodeDef.getOutcomeTaskNodes()) {
			if (BeanUtils.isNotEmpty(usersMap)) {
				String userIdStr = usersMap.get("uId_" + nodeDef.getNodeId()).asText();
				String[] userIds = userIdStr.split(",");
				List<BpmIdentity> bpmIdentities = new ArrayList<BpmIdentity>();
				if (BeanUtils.isEmpty(userIds))
					continue;

				for (String uId : userIds) {
					IUser user = userServiceImpl.getUserById(uId);
					DefaultBpmIdentity identity = new DefaultBpmIdentity(user);
					bpmIdentities.add(identity);
				}

				nodeIdentityMap.put(nodeDef.getNodeId(), bpmIdentities);
			}

		}
		return nodeIdentityMap;
	}

	@Override
	@Transactional
	public CommonResult<String> saveDraft(DoNextParamObject doNextParamObject) throws Exception {
		try {
			doNextParamObject.setActionName("saveDraft");
			// 获取cmd对象。
			DefaultTaskFinishCmd cmd = getCmdFromRequest(doNextParamObject);
			bpmTaskService.saveDraft(cmd);
			return new CommonResult<String>(true, "保存成功!", "");
		} catch (Exception e) {
			String rootCauseMsg = ExceptionUtils.getRootCauseMessage(e);
			return new CommonResult<String>(false, "保存失败：" + rootCauseMsg, "");
		}
	}

	@Override
	public TaskGetVo getTaskById(String taskId) throws Exception {
		// 获取候选记录
		DefaultBpmTask bpmTask = bpmTaskManager.getByTaskId(taskId);

		List<BpmIdentity> bpmIdentities = new ArrayList<>();
		if (BeanUtils.isEmpty(bpmTask)) {
			bpmTask = new DefaultBpmTask();
			BpmTaskNoticeManager noticeManager = AppUtil.getBean(BpmTaskNoticeManager.class);
			BpmTaskNotice bpmTaskNotice = noticeManager.get(taskId);
			if (BeanUtils.isEmpty(bpmTaskNotice)) {
				throw new BaseException("此任务已被处理或不存在！");
			}
			bpmTask = bpmTaskNotice.convertToBpmTask();
		} else {
			bpmTaskService.getTaskCandidates(taskId);
		}
		// 流程图layout
		BpmProcessInstance bpmProcessInstance = bpmInstService.getProcessInstance(bpmTask.getProcInstId());
		bpmTask.setInstStatus(bpmProcessInstance.getStatus());
		BpmDefLayout bpmDefLayout = diagramService.getLayoutByDefId(bpmProcessInstance.getProcDefId());

		// 审批历史
		List<BpmTaskOpinion> opinionList = bpmOpinionService.getTaskOpinions(bpmTask.getProcInstId());

		// 以下要把整理意见格式，要展示出如果上下节点key一致的话要展现在同一个tr中
		List<List<BpmTaskOpinion>> llist = new ArrayList<List<BpmTaskOpinion>>();
		List<BpmTaskOpinion> list = null;
		String preKey = "";
		for (BpmTaskOpinion bto : opinionList) {
			if (!preKey.equals(bto.getTaskKey())) {
				list = new ArrayList<BpmTaskOpinion>();
				llist.add(list);
				if (StringUtil.isNotEmpty(bto.getTaskKey())) {
					preKey = bto.getTaskKey();
				} else {
					preKey = "";
				}
			}
			list.add(bto);
		}
		BpmNodeDef nodeDef = null;
		if (!"COMMU".equals(bpmTask.getStatus()) && !"FOLLOW".equals(bpmTask.getStatus())
				&& !"COPYTO".equals(bpmTask.getStatus())) {
			nodeDef = getCurNodeProperties(taskId, "", "");
		}
		
		//判断是否显示表单修改记录
		boolean showModifyRecord = false;
		BpmDefinition bpmDefinition = bpmDefinitionManager.getById(bpmTask.getProcDefId());
		if(BeanUtils.isNotEmpty(bpmDefinition)) {
			int modifyRecord = bpmDefinition.getShowModifyRecord();
			if(modifyRecord == 1) {
				showModifyRecord = true;
			}
		}
		
		//表单formKey
		String defId = bpmTask.getProcDefId();
		String nodeId = bpmTask.getNodeId();
		BpmFormService bpmFormService = BpmFormFactory.getFormService(FormType.PC);
		FormModel formModel = bpmFormService.getByDefId(defId, nodeId, bpmProcessInstance, true);
		String formKey = "";
		if(BeanUtils.isNotEmpty(formModel)) {
			formKey = formModel.getFormKey();
		}
		TaskGetVo getVo = new TaskGetVo(bpmTask, bpmDefLayout, llist, bpmIdentities, nodeDef, showModifyRecord, formKey);
		if(BeanUtils.isNotEmpty(nodeDef)){
			NodeProperties nodeProperties = nodeDef.getLocalProperties();
			getVo.setInitFillData(nodeProperties.isInitFillData());
		}
		return getVo;
	}

	private void getNextAllNodes(BpmNodeDef curNode, List<BpmNodeDef> nextAllNodeDef) {
		List<BpmNodeDef> outcomeNodes = curNode.getOutcomeNodes();
		for (BpmNodeDef bpmNodeDef : outcomeNodes) {
			String typeKey = bpmNodeDef.getType().getKey();
			if (NodeType.SERVICETASK.getKey().equals(typeKey) || NodeType.EXCLUSIVEGATEWAY.getKey().equals(typeKey)
					|| NodeType.PARALLELGATEWAY.getKey().equals(typeKey)
					|| NodeType.INCLUSIVEGATEWAY.getKey().equals(typeKey)) {
				getNextAllNodes(bpmNodeDef, nextAllNodeDef);
			} else {
				// 用户任务和会签任务，直接放入下一个节点的集合里边
				nextAllNodeDef.add(bpmNodeDef);
			}
		}
	}

	@Override
	@Transactional
	public TaskToAgreeVo toAgree(String taskId, String actionName) throws Exception {
		DefaultBpmTask task = bpmTaskManager.get(taskId);
		if (task == null)
			throw new RuntimeException("任务不存在，可能已经被处理！");
		TaskToAgreeVo toAgreeVo = new TaskToAgreeVo();
		BpmProcessInstance bpmProcessInstance = bpmInstService.getProcessInstance(task.getProcInstId());
		String defId = task.getProcDefId();
		String nodeId = task.getNodeId();
		String defKey = bpmProcessInstance.getProcDefKey();
		String typeId = task.getTypeId();
		toAgreeVo.setTaskId(taskId);
		toAgreeVo.setActionName(actionName);
		BpmNodeDef taskNodeDef = bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);

		NodeProperties nodeProperties = BpmUtil.getNodeProperties(bpmProcessInstance, nodeId);

		String jumpType = "common";
		boolean isGoNextJustEndEvent = isGoNextJustEndEvent(taskNodeDef);// 任务下一个节点是否为结束节点
		toAgreeVo.setGoNextJustEndEvent(isGoNextJustEndEvent);
		if (nodeProperties != null) {
			jumpType = nodeProperties.getJumpType();
			if (OpinionStatus.AGREE.getKey().equals(actionName)) {
				// 主要用于人员的计算。
				List<ObjectNode> boDatas = boDataService.getDataByInst(bpmProcessInstance);
				// BO数据前置处理
				BoDataUtil.hanlerData(bpmProcessInstance, nodeId, boDatas);

				if (jumpType.indexOf("select") > -1) {
					Map<String, Object> outcomeUserMap = new HashMap<>();
					List<BpmNodeDef> outcomeNodes = taskNodeDef.getOutcomeNodes();
					List<BpmNodeDef> handlerSelectOutcomeNodes = handlerSelectOutcomeNodes(outcomeNodes);
					for (BpmNodeDef bpmNodeDef : handlerSelectOutcomeNodes) {
						List<BpmIdentity> bpmIdentitys = bpmIdentityService.searchByNode(task.getProcInstId(),
								bpmNodeDef.getNodeId());
						outcomeUserMap.put(bpmNodeDef.getNodeId(), bpmIdentitys);
					}
					toAgreeVo.setOutcomeUserMap(outcomeUserMap);
					toAgreeVo.setOutcomeNodes(handlerSelectOutcomeNodes);
				}
				if (jumpType.indexOf("free") > -1) {
					List<BpmNodeDef> allNodeDef = bpmDefinitionAccessor.getAllNodeDef(defId);
					// 移除开始节点
					List<BpmNodeDef> removeList = new ArrayList<BpmNodeDef>();
					Map<String, Object> allNodeUserMap = new HashMap<>();
					for (BpmNodeDef bpmNodeDef : allNodeDef) {

						if (NodeType.START.equals(bpmNodeDef.getType())) {
							removeList.add(bpmNodeDef);
						} else if (NodeType.USERTASK.equals(bpmNodeDef.getType())
								|| NodeType.SIGNTASK.equals(bpmNodeDef.getType())) {
							List<BpmIdentity> bpmIdentitys = bpmIdentityService.searchByNode(task.getProcInstId(),
									bpmNodeDef.getNodeId());
							allNodeUserMap.put(bpmNodeDef.getNodeId(), bpmIdentitys);
						}
					}
					allNodeDef.removeAll(removeList);
					toAgreeVo.setAllNodeDef(allNodeDef);
					toAgreeVo.setAllNodeUserMap(allNodeUserMap);
				}
			}
		}

		// 如果是会签节点， 判断用户是否有会签特权中的 [所有， 直接]
		if (taskNodeDef instanceof SignNodeDef) {
			String userId = ContextUtil.getCurrentUserId();
			SignNodeDef signNodeDef = (SignNodeDef) taskNodeDef;
			Map<String, Object> variables = natTaskService.getVariables(taskId);
			List<PrivilegeMode> pmLists = signService.getPrivilege(userId, signNodeDef, variables);
			if (pmLists.contains(PrivilegeMode.ALL) || pmLists.contains(PrivilegeMode.DIRECT)) {
				toAgreeVo.setDirectHandlerSign(true);
			}
		}
		List<String> approvalItem = bpmModelFeignService.getApprovalByDefKeyAndTypeId(defKey, typeId,
				ContextUtil.getCurrentUserId());
		toAgreeVo.setJumpType(jumpType);
		toAgreeVo.setApprovalItem(approvalItem);
		return toAgreeVo;
	}

	// 判断当前节点的后续节点是否结束节点
	private boolean isGoNextJustEndEvent(BpmNodeDef taskNodeDef) {
		boolean isGoNextJustEndEvent = true;
		try {
			List<BpmNodeDef> outcomeNodes = taskNodeDef.getOutcomeNodes();
			if (BeanUtils.isNotEmpty(outcomeNodes)) {
				for (BpmNodeDef bpmNodeDef : outcomeNodes) {
					if (!bpmNodeDef.getType().equals(NodeType.END)) {
						return false;
					}
				}
			}
		} catch (Exception e) {
		}
		return isGoNextJustEndEvent;
	}

	@Override
	@Transactional
	public TaskToRejectVo toReject(String taskId, String backModel) throws Exception {
		boolean canRejectToStart = false; // 是否可以驳回发起人
		boolean canRejectToAnyNode = false; // 是否可以驳回指定节点
		boolean canReject = false;
		boolean canRejectPreAct = false;// 是否可以驳回到上一步
		DefaultBpmTask task = bpmTaskManager.get(taskId);
		if (BeanUtils.isEmpty(task)) {
			throw new WorkFlowException("当前任务已办理，不可重复办理！");
		}
		String defId = task.getProcDefId();
		String nodeId = task.getNodeId();

		BpmNodeDef taskNodeDef = bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);
		BpmProcessDef<BpmProcessDefExt> bpmProcessDef = bpmDefinitionAccessor.getBpmProcessDef(defId);

		List<Button> buttons = BpmUtil.getButtons(taskNodeDef, task);
		for (Button button : buttons) {
			if ("reject".equals(button.getAlias())) {
				String rejectMode = button.getRejectMode();
				if (StringUtil.isNotEmpty(rejectMode)) {
					List<String> rejectModeList = Arrays.asList(rejectMode.split(","));
					if (rejectModeList.contains("backToStart")) {
						canRejectToStart = true;
					}
					if (rejectModeList.contains("rejectPre")) {
						canRejectPreAct = true;
					}
					if (rejectModeList.contains("reject")) {
						canRejectToAnyNode = true;
					}

				}
			}
		}

		NodeProperties nodeProperties = taskNodeDef.getLocalProperties();
		String backMode = nodeProperties.getBackMode();
		if (StringUtil.isEmpty(backMode))
			backMode = "normal";
		String backNode = nodeProperties.getBackNode();
		String procInstId = task.getProcInstId();

		// 允许直来直往的节点
		List<BpmNodeDef> allowDirectNode = new ArrayList<BpmNodeDef>();
		// 允许按流程图执行的节点
		List<BpmNodeDef> allowNormalNode = new ArrayList<BpmNodeDef>();
		// 查找出在网关内的节点
		Map<String, BpmNodeDef> inGatewayNodeMap = BpmUtil.getInGatewayNodeMap(bpmProcessDef);
		// 查找出该点在流程图上所有的前置节点，因为某些复杂网关嵌套的情况下，堆栈会乱
		Map<String, BpmNodeDef> beforeNodes = BpmUtil.getNodesByDirection(taskNodeDef,"");
		// 找到所有走过的节点
		BpmExeStackRelationManager relationManager = AppUtil.getBean(BpmExeStackRelationManager.class);
		List<BpmExeStackRelation> relationList = relationManager.getListByProcInstId(procInstId);

		Set<String> bpmExeStackSet = new HashSet<String>();
		for (BpmExeStackRelation relation : relationList) {
			BpmNodeDef node = beforeNodes.get(relation.getFromNodeId());
			if (BeanUtils.isEmpty(node) || bpmExeStackSet.contains(node.getNodeId())
					|| node.getNodeId().equals(nodeId)) {
				continue;
			}
			// 外部子流程节点，用户任务会签任务，允许驳回
			if (node.getType().equals(NodeType.CALLACTIVITY) || node.getType().equals(NodeType.USERTASK)
					|| node.getType().equals(NodeType.SIGNTASK)) {
				allowDirectNode.add(node);
				bpmExeStackSet.add(node.getNodeId());
				// 不在网关内，可以按照流程图驳回
				if (!inGatewayNodeMap.containsKey(node.getNodeId())) {
					allowNormalNode.add(node);
				}
			}
		}
		TaskToRejectVo taskToRejectVo = new TaskToRejectVo();
		taskToRejectVo.setTaskId(taskId);
		taskToRejectVo.setBackMode(backMode);
		taskToRejectVo.setBackNode(backNode);
		// 只保留指定的可驳回的节点
		List<String> backNodeList = new ArrayList<String>();
		if (StringUtil.isNotEmpty(backNode)) {
			String[] split = backNode.split(",");
			backNodeList = Arrays.asList(split);
			List<BpmNodeDef> tmp = new ArrayList<BpmNodeDef>();
			for (BpmNodeDef bpmNodeDef : allowNormalNode) {
				if (backNodeList.contains(bpmNodeDef.getNodeId())) {
					tmp.add(bpmNodeDef);
				}
			}
			allowNormalNode = tmp;
			tmp = new ArrayList<BpmNodeDef>();
			for (BpmNodeDef bpmNodeDef : allowDirectNode) {
				if (backNodeList.contains(bpmNodeDef.getNodeId())) {
					tmp.add(bpmNodeDef);
				}
			}
			allowDirectNode = tmp;
		}
		if (canRejectToAnyNode) {
			canRejectToAnyNode = !(allowDirectNode.size() == 0 && allowNormalNode.size() == 0);
		}
		taskToRejectVo.setAllowNormalNode(allowNormalNode);
		taskToRejectVo.setAllowDirectNode(allowDirectNode);
		taskToRejectVo.setCanReject(canReject);
		taskToRejectVo.setCanRejectPreAct(canRejectPreAct);
		taskToRejectVo.setCanRejectToAnyNode(canRejectToAnyNode);
		taskToRejectVo.setCanRejectToStart(canRejectToStart);

		List<BpmNodeDef> incomeNodes = taskNodeDef.getIncomeNodes();
		// 判断当前节点是否在网关的出口，如果是驳回的时候，隐藏驳回上一步按钮，让用户选择一条分支
		if (incomeNodes.size() == 1) {
			BpmNodeDef beforNode = incomeNodes.get(0);
			if ((beforNode.getType().equals(NodeType.PARALLELGATEWAY)
					|| beforNode.getType().equals(NodeType.INCLUSIVEGATEWAY))
					&& beforNode.getIncomeNodes().size() > 1) {
				taskToRejectVo.setAfterGateway(true);
			}
		}
		//网关里面，隐藏驳回上一步按钮，让用户选择一个节点驳回
		if (inGatewayNodeMap.containsKey(nodeId)) {
			taskToRejectVo.setInGateway(true);
		}
		//标记是不是内部子流程节点，如果是，则不允许驳回发起人不重走
		if (BeanUtils.isNotEmpty(taskNodeDef.getParentBpmNodeDef())) {
			taskToRejectVo.setInSubProcess(true);
		}
		return taskToRejectVo;
	}

	@Override
	@Transactional
	public CommonResult<String> withDraw(WithDrawParam withDrawParam) throws Exception {
		try {
			taskTransService.withDraw(withDrawParam.getTaskId(), withDrawParam.getNotifyType(),
					withDrawParam.getOpinion());
			return new CommonResult<String>(true, "追回成功", "");
		} catch (Exception e) {
			return new CommonResult<String>(false, "追回失败：" + e.getMessage(), "");
		}
	}

	@Override
	public List<ObjectNode> opinionHistory(String instId, String taskId, boolean isCommu) throws IOException {
		if ((instId == null || instId.isEmpty()) && taskId != null) {
			instId = bpmTaskService.getByTaskId(taskId).getProcInstId();
		}
		List<BpmTaskOpinion> bpmTaskOpinions = bpmOpinionService.getTaskOpinions(instId);

		List<ObjectNode> opinionList = new ArrayList<ObjectNode>();
		Set<String> userIds = new HashSet<>();

		for (int i = 0; i < bpmTaskOpinions.size(); i++) {
			ObjectNode obj = (ObjectNode) JsonUtil.toJsonNode(bpmTaskOpinions.get(i));
			opinionList.add(obj);
			if (obj.hasNonNull("agentLeaderId")) {
				userIds.add(obj.get("agentLeaderId").asText());
			}
		}
		if (!userIds.isEmpty()) {
			UCFeignService uCFeignService = AppUtil.getBean(UCFeignService.class);
			ArrayNode users = uCFeignService.getUserByIdsOrAccounts(StringUtil.join(new ArrayList<>(userIds), ","));
			Map<String, String> userNameMap = new HashMap<>();
			for (JsonNode jsonNode : users) {
				ObjectNode objectNode = (ObjectNode) jsonNode;
				userNameMap.put(objectNode.get("id").asText(), objectNode.get("fullname").asText());
			}
			for (ObjectNode opinion : opinionList) {
				if (opinion.hasNonNull("agentLeaderId")) {
					opinion.put("statusVal", "代【" + userNameMap.get(opinion.get("agentLeaderId").asText()) + "】"
							+ OpinionStatus.fromKey(opinion.get("status").asText()).getValue());
				}
			}
		}

		QueryFilter queryFilter = QueryFilter.<BpmInterposeRecored> build();
		queryFilter.addFilter("PROC_INST_ID_", instId, QueryOP.EQUAL);
		BpmInterposeRecoredManager manager = AppUtil.getBean(BpmInterposeRecoredManager.class);
		PageList<BpmInterposeRecored> query = manager.query(queryFilter);
		List<ObjectNode> interPoseList = new ArrayList<ObjectNode>();
		for (BpmInterposeRecored recored : query.getRows()) {
			ObjectNode obj = (ObjectNode) JsonUtil.toJsonNode(recored);
			obj.put("statusVal", InterPoseType.fromKey(recored.getStatus()).getValue());
			interPoseList.add(obj);
		}
		opinionList.addAll(interPoseList);
		return opinionList;
	}

	@Override
	@Transactional
	public CommonResult<String> taskToInqu(TaskTransParamObject taskTransParamObject) throws Exception {
		BpmTask pendingTask = bpmTaskManager.get(taskTransParamObject.getTaskId());
		if (BeanUtils.isEmpty(pendingTask)) {
			return new CommonResult<String>(false, "当前任务已办理，不可重复办理！");
		}
		taskTransParamObject.setTaskId(pendingTask.getTaskId());
		String notifyType = taskTransParamObject.getNotifyType();
		String opinion = taskTransParamObject.getOpinion();
		String userIds = taskTransParamObject.getUserIds();
		String currentUserId = ContextUtil.getCurrentUserId();
		if (StringUtil.isEmpty(userIds)) {
			throw new RequiredException(ResponseErrorEnums.REQUIRED_ERROR + ":userIds征询人员id必填");
		}
		if (StringUtil.isEmpty(opinion)) {
			throw new RequiredException(ResponseErrorEnums.REQUIRED_ERROR.getMessage() + ":opinion征询意见必填");
		}
		String[] userIdArray = userIds.split(",");

		List<IUser> userList = new ArrayList<IUser>();
		for (String id : userIdArray) {
			IUser u = userServiceImpl.getUserById(id);
			if (u != null)
				userList.add(u);
			if (currentUserId.equals(u.getUserId())) {
				return new CommonResult<String>(false, "征询人员不能包含本人", "");
			}
		}
		String files = taskTransParamObject.getFiles();
		taskTransParamObject.setVoteType(null);
		ObjectNode transObject = (ObjectNode) JsonUtil.toJsonNode(taskTransParamObject.toString());
		BpmTaskTrans taskTrans = JsonUtil.toBean(transObject, BpmTaskTrans.class);
		taskTransService.addTaskToInqu(taskTrans, userList, notifyType, opinion, files);
		return new CommonResult<String>(true, "征询成功", "");
	}

	@Override
	@Transactional
	public void addReadRecord(Object obj) {
		try {
			boolean isCreate = false;
			BpmReadRecord bpmReadRecord = new BpmReadRecord();
			bpmReadRecord.setId(UniqueIdUtil.getSuid());
			IUser user = ContextUtil.getCurrentUser();
			if (!user.isAdmin()) {
				if (BeanUtils.isNotEmpty(user)) {
					bpmReadRecord.setReader(user.getUserId());
					bpmReadRecord.setReaderName(user.getFullname());
				}
				bpmReadRecord.setReadTime(LocalDateTime.now());
				ObjectNode node = ucFeignService.getMainGroup(user.getUserId());
				if (BeanUtils.isNotEmpty(node)) {
					bpmReadRecord.setOrgId(node.get("id").asText());
					bpmReadRecord.setOrgPath(node.get("pathName").asText());
				}
				DefaultBpmProcessInstance instance = null;
				if (obj instanceof DefaultBpmTask) {
					isCreate = true;
					DefaultBpmTask task = (DefaultBpmTask) obj;
					bpmReadRecord.setTaskId(task.getId());
					bpmReadRecord.setTaskName(task.getName());
					bpmReadRecord.setProcDefId(task.getProcDefId());
					bpmReadRecord.setProcInstId(task.getProcInstId());
					bpmReadRecord.setTaskKey(task.getNodeId());

				} else if (obj instanceof DefaultBpmProcessInstance) {
					isCreate = true;
					instance = (DefaultBpmProcessInstance) obj;
				}
				if (BeanUtils.isNotEmpty(instance)) {
					bpmReadRecord.setProcInstId(instance.getId());
					bpmReadRecord.setProcDefId(instance.getProcDefId());
					bpmReadRecord.setSupInstId(instance.getParentInstId());
				}
				if (isCreate) {
					bpmReadRecordManager.create(bpmReadRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	@Transactional
	public void noticeTurnDode(String taskId) {
		try {
			BpmTaskNoticeManager noticeManager = AppUtil.getBean(BpmTaskNoticeManager.class);
			BpmTaskNoticeDoneManager noticeDoneManager = AppUtil.getBean(BpmTaskNoticeDoneManager.class);
			// 1.添加知会已办
			BpmTaskNotice bpmTaskNotice = noticeManager.get(taskId);// 查询知会待办
			String taskName = "";
			if (bpmTaskNotice.getStatus().equals("FOLLOW")) {
				taskName = "【跟踪】" + bpmTaskNotice.getName();
			} else {
				taskName = bpmTaskNotice.getName();
			}
			BpmTaskNoticeDone bpmTaskNoticeDone = new BpmTaskNoticeDone(taskName, bpmTaskNotice.getProcDefId(),
					bpmTaskNotice.getProcInstId(), bpmTaskNotice.getAssigneeId(), bpmTaskNotice.getAssigneeName(),
					bpmTaskNotice.getStatus(), bpmTaskNotice.getSubject(), bpmTaskNotice.getProcDefName(),
					bpmTaskNotice.getOwnerName(), bpmTaskNotice.getId(), bpmTaskNotice.getTaskId(),
					bpmTaskNotice.getNodeId(), bpmTaskNotice.getSupportMobile());
			noticeDoneManager.create(bpmTaskNoticeDone);

			// 2.删除知会待办
			if (!"COPYTO".equals(bpmTaskNotice.getStatus())) {
				noticeManager.removeByIds(taskId);
			} else {
				BpmTaskNotice notice = bpmTaskNotice;
				notice.setIsRead(1);
				noticeManager.update(notice);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ObjectNode getAfterJumpNode(ObjectNode obj) throws Exception {
		String taskId = "";
		if (obj.has("taskId")) {
			taskId = obj.get("taskId").asText();
		}
		String defId = "";
		if (obj.has("defId")) {
			defId = obj.get("defId").asText();
		}

		String instId = "";
		if (obj.has("instId")) {
			instId = obj.get("instId").asText();
		}

		String bodata = "";
		if (obj.has("data")) {
			bodata = obj.get("data").asText();
			bodata = Base64.getFromBase64(bodata);
		}
		String curNodeId = "";
		BpmNodeDef curBpmNodeDef = null;

		if (StringUtil.isNotEmpty(taskId)) {
			DefaultBpmTask task = bpmTaskManager.get(taskId);
			if (BeanUtils.isEmpty(task)) {
				throw new NotFoundException("任务不存在，可能被处理了。任务ID:" + taskId);
			}
			curNodeId = task.getNodeId();
			defId = task.getProcDefId();
		} else if (StringUtil.isNotEmpty(instId)) {
			DefaultBpmProcessInstance defaultBpmProcessInstance = bpmProcessInstanceManager.get(instId);
			if (BeanUtils.isNotEmpty(defaultBpmProcessInstance)) {
				defId = defaultBpmProcessInstance.getProcDefId();
			}
		} else if (StringUtil.isEmpty(defId)) {
			throw new RequiredException("任务ID和定义ID必须传一个");
		}
		BpmProcessDef<BpmProcessDefExt> bpmProcessDefExt = bpmDefinitionAccessor.getBpmProcessDef(defId);
		List<BpmNodeDef> bpmnNodeDefs = bpmProcessDefExt.getBpmnNodeDefs();
		boolean isSkipFirstNode = false;
		if (StringUtil.isEmpty(taskId)) {
			curBpmNodeDef = bpmProcessDefExt.getStartEvent();
			BpmDefExtProperties extProperties = bpmProcessDefExt.getProcessDefExt().getExtProperties();
			if (extProperties.isSkipFirstNode()) {
				isSkipFirstNode = true;
			}

		} else {
			curBpmNodeDef = findBpmNodeDefByNodeId(bpmnNodeDefs, curNodeId);
		}

		List<BpmNodeDef> nextAllNodeDef = new ArrayList<>();
		NodeProperties nodeProperties = curBpmNodeDef.getLocalProperties();
		String jumpType = nodeProperties.getJumpType();
		String helpGlobal = nodeProperties.getHelpGlobal();
		String help = nodeProperties.getHelp();
		String choiceExcutor = nodeProperties.getChoiceExcutor();
		String approvalArea = nodeProperties.getApprovalArea();
		Boolean referOpinion = nodeProperties.isReferOpinion();
		// 如果是选择路径跳转。或者选择执行人。则找出下一个节点
		if (StringUtil.isNotEmpty(jumpType) && !"select".equals(jumpType)) {
			nextAllNodeDef = bpmnNodeDefs;
		} else if ("select".equals(jumpType) || StringUtil.isNotEmpty(choiceExcutor)) {
			if (isSkipFirstNode) {
				curBpmNodeDef = curBpmNodeDef.getOutcomeNodes().get(0);
			}
			getNextAllNodes(curBpmNodeDef, nextAllNodeDef);
		}

		DefaultBpmProcessDefExt defExt = (DefaultBpmProcessDefExt) bpmProcessDefExt.getProcessDefExt();
		List<ObjectNode> bodatas = BusDataUtil.transFormDataToBoData(defExt, bodata);
		BpmContextUtil.setBoToContext(bodatas);
		// BpmContextUtil.setBoToTreadContext(bodatas);

		Map<String, Object> variables = new HashMap<String, Object>();

		if (StringUtil.isNotEmpty(taskId)) {
			BpmTask bpmTask = bpmTaskManager.get(taskId);
			// 流转任务不执行此操作
			if (!bpmTask.getStatus().equals("TRANSFORMED") && "candidate".equals(choiceExcutor)) {
				TaskService taskService = AppUtil.getBean(TaskService.class);
				variables = taskService.getVariables(taskId);
				variables.put(BpmConstants.NOT_REQUEST_UC, "true");
			} else {
				variables.put(BpmConstants.START_USER, ContextUtil.getCurrentUserId());
			}
		}

		Map<String, Object> nodeMap = new HashMap<>();
		ArrayNode nodeArray = JsonUtil.getMapper().createArrayNode();
		Map<String, BpmNodeDef> inGatewayNodeMap = BpmUtil.getInGatewayNodeMap(bpmProcessDefExt);
		for (BpmNodeDef bpmNodeDef : nextAllNodeDef) {
			try {
				String nodeType = bpmNodeDef.getType().getKey();
				// 只有用户任务和会签任务。才抽取节点执行人
				if (!curNodeId.equals(bpmNodeDef.getNodeId())
						&& (NodeType.USERTASK.getKey().equals(nodeType) || NodeType.SIGNTASK.getKey().equals(nodeType)
								|| NodeType.CUSTOMSIGNTASK.getKey().equals(nodeType))) {
					if (inGatewayNodeMap.containsKey(bpmNodeDef.getNodeId())) {
						continue;
					}
					ObjectNode nodeObj = JsonUtil.getMapper().createObjectNode();
					nodeObj.put("nodeName", bpmNodeDef.getName());
					nodeObj.put("nodeId", bpmNodeDef.getNodeId());
					nodeObj.put("nodeType", bpmNodeDef.getType().getKey());
					nodeArray.add(nodeObj);

					// 如果是选择候选人，则抽取候选人
					if ("candidate".equals(choiceExcutor)) {
						UserQueryPluginHelper userQueryPluginHelper = AppUtil.getBean(UserQueryPluginHelper.class);
						List<BpmPluginContext> bpmPluginContexts = bpmNodeDef.getBpmPluginContexts();
						// 解析节点人员。抽取执行人

						List<BpmIdentity> bpmIdentities = userQueryPluginHelper.query(bpmPluginContexts, variables,
								UserQueryPluginHelper.TYPE_USER);
						List<Map<String, Object>> identitiesMap = new ArrayList<>();
						for (BpmIdentity bpmIdentitie : bpmIdentities) {
							identitiesMap.add(JsonUtil.toMap(JsonUtil.toJson(bpmIdentitie)));
						}
						nodeMap.put(bpmNodeDef.getNodeId(), identitiesMap);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (StringUtil.isNotEmpty(taskId)) {
			BpmTask bpmTask = bpmTaskManager.get(taskId);
			// 流转任务不执行选择路径跳转
			if (bpmTask.getStatus().equals("TRANSFORMED")) {
				jumpType = "";
				choiceExcutor = "";
				nodeArray = JsonUtil.getMapper().createArrayNode();
			}
		}
		ObjectNode resulObj = JsonUtil.getMapper().createObjectNode();
		resulObj.put("jumpType", jumpType);
		resulObj.put("help", help);
		resulObj.put("helpGlobal", helpGlobal);
		resulObj.set("afterNodes", nodeArray);
		resulObj.put("choiceExcutor", choiceExcutor);
		resulObj.put("approvalArea", approvalArea);
		resulObj.put("referOpinion", referOpinion);
		// 不是选择候选人。直接返回，不进行抽取
		if (!"candidate".equals(choiceExcutor)) {
			return resulObj;
		}

		UCFeignService ucFeignService = AppUtil.getBean(UCFeignService.class);
		if (BeanUtils.isNotEmpty(nodeMap)) {
			nodeMap = ucFeignService.calculateNodeUser(nodeMap);
		}
		for (JsonNode jsonNode : nodeArray) {
			ObjectNode objNode = (ObjectNode) jsonNode;
			if (nodeMap.containsKey(objNode.get("nodeId").asText())) {
				List<Object> list = (List<Object>) nodeMap.get(objNode.get("nodeId").asText());
				ArrayNode arrayNode = JsonUtil.getMapper().createArrayNode();
				for (Object excutorObj : list) {
					arrayNode.add(JsonUtil.toJsonNode(excutorObj));
				}
				objNode.set("excutorList", arrayNode);
			}
		}
		return resulObj;
	}

	@Override
	public BpmNodeDef getCurNodeProperties(String taskId, String defId, String instId) throws Exception {
		String curNodeId = "";
		BpmNodeDef curBpmNodeDef = null;
		if (StringUtil.isNotEmpty(taskId)) {
			DefaultBpmTask task = bpmTaskManager.get(taskId);
			if (BeanUtils.isEmpty(task)) {
				throw new NotFoundException("任务不存在，可能被处理了。任务ID:" + taskId);
			}
			curNodeId = task.getNodeId();
			defId = task.getProcDefId();
		} else if (StringUtil.isEmpty(defId)) {
			throw new RequiredException("任务ID和定义ID必须传一个");
		}
		BpmProcessDef<BpmProcessDefExt> bpmProcessDefExt = bpmDefinitionAccessor.getBpmProcessDef(defId);
		if (StringUtil.isEmpty(taskId)) {
			curBpmNodeDef = bpmProcessDefExt.getStartEvent();
			/*
			 * BpmDefExtProperties extProperties =
			 * bpmProcessDefExt.getProcessDefExt().getExtProperties(); if
			 * (extProperties.isSkipFirstNode()) { curBpmNodeDef =
			 * curBpmNodeDef.getOutcomeNodes().get(0); }
			 */
		} else {
			List<BpmNodeDef> bpmnNodeDefs = new ArrayList<BpmNodeDef>();
			List<BpmNodeDef> tempNodes = bpmProcessDefExt.getBpmnNodeDefs();
			for (BpmNodeDef bpmNodeDef : tempNodes) {
				bpmnNodeDefs.add(bpmNodeDef);
				if (bpmNodeDef.getType().getKey().equals(NodeType.SUBPROCESS.getKey())) {
					getSubNodes(bpmnNodeDefs, bpmNodeDef);
				}
			}
			for (BpmNodeDef bpmNodeDef : bpmnNodeDefs) {
				if(bpmNodeDef.getNodeId().equals(curNodeId)){
					curBpmNodeDef = bpmNodeDef;
					break;
				}
			}
		}
		return curBpmNodeDef;
	}
	
	private void getSubNodes(List<BpmNodeDef> bpmnNodeDefs,BpmNodeDef bpmNodeDef){
		SubProcessNodeDef subProcessNodeDef = (SubProcessNodeDef) bpmNodeDef;
		List<BpmNodeDef> subNodeDefs = subProcessNodeDef.getChildBpmProcessDef().getBpmnNodeDefs();
		for (BpmNodeDef subNodeDef : subNodeDefs) {
			bpmnNodeDefs.add(subNodeDef);
			if (subNodeDef.getType().getKey().equals(NodeType.SUBPROCESS.getKey())) {
				getSubNodes(bpmnNodeDefs, subNodeDef);
			}
		}
	}

	private BpmNodeDef findBpmNodeDefByNodeId(List<BpmNodeDef> bpmnNodeDefs, String nodeId) {
		BpmNodeDef curBpmNodeDef = null;
		for (BpmNodeDef bpmNodeDef : bpmnNodeDefs) {
			if (bpmNodeDef.getType().getKey().equals(NodeType.SUBPROCESS.getKey())) {
				SubProcessNodeDef subProcessNodeDef = (SubProcessNodeDef) bpmNodeDef;
				curBpmNodeDef = findBpmNodeDefByNodeId(subProcessNodeDef.getChildBpmProcessDef().getBpmnNodeDefs(),
						nodeId);
			} else if (bpmNodeDef.getNodeId().equals(nodeId)) {
				curBpmNodeDef = bpmNodeDef;
			}
		}
		return curBpmNodeDef;
	}

	@Override
	public PageList<Map<String, Object>> getFlowFieldList(QueryFilter queryFilter) throws Exception {
		// 查询列表
		try {
			List<Map<String, Object>> list = bpmProcessInstanceManager.getFlowFieldList(queryFilter);
			return new PageList<Map<String, Object>>(list);
		} catch (Exception e) {
			throw new RuntimeException("数据库查询出错了！");
		}

	}

	@Override
	@Transactional
	public CommonResult<String> taskToApproveLine(TaskApproveLineParam taskApproveLineParam) throws Exception {
		String notifyType = taskApproveLineParam.getNotifyType();
		String opinion = taskApproveLineParam.getOpinion();
		String userIds = taskApproveLineParam.getUserIds();
		String currentUserId = ContextUtil.getCurrentUserId();
		if (StringUtil.isEmpty(userIds)) {
			throw new RequiredException(ResponseErrorEnums.REQUIRED_ERROR.getMessage() + ":userIds流转人员id必填");
		}
		if (StringUtil.isEmpty(opinion)) {
			throw new RequiredException(ResponseErrorEnums.REQUIRED_ERROR.getMessage() + ":opinion流转意见必填");
		}
		String[] userIdArray = userIds.split(",");

		List<IUser> userList = new ArrayList<IUser>();
		for (String id : userIdArray) {
			IUser u = userServiceImpl.getUserById(id);
			if (u != null)
				userList.add(u);
			if (currentUserId.equals(u.getUserId())) {
				throw new RuntimeException("流转人员不能包含本人！");
			}
		}
		TaskTransParamObject taskTransParamObject = JsonUtil.toBean(JsonUtil.toJson(taskApproveLineParam),
				TaskTransParamObject.class);
		taskTransParamObject.setAction("submit");// 流转结束后的动作 往下流转
		taskTransParamObject.setDecideType("agree");// 计票策略 同意票
		taskTransParamObject.setSignType("parallel");// 流转类型 并行
		taskTransParamObject.setVoteType("percent");// 投票类型
		taskTransParamObject.setVoteAmount(Short.valueOf("100"));// 票数
		ObjectNode transObject = (ObjectNode) JsonUtil.toJsonNode(taskTransParamObject.toString());
		BpmTaskTrans taskTrans = JsonUtil.toBean(transObject, BpmTaskTrans.class);
		taskTransService.addApproveLineTask(taskTrans, userList, notifyType, opinion);
		return new CommonResult<String>(true, "发起并行审批成功", "");
	}

	@Override
	@Transactional
	public CommonResult<String> taskToSignSequence(TaskTransParamObject taskTransParamObject) throws Exception {
		String notifyType = taskTransParamObject.getNotifyType();
		String opinion = taskTransParamObject.getOpinion();
		String userIds = taskTransParamObject.getUserIds();
		String currentUserId = ContextUtil.getCurrentUserId();
		if (StringUtil.isEmpty(userIds)) {
			throw new RequiredException(ResponseErrorEnums.REQUIRED_ERROR.getMessage() + ":userIds签署人员id必填");
		}
		if (StringUtil.isEmpty(opinion)) {
			throw new RequiredException(ResponseErrorEnums.REQUIRED_ERROR.getMessage() + ":opinion签署意见必填");
		}
		String[] userIdArray = userIds.split(",");

		List<IUser> userList = new ArrayList<IUser>();
		for (String id : userIdArray) {
			IUser u = userServiceImpl.getUserById(id);
			if (u != null)
				userList.add(u);
			if (currentUserId.equals(u.getUserId())) {
				throw new RuntimeException("签署人员不能包含本人！");
			}
		}
		ObjectNode transObject = (ObjectNode) JsonUtil.toJsonNode(taskTransParamObject.toString());
		BpmTaskTrans taskTrans = JsonUtil.toBean(transObject, BpmTaskTrans.class);
		taskTransService.addSignSequenceTask(taskTrans, userList, notifyType, opinion);
		return new CommonResult<String>(true, "发起顺序签署成功", "");
	}

	@Override
	@Transactional
	public CommonResult<String> revokeTrans(RevokeTransParamObject revokeTransParamObject) throws Exception {
		// 并行审批源撤回
		if (revokeTransParamObject.getTaskId().equals(revokeTransParamObject.getParentTaskId())) {
			try {
				taskTransService.withDraw(revokeTransParamObject.getTaskId(), revokeTransParamObject.getMessageType(),
						revokeTransParamObject.getCause(), TemplateConstants.TYPE_KEY.BPMN_TASK_APPROVE_LINE_CANCEL);
				return new CommonResult<String>(true, "撤回成功", "");
			} catch (Exception e) {
				return new CommonResult<String>(false, "撤回失败：" + e.getMessage(), "");
			}
		} else {// 并行审批任务撤回
			DefaultBpmTask parentTask = bpmTaskManager.get(revokeTransParamObject.getParentTaskId());
			if (!(BeanUtils.isNotEmpty(parentTask)
					&& TaskType.APPROVELINEING.getKey().equals(parentTask.getStatus()))) {
				return new CommonResult<String>(false, "撤回失败：当前节点已不支持撤回！");
			}
			DefaultBpmCheckOpinion bpmCheckOpinion = bpmCheckOpinionManager.getByTaskIdStatus(
					revokeTransParamObject.getTaskId(), OpinionStatus.APPROVE_LINEING_AGREE.getKey());
			if (BeanUtils.isEmpty(bpmCheckOpinion)) {
				return new CommonResult<String>(false,
						"撤回失败：根据任务ID：" + revokeTransParamObject.getTaskId() + "未找到对应审批记录！");
			}
			try {
				IUser user = ContextUtil.getCurrentUser();
				// 添加新的流转任务
				taskTransService.addRevokeTask(parentTask, user, revokeTransParamObject.getMessageType());
				// 更新审批记录
				bpmCheckOpinion.setStatus(OpinionStatus.REVOKER.getKey());
				bpmCheckOpinion.setCompleteTime(LocalDateTime.now());
				long durMs = TimeUtil.getTime(bpmCheckOpinion.getCompleteTime(), bpmCheckOpinion.getCreateTime());
				bpmCheckOpinion.setDurMs(durMs);
				bpmCheckOpinion.setAuditor(user.getUserId());
				bpmCheckOpinion.setAuditorName(user.getFullname());
				bpmCheckOpinion.setOpinion(revokeTransParamObject.getCause());
				bpmCheckOpinionManager.update(bpmCheckOpinion);
				return new CommonResult<String>(true, "撤回成功", "");
			} catch (Exception e) {
				return new CommonResult<String>(false, "撤回失败：" + e.getMessage(), "");
			}
		}
	}

	@Override
	@Transactional
	public void revokeSignSequence(RevokeTransParamObject revokeTransParamObject) throws Exception {
		taskTransService.revokeSignSequence(revokeTransParamObject);
	}

	@Override
	@Transactional
	public CommonResult<String> taskToSignLine(TaskTransParamObject taskTransParamObject) throws Exception {

		String notifyType = taskTransParamObject.getNotifyType();
		String opinion = taskTransParamObject.getOpinion();
		String userIds = taskTransParamObject.getUserIds();
		String currentUserId = ContextUtil.getCurrentUserId();
		if (StringUtil.isEmpty(userIds)) {
			throw new RequiredException(ResponseErrorEnums.REQUIRED_ERROR.getMessage() + ":userIds签署人员id必填");
		}
		if (StringUtil.isEmpty(opinion)) {
			throw new RequiredException(ResponseErrorEnums.REQUIRED_ERROR.getMessage() + ":opinion签署意见必填");
		}
		String[] userIdArray = userIds.split(",");

		List<IUser> userList = new ArrayList<IUser>();
		for (String id : userIdArray) {
			IUser u = userServiceImpl.getUserById(id);
			if (u != null)
				userList.add(u);
			if (currentUserId.equals(u.getUserId())) {
				throw new RuntimeException("签署人员不能包含本人！");
			}
		}
		ObjectNode transObject = (ObjectNode) JsonUtil.toJsonNode(taskTransParamObject.toString());
		BpmTaskTrans taskTrans = JsonUtil.toBean(transObject, BpmTaskTrans.class);
		taskTransService.addSignLineTask(taskTrans, userList, notifyType, opinion);
		return new CommonResult<String>(true, "发起并行签署成功", "");

	}

	@Override
	@Transactional
	public void revokeSignLine(RevokeSignLineParamObject revokeParamObject) throws Exception {
		taskTransService.revokeSignLine(revokeParamObject);
	}

	@Override
	@Transactional
	public CommonResult<String> taskCustomSignUsers(AssignParamObject assignParamObject) throws Exception {
		IUser user = ContextUtil.getCurrentUser();
		String taskId = assignParamObject.getTaskId();
		String userIds = assignParamObject.getUserId();
		String messageType = assignParamObject.getMessageType();
		String addReason = assignParamObject.getOpinion();
		if (StringUtil.isEmpty(userIds)) {
			throw new RuntimeException("必须传入签署人员");
		}
		String[] userIdAry = userIds.split(",");
		List<String> userIdList = new ArrayList<String>();
		for (String userId : userIdAry) {
			if (user.getUserId().equals(userId)) {
				throw new RuntimeException(String.format("签署人员不能为自己:%s", userId));
			}
			IUser userById = ServiceUtil.getUserById(userId);
			if (BeanUtils.isEmpty(userById)) {
				throw new RuntimeException(String.format("必须传入签署人员(userId):%s不存在", userId));
			}
			userIdList.add(userById.getUserId());
		}
		String[] userIdsAry = new String[userIdList.size()];
		userIdList.toArray(userIdsAry);
		ResultMessage addSignTask = signService.addCustomSignTask(taskId, userIdAry);
		// 签署成功 发送消息
		if (addSignTask.getResult() == ResultMessage.SUCCESS) {
			if (StringUtil.isNotEmpty(messageType)) {
				List<BpmIdentity> bpmIdentities = (List<BpmIdentity>) addSignTask.getVars().get("users");
				List<IUser> users = bpmIdentityExtractService.extractUser(bpmIdentities);
				Map<String, Object> variables = natTaskService.getVariables(taskId);
				variables.put("cause", addReason);
				variables.put("sender", ContextUtil.getCurrentUser().getFullname());
				variables.put("taskSubject", variables.get("subject_"));
				String baseUrl = "";// SysPropertyUtil.getByAlias(TemplateConstants.TEMP_VAR.BASE_URL);
				variables.put("baseUrl", baseUrl);

				List<Map<String, String>> taskIds = (List<Map<String, String>>) addSignTask.getVars().get("taskIds");
				for (IUser iuser : users) {
					String taskid = findTaskId(taskIds, user.getUserId());
					if (StringUtil.isNotEmpty(taskid)) {
						variables.put("taskId", taskid);
						users = new ArrayList<IUser>();
						users.add(user);
						// 发送消息
						signService.sendNotify(users, Arrays.asList(messageType.split(",")),
								TemplateConstants.TYPE_KEY.BPMN_TASK_SIGN_SEQUENCE, variables);
					}
				}
			}

			// 审批命令
			DefaultTaskFinishCmd cmd = new DefaultTaskFinishCmd();
			cmd.setTaskId(taskId);
			cmd.setActionName("agree");
			cmd.setApprovalOpinion(assignParamObject.getOpinion());
			cmd.setDataMode(ActionCmd.DATA_MODE_BO);
			cmd.addTransitVars(BpmConstants.CUSTOM_SIGN_STATUS,
					String.valueOf(addSignTask.getVars().get("customStatus")));
			bpmTaskActionService.finishTask(cmd);

			return new CommonResult<String>(true, "添加签署人员成功");
		} else {
			throw new RuntimeException(addSignTask.getCause());
		}
	}

	@Override
	public CommonResult<BpmIdentity> nextExecutor(String taskId) {
		DefaultBpmTask task = bpmTaskManager.get(taskId);
		if (BeanUtils.isEmpty(task)) {
			throw new RuntimeException("任务不存在，可能已经被处理了.");
		}
		BpmIdentity bpmIdentity = null;
		// 如果是顺序签署任务 添加同意时 agreeTrans 显示下一步执行人
		if (TaskType.SIGNSEQUENCEED.getKey().equals(task.getStatus())) {
			String varName = BpmConstants.SIGN_USERIDS + task.getNodeId();
			Integer loopCounter = (Integer) natTaskService.getVariable(taskId, BpmConstants.NUMBER_OF_LOOPCOUNTER);
			List<BpmIdentity> list = (List<BpmIdentity>) natTaskService.getVariable(taskId, varName);
			if ((loopCounter + 1) < list.size()) {
				bpmIdentity = list.get(loopCounter + 1);
			}

		}
		return new CommonResult<BpmIdentity>(true, "获取成功", bpmIdentity);
	}
}
