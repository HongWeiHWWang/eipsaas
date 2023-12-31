package com.hotent.bpm.api.event;

import org.springframework.context.ApplicationEvent;

/**
 * 自动任务完成的监听器。
 * <pre> 
 * 描述：自动任务完成时，需要修改节点的状态数据。
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-25-下午2:47:06
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class AutoTaskEndEvent extends ApplicationEvent{

	private static final long serialVersionUID = -1639990460603633278L;

	public AutoTaskEndEvent(Object source) {
		super(source);
		// TODO Auto-generated constructor stub
	}

}
