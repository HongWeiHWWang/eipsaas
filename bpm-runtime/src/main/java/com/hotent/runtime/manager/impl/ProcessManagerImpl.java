package com.hotent.runtime.manager.impl;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.context.BaseContext;
import com.hotent.base.enums.ResponseErrorEnums;
import com.hotent.base.exception.BaseException;
import com.hotent.base.exception.RequiredException;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryField;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.service.PropertyService;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.Base64;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.Dom4jUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.base.util.string.StringValidator;
import com.hotent.base.util.time.TimeUtil;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.cmd.BaseActionCmd;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.constant.NodeStatus;
import com.hotent.bpm.api.constant.NodeType;
import com.hotent.bpm.api.constant.OpinionStatus;
import com.hotent.bpm.api.constant.ProcessInstanceStatus;
import com.hotent.bpm.api.model.form.FormType;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.def.BpmDefinition;
import com.hotent.bpm.api.model.process.def.BpmProcessDef;
import com.hotent.bpm.api.model.process.def.BpmProcessDefExt;
import com.hotent.bpm.api.model.process.def.BpmVariableDef;
import com.hotent.bpm.api.model.process.def.NodeProperties;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeForm;
import com.hotent.bpm.api.model.process.task.BpmTask;
import com.hotent.bpm.api.model.process.task.BpmTaskOpinion;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.api.service.BpmDefinitionService;
import com.hotent.bpm.api.service.BpmFormService;
import com.hotent.bpm.api.service.BpmInstService;
import com.hotent.bpm.api.service.BpmOpinionService;
import com.hotent.bpm.api.service.BpmTaskActionService;
import com.hotent.bpm.api.service.BpmTaskService;
import com.hotent.bpm.api.service.DiagramService;
import com.hotent.bpm.engine.def.impl.handler.PropertiesBpmDefXmlHandler;
import com.hotent.bpm.engine.form.BpmFormFactory;
import com.hotent.bpm.engine.inst.DefaultProcessInstCmd;
import com.hotent.bpm.engine.task.cmd.DefaultTaskFinishCmd;
import com.hotent.bpm.model.form.Form;
import com.hotent.bpm.model.form.FormCategory;
import com.hotent.bpm.model.form.FormModel;
import com.hotent.bpm.model.identity.DefaultBpmIdentity;
import com.hotent.bpm.model.var.DefaultBpmVariableDef;
import com.hotent.bpm.natapi.inst.NatProInstanceService;
import com.hotent.bpm.natapi.task.NatTaskService;
import com.hotent.bpm.persistence.manager.BpmBusLinkManager;
import com.hotent.bpm.persistence.manager.BpmCheckOpinionManager;
import com.hotent.bpm.persistence.manager.BpmCommuReceiverManager;
import com.hotent.bpm.persistence.manager.BpmDefinitionManager;
import com.hotent.bpm.persistence.manager.BpmExeStackRelationManager;
import com.hotent.bpm.persistence.manager.BpmProStatusManager;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.persistence.manager.BpmTaskCommuManager;
import com.hotent.bpm.persistence.manager.BpmTaskManager;
import com.hotent.bpm.persistence.manager.BpmTaskNoticeManager;
import com.hotent.bpm.persistence.model.BpmBusLink;
import com.hotent.bpm.persistence.model.BpmExeStackRelation;
import com.hotent.bpm.persistence.model.BpmIdentityResult;
import com.hotent.bpm.persistence.model.BpmTaskNotice;
import com.hotent.bpm.persistence.model.DefaultBpmCheckOpinion;
import com.hotent.bpm.persistence.model.DefaultBpmDefinition;
import com.hotent.bpm.persistence.model.DefaultBpmProStatus;
import com.hotent.bpm.persistence.model.DefaultBpmProcessDefExt;
import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;
import com.hotent.bpm.persistence.model.DefaultBpmTask;
import com.hotent.bpm.persistence.util.BpmStackRelationUtil;
import com.hotent.bpm.persistence.util.ServiceUtil;
import com.hotent.bpm.util.BpmCheckOpinionUtil;
import com.hotent.bpm.util.BpmIdentityUtil;
import com.hotent.bpm.util.BpmUtil;
import com.hotent.runtime.manager.IFlowManager;
import com.hotent.runtime.manager.IProcessManager;
import com.hotent.runtime.params.BpmCheckOpinionVo;
import com.hotent.runtime.params.BpmImageParamObject;
import com.hotent.runtime.params.BpmNodeDefVo;
import com.hotent.runtime.params.BpmTaskResult;
import com.hotent.runtime.params.DefOtherParam;
import com.hotent.runtime.params.DoEndParamObject;
import com.hotent.runtime.params.DoNextParamObject;
import com.hotent.runtime.params.StartFlowParamObject;
import com.hotent.runtime.params.StartResult;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.model.IdentityType;
import com.hotent.uc.api.service.IUserService;

@Service("IProcessManager")
public class ProcessManagerImpl implements IProcessManager {
	@Resource
	BpmOpinionService bpmOpinionService;
	@Resource
	BpmInstService processInstanceService;
	@Resource
	BpmTaskService bpmTaskService;
	@Resource
	BpmDefinitionService bpmDefinitionService;
	@Resource
	BpmTaskActionService bpmTaskActionService;
	@Resource
	BpmTaskManager bpmTaskManager;
	@Resource
	BpmDefinitionAccessor bpmDefinitionAccessor;
	@Resource
	BpmExeStackRelationManager relationManager;
	@Resource
	BpmProcessInstanceManager bpmProcessInstanceManager;
	@Resource
	PropertiesBpmDefXmlHandler propertiesBpmDefXmlHandler;
	@Resource
	BpmDefinitionManager bpmDefinitionManager;
	@Resource
	BpmInstService bpmInstService;
	@Resource
	BpmCheckOpinionManager bpmCheckOpinionManager;
	@Resource
	BpmProStatusManager bpmProStatusManager;
	@Resource
	BpmBusLinkManager bpmBusLinkManager;
	@Resource
	NatTaskService natTaskService;
	@Resource(name="defaultBpmFormService")
	BpmFormService bpmFormService;
    @Resource
    BpmCommuReceiverManager bpmCommuReceiverManager;
    @Resource
    BpmTaskCommuManager bpmTaskCommuManager;
    @Resource
    IUserService userServiceImpl;
    @Resource
    NatProInstanceService natProInstanceService;
    @Resource
    BpmProcessInstanceManager bpmProcessInstanceMapper;
    @Resource
    IFlowManager flowManager;
    @Resource
    BaseContext baseContext;
    
	@Override
	@Transactional
	public StartResult start(StartFlowParamObject startFlowParamObject) throws Exception {
		ContextUtil.clearAll();
		DefaultProcessInstCmd processCmd=getStartCmd(startFlowParamObject) ;
		BpmProcessInstance instance = processInstanceService.startProcessInst(processCmd);
		String instId = instance.getId();
		return new StartResult("流程启动成功", instId);
	}
	
	@Override
	@Transactional
	public StartResult saveDraft(StartFlowParamObject startFlowParamObject) throws Exception {
		ContextUtil.clearAll();
		DefaultProcessInstCmd processCmd=getStartCmd(startFlowParamObject) ;
        processCmd.setApproval(startFlowParamObject.getApproval());
		Map map = processInstanceService.saveDraft(processCmd);
		BpmProcessInstance instance= (BpmProcessInstance) map.get("instance");
		String msg= (String) map.get("msg");
		String instId = instance.getId();
		return new StartResult(msg, instId);
	}
	
