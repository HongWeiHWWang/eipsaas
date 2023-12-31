package com.hotent.runtime.core.persistence.manager;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.JsonUtil;
import com.hotent.bpm.api.model.form.FormType;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.def.BpmVariableDef;
import com.hotent.bpm.engine.inst.DefaultProcessInstCmd;
import com.hotent.bpm.persistence.model.CopyTo;
import com.hotent.bpm.persistence.model.DefaultBpmDefinition;
import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;
import com.hotent.bpm.persistence.model.DefaultBpmTask;
import com.hotent.bpm.persistence.model.DefaultBpmTaskTurn;
import com.hotent.runtime.core.RunTimeTestCase;
import com.hotent.runtime.manager.IFlowManager;
import com.hotent.runtime.model.BpmTaskTransRecord;
import com.hotent.runtime.params.AssignParamObject;
import com.hotent.runtime.params.CommunicateParamObject;
import com.hotent.runtime.params.DoNextParamObject;
import com.hotent.runtime.params.FlowImageVo;
import com.hotent.runtime.params.FormAndBoVo;
import com.hotent.runtime.params.InstFormAndBoVo;
import com.hotent.runtime.params.IsAllowAddSignObject;
import com.hotent.runtime.params.ModifyExecutorsParamObject;
import com.hotent.runtime.params.NodeOpinionVo;
import com.hotent.runtime.params.RevokeParamObject;
import com.hotent.runtime.params.SelectDestinationVo;
import com.hotent.runtime.params.StartCmdParam;
import com.hotent.runtime.params.TaskDetailVo;
import com.hotent.runtime.params.TaskDoNextVo;
import com.hotent.runtime.params.TaskGetVo;
import com.hotent.runtime.params.TaskToAgreeVo;
import com.hotent.runtime.params.TaskToRejectVo;
import com.hotent.runtime.params.TaskTransParamObject;
import com.hotent.runtime.params.TaskjImageVo;
import com.hotent.runtime.params.WithDrawParam;

/**
 * IFlowManager测试
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月27日
 */
public class IFlowManagerTest extends RunTimeTestCase{
	public String account = "admin";
	public String proInstId = "20000000557585";
	public String nodeId = "UserTask2";
	public String taskId = "20000000557516";
	public String formType = "pc";
	public String messageType = "inner";
	public String defId = "20000000557495";
	public String defKey = "asas";
	
	@Resource
	IFlowManager flowManager;
	
	
	@Before
    public void runBeforeTestMethod() {
		System.out.println("测试开始");
    }

	
	@After
    public void runAfterTestMethod() {
        System.out.println("测试完成");
    }
	
	private QueryFilter getQueryFilter(){
		QueryFilter filter = QueryFilter.build().withDefaultPage();
		return filter;
	}
	
