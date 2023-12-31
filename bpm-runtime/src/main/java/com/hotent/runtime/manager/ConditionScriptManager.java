package com.hotent.runtime.manager;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.hotent.base.manager.BaseManager;
import com.hotent.runtime.model.ConditionScript;

 

public interface ConditionScriptManager extends BaseManager<ConditionScript> {

	/**
	 * 根据类名获取方法
	 * @param className
	 * @param conditionScript	:初始化对象
	 * @param type:1条件脚本，2人员脚本
	 * @return
	 */
	public ArrayNode getMethodsByClassName(String className, ConditionScript conditionScript,Integer type) throws Exception;

}
