package com.hotent.uc.controller;

import java.util.ArrayList;
import java.util.List;

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

import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.uc.manager.TenantIgnoreMenuManager;
import com.hotent.uc.model.TenantIgnoreMenu;

/**
 * 
 * <pre> 
 * 描述：租户禁用菜单 控制器类
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2020-04-20 17:00:53
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@RestController
@RequestMapping(value="/uc/tenantIgnoreMenu/v1")
@Api(tags="租户禁用菜单管理")
public class TenantIgnoreMenuController extends BaseController<TenantIgnoreMenuManager,TenantIgnoreMenu>{
	@Resource
	TenantIgnoreMenuManager tenantIgnoreMenuManager;
	
	/**
	 * 租户禁用菜单列表(分页条件查询)数据
	 * @param request
	 * @return
	 * @throws Exception 
	 * PageJson
	 * @exception 
	 */
	@PostMapping("/listJson")
	@ApiOperation(value="租户禁用菜单数据列表", httpMethod = "POST", notes = "获取租户禁用菜单列表")
	public PageList<TenantIgnoreMenu> list(@ApiParam(name="queryFilter",value="查询对象")@RequestBody QueryFilter queryFilter) throws Exception{
		return tenantIgnoreMenuManager.query(queryFilter);
	}
	
	/**
	 * 租户禁用菜单明细页面
	 * @param id
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/getJson")
	@ApiOperation(value="租户禁用菜单数据详情",httpMethod = "GET",notes = "租户禁用菜单数据详情")
	public TenantIgnoreMenu get(@ApiParam(name="id",value="业务对象主键", required = true)@RequestParam String id) throws Exception{
		return tenantIgnoreMenuManager.get(id);
	}
	
	/**
	 * 获取租户禁用菜单
	 * @param id
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/getIgnoreMenuCodes")
	@ApiOperation(value="租户禁用菜单数据详情",httpMethod = "GET",notes = "租户禁用菜单数据详情")
	public List<String> getIgnoreMenuCodes(@ApiParam(name="tenantId",value="租户id", required = true)@RequestParam String tenantId) throws Exception{
		List<TenantIgnoreMenu> ignoreMenus = tenantIgnoreMenuManager.getByTenantId(tenantId);
		List<String> ignoreCodes = new ArrayList<String>();
		if(BeanUtils.isNotEmpty(ignoreMenus)){
			for (TenantIgnoreMenu menu : ignoreMenus) {
				ignoreCodes.add(menu.getMenuCode());
			}
		}
		return ignoreCodes;
	}
	
    /**
	 * 新增租户禁用菜单
	 * @param tenantIgnoreMenu
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@PostMapping(value="save")
	@ApiOperation(value = "新增,更新租户禁用菜单数据", httpMethod = "POST", notes = "新增,更新租户禁用菜单数据")
	public CommonResult<String> save(@ApiParam(name="tenantIgnoreMenu",value="租户禁用菜单业务对象", required = true)@RequestBody TenantIgnoreMenu tenantIgnoreMenu) throws Exception{
		String msg = "添加租户禁用菜单成功";
		if(StringUtil.isEmpty(tenantIgnoreMenu.getId())){
			tenantIgnoreMenuManager.create(tenantIgnoreMenu);
		}else{
			tenantIgnoreMenuManager.update(tenantIgnoreMenu);
			 msg = "更新租户禁用菜单成功";
		}
		return new CommonResult<String>(msg);
	}
	
	@PostMapping(value="saveByTenantId")
	@ApiOperation(value = "新增,更新租户禁用菜单数据", httpMethod = "POST", notes = "新增,更新租户禁用菜单数据")
	public CommonResult<String> saveByTenantId(@ApiParam(name="tenantId",value="租户id", required = true)@RequestParam String tenantId,
			@ApiParam(name="ignoreMenus",value="需要禁用的菜单别名列表", required = true)@RequestBody List<String> ignoreMenus) throws Exception{
		return tenantIgnoreMenuManager.saveByTenantId(tenantId, ignoreMenus);
	}
	
	/**
	 * 批量删除租户禁用菜单记录
	 * @param ids
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="/remove")
	@ApiOperation(value = "批量删除租户禁用菜单记录", httpMethod = "DELETE", notes = "批量删除租户禁用菜单记录")
	public CommonResult<String> removes(@ApiParam(name="ids",value="业务主键数组,多个业务主键之间用逗号分隔", required = true)@RequestParam String...ids) throws Exception{
		tenantIgnoreMenuManager.removeByIds(ids);
		return new CommonResult<String>(true, "删除成功");
	}
}
