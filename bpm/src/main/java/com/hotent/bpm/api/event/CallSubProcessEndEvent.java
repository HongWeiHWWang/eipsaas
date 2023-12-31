package com.hotent.bpm.api.event;

import org.springframework.context.ApplicationEvent;

import com.hotent.bpm.api.model.delegate.BpmDelegateExecution;

/**
 * 外部子流程结束事件。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-4-18-下午1:52:34
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class CallSubProcessEndEvent extends ApplicationEvent {

	/**
	 * serialVersionUID
	 * @since 1.0.0
	 */
	private static final long serialVersionUID = 1729093748558427708L;
	
	
	public CallSubProcessEndEvent(BpmDelegateExecution execution) {
		super(execution);
	}

}
