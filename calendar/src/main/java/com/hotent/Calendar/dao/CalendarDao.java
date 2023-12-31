package com.hotent.Calendar.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.Calendar.model.Calendar;


public interface CalendarDao extends BaseMapper<Calendar> {
	/**
	 * 取得默认的日历。
	 * @return
	 */
	public Calendar getDefaultCalendar();
	
	/**
	 * 设置默认日历
	 * @param id
	 */
	public void setNotDefaultCal();

}


