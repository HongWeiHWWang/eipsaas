package com.hotent.runtime.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.context.BaseContext;
import com.hotent.base.controller.BaseController;
import com.hotent.base.exception.WorkFlowException;
import com.hotent.base.feign.BpmModelFeignService;
import com.hotent.base.feign.FormFeignService;
import com.hotent.base.feign.PortalFeignService;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.jms.Notice;
import com.hotent.base.model.CommonResult;
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
import com.hotent.base.util.FeignServiceUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.SQLUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.time.DateUtil;
import com.hotent.base.util.time.TimeUtil;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.constant.InterPoseType;
import com.hotent.bpm.api.constant.NodeType;
import com.hotent.bpm.api.constant.OpinionStatus;
import com.hotent.bpm.api.constant.ProcessInstanceStatus;
import com.hotent.bpm.api.helper.identity.BpmIdentityExtractService;
import com.hotent.bpm.api.model.form.FormType;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.def.BpmDefExtProperties;
import com.hotent.bpm.api.model.process.def.BpmDefinition;
import com.hotent.bpm.api.model.process.def.BpmProcessDef;
import com.hotent.bpm.api.model.process.def.BpmProcessDefExt;
import com.hotent.bpm.api.model.process.def.BpmVariableDef;
import com.hotent.bpm.api.model.process.inst.BpmInstanceTrack;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.UserTaskNodeDef;
import com.hotent.bpm.api.model.process.task.BpmTask;
import com.hotent.bpm.api.model.process.task.BpmTaskOpinion;
import com.hotent.bpm.api.model.process.task.SkipResult;
import com.hotent.bpm.api.service.BoDataService;
import com.hotent.bpm.api.service.BoSubDataHandlers;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.api.service.BpmFormService;
import com.hotent.bpm.api.service.BpmIdentityService;
import com.hotent.bpm.api.service.BpmInstService;
import com.hotent.bpm.api.service.BpmOpinionService;
import com.hotent.bpm.api.service.BpmTaskService;
import com.hotent.bpm.engine.form.BpmFormFactory;
import com.hotent.bpm.engine.inst.DefaultProcessInstCmd;
import com.hotent.bpm.listener.BusDataUtil;
import com.hotent.bpm.model.form.FormModel;
import com.hotent.bpm.model.identity.DefaultBpmIdentity;
import com.hotent.bpm.persistence.manager.BpmBusLinkManager;
import com.hotent.bpm.persistence.manager.BpmCheckOpinionManager;
import com.hotent.bpm.persistence.manager.BpmCptoReceiverManager;
import com.hotent.bpm.persistence.manager.BpmDefActManager;
import com.hotent.bpm.persistence.manager.BpmDefAuthorizeManager;
import com.hotent.bpm.persistence.manager.BpmDefUserManager;
import com.hotent.bpm.persistence.manager.BpmDefinitionManager;
import com.hotent.bpm.persistence.manager.BpmInterposeRecoredManager;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.persistence.manager.BpmReadRecordManager;
import com.hotent.bpm.persistence.manager.BpmSecretaryManageManager;
import com.hotent.bpm.persistence.manager.BpmTaskCandidateManager;
import com.hotent.bpm.persistence.manager.BpmTaskManager;
import com.hotent.bpm.persistence.manager.BpmTaskNoticeManager;
import com.hotent.bpm.persistence.manager.BpmTaskTurnManager;
import com.hotent.bpm.persistence.manager.BpmTaskUrgentManager;
import com.hotent.bpm.persistence.manager.CopyToManager;
import com.hotent.bpm.persistence.model.BpmBusLink;
import com.hotent.bpm.persistence.model.BpmCptoReceiver;
import com.hotent.bpm.persistence.model.BpmDefAct;
import com.hotent.bpm.persistence.model.BpmDefAuthorizeType.BPMDEFAUTHORIZE_RIGHT_TYPE;
import com.hotent.bpm.persistence.model.BpmDefUser;
import com.hotent.bpm.persistence.model.BpmIdentityResult;
import com.hotent.bpm.persistence.model.BpmInterposeRecored;
import com.hotent.bpm.persistence.model.BpmReadRecord;
import com.hotent.bpm.persistence.model.BpmTaskNotice;
import com.hotent.bpm.persistence.model.BpmTaskNoticeDone;
import com.hotent.bpm.persistence.model.BpmTaskUrgent;
import com.hotent.bpm.persistence.model.CopyTo;
import com.hotent.bpm.persistence.model.DefaultBpmCheckOpinion;
import com.hotent.bpm.persistence.model.DefaultBpmDefinition;
import com.hotent.bpm.persistence.model.DefaultBpmProcessDefExt;
import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;
import com.hotent.bpm.persistence.model.DefaultBpmTask;
import com.hotent.bpm.persistence.model.DefaultBpmTaskCandidate;
import com.hotent.bpm.persistence.model.DefaultBpmTaskTurn;
import com.hotent.bpm.persistence.model.ResultMessage;
import com.hotent.bpm.persistence.model.TaskTurnAssign;
import com.hotent.bpm.persistence.util.BpmStackRelationUtil;
import com.hotent.bpm.persistence.util.BpmUtil;
import com.hotent.bpm.persistence.util.ServiceUtil;
import com.hotent.bpm.util.BoDataUtil;
import com.hotent.bpm.util.MessageUtil;
import com.hotent.runtime.manager.BpmTaskSignLineManager;
import com.hotent.runtime.manager.BpmTaskSignSequenceManager;
import com.hotent.runtime.manager.BpmTaskTransManager;
import com.hotent.runtime.manager.IFlowManager;
import com.hotent.runtime.manager.IProcessManager;
import com.hotent.runtime.model.RelatedInformation;
import com.hotent.runtime.model.Script;
import com.hotent.runtime.params.BpmCheckOpinionVo;
import com.hotent.runtime.params.BpmImageParamObject;
import com.hotent.runtime.params.BpmNodeDefVo;
import com.hotent.runtime.params.CopyToParam;
import com.hotent.runtime.params.CustomSignRevokeParam;
import com.hotent.runtime.params.DoEndParamObject;
import com.hotent.runtime.params.DoNextParamObject;
import com.hotent.runtime.params.FlowImageVo;
import com.hotent.runtime.params.FormAndBoVo;
import com.hotent.runtime.params.InfoboxVo;
import com.hotent.runtime.params.InstFormAndBoVo;
import com.hotent.runtime.params.NodeStatusVo;
import com.hotent.runtime.params.RevokeParamObject;
import com.hotent.runtime.params.RevokeSignLineParamObject;
import com.hotent.runtime.params.RevokeTransParamObject;
import com.hotent.runtime.params.SelectDestinationVo;
import com.hotent.runtime.params.StartCmdParam;
import com.hotent.runtime.params.StartFlowParamObject;
import com.hotent.runtime.params.StartResult;
import com.hotent.runtime.params.ToCopyToVo;
import com.hotent.runtime.service.RevokeHandler;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserService;

/**
 * 流程实例相关接口
 *
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */

@RestController
@RequestMapping("/runtime/instance/v1/")
@Api(tags = "流程实例管理")
@ApiGroup(group= {ApiGroupConsts.GROUP_BPM})
public class InstanceController extends BaseController<BpmProcessInstanceManager,DefaultBpmProcessInstance> {

	@Resource
	IFlowManager iFlowService;
	@Resource
	IProcessManager iProcessService;
	@Resource
    BpmInstService processInstanceService;
	@Resource
    BpmDefinitionAccessor bpmDefinitionAccessor;
	@Resource
	CopyToManager copyToManager;
	@Resource
    BpmTaskService bpmTaskService;
	@Resource
	BpmProcessInstanceManager bpmProcessInstanceManager;
	@Resource
    BpmOpinionService bpmOpinionService;
	@Resource
    BpmIdentityService bpmIdentityService;
	@Resource
	FormFeignService formRestfulService;
	@Resource
	BpmTaskTurnManager bpmTaskTurnManager;
	@Resource
	BpmTaskManager bpmTaskManager;
	@Resource
	BpmCptoReceiverManager bpmCptoReceiverManager;
	@Resource
	BpmDefinitionManager bpmDefinitionManager;
	@Resource
	BpmBusLinkManager bpmBusLinkManager;
	@Resource
	BoDataService boDataService;
	@Resource
	UCFeignService ucFeignService;
	@Resource
	BpmCheckOpinionManager bpmCheckOpinionManager;
	@Resource
	BpmDefUserManager bpmDefUserManager;
	@Resource
	BpmDefActManager bpmDefActManager;
	@Resource
	IUserService ius;
	@Resource
	BpmReadRecordManager bpmReadRecordManager;
	@Resource
	BpmTaskUrgentManager bpmTaskUrgentManager;
	@Resource
	BpmTaskTransManager bpmTaskTransManager;
	@Resource
	BpmTaskSignSequenceManager signSequenceManager;
	@Resource
	BpmTaskSignLineManager signLineManager;
    @Resource
    BoSubDataHandlers boSubDataHandler;
    @Resource
    RevokeHandler revokeHandler;
    @Resource
    BaseContext baseContext;

