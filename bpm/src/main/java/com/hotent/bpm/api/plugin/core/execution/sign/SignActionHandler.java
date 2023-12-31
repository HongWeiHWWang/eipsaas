package com.hotent.bpm.api.plugin.core.execution.sign;

import com.hotent.bpm.api.cmd.TaskFinishCmd;
import com.hotent.bpm.api.model.delegate.BpmDelegateExecution;

/**
 * 会签处理接口。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-7-29-上午11:18:42
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface SignActionHandler {
	
	SignResult handByActionType(TaskFinishCmd cmd,BpmDelegateExecution bpmDelegateExecution) throws Exception;
	
	

}
