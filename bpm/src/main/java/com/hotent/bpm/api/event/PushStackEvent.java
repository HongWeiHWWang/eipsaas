package com.hotent.bpm.api.event;

import org.springframework.context.ApplicationEvent;

/**
 * 
 * <pre> 
 * bpm 发布添加堆栈事件  
 * activiti 监听事件添加堆栈数据
 * 构建组：x5-bpmx-api
 * 作者：liygui
 * 邮箱:liygui@jee-soft.cn
 * 日期:2020-03-23
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class PushStackEvent extends ApplicationEvent {

	/**
	 * serialVersionUID
	 * @since 1.0.0
	 */
	private static final long serialVersionUID = 6280544763254770424L;

	public PushStackEvent(Object source) {
		super(source);
		
	}

}
