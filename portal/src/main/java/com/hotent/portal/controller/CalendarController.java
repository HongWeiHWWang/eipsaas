package com.hotent.portal.controller;

import java.time.LocalDateTime;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.calendar.ICalendarService;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.time.DateFormatUtil;
import com.hotent.portal.params.CalendarVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 日历相关操作
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年8月2日
 */
@RestController
@RequestMapping("/portal/calendar/v1")
@Api(tags="工作日历设置")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class CalendarController {
	@Resource 
	ICalendarService iCalendarService;

	@RequestMapping(value="getWorkTimeByUser", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取有效工时", httpMethod = "POST", notes = "根据用户开始时间和结束时间，获取这段时间的有效工时")
	public String getWorkTimeByUser(@ApiParam(name="calendarVo",value="日历操作参数")@RequestBody CalendarVo calendarVo) throws Exception {
		Long workTime = iCalendarService.getWorkTimeByUser(calendarVo.getUserId(), DateFormatUtil.parse(calendarVo.getStartTime()), DateFormatUtil.parse(calendarVo.getEndTime()));
		return String.valueOf(workTime);
	}
	
	
	@RequestMapping(value="getEndTimeByUser", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取有效工时", httpMethod = "POST", notes = "根据用户开始时间和结束时间，获取这段时间的有效工时")
	public String getEndTimeByUser(@ApiParam(name="calendarVo",value="日历操作参数")@RequestBody CalendarVo calendarVo) throws Exception {
			String sTime = calendarVo.getStartTime();
			LocalDateTime endTime = StringUtil.isEmpty(sTime)?iCalendarService.getEndTimeByUser(calendarVo.getUserId(), Long.valueOf(calendarVo.getTime())):iCalendarService.getEndTimeByUser(calendarVo.getUserId(), DateFormatUtil.parse(sTime), Long.valueOf(calendarVo.getTime()));
			return DateFormatUtil.formaDatetTime(endTime);
	}
}
