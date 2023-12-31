package com.hotent.Calendar.model;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 对象功能:日历设置 entity对象
 * 开发公司:广州宏天软件有限公司
 * 开发人员:zyp
 * 创建时间:2014-01-07 16:06:46
 */
@TableName("portal_cal_setting")
@ApiModel(description="日历设置")
public class CalendarSetting extends BaseModel<CalendarSetting>{
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(name="id", notes="主键")
	@TableId("id_")
	protected String  id;
	
	@TableField("calendar_Id_")
	@ApiModelProperty("日历ID")
	protected String  calendarId;  
	
	@TableField("years_")
	@ApiModelProperty("年份")
	protected Long   years;   
	
	@TableField("months_")
	@ApiModelProperty("月份")
	protected Long   months;     
	
	@TableField("days_")
	@ApiModelProperty("天数")
	protected Long   days;    
	
	@TableField("date_type_")
	@ApiModelProperty("上班类型")
	protected String  dateType;
	
	@TableField("shift_id_")
	@ApiModelProperty("班次ID")
	protected String  shiftId;   
	
	@TableField("cal_Day_")
	@ApiModelProperty("CALDAY")
	protected String  calDay;      
	
	@TableField(exist=false)
	protected String wtName=""; /*班次名称*/

	/**
	 *DateType1上班 
	 */
	public static final String SETTING_DATETYPE_WORK="1";
	/**
	 * DateType 2休假
	 */
	public static final String SETTING_DATETYPE_HOLIDAY="2";
	
	//是否法定假日
	@Deprecated
	@TableField(exist=false)
	protected boolean isLegal=false;
	
	/**
	 * 班次时间列表
	 */
	protected List<CalendarShiftPeroid> calendarShiftPeroidList;
	
	public List<CalendarShiftPeroid> getCalendarShiftPeroidList() {
		return calendarShiftPeroidList;
	}
	public void setCalendarShiftPeroidList(
			List<CalendarShiftPeroid> calendarShiftPeroidList) {
		this.calendarShiftPeroidList = calendarShiftPeroidList;
	}
	
	public boolean isLegal() {
		return isLegal;
	}
	public void setLegal(boolean isLegal) {
		this.isLegal = isLegal;
	}
	public String getWtName() {
		return wtName;
	}
	public void setWtName(String wtName) {
		this.wtName = wtName;
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
	public void setCalendarId(String calendarId) 
	{
		this.calendarId = calendarId;
	}
	/**
	 * 返回 日历ID
	 * @return
	 */
	public String getCalendarId() 
	{
		return this.calendarId;
	}
	public void setYears(Long years) 
	{
		this.years = years;
	}
	/**
	 * 返回 年份
	 * @return
	 */
	public Long getYears() 
	{
		return this.years;
	}
	public void setMonths(Long months) 
	{
		this.months = months;
	}
	/**
	 * 返回 月份
	 * @return
	 */
	public Long getMonths() 
	{
		return this.months;
	}
	public void setDays(Long days) 
	{
		this.days = days;
	}
	/**
	 * 返回 天数
	 * @return
	 */
	public Long getDays() 
	{
		return this.days;
	}
	public void setDateType(String dateType) 
	{
		this.dateType = dateType;
	}
	/**
	 * 返回 上班类型
	 * @return
	 */
	public String getDateType() 
	{
		return this.dateType;
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
	public void setCalDay(String calDay) 
	{
		this.calDay = calDay;
	}
	/**
	 * 返回 CALDAY
	 * @return
	 */
	public String getCalDay() 
	{
		return this.calDay;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("calendarId", this.calendarId) 
		.append("years", this.years) 
		.append("months", this.months) 
		.append("days", this.days) 
		.append("dateType", this.dateType) 
		.append("shiftId", this.shiftId) 
		.append("calDay", this.calDay) 
		.toString();
	}
}