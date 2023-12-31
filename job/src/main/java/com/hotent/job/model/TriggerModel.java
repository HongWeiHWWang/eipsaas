package com.hotent.job.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 定时计划
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月6日
 */
@ApiModel(description="定时计划实体")
public class TriggerModel {
	

	@ApiModelProperty(name="jobName", notes="任务名称")
	private String jobName="";
	
	@ApiModelProperty(name="triggerName", notes="触发器名称")
	private String triggerName="";
	
	@ApiModelProperty(name="description", notes="描述")
	private String description="";
	
	@ApiModelProperty(name="state", notes="状态()")
	private String state="";
	
	public TriggerModel(){
		
	}
	
	/**
	 * 定时计划有参构造类
	 * @param jobName 任务名称
	 * @param triggerName 触发器名称
	 * @param triggerDescription 描述
	 * @param state 状态
	 */
	public TriggerModel(String jobName, String triggerName, String triggerDescription, String state) {
		this.jobName = jobName;
		this.triggerName = triggerName;
		this.description = triggerDescription;
		this.state = state;
	}
	
	/**
	 * 返回计划名称
	 * @return
	 */
	public String getJobName() {
		return jobName;
	}
	
	/**
	 * 设置计划名称
	 * @param jobName 计划名称
	 */
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	
	/**
	 * 返回触发器名称
	 * @return
	 */
	public String getTriggerName() {
		return triggerName;
	}
	
	/**
	 * 设置触发器名称
	 * @param triggerName 触发器名称
	 */
	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}
	
	/**
	 * 返回计划描述
	 * @return 
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * 设置描述
	 * @param triggerDescription 描述
	 */
	public void setDescription(String triggerDescription) {
		this.description = triggerDescription;
	}
	
	/**
	 * 返回状态
	 * @return
	 */
	public String getState() {
		return state;
	}
	
	/**
	 * 设置状态
	 * @param state 状态
	 */
	public void setState(String state) {
		this.state = state;
	}
}
