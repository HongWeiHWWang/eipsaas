package com.hotent.runtime.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.context.BaseContext;
import com.hotent.base.controller.BaseController;
import com.hotent.base.exception.NotFoundException;
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
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.ThreadMsgUtil;
import com.hotent.bpm.api.helper.identity.BpmIdentityExtractService;
import com.hotent.bpm.api.model.form.FormType;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.def.BpmDefinition;
import com.hotent.bpm.api.model.process.def.BpmVariableDef;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.Button;
import com.hotent.bpm.api.model.process.task.BpmTask;
import com.hotent.bpm.api.service.BpmAgentService;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.api.service.BpmInstService;
import com.hotent.bpm.api.service.BpmTaskActionService;
import com.hotent.bpm.api.service.BpmTaskService;
import com.hotent.bpm.model.identity.DefaultBpmIdentity;
import com.hotent.bpm.natapi.task.NatTaskService;
import com.hotent.bpm.persistence.manager.BpmCheckOpinionManager;
import com.hotent.bpm.persistence.manager.BpmCommuReceiverManager;
import com.hotent.bpm.persistence.manager.BpmDefAuthorizeManager;
import com.hotent.bpm.persistence.manager.BpmDefinitionManager;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.persistence.manager.BpmTaskCandidateManager;
import com.hotent.bpm.persistence.manager.BpmTaskCommuManager;
import com.hotent.bpm.persistence.manager.BpmTaskManager;
import com.hotent.bpm.persistence.manager.BpmTaskNoticeDoneManager;
import com.hotent.bpm.persistence.manager.BpmTaskNoticeManager;
import com.hotent.bpm.persistence.model.BpmCommuReceiver;
import com.hotent.bpm.persistence.model.BpmDefAuthorizeType;
import com.hotent.bpm.persistence.model.BpmDefAuthorizeType.BPMDEFAUTHORIZE_RIGHT_TYPE;
import com.hotent.bpm.persistence.model.BpmTaskCommu;
import com.hotent.bpm.persistence.model.BpmTaskNotice;
import com.hotent.bpm.persistence.model.BpmTaskNoticeDone;
import com.hotent.bpm.persistence.model.DefaultBpmCheckOpinion;
import com.hotent.bpm.persistence.model.DefaultBpmDefinition;
import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;
import com.hotent.bpm.persistence.model.DefaultBpmTask;
import com.hotent.bpm.persistence.model.DefaultBpmTaskCandidate;
import com.hotent.bpm.persistence.model.DefaultBpmTaskTurn;
import com.hotent.bpm.persistence.util.ServiceUtil;
import com.hotent.bpm.util.MessageUtil;
import com.hotent.runtime.manager.BpmTaskTransManager;
import com.hotent.runtime.manager.BpmTaskTransRecordManager;
import com.hotent.runtime.manager.IFlowManager;
import com.hotent.runtime.manager.IProcessManager;
import com.hotent.runtime.manager.TaskTransService;
import com.hotent.runtime.model.BpmTaskTransRecord;
import com.hotent.runtime.params.AssignParamObject;
import com.hotent.runtime.params.BpmNodeDefVo;
import com.hotent.runtime.params.BpmTaskResult;
import com.hotent.runtime.params.CommunicateParamObject;
import com.hotent.runtime.params.DoNextParamObject;
import com.hotent.runtime.params.IsAllowAddSignObject;
import com.hotent.runtime.params.ModifyExecutorsParamObject;
import com.hotent.runtime.params.TaskApproveLineParam;
import com.hotent.runtime.params.TaskCommuVo;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 流程任务相关接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */

@RestController
@RequestMapping("/runtime/task/v1/")
@Api(tags="流程任务")
@ApiGroup(group= {ApiGroupConsts.GROUP_BPM})
public class TaskController extends BaseController<BpmTaskManager,DefaultBpmTask> {
	
	@Resource
	IFlowManager iFlowService;
	@Resource
	NatTaskService natTaskService;
	@Resource
	BpmTaskManager bpmTaskManager;
	@Resource
	BpmDefinitionAccessor bpmDefinitionAccessor;
	@Resource
	IUserService userService;
	@Resource
	IProcessManager iProcessService;
	@Resource
	BpmTaskTransRecordManager taskTransRecordManager;
	@Resource
	BpmTaskCommuManager bpmTaskCommuManager;
	@Resource
	BpmInstService bpmInstService;
	@Resource
	BpmDefAuthorizeManager bpmDefAuthorizeManager;
	@Resource
	BpmCommuReceiverManager bpmCommuReceiverManager;
	@Resource
	BpmTaskActionService bpmTaskActionService;
	@Resource
	BpmAgentService bpmAgentService;
	@Resource
	TaskTransService taskTransService;
    @Resource
    BpmProcessInstanceManager bpmProcessInstanceManager;
    @Resource
    IUserService ius;
    @Resource
    BpmTaskTransManager bpmTaskTransManager;
    @Resource
    BpmCheckOpinionManager bpmCheckOpinionManager;
    @Resource
    BpmDefinitionManager bpmDefinitionManager;
    @Resource
    BaseContext baseContext;

    @RequestMapping(value = "getBpmTaskNoticeById", method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "根据主键ID获取待办知会任务信息", httpMethod = "GET", notes = "根据主键ID获取待办知会任务信息")
    public BpmTaskNotice getBpmTaskNoticeById(@ApiParam(required = true, name = "id", value = "待办知会任务主键Id") String id){
        BpmTaskNoticeManager noticeManager = AppUtil.getBean(BpmTaskNoticeManager.class);
        BpmTaskNotice notice = noticeManager.get(id);
        return notice;
    }

    @RequestMapping(value = "getBpmTaskNoticeDoneById", method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "根据主键ID获取已办知会任务信息", httpMethod = "GET", notes = "根据主键ID获取已办知会任务信息")
    public BpmTaskNoticeDone getBpmTaskNoticeDoneById(@ApiParam(required = true, name = "id", value = "已办知会任务主键Id") String id){
        BpmTaskNoticeDoneManager noticeDoneManager = AppUtil.getBean(BpmTaskNoticeDoneManager.class);
        BpmTaskNoticeDone noticeDone = noticeDoneManager.get(id);
        return noticeDone;
    }

    @RequestMapping(value = "getTaskKeyByTaskId", method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "根据任务Id获取审批历史数据（只有一条）", httpMethod = "GET", notes = "根据任务Id获取审批历史数据（只有一条）")
    public DefaultBpmCheckOpinion getTaskKeyByTaskId(@ApiParam(required = true, name = "taskId", value = "任务Id") String taskId){
        DefaultBpmCheckOpinion defaultBpmCheckOpinion = bpmCheckOpinionManager.getTaskKeyByTaskId(taskId);
        return defaultBpmCheckOpinion;
    }

    @RequestMapping(value = "getTaskKeyByNodeId", method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "根据任务节点ID和流程实例ID获取审批历史数据（只有一条）", httpMethod = "GET", notes = "根据任务节点ID和流程实例ID获取审批历史数据（只有一条）")
    public DefaultBpmCheckOpinion getTaskKeyByNodeId(@ApiParam(required = true, name = "nodeId", value = "任务节点Id") String nodeId,
                                                     @ApiParam(required = true, name = "instId", value = "流程实例Id") String instId){
        DefaultBpmCheckOpinion defaultBpmCheckOpinion = bpmCheckOpinionManager.getTaskKeyByNodeId(nodeId,instId);
        return defaultBpmCheckOpinion;
    }

    @RequestMapping(value = "retrieveBpmTask", method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "取回委托/转办流程", httpMethod = "GET", notes = "取回委托/转办流程流程")
    public CommonResult<String> retrieveBpmTask(@ApiParam(required = true, name = "taskId", value = "任务taskId") String taskId){
        IUser user= ContextUtil.getCurrentUser();
        return bpmTaskManager.retrieveBpmTask(user,taskId);
    }

