package com.hotent.bpm.api.event;

import org.springframework.context.ApplicationEvent;

/**
 * 流程仿真测试， 构造函数的值为BpmDelegateTask。
 * 自动执行任务
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：liygui
 * 邮箱:liygui@jee-soft.cn
 * 日期:2018-01-03-上午9:46:15
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class AutoTestEvent extends ApplicationEvent {

	/**
	 * serialVersionUID
	 * @since 1.0.0
	 */
	private static final long serialVersionUID = 6280544763254770424L;

	public AutoTestEvent(Object source) {
		super(source);
		
	}

}
