package com.hotent.activiti.ext.sign;

import org.activiti.engine.delegate.DelegateExecution;

import com.hotent.activiti.ext.factory.BpmDelegateFactory;
import com.hotent.bpm.api.model.delegate.BpmDelegateExecution;
import com.hotent.bpm.api.service.SignComplete;


/**
 * 会签结束判断。
 * 实际判断丢给SignComplete接口实现。
 * <pre> 
 * 构建组：x5-bpmx-activiti
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-4-1-下午2:24:32
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class ActSignComplete {

	SignComplete signComplete  ;
	
	public void setBpmSignComplete(SignComplete signComplete_){
		this.signComplete=signComplete_;
	}
	
	public boolean isComplete(DelegateExecution delegateExecution) throws Exception {
		BpmDelegateExecution bpmDelegatetion= BpmDelegateFactory.getBpmDelegateExecution(delegateExecution);
		boolean rtn= signComplete.isComplete(bpmDelegatetion);
		return rtn;
	}

}
