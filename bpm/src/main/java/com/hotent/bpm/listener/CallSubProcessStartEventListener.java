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
import com.hotent.bpm.api.constant.NodeStatus;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.event.CallSubProcessStartEvent;
import com.hotent.bpm.api.model.delegate.BpmDelegateExecution;
import com.hotent.bpm.api.model.delegate.BpmDelegateTask;
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
 * 外部子流程进入时触发的事件。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-4-17-下午1:59:22
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@Service(value="callSubProcessStartEventListener")
public class CallSubProcessStartEventListener implements  ApplicationListener<CallSubProcessStartEvent>,Ordered{

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
	public void onApplicationEvent(CallSubProcessStartEvent ev) {
		
		BpmDelegateExecution execution=(BpmDelegateExecution) ev.getSource();
		
		String nodeId=execution.getNodeId();
		String bpmnDefId=execution.getBpmnDefId();
		String nodeName=execution.getNodeName();
		
		//传递流程变量
		Map<String,Object> variables= execution.getVariables();
		
		Integer completeInstance=(Integer)variables.get(BpmConstants.NUMBER_OF_COMPLETED_INSTANCES);
		
		//将流程变量通过这个方式
		removeVars(variables);
		ContextThreadUtil.cleanCommuVars();
		ContextThreadUtil.setCommuVars(variables);
		
		String instId=(String)execution.getVariable(BpmConstants.PROCESS_INST_ID);

		//首次调用。
		if(completeInstance==null){
			bpmProStatusManager.createOrUpd(instId, bpmnDefId, nodeId, nodeName, NodeStatus.PENDING);
		}
		
		// 构建堆栈信息 用于驳回外部子流程
		ActionCmd cmd = ContextThreadUtil.getActionCmd();
		DefaultBpmTask bpmTask = (DefaultBpmTask) cmd.getTransitVars(BpmConstants.BPM_TASK);
		String targetNode = bpmTask.getNodeId();
		cmd.addTransitVars(BpmConstants.TARGET_NODE, targetNode);
		bpmTask.setId(execution.getId());
		bpmTask.setNodeId(nodeId);
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
	 * 移除不必要的变量。
	 * @param variables 
	 * void
	 */
	private void removeVars(Map<String,Object> variables){
		variables.remove(BpmConstants.NUMBER_OF_LOOPCOUNTER);
		variables.remove(BpmConstants.NUMBER_OF_ACTIVE_INSTANCES);
		variables.remove(BpmConstants.NUMBER_OF_COMPLETED_INSTANCES);
		variables.remove(BpmConstants.NUMBER_OF_INSTANCES);
		// 任务完成时已经记录了parentStack 流程变量中的已经不是当前最新的parentStack
		variables.remove(BpmConstants.PARENT_STACK);
		
	}
	
	@Override
	public int getOrder() {
		return 1;
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
		Object tokenObj = natProInstanceService.getVariable(bpmTask.getExecId(), BpmConstants.TOKEN_NAME);
		String token = tokenObj == null ? null : tokenObj.toString();

		if (token == null)
		{
			BpmDelegateTask task = natTaskService.getByTaskId(bpmTask.getTaskId());
			token = task.getVariable(BpmConstants.TOKEN_NAME) != null ? task.getVariable(BpmConstants.TOKEN_NAME).toString() : null;
		}

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
		bpmExeStackExecutorManager.update(executor);
	}
}
