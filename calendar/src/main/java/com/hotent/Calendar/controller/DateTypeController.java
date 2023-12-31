package com.hotent.Calendar.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.Calendar.manager.CalendarDateTypeManager;
import com.hotent.Calendar.model.CalendarDateType;
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
 * 时间维度管理
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月26日
 */
@RestController
@RequestMapping("/calendar/dateType/v1/")
@Api(tags="时间维度管理")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class DateTypeController extends BaseController<CalendarDateTypeManager,CalendarDateType>{
	@Resource
	CalendarDateTypeManager calendarDateTypeManager;

	@RequestMapping(value="list", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "日历类型列表(分页条件查询)", httpMethod = "POST", notes = "日历类型列表(分页条件查询)")
	public PageList<CalendarDateType> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<CalendarDateType> queryFilter) throws Exception {
		return calendarDateTypeManager.query(queryFilter);
	}
	
	@RequestMapping(value="edit",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "编辑日历类型", httpMethod = "GET", notes = "编辑日历类型")
	public CalendarDateType edit(@ApiParam(name="id",value="编辑日历类型id", required = true) @RequestParam String id) throws Exception{
		if(StringUtil.isNotEmpty(id)){//编辑
			return calendarDateTypeManager.get(id);
		}else{//新增
			return new CalendarDateType();
		}
	}
	
	@RequestMapping(value="get",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取日历类型明细", httpMethod = "GET", notes = "获取日历类型明细")
	public CalendarDateType get(@ApiParam(name="id",value="日历类型id", required = true) @RequestParam String id) throws Exception{
		return calendarDateTypeManager.get(id);
	}
	
	@RequestMapping(value="save",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存日历类型信息", httpMethod = "POST", notes = "保存日历类型信息")
	public CommonResult<String> save(@ApiParam(name="map",value="日历类型信息", required = true) @RequestBody CalendarDateType calendarDateType) throws Exception{
		String id=calendarDateType.getId();
		String resultMsg = "";
		boolean isTrue = true;
		try {
			if(StringUtil.isEmpty(id)){
				calendarDateTypeManager.create(calendarDateType);
				resultMsg="添加日历类型成功";
			}else{
				calendarDateTypeManager.update(calendarDateType);
				resultMsg="更新日历类型成功";
			}
		} catch (Exception e) {
			isTrue = false;
			resultMsg = e.getMessage();
		}
		return new CommonResult<String>(isTrue, resultMsg);
	}
	
	
	@RequestMapping(value="remove",method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除日历类型记录(逻辑删除)", httpMethod = "DELETE", notes = "批量删除日历类型记录(逻辑删除)")
	public CommonResult<String> remove(@ApiParam(name="aryIds",value="日历分配记录ID，多个用“,”号分隔", required = true) @RequestParam String aryIds) throws Exception{
		String[] ids = null;
		if(!StringUtil.isEmpty(aryIds)){
			ids = aryIds.split(",");
		}
		calendarDateTypeManager.removeByIds(ids);
		return new CommonResult<>(true, "删除成功");
	}
	
}