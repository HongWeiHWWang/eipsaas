package com.hotent.service.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.service.InvokeResult;
import com.hotent.base.service.ServiceClient;
import com.hotent.service.manager.ServiceSetManager;
import com.hotent.service.model.ServiceSet;
import com.hotent.service.parse.ServiceBean;
import com.hotent.service.parse.ServiceParser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/service/set/v1/")
@Api(tags="服务调用设置")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class ServiceSetController {
	@Resource
	ServiceParser serviceParser;
	@Resource
	ServiceSetManager serviceSetManager;
	@Resource
	ServiceClient serviceClient;
	
	@RequestMapping(value="list", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "获取服务调用设置列表（带分页信息）", httpMethod = "POST", notes = "获取服务调用设置列表")
	public PageList<ServiceSet> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter queryFilter) throws Exception {
		return serviceSetManager.query(queryFilter);
	}
	
	@RequestMapping(value="parser",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "解析WebService", httpMethod = "POST", notes = "解析WebService详情")
	public ServiceBean parser(@ApiParam(name="wsdlUrl",value="WebService的wsdl地址", required = true) @RequestParam String wsdlUrl) throws Exception{
		return serviceParser.parse(wsdlUrl);
	}
	
	@RequestMapping(value="detail",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取服务调用设置详情", httpMethod = "GET", notes = "获取服务调用设置详情")
	public ServiceSet detail(@ApiParam(name="alias",value="服务设置别名", required = true) @RequestParam String alias) throws Exception{
		return serviceSetManager.getByAlias(alias);
	}
	
	@RequestMapping(value="invoke",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "通过设置别名调用服务", httpMethod = "POST", notes = "通过设置别名调用服务")
	public InvokeResult invoke(@ApiParam(name="alias",value="服务设置别名", required = true) @RequestParam String alias,
							   @ApiParam(name="map",value="调用参数") @RequestBody Map<String, Object> map) throws Exception{
		return serviceClient.invoke(alias, map);
	}
	
	@RequestMapping(value="save",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存服务调用设置", httpMethod = "POST", notes = "保存服务调用设置")
	public CommonResult<String> save(@ApiParam(name="serviceSet",value="服务调用设置", required = true) @RequestBody ServiceSet serviceSet) throws Exception{
		serviceSetManager.saveData(serviceSet);
		return new CommonResult<String>("保存成功");
	}
	
	@RequestMapping(value="removes",method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除bo定义", httpMethod = "DELETE", notes = "批量删除bo定义")
	public CommonResult<String> batchRemove(@ApiParam(name="ids",value="bo主键集合", required = true) @RequestParam String...ids) throws Exception{
		serviceSetManager.removeByIds(ids);
		return new CommonResult<String>(true, "删除成功");
	}
}