	/**
	 * 获取用户待办事宜
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getTodoList() throws Exception{
		PageList<DefaultBpmTask> pageList = flowManager.getTodoList(account, getQueryFilter());
		assertEquals(10,pageList.getPageSize());
	}
	
	/**
	 * 获取用户的已办事宜
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getDoneList() throws Exception{
		PageList<Map<String,Object>> pageList = flowManager.getDoneList(account, getQueryFilter(),"running");
		assertEquals(10,pageList.getPageSize());
	}
	
	/**
	 * 获取用户的办结事宜。
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getMyCompletedList() throws Exception{
		PageList<DefaultBpmProcessInstance> pageList = flowManager.getMyCompletedList(account, getQueryFilter());
		assertEquals(10,pageList.getPageSize());
	}
	
	/**
	 * 我的请求
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getMyRequestList() throws Exception{
		PageList<DefaultBpmProcessInstance> pageList = flowManager.getMyRequestList(account, getQueryFilter());
		assertEquals(10,pageList.getPageSize());
	}
	
	/**
	 * 获取用户可发起的流程
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getMyFlowList() throws Exception{
		PageList<DefaultBpmDefinition> pageList = flowManager.getMyFlowList(account, getQueryFilter(),"");
		assertEquals(10,pageList.getPageSize());
	}
	
	/**
	 * 获取用户的草稿列表
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getMyDraftList() throws Exception{
		PageList<DefaultBpmProcessInstance> pageList = flowManager.getMyDraftList(account, getQueryFilter());
		assertEquals(10,pageList.getPageSize());
	}
	
	/**
	 * 根据流程定义ID或流程定义KEY获取流程变量
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getWorkflowVar() throws Exception{
		//json {defId:"流程定义ID",defKey:"流程定义key"}
		ObjectNode obj = JsonUtil.getMapper().createObjectNode();
		obj.put("defId", defId);
		obj.put("defKey", defKey);
		List<BpmVariableDef> list = flowManager.getWorkflowVar(obj.toString());
		assertEquals(0,list.size());
	}
	
	/**
	 * 获取用户的抄送转发事宜
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getReceiverCopyTo() throws Exception{
		PageList<CopyTo> pageList = flowManager.getReceiverCopyTo(account, getQueryFilter(),"");
		assertEquals(10,pageList.getPageSize());
	}
	
	/**
	 * 获取用户转办代理事宜
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getDelegate() throws Exception{
		PageList<DefaultBpmTaskTurn> pageList = flowManager.getDelegate(account, getQueryFilter());
		assertEquals(10,pageList.getPageSize());
	}
	
	/**
	 * 获取用户转办代理事宜
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getMyTrans() throws Exception{
		PageList<BpmTaskTransRecord> pageList = flowManager.getMyTrans(account, getQueryFilter());
		assertEquals(10,pageList.getPageSize());
	}
	
	/**
	 * 根据id删除草稿
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void removeDraftById() throws Exception{
		String runId = "11111";
		CommonResult<String> result = flowManager.removeDraftById(runId);
		assertEquals(true,result.getState());
	}
	
	/**
	 * 任务转办
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void taskAssign() throws Exception{
		AssignParamObject assignParamObject = new AssignParamObject();
		assignParamObject.setAccount(account);
		assignParamObject.setMessageType(messageType);
		assignParamObject.setOpinion("烦请帮忙办理该任务，谢谢！");
		assignParamObject.setTaskId(taskId);
		assignParamObject.setAccount("zhangsan,lisi");
		CommonResult<String> result = flowManager.delegate(assignParamObject);
		assertEquals(true,result.getState());
	}
	
	
	/**
	 * 任务沟通
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void communicate() throws Exception{
		CommunicateParamObject communicateParamObject = new CommunicateParamObject();
		communicateParamObject.setAccount(account);
		communicateParamObject.setMessageType(messageType);
		communicateParamObject.setOpinion("烦请各位给点意见，速度！谢谢！");
		communicateParamObject.setTaskId(taskId);
		communicateParamObject.setAccount("zhangsan,lisi");
		CommonResult<String> result = flowManager.communicate(communicateParamObject);
		assertEquals(true,result.getState());
	}
	
	/**
	 * 流程任务加签，增加会签人员
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void taskSignUsers() throws Exception{
		AssignParamObject assignParamObject = new AssignParamObject();
		assignParamObject.setAccount(account);
		assignParamObject.setMessageType(messageType);
		assignParamObject.setOpinion("一起投个票，谢谢！");
		assignParamObject.setTaskId(taskId);
		assignParamObject.setAccount("zhangsan,lisi");
		CommonResult<String> result = flowManager.taskSignUsers(assignParamObject);
		assertEquals(true,result.getState());
	}
	
	/**
	 * 根据实例id撤回流程（撤销）
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void revokeInstance() throws Exception{
		RevokeParamObject revokeParamObject = new RevokeParamObject();
		revokeParamObject.setAccount(account);
		revokeParamObject.setCause("测试撤回");
		revokeParamObject.setInstanceId(proInstId);
		revokeParamObject.setIsHandRevoke(false);
		revokeParamObject.setMessageType(messageType);
		CommonResult<String> result = flowManager.revokeInstance(revokeParamObject);
		assertEquals(true,result.getState());
	}
	
	/**
	 * 根据任务id获取下一环节处理人
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getNextTaskUsers() throws Exception{
		Map<String, List<BpmIdentity>> result = flowManager.getNextTaskUsers(taskId);
		assertEquals(2,result.size());
	}
	
	/**
	 * 修改任务执行人
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void setTaskExecutors() throws Exception{
		ModifyExecutorsParamObject modifyExecutorsParamObject = new ModifyExecutorsParamObject();
		modifyExecutorsParamObject.setCause("改人员已休假，暂由张三处理该任务");
		modifyExecutorsParamObject.setMessageType(messageType);
		modifyExecutorsParamObject.setTaskId(taskId);
		modifyExecutorsParamObject.setUserIds(new String[]{"1"});
		CommonResult<String> result = flowManager.setTaskExecutors(modifyExecutorsParamObject);
		assertEquals(true,result.getState());
	}
	
	/**
	 * 判断用户是否有添加会签权限
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void isAllowAddSign() throws Exception{
		IsAllowAddSignObject isAllowAddSignObject = new IsAllowAddSignObject();
		isAllowAddSignObject.setTaskId(taskId);
		isAllowAddSignObject.setAccount("zhangsan");
		boolean result = flowManager.isAllowAddSign(isAllowAddSignObject);
		assertEquals(true,result);
	}
	
	/**
	 * 保存流转信息（增加流转）
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void taskToTrans() throws Exception{
		TaskTransParamObject taskTransParamObject = new TaskTransParamObject();
		taskTransParamObject.setAction("back");
		taskTransParamObject.setDecideType("agree");
		taskTransParamObject.setTaskId(taskId);
		taskTransParamObject.setInstanceId(proInstId);
		taskTransParamObject.setUserIds("1");
		taskTransParamObject.setNotifyType("inner");
		taskTransParamObject.setSignType("parallel");
		taskTransParamObject.setOpinion("测试");
		taskTransParamObject.setVoteAmount(Short.valueOf("1"));
		taskTransParamObject.setVoteType("amount");
		CommonResult<String> result = flowManager.taskToTrans(taskTransParamObject);
		assertEquals(true,result.getState());
	}
	
	/**
	 * 获取处理任务的 在线表单地址
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getUrlFormByTaskId() throws Exception{
		String result = flowManager.getUrlFormByTaskId(taskId,formType);
		assertEquals("",result);
	}
	
	/**
	 * 获取实例表单  
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getInstUrlForm() throws Exception{
		String result = flowManager.getInstUrlForm(proInstId, nodeId, formType);
		assertEquals("",result);
	}
	
	/**
	 * 我的请求（包括人工终止和结束状态的实例）
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getMyRequestListAll() throws Exception{
		PageList<DefaultBpmProcessInstance> pageList = flowManager.getMyRequestListAll(account, getQueryFilter());
		assertEquals(10,pageList.getPageSize());
	}
	
	/**
	 * 查询流程定义列表
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getBpmDefList() throws Exception{
		PageList<DefaultBpmDefinition> pageList = flowManager.getBpmDefList(account, getQueryFilter());
		assertEquals(10,pageList.getPageSize());
	}
	
	/**
	 * 查询流程实例列表
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getInstanceList() throws Exception{
		PageList<DefaultBpmProcessInstance> pageList = flowManager.getInstanceList(account, getQueryFilter());
		assertEquals(10,pageList.getPageSize());
	}
	
	/**
	 * 获取流程实例的表单和数据
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getInstFormAndBO() throws Exception{
		//InstFormAndBoVo result = flowManager.getInstFormAndBO(proInstId, nodeId, FormType.PC);
		//assertEquals(null,result);
	}
	
	/**
	 * 获取流程实例的表单和数据
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getFormAndBO() throws Exception{
		StartCmdParam startCmdParam = new StartCmdParam();
		startCmdParam.setFormType("inner");
		startCmdParam.setProInstId(proInstId);
		FormAndBoVo result = flowManager.getFormAndBO(startCmdParam, FormType.PC);
		assertEquals(null,result);
	}
	
	/**
	 * 获取流程实例的表单和数据
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getStartCmd() throws Exception{
		StartCmdParam startCmdParam = new StartCmdParam();
		startCmdParam.setFormType("inner");
		startCmdParam.setProInstId(proInstId);
		DefaultProcessInstCmd result = flowManager.getStartCmd(startCmdParam);
		assertEquals(null,result);
	}
	
	/**
	 * 获取可以选择的路径
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void selectDestination() throws Exception{
		SelectDestinationVo result = flowManager.selectDestination(defId);
		assertEquals(null,result);
	}
	
	/**
	 * 节点图片
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void flowImage() throws Exception{
		FlowImageVo result = flowManager.flowImage(proInstId, "", "", nodeId, "");
		assertEquals(null,result);
	}
	
	/**
	 * 流程审批历史（页面数据）
	 * @throws Exception
	 */
	@Test
	public void opinionHistory() throws Exception{
		List<ObjectNode> result = flowManager.opinionHistory(proInstId , taskId);
		assertEquals(null,result);
	}
	
