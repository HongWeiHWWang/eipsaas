package com.hotent.Calendar.dao;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.Calendar.model.CalendarDateType;


public interface CalendarDateTypeDao extends BaseMapper<CalendarDateType> {

	List<CalendarDateType> getPhByWeekList();

	List<CalendarDateType> getLhByYearList();

	List<CalendarDateType> getChByYearList();


}
