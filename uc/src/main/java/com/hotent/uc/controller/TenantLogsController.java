package com.hotent.uc.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.StringUtil;
import com.hotent.uc.manager.TenantLogsManager;
import com.hotent.uc.model.TenantLogs;

/**
 * 
 * <pre> 
 * 描述：租户管理操作日志 控制器类
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2020-04-17 14:53:55
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@RestController
@RequestMapping(value="/uc/tenantLogs/v1")
@Api(tags="租户管理操作日志")
@ApiGroup(group= {ApiGroupConsts.GROUP_UC})
public class TenantLogsController extends BaseController<TenantLogsManager,TenantLogs>{
	@Resource
	TenantLogsManager tenantLogsManager;
	
	/**
	 * 租户管理操作日志列表(分页条件查询)数据
	 * @param request
	 * @return
	 * @throws Exception 
	 * PageJson
	 * @exception 
	 */
	@PostMapping("/listJson")
	@ApiOperation(value="租户管理操作日志数据列表", httpMethod = "POST", notes = "获取租户管理操作日志列表")
	public PageList<TenantLogs> list(@ApiParam(name="queryFilter",value="查询对象")@RequestBody QueryFilter queryFilter) throws Exception{
		return tenantLogsManager.query(queryFilter);
	}
	
	/**
	 * 租户管理操作日志明细页面
	 * @param id
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/getJson")
	@ApiOperation(value="租户管理操作日志数据详情",httpMethod = "GET",notes = "租户管理操作日志数据详情")
	public TenantLogs get(@ApiParam(name="id",value="业务对象主键", required = true)@RequestParam String id) throws Exception{
		return tenantLogsManager.get(id);
	}
	
    /**
	 * 新增租户管理操作日志
	 * @param tenantLogs
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@PostMapping(value="save")
	@ApiOperation(value = "新增,更新租户管理操作日志数据", httpMethod = "POST", notes = "新增,更新租户管理操作日志数据")
	public CommonResult<String> save(@ApiParam(name="tenantLogs",value="租户管理操作日志业务对象", required = true)@RequestBody TenantLogs tenantLogs) throws Exception{
		String msg = "添加租户管理操作日志成功";
		if(StringUtil.isEmpty(tenantLogs.getId())){
			tenantLogsManager.create(tenantLogs);
		}else{
			tenantLogsManager.update(tenantLogs);
			 msg = "更新租户管理操作日志成功";
		}
		return new CommonResult<String>(msg);
	}
	
	/**
	 * 批量删除租户管理操作日志记录
	 * @param ids
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="/remove")
	@ApiOperation(value = "批量删除租户管理操作日志记录", httpMethod = "DELETE", notes = "批量删除租户管理操作日志记录")
	public CommonResult<String> removes(@ApiParam(name="ids",value="业务主键数组,多个业务主键之间用逗号分隔", required = true)@RequestParam String...ids) throws Exception{
		tenantLogsManager.removeByIds(ids);
		return new CommonResult<String>(true, "删除成功");
	}
}
