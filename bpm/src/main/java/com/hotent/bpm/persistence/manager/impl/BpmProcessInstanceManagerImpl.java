package com.hotent.bpm.persistence.manager.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.exception.WorkFlowException;
import com.hotent.base.groovy.GroovyScriptEngine;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.model.HtJsonNodeFactory;
import com.hotent.base.model.HtObjectNode;
import com.hotent.base.query.Direction;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.time.DateUtil;
import com.hotent.base.util.time.TimeUtil;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.cmd.ProcessInstCmd;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.constant.NodeStatus;
import com.hotent.bpm.api.constant.NodeType;
import com.hotent.bpm.api.constant.OpinionStatus;
import com.hotent.bpm.api.constant.ProcessInstanceStatus;
import com.hotent.bpm.api.constant.ScriptType;
import com.hotent.bpm.api.constant.TemplateConstants;
import com.hotent.bpm.api.context.BpmContextUtil;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.def.BpmDefinition;
import com.hotent.bpm.api.model.process.def.BpmProcessDef;
import com.hotent.bpm.api.model.process.def.BpmProcessDefExt;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.service.BoDataService;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.api.service.BpmInstService;
import com.hotent.bpm.api.service.BpmTaskActionService;
import com.hotent.bpm.engine.inst.DefaultProcessInstCmd;
import com.hotent.bpm.engine.task.cmd.DefaultTaskFinishCmd;
import com.hotent.bpm.exception.BusinessException;
import com.hotent.bpm.model.identity.DefaultBpmIdentity;
import com.hotent.bpm.natapi.inst.NatProInstanceService;
import com.hotent.bpm.persistence.constants.ProcDefTestStatus;
import com.hotent.bpm.persistence.dao.ActTaskDao;
import com.hotent.bpm.persistence.dao.BpmCheckOpinionDao;
import com.hotent.bpm.persistence.dao.BpmExeStackDao;
import com.hotent.bpm.persistence.dao.BpmExeStackRelationDao;
import com.hotent.bpm.persistence.dao.BpmProcessInstanceDao;
import com.hotent.bpm.persistence.manager.ActExecutionManager;
import com.hotent.bpm.persistence.manager.ActTaskManager;
import com.hotent.bpm.persistence.manager.BpmCheckOpinionManager;
import com.hotent.bpm.persistence.manager.BpmDefAuthorizeManager;
import com.hotent.bpm.persistence.manager.BpmDefinitionManager;
import com.hotent.bpm.persistence.manager.BpmExeStackExecutorManager;
import com.hotent.bpm.persistence.manager.BpmExeStackManager;
import com.hotent.bpm.persistence.manager.BpmProStatusManager;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.persistence.manager.BpmSignDataManager;
import com.hotent.bpm.persistence.manager.BpmTaskCandidateManager;
import com.hotent.bpm.persistence.manager.BpmTaskManager;
import com.hotent.bpm.persistence.manager.BpmTaskNoticeDoneManager;
import com.hotent.bpm.persistence.manager.BpmTaskNoticeManager;
import com.hotent.bpm.persistence.manager.BpmTaskReadManager;
import com.hotent.bpm.persistence.manager.BpmTaskTurnManager;
import com.hotent.bpm.persistence.manager.CopyToManager;
import com.hotent.bpm.persistence.model.ActExecution;
import com.hotent.bpm.persistence.model.ActTask;
import com.hotent.bpm.persistence.model.AuthorizeRight;
import com.hotent.bpm.persistence.model.BpmDefAuthorizeType.BPMDEFAUTHORIZE_RIGHT_TYPE;
import com.hotent.bpm.persistence.model.BpmExeStack;
import com.hotent.bpm.persistence.model.BpmExeStackExecutor;
import com.hotent.bpm.persistence.model.BpmExeStackRelation;
import com.hotent.bpm.persistence.model.BpmTaskNotice;
import com.hotent.bpm.persistence.model.DefaultBpmCheckOpinion;
import com.hotent.bpm.persistence.model.DefaultBpmDefinition;
import com.hotent.bpm.persistence.model.DefaultBpmProStatus;
import com.hotent.bpm.persistence.model.DefaultBpmProcessDefExt;
import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;
import com.hotent.bpm.persistence.model.DefaultBpmTask;
import com.hotent.bpm.persistence.model.DefaultBpmTaskCandidate;
import com.hotent.bpm.persistence.model.ResultMessage;
import com.hotent.bpm.persistence.util.BpmStackRelationUtil;
import com.hotent.bpm.persistence.util.BpmUtil;
import com.hotent.bpm.util.BpmCheckOpinionUtil;
import com.hotent.bpm.util.MessageUtil;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IGroup;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserService;


@Service("bpmProcessInstanceManager")
public class BpmProcessInstanceManagerImpl extends BaseManagerImpl<BpmProcessInstanceDao, DefaultBpmProcessInstance> implements BpmProcessInstanceManager
{

	protected static final Logger LOGGER = LoggerFactory.getLogger(BpmProcessInstanceManagerImpl.class);
	@Resource
	BpmExeStackDao bpmExeStackDao;
	@Resource
	NatProInstanceService natProInstanceService;
	@Resource
	BpmCheckOpinionManager bpmCheckOpinionManager;
	@Resource
	BpmTaskManager bpmTaskManager;
	@Resource
	BpmTaskCandidateManager bpmTaskCandidateManager;
	@Resource
	BpmProStatusManager bpmProStatusManager;
	@Resource
	CopyToManager copyToManager;
	@Resource
	BpmTaskTurnManager bpmTaskTurnManager;
	@Resource
	BpmSignDataManager bpmSignDataManager;
	@Resource
	BpmTaskReadManager bpmTaskReadManager;
	@Resource
	ActExecutionManager actExecutionManager;
	@Resource
	ActTaskDao actTaskDao;
	@Resource
	BpmDefAuthorizeManager bpmDefAuthorizeManager;

	@Resource
	BpmDefinitionAccessor bpmDefinitionAccessor;
	@Resource
	ActTaskManager actTaskManager;
	@Resource
	IUserService userServiceImpl;
	@Resource
	BpmExeStackManager bpmExeStackManager;
	/*@Resource
	BpmFormService bpmFormService;*/
	@Resource
	GroovyScriptEngine groovyScriptEngine;
	@Resource
	BpmTaskActionService bpmTaskActionService;
	@Resource
	IUserService userService;
    @Resource
    BpmDefinitionManager bpmDefinitionManager;
    @Resource
    BpmTaskNoticeManager bpmTaskNoticeManager;
    @Resource
    BpmTaskNoticeDoneManager bpmTaskNoticeDoneManager;
    @Resource
    BpmProcessInstanceManager bpmProcessInstanceManager;
    @Resource
    BpmInstService bpmInstService;
    @Resource
    BoDataService boDataService;


