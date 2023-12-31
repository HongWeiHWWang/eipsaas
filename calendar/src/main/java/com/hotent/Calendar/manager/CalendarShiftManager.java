package com.hotent.Calendar.manager;

import com.hotent.Calendar.model.CalendarShift;
import com.hotent.base.manager.BaseManager;


public interface CalendarShiftManager  extends BaseManager<CalendarShift>{
	
	void setDefaultShift(String id);
	
}

