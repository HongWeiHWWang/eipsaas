package com.hotent.Calendar.manager;

import java.time.LocalDateTime;
import java.util.List;

import com.hotent.Calendar.model.CalendarShiftPeroid;
import com.hotent.Calendar.model.TimePeroid;
import com.hotent.base.manager.BaseManager;


public interface CalendarShiftPeroidManager  extends BaseManager<CalendarShiftPeroid>{

	List<TimePeroid> getRealShiftPeroidList(String userId, LocalDateTime start_time,
			LocalDateTime end_time);

	List<CalendarShiftPeroid> getByShiftId(String shiftId);

	void shiftPeroidAdd(String shiftId, String[] startTime, String[] endTime,
			String[] memo);
	
}

