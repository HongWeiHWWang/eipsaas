package com.hotent.base.cache.support;

import java.lang.reflect.Method;

/**
 * Cache key generator. Used for creating a key based on the given method
 * (used as context) and its parameters.
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年6月16日
 */
public interface KeyGenerator {

	/**
	 * Generate a key for the given method and its parameters.
	 * @param target the target instance
	 * @param method the method being called
	 * @param params the method parameters (with any var-args expanded)
	 * @return a generated key
	 */
	Object generate(Object target, Method method, Object... params);

}
