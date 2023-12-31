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
import com.hotent.bpm.persistence.manager.BpmReminderHistoryManager;
import com.hotent.bpm.persistence.model.BpmReminderHistory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 催办历史 控制器类
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月9日
 */
@RestController
@RequestMapping("/flow/bpmReminderHistory/v1/")
@Api(tags="流程催办历史")
@ApiGroup(group= {ApiGroupConsts.GROUP_BPM})
public class BpmReminderHistoryController extends BaseController<BpmReminderHistoryManager, BpmReminderHistory>{
	@RequestMapping(value="list", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "催办历史列表(分页条件查询)数据", httpMethod = "POST", notes = "获取催办历史列表")
	public PageList<BpmReminderHistory> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<BpmReminderHistory> queryFilter) throws Exception {
		return baseService.query(queryFilter);
	}
	
	@RequestMapping(value="get",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取催办历史明细", httpMethod = "GET", notes = "获取催办历史明细")
	public BpmReminderHistory get(@ApiParam(name="id",value="催办历史id", required = true) @RequestParam String id) throws Exception{
		return baseService.get(id);
	}
	
	@RequestMapping(value="save",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "添加催办历史信息", httpMethod = "POST", notes = "添加催办历史信息")
	public CommonResult<String> save(@ApiParam(name="bpmReminderHistory",value="催办历史类", required = true) @RequestBody BpmReminderHistory bpmReminderHistory) throws Exception{
		String id=bpmReminderHistory.getId();
		String resultMsg = "添加催办历史成功";
		if(StringUtil.isEmpty(id)){
			bpmReminderHistory.setId(UniqueIdUtil.getSuid());
			baseService.create(bpmReminderHistory);
		}else{
			baseService.update(bpmReminderHistory);
			resultMsg="更新催办历史成功";
		}
		return new CommonResult<String>(true,resultMsg,"");
	}
	
	@RequestMapping(value="remove",method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "删除催办历史记录", httpMethod = "DELETE", notes = "删除催办历史记录")
	public CommonResult<String> remove(@ApiParam(name="aryIds",value="催办历史记录ID，多个用“,”号分隔", required = true) @RequestParam String aryIds) throws Exception{
		baseService.removeByIds(aryIds);
		return new CommonResult<String>(true,"删除催办历史成功","");
	}
}
