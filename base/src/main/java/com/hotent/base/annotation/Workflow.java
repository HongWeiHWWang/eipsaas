package com.hotent.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 工作流注解
 * <pre>
 * 在某个方法上添加这个注解，则执行该方法时会自动启动对应的工作流实例。
 * </pre>
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年8月16日
 */
@Target({ElementType.METHOD}) 
@Retention(RetentionPolicy.RUNTIME) 
public @interface Workflow {
	/**
	 * 流程定义key。
	 * @return
	 */
	public String flowKey() default "";
	/**
	 * 业务系统编码
	 * <pre>
	 * 当整个系统中很多模块都通过这种方式启动流程时，传递的业务主键可能会有重复的情况，添加一个业务系统编码可以避免主键重复。
	 * </pre>
	 * @return
	 */
	public String sysCode() default "";
	/**
	 * 实例ID关联字段
	 * <pre>
	 * 流程启动成功后会返回一个流程实例ID，该实例ID可以保存到业务数据中去，通过这个属性可以设置保存这个实例ID的字段。
	 * </pre>
	 * @return
	 */
	public String instanceIdField() default "instanceId";
	/**
	 * 变量keys集合
	 * <pre>
	 * 流程启动时可以传递变量集合，通过keys指定变量的值来自业务实体的哪些字段。
	 * </pre>
	 * @return
	 */
	public String[] varKeys() default "";
}
