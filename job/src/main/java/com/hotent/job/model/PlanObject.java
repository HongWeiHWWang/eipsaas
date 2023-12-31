package com.hotent.job.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 计划对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月7日
 */
@ApiModel(description="计划对象")
public class PlanObject {
	/** 启动一次 */
	public static final int TYPE_FIRST = 1;
	/** 每分钟执行 */
	public static final int TYPE_PER_MINUTE = 2;
	/** 每天时间点执行 */
	public static final int TYPE_PER_DAY = 3;
	/** 每周时间点执行 */
	public static final int TYPE_PER_WEEK = 4;
	/** 每月执行 */
	public static final int TYPE_PER_MONTH = 5;
	/** cron表达式 */
	public static final int TYPE_CRON = 6;

	/**
	 * 计划类型
	 * 
	 * <pre>
	 * 1:启动一次 
	 * 2:每分钟执行
	 * 3:每天时间点执行 
	 * 4:每周时间点执行 
	 * 5:每月执行 
	 * 6:cron表达式
	 * </pre>
	 */
	@ApiModelProperty(name="type", notes="计划类型(1:启动一次  2:每分钟执行  3:每天时间点执行  4:每周时间点执行  5:每月执行  6:cron表达式)")
	private int type = 0;

	/**
	 * 定时间隔
	 */
	@ApiModelProperty(name="timeInterval", notes="定时间隔")
	private String timeInterval = "";
	
	/**
	 * 返回计划类型
	 * @return
	 */
	public int getType() {

		return type;
	}
	
	/**
	 * 设置计划类型
	 * @param type 类型
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	/**
	 * 返回时间间隔
	 * @return
	 */
	public String getTimeInterval() {
		return timeInterval;
	}
	
	/**
	 * 设置时间间隔
	 * @param timeInterval 时间间隔
	 */
	public void setTimeInterval(String timeInterval) {
		this.timeInterval = timeInterval;
	}

}
