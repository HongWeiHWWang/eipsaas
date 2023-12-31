package com.hotent.Calendar.manager.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.Calendar.dao.CalendarShiftPeroidDao;
import com.hotent.Calendar.manager.CalendarAssignManager;
import com.hotent.Calendar.manager.CalendarSettingManager;
import com.hotent.Calendar.manager.CalendarShiftPeroidManager;
import com.hotent.Calendar.model.CalendarSetting;
import com.hotent.Calendar.model.CalendarShiftPeroid;
import com.hotent.Calendar.model.TimePeroid;
import com.hotent.base.id.IdGenerator;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.time.TimeUtil;

@Service("calendarShiftPeroidManager")
public class CalendarShiftPeroidManagerImpl extends BaseManagerImpl<CalendarShiftPeroidDao, CalendarShiftPeroid> implements CalendarShiftPeroidManager{
	@Resource
	CalendarAssignManager calendarAssignManager;
	@Resource
	CalendarSettingManager calendarSettingManager;
	@Resource
	IdGenerator idGenerator;
	/**
	 * 根据开始时间 和结束时间 得到实际上班时间段列表
	 * 分5种情况,顺序不能更改
	 * 1上班时间段正好落在startTime 和 endTime之外,  要去掉
	 * 2上班时间 包含了 startTime到endTime这一段,修改timePeroid
	 * 3上班时间段落在startTime 上,修改timePeroid,上班时间段分成2段
	 * 4上班时间段落在endTime 上,修改timePeroid,上班时间段分成2段
	 * 5上班时间段正好落在startTime 和 endTime之间,  不用处理
	 * 
	 * 请假不一样，请假不可能落在任务开始或结束时间点上
	 */
	@Override
	public List<TimePeroid> getRealShiftPeroidList(String userId,LocalDateTime start_time, LocalDateTime end_time) {
		List<TimePeroid> timePeroidList = this.getShiftTimePeroidList(userId,start_time, end_time);
		Iterator<TimePeroid> it = timePeroidList.iterator();
		//remove 会报错，只能用Iterator
		// for (TimePeroid timePeroid : timePeroidList) {
		while (it.hasNext()) {
			TimePeroid timePeroid = it.next();
			LocalDateTime starDate = timePeroid.getStartDate();
			LocalDateTime endDate = timePeroid.getEndDate();
			if (timePeroid != null) {
				if ((starDate.compareTo(start_time) <= 0 && endDate
						.compareTo(start_time) <= 0)
						|| (starDate.compareTo(end_time) >= 0 && endDate
								.compareTo(end_time) >= 0)) {
					it.remove();
				} else if (starDate.compareTo(start_time) < 0
						&& endDate.compareTo(end_time) > 0) {
					timePeroid.setStartDate(start_time);
					timePeroid.setEndDate(end_time);
				}else if (starDate.compareTo(start_time) < 0
						&& endDate.compareTo(start_time) > 0) {
					timePeroid.setStartDate(start_time);
				} else if (starDate.compareTo(end_time) < 0
						&& endDate.compareTo(end_time) > 0) {
					timePeroid.setEndDate(end_time);
				} 
			}
		}
		return timePeroidList;
	}
	
	/**
	 * 取得用户的日历设置（包含了上班班次）
	 * @author hjx
	 * @version 创建时间：2014-2-24  上午9:20:06
	 * @param userId
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	public List<CalendarSetting> getShiftPeroidList(String userId, LocalDateTime start_time,
			LocalDateTime end_time) {
		
		//根据用户ID取得日历ID，一个用户应该只有一个日历，不能有多个
		String calendarId=calendarAssignManager.getCalendarIdByUserId(userId);
		
		//根据日历取得班次时间段列表
		List<CalendarSetting>  calendarSettingList = calendarSettingManager.getByCalendarId(calendarId, start_time, end_time);
		return calendarSettingList;
	}
	
	
	/**
	 * List CalendarSetting 转List TimePeroid
	 * @author hjx
	 * @version 创建时间：2014-2-24  上午9:59:18
	 * @param userId
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	public List<TimePeroid> getShiftTimePeroidList(String userId,
			LocalDateTime start_time, LocalDateTime end_time) {
		// 取得用户的日历设置（包含了上班班次）
		List<CalendarSetting> list = this.getShiftPeroidList(userId,
				start_time, end_time);
		List<TimePeroid> timePeroidList = new ArrayList<TimePeroid>();

		for (CalendarSetting calendarSetting : list) {
			if (CalendarSetting.SETTING_DATETYPE_WORK.equals(calendarSetting.getDateType())) {//只统计上班时段，双休、节假日不计算在内
				String claDay = calendarSetting.getCalDay();
				List<CalendarShiftPeroid> cspList = calendarSetting.getCalendarShiftPeroidList();
				if (cspList != null && cspList.size() > 0) {
					for (CalendarShiftPeroid calendarShiftPeroid : cspList) {
						LocalDateTime starDate = TimeUtil.getDateTimeByTimeString(claDay
								+ " " + calendarShiftPeroid.getStartTime()
								+ ":00");
						LocalDateTime endDate = TimeUtil.getDateTimeByTimeString(claDay
								+ " " + calendarShiftPeroid.getEndTime()
								+ ":00");
						TimePeroid timePeroid = new TimePeroid(starDate,
								endDate, TimePeroid.PEROID_TYPE_SHIFT);
						timePeroidList.add(timePeroid);
					}
				}
			}
		}
		return timePeroidList;
	}
	
	/**
	 * 根据shiftId取CalendarShiftPeroid
	 * @param settingId
	 * @return
	 */
	@Override
	public List<CalendarShiftPeroid> getByShiftId(String shiftId){
		return baseMapper.getCalendarShiftPeroidList(shiftId);
	}
	
	@Override
	public void shiftPeroidAdd(String shiftId, String[] startTime, String[] endTime, String[] memo){
		
		if(startTime!=null && endTime!=null){
			baseMapper.delByMainId(shiftId);
			
			for(int idx=0;idx<startTime.length;idx++){
				CalendarShiftPeroid calendarShiftPeroid = new CalendarShiftPeroid();
				try {
					calendarShiftPeroid.setId(idGenerator.getSuid());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				calendarShiftPeroid.setShiftId(shiftId);
				calendarShiftPeroid.setStartTime(startTime[idx]);
				calendarShiftPeroid.setEndTime(endTime[idx]);
				calendarShiftPeroid.setMemo(memo[idx]);
				this.create(calendarShiftPeroid);
			}
		}
	}
}
