package com.hotent.Calendar.manager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.hotent.Calendar.model.CalendarAssign;
import com.hotent.Calendar.model.CalendarSetting;
import com.hotent.Calendar.model.CalendarShiftPeroid;
import com.hotent.base.manager.BaseManager;

public interface CalendarAssignManager extends BaseManager<CalendarAssign>{

	/**
	 * 取日历设置。
	 * <pre>
	 * 	1.根据个人获取日历。
	 *  2.没有获取到则获取部门的日历。
	 *  3.部门也没有设置的情况，获取默认的日历。
	 *  4.没有获取到则返回为空。
	 * </pre>
	 * @param userId
	 * @return
	 */
	public String getCalendarIdByUserId(String userId);
	
	
	/**
	 * 根据日历列表获取相应的工作时间分段列表
	 * @param list
	 * @return
	 */
	public List<CalendarShiftPeroid> getBycalList(List<CalendarSetting> list);
	
	
	/**
	 * 返回系统默认工作日历的工作时段
	 * @param startDate 开始时间
	 * @return
	 */
	public List<CalendarShiftPeroid> getTaskTimeByDefault(LocalDateTime startDate);
	
	
	/**
	 * 取被分配的类型,用户或组织
	 * @return
	 */
	public List<Map<String,String>> getAssignUserType();
	
	
	/**
	 * 根据日历id删除记录
	 * @param calId
	 */
	public void delByCalId(String[] calIds);
	
	
	/**
	 * 根据用户ID得到唯一条分配信息
	 * @param assignId
	 * @return
	 */
	public CalendarAssign getbyAssignId(String assignId);
	
	/**
	 * 保存日历分配设置
	 * @param assign 日历分配JSON
	 * @return 若部分用户已经分配了日历，则提示不能重复分配，这里返回这些用户的名称
	 */
	public List<String> saveAssign(String assign) throws Exception;
}