	@Override
	public String getSubject(BpmProcessDef<BpmProcessDefExt> bpmDefinition, ProcessInstCmd processInstCmd, DefaultBpmProcessInstance defaultBpmProcessInstance)
	{
		
		// 若设置了标题，则直接返回该标题，否则按后台的标题规则返回
		if (StringUtils.isNotEmpty(processInstCmd.getSubject())){
			return processInstCmd.getSubject();
		}
		DefaultBpmProcessDefExt defExt = (DefaultBpmProcessDefExt) bpmDefinition.getProcessDefExt();
		String rule = defExt.getExtProperties().getSubjectRule();
        rule = rule.replace("<p>","" );
        rule = rule.replace("</p>","" );
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", bpmDefinition.getName());
		
		map.put("startDate", DateUtil.getCurrentTime("yyyy-MM-dd"));
		map.put("startTime", DateUtil.getCurrentTime());
		map.put("businessKey", processInstCmd.getBusinessKey());
		//流程变量
        map.put("flowKey_", processInstCmd.getFlowKey());
        map.put("instanceId_", processInstCmd.getInstId());
        String startUserName = ContextUtil.getCurrentUser().getFullname();
        if (BeanUtils.isNotEmpty(processInstCmd.getTransitVars(BpmConstants.START_USER))) {
        	startUserName = ((IUser)processInstCmd.getTransitVars(BpmConstants.START_USER)).getFullname();
		}
        map.put("startUser", startUserName);
        map.put("startorName", startUserName);
		map.putAll(((DefaultProcessInstCmd) processInstCmd).getVariables());

		
		Map<String,ObjectNode> boMap=BpmContextUtil.getBoFromContext();
		if(BeanUtils.isNotEmpty(boMap)){
			//Iterator<Entry<String, ObjectNode>> iterator = boMap.entrySet().iterator();
			for (Iterator<Entry<String, ObjectNode>> iterator = boMap.entrySet().iterator(); iterator.hasNext();) {
				Entry<String, ObjectNode> ent = iterator.next();
				ObjectNode boData = ent.getValue();
				ObjectNode bodef=(ObjectNode) boData.get("boDef");
				if(BeanUtils.isEmpty(bodef)) continue;
				String boName=bodef.get("alias").asText();
				Map<String, Object> dataMap = new  HashMap<>();
				try {
					dataMap = JsonUtil.toMap(JsonUtil.toJson(boData.get("data")));
				} catch (IOException e) {
					e.printStackTrace();
				}
				for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
					map.put(boName +"." + entry.getKey(),  entry.getValue());
				}
			}
		}else if(BeanUtils.isNotEmpty(processInstCmd.getBusData())){
			try {
				JsonNode dataNode = JsonUtil.toJsonNode(processInstCmd.getBusData());
				Iterator<Entry<String, JsonNode>> it = dataNode.fields();
				while (it.hasNext())
	            {
	                Entry<String, JsonNode> entry = it.next();
	                Iterator<Entry<String, JsonNode>> subIt = entry.getValue().fields();
	                while (subIt.hasNext())
		            {
		                Entry<String, JsonNode> subEntry = subIt.next();
		                if(!subEntry.getKey().contains("sub_") && BeanUtils.isNotEmpty(subEntry.getValue())){
		                	map.put(entry.getKey() +"." + subEntry.getKey(),  subEntry.getValue().asText());
		                }
		            }
	            }
			} catch (Exception e) {
				System.out.println("解析流程数据失败："+e.getMessage());
			}
		}
		
