package com.hotent.base.cache.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 二级缓存配置项
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
public @interface SecondaryCache {
    /**
     * 缓存有效时间
     *
     * @return long
     */
    int expireTime() default 24;
    
    /**
     * 缓存有效时间表达式
     * <p>可以通过SpEL表达式从注解的方法入参中提取有效时间，支持：int类型、String类型的int值，注意：在该属性和{@code expireTime}均提供的情况下，优先获取该属性。</p>
     * 
     * @return
     */
    String expireTimeExp() default "";

    /**
     * 缓存主动在失效前强制刷新缓存的时间
     * 建议是： preloadTime = expireTime * 0.2
     *
     * @return long
     */
    int preloadTime() default 5;

    /**
     * 时间单位 {@link TimeUnit}
     *
     * @return TimeUnit
     */
    TimeUnit timeUnit() default TimeUnit.HOURS;

    /**
     * 是否强制刷新（直接执行被缓存方法），默认是false
     *
     * @return boolean
     */
    boolean forceRefresh() default false;

    /**
     * 是否允许缓存NULL值
     *
     * @return boolean
     */
    boolean isAllowNullValue() default false;

    /**
     * 非空值和null值之间的时间倍率，默认是1。isAllowNullValue=true才有效
     * <p>
     * 如配置缓存的有效时间是200秒，倍率这设置成10，
     * 那么当缓存value为null时，缓存的有效时间将是20秒，非空时为200秒
     * </p>
     *
     * @return int
     */
    int magnification() default 1;
}
