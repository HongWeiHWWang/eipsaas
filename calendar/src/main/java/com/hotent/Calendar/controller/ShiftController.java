package com.hotent.Calendar.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.Calendar.manager.CalendarShiftManager;
import com.hotent.Calendar.manager.CalendarShiftPeroidManager;
import com.hotent.Calendar.model.CalendarShift;
import com.hotent.Calendar.model.CalendarShiftPeroid;
import com.hotent.Calendar.params.CalendarShiftVo;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.id.IdGenerator;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 班次管理
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月26日
 */
@RestController
@RequestMapping("/calendar/shift/v1/")
@Api(tags="班次管理")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class ShiftController extends BaseController<CalendarShiftManager,CalendarShift> {
	@Resource
	private CalendarShiftManager calendarShiftManager;
	@Resource
	private  CalendarShiftPeroidManager calendarShiftPeroidManager;
	@Resource
	IdGenerator idGenerator;

	@RequestMapping(value="list", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "班次管理列表(分页条件查询)", httpMethod = "POST", notes = "班次管理列表(分页条件查询)")
	public PageList<CalendarShift> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<CalendarShift> queryFilter) throws Exception {
		return calendarShiftManager.query(queryFilter);
	}
	
	@RequestMapping(value="get",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取班次明细", httpMethod = "GET", notes = "获取班次明细")
	public CalendarShiftVo get(@ApiParam(name="id",value="班次id", required = true) @RequestParam String id) throws Exception{
		List<CalendarShiftPeroid> shiftPeroidlist = new ArrayList<CalendarShiftPeroid>();
		if (StringUtil.isNotEmpty(id)) {
			CalendarShift shift = calendarShiftManager.get(id);
			shiftPeroidlist = calendarShiftPeroidManager.getByShiftId(id);
			return new CalendarShiftVo(shift, shiftPeroidlist);
		}
		return null;
	}

	@RequestMapping(value="save",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存班次信息", httpMethod = "POST", notes = "保存班次信息")
	public CommonResult<String> save(@ApiParam(name="calendarShiftVo",value="班次信息", required = true) @RequestBody CalendarShiftVo calendarShiftVo) throws Exception{
		String msg = "";
		CalendarShift calendarshift = calendarShiftVo.getCalendarShift();
		List<CalendarShiftPeroid> peroids = calendarShiftVo.getShiftPeroidlist();
		calendarshift.setCalendarShiftPeroidList(peroids);
		String shiftId = calendarshift.getId();
		if (StringUtil.isEmpty(shiftId)) {
			shiftId = idGenerator.getSuid();
			calendarshift.setId(shiftId);
			calendarShiftManager.create(calendarshift);
			msg = "保存成功";
		} else {
			shiftId = calendarshift.getId();
			calendarShiftManager.update(calendarshift);
			msg = "更新成功";
		}
		return new CommonResult<String>(true, msg);
	}
	
	@RequestMapping(value="remove",method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除班次记录(逻辑删除)", httpMethod = "DELETE", notes = "批量删除班次记录(逻辑删除)")
	public CommonResult<String> remove(@ApiParam(name="ids",value="班次记录ID，多个用“,”号分隔", required = true) @RequestParam String ids) throws Exception{
		String[] aryIds = StringUtil.getStringAryByStr(ids);
		calendarShiftManager.removeByIds(aryIds);
		return new CommonResult<>(true, "删除成功");
	}
	
	@RequestMapping(value="setDefault",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "设置默认时，只能有一个是默认的", httpMethod = "GET", notes = "设置默认时，只能有一个是默认的")
	public CommonResult<String> setDefault(@ApiParam(name="id",value="班次id", required = true) @RequestParam String id) throws Exception{
		String msg = "设置成功";
		calendarShiftManager.setDefaultShift(id);
		return new CommonResult<String>(true, msg);
	}
	
}
