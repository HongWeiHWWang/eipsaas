package com.hotent.bpm.api.event;

import org.springframework.context.ApplicationEvent;


/**
 * 
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-6-6-下午1:41:57
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class BpmStartEvent extends ApplicationEvent{

	/**
	 * serialVersionUID
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -9180179153792522401L;

	public BpmStartEvent(BpmStartModel source) {
		super(source);
		
	}
	
	
	
}
