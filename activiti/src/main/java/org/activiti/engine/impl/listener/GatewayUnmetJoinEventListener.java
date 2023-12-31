package org.activiti.engine.impl.listener;

import javax.annotation.Resource;

import org.activiti.engine.impl.entity.GatewayUnmetJoinEventModel;
import org.activiti.engine.impl.event.GatewayUnmetJoinEvent;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.model.delegate.BpmDelegateTask;
import com.hotent.base.exception.WorkFlowException;
import com.hotent.bpm.model.def.SubProcessStartOrEndEventModel;
import com.hotent.bpm.natapi.task.NatTaskService;
import com.hotent.bpm.persistence.manager.impl.BpmExeStackManagerImpl;
import com.hotent.bpm.persistence.model.DefaultBpmTask;

/**
 * 网关聚会时条件未满足时触发事件 luoxw
 */
@Service
@Transactional
public class GatewayUnmetJoinEventListener implements ApplicationListener<GatewayUnmetJoinEvent>, Ordered{

	
	@Resource
	BpmExeStackManagerImpl bpmExeStackManager;
	@Resource
	NatTaskService natTaskService;

	@Override
	public int getOrder(){
		return 1;
	}

	@Override
	public void onApplicationEvent(GatewayUnmetJoinEvent ev)
	{
		GatewayUnmetJoinEventModel eventModel = (GatewayUnmetJoinEventModel) ev.getSource();
	
		String nodeId = eventModel.getActivity() .getId();
		String nodeName = eventModel.getActivityExecution().getCurrentActivityName();
		
		ActionCmd cmd = ContextThreadUtil.getActionCmd();
		DefaultBpmTask bpmTask = (DefaultBpmTask) cmd.getTransitVars(BpmConstants.BPM_TASK);
		
		if(bpmTask==null) return;
 
		if (eventModel.getFlag() != null && eventModel.getFlag().equals("ParallelMultiInstanceEnd")){
			// 并行多实例非最后一个结束时记堆栈，这时特殊需要换成结束那个结点的ID
			// 并行多实例时会发布结束结节的事件，所以可以取到结束结点事件的数据，因为这里未来聚合的网关事件取到的是子流程块的节点，所以需要替换成结束的结点
			SubProcessStartOrEndEventModel model = (SubProcessStartOrEndEventModel) cmd.getTransitVars("SubProcessStartOrEndEventModel");
			if(model==null) return;
			if (model != null){
				nodeId = model.getNodeId();
				nodeName = model.getNodeName();
			} 
		}

		// 注意此任务是上一节点的来向任务
		String oleNodeId = bpmTask.getNodeId();
		String oleNodeName = bpmTask.getName();

		bpmTask.setNodeId(nodeId);
		bpmTask.setName(nodeName);

		bpmTask.setIsGateWay(true);
		ContextThreadUtil.addTask(bpmTask);
		cmd.addTransitVars("CurrentEventType", "GatewayUnmetJoinEvent");
		cmd.addTransitVars("GatewayUnmetNoteType", eventModel.getNoteType());

		// 流程引擎的任务。
		if (StringUtil.isNotZeroEmpty(bpmTask.getTaskId())){
			BpmDelegateTask task  = natTaskService.getByTaskId(bpmTask.getTaskId());
			try {
				bpmExeStackManager.pushStack(task);
			} catch (Exception e) {
				throw new WorkFlowException(ExceptionUtils.getRootCauseMessage(e));
			}
		}
				
		bpmTask.setNodeId(oleNodeId);
		bpmTask.setName(oleNodeName);
	}
}
