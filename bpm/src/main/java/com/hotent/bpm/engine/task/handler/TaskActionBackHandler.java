package com.hotent.bpm.engine.task.handler;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.ActionType;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.model.delegate.BpmDelegateTask;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.def.NodeProperties;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.CallActivityNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.SignNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.UserTaskNodeDef;
import com.hotent.bpm.api.model.process.task.BpmTask;
import com.hotent.bpm.api.plugin.core.def.TaskActionHandlerDef;
import com.hotent.bpm.api.plugin.core.session.TaskActionPluginSession;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.engine.task.cmd.DefaultTaskFinishCmd;
import com.hotent.bpm.natapi.inst.NatProInstanceService;
import com.hotent.bpm.persistence.manager.BpmExeStackExecutorManager;
import com.hotent.bpm.persistence.manager.BpmExeStackManager;
import com.hotent.bpm.persistence.manager.BpmSignDataManager;
import com.hotent.bpm.persistence.model.BpmExeStack;

/**
 * 
 * <pre>
 *  
 * 描述：任务回退处理器
 * 构建组：x5-bpmx-plugin-core
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2014-3-18-下午2:24:51
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@Component
public class TaskActionBackHandler extends AbstractTaskActionHandler {

	@Resource
	BpmExeStackManager bpmExeStackManager;
	@Resource
	NatProInstanceService natProInstanceService;
	@Resource
	BpmExeStackExecutorManager exeStrackExecutorManager;
	@Resource
	BpmDefinitionAccessor bpmDefinitionAccessor;
	@Resource
	BpmSignDataManager bpmSignDataManager;

	@Override
	public boolean isNeedCompleteTask() {
		return true;
	}

	@Override
	public void preActionHandler(TaskActionPluginSession pluginSession, TaskActionHandlerDef def) throws Exception {
		popStack(pluginSession);

	}

	/**
	 * 退出堆栈。
	 * 
	 * @param pluginSession void
	 * @throws Exception
	 */
	private void popStack(TaskActionPluginSession pluginSession) throws Exception {
		DefaultTaskFinishCmd cmd = (DefaultTaskFinishCmd) pluginSession.getTaskFinishCmd();
		String instId = cmd.getInstId();
		String destinationNode = cmd.getDestination();
		String destinationToken = "";
		String handMode = (String) cmd.getTransitVars(BpmConstants.BACK_HAND_MODE);

		BpmTask task = (BpmTask) cmd.getTransitVars(BpmConstants.BPM_TASK);
		BpmDelegateTask bpmDelegateTask = natTaskService.getByTaskId(task.getId());
		String exeId = bpmDelegateTask.getExecutionId();
		task.setExecId(exeId);
		Object objToken = natProInstanceService.getVariable(exeId, BpmConstants.TOKEN_NAME);
		String currentToken = objToken != null ? objToken.toString() : null;
		// 如果目标节点为空，那么去上级堆栈。
		BpmExeStack stack;
		if (StringUtil.isEmpty(destinationNode)) {
			stack = bpmExeStackManager.getPrevStack(instId, task.getNodeId(), currentToken);
			destinationToken = currentToken;
		} else {
			stack = bpmExeStackManager.getStack(instId, destinationNode, currentToken);
			destinationToken = currentToken;
		}
		if (stack == null) {
			stack = bpmExeStackManager.getStack(instId,
					StringUtil.isEmpty(destinationNode) ? task.getNodeId() : destinationNode, null);
			destinationToken = "";
		}

		// 当连续驳回 stack 为空 如果设置了targetNode，则抛出异常不允许进行驳回。
		/*if (BeanUtils.isNotEmpty(stack)) {
			String targetNode = stack.getTargetNode();
			if (StringUtil.isNotEmpty(targetNode)) {
				throw new RuntimeException("其他人已经驳回到此节点，不允许驳回!");
			}
		}*/

		cmd.setDestinationToken(destinationToken);
		cmd.setDestination(destinationNode);

		// 如果不是干预执行的情况 并且不是撤销
		if (BeanUtils.isNotEmpty(stack) && stack.getInterpose() == 0 && (short) stack.getIsMulitiTask() == 0
				&& (cmd.getTransitVars("IsDoneUnused") == null || !(Boolean) cmd.getTransitVars("IsDoneUnused"))) {
			BpmNodeDef bpmNodeDef = bpmDefinitionAccessor.getBpmNodeDef(stack.getPrcoDefId(), destinationNode);
			// 驳回UserTaskNodeDef节点
			if (bpmNodeDef instanceof UserTaskNodeDef) {
				// 是否配置了驳回时候选人为节点插件人员，
				UserTaskNodeDef backTargetNodeDef = (UserTaskNodeDef) bpmNodeDef;
				NodeProperties nodeProperties = backTargetNodeDef.getLocalProperties();
				String backUserMode = nodeProperties.getBackUserMode();
				if (StringUtil.isEmpty(backUserMode) || backUserMode.equals("history")) {
					// 从堆栈中获取执行人作为任务的执行人
					List<BpmIdentity> identitys = exeStrackExecutorManager.getBpmIdentitysByStackId(stack.getId());
					cmd.addBpmIdentity(stack.getNodeId(), identitys);
				}
			}
		}
		// 如果是撤回会签任务
		if (BeanUtils.isNotEmpty(stack) && stack.getInterpose() == 0
				&& (cmd.getTransitVars("IsDoneUnused") != null && (Boolean) cmd.getTransitVars("IsDoneUnused"))) {
			BpmNodeDef bpmNodeDef = bpmDefinitionAccessor.getBpmNodeDef(stack.getPrcoDefId(), destinationNode);
			// 会签直来直往 驳回后返回到会签节点
			if (bpmNodeDef instanceof SignNodeDef) {
				// 是否配置了驳回时候选人为节点插件人员， 撤回的情况不考虑是否撤回历史处理人
				List<BpmIdentity> signBpmIdentity = bpmSignDataManager.getByInstanIdAndNodeIdAndNo(instId,
						destinationNode);
				cmd.addBpmIdentity(destinationNode, signBpmIdentity);
				return;
				
			}
		}
		// 出栈
		bpmExeStackManager.popStack(instId, task.getNodeId(), currentToken, handMode, destinationNode,
				destinationToken);
	}

	@Override
	public void afterActionHandler(TaskActionPluginSession pluginSession, TaskActionHandlerDef def) {

	}

	@Override
	public ActionType getActionType() {
		return ActionType.BACK;
	}

}