		rule = BpmUtil.getTitleByRule(rule, map);
		// 如果不是正式, 显示测试标题带测试状态 
		 DefaultBpmDefinition bpmDef = this.bpmDefinitionManager.getById(bpmDefinition.getProcessDefinitionId());
		if(ProcDefTestStatus.TEST.getKey().equalsIgnoreCase(bpmDef.getStatus())){
			rule = ProcDefTestStatus.TEST.getName() + " -- " + rule;
		}
		return rule;

	}

	
	@Override
    @Transactional
	public void physicsRemove(String processInstId){

		DefaultBpmProcessInstance inst = (DefaultBpmProcessInstance) get(processInstId);
		// 草稿
		if (ProcessInstanceStatus.STATUS_DRAFT.getKey().equals(inst.getStatus())){
			super.remove(processInstId);
		} 
		else{
			BpmProcessInstance topInstance = getTopBpmProcessInstance(processInstId);

			String topInstId = topInstance.getId();

			List<DefaultBpmProcessInstance> instList = getByParentId(topInstId, true);

			String topBpmnInstId = topInstance.getBpmnInstId();

			List<String> instIdList = getInstList(instList);

			List<String> bpmnInstList = baseMapper.getBpmnByInstList(instIdList);
			// 删除
			removeCascade(instIdList);
			// 删除流程数据。
			actExecutionManager.delByInstList(bpmnInstList);
			// 删除关联的实例。
			actExecutionManager.remove(topBpmnInstId);
		}
	}


    @Transactional
    public void remove(String processInstId,Boolean isBpm){
        DefaultBpmProcessInstance inst = (DefaultBpmProcessInstance) get(processInstId);
        // 草稿
        if (ProcessInstanceStatus.STATUS_DRAFT.getKey().equals(inst.getStatus())){
            super.remove(processInstId);
        } //流程实例非草稿实现逻辑删除
        else{
            if(!isBpm){
                baseMapper.isDeleInst(processInstId,1);//根据流程实例ID物理删除实例记录数据
                baseMapper.isDeleOpinion(processInstId,1);//根据流程实例ID物理删除审批记录数据
                baseMapper.isDeleNotice(processInstId,1);//根据流程实例ID物理删除知会待办记录数据
                baseMapper.isDeleTask(processInstId,1);//根据流程实例ID物理删除待办记录数据
            }else {
                //逻辑删除
                BpmProcessInstance topInstance = getTopBpmProcessInstance(processInstId);

                String topInstId = topInstance.getId();

                List<DefaultBpmProcessInstance> instList = getByParentId(topInstId, true);

                String topBpmnInstId = topInstance.getBpmnInstId();

                List<String> instIdList = getInstList(instList);

                List<String> bpmnInstList = baseMapper.getBpmnByInstList(instIdList);
                // 删除
                removeCascade(instIdList);
                // 删除流程数据。
                actExecutionManager.delByInstList(bpmnInstList);
                // 删除关联的实例。
                actExecutionManager.remove(topBpmnInstId);
            }
        }
    }


    @Transactional
	public void remove(String instId){
		this.remove(instId,false);
	}

	private List<String> getInstList(List<DefaultBpmProcessInstance> instList)
	{
		List<String> list = new ArrayList<String>();
		for (DefaultBpmProcessInstance instance : instList)
		{
			list.add(instance.getId());
		}
		return list;
	}

	/**
	 * 删除任务数据 删除任务人员数据 删除实例数据 删除抄送数据 状态数据 TASK_READ BPM_TASK_SIGNDATA
	 * BPM_TASK_TURN
	 * 
	 * @param instList
	 *            void
	 */
    @Transactional
	private void removeCascade(List<String> instList)
	{
		// 删除意见数据
		bpmCheckOpinionManager.delByInstList(instList);
		// 删除候选人数据
		bpmTaskCandidateManager.delByInstList(instList);
		// 删除任务
		bpmTaskManager.delByInstList(instList);
		// 删除状态数据
		bpmProStatusManager.delByInstList(instList);
		// 抄送删除
		copyToManager.delByInstList(instList);
		// 任务转办代理
		bpmTaskTurnManager.delByInstList(instList);
		// 会签数据
		bpmSignDataManager.delByInstList(instList);
		// 是否阅读
		bpmTaskReadManager.delByInstList(instList);

		for (String id : instList)
		{
			super.remove(id);
		}

	}

	/**
	 * 更新时如果状态为结束或者手工结束，则删除运行实例数据，更新历史数据。
	 */
	@Override
    @Transactional
	public void update(DefaultBpmProcessInstance entity)
	{

		String status = entity.getStatus();
		// 流程结束时，删除当前实例数据并归档。
		if (ProcessInstanceStatus.STATUS_END.getKey().equals(status) || ProcessInstanceStatus.STATUS_MANUAL_END.getKey().equals(status))
		{
			// baseMapper.remove(entity.getId());
			super.update(entity);
			baseMapper.createHistory(entity);
		} else
		{
			super.update(entity);
		}
	}

	@Override
	public DefaultBpmProcessInstance getByBpmnInstId(String bpmnInstId)
	{
		return baseMapper.getBpmnInstId(bpmnInstId);
	}

	@Override
	public DefaultBpmProcessInstance getBpmProcessInstanceHistory(String procInstId)
	{
		return baseMapper.getBpmProcessInstanceHistory(procInstId);
	}

	@Override
	public DefaultBpmProcessInstance getBpmProcessInstanceHistoryByBpmnInstId(String bpmnInstId)
	{
		return baseMapper.getBpmProcessInstanceHistoryByBpmnInstId(bpmnInstId);
	}

	@Override
	public IPage<DefaultBpmProcessInstance> getByUserId(String userId)
	{
		Map<String,Object> params=new HashMap<String,Object>();
    	params.put("userId", userId);
    	IPage<DefaultBpmProcessInstance> page = new Page<DefaultBpmProcessInstance>(0, Integer.MAX_VALUE);
		return baseMapper.getByUserId(page,params);
	}

	@Override
	public IPage<DefaultBpmProcessInstance> getByUserId(String userId, PageBean pageBean)
	{
		QueryFilter queryFilter = QueryFilter.build().withParam("userId", userId);
		IPage<DefaultBpmProcessInstance> page = new Page<DefaultBpmProcessInstance>(0, Integer.MAX_VALUE);
    	if(BeanUtils.isNotEmpty(pageBean)){
    		page = convert2IPage(pageBean);
    	}
		return baseMapper.getByUserId(page,queryFilter.getParams());
	}

	@Override
    @Transactional
	public void updateStatusByBpmnInstanceId(String processInstanceId, String status)
	{
		baseMapper.updateStatusByBpmnInstanceId(processInstanceId,status);
	}

	@Override
    @Transactional
	public void updateStatusByInstanceId(String processInstanceId, String status)
	{
		baseMapper.updateStatusByInstanceId(processInstanceId,status);
	}

	@Override
	public IPage<DefaultBpmProcessInstance> getByUserIdGroupList(String userId, List<IGroup> groupList)
	{
		return baseMapper.getByUserIdGroupList(this.getIPage(null),userId,groupList);
	}

	@Override
	public IPage<DefaultBpmProcessInstance> getByUserIdGroupList(String userId, List<IGroup> groupList, PageBean pageBean)
	{
		return baseMapper.getByUserIdGroupList(convert2IPage(pageBean),userId,groupList);
	}

	@Override
	public IPage<DefaultBpmProcessInstance> getByAttendUserId(String userId)
	{
		return baseMapper.getByAttendUserId(this.getIPage(null),userId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageList<DefaultBpmProcessInstance> getByAttendUserId(String userId, PageBean pageBean)
	{
		return  (PageList<DefaultBpmProcessInstance>) baseMapper.getByAttendUserId(this.getIPage(null),userId);
	}

	@Override
	public List<DefaultBpmProcessInstance> getListByBpmnDefKey(String bpmDefKey)
	{
		List<DefaultBpmProcessInstance> list = baseMapper.getListByBpmnDefKey(bpmDefKey);
		return list;
	}

	@Override
	public IPage<DefaultBpmProcessInstance> getByAttendUserId(String userId, QueryFilter queryFilter)
	{
		return baseMapper.getByAttendUserId(this.getIPage(queryFilter),userId);
	}

	@Override
	public IPage<DefaultBpmProcessInstance> getByUserIdGroupList(String userId, List<IGroup> groupList, QueryFilter queryFilter)
	{
		return baseMapper.getByUserIdGroupList(this.getIPage(queryFilter),userId,groupList);
	}
	
	private  IPage<DefaultBpmProcessInstance> getIPage(QueryFilter queryFilter){
    	IPage<DefaultBpmProcessInstance> page = new Page<DefaultBpmProcessInstance>(0, Integer.MAX_VALUE);
    	if (BeanUtils.isEmpty(queryFilter)) {
    		return page;
		}
    	PageBean pageBean = queryFilter.getPageBean();
    	if(BeanUtils.isNotEmpty(pageBean)){
    		page = convert2IPage(pageBean);
    	}
    	return page;
	}

	@Override
	public IPage<DefaultBpmProcessInstance> getByUserId(String userId, QueryFilter queryFiler)
	{
    	queryFiler.addParams("userId", userId);
		return baseMapper.getByUserId(this.getIPage(queryFiler),queryFiler.getParams());
	}

	@Override
	public IPage<DefaultBpmProcessInstance> getMyRequestByUserId(String userId, QueryFilter queryFilter)
	{
		queryFilter.addFilter("create_by_", userId, QueryOP.EQUAL);
		queryFilter.addFilter("IS_DELE_", 1, QueryOP.NOT_EQUAL);
		queryFilter.setDefaultSort("create_time_",Direction.DESC);
		Wrapper<DefaultBpmProcessInstance> wrapper = convert2Wrapper(queryFilter, currentModelClass());
		return baseMapper.selectPage(convert2IPage(queryFilter.getPageBean()),wrapper);
	}

	@Override
	public List<Map<String, Object>> getMyRequestCount(String userId) {
		Map<String,Object> param = new HashMap<>();
		param.put("userId",userId);
		return baseMapper.getMyRequestCount(param);
	}
	@Override
	public Long getMyRequestCountByUserId(String userId) {
		return baseMapper.getMyRequestCountByUserId(userId);
	}

	@Override
	public IPage<DefaultBpmProcessInstance> getMyCompletedByUserId(String userId, QueryFilter queryFilter)
	{
		queryFilter.withParam("userId",userId);
		return baseMapper.getMyCompletedByUserId(this.getIPage(queryFilter),queryFilter.getParams());
	}

	@Override
	public IPage<DefaultBpmProcessInstance> getDraftsByUserId(String userId, QueryFilter queryFilter)
	{
		queryFilter.withParam("userId",userId);
		Wrapper<DefaultBpmProcessInstance> wrapper = convert2Wrapper(queryFilter, currentModelClass());
		return baseMapper.selectPage(convert2IPage(queryFilter.getPageBean()),wrapper);
//		return baseMapper.getDraftsByUserId(this.getIPage(queryFilter),queryFilter.getParams());
	}

	@Override
	public IPage<Map<String, Object>> getHandledByUserId(String userId, QueryFilter queryFilter)
	{
		queryFilter.withParam("userId",userId);
		return baseMapper.getHandledByUserId(convert2IPage(queryFilter.getPageBean()),convert2Wrapper(queryFilter,currentModelClass()));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PageList<Map<String, Object>> getDoneInstList(String userId, QueryFilter queryFilter)
	{
		queryFilter.withParam("userId",userId);
		queryFilter.setSorter(new ArrayList<>());
		Wrapper<DefaultBpmProcessInstance> convert2Wrapper = convert2Wrapper(queryFilter, currentModelClass());
		IPage<Map<String, Object>> doneInstList = baseMapper.getDoneInstList(convert2IPage(queryFilter.getPageBean()), convert2Wrapper);
		return new PageList<>(doneInstList);
	}

	@Override
	public List<Map<String, Object>> getDoneInstCount(String userId, QueryFilter queryFilter) {
		queryFilter.withParam("userId",userId);
		return baseMapper.getDoneInstCount(convert2Wrapper(queryFilter,currentModelClass()));
	}
	@Override
	public Long getDoneInstCount(String userId) {
		return baseMapper.getDoneInstCountByUserId(userId);
	}

	@Override
	public IPage<DefaultBpmProcessInstance> getCompletedByUserId(String userId, QueryFilter queryFilter)
	{	
		queryFilter.addFilter("opinion.auditor_", userId, QueryOP.EQUAL);
		queryFilter.addFilter("wfInst.status_", "end,manualend", QueryOP.IN);
		
		queryFilter.setDefaultSort("wfInst.create_time_", Direction.DESC);
		return baseMapper.getCompletedByUserId(this.getIPage(queryFilter),convert2Wrapper(queryFilter, currentModelClass()));
	}

	@Override
    @Transactional
	public void updForbiddenByDefKey(String defKey, Integer isForbidden)
	{
		baseMapper.updForbiddenByDefKey(defKey,isForbidden);
	}

	@Override
    @Transactional
	public void updForbiddenByInstId(String instId, Integer isForbidden)
	{
		baseMapper.updForbiddenByInstId(instId,isForbidden);

	}

    @Override
    @Transactional
    public void removeBpm(String instId){
        this.remove(instId,false);
    }

    @Override
    @Transactional
    public void removeTestInstByDefKey(String defKey,Boolean isBpm)
    {
        List<DefaultBpmProcessInstance> list = baseMapper.getByDefKeyFormal(defKey,BpmProcessInstance.FORMAL_NO);
        for (DefaultBpmProcessInstance instance : list)
        {
            this.remove(instance.getId(),isBpm);
        }
    }

	@Override
	public List<DefaultBpmProcessInstance> getByParentId(String parentId, boolean includeSelf)
	{

		List<DefaultBpmProcessInstance> list = new ArrayList<DefaultBpmProcessInstance>();
		if (includeSelf)
		{
			DefaultBpmProcessInstance instance = super.get(parentId);
			list.add(instance);List<DefaultBpmProcessInstance> instances = baseMapper.getByParentId(parentId);
		}
		List<DefaultBpmProcessInstance> instances = baseMapper.getByParentId(parentId);

		if (BeanUtils.isEmpty(instances)) return list;

		for (DefaultBpmProcessInstance instance : instances){
			recursionByParent(instance, list);
		}

		return list;
	}

    @Transactional
	private void recursionByParent(DefaultBpmProcessInstance parentInst, List<DefaultBpmProcessInstance> list)
	{
		list.add(parentInst);
		List<DefaultBpmProcessInstance> instances = baseMapper.getByParentId(parentInst.getId());
		if (BeanUtils.isEmpty(instances)) 	return;
		
		for (DefaultBpmProcessInstance instance : instances){
			recursionByParent(instance, list);
		}

	}

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
	 * @throws Exception 
	 */
	@SuppressWarnings("unused")
	@Override
    @Transactional
	public ResultMessage revokeInstance(String instanceId, String informType, String cause) throws Exception
	{
		ResultMessage resultMessage = canRevokeToStart(instanceId);
		// 检查不符合撤销条件则返回。
		if (ResultMessage.ERROR == resultMessage.getResult()) return resultMessage;

		// 获取流程第一个节点
		BpmNodeDef bpmNodeDef = (BpmNodeDef) resultMessage.getVars().get("bpmNodeDef");

		List<DefaultBpmProcessInstance> instList = getByParentId(instanceId, true);

		DefaultBpmProcessInstance mainInstance = getMainInstance(instList, instanceId);
		ActionCmd actionCmd = ContextThreadUtil.getActionCmd();

		if (actionCmd == null){
			DefaultProcessInstCmd actionCmd2 = new DefaultProcessInstCmd();
			actionCmd2.setInstId(instanceId);
			actionCmd2.addTransitVars("IsUnused", true);
			ContextThreadUtil.setActionCmd(actionCmd2);

		}
		else{
			actionCmd.addTransitVars("IsUnused", true);
		}

		// 获取流程实例列表。
		List<String> includeIdList = getIdList(instList, instanceId, true, false);
		// 获取BPMN流程实例列表。
		List<String> includeBpmnIdList = getIdList(instList, instanceId, true, true);
		List<String> notIncludeBpmnIdList = getIdList(instList, instanceId, false, true);

		List<IUser> users = getNotifyUsers(includeIdList);

		// 根据实例删除流程候选人
		bpmTaskCandidateManager.delByInstList(includeIdList);
		// 删除任务
		bpmTaskManager.delByInstList(includeIdList);

		// 任务转办代理
		bpmTaskTurnManager.delByInstList(includeIdList);
		// 会签数据
		bpmSignDataManager.delByInstList(includeIdList);
		// 是否阅读
		bpmTaskReadManager.delByInstList(includeIdList);

		// 删除execution。
		if (BeanUtils.isNotEmpty(notIncludeBpmnIdList)){
			actExecutionManager.delByInstList(notIncludeBpmnIdList);
		}else{
			// 修复会签任务撤回发起人 流程异常  删除actExecution active 数据  和 var相关数据
	        actExecutionManager.delActiveByInstList(includeBpmnIdList);
		}
		// 删除指定的流程变量。
		actTaskDao.delSpecVarsByInstList(includeBpmnIdList);
		// 删除流程候选人
		actTaskDao.delCandidateByInstList(includeBpmnIdList);
		// 删除关联流程任务
		actTaskDao.delByInstList(includeBpmnIdList);
		// 创建任务与流程进行关联。
		ActExecution actExecution = actExecutionManager.get(mainInstance.getBpmnInstId());
		// 创建ACT_RU_TASK任务与之关联。
		ActTask actTask = actTaskManager.createTask(actExecution, mainInstance, bpmNodeDef);
		// 更新流程实例状态。
		mainInstance.setStatus(ProcessInstanceStatus.STATUS_REVOKE_TOSTART.getKey());
		super.update(mainInstance);
		// 更新主execution。
		updActExecution(actExecution, bpmNodeDef.getNodeId());

		// 更新节点状态
		updProStatus(instanceId, bpmNodeDef.getNodeId(), includeIdList);

		// 添加意见
		updOpinion(includeIdList, actTask, mainInstance, cause);
		// 退出堆栈
		bpmExeStackManager.popStartStack(mainInstance.getId(), "", BpmExeStack.HAND_MODE_NORMAL);

		// 删除堆栈关系
		bpmExeStackDao.removeBpmExeStackRelationInToStackId(instanceId,"%");
		bpmExeStackDao.removeBpmExeStackRelationInFromStackId(instanceId,"%");
		// 删除堆栈
		String targetNodePath = bpmExeStackDao.getInitStack(instanceId).getNodePath();
		bpmExeStackDao.removeExeStackExceptParentId(instanceId,"0");

        //根据流程实例ID撤回所有传阅任务
        if(StringUtil.isNotEmpty(instanceId)){
            String id = "";//传阅任务主键ID
            List<BpmTaskNotice> list1 = bpmTaskNoticeManager.getBpmTaskNoticeByInstId(instanceId);
            for (int i = 0; i < list1.size(); i++) {
                id = list1.get(i).getId();
                BpmTaskNoticeManager noticeManager = AppUtil.getBean(BpmTaskNoticeManager.class);
                BpmTaskNotice bpmTaskNotice = noticeManager.get(id);//根据主键ID获取传阅任务
                BpmDefinition bpmDefinition = bpmDefinitionManager.getById(bpmTaskNotice.getProcDefId());
                if(bpmTaskNotice.getIsRead()==1 && "false".equals(bpmDefinition.getIsReadRevoke())) {
                    break;//传阅接收人已阅，无法撤回
                }else{
                    if(bpmTaskNotice.getIsRead()==1 && "true".equals(bpmDefinition.getIsReadRevoke())){
                        bpmTaskNoticeDoneManager.delBpmTaskNoticeDoneById(id);//删除知会已办传阅任务
                    }
                    bpmTaskNotice.setIsRevoke(1);
                    noticeManager.update(bpmTaskNotice);
                }
            }
        }
		// 发送通知 。
		//notifyUsers(users, mainInstance, informType, cause);

        //撤回发起人执行前置事件
        BpmProcessInstance bpmProcessInstance = bpmInstService.getProcessInstance(instanceId);
        //1.获取BO数据
        List<ObjectNode> boDatas = boDataService.getDataByInst(bpmProcessInstance);
        //2.设置bo数据到上下文。
        BpmContextUtil.setBoToContext(boDatas);
        //3.获取前置事件内容
        ScriptType scriptType= ScriptType.fromKey("create");
        String script=bpmNodeDef.getScripts().get(scriptType);
        if(StringUtil.isEmpty(script)) return resultMessage;
        //给groovy脚本引擎传参数
        Map<String, Object> vars=new HashMap<>();
        ActionCmd cmd= ContextThreadUtil.getActionCmd();
        Map<String,ObjectNode> boMap= BpmContextUtil.getBoFromContext();
        if(BeanUtils.isNotEmpty(boMap)){
            Map<String, HtObjectNode> newMap =new HashMap<>();
            for (Iterator<Entry<String, ObjectNode>> iterator = boMap.entrySet().iterator(); iterator.hasNext();) {
                Entry<String, ObjectNode> next = iterator.next();
                newMap.put(next.getKey(), HtJsonNodeFactory.build().htObjectNode(next.getValue()));
            }
            vars.putAll(newMap);
            cmd.setBusData(JsonUtil.toJson(boDatas));
        }
        vars.put("nodeDef", bpmNodeDef);
        vars.put("cmd", cmd);
        try {
            groovyScriptEngine.execute(script, vars);
        } catch (BusinessException e) {
            throw new WorkFlowException(ExceptionUtils.getRootCauseMessage(e));
        } catch (Exception e) {
            StringBuffer sb = new StringBuffer();
            sb.append("<br/><br/>流程在节点："+bpmNodeDef.getName()+"("+bpmNodeDef.getNodeId()+")执行前置事件时出现异常情况！");
            sb.append("<br/>请联系管理员！");
            sb.append("<br/>可能原因为："+e.getMessage());
            sb.append("<br/>执行脚本为："+script);
            sb.append("脚本变量："+vars.toString());
            throw new WorkFlowException(sb.toString());
        }
		return resultMessage;
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
	@SuppressWarnings("unused")
	@Override
    @Transactional
	public ResultMessage revokeTask(String instId, String informType, String cause) throws Exception
	{
		BpmDefinitionAccessor bpmDefinitionAccessor = (BpmDefinitionAccessor) AppUtil.getBean("bpmDefinitionAccessor");
		BpmExeStackExecutorManager bpmExeStackExecutorManager = AppUtil.getBean(BpmExeStackExecutorManager.class);
		BpmExeStackRelationDao relationDao = AppUtil.getBean(BpmExeStackRelationDao.class);
		BpmCheckOpinionDao opinionDao = AppUtil.getBean(BpmCheckOpinionDao.class);

		// 找到此流程实例的任务列表，如果有多个任务只要有只有在所有人未处理时才能撤回
		// 1.找出当前流程实例所在节点，当前节点如果是会签不充许撤回
		List<DefaultBpmTask> list = bpmTaskManager.getByInstId(instId);
		DefaultBpmTask runningTask = list.get(0);
		String prcoDefId = runningTask.getProcDefId();
		String nodeId = runningTask.getNodeId();
		String taskId = runningTask.getTaskId();
		List<BpmNodeDef> nodeDefs = bpmDefinitionAccessor.getStartNodes(prcoDefId);
		BpmNodeDef node = bpmDefinitionAccessor.getBpmNodeDef(prcoDefId, nodeId);
		NodeType type = node.getType();
		BpmProcessInstance instance = super.get(instId);
		String status = instance.getStatus();
		// 流程状态。
		if (ProcessInstanceStatus.STATUS_REVOKE_TOSTART.getKey().equals(status) || ProcessInstanceStatus.STATUS_BACK_TOSTART.getKey().equals(status))
		{
			ResultMessage message = new ResultMessage(ResultMessage.FAIL, "流程已处于第一个节点!");
			return message;

		}
		// 验证任务是否已经在发起节点。
		boolean rtn = validTask(instance.getBpmnInstId(), nodeDefs.get(0));
		if (!rtn)
		{
			ResultMessage message = new ResultMessage(ResultMessage.FAIL, "任务已在发起节点,不能再撤销!");
			return message;
		}

		if (!type.getKey().equals(NodeType.USERTASK.getKey()))
		{
			ResultMessage message = new ResultMessage(ResultMessage.FAIL, "撤回失败，非用户任务节点不允许撤回");
			return message;
		}
		// 2.找到所在节点的前继节点
		List<BpmNodeDef> inList = node.getIncomeNodes();
		BpmExeStackExecutor stackExecutor = bpmExeStackExecutorManager.getByTaskId(taskId);
		BpmExeStackRelation relation = relationDao.getByToStackId(stackExecutor.getStackId());
		if (!relation.getFromNodeType().equals("userTask") && !NodeType.EXCLUSIVEGATEWAY.getKey().equals(relation.getFromNodeType()))
		{
			// 是谁发过来的如果不是用户任务节点，不充许撤回
			ResultMessage message = new ResultMessage(ResultMessage.FAIL, "撤回失败，有网关节点不允许撤回");
			return message;
		}
		while(NodeType.EXCLUSIVEGATEWAY.getKey().equals(relation.getFromNodeType())){
			relation = relationDao.getByToStackId(relation.getFromStackId());
		}

		// 4.判断前继节点的处理人是否为当前登录者
		List<DefaultBpmCheckOpinion> listOpinions = opinionDao.getByInstNodeIdAgree(instId,relation.getFromNodeId());
		while(BeanUtils.isEmpty(listOpinions) && BeanUtils.isNotEmpty(relation) ){
			relation = relationDao.getByFromStackId(relation.getFromStackId());
			if(BeanUtils.isNotEmpty(relation)){
				listOpinions = opinionDao.getByInstNodeIdAgree(instId,relation.getFromNodeId());
			}
		}
		boolean isCanRecall = false;
		for (DefaultBpmCheckOpinion defaultBpmCheckOpinion : listOpinions){
			if (defaultBpmCheckOpinion.getAuditor().equals(ContextUtil.getCurrentUser().getUserId())){
				isCanRecall = true;
				break;
			}
		}
		if (isCanRecall){
			BpmIdentity bpmIdentity = DefaultBpmIdentity.getIdentityByUserId(ContextUtil.getCurrentUser().getUserId(), ContextUtil.getCurrentUser().getFullname());
			// 调用驳回方式撤回
			DefaultTaskFinishCmd cmd = getCmdFromRecall(taskId, "reject", "撤回 " + cause, "normal", relation.getFromNodeId(), bpmIdentity);

			// 判断是否允许按流程图执行进行驳回
			List<BpmNodeDef> listBpmNodeDef = BpmStackRelationUtil.getHistoryListBpmNodeDef(instId, nodeId, "pre");
			List<BpmNodeDef> bpmExeStacksGoMapUserNode = new ArrayList<BpmNodeDef>();
			boolean isCanReject = false;
			List<BpmExeStackRelation> relationList= relationDao.getListByProcInstId(instId);
			for (BpmNodeDef itemNode : listBpmNodeDef){
				if (!itemNode.getType().equals(NodeType.USERTASK)) continue;
				
				boolean isHavePre = BpmStackRelationUtil.isHaveAndOrGateway(instId, node.getNodeId(), "pre",relationList);
				boolean isHaveAfter = BpmStackRelationUtil.isHaveAndOrGateway(instId, node.getNodeId(), "after",relationList);
				isCanReject = !(isHavePre && isHaveAfter) && relation.getFromNodeId().equals(itemNode.getNodeId());
				if (isCanReject) 	break;

			}
			if (!isCanReject){
				ResultMessage message = new ResultMessage(ResultMessage.FAIL, "撤回失败，当前节点状态下不允许撤回");
				return message;
			}
			boolean result = bpmTaskActionService.finishTask(cmd);
			DefaultBpmProcessInstance processInstance = super.get(instId);
			List<IUser> listUsers = new ArrayList<IUser>();
			IUser user = userService.getUserById(runningTask.getOwnerId());
			listUsers.add(user);
			BpmProcessInstanceManagerImpl.notifyUsers(listUsers, instance, informType, cause);
			// 更新流程实例状态。
			processInstance.setStatus(ProcessInstanceStatus.STATUS_REVOKE.getKey());
			super.update(processInstance);
			//根据流程实例ID撤回所有传阅任务
            if(StringUtil.isNotEmpty(instId)){
                String id = "";//传阅任务主键ID
                List<BpmTaskNotice> list1 = bpmTaskNoticeManager.getBpmTaskNoticeByTaskId(taskId);
                for (int i = 0; i < list1.size(); i++) {
                    id = list1.get(i).getId();
                    BpmTaskNoticeManager noticeManager = AppUtil.getBean(BpmTaskNoticeManager.class);
                    BpmTaskNotice bpmTaskNotice = noticeManager.get(id);//根据主键ID获取传阅任务
                    BpmDefinition bpmDefinition = bpmDefinitionManager.getById(bpmTaskNotice.getProcDefId());
                    if(bpmTaskNotice.getIsRead()==1 && "false".equals(bpmDefinition.getIsReadRevoke())) {
                        break;//传阅接收人已阅，无法撤回
                    }else{
                        if(bpmTaskNotice.getIsRead()==1 && "true".equals(bpmDefinition.getIsReadRevoke())){
                            bpmTaskNoticeDoneManager.delBpmTaskNoticeDoneById(id);//删除知会已办传阅任务
                        }
                        bpmTaskNotice.setIsRevoke(1);
                        noticeManager.update(bpmTaskNotice);
                    }
                }
            }
			ResultMessage message = new ResultMessage(ResultMessage.SUCCESS, "撤回成功");
			return message;
		} 
		else{
			ResultMessage message = new ResultMessage(ResultMessage.FAIL, "撤回失败，下个节点任务已被处理，不可撤回！");
			return message;
		}

	}

	// 撤回命令
	private DefaultTaskFinishCmd getCmdFromRecall(String taskId, String actionName, String opinion, String backHandMode, String toNodeId, BpmIdentity bpmIdentity)
	{
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
	 * 更新ACT_RU_EXECUTION表。
	 * 
	 * @param actExecution
	 *            void
	 */
    @Transactional
	private void updActExecution(ActExecution actExecution, String currentNode)
	{
		actExecution.setActId(currentNode);
		actExecution.setIsActive((short) 1);

		actExecutionManager.update(actExecution);

	}

	/**
	 * 更新意见数据。
	 * 
	 * <pre>
	 * 	1.更新为审批的意见数据为撤销到发起人，修改意见为意见。
	 * 	2.增加意见。
	 * </pre>
	 * 
	 * void
	 */
    @Transactional
	private void updOpinion(List<String> includeIdList, ActTask actTask, BpmProcessInstance mainInstance, String cause)
	{
		// 更新为审批的意见状态为撤销到发起人
		List<DefaultBpmCheckOpinion> opinionList = bpmCheckOpinionManager.getByInstIdsAndWait(includeIdList);
		long completeTime = System.currentTimeMillis();
		IUser user = ContextUtil.getCurrentUser();
		for (DefaultBpmCheckOpinion opinion : opinionList){
			opinion.setStatus(OpinionStatus.REVOKER_TO_START.getKey());
			long startTime =TimeUtil.getTimeMillis(opinion.getCreateTime()) ;
			long durms = completeTime - startTime;
			opinion.setDurMs(durms);
			opinion.setAuditor(user.getUserId());
			opinion.setAuditorName(user.getFullname());
			opinion.setOpinion(cause);
			opinion.setCompleteTime(LocalDateTime.now());
			bpmCheckOpinionManager.update(opinion);
		}

		DefaultBpmCheckOpinion checkOpinion = BpmCheckOpinionUtil.buildBpmCheckOpinion(actTask, mainInstance.getParentInstId(), mainInstance.getId());

		bpmCheckOpinionManager.create(checkOpinion);

	}

	/**
	 * 更新节点状态。
	 * 
	 * @param instanceId
	 * @param nodeId
	 * @param includeIdList
	 *            void
	 */
    @Transactional
	private void updProStatus(String instanceId, String nodeId, List<String> includeIdList){

		bpmProStatusManager.updStatusByInstList(includeIdList, NodeStatus.RECOVER_TO_START);

		DefaultBpmProStatus proStatus = bpmProStatusManager.getByInstNodeId(instanceId, nodeId);
		proStatus.setStatus(NodeStatus.PENDING.getKey());
		bpmProStatusManager.update(proStatus);
	}

	/**
	 * 通知相应的人员。
	 * 
	 * @param recievers
	 * @param instance
	 * @param informType
	 * @param cause
	 *            void
	 */
    @Transactional
	public static void notifyUsers(List<IUser> recievers, BpmProcessInstance instance, String informType, String cause)
	{
		if (BeanUtils.isEmpty(recievers))
			return;
		Map<String, Object> vars = new HashMap<String, Object>();

		vars.put(TemplateConstants.TEMP_VAR.TASK_SUBJECT, instance.getSubject());
		vars.put(TemplateConstants.TEMP_VAR.CAUSE, cause);

		try{
			MessageUtil.sendMsg(TemplateConstants.TYPE_KEY.BPMN_RECOVER, informType, recievers, vars);
		} 
		catch (Exception e){
			// 记录日志
			LOGGER.debug(e.getMessage());
		}
	}

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
	 */
	@Override
	public List<IUser> getNotifyUsers(List<String> includeIdList)
	{
		Set<IUser> userSet = new HashSet<IUser>();
		List<DefaultBpmTask> bpmTasks = bpmTaskManager.getByInstList(includeIdList);

		for (DefaultBpmTask bpmTask : bpmTasks){
			String assigneeId = bpmTask.getAssigneeId();
			if (StringUtil.isNotZeroEmpty(assigneeId)){
				userSet.add(userServiceImpl.getUserById(assigneeId));
			}
		}

		List<DefaultBpmTaskCandidate> candidates = bpmTaskCandidateManager.getByInstList(includeIdList);
		for (DefaultBpmTaskCandidate candidate : candidates){
			String executorId = candidate.getExecutor();
			if (BpmIdentity.TYPE_USER.equals(candidate.getType())){
				userSet.add(userServiceImpl.getUserById(executorId));
			} 
			else{
				userSet.addAll(userServiceImpl.getUserListByGroup(candidate.getType(), executorId));
			}
		}
		// 为空的情况直接返回。
		if (BeanUtils.isEmpty(userSet)) return null;
		List<IUser> users = new ArrayList<IUser>();
		users.addAll(userSet);

		return users;
	}

	private List<String> getIdList(List<DefaultBpmProcessInstance> instList, String instanceId, boolean includeSelf, boolean isBpmnId)
	{
		List<String> includeIdList = new ArrayList<String>();
		for (DefaultBpmProcessInstance instance : instList){
			if (!includeSelf && instanceId.equals(instance.getId())) continue;

			if (isBpmnId){
				includeIdList.add(instance.getBpmnInstId());
			} 
			else{
				includeIdList.add(instanceId);
			}

		}
		return includeIdList;
	}

	private DefaultBpmProcessInstance getMainInstance(List<DefaultBpmProcessInstance> instList, String instanceId){
		for (DefaultBpmProcessInstance instance : instList){
			if (instance.getId().equals(instanceId)){
				return instance;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DefaultBpmProcessInstance> queryList(QueryFilter queryFilter) throws IOException{
		//增加流程分管授权查询判断
		IUser user = ContextUtil.getCurrentUser();
		String userId = user.getUserId();			
		Map<String, ObjectNode> authorizeRightMap = null;
		
		boolean isAdmin=user.isAdmin();
		
		queryFilter.addParams("isAdmin", isAdmin?1:0);
		
		if(!isAdmin){
			//获得流程分管授权与用户相关的信息
			Map<String,Object> actRightMap= bpmDefAuthorizeManager.getActRightByUserId(userId, BPMDEFAUTHORIZE_RIGHT_TYPE.INSTANCE,true,true);
			//获得流程分管授权与用户相关的信息集合的流程KEY
			String defKeys = (String) actRightMap.get("defKeys");
			if(StringUtil.isNotEmpty(defKeys)){
				queryFilter.addParams("defKeys", defKeys);
			}
			//获得流程分管授权与用户相关的信息集合的流程权限内容
			authorizeRightMap = (Map<String,ObjectNode>) actRightMap.get("authorizeRightMap");
		}
		
		//查询列表
		PageList<DefaultBpmProcessInstance> bpmProcessInstanceList=(PageList<DefaultBpmProcessInstance>)query(queryFilter);
		
		//把前面获得的流程分管授权的权限内容设置到流程管理列表
		for (DefaultBpmProcessInstance instance : bpmProcessInstanceList.getRows()){
			ObjectNode rightJson=null;
			String defKey=instance.getProcDefKey();
			if(instance.getDuration()== null || instance.getDuration()<1){
				instance.setDuration(TimeUtil.getTime(LocalDateTime.now(), instance.getCreateTime()));
			}
			if(authorizeRightMap==null){
				rightJson=AuthorizeRight.getAdminRight();
			}
			else{
				rightJson=authorizeRightMap.get(defKey);
			}
			instance.setAuthorizeRight(rightJson);
		}
		
		return bpmProcessInstanceList.getRows();
	}

	

	@Override
    public ResultMessage canRevokeToStart(String instanceId) throws Exception
	{

		BpmProcessInstance instance = super.get(instanceId);
		String defId = instance.getProcDefId();
		List<BpmNodeDef> nodeDefs = bpmDefinitionAccessor.getStartNodes(defId);

		ResultMessage message = new ResultMessage();

		IUser user = ContextUtil.getCurrentUser();
		if (user == null){
			message.setResult(ResultMessage.ERROR);
			message.setMessage("请先设置当前执行人!");
			return message;
		}

		if (nodeDefs.size() > 1){
			message.setResult(ResultMessage.ERROR);
			message.setMessage("发起节点后有多个节点!");
			return message;
		}

		String status = instance.getStatus();
		// 流程状态。
		if (ProcessInstanceStatus.STATUS_REVOKE_TOSTART.getKey().equals(status) 
				|| ProcessInstanceStatus.STATUS_BACK_TOSTART.getKey().equals(status)){
			message.setResult(ResultMessage.ERROR);
			message.setMessage("流程已处于第一个节点!");
			return message;
		}

		if (!user.getUserId().equals(instance.getCreateBy())){
			message.setResult(ResultMessage.ERROR);
			message.setMessage("当前执行人和流程发起人不是同一个人!");
			return message;
		}
		// 验证任务是否已经在发起节点。
		boolean rtn = validTask(instance.getBpmnInstId(), nodeDefs.get(0));
		if (!rtn){
			message.setResult(ResultMessage.ERROR);
			message.setMessage("任务已在发起节点,不能再撤销!");
			return message;
		}

		// 添加流程节点。
		message.addVariable("bpmNodeDef", nodeDefs.get(0));
		message.setResult(ResultMessage.SUCCESS);
		return message;
	}

	private boolean validTask(String bpmnInstId, BpmNodeDef nodeDef){
		String nodeId = nodeDef.getNodeId();
		List<ActTask> list = actTaskDao.getByInstId(bpmnInstId);

		for (ActTask task : list){
			if (nodeId.equals(task.getTaskDefKey())){
				return false;
			}
		}
		return true;
	}

	@Override
	public ResultMessage canRevoke(String instanceId, String nodeId){
		ResultMessage message = ResultMessage.getSuccess();

		BpmProcessInstance processInstance = super.get(instanceId);
		// 判断实例状态。
		message = checkInstance(processInstance);

		if (message.getResult() == ResultMessage.ERROR){
			return message;
		}
		

		return message;
	}

	

	private ResultMessage checkInstance(BpmProcessInstance processInstance){
		ResultMessage message = ResultMessage.getSuccess();
		String status = processInstance.getStatus();
		if (ProcessInstanceStatus.STATUS_RUNNING.getKey().equals(status))
		{
			return message;
		}
		message.setResult(ResultMessage.ERROR);
		String msg = "";
		if (ProcessInstanceStatus.STATUS_BACK.getKey().equals(status)){
			msg = "流程被驳回";
		} 
		else if (ProcessInstanceStatus.STATUS_BACK_TOSTART.getKey().equals(status)){
			msg = "流程被驳回到发起人";
		} 
		else if (ProcessInstanceStatus.STATUS_END.getKey().equals(status)){
			msg = "流程实例已结束";
		} else if (ProcessInstanceStatus.STATUS_END.getKey().equals(status))
		{
			msg = "流程实例被人工终止";
		} else if (ProcessInstanceStatus.STATUS_DRAFT.getKey().equals(status))
		{
			msg = "流程实例为草稿状态";
		} else if (ProcessInstanceStatus.STATUS_REVOKE.getKey().equals(status))
		{
			msg = "流程实例为撤销状态";
		} else if (ProcessInstanceStatus.STATUS_REVOKE_TOSTART.getKey().equals(status))
		{
			msg = "流程实例为撤销状态";
		}
		message.setMessage(msg);
		return message;
	}

	@Override
	public BpmProcessInstance getTopBpmProcessInstance(String proceInstId)
	{
		BpmProcessInstance instance = get(proceInstId);
		
		return  getTopBpmProcessInstance(instance);
	}

	@Override
	public List<DefaultBpmProcessInstance> getListByDefId(String defId)
	{
		return baseMapper.getListByDefId(defId);
	}

	@Override
	public BpmProcessInstance getTopBpmProcessInstance(BpmProcessInstance instance)
	{
		while (StringUtil.isNotZeroEmpty(instance.getParentInstId())){
			instance = super.get(instance.getParentInstId());
		}
		return instance;
	}

	@Override
	public List<BpmProcessInstance> getBpmProcessByParentIdAndSuperNodeId(String parentInstId, String superNodeId) {
		return baseMapper.getBpmnByParentIdAndSuperNodeId(parentInstId,superNodeId);
	}
	
	@Override
	public List<BpmProcessInstance> getHiBpmProcessByParentIdAndSuperNodeId(String parentInstId, String superNodeId) {
		return baseMapper.getHiBpmnByParentIdAndSuperNodeId(parentInstId,superNodeId);
	}

	@Override
	public DefaultBpmProcessInstance getByBusinessKey(String businessKey) {
		return baseMapper.getByBusinessKey(businessKey);
	}

	@Override
	public PageList<DefaultBpmProcessInstance> getMyHandledMeeting(QueryFilter queryFilter) {
		PageBean pageBean = queryFilter.getPageBean();
		IPage<DefaultBpmProcessInstance> page = new Page<DefaultBpmProcessInstance>(0, Integer.MAX_VALUE);
    	if(BeanUtils.isNotEmpty(pageBean)){
    		page = convert2IPage(pageBean);
    	}
		IPage<DefaultBpmProcessInstance> insList=baseMapper.getMyHandledMeeting(page,queryFilter.getParams());
		return new PageList<>(insList);
	}

    @Override
    public PageList<DefaultBpmProcessInstance> queryByuserId(QueryFilter queryFilter) {
        PageBean pageBean = queryFilter.getPageBean();
        IPage<DefaultBpmProcessInstance> page = new Page<DefaultBpmProcessInstance>(0, Integer.MAX_VALUE);
    	if(BeanUtils.isNotEmpty(pageBean)){
    		page = convert2IPage(pageBean);
    	}
        IPage<DefaultBpmProcessInstance> query = baseMapper.queryByuserId(page,convert2Wrapper(queryFilter,currentModelClass()));
        return new PageList<DefaultBpmProcessInstance>(query);
    }

    @Override
    public IPage<DefaultBpmProcessInstance> getById(String auditor,QueryFilter queryFilter){
        queryFilter.withParam("auditor",auditor);
        queryFilter.setDefaultSort("create_time_",Direction.DESC);
        PageBean pageBean = queryFilter.getPageBean();
        IPage<DefaultBpmProcessInstance> page = new Page<DefaultBpmProcessInstance>(0, Integer.MAX_VALUE);
    	if(BeanUtils.isNotEmpty(pageBean)){
    		page = convert2IPage(pageBean);
    	}
	    return baseMapper.getById(page,convert2Wrapper(queryFilter,currentModelClass()));
    }

    @Override
    @Transactional
    public void restore(String id) {
        baseMapper.isDeleInst(id,0);//根据流程实例ID恢复实例记录数据
        baseMapper.isDeleOpinion(id,0);//根据流程实例ID恢复审批记录数据
        baseMapper.isDeleNotice(id,0);//根据流程实例ID恢复知会待办记录数据
        baseMapper.isDeleTask(id,0);//根据流程实例ID恢复待办记录数据
    }

    @Override
    public List<String> getBpmTaskIdByInstId(String instId) {
        return baseMapper.getBpmTaskIdByInstId(instId);
    }

    @Override
    @Transactional
    public void deleteNotice(String instId) {
        baseMapper.deleteNotice(instId);
    }

    @Override
    public List<String> getNodeIdByInstId(String instId) {
        return baseMapper.getNodeIdByInstId(instId);
    }

	@Override
	public List<Map<String, Object>> getFlowFieldList(QueryFilter queryFilter) {
		//todo zxy
		return baseMapper.getFlowFieldList(convert2IPage(queryFilter.getPageBean()),convert2Wrapper(queryFilter, currentModelClass()));
	}
	

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
	 * @throws Exception 
	 */
	@Override
	public Object getNodeApprovalUsers(List<String> includeIdList) throws Exception
	{
		List<DefaultBpmTask> bpmTasks = bpmTaskManager.getByInstList(includeIdList);

		if (BeanUtils.isEmpty(bpmTasks) || bpmTasks.size()<1) {
			return null;
		}
		Map<String,String> taskIdMap = new HashMap<>();
		Map<String, Map<String, IUser>> nodeApprovalMap =new HashMap<>();
		for (DefaultBpmTask bpmTask : bpmTasks){
			String assigneeId = bpmTask.getAssigneeId();
			taskIdMap.put(bpmTask.getTaskId(), bpmTask.getNodeId());
			if (StringUtil.isNotZeroEmpty(assigneeId)){
				Map<String, IUser> map=new HashMap<>();
				if(nodeApprovalMap.containsKey(bpmTask.getNodeId())) {
					map =  nodeApprovalMap.get(bpmTask.getNodeId());
				}
				map.put(assigneeId, userServiceImpl.getUserById(assigneeId));
				nodeApprovalMap.put(bpmTask.getNodeId(),map);
			}
		}
		List<DefaultBpmTaskCandidate> candidates = bpmTaskCandidateManager.getByInstList(includeIdList);
		for (DefaultBpmTaskCandidate candidate : candidates){
			String executorId = candidate.getExecutor();
			String nodeId =taskIdMap.get(candidate.getTaskId());
			Map<String, IUser> map = nodeApprovalMap.containsKey(nodeId)?nodeApprovalMap.get(nodeId):new HashMap<>();
			if (BpmIdentity.TYPE_USER.equals(candidate.getType())){
				IUser userById = userServiceImpl.getUserById(executorId);
				if (BeanUtils.isEmpty(userById)) {
					continue;
				}
				map.put(userById.getUserId(), userById);
			} 
			else{
				Map<String, IUser> userMap=new HashMap<>();
				List<IUser> users = userServiceImpl.getUserListByGroup(candidate.getType(), executorId);
				for (IUser iUser : users) {
					userMap.put(iUser.getUserId(), iUser);
				}
				map.putAll(userMap);
			}
			nodeApprovalMap.put(nodeId, map);
		}
		
		
		List<Map<String, Object>> resultList =new ArrayList<>();
		List<BpmNodeDef> allNodeDef = bpmDefinitionAccessor.getAllNodeDef(bpmTasks.get(0).getProcDefId());
		for (BpmNodeDef bpmNodeDef : allNodeDef) {
			if (nodeApprovalMap.containsKey(bpmNodeDef.getNodeId())) {
				Map<String, IUser> map = nodeApprovalMap.get(bpmNodeDef.getNodeId());
				Map<String, Object> jsonNode = JsonUtil.toMap(JsonUtil.toJson(bpmNodeDef));
				jsonNode.put("assigneeUsers", map.values());
				resultList.add(jsonNode);
			}
			
		}
		return resultList;
	}


	@Override
	public List<DefaultBpmProcessInstance> getListByRightMap(Map<String, String> userRightMap) {
		return baseMapper.getListByRightMap(userRightMap);
	}

}
