package com.hotent.Calendar.params;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hotent.Calendar.model.CalendarShift;
import com.hotent.Calendar.model.CalendarShiftPeroid;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 班次管理信息vo
 * @company 广州宏天软件股份有限公司
 * @author zhangxw
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月5日
 */
@ApiModel(value="日历分配信息")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CalendarShiftVo {

	@ApiModelProperty(name="calendarShift",notes="班次对象")
	private CalendarShift calendarShift;
	
	@ApiModelProperty(name="shiftPeroidlist",notes="班次时间列表")
	private List<CalendarShiftPeroid> shiftPeroidlist;
	
	public CalendarShiftVo(){}
	
	public CalendarShiftVo(CalendarShift calendarShift, List<CalendarShiftPeroid> shiftPeroidlist){
		this.calendarShift = calendarShift;
		this.shiftPeroidlist = shiftPeroidlist;
	}

	public CalendarShift getCalendarShift() {
		return calendarShift;
	}

	public void setCalendarShift(CalendarShift calendarShift) {
		this.calendarShift = calendarShift;
	}

	public List<CalendarShiftPeroid> getShiftPeroidlist() {
		return shiftPeroidlist;
	}

	public void setShiftPeroidlist(List<CalendarShiftPeroid> shiftPeroidlist) {
		this.shiftPeroidlist = shiftPeroidlist;
	}
	
}
