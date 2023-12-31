package com.hotent.job.model;


import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.hotent.base.entity.BaseModel;
import org.apache.commons.lang.builder.ToStringBuilder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * 系统定时计划日志
 *
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月6日
 */
@TableName("portal_sys_joblog")
@ApiModel(description="系统定时计划日志")
public class SysJobLog  extends Model<SysJobLog> {
	@ApiModelProperty(name="id", notes="主键")
	@TableId("id_")
	protected String id;

	@ApiModelProperty(name="jobName", notes="任务名")
	@TableField("job_name_")
	protected String jobName;

	@ApiModelProperty(name="trigName", notes="触发器名称")
	@TableField("trig_name_")
	protected String trigName;

	@ApiModelProperty(name="startTime", notes="开始时间")
	@TableField("start_time_")
	protected LocalDateTime startTime;

	@ApiModelProperty(name="endTime", notes="结束时间")
	@TableField("end_time_")
	protected LocalDateTime endTime;

	@ApiModelProperty(name="content", notes="内容")
	@TableField("content_")
	protected String content;

	@ApiModelProperty(name="state", notes="状态(0:未成功 1:成功)", allowableValues="0,1")
	@TableField("state_")
	protected String state;

	@ApiModelProperty(name="runTime",notes="运行时长")
	@TableField("run_time_")
	protected Long runTime;

	/**
	 * 定时计划日志无参构造方法
	 */
	public SysJobLog(){}



	/**
	 * 定时计划日志有参构造方法
	 * @param jobName 任务名
	 * @param trigName 触发器名称
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param content 内容
	 * @param state 状态
	 * @param runTime 运行时长
	 */
	public SysJobLog(String jobName, String trigName, LocalDateTime startTime, LocalDateTime endTime, String content, String state,
			Long runTime) {
		super();
		this.jobName = jobName;
		this.trigName = trigName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.content = content;
		this.state = state;
		this.runTime = runTime;
	}



	/**
	 * 设置主键
	 * @param id 主键
	 */
	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * 返回 主键
	 * @return
	 */
	public String getId()
	{
		return this.id;
	}

	/**
	 * 设置任务名
	 * @param jobName 任务名
	 */
	public void setJobName(String jobName)
	{
		this.jobName = jobName;
	}

	/**
	 * 返回 任务名
	 * @return
	 */
	public String getJobName()
	{
		return this.jobName;
	}

	/**
	 * 设置触发器名称
	 * @param trigName 触发器名称
	 */
	public void setTrigName(String trigName)
	{
		this.trigName = trigName;
	}

	/**
	 * 返回 触发器名称
	 * @return
	 */
	public String getTrigName()
	{
		return this.trigName;
	}

	/**
	 * 设置开始时间
	 * @param startTime 开始时间
	 */
	public void setStartTime(LocalDateTime startTime)
	{
		this.startTime = startTime;
	}
	/**
	 * 返回 开始时间
	 * @return
	 */
	public LocalDateTime getStartTime()
	{
		return this.startTime;
	}

	/**
	 * 设置结束时间
	 * @param endTime 结束时间
	 */
	public void setEndTime(LocalDateTime endTime)
	{
		this.endTime = endTime;
	}

	/**
	 * 返回 结束时间
	 * @return
	 */
	public LocalDateTime getEndTime()
	{
		return this.endTime;
	}

	/**
	 * 设置内容
	 * @param content 内容
	 */
	public void setContent(String content)
	{
		this.content = content;
	}

	/**
	 * 返回 内容
	 * @return
	 */
	public String getContent()
	{
		return this.content;
	}

	/**
	 * 设置状态
	 * @param state 状态
	 */
	public void setState(String state)
	{
		this.state = state;
	}

	/**
	 * 返回 状态
	 * @return
	 */
	public String getState()
	{
		return this.state;
	}

	/**
	 * 设置运行时长
	 * @param runTime 运行时长
	 */
	public void setRunTime(Long runTime)
	{
		this.runTime = runTime;
	}

	/**
	 * 返回 运行时长
	 * @return
	 */
	public Long getRunTime()
	{
		return this.runTime;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return new ToStringBuilder(this)
		.append("id", this.id)
		.append("jobName", this.jobName)
		.append("trigName", this.trigName)
		.append("startTime", this.startTime)
		.append("endTime", this.endTime)
		.append("content", this.content)
		.append("state", this.state)
		.append("runTime", this.runTime)
		.toString();
	}
}