    @RequestMapping(value = "getLeaderTodoList", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "获取用户领导的待办事宜", httpMethod = "POST", notes = "获取用户领导的待办事宜")
    public PageList<DefaultBpmTask> getLeaderTodoList(
            @ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter) throws Exception{
        if(BeanUtils.isNotEmpty(queryFilter.getQuerys()) && queryFilter.getQuerys().size()>0){
            List<QueryField> fields = new ArrayList<>();
            for (Iterator<QueryField> iterator = queryFilter.getQuerys().iterator(); iterator.hasNext();) {
                QueryField field = (QueryField) iterator.next();
                if ("urgentStateValue".equals(field.getProperty())) {
                    field.setGroup("groupUrgent");
                    field.setRelation(FieldRelation.AND);
                    QueryFilter defFilter = QueryFilter.<DefaultBpmDefinition>build();
                    defFilter.addFilter("IS_MAIN_", "Y", QueryOP.EQUAL);
                    defFilter.addFilter("SHOW_URGENT_STATE_", "1", QueryOP.EQUAL);
                    PageList<DefaultBpmDefinition> query = bpmDefinitionManager.query(defFilter);
                    List<String> defKeys = new ArrayList<>();
                    defKeys.add("-1");
                    if (BeanUtils.isNotEmpty(query.getRows())) {
                        for (DefaultBpmDefinition def : query.getRows()) {
                            defKeys.add(def.getDefKey());
                        }
                    }
                    fields.add(new QueryField("PROC_DEF_KEY_", defKeys, QueryOP.IN, FieldRelation.AND, "groupUrgent"));
                }else {
                    fields.add(field);
                }
            }
            queryFilter.setQuerys(fields);
        }

        queryFilter.setGroupRelation(FieldRelation.AND);
        PageList<DefaultBpmTask> pageList = iFlowService.getLeaderTodoList(baseContext.getCurrentUserAccout(), queryFilter);
        return pageList;
    }


    @RequestMapping(value = "getTodoList", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "获取用户的待办事宜", httpMethod = "POST", notes = "获取用户的待办事宜")
	public PageList<DefaultBpmTask> getTodoList(@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter<DefaultBpmTask> queryFilter) throws Exception{
		if(BeanUtils.isNotEmpty(queryFilter.getQuerys()) && queryFilter.getQuerys().size()>0){
			List<QueryField> fields = new ArrayList<>();
			for (Iterator<QueryField> iterator = queryFilter.getQuerys().iterator(); iterator.hasNext();) {
				QueryField field = (QueryField) iterator.next();
				if ("urgentStateValue".equals(field.getProperty())) {
					field.setGroup("groupUrgent");
					field.setRelation(FieldRelation.AND);
					QueryFilter<DefaultBpmDefinition> defFilter = QueryFilter.<DefaultBpmDefinition>build();
					defFilter.addFilter("IS_MAIN_", "Y", QueryOP.EQUAL);
					defFilter.addFilter("SHOW_URGENT_STATE_", "1", QueryOP.EQUAL);
					PageList<DefaultBpmDefinition> query = bpmDefinitionManager.query(defFilter);
					List<String> defKeys = new ArrayList<>();
					defKeys.add("-1");
					if (BeanUtils.isNotEmpty(query.getRows())) {
						for (DefaultBpmDefinition def : query.getRows()) {
							defKeys.add(def.getDefKey());
						}
					}
					fields.add(new QueryField("PROC_DEF_KEY_", defKeys, QueryOP.IN, FieldRelation.AND, "groupUrgent"));
				}else {
					fields.add(field);
				}
			}
			queryFilter.setQuerys(fields);
		}
		
		queryFilter.setGroupRelation(FieldRelation.AND);
        PageList<DefaultBpmTask> pageList = iFlowService.getTodoList(baseContext.getCurrentUserAccout(), queryFilter);
		return pageList;
	}

	@PostMapping(value = "getTodoCount", produces = {"application/json; charset=utf-8"})
	@ApiOperation(value="获取待办数目", httpMethod = "POST", notes = "获取待办数目")
	public List<Map<String,Object>> getTodoCount(@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter) throws Exception{
		return bpmTaskManager.getCountByUserId(baseContext.getCurrentUserId());
	}

	@RequestMapping(value = "getMobileTodoList", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "获取用户手机的待办事宜", httpMethod = "POST", notes = "获取用户手机的待办事宜")
	public PageList<DefaultBpmTask> getMobileTodoList(
			@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter) throws Exception{
		queryFilter.addFilter("bt.SUPPORT_MOBILE_", 1, QueryOP.EQUAL,FieldRelation.AND, "m");
		PageList<DefaultBpmTask> pageList = iFlowService.getTodoList(baseContext.getCurrentUserAccout(), queryFilter);
		return pageList;
	}
	
	@RequestMapping(value = "getDelegate", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "获取用户转办代理事宜", httpMethod = "POST", notes = "获取用户转办代理事宜")
	public PageList<DefaultBpmTaskTurn> getMyDelegate(@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter) throws Exception {
		return iFlowService.getDelegate(baseContext.getCurrentUserAccout(), queryFilter);
	}

	@PostMapping(value = "getDelegateCount", produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "获取用户转办代理事宜数量", httpMethod = "POST", notes = "获取用户转办代理事宜")
	public List<Map<String,Object>> getMyDelegateCount(@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter) throws Exception {
    	return iFlowService.getDelegateCount(baseContext.getCurrentUserAccout(),queryFilter);
	}

	@RequestMapping(value = "getMobileDelegate", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "获取手机用户转办代理事宜", httpMethod = "POST", notes = "获取手机用户转办代理事宜")
	public PageList<DefaultBpmTaskTurn> getMobileDelegate(@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter) throws Exception {
//		queryFilter.addFilter("hi.SUPPORT_MOBILE_", 1, QueryOP.EQUAL,FieldRelation.AND, "m");
		return iFlowService.getDelegate(baseContext.getCurrentUserAccout(), queryFilter);
	}
	
	@RequestMapping(value = "getMyTrans", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "我的流转任务", httpMethod = "POST", notes = "我的流转任务")
	public PageList<BpmTaskTransRecord> getMyTrans(@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter,
			HttpServletResponse response) throws Exception {
		return iFlowService.getMyTrans(baseContext.getCurrentUserAccout(), queryFilter);
	}
	
	@RequestMapping(value = "delegate", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "任务转办", httpMethod = "POST", notes = "任务转办")
	public CommonResult<String> delegate(@ApiParam(required = true, name = "assignParamObject", value = "任务转办参数") @RequestBody AssignParamObject assignParamObject,HttpServletResponse response,
			@ApiParam(name="leaderId",value="领导id", required = false) @RequestParam Optional<String> leaderId) throws Exception{
		
		ckeckCanApproval(assignParamObject.getTaskId(),"");
		if (StringUtil.isNotZeroEmpty(leaderId.orElse(""))) {
			ThreadMsgUtil.addMapMsg("leaderId", leaderId.get());
			if (leaderId.get().equals(assignParamObject.getUserId())) {
				throw new RuntimeException("代领导转办时不能再转办给领导！");
			}
		}
		return iFlowService.delegate(assignParamObject);
	}
	
	@RequestMapping(value = "doCancelTurn", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "取消代理或转办", httpMethod = "POST", notes = "取消代理或转办")
	public CommonResult<String> doCancelTurn(@ApiParam(required = true, name = "assignParamObject", value = "取消代理或转办") @RequestBody AssignParamObject assignParamObject,HttpServletResponse response) throws Exception{
		try
		{
			bpmAgentService.retrieveTask(assignParamObject.getTaskId(), assignParamObject.getMessageType(), assignParamObject.getOpinion());
		} catch (Exception e)
		{
			return new CommonResult<String>(false,"取消任务失败！"+e.getMessage());
		}
		return new CommonResult<String>(true,"取消流转成功！");
	}
	
	@RequestMapping(value = "doRevokeTrans", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "处理撤销流转任务", httpMethod = "POST", notes = "处理撤销流转任务")
	public CommonResult<String> doRevokeTrans(@ApiParam(required = true, name = "withDrawParam", value = "撤销流转任务参数") @RequestBody WithDrawParam withDrawParam,HttpServletResponse response) throws Exception{
		try
		{
			return iFlowService.withDraw(withDrawParam);
		} catch (Exception e)
		{
			return new CommonResult<String>(false,"流转任务取回失败！"+e.getMessage());
		}
	}
	
