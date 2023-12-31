package com.hotent.bpm.api.event;

import org.springframework.context.ApplicationEvent;

import com.hotent.bpm.api.model.process.def.BpmDefinition;

/**
 * 流程定义删除时需要处理事件。
 * <pre> 
 * 描述：流程定义删除时需要处理事件。
 * 构造函数参数为entityId，即流程定义ID。
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-23-下午11:11:13
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class BpmDefinitionDelEvent  extends ApplicationEvent {

	/**
	 * serialVersionUID
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 5716608067805584876L;

	public BpmDefinitionDelEvent(BpmDefinition source) {
		super(source);
		
	}

}
