package com.hotent.Calendar.manager;

import java.time.LocalDateTime;
import java.util.List;

import com.hotent.Calendar.model.CalendarSetting;
import com.hotent.Calendar.model.CalendarShiftPeroid;
import com.hotent.base.manager.BaseManager;

public interface CalendarSettingManager extends BaseManager<CalendarSetting>{
	/**
	 * 根据日历查询时间。
	 * 将时间进行分段。
	 * 开始时间1 结束时间1
	 * 开始时间2 结束时间2
	 * @param calendarId
	 * @return
	 */
	List<CalendarShiftPeroid> getByCalendarId(String calendarId,LocalDateTime startTime);
	/**
	 * 获取工作日历设置
	 * 以日历ID，开始时间和结束时间为备件获取所有符合备件的工作时间的列表。
	 * @param calendarId 日历ID
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return 工作时间的列表。
	 */
	List<CalendarSetting> getByCalendarId(String calendarId,LocalDateTime startTime,LocalDateTime endTime);
	/**
	 * 获取工作日历设置
	 * 以日历ID，年份，月份为查询条件
	 * @param id 日历ID
	 * @param year 年份
	 * @param month 月份
	 * @return
	 */
	List<CalendarSetting> getCalByIdYearMon(String id, int year, int month);
	/**
	 * 获取工作日历设置
	 * 以日历ID，年份为查询条件
	 * @param id
	 * @param year
	 * @return
	 */
	List<CalendarSetting> getCalByIdYear(String id, int year);	
	/**
	 * 根据 日历id，year，month 删除日历
	 * @param calid 日历ID
	 * @param year 年份
	 * @param month 月份
	 */
	void delByCalidYearMon(String calid, Short year, Short month);
	/**
	 * 根据日历id删除记录
	 * @param calIds 日历ID
	 */
	void delByCalId(String[] calIds);
	
	List<CalendarSetting> generateCalendarByDateType(LocalDateTime startTime, LocalDateTime endTime);
}
