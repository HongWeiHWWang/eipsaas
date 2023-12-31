package com.hotent.bpm.api.event;

import org.springframework.context.ApplicationEvent;

import com.hotent.bpm.api.model.delegate.BpmDelegateExecution;

/**
 * 流程发起时事件。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-25-下午9:56:12
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class StartFlowEvent extends ApplicationEvent {

	

	/**
	 * serialVersionUID
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -5046496775571881694L;

	
	public StartFlowEvent(BpmDelegateExecution source) {
		super(source);
	}
}