	private  DefaultProcessInstCmd getStartCmd(StartFlowParamObject startFlowParamObject) throws Exception{
	
		String defId = startFlowParamObject.getDefId();
		String flowKey = startFlowParamObject.getFlowKey();
		String subject = startFlowParamObject.getSubject();
		String sysCode = startFlowParamObject.getSysCode();
		String formType = startFlowParamObject.getFormType();
		// 已有实例，则是从草稿中启动
		String proInstId = startFlowParamObject.getProInstId();
		
		Map<String,String> vars = startFlowParamObject.getVars();
		// 表单数据，先解密
		String busData = "";
		if(StringUtil.isNotEmpty(startFlowParamObject.getData())){
			busData = Base64.getFromBase64(startFlowParamObject.getData());
		}
		// 表单的业务键
		String businessKey = startFlowParamObject.getBusinessKey();
		// 流程key和定义id二选一。
		if (StringUtil.isEmpty(defId) && StringUtil.isEmpty(flowKey)) {
			throw new RequiredException(ResponseErrorEnums.REQUIRED_ERROR.getMessage() + ":" + "流程定义ID和流程key必须填写一个!");
		}
		
		if(StringUtil.isNotEmpty(proInstId)){
			DefaultBpmProcessInstance processInstance = bpmProcessInstanceManager.get(proInstId);
			if(BeanUtils.isNotEmpty(processInstance)){
				if(!ProcessInstanceStatus.STATUS_DRAFT.getKey().equals(processInstance.getStatus())){
					throw new BaseException("该实例id对应的流程实例不是草稿状态");
				}
			}
		}
		
		DefaultProcessInstCmd processCmd = new DefaultProcessInstCmd();
		
		if (StringUtil.isNotEmpty(startFlowParamObject.getAccount()) && !ContextUtil.getCurrentUser().getAccount().equals(startFlowParamObject.getAccount())) {
			processCmd.addTransitVars(BpmConstants.START_USER, userServiceImpl.getUserByAccount(startFlowParamObject.getAccount()));
		}
		
		if (StringUtil.isNotEmpty(businessKey)) {
			processCmd.setBusinessKey(businessKey);
		}
		
		if (BeanUtils.isNotEmpty(startFlowParamObject.getUrgentStateValue()) && startFlowParamObject.getUrgentStateValue().hasNonNull("new")) {
			processCmd.setUrgentState(startFlowParamObject.getUrgentStateValue().get("new").asText());
		}
		
		if (StringUtil.isNotEmpty(defId)) {
			processCmd.setProcDefId(defId);
		}
		if (StringUtil.isEmpty(defId) && StringUtil.isNotEmpty(flowKey)) {
			BpmDefinition bpmDefinition = bpmDefinitionService.getBpmDefinitionByDefKey(flowKey, false);
			if(BeanUtils.isNotEmpty(bpmDefinition)){
				defId = bpmDefinition.getDefId();
				processCmd.setFlowKey(flowKey);
			}else{
				throw new BaseException("flowKey为"+flowKey+"的流程不存在");
			}
			
		}
		BpmDefinition def = bpmDefinitionService.getBpmDefinitionByDefId(defId);
		if (BeanUtils.isEmpty(def)) {
			throw new BaseException("id为"+defId+"的流程不存在");
		}else if (BpmDefinition.STATUS.FORBIDDEN.equals(def.getStatus()) || BpmDefinition.STATUS.FORBIDDEN_INSTANCE.equals(def.getStatus())) {
			throw new BaseException("流程定义已被禁止无法发起，请联系管理员");
		}
		
		if (StringUtil.isNotEmpty(subject)) {
			processCmd.setSubject(subject);
		}
        processCmd.setSupportMobile(startFlowParamObject.getSupportMobile());
		// 表单数据
		processCmd.setBusData(busData);
		
		processCmd.setAgentLeaderId(startFlowParamObject.getAgentLeaderId());
		
		if(StringUtil.isNotEmpty(sysCode)){
			processCmd.setSysCode(sysCode);
		}
		//设置表单类型
		if(StringUtil.isEmpty(formType)){
			processCmd.setDataMode(getFormType(defId));
		}else{
			processCmd.setDataMode(FormCategory.FRAME.value().equals(formType)? ActionCmd.DATA_MODE_PK:ActionCmd.DATA_MODE_BO);
		}
		
		ObjectNode node = JsonUtil.getMapper().createObjectNode();
		node.put("defId", startFlowParamObject.getDefId());
		node.put("flowKey", startFlowParamObject.getFlowKey());
		
		//获取用户自定义的流程变量
		List<BpmVariableDef> list = flowManager.getWorkflowVar(node.toString());
		if (list != null && list.size() > 0) {
			Map<String, String> existVars = startFlowParamObject.getVars();
			if (BeanUtils.isEmpty(existVars)) {
				existVars = new HashMap<>();
				startFlowParamObject.setVars(existVars);
			}
			for (int i = 0; i < list.size(); i++) {
				String key = list.get(i).getName();
				if (existVars.containsKey(key)) {
					continue;
				}
				existVars.put(key, list.get(i).getDefaultVal() + "");
			}
		}
		// 校验流程变量。
		setValriablesToCmd(defId,"",vars,processCmd);
		
		//如果设置了跳转目标节点。则设置
		if (StringUtil.isNotEmpty(startFlowParamObject.getDestination())) {
			processCmd.setDestination(startFlowParamObject.getDestination());
		}
		
		String nodeUsers=startFlowParamObject.getNodeUsers();
		
		if(StringUtil.isNotEmpty(nodeUsers)){
			//是否由选择人员做为下一节点处理人
			int isSendNodeUsers=startFlowParamObject.getIsSendNodeUsers();
			Map<String, List<BpmIdentity>> specUserMap= BpmIdentityUtil.getBpmIdentity(nodeUsers);
			
			//指定执行人
			if(BeanUtils.isNotEmpty(specUserMap)){
				processCmd.addVariable(BpmConstants.BPM_NODE_USERS, JsonUtil.toJson(specUserMap));
				if(isSendNodeUsers==1){
					processCmd.setBpmIdentities(specUserMap);
				}
			}
		}
		

		if (StringUtil.isNotEmpty(proInstId)) {
			processCmd.setInstId(proInstId);
		}
        if (StringUtil.isNotEmpty(startFlowParamObject.getStartOrgId())) {
        	processCmd.addTransitVars(BpmConstants.START_ORG_ID, startFlowParamObject.getStartOrgId());
		}
        //针对处理任务时保存草稿操作对设置有子表授权数据的影响
        if(StringUtil.isNotEmpty(startFlowParamObject.getTaskId())) {
        	DefaultBpmTask task = bpmTaskManager.getByTaskId(startFlowParamObject.getTaskId());
        	if(BeanUtils.isNotEmpty(task)) {
        		processCmd.addTransitVars(BpmConstants.BPM_TASK, task);
        	}
        }
		return processCmd;
		
	}
	
	
	/**
	 * 获取表单类型
	 * @param defId
	 * @return
	 */
	private String getFormType(String defId){
		String formType = ActionCmd.DATA_MODE_BO;
		try {
			BpmNodeForm nodeForm = bpmFormService.getByDefId(defId);
			if(BeanUtils.isNotEmpty(nodeForm)){
				Form form = nodeForm.getForm();
				if(BeanUtils.isNotEmpty(form)){
					FormCategory type = form.getType();
					if(FormCategory.FRAME.equals(type)){
						formType = ActionCmd.DATA_MODE_PK;
					}
				}
			}
		} catch (Exception e) {}
		return formType;
	}
	
