package com.hotent.bpm.persistence.util;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.hotent.base.groovy.GroovyScriptEngine;
import com.hotent.bpm.api.model.process.nodedef.JumpRule;
import com.hotent.bpm.api.service.JumpRuleCalc;

/**
 * 跳转规则计算。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-27-上午9:18:43
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@Component
public class JumpRuleCalcImpl implements JumpRuleCalc {
	
	@Resource
	GroovyScriptEngine groovyScriptEngine  ;

	@Override
	public String eval(List<? extends JumpRule> jumpRuleList, Map<String, Object> params) {
		for(JumpRule rule:jumpRuleList){
			String condition=rule.getCondition();
			Boolean rtn= groovyScriptEngine.executeBoolean(condition, params);
			if(rtn){
				return rule.getTargetNode();
			}
		}
		return "";
	}

}
