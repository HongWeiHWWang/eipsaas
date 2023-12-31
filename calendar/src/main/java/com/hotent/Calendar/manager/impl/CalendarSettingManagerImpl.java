package com.hotent.Calendar.manager.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.Calendar.dao.CalendarDateTypeDao;
import com.hotent.Calendar.dao.CalendarSettingDao;
import com.hotent.Calendar.dao.CalendarShiftDao;
import com.hotent.Calendar.dao.CalendarShiftPeroidDao;
import com.hotent.Calendar.manager.CalendarSettingManager;
import com.hotent.Calendar.model.CalendarDateType;
import com.hotent.Calendar.model.CalendarSetting;
import com.hotent.Calendar.model.CalendarShift;
import com.hotent.Calendar.model.CalendarShiftPeroid;
import com.hotent.base.id.IdGenerator;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.time.DateFormatUtil;
import com.hotent.base.util.time.TimeUtil;


@Service("calendarSettingManager")
public class CalendarSettingManagerImpl extends BaseManagerImpl<CalendarSettingDao, CalendarSetting> implements CalendarSettingManager{
	@Resource
	CalendarShiftPeroidDao calendarShiftPeroidDao;
	@Resource
	CalendarShiftDao calendarShiftDao;
	@Resource
	CalendarDateTypeDao calendarDateTypeDao;
	@Resource
	protected IdGenerator idGenerator;
	/**
	 * 根据日历查询时间。
	 * 将时间进行分段。
	 * 开始时间1 结束时间1
	 * 开始时间2 结束时间2
	 * @param calendarId
	 * @return
	 */
	@Deprecated
	public List<CalendarShiftPeroid> getByCalendarId(String calendarId,LocalDateTime startTime){
		List<CalendarShiftPeroid>  rtnList=new ArrayList<CalendarShiftPeroid>();
		List<CalendarShiftPeroid>  tmpList=new ArrayList<CalendarShiftPeroid>();
		List<CalendarSetting> list=baseMapper.getByCalendarId(calendarId,startTime);
		for(CalendarSetting calendarSetting:list){
			String calDay=calendarSetting.getCalDay();
			List<CalendarShiftPeroid> workTimeList=calendarSetting.getCalendarShiftPeroidList();
			for(CalendarShiftPeroid calendarShiftPeroid:workTimeList){
				calendarShiftPeroid.setCalDay(calDay);
				tmpList.add((CalendarShiftPeroid)calendarShiftPeroid.clone());
			}
		}
		int len=tmpList.size();
		for(int i=0;i<len;i++){
			CalendarShiftPeroid curTime=tmpList.get(i);
			if(i<len-1){
				int j=i+1;
				CalendarShiftPeroid nextTime=tmpList.get(j);
				if(curTime.getEndDateTime().compareTo(nextTime.getStartDateTime())>0){
					curTime.setEndDateTime(nextTime.getEndDateTime());
					rtnList.add(curTime);
					i++;
				}
				else{
					rtnList.add(curTime);
				}
			}
			else{
				rtnList.add(curTime);
			}
		}
		return rtnList;
	}
	
	/**
	 * 获取工作时间。
	 * set CalendarShiftPeroidList
	 * 以日历ID，开始时间和结束时间为备件获取所有符合备件的工作时间的列表。
	 * 如果用户没有找到日历，则生成一个临时的日历设置
	 * @author hjx
	 * @param calendarId 日历ID
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return 工作时间的列表。
	 */
	public List<CalendarSetting> getByCalendarId(String calendarId,LocalDateTime startTime,LocalDateTime endTime){
		List<CalendarShiftPeroid>  tmpList=new ArrayList<CalendarShiftPeroid>();
		List<CalendarSetting> list =new ArrayList<CalendarSetting>();
		
		//如果用户没有找到日历，则生成一个临时的日历设置
		if("0".equals(calendarId)){
			list=this.generateCalendarByDateType( startTime, endTime);
		}else{
			String startDate = startTime!=null?TimeUtil.getDateString(startTime):null;//只要年月日，有可能上班时间段落在开始日期上，
			String endDate = endTime!=null?TimeUtil.getDateString(endTime):null;
			list= baseMapper.getSegmentByCalId(calendarId, startDate, endDate);
			//如果用户某段时间没有设置过日历，还是生成临时日历
			if(list==null||list.size()<=0)list=this.generateCalendarByDateType( startTime, endTime);
		}
		
		for(CalendarSetting calendarSetting:list){
			String shiftId=calendarSetting.getShiftId();
			tmpList=calendarShiftPeroidDao.getCalendarShiftPeroidList(shiftId);
			calendarSetting.setCalendarShiftPeroidList(tmpList);
		}
		return list;
	}
	
	/**
	 * 根据日历id，year，month 得到日历
	 * @param id
	 * @param year
	 * @param month
	 * @return
	 */
	public List<CalendarSetting> getCalByIdYearMon(String id, int year, int month){
		return baseMapper.getCalByIdYearMon(id, year, month);
	}
	
