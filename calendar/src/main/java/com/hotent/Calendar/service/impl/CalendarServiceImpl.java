package com.hotent.Calendar.service.impl;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.hotent.Calendar.calc.ICalendarCalc;
import com.hotent.Calendar.manager.CalendarAssignManager;
import com.hotent.Calendar.model.TimePeroid;
import com.hotent.Calendar.util.CalendarUtil;
import com.hotent.base.calendar.ICalendarService;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.time.TimeUtil;

@Primary
@Service
public class CalendarServiceImpl implements ICalendarService {
	@Resource
	CalendarAssignManager calendarAssignManager;
	@Resource
    SortedMap<String, ICalendarCalc> calendarCalcMap;
	
	@Override
	public LocalDateTime getEndTimeByUser(String userId, long time) throws Exception {
		return this.getEndTimeByUser(userId,LocalDateTime.now(), time);
	}
	/**
	 * 根据时长 和开始时间，计算任务完成时间
	 * time  分钟
	 * @throws Exception 
	 */
	@Override
	public LocalDateTime getEndTimeByUser(String userId, LocalDateTime startTime, long time) throws Exception {
		if(BeanUtils.isEmpty(startTime)) return null;
		//暂定一个结束时间为开始后的120天，不够再加120天
		LocalDateTime date=null;
		LocalDateTime endTime=startTime;
		LocalDateTime finalTime=TimeUtil.getNextDays(startTime, 365*30);
		//一直循环，最多循环到任务开始时间往后推3年
		while (date == null && endTime.compareTo(finalTime)<0) {
		    endTime =TimeUtil.getNextDays(endTime, 120);
			SortedMap<LocalDateTime, TimePeroid> timePeroidMap = new TreeMap<LocalDateTime, TimePeroid>();
			// map遍历
			Iterator<Entry<String, ICalendarCalc>> iter = calendarCalcMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, ICalendarCalc> entry = (Map.Entry<String, ICalendarCalc>) iter.next();
				ICalendarCalc calc = (ICalendarCalc) entry.getValue();
				List<TimePeroid> timePeroid = calc.getRealTimePeroidList(userId, startTime, endTime);
				timePeroidMap = calc.overrideCalendarShiftPeroidMap(timePeroidMap, timePeroid);
			}
			// 根据时长找到结束时间
			date = CalendarUtil.getEndTime(timePeroidMap, time);
		}
		 return date;

	}
	
  /**
   * 根据用户开始时间结束时间获取有效的工时
   */
	@Override
	public Long getWorkTimeByUser(String userId, LocalDateTime startTime, LocalDateTime endTime) {
		if(endTime.compareTo(startTime) <= 0)return 0L;
		SortedMap<LocalDateTime,TimePeroid> timePeroidMap=new TreeMap<LocalDateTime,TimePeroid>();
		if(BeanUtils.isEmpty(calendarCalcMap)){
			calendarCalcMap = (SortedMap<String, ICalendarCalc>) AppUtil.getBean("calendarCalcMap");
		}
		// map遍历
		Iterator<Entry<String, ICalendarCalc>> iter = calendarCalcMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, ICalendarCalc> entry = (Map.Entry<String, ICalendarCalc>) iter.next();
			ICalendarCalc calc =(ICalendarCalc) entry.getValue();
			List<TimePeroid> timePeroid=calc.getRealTimePeroidList(userId, startTime, endTime);
			timePeroidMap=calc.overrideCalendarShiftPeroidMap(timePeroidMap,timePeroid);
		}
		return CalendarUtil.getCountTimePeroid(timePeroidMap);
	}
}
