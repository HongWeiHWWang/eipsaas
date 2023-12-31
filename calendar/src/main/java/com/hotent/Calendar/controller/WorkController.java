package com.hotent.Calendar.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.Calendar.manager.CalendarAssignManager;
import com.hotent.Calendar.manager.CalendarDateTypeManager;
import com.hotent.Calendar.manager.CalendarManager;
import com.hotent.Calendar.manager.CalendarSettingManager;
import com.hotent.Calendar.manager.CalendarShiftManager;
import com.hotent.Calendar.model.Calendar;
import com.hotent.Calendar.model.CalendarSettingEvent;
import com.hotent.Calendar.model.CalendarShift;
import com.hotent.Calendar.params.CalendarSettingGetVo;
import com.hotent.Calendar.params.CalendarSettingVo;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.MapUtil;
import com.hotent.base.util.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 系统日历
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月26日
 */
@RestController
@RequestMapping("/calendar/work/v1/")
@Api(tags="工作日历管理")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class WorkController extends BaseController<CalendarManager,Calendar> {
	@Resource
	CalendarManager calendarManager;
	@Resource
	CalendarAssignManager calendarAssignManager;
	@Resource
	CalendarSettingManager calendarSettingManager;
	@Resource
	CalendarShiftManager calendarShiftManager;
	@Resource
	CalendarDateTypeManager calendarDateTypeManager;
	
	@RequestMapping(value="list", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "系统日历列表(分页条件查询)", httpMethod = "POST", notes = "系统日历列表(分页条件查询)")
	public PageList<Calendar> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<Calendar> queryFilter) throws Exception {
		return calendarManager.query(queryFilter);
	}
	
	@RequestMapping(value="detail",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取工作日历细信息", httpMethod = "GET", notes = "获取工作日历详细信息")
	public CalendarSettingGetVo detail(@ApiParam(name="id",value="工作日历id", required = true) @RequestParam String id) throws Exception{
		CalendarSettingGetVo workGetVo = new CalendarSettingGetVo();
		if (StringUtil.isNotEmpty(id)) {
			Calendar calendar = calendarManager.get(id);
			workGetVo.setCalendar(calendar);
		}
		List<CalendarShift> shifts = calendarShiftManager.getAll();
		workGetVo.setShifts(shifts);
		return workGetVo;
	}
	
	@RequestMapping(value="calendarSetting",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "查询工作日历设置", httpMethod = "GET", notes = "查询工作日历设置")
	public CalendarSettingVo calendarSetting(@ApiParam(name="id",value="日历id", required = true) @RequestParam String id,@ApiParam(name="year",value="年份", required = true) @RequestParam Integer year) throws Exception{
		if(StringUtil.isEmpty(id)||BeanUtils.isEmpty(year)){
			return null;
		}
		List<CalendarSettingEvent> calendarSettingEvent = calendarManager.getCalendarSettingEvent(id, year);
		return new CalendarSettingVo(calendarSettingEvent, year, true);
	}
	
	
	@RequestMapping(value="get",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取系统日历信息", httpMethod = "GET", notes = "获取系统日历信息")
	public Calendar get(@ApiParam(name="id",value="系统日历id", required = true) @RequestParam String id) throws Exception{
		Calendar calendar = null;
		if (StringUtil.isNotEmpty(id)) {
			calendar = calendarManager.get(id);
		}
		return calendar;
	}
	
	
	@RequestMapping(value="save",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存系统日历信息", httpMethod = "POST", notes = "保存系统日历信息")
	public CommonResult<String> save(@ApiParam(name="map",value="系统日历信息", required = true) @RequestBody Map<String,Object> map) throws Exception{
		String json = MapUtil.getString(map, "calendar");
		calendarManager.saveCalendar(json);
		return new CommonResult<>(true, "日历设置成功");
	}
	
	@RequestMapping(value="remove",method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除系统日历(逻辑删除)", httpMethod = "DELETE", notes = "批量删除系统日历(逻辑删除)")
	public CommonResult<String> remove(@ApiParam(name="ids",value="系统日历ID，多个用“,”号分隔", required = true) @RequestParam String ids) throws Exception{
		String[] aryIds = null;
		if(!StringUtil.isEmpty(ids)){
			aryIds = ids.split(",");
		}
		calendarAssignManager.delByCalId(aryIds); // 系统日历
		calendarSettingManager.delByCalId(aryIds); // 具体的日历设置
		calendarManager.removeByIds(aryIds); // 日历
		return new CommonResult<>(true, "删除成功");
	}
	
	@RequestMapping(value="setDefault",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "设置默认时，只能有一个是默认的", httpMethod = "GET", notes = "设置默认时，只能有一个是默认的")
	public CommonResult<String> setDefault(@ApiParam(name="id",value="日历id", required = true) @RequestParam String id) throws Exception{
		calendarManager.setDefaultCal(id);
		return new CommonResult<String>(true, "设置成功");
	}

}
