package com.hotent.Calendar.manager.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.Calendar.dao.CalendarDao;
import com.hotent.Calendar.dao.CalendarSettingDao;
import com.hotent.Calendar.manager.CalendarManager;
import com.hotent.Calendar.manager.CalendarSettingManager;
import com.hotent.Calendar.model.Calendar;
import com.hotent.Calendar.model.CalendarSetting;
import com.hotent.Calendar.model.CalendarSettingEvent;
import com.hotent.base.id.IdGenerator;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;

@Service("CalendarManager")
public class CalendarManagerImpl extends BaseManagerImpl<CalendarDao, Calendar> implements CalendarManager{
	@Resource
	IdGenerator idGenerator;
	@Resource
	CalendarSettingDao calendarSettingDao;
	@Resource
	CalendarSettingManager calendarSettingManager;
	
	/**
	 * 取得默认的日历。
	 * @return
	 */
	public Calendar getDefaultCalendar(){
		return baseMapper.getDefaultCalendar();
	}
	
	/**
	 * 设置默认日历
	 * @param id
	 */
	public void setNotDefaultCal(Long id){
		//设置非默认
		baseMapper.setNotDefaultCal();
		Calendar syscal = this.get(id.toString());
		syscal.setIsDefault('1');
		update(syscal);
	}
	
	/**
	 * 通过日历ID和年份获取日历设置事件
	 * @param calendarId
	 * @param year
	 * @return
	 */
	public List<CalendarSettingEvent> getCalendarSettingEvent(String calendarId, int year){
		List<CalendarSettingEvent> events = new ArrayList<CalendarSettingEvent>();
		List<CalendarSetting> settings = calendarSettingDao.getCalByIdYear(calendarId, year);
		if(BeanUtils.isEmpty(settings)) return events;
		Set<List<CalendarSetting>> set = new HashSet<List<CalendarSetting>>();
		CalendarSetting firstSetting = settings.get(0);
		hanlderSetting(set, settings, firstSetting.getMonths(), firstSetting.getDays(), firstSetting.getShiftId());
		convert2settingEvent(events, set);
		return events;
	}
	
	//将日历设置按照月份为单位，连续的日历日归纳到一个列表中
	private void hanlderSetting(Set<List<CalendarSetting>> set, List<CalendarSetting> settings, Long preMonth, Long preDay, String preShiftId){
		List<CalendarSetting> partitions = new ArrayList<CalendarSetting>();
		set.add(partitions);
		Iterator<CalendarSetting> iter = settings.iterator();
		while(iter.hasNext()){
			CalendarSetting calendarSetting = iter.next();
			Long months = calendarSetting.getMonths();
			Long days = calendarSetting.getDays();
			String shiftId = calendarSetting.getShiftId();
			Boolean shiftCompare = false;
			if(StringUtil.isEmpty(preShiftId)){
				if(StringUtil.isEmpty(shiftId)){
					shiftCompare = true;
				}
			}
			else{
				shiftCompare = preShiftId.equals(shiftId);
			}
			if(preMonth.equals(months) && (preDay.equals(days)||(days.intValue() - preDay.intValue() == 1)) && shiftCompare){
				preDay = days;
				partitions.add(calendarSetting);
				iter.remove();
			}
			else{
				hanlderSetting(set, settings, months, days, shiftId);
				break;
			}
		}
	}
	
	//转化日历设置为日历设置事件
	private void convert2settingEvent(List<CalendarSettingEvent> events, Set<List<CalendarSetting>> set){
		Iterator<List<CalendarSetting>> iterator = set.iterator();
		while(iterator.hasNext()){
			List<CalendarSetting> list = iterator.next();
			CalendarSetting startSetting = list.get(0);
			CalendarSetting endSetting = list.get(list.size()-1);
			Long years = startSetting.getYears();
			Long months = startSetting.getMonths();
			CalendarSettingEvent event = new CalendarSettingEvent(years, years, months, months, 
																  startSetting.getDays(), endSetting.getDays(), 
																  endSetting.getShiftId());
			events.add(event);
		}
	}
	
