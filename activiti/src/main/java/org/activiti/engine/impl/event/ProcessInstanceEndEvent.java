package org.activiti.engine.impl.event;

import org.springframework.context.ApplicationEvent;

import com.hotent.bpm.api.model.delegate.BpmDelegateExecution;

/**
 * 流程结束监听事件。
 * <pre> 
 * 描述：这个事件在流程结束监听中使用，构造函数的传入值为流程实例ID。
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-20-下午4:40:21
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class ProcessInstanceEndEvent extends ApplicationEvent {
	/**
	 * serialVersionUID
	 * @since 1.0.0
	 */
	private static final long serialVersionUID = 3649593203179292948L;

	public ProcessInstanceEndEvent(BpmDelegateExecution source) {
		super(source);
		
	}

}