	/**
	 * 获取任务处理页面参数
	 * @throws Exception
	 */
	@Test
	public void taskDoNext() throws Exception{
		TaskDoNextVo result = flowManager.taskDoNext(taskId);
		assertEquals(null,result);
	}
	
	/**
	 * 获取用户审批界面参数
	 * @throws Exception
	 */
	@Test
	public void taskApprove() throws Exception{
		TaskDoNextVo result = flowManager.taskApprove(taskId);
		assertEquals(null,result);
	}
	
	/**
	 * 获取任务流程图页面参数
	 * @throws Exception
	 */
	@Test
	public void taskImage() throws Exception{
		TaskjImageVo result = flowManager.taskImage(taskId, "");
		assertEquals(null,result);
	}
	
	/**
	 * 获取流程实例中指定节点的审批意见
	 * @throws Exception
	 */
	@Test
	public void nodeOpinion() throws Exception{
		//Object result = flowManager.nodeOpinion(proInstId, nodeId);
		//assertEquals(null,result);
	}
	
	/**
	 * 获取任务的详情
	 * @throws Exception
	 */
	@Test
	public void taskDetail() throws Exception{
		TaskDetailVo result = flowManager.taskDetail(taskId, "", FormType.PC,"");
		assertEquals(null,result);
	}
	
