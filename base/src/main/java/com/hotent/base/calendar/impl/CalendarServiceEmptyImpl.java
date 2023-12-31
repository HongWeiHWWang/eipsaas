package com.hotent.base.calendar.impl;

import java.time.LocalDateTime;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.hotent.base.calendar.ICalendarService;

@Service
public class CalendarServiceEmptyImpl implements ICalendarService{
	private static final Log logger= LogFactory.getLog(CalendarServiceEmptyImpl.class);
	private final String WARN_MESSAGE = "[Calendar]: There is no implements of ICalendarService, so we can not calculate the working day, we return the calendar day instead.";
	
	@Override
	public LocalDateTime getEndTimeByUser(String userId, long time) throws Exception {
		logger.warn(WARN_MESSAGE);
		LocalDateTime now = LocalDateTime.now();
		return now.plusMinutes(time);
	}
	@Override
	public LocalDateTime getEndTimeByUser(String userId, LocalDateTime startTime, long time) throws Exception {
		logger.warn(WARN_MESSAGE);
		return startTime.plusMinutes(time);
	}
	@Override
	public Long getWorkTimeByUser(String userId, LocalDateTime startTime, LocalDateTime endTime) {
		logger.warn(WARN_MESSAGE);
		int endMinutes = endTime.getMinute();
		int startMinutes = startTime.getMinute();
		int result = endMinutes - startMinutes;
		return (long)result;
	}
}
