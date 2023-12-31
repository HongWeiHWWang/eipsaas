package com.hotent.activiti.cmd;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

/**
 * 获取顶层流程的流程变量。
 * <pre> 
 * 构建组：x5-bpmx-activiti
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-8-4-下午3:28:19
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class GetSuperVariableCmd  implements Command<Object> {
	
	private String bpmnInstId="";
	private String varName="";
	
	public void setBpmnInstId(String bpmnInstId){
		this.bpmnInstId=bpmnInstId;
	} 
	
	public void setVarName(String varName){
		this.varName=varName;
	} 
	
	public GetSuperVariableCmd(){
	}
	
	public GetSuperVariableCmd(String bpmnInstId,String varName){
		this.bpmnInstId=bpmnInstId;
		this.varName=varName;
	}

	@Override
	public Object execute(CommandContext context) {
		ExecutionEntity execution= context.getExecutionEntityManager().findExecutionById(bpmnInstId);
		if(execution.getSuperExecution()==null){
			return null;
		}
		ExecutionEntity superExecution=execution.getSuperExecution();
		return  superExecution.getVariable(this.varName);
	}

}
