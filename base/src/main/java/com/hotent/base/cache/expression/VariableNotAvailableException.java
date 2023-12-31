package com.hotent.base.cache.expression;

import org.springframework.expression.EvaluationException;

/**
 * A specific {@link EvaluationException} to mention that a given variable
 * used in the expression is not available in the context.
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年6月16日
 */
@SuppressWarnings("serial")
class VariableNotAvailableException extends EvaluationException {

	private final String name;

	public VariableNotAvailableException(String name) {
		super("Variable '" + name + "' is not available");
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
