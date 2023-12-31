package com.hotent.bpm.api.event;

import org.springframework.context.ApplicationEvent;

/**
 * 删除实例事件。
 * 构造函数为流程实例ID。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-5-14-下午2:54:48
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class DeleteInstEvent extends ApplicationEvent {

	/**
	 * serialVersionUID
	 * @since 1.0.0
	 */
	private static final long serialVersionUID = -6954700313881450594L;
	
	
	public DeleteInstEvent(String instId) {
		super(instId);
	}

}
