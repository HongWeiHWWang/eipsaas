package com.hotent.form.controller;


import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.form.model.FormHistory;
import com.hotent.form.persistence.manager.FormHistoryManager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * 流程表单HTML设计历史记录 控制器类
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月8日
 */
@RestController
@RequestMapping("/form/history/v1")
@Api(tags="历史表单")
@ApiGroup(group= {ApiGroupConsts.GROUP_FORM})
@SuppressWarnings({ "unchecked", "rawtypes" })
public class HistoryController extends BaseController<FormHistoryManager, FormHistory>{
	@Resource
	FormHistoryManager bpmFormHistoryManager;

	@RequestMapping(value="list", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "流程表单HTML设计历史记录列表(分页条件查询)", httpMethod = "POST", notes = "流程表单HTML设计历史记录列表(分页条件查询)")	
	public  PageList<FormHistory> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter queryFilter)throws Exception{
		return bpmFormHistoryManager.query(queryFilter);
	}

	@RequestMapping(value="historyGet", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "流程表单HTML设计历史记录信息", httpMethod = "POST", notes = "流程表单HTML设计历史记录信息")
	public CommonResult get(@ApiParam(name="id",value="流程表单HTML设计历史记录ID")@RequestBody String id) throws Exception{
		FormHistory bpmFormHistory=null;
		if(StringUtil.isNotEmpty(id)){
			bpmFormHistory=bpmFormHistoryManager.get(id);
		}
		Map map=new HashMap();
		map.put("bpmFormHistory", bpmFormHistory);
		return new CommonResult(true, "", map);
	}


	@RequestMapping(value="save",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存流程表单HTML设计历史记录信息", httpMethod = "POST", notes = "保存流程表单HTML设计历史记录信息")
	public CommonResult save(@ApiParam(name="bpmFormHistory",value="流程表单HTML设计历史记录对象", required = true) @RequestBody FormHistory bpmFormHistory) throws Exception{
		String id=bpmFormHistory.getId();
		if(StringUtil.isEmpty(id)){
			bpmFormHistory.setId(UniqueIdUtil.getSuid());
		}
		bpmFormHistoryManager.create(bpmFormHistory);
		return new CommonResult(true, "添加流程表单HTML设计历史记录成功", null);
	}


	@RequestMapping(value="remove",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除流程表单HTML设计历史记录", httpMethod = "POST", notes = "批量删除流程表单HTML设计历史记录")
	public CommonResult remove(@ApiParam(name="id",value="流程表单HTML设计历史记录ID!多个ID用,分割", required = true)@RequestBody String id) throws Exception{
		String[] aryIds=null;
		if(!StringUtil.isEmpty(id)){
			aryIds=id.split(",");
		}
		bpmFormHistoryManager.removeByIds(aryIds);
		return new CommonResult(true,"删除流程表单HTML设计历史记录成功",null);
	}

	@RequestMapping(value="getByAjax",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "异步返回历史记录", httpMethod = "POST", notes = "异步返回历史记录")
	public FormHistory getByAjax(@ApiParam(name="hisId",value="流程表单HTML设计历史记录ID!", required = true)@RequestBody String hisId) throws Exception {
		FormHistory BpmFormHistory = bpmFormHistoryManager.get(hisId);
		return BpmFormHistory;
	}
}
