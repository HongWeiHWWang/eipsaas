package com.hotent.bpm.engine.execution.sign.handler;

import java.util.Map;

import com.hotent.bpm.api.plugin.core.execution.sign.SignActionHandler;

/**
 * 会签处理器容器。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-7-29-上午11:41:32
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class SignActionHandlerContainer {

	Map<String, SignActionHandler> actionHandlers;

	public void setActionHandlers(Map<String, SignActionHandler> actionHandlers) {
		this.actionHandlers = actionHandlers;
	}

	public SignActionHandler getSignActionHandler(String actionType) {
		if(actionHandlers.containsKey(actionType)){
			return actionHandlers.get(actionType);
		}
		return null;
	}
	
	
	
}