	@RequestMapping(value = "communicate", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "任务沟通", httpMethod = "POST", notes = "任务沟通")
	public CommonResult<String> communicate(@ApiParam(required = true, name = "communicateParamObject", value = "任务沟通参数") @RequestBody CommunicateParamObject communicateParamObject) throws Exception{
		return iFlowService.communicate(communicateParamObject);
	}
	
	@RequestMapping(value = "taskSignUsers", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "加签", httpMethod = "POST", notes = "加签")
	public CommonResult<String> taskSignUsers(@ApiParam(required = true, name = "signParamObject", value = "任务加签参数") @RequestBody AssignParamObject signParamObject,HttpServletResponse response) throws Exception{
		return iFlowService.taskSignUsers(signParamObject);
	}
	
	@RequestMapping(value = "taskCustomSignUsers", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "添加签署人员", httpMethod = "POST", notes = "添加签署人员")
	public CommonResult<String> taskCustomSignUsers(@ApiParam(required = true, name = "signParamObject", value = "任务加签参数") @RequestBody AssignParamObject signParamObject,HttpServletResponse response) throws Exception{
		return iFlowService.taskCustomSignUsers(signParamObject);
	}
	
	@GetMapping(value="getTaskVar",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation(value = "根据任务taskId, 获取流程变量(全局和节点)", httpMethod = "GET", notes = "根据任务id, 获取流程变量(全局和节点)")
	public Map<String,Object> getTaskVar(@ApiParam(name="taskId",required=true) @RequestParam String taskId){
		return natTaskService.getVariables(taskId);
	}
	
	@GetMapping(value="getTaskVarLocal",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation(value = "根据任务taskId,获取流程任务节点的变量", httpMethod = "GET", notes = "根据任务id,获取流程任务节点的变量")
	public Map<String,Object> getTaskVarLocal(@ApiParam(name="taskId",required=true) @RequestParam String taskId){
		return natTaskService.getVariablesLocal(taskId);
	}
	
	@GetMapping(value="getWorkflowVar",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation(value = "根据流程定义ID或流程定义KEY获取流程变量", httpMethod = "GET", notes = "根据流程定义ID或流程定义KEY获取流程变量")
	public List<BpmVariableDef> getWorkflowVar(@ApiParam(name="json",required=true) @RequestParam String json) throws Exception{
		return iFlowService.getWorkflowVar(json);
	}
	
	
	@PostMapping(value="setTaskVar",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation(value = "根据任务taskId,设置流程变量", httpMethod = "POST", notes = "根据任务taskId,设置流程变量")
	public CommonResult<String> setTaskVar(
			@ApiParam(name="taskId",required=true)  @RequestParam String taskId,
			@ApiParam(name="variables",required=true)   @RequestBody Map<String,Object> variables) throws Exception{
		return iProcessService.setTaskVar(taskId, variables);
	}
	
	@PostMapping(value="setTaskVarLocal",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation(value = "根据任务taskId,设置任务节点本地变量", httpMethod = "POST", notes = "根据任务taskId,设置流程变量")
	public CommonResult<String> setTaskVarLocal(
			@ApiParam(name="taskId",required=true)  @RequestParam String taskId,
			@ApiParam(name="variables",required=true)   @RequestBody Map<String,Object> variables) throws Exception{
		return iProcessService.setTaskVarLocal(taskId, variables);
	}
	
	
	@PostMapping(value="isAllowAddSign",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation(value = "判断用户是否有添加会签权限", httpMethod = "POST", notes = "判断用户是否有添加会签权限")
	public Boolean isAllowAddSign(@RequestBody@ApiParam(name="isAllowAddSignObject",required=true)IsAllowAddSignObject isAllowAddSignObject) throws Exception {
		return iFlowService.isAllowAddSign(isAllowAddSignObject);
	}
	
	@RequestMapping(value = "setTaskExecutors", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "修改任务执行人", httpMethod = "POST", notes = "修改任务执行人")
	public CommonResult<String> setTaskExecutors(@ApiParam(required = true, name = "modifyExecutorsParamObject", value = "修改执行人对象") @RequestBody ModifyExecutorsParamObject modifyExecutorsParamObject)throws Exception {
		return iFlowService.setTaskExecutors(modifyExecutorsParamObject);
	}
	
	@RequestMapping(value="taskToTrans",method=RequestMethod.POST,produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value="保存流转信息（增加流转）",httpMethod="POST",notes="保存流转信息（增加流转）")
	public CommonResult<String> taskToTrans(@ApiParam(name="taskTransParamObject",value="流转参数对象",required=true) @RequestBody TaskTransParamObject taskTransParamObject,
			                                @ApiParam(name="leaderId",value="领导id", required = false) @RequestParam Optional<String> leaderId) throws Exception {
		
		ckeckCanApproval(taskTransParamObject.getTaskId(),"");
		if (StringUtil.isNotZeroEmpty(leaderId.orElse(""))) {
			ThreadMsgUtil.addMapMsg("leaderId", leaderId.get());
		}
		return iFlowService.taskToTrans(taskTransParamObject);
	}

    @RequestMapping(value="userTaskToSign",method=RequestMethod.POST,produces = {
            "application/json; charset=utf-8" })
    @ApiOperation(value="普通任务加签（流转做的）",httpMethod="POST",notes="普通任务加签")
    public CommonResult<String> userTaskToSign(@ApiParam(name="taskTransParamObject",value="加签参数对象",required=true) @RequestBody TaskTransParamObject taskTransParamObject,
                                            @ApiParam(name="leaderId",value="领导id", required = false) @RequestParam Optional<String> leaderId) throws Exception {

        ckeckCanApproval(taskTransParamObject.getTaskId(),"");
        if (StringUtil.isNotZeroEmpty(leaderId.orElse(""))) {
            ThreadMsgUtil.addMapMsg("leaderId", leaderId.get());
        }
        return iFlowService.userTaskToSign(taskTransParamObject);
    }
	
	@RequestMapping(value="taskToSignSequence",method=RequestMethod.POST,produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value="保存顺序签署信息",httpMethod="POST",notes="保存顺序签署信息")
	public CommonResult<String> taskToSignSequence(@ApiParam(name="taskTransParamObject",value="流转参数对象",required=true) @RequestBody TaskTransParamObject taskTransParamObject,
			@ApiParam(name="leaderId",value="领导id", required = false) @RequestParam Optional<String> leaderId) throws Exception {
		
		ckeckCanApproval(taskTransParamObject.getTaskId(),"");
		if (StringUtil.isNotZeroEmpty(leaderId.orElse(""))) {
			ThreadMsgUtil.addMapMsg("leaderId", leaderId.get());
		}
		return iFlowService.taskToSignSequence(taskTransParamObject);
	}
	
	@RequestMapping(value="taskToSignLine",method=RequestMethod.POST,produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value="保存并行签署信息",httpMethod="POST",notes="保存并行签署信息")
	public CommonResult<String> taskToSignLine(@ApiParam(name="taskTransParamObject",value="流转参数对象",required=true) @RequestBody TaskTransParamObject taskTransParamObject,
			@ApiParam(name="leaderId",value="领导id", required = false) @RequestParam Optional<String> leaderId) throws Exception {
		if (StringUtil.isNotZeroEmpty(leaderId.orElse(""))) {
			ThreadMsgUtil.addMapMsg("leaderId", leaderId.get());
		}
		return iFlowService.taskToSignLine(taskTransParamObject);
	}
	
	@RequestMapping(value="taskToApproveLine",method=RequestMethod.POST,produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value="保存并行审批信息",httpMethod="POST",notes="保存并行审批信息")
	public CommonResult<String> taskToApproveLine(@ApiParam(name="taskTransParamObject",value="并行审批参数对象",required=true) @RequestBody TaskApproveLineParam taskApproveLineParam,
			@ApiParam(name="leaderId",value="领导id", required = false) @RequestParam Optional<String> leaderId) throws Exception {
		if (StringUtil.isNotZeroEmpty(leaderId.orElse(""))) {
			ThreadMsgUtil.addMapMsg("leaderId", leaderId.get());
		}
		return iFlowService.taskToApproveLine(taskApproveLineParam);
	}
	
	
	@RequestMapping(value="getTaskByTaskId",method=RequestMethod.GET,produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value="通过任务id获取任务对象",httpMethod="GET",notes="通过任务id获取任务对象")
	public BpmTaskResult getTaskByTaskId(@ApiParam(name="taskId",value="任务id",required=true) @RequestParam String taskId) throws Exception{
		return iProcessService.getTaskByTaskId(taskId);
	}
	
	@RequestMapping(value="getTaskNameByTaskId",method=RequestMethod.GET,produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value="通过任务id获取任务名称",httpMethod="GET",notes="通过任务id获取任务名称")
	public String getTaskNameByTaskId(@ApiParam(name="taskId",value="任务id",required=true) @RequestParam String taskId) throws Exception{
		return iProcessService.getTaskNameByTaskId(taskId);
	}
	
	@RequestMapping(value="getTasksByInstId",method=RequestMethod.GET,produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value="通过实例id获取任务列表",httpMethod="GET",notes="通过实例id获取任务列表")
	public PageList<DefaultBpmTask> getTasksByInstId(@ApiParam(name="instId",value="实例id",required=true) @RequestParam String instId) throws Exception{
		return iProcessService.getTasksByInstId(instId);
	}
	
	@RequestMapping(value = "getNextTaskUsers", method = RequestMethod.GET, produces = {
		"application/json; charset=utf-8" })
	@ApiOperation(value="根据任务id获取下一环节处理人",notes="根据任务id获取下一环节处理人",httpMethod="GET")
	public Map<String, List<BpmIdentity>> getNextTaskUsers(@ApiParam(name="taskId",value="任务id",required=true) @RequestParam String taskId) throws Exception {
	return iFlowService.getNextTaskUsers(taskId);
	}

	@RequestMapping(value="getApprovalItems",method= RequestMethod.GET,produces={"application/json; charset=utf-8"})
	@ApiOperation(value="根据任务id获取预先设置的审批用语列表",notes="根据任务id获取预先设置的审批用语列表",httpMethod="GET")
	public List<String> getApprovalItems(@ApiParam(name="taskId",value="任务id",required=true) @RequestParam String taskId) throws Exception {
	return iProcessService.getApprovalItems(taskId);
	}
	
	@RequestMapping(value="getTaskOutNodes",method=RequestMethod.GET,produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value="通过任务id获取任务的后续节点",httpMethod="GET",notes="通过任务id获取任务的后续节点")
	public List<BpmNodeDefVo> getTaskOutNodes(@ApiParam(name="taskId",value="任务id",required=true) @RequestParam String taskId) throws Exception{
		return iProcessService.getTaskOutNodes(taskId);
	}
	

	@GetMapping(value="getUrlFormByTaskId",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation(value = "获取任务的在线表单地址", httpMethod = "GET", notes = "获取任务的在线表单地址")
	public String getUrlFormByTaskId(
			@ApiParam(name="taskId",required=true)  @RequestParam String taskId,
			@ApiParam(name="formType",required=true,defaultValue="pc")  @RequestParam String formType) throws Exception{
		return iFlowService.getUrlFormByTaskId( taskId,formType );
	}
	
	@GetMapping(value="getInstUrlForm",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation(value = "获取实例在线表单", httpMethod = "GET", notes = "获取实例在线表单")
	public String getInstUrlForm(
			@ApiParam(name="proInstId",required=true) @RequestParam String proInstId, 
			@ApiParam(name="nodeId",required=false) @RequestParam(required=false) String nodeId,
			@ApiParam(name="formType",required=true,defaultValue="pc")  @RequestParam  String formType ) throws Exception{
		return iFlowService.getInstUrlForm(proInstId,nodeId,formType);
	}
	
	@RequestMapping(value="taskDoNext",method=RequestMethod.GET,produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value="管理员处理任务页面",httpMethod="GET",notes="管理员处理任务页面")
	public TaskDoNextVo taskDoNext(@ApiParam(name="taskId",value="任务id",required=true) @RequestParam String taskId) throws Exception {
		return iFlowService.taskDoNext(taskId);
	}
	
	@RequestMapping(value="taskApprove",method=RequestMethod.GET,produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value="获取用户审批界面参数",httpMethod="GET",notes="获取用户审批界面参数")
	public TaskDoNextVo taskApprove(@ApiParam(name="taskId",value="任务id",required=true) @RequestParam String taskId) throws Exception {
		return iFlowService.taskApprove(taskId);
	}
	
	@RequestMapping(value="taskImage",method=RequestMethod.GET,produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value="获取查看任务流程图参数",httpMethod="GET",notes="获取查看任务流程图界面参数")
	public TaskjImageVo taskImage(
			@ApiParam(name="taskId",value="任务id") @RequestParam Optional<String> taskId,
			@ApiParam(name="defId",value="流程定义id") @RequestParam Optional<String> defId) throws Exception {
		return iFlowService.taskImage(taskId.orElse(""),defId.orElse(""));
	}
	
	@RequestMapping(value="nodeOpinion",method=RequestMethod.GET,produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value="获取流程实例中指定节点的审批意见",httpMethod="GET",notes="获取流程实例中指定节点的审批意见")
	public Object nodeOpinion(
			@ApiParam(name="defId",value="流程定义id",required=true) @RequestParam Optional<String> defId,
			@ApiParam(name="instId",value="流程实例id",required=true) @RequestParam Optional<String> instId,
			@ApiParam(name="nodeId",value="任务节点id，多个以逗号拼接",required=true) @RequestParam String nodeId) throws Exception {
		Map<String, Object> map= new HashMap<>();
		if (BeanUtils.isNotEmpty(nodeId)) {
			String[] split = nodeId.split(",");
			for (String id : split) {
				Object nodeOpinion = iFlowService.nodeOpinion(defId.orElse(""),instId.orElse(""),id);
				if (split.length==1) {
					return nodeOpinion;
				}
				map.put(id, nodeOpinion);
			}
		}
		return map;
	}
	
	/**
	 * @param taskId
	 * @param leaderId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="getButtonsBytaskId",method=RequestMethod.GET,produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value="根据任务ID获取审批按钮",httpMethod="GET",notes="根据任务ID获取审批按钮")
	public TaskDetailVo getButtonsBytaskId(@ApiParam(name="taskId",value="任务id",required=true) @RequestParam String taskId,
			                               @ApiParam(name="leaderId",value="代为处理的领导id",required=true) @RequestParam Optional<String> leaderId) throws Exception {
		/*if (StringUtil.isNotZeroEmpty(leaderId.orElse(""))) {
			ThreadMsgUtil.addMapMsg("leaderId", leaderId.get());
		}*/
		TaskDetailVo buttonsBytaskId = iFlowService.getButtonsBytaskId(taskId);
		if(BeanUtils.isNotEmpty(buttonsBytaskId.getButtons())) {

            for (Iterator<Button> iterator = buttonsBytaskId.getButtons().iterator(); iterator.hasNext(); ) {
                Button button = iterator.next();
                String btnName = button.getAlias();
                if (StringUtil.isNotZeroEmpty(leaderId.orElse("")) && !btnName.equals("agree") && !btnName.equals("reject") && !btnName.equals("lockUnlock")) {
                    iterator.remove();
                }
            }
            return buttonsBytaskId;
        }else{
		    return new TaskDetailVo();
        }
	}

    @RequestMapping(value="taskDetail",method=RequestMethod.GET,produces = {
            "application/json; charset=utf-8" })
    @ApiOperation(value="获取任务的详情",httpMethod="GET",notes="获取任务的详情")
    public TaskDetailVo taskDetail(@ApiParam(name="taskId",value="任务id",required=true) @RequestParam String taskId,
                                   @ApiParam(name="reqParams",value="请求参数",required=true) @RequestParam String reqParams,
                                   @ApiParam(name="leaderId",value="代为处理的领导id",required=false) @RequestParam Optional<String> leaderId) throws Exception {
        return iFlowService.taskDetail(taskId,reqParams,FormType.PC,leaderId.orElse(""));
    }
	
	@RequestMapping(value="taskDetailBo",method=RequestMethod.GET,produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value="获取任务的详情",httpMethod="GET",notes="获取任务的详情")
	public TaskDetailVo taskDetailBo(@ApiParam(name="taskId",value="任务id",required=true) @RequestParam String taskId,
			@ApiParam(name="reqParams",value="请求参数",required=true) @RequestParam String reqParams,
			@ApiParam(name="leaderId",value="代为处理的领导id",required=false) @RequestParam Optional<String> leaderId) throws Exception {
		return iFlowService.taskDetail(taskId,reqParams,FormType.PC,leaderId.orElse("").equals("0")?"":leaderId.orElse(""));
	}

	@RequestMapping(value="taskMobileDetail",method=RequestMethod.GET,produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value="获取手机任务的详情",httpMethod="GET",notes="获取手机任务的详情")
	public TaskDetailVo taskMobileDetail(@ApiParam(name="taskId",value="任务id",required=true) @RequestParam String taskId,
			@ApiParam(name="reqParams",value="请求参数",required=true) @RequestParam String reqParams) throws Exception {
		return iFlowService.taskDetailMobile(taskId,reqParams,FormType.MOBILE);
	}
	
	@RequestMapping(value="getMyTasks",method=RequestMethod.GET,produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value="获取我的待办，并且进行条件过滤",httpMethod="GET",notes="获取我的待办，并且进行条件过滤")
	public PageList<DefaultBpmTask> getMyTasks(
			@ApiParam(name="account",value="用户账号",required=true) @RequestParam String account,
			@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter queryFilter) throws Exception {
		IUser user = ServiceUtil.getUserByAccount(account);
		return (PageList<DefaultBpmTask>) bpmTaskManager.getByUserId(user.getUserId(), queryFilter);
	}
	
	@RequestMapping(value="getTaskEnt",method=RequestMethod.GET,produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value="根据任务id获取待办",httpMethod="GET",notes="根据任务id获取待办")
	public BpmTask getTaskEnt(
			@ApiParam(name="taskId",value="任务id",required=true) @RequestParam String taskId) throws Exception {
		return bpmTaskManager.get(taskId);
	}
	
	@RequestMapping(value="getTaskVars",method=RequestMethod.GET,produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value="获取任务上下文流程变量",httpMethod="GET",notes="获取任务上下文流程变量")
	public Map<String, Object> getTaskVars(
			@ApiParam(name="taskId",value="任务id",required=true) @RequestParam String taskId) throws Exception {
		return iFlowService.getTaskVars(taskId,null);
	}
	
	@RequestMapping(value="complete",method=RequestMethod.POST,produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value="处理任务",httpMethod="POST",notes="处理任务")
	public CommonResult<String> complete(@ApiParam(name="doNextParamObject",value="流转参数对象",required=true) @RequestBody DoNextParamObject doNextParamObject) throws Exception {
		return iFlowService.complete(doNextParamObject);
	}
	
	@RequestMapping(value="saveDraft",method=RequestMethod.POST,produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value="保存草稿",httpMethod="POST",notes="保存草稿")
	public CommonResult<String> saveDraft(@ApiParam(name="doNextParamObject",value="流转参数对象",required=true) @RequestBody DoNextParamObject doNextParamObject) throws Exception {
		return iFlowService.saveDraft(doNextParamObject);
	}
	
	@RequestMapping(value="list", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "任务列表(分页条件查询)数据", httpMethod = "POST", notes = "获取任务列表")
	public PageList<DefaultBpmTask> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter queryFilter) throws Exception {
		IUser user = ContextUtil.getCurrentUser();
		//当前人是否超管
		boolean isAdmin=user.isAdmin();
		queryFilter.addParams("isAdmin", isAdmin?1:0);
		//普通用户先获取授权流程及权限。
		if (!isAdmin) {
			// 获得流程分管授权与用户相关的信息
			Map<String, Object> actRightMap = bpmDefAuthorizeManager.getActRightByUserId(user.getUserId(), BPMDEFAUTHORIZE_RIGHT_TYPE.TASK, true, true);
			// 获得流程分管授权与用户相关的信息集合的流程KEY
			String defKeys = (String) actRightMap.get("defKeys");
			if (StringUtil.isNotEmpty(defKeys)){
				queryFilter.addParams("defKeys", defKeys);
			}
		}
		PageList<DefaultBpmTask> query = bpmTaskManager.query(queryFilter);
		if (BeanUtils.isEmpty(query) || BeanUtils.isEmpty(query.getRows())) {
			return  query;
		}
		Map<String, List<DefaultBpmTaskCandidate>> cMap = new HashMap<>();
		for (DefaultBpmTask task : query.getRows()) {
			if (StringUtil.isEmpty(task.getAssigneeName())) {
				cMap.put(task.getTaskId(), new ArrayList<>());
			}
		}
		Set<String> keySet = cMap.keySet();
		if (BeanUtils.isNotEmpty(keySet)) {
			QueryFilter<DefaultBpmTaskCandidate> cQueryFilter = QueryFilter.build();
			cQueryFilter.addFilter("task_id_", StringUtil.join(keySet,","), QueryOP.IN);
			BpmTaskCandidateManager candidateManager = AppUtil.getBean(BpmTaskCandidateManager.class);
			PageList<DefaultBpmTaskCandidate> candidateQuery = candidateManager.query(cQueryFilter);
			if (BeanUtils.isEmpty(candidateQuery) || BeanUtils.isEmpty(candidateQuery.getRows())) {
				return  query;
			}
			for (DefaultBpmTaskCandidate candidate : candidateQuery.getRows()) {
				cMap.get(candidate.getTaskId()).add(candidate);
			}
			for (DefaultBpmTask task : query.getRows()) {
				if (cMap.containsKey(task.getTaskId()) && BeanUtils.isNotEmpty(cMap.get(task.getTaskId()))) {
					List<DefaultBpmTaskCandidate> list = cMap.get(task.getTaskId());
					List<BpmIdentity> identityList = new ArrayList<>();
					for (DefaultBpmTaskCandidate candidate : list) {
						identityList.add(new DefaultBpmIdentity(candidate.getExecutor(),"",candidate.getType()));
					}
					task.setIdentityList(identityList);
				}
			}
		}
		
		return  query;
	}
	
	@RequestMapping(value="get",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取任务明细", httpMethod = "GET", notes = "获取任务明细")
	public TaskGetVo get(@ApiParam(name="id",value="任务id", required = true) @RequestParam String id) throws Exception{
		return iFlowService.getTaskById(id);
	}

    @RequestMapping(value="getNotice",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
    @ApiOperation(value = "获取任务明细", httpMethod = "GET", notes = "获取任务明细")
    public TaskGetVo getNotice(@ApiParam(name="id",value="任务id", required = true) @RequestParam String id) throws Exception{
        return iFlowService.getTaskById(id);
    }
	
	@RequestMapping(value="taskToAgree",method=RequestMethod.GET,produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value="任务办理(同意、反对、弃权)",httpMethod="GET",notes="任务办理(同意、反对、弃权)")
	public TaskToAgreeVo taskToAgree(
			@ApiParam(name="taskId",value="任务id",required=true) @RequestParam String taskId,
			@ApiParam(name="actionName",value="审批动作",required=true) @RequestParam String actionName) throws Exception {
		return iFlowService.toAgree(taskId,actionName);
	}
	
	@RequestMapping(value="taskToReject",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "驳回任务页面参数", httpMethod = "GET", notes = "驳回任务页面参数")
	public TaskToRejectVo taskToReject(
			@ApiParam(name="taskId",value="任务id", required = true) @RequestParam String taskId,
			@ApiParam(name="backModel",value="驳回模式：reject、backToStart") @RequestParam String backModel) throws Exception{
		return iFlowService.toReject(taskId,backModel);
	}
	
	@RequestMapping(value="handlerTypes",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取支持的消息处理类型", httpMethod = "GET", notes = "获取支持的消息处理类型")
	public Map<String, String> getHandlerTypes() throws Exception{
		return MessageUtil.getHandlerTypes();
	}
	
	@RequestMapping(value="getTaskTransById",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据流转任务id明细", httpMethod = "GET", notes = "根据流转任务id明细")
	public BpmTaskTransRecord getTaskTransById(@ApiParam(name="id",value="流转任务id", required = true) @RequestParam String id) throws Exception{
		return taskTransRecordManager.get(id);
	}
	
	@RequestMapping(value="getTransRecordList",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取任务的流转记录明细", httpMethod = "GET", notes = "获取任务的流转记录明细")
	public List<BpmTaskTransRecord> getTransRecordList(@ApiParam(name="taskId",value="任务id", required = true) @RequestParam String taskId) throws Exception{
		QueryFilter queryFilter = QueryFilter.<BpmTaskTransRecord>build()
				                             .withPage(new PageBean(1, Integer.MAX_VALUE))
				                             .withParam("taskId", taskId);
		return taskTransRecordManager.getTransRecordList(queryFilter);
	}
	
	@RequestMapping(value="withDraw",method=RequestMethod.POST,produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value="撤销我流转出去的任务",httpMethod="POST",notes="撤销我流转出去的任务")
	public CommonResult<String> withDraw(@ApiParam(name="taskId",value="任务id",required=true) @RequestBody WithDrawParam withDrawParam) throws Exception {
		return iFlowService.withDraw(withDrawParam);
	}
	
	@RequestMapping(value="remove",method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "删除任务记录", httpMethod = "DELETE", notes = "删除任务记录")
	public CommonResult<String> remove(@ApiParam(name="ids",value="任务记录ID，多个用“,”号分隔", required = true) @RequestParam String ids) throws Exception{
		String[] aryIds = null;
		if(!StringUtil.isEmpty(ids)){
			aryIds = ids.split(",");
		}
		bpmTaskManager.removeByIds(aryIds);
		return new CommonResult<String>(true,"删除任务成功","");
	}
	
	@RequestMapping(value="getTaskCommu",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取沟通反馈任务", httpMethod = "GET", notes = "通过沟通任务id获取沟通反馈任务")
	public TaskCommuVo getTaskCommu(@ApiParam(name="taskId",value="任务id", required = true) @RequestParam String taskId) throws Exception{
		BpmTask bpmTask = bpmTaskManager.get(taskId);
		BpmTaskCommu taskCommu = bpmTaskCommuManager.getByTaskId(bpmTask.getParentId());
		List<BpmCommuReceiver> commuReceivers = null; // 回复消息的
		if (taskCommu != null) {
			commuReceivers = bpmCommuReceiverManager.getByCommuStatus(taskCommu.getId(), null);
		}
		return new TaskCommuVo(taskCommu, commuReceivers);
	}
	
	@RequestMapping(value="canLock",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取任务是否能锁定", httpMethod = "GET", notes = "获取任务是否能锁定，返回当前任务的可操作状态：0:任务已经处理,1:可以锁定,2:不需要解锁 ,3:可以解锁，4,被其他人锁定,5:这种情况一般是管理员操作，所以不用出锁定按钮")
	public int canLock(@ApiParam(name="taskId",value="任务id", required = true) @RequestParam String taskId,
			           @ApiParam(name="leaderId",value="领导id", required = true) @RequestParam Optional<String> leaderId) throws Exception{
		DefaultBpmTask task = bpmTaskManager.get(taskId);
		if(task==null){
			return 0;
		}
		boolean isForbindden = bpmInstService.isSuspendByInstId(task.getProcInstId());
		if(isForbindden){//流程已经被禁止
			return 6;
		}
		int rtn = bpmTaskManager.canLockTask(taskId,leaderId.orElse(""));
		// 判断权限
		return rtn;
	}
	
	@RequestMapping(value="isForbindden",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取流程任务是否已被禁用", httpMethod = "GET", notes = "获取流程任务是否已被禁用：1、流程已经被禁止，2、任务不存在，3、没有处理此任务的权限。")
	public int isForbindden(@ApiParam(name="taskId",value="任务id", required = true) @RequestParam String taskId) throws Exception{
		DefaultBpmTask task = bpmTaskManager.get(taskId);
		if(task == null) return 2; //任务不存在，可能已经被处理！
		
		boolean isForbindden = bpmInstService.isSuspendByInstId(task.getProcInstId());
		IUser user=ContextUtil.getCurrentUser();
		if(!user.isAdmin()){
			ObjectNode jsonObj = bpmDefAuthorizeManager.getRight(task.getProcDefKey(), BpmDefAuthorizeType.BPMDEFAUTHORIZE_RIGHT_TYPE.TASK);
			if(jsonObj == null && !ContextUtil.getCurrentUserId().equals(task.getAssigneeId()) ) return 3;//没有处理此任务的权限!
		}
		if(isForbindden){//流程已经被禁止
			return 1;
		}else{
			return 0;
		}
	}
	
	@RequestMapping(value="lockUnlock",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取任务锁定状态", httpMethod = "GET", notes = "获取任务锁定状态：0:任务已经处理,1:可以锁定,2:不需要解锁 ,3:可以解锁，4,被其他人锁定。")
	public int lockUnlock(@ApiParam(name="taskId",value="任务id", required = true) @RequestParam String taskId,
			              @ApiParam(name="leaderId",value="领导id", required = false) @RequestParam Optional<String> leaderId) throws Exception{
		//0:任务已经处理,1:可以锁定,2:不需要解锁 ,3:可以解锁，4,被其他人锁定 
		String curUserId=ContextUtil.getCurrentUserId();
		if (StringUtil.isNotZeroEmpty(leaderId.orElse(""))) {
			ThreadMsgUtil.addMapMsg("leaderId", leaderId.get());
			curUserId = leaderId.get();
		}
		int rtn = bpmTaskManager.canLockTask(taskId);
		if(rtn==0 ||  rtn==4 ||  rtn==2 || rtn==5){
			return rtn;
		}
		
		//锁定
		if(rtn==1){
			bpmTaskManager.lockTask(taskId, curUserId);
		}
		//解锁
		else{
			bpmTaskManager.unLockTask(taskId);
		}
		
		return rtn;
	}
	
	@RequestMapping(value="getCandidatesListByInstId",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据流程实例获取其下的候选人列表", httpMethod = "GET", notes = "根据流程实例获取其下的候选人列表")
	public List<BpmIdentity> getCandidatesListByInstId(@ApiParam(name="instId",value="流程实例id", required = true) @RequestParam String instId) throws Exception{
		QueryFilter queryFilter = QueryFilter.<DefaultBpmTask>build();
		queryFilter.addFilter("PROC_INST_ID_", instId, QueryOP.EQUAL);
		queryFilter.addFilter("task.STATUS_","TRANSFORMING" ,QueryOP.NOT_EQUAL);
		PageList<DefaultBpmTask> query = bpmTaskManager.query(queryFilter);
		if (query.getRows().size() !=1 ) {
			return null;
		}
		DefaultBpmTask defaultBpmTask = query.getRows().get(0);
		BpmTaskService bpmTaskService  = AppUtil.getBean(BpmTaskService.class);
		List<BpmIdentity> bpmIdentities = bpmTaskService.getTaskCandidates(defaultBpmTask.getTaskId());
		return bpmIdentities;
	}
	
	@RequestMapping(value="taskNode",method=RequestMethod.GET,produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value="获取可跟踪的任务节点",httpMethod="GET",notes="获取可跟踪的任务节点")
	public TaskjImageVo taskNode(
			@ApiParam(name="taskId",value="任务id") @RequestParam Optional<String> taskId,
			@ApiParam(name="defId",value="流程定义id") @RequestParam Optional<String> defId) throws Exception {
		TaskjImageVo taskNode = iFlowService.taskImage(taskId.orElse(""),defId.orElse(""));
		return taskNode;
	}
	
	@RequestMapping(value="addSign",method=RequestMethod.POST,produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value="加签",httpMethod="POST",notes="加签")
	public CommonResult<String> addSign(@ApiParam(name="taskTransParamObject",value="流转参数对象",required=true) @RequestBody TaskTransParamObject taskTransParamObject) throws Exception {
		return taskTransService.addSign(taskTransParamObject);
	}

    @RequestMapping(value="isEnd",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
    @ApiOperation(value = "流程是否结束", httpMethod = "POST", notes = "流程是否结束")
    public CommonResult<String> isEnd(@ApiParam(name="procInstId",value="流程实例id", required = true) @RequestParam String procInstId) throws Exception{
        try {
            DefaultBpmProcessInstance defaultBpmProcessInstance = bpmProcessInstanceManager.get(procInstId);
            if(BeanUtils.isNotEmpty(defaultBpmProcessInstance)){
                if(!defaultBpmProcessInstance.getStatus().equals("end")){
                    return new CommonResult<>(true,"");
                }else{
                    return new CommonResult<>(false,"流程实例已结束!");
                }
            }else{
                return new CommonResult<>(false,"流程实例不存在");
            }
        } catch (Exception e) {
            return new CommonResult<>(false,e.getMessage());
        }
    }

    @RequestMapping(value="taskToInqu",method=RequestMethod.POST,produces = {
            "application/json; charset=utf-8" })
    @ApiOperation(value="征询设置",httpMethod="POST",notes="征询设置")
    public CommonResult<String> taskToInqu(@ApiParam(name="taskTransParamObject",value="征询参数对象",required=true) @RequestBody TaskTransParamObject taskTransParamObject,
    		                               @ApiParam(name="leaderId",value="代为处理的领导id",required=true) @RequestParam Optional<String> leaderId) throws Exception {
    	ckeckCanApproval(taskTransParamObject.getTaskId(),"");
    	if (StringUtil.isNotZeroEmpty(leaderId.orElse(""))) {
			ThreadMsgUtil.addMapMsg("leaderId", leaderId.get());
		}
    	return iFlowService.taskToInqu(taskTransParamObject);
    }

    @RequestMapping(value="taskToInquReply",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
    @ApiOperation(value = "征询回复", httpMethod = "POST", notes = "征询回复")
    public CommonResult<String> taskToInquReply(@ApiParam(name="dbo",value="回复信息", required = true) @RequestBody DefaultBpmCheckOpinion dbo,
    		                                    @ApiParam(name="leaderId",value="代为处理的领导id",required=true) @RequestParam Optional<String> leaderId) throws Exception{
        try {
        	ckeckCanApproval(dbo.getTaskId(),"");
        	if (StringUtil.isNotZeroEmpty(leaderId.orElse(""))) {
    			ThreadMsgUtil.addMapMsg("leaderId", leaderId.get());
    		}
            bpmTaskTransManager.taskToInquReply(dbo);
            return new CommonResult<>(true,"征询回复成功");
        } catch (Exception e) {
            return new CommonResult<>(false,"征询回复失败："+e.getMessage());
        }
    }

    @RequestMapping(value="addReadRecord",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
    @ApiOperation(value = "根据所传任务id新增该任务的阅读记录", httpMethod = "POST", notes = "根据所传任务id新增该任务的阅读记录")
    public void addReadRecord(@ApiParam(name="taskId",value="任务id", required = true) @RequestParam String taskId) throws Exception{
        DefaultBpmTask bpmTask = bpmTaskManager.get(taskId);
        if (BeanUtils.isEmpty(bpmTask)) {
            bpmTask = new DefaultBpmTask();
            BpmTaskNoticeManager noticeManager = AppUtil.getBean(BpmTaskNoticeManager.class);
            BpmTaskNotice bpmTaskNotice = noticeManager.get(taskId);
            if (BeanUtils.isEmpty(bpmTaskNotice)) {
                throw new NotFoundException("根据所传任务id:"+taskId+"未找到任务！");
            }
            bpmTask = bpmTaskNotice.convertToBpmTask();
        }
        iFlowService.addReadRecord(bpmTask);//添加阅读记录
        bpmCheckOpinionManager.checkOpinionIsRead(bpmTask.getId());//根据任务ID修改任务为已阅
    }

    @RequestMapping(value="noticeTurnDode",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
    @ApiOperation(value = "知会任务待办转已办", httpMethod = "POST", notes = "知会任务待办转已办")
    public void noticeTurnDode(@ApiParam(name="taskId",value="知会任务主键id集合", required = true) @RequestParam String taskId) throws Exception{
        String[] val = taskId.split(",");
        for(String id:val){
            iFlowService.noticeTurnDode(id);
        }
    }

    @RequestMapping(value="getCurNodeProperties",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
    @ApiOperation(value = "获取当前节点属性,启动的时候取发起节点", httpMethod = "GET", notes = "获取当前节点属性,启动的时候取发起节点")
    public BpmNodeDef getCurNodeProperties(@ApiParam(name="taskId",value="任务id", required = false) @RequestParam Optional<String> taskId,
    		                    @ApiParam(name="defId",value="定义id", required = false) @RequestParam Optional<String> defId,
    		                    @ApiParam(name="instId",value="流程实例id", required = false) @RequestParam Optional<String> instId) throws Exception{
       return iFlowService.getCurNodeProperties(taskId.orElse(""),defId.orElse(""),instId.orElse(""));
    }
    
    
    
    @RequestMapping(value="getAfterJumpNodes",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
    @ApiOperation(value = "根据节点配置获取后续可跳转节点", httpMethod = "POST", notes = "根据节点配置获取后续可跳转节点")
    public ObjectNode getAfterJumpNodes(@ApiParam(name="taskId",value="任务id", required = true) @RequestBody ObjectNode obj ) throws Exception{
       return iFlowService.getAfterJumpNode( obj );
    }

    @RequestMapping(value="getNoticeTodoReadList",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
    @ApiOperation(value = "获取待阅任务（知会任务）", httpMethod = "POST", notes = "获取待阅任务（知会任务）")
    public PageList<BpmTaskNotice> getNoticeTodoReadList(@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter) throws Exception{
        PageList<BpmTaskNotice> pageList = iFlowService.getNoticeTodoReadList(baseContext.getCurrentUserAccout(), queryFilter);
        return pageList;
    }

	@PostMapping(value="getNoticeTodoReadCount", produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取待阅任务（知会任务）数量", httpMethod = "POST", notes = "获取待阅任务（知会任务）数量")
	public List<Map<String,Object>> getNoticeTodoReadCount(@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter) throws Exception{
		return iFlowService.getNoticeTodoReadCount(baseContext.getCurrentUserAccout(), queryFilter);
	}

    @RequestMapping(value="getNoticeDoneReadList",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
    @ApiOperation(value = "获取已阅任务（知会任务）", httpMethod = "POST", notes = "获取已阅任务（知会任务）")
    public PageList<BpmTaskNoticeDone> getNoticeDoneReadList(@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter) throws Exception{
        //JAVA 8
        List<QueryField> boys = queryFilter.getQuerys();
        Optional<QueryField> queryFieldOptional= boys.stream().filter(s->s.getProperty().equals("inst.PROC_DEF_KEY_")).findFirst();
        if (queryFieldOptional.isPresent()) {// 判断是否存在 inst.PROC_DEF_KEY_ 这个查询条件
            QueryField queryField = queryFieldOptional.get();
            //通过流程定义KEY获取流程定义信息
            DefaultBpmDefinition po = bpmDefinitionManager.getMainByDefKey(queryFieldOptional.get().getValue().toString());
            //通过流程定义ID查询已阅任务
            queryFilter.addFilter("bpm_task_notice_done.PROC_DEF_ID_", po.getDefId(), QueryOP.EQUAL);
            //删除通过流程定义key获取已阅任务的参数
            queryFilter.getQuerys().remove(queryFieldOptional.get());
        }
	    PageList<BpmTaskNoticeDone> pageList = iFlowService.getNoticeDoneReadList(baseContext.getCurrentUserAccout(), queryFilter);
        return pageList;
    }

	@PostMapping(value="getNoticeDoneReadCount", produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取待阅任务（知会任务）数量", httpMethod = "POST", notes = "获取待阅任务（知会任务）数量")
	public List<Map<String,Object>> getNoticeDoneReadCount(@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter) throws Exception{
		return iFlowService.getNoticeDoneReadCount(baseContext.getCurrentUserAccout(), queryFilter);
	}

    @RequestMapping(value="getMyNoticeReadList",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
    @ApiOperation(value = "获取我传阅的任务（知会任务）", httpMethod = "POST", notes = "获取我传阅的任务（知会任务）")
    public PageList<BpmTaskNotice> getMyNoticeReadList(@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter) throws Exception{
        PageList<BpmTaskNotice> pageList = iFlowService.getMyNoticeReadList(baseContext.getCurrentUserAccout(), queryFilter);
        return pageList;
    }

	@PostMapping(value="getMyNoticeReadCount", produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取待阅任务（知会任务）数量", httpMethod = "POST", notes = "获取待阅任务（知会任务）数量")
	public List<Map<String,Object>> getMyNoticeReadCount(@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter) throws Exception{
		return iFlowService.getMyNoticeReadCount(baseContext.getCurrentUserAccout(), queryFilter);
	}

    @RequestMapping(value="delBpmTaskNoticeById",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
    @ApiOperation(value = "根据ID主键ID删除传阅任务", httpMethod = "POST", notes = "根据ID主键ID删除传阅任务")
    public CommonResult<String> delBpmTaskNoticeById(@ApiParam(name="id",value="主键ID", required = true) @RequestParam String id) throws Exception{
        try {
            iFlowService.delBpmTaskNoticeById(id);
            return new CommonResult<>(true,"撤回成功");
        } catch (Exception e) {
            return new CommonResult<>(false,"撤回失败："+e.getMessage());
        }
    }
    
    @RequestMapping(value="nextExecutor",method=RequestMethod.GET,produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value="获取顺序签署下一步执行人",httpMethod="GET",notes="获取顺序签署下一步执行人")
	public CommonResult<BpmIdentity> nextExecutor(@ApiParam(name="taskId",value="任务id",required=true) @RequestParam String taskId) throws Exception {
		return iFlowService.nextExecutor(taskId);
	}
    
    @RequestMapping(value="testRevoke",method=RequestMethod.GET,produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value="顺签撤回",httpMethod="GET",notes="顺签撤回")
	public CommonResult<BpmIdentity> testRevoke(@ApiParam(name="taskId",value="任务id",required=true) @RequestParam String taskId
			,@ApiParam(name="customSignTaskId",value="任务id",required=true) @RequestParam String customSignTaskId
			,@ApiParam(name="instId",value="instId",required=true) @RequestParam String instId) throws Exception {
		 /**
		  * 顺签An-1撤回An 的验证
    	bpmTaskManager.sequentialTaskRevoke(taskId, customSignTaskId);
		  */
    	/*
    	 * 
    	 * 并行审批 An 撤回验证
    	bpmTaskManager.approvalTaskRevoke(taskId, customSignTaskId);
    	 */
    	/**
    	 * 并审 A 撤回A1 ... An 
    	 * 
    	 */
		 return new CommonResult<>(true,"成功");
	}
	
	//获取代办任务列表
	@RequestMapping(value="getBpmTaskByInstId",method=RequestMethod.GET,produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value="根据流程实例Id获取代办任务",httpMethod="GET",notes="根据流程实例Id获取代办任务")
	public List<DefaultBpmTask> getBpmTaskByInstId(@ApiParam(name="instId",value="流程实例Id",required=true) @RequestParam String instId) throws Exception {
		IUser iUser=userService.getUserByAccount(baseContext.getCurrentUserAccout());
		QueryFilter queryFilter=QueryFilter.build().withPage(new PageBean(1, Integer.MAX_VALUE));
		queryFilter.addFilter("bt.proc_inst_id_", instId, QueryOP.EQUAL);
		return bpmTaskManager.getByUserId(iUser.getUserId(),queryFilter).getRows();
	}
	
	/**
	 * 根据流程实例id或者任务id，判断当前实例是否可以审批
	 * @param taskId
	 * @param instId
	 * @return
	 */
	private void ckeckCanApproval(String taskId, String instId) {
		if (StringUtil.isEmpty(instId) && StringUtil.isNotEmpty(taskId)) {
			DefaultBpmTask defaultBpmTask = baseService.get(taskId);
			if (BeanUtils.isEmpty(defaultBpmTask)) {
				return;
			}
			instId = defaultBpmTask.getProcInstId();
		}
		BpmProcessInstance instance = bpmProcessInstanceManager.get(instId);
		if (BeanUtils.isEmpty(instance)) {
			return;
		}
		if (BpmProcessInstance.FORBIDDEN_YES == instance.getIsForbidden()) {
			throw new RuntimeException("流程实例已被挂起，无法审批");
		}
		BpmDefinition bpmDefinition = bpmDefinitionManager.getById(instance.getProcDefId());
		if (BeanUtils.isNotEmpty(bpmDefinition)
				&& BpmDefinition.STATUS.FORBIDDEN_INSTANCE.equals(bpmDefinition.getStatus())) {
			throw new RuntimeException("流程定义已被禁止实例，无法审批");
		}
	}
	
	@RequestMapping(value="getCandidatesListByTaskId",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据任务id获取其下的候选人列表并抽取执行人", httpMethod = "GET", notes = "根据任务id获取其下的候选人列表并抽取执行人")
	public ArrayNode getCandidatesListByTaskId(@ApiParam(name="taskId",value="流程实例id", required = true) @RequestParam String taskId) throws Exception{
		BpmTaskService bpmTaskService  = AppUtil.getBean(BpmTaskService.class);
		List<BpmIdentity> bpmIdentities = bpmTaskService.getTaskCandidates(taskId);
		ArrayNode bpmIdentitiesArray = JsonUtil.getMapper().createArrayNode();
		if (BeanUtils.isEmpty(bpmIdentities)) {
			return bpmIdentitiesArray;
		}
		BpmIdentityExtractService bpmIdentityExtractService = AppUtil.getBean(BpmIdentityExtractService.class);
		for (BpmIdentity identity : bpmIdentities) {
			ObjectNode identityObj = (ObjectNode) JsonUtil.toJsonNode((DefaultBpmIdentity)identity);
			bpmIdentitiesArray.add(identityObj);
			
			if (BpmIdentity.TYPE_USER.equals(identity.getType())) {
				continue;
			}
			List<BpmIdentity> tempList = new ArrayList<>();
			tempList.add(identity);
			List<IUser> extractUser = bpmIdentityExtractService.extractUser(tempList);
			if (BeanUtils.isNotEmpty(extractUser)) {
				List<String> userNames = new ArrayList<>();
				List<String> userIds = new ArrayList<>();
				for (IUser iUser : extractUser) {
					userNames.add(iUser.getFullname());
					userIds.add(iUser.getUserId());
				}
				identityObj.put("userNames", StringUtil.join(userNames, ","));
				identityObj.put("userIds", StringUtil.join(userIds, ","));
			}
		}
		return bpmIdentitiesArray;
	}
	
	@RequestMapping(value="getTaskListByTenantId",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据租户id获取任务列表", httpMethod = "GET", notes = "根据租户id获取任务列表")
	public List<ObjectNode> getTaskListByTenantId(@ApiParam(name="tenantId",value="租户id", required = true) @RequestParam String tenantId) throws Exception{
		return bpmTaskManager.getTaskListByTenantId(tenantId);
	}
	
	@RequestMapping(value = "getLeaderTodoCount", produces = {"application/json; charset=utf-8"})
    @ApiOperation(value="获取领导待办数目", httpMethod = "GET", notes = "获取领导待办数目")
    public List<Map<String,Object>> getLeaderTodoCount() throws Exception{
        return bpmTaskManager.getLeaderCountByUserId(baseContext.getCurrentUserId());
    }


}
