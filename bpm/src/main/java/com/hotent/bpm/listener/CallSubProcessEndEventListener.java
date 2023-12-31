package com.hotent.bpm.listener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

import com.hotent.base.exception.WorkFlowException;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.constant.MultiInstanceType;
import com.hotent.bpm.api.constant.NodeStatus;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.event.CallSubProcessEndEvent;
import com.hotent.bpm.api.model.delegate.BpmDelegateExecution;
import com.hotent.bpm.api.model.process.task.BpmTask;
import com.hotent.bpm.natapi.inst.NatProInstanceService;
import com.hotent.bpm.natapi.task.NatTaskService;
import com.hotent.bpm.persistence.manager.BpmExeStackExecutorManager;
import com.hotent.bpm.persistence.manager.BpmExeStackManager;
import com.hotent.bpm.persistence.manager.BpmProStatusManager;
import com.hotent.bpm.persistence.model.BpmExeStack;
import com.hotent.bpm.persistence.model.BpmExeStackExecutor;
import com.hotent.bpm.persistence.model.DefaultBpmTask;
import com.hotent.uc.api.impl.util.ContextUtil;

/**
 *  <pre> 
 * 子流程结束时将变量传递出来。
 * 1.传递的变量名称为： callActivityVars_ +"节点名称";
 * 2.如果流程为串行流程那么删除 相关的用户变量。
 *
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-4-18-下午6:31:29
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@Service(value="callSubProcessEndEventListener")
public class CallSubProcessEndEventListener implements  ApplicationListener<CallSubProcessEndEvent>,Ordered{

	@Resource
	BpmProStatusManager bpmProStatusManager;
	@Resource
	BpmExeStackManager bpmExeStackManager;
	@Resource
	BpmExeStackExecutorManager bpmExeStackExecutorManager;
	@Resource
	NatTaskService natTaskService;
	@Resource
	NatProInstanceService natProInstanceService;
	
	@Override
	public int getOrder() {
		return 1;
	}

	@Override
	public void onApplicationEvent(CallSubProcessEndEvent endEvent) {
		BpmDelegateExecution execution=(BpmDelegateExecution) endEvent.getSource();
		Integer instCount=(Integer) execution.getVariable(BpmConstants.NUMBER_OF_INSTANCES);
		Integer completeInstCount=(Integer) execution.getVariable(BpmConstants.NUMBER_OF_COMPLETED_INSTANCES);
		//单实例的情况。
		if(instCount==null){
			setVars(execution);
			
			updNodeStatus(execution);
		}
		//多实例的情况
		else if(instCount.equals(completeInstCount)){
			MultiInstanceType mulType=execution.multiInstanceType();
			
			if(MultiInstanceType.SEQUENTIAL.equals(mulType)){
				String varName=BpmConstants.SIGN_USERIDS + execution.getNodeId();
				execution.removeVariable(varName);
			}
			setVars(execution);
			
			updNodeStatus(execution);
		}
		
		// 构建堆栈信息 用于驳回外部子流程
 		String nodeId = execution.getNodeId();
		String nodeName = execution.getNodeName();
		String instId=(String)execution.getVariable(BpmConstants.PROCESS_INST_ID);
		ActionCmd cmd = ContextThreadUtil.getActionCmd();
		DefaultBpmTask bpmTask = new DefaultBpmTask();
		DefaultBpmTask _bpmTask = (DefaultBpmTask) cmd.getTransitVars(BpmConstants.BPM_TASK);
		bpmTask.setId(execution.getId());
		bpmTask.setNodeId(nodeId);
		bpmTask.setProcDefId(_bpmTask.getProcDefId());
		bpmTask.setProcInstId(instId);
		bpmTask.setName(nodeName);
		bpmTask.setIsGateWay(true);
		ContextThreadUtil.addTask(bpmTask);
		// 进入子流程 数据放入堆栈中
		try {
			bpmExeStackManager.pushStack(execution);
			updStack(bpmTask, cmd);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WorkFlowException(ExceptionUtils.getRootCauseMessage(e));
		}
		
	}
	
	/**
	 * 将流程变量从内部子流程传递出来。
	 * @param execution 
	 * void
	 */
	private void setVars(BpmDelegateExecution execution){
		Map<String, Object> vars=ContextThreadUtil.getCommuVars();
		String varName=BpmConstants.CALL_ACTIVITI_VARS + execution.getNodeId();
		execution.setVariable(varName, vars);
	}

	/**
	 * 更新节点状态为完成。
	 * @param execution 
	 * void
	 */
	private void updNodeStatus(BpmDelegateExecution execution){
		String instId=(String) execution.getVariable(BpmConstants.PROCESS_INST_ID);
		String bpmnDefId=execution.getBpmnDefId();
		String nodeId=execution.getNodeId();
		String nodeName=execution.getNodeName();
		
		bpmProStatusManager.createOrUpd(instId, bpmnDefId, nodeId, nodeName, NodeStatus.COMPLETE);
	}
	
	/**
	 * 更新堆栈数据。 结束时
	 * 
	 * @param bpmTask
	 *            void
	 * @param cmd
	 */
	private void updStack(BpmTask bpmTask, ActionCmd cmd)
	{
		String token = null;

		BpmExeStack bpmExeStack = bpmExeStackManager.getStack(bpmTask.getProcInstId(), bpmTask.getNodeId(), token);
		if (bpmExeStack == null)
			// 当驳回到并行之前的节点时不需要token
			bpmExeStack = bpmExeStackManager.getStack(bpmTask.getProcInstId(), bpmTask.getNodeId(), null);
		// TODO delete 兼容错误数据 旧流程任务没有堆栈信息，
		if (bpmExeStack == null)
			return;

		bpmExeStack.setEndTime(LocalDateTime.now());

		bpmExeStackManager.update(bpmExeStack);
		// 更新堆栈执行人信息。
		BpmExeStackExecutor executor = bpmExeStackExecutorManager.getByTaskId(bpmTask.getId());
		// 将变量设置临时变量。
		cmd.addTransitVars(BpmConstants.PARENT_STACK, bpmExeStack);
		ContextThreadUtil.putCommonVars(BpmConstants.PARENT_STACK, bpmExeStack);
		// 驳回至该节点时。executor 通过taskId 取不到
		if (executor == null)
		{
			List<BpmExeStackExecutor> executorList = bpmExeStackExecutorManager.getByStackId(bpmExeStack.getId());
			if (executorList.size() == 1)
				executor = executorList.get(0);
			else
				return;
		}
		// 设置任务执行人
		executor.setAssigneeId(ContextUtil.getCurrentUserId());

		executor.setEndTime(LocalDateTime.now());
		// 是否干预
		executor.setStatus(1);
		bpmExeStackExecutorManager.update(executor);
	}
	

}
