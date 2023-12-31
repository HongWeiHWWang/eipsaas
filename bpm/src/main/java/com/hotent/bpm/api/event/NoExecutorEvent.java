package com.hotent.bpm.api.event;

import org.springframework.context.ApplicationEvent;

/**
 * 没有执行人事件。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-5-8-下午5:31:20
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class NoExecutorEvent  extends ApplicationEvent {

	/**
	 * serialVersionUID
	 * @since 1.0.0
	 */
	private static final long serialVersionUID = 3079601702283423458L;
	
	public NoExecutorEvent(NoExecutorModel model) {
		super(model);
	}

}
