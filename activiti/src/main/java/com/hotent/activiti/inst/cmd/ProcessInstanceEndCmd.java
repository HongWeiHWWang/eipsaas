package com.hotent.activiti.inst.cmd;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

public class ProcessInstanceEndCmd  implements Command<Void>{
	private String processInstanceId=null;
	
	public ProcessInstanceEndCmd(String processInstanceId){
		this.processInstanceId=processInstanceId;
	}
	
	@Override
	public Void execute(CommandContext cmdContext) {
		ExecutionEntity executionEntity =cmdContext.getExecutionEntityManager().findExecutionById(processInstanceId);
		ExecutionEntity parentEnt= getTopExecution(executionEntity);
		parentEnt.end();
		return null;
	}
	
	private ExecutionEntity getTopExecution(ExecutionEntity executionEntity){
		ExecutionEntity parentEnt= executionEntity.getParent();
		if(parentEnt==null){
			return executionEntity;
		}
		return getTopExecution(parentEnt);
	}

}
