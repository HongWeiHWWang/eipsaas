package com.hotent.runtime.controller;


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
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.runtime.manager.BpmTestCaseManager;
import com.hotent.runtime.model.BpmTestCase;
import com.hotent.runtime.params.TestCaseBaseInfoVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * 流程的测试用例设置 控制器类
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */

@RestController
@RequestMapping("/runtime/bpmTestCase/v1/")
@Api(tags="流程测试用例设置")
@ApiGroup(group= {ApiGroupConsts.GROUP_BPM})
public class BpmTestCaseController extends BaseController<BpmTestCaseManager,BpmTestCase>{
	@Resource
	BpmTestCaseManager bpmTestCaseManager;
	
	@RequestMapping(value="list", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "获取测试用例设置列表（带分页信息）", httpMethod = "POST", notes = "获取测试用例设置列表")
	public PageList<BpmTestCase> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter queryFilter) throws Exception {
		return bpmTestCaseManager.query(queryFilter);
	}
	
	@RequestMapping(value="get",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取测试用例设置明细", httpMethod = "GET", notes = "获取测试用例设置明细")
	public BpmTestCase get(@ApiParam(name="id",value="bo定义别名", required = true) @RequestParam String id) throws Exception{
		return bpmTestCaseManager.get(id);
	}
	
	@RequestMapping(value="save",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存流程的测试用例设置信息", httpMethod = "POST", notes = "保存流程的测试用例设置信息")
	public CommonResult<String> save(@ApiParam(name="bpmTestCase",value="流程的测试用例设置", required = true) @RequestBody BpmTestCase bpmTestCase) throws Exception{
		String id=bpmTestCase.getId();
		String resultMsg=null;
		if(StringUtil.isEmpty(id)){
			bpmTestCase.setId(UniqueIdUtil.getSuid());
			bpmTestCaseManager.create(bpmTestCase);
			resultMsg="添加流程的测试用例设置成功";
		}else{
			bpmTestCaseManager.update(bpmTestCase);
			resultMsg="更新流程的测试用例设置成功";
		}
		return new CommonResult<String>(true,resultMsg,"");
	}
	
	@RequestMapping(value="remove",method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "删除流程的测试用例设置", httpMethod = "DELETE", notes = "删除流程的测试用例设置")
	public CommonResult<String> remove(@ApiParam(name="ids",value="流程的测试用例设置id，多个用“,”号分隔", required = true) @RequestParam String ids) throws Exception{
		String message = "删除流程的测试用例设置成功";
		boolean isTrue = true;
		try {
			String[] idArr = null;
			if(!StringUtil.isEmpty(ids)){
				idArr = ids.split(",");
			}
			bpmTestCaseManager.removeByIds(idArr);
		} catch (Exception e) {
			message= "删除流程的测试用例设置失败："+e.getMessage();
			isTrue = false;
		}
		return new CommonResult<String>(isTrue,message,"");
	}
	
	@RequestMapping(value="startTest",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "启动测试", httpMethod = "POST", notes = "启动测试")
	public CommonResult<String> startTest(@ApiParam(name="ids",value="流程的测试用例设置id，多个用“,”号分隔", required = true) @RequestBody String ids) throws Exception{
		bpmTestCaseManager.startTest(ids);
		return new CommonResult<String>(true,"启动测试用例成功","");
	}
	
	@RequestMapping(value="doNext",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "继续启动测试用例试", httpMethod = "POST", notes = "继续启动测试用例")
	public CommonResult<String> doNext(@ApiParam(name="id",value="流程实例id", required = true) @RequestParam String id) throws Exception{
		if(StringUtil.isEmpty(id)){
			return new CommonResult<String>(false, "没有传递流程实例id","");
		}
		bpmTestCaseManager.doNext(id);
		return  new CommonResult<String>(true, "测试用例重新启动成功","");
	}
	
	@RequestMapping(value="getBaseInfo",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取配置信息", httpMethod = "GET", notes = "获取配置信息")
	public TestCaseBaseInfoVo getBaseInfo(@ApiParam(name="defKey",value="流程定义Key", required = true) @RequestParam String defKey) throws Exception{
		return bpmTestCaseManager.getBaseInfo(defKey);
	}
	
	
	@RequestMapping(value="getReportData",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取测试用例图表信息", httpMethod = "POST", notes = "获取测试用例图表信息")
	public CommonResult<ObjectNode> getReportData(@ApiParam(name="aryIds",value="流程的测试用例设置id，多个用“,”号分隔", required = true) @RequestBody String aryIds) throws Exception{
		if(StringUtil.isEmpty(aryIds)){
			return new CommonResult<ObjectNode>(false, "测试用例id不能为空", null);
		}
		ObjectNode jo = bpmTestCaseManager.getReportData(aryIds);
		return  new CommonResult<ObjectNode>(true, "获取测试用例图表信息成功", jo); 
	}
}