	/**
	 * 获取合法的流程变量。
	 * @param defId
	 * @param nodeId
	 * @return
	 * @throws Exception 
	 */
	private Map<String,Object> getActVars(String defId,String nodeId, Map<String,String> varMap) throws Exception{
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
			//变量必填
//			if(var.isRequired()) {
//				if(!varMap.containsKey(varkey)){
//					throw new Exception("变量:[" +varkey +"]必填");
//				}
//			}
			String val=varMap.get(varkey);
			if(val!=null){
				Object rtnVal=DefaultBpmVariableDef.getValue(var.getDataType(), val);
				rtnMap.put(varkey, rtnVal);
			}
		}
		return rtnMap;
	}
	
	
	
	@Override
	@Transactional
	public CommonResult<String> doNext(DoNextParamObject doNextParamObject) throws Exception{
		// 清理上下文线程变量。
		ContextUtil.clearAll();

		// 传入的账户
		String account = doNextParamObject.getAccount();
		ServiceUtil.setCurrentUser(account);
		String taskId = doNextParamObject.getTaskId();
		String actionName = doNextParamObject.getActionName();
		String opinion = doNextParamObject.getOpinion();
		String data = "";
		String base64data = doNextParamObject.getData();
		if(StringUtil.isNotEmpty(base64data)){
			data = Base64.getFromBase64(base64data);
		}
		// 会签直接处理，当值为true时 ，会签直接完成不用等其他人进行审批。
		Boolean directHandlerSign = doNextParamObject.getDirectHandlerSign();
		// 退回模式
		String backHandMode = doNextParamObject.getBackHandMode();
		String jumpType = doNextParamObject.getJumpType();
		if (bpmTaskService.getByTaskId(taskId) == null) {
			// 返回错误消息。
			throw new RuntimeException("此任务已被处理或不存在");
		}
		DefaultTaskFinishCmd cmd = new DefaultTaskFinishCmd();
		// 自由跳转 或者 驳回到指定节点
		if ("free".equals(jumpType) || "select".equals(jumpType) || "reject".equals(actionName)) {
			//跳转的目标节点，传入节点id
			String destination = doNextParamObject.getDestination();
			if (StringUtil.isNotEmpty(destination)) {
				cmd.setDestination(destination);
			}
		}
		// 会签任务的直接处理
		if (directHandlerSign) {
			cmd.addTransitVars(BpmConstants.SIGN_DIRECT, "1");
		}
		cmd.setTaskId(taskId);
		cmd.setActionName(actionName);
		// 设置表单意见。
		cmd.setApprovalOpinion(opinion);
		// 处理表单意见，如果表单的意见存在则覆盖之前的意见。 覆盖业务表单数据
		if (StringUtil.isNotEmpty(data)) {
			BpmUtil.handOpinion(data, cmd);
			cmd.setBusData(data);
			cmd.setDataMode(ActionCmd.DATA_MODE_BO);
		}

		// 流程变量
		BpmTask bpmTask = (BpmTask) cmd.getTransitVars(BpmConstants.BPM_TASK);
		Map<String,String> vars = doNextParamObject.getVars();
		// 校验流程变量。
		setValriablesToCmd(bpmTask.getProcDefId(),bpmTask.getNodeId(),vars,cmd);

		// 设置流程驳回时跳转模式。direct：回到本节点，按流程图执行
		cmd.addTransitVars(BpmConstants.BACK_HAND_MODE, backHandMode);

		// 设置节点人员
		String nodeUsers = "";
		if(StringUtil.isNotEmpty(doNextParamObject.getNodeUsers())){
			nodeUsers = Base64.getFromBase64(doNextParamObject.getNodeUsers());
		}
		if (StringUtil.isNotEmpty(nodeUsers)) {
			Map<String, List<BpmIdentity>> specUserMap = BpmIdentityUtil.getBpmIdentity(nodeUsers);
			cmd.addTransitVars(BpmConstants.BPM_NODE_USERS, specUserMap);
			cmd.setBpmIdentities(specUserMap);
		}
		if(StringUtil.isNotEmpty(doNextParamObject.getNotifyType())){
			cmd.setNotifyType(doNextParamObject.getNotifyType());
		}
		// 设置上下文执行人
		ContextUtil.setCurrentUserByAccount(account);
		bpmTaskActionService.finishTask(cmd);

		return new CommonResult<String>(true, "任务处理成功");
		
	}
	
	/**
	 * 设置流程变量。
	 * @param defId		流程定义ID
	 * @param nodeId	节点ID
	 * @param vars		流程变量 格式
	 * @param cmd		
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private void setVariables(String defId,String nodeId,String vars,BaseActionCmd cmd) throws Exception{
		Map<String, String> variables = convertVars(vars);
		//检查流程变量合法性。
		Map<String, Object> varMap= getActVars(defId, nodeId, variables);
		// 流程变量
		if (BeanUtils.isNotEmpty(varMap)) {
			cmd.setVariables(varMap);
		}
	}
	
	private void setValriablesToCmd(String defId,String nodeId ,Map<String,String> variables,BaseActionCmd cmd) throws Exception{
		// 检查流程变量合法性。
		if(BeanUtils.isEmpty(variables)) return;
		Map<String, Object> varMap = getActVars(defId, nodeId, variables);
		// 流程变量
		if (BeanUtils.isNotEmpty(varMap)) {
			cmd.setVariables(varMap);
		}
	}
	
	/**
	 * 根据指定taskID获取可驳回的节点。
	 * <pre>
	 * json 格式: {taskId:"",rejectType:"direct,normal"}
	 * rejectType:返回方式 
	 * direct ：直来直往
	 * normal ：按照流程图执行
	 * </pre>
	 */
	@Override
	public List<BpmNodeDefVo> getEnableRejectNode(String taskId,String rejectType) throws Exception {
		DefaultBpmTask task = bpmTaskManager.get(taskId);
		String defId = task.getProcDefId();
		String nodeId = task.getNodeId();
		BpmNodeDef taskNodeDef = bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);
		NodeProperties nodeProperties=taskNodeDef.getLocalProperties();
		String backMode= nodeProperties.getBackMode();
		if(StringUtil.isEmpty(backMode))backMode="normal";
		String procInstId = task.getProcInstId();
		
		List<BpmNodeDef> listBpmNodeDef = BpmStackRelationUtil.getHistoryListBpmNodeDef(procInstId, task.getNodeId(), "pre");

		// 允许直来直往的节点
		List<BpmNodeDefVo> bpmExeStacksUserNode = new ArrayList<BpmNodeDefVo>();
		// 允许按流程图执行的节点
		List<BpmNodeDefVo> bpmExeStacksGoMapUserNode = new ArrayList<BpmNodeDefVo>();
		List<BpmExeStackRelation> relationList= relationManager.getListByProcInstId(procInstId);
		for (BpmNodeDef node : listBpmNodeDef) {
			if ((node.getType().equals(NodeType.USERTASK)||node.getType().equals(NodeType.SIGNTASK))&&!node.getNodeId().equals(nodeId)) {
				BpmNodeDefVo vo = BpmNodeDefVo.parseVo(node);
				bpmExeStacksUserNode.add(vo);

				boolean isHavePre = BpmStackRelationUtil.isHaveAndOrGateway(procInstId, node.getNodeId(), "pre",relationList);
				boolean isHaveAfter = BpmStackRelationUtil.isHaveAndOrGateway(procInstId, node.getNodeId(), "after",relationList);
				if (!(isHavePre && isHaveAfter)) {
					bpmExeStacksGoMapUserNode.add(vo);
				}else{
					List<BpmNodeDef> incomeNodes = node.getIncomeNodes();
					if(BeanUtils.isNotEmpty(incomeNodes)){
						BpmNodeDef nodeDef = incomeNodes.get(0);
						//如果是从开始网关进入的用户节点，则允许按流程图驳回
						if(node.getType().equals(NodeType.USERTASK) && (nodeDef.getType().equals(NodeType.START)||nodeDef.getType().equals(NodeType.USERTASK))){
							bpmExeStacksGoMapUserNode.add(vo);
						}
					}
				}
			}

		}
		return "direct".equals(rejectType)?bpmExeStacksUserNode:bpmExeStacksGoMapUserNode;
	}
	
	
	/**
	 * 将流程变量从一个json数据格式转换成一个map的结构。
	 * @param varsJson 变量格式如下: [{name:"",val:""},{name:"",val:""}]
	 * @return
	 * @throws Exception
	 */
	private Map<String, String> convertVars(String varsJson) throws Exception {
		Map<String, String> varMap = new HashMap<String, String>();
		
		if(StringUtil.isEmpty(varsJson)) return varMap;
		
		ArrayNode  array = (ArrayNode) JsonUtil.toJsonNode(varsJson);
		for (int i = 0; i < array.size(); i++) {
			String varStr = array.get(i).toString();
			ObjectNode var = (ObjectNode) JsonUtil.toJsonNode(varStr);
			String name = var.get("name").asText();
			String value = var.get("val").asText();
			if (StringUtil.isEmpty(name)) {
				throw new Exception("流程变量的变量名不能为空");
			}
		}
		return varMap;
	}
	

	
	@Override
	public List<BpmTaskOpinion> getHistoryOpinion(String procInstId,String taskId) throws Exception {
		
		// 流程key和定义id二选一。
		if (StringUtil.isEmpty(procInstId) && StringUtil.isEmpty(taskId)) {
			throw new RequiredException(ResponseErrorEnums.REQUIRED_ERROR.getMessage() + ":procInstId和taskId必须填写一个!");
		}

		if ((procInstId == null || procInstId.isEmpty()) && taskId != null) {
			BpmTask bpmTask = bpmTaskService.getByTaskId(taskId);
			if(BeanUtils.isEmpty(bpmTask)){
				throw new RuntimeException("任务不存在或已经被处理！");
			}
			procInstId = bpmTaskService.getByTaskId(taskId).getProcInstId();
		}
		List<BpmTaskOpinion> list = bpmOpinionService.getTaskOpinions(procInstId);
		return list;
		
	}
	
	@Override
	public CommonResult<String> getBusinessKey(String procInstanceId,String taskId) throws NullPointerException {
		if ((StringUtil.isEmpty(procInstanceId)) && StringUtil.isNotEmpty(taskId)) {
			procInstanceId = bpmTaskService.getByTaskId(taskId).getProcInstId();
		}
		DefaultBpmProcessInstance instance = bpmProcessInstanceManager.get(procInstanceId);
		if(BeanUtils.isEmpty(instance)){
			throw new RuntimeException("根据输入的信息没有找到流程实例数据！");
		}
		return new CommonResult<String>(true, "业务主键--"+instance.getBizKey(), instance.getBizKey());
	}

	/**
	 * 根据BussinessKey获取流程实例ID
	 * @param businessKey 
	 * @return
	 */
	@Override
	public CommonResult<String> getProcInstId(String businessKey) throws NullPointerException {
		BpmProcessInstance instance= bpmProcessInstanceManager.getByBusinessKey(businessKey);
		if(BeanUtils.isEmpty(instance)){
			throw new NullPointerException("根据输入的businessKey找不到对应的实例数据！");
		}
		return new CommonResult<String>(true,  "流程实例id--"+instance.getId(),instance.getId());
	}
	
	@Override
	@Transactional
	public CommonResult<String> doEndProcess(DoEndParamObject doEndParamObject) throws Exception {
		// 传入的账户
		String account = doEndParamObject.getAccount();
		if (StringUtil.isEmpty(account)) {
			throw new RequiredException(ResponseErrorEnums.REQUIRED_ERROR.getMessage() + ":用户帐号必填");
		}
		String taskId = doEndParamObject.getTaskId();
		if (StringUtil.isEmpty(taskId)) {
			throw new RequiredException(ResponseErrorEnums.REQUIRED_ERROR.getMessage() + ":任务id必填");
		}
		String endReason = doEndParamObject.getEndReason();
		if (StringUtil.isEmpty(endReason)) {
			throw new RequiredException(ResponseErrorEnums.REQUIRED_ERROR.getMessage() + ":终止理由必填");
		}
		// 消息通知类型：inner内部消息，mail邮件，sms短信,多个之单使用英文逗号隔开
		String messageType = StringUtil.isNotEmpty(doEndParamObject.getMessageType()) ? doEndParamObject.getMessageType()
				: "mail";
		String files = doEndParamObject.getFiles();
		if(BeanUtils.isEmpty(bpmTaskManager.get(taskId))){
			throw new RuntimeException("任务不存在或已被处理！");
		}
		IUser user = ServiceUtil.getUserByAccount(account);
		if(BeanUtils.isEmpty(user)){
			throw new RuntimeException("根据输入的用户帐号，找不到用户！");
		}
		bpmTaskManager.endProcessByTaskId(taskId, messageType, endReason,files);

		return new CommonResult<String>(true, "已终止流程", "");
	}

	@Override
	public List<String> getApprovalItems(String taskId) throws Exception {
		if(StringUtil.isEmpty(taskId)){
			throw new RequiredException(ResponseErrorEnums.REQUIRED_ERROR.getMessage()+":任务id必填");
		}
		DefaultBpmTask task = bpmTaskManager.get(taskId);
		if(BeanUtils.isEmpty(task)){
			throw new RuntimeException("任务不存在或已被处理！");
		}
		BpmProcessInstance bpmProcessInstance = bpmInstService.getProcessInstance(task.getProcInstId());
		String defKey = bpmProcessInstance.getProcDefKey();
		String typeId = task.getTypeId();
		
		//mark 调接口
		List<String> approvalItem  = new ArrayList<String>();
		return approvalItem;
	}

	@Override
	public List<BpmIdentityResult> getNodeUsers(String taskId) throws Exception {
		if(StringUtil.isEmpty(taskId)){
			throw new RequiredException(ResponseErrorEnums.REQUIRED_ERROR.getMessage()+":任务id必填");
		}
		DefaultBpmTask task = bpmTaskManager.get(taskId);
		if(BeanUtils.isEmpty(task)){
			throw new RuntimeException("任务不存在或已被处理！");
		}
		BpmProcessInstance bpmProcessInstance = bpmInstService.getProcessInstance(task.getProcInstId());
		String instanceId = bpmProcessInstance.getId();//流程实例id
		String nodeId = task.getNodeId();//任务节点id
		List<BpmTaskOpinion> opinionList = bpmOpinionService.getByInstNodeId(instanceId, nodeId);
		List<BpmIdentityResult> result = new ArrayList<BpmIdentityResult>();
		for(BpmTaskOpinion b : opinionList){
			String qualfieds = b.getQualfieds();
			if(b.getCompleteTime()!=null){
				continue;
			}
			result.addAll(qualfields2BpmIdentityResult(qualfieds));
		}
		BeanUtils.removeDuplicate(result);
		return result;
	}
	
	private List<BpmIdentityResult> qualfields2BpmIdentityResult(String qualfieds) throws Exception{
		List<BpmIdentityResult> result = new ArrayList<BpmIdentityResult>();
		if(StringUtil.isEmpty(qualfieds)){
			return result;
		}
		JsonNode parse = JsonUtil.toJsonNode(qualfieds);
		if(BeanUtils.isNotEmpty(parse) && parse.isArray()){
			ArrayNode ArrayNode = (ArrayNode) parse;
			for (JsonNode jsonNode : ArrayNode) {
				if(BeanUtils.isEmpty(jsonNode) || !jsonNode.isObject()){
					continue;
				}
				ObjectNode jobject = (ObjectNode) jsonNode;
				JsonNode typeObj = jobject.get("type");
				if(BeanUtils.isNotEmpty(jobject.get("type")) && jobject.get("type").isTextual()){
					if("user".equals(jobject.get("type").asText())){
						String id = jobject.get("id").asText();
						if(BeanUtils.isNotEmpty(id)){
							IUser user = ServiceUtil.getUserById(id);
							result.add(new BpmIdentityResult(user));
						}
					}
				}
			}
		}
		return result;
	}

	@Override
	public List<BpmCheckOpinionVo> getProcessOpinionByActInstId(String actTaskId) throws Exception {
		if(StringUtil.isEmpty(actTaskId)){
			throw new RequiredException(ResponseErrorEnums.REQUIRED_ERROR.getMessage()+":actTaskId必填！");
		}
		BpmProcessInstance process = bpmProcessInstanceManager.getByBpmnInstId(actTaskId);
		if(BeanUtils.isEmpty(process)){
			throw new RuntimeException("根据sctTaskId找不到对应的数据！");
		}
		List<DefaultBpmCheckOpinion> bpmCheckOpinionList = bpmCheckOpinionManager.getByInstId(process.getId());
		List<BpmCheckOpinionVo> checkOpinionVoList = new ArrayList<BpmCheckOpinionVo>();
		for(DefaultBpmCheckOpinion o : bpmCheckOpinionList){
			o.setQualfieds("");
			JsonNode object = JsonUtil.toJsonNode(o);
			BpmCheckOpinionVo b = JsonUtil.toBean(object, BpmCheckOpinionVo.class);
			checkOpinionVoList.add(b);
		}
		return checkOpinionVoList;
	}

	@Override
	public BpmProcessInstance getProcessRunByTaskId(String taskId) throws Exception {
		if(StringUtil.isEmpty(taskId)){
			throw new RequiredException(ResponseErrorEnums.REQUIRED_ERROR.getMessage()+":任务id必填");
		}
		DefaultBpmTask task = bpmTaskManager.get(taskId);
		if(BeanUtils.isEmpty(task)){
			throw new RuntimeException("任务不存在或已被处理！");
		}
		BpmProcessInstance bpmProcessInstance = bpmInstService.getProcessInstance(task.getProcInstId());
		return bpmProcessInstance;
	}

	@Override
	public String getStatusByRunidNodeId(String instId, String nodeId) throws Exception {
		if(StringUtil.isEmpty(instId)){
			throw new RequiredException(ResponseErrorEnums.REQUIRED_ERROR.getMessage()+"：instId流程实例id必填！");
		}
		if(StringUtil.isEmpty(nodeId)){
			throw new RequiredException(ResponseErrorEnums.REQUIRED_ERROR.getMessage()+"：nodeId对应节点id必填！");
		}
		DefaultBpmProStatus proStatus = bpmProStatusManager.getByInstNodeId(instId, nodeId);
		if(BeanUtils.isEmpty(proStatus)){
			throw new RuntimeException("未获取到该实例该节点的状态！");
		}
		String status = "";
		if(NodeStatus.SUBMIT.getKey().equals(proStatus.getStatus())){
			status = proStatus.getStatus() + "——"+NodeStatus.SUBMIT.getValue();
		}
		if(NodeStatus.RE_SUBMIT.getKey().equals(proStatus.getStatus())){
			status = proStatus.getStatus() + "——"+NodeStatus.RE_SUBMIT.getValue();
		}
		if(NodeStatus.AGREE.getKey().equals(proStatus.getStatus())){
			status = proStatus.getStatus() + "——"+NodeStatus.AGREE.getValue();
		}
		if(NodeStatus.PENDING.getKey().equals(proStatus.getStatus())){
			status = proStatus.getStatus() + "——"+NodeStatus.PENDING.getValue();
		}
		if(NodeStatus.OPPOSE.getKey().equals(proStatus.getStatus())){
			status = proStatus.getStatus() + "——"+NodeStatus.OPPOSE.getValue();
		}
		if(NodeStatus.BACK.getKey().equals(proStatus.getStatus())){
			status = proStatus.getStatus() + "——"+NodeStatus.BACK.getValue();
		}
		if(NodeStatus.BACK_TO_START.getKey().equals(proStatus.getStatus())){
			status = proStatus.getStatus() + "——"+NodeStatus.BACK_TO_START.getValue();
		}
		if(NodeStatus.COMPLETE.getKey().equals(proStatus.getStatus())){
			status = proStatus.getStatus() + "——"+NodeStatus.COMPLETE.getValue();
		}
		if(NodeStatus.RECOVER.getKey().equals(proStatus.getStatus())){
			status = proStatus.getStatus() + "——"+NodeStatus.RECOVER.getValue();
		}
		if(NodeStatus.RECOVER_TO_START.getKey().equals(proStatus.getStatus())){
			status = proStatus.getStatus() + "——"+NodeStatus.RECOVER_TO_START.getValue();
		}
		if(NodeStatus.SIGN_PASS.getKey().equals(proStatus.getStatus())){
			status = proStatus.getStatus() + "——"+NodeStatus.SIGN_PASS.getValue();
		}
		if(NodeStatus.SIGN_NO_PASS.getKey().equals(proStatus.getStatus())){
			status = proStatus.getStatus() + "——"+NodeStatus.SIGN_NO_PASS.getValue();
		}
		if(NodeStatus.MANUAL_END.getKey().equals(proStatus.getStatus())){
			status = proStatus.getStatus() + "——"+NodeStatus.MANUAL_END.getValue();
		}
		if(NodeStatus.ABANDON.getKey().equals(proStatus.getStatus())){
			status = proStatus.getStatus() + "——"+NodeStatus.ABANDON.getValue();
		}
		if(NodeStatus.SUSPEND.getKey().equals(proStatus.getStatus())){
			status = proStatus.getStatus() + "——"+NodeStatus.SUSPEND.getValue();
		}
		return status;
	}

	@Override
	public BpmTaskResult getTaskByTaskId(String taskId) throws Exception {
		if(StringUtil.isEmpty(taskId)){
			throw new RequiredException(ResponseErrorEnums.REQUIRED_ERROR.getMessage()+":任务id必填");
		}
		DefaultBpmTask task = bpmTaskManager.get(taskId);
		if(BeanUtils.isEmpty(task)){
			throw new RuntimeException("任务不存在或已被处理！");
		}
		return new BpmTaskResult(task);
	}

	@Override
	public String getTaskNameByTaskId(String taskId) throws Exception {
		BpmTaskResult taskByTaskId = getTaskByTaskId(taskId);
		return taskByTaskId.getTaskName();
	}

	@Override
	public DefaultBpmProcessInstance getInstancetByBusinessKey(String businessKey) throws Exception {
		if(StringUtil.isEmpty(businessKey)){
			throw new RequiredException(ResponseErrorEnums.REQUIRED_ERROR.getMessage()+"：businessKey必填");
		}
		DefaultBpmProcessInstance instance = bpmProcessInstanceManager.getByBusinessKey(businessKey);
		
		if(BeanUtils.isEmpty(instance)){
			instance = this.getInstanceFromBusLink(businessKey,null);
			if(BeanUtils.isEmpty(instance)){
				throw new RuntimeException("根据【"+businessKey+"】没有找到对应的数据");
			}
		}
		return instance;
	}
	
	@Override
	public DefaultBpmProcessInstance getInstancetByBizKeySysCode(String businessKey,String sysCode) throws Exception {
		if(StringUtil.isEmpty(businessKey)){
			throw new RequiredException(ResponseErrorEnums.REQUIRED_ERROR.getMessage()+"：businessKey必填");
		}
		if(StringUtil.isEmpty(sysCode)){
			return this.getInstancetByBusinessKey(businessKey);
		}
		QueryFilter queryFilter = QueryFilter.<DefaultBpmProcessInstance>build().withDefaultPage();
		queryFilter.withParam("biz_key_", businessKey);
		queryFilter.withParam("sys_code_", sysCode);
		PageList<DefaultBpmProcessInstance> pageList =  bpmProcessInstanceManager.query(queryFilter);
		if(BeanUtils.isEmpty(pageList)||pageList.getRows().size()==0){
			DefaultBpmProcessInstance processInstance = getInstanceFromBusLink(businessKey, sysCode);
			if(BeanUtils.isEmpty(processInstance)){
				throw new RuntimeException("根据businessKey【"+businessKey+"】、sysCode【"+sysCode+"】没有找到对应的数据");
			}
			return processInstance;
		}else{
			if(pageList.getRows().size()>1){
				throw new RuntimeException("根据businessKey【"+businessKey+"】、sysCode【"+sysCode+"】找到了多（"+pageList.getTotal()+"）条对应的数据");
			}else{
				return pageList.getRows().get(0);
			}
		}
	}
	
	/**
	 * 通过businessKey在BpmBusLink中查询流程实例
	 * @param businessKey
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private DefaultBpmProcessInstance getInstanceFromBusLink(String businessKey,String sysCode){
		DefaultBpmProcessInstance processInstance = null;
		if(StringUtil.isNotEmpty(businessKey)){
			QueryFilter queryFilter = QueryFilter.<BpmBusLink>build().withDefaultPage();
			queryFilter.withParam("businesskey_str_", businessKey);
			if(StringUtil.isNotEmpty(sysCode)){
				queryFilter.withParam("sys_code_", sysCode);
			}
			PageList<BpmBusLink> pageList =  bpmBusLinkManager.query(queryFilter);
			if(BeanUtils.isEmpty(pageList.getRows())&&StringValidator.isNumberic(businessKey)){
				QueryFilter queryFilter2 = QueryFilter.<BpmBusLink>build().withDefaultPage();
				queryFilter2.withParam("businesskey_", businessKey);
				if(StringUtil.isNotEmpty(sysCode)){
					queryFilter2.withParam("sys_code_", sysCode);
				}
				pageList = bpmBusLinkManager.query(queryFilter2);
			}
			if(BeanUtils.isNotEmpty(pageList.getRows())){
				List<DefaultBpmProcessInstance> proInsts = new ArrayList<DefaultBpmProcessInstance>();
				for (BpmBusLink bpmBusLink : pageList.getRows()) {
					DefaultBpmProcessInstance instance = bpmProcessInstanceManager.get(bpmBusLink.getProcInstId());
					if(BeanUtils.isNotEmpty(instance)){
						proInsts.add(instance);
					}
				}
				HashSet h = new HashSet(proInsts);
				proInsts.clear();
				proInsts.addAll(h);
				if(proInsts.size()>1){
					throw new RuntimeException("根据businessKey【"+businessKey+"】、sysCode【"+sysCode+"】找到了多（"+proInsts.size()+"）条对应的数据");
				}else{
					if(BeanUtils.isNotEmpty(proInsts)){
						processInstance = proInsts.get(0);
					}
				}
			}
		}
		return processInstance;
	}
	

	@Override
	public DefaultBpmProcessInstance getInstanceByInstId(String instId) throws Exception {
		if(StringUtil.isEmpty(instId)){
			throw new RequiredException(ResponseErrorEnums.REQUIRED_ERROR.getMessage()+"：instId流程实例id必填");
		}
		DefaultBpmProcessInstance instance = bpmProcessInstanceManager.get(instId);

		if(BeanUtils.isEmpty(instance)){
			throw new RuntimeException("根据【"+instId+"】没有找到对应的数据");
		}
		//是否显示表单修改记录
		BpmDefinition bpmDefinition = bpmDefinitionService.getBpmDefinitionByDefId(instance.getProcDefId());
		if(BeanUtils.isNotEmpty(bpmDefinition)) {
			int showModifyRecord = bpmDefinition.getShowModifyRecord();
			if(showModifyRecord == 1) {
				instance.setShowModifyRecord(true);
			}
		}
		//表单formKey
		BpmFormService bpmFormService = BpmFormFactory.getFormService(FormType.PC);
		FormModel formModel = bpmFormService.getByDefId(instance.getProcDefId(), "", instance, true);
		if(BeanUtils.isNotEmpty(formModel)) {
			instance.setBpmFormKey(formModel.getFormKey());
		}
		
		return instance;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageList<DefaultBpmProcessInstance> getInstanceListByXml(String xml) throws Exception {
		Document doc = Dom4jUtil.loadXml(xml);
		Element root = doc.getRootElement();
		String account = root.attributeValue("account"); // 用户账号
		String subject = root.attributeValue("subject");// 任务标题
		String status = root.attributeValue("status");// 流程实例状态
		String pageSize = root.attributeValue("pageSize");// 页数
		String currentPage = root.attributeValue("currentPage");// 当前页
		if(StringUtil.isNotEmpty(account)){
			IUser user = ServiceUtil.getUserByAccount(account);
			if(BeanUtils.isEmpty(user)){
				throw new RuntimeException("用户不存在");
			}
		}
		ContextUtil.setCurrentUserByAccount(account);
		QueryFilter queryFilter = QueryFilter.<DefaultBpmProcessInstance>build().withDefaultPage();
		// 设置分页
		PageBean page = new PageBean(1,20);
		if (StringUtil.isNotEmpty(pageSize)) {
			page.setPageSize(Integer.valueOf(pageSize));
		}
		if (StringUtil.isNotEmpty(currentPage)) {
			page.setPage(new Integer(currentPage));
		}
		queryFilter.setPageBean(page);
		if(StringUtil.isNotEmpty(status)){
			queryFilter.withQuery(new QueryField("status_",status));
		}
		if(StringUtil.isNotEmpty(subject)){
			queryFilter.withQuery(new QueryField("subject_",subject,QueryOP.LIKE));
		}
		PageList<DefaultBpmProcessInstance> list = (PageList<DefaultBpmProcessInstance>)bpmProcessInstanceManager.queryList(queryFilter);
		return list;
	}

	@Override
	public PageList<DefaultBpmTask> getTasksByInstId(String instId) throws Exception {
		if(StringUtil.isEmpty(instId)){
			throw new RequiredException(ResponseErrorEnums.REQUIRED_ERROR.getMessage()+"：instId实例id必填！");
		}
		PageList<DefaultBpmTask> taskList = new  PageList<>();
		taskList.setRows(bpmTaskManager.getByInstId(instId));
		return taskList;
	}

	@Override
	public List<BpmNodeDefVo> getTaskOutNodes(String taskId) throws Exception {
		DefaultBpmTask task = bpmTaskManager.get(taskId);
		if(BeanUtils.isEmpty(task)){
			throw new RuntimeException("任务不存在或已被处理！");
		}
		BpmProcessInstance bpmProcessInstance = bpmInstService.getProcessInstance(task.getProcInstId());
		String defId = bpmProcessInstance.getProcDefId();//流程定义id
		String nodeId = task.getNodeId();//任务节点id
		BpmNodeDef taskNodeDef = bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);
		List<BpmNodeDef> nodes = taskNodeDef.getOutcomeNodes();
		List<BpmNodeDef> nextNodes = handlerSelectOutcomeNodes(nodes);
		List<BpmNodeDefVo> array = new ArrayList<BpmNodeDefVo>();
		for(BpmNodeDef n : nextNodes){
			BpmNodeDefVo nodeDefVo = BpmNodeDefVo.parseVo(n);
			array.add(nodeDefVo);
		}
		return array;
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
	public String getDetailUrl(String taskId) throws Exception {
		ApplicationContext  applicationContext = AppUtil.getApplicaitonContext();
		String applicationName = applicationContext.getApplicationName();
		DefaultBpmTask task = bpmTaskManager.get(taskId);
		if(BeanUtils.isEmpty(task)){
			throw new RuntimeException("任务不存在或已被处理！");
		}
		BpmProcessInstance bpmProcessInstance = bpmInstService.getProcessInstance(task.getProcInstId());
		if (!ActionCmd.DATA_MODE_BO.equals(bpmProcessInstance.getDataMode())){
			throw new RuntimeException("该任务对应的节点不是在线表单！");
		}
		String url = applicationName + "/flow/instance/instanceFlowForm?id="+bpmProcessInstance.getId();
		return url;
	}

	@Override
	@Transactional
	public CommonResult<String> setTaskVar(String taskId, Map<String, Object> variables) throws Exception {
		natTaskService.setVariables(taskId, variables);
		return new CommonResult<String>(true, "设置成功", "");
	}

	@Override
	@Transactional
	public CommonResult<String> setTaskVarLocal(String taskId, Map<String, Object> variables) throws Exception {
		natTaskService.setVariablesLocal(taskId, variables);
		return new CommonResult<String>(true, "设置成功", "");
	}


	@Override
	@Transactional
	public CommonResult<String> forbiddenInstance(String instId) throws Exception {
		processInstanceService.suspendProcessInstance(instId);
		return new CommonResult<String>(true, "流程实例挂起成功", "");
	}


	@Override
	@Transactional
	public CommonResult<String> unForbiddenInstance(String instId) throws Exception {
		processInstanceService.recoverProcessInstance(instId);
		return new CommonResult<String>(true, "流程实例取消挂起成功", "");
	}

	@Override
	public List<BpmProcessInstance> getBpmProcessByParentIdAndSuperNodeId(
			String parentInstId, String superNodeId) throws Exception {
		return bpmProcessInstanceManager.getBpmProcessByParentIdAndSuperNodeId(parentInstId, superNodeId);
	}
	
	@Override
	public List<DefaultBpmProcessInstance> getInstancesByParentId(
			String parentInstId, String status) throws Exception {
		QueryFilter queryFilter = QueryFilter.<DefaultBpmProcessInstance>build().withDefaultPage(); 
		queryFilter.withParam("parent_inst_id_", parentInstId);
		if(StringUtil.isNotEmpty(status)){
			queryFilter.withParam("status_", status);
		}
		return bpmProcessInstanceManager.queryList(queryFilter);
	}


	@Override
	public List<DefaultBpmProcessInstance> getInstancesByDefId(String defId,
			String status) throws Exception {
		QueryFilter queryFilter = QueryFilter.<DefaultBpmProcessInstance>build().withDefaultPage(); 
		queryFilter.withParam("proc_def_id_", defId);
		if(StringUtil.isNotEmpty(status)){
			queryFilter.withParam("status_", status);
		}
		return bpmProcessInstanceManager.queryList(queryFilter);
	}


	@Override
	public BpmProcessInstance getTopBpmProcessInstance(String instId)
			throws Exception {
		return bpmProcessInstanceManager.getTopBpmProcessInstance(instId);
	}


	@Override
	public String getBpmImage(BpmImageParamObject bpmImageParamObject)
			throws Exception {
		DiagramService diagramService = (DiagramService) AppUtil.getBean("diagramService");
		BpmInstService instService = AppUtil.getBean(BpmInstService.class);
		InputStream is = null;
		//处理流程实例为草稿状态时根据流程定义获取流程图
		if(StringUtil.isNotEmpty(bpmImageParamObject.getProcInstId())){
			BpmProcessInstance instance = instService.getProcessInstance(bpmImageParamObject.getProcInstId());
			if(BeanUtils.isNotEmpty(instance)&&ProcessInstanceStatus.STATUS_DRAFT.getKey().equals(instance.getStatus())){
				bpmImageParamObject.setDefId(instance.getProcDefId());
			}
		}
		if (StringUtils.isNotEmpty(bpmImageParamObject.getDefId())) {
			is = diagramService.getDiagramByBpmnDefId(bpmImageParamObject.getDefId());
		} else if (StringUtils.isNotEmpty(bpmImageParamObject.getBpmnInstId())) {
			is = getDiagramByInstance(diagramService, bpmImageParamObject.getBpmnInstId());
		} else if (StringUtils.isNotEmpty(bpmImageParamObject.getTaskId())) {
			BpmTaskService bpmTaskService = (BpmTaskService) AppUtil .getBean("defaultBpmTaskService");
			BpmTask bpmTask = bpmTaskService.getByTaskId(bpmImageParamObject.getTaskId());
			is = getDiagramByInstance(diagramService,  bpmTask.getBpmnInstId());
		} else if (StringUtils.isNotEmpty(bpmImageParamObject.getProcInstId())){
			BpmProcessInstance instance = instService.getProcessInstance(bpmImageParamObject.getProcInstId());
			is = getDiagramByInstance(diagramService,  instance.getBpmnInstId());
		}

		if (is == null)
			return "";
		byte[] data = null;
		try {
			data = new byte[is.available()];
			is.read(data);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "data:image/png;base64,"+new String(org.apache.commons.codec.binary.Base64.encodeBase64(data));
	}
	
	private InputStream getDiagramByInstance(DiagramService diagramService, String bpmnInstId) {
		BpmInstService bpmInstService = AppUtil.getBean(BpmInstService.class);
		BpmProcessInstanceManager bpmProcessInstanceManager = AppUtil.getBean(BpmProcessInstanceManager.class);
		BpmProcessInstance bpmProcessInstance = bpmInstService
				.getProcessInstanceByBpmnInstId(bpmnInstId);
		if(bpmProcessInstance==null)
			
			bpmProcessInstance=bpmProcessInstanceManager.getBpmProcessInstanceHistoryByBpmnInstId(bpmnInstId);
		Map<String, String> colorMap = bpmProStatusManager
				.getProcessInstanceStatus(bpmProcessInstance.getId());
		return diagramService.getDiagramByDefId(
				bpmProcessInstance.getProcDefId(), colorMap);
	}


	@Override
	public CommonResult<String> setDefOtherParam(DefOtherParam defOtherParam) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

    //沟通反馈
    @Override
    @Transactional
    public CommonResult<String> doNextcommu(DoNextParamObject doNextParamObject) throws Exception{
	    //通过知会任务ID查询沟通知会任务是否被反馈
        BpmTaskNoticeManager noticeManager = AppUtil.getBean(BpmTaskNoticeManager.class);
        BpmTaskNotice bpmTaskNotice = noticeManager.get(doNextParamObject.getTaskId());
        if(BeanUtils.isEmpty(bpmTaskNotice)) {
            return new CommonResult<String>(false, "该沟通已反馈");
        }
        String opinion=doNextParamObject.getOpinion();
        //通讯接收人。
        //BpmTaskCommu taskCommu=bpmTaskCommuManager.getByInstId(doNextParamObject.getInstId());
        //BpmCommuReceiver commuReceiver = bpmCommuReceiverManager.getByCommuId(taskCommu.getId());

        //commuReceiver.setStatus(BpmCommuReceiver.COMMU_FEEDBACK);
        //
        //if(commuReceiver.getReceiveTime()==null){
        //    commuReceiver.setReceiveTime(LocalDateTime.now());
        //}
        //commuReceiver.setFeedbackTime(LocalDateTime.now());
        //
        //commuReceiver.setOpinion(opinion);
        //
        //bpmCommuReceiverManager.update(commuReceiver);
        //将沟通反馈信息加入审批历史
        updOpinionComplete(doNextParamObject.getInstId(), doNextParamObject.getTaskId(),OpinionStatus.FEEDBACK,ContextUtil.getCurrentUserId(), opinion,doNextParamObject.getFiles(),doNextParamObject.getZfiles());
        //处理沟通代表知会任务 删除知会任务记录
        noticeManager.removeByIds(bpmTaskNotice.getId());
        /**
         * 发送通知。
         */
		/*IUser userByAccount = ServiceUtil.getUserById(taskCommu.getSenderId());
		List<IUser> userarr=new ArrayList<IUser>();
		userarr.add(userByAccount);
		MessageUtil.sendMsgnew(doNextParamObject.getNotifyType(), userarr, TemplateConstants.TYPE_KEY.BPM_COMMU_FEEDBACK.toString(), opinion);
		*/
        return new CommonResult<String>(true, "反馈成功");
    }

    private void updOpinionComplete(String instId,String noticeId,OpinionStatus opinionStatus, String commuUser, String opinion,String files,String zFiles){
	    //根据用户ID获取用户信息
        IUser user = userServiceImpl.getUserById(commuUser);
        //根据实例ID，审批状态，带执行人查询审批历史信息
        DefaultBpmCheckOpinion checkOpinion = bpmCheckOpinionManager.getByTeam(instId,noticeId,OpinionStatus.AWAITING_FEEDBACK,user.getFullname());

        checkOpinion.setFiles(files);
        checkOpinion.setZfiles(zFiles);
        checkOpinion.setCompleteTime(LocalDateTime.now());
        long durMs =TimeUtil.getTime (LocalDateTime.now(),checkOpinion.getCreateTime());
        checkOpinion.setDurMs(durMs);
        checkOpinion.setAuditor(user.getUserId());
        checkOpinion.setAuditorName(user.getFullname());
        checkOpinion.setStatus(opinionStatus.getKey());;
        checkOpinion.setOpinion(opinion);
        bpmCheckOpinionManager.update(checkOpinion);
    }

	@Override
	public Map<String, Object> getUrgentStateConf(ObjectNode obj) throws Exception {
		String defId =obj.hasNonNull("defId") ? obj.get("defId").asText():"";
		String taskId =obj.hasNonNull("taskId") ? obj.get("taskId").asText():"";
		String instId =obj.hasNonNull("instId") ? obj.get("instId").asText():"";
		boolean getConf =obj.hasNonNull("getConf") ? obj.get("getConf").asBoolean():false;
		String nodeId ="";
		Map<String, Object>  result = new HashMap<>();
		if (StringUtil.isNotEmpty(taskId)) {
			 DefaultBpmTask defaultBpmTask = bpmTaskManager.get(obj.get("taskId").asText());
			 if (BeanUtils.isNotEmpty(defaultBpmTask)) {
				 nodeId = defaultBpmTask.getNodeId();
				 instId =defaultBpmTask.getProcInstId();
				 defId =defaultBpmTask.getProcDefId();
			 }
			
		}else if (StringUtil.isNotEmpty(instId)) {
			 DefaultBpmProcessInstance instance = bpmProcessInstanceManager.get(instId);
			 if (BeanUtils.isNotEmpty(instance)) {
				 defId = instance.getProcDefId();
				 result.put("value", instance.getUrgentState());
			 }
		}

		DefaultBpmDefinition defaultBpmDefinition = bpmDefinitionManager.getById(defId);
		if ((BeanUtils.isEmpty(defaultBpmDefinition) || defaultBpmDefinition.getShowUrgentState()==0) && !getConf) {
			return result;
		}
		PropertyService propertyService = AppUtil.getBean(PropertyService.class);
		String byAlias = propertyService.getProperty("urgentStateConf","");
		result.put("conf", byAlias);
		
		if (getConf) {
			return result;
		}
		if (!result.containsKey("value")) {
			DefaultBpmProcessInstance instance = bpmProcessInstanceManager.get(instId);
			 if (BeanUtils.isNotEmpty(instance)) {
				 result.put("value", instance.getUrgentState());
			 }
		}
		if (StringUtil.isNotEmpty(nodeId)) {
			BpmNodeDef bpmNodeDef = bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);
			if (BeanUtils.isNotEmpty(bpmNodeDef) && bpmNodeDef.getNodeProperties().size()>0 && bpmNodeDef.getNodeProperties().get(0).isAllowEditUrgentState()) {
				result.put("right", "w");
			}
		}
		if (!result.containsKey("value")) {
			result.put("right", "w");
		}
		return result;
	}

    @Override
    @Transactional
    public CommonResult<String> doNextCopyto(DoNextParamObject doNextParamObject) throws Exception {
        //根据用户ID获取用户信息
        IUser user = userServiceImpl.getUserById(ContextUtil.getCurrentUserId());
        List<BpmIdentity> identityList = new ArrayList<BpmIdentity>();
        BpmIdentity bpmIdentity = new DefaultBpmIdentity();
        bpmIdentity.setType(IdentityType.USER);
        bpmIdentity.setId(user.getUserId());
        bpmIdentity.setName(user.getFullname());
        identityList.add(bpmIdentity);
        //根据流程实例ID查询流程实例
        DefaultBpmProcessInstance def=bpmProcessInstanceMapper.get(doNextParamObject.getInstId());
        String bpmnInstId = def.getBpmnInstId();
        //根据BPM流程实例ID和流程变量名称获取上级变量数据
        String superInstId = "";
        if(!OpinionStatus.END.getKey().equals(def.getStatus())){
            superInstId = (String) natProInstanceService.getSuperVariable(bpmnInstId, BpmConstants.PROCESS_INST_ID);
        }
        //将传阅回复信息加入审批历史
        DefaultBpmCheckOpinion checkOpinion = new DefaultBpmCheckOpinion();
        checkOpinion.setId(UniqueIdUtil.getSuid());
        checkOpinion.setProcDefId(def.getBpmnDefId());
        checkOpinion.setSupInstId(superInstId);
        checkOpinion.setProcInstId(doNextParamObject.getInstId());
        checkOpinion.setTaskId("");
        checkOpinion.setTaskKey("");
        checkOpinion.setTaskName("传阅任务");
        checkOpinion.setFiles(doNextParamObject.getFiles());
        checkOpinion.setZfiles(doNextParamObject.getZfiles());
        checkOpinion.setStatus(OpinionStatus.COPYTO_REPLY.getKey());
        checkOpinion.setCreateTime(LocalDateTime.now());
        checkOpinion.setQualfieds(BpmCheckOpinionUtil.getIdentityIds(identityList));
        checkOpinion.setQualfiedNames(user.getFullname());
        checkOpinion.setOpinion(doNextParamObject.getOpinion());
        checkOpinion.setCompleteTime(LocalDateTime.now());
        long durMs =TimeUtil.getTime (LocalDateTime.now(),checkOpinion.getCreateTime());
        checkOpinion.setDurMs(durMs);
        checkOpinion.setAuditor(user.getUserId());
        checkOpinion.setAuditorName(user.getFullname());
        checkOpinion.setIsRead(1);
        bpmCheckOpinionManager.create(checkOpinion);
        return new CommonResult<String>(true, "回复成功");
    }
}
