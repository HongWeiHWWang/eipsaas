package com.hotent.uc.controller;

import java.util.Optional;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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
import com.hotent.uc.manager.TenantAuthManager;
import com.hotent.uc.model.TenantAuth;
import com.hotent.uc.params.tenant.TenantAuthAddObject;

/**
 * 
 * <pre> 
 * 描述：租户分类管理员 控制器类
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2020-04-17 10:55:39
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@RestController
@RequestMapping(value="/uc/tenantAuth/v1")
@Api(tags="租户分类管理员")
@ApiGroup(group= {ApiGroupConsts.GROUP_UC})
public class TenantAuthController extends BaseController<TenantAuthManager,TenantAuth>{
	@Resource
	TenantAuthManager tenantAuthManager;
	
	/**
	 * 租户管理员列表(分页条件查询)数据
	 * @param request
	 * @return
	 * @throws Exception 
	 * PageJson
	 * @exception 
	 */
	@PostMapping("/listJson")
	@ApiOperation(value="租户管理员数据列表", httpMethod = "POST", notes = "获取租户管理员列表")
	public PageList<TenantAuth> list(@ApiParam(name="queryFilter",value="查询对象")@RequestBody QueryFilter queryFilter) throws Exception{
		return tenantAuthManager.query(queryFilter);
	}
	
	/**
	 * 租户管理员列表(分页条件查询)数据
	 * @param request
	 * @return
	 * @throws Exception 
	 * PageJson
	 * @exception 
	 */
	@PostMapping("/queryByTypeOrTenant")
	@ApiOperation(value="租户管理员数据列表", httpMethod = "POST", notes = "获取租户管理员列表")
	public PageList<TenantAuth> queryByTypeOrTenant(@ApiParam(name="queryFilter",value="查询对象")@RequestBody QueryFilter queryFilter) throws Exception{
		return tenantAuthManager.queryByTypeAndTenantId(queryFilter);
	}
	
	/**
	 * 租户管理员明细页面
	 * @param id
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/getJson")
	@ApiOperation(value="租户管理员数据详情",httpMethod = "GET",notes = "租户管理员数据详情")
	public TenantAuth get(@ApiParam(name="id",value="业务对象主键", required = true)@RequestParam String id) throws Exception{
		return tenantAuthManager.get(id);
	}
	
    /**
	 * 新增租户管理员
	 * @param tenantAuth
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@PostMapping(value="save")
	@ApiOperation(value = "新增,更新租户管理员数据", httpMethod = "POST", notes = "新增,更新租户管理员数据")
	public CommonResult<String> save(@ApiParam(name="tenantAuth",value="租户管理员业务对象", required = true)@RequestBody TenantAuth tenantAuth) throws Exception{
		String msg = "添加租户管理员成功";
		if(StringUtil.isEmpty(tenantAuth.getId())){
			tenantAuthManager.create(tenantAuth);
		}else{
			tenantAuthManager.update(tenantAuth);
			 msg = "更新租户管理员成功";
		}
		return new CommonResult<String>(msg);
	}
	
	/**
	 * 批量删除租户管理员记录
	 * @param ids
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="/remove")
	@ApiOperation(value = "批量删除租户管理员记录", httpMethod = "DELETE", notes = "批量删除租户管理员记录")
	public CommonResult<String> removes(@ApiParam(name="ids",value="业务主键数组,多个业务主键之间用逗号分隔", required = true)@RequestParam String...ids) throws Exception{
		tenantAuthManager.removeByIds(ids);
		return new CommonResult<String>(true, "删除成功");
	}
	
	@RequestMapping(value="/saveTenantAuth",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "分配租户管理员（按用户）", httpMethod = "POST", notes = "分配租户管理员（按用户）")
	public CommonResult<String> saveTenantAuth(@ApiParam(name="authAddObject",value="authAddObject", required = true) @RequestBody TenantAuthAddObject authAddObject) throws Exception{
		return tenantAuthManager.saveTenantAuth(authAddObject);
	}
	
	@RequestMapping(value="deleteTenantAuth",method=RequestMethod.DELETE, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "移除租户管理员", httpMethod = "DELETE", notes = "移除租户管理员")
	public CommonResult<String> deleteUserRole(@ApiParam(name="typeId",value="租户类型id", required = true) @RequestParam String typeId,
			@ApiParam(name="tenantId",value="租户id", required = false) @RequestParam Optional<String> tenantId,
			@ApiParam(name="userIds",value="用户ID，多个用“,”号隔开", required = true) @RequestParam String userIds) throws Exception{
		return tenantAuthManager.removeTenantAuth(typeId,tenantId.orElse(""),userIds);
	}
}
