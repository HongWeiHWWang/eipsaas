package com.hotent.Calendar.controller;

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
 * 班次时间段管理
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月26日
 */
@RestController
@RequestMapping("/calendar/shiftPeroid/v1/")
@Api(tags="班次时间段管理")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class ShiftPeroidController extends BaseController<CalendarShiftPeroidManager,CalendarShiftPeroid> {
	@Resource
	CalendarShiftManager calendarShiftManager;
	@Resource
	CalendarShiftPeroidManager calendarShiftPeroidManager;
	@Resource
	IdGenerator idGenerator;

	@RequestMapping(value="list", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "班次时间段列表(分页条件查询)", httpMethod = "POST", notes = "班次时间段列表(分页条件查询)")
	public PageList<CalendarShiftPeroid> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<CalendarShiftPeroid> queryFilter) throws Exception {
		return calendarShiftPeroidManager.query(queryFilter);
	}

	@RequestMapping(value="edit",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "编辑班次时间段", httpMethod = "GET", notes = "编辑班次时间段")
	public CalendarShiftPeroid edit(@ApiParam(name="id",value="编辑班次时间段id", required = true) @RequestParam String id) throws Exception{
		if(StringUtil.isNotEmpty(id)){//编辑
			return calendarShiftPeroidManager.get(id);
		}else{//新增
			return new CalendarShiftPeroid();
		}
	}
	
	@RequestMapping(value="get",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取班次详细信息", httpMethod = "GET", notes = "获取班次详细信息")
	public CalendarShift get(@ApiParam(name="id",value="日历分配id", required = true) @RequestParam String id) throws Exception{
		if (StringUtil.isNotEmpty(id)) {
			return calendarShiftManager.get(id);
		} 
		return null;
	}

	
	@RequestMapping(value="save",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存班次信息", httpMethod = "POST", notes = "保存班次信息")
	public CommonResult<String> save(@ApiParam(name="shift",value="班次信息", required = true) @RequestBody CalendarShift shift) throws Exception{
		String id = shift.getId();
		String resultMsg = "";
		if (StringUtil.isEmpty(id)) {
			shift.setId(idGenerator.getSuid());
			calendarShiftManager.create(shift);
			resultMsg = "添加成功";
		} else {
			calendarShiftManager.update(shift);
			resultMsg = "更新成功";
		}
		return new CommonResult<String>(true, resultMsg);
	}
	
	@RequestMapping(value="remove",method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除班次记录(逻辑删除)", httpMethod = "DELETE", notes = "批量删除班次记录(逻辑删除)")
	public CommonResult<String> remove(@ApiParam(name="aryIds",value="班次记录ID，多个用“,”号分隔", required = true) @RequestParam String aryIds) throws Exception{
		String[] ids = null;
		if(!StringUtil.isEmpty(aryIds)){
			ids = aryIds.split(",");
		}
		calendarShiftManager.removeByIds(ids);
		return new CommonResult<>(true, "删除成功");
	}
}