	/**
	 * 获取任务上下文流程变量
	 * @throws Exception
	 */
	@Test
	public void getTaskVars() throws Exception{
		Map<String, Object> result = flowManager.getTaskVars(taskId, null);
		assertEquals(null,result);
	}
	
	/**
	 * 处理任务
	 * @throws Exception
	 */
	@Test
	public void complete() throws Exception{
		DoNextParamObject doNextParamObject = new DoNextParamObject();
		doNextParamObject.setAccount(account);
		doNextParamObject.setActionName("agree");
		doNextParamObject.setTaskId(taskId);
		doNextParamObject.setOpinion("测试处理任务");
		CommonResult<String> result = flowManager.complete(doNextParamObject);
		assertEquals(true,result.getState());
	}
	
	/**
	 * 保存草稿
	 * @throws Exception
	 */
	@Test
	public void saveDraft() throws Exception{
		DoNextParamObject doNextParamObject = new DoNextParamObject();
		doNextParamObject.setAccount(account);
		doNextParamObject.setActionName("agree");
		doNextParamObject.setTaskId(taskId);
		doNextParamObject.setOpinion("测试处理任务");
		CommonResult<String> result = flowManager.saveDraft(doNextParamObject);
		assertEquals(true,result.getState());
	}
	
	/**
	 * 获取任务任务明细
	 * @throws Exception
	 */
	@Test
	public void getTaskById() throws Exception{
		TaskGetVo result = flowManager.getTaskById(taskId);
		assertEquals(null,result);
	}
	
	/**
	 * 任务办理(同意、反对、弃权)
	 * @throws Exception
	 */
	@Test
	public void toAgree() throws Exception{
		TaskToAgreeVo result = flowManager.toAgree(taskId,"agree");
		assertEquals(null,result);
	}
	
	/**
	 * 驳回任务页面参数
	 * @throws Exception
	 */
	@Test
	public void toReject() throws Exception{
		TaskToRejectVo result = flowManager.toReject(taskId,"reject");
		assertEquals(null,result);
	}
	
	/**
	 * 撤销我流转出去的任务
	 * @throws Exception
	 */
	@Test
	public void withDraw() throws Exception{
		WithDrawParam withDrawParam = new WithDrawParam();
		withDrawParam.setNotifyType(messageType);
		withDrawParam.setOpinion("测试撤回");
		withDrawParam.setTaskId(taskId);
		CommonResult<String> result = flowManager.withDraw(withDrawParam);
		assertEquals(true,result.getState());
	}
	
}
