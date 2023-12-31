package com.hotent.Calendar.model;

import java.time.LocalDateTime;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hotent.base.entity.BaseModel;
import com.hotent.base.util.time.TimeUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 对象功能:班次时间 entity对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zyp
 * 创建时间:2014-02-18 10:45:58
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@TableName("portal_cal_shift_peroid")
@ApiModel(description="班次时间")
public class CalendarShiftPeroid extends BaseModel<CalendarShiftPeroid>implements Cloneable{
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(name="id", notes="主键")
	@TableId("id_")
	protected String  id;
	
	@TableField("shift_id_")
	@ApiModelProperty("班次ID")
	protected String  shiftId;  
	
	@TableField("start_time_")
	@ApiModelProperty("开始时间")
	protected String  startTime; 
	
	@TableField("end_time_")
	@ApiModelProperty("结束时间")
	protected String  endTime; 
	
	@TableField("memo_")
	@ApiModelProperty("备注")
	protected String  memo;     

	//日期
	@TableField(exist=false)
	protected String calDay="";
	//开始时间
	@TableField(exist=false)
	@Deprecated
	protected LocalDateTime startDateTime;
	//结束时间
	@TableField(exist=false)
	@Deprecated
	protected LocalDateTime endDateTime;

	public String getCalDay() {
		return calDay;
	}
	public void setCalDay(String calDay) {
		this.calDay = calDay;
		String start=calDay +" " + this.startTime;
		String end=calDay +" " + this.endTime;

		this.startDateTime= TimeUtil.getDateTimeByTimeString(start);
		this.endDateTime=TimeUtil.getDateTimeByTimeString(end);
		if(this.startDateTime.compareTo(this.endDateTime)>0){
			this.endDateTime=TimeUtil.getNextDays(this.endDateTime,1);
		}

	}
	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}
	public void setStartDateTime(LocalDateTime startDateTime) {
		this.startDateTime = startDateTime;
	}
	public LocalDateTime getEndDateTime() {
		return endDateTime;
	}
	public void setEndDateTime(LocalDateTime endDateTime) {
		this.endDateTime = endDateTime;
	}
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
	public void setShiftId(String shiftId) 
	{
		this.shiftId = shiftId;
	}
	/**
	 * 返回 班次ID
	 * @return
	 */
	public String getShiftId() 
	{
		return this.shiftId;
	}
	public void setStartTime(String startTime) 
	{
		this.startTime = startTime;
	}
	/**
	 * 返回 开始时间
	 * @return
	 */
	public String getStartTime() 
	{
		return this.startTime;
	}
	public void setEndTime(String endTime) 
	{
		this.endTime = endTime;
	}
	/**
	 * 返回 结束时间
	 * @return
	 */
	public String getEndTime() 
	{
		return this.endTime;
	}
	public void setMemo(String memo) 
	{
		this.memo = memo;
	}
	/**
	 * 返回 备注
	 * @return
	 */
	public String getMemo() 
	{
		return this.memo;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("shiftId", this.shiftId) 
		.append("startTime", this.startTime) 
		.append("endTime", this.endTime) 
		.append("memo", this.memo) 
		.toString();
	}
	
	public Object clone()
	{
		CalendarShiftPeroid obj=null;
		try{
			obj=(CalendarShiftPeroid)super.clone();
		}catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return obj;
	}
}