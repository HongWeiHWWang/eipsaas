package com.hotent.uc.controller;

import com.hotent.base.query.FieldRelation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.security.core.context.SecurityContextHolder;
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
import com.hotent.base.context.BaseContext;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.uc.manager.TenantAuthManager;
import com.hotent.uc.manager.TenantManageManager;
import com.hotent.uc.manager.TenantTypeManager;
import com.hotent.uc.model.TenantAuth;
import com.hotent.uc.model.TenantManage;
import com.hotent.uc.model.TenantType;
import com.hotent.uc.model.User;
import com.hotent.uc.util.ContextUtil;

/**
 * 
 * <pre> 
 * 描述：租户类型管理 控制器类
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2020-04-17 10:52:37
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@RestController
@RequestMapping(value="/uc/tenantType/v1")
@Api(tags="租户类型管理")
@ApiGroup(group= {ApiGroupConsts.GROUP_UC})
public class TenantTypeController extends BaseController<TenantTypeManager, TenantType>{
	@Resource
	TenantTypeManager tenantTypeManager;
	@Resource
	TenantManageManager tenantManageManager;
	@Resource
	TenantAuthManager tenantAuthManager;
	@Resource
	BaseContext baseContext;
	
	
	/**
	 * 租户类型管理列表(分页条件查询)数据
	 * @param request
	 * @return
	 * @throws Exception 
	 * PageJson
	 * @exception 
	 */
	@PostMapping("/listJson")
	@ApiOperation(value="租户类型管理数据列表", httpMethod = "POST", notes = "获取租户类型管理列表")
	public PageList<TenantType> list(@ApiParam(name="queryFilter",value="查询对象")@RequestBody QueryFilter queryFilter) throws Exception{
//		if(!ContextUtil.getCurrentUser().isAdmin()){
//			List<String> authIds = getAuthId();
//			queryFilter.addFilter("create_by_",baseContext.getCurrentUserId(),QueryOP.IN, FieldRelation.OR);
//			queryFilter.addFilter("id_", authIds, QueryOP.IN,FieldRelation.OR);
//		}
		return tenantTypeManager.query(queryFilter);
	}
	
	/**
	 * 租户类型管理明细页面
	 * @param id
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/getJson")
	@ApiOperation(value="租户类型管理数据详情",httpMethod = "GET",notes = "租户类型管理数据详情")
	public TenantType get(@ApiParam(name="id",value="业务对象主键", required = true)@RequestParam String id) throws Exception{
		return tenantTypeManager.get(id);
	}
	
	/**
	 * 租户类型管理明细页面
	 * @param id
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/getAll")
	@ApiOperation(value="根据状态获取所有租户类型",httpMethod = "GET",notes = "根据状态获取所有租户类型")
	public List<TenantType> getAll(@ApiParam(name="status:enable（启用）、disabled（禁用）",value="状态", required = false)@RequestParam Optional<String> status) throws Exception{
		List<String> authIds = null;
//		if(BeanUtils.isNotEmpty(SecurityContextHolder.getContext().getAuthentication())){
//			User user = ContextUtil.getCurrentUser();
//			if(!user.isAdmin()){
//				authIds = getAuthId();
//			}
//		}
		return tenantTypeManager.getByStatus(status.orElse(null),authIds);
	}
	
    /**
	 * 新增租户类型管理
	 * @param tenantType
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@PostMapping(value="save")
	@ApiOperation(value = "新增,更新租户类型管理数据", httpMethod = "POST", notes = "新增,更新租户类型管理数据")
	public CommonResult<String> save(@ApiParam(name="tenantType",value="租户类型管理业务对象", required = true)@RequestBody TenantType tenantType) throws Exception{
		String msg = "添加租户类型管理成功";
		if(StringUtil.isEmpty(tenantType.getId())){
			TenantType type = tenantTypeManager.getByCode(tenantType.getCode());
			if(BeanUtils.isNotEmpty(type)){
				return new CommonResult<String>(false,"新增失败！租户编码【"+tenantType.getCode()+"】已存在，请输入其他编码。");
			}else{
				tenantType.setStatus("enable");
				tenantTypeManager.create(tenantType);
			}
		}else{
			TenantType type = tenantTypeManager.get(tenantType.getId());
			if(BeanUtils.isNotEmpty(type) && !type.getCode().equals(tenantType.getCode())){
				return new CommonResult<String>(false,"更新失败！不能变更租户编码。");
			}else{
				tenantTypeManager.update(tenantType);
				msg = "更新租户类型管理成功";
			}
		}
		return new CommonResult<String>(msg);
	}
	
	@PostMapping(value="setDefault")
	@ApiOperation(value = "新增,更新租户类型管理数据", httpMethod = "POST", notes = "新增,更新租户类型管理数据")
	public CommonResult<String> setDefault(@ApiParam(name="code",value="租户类型编码", required = true)@RequestParam String code) throws Exception{
		return tenantTypeManager.setDefault(code);
	}
	
	/**
	 * 批量删除租户类型管理记录
	 * @param ids
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="/remove")
	@ApiOperation(value = "批量删除租户类型管理记录", httpMethod = "DELETE", notes = "批量删除租户类型管理记录")
	public CommonResult<String> removes(@ApiParam(name="ids",value="业务主键数组,多个业务主键之间用逗号分隔", required = true)@RequestParam String...ids) throws Exception{
		for (String id : ids) {
			TenantType type = this.get(id);
			if(BeanUtils.isNotEmpty(type) && TenantType.DEFAULT.equals(type.getIsDefault())){
				return new CommonResult<String>(false,"被删除租户类型【"+type.getName()+"】为默认租户类型，不能被删除！");
			}
			List<TenantManage> list = tenantManageManager.getByTypeId(id);
			if(BeanUtils.isNotEmpty(list)){
				return new CommonResult<String>(false,"被删除租户类型【"+type.getName()+"】下已存在租户，不能被删除！");
			}
		}
		tenantTypeManager.removeByIds(ids);
		return new CommonResult<String>(true, "删除成功");
	}
	
	@RequestMapping(value="checkCode", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "检查租户类型编码是否存在", httpMethod = "GET", notes = "检查租户类型编码是否存在")
	public boolean checkCode(@ApiParam(name="code",value="租户类型编码") @RequestParam String code) throws Exception{
		TenantType type = tenantTypeManager.getByCode(code);
		return  BeanUtils.isNotEmpty(type);
	}
	/**
	 * 获取当前用户的权限id
	 * @return
	 */
	private List<String> getAuthId(){
		String currentUserId = baseContext.getCurrentUserId();
		List<String> ids = new ArrayList<String>();
		List<TenantAuth> auths = tenantAuthManager.getByUserId(null, null, currentUserId);
		if(BeanUtils.isNotEmpty(auths)){
			for (TenantAuth tenantAuth : auths) {
				if(StringUtil.isNotEmpty(tenantAuth.getTenantId())){
					ids.add(tenantAuth.getTypeId());
				}
			}
		}
		if(BeanUtils.isEmpty(ids)){
			ids.add("-1");
		}
		return ids;
	}
}
