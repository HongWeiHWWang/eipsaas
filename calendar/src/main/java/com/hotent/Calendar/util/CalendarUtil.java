package com.hotent.Calendar.util;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import com.hotent.Calendar.model.TimePeroid;
import com.hotent.base.util.time.TimeUtil;
/**
 * 日历工具类
 * @author hjx
 *
 */
public class CalendarUtil {
	/**
	 * 默认结束时间向后推30天
	 * @author hjx
	 * @version 创建时间：2014-2-24  下午2:44:42
	 * @param startTime
	 * @return
	 */
	public static LocalDateTime getDefaultEndDate(LocalDateTime startTime){
		return TimeUtil.getNextDays(startTime,30);
	}
	
	/**
	 *  list转SortedMap
	 *  SortedMap保证 key不重复
	 *  SortedMap保证了时间段的顺序
	 * @author hjx
	 * @version 创建时间：2014-2-24  上午11:54:14
	 * @param list
	 * @return
	 */
	public static SortedMap<LocalDateTime, TimePeroid> getTimePeroidMap(List<TimePeroid> list) {
		if(list==null||list.size()<=0)return null;
		SortedMap<LocalDateTime, TimePeroid> timePeroidMap=new TreeMap<LocalDateTime, TimePeroid>();
	    for(TimePeroid timePeroid :list){
	    	if(timePeroid!=null)
	    	timePeroidMap.put(timePeroid.getStartDate(), timePeroid);
	    }
		return timePeroidMap;
	}
	
	/**
	 * 根据sortedMap 合计所有时间段的时间总和
	 * @author hjx
	 * @version 创建时间：2014-2-24  下午2:22:51
	 * @param sortedMap
	 * @return
	 */
	public static long getCountTimePeroid(SortedMap<LocalDateTime, TimePeroid> sortedMap) {
		if(sortedMap==null|| sortedMap.size()==0)return 0;
		long countTime = 0;
		// map遍历
		Iterator<Entry<LocalDateTime, TimePeroid>> iter = sortedMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<LocalDateTime, TimePeroid> entry = (Map.Entry<LocalDateTime, TimePeroid>) iter.next();
			Object val = entry.getValue();
			long tmpTime = ((TimePeroid) val).getTime();
			countTime += tmpTime;
		}
		return countTime;
	}
	
	/**
	 * 根据时长 和上班时段列表sortedMap，计算任务完成时间
	 * @author hjx
	 * @version 创建时间：2014-2-24  下午2:22:51
	 * @param sortedMap
	 * @return
	 * @throws Exception 
	 */
	public static LocalDateTime getEndTime(SortedMap<LocalDateTime, TimePeroid> sortedMap,long time) throws Exception {
		long millisecond=time*60*1000;
		long countTime = 0;
		TimePeroid timePeroid=new TimePeroid();
		// map遍历
		Iterator<Entry<LocalDateTime, TimePeroid>> iter = sortedMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<LocalDateTime, TimePeroid> entry = (Map.Entry<LocalDateTime, TimePeroid>) iter.next();
			Object val = entry.getValue();
			timePeroid=(TimePeroid) val;
			long tmpTime = timePeroid.getTime();
			countTime += tmpTime;
			if(countTime>=millisecond)
				break;
		}
		//所有的时间段都加完了仍不够，则返回null，系统要重新计算endtime，再后推一个月
		if(countTime<millisecond)return null;
		
		//需要倒推的时间
		long moreTime= countTime-millisecond;
		//当前时间段倒推
		long millisecond2= TimeUtil.getTimeMillis(timePeroid.getEndDate())-moreTime;
		//转化成日期
		return TimeUtil.getLocalDateTimeByMills(millisecond2);
	}
	
	/**
	 * 获取指定年月的天数
	 * @param year 指定年
	 * @param month 指定月
	 * @return
	 */
	public static Integer getDaysOfMonth(Integer year, Integer month){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return cal.get(Calendar.DAY_OF_MONTH);
	}
}
