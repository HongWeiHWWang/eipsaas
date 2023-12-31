package com.hotent.runtime.core.persistence.manager;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;
import com.hotent.bpm.api.model.process.task.BpmTaskOpinion;
import com.hotent.bpm.persistence.model.BpmIdentityResult;
import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;
import com.hotent.bpm.persistence.model.DefaultBpmTask;
import com.hotent.runtime.core.RunTimeTestCase;
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

/**
 * IProcessManager测试
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月27日
 */
public class IProcessManagerTest extends RunTimeTestCase{
	
	public String account = "admin";
	public String proInstId = "20000000557585";
	public String nodeId = "UserTask2";
	public String taskId = "20000000557516";
	public String formType = "pc";
	public String messageType = "inner";
	public String defId = "20000000557495";
	public String defKey = "asas";
	public String businessKey = "100000001";
	public String sysCode = "testCode";
	
	@Resource
	IProcessManager processManager;
	
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
	 * 客户端启动流程
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void start() throws Exception{
		StartFlowParamObject startFlowParamObject = new StartFlowParamObject();
		startFlowParamObject.setAccount(account);
		startFlowParamObject.setFlowKey(defKey);
		StartResult result = processManager.start(startFlowParamObject);
		assertEquals(true,result.getState());
	}
	
	/**
	 *  客户端提交数据,执行流程往下跳转:同意
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void doNextAgree() throws Exception{
		DoNextParamObject doNextParamObject = new DoNextParamObject();
		doNextParamObject.setAccount(account);
		doNextParamObject.setActionName("agree");
		doNextParamObject.setTaskId(taskId);
		doNextParamObject.setOpinion("测试任务审批");
		CommonResult<String> result = processManager.doNext(doNextParamObject);
		assertEquals(true,result.getState());
	}
	
	/**
	 *  客户端提交数据,执行流程往下跳转:驳回
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void doNextReject() throws Exception{
		DoNextParamObject doNextParamObject = new DoNextParamObject();
		doNextParamObject.setAccount(account);
		doNextParamObject.setActionName("reject");
		doNextParamObject.setTaskId(taskId);
		doNextParamObject.setOpinion("测试任务审批");
		doNextParamObject.setDestination(nodeId);
		CommonResult<String> result = processManager.doNext(doNextParamObject);
		assertEquals(true,result.getState());
	}
	
	/**
	 *  客户端提交数据,执行流程往下跳转:驳回到发起人
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void doNextBackToStart() throws Exception{
		DoNextParamObject doNextParamObject = new DoNextParamObject();
		doNextParamObject.setAccount(account);
		doNextParamObject.setActionName("backToStart");
		doNextParamObject.setTaskId(taskId);
		doNextParamObject.setOpinion("测试任务审批");
		CommonResult<String> result = processManager.doNext(doNextParamObject);
		assertEquals(true,result.getState());
	}
	
	/**
	 * 人工终止流程
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void doEndProcess() throws Exception{
		DoEndParamObject doEndParamObject = new DoEndParamObject();
		doEndParamObject.setAccount(account);
		doEndParamObject.setEndReason("测试终止流程");
		doEndParamObject.setMessageType(messageType);
		doEndParamObject.setTaskId(taskId);
		CommonResult<String> result = processManager.doEndProcess(doEndParamObject);
		assertEquals(true,result.getState());
	}
	
	/**
	 * 按流程实例ID或任务实例ID取得某个流程的审批历史
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getHistoryOpinion() throws Exception{
		List<BpmTaskOpinion> result = processManager.getHistoryOpinion(proInstId, taskId);
		assertEquals(10,result.size());
	}
	
	/**
	 * 按Activiti实例Id取得对应流程的审批历史，act_ru_task表的PROC_INST_ID_值
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getProcessOpinionByActInstId() throws Exception{
		List<BpmCheckOpinionVo> result = processManager.getProcessOpinionByActInstId(taskId);
		assertEquals(10,result.size());
	}
	
	/**
	 * 根据任务ID获取可驳回的节点
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getEnableRejectNode() throws Exception{
		List<BpmNodeDefVo> result = processManager.getEnableRejectNode(taskId, "reject");
		assertEquals(10,result.size());
	}
	
	/**
	 * 根据任务ID或流程实例ID获取BusinessKey（流程表单为URL表单的情况）
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getBusinessKey() throws Exception{
		CommonResult<String> result = processManager.getBusinessKey(proInstId,taskId);
		assertEquals(true,result.getState());
	}
	
	/**
	 * 根据BussinessKey获取流程实例ID
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getProcInstId() throws Exception{
		CommonResult<String> result = processManager.getProcInstId(businessKey);
		assertEquals(true,result.getState());
	}
	
	/**
	 * 通过表单保存草稿
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void saveDraft() throws Exception{
		StartFlowParamObject startFlowParamObject = new StartFlowParamObject();
		startFlowParamObject.setAccount(account);
		startFlowParamObject.setFlowKey(defKey);
		CommonResult<String> result = processManager.saveDraft(startFlowParamObject);
		assertEquals(true,result.getState());
	}
	
	/**
	 * 设置流程的其他参数
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void setDefOtherParam() throws Exception{
		DefOtherParam defOtherParam = new DefOtherParam();
		defOtherParam.setAllowCopyTo(true);
		defOtherParam.setAllowExecutorEmpty(true);
		defOtherParam.setAllowTransTo(true);
		defOtherParam.setArchiveNotifyType(messageType);
		defOtherParam.setSkipExecutorEmpty(true);
		CommonResult<String> result = processManager.setDefOtherParam(defOtherParam);
		assertEquals(true,result.getState());
	}
	
	/**
	 *  根据任务id获取预先设置的审批用语列表
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getApprovalItems() throws Exception{
		List<String> result = processManager.getApprovalItems(taskId);
		assertEquals(0,result.size());
	}
	
	/**
	 *  根据任务id获取其处理人
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getNodeUsers() throws Exception{
		List<BpmIdentityResult> result = processManager.getNodeUsers(taskId);
		assertEquals(0,result.size());
	}
	
	/**
	 * 根据流程taskId获取对应的流程运行对象
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getProcessRunByTaskId() throws Exception{
		BpmProcessInstance result = processManager.getProcessRunByTaskId(taskId);
		assertTrue(result!=null);
	}
	
	/**
	 * 根据instId和nodeId获取节点的状态
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getStatusByRunidNodeId() throws Exception{
		String result = processManager.getStatusByRunidNodeId(proInstId,taskId);
		assertTrue(result!=null);
	}
	
	/**
	 * 根据任务ID获取流程任务实例
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getTaskByTaskId() throws Exception{
		BpmTaskResult result = processManager.getTaskByTaskId(taskId);
		assertTrue(result!=null);
	}
	
	/**
	 * 根据taskid获取taskName
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getTaskNameByTaskId() throws Exception{
		String result = processManager.getTaskNameByTaskId(taskId);
		assertTrue(result!=null);
	}
	
	/**
	 * 通过businessKey获取运行实例
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getInstancetByBusinessKey() throws Exception{
		DefaultBpmProcessInstance result = processManager.getInstancetByBusinessKey(businessKey);
		assertTrue(result!=null);
	}
	
	/**
	 * 通过businessKey和sysCode获取运行实例
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getInstancetByBizKeySysCode() throws Exception{
		DefaultBpmProcessInstance result = processManager.getInstancetByBizKeySysCode(businessKey,sysCode);
		assertTrue(result!=null);
	}
	
	/**
	 * 根据实例id获取实例对象
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getInstanceByInstId() throws Exception{
		BpmProcessInstance result = processManager.getInstanceByInstId(proInstId);
		assertTrue(result!=null);
	}
	
	/**
	 * 根据xml获取实例列表
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getInstanceListByXml() throws Exception{
		PageList<DefaultBpmProcessInstance> result = processManager.getInstanceListByXml("");
		assertTrue(result!=null);
	}
	
	/**
	 * 取得某个运行流程实例对应的任务列表
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getTasksByInstId() throws Exception{
		PageList<DefaultBpmTask> result = processManager.getTasksByInstId(proInstId);
		assertTrue(result!=null);
	}
	
	/**
	 * 根据任务id获取任务的后续节点
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getTaskOutNodes() throws Exception{
		List<BpmNodeDefVo> result = processManager.getTaskOutNodes(taskId);
		assertTrue(result!=null);
	}
	
	/**
	 * 根据任务id获取任务的后续节点
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getDetailUrl() throws Exception{
		String result = processManager.getDetailUrl(taskId);
		assertTrue(result!=null);
	}
	
	/**
	 * 根据任务taskId,设置流程变量（全局）
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void setTaskVar() throws Exception{
		Map<String,Object> variables = new HashMap<String, Object>();
		variables.put("bly", "aa");
		CommonResult<String> result = processManager.setTaskVar(taskId, variables);
		assertEquals(true, result.getState());
	}
	
	/**
	 * 设置任务节点本地变量
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void setTaskVarLocal() throws Exception{
		Map<String,Object> variables = new HashMap<String, Object>();
		variables.put("bly", "aa");
		CommonResult<String> result = processManager.setTaskVarLocal(taskId, variables);
		assertEquals(true, result.getState());
	}
	
	/**
	 * 通过流程实例id挂起流程
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void forbiddenInstance() throws Exception{
		CommonResult<String> result = processManager.forbiddenInstance(proInstId);
		assertEquals(true, result.getState());
	}
	
	/**
	 * 通过流程实例id取消挂起流程实例
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void unForbiddenInstance() throws Exception{
		CommonResult<String> result = processManager.unForbiddenInstance(proInstId);
		assertEquals(true, result.getState());
	}
	
	/**
	 * 根据父流程实例ID和节点定义ID查子流程实例
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getBpmProcessByParentIdAndSuperNodeId() throws Exception{
		List<BpmProcessInstance> result = processManager.getBpmProcessByParentIdAndSuperNodeId(proInstId,nodeId);
		assertEquals(1, result.size());
	}
	
	/**
	 * 通过父流程实例ID和实例的状态获取实例列表
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getInstancesByParentId() throws Exception{
		List<DefaultBpmProcessInstance> result = processManager.getInstancesByParentId(proInstId,"running");
		assertEquals(1, result.size());
	}
	
	/**
	 * 通过父流程定义ID和实例的状态获取实例列表
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getInstancesByDefId() throws Exception{
		List<DefaultBpmProcessInstance> result = processManager.getInstancesByDefId(defId,"running");
		assertEquals(1, result.size());
	}
	
	/**
	 * 根据流程实例ID查询顶级的流程实例。
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getTopBpmProcessInstance() throws Exception{
		BpmProcessInstance result = processManager.getTopBpmProcessInstance(proInstId);
		assertFalse(result==null);
	}
	
	/**
	 * 根据流程定义id或流程实例id或任务id或BPMN实例ID获取流程图。
	 * @throws IOException
	 * @throws Exception
	 */
	@Test
	public void getBpmImage() throws Exception{
		BpmImageParamObject bpmImageParamObject = new BpmImageParamObject();
		bpmImageParamObject.setProcInstId(proInstId);
		String result = processManager.getBpmImage(bpmImageParamObject);
		assertFalse(result==null);
	}
}
