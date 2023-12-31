package com.hotent.bpm.api.event;

import org.springframework.context.ApplicationEvent;

/**
 * 任务发起的事件， 构造函数的值为BpmDelegateTask。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-25-下午9:46:15
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class TaskCreateEvent extends ApplicationEvent {

	/**
	 * serialVersionUID
	 * @since 1.0.0
	 */
	private static final long serialVersionUID = 6280544763254770424L;

	public TaskCreateEvent(Object source) {
		super(source);
		
	}

}
