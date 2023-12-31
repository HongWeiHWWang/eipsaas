package com.hotent.Calendar.calc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.SortedMap;

import com.hotent.Calendar.model.TimePeroid;

public interface ICalendarCalc {
	

	/**
	 * 类型 ，加班  、请假
	 * @author hjx
	 * @version 创建时间：2014-2-19  下午3:32:01
	 * @return
	 */
	 public String getType();
	 
	 /**
	  * 得到实际 精确的时间段列表
	  * 列表的所有时间都在 startTime 和endTime之间
	  * @author hjx
	  * @version 创建时间：2014-2-24  上午10:58:05
	  * @param userId
	  * @param startTime
	  * @param endTime
	  * @return
	  */
	 public List<TimePeroid>  getRealTimePeroidList(String userId, LocalDateTime startTime, LocalDateTime endTime);
	 
	/**
	 * 上班列表覆盖上班列表
	 * 请假列表覆盖上班列表
	 * 加班列表覆盖上班列表
	 * list 覆盖map
	 * Collections.sort(lists);  
	 * @author hjx
	 * @version 创建时间：2014-2-21  上午10:09:46
	 * @param list
	 * @return
	 */
	 public SortedMap<LocalDateTime,TimePeroid> overrideCalendarShiftPeroidMap(SortedMap<LocalDateTime,TimePeroid> calendarShiftPeroidMap ,List<TimePeroid> overTimePeroidlist);
}
