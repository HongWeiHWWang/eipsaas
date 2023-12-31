package com.hotent.activiti.inst.service.impl;

import java.awt.geom.Point2D;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hotent.activiti.def.graph.ilog.activiti.BPMNEdge;
import com.hotent.activiti.def.graph.ilog.activiti.BPMNShap;
import com.hotent.activiti.def.graph.ilog.activiti.ProcessDiagramGenerator;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.cmd.BaseActionCmd;
import com.hotent.bpm.api.cmd.ProcessInstCmd;
import com.hotent.bpm.api.constant.AopType;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.constant.NodeType;
import com.hotent.bpm.api.constant.OpinionStatus;
import com.hotent.bpm.api.constant.ProcessInstanceStatus;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.event.BpmStartEvent;
import com.hotent.bpm.api.event.BpmStartModel;
import com.hotent.bpm.api.model.process.def.BpmDefinition;
import com.hotent.bpm.api.model.process.def.BpmProcessDef;
import com.hotent.bpm.api.model.process.def.BpmProcessDefExt;
import com.hotent.bpm.api.model.process.def.BpmVariableDef;
import com.hotent.bpm.api.model.process.inst.BpmInstanceTrack;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;
import com.hotent.bpm.api.model.process.inst.BpmTrackPoint;
import com.hotent.bpm.api.model.process.inst.BpmTrackSize;
import com.hotent.bpm.api.model.process.inst.TrackNode;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.UserTaskNodeDef;
import com.hotent.bpm.api.model.process.task.BpmTask;
import com.hotent.bpm.api.plugin.core.factory.BpmPluginSessionFactory;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.api.service.BpmDefinitionService;
import com.hotent.bpm.api.service.BpmInstService;
import com.hotent.bpm.engine.inst.DefaultProcessInstCmd;
import com.hotent.bpm.exception.StartFlowException;
import com.hotent.bpm.model.var.DefaultBpmVariableDef;
import com.hotent.bpm.natapi.inst.NatProInstanceService;
import com.hotent.bpm.persistence.manager.BpmCheckOpinionManager;
import com.hotent.bpm.persistence.manager.BpmDefinitionManager;
import com.hotent.bpm.persistence.manager.BpmExeStackManager;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.persistence.manager.BpmTaskManager;
import com.hotent.bpm.persistence.manager.impl.BpmTaskTurnManagerImpl;
import com.hotent.bpm.persistence.model.DefaultBpmCheckOpinion;
import com.hotent.bpm.persistence.model.DefaultBpmDefinition;
import com.hotent.bpm.persistence.model.DefaultBpmProcessDefExt;
import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;
import com.hotent.bpm.persistence.model.DefaultBpmTask;
import com.hotent.bpm.persistence.model.DefaultBpmTaskTurn;
import com.hotent.bpm.util.BpmUtil;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IGroup;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserGroupService;
import com.hotent.uc.api.service.IUserService;

import io.seata.spring.annotation.GlobalTransactional;

