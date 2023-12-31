package com.hotent.runtime.manager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.bpm.api.model.form.FormType;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.def.BpmVariableDef;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.engine.inst.DefaultProcessInstCmd;
import com.hotent.bpm.persistence.model.BpmTaskNotice;
import com.hotent.bpm.persistence.model.BpmTaskNoticeDone;
import com.hotent.bpm.persistence.model.CopyTo;
import com.hotent.bpm.persistence.model.DefaultBpmDefinition;
import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;
import com.hotent.bpm.persistence.model.DefaultBpmTask;
import com.hotent.bpm.persistence.model.DefaultBpmTaskTurn;
import com.hotent.runtime.model.BpmTaskTransRecord;
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

/**
 * 流程的相关接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
public interface IFlowManager {

    /**
     * 获取用户领导的待办事宜
     * @param account
     * @param queryFilter
     * @return
     * @throws Exception
     */
    PageList<DefaultBpmTask> getLeaderTodoList(String account, QueryFilter queryFilter) throws Exception;


    /**
     * 获取用户待办事宜
     * @param account
     * @param queryFilter
     * @return
     * @throws Exception
     */
    PageList<DefaultBpmTask> getTodoList(String account, QueryFilter<DefaultBpmTask> queryFilter) throws Exception;

	/**
	 * 获取待办列表查询条件
	 * @return
	 * @throws Exception
	 */
	QueryFilter getTodoQueryFilter(QueryFilter filter) throws Exception;

    /**
     * 根据ID主键ID删除传阅任务
     * @param id
     */
    void delBpmTaskNoticeById(String id) throws Exception;

    /**
     * 获取我传阅的任务（知会任务）
     * @param account
     * @param queryFilter
     * @return
     * @throws Exception
     */
    PageList<BpmTaskNotice> getMyNoticeReadList(String account, QueryFilter queryFilter) throws Exception;

	/**
	 * 获取我传阅的任务（知会任务）在各分类下的数量
	 * @param account
	 * @param queryFilter
	 * @return
	 * @throws Exception
	 */
    List<Map<String,Object>> getMyNoticeReadCount(String account, QueryFilter queryFilter) throws Exception;

    /**
     * 获取待阅任务（知会任务）
     * @param account
     * @param queryFilter
     * @return
     * @throws Exception
     */
    PageList<BpmTaskNotice> getNoticeTodoReadList(String account, QueryFilter queryFilter) throws Exception;

	/**
	 *获取待阅在各分类下的数量
	 * @return
	 * @throws Exception
	 */
	List<Map<String,Object>> getNoticeTodoReadCount(String account, QueryFilter filter) throws Exception;

    /**
     * 获取已阅任务（知会任务）
     * @param account
     * @param queryFilter
     * @return
     * @throws Exception
     */
    PageList<BpmTaskNoticeDone> getNoticeDoneReadList(String account, QueryFilter queryFilter) throws Exception;

	/**
	 * 获取已阅任务（知会任务）在各分类下的数量
	 * @param account
	 * @param queryFilter
	 * @return
	 * @throws Exception
	 */
    List<Map<String,Object>> getNoticeDoneReadCount(String account,QueryFilter queryFilter) throws Exception;
	
	/**
	 * 获取用户的已办事宜
	 * @param account
	 * @param queryFilter
	 * @param status
	 * @return
	 * @throws Exception
	 */
	PageList<Map<String,Object>> getDoneList(String account, QueryFilter queryFilter,String status) throws Exception;
	
	/**
	 * 获取办结事宜。
	 * @param account
	 * @param queryFilter
	 * @return
	 * @throws Exception
	 */
	PageList<DefaultBpmProcessInstance> getCompletedList(String account, QueryFilter queryFilter) throws Exception;
	
	/**
	 * 获取用户的办结事宜。
	 * @param account
	 * @param queryFilter
	 * @return
	 * @throws Exception
	 */
	PageList<DefaultBpmProcessInstance> getMyCompletedList(String account, QueryFilter queryFilter) throws Exception;
	
	
	/**
	 * 我的请求
	 * @param account
	 * @param queryFilter
	 * @return
	 * @throws Exception
	 */
	PageList<DefaultBpmProcessInstance> getMyRequestList(String account, QueryFilter queryFilter) throws Exception;
	
	/**
	 * 获取用户可发起的流程
	 * @param account
	 * @param queryFilter
	 * @param typeId
	 * @return
	 * @throws Exception
	 */
	PageList<DefaultBpmDefinition> getMyFlowList(String account, QueryFilter queryFilter,String typeId) throws Exception;
	

	/**
	 * 获取用户的草稿列表
	 * @param account
	 * @param queryFilter
	 * @return
	 * @throws Exception
	 */
	PageList<DefaultBpmProcessInstance> getMyDraftList(String account, QueryFilter queryFilter) throws Exception;
	
	
 
	/**
	 * 根据流程定义ID或流程定义KEY获取流程变量
	 * 
	 * @param json {defId:"流程定义ID",defKey:"流程定义key"}
	 * @return
	 */
	List<BpmVariableDef> getWorkflowVar(String json) throws Exception;
	
	/**
	 * 获取用户的抄送转发事宜
	 * @param account
	 * @param queryFilter
	 * @param type
	 * @return
	 * @throws Exception
	 */
	PageList<CopyTo> getReceiverCopyTo(String account, QueryFilter queryFilter,String type) throws Exception ;
	
	/**
	 * 由我发出的抄送
	 * @param account
	 * @param queryFilter
	 * @return
	 * @throws Exception
	 */
	PageList<CopyTo> myCopyTo(String account, QueryFilter queryFilter) throws Exception ;
	
	/**
	 * 获取用户转办代理事宜
	 * @param account
	 * @param queryFilter
	 * @return
	 * @throws Exception
	 */
	PageList<DefaultBpmTaskTurn> getDelegate(String account, QueryFilter queryFilter) throws Exception;

	List<Map<String,Object>> getDelegateCount(String account, QueryFilter queryFilter) throws Exception;
	
	/**
	 * 我的流转任务
	 * @param account
	 * @param queryFilter
	 * @return
	 * @throws Exception
	 */
	PageList<BpmTaskTransRecord> getMyTrans(String account, QueryFilter queryFilter) throws Exception;
	
	/**
	 * 根据id删除草稿
	 * @param id
	 * @return
	 */
	CommonResult<String> removeDraftById(String runId) throws Exception;

	/**
	 * 任务转办
	 * @param assignParamObject
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> delegate(AssignParamObject assignParamObject) throws Exception;
	
	/**
	 * 任务沟通
	 * @param communicateParamObject
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> communicate(CommunicateParamObject communicateParamObject) throws Exception;
	
	/**
	 * 流程任务加签，增加会签人员
	 * @param assignParamObject
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> taskSignUsers(AssignParamObject assignParamObject) throws Exception;
	
	/**
	 *   添加签署人员 （根据加签代码修改）
	 * @param assignParamObject
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> taskCustomSignUsers(AssignParamObject assignParamObject) throws Exception;
	
	/**
	 * 根据实例id撤回流程（撤销）
	 * @param instanceId
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> revokeInstance(RevokeParamObject revokeParamObject) throws Exception;
	
//	/**
//	 * 根据流程定义key获取流程的所有节点信息
//	 * @param defKey
//	 * @return
//	 * @throws Exception
//	 */
//	List<BpmNodeDefVo> getNodesByDefKey(String defKey) throws Exception ;
	
	/**
	 * 根据任务id获取下一环节处理人
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	Map<String, List<BpmIdentity>> getNextTaskUsers(String taskId) throws Exception;
	
	/**
	 * 修改任务执行人
	 * @param modifyExecutorsParamObject
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> setTaskExecutors(ModifyExecutorsParamObject modifyExecutorsParamObject) throws Exception;
	
	/**
	 * 判断用户是否有添加会签权限
	 * @param json
	 * @return
	 * @throws Exception
	 */
	Boolean isAllowAddSign(IsAllowAddSignObject isAllowAddSignObject) throws Exception;
	
	/**
	 * 保存流转信息（增加流转）
	 * @param taskTransParamObject
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> taskToTrans(TaskTransParamObject taskTransParamObject) throws Exception;

    /**
     * 普通任务加签
     * @param taskTransParamObject
     * @return
     * @throws Exception
     */
    CommonResult<String> userTaskToSign(TaskTransParamObject taskTransParamObject) throws Exception;
	
	/**
	 * 保存并行审批信息
	 * @param taskApproveLineParam
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> taskToApproveLine(TaskApproveLineParam taskApproveLineParam) throws Exception;
	
	/**
	 * 获取处理任务的 在线表单地址
	 * @param taskId
	 * @param formType
	 * @return
	 * @throws Exception 
	 */
	String getUrlFormByTaskId(String taskId, String formType) throws Exception;
	
	/**
	 * 获取实例表单  
	 *  如果nodeId 不为空，先获取节点配置的实例表单
	 *  否则获取全局的实例表单地址
	 * @param proInstId
	 * @param nodeId
	 * @param formType  pc/mobile
	 * @return
	 * @throws Exception 
	 */
	String getInstUrlForm(String proInstId, String nodeId, String formType) throws Exception;
	
	/**
	 * 我的请求（包括人工终止和结束状态的实例）
	 * @param account
	 * @param queryFilter
	 * @return
	 * @throws Exception
	 */
	PageList<DefaultBpmProcessInstance> getMyRequestListAll(String account, QueryFilter queryFilter) throws Exception;
	
	/**
	 * 查询流程定义列表
	 * @param account
	 * @param queryFilter
	 * @return
	 * @throws Exception
	 */
	PageList<DefaultBpmDefinition> getBpmDefList(String account, QueryFilter queryFilter) throws Exception;
	
	/**
	 * 新建流程列表
	 * @param account
	 * @param queryFilter
	 * @return
	 * @throws Exception
	 */
	PageList<DefaultBpmDefinition> newProcess(String account, QueryFilter queryFilter) throws Exception;

	/**
	 * 流程分类下的数量
	 * @param queryFilter
	 * @return
	 * @throws Exception
	 */
	List<Map<String,Object>> newProcessCount(QueryFilter queryFilter) throws Exception;
	
	/**
	 * 我的请求列表
	 * @param account
	 * @param queryFilter
	 * @return
	 * @throws Exception
	 */
	PageList<DefaultBpmProcessInstance> myRequest(String account, QueryFilter queryFilter) throws Exception;

	List<Map<String,Object>> myRequestCount(String account) throws Exception;
	
	/**
	 * 查询流程实例列表
	 * @param account
	 * @param queryFilter
	 * @return
	 * @throws Exception
	 */
	PageList<DefaultBpmProcessInstance> getInstanceList(String account, QueryFilter queryFilter) throws Exception;
	
	/**
	 * 获取流程实例的表单和数据
	 * @param proInstId
	 * @param defKey
	 * @return
	 * @throws Exception
	 */
	InstFormAndBoVo getInstFormAndBO(String proInstId,String nodeId,String formId,FormType formType,Boolean includData,Boolean getStartForm) throws Exception;
	
	/**
	 * 流程启动时获取bo和表单。
	 * @param startCmdParam
	 * @return
	 * @throws Exception
	 */
	FormAndBoVo getFormAndBO(StartCmdParam startCmdParam,FormType formType) throws Exception;
	
	/**
	 * 获取发起的cmd格式数据
	 * @param request
	 * @param startCmdParam
	 * @return
	 * @throws Exception
	 */
	DefaultProcessInstCmd getStartCmd(StartCmdParam startCmdParam) throws Exception;

	/**
	 * 获取可以选择的路径
	 * @param defId
	 * @return
	 * @throws Exception
	 */
	SelectDestinationVo selectDestination(String defId) throws Exception;
	
	/**
	 * 节点图片
	 * @param proInstId
	 * @param type
	 * @param from
	 * @param nodeId
	 * @param defId
	 * @return
	 * @throws Exception
	 */
	FlowImageVo flowImage(String proInstId, String type, String from, String nodeId,String defId) throws Exception;
	
	/**
	 * 流程审批历史（页面数据）
	 * @param instId
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	List<ObjectNode> opinionHistory(String instId, String taskId) throws Exception;
	
	/**
	 * 获取任务处理页面参数
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	TaskDoNextVo taskDoNext(String taskId) throws Exception;
	
	/**
	 * 获取用户审批界面参数
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	TaskDoNextVo taskApprove(String taskId) throws Exception;
	
	/**
	 * 获取任务流程图页面参数
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	TaskjImageVo taskImage(String taskId,String defId) throws Exception;
	
	/**
	 * 获取流程实例中指定节点的审批意见
	 * @param instId
	 * @param nodeId
	 * @return
	 * @throws Exception
	 */
	Object nodeOpinion(String defId,String instId,String nodeId) throws Exception;
	
	/**
	 * 获取任务的详情
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	TaskDetailVo taskDetail(String taskId,String reqValue,FormType formType,String leaderId) throws Exception;

    TaskDetailVo taskDetailMobile(String taskId,String reqValue,FormType formType) throws Exception;
	
	/**
	 * 获取任务上下文流程变量
	 * @param taskId
	 * @param vars
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getTaskVars(String taskId,Map<String,String> vars) throws Exception;
	
	/**
	 * 处理任务
	 * @param doNextParamObject
	 * @param reqValue
	 * @param usersMap
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> complete(DoNextParamObject doNextParamObject) throws Exception;
	
	/**
	 * 保存草稿
	 * @param doNextParamObject
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> saveDraft(DoNextParamObject doNextParamObject) throws Exception;
	
	/**
	 * 获取任务任务明细
	 * @param taskId
	 * @param reqValue
	 * @return
	 * @throws Exception
	 */
	TaskGetVo getTaskById(String taskId) throws Exception;
	
	/**
	 * 任务办理(同意、反对、弃权)
	 * @param taskId
	 * @param actionName
	 * @return
	 * @throws Exception
	 */
	TaskToAgreeVo toAgree(String taskId, String actionName) throws Exception;
	
	/**
	 * 驳回任务页面参数
	 * @param taskId
	 * @param actionName
	 * @return
	 * @throws Exception
	 */
	TaskToRejectVo toReject(String taskId, String backModel) throws Exception;
	
	/**
	 * 撤销我流转出去的任务
	 * @param withDrawParam
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> withDraw(WithDrawParam withDrawParam) throws Exception;

	List<ObjectNode> opinionHistory(String instId,String taskId,boolean isCommu) throws IOException;

    /**
     * 征询设置
     * @param taskTransParamObject
     * @return
     * @throws Exception
     */
    CommonResult<String> taskToInqu(TaskTransParamObject taskTransParamObject) throws Exception;

    //添加阅读记录
    void addReadRecord(Object obj) throws Exception;

    //知会任务待办转已办
    void noticeTurnDode(String taskId);

	ObjectNode getAfterJumpNode(ObjectNode obj ) throws Exception;

	BpmNodeDef getCurNodeProperties(String orElse, String orElse2,String instId) throws Exception;
	
	/**
	 * 获取流程字段信息
	 * @param account
	 * @param queryFilter
	 * @param status
	 * @return
	 * @throws Exception
	 */
	PageList<Map<String,Object>> getFlowFieldList(QueryFilter queryFilter) throws Exception;

	PageList<Map<String, Object>> getDoneInstList(String current, QueryFilter queryFilter, String status) throws Exception;

	List<Map<String,Object>> getDoneInstCount(String current,QueryFilter queryFilter,String status) throws Exception;

	CommonResult<String> taskToSignSequence(
			TaskTransParamObject taskTransParamObject) throws Exception;
	
	/**
	 * 撤回流转出去的任务
	 * @param revokeParamObject
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> revokeTrans(RevokeTransParamObject revokeTransParamObject) throws Exception;

	void revokeSignSequence(
			RevokeTransParamObject revokeTransParamObject) throws Exception;

	CommonResult<String> taskToSignLine(
			TaskTransParamObject taskTransParamObject) throws Exception;
	/**
	 * 并行签署撤回
	 * @param revokeParamObject
	 * @throws Exception 
	 */
	void revokeSignLine(RevokeSignLineParamObject revokeParamObject) throws Exception;

    /**
     * 根据任务ID获取审批按钮
     * @param taskId
     * @return
     */
    TaskDetailVo getButtonsBytaskId(String taskId) throws Exception;
	
	/**
	 * 获取顺签下一步执行人
	 * @param taskId
	 * @return
	 */
	CommonResult<BpmIdentity> nextExecutor(String taskId);
}
