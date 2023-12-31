package com.hotent.base.calendar;
import java.time.LocalDateTime;

/**
 * 工作日历服务
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月2日
 */
public interface ICalendarService{ 
	/**
	 * 根据用户和指定工时从现在开始获取任务的完成时间
	 * @param userId		用户ID
	 * @param time			工时(分钟)
	 * @return				完成时间
	 * @throws Exception
	 */
	LocalDateTime getEndTimeByUser(String userId, long time) throws Exception;
		
	/**
	 * 根据用户，指定工时，指定开始时间,计算任务实际完成时间
	 * @param userId		用户ID
	 * @param startTime		开始时间
	 * @param time			工时(分钟)
	 * @return				完成时间
	 * @throws Exception
	 */
	 LocalDateTime getEndTimeByUser(String userId, LocalDateTime startTime, long time) throws Exception;

	 /**
	  * 根据用户开始时间和结束时间，获取这段时间的有效工时
	  * @param userId		用户ID
	  * @param startTime	开始时间
	  * @param endTime		结束时间
	  * @return				工时(分钟)
	  */
	Long getWorkTimeByUser(String userId, LocalDateTime startTime, LocalDateTime endTime); 
}