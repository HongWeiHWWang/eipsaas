package com.hotent.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据权限过滤
 * <pre>
 * 数据权限过滤
 * </pre>
 *
 * @company 广州宏天软件股份有限公司
 * @author liygui
 * @email liygui@jee-soft.cn
 * @date 2018年9月28日
 */
@Target({ElementType.METHOD}) 
@Retention(RetentionPolicy.RUNTIME) 
public @interface DataPermission {
	
}
