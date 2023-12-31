package com.hotent.bpmModel.controller;


import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bpm.persistence.manager.BpmTaskReminderManager;
import com.hotent.bpm.persistence.model.BpmTaskReminder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;



/**
 * 流程催办 控制器类
 * @company 广州宏天软件有限公司
 * @author wanghb
 * @email wanghb@jee-soft.cn
 * @date 2018年6月26日
 */
@RestController
@RequestMapping("/flow/bpmTaskReminder/v1/")
@Api(tags="流程催办")
@ApiGroup(group= {ApiGroupConsts.GROUP_BPM})
public class BpmTaskReminderController extends BaseController<BpmTaskReminderManager, BpmTaskReminder>{
	/**
	 * 任务催办列表(分页条件查询)数据
	 * @param request
	 * @param reponse
	 * @return
	 * @throws Exception 
	 * PageJson
	 * @exception 
	 */
	@RequestMapping(value="listJson", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "任务催办列表(分页条件查询)数据", httpMethod = "POST", notes = "任务催办列表(分页条件查询)数据")
	public PageList<BpmTaskReminder> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<BpmTaskReminder> queryFilter) throws Exception{
		return baseService.query(queryFilter);
	}
	
	/**
	 * 任务催办明细页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@RequestMapping(value="get",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "任务催办明细页面", httpMethod = "GET", notes = "任务催办明细页面")
	public Object get(
			@ApiParam(name="id",value="，任务催办id", required = true) @RequestParam String id) throws Exception {
		if(StringUtil.isEmpty(id)){
			return null;
		}
		BpmTaskReminder bpmTaskReminder=baseService.get(id);
		return bpmTaskReminder;
	}
	
	/**
	 * 保存任务催办信息
	 * @param request
	 * @param response
	 * @param bpmTaskReminder
	 * @throws Exception 
	 * void
	 * @exception 
	 */
	@RequestMapping(value="save",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存任务催办信息", httpMethod = "POST", notes = "保存任务催办信息")
	public CommonResult<String> save(
			@ApiParam(name="bpmTaskReminder",value="任务催办对象", required = true) @RequestBody BpmTaskReminder bpmTaskReminder)throws Exception {
		String id=bpmTaskReminder.getId();
		try {
			if(StringUtil.isEmpty(id)){
				bpmTaskReminder.setId(UniqueIdUtil.getSuid());
				baseService.create(bpmTaskReminder);
				return new CommonResult<String>(true,"添加任务催办成功","");
			}else{
				baseService.update(bpmTaskReminder);
				return new CommonResult<String>(true,"更新任务催办成功","");
			}
		} catch (Exception e) {
			return new CommonResult<String>(false,e.getMessage(),"");
		}
	}
	
	/**
	 * 批量删除任务催办记录
	 * @param request
	 * @param response
	 * @throws Exception 
	 * void
	 * @exception 
	 */
	
	@RequestMapping(value="remove",method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除任务催办记录", httpMethod = "DELETE", notes = "批量删除任务催办记录")
	public CommonResult<String> remove(
			@ApiParam(name="id",value="催办id字符串", required = true) @RequestParam String id)throws Exception {
		try {
			String[] aryIds = id.split(",");
			baseService.removeByIds(aryIds);
			return new CommonResult<String>(true,"删除任务催办成功","");
		} catch (Exception e) {
			return new CommonResult<String>(false,"删除任务催办成功","");
		}
	}
	
	@RequestMapping(value="executeTaskReminderJob",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "执行催办任务", httpMethod = "GET", notes = "执行催办任务")
	public CommonResult<String>  executeTaskReminderJob() throws Exception{
		baseService.executeTaskReminderJob();
		return new CommonResult<String>(true,"执行催办任务成功","");
	}
	
}
