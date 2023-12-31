package com.hotent.bpm.api.event;

import org.springframework.context.ApplicationEvent;

/**
 * 自动任务事件，构造函数的参数类型为BpmDelegateExecution。
 * <pre> 
 * 监听器：AutoTaskEventListener 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-24-上午11:41:23
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class AutoTaskRunEvent extends ApplicationEvent{

	/**
	 * serialVersionUID
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -8461719811582601640L;
	

	
	public AutoTaskRunEvent(Object source) {
		super(source);
		
	}
	
	

	

}
