package com.hotent.bpm.api.event;

import org.springframework.context.ApplicationEvent;

import com.hotent.bpm.api.model.delegate.BpmDelegateExecution;


/**
 * 子流程发起事件，构造函数的传入值为BpmDelegateExecution。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-25-下午9:56:12
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class CallSubProcessStartEvent extends ApplicationEvent {

	/**
	 * serialVersionUID
	 * @since 1.0.0
	 */
	private static final long serialVersionUID = -8204014736320072520L;

	
	public CallSubProcessStartEvent(BpmDelegateExecution bpmDelegateExecution) {
		super(bpmDelegateExecution);
	}
}
