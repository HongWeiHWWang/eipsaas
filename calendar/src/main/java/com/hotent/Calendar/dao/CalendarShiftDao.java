package com.hotent.Calendar.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.Calendar.model.CalendarShift;


public interface CalendarShiftDao  extends BaseMapper<CalendarShift> {

	CalendarShift getUniqueDefaultShift();
	/**
	 * 设置默认
	 * @param id
	 */
	public void setNotDefaultShift();
}


