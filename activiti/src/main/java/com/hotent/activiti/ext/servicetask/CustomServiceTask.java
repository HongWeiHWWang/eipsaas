package com.hotent.activiti.ext.servicetask;

import java.util.List;

import javax.annotation.Resource;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import com.hotent.activiti.ext.factory.BpmDelegateFactory;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.constant.EventType;
import com.hotent.bpm.api.constant.NodeStatus;
import com.hotent.bpm.api.model.delegate.BpmDelegateExecution;
import com.hotent.bpm.api.plugin.core.cmd.ExecutionCommand;
import com.hotent.bpm.api.service.BpmProStatusService;

/**
 * 自动任务处理类。
 * 
 * <pre> 
 * 构建组：x5-bpmx-activiti
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-24-上午10:53:30
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class CustomServiceTask implements JavaDelegate {
	
	@Resource
	BpmProStatusService bpmProStatusService;
	
	private List<ExecutionCommand> executionCommands;
	
	public List<ExecutionCommand> getExecutionCommands() {
		return executionCommands;
	}

	public void setExecutionCommands(List<ExecutionCommand> executionCommands) {
		this.executionCommands = executionCommands;
	}

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		//获取节点配置。
		BpmDelegateExecution bpmExecution= BpmDelegateFactory.getBpmDelegateExecution(execution);
		
		if(executionCommands!=null ){
			for(ExecutionCommand cmd:executionCommands){
				cmd.execute(EventType.AUTO_TASK_EVENT, bpmExecution);
			}
		}
		String instId=(String)bpmExecution.getVariable(BpmConstants.PROCESS_INST_ID);
		//标记自动任务节点为完成状态。
		bpmProStatusService.createOrUpd(instId, bpmExecution.getBpmnDefId(), bpmExecution.getNodeId(), bpmExecution.getNodeName(), NodeStatus.COMPLETE);
	}

}
