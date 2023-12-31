package com.hotent.bpm.api.event;

import org.springframework.context.ApplicationEvent;

/**
 * 任务通知事件。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-5-8-下午3:57:27
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class TaskNotifyEvent extends ApplicationEvent {

	/**
	 * serialVersionUID
	 * @since 1.0.0
	 */
	private static final long serialVersionUID = 8632666682010738355L;
	
	public TaskNotifyEvent(NotifyTaskModel source) {
		super(source);
	}

}
