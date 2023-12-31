package com.hotent.Calendar.params;



import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import com.hotent.Calendar.model.Calendar;
import com.hotent.Calendar.model.CalendarAssign;

/**
 * 日历分配信息vo
 * @company 广州宏天软件股份有限公司
 * @author zhangxw
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月5日
 */
@ApiModel(value="日历分配信息")
public class CalendarAssignGetVo {

	@ApiModelProperty(name="calendarAssign",notes="日历分配对象")
	private CalendarAssign calendarAssign;
	
	@ApiModelProperty(name="calendarList",notes="系统日历列表")
	private List<Calendar> calendarList;
	
	public CalendarAssignGetVo(){}
	
	public CalendarAssignGetVo(CalendarAssign calendarAssign, List<Calendar> calendarList){
		this.calendarAssign = calendarAssign;
		this.calendarList = calendarList;
	}

	public CalendarAssign getCalendarAssign() {
		return calendarAssign;
	}

	public void setCalendarAssign(CalendarAssign calendarAssign) {
		this.calendarAssign = calendarAssign;
	}

	public List<Calendar> getCalendarList() {
		return calendarList;
	}

	public void setCalendarList(List<Calendar> calendarList) {
		this.calendarList = calendarList;
	}
	
	
}
