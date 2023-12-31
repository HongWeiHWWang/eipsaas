package com.hotent.bpm.engine.task.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;

import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.bpm.api.cmd.TaskFinishCmd;
import com.hotent.bpm.api.constant.ActionType;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.constant.MultiInstanceType;
import com.hotent.bpm.api.constant.NodeStatus;
import com.hotent.bpm.api.constant.OpinionStatus;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.model.delegate.BpmDelegateExecution;
import com.hotent.bpm.api.plugin.core.execution.sign.SignActionHandler;
import com.hotent.bpm.api.plugin.core.execution.sign.SignResult;
import com.hotent.bpm.api.service.CustomSignComplete;
import com.hotent.bpm.engine.execution.sign.handler.SignActionHandlerContainer;
import com.hotent.bpm.persistence.manager.BpmCheckOpinionManager;
import com.hotent.bpm.persistence.manager.BpmDefinitionManager;
import com.hotent.bpm.persistence.manager.BpmProStatusManager;
import com.hotent.bpm.persistence.manager.BpmTaskCandidateManager;
import com.hotent.bpm.persistence.manager.BpmTaskManager;
import com.hotent.bpm.persistence.model.DefaultBpmCheckOpinion;
import com.hotent.bpm.persistence.model.DefaultBpmTask;
import com.hotent.uc.api.impl.util.ContextUtil;

/**
 * 判断并行签署是否完成。
 * <pre>
 *  
 * 构建组：x5-bpmx-core
 * 作者：jason
 * 邮箱：liygui@jee-soft.cn
 * 日期：2020-02-29-上午10:38:34
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class CustomSignCompleteImpl implements CustomSignComplete{	

	@Resource
	SignActionHandlerContainer actionHandlerContainer; 
	@Resource
	BpmProStatusManager bpmProStatusManager;
	@Resource
	BpmDefinitionManager bpmDefinitionManager;
	@Resource
	BpmCheckOpinionManager bpmCheckOpinionManager;
	@Resource
	BpmTaskManager bpmTaskManager;
	@Resource
	BpmTaskCandidateManager bpmTaskCandidateManager;
	
	
	@Override
	@Transactional
	public boolean isComplete(BpmDelegateExecution bpmDelegateExecution) throws Exception {
		String instId=(String) bpmDelegateExecution.getVariable(BpmConstants.PROCESS_INST_ID);
		//获得任务数据
		TaskFinishCmd taskFinishCmd=(TaskFinishCmd)ContextThreadUtil.getActionCmd();
		if(BeanUtils.isEmpty(actionHandlerContainer)){
			actionHandlerContainer = (SignActionHandlerContainer) AppUtil.getBean("signActionHandlerContainer");
		}
		//根据actiontype 获取处理器。
		SignActionHandler handler=actionHandlerContainer.getSignActionHandler(taskFinishCmd.getActionType().getKey());
		
		SignResult result= handler.handByActionType(taskFinishCmd,bpmDelegateExecution);
		
		handComplete(result,instId,bpmDelegateExecution,taskFinishCmd.getActionType());
		
		return result.isComplete();
	}
	
	
	/**
	 * 根据会签结果完成最终会签结果的处理。
	 * @param result
	 * @param instId
	 * @param bpmDelegateExecution
	 * @param actionType 
	 * void
	 */
	private void handComplete(SignResult result,String instId,BpmDelegateExecution bpmDelegateExecution,ActionType actionType){
		
		if(!result.isComplete()) return;
		
		String nodeId=bpmDelegateExecution.getNodeId();
		String nodeName=bpmDelegateExecution.getNodeName();	
		if(BeanUtils.isEmpty(bpmDefinitionManager)){
			bpmDefinitionManager = AppUtil.getBean(BpmDefinitionManager.class);
		}
		String defId=bpmDefinitionManager.getDefIdByBpmnDefId(bpmDelegateExecution.getBpmnDefId());
		
		//更新节点状态。,结果为同意的修改为：会签通过，否则为会签不通过状态
		NodeStatus signNodeStatus = null;
		if(result.getNodeStatus().getKey().equals(NodeStatus.AGREE.getKey())){
			signNodeStatus = NodeStatus.SIGN_PASS;
		}else if(result.getNodeStatus().getKey().equals(NodeStatus.OPPOSE.getKey())){
			signNodeStatus = NodeStatus.SIGN_NO_PASS;
		}else{
			//获得任务数据
			TaskFinishCmd taskFinishCmd=(TaskFinishCmd)ContextThreadUtil.getActionCmd();
			signNodeStatus = getNodeStatus(taskFinishCmd);
		}
		if(BeanUtils.isEmpty(bpmProStatusManager)){
			bpmProStatusManager = AppUtil.getBean(BpmProStatusManager.class);
		}
		bpmProStatusManager.createOrUpd(instId, defId, nodeId, nodeName,signNodeStatus);
		

		boolean isParallel=!MultiInstanceType.SEQUENTIAL.equals(bpmDelegateExecution.multiInstanceType());
		
		String executeParentId=isParallel?bpmDelegateExecution.getParentExecution().getParentExecution().getId():bpmDelegateExecution.getParentId();
		
		// 并签顺签数据处理
		
		//删除任务和候选人，更新代审批意见数据为取消
		updOpinion(executeParentId, nodeId,result.getOpinionStatus().getKey());
		if(ActionType.APPROVE.equals(actionType)){
			String resultVarName=BpmConstants.SIGN_RESULT + nodeId;
			//设置最终审批结果,以供其后的分支网关做判断。
			bpmDelegateExecution.setVariable(resultVarName, result.getNodeStatus().getKey());
		}
		//删除串行会签节点人员变量。
		String varName=BpmConstants.SIGN_USERIDS + nodeId;
		if(MultiInstanceType.SEQUENTIAL.equals(bpmDelegateExecution.multiInstanceType())){
			bpmDelegateExecution.removeVariable(varName);
		}
	}
	
	
	private void updOpinion(String executeParentId,String nodeId,String status){
		bpmTaskManager = AppUtil.getBean(BpmTaskManager.class);
		bpmCheckOpinionManager = AppUtil.getBean(BpmCheckOpinionManager.class);
		bpmTaskCandidateManager = AppUtil.getBean(BpmTaskCandidateManager.class);
		List<DefaultBpmTask> bpmTasks= bpmTaskManager.getByExeIdAndNodeId(executeParentId, nodeId);
		for(DefaultBpmTask task:bpmTasks){
			String taskId=task.getId();
			// 更新意见状态为取消。
			DefaultBpmCheckOpinion byTaskId = bpmCheckOpinionManager.getByTaskIdStatus(task.getTaskId(),OpinionStatus.AWAITING_CHECK.getKey());
			if (BeanUtils.isNotEmpty(byTaskId)) {
				byTaskId.setAuditor(ContextUtil.getCurrentUserId());
				byTaskId.setAuditorName(ContextUtil.getCurrentUser().getFullname());
				byTaskId.setStatus(status);
				byTaskId.setCompleteTime(LocalDateTime.now());
				bpmCheckOpinionManager.update(byTaskId);
			}
			//删除候选人 
			bpmTaskCandidateManager.removeByTaskId(taskId);
			//删除任务
			bpmTaskManager.remove(taskId);
		}
	}
	
	
	
	@SuppressWarnings("incomplete-switch")
	private NodeStatus getNodeStatus(TaskFinishCmd cmd){
		NodeStatus nodeStatus = NodeStatus.AGREE;
		String action = cmd.getActionName();
		// 在已办中撤回
		Object isDoneUnused = cmd.getTransitVars("IsDoneUnused");
		if (isDoneUnused != null){
			nodeStatus = NodeStatus.RECOVER;
			return nodeStatus;
		}
		switch (cmd.getActionType()){
			case APPROVE:
				nodeStatus = NodeStatus.fromKey(cmd.getActionName());
				break;
			case BACK_TO_START:
				nodeStatus = NodeStatus.BACK_TO_START;
				break;
			case BACK:
				if ("toStart".equals(action) || "backToStart".equals(action)){
					nodeStatus = NodeStatus.BACK_TO_START;
				} 
				else{
					nodeStatus = NodeStatus.BACK;
				}
				break;
			case RECOVER:
				if ("toStart".equals(action)){
					nodeStatus = NodeStatus.RECOVER_TO_START;
				} 
				else{
					nodeStatus = NodeStatus.RECOVER;
				}
				break;
		}
		return nodeStatus;
	}
	
	

}