    @RequestMapping(value="getSubDataSqlByFk", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
    @ApiOperation(value = "根据外键获取子表数据sql", httpMethod = "POST", notes = "根据外键获取子表数据sql")
    public CommonResult<String> getSubDataSqlByFk(@ApiParam(name="boEnt",value="bo实体")@RequestBody ObjectNode boEnt,
                                                  @ApiParam(name="fkValue",value="外键值 ")@RequestParam Object fkValue,
                                                  @ApiParam(name="defId",value="定义id ")@RequestParam String defId,
                                                  @ApiParam(name="nodeId",value="节点id ")@RequestParam String nodeId,
                                                  @ApiParam(name="parentDefKey",value="父流程key ")@RequestParam String parentDefKey) throws Exception {
        return boSubDataHandler.getSubDataSqlByFk(boEnt,fkValue,defId,nodeId,parentDefKey);
    }

	@RequestMapping(value = "getDoneList", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户的已办事宜", httpMethod = "POST", notes = "获取用户的已办事宜，参数status表示流程状态，不填表示查询所有")
	public PageList<Map<String, Object>> getDoneList(
			@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter,
			@ApiParam(name = "status", value = "流程状态", allowableValues = "running,end,manualend,cancel,back,revoke,revokeToStart", required = false) @RequestParam(required = false) String status,
			@ApiParam(name = "isCheckRevoke", value = "是否检测可撤回状态") @RequestParam Optional<Boolean> isCheckRevoke,
			HttpServletResponse response) throws Exception {
		PageList<Map<String, Object>> pageList = iFlowService.getDoneList(baseContext.getCurrentUserAccout(), queryFilter, status);
		// 如果需要检测是否可以撤回
		if (isCheckRevoke.orElse(false)) {
			revokeHandler.checkRevoke(pageList);
		}
		return pageList;
	}

	@RequestMapping(value = "getDoneInstList", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户经办的流程实例", httpMethod = "POST", notes = "获取用户的已办事宜，参数status表示流程状态，不填表示查询所有")
	public PageList<Map<String, Object>> getDoneInstList(
			@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter,
			@ApiParam(name = "status", value = "流程状态", allowableValues = "running,end,manualend,cancel,back,revoke,revokeToStart", required = false) @RequestParam(required = false) String status,
			HttpServletResponse response) throws Exception {
		return iFlowService.getDoneInstList(baseContext.getCurrentUserAccout(), queryFilter, status);
	}

	@PostMapping(value = "getDoneInstCount", produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户经办的流程实例数", httpMethod = "POST", notes = "获取用户的已办事宜，参数status表示流程状态，不填表示查询所有")
	public List<Map<String, Object>> getDoneInstCount(
			@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter,
			@ApiParam(name = "status", value = "流程状态数", allowableValues = "running,end,manualend,cancel,back,revoke,revokeToStart", required = false) @RequestParam(required = false) String status,
			HttpServletResponse response) throws Exception {
		return iFlowService.getDoneInstCount(baseContext.getCurrentUserAccout(), queryFilter, status);
	}

	@RequestMapping(value = "getMobileDoneList", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户手机的已办事宜", httpMethod = "POST", notes = "获取用户手机的已办事宜，参数status表示流程状态，不填表示查询所有")
	public PageList<Map<String, Object>> getMobileDoneList(
			@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter,
			@ApiParam(name = "status", value = "流程状态", allowableValues = "running,end,manualend,cancel,back,revoke,revokeToStart", required = false) @RequestParam(required = false) String status,
			HttpServletResponse response) throws Exception {
//	    queryFilter.addFilter("wfInst.SUPPORT_MOBILE_", 1,QueryOP.EQUAL,FieldRelation.AND, "m");
		return iFlowService.getDoneList(baseContext.getCurrentUserAccout(), queryFilter, status);
	}

	@RequestMapping(value = "completed", method = RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取办结事宜", httpMethod = "POST", notes = "获取用户的办结事宜")
	public PageList<DefaultBpmProcessInstance> completed(
			@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter,
			HttpServletResponse response) throws Exception {
		return iFlowService.getCompletedList(baseContext.getCurrentUserAccout(), queryFilter);
	}

	@RequestMapping(value = "myCompleted", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户办结事宜", httpMethod = "POST", notes = "获取用户的办结事宜")
	public PageList<DefaultBpmProcessInstance> myCompleted(
			@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter,
			HttpServletResponse response) throws Exception {
		return iFlowService.getMyCompletedList(baseContext.getCurrentUserAccout(), queryFilter);
	}

	@RequestMapping(value = "getMyRequestList", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "我的请求", httpMethod = "POST", notes = "我的请求")
	public PageList<DefaultBpmProcessInstance> getMyRequestList(
			@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter,
			HttpServletResponse response) throws Exception {
		return iFlowService.getMyRequestList(baseContext.getCurrentUserAccout(), queryFilter);
	}

	@RequestMapping(value = "getMyDraftList", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获取我的草稿列表", httpMethod = "POST", notes = "获取我的草稿列表")
	public PageList<DefaultBpmProcessInstance> getMyDraftList(
			@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter,
			HttpServletResponse response) throws Exception {
		return iFlowService.getMyDraftList(baseContext.getCurrentUserAccout(), queryFilter);
	}

	@RequestMapping(value = "getReceiverCopyTo", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户的抄送转发数据", httpMethod = "POST", notes = "获取用户的抄送转发数据")
	public PageList<CopyTo> getReceiverCopyTo(
			@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter,
			@ApiParam(name = "type", value = "类型：copyto(抄送) trans(转发)", allowableValues = "copyto,trans", required = false) @RequestParam(required = false) String type,
			HttpServletResponse response) throws Exception {
		return iFlowService.getReceiverCopyTo(baseContext.getCurrentUserAccout(), queryFilter, type);
	}

	@RequestMapping(value = "getMobileReceiverCopyTo", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获取手机用户的抄送转发数据", httpMethod = "POST", notes = "获取手机用户的抄送转发数据")
	public PageList<CopyTo> getMobileReceiverCopyTo(
			@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter,
			@ApiParam(name = "type", value = "类型：copyto(抄送) trans(转发)", allowableValues = "copyto,trans", required = false) @RequestParam(required = false) String type,
			HttpServletResponse response) throws Exception {
		queryFilter.addFilter("support_mobile_", 1, QueryOP.EQUAL, FieldRelation.AND, "m");
		return iFlowService.getReceiverCopyTo(baseContext.getCurrentUserAccout(), queryFilter, type);
	}

	@RequestMapping(value = "myCopyTo", method = RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "由我发出的抄送", httpMethod = "POST", notes = "获取用户的抄送转发数据")
	public PageList<CopyTo> myCopyTo(
			@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter)
			throws Exception {
		return iFlowService.myCopyTo(baseContext.getCurrentUserAccout(), queryFilter);
	}

	@RequestMapping(value = "revokeInstance", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "根据实例id撤回流程（撤销）", httpMethod = "POST", notes = "根据实例id撤回流程（撤销）")
	public CommonResult<String> revokeInstance(
			@ApiParam(required = true, name = "revokeParamObject", value = "流程撤销对象") @RequestBody RevokeParamObject revokeParamObject)
			throws Exception {
		return iFlowService.revokeInstance(revokeParamObject);
	}

	@RequestMapping(value = "getProcessRunByTaskId", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "根据taskId获取对应的流程运行对象", notes = "根据taskId获取对应的流程运行对象", httpMethod = "GET")
	public BpmProcessInstance getProcessRunByTaskId(
			@ApiParam(name = "taskId", value = "任务id", required = true) @RequestParam String taskId) throws Exception {
		return iProcessService.getProcessRunByTaskId(taskId);
	}

	@RequestMapping(value = "getInstancetByBusinessKey", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "通过businessKey获取运行实例", notes = "通过businessKey获取运行实例", httpMethod = "GET")
	public BpmProcessInstance getInstancetByBusinessKey(
			@ApiParam(name = "businessKey", value = "业务主键", required = true) @RequestParam String businessKey)
			throws Exception {
		return iProcessService.getInstancetByBusinessKey(businessKey);
	}

	@RequestMapping(value = "getInstancetByBizKeySysCode", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "通过businessKey获取运行实例", notes = "通过businessKey获取运行实例", httpMethod = "GET")
	public BpmProcessInstance getInstancetByBizKeySysCode(
			@ApiParam(name = "businessKey", value = "业务主键", required = true) @RequestParam String businessKey,
			@ApiParam(name = "sysCode", value = "业务系统编码", required = true) @RequestParam String sysCode)
			throws Exception {
		return iProcessService.getInstancetByBizKeySysCode(businessKey, sysCode);
	}

	@RequestMapping(value = "getInstanceByInstId", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "根据实例id获取实例对象", notes = "根据实例id获取实例对象", httpMethod = "GET")
	public BpmProcessInstance getInstanceByInstId(
			@ApiParam(name = "instId", value = "实例id", required = true) @RequestParam String instId) throws Exception {
		return iProcessService.getInstanceByInstId(instId);
	}

	@RequestMapping(value = "getInstanceListByXml", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "根据xml获取实例列表", notes = "根据xml获取实例列表，xml的根元素包含属性account（用户帐号）、subject（标题）、<br/>status（流程状态，draft草稿，pending挂起，running运行中，manualend人工结束，revokeToStart撤销到发起人，back驳回，end结束）、<br/>"
			+ "pageSize（分页大小，不填默认20）、currentPage（当前页，不填默认第一页）", httpMethod = "GET")
	public PageList<DefaultBpmProcessInstance> getInstanceListByXml(
			@ApiParam(name = "xml", value = "xml", required = true) @RequestParam String xml) throws Exception {
		return iProcessService.getInstanceListByXml(xml);
	}

	@RequestMapping(value = "forbiddenInstance", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "通过流程实例id挂起流程实例", notes = "通过流程实例id挂起流程实例", httpMethod = "GET")
	public CommonResult<String> forbiddenInstance(
			@ApiParam(name = "instId", value = "实例id", required = true) @RequestParam String instId) throws Exception {
		return iProcessService.forbiddenInstance(instId);
	}

	@RequestMapping(value = "unForbiddenInstance", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "通过流程实例id取消挂起流程实例", notes = "通过流程实例id取消挂起流程实例", httpMethod = "GET")
	public CommonResult<String> unForbiddenInstance(
			@ApiParam(name = "instId", value = "实例id", required = true) @RequestParam String instId) throws Exception {
		return iProcessService.unForbiddenInstance(instId);
	}

	@RequestMapping(value = "getBpmProcessByParentIdAndSuperNodeId", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "根据父流程实例ID和节点定义ID查子流程实例", notes = "根据父流程实例ID和节点定义ID查子流程实例", httpMethod = "GET")
	public List<BpmProcessInstance> getBpmProcessByParentIdAndSuperNodeId(
			@ApiParam(name = "parentInstId", value = "父实例ID", required = true) @RequestParam String parentInstId,
			@ApiParam(name = "superNodeId", value = "节点ID", required = true) @RequestParam String superNodeId)
			throws Exception {
		return iProcessService.getBpmProcessByParentIdAndSuperNodeId(parentInstId, superNodeId);
	}

	@RequestMapping(value = "getInstancesByParentId", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "通过父流程实例ID和实例的状态获取实例列表", notes = "通过父流程实例ID和实例的状态获取实例列表", httpMethod = "GET")
	public List<DefaultBpmProcessInstance> getInstancesByParentId(
			@ApiParam(name = "parentInstId", value = "父实例ID", required = true) @RequestParam String parentInstId,
			@ApiParam(name = "status", value = "状态（draft：草稿，running：运行中，end：结束，manualend：人工结束，backToStart：驳回到发起人，back：驳回，revoke：撤销，revokeToStart：撤销到发起人）", required = true) @RequestParam String status)
			throws Exception {
		return iProcessService.getInstancesByParentId(parentInstId, status);
	}

	@RequestMapping(value = "getInstancesByDefId", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "通过流程定义id和实例的状态获取实例列表", notes = "通过流程定义id和实例的状态获取实例列表", httpMethod = "GET")
	public List<DefaultBpmProcessInstance> getInstancesByDefId(
			@ApiParam(name = "defId", value = "", required = true) @RequestParam String defId,
			@ApiParam(name = "status", value = "状态（draft：草稿，running：运行中，end：结束，manualend：人工结束，backToStart：驳回到发起人，back：驳回，revoke：撤销，revokeToStart：撤销到发起人）", required = true) @RequestParam String status)
			throws Exception {
		return iProcessService.getInstancesByDefId(defId, status);
	}

	@RequestMapping(value = "getTopBpmProcessInstance", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "根据流程实例ID查询顶级的流程实例（根据父实例向上查找，只到找到父实例为0的实例为止）", notes = "根据流程实例ID查询顶级的流程实例（根据父实例向上查找，只到找到父实例为0的实例为止）", httpMethod = "GET")
	public BpmProcessInstance getTopBpmProcessInstance(
			@ApiParam(name = "instId", value = "实例id", required = true) @RequestParam String instId) throws Exception {
		return iProcessService.getTopBpmProcessInstance(instId);
	}

	@RequestMapping(value = "getMyRequestListAll", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "我的请求（包括人工终止和结束状态的实例）", httpMethod = "POST", notes = "我的请求（包括人工终止和结束状态的实例）")
	public PageList<DefaultBpmProcessInstance> getMyRequestListAll(
			@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter,
			HttpServletResponse response) throws Exception {
		return iFlowService.getMyRequestListAll(baseContext.getCurrentUserAccout(), queryFilter);
	}

	@RequestMapping(value = "getBpmImage", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "根据流程定义id或流程实例id或任务id或BPMN实例ID获取流程图。", notes = "根据流程定义id或流程实例id或任务id或BPMN实例ID获取流程图。", httpMethod = "POST")
	public String getBpmImage(
			@ApiParam(name = "BpmImageParamObject", value = "获取流程图（状态）参数", required = true) @RequestBody BpmImageParamObject bpmImageParamObject)
			throws Exception {
		return iProcessService.getBpmImage(bpmImageParamObject);
	}

	@RequestMapping(value = "getInstanceList", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "查询流程实例列表", httpMethod = "POST", notes = "查询流程实例列表")
	public PageList<DefaultBpmProcessInstance> getInstanceList(
			@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter,
			@ApiParam(required = true, name = "defId", value = "查询参数对象") @RequestParam Optional<String> defId)
			throws Exception {
		if (StringUtil.isNotEmpty(defId.orElse("")))
			queryFilter.addFilter("proc_def_id_", defId.orElse(""), QueryOP.EQUAL);
		return iFlowService.getInstanceList(baseContext.getCurrentUserAccout(), queryFilter);
	}

	@RequestMapping(value = "newProcess", method = RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "新建流程", httpMethod = "POST", notes = "查询新建流程列表")
	public PageList<DefaultBpmDefinition> newProcess(
			@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter)
			throws Exception {
		return iFlowService.newProcess(baseContext.getCurrentUserAccout(), queryFilter);
	}

	@PostMapping(value = "newProcessCount", produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "新建流程", httpMethod = "POST", notes = "查询新建流程列表")
	public List<Map<String,Object>> newProcessCount(
			@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter)
			throws Exception {
		return iFlowService.newProcessCount(queryFilter);
	}

	@RequestMapping(value = "myRequest", method = RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "我的请求", httpMethod = "POST", notes = "查询我的请求列表")
	public PageList<DefaultBpmProcessInstance> myRequest(
			@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter)
			throws Exception {
		return iFlowService.myRequest(baseContext.getCurrentUserAccout(), queryFilter);
	}

	@GetMapping(value = "myRequestCount", produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "我的请求", httpMethod = "POST", notes = "查询我的请求列表在个分类的数量")
	public List<Map<String,Object>> myRequestCount() throws Exception {
		return iFlowService.myRequestCount(baseContext.getCurrentUserAccout());
	}

	@RequestMapping(value = "myMobileRequest", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "手机端我的请求", httpMethod = "POST", notes = "查询手机端我的请求列表")
	public PageList<DefaultBpmProcessInstance> myMobileRequest(
			@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter)
			throws Exception {
//		 queryFilter.addFilter("SUPPORT_MOBILE_", 1, QueryOP.EQUAL,FieldRelation.AND,"m");
		return iFlowService.myRequest(baseContext.getCurrentUserAccout(), queryFilter);
	}

	@RequestMapping(value = "myMobileDraft", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获取手机端我的草稿列表", httpMethod = "POST", notes = "获取手机端我的草稿列表")
	public PageList<DefaultBpmProcessInstance> myMobileDraft(
			@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter,
			HttpServletResponse response) throws Exception {
		queryFilter.addFilter("SUPPORT_MOBILE_", 1, QueryOP.EQUAL, FieldRelation.AND, "m");
		return iFlowService.getMyDraftList(baseContext.getCurrentUserAccout(), queryFilter);
	}

	@RequestMapping(value = "myMobileProcess", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "手机端新建流程", httpMethod = "POST", notes = "手机端查询新建流程列表")
	public PageList<DefaultBpmDefinition> myMobileProcess(
			@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter)
			throws Exception {
		queryFilter.addFilter("SUPPORT_MOBILE_", 1, QueryOP.EQUAL, FieldRelation.AND, "m");
		return iFlowService.newProcess(baseContext.getCurrentUserAccout(), queryFilter);
	}

	/**
	 * 根据任务id获取流程实例某个节点上的执行人员
	 *
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getNodeUsers", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "根据任务id获取流程实例某个节点上的执行人员", notes = "根据任务id获取流程实例某个节点上的执行人员", httpMethod = "GET")
	public List<BpmIdentityResult> getNodeUsers(
			@ApiParam(value = "任务id", name = "taskId", required = true) @RequestParam String taskId) throws Exception {
		return iProcessService.getNodeUsers(taskId);
	}

	@RequestMapping(value = "start", method = RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "客户端启动流程", httpMethod = "POST", notes = "客户端启动流程")
	public StartResult start(
			@ApiParam(name = "startFlowParamObject", value = "流程启动参数", required = true) @RequestBody StartFlowParamObject startFlowParamObject)
			throws Exception {

		return iProcessService.start(startFlowParamObject);
	}

	@RequestMapping(value = "saveDraft", method = RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存草稿", httpMethod = "POST", notes = "保存草稿")
	public StartResult saveDraft(
			@ApiParam(name = "startFlowParamObject", value = "流程启动参数", required = true) @RequestBody StartFlowParamObject startFlowParamObject)
			throws Exception {
		startFlowParamObject.setAccount(baseContext.getCurrentUserAccout());
		return iProcessService.saveDraft(startFlowParamObject);
	}

	@RequestMapping(value = "doNext", method = RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "客户端提交数据,执行流程往下跳转", httpMethod = "POST", notes = "客户端提交数据,执行流程往下跳转")
	public CommonResult<String> doNext(
			@ApiParam(name = "doNextParamObject", value = "流程向下执行对象", required = true) @RequestBody DoNextParamObject doNextParamObject)
			throws Exception {
		return iProcessService.doNext(doNextParamObject);
	}

	@RequestMapping(value = "doEndProcess", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "人工终止流程", httpMethod = "POST", notes = "人工终止流程")
	public CommonResult<String> doEndProcess(
			@ApiParam(name = "doEndParamObject", value = "流程终止对象") @RequestBody DoEndParamObject doEndParamObject)
			throws Exception {
		return iProcessService.doEndProcess(doEndParamObject);
	}

	@RequestMapping(value = "getHistoryOpinion", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "按流程实例ID或任务实例ID取得某个流程的审批历史", httpMethod = "GET", notes = "按流程实例ID或任务实例ID取得某个流程的审批历史")
	public List<BpmTaskOpinion> getHistoryOpinion(
			@ApiParam(name = "instanId", value = "流程实例id", required = false) @RequestParam(required = false) String instanId,
			@ApiParam(name = "taskId", value = "任务id", required = false) @RequestParam(required = false) String taskId)
			throws Exception {
		return iProcessService.getHistoryOpinion(instanId, taskId);
	}

	@RequestMapping(value = "getProcessOpinionByActInstId", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "按Activiti实例Id取得对应流程的审批历史", httpMethod = "GET", notes = "按Activiti实例Id取得对应流程的审批历史")
	public List<BpmCheckOpinionVo> getProcessOpinionByActInstId(
			@ApiParam(name = "actTaskId", value = "Activiti任务Id", required = true) @RequestParam String actTaskId)
			throws Exception {
		return iProcessService.getProcessOpinionByActInstId(actTaskId);
	}

	@RequestMapping(value = "getEnableRejectNode", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "根据任务ID获取可驳回的节点", httpMethod = "POST", notes = "根据任务ID获取可驳回的节点，rejectType驳回方式：direct直来直往，normal按照流程图执行")
	public List<BpmNodeDefVo> getEnableRejectNode(
			@ApiParam(name = "taskId", value = "任务id") @RequestParam String taskId,
			@ApiParam(name = "rejectType", allowableValues = "direct,normal", value = "返回方式") @RequestParam String rejectType)
			throws Exception {
		return iProcessService.getEnableRejectNode(taskId, rejectType);
	}

	@RequestMapping(value = "getBusinessKey", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "根据任务ID或流程实例ID获取BusinessKey（流程表单为URL表单的情况）", httpMethod = "GET", notes = "根据任务ID或流程实例ID获取BusinessKey（流程表单为URL表单的情况）")
	public CommonResult<String> getBusinessKey(
			@ApiParam(name = "instanId", value = "流程实例id", required = false) @RequestParam(required = false) String instanId,
			@ApiParam(name = "taskId", value = "任务id", required = false) @RequestParam(required = false) String taskId)
			throws NullPointerException {
		return iProcessService.getBusinessKey(instanId, taskId);
	}

	@RequestMapping(value = "getProcInstId", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "根据BussinessKey获取流程实例ID", httpMethod = "POST", notes = "根据BussinessKey获取流程实例ID")
	public CommonResult<String> getProcInstId(
			@ApiParam(name = "businessKey", value = "businessKey") @RequestParam String businessKey)
			throws NullPointerException {
		return iProcessService.getProcInstId(businessKey);
	}

	/**
	 * 根据任务id获取在线表单地址
	 *
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getDetailUrl", method = RequestMethod.GET, produces = { "application/json;charset=utf-8" })
	@ApiOperation(value = "根据任务id获取在线表单地址", notes = "根据任务id获取在线表单地址", httpMethod = "GET")
	public String getDetailUrl(@ApiParam(name = "taskId", value = "任务id") @RequestParam String taskId)
			throws Exception {
		return iProcessService.getDetailUrl(taskId);
	}

	@RequestMapping(value = "transToMore", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "抄送转发", httpMethod = "POST", notes = "抄送转发")
	public CommonResult<String> transToMore(
			@ApiParam(required = true, name = "copyToParam", value = "抄送转发参数对象") @RequestBody CopyToParam copyToParam)
			throws Exception {
		if (StringUtil.isEmpty(copyToParam.getUserId())) {
			return new CommonResult<String>(false, "请选择要传阅的人员！", "");
		}
		String[] userIds = copyToParam.getUserId().split("\\,");
		String curUserId = ContextUtil.getCurrentUserId();
		List<String> userList = new ArrayList<String>();
		for (String id : userIds) {
			if (curUserId.equals(id)) {
				return new CommonResult<String>(false, "传阅人不能为自己！", "");
			}
			userList.add(id);
		}
		if (StringUtil.isNotEmpty(copyToParam.getInstanceId()) && StringUtil.isEmpty(copyToParam.getTaskId())) {// 无任务ID时
																												// 已办批量传阅
			String[] instIds = copyToParam.getInstanceId().split("\\,");
			for (String instId : instIds) {
				copyToManager.transToMore(instId, userList, copyToParam.getMessageType(), copyToParam.getOpinion(),
						copyToParam.getCopyToType(), copyToParam.getTaskId(), copyToParam.getFiles(),
						copyToParam.getSelectNodeId());
			}
		} else if (StringUtil.isNotEmpty(copyToParam.getTaskId()) && StringUtil.isEmpty(copyToParam.getInstanceId())) {// 有任务ID时
																														// 待办批量传阅
			String[] taskIds = copyToParam.getTaskId().split("\\,");
			for (String taskId : taskIds) {
				DefaultBpmTask task = bpmTaskManager.get(taskId);
				if (BeanUtils.isEmpty(task)) {
					throw new RuntimeException("任务不存在或已经被处理！");
				}
				copyToManager.transToMore(task.getProcInstId(), userList, copyToParam.getMessageType(),
						copyToParam.getOpinion(), copyToParam.getCopyToType(), taskId, copyToParam.getFiles(),
						copyToParam.getSelectNodeId());
			}
		} else if ((StringUtil.isNotEmpty(copyToParam.getTaskId())
				|| StringUtil.isNotEmpty(copyToParam.getSelectNodeId()))
				&& StringUtil.isNotEmpty(copyToParam.getInstanceId())) {// 审批页面传阅
			copyToManager.transToMore(copyToParam.getInstanceId(), userList, copyToParam.getMessageType(),
					copyToParam.getOpinion(), copyToParam.getCopyToType(), copyToParam.getTaskId(),
					copyToParam.getFiles(), copyToParam.getSelectNodeId());
		}
		return new CommonResult<String>(true, "传阅成功", null);
	}

	@RequestMapping(value = "getInstFormAndBO", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获取流程实例的表单和数据", notes = "获取流程实例的表单和数据", httpMethod = "GET")
	public InstFormAndBoVo getInstFormAndBO(
			@ApiParam(name = "proInstId", value = "流程实例id") @RequestParam Optional<String> proInstId,
			@ApiParam(name = "nodeId", value = "任务节点id") @RequestParam Optional<String> nodeId,
			@ApiParam(name = "formId", value = "表单id") @RequestParam Optional<String> formId,
			@ApiParam(name = "getStartForm", value = "获取发起节点表单") @RequestParam Optional<Boolean> getStartForm,
			@ApiParam(name = "includData", value = "任务节点id") @RequestParam Optional<Boolean> includData)
			throws Exception {
		return iFlowService.getInstFormAndBO(proInstId.orElse(null), nodeId.orElse(null), formId.orElse(null), FormType.PC,
				includData.orElse(true), getStartForm.orElse(false));
	}

	@RequestMapping(value = "getMobileInstFormAndBO", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获取手机流程实例的表单和数据", notes = "获取手机流程实例的表单和数据", httpMethod = "GET")
	public InstFormAndBoVo getMobileInstFormAndBO(
			@ApiParam(name = "proInstId", value = "流程实例id") @RequestParam Optional<String> proInstId,
			@ApiParam(name = "nodeId", value = "任务节点id") @RequestParam Optional<String> nodeId) throws Exception {
		return iFlowService.getInstFormAndBO(proInstId.orElse(null), nodeId.orElse(null), null, FormType.MOBILE, true, false);
	}

	@RequestMapping(value = "getFormAndBO", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "流程启动时获取bo和表单", notes = "流程启动时获取bo和表单", httpMethod = "POST")
	public FormAndBoVo getFormAndBO(
			@ApiParam(required = true, name = "startCmdParam", value = "参数对象") @RequestBody StartCmdParam startCmdParam)
			throws Exception {
		return iFlowService.getFormAndBO(startCmdParam, FormType.PC);
	}

	@RequestMapping(value = "getMobileFormAndBO", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "手机流程启动时获取bo和表单", notes = "手机流程启动时获取bo和表单", httpMethod = "POST")
	public FormAndBoVo getMobileFormAndBO(
			@ApiParam(required = true, name = "startCmdParam", value = "参数对象") @RequestBody StartCmdParam startCmdParam)
			throws Exception {
		return iFlowService.getFormAndBO(startCmdParam, FormType.MOBILE);
	}

	@RequestMapping(value = "getStartCmd", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获取发起的cmd格式数据", httpMethod = "POST", notes = "获取发起的cmd格式数据")
	public DefaultProcessInstCmd getStartCmd(@RequestBody StartCmdParam startCmdParam) throws Exception {
		return iFlowService.getStartCmd(startCmdParam);
	}

	@RequestMapping(value = "startForm", method = RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "业务数据模板中 启动流程实例", httpMethod = "POST", notes = "业务数据模板中 启动流程实例")
	public CommonResult<String> startForm(
			@ApiParam(required = true, name = "defKey", value = "流程定义defKey") @RequestParam String defKey,
			@ApiParam(required = true, name = "businessKey", value = "业务主键") @RequestParam String businessKey,
			@ApiParam(required = true, name = "boAlias", value = "bo定义alias") @RequestParam String boAlias)
			throws Exception {
		try {
			DefaultProcessInstCmd cmd = new DefaultProcessInstCmd();
			cmd.setFlowKey(defKey);
			cmd.setDataMode(ActionCmd.DATA_MODE_BO);
			cmd.setActionName("startFlow");
			// 获取编辑数据
			ObjectNode formRestParams = JsonUtil.getMapper().createObjectNode();
			formRestParams.put("saveType", "database");
			formRestParams.put("boid", businessKey);
			formRestParams.put("code", boAlias);
			ObjectNode boData = formRestfulService.getBodataById(formRestParams);
			cmd.setBusData(JsonUtil.toJson(BoDataUtil.hanlerData(Arrays.asList(boData))));
			processInstanceService.startProcessInst(cmd);
			return new CommonResult<String>(true, "流程启动成功", "");
		} catch (Exception e) {
			return new CommonResult<String>(false, "流程启动失败：" + e.getMessage(), "");
		}
	}

	@RequestMapping(value = "sendNodeUsers", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获取可发送到节点指定人", httpMethod = "GET", notes = "获取可发送到节点指定人")
	public List<BpmNodeDef> nodeUsers(
			@ApiParam(required = true, name = "defId", value = "流程定义id") @RequestParam String defId,
			@ApiParam(name = "nodeId", value = "节点id") @RequestParam String nodeId) throws Exception {
		List<BpmNodeDef> listNodeDefs = new ArrayList<BpmNodeDef>();
		if (StringUtil.isEmpty(nodeId)) {
			BpmProcessDef<BpmProcessDefExt> procDef = bpmDefinitionAccessor.getBpmProcessDef(defId);
			BpmDefExtProperties prop = procDef.getProcessDefExt().getExtProperties();
			listNodeDefs = bpmDefinitionAccessor.getStartNodes(defId);
			boolean isSkip = prop.isSkipFirstNode();
			if (isSkip) {
				listNodeDefs = BpmStackRelationUtil.getAfterListNode(defId, listNodeDefs.get(0).getNodeId());
			}
		} else {
			listNodeDefs = BpmStackRelationUtil.getAfterListNode(defId, nodeId);
		}
		return listNodeDefs;
	}

	@RequestMapping(value = "selectDestination", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "选择路径", notes = "选择路径", httpMethod = "GET")
	public SelectDestinationVo selectDestination(
			@ApiParam(name = "defId", value = "流程定义id", required = true) @RequestParam String defId) throws Exception {
		return iFlowService.selectDestination(defId);
	}

	@RequestMapping(value = "instanceToStart", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "进入流程页面获取相关数据", notes = "进入流程页面获取相关数据", httpMethod = "GET")
	public BpmDefExtProperties instanceToStart(
			@ApiParam(name = "defId", value = "流程定义id", required = true) @RequestParam String defId) throws Exception {
		BpmProcessDef<BpmProcessDefExt> procDef = bpmDefinitionAccessor.getBpmProcessDef(defId);
		BpmDefExtProperties prop = procDef.getProcessDefExt().getExtProperties();
		return prop;
	}

	@RequestMapping(value = "instanceToCopyTo", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "进入流程转发页面", notes = "进入流程转发页面", httpMethod = "GET")
	public ToCopyToVo toCopyTo(
			@ApiParam(name = "proInstId", value = "流程实例id", required = true) @RequestParam String proInstId,
			@ApiParam(name = "taskId", value = "任务id", required = true) @RequestParam String taskId,
			@ApiParam(name = "copyToType", value = "类型：0 抄送  1转发", required = true) @RequestParam String copyToType)
			throws Exception {
		String nodeId = "";
		if (StringUtil.isNotEmpty(taskId)) {
			BpmTask task = bpmTaskService.getByTaskId(taskId);
			proInstId = task.getProcInstId();
			nodeId = task.getNodeId();
		}
		Map<String, String> handlerTypes = MessageUtil.getHandlerTypes();
		ToCopyToVo toCopyToVo = new ToCopyToVo();
		toCopyToVo.setCopyToType(copyToType);
		toCopyToVo.setProInstId(proInstId);
		toCopyToVo.setNodeId(nodeId);
		toCopyToVo.setHandlerTypes(handlerTypes);
		return toCopyToVo;
	}

	@RequestMapping(value = "get", method = RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取流程实例明细", httpMethod = "GET", notes = "获取流程实例明细")
	public DefaultBpmProcessInstance get(
			@ApiParam(name = "id", value = "流程实例id", required = true) @RequestParam String id) throws Exception {
		return bpmProcessInstanceManager.get(id);
	}

	@RequestMapping(value = "realDetail", method = RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取流程实例明细", httpMethod = "GET", notes = "获取流程实例明细")
	public DefaultBpmProcessInstance realDetail(
			@ApiParam(name = "cptoReceiverId", value = "抄送接收对象id", required = true) @RequestParam String cptoReceiverId,
			@ApiParam(name = "id", value = "流程实例id", required = true) @RequestParam String id) throws Exception {
		BpmCptoReceiver model = bpmCptoReceiverManager.get(cptoReceiverId);
		if (model != null && model.getIsRead() == 0) {
			model.setIsRead((short) 1);
			bpmCptoReceiverManager.update(model);
		}
		return bpmProcessInstanceManager.get(id);
	}

	@RequestMapping(value = "instanceFlowImage", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获取流程实例明细", httpMethod = "GET", notes = "获取流程实例明细")
	public FlowImageVo flowImage(
			@ApiParam(name = "proInstId", value = "流程实例id", required = true) @RequestParam Optional<String> proInstId,
			@ApiParam(name = "type", value = "如果为子流程：subFlow") @RequestParam Optional<String> type,
			@ApiParam(name = "from", value = "") @RequestParam Optional<String> from,
			@ApiParam(name = "nodeId", value = "节点id") @RequestParam Optional<String> nodeId,
			@ApiParam(name = "defId", value = "流程定义id") @RequestParam Optional<String> defId) throws Exception {
		return iFlowService.flowImage(proInstId.orElse(null), type.orElse(null), from.orElse(null), nodeId.orElse(null),
				defId.orElse(null));
	}

	@RequestMapping(value = "getBpmImage", method = RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取流程图", httpMethod = "GET", notes = "获取流程图")
	public String getBpmImage(@ApiParam(name = "defId", value = "流程定义id") @RequestParam Optional<String> defId,
			@ApiParam(name = "bpmnInstId", value = "bpmnInstId") @RequestParam Optional<String> bpmnInstId,
			@ApiParam(name = "taskId", value = "taskId") @RequestParam Optional<String> taskId,
			@ApiParam(name = "proInstId", value = "proInstId") @RequestParam Optional<String> proInstId)
			throws Exception {
		BpmImageParamObject object = new BpmImageParamObject();
		object.setDefId(defId.orElse(null));
		object.setBpmnInstId(bpmnInstId.orElse(null));
		object.setTaskId(taskId.orElse(null));
		object.setProcInstId(proInstId.orElse(null));
		return iProcessService.getBpmImage(object);
	}

	@RequestMapping(value = "instanceNodeStatus", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获取节点审批状态", httpMethod = "GET", notes = "获取节点审批状态")
	public NodeStatusVo getNodeStatus(
			@ApiParam(name = "instId", value = "流程实例id", required = true) @RequestParam String instId,
			@ApiParam(name = "nodeId", value = "节点id", required = true) @RequestParam String nodeId) throws Exception {
		List<IUser> userList = null;
		// 获取审批情况
		List<BpmTaskOpinion> bpmTaskOpinions = bpmOpinionService.getByInstNodeId(instId, nodeId);
		// 没有审批、则获取有审批权限的人...
		if (bpmTaskOpinions.size() < 1) {
			userList = bpmIdentityService.queryUsersByNode(instId, nodeId);
		}
		return new NodeStatusVo(userList, bpmTaskOpinions);
	}

	@RequestMapping(value = "instanceFlowOpinions", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "流程审批历史（页面数据）", httpMethod = "GET", notes = "流程审批历史")
	public List<ObjectNode> opinionHistory(
			@ApiParam(name = "instId", value = "流程实例id", required = true) @RequestParam Optional<String> instId,
			@ApiParam(name = "taskId", value = "任务id", required = true) @RequestParam Optional<String> taskId,
			@ApiParam(name = "isCommu", value = "是否包含沟通", required = false) @RequestParam Optional<Boolean> isCommu,
			@ApiParam(name = "isRelFlow", value = "是否相关流程", required = false) @RequestParam Optional<Boolean> isRelFlow)
			throws Exception {
		if (StringUtil.isNotEmpty(instId.orElse(null)) && !isRelFlow.orElse(false)) {
			// checkInstAuth(instId.get());
		}
		List<ObjectNode> listobj = iFlowService.opinionHistory(instId.orElse(null), taskId.orElse(null),
				isCommu.orElse(true));
		List<ObjectNode> listOpinion = new ArrayList<ObjectNode>();
		for (ObjectNode object : listobj) {
			if (!object.get("auditor").isNull() && "-1".equals(object.get("auditor").asText())) {// 过滤系统抄送审批历史
				continue;
			}
			listOpinion.add(object);
		}
		for (ObjectNode defaultBpmCheckOpinion : listOpinion) {
			if (OpinionStatus.AWAITING_CHECK.getKey().equals(defaultBpmCheckOpinion.get("status").asText())) {
				defaultBpmCheckOpinion.put("completeTime", TimeUtil.getCurrentTime());
				//新需求，此处抽取用户展示
				JsonNode qualfieds = defaultBpmCheckOpinion.get("qualfieds");
				if (BeanUtils.isEmpty(qualfieds)) {
					continue;
				}
				ArrayNode qualfiedsObj = null;
				try {
					 qualfiedsObj = (ArrayNode) JsonUtil.toJsonNode(qualfieds.asText());
				} catch (Exception e) {
					continue;
				}
				BpmIdentityExtractService bpmIdentityExtractService = AppUtil.getBean(BpmIdentityExtractService.class);
				Set<String> userNames = new HashSet<>();
				for (JsonNode jsonNode : qualfiedsObj) {
					ObjectNode identityObj = (ObjectNode) jsonNode;
					DefaultBpmIdentity identity = JsonUtil.toBean(identityObj, DefaultBpmIdentity.class);
					if (!BpmIdentity.TYPE_USER.equals(identity.getType())) {
						List<BpmIdentity> tempList = new ArrayList<>();
						tempList.add(identity);
						List<IUser> extractUser = bpmIdentityExtractService.extractUser(tempList);
						if (BeanUtils.isNotEmpty(extractUser)) {
							for (IUser iUser : extractUser) {
								userNames.add(iUser.getFullname());
							}
						}
					}else {
						userNames.add(identity.getName());
					}
				}
				defaultBpmCheckOpinion.put("qualfiedNames", StringUtil.join(userNames, ","));
			}
		}
		Collections.sort(listOpinion, new Comparator<ObjectNode>() {
			@Override
			public int compare(ObjectNode opinion1, ObjectNode opinion2) {
				if (BeanUtils.isEmpty(opinion1.get("completeTime"))) {
					opinion1.put("completeTime", TimeUtil.getCurrentTime());
				}
				return opinion2.get("completeTime").asText().compareTo(opinion1.get("completeTime").asText());
			}

		});
		// 处理审批历史
		listOpinion = dealOpinions(listOpinion);
		return listOpinion;
	}

	/**
	 * 处理审批历史中审批人、所在部门和意见控制
	 *
	 * @param listOpinion
	 * @throws IOException
	 */
	private List<ObjectNode> dealOpinions(List<ObjectNode> listOpinion) throws IOException {
		List<String> auditor = new ArrayList<>();
		List<String> strauditor = new ArrayList<>();
		for (ObjectNode defaultBpmCheckOpinion : listOpinion) {
			if (BeanUtils.isNotEmpty(defaultBpmCheckOpinion.get("status"))
					&& OpinionStatus.AWAITING_CHECK.getKey().equals(defaultBpmCheckOpinion.get("status").asText())
					&& defaultBpmCheckOpinion.get("interpose").asInt() == 0) {
				defaultBpmCheckOpinion.put("completeTime", "");
			}
			if (BeanUtils.isNotEmpty(defaultBpmCheckOpinion.get("opinion"))
					&& defaultBpmCheckOpinion.get("opinion").asText().equals("抄送消息")) {
				defaultBpmCheckOpinion.put("opinion", "");
			}
			try {
				if (BeanUtils.isEmpty(defaultBpmCheckOpinion.get("auditor"))) {
					ArrayNode array = (ArrayNode) JsonUtil.toJsonNode(defaultBpmCheckOpinion.get("qualfieds").asText());
					for (JsonNode str : array) {
						strauditor.add(str.get("id").asText());
					}
					// 获取毫秒数
					LocalDateTime oldDateTime = TimeUtil
							.convertString(defaultBpmCheckOpinion.get("createTime").asText());
					Long oldDate = oldDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
					Long newDate = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
					defaultBpmCheckOpinion.put("durMs", newDate - oldDate);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (BeanUtils.isNotEmpty(defaultBpmCheckOpinion.get("auditor"))) {
				auditor.add(defaultBpmCheckOpinion.get("auditor").asText());
			}
		}
		for (int i = 0; i < listOpinion.size(); i++) {
			if (BeanUtils.isNotEmpty(listOpinion.get(i).get("status"))
					&& ( ( OpinionStatus.SKIP.getKey().equals(listOpinion.get(i).get("status").asText()) && !SkipResult.SKIP_FIRST.equals(listOpinion.get(i).get("skipType").asText()) )
							|| OpinionStatus.END.getKey().equals(listOpinion.get(i).get("status").asText()))
					) {
				listOpinion.remove(i);
				i--;
			}
		}
		// 把循环调用的ucFeignService抽取出来，提高效率
		if (auditor.size() > 0) {
			List<Map<String, String>> jobNames = FeignServiceUtil.getPathNames(auditor);
			for (Map<String, String> jobNamesMap : jobNames) {
				if (StringUtil.isNotEmpty(jobNamesMap.get("userId"))) {
					for (ObjectNode checkOpinion : listOpinion) {
						if (BeanUtils.isNotEmpty(checkOpinion.get("adminInterPose"))) {
							checkOpinion.put("postName", "(管理员)");
						} else if (jobNamesMap.get("userId").equals(checkOpinion.get("auditor").asText())) {
							if (StringUtil.isNotEmpty(jobNamesMap.get("dutyName"))) {
								checkOpinion.put("postName", "(" + jobNamesMap.get("dutyName") + ")");
							}
						}
					}
				}
			}
		}
		// 把循环调用的ucFeignService抽取出来，提高效率 审批历史没有操作人就取有权限审批用户组
		if (strauditor.size() > 0) {
			List<Map<String, String>> pathNames = FeignServiceUtil.getPathNames(strauditor);
			// 添加还没操作人的组织全路径
			for (Map<String, String> pathNamesMap : pathNames) {
				if (StringUtil.isNotEmpty(pathNamesMap.get("userId"))) {
					for (ObjectNode checkOpinion : listOpinion) {
						if (BeanUtils.isEmpty(checkOpinion.get("auditor"))) {
							ArrayNode array = (ArrayNode) JsonUtil.toJsonNode(checkOpinion.get("qualfieds").asText());
							for (JsonNode str : array) {
								if (pathNamesMap.get("userId").equals(str.get("id").asText())) {
									String pathName = pathNamesMap.get("pathName");
									checkOpinion.put("orgPath", pathName);
									if (StringUtil.isNotEmpty(pathNamesMap.get("dutyName"))) {
										checkOpinion.put("postName", "(" + pathNamesMap.get("dutyName") + ")");
									}
									checkOpinion.put("auditorName", pathNamesMap.get("fullName"));
								}
							}
						}
					}
				}
			}
		}

		// 对审批历史做处理 去掉通过取消的审批历史
		try {
			if (BeanUtils.isNotEmpty(listOpinion)) {
				List<ObjectNode> newlist = new ArrayList<ObjectNode>();
				for (ObjectNode objectNode : listOpinion) {
					if (!(BeanUtils.isNotEmpty(objectNode.get("status"))
							&& OpinionStatus.SIGN_PASS_CANCEL.getKey().equals(objectNode.get("status").asText()))) {
						newlist.add(objectNode);
					}
				}
				listOpinion = newlist;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listOpinion;
	}

	@RequestMapping(value = "remove", method = RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "删除流程实例", httpMethod = "DELETE", notes = "删除流程实例")
	public Object remove(@ApiParam(name = "ids", value = "流程实例id，多个用“,”号隔开", required = true) @RequestParam String ids)
			throws Exception {
		String[] idArr = null;
		if (!StringUtil.isEmpty(ids)) {
			idArr = ids.split(",");
		}
		Set<String> hasDelRightInstIds =null;
		if (!ContextUtil.getCurrentUser().isAdmin()) {
			hasDelRightInstIds = new HashSet<>();
			BpmDefAuthorizeManager authorizeManager = AppUtil.getBean(BpmDefAuthorizeManager.class);
			// 获得流程分管授权与用户相关的信息
			Map<String, Object> actRightMap = authorizeManager.getActRightByUserId(ContextUtil.getCurrentUserId(),  BPMDEFAUTHORIZE_RIGHT_TYPE.INSTANCE, true, true);
			if (BeanUtils.isEmpty(actRightMap) || BeanUtils.isEmpty(actRightMap.get("authorizeRightMap"))) {
				new CommonResult<String>(false, "您没有删除这些流程实例的权限", "");
			}
			Set<String> hasDelRightDefs =new HashSet<>();
			Map<String, Object> authorizeRightMap = (Map<String, Object>) actRightMap.get("authorizeRightMap");
			for (Iterator<Entry<String, Object>> iterator = authorizeRightMap.entrySet().iterator(); iterator.hasNext();) {
				Entry<String, Object> next =  iterator.next();
				ObjectNode  value=  (ObjectNode) next.getValue();
				if (value.hasNonNull("i_del") && value.get("i_del").asBoolean()) {
					hasDelRightDefs.add(next.getKey());
				}
			}
			QueryFilter<DefaultBpmProcessInstance> queryFilter = QueryFilter.build();
			queryFilter.addFilter("proc_def_key_", StringUtil.join(hasDelRightDefs, ","), QueryOP.IN);
			queryFilter.withPage(new PageBean(1, Integer.MAX_VALUE));
			PageList<DefaultBpmProcessInstance> query = baseService.query(queryFilter);
			if (BeanUtils.isEmpty(query) || BeanUtils.isEmpty(query.getRows())) {
				new CommonResult<String>(false, "您没有删除这些流程实例的权限", "");
			}
			for (DefaultBpmProcessInstance inst : query.getRows()) {
				hasDelRightInstIds.add(inst.getId());
			}
		}
		int delFailCounts = 0;
		for(String str : idArr){
			if (hasDelRightInstIds !=null && !hasDelRightInstIds.contains(str)) {
				delFailCounts++;
				continue;
			}
            bpmProcessInstanceManager.removeBpm(str);
        }
		String mString = "删除流程实例成功";
		if (delFailCounts >0) {
			mString = "成功删除:"+(idArr.length -delFailCounts)+"条，无权限删除:"+delFailCounts+"条";
		}
		return new CommonResult<String>(true,mString , "");
	}

	@RequestMapping(value = "restore", method = RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据流程实例ID恢复实例数据", httpMethod = "GET", notes = "根据流程实例ID恢复实例数据")
	public Object restore(@ApiParam(name = "id", value = "流程实例id", required = true) @RequestParam String id)
			throws Exception {
		String[] idArr = null;
		if (StringUtil.isNotEmpty(id)) {
			idArr = id.split(",");
		}
		for (String instId : idArr) {
			bpmProcessInstanceManager.restore(instId);
		}
		return new CommonResult<String>(true, "恢复成功", "");
	}

	@RequestMapping(value = "checkInvoke", method = RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "判断根据流程实是否例撤销到发起人", httpMethod = "GET", notes = "判断根据流程实是否例撤销到发起人")
	public CommonResult<String> checkInvoke(
			@ApiParam(name = "invokeToStart", value = "", required = true) @RequestParam Integer invokeToStart,
			@ApiParam(name = "instanceId", value = "流程实例id", required = true) @RequestParam String instanceId)
			throws Exception {
		ResultMessage result = null;
		invokeToStart = BeanUtils.isEmpty(invokeToStart) ? 1 : invokeToStart;
		if (invokeToStart == 1) {
			result = bpmProcessInstanceManager.canRevokeToStart(instanceId);
			return new CommonResult<String>(true, result.getMessage(), null);
		}
		return new CommonResult<String>(false, "", null);
	}

	@RequestMapping(value = "getPathNodes", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "通过流程实例ID获取流程运行轨迹", httpMethod = "GET", notes = "通过流程实例ID获取流程运行轨迹")
	public List<BpmInstanceTrack> getPathNodes(
			@ApiParam(name = "instanceId", value = "流程实例id", required = true) @RequestParam String procInstId)
			throws Exception {
		return processInstanceService.getTracksByInstId(procInstId);
	}

	@RequestMapping(value = "getStatusByRunidNodeId", method = RequestMethod.GET, produces = {
			"application/json;charset=utf-8" })
	@ApiOperation(value = "根据实例id和节点id获取节点状态", notes = "根据实例id和节点id获取节点状态", httpMethod = "GET")
	public String getStatusByRunidNodeId(@ApiParam(name = "instId", value = "实例id") @RequestParam String instId,
			@ApiParam(name = "nodeId", value = "节点id") @RequestParam String nodeId) throws Exception {
		return iProcessService.getStatusByRunidNodeId(instId, nodeId);
	}

	@RequestMapping(value = "getDataByDefId", method = RequestMethod.GET, produces = {
			"application/json;charset=utf-8" })
	@ApiOperation(value = "根据流程定义id获取bo数据", notes = "根据流程定义id获取bo数据", httpMethod = "GET")
	public List<ObjectNode> getDataByDefId(@ApiParam(name = "defId", value = "流程定义id") @RequestParam String defId)
			throws Exception {
		return boDataService.getDataByDefId(defId);
	}

	@RequestMapping(value = "removeDraftById", method = RequestMethod.DELETE, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = " 删除草稿", httpMethod = "DELETE", notes = "删除草稿")
	public CommonResult<String> removeDraftById(
			@ApiParam(name = "ids", value = "流程实例id，多个用“,”号隔开", required = true) @RequestParam String ids)
			throws Exception {
		String[] aryIds = null;
		if (!StringUtil.isEmpty(ids)) {
			aryIds = ids.split(",");
		}
		for(String str : aryIds){
            bpmProcessInstanceManager.removeBpm(str);
        }
		return new CommonResult<String>(true, "删除草稿成功", "");
	}

	@RequestMapping(value = "taskTurnAssigns", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "通过Id获取转办人员", httpMethod = "GET", notes = "通过Id获取转办人员")
	public List<TaskTurnAssign> taskTurnAssigns(
			@ApiParam(name = "taskTurnId", value = "taskTurnId", required = true) @RequestParam String taskTurnId)
			throws Exception {
		return bpmTaskTurnManager.getTurnAssignByTaskTurnId(taskTurnId);
	}

	@RequestMapping(value = "getByBusinesKey", method = RequestMethod.GET, produces = {
			"application/json;charset=utf-8" })
	@ApiOperation(value = "获取流程实例与业务数据关系", notes = "获取流程实例与业务数据关系", httpMethod = "GET")
	public BpmBusLink getByBusinesKey(
			@ApiParam(name = "businessKey", value = "业务数据id", required = true) @RequestParam String businessKey,
			@ApiParam(name = "formIdentity", value = "表单标识") @RequestParam String formIdentity,
			@ApiParam(name = "isNumber", value = "是否数字类型", required = true) @RequestParam Boolean isNumber)
			throws Exception {
		return bpmBusLinkManager.getByBusinesKey(businessKey, formIdentity, isNumber);
	}

	@RequestMapping(value = "getDefaultInfobox", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "信息盒子", httpMethod = "GET", notes = "首页栏目信息盒子")
	public List<InfoboxVo> getDefaultInfobox() throws Exception {
		List<InfoboxVo> list = new ArrayList<InfoboxVo>();
		try {
			InfoboxVo myTaksBox = this.getMyTaksBox(); // 我的待办
			InfoboxVo myMessBox = this.getMyMessBox(); // 内部消息
			InfoboxVo myProCopytoBox = this.getMyProCopytoBox(); // 抄送转发
			InfoboxVo myAlreadyBox = getMyAlreadyBox(); // 已办
			InfoboxVo myCompletedBox = getMyCompletedBox(); // 我的办结

			InfoboxVo myAccordingMattersBox = this.getMyAccordingMattersBox(); // 转办代理事宜
			InfoboxVo myRequestBox = this.getMyRequestBox(); // 我的请求
			InfoboxVo myDraftBox = this.getMyDraftBox(); // 我的草稿

			list.add(myTaksBox); // 我的待办
			list.add(myMessBox); // 内部消息
			list.add(myProCopytoBox); // 抄送转发
			list.add(myAlreadyBox); // 已办
			list.add(myCompletedBox); // 我的办结

			list.add(myAccordingMattersBox); // 转办代理事宜
			list.add(myRequestBox); // 我的请求
			list.add(myDraftBox); // 我的草稿

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@RequestMapping(value = "getDefaultInfoMap", method = RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "信息盒子", httpMethod = "GET", notes = "首页栏目信息盒子")
	public Map<String, Object> getDefaultInfoMap() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			InfoboxVo myTaksBox = this.getMyTaksBox(); // 我的待办
			InfoboxVo myAlreadyBox = getMyAlreadyBox(); // 已办
			InfoboxVo myRequestBox = this.getMyRequestBox(); // 我的请求
			InfoboxVo myAccordingMattersBox = this.getMyAccordingMattersBox(); // 转办代理事宜
//			InfoboxVo myCompletedBox = getMyCompletedBox(); // 我的办结
//			InfoboxVo myDraftBox = this.getMyDraftBox(); // 我的草稿
			map.put("myTaks", myTaksBox);// 我的待办
			map.put("myAlready", myAlreadyBox);// 已办
//			map.put("myCompleted", myCompletedBox);// 我的办结
			map.put("myAccordingMatters", myAccordingMattersBox);// 转办代理事宜
			map.put("myRequest", myRequestBox);// 我的请求
//			map.put("myDraft", myDraftBox);// 我的草稿
            Map<String, Object> userInfoMap = ucFeignService.getDetailByAccountOrId(ContextUtil.getCurrentUserId());
            if (BeanUtils.isNotEmpty(userInfoMap)) {
            	map.putAll(userInfoMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@RequestMapping(value = "getFlowsMap", method = RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "首页栏目获取固化流程信息", httpMethod = "GET", notes = "首页栏目获取固化流程信息")
	public Map<String, DefaultBpmDefinition> getFlowsMap() throws Exception {
		Map<String, DefaultBpmDefinition> map = new HashMap<String, DefaultBpmDefinition>();
		try {
			String[] flowKeys = { "xwggsp", "hylc" };
			QueryFilter queryFilter = QueryFilter.<DefaultBpmDefinition>build().withPage(new PageBean(1, 20));
			queryFilter.addFilter("defKey", flowKeys, QueryOP.IN);
			PageList<DefaultBpmDefinition> flows = bpmDefinitionManager.query(queryFilter);
			if (BeanUtils.isNotEmpty(flows) && flows.getTotal() > 0) {
				for (DefaultBpmDefinition flow : flows.getRows()) {
					map.put(flow.getDefKey(), flow);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 我的草稿
	 *
	 * @return
	 */
	private InfoboxVo getMyDraftBox() {
		String userId = ContextUtil.getCurrentUserId();
		QueryFilter querFilter = QueryFilter.<DefaultBpmProcessInstance>build().withPage(new PageBean(1, 1));
		PageList<DefaultBpmProcessInstance> list = new PageList<>(
				bpmProcessInstanceManager.getDraftsByUserId(userId, querFilter));
		InfoboxVo infobox = new InfoboxVo();
		infobox.setIcon("fa-pencil-square-o");
		infobox.setColor(InfoboxVo.COLOR_WOOD);
		infobox.setDataText(list.getTotal() + "");
		infobox.setDataContent("我的草稿");
		infobox.setUrl("#/initiatedProcess/myDraft");
		return infobox;
	}

	/**
	 * 我的待办
	 *
	 * @return
	 */
	private InfoboxVo getMyTaksBox() {
		String userId = ContextUtil.getCurrentUserId();
//		PageList<DefaultBpmTask> list = bpmTaskManager.getByUserId(userId);
		Long count = bpmTaskManager.getTodoCountByUserId(userId);
		InfoboxVo infobox = new InfoboxVo();
		infobox.setIcon("fa-pencil-square-o");
		infobox.setColor(InfoboxVo.COLOR_WOOD);
		infobox.setDataText(String.valueOf(count));
		infobox.setDataContent("我的待办");
		infobox.setUrl("/v-flow/v-todo");
		return infobox;
	}


	/**
	 * 我的办结
	 *
	 * @return
	 */
	private InfoboxVo getMyCompletedBox() {
		String userId = ContextUtil.getCurrentUserId();
		QueryFilter querFilter = QueryFilter.<DefaultBpmProcessInstance>build().withPage(new PageBean(1, 1));
		PageList<DefaultBpmProcessInstance> list = new PageList<>(
				bpmProcessInstanceManager.getMyCompletedByUserId(userId, querFilter));
		InfoboxVo infobox = new InfoboxVo();
		infobox.setIcon("fa-check-square-o");
		infobox.setColor(InfoboxVo.COLOR_BROWN);
		infobox.setDataText(list.getTotal() + "");
		infobox.setDataContent("我的办结");
		infobox.setUrl("/v-flow/v-done");
		return infobox;
	}

	/**
	 * 我的请求
	 *
	 * @return
	 */
	private InfoboxVo getMyRequestBox() {
		String userId = ContextUtil.getCurrentUserId();
		Long count = bpmProcessInstanceManager.getMyRequestCountByUserId(userId);
		InfoboxVo infobox = new InfoboxVo();
		infobox.setIcon("fa-hand-o-up");
		infobox.setColor(InfoboxVo.COLOR_BLUE2);
		infobox.setDataText(String.valueOf(count));
		infobox.setDataContent("我的请求");
		infobox.setUrl("/v-flow/v-request");
		return infobox;
	}

	/**
	 * 转办代理事宜
	 *
	 * @return
	 */
	private InfoboxVo getMyAccordingMattersBox() {
		String userId = ContextUtil.getCurrentUserId();
		Long count = bpmTaskTurnManager.getMyDelegateCountByUserId(userId);
		InfoboxVo infobox = new InfoboxVo();
		infobox.setIcon("fa-share");
		infobox.setColor(InfoboxVo.COLOR_PINK);
		infobox.setDataText(String.valueOf(count));
		infobox.setDataContent("转办代理");
		infobox.setUrl("/v-flow/v-todo");
		return infobox;
	}

	/**
	 * 已办事宜
	 *
	 * @return
	 * @throws Exception
	 */
	private InfoboxVo getMyAlreadyBox() throws Exception {
		String userId = ContextUtil.getCurrentUserId();
		Long count = bpmProcessInstanceManager.getDoneInstCount(userId);
		InfoboxVo infobox = new InfoboxVo();
		infobox.setIcon("fa-flag");
		infobox.setColor(InfoboxVo.COLOR_RED);
		infobox.setDataText(String.valueOf(count));
		infobox.setDataContent("已办事宜");
		infobox.setUrl("/v-flow/v-done");
		return infobox;
	}

	/**
	 * 查看抄送转发
	 *
	 * @return
	 */
	private InfoboxVo getMyProCopytoBox() {
		String userId = ContextUtil.getCurrentUserId();
		QueryFilter querFilter = QueryFilter.<CopyTo>build().withPage(new PageBean(1, 1));
		PageList<CopyTo> list = new PageList<>(copyToManager.getMyCopyTo(userId, querFilter));
		InfoboxVo infobox = new InfoboxVo();
		infobox.setIcon("fa-comments");
		infobox.setColor(InfoboxVo.COLOR_BLUE3);
		infobox.setDataText("(" + list.getTotal() + "/" + list.getTotal() + ")");
		infobox.setDataContent("抄送转发");
		infobox.setUrl("#/initiatedProcess/myCopyTo");
		return infobox;
	}

	/**
	 * 查看内部消息
	 *
	 * @return
	 */
	private InfoboxVo getMyMessBox() {
		Integer messCount = 0;
		Integer noReadMessCount = 0;
		PortalFeignService PortalFeignService = AppUtil.getBean(PortalFeignService.class);
		try {
			ObjectNode info = PortalFeignService.getMessBoxInfo(baseContext.getCurrentUserAccout());
			if (BeanUtils.isNotEmpty(info)) {
				messCount = info.get("messCount").asInt();
				noReadMessCount = info.get("noReadMessCount").asInt();
			}
		} catch (Exception e) {
		}

		InfoboxVo infobox = new InfoboxVo();
		infobox.setIcon("fa-comments");
		infobox.setColor(InfoboxVo.COLOR_BLUE2);
		infobox.setDataText("(" + noReadMessCount + "/" + messCount + ")");
		infobox.setDataContent("内部消息");
		infobox.setUrl("#/messageReceiver/insideMessageList");
		return infobox;
	}

	@RequestMapping(value = "getNewsAndBulletin", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获取新闻公告", httpMethod = "POST", notes = "查询新建流程列表")
	public ArrayNode getNewsAndBulletin(
			@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody Optional<QueryFilter> queryFilter)
			throws Exception {
		ArrayNode messageNews = JsonUtil.getMapper().createArrayNode();
		List<String> dataIds = getMessageNewsIds();
		if (BeanUtils.isNotEmpty(dataIds)) {
			PortalFeignService portalFeignService = AppUtil.getBean(PortalFeignService.class);
			QueryFilter newsFilter = QueryFilter.build().withPage(new PageBean(1, 15));
			ObjectNode node = (ObjectNode) JsonUtil.toJsonNode(newsFilter);
			ArrayNode array = JsonUtil.getMapper().createArrayNode();
			QueryField queryField = new QueryField("id", dataIds, QueryOP.IN, FieldRelation.AND);
			array.add(JsonUtil.toJsonNode(queryField));
			node.set("querys", array);
			ObjectNode dataPageList = portalFeignService.getMessageNews(node);
			if (BeanUtils.isNotEmpty(dataPageList) && BeanUtils.isNotEmpty(dataPageList.get("rows"))) {
				messageNews = (ArrayNode) JsonUtil.toJsonNode(dataPageList.get("rows"));
			}
		}
		return messageNews;
	}

	private List<String> getMessageNewsIds() {
		List<String> ids = new ArrayList<String>();
		QueryFilter filter = QueryFilter.<DefaultBpmProcessInstance>build().withPage(new PageBean(1, Integer.MAX_VALUE));
		filter.addFilter("procDefKey", "xwggsp", QueryOP.EQUAL);
		filter.addFilter("status", "end", QueryOP.EQUAL);
		filter.addFilter("resultType", "agree", QueryOP.EQUAL);
		PageList<DefaultBpmProcessInstance> pageList = bpmProcessInstanceManager.query(filter);
		if (BeanUtils.isNotEmpty(pageList) && pageList.getTotal() > 0) {
			List<DefaultBpmProcessInstance> list = pageList.getRows();
			List<String> instIds = new ArrayList<String>();
			for (DefaultBpmProcessInstance instance : list) {
				instIds.add(instance.getId());
			}
			QueryFilter linkFilter = QueryFilter.<BpmBusLink>build().withPage(new PageBean(1, Integer.MAX_VALUE));
			linkFilter.addFilter("procInstId", instIds, QueryOP.IN);
			PageList<BpmBusLink> linkPage = bpmBusLinkManager.query(linkFilter);
			if (BeanUtils.isNotEmpty(linkPage) && linkPage.getTotal() > 0) {
				List<BpmBusLink> links = linkPage.getRows();
				for (BpmBusLink bpmBusLink : links) {
					ids.add(bpmBusLink.getBusinesskeyStr());
				}
			}
		}
		return ids;
	}

	@RequestMapping(value = "publishMsgNews", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "通过Id获取转办人员", httpMethod = "POST", notes = "通过Id获取转办人员")
	public void publishMsgNews(@ApiParam(name = "params", required = true) @RequestBody String params)
			throws Exception {
		ObjectNode node = (ObjectNode) JsonUtil.toJsonNode(params);
		String instId = node.get("instId").asText();
		DefaultBpmProcessInstance instance = bpmProcessInstanceManager.get(instId);
		if (BeanUtils.isNotEmpty(instance)
				&& (StringUtil.isEmpty(instance.getResultType()) || "agree".equals(instance.getResultType()))) {
			List<BpmBusLink> links = bpmBusLinkManager.getByInstId(instId);
			if (BeanUtils.isNotEmpty(links)) {
				PortalFeignService portalFeignService = AppUtil.getBean(PortalFeignService.class);
				List<String> array = new ArrayList<String>();
				for (BpmBusLink bpmBusLink : links) {
					array.add(bpmBusLink.getBusinesskeyStr());
				}
				portalFeignService.publicMsgNews(StringUtils.join(array, ","));
			}
		}
	}

	@RequestMapping(value = "getBusLink", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "根据流程实例获取关联数据", httpMethod = "POST", notes = "根据流程实例获取关联数据")
	public List<String> getBusLink(@ApiParam(name = "params", required = true) @RequestBody ObjectNode params) throws Exception {
			List<String> res = new ArrayList<>();
			String taskType=params.get("taskType").asText();
			String defKey=params.get("defKey").asText();
			IUser user = ServiceUtil.getUserByAccount(baseContext.getCurrentUserAccout());
			QueryFilter queryFilter=QueryFilter.<DefaultBpmTask>build();
			queryFilter.setPageBean(null);
			List<String> ids=new ArrayList<>();
			if("todo".equals(taskType)){//代办
				queryFilter.setPageBean(new PageBean(false));
				queryFilter.addFilter("inst.proc_def_key_", defKey, QueryOP.EQUAL);
				List<DefaultBpmTask> list = bpmTaskManager.getAllByUserId(user.getUserId(),queryFilter);
				list.forEach(item ->{
					ids.add(item.getProcInstId());
				});
			}else if("done".equals(taskType)){//已办
				queryFilter.addFilter("proc_def_key_", defKey, QueryOP.EQUAL);
				List<Map<String, Object>> list = bpmProcessInstanceManager.getDoneInstList(user.getUserId(), queryFilter).getRows();
				list.forEach(item->{
					ids.add(item.get("id")+"");
				});
				System.out.println(list);
			}else if("request".equals(taskType)){//我的请求
				queryFilter.addFilter("proc_def_key_", defKey, QueryOP.EQUAL);
				List<DefaultBpmProcessInstance> list= iFlowService.myRequest(baseContext.getCurrentUserAccout(), queryFilter).getRows();
				list.forEach(item->{
					ids.add(item.getId());
				});
			}else if("todoRead".equals(taskType)){//待阅
				queryFilter.addFilter("bpm_pro_inst.proc_def_key_", defKey, QueryOP.EQUAL);
				List<BpmTaskNotice> list=iFlowService.getNoticeTodoReadList(baseContext.getCurrentUserAccout(), queryFilter).getRows();
				list.forEach(item->{
					ids.add(item.getProcInstId());
				});
			}else if("doneRead".equals(taskType)){//已阅
				DefaultBpmDefinition po = bpmDefinitionManager.getMainByDefKey(defKey);
				queryFilter.addFilter("bpm_task_notice_done.PROC_DEF_ID_", po.getDefId(), QueryOP.EQUAL);
				List<BpmTaskNoticeDone> list= iFlowService.getNoticeDoneReadList(baseContext.getCurrentUserAccout(), queryFilter).getRows();
				list.forEach(item->{
					ids.add(item.getProcInstId());
				});
			}else if("myRead".equals(taskType)){//我传阅的
				queryFilter.addFilter("bpm_pro_inst.proc_def_key_", defKey, QueryOP.EQUAL);
				List<BpmTaskNotice> list = iFlowService.getMyNoticeReadList(baseContext.getCurrentUserAccout(), queryFilter).getRows();
				list.forEach(item->{
					ids.add(item.getProcInstId());
				});
			}else if("myDelegate".equals(taskType)){//我转办的
				queryFilter.addFilter("hi.proc_def_key_", defKey, QueryOP.EQUAL);
				List<DefaultBpmTaskTurn> list = iFlowService.getDelegate(baseContext.getCurrentUserAccout(), queryFilter).getRows();
				list.forEach(item->{
					ids.add(item.getProcInstId());
				});
			}
			if(ids.size()>0){
				QueryFilter busQueryFilter = QueryFilter.<BpmBusLink>build().withQuery(new QueryField("proc_inst_id_", ids, QueryOP.IN));
				PageList<BpmBusLink> links = bpmBusLinkManager.query(busQueryFilter);
				links.getRows().forEach(item->{
					res.add(item.getBusinesskeyStr());
				});
			}
			return  res;
	}

	/**
	 * 获取流程运行时的变量
	 */
	@RequestMapping(value = "getInstRunDataList", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获取流程运行时的变量", httpMethod = "GET", notes = "获取流程运行时的变量")
	public List<ObjectNode> getInstRunDataList(
			@ApiParam(name = "instanceId", required = true) @RequestParam String instanceId) throws Exception {
		List<ObjectNode> runDataList = new ArrayList<ObjectNode>();
		if (StringUtil.isNotEmpty(instanceId)) {
			BpmTaskManager bpmTaskManager = AppUtil.getBean(BpmTaskManager.class);
			TaskService taskService = AppUtil.getBean(TaskService.class);
			BpmInstService bpmInstService = AppUtil.getBean(BpmInstService.class);
			BpmProcessInstance bpmProcessInstance = bpmInstService.getProcessInstance(instanceId);
			List<DefaultBpmTask> tasks = bpmTaskManager.getByInstId(instanceId);
			String defId = bpmProcessInstance.getProcDefId();
			Map<String, Object> params = new HashMap<>();
			if (tasks.size() > 0) {
				String taskId = tasks.get(0).getTaskId();
				params = taskService.getVariables(taskId);
			}
			List<BpmVariableDef> bpmVariableList = getAllBpmVariableDef(defId);
			for (BpmVariableDef bpmVariableDef : bpmVariableList) {
				ObjectNode object = (ObjectNode) JsonUtil.toJsonNode(bpmVariableDef);
				if (BeanUtils.isNotEmpty(params.get(bpmVariableDef.getVarKey()))) {
					String dataType = bpmVariableDef.getDataType();
					String dataValue = params.get(bpmVariableDef.getVarKey()).toString();
					if ("int".equals(dataType)) {
						object.put("runVal", Integer.valueOf(dataValue));
					} else if ("float".equals(dataType)) {
						object.put("runVal", Float.valueOf(dataValue));
					} else if ("double".equals(dataType)) {
						object.put("runVal", Double.valueOf(dataValue));
					} else {
						object.put("runVal", dataValue);
					}
				}
				runDataList.add(object);
			}
		}
		return runDataList;
	}

	/**
	 * 所有的变量
	 *
	 * @throws Exception
	 */
	private List<BpmVariableDef> getAllBpmVariableDef(String defId) throws Exception {
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

	/**
	 * 移交流程列表
	 *
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "inst/listJson", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获取移交流程列表（带分页信息，DefaultBpmProcessInstance对象）", httpMethod = "POST", notes = "获取移交流程列表（带分页信息，DefaultBpmProcessInstance对象）")
	public PageList<DefaultBpmProcessInstance> listJson(
			@ApiParam(name = "queryFilter", value = "通用查询对象") @RequestBody QueryFilter queryFilter,
			@ApiParam(name = "userId", value = "移交人id", required = true) @RequestParam Optional<String> userId)
			throws Exception {
		queryFilter.addParams("userId", userId.orElse(ContextUtil.getCurrentUserId()));
		PageList<DefaultBpmProcessInstance> list = bpmProcessInstanceManager.queryByuserId(queryFilter);
		return list;
	}

	private void setNoAuthFilter(QueryFilter queryFilter) {
		String ids = "";
		List<BpmDefUser> list = bpmDefUserManager.getByUserMap(BpmDefUser.BPMDEFUSER_OBJ_TYPE.BPM_DEF);
		if (BeanUtils.isNotEmpty(list)) {
			Map<String, String> userRightMap = new HashMap<String, String>();
			for (BpmDefUser bpmDefUser : list) {
				if (StringUtil.isNotEmpty(bpmDefUser.getAuthOrg())) {
					List<BpmDefAct> bpmDefAct = bpmDefActManager.getByAuthorizeId(bpmDefUser.getAuthorizeId());
					if (BeanUtils.isNotEmpty(bpmDefAct)) {
						for (BpmDefAct bpm : bpmDefAct) {
							String type = bpm.getType();
							if ("1".equals(type)) {
								userRightMap.put(bpm.getDefKey(), bpmDefUser.getAuthOrg());
							}
							if ("2".equals(type)) {
								List<String> defKeys = bpmDefinitionManager.queryByTypeId(Arrays.asList(new String[] {bpm.getDefKey()}));
								for (String defKey : defKeys) {
									userRightMap.put(defKey, bpmDefUser.getAuthOrg());
								}
							}
						}
					}
				} else {
					List<BpmDefAct> bpmDefAct = bpmDefActManager.getByAuthorizeId(bpmDefUser.getAuthorizeId());
					if (BeanUtils.isNotEmpty(bpmDefAct)) {
						for (BpmDefAct bpm : bpmDefAct) {
							String type = bpm.getType();
							if ("1".equals(type)) {
								userRightMap.put(bpm.getDefKey(), null);
							}
							if ("2".equals(type)) {
								List<String> defKeys = bpmDefinitionManager.queryByTypeId(Arrays.asList(new String[] {bpm.getDefKey()}));
								for (String defKey : defKeys) {
									userRightMap.put(defKey, null);
								}
							}
						}
					}

				}
			}
			if (BeanUtils.isNotEmpty(userRightMap)) {
				Map<String, Set<String>> maps = ucFeignService.getChildrenIds(userRightMap);
				Map<String, String> rightMap = StringUtil.getMapStringByMapList(maps);

				userRightMap.putAll(rightMap);
				List<DefaultBpmProcessInstance> instanceList = bpmProcessInstanceManager.getListByRightMap(userRightMap);
				if(BeanUtils.isNotEmpty(instanceList)) {
					for(DefaultBpmProcessInstance instance:instanceList) {
						ids += instance.getId() + ",";
					}
				}
			}
		}
		queryFilter.addFilter("ID_", ids, QueryOP.IN);
	}

	/**
	 * 流程实例列表
	 *
	 * @param queryFilter
	 * @param defId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getInstDetailList", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "查询流程实例列表", httpMethod = "POST", notes = "查询流程实例列表")
	public PageList<ObjectNode> getInstDetailList(
			@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter,
			@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestParam Optional<String> defId)
			throws Exception {
		if (StringUtil.isNotEmpty(defId.orElse("")))
			queryFilter.addFilter("proc_def_id_", defId.orElse(""), QueryOP.EQUAL);
		List<QueryField> querys = queryFilter.getQuerys();
		for (Iterator<QueryField> iterator = querys.iterator(); iterator.hasNext();) {
			QueryField queryField = (QueryField) iterator.next();
			if (BeanUtils.isEmpty(queryField.getValue())) {
				iterator.remove();
			} else if ("id".equals(queryField.getProperty())) {// 根据审批人查询
				List<String> instIds = new ArrayList<String>();
				instIds.add("1");
				QueryFilter taskFilter = QueryFilter.<DefaultBpmTask>build();
				taskFilter.addFilter("task.STATUS_", "TRANSFORMING", QueryOP.NOT_EQUAL);
				taskFilter.addFilter("task.STATUS_", "FOLLOW", QueryOP.NOT_EQUAL);
				taskFilter.addFilter("task.ASSIGNEE_ID_", (String) queryField.getValue(), QueryOP.EQUAL);
				PageList<DefaultBpmTask> taskquery = bpmTaskManager.query(taskFilter);
				for (DefaultBpmTask task : taskquery.getRows()) {
					instIds.add(task.getProcInstId());
				}
				queryField.setValue(instIds);
			} else if ("createOrgId".equals(queryField.getProperty())) {// 根据实例申请人所属组织查询。（包含下级组织）
				List<String> orgIds = new ArrayList<String>();
				orgIds.add("1");
				List<ObjectNode> childOrg = new ArrayList<ObjectNode>();
				orgIds.add((String) queryField.getValue());
				try {
					childOrg = ucFeignService.getChildOrg((String) queryField.getValue());
				} catch (Exception e) {
				}
				if (BeanUtils.isNotEmpty(childOrg)) {
					for (ObjectNode objectNode : childOrg) {
						orgIds.add(objectNode.get("id").asText());
					}
				}
				queryField.setValue(orgIds);
			}
		}
		IUser user = ContextUtil.getCurrentUser();
		if (!user.isAdmin()) {
			// 添加数据分类授权
			// getAuthFilter(queryFilter);
			// 去掉分管授权中未授权的实例ID
			setNoAuthFilter(queryFilter);
		}
		PageList<DefaultBpmProcessInstance> instanceList = iFlowService.getInstanceList(baseContext.getCurrentUserAccout(), queryFilter);
		PageList<ObjectNode> result = new PageList<ObjectNode>();
		result.setPage(instanceList.getPage());
		result.setPageSize(instanceList.getPageSize());
		result.setTotal(instanceList.getTotal());
		result.setRows(new ArrayList<ObjectNode>());
		if (BeanUtils.isEmpty(instanceList) || instanceList.getRows().size() == 0) {
			return result;
		}
		Map<String, ObjectNode> instMap = new HashMap<String, ObjectNode>();
		Set<String> creatIdSet = new HashSet<String>();
		// 将所有实例的创建人id放入set集合。
		for (DefaultBpmProcessInstance inst : instanceList.getRows()) {
			creatIdSet.add(inst.getCreateBy());
			instMap.put(inst.getId(), (ObjectNode) JsonUtil.toJsonNode(inst));
		}
		// 调用uc接口传入用户id集合，批量获取用户信息
		Map<String, String> userMap = new HashMap<String, String>();
		ArrayNode userByIdsList = ucFeignService
				.getUserByIdsOrAccounts(StringUtil.join(new ArrayList<String>(creatIdSet), ","));
		for (JsonNode jsonNode : userByIdsList) {
			if (BeanUtils.isEmpty(jsonNode)) {
				continue;
			}
			userMap.put(jsonNode.get("id").asText(), jsonNode.get("fullname").asText());
		}
		// 完善实例申请人姓名信息。
		for (ObjectNode instNode : instMap.values()) {
			if (BeanUtils.isNotEmpty(instNode.get("createBy")) && BeanUtils.isEmpty(instNode.get("creator"))) {
				instNode.put("creator", userMap.get(instNode.get("createBy").asText()));
			}
		}
		// 完善节点执行人信息
		QueryFilter taskFilter = QueryFilter.<DefaultBpmTask>build();
		String instIdList = StringUtil.join(new ArrayList<String>(instMap.keySet()), ",");
		// 过滤掉跟踪类任务、传阅任务和流转源任务
		taskFilter.addFilter("PROC_INST_ID_", instIdList, QueryOP.IN);
		taskFilter.addFilter("task.STATUS_", "TRANSFORMING", QueryOP.NOT_EQUAL);
		taskFilter.addFilter("task.STATUS_", "FOLLOW", QueryOP.NOT_EQUAL);
		taskFilter.addFilter("task.STATUS_", "COPYTO", QueryOP.NOT_EQUAL);
		PageList<DefaultBpmTask> taskList = bpmTaskManager.query(taskFilter);

		for (DefaultBpmTask task : taskList.getRows()) {
			if (!instMap.containsKey(task.getProcInstId())) {
				continue;
			}
			ObjectNode instNode = instMap.get(task.getProcInstId());
			// 完善实例当前节点名称信息
			instNode.put("curNodeName", task.getName());
			instNode.put("taskId", task.getId());
			if (StringUtil.isEmpty(task.getAssigneeName())) {
				continue;
			}
			if (instNode.findValue("excutorName") == null) {
				instNode.put("excutorName", task.getAssigneeName());
				instNode.put("excutorId", "," + task.getAssigneeId() + ",");
			} else {
				// 根据执行人id进行去重
				if (instNode.get("excutorId").asText().indexOf("," + task.getAssigneeId() + ",") < 0) {
					instNode.put("excutorName", instNode.get("excutorName").asText() + "," + task.getAssigneeName());
					instNode.put("excutorId", instNode.get("excutorId").asText() + task.getAssigneeId() + ",");
				}
			}
		}
		// 区分候选人和空执行人。
		BpmTaskCandidateManager bpmTaskCandidateManager = AppUtil.getBean(BpmTaskCandidateManager.class);
		QueryFilter cQueryFilter = QueryFilter.<DefaultBpmTaskCandidate>build();
		cQueryFilter.addFilter("PROC_INST_ID_", instIdList, QueryOP.IN);
		PageList<DefaultBpmTaskCandidate> query = bpmTaskCandidateManager.query(cQueryFilter);
		for (DefaultBpmTaskCandidate taskCandidate : query.getRows()) {
			if (!instMap.containsKey(taskCandidate.getProcInstId())) {
				continue;
			}
			ObjectNode instNode = instMap.get(taskCandidate.getProcInstId());
			if (!(ProcessInstanceStatus.STATUS_MANUAL_END.getKey().equals(instNode.get("status").asText())
					|| ProcessInstanceStatus.STATUS_END.getKey().equals(instNode.get("status").asText()))) {
				instNode.put("isCandidates", true);
				instNode.put("excutorName", "多人");
			}
		}

		// 将格式化之后的数组按照初始instanceList的顺序放入结果集中
		ArrayList<ObjectNode> arrayList = new ArrayList<ObjectNode>();
		for (DefaultBpmProcessInstance inst : instanceList.getRows()) {
			ObjectNode node = instMap.get(inst.getId());
			arrayList.add(node);
		}
		result.setRows(arrayList);
		return result;
	}

	@RequestMapping(value = "getByInstId", method = RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据流程实例ID获取流程实例信息", httpMethod = "GET", notes = "根据流程实例ID获取流程实例信息")
	public DefaultBpmProcessInstance getByInstId(
			@ApiParam(name = "id", value = "是否相关流程", required = true) @RequestParam String id,
			@ApiParam(name = "isRelFlow", required = false) @RequestParam Optional<Boolean> isRelFlow)
			throws Exception {
		if (!isRelFlow.orElse(false)) {
			// 验证权限
			// checkInstAuth(id);
		}
		DefaultBpmProcessInstance instance = bpmProcessInstanceManager.get(id);
		// iFlowService.addReadRecord(instance);
		return instance;
	}

	@RequestMapping(value = "updateFlowOpinions", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "修改审批历史包含沟通", httpMethod = "POST", notes = "修改审批历史包含沟通")
	public CommonResult<String> updateFlowOpinions(
			@ApiParam(name = "opinions", value = "审批历史信息") @RequestBody ObjectNode opinions) throws Exception {
		return bpmCheckOpinionManager.updateFlowOpinions(opinions);
	}

	@RequestMapping(value = "delFlowOpinions", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "删除审批历史包含沟通", httpMethod = "POST", notes = "人工终止流程")
	public CommonResult<String> delFlowOpinions(@ApiParam(name = "id", value = "审批意见id") @RequestParam String id,
			@ApiParam(name = "opinion", value = "删除原因") @RequestBody String opinion) throws Exception {
		return bpmCheckOpinionManager.delFlowOpinions(id, opinion);
	}

	@RequestMapping(value = "saveFormData", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "保存表单数据", httpMethod = "POST", notes = "保存表单数据")
	public CommonResult<String> saveFormData(
			@ApiParam(name = "startFlowParamObject", value = "流程启动参数", required = true) @RequestBody StartFlowParamObject startFlowParamObject)
			throws Exception {
		startFlowParamObject.setAccount(baseContext.getCurrentUserAccout());
		try {
			DefaultBpmProcessInstance defaultBpmProcessInstance = bpmProcessInstanceManager
					.get(startFlowParamObject.getProInstId());
			String busData = "";
			if (StringUtil.isNotEmpty(startFlowParamObject.getData())) {
				busData = Base64.getFromBase64(startFlowParamObject.getData());
			}
			DefaultProcessInstCmd processCmd = new DefaultProcessInstCmd();
			processCmd.setBusData(busData);
			BusDataUtil.handSaveBoData(defaultBpmProcessInstance, processCmd);
			// DefaultProcessInstanceService instanceService =
			// AppUtil.getBean(DefaultProcessInstanceService.class);
			// instanceService.updSubject(defaultBpmProcessInstance, processCmd);
			BpmInterposeRecored bpmInterposeRecored = new BpmInterposeRecored(startFlowParamObject.getProInstId(),
					startFlowParamObject.getExpression(), InterPoseType.MODIFY_DATA,
					startFlowParamObject.getExpression());
			BpmInterposeRecoredManager manager = AppUtil.getBean(BpmInterposeRecoredManager.class);
			manager.create(bpmInterposeRecored);
			return new CommonResult<String>("修改表单数据成功");
		} catch (Exception e) {
			return new CommonResult<String>(false, e.getMessage());
		}
	}

	@RequestMapping(value = "relatedProcess", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "根据申请人获取相关流程", httpMethod = "POST", notes = "根据申请人获取相关流程")
	public PageList<DefaultBpmProcessInstance> relatedProcess(
			@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter)
			throws Exception {
		String auditor = ContextUtil.getCurrentUserId();
		List strauditor = new ArrayList<>();
		IPage<DefaultBpmProcessInstance> list = bpmProcessInstanceManager.getById(auditor, queryFilter);
		if (list.getRecords().size() > 0) {
			for (DefaultBpmProcessInstance instance : list.getRecords()) {
				if (StringUtil.isNotEmpty(instance.getCreateBy())) {
					strauditor.add(instance.getCreateBy());
				}
			}
		}
		List<Map<String, String>> pathNames = FeignServiceUtil.getPathNames(strauditor);
		for (DefaultBpmProcessInstance instance : list.getRecords()) {
			for (Map<String, String> jobNamesMap : pathNames) {
				if (jobNamesMap.get("userId").equals(instance.getCreateBy())) {
					if (StringUtil.isNotEmpty(jobNamesMap.get("pathName"))) {
						String pathName = jobNamesMap.get("pathName").replace("旭辉/旭辉集团/", "");
						instance.setCreateOrgPath(pathName);
					}
					if (StringUtil.isEmpty(instance.getCreator())) {
						instance.setCreator(jobNamesMap.get("fullName"));
					}
				}
			}
		}
		return new PageList<>(list);
	}

	@RequestMapping(value = "getHasAuthFlowList", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获取有权限的流程并将其按照分类名称进行分组", httpMethod = "POST", notes = "获取有权限的流程并将其按照分类名称进行分组")
	public List<ObjectNode> getHasAuthFlowList(
			@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter)
			throws Exception {
		PageList<DefaultBpmDefinition> query = iFlowService.newProcess(baseContext.getCurrentUserAccout(), queryFilter);
		Map<String, ArrayNode> flowMap = new HashMap<>();
		List<ObjectNode> parentTypeList = new ArrayList<>();

		if (BeanUtils.isEmpty(query.getRows())) {
			return parentTypeList;
		}

		for (DefaultBpmDefinition def : query.getRows()) {
			ArrayNode list = null;
			if (flowMap.containsKey(def.getTypeId())) {
				list = flowMap.get(def.getTypeId());
			} else {
				list = JsonUtil.getMapper().createArrayNode();
			}
			list.add(JsonUtil.toJsonNode(def));
			flowMap.put(def.getTypeId(), list);
		}
		PortalFeignService portalFeignService = AppUtil.getBean(PortalFeignService.class);
		QueryFilter typefilter = QueryFilter.build();
		typefilter.addFilter("ID_", StringUtil.join(new ArrayList<>(flowMap.keySet()), ","), QueryOP.IN);
		FieldSort fieldSort = new FieldSort("sn_");
		typefilter.withSorter(fieldSort);
		ObjectNode allSysType = portalFeignService.getAllSysType(typefilter);
		ArrayNode typeList = (ArrayNode) allSysType.get("rows");

		for (JsonNode jsonNode : typeList) {
			ObjectNode typeNode = (ObjectNode) jsonNode;
			if ("6".equals(typeNode.get("parentId").asText())) {
				parentTypeList.add(typeNode);
			}
			typeNode.set("flowList", flowMap.get(jsonNode.get("id").asText()));
		}

		for (JsonNode jsonNode : typeList) {
			for (ObjectNode parentType : parentTypeList) {
				if (jsonNode.get("path").asText().indexOf(parentType.get("path").asText()) == 0
						&& !jsonNode.get("path").asText().equals(parentType.get("path").asText())) {
					ArrayNode flowlit = (ArrayNode) parentType.get("flowList");
					flowlit.addAll((ArrayNode) jsonNode.get("flowList"));
					parentType.set("flowList", flowlit);
					break;
				}
			}
		}
		return parentTypeList;
	}

	@RequestMapping(value = "doNextcommu", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "沟通反馈", httpMethod = "POST", notes = "沟通反馈")
	public CommonResult<String> doNextcommu(
			@ApiParam(name = "doNextParamObject", value = "沟通反馈对象", required = true) @RequestBody DoNextParamObject doNextParamObject,
			HttpServletRequest request) throws Exception {
		return iProcessService.doNextcommu(doNextParamObject);
	}

	@RequestMapping(value = "getRelatedInformationById", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "根据流程实例ID获取相关信息", httpMethod = "GET", notes = "根据流程实例ID获取相关信息")
	public RelatedInformation getRelatedInformationById(@ApiParam(name = "id", required = true) @RequestParam String id)
			throws Exception {
		RelatedInformation relatedInformation = new RelatedInformation();
		DefaultBpmProcessInstance defaultBpmProcessInstance = bpmProcessInstanceManager.get(id);
		if (StringUtil.isNotEmpty(defaultBpmProcessInstance.getCreator())) {
			relatedInformation.setName(defaultBpmProcessInstance.getCreator());
		} else {
			String creatorId = defaultBpmProcessInstance.getCreateBy();
			IUser user = ius.getUserById(creatorId);
			relatedInformation.setName(user.getFullname());
		}
		relatedInformation.setSubject(defaultBpmProcessInstance.getSubject());
		ObjectNode deptobj = ucFeignService.getMainGroup(defaultBpmProcessInstance.getCreateBy());
		if (BeanUtils.isNotEmpty(deptobj)) {
			relatedInformation.setDeptName(deptobj.get("pathName").asText());
		}
		relatedInformation.setTemplate(defaultBpmProcessInstance.getProcDefName());
		return relatedInformation;
	}

	@RequestMapping(value = "getByRecordInstId", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "根据流程实例ID获取阅读记录", httpMethod = "POST", notes = "根据流程实例ID获取阅读记录")
	public PageList<BpmReadRecord> getByRecordInstId(
			@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter<BpmReadRecord> queryFilter)
			throws Exception {
		Map<String, Object> params = new HashMap<>();
		String dbType = SQLUtil.getDbType();
		if ("mysql".equals(dbType)) {
			params.put("dbType", dbType);
		} else if ("oracle".equals(dbType)) {
			params.put("dbType", dbType);
		}

		if (queryFilter.getParams().get("distinct") != null) {
			params.put("distinct", queryFilter.getParams().get("distinct").toString());
		}
		queryFilter.setParams(params);
		return bpmReadRecordManager.query(queryFilter);
	}

	@RequestMapping(value = "isSynchronize", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "根据流程实例ID,任务节点,审批状态验证审批记录", httpMethod = "GET", notes = "根据流程实例ID,任务节点,审批状态验证审批记录")
	public Boolean isSynchronize(@ApiParam(name = "instId", required = false) @RequestParam String instId,
			@ApiParam(name = "nodeIds", required = false) @RequestParam String nodeIds,
			@ApiParam(name = "status", required = false) @RequestParam String status,
			@ApiParam(name = "lastStatus", required = false) @RequestParam String lastStatus,
			@ApiParam(name = "lastNodeIds", required = false) @RequestParam String lastNodeIds) throws Exception {
		Boolean flag = false;
		String dbType = SQLUtil.getDbType();
		String[] nodeId = nodeIds.split(",");
		for (String str : nodeId) {
			DefaultBpmCheckOpinion defaultBpmCheckOpinion = bpmCheckOpinionManager.getBpmOpinion(instId, str, dbType);
			if (status.equals(defaultBpmCheckOpinion.getStatus()) && !lastNodeIds.equals(str)) {
				flag = true;
			} else {
				if (lastNodeIds.equals(str) && lastStatus.equals(status)) {
					flag = true;
				} else {
					flag = false;
					break;
				}
			}
		}
		return flag;
	}

	@RequestMapping(value = "isBackendValidate", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "后端验证", httpMethod = "POST", notes = "后端验证")
	public boolean isBackendValidate(@ApiParam(name = "param", required = false) @RequestParam String param)
			throws Exception {
		if (StringUtil.isNotEmpty(param) && "test".equals(param)) {
			return true;
		} else {
			return false;
		}
	}

	@RequestMapping(value = "isBackendValidateReturnObject", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "后端验证", httpMethod = "POST", notes = "后端验证")
	public Object isBackendValidateReturnObject(@ApiParam(name = "param", required = false) @RequestParam String param,
			@RequestBody ObjectNode formData) throws Exception {
		System.out.println(formData);
		ObjectNode createObjectNode = JsonUtil.getMapper().createObjectNode();
		if (StringUtil.isNotEmpty(param) && "test".equals(param)) {
			createObjectNode.put("valid", true);
			return createObjectNode;
		} else {
			createObjectNode.put("valid", false);
			ObjectNode data = JsonUtil.getMapper().createObjectNode();
			data.put("message", "已经存在");
			createObjectNode.set("data", data);
			return createObjectNode;
		}
	}

	@RequestMapping(value = "doEndProcessById", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "根据流程实例ID终止流程", httpMethod = "GET", notes = "根据流程实例ID终止流程")
	public CommonResult<String> doEndProcessById(@ApiParam(name = "id", required = true, value = "流程实例id") @RequestParam String id,
			                                     @ApiParam(name = "reason", required = false, value = "终止原因") @RequestParam Optional<String> reason)
			throws Exception {
		try {
			List<String> list = bpmProcessInstanceManager.getBpmTaskIdByInstId(id);// 根据流程实例ID获取任务ID
			if (BeanUtils.isEmpty(list)) {
				throw new WorkFlowException("流程实例没有待办任务存在，无需终止");
			}
			DoEndParamObject doEndParamObject = new DoEndParamObject();
			doEndParamObject.setTaskId(list.get(0));
			doEndParamObject.setMessageType("inner");
			doEndParamObject.setAccount(baseContext.getCurrentUserAccout());
			doEndParamObject.setFiles("");
			doEndParamObject.setEndReason(StringUtil.isEmpty(reason.orElse(""))?"流程发起人人工终止":reason.get());
			iProcessService.doEndProcess(doEndParamObject);
			bpmProcessInstanceManager.deleteNotice(id);// 根据流程实例ID删除知会待办记录数据
			return new CommonResult<String>(true, "终止流程成功", "");
		} catch (Exception e) {
			return new CommonResult<String>(false, "终止流程失败", e.getMessage());
		}
	}

	@RequestMapping(value = "getFlowFieldList", method = RequestMethod.POST, produces = {
			"application/json;charset=utf-8" })
	@ApiOperation(value = "获取流程字段信息", notes = "获取流程字段信息", httpMethod = "POST")
	public List<Map<String, Object>> getFlowFieldList(
			@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter)
			throws Exception {
		return bpmProcessInstanceManager.getFlowFieldList(queryFilter);
	}

	@RequestMapping(value = "getUrgentStateConf", method = RequestMethod.POST, produces = {
			"application/json;charset=utf-8" })
	@ApiOperation(value = "获取流程紧急状态配置", notes = "获取流程字段信息", httpMethod = "POST")
	public Map<String, Object> getUrgentStateConf(
			@ApiParam(required = true, name = "conf", value = "参数对象") @RequestBody ObjectNode obj) throws Exception {
		return iProcessService.getUrgentStateConf(obj);
	}

	@RequestMapping(value = "getUrgrntById", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "根据流程实例ID、催办人ID获取催办记录", httpMethod = "POST", notes = "根据流程实例ID、催办人ID获取催办记录")
	public PageList<BpmTaskUrgent> getUrgrntById(
			@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter)
			throws Exception {
		return bpmTaskUrgentManager.query(queryFilter);
	}

	@RequestMapping(value = "getExcutorNameByInstId", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "根据流程实例ID获取当前实例的所有节点和审批人", httpMethod = "GET", notes = "根据流程实例ID获取当前实例的所有节点和审批人")
	public Object getExcutorNameByInstId(@ApiParam(name = "instId", required = false) @RequestParam String instId)
			throws Exception {
		List<String> includeIdList = new ArrayList<String>();
		includeIdList.add(instId);
		return bpmProcessInstanceManager.getNodeApprovalUsers(includeIdList);
	}

    @RequestMapping(value = "sendUrgentByInstId", method = RequestMethod.POST, produces = {
            "application/json;charset=utf-8" })
    @ApiOperation(value = "发送人工催办", notes = "发送人工催办", httpMethod = "POST")
    public CommonResult<String> sendUrgentByInstId(
            @ApiParam(required = true, name = "bpmTaskUrgent", value = "参数对象") @RequestBody BpmTaskUrgent bpmTaskUrgent)
            throws Exception {
        try {
            String subject = "";// 流程标题
            String defKey = "";// 流程定义Key
            String instId = bpmTaskUrgent.getInstId();// 流程实例ID
            // 1.获取任务ID、任务名称、流程标题
            QueryFilter taskFilter = QueryFilter.build();
            for(String key : bpmTaskUrgent.getAppointeeObj().keySet()){
                String nodeId = key;//催办任务节点
                bpmTaskUrgent.setNodeId(nodeId);

                // 过滤掉跟踪类任务、传阅任务和流转源任务
                taskFilter.addFilter("PROC_INST_ID_", instId, QueryOP.IN);
                taskFilter.addFilter("task.STATUS_", "TRANSFORMING", QueryOP.NOT_EQUAL);
                taskFilter.addFilter("task.NODE_ID_", bpmTaskUrgent.getNodeId(), QueryOP.EQUAL);
                taskFilter.addFilter("task.STATUS_", "FOLLOW", QueryOP.NOT_EQUAL);
                taskFilter.addFilter("task.STATUS_", "COPYTO", QueryOP.NOT_EQUAL);
                PageList<DefaultBpmTask> taskList = bpmTaskManager.query(taskFilter);
                // 如果是普通用户任务
                if (BeanUtils.isEmpty(taskList) && taskList.getRows().size() == 0) {
                    return new CommonResult<String>(false, "催办失败，所选节点已无任务存在", "");
                }
                // 是否催办秘书
                boolean isAppointeeSecretary = bpmTaskUrgent.isAppointeeSecretary();
                //获取催办任务节点的被催办人
                String appointeeId = "";//被催办人ID
                String appointeeName = "";//被催办人
                List list = (List) bpmTaskUrgent.getAppointeeObj().get(nodeId);
                for(int i=0;i<list.size();i++){
                    Map<Script,Object> map = (Map<Script, Object>) list.get(i);
                    appointeeId = appointeeId+map.get("userId").toString()+",";
                    appointeeName = appointeeName+map.get("fullname").toString()+",";
                    bpmTaskUrgent.setNodeName(map.get("nodeName").toString());//获取催办任务节点名称
                }
                bpmTaskUrgent.setAppointee(appointeeName.substring(0, appointeeName.length()- 1));
                bpmTaskUrgent.setAppointeeId(appointeeId.substring(0, appointeeId.length()- 1));
                // 被催办人的id集合
                Set<String> appointeeIdSet = new HashSet<>(Arrays.asList(bpmTaskUrgent.getAppointeeId().split(",")));
                defKey = taskList.getRows().get(0).getProcDefKey();
                subject = taskList.getRows().get(0).getSubject();

                Map<String, Set<String>> secretarys = new HashMap<>();
                // 根据所选的催办人和流程key,查找秘书
                if (isAppointeeSecretary) {
                    BpmSecretaryManageManager manageManager = AppUtil.getBean(BpmSecretaryManageManager.class);
                    secretarys = manageManager.getSecretaryByleaderIds(appointeeIdSet, defKey);
                }
                // 如果是普通用户任务
                if (taskList.getRows().size() == 1) {
                    DefaultBpmTask task = taskList.getRows().get(0);
                    bpmTaskUrgent.setTaskId(task.getTaskId());
                    sendPromoterNotice(subject, defKey, bpmTaskUrgent, secretarys);
                } else {// 会签任务
                    for (DefaultBpmTask task : taskList.getRows()) {
                        // 取出当前会签任务的处理人。
                        String assigneeId = task.getAssigneeId();
                        bpmTaskUrgent.setTaskId(task.getTaskId());
                        // 判断任务处理人是否在被催办人里面
                        if (appointeeIdSet.contains(assigneeId)) {
                            Map<String, Set<String>> curTasksecretarys = new HashMap<>();
                            // 判断当前处理人是否有秘书
                            if (secretarys.containsKey(assigneeId)) {
                                curTasksecretarys.put(assigneeId, secretarys.get(assigneeId));
                            }
                            sendPromoterNotice(subject, defKey, bpmTaskUrgent, curTasksecretarys);
                        }
                    }
                }
            }
            return new CommonResult<String>(true, "催办成功", "");
        } catch (Exception e) {
            return new CommonResult<String>(false, "催办失败", e.getMessage());
        }
    }


    @RequestMapping(value = "sendBpmTaskUrgent", method = RequestMethod.POST, produces = {
			"application/json;charset=utf-8" })
	@ApiOperation(value = "发送人工催办", notes = "发送人工催办", httpMethod = "POST")
	public CommonResult<String> sendBpmTaskUrgent(
			@ApiParam(required = true, name = "bpmTaskUrgent", value = "参数对象") @RequestBody BpmTaskUrgent bpmTaskUrgent)
			throws Exception {
		try {
			String subject = "";// 流程标题
			String defKey = "";// 流程定义Key
			String instId = bpmTaskUrgent.getInstId();// 流程实例ID
			// 1.获取任务ID、任务名称、流程标题
			QueryFilter taskFilter = QueryFilter.build();
			// 过滤掉跟踪类任务、传阅任务和流转源任务
			taskFilter.addFilter("PROC_INST_ID_", instId, QueryOP.IN);
			taskFilter.addFilter("task.STATUS_", "TRANSFORMING", QueryOP.NOT_EQUAL);
			taskFilter.addFilter("task.NODE_ID_", bpmTaskUrgent.getNodeId(), QueryOP.EQUAL);
			taskFilter.addFilter("task.STATUS_", "FOLLOW", QueryOP.NOT_EQUAL);
			taskFilter.addFilter("task.STATUS_", "COPYTO", QueryOP.NOT_EQUAL);
			PageList<DefaultBpmTask> taskList = bpmTaskManager.query(taskFilter);
			// 如果是普通用户任务
			if (BeanUtils.isEmpty(taskList) && taskList.getRows().size() == 0) {
				return new CommonResult<String>(false, "催办失败，所选节点已无任务存在", "");
			}
			// 是否催办秘书
			boolean isAppointeeSecretary = bpmTaskUrgent.isAppointeeSecretary();
			// 催办人的id集合
			Set<String> appointeeIdSet = new HashSet<>(Arrays.asList(bpmTaskUrgent.getAppointeeId().split(",")));
			defKey = taskList.getRows().get(0).getProcDefKey();
			subject = taskList.getRows().get(0).getSubject();

			Map<String, Set<String>> secretarys = new HashMap<>();
			// 根据所选的催办人和流程key,查找秘书
			if (isAppointeeSecretary) {
				BpmSecretaryManageManager manageManager = AppUtil.getBean(BpmSecretaryManageManager.class);
				secretarys = manageManager.getSecretaryByleaderIds(appointeeIdSet, defKey);
			}
			// 如果是普通用户任务
			if (taskList.getRows().size() == 1) {
				DefaultBpmTask task = taskList.getRows().get(0);
				bpmTaskUrgent.setTaskId(task.getTaskId());
				sendPromoterNotice(subject, defKey, bpmTaskUrgent, secretarys);
			} else {// 会签任务
				for (DefaultBpmTask task : taskList.getRows()) {
					// 取出当前会签任务的处理人。
					String assigneeId = task.getAssigneeId();
					bpmTaskUrgent.setTaskId(task.getTaskId());
					// 判断任务处理人是否在催办人里面
					if (appointeeIdSet.contains(assigneeId)) {
						Map<String, Set<String>> curTasksecretarys = new HashMap<>();
						// 判断当前处理人是否有秘书
						if (secretarys.containsKey(assigneeId)) {
							curTasksecretarys.put(assigneeId, secretarys.get(assigneeId));
						}
						sendPromoterNotice(subject, defKey, bpmTaskUrgent, curTasksecretarys);
					}
				}
			}
			return new CommonResult<String>(true, "催办成功", "");
		} catch (Exception e) {
			return new CommonResult<String>(false, "催办失败", e.getMessage());
		}
	}

	// 发送催办消息，保存催办记录
	private void sendPromoterNotice(String subject, String defKey, BpmTaskUrgent bpmTaskUrgent,
			Map<String, Set<String>> secretarys) throws IOException {
		bpmTaskUrgent.setUrgrntDate(LocalDateTime.now());
		String instId = bpmTaskUrgent.getInstId();
		// 被催办人的id集合
		Set<String> sendMsgUserIds = new HashSet<>(Arrays.asList(bpmTaskUrgent.getAppointeeId().split(",")));
		if (secretarys != null && !secretarys.isEmpty()) {
			for (Set<String> set : secretarys.values()) {
				sendMsgUserIds.addAll(set);
			}
			bpmTaskUrgent.setAppointeeSecretaryConf(JsonUtil.toJson(secretarys));
		}
		ArrayNode users = ucFeignService.getUserByIdsOrAccounts(StringUtil.join(new ArrayList<>(sendMsgUserIds), ","));
		if (BeanUtils.isEmpty(users)) {
			return;
		}
		String[] accountArray = new String[users.size()];
		for (int i = 0; i < users.size(); i++) {
			ObjectNode jsonNode = (ObjectNode) users.get(i);
			if (BeanUtils.isNotEmpty(jsonNode) && jsonNode.hasNonNull("account")) {
				accountArray[i] = jsonNode.get("account").asText();
			}
		}
		// 解析催办内容
		DefaultBpmProcessInstance instance = bpmProcessInstanceManager.get(instId);
		String creator = instance.getCreator();
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("title", subject);
		vars.put("startorName", creator);
		vars.put("startDate", DateUtil.getCurrentTime("yyyy-MM-dd"));
		// 流程变量
		vars.put("flowKey_", defKey);
		vars.put("instanceId_", instId);
		vars.put("startUser", creator);
		// 可选变量
		vars.put("promoter", bpmTaskUrgent.getPromoter());
		vars.put("appointee", bpmTaskUrgent.getAppointee());
		vars.put("instId", instId);
		vars.put("subject", subject);
		vars.put("sponsor", creator);
		vars.put("nodeName", bpmTaskUrgent.getNodeName());
		String html = BpmUtil.getTitleByRule(bpmTaskUrgent.getContent(), vars);
		bpmTaskUrgent.setContent(html);
		String notifyType = bpmTaskUrgent.getType();
		// TODO 否短信审批 。目前深交所为提供接口和逻辑，暂不实现，改为发送短信
		if (BpmTaskUrgent.TYPR_SMS_APPROVAL.equals(notifyType)) {
			notifyType = BpmTaskUrgent.TYPR_SMS;
		}
		//发送消息
		Notice notice = new Notice();
		notice.setSubject(bpmTaskUrgent.getSubject());
		notice.setContent(bpmTaskUrgent.getContent());
		notice.setUseTemplate(false);
		notice.setMessageTypes(MessageUtil.parseNotifyType(notifyType));
		notice.setSender("");
		notice.setTemplateType("");
		notice.setVars(vars);
		notice.setReceivers(accountArray);
		PortalFeignService PortalFeignService = AppUtil.getBean(PortalFeignService.class);
		bpmTaskUrgentManager.create(bpmTaskUrgent);
		PortalFeignService.sendNoticeToQueue(notice);
	}

	@RequestMapping(value = "doNextCopyto", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "传阅回复", httpMethod = "POST", notes = "传阅回复")
	public CommonResult<String> doNextCopyto(
			@ApiParam(name = "doNextParamObject", value = "传阅回复对象", required = true) @RequestBody DoNextParamObject doNextParamObject,
			HttpServletRequest request) throws Exception {
		return iProcessService.doNextCopyto(doNextParamObject);
	}

	/**
	 * 通用流程明细页面
	 *
	 * @param id
	 * @return
	 * @throws Exception ModelAndView
	 */
	@GetMapping(value = "/getMyOftenFlow")
	@ApiOperation(value = "获取我的常用流程", httpMethod = "GET", notes = "获取我的常用流程")
	public PageList<DefaultBpmDefinition> getMyOftenFlow() throws Exception {
		BpmModelFeignService service = AppUtil.getBean(BpmModelFeignService.class);
		Set<String> myOftenFlowKey = service.getMyOftenFlowKey();

		//2020-6-4 为了提高栏目加载速度，此处先不进行权限判断，在页面启动流程前再进行判断。

		// 获取用户分管授权的流程，只有有权限的流程才能发起
//		BpmDefAuthorizeManager bpmDefAuthorizeManager = AppUtil.getBean(BpmDefAuthorizeManager.class);
//		Map<String, Object> actRightMap = bpmDefAuthorizeManager.getActRightByUserId(baseContext.getCurrentUserId(), BpmDefAuthorizeType.BPMDEFAUTHORIZE_RIGHT_TYPE.START, true, true);
//		String defKeys = (String) actRightMap.get("defKeys");
//		if (StringUtil.isEmpty(defKeys)) {
//			return new PageList<DefaultBpmDefinition>();
//		}
//
//		for (Iterator<String> iterator = myOftenFlowKey.iterator(); iterator.hasNext();) {
//			String defKey = iterator.next();
//			if (defKeys.indexOf("'"+defKey+"'") <0) {
//				iterator.remove();
//			}
//		}

		//判断是否为空
		if(BeanUtils.isEmpty(myOftenFlowKey)) {
			return new PageList<DefaultBpmDefinition>();
		}
		QueryFilter<DefaultBpmDefinition> queryFilter = QueryFilter.<DefaultBpmDefinition>build();
		queryFilter.addFilter("DEF_KEY_", new ArrayList<>(myOftenFlowKey), QueryOP.IN);
		queryFilter.addFilter("IS_MAIN_", "Y", QueryOP.EQUAL);
		PageList<DefaultBpmDefinition> query = bpmDefinitionManager.query(queryFilter);
		QueryFilter<DefaultBpmDefinition> shareFilter = QueryFilter.<DefaultBpmDefinition>build();
		PageList<DefaultBpmDefinition> shareQuery = iFlowService.newProcess(baseContext.getCurrentUserAccout(), shareFilter);
		Map<String, List<BpmIdentity>> leaderMap = new HashMap<>();
		if (BeanUtils.isNotEmpty(shareQuery) && shareQuery.getRows().size() > 0) {
			for (DefaultBpmDefinition def : shareQuery.getRows()) {
				if (BeanUtils.isNotEmpty(def) && BeanUtils.isNotEmpty(def.getLeaders())) {
					leaderMap.put(def.getDefKey(), def.getLeaders());
				}
			}
		}

		if (BeanUtils.isNotEmpty(query) && query.getRows().size() > 0) {
			for (DefaultBpmDefinition def : query.getRows()) {
				if (leaderMap.containsKey(def.getDefKey())) {
					def.setLeaders(leaderMap.get(def.getDefKey()));
				}
			}
		}
		return query;
	}

	@RequestMapping(value = "revokeTrans", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "并行审批任务撤回", httpMethod = "POST", notes = "并行审批任务撤回")
	public CommonResult<String> revokeTrans(
			@ApiParam(required = true, name = "revokeParamObject", value = "流转任务撤销对象") @RequestBody RevokeTransParamObject revokeTransParamObject)
			throws Exception {
		return iFlowService.revokeTrans(revokeTransParamObject);
	}

	@RequestMapping(value = "revokeCustomSign", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "签署撤回", httpMethod = "POST", notes = "顺序签署撤回")
	public CommonResult<String> revokeSignSequence(
			@ApiParam(required = true, name = "revokeParamObject", value = "撤销对象") @RequestBody CustomSignRevokeParam customSignRevokeParam)
			throws Exception {
		revokeHandler.doRevoke(customSignRevokeParam);
		return new CommonResult<String>(true, "撤回成功", "");
	}

	@RequestMapping(value = "revokeSignLine", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "并行签署撤回", httpMethod = "POST", notes = "并行签署撤回")
	public CommonResult<String> revokeSignLine(
			@ApiParam(required = true, name = "revokeParamObject", value = "撤销对象") @RequestBody RevokeSignLineParamObject revokeParamObject)
			throws Exception {
		iFlowService.revokeSignLine(revokeParamObject);
		return new CommonResult<String>(true, "撤回成功", "");
	}

	@RequestMapping(value = "getDefStatus", method = RequestMethod.GET, produces = {
	"application/json; charset=utf-8"})
	@ApiOperation(value = "定义id或者实例id或者任务id获取流程状态", httpMethod = "GET", notes = "定义id或者实例id或者任务id获取流程状态")
	public String getDefStatus(
		@ApiParam(required = false, name = "instId", value = "实例id") @RequestParam Optional<String> instId,
		@ApiParam(required = false, name = "taskId", value = "任务id") @RequestParam Optional<String> taskId,
		@ApiParam(required = false, name = "defId", value = "定义id") @RequestParam Optional<String> defId)
		throws Exception {
		String instanceId = "";
		String definitionId = "";
		if (StringUtil.isNotEmpty(instId.orElse(""))) {
			instanceId = instId.get();
		}
		if (StringUtil.isNotEmpty(defId.orElse(""))) {
			definitionId = defId.get();
		}
		if (StringUtil.isEmpty(instanceId) && StringUtil.isNotEmpty(taskId.get())) {
			DefaultBpmTask defaultBpmTask = bpmTaskManager.get(taskId.get());
			if (BeanUtils.isNotEmpty(defaultBpmTask)) {
				instanceId  = defaultBpmTask.getProcInstId();
			}
		}
		String result = "";
		if (StringUtil.isNotEmpty(instanceId)) {
			BpmProcessInstance instance = bpmProcessInstanceManager.get(instanceId);
			int forbindden = instance.getIsForbidden();
			if (BpmProcessInstance.FORBIDDEN_YES == forbindden){
				result +="1";
			}
			definitionId = instance.getProcDefId();
		}
		BpmDefinition bpmDefinition = bpmDefinitionManager.getById(definitionId);
		if (BeanUtils.isNotEmpty(bpmDefinition)) {
			String status = bpmDefinition.getStatus();
			if (BpmDefinition.STATUS.FORBIDDEN.equals(status) )
			{
				result +="2";
			}else if (BpmDefinition.STATUS.FORBIDDEN_INSTANCE.equals(status)) {
				result +="3";
			}
		}
		return result;
	}

	//获取节点表单
	@RequestMapping(value="printBoAndFormKey",method=RequestMethod.GET, produces = {"application/json; charset=utf-8"})
	public ObjectNode printBoAndFormKey(@ApiParam(required = false, name = "defId", value = "流程定义Id") @RequestParam String defId,
								 @ApiParam(required = false, name = "nodeId", value = "节点Id") @RequestParam String nodeId,
								 @ApiParam(required = false, name = "procInstId", value = "流程实例Id") @RequestParam String procInstId) throws Exception {
		BpmFormService bpmFormService = BpmFormFactory.getFormService(FormType.PC);
		BpmProcessInstance processInstance=bpmProcessInstanceManager.get(procInstId);
		FormModel formModel = bpmFormService.getByDefId(defId, nodeId, processInstance,false);
		List<ObjectNode> boDatas =boDataService.getDataByInst(processInstance);
		ObjectNode jsondata = (ObjectNode)BoDataUtil.hanlerData(boDatas);
		List<ObjectNode> flowOpinionsList = this.getInstanceFlowOpinions(procInstId);
		ArrayNode arrayNode=JsonUtil.getMapper().createArrayNode();
		flowOpinionsList.forEach(item->{
			arrayNode.add(item);
		});
		ObjectNode objectNode= JsonUtil.getMapper().createObjectNode();
		objectNode.put("formName", formModel.getName());
		objectNode.put("formKey", formModel.getFormKey());
		objectNode.set("boData", jsondata);
		objectNode.put("subject",processInstance.getSubject());
		objectNode.set("flowOpinions",arrayNode);
		return objectNode;
	};

	public List<ObjectNode> getInstanceFlowOpinions(String instId) throws Exception{

		List<ObjectNode> listobj=iFlowService.opinionHistory(instId,null);
		List<ObjectNode> listOpinion = new ArrayList<ObjectNode>();
		for (ObjectNode object : listobj) {
			if(!object.get("auditor").isNull() && "-1".equals(object.get("auditor").asText())){//过滤系统抄送审批历史
				continue ;
			}
			listOpinion.add(object);
		}
		for(ObjectNode defaultBpmCheckOpinion : listOpinion) {
			if(OpinionStatus.AWAITING_CHECK.getKey().equals(defaultBpmCheckOpinion.get("status").asText())){
				defaultBpmCheckOpinion.put("completeTime",TimeUtil.getCurrentTime());
			}
		}
		Collections.sort(listOpinion, new Comparator<ObjectNode>() {
			@Override
			public int compare(ObjectNode opinion1, ObjectNode opinion2) {
				if(BeanUtils.isEmpty(opinion1.get("completeTime"))){
					opinion1.put("completeTime",TimeUtil.getCurrentTime());
				}
				return opinion2.get("completeTime").asText().compareTo(opinion1.get("completeTime").asText());
			}

		});
		//处理审批历史
		listOpinion = dealOpinions(listOpinion);
		return listOpinion;
	}

	@RequestMapping(value = "getFlowKey", method = RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取流程key", httpMethod = "GET", notes = "根据定义id或流程实例ID或任务id获取流程key")
	public CommonResult<String> getFlowKey(@ApiParam(name = "defId", required = true, value = "流程定义id") @RequestParam Optional<String> defId,
			@ApiParam(name = "procInstId", required = true, value = "流程实例id") @RequestParam Optional<String> procInstId,
			@ApiParam(name = "taskId", required = true, value = "任务id") @RequestParam Optional<String> taskId)
		throws Exception {
		String defIdStr = defId.orElse("");
		String procInstIdStr = procInstId.orElse("");
		String taskIdStr = taskId.orElse("");
		String flowKey = "";
		if(StringUtil.isEmpty(defIdStr) && StringUtil.isEmpty(procInstIdStr) && StringUtil.isEmpty(taskIdStr)){
			return new CommonResult<String>(false, "请传入流程定义id或流程实例id或任务id获取其流程key。");
		}else if(StringUtil.isNotEmpty(defIdStr)){
			DefaultBpmDefinition def = bpmDefinitionManager.get(defIdStr);
			if(BeanUtils.isNotEmpty(def)){
				flowKey = def.getDefKey();
			}
		}else if(StringUtil.isNotEmpty(procInstIdStr)){
			DefaultBpmProcessInstance instance = bpmProcessInstanceManager.get(procInstIdStr);
			if(BeanUtils.isNotEmpty(instance)){
				flowKey = instance.getProcDefKey();
			}
		}else{
			DefaultBpmTask task = bpmTaskManager.getByTaskId(taskIdStr);
			if(BeanUtils.isNotEmpty(task)){
				flowKey = task.getProcDefKey();
			}else{
				BpmTaskNoticeManager noticeManager = AppUtil.getBean(BpmTaskNoticeManager.class);
				BpmTaskNotice notice = noticeManager.get(taskIdStr);
				if(BeanUtils.isNotEmpty(notice)){
					DefaultBpmDefinition def = bpmDefinitionManager.get(notice.getProcDefId());
					if(BeanUtils.isNotEmpty(def)){
						flowKey = def.getDefKey();
					}
				}
			}
		}
		if(StringUtil.isNotEmpty(flowKey)){
			return new CommonResult<String>(true, "获取流程key成功。",flowKey);
		}else{
			return new CommonResult<String>(false, "获取流程key失败。");
		}
	}

	@RequestMapping(value = "testRestful", method = RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "testRestful", httpMethod = "POST", notes = "testRestful")
	public CommonResult<String> testRestful(
			@ApiParam(required = true, name = "params", value = "params") @RequestBody String params) throws Exception {
		System.out.println("****************************************");
		System.out.println(params);
		System.out.println("****************************************");
		return new CommonResult<String>(true, "成功。");
	}

}
