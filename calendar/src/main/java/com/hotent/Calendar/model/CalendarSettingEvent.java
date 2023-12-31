package com.hotent.Calendar.model;

import java.util.ArrayList;
import java.util.List;

import com.hotent.Calendar.util.CalendarUtil;

/**
 * 日历设置事件
 * <pre>
 * 日历设置事件是可以跨多个自然天的对象
 * </pre>
 * @author heyifan
 */
public class CalendarSettingEvent {
	private Integer startYear;			/*开始的年份*/
	private Integer endYear;			/*结束的年份*/
	private Integer startMonth;			/*开始的月份*/
	private Integer endMonth;			/*结束的月份*/
	private Integer startDay;			/*开始的那天*/
	private Integer endDay;				/*结束的那天*/
	private String shiftId;				/*对应的班次ID*/
	
	public CalendarSettingEvent(){
	}
	
	public CalendarSettingEvent(Integer startYear, Integer endYear, Integer startMonth, Integer endMonth,
								Integer startDay, Integer endDay, String shiftId){
		this.startYear = startYear;
		this.endYear = endYear;
		this.startMonth = startMonth;
		this.endMonth = endMonth;
		this.startDay = startDay;
		this.endDay = endDay;
		this.shiftId = shiftId;
	}
	
	public CalendarSettingEvent(Long startYear, Long endYear, Long startMonth, Long endMonth,
								Long startDay, Long endDay, String shiftId){
		this.startYear = startYear.intValue();
		this.endYear = endYear.intValue();
		this.startMonth = startMonth.intValue();
		this.endMonth = endMonth.intValue();
		this.startDay = startDay.intValue();
		this.endDay = endDay.intValue();
		this.shiftId = shiftId;
	}
	
	public Integer getStartYear() {
		return startYear;
	}
	public void setStartYear(Integer startYear) {
		this.startYear = startYear;
	}
	public Integer getEndYear() {
		return endYear;
	}
	public void setEndYear(Integer endYear) {
		this.endYear = endYear;
	}
	public Integer getStartMonth() {
		return startMonth;
	}
	public void setStartMonth(Integer startMonth) {
		this.startMonth = startMonth;
	}
	public Integer getEndMonth() {
		return endMonth;
	}
	public void setEndMonth(Integer endMonth) {
		this.endMonth = endMonth;
	}
	public Integer getStartDay() {
		return startDay;
	}
	public void setStartDay(Integer startDay) {
		this.startDay = startDay;
	}
	public Integer getEndDay() {
		return endDay;
	}
	public void setEndDay(Integer endDay) {
		this.endDay = endDay;
	}
	public String getShiftId() {
		return shiftId;
	}
	public void setShiftId(String shiftId) {
		this.shiftId = shiftId;
	}
	
	/**
	 * 将事件分拆为以月为单位
	 * @return
	 * @throws Exception
	 */
	public List<CalendarSettingEvent> partition() throws Exception{
		List<CalendarSettingEvent> list = new ArrayList<CalendarSettingEvent>();
		//跨年的事件，将其按年分拆
		if(yearsMinus() > 0){
			Integer daysOfMonth = CalendarUtil.getDaysOfMonth(this.startYear, 12);
			CalendarSettingEvent startEvent = new CalendarSettingEvent(this.startYear, this.startYear,this.startMonth, 
																		12 ,this.startDay, daysOfMonth ,this.shiftId);
			CalendarSettingEvent endEvent = new CalendarSettingEvent(this.endYear,this.endYear, 1 ,this.endMonth, 
																		1, this.endDay,this.shiftId);
			list.add(startEvent);
			list.add(endEvent);
		}
		//跨两个月的，按月分拆为两个
		else if(monthMinus() == 1){
			Integer daysOfMonth = CalendarUtil.getDaysOfMonth(this.startYear, this.startMonth);
			CalendarSettingEvent startEvent = new CalendarSettingEvent(this.startYear, this.startYear,this.startMonth, 
																	   this.startMonth ,this.startDay, daysOfMonth ,this.shiftId);
			CalendarSettingEvent endEvent = new CalendarSettingEvent(this.startYear, this.startYear,this.endMonth, 
																	 this.endMonth , 1 ,this.endDay ,this.shiftId);
			list.add(startEvent);
			list.add(endEvent);
		}
		//跨三个月的，按月分拆为三个
		else if(monthMinus() == 2){
			Integer daysOfMonth = CalendarUtil.getDaysOfMonth(this.startYear, this.startMonth);
			CalendarSettingEvent startEvent = new CalendarSettingEvent(this.startYear, this.startYear,this.startMonth, 
																	   this.startMonth ,this.startDay, daysOfMonth ,this.shiftId);
			Integer daysOfMonth2 = CalendarUtil.getDaysOfMonth(this.startYear, this.startMonth + 1);
			CalendarSettingEvent midEvent = new CalendarSettingEvent(this.startYear, this.startYear,this.startMonth + 1, 
					   												   this.startMonth + 1 ,1 , daysOfMonth2 ,this.shiftId);
			CalendarSettingEvent endEvent = new CalendarSettingEvent(this.startYear, this.startYear,this.endMonth, 
																	 this.endMonth , 1 ,this.endDay ,this.shiftId);
			list.add(startEvent);
			list.add(midEvent);
			list.add(endEvent);
		}
		return list;
	}
	
	/**
	 * 是否需要分拆
	 * @return
	 * @throws Exception
	 */
	public Boolean shouldPartition() throws Exception{
		if(yearsMinus() > 0) return true;
		else if(monthMinus() > 0) return true;
		else return false;
	}
	
	/**
	 * 获取年差
	 * @return
	 */
	private Integer yearsMinus() throws Exception{
		Integer minus = this.endYear-this.startYear;
		if(minus < 0 || minus > 1){
			throw new RuntimeException("工作日历的设置有误");
		}
		return minus;
	}
	
	/**
	 * 获取月差
	 * @return
	 */
	private Integer monthMinus() throws Exception{
		Integer minus = this.endMonth - this.startMonth;
		if(minus < 0 || minus > 1){
			throw new RuntimeException("工作日历的设置有误");
		}
		return minus;
	}
}