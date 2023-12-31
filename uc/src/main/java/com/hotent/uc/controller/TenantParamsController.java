package com.hotent.uc.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
import com.hotent.uc.manager.TenantParamsManager;
import com.hotent.uc.model.TenantParams;
import com.hotent.uc.params.params.ParamObject;

/**
 * 
 * <pre> 
 * 描述：租户扩展参数值 控制器类
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2020-04-17 14:54:36
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@RestController
@RequestMapping(value="/uc/tenantParams/v1")
@Api(tags="租户扩展参数值")
@ApiGroup(group= {ApiGroupConsts.GROUP_UC})
public class TenantParamsController extends BaseController<TenantParamsManager, TenantParams>{
	@Resource
	TenantParamsManager tenantParamsManager;
	
	/**
	 * 租户扩展参数值列表(分页条件查询)数据
	 * @param request
	 * @return
	 * @throws Exception 
	 * PageJson
	 * @exception 
	 */
	@PostMapping("/listJson")
	@ApiOperation(value="租户扩展参数值数据列表", httpMethod = "POST", notes = "获取租户扩展参数值列表")
	public PageList<TenantParams> list(@ApiParam(name="queryFilter",value="查询对象")@RequestBody QueryFilter queryFilter) throws Exception{
		return tenantParamsManager.query(queryFilter);
	}
	
	/**
	 * 租户扩展参数值明细页面
	 * @param id
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/getJson")
	@ApiOperation(value="租户扩展参数值数据详情",httpMethod = "GET",notes = "租户扩展参数值数据详情")
	public TenantParams get(@ApiParam(name="id",value="业务对象主键", required = true)@RequestParam String id) throws Exception{
		return tenantParamsManager.get(id);
	}
	
    /**
	 * 新增租户扩展参数值
	 * @param tenantParams
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@PostMapping(value="save")
	@ApiOperation(value = "新增,更新租户扩展参数值数据", httpMethod = "POST", notes = "新增,更新租户扩展参数值数据")
	public CommonResult<String> save(@ApiParam(name="tenantParams",value="租户扩展参数值业务对象", required = true)@RequestBody TenantParams tenantParams) throws Exception{
		String msg = "添加租户扩展参数值成功";
		if(StringUtil.isEmpty(tenantParams.getId())){
			tenantParamsManager.create(tenantParams);
		}else{
			tenantParamsManager.update(tenantParams);
			 msg = "更新租户扩展参数值成功";
		}
		return new CommonResult<String>(msg);
	}
	
	/**
	 * 批量删除租户扩展参数值记录
	 * @param ids
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="/remove")
	@ApiOperation(value = "批量删除租户扩展参数值记录", httpMethod = "DELETE", notes = "批量删除租户扩展参数值记录")
	public CommonResult<String> removes(@ApiParam(name="ids",value="业务主键数组,多个业务主键之间用逗号分隔", required = true)@RequestParam String...ids) throws Exception{
		tenantParamsManager.removeByIds(ids);
		return new CommonResult<String>(true, "删除成功");
	}
	
	@RequestMapping(value="getParamsValue",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取租户参数值列表", httpMethod = "GET", notes = "获取租户参数值列表")
	public List<TenantParams> getParamsValue(@ApiParam(name="tenantId",value="租户id", required = true)@RequestParam String tenantId) throws Exception{
		return tenantParamsManager.getByTenantId(tenantId);
	}
	
	@RequestMapping(value="saveTenantParams",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "保存租户扩展参数值", httpMethod = "POST", notes = "保存租户扩展参数值")
	public CommonResult<String> saveTenantParams(@ApiParam(name="tenantId",value="租户id",required=true)  @RequestParam String tenantId,
			@ApiParam(name="params",value="租户参数值",required=true) @RequestBody List<ParamObject> params) throws Exception{
		return tenantParamsManager.saveUserParams(tenantId,params);
	}
}
