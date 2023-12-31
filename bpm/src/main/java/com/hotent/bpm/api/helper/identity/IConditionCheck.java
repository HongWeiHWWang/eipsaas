package com.hotent.bpm.api.helper.identity;

import com.hotent.bpm.api.plugin.core.session.BpmUserCalcPluginSession;

/**
 * 条件检查接口。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-7-8-下午2:42:17
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface IConditionCheck {
	
	boolean check(String condition,String model,BpmUserCalcPluginSession session);

}
