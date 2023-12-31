package com.hotent.Calendar.calc.impl;


import java.time.LocalDateTime;
import java.util.List;
import java.util.SortedMap;

import javax.annotation.Resource;

import com.hotent.Calendar.calc.ICalendarCalc;
import com.hotent.Calendar.manager.CalendarShiftPeroidManager;
import com.hotent.Calendar.model.TimePeroid;
import com.hotent.Calendar.util.CalendarUtil;

public class ShiftCalc implements ICalendarCalc {
	@Resource
	CalendarShiftPeroidManager calendarShiftPeroidManager;

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "shift";
	}

	@Override
	public List<TimePeroid> getRealTimePeroidList(String userId, LocalDateTime startTime,
			LocalDateTime endTime) {
		List<TimePeroid> list=calendarShiftPeroidManager.getRealShiftPeroidList(userId,startTime,endTime);
		return list;
	}

	@Override
	public SortedMap<LocalDateTime, TimePeroid> overrideCalendarShiftPeroidMap(
			SortedMap<LocalDateTime, TimePeroid> calendarShiftPeroidMap,
			List<TimePeroid> shiftTimePeroidlist) {
		// TODO Auto-generated method stub
		return CalendarUtil.getTimePeroidMap(shiftTimePeroidlist);
	}

}
