package com.hotent.Calendar.dao;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.Calendar.model.CalendarSetting;


public interface CalendarSettingDao extends BaseMapper<CalendarSetting> {
	/**
	 * 根据日历ID取得日历列表。
	 * @param calendarId
	 * @return
	 */
	public List<CalendarSetting> getByCalendarId(@Param("calendarId") String calendarId,@Param("startTime") LocalDateTime startTime);

	/**
	 * 根据日历和开始结束时间取得时间段。
	 * @param calendarId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<CalendarSetting> getSegmentByCalId(@Param("calendarId") String calendarId,@Param("startDate") String startDate ,@Param("endDate") String endDate);

	/**
	 * 根据日历id，year，month 得到日历
	 * @param id
	 * @param year
	 * @param month
	 * @return
	 */
	public List<CalendarSetting> getCalByIdYearMon(@Param("id") String id,@Param("year") int year,@Param("month") int month);
	
	/**
	 * 根据日历id，year 得到日历
	 * @param id
	 * @param year
	 * @param month
	 * @return
	 */
	public List<CalendarSetting> getCalByIdYear(@Param("id") String id,@Param("year") int year);

	/**
	 * 根据 日历id，year，month 删除日历
	 * @param calid
	 * @param year
	 * @param month
	 */
	public void delByCalidYearMon(@Param("id") String calid,@Param("year") short year,@Param("month") short month);
	
	/**
	 * 根据日历ID删除所有日历设置
	 * @param calendarId
	 */
	public void delByCalendarId(@Param("calendarId") String calendarId);

	/**
	 * 根据日历id删除记录
	 * @param calId
	 */
	public void delByCalId(@Param("calendarId") String calId);
}
