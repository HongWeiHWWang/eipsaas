package com.hotent.job.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="定时任务vo对象")
public class SchedulerVo {
	@ApiModelProperty("类名")
	protected String className;
	@ApiModelProperty("任务名")
	protected String jobName;
	@ApiModelProperty("参数")
	protected String parameterJson;
	@ApiModelProperty("描述")
	protected String description;

	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getParameterJson() {
		return parameterJson;
	}
	public void setParameterJson(String parameterJson) {
		this.parameterJson = parameterJson;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