/**
 * 
 * <pre>
 * 描述：流程实例服务类
 * 构建组：x5-bpmx-core
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2013-12-23-上午7:39:10
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@Service
public class DefaultProcessInstanceService implements BpmInstService
{
	@Resource
	BpmProcessInstanceManager bpmProcessInstanceManager;
	@Resource
	BpmTaskTurnManagerImpl bpmTaskTurnManager;
	@Resource
	BpmTaskManager bpmTaskManager;
	@Resource
	BpmDefinitionManager bpmDefinitionManager;
	@Resource
	BpmDefinitionService  bpmDefinitionService;
	@Resource
	NatProInstanceService natProInstanceService;
	@Resource
	BpmDefinitionAccessor bpmDefinitionAccessor;
	@Resource
	BpmPluginSessionFactory bpmPluginSessionFactory;
	@Resource
	IUserService userServiceImpl;
	@Resource
	IUserGroupService defaultUserGroupService;
	@Resource
	BpmCheckOpinionManager bpmCheckOpinionManager;
	@Resource
	BpmExeStackManager bpmExeStackManager;

	@Override
	@GlobalTransactional
	public BpmProcessInstance startProcessInst(ProcessInstCmd processInstCmd) throws Exception{

		// 主键从cmd中获取
		DefaultBpmProcessInstance instance = getProcessInst(processInstCmd, false);

		BaseActionCmd baseActionCmd = (BaseActionCmd) processInstCmd;
		// 是否从草稿启动。
		boolean isAdd = StringUtil.isEmpty(baseActionCmd.getInstId());
		// 设置流程实例。
		baseActionCmd.setInstId(instance.getId());

		baseActionCmd.addTransitVars(BpmConstants.PROCESS_INST, instance);
		baseActionCmd.addVariable("subject_", instance.getSubject());

		ContextThreadUtil.setActionCmd(baseActionCmd);

		// 在流程发起之前发布事件。
		BpmStartModel startModel = new BpmStartModel(instance, baseActionCmd, AopType.PREVIOUS);
		BpmStartEvent startEvent = new BpmStartEvent(startModel);
		//AppUtil.getBean(BpmStartEventListener.class);
		AppUtil.publishEvent(startEvent);

		String destination = baseActionCmd.getDestination();

		boolean isSkipFirstNode = BpmUtil.getSkipFirstNode(instance.getProcDefId());

		String bpmnInstId = "";

		boolean isDestEmpty = StringUtil.isEmpty(destination);

		if(isSkipFirstNode && !isDestEmpty ){
			// 跳过第一个节点（作为发起节点）,选择路径
			Map<String, Object> variables = baseActionCmd.getVariables();
			variables.put(BpmConstants.START_DESTINATION, true);
			bpmnInstId = natProInstanceService.startProcessInstance(instance.getBpmnDefId(), instance.getBizKey(), variables, destination);
		}else{
			if (isDestEmpty || isSkipFirstNode){
				// 启动流程实例
				bpmnInstId = natProInstanceService.startProcessInstance(instance.getBpmnDefId(), instance.getBizKey(), baseActionCmd.getVariables());
			} 
			else if (!isSkipFirstNode && !isDestEmpty){
				bpmnInstId = natProInstanceService.startProcessInstance(instance.getBpmnDefId(), instance.getBizKey(), baseActionCmd.getVariables(), destination);
			}
		}

		instance.setBpmnInstId(bpmnInstId);

		// 在流程发起之后发布事件。
		BpmStartModel startModelAfter = new BpmStartModel(instance, baseActionCmd, AopType.POST);
		BpmStartEvent startEventAfter = new BpmStartEvent(startModelAfter);
		//AppUtil.getBean(BpmStartEventListener.class);
		AppUtil.publishEvent(startEventAfter);
		
		if (isAdd){
			bpmProcessInstanceManager.create(instance);
		} else{
			bpmProcessInstanceManager.update(instance);
		}
		
		
		updSubject(instance, processInstCmd);
		
		// 如果是外部表单， 则设置bizKey
		if(ActionCmd.DATA_MODE_PK.equals(ContextThreadUtil.getActionCmd().getDataMode())){
			instance.setBizKey(ContextThreadUtil.getActionCmd().getBusinessKey());
			instance.setSysCode(ContextThreadUtil.getActionCmd().getSysCode());
			bpmProcessInstanceManager.update(instance);
		}
		
		// 处理任务跳过.  这行代码要放到return 之前  ， 如果是去不跳过，到结束节点会发布流程结束事件更新instance， 如果updSubject放在后面又会改变流程实例状态为启动时的流程实例状态
		handlerSkipTask(instance);

		return instance;
	}

	/**
	 * 启动或保存草稿后 保存标题
	 * 
	 * @param instance
	 * @param processInstCmd
	 * @throws Exception 
	 */
	private void updSubject(DefaultBpmProcessInstance instance, ProcessInstCmd processInstCmd) throws Exception{
		
		processInstCmd.setVariables(getActVars(instance.getProcDefId(),""));
		// 修改任务标题
		List<DefaultBpmTask> listTasks = bpmTaskManager.getByInstId(instance.getId());
		for (DefaultBpmTask defaultBpmTask : listTasks){
			defaultBpmTask.setSubject(instance.getSubject());
			bpmTaskManager.update(defaultBpmTask);

			// 修改代理表中的标题
			DefaultBpmTaskTurn bpmTaskTurn = (DefaultBpmTaskTurn) bpmTaskTurnManager.getByTaskId(defaultBpmTask.getTaskId());
			if (bpmTaskTurn != null){
				bpmTaskTurn.setTaskSubject(instance.getSubject());
				bpmTaskTurnManager.update(bpmTaskTurn);
			}
		}
	}

	/**
	 * 处理任务跳过。
	 * 
	 * <pre>
	 * 有三种情况跳过节点。
	 * 1.需要跳过第一个任务节点。
	 * 2.系统配置了相邻节点执行人相同跳过。
	 * 3.系统配置了允许任务执行人为空，并且执行人为空时跳过。
	 * </pre>
	 * 
	 * @param instance
	 *            void
	 * @throws Exception 
	 */
	private void handlerSkipTask(DefaultBpmProcessInstance instance) throws Exception{
		String instId = instance.getId();
		Set<BpmTask> set = ContextThreadUtil.getByInstId(instId);
		// 删除相关的任务。
		ContextThreadUtil.clearTaskByInstId(instId);

		if (BeanUtils.isEmpty(set)) return;
		
		for (Iterator<BpmTask> it = set.iterator(); it.hasNext();){
			BpmTask task = it.next();
			BpmUtil.setTaskSkip(task);
			if (task.getSkipResult().isSkipTask()){
				BpmUtil.finishTask(task);
			}
		}
	}

	/**
	 * 设置实例数据。
	 * 
	 * @param bpmDefinition
	 * @param processDef
	 * @param instance
	 * @param processInstCmd
	 *            void
	 * @throws Exception 
	 */
	private DefaultBpmProcessInstance getProcessInst(ProcessInstCmd cmd, boolean isDraft) throws Exception
	{
		DefaultProcessInstCmd processInstCmd = (DefaultProcessInstCmd) cmd;
		String instId = processInstCmd.getInstId();

		DefaultBpmProcessInstance instance = null;

		// 从草稿启动流程。
		if (StringUtil.isNotEmpty(instId)){
			instance = bpmProcessInstanceManager.get(instId);

		}

		// 流程实例为空的情况直接启动流程。
		if (instance == null){
			DefaultBpmDefinition bpmDefinition = getDefByCmd(processInstCmd);
			instance = new DefaultBpmProcessInstance();
			
			// 设置 实例标题
			BpmProcessDef<BpmProcessDefExt> processDef = bpmDefinitionAccessor.getBpmProcessDef(bpmDefinition.getDefId());
			String subject = bpmProcessInstanceManager.getSubject(processDef, processInstCmd, instance);
			instance.setSubject(subject);
			// 如果没有提供id
			if (StringUtil.isEmpty(instId)){
				instance.setId(UniqueIdUtil.getSuid());
			}
			// 如果外部提供了流程Id,且不是草稿
			else{
				processInstCmd.setInstId(null);
				instance.setId(instId);
			}

			instance.setProcDefId(bpmDefinition.getDefId());
			instance.setTypeId(bpmDefinition.getTypeId());
			instance.setProcDefKey(bpmDefinition.getDefKey());
			instance.setBpmnDefId(bpmDefinition.getBpmnDefId());
			instance.setProcDefName(bpmDefinition.getName());
			
			instance.setSupportMobile(cmd.getSupportMobile());
			//数据模式为主键
			if(ActionCmd.DATA_MODE_PK.equals( cmd.getDataMode())){
				instance.setBizKey(processInstCmd.getBusinessKey());
				instance.setSysCode(processInstCmd.getSysCode());
			}
			
			instance.setParentInstId("0");

			if (bpmDefinition.getTestStatus().equals(BpmDefinition.TEST_STATUS.RUN)){
				instance.setIsFormmal(BpmProcessInstance.FORMAL_YES);
			}
			else if (bpmDefinition.getTestStatus().equals(BpmDefinition.TEST_STATUS.TEST)){
				instance.setIsFormmal(BpmProcessInstance.FORMAL_NO);
			}
		}

		IUser startUser = ContextUtil.getCurrentUser();
		if (BeanUtils.isNotEmpty(cmd.getTransitVars(BpmConstants.START_USER))) {
			startUser = (IUser) cmd.getTransitVars(BpmConstants.START_USER);
		}
		// 设置创建用户ID
		instance.setCreateBy(startUser.getUserId());
		instance.setCreator(startUser.getFullname());
		instance.setCreateTime(LocalDateTime.now());
		instance.setStatus(isDraft ? ProcessInstanceStatus.STATUS_DRAFT.getKey() : ProcessInstanceStatus.STATUS_RUNNING.getKey());
		instance.setUrgentState(cmd.getUrgentState());
		processInstCmd.setProcDefId(instance.getProcDefId());
		processInstCmd.setFlowKey(instance.getProcDefKey());
		// 设置流程变量。
		// 是否主键方式提交。
		if(ActionCmd.DATA_MODE_PK.equals( cmd.getDataMode())){
			processInstCmd.addVariable(BpmConstants.BUSINESS_KEY, instance.getBizKey());
		}
		
		//如果前端传入了流程发起人的组织id，则以前端传入的为准
		if (BeanUtils.isNotEmpty(processInstCmd.getTransitVars(BpmConstants.START_ORG_ID))) {
			instance.setCreateOrgId((String)processInstCmd.getTransitVars(BpmConstants.START_ORG_ID));
		}
		processInstCmd.addVariable(BpmConstants.SUBJECT, instance.getSubject());
		processInstCmd.addVariable(BpmConstants.PROCESS_INST_ID, instance.getId());
		processInstCmd.addVariable(BpmConstants.PROCESS_DEF_ID, instance.getProcDefId());
		processInstCmd.addVariable(BpmConstants.BPM_FLOW_KEY, instance.getProcDefKey());

		return instance;

	}

	/**
	 * 更加CMD对象获取流程定义。
	 * 
	 * @param processInstCmd
	 * @return DefaultBpmDefinition
	 */
	private DefaultBpmDefinition getDefByCmd(ProcessInstCmd processInstCmd)
	{
		DefaultBpmDefinition bpmDefinition = null;

		if (StringUtils.isEmpty(processInstCmd.getProcDefId()) && 
				StringUtils.isEmpty(processInstCmd.getFlowKey()) && 
				StringUtils.isEmpty(processInstCmd.getBpmnDefId())){
			throw new StartFlowException("没有传入流程定义ID,请传入ProcDefId,FlowKey,BpmnDefId中的任何一个");
		}

		if (StringUtils.isNotEmpty(processInstCmd.getProcDefId())){
			bpmDefinition = bpmDefinitionManager.getById(processInstCmd.getProcDefId());
		} 
		else if (StringUtils.isNotEmpty(processInstCmd.getFlowKey())){
			bpmDefinition = bpmDefinitionManager.getMainByDefKey(processInstCmd.getFlowKey(), false);
		} 
		else{
			String defId = bpmDefinitionManager.getDefIdByBpmnDefId(processInstCmd.getBpmnDefId());
			bpmDefinition = bpmDefinitionManager.getById(defId);
		}
		return bpmDefinition;
	}

	@Override
	public Map saveDraft(ProcessInstCmd processInstCmd) throws Exception{
		//设置cmd到上下文线程变量中。
		ContextThreadUtil.setActionCmd(processInstCmd);

		DefaultBpmProcessInstance instance = getProcessInst(processInstCmd, true);

		BpmStartModel startModel = new BpmStartModel(instance, processInstCmd, AopType.PREVIOUS);
		BpmStartEvent startEvent = new BpmStartEvent(startModel);
		//AppUtil.getBean(BpmStartEventListener.class);
		AppUtil.publishEvent(startEvent);
		String msg="保存表单信息成功";
        //判断是否从待办审批页面点的保存
		if(!processInstCmd.getApproval()){
            if (StringUtil.isEmpty(processInstCmd.getInstId())){
                bpmProcessInstanceManager.create(instance);
				msg="保存草稿成功";
            }
            else{
                bpmProcessInstanceManager.update(instance);
            }
        }
		updSubject(instance, processInstCmd);
		Map map=new HashMap();
		map.put("instance",instance);
		map.put("msg",msg);
		return map;
	}

	@Override
	public BpmProcessInstance startDraftProcessInstance(BpmProcessInstance processInstance) throws Exception
	{
		String instId = processInstance.getId();
		if (!ProcessInstanceStatus.STATUS_DRAFT.getKey().equals(processInstance.getStatus()))
		{
			throw new StartFlowException("启动流程失败,此实例已经启动");
		}
		DefaultProcessInstCmd processInstCmd = new DefaultProcessInstCmd();
		processInstCmd.setInstId(instId);
		return startProcessInst(processInstCmd);
	}

	@Override
	public BpmProcessInstance startDraftProcessInstance(BpmProcessInstance processInstance, Map<String, Object> variables) throws Exception
	{
		String instId = processInstance.getId();
		if (!ProcessInstanceStatus.STATUS_DRAFT.getKey().equals(processInstance.getStatus()))
		{
			throw new StartFlowException("启动流程失败,此实例已经启动");
		}
		DefaultProcessInstCmd processInstCmd = new DefaultProcessInstCmd();
		processInstCmd.setInstId(instId);
		processInstCmd.setVariables(variables);
		return startProcessInst(processInstCmd);
	}

	@SuppressWarnings(
	{ "unchecked", "rawtypes" })
	@Override
	public List<BpmProcessInstance> getProcessInstancesByUserId(String userId)
	{
		return (List) bpmProcessInstanceManager.getByUserId(userId);
	}

	@SuppressWarnings(
	{ "unchecked", "rawtypes" })
	@Override
	public PageList<BpmProcessInstance> getProcessInstancesByUserId(String userId, PageBean page)
	{
		return (PageList) bpmProcessInstanceManager.getByUserId(userId, page);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<BpmProcessInstance> getProcessInstancesByUserId(String userId, QueryFilter queryFilter)
	{
		return (List) bpmProcessInstanceManager.getByUserId(userId, queryFilter);
	}

	@SuppressWarnings(
	{ "unchecked", "rawtypes" })
	@Override
	public List<BpmProcessInstance> getAttendProcessInstancesByUserId(String userId)
	{
		return (List) bpmProcessInstanceManager.getByAttendUserId(userId);
	}

	@SuppressWarnings(
	{ "rawtypes", "unchecked" })
	@Override
	public PageList<BpmProcessInstance> getAttendProcessInstancesByUserId(String userId, PageBean page)
	{
		return (PageList) bpmProcessInstanceManager.getByAttendUserId(userId, page);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<BpmProcessInstance> getAttendProcessInstancesByUserId(String userId, QueryFilter queryFilter)
	{
		return (List) bpmProcessInstanceManager.getByAttendUserId(userId, queryFilter);

	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<BpmProcessInstance> getDraftsByUserId(String userId, QueryFilter queryFilter)
	{
		// bpmProcessInstanceManager.getd
		return (List) bpmProcessInstanceManager.getDraftsByUserId(userId, queryFilter);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<BpmProcessInstance> getAll(QueryFilter queryFilter)
	{
		return (List) bpmProcessInstanceManager.query(queryFilter);
	}

	@Override
	public boolean removeProcessInstance(String processInstId)
	{
		bpmProcessInstanceManager.remove(processInstId);
		return true;
	}

	@Override
	public boolean suspendProcessInstance(String processInstId)
	{
		try
		{
//			DefaultBpmProcessInstance instance = bpmProcessInstanceManager.get(processInstId);
			bpmProcessInstanceManager.updForbiddenByInstId(processInstId, BpmProcessInstance.FORBIDDEN_YES);
//			natProInstanceService.suspendProcessInstanceById(instance.getBpmnInstId());
		} catch (Exception ex)
		{
			return false;
		}
		return true;
	}

	@Override
	public boolean recoverProcessInstance(String processInstId)
	{
		try
		{
//			DefaultBpmProcessInstance instance = bpmProcessInstanceManager.get(processInstId);
			bpmProcessInstanceManager.updForbiddenByInstId(processInstId, BpmProcessInstance.FORBIDDEN_NO);
//			natProInstanceService.activateProcessInstanceById(instance.getBpmnInstId());
		} catch (Exception ex)
		{
			return false;
		}
		return true;
	}

	@Override
	public boolean endProcessInstance(String processInstId)
	{
		DefaultBpmProcessInstance instance = bpmProcessInstanceManager.get(processInstId);
		bpmProcessInstanceManager.updateStatusByInstanceId(processInstId, ProcessInstanceStatus.STATUS_END.getKey());
		natProInstanceService.endProcessInstance(instance.getBpmnInstId());
		return true;
	}

	@Override
	public BpmProcessInstance getProcessInstance(String processInstId)
	{
		return bpmProcessInstanceManager.get(processInstId);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<BpmProcessInstance> getByTaskUserId(String userId)
	{
		IUser user = userServiceImpl.getUserById(userId);
		List<IGroup> groupList = defaultUserGroupService.getGroupsByUserIdOrAccount(user.getAccount());
		return (List)bpmProcessInstanceManager.getByUserIdGroupList(userId, groupList);
	}
	
	@Override
	public PageList<BpmProcessInstance> getByTaskUserId(String userId, PageBean page)
	{
		IUser user = userServiceImpl.getUserById(userId);
		List<IGroup> groupList = defaultUserGroupService.getGroupsByUserIdOrAccount(user.getAccount());
		IPage<DefaultBpmProcessInstance> list= bpmProcessInstanceManager.getByUserIdGroupList(userId, groupList, page);
		List<BpmProcessInstance> arys = new ArrayList<>();
		for(DefaultBpmProcessInstance instance : list.getRecords()) {
			arys.add(instance);
		}
		return new PageList<BpmProcessInstance>(arys);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<BpmProcessInstance> getByTaskUserId(String userId, QueryFilter queryFilter)
	{
		List<IGroup> groupList = defaultUserGroupService.getGroupsByUserIdOrAccount(userId);
		return (List) bpmProcessInstanceManager.getByUserIdGroupList(userId, groupList, queryFilter);
	}

	@Override
	public BpmProcessInstance getProcessInstanceByBpmnInstId(String bpmnInstId)
	{
		return bpmProcessInstanceManager.getByBpmnInstId(bpmnInstId);
	}

	@Override
	public void removeTestInstByDefKey(String defKey)
	{
		bpmProcessInstanceManager.removeTestInstByDefKey(defKey,false);

	}

	// @Override
	public void revokeInstance(String instanceId, String informType, String cause) throws Exception
	{
		bpmProcessInstanceManager.revokeInstance(instanceId, informType, cause);

	}

	@Override
	public boolean isSuspendByInstId(String processInstId)
	{
		BpmProcessInstance instance = bpmProcessInstanceManager.get(processInstId);
		int forbindden = instance.getIsForbidden();
		if (BpmProcessInstance.FORBIDDEN_YES == forbindden)
		{
			return true;
		}
		BpmDefinition bpmDefinition = bpmDefinitionManager.getById(instance.getProcDefId());
		String status = bpmDefinition.getStatus();
		if (/*BpmDefinition.STATUS.FORBIDDEN.equals(status) || */BpmDefinition.STATUS.FORBIDDEN_INSTANCE.equals(status))
		{
			return true;
		}
		return false;
	}

	@Override
	public List<UserTaskNodeDef> getApprovalNodes(String processInstId) throws Exception
	{
		List<DefaultBpmCheckOpinion> checkOpinions = bpmCheckOpinionManager.getByInstId(processInstId);
		DefaultBpmProcessInstance instance = bpmProcessInstanceManager.get(processInstId);
		String procDefId = instance.getProcDefId();
		List<BpmNodeDef> nodeDefs = bpmDefinitionAccessor.getNodesByType(procDefId, NodeType.USERTASK);

		List<UserTaskNodeDef> userTaskNodeDefs = new ArrayList<UserTaskNodeDef>();

		for (BpmNodeDef bpmNodeDef : nodeDefs)
		{
			for (DefaultBpmCheckOpinion opinion : checkOpinions)
			{
				String status = opinion.getStatus();
				if (OpinionStatus.AWAITING_CHECK.toString().equals(status) || OpinionStatus.REJECT.toString().equals(status) || OpinionStatus.BACK_TO_START.toString().equals(status))
				{
					continue;
				}
				if (bpmNodeDef.getNodeId().equals(opinion.getTaskKey()))
				{
					userTaskNodeDefs.add((UserTaskNodeDef) bpmNodeDef);
				}
			}
		}
		return userTaskNodeDefs;
	}
	
	// 通过环节ID获取环节
	private BPMNShap getByTaskKey(List<BPMNShap> shaps, String taskKey){
		BPMNShap shap = null;
		if(StringUtil.isEmpty(taskKey) || BeanUtils.isEmpty(shaps)) return shap;
		for (BPMNShap bpmnShap : shaps) {
			if(taskKey.equals(bpmnShap.getBpmnElement())){
				shap = bpmnShap;
				break;
			}
		}
		return shap;
	}
	
	// 通过线条ID获取线条对象
	private BPMNEdge getByEdgeId(List<BPMNEdge> edges, String edgeId){
		BPMNEdge edge = null;
		if(StringUtil.isEmpty(edgeId) || BeanUtils.isEmpty(edges)) return edge;
		for (BPMNEdge bpmnEdge : edges) {
			if(edgeId.equals(bpmnEdge.getId())){
				edge = bpmnEdge;
				break;
			}
		}
		return edge;
	}
	
	// 将审批意见转换为轨迹数组
	private List<BpmInstanceTrack> convertFromOpinions(List<DefaultBpmCheckOpinion> checkOpinions, List<BPMNShap> shaps){
		List<BpmInstanceTrack> tracks = new ArrayList<BpmInstanceTrack>();
		int size = 0;
		LocalDateTime preTime = null;
		//TODO 根据按时间排序的审批意见运算流程轨迹是不可靠的，如存在同步网关时
		for (DefaultBpmCheckOpinion defaultBpmCheckOpinion : checkOpinions) {
			String status = defaultBpmCheckOpinion.getStatus();
			String taskKey = defaultBpmCheckOpinion.getTaskKey();
			LocalDateTime createTime = defaultBpmCheckOpinion.getCreateTime();
			BPMNShap bpmnshap = getByTaskKey(shaps, taskKey);
			if(bpmnshap==null)continue;
			if(preTime==null || preTime.compareTo(createTime) != 0){
				size++;
			}
			
			BpmInstanceTrack track = new BpmInstanceTrack();
			track.setDuration(defaultBpmCheckOpinion.getDurMs());
			track.setStatus(status);
			track.setTaskKey(taskKey);
			track.setSn(size);
			
			if(OpinionStatus.START.getKey().equals(status) || OpinionStatus.END.getKey().equals(status)){
				preTime = null;
				track.setType(BpmInstanceTrack.TYPE_EVENT);
				track.setPoint(new BpmTrackPoint(bpmnshap.getX(), bpmnshap.getY()));
				track.setRadius(bpmnshap.getWidth()/2);
			}
			else{
				preTime = createTime;
				if(OpinionStatus.SKIP.getKey().equals(status)){
					preTime = null;
				}
				track.setType(BpmInstanceTrack.TYPE_RECT);
				track.setPoint(new BpmTrackPoint(bpmnshap.getX(), bpmnshap.getY()));
				track.setSize(new BpmTrackSize(bpmnshap.getWidth(), bpmnshap.getHeight()));
			}
			tracks.add(track);
		}
		return tracks;
	}
	
	// 轨迹插值(用线条和网关将各环节连接起来)
	private void interpolation(List<BpmInstanceTrack> tracks, List<BPMNEdge> edges, List<BPMNShap> shaps){
		if(BeanUtils.isEmpty(tracks) || BeanUtils.isEmpty(edges)) return;
		int size = tracks.size();
		if(size < 2) return;
		Map<String, List<BpmInstanceTrack>> map = new HashMap<String, List<BpmInstanceTrack>>();
		int sn = 1;
		List<BpmInstanceTrack> twins = null;
		do{
			List<BpmInstanceTrack> preTracks = getBySn(tracks, sn);
			if(sn==1 && preTracks.size()!=1){
				throw new RuntimeException("轨迹的开始节点要求有且仅有一个");
			}
			for(BpmInstanceTrack preTrack : preTracks){
				twins = getTwins(tracks, sn, preTrack);
				if(twins==null) continue;
				List<BpmInstanceTrack> interpoles = getInterpoles(twins, edges, shaps);
				map.put(sn + "", interpoles);
			}
			sn++;
		}
		while(BeanUtils.isNotEmpty(twins));
		mergeTracks(tracks, map, sn);
	}
	
	// 融合轨迹
	private void mergeTracks(List<BpmInstanceTrack> tracks, Map<String, List<BpmInstanceTrack>> map, int total){
		List<BpmInstanceTrack> resultTracks = new ArrayList<BpmInstanceTrack>();
		int sn = 1;
		List<BpmInstanceTrack> orginTracks = null;
		Map<String, List<BpmInstanceTrack>> orginMap = new HashMap<String, List<BpmInstanceTrack>>();
		do{
			orginTracks = getBySn(tracks, sn);
			if(BeanUtils.isNotEmpty(orginTracks)){
				orginMap.put(sn+"", orginTracks);
			}
			sn++;
		}
		while(BeanUtils.isNotEmpty(orginTracks));
		
		int newSn = 1;
		for(int i = 1; i < total; i++){
			List<BpmInstanceTrack> orgins = orginMap.get(i+"");
			for (BpmInstanceTrack orgin : orgins) {
				orgin.setSn(newSn);
				resultTracks.add(orgin);
			}
			newSn++;
			List<BpmInstanceTrack> adds = map.get(i+"");
			if(adds==null)continue;
			int addCount = adds.size();
			if(addCount==0) continue;
			for (BpmInstanceTrack add : adds) {
				add.setSn(newSn++);
				resultTracks.add(add);
			}
		}
		tracks.clear();
		tracks.addAll(resultTracks);
	}
	
	// 获取插值线条和网关
	private List<BpmInstanceTrack> getInterpoles(List<BpmInstanceTrack> twins, List<BPMNEdge> edges, List<BPMNShap> shaps){
		List<BpmInstanceTrack> list = new ArrayList<BpmInstanceTrack>();
		
		BpmInstanceTrack preTrack = twins.get(0);
		BpmInstanceTrack afterTrack = twins.get(1);
		String preTaskKey = preTrack.getTaskKey();
		String afterTaskKey = afterTrack.getTaskKey();
		// 连续在一个环节完成任务(会签、流转)
		if(preTaskKey.equals(afterTaskKey)){
			
		}
		else{
			TrackNode finalNode = getPathNode(preTaskKey, afterTaskKey, edges);
			if(finalNode != null){
				List<BpmInstanceTrack> trackByTrackNode = getTrackByTrackNode(finalNode, edges, shaps);
				list.addAll(trackByTrackNode);
			}
			else{
				BpmInstanceTrack flyLineBetweenTwins = getFlyLineBetweenTwins(preTrack, afterTrack);
				list.add(flyLineBetweenTwins);
			}
		}
		return list;
	}
	
	// 获取线条或者网关轨迹
	private List<BpmInstanceTrack> getTrackByTrackNode(TrackNode trackNode , List<BPMNEdge> edges, List<BPMNShap> shaps){
		List<BpmInstanceTrack> tracks = new ArrayList<BpmInstanceTrack>();
		TrackNode preNode = trackNode;
		int size = trackNode.getDepth();
		int index = 0;
		do{
			index++;
			preNode = preNode.getParent();
			if(index>=size-1) continue;
			if(preNode.isEdge()){
				BPMNEdge edge = getByEdgeId(edges, preNode.getId());
				BpmInstanceTrack track = new BpmInstanceTrack();
				track.setType(BpmInstanceTrack.TYPE_LINE);
				List<Point2D.Double> points = edge.getPoints();
				List<BpmTrackPoint> lines = new ArrayList<BpmTrackPoint>();
				for (Point2D.Double double1 : points) {
					lines.add(new BpmTrackPoint(double1.getX(), double1.getY()));
				}
				track.setPoints(lines);
				tracks.add(0, track);
			}
			else{
				BPMNShap shap = getByTaskKey(shaps, preNode.getId());
				BpmInstanceTrack track = new BpmInstanceTrack();
				double width = shap.getWidth();
				double height = shap.getHeight();
				track.setPoint(new BpmTrackPoint(shap.getX(), shap.getY()));
				if(width==height){
					track.setType(BpmInstanceTrack.TYPE_DIAMOND);
					track.setLength(height);
				}
				else{
					track.setType(BpmInstanceTrack.TYPE_RECT);
					track.setSize(new BpmTrackSize(width, height));
				}
				tracks.add(0, track);
			}
		}
		while(preNode!=null);
		return tracks;
	}
	
	// 在两个环节之间直接连线
	private BpmInstanceTrack getFlyLineBetweenTwins(BpmInstanceTrack preTrack, BpmInstanceTrack afterTrack){
		Double startX = 0d;
		Double startY = 0d;
		Double endX = 0d;
		Double endY = 0d;
		switch(preTrack.getType()){
			case BpmInstanceTrack.TYPE_EVENT:
				startX = preTrack.getPoint().getX() + preTrack.getRadius() / 2;
				startY = preTrack.getPoint().getY();
				break;
			case BpmInstanceTrack.TYPE_RECT:
				startX = preTrack.getPoint().getX() + preTrack.getSize().getWidth() / 2;
				startY = preTrack.getPoint().getY();
				break;
			case BpmInstanceTrack.TYPE_DIAMOND:
				startX = preTrack.getPoint().getX() + preTrack.getLength() / 2;
				startY = preTrack.getPoint().getY();
				break;
		}
		switch(afterTrack.getType()){
			case BpmInstanceTrack.TYPE_EVENT:
				endX = afterTrack.getPoint().getX() + afterTrack.getRadius() / 2;
				endY = afterTrack.getPoint().getY();
				break;
			case BpmInstanceTrack.TYPE_RECT:
				endX = afterTrack.getPoint().getX() + afterTrack.getSize().getWidth() / 2;
				endY = afterTrack.getPoint().getY();
				break;
			case BpmInstanceTrack.TYPE_DIAMOND:
				endX = afterTrack.getPoint().getX() + afterTrack.getLength() / 2;
				endY = afterTrack.getPoint().getY();
				break;
		}
		BpmInstanceTrack track = new BpmInstanceTrack();
		track.setType(BpmInstanceTrack.TYPE_LINE);
		List<BpmTrackPoint> lines = new ArrayList<BpmTrackPoint>();
		lines.add(new BpmTrackPoint(startX, startY));
		lines.add(new BpmTrackPoint(startX, startY - 5));
		lines.add(new BpmTrackPoint(endX, startY - 5));
		lines.add(new BpmTrackPoint(endX, endY));
		track.setPoints(lines);
		return track;
	}
	
	// 获取到达目标的路径节点
	private TrackNode getPathNode(String preTaskKey, String afterTaskKey, List<BPMNEdge> edges){
		List<TrackNode> fromNodes = new ArrayList<TrackNode>();
		TrackNode root = new TrackNode(preTaskKey);
		fromNodes.add(root);
		return findPath(fromNodes, afterTaskKey, edges);
	}
	
	// 递归查找路径（最短路径）
	private TrackNode findPath(List<TrackNode> nodes, String afterTaskKey, List<BPMNEdge> edges){
		List<TrackNode> fromNodes = new ArrayList<TrackNode>();
		for (TrackNode node : nodes) {
			List<BPMNEdge> forks = getForks(node.getId(), edges);
			for (BPMNEdge edge : forks) {
				String targetRef = edge.getTargetRef();
				TrackNode edgeNode = new TrackNode(edge.getId(), node);
				TrackNode targetNode = new TrackNode(targetRef, edgeNode);
				targetNode.setType(TrackNode.TYPE_SHAP);
				if(targetRef.equals(afterTaskKey)){
					return targetNode;
				}
				else {
					fromNodes.add(targetNode);
				}
			}
		}
		if (fromNodes.size() > 0) {
			// 如果两个节点之前相隔太远，则认为是按正常流程图不可达，除非是任意跳转或驳回
			if (fromNodes.get(0).getDepth() > 10) {
				return null;
			}
			return findPath(fromNodes, afterTaskKey, edges);
		}
		return null;
	}
	
	// 获取分支
	private List<BPMNEdge> getForks(String preTaskKey, List<BPMNEdge> edges){
		List<BPMNEdge> forks = new ArrayList<BPMNEdge>();
		for(BPMNEdge bpmnEdge : edges) {
			if(preTaskKey.equals(bpmnEdge.getSourceRef())){
				forks.add(bpmnEdge);
			}
			// 直接驳回的情况也算
			else if (preTaskKey.equals(bpmnEdge.getTargetRef())) {
				forks.add(bpmnEdge);
			}
		}
		return forks;
	}
	
	// 通过sn获取轨迹
	private List<BpmInstanceTrack> getBySn(List<BpmInstanceTrack> tracks, int sn){
		List<BpmInstanceTrack> list = new ArrayList<BpmInstanceTrack>();
		for (BpmInstanceTrack track : tracks) {
			if(track.getSn().intValue()==sn){
				list.add(track);
			}
		}
		return list;
	}
	
	// 获取结对的轨迹
	private List<BpmInstanceTrack> getTwins(List<BpmInstanceTrack> tracks, int sn, BpmInstanceTrack preTrack){
		List<BpmInstanceTrack> afterTracks = getBySn(tracks, sn + 1);
		if(BeanUtils.isEmpty(afterTracks)) return null;
		List<BpmInstanceTrack> twins = new ArrayList<BpmInstanceTrack>();
		//TODO 多次 结对
		BpmInstanceTrack afterTrack = afterTracks.get(0);
		twins.add(preTrack);
		twins.add(afterTrack);
		return twins;
	}
	
	@Override
	public List<BpmInstanceTrack> getTracksByInstId(String processInstId) {
		DefaultBpmProcessInstance instance = bpmProcessInstanceManager.get(processInstId);
		String defId = instance.getProcDefId();
		BpmDefinition bpmDefinition = bpmDefinitionService.getBpmDefinitionByDefId(defId);
		String bpmnXml = bpmDefinition.getBpmnXml();
		
		List<DefaultBpmCheckOpinion> checkOpinions = bpmCheckOpinionManager.getByInstId(processInstId);
		List<BPMNShap> shaps = ProcessDiagramGenerator.extractBPMNShap(bpmnXml);
		List<BPMNEdge> edges = ProcessDiagramGenerator.extractBPMNEdge(bpmnXml);
		
		List<BpmInstanceTrack> tracks = convertFromOpinions(checkOpinions, shaps);
		interpolation(tracks, edges, shaps);
		return tracks;
	}
	
	private Map<String,Object> getActVars(String defId,String nodeId) throws Exception{
		BpmProcessDef<BpmProcessDefExt> bpmProcessDefExt = bpmDefinitionAccessor.getBpmProcessDef(defId);
		DefaultBpmProcessDefExt defExt = (DefaultBpmProcessDefExt) bpmProcessDefExt.getProcessDefExt();
		List<BpmVariableDef> bpmVariableList=defExt.getVariableList() ;
		if(bpmVariableList==null) bpmVariableList=new ArrayList<BpmVariableDef>();
		
		Map<String,Object> rtnMap=new HashMap<String, Object>();
		
		if(StringUtil.isNotEmpty(nodeId)){
			bpmVariableList.addAll(defExt.getVariableList(nodeId)) ;
		}
		for (BpmVariableDef var : bpmVariableList) {
			String varkey=var.getVarKey();
			Object rtnVal=DefaultBpmVariableDef.getValue(var.getDataType(), String.valueOf(var.getDefaultVal()));
			rtnMap.put(varkey, rtnVal);
		}
		return rtnMap;
	}
}
