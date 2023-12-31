package com.hotent.Calendar.manager;

import java.util.Map;

import com.hotent.Calendar.model.CalendarDateType;
import com.hotent.base.manager.BaseManager;

public interface CalendarDateTypeManager extends BaseManager<CalendarDateType>{

	Map<Integer, String> getLhByYearMon(String statTime, String endTime);
	
	//得到周日期范围的公休日
	Map<Integer, String> getPhByWeekMap();
	
	
}
