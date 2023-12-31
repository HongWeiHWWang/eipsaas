package com.hotent.Calendar.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.Calendar.model.CalendarShiftPeroid;


public interface CalendarShiftPeroidDao  extends BaseMapper<CalendarShiftPeroid> {

	
	/**
	 * 根据外键获取子表明细列表
	 * @param settingid
	 * @return
	 */
	public List<CalendarShiftPeroid> getCalendarShiftPeroidList(@Param("shiftId") String shiftId);
	
	/**
	 * 根据外键删除子表记录
	 * @param settingid
	 * @return
	 */
	public void delByMainId(@Param("shiftId") String settingid);
}


