package com.hotent.Calendar.calc.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.hotent.Calendar.calc.ICalendarCalc;
import com.hotent.Calendar.model.TimePeroid;
/**
 * 请假算法
 * 
 * @author lenovo
 *
 */
public class AbsenceCalc implements ICalendarCalc {
	
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "absence";
	}
	
	/**
	 * 根据开始时间 和结束时间 得到时间段列表
	 * @author hjx
	 * @version 创建时间：2014-2-23  下午5:30:12
	 * @param userId
	 * @param startTime
	 * @param time
	 * @return
	 */
	@Override
	public List<TimePeroid> getRealTimePeroidList(String userId, LocalDateTime start_time, LocalDateTime end_time){
		//TODO hjx
		//List<CalendarAbsence> absenceTimeList= calendarAbsenceManager.getListByUserId( userId,"",start_time, end_time);
		List<TimePeroid> timePeroidList =new ArrayList<TimePeroid>();
//		for(CalendarAbsence absence : absenceTimeList){
//			TimePeroid timePeroid=new TimePeroid(absence);
//			timePeroidList.add(timePeroid);
//		}
		return timePeroidList;
	}
	
	/**
	 * 请假列表覆盖上班列表
	 * list 覆盖map
	 * Collections.sort(lists);  
	 * @author hjx
	 * @version 创建时间：2014-2-21  上午10:09:46
	 * @param list
	 * @return
	 */
	@Override
	public SortedMap<LocalDateTime,TimePeroid> overrideCalendarShiftPeroidMap(SortedMap<LocalDateTime,TimePeroid> treemap ,List<TimePeroid> calendarAbsenceList){
		//没有缺勤记录则直接返回
		if(calendarAbsenceList==null ||calendarAbsenceList.size()<=0)return treemap;
		
		SortedMap<LocalDateTime,TimePeroid> newMap=new TreeMap<LocalDateTime,TimePeroid>();//用于存放新增对象
		
		for(TimePeroid timePeroid: calendarAbsenceList){//循环缺勤时间段	
			LocalDateTime absenceStartDate=timePeroid.getStartDate();
			LocalDateTime absenceEndDate=timePeroid.getEndDate();
			
			 Iterator<Map.Entry<LocalDateTime, TimePeroid>> it = treemap.entrySet().iterator();
			 while(it.hasNext()){//循环上班时间段
		            Map.Entry<LocalDateTime, TimePeroid> entry=it.next();
		            LocalDateTime key=entry.getKey();
		            TimePeroid mtp=entry.getValue();
		            LocalDateTime startDate=mtp.getStartDate();
		            LocalDateTime endDate=mtp.getEndDate();
		          //1，上班时间段完全落在某个请假段内，直接remove这个上班时间段
					//2，上班时间段落在请假开始时间上，
					     //2.1上班时间段落在请假开始时间上，同时这个段也落在请假结束时间上。即请假只是上班时间段的一部分,remove  后还要put2个
					     //2.2上班时间段落在请假开始时间上，
					//3，上班时间段落在请假结束时间上，remove put
		            if(endDate.compareTo(absenceEndDate)<=0&&startDate.compareTo(absenceStartDate)>=0){
		            	it.remove();
		            }
		            else if(endDate.compareTo(absenceStartDate)>=0&&startDate.compareTo(absenceStartDate)<=0){
		            	  if(endDate.compareTo(absenceEndDate)>=0&&startDate.compareTo(absenceEndDate)<=0){
				            	it.remove();
				            	TimePeroid tmpTimePeroid1 =new TimePeroid(startDate,absenceStartDate,TimePeroid.PEROID_TYPE_ABSENCE);
				            	TimePeroid tmpTimePeroid2 =new TimePeroid(absenceEndDate,endDate,TimePeroid.PEROID_TYPE_ABSENCE);
				            	newMap.put(tmpTimePeroid1.getStartDate(),tmpTimePeroid1);
				            	newMap.put(tmpTimePeroid2.getStartDate(),tmpTimePeroid2);
				            }else
				            {
				            	it.remove();
				            	TimePeroid tmpTimePeroid =new TimePeroid(startDate,absenceStartDate,TimePeroid.PEROID_TYPE_ABSENCE);
				            	newMap.put(tmpTimePeroid.getStartDate(),tmpTimePeroid);
				            }
		            }
		            else if(endDate.compareTo(absenceEndDate)>=0&&startDate.compareTo(absenceEndDate)<=0){
		            	it.remove();
		            	TimePeroid tmpTimePeroid =new TimePeroid(absenceEndDate,endDate,TimePeroid.PEROID_TYPE_ABSENCE);
		            	newMap.put(tmpTimePeroid.getStartDate(),tmpTimePeroid);
		            }
			 }
		}
		//putAll可以合并两个MAP，只不过如果有相同的key那么用后面的覆盖前面的
		 treemap.putAll(newMap);
		 return treemap;
	}



}
