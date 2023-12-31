package com.hotent.Calendar.calc.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.SortedMap;

import com.hotent.Calendar.calc.ICalendarCalc;
import com.hotent.Calendar.model.TimePeroid;

public class OverCalc implements ICalendarCalc {
	
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "over";
	}
	
	/**
	 * OverTimeList 转TimePeroid list
	 */
	@Override
	public List<TimePeroid> getRealTimePeroidList(String userId, LocalDateTime start_time,LocalDateTime end_time) {
//		List<OverTime> overTimeList= overTimeManager.getRealOverTimeList( userId,  start_time, end_time);
//		List<TimePeroid> timePeroidList =new ArrayList<TimePeroid>();
//		for(OverTime over : overTimeList){
//			TimePeroid timePeroid=new TimePeroid(over);
//			timePeroidList.add(timePeroid);
//		}
//		return timePeroidList;
		return null;
	}
	
 
	/**
	 * 
	 * 加列表覆盖上班列表
	 * list 覆盖map
	 * Collections.sort(lists);  
	 * @author hjx
	 * @version 创建时间：2014-2-21  上午10:09:46
	 * @param list
	 * @return
	 */
	@Override
	public SortedMap<LocalDateTime,TimePeroid> overrideCalendarShiftPeroidMap(SortedMap<LocalDateTime,TimePeroid> calendarShiftPeroidMap ,List<TimePeroid> overTimePeroidlist){
		//没有加班则直接返回 
		if(overTimePeroidlist==null ||overTimePeroidlist.size()<=0) return calendarShiftPeroidMap;
		for(TimePeroid timePeroid: overTimePeroidlist){//循环加班时间段
			//TimePeroid timePeroid =new TimePeroid(overTime);
			calendarShiftPeroidMap.put(timePeroid.getStartDate(),timePeroid);
		}
		return calendarShiftPeroidMap;
	}

//	@Override
//	public SortedMap<Date, TimePeroid> getTimePeroidMap(String userId,
//			Date startTime, Date endTime) {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
