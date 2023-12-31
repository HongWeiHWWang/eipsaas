package com.hotent.Calendar.params;



import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import com.hotent.Calendar.model.Calendar;
import com.hotent.Calendar.model.CalendarShift;

/**
 * 系统日历信息vo
 * @company 广州宏天软件股份有限公司
 * @author zhangxw
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月5日
 */
@ApiModel(value="系统日历信息")
public class CalendarSettingGetVo {

	@ApiModelProperty(name="calendar",notes="系统日历对象")
	private Calendar calendar;
	
	@ApiModelProperty(name="shifts",notes="班次列表")
	private List<CalendarShift> shifts;
	
	public CalendarSettingGetVo(){}
	
	public CalendarSettingGetVo(Calendar calendar, List<CalendarShift> shifts){
		this.calendar = calendar;
		this.shifts = shifts;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	public List<CalendarShift> getShifts() {
		return shifts;
	}

	public void setShifts(List<CalendarShift> shifts) {
		this.shifts = shifts;
	}

	
	
}
