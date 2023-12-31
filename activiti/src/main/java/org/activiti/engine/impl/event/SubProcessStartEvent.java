package org.activiti.engine.impl.event;

import org.springframework.context.ApplicationEvent;

/**
 * 子流程开始触发时事件
 * <pre> 
 * 作者：luoxw
 * 邮箱: 
 * 日期:2015-6-18
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class SubProcessStartEvent extends ApplicationEvent {

	/**
	 * serialVersionUID
	 * @since 1.0.0
	 */
	private static final long serialVersionUID = 6280544763254770424L;

	public SubProcessStartEvent(Object source) {
		super(source);
		
	}

}
