package com.hotent.bpm.api.service;

import java.util.List;
import java.util.Map;

import com.hotent.bpm.api.model.process.nodedef.JumpRule;

/**
 * 跳转规则。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-27-上午9:10:11
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface JumpRuleCalc {
	
	/**
	 * 根据条件跳转规则计算跳转到的节点。
	 * @param jumpRule
	 * @return 
	 * String
	 * @exception 
	 * @since  1.0.0
	 */
	String eval(List<? extends JumpRule> jumpRule,Map<String ,Object> params);

}
