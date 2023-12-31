package com.hotent.uc.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.uc.exception.BaseException;
import com.hotent.uc.model.Properties;
import com.hotent.uc.params.properties.PropertiesVo;

/**
 * 系统参数组织模块接口
 * @author zhangxw
 *
 */
@RestController
@RequestMapping("/api/properties/v1/")
@Api(tags="PropertiesController")
public class PropertiesController extends BaseController {
	
	
	/**
	 * 查询系统参数
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="properties/getPropertiesPage",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取系统参数列表（带分页信息）", httpMethod = "POST", notes = "获取系统参数列表")
	public PageList<Properties> getPropertiesPage(@ApiParam(name = "filter", value = "查询参数", required = true) @RequestBody QueryFilter filter) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	/**
	 * 获取所有系统参数
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="properties/getPropertiesList",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取所有系统参数", httpMethod = "POST", notes = "获取所有系统参数")
	public List<Properties> getPropertiesList(@ApiParam(name = "filter", value = "查询参数", required = true) @RequestBody QueryFilter filter) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	
	/**
	 * 更新系统参数
	 * @param Properties
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="properties/updateProperties",method=RequestMethod.PUT, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "更新系统参数", httpMethod = "PUT", notes = "更新系统参数")
	public CommonResult<String> updateProperties(@ApiParam(name="Properties",value="系统参数参数对象", required = true) @RequestBody  PropertiesVo Properties) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}
	
	
	/**
	 * 获取系统参数信息
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="properties/getProperties",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "根据编码获取系统参数信息", httpMethod = "GET", notes = "获取系统参数信息")
	public Properties getProperties(@ApiParam(name="code",value="系统参数编码", required = true) @RequestParam String code) throws Exception{
		throw new BaseException("uc-demo模块不需要实现该接口");
	}

}
