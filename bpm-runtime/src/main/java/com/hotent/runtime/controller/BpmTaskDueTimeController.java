package com.hotent.runtime.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.time.LocalDateTime;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.feign.PortalFeignService;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.time.DateFormatUtil;
import com.hotent.base.util.time.DateUtil;
import com.hotent.base.util.time.TimeUtil;
import com.hotent.bpm.persistence.manager.BpmTaskDueTimeManager;
import com.hotent.bpm.persistence.model.BpmTaskDueTime;



/**
 * 任务期限统计 控制器类
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月11日
 */
@RestController
@RequestMapping("/runtime/bpmTaskDueTime/v1/")
@Api(tags="任务期限统计")
@ApiGroup(group= {ApiGroupConsts.GROUP_BPM})
public class BpmTaskDueTimeController extends BaseController<BpmTaskDueTimeManager,BpmTaskDueTime>{
	@Resource
	BpmTaskDueTimeManager bpmTaskDueTimeManager;
	@Resource
	PortalFeignService PortalFeignService;
	
	@RequestMapping(value="list", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "任务期限统计列表(分页条件查询)", httpMethod = "POST", notes = "任务期限统计列表(分页条件查询)")
	public PageList<BpmTaskDueTime> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter queryFilter) throws Exception {
		return bpmTaskDueTimeManager.query(queryFilter);
	}
	
	
	@RequestMapping(value="get",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取任务期限统计明细", httpMethod = "GET", notes = "获取任务期限统计明细")
	public BpmTaskDueTime get(@ApiParam(name="id",value="任务期限统计id", required = true) @RequestParam String id) throws Exception{
		return bpmTaskDueTimeManager.get(id);
	}
	
	@RequestMapping(value="getByTaskId",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据任务id 获取最新的延期信息", httpMethod = "GET", notes = "根据任务id 获取最新的延期信息")
	public BpmTaskDueTime getByTaskId(@ApiParam(name="taskId",value="任务id", required = true) @RequestParam String taskId) throws Exception{
		BpmTaskDueTime bpmTaskDueTime=bpmTaskDueTimeManager.getByTaskId(taskId);
		if(BeanUtils.isEmpty(bpmTaskDueTime)){
			return new BpmTaskDueTime();
		}
		int remainingTime=getRemainingTime(bpmTaskDueTime);
		bpmTaskDueTime.setRemainingTime(remainingTime);
		return bpmTaskDueTime;
	}
	
	@RequestMapping(value="getExpirationDate",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据id获取到期时间", httpMethod = "GET", notes = "根据id获取到期时间")
	public Object getExpirationDate(
			@ApiParam(name="id",value="任务id", required = true) @RequestParam String id,
			@ApiParam(name="addDueTime",value="任务id", required = true) @RequestParam Integer addDueTime) throws Exception{
		addDueTime = BeanUtils.isEmpty(addDueTime)?0:addDueTime;
		BpmTaskDueTime bpmTaskDueTime=bpmTaskDueTimeManager.get(id);
		LocalDateTime expDate =  getExpDate(bpmTaskDueTime,addDueTime);
		if(BeanUtils.isNotEmpty(expDate)) return TimeUtil.getTimeMillis(expDate);
		return "0000-00-00 00:00:00";
	}
	
	@RequestMapping(value="save",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "添加任务期限统计信息", httpMethod = "POST", notes = "添加任务期限统计信息")
	public CommonResult<String> save(@ApiParam(name="bpmTaskDueTime",value="任务期限统计信息", required = true) @RequestBody BpmTaskDueTime bpmTaskDueTime) throws Exception{
		String resultMsg=null;
		boolean isTrue = true;
		try {
			//任务是否已过期
			boolean isExpire = false;
			String id = bpmTaskDueTime.getId();
			if(StringUtil.isNotEmpty(id)){
				BpmTaskDueTime oldBpmTaskDueTime = bpmTaskDueTimeManager.get(id);
				if(BeanUtils.isNotEmpty(oldBpmTaskDueTime)){
					String expireTime = DateFormatUtil.formaDatetTime(oldBpmTaskDueTime.getExpirationDate());
					String nowtime = DateFormatUtil.formaDatetTime(LocalDateTime.now());
					isExpire = DateUtil.compare(expireTime,nowtime);
					if(isExpire){
						resultMsg="当前任务已过期，不能再进行延期操作！";
						isTrue = false;
					}
				}
			}
			if(!isExpire){
				LocalDateTime expDate =  getExpDate(bpmTaskDueTime,bpmTaskDueTime.getAddDueTime());
				bpmTaskDueTime.setDueTime(bpmTaskDueTime.getDueTime()+bpmTaskDueTime.getAddDueTime());
				bpmTaskDueTime.setExpirationDate(expDate);
				bpmTaskDueTime.setCreateTime(LocalDateTime.now());
				bpmTaskDueTime.setStatus((short)1);
				bpmTaskDueTime.setParentId(bpmTaskDueTime.getId());
				int remainingTime = getRemainingTime(bpmTaskDueTime);
				
				bpmTaskDueTime.setRemainingTime(remainingTime);
				
				bpmTaskDueTimeManager.updateAndSave(bpmTaskDueTime);
				resultMsg="延期成功";
			}
		} catch (Exception e) {
			resultMsg="对任务期限统计操作失败："+e.getMessage();
			isTrue = false;
		}
		return new CommonResult<String>(isTrue,resultMsg,"");
	}
	
	private int getRemainingTime(BpmTaskDueTime bpmTaskDueTime) throws Exception {
		int remainingTime=0;
		if("caltime".equals(bpmTaskDueTime.getDateType())){
			// getSecondDiff 秒
			remainingTime = TimeUtil.getSecondDiff(LocalDateTime.now(), bpmTaskDueTime.getStartTime())/60;
		}else{
			// getWorkTimeByUser 毫秒
			ObjectNode params=JsonUtil.getMapper().createObjectNode();
			params.put("userId", bpmTaskDueTime.getUserId());
			params.put("startTime", DateFormatUtil.formaDatetTime(bpmTaskDueTime.getStartTime()));
			params.put("endTime", DateFormatUtil.formaDatetTime(LocalDateTime.now()));
			remainingTime =(int) (PortalFeignService.getWorkTimeByUser(params)/60000);
		}
		remainingTime = bpmTaskDueTime.getDueTime() - remainingTime;
		if(remainingTime<=0){
			remainingTime = 0;
		}
		return remainingTime;
	}

	@RequestMapping(value="remove",method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "删除任务期限统计记录", httpMethod = "DELETE", notes = "删除任务期限统计记录")
	public CommonResult<String> remove(@ApiParam(name="aryIds",value="任务期限统计记录ID，多个用“,”号分隔", required = true) @RequestParam String aryIds) throws Exception{
		String[] ids = null;
		if(!StringUtil.isEmpty(aryIds)){
			ids = aryIds.split(",");
		}
		bpmTaskDueTimeManager.removeByIds(ids);
		return new CommonResult<String>(true,"删除催办历史成功","");
	}

	
	private LocalDateTime getExpDate(BpmTaskDueTime bpmTaskDueTime,int addDueTime) throws Exception{
		LocalDateTime expDate = null;
		// 日历日
		if("caltime".equals(bpmTaskDueTime.getDateType())){
			expDate =  TimeUtil.getLocalDateTimeByMills(TimeUtil.getNextTime(TimeUtil.MINUTE, addDueTime,TimeUtil.getTimeMillis(bpmTaskDueTime.getExpirationDate())));
		}else{
			ObjectNode params=JsonUtil.getMapper().createObjectNode();
			params.put("userId", bpmTaskDueTime.getUserId());
			if(BeanUtils.isNotEmpty(bpmTaskDueTime.getExpirationDate())){
				params.put("startTime", DateFormatUtil.formaDatetTime(bpmTaskDueTime.getExpirationDate()));
			}
			params.put("time", addDueTime);
			String expDateStr = PortalFeignService.getEndTimeByUser(params);
			expDate = DateFormatUtil.parse(expDateStr);
		}
		
		return expDate;
	}
}
