package com.hotent.uc.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateMethod {
	/**
	 * 比对操作的新旧值时，针对哪个参数进行比对
	 * @return
	 */
	Class<?> type() default UpdateCompare.class;
}