	@Override
	public List<CalendarSetting> getCalByIdYear(String id, int year) {
		return baseMapper.getCalByIdYear(id, year);
	}
	
	/**
	 * 根据 日历id，year，month 删除日历
	 * @param calid
	 * @param year
	 * @param month
	 */
	public void delByCalidYearMon(String calid, Short year, Short month){
		baseMapper.delByCalidYearMon(calid, year, month);
	}
	
	/**
	 * 根据日历id删除记录
	 * @param calId
	 */
	public void delByCalId(String[] calIds){
		for (String calId : calIds) {
			baseMapper.delByCalId(calId);
		}
	}
	
	/**
	 * 生成一个临时日历
	 * @author hjx
	 * @version 创建时间：2014-2-26  下午1:57:31
	 * @return
	 */
	private List<CalendarSetting> generateTempCalendar(LocalDateTime startTime,LocalDateTime endTime) {
		List<CalendarSetting> list = new ArrayList<CalendarSetting>();

		// 获取cal_shit表的默认班次的id，默认班次数据sql脚本insert到表里
		CalendarShift calendarShift = calendarShiftDao.getUniqueDefaultShift();
		if(calendarShift==null){
			throw new RuntimeException("请设置默认班次");
		}
		String shiftId = calendarShift.getId();
        
		//循环次数，即天数
		ZoneId zone = ZoneId.systemDefault();
		Instant sinstant = startTime.atZone(zone).toInstant();
		Instant einstant = endTime.atZone(zone).toInstant();
		double times=(einstant.toEpochMilli()-sinstant.toEpochMilli()) / (double)(1000 * 60 * 60 * 24);
		int n = (int) Math.ceil (times);
		for (int i = 0; i <= n; i++) {
			ZoneId zoneId = ZoneId.systemDefault();
	        ZonedDateTime zdt = startTime.atZone(zoneId);
	        Date date = Date.from(zdt.toInstant());
			Calendar c = Calendar.getInstance();
			c.setTime(date);          // 当天
			c.add(Calendar.DAY_OF_YEAR, i); // 下一天
			// 得到格式化的日期
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String calDay = sdf.format(c.getTime());

			int years = c.get(Calendar.YEAR);         // 年
			int months = c.get(Calendar.MONTH) + 1;   // 月
			int days = c.get(Calendar.DATE);          // 日
			int weeks = c.get(Calendar.DAY_OF_WEEK) - 1;// 星期
			weeks=(weeks==0?7:weeks);//1代表星期一 2代表星期二 7代表星期日
			CalendarSetting calendarSetting = new CalendarSetting();
			calendarSetting.setId(idGenerator.getSuid());
			calendarSetting.setCalendarId(idGenerator.getSuid());
			calendarSetting.setYears(Long.valueOf(years));
			calendarSetting.setMonths(Long.valueOf(months));
			calendarSetting.setDays(Long.valueOf(days));
			calendarSetting.setShiftId(shiftId);
			calendarSetting.setCalDay(calDay);
			calendarSetting.setDateType(CalendarSetting.SETTING_DATETYPE_WORK);
			list.add(calendarSetting);
		}
		return list;
	}
   
	/**
	 * 根据cal_date_type设置日历的公休日、节假日
	 */
	@Override
	public  List<CalendarSetting>  generateCalendarByDateType(LocalDateTime startTime,LocalDateTime endTime){
		List<CalendarSetting> list=this.generateTempCalendar( startTime, endTime);
		//设置双休日，双休日不一定在周六、周日，而要根据cal_date_type表的公休日来设置
		//得到周日期范围的公休日
		List<CalendarDateType> phList=calendarDateTypeDao.getPhByWeekList();
		//得到年日期范围的法定假日（如春节 中秋节 寒暑假）
		List<CalendarDateType> lhList=calendarDateTypeDao.getLhByYearList();
		//得到年日期范围的公司假日
		List<CalendarDateType> chList=calendarDateTypeDao.getChByYearList();

		for(CalendarSetting calendarSetting:list){
			LocalDateTime date = null;
			try {
				date = DateFormatUtil.parse(calendarSetting.getCalDay()+" 00:00:00");
			} catch (ParseException e) {
				throw new RuntimeException("", e);
			}

			//如果是年日期范围的法定假日，setting改成休假
			for(CalendarDateType calDateType:lhList){
				if(date.compareTo(calDateType.getYearBegin())<=0&&date.compareTo(calDateType.getYearEnd())>=0)
					calendarSetting.setDateType(CalendarSetting.SETTING_DATETYPE_HOLIDAY);
			}

			//如果是年日期范围的法定假日，setting改成休假
			for(CalendarDateType calDateType:chList){
				if(date.compareTo(calDateType.getYearBegin())<=0&&date.compareTo(calDateType.getYearEnd())>=0)
					calendarSetting.setDateType(CalendarSetting.SETTING_DATETYPE_HOLIDAY);
			}
		}
		return list;
	}
}