	/**
	 * 保存日历设置情况
	 */
	@Override
	public String saveCalendar(String json) throws Exception {
		ObjectNode jsonObject = (ObjectNode) JsonUtil.toJsonNode(json);
		String baseStr = jsonObject.get("base").toString();
		String settingEventsStr = jsonObject.get("settingEvents").toString();
		if(StringUtil.isEmpty(baseStr)||StringUtil.isEmpty(settingEventsStr)) throw new RuntimeException("工作日历的设置不能为空");
		Calendar calendar = JsonUtil.toBean(baseStr, Calendar.class);
		List<CalendarSettingEvent> calendarSettingEvents = new ArrayList<CalendarSettingEvent>();
		ArrayNode array = (ArrayNode) JsonUtil.toJsonNode(settingEventsStr);
		for (JsonNode jsonNode : array) {
			calendarSettingEvents.add(JsonUtil.toBean(jsonNode, CalendarSettingEvent.class));
		}
		String calendarId = calendar.getId();
		if (StringUtil.isEmpty(calendarId)) {
			calendarId = idGenerator.getSuid();
			Calendar defaultCal = baseMapper.getDefaultCalendar();
			calendar.setId(calendarId);
			if (defaultCal == null) {//如果不存在默认日历
				calendar.setIsDefault('1');//设置默认日历
			} else {
				calendar.setIsDefault('0');
			}
			create(calendar);
		} else {
			this.update(calendar);
			calendarSettingDao.delByCalendarId(calendarId);
		}
		if(BeanUtils.isNotEmpty(calendarSettingEvents)){
			handlerCalendarSetting(calendarId, calendarSettingEvents);
		}
		return calendarId;
	}
	
	/**
	 * 处理工作日历设置
	 * <pre>
	 * 工作日历设置的Json格式如下：
	 * [
	 * ]
	 * </pre>
	 * @param calendarSettingEvents
	 * @throws Exception
	 */
	private void handlerCalendarSetting(String calendarId, List<CalendarSettingEvent> calendarSettingEvents) throws Exception{
		for(CalendarSettingEvent event : calendarSettingEvents){
			handlerCalendarSettingEvent(calendarId, event);
		}
	}
	
	//递归拆分日历事件
	private void handlerCalendarSettingEvent(String calendarId, CalendarSettingEvent event) throws Exception{
		if(event.shouldPartition()){
			List<CalendarSettingEvent> partitions = event.partition();
			//递归拆分
			for(CalendarSettingEvent partition : partitions){
				handlerCalendarSettingEvent(calendarId, partition);
			}
		}
		else{
			addCalendarSetting(calendarId, event);
		}
	}
	
	//保存日历设置
	private void addCalendarSetting(String calendarId, CalendarSettingEvent event) throws Exception{
		Integer year = event.getStartYear();
		Integer month = event.getStartMonth();
		String shiftId = event.getShiftId();
		int startDay = event.getStartDay();
		int dayMinus = event.getEndDay() - startDay;
		for(int i = 0;i <= dayMinus;i++){
			int day = startDay + i;
			CalendarSetting setting=new CalendarSetting();
			setting.setCalendarId(calendarId);
			setting.setYears((long) year);
			setting.setMonths((long) month);
			setting.setDays((long) day);
			setting.setCalDay(formatDateString(year,month,day));
			setting.setDateType(CalendarSetting.SETTING_DATETYPE_HOLIDAY);
			if(StringUtil.isNotEmpty(shiftId)){
				setting.setDateType(CalendarSetting.SETTING_DATETYPE_WORK);
				setting.setShiftId(shiftId);
			}
			calendarSettingManager.create(setting);
		}
	}
	
	//格式化字符串类型的日期
	private String formatDateString(int year,int month,int day){
		StringBuffer date = new StringBuffer();
		date.append(year);
		date.append("-");
		if(month<10){
			date.append("0");
			date.append(month);
		}else{
			date.append(month);
		}
		date.append("-");
		if(day<10){
			date.append("0");
			date.append(day);
		}else{
			date.append(day);
		}
		return date.toString();
	}


	@Override
	public void setDefaultCal(String id) {
		//设置非默认
		baseMapper.setNotDefaultCal();
		Calendar syscal =this.get(id);
		syscal.setIsDefault('1');
		update(syscal);
	}
}
