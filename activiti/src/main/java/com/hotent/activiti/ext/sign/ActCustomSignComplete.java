package com.hotent.activiti.ext.sign;

import javax.annotation.Resource;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.delegate.DelegateExecution;

import com.hotent.activiti.ext.factory.BpmDelegateFactory;
import com.hotent.bpm.api.model.delegate.BpmDelegateExecution;
import com.hotent.bpm.api.service.CustomSignComplete;

/**
 * 并行签署结束判断。 实际判断丢给SignComplete接口实现。
 * 
 * <pre>
 *  
 * 构建组：x5-bpmx-activiti
 * 作者：jason
 * 邮箱:jason@jee-soft.cn
 * 日期:2014-4-1-下午2:24:32
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class ActCustomSignComplete {

	CustomSignComplete customSignComplete;

	@Resource
	RepositoryService repositoryService;

	public void setBpmSignComplete(CustomSignComplete customSignComplete) {
		this.customSignComplete = customSignComplete;
	}

	public boolean isComplete(DelegateExecution delegateExecution) throws Exception {
		BpmDelegateExecution bpmDelegatetion = BpmDelegateFactory.getBpmDelegateExecution(delegateExecution);
		boolean rtn = customSignComplete.isComplete(bpmDelegatetion);
		return rtn;
	}

}
