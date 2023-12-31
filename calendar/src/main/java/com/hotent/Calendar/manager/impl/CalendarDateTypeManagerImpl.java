package com.hotent.Calendar.manager.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hotent.Calendar.dao.CalendarDateTypeDao;
import com.hotent.Calendar.manager.CalendarDateTypeManager;
import com.hotent.Calendar.model.CalendarDateType;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.time.DateUtil;
import com.hotent.base.util.time.TimeUtil;

@Service("calDateTypeManager")
public class CalendarDateTypeManagerImpl extends BaseManagerImpl<CalendarDateTypeDao, CalendarDateType> implements CalendarDateTypeManager{
	
	/**
	 * 根据年月取法定假日。
	 * map 对象存放天数 {1,"五一"}
	 * @return
	 */
	@Override
	public Map<Integer,String> getLhByYearMon(String statTime, String endTime){
		
		Map<Integer,String> map=new HashMap<Integer, String>();
		
		LocalDateTime startDate=TimeUtil.convertString(statTime, "yyyy-MM-dd");
		LocalDateTime endDate=TimeUtil.convertString(endTime, "yyyy-MM-dd");
		List<CalendarDateType> valist = baseMapper.getLhByYearList();
		int curMonth = Integer.parseInt(statTime.split("-")[1]);
		for(CalendarDateType va:valist){
			if(va.getYearBegin().compareTo(startDate)>=0||va.getYearEnd().compareTo(endDate)<=0){
			LocalDateTime[] days = DateUtil.getDaysBetween(va.getYearBegin(), va.getYearEnd());
			for(LocalDateTime day:days){
				int tmpMonth = day.getMonthValue();
				if(curMonth==tmpMonth){
					map.put(days.length,va.getName());
				}
			}
		}
		}
		return map;
	}


	@Override
	public Map<Integer, String> getPhByWeekMap() {
		Map<Integer, String> map =new HashMap<Integer, String>();
		List<CalendarDateType> phList=baseMapper.getPhByWeekList();
		for(CalendarDateType calendarDateType :phList){
			map.put(calendarDateType.getWeekBegin().intValue(), calendarDateType.getName());
		}
		return map;
	}

}
