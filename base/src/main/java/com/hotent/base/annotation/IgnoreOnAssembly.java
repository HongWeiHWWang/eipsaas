package com.hotent.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在Assembly中忽略这个类(不注入这个类到spring容器中)
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2019年5月7日
 */
@Target({ElementType.TYPE}) 
@Retention(RetentionPolicy.RUNTIME) 
public @interface IgnoreOnAssembly {
}