package com.hotent.bpm.api.event;

import org.springframework.context.ApplicationEvent;

/**
 * 会签任务创建事件。
 * 构造函数BpmDelegateTask。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-31-下午2:36:23
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class TaskSignCreateEvent extends ApplicationEvent {

	/**
	 * serialVersionUID
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -6299066629712054527L;

	public TaskSignCreateEvent(Object source) {
		super(source);
	}

}
