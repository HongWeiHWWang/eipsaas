package com.hotent.base.cache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import com.hotent.base.cache.support.ExpireMode;

/**
 * 一级缓存配置项
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年6月16日
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface FirstCache {
    /**
     * 缓存初始Size
     *
     * @return int
     */
    int initialCapacity() default 10;

    /**
     * 缓存最大Size
     *
     * @return int
     */
    int maximumSize() default 5000;

    /**
     * 缓存有效时间
     *
     * @return int
     */
    int expireTime() default 7;
    
    /**
     * 缓存有效时间表达式
     * <p>可以通过SpEL表达式从注解的方法入参中提取有效时间，支持：int类型、String类型的int值，注意：在该属性和{@code expireTime}均提供的情况下，优先获取该属性。</p>
     * 
     * @return
     */
    String expireTimeExp() default "";

    /**
     * 缓存时间单位
     *
     * @return TimeUnit
     */
    TimeUnit timeUnit() default TimeUnit.HOURS;

    /**
     * 缓存失效模式
     *
     * @return ExpireMode
     * @see ExpireMode
     */
    ExpireMode expireMode() default ExpireMode.WRITE;
}
