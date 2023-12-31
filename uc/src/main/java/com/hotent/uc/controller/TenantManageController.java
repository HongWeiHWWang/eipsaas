package com.hotent.uc.controller;

import java.util.ArrayList;
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

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.exception.BaseException;
import com.hotent.base.feign.BpmRuntimeFeignService;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.FieldRelation;
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
import com.hotent.uc.model.User;
import com.hotent.uc.util.ContextUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * <pre> 
 * 描述：租户管理 控制器类
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2020-04-17 10:56:07
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@RestController
@RequestMapping(value="/uc/tenantManage/v1")
@Api(tags="租户管理 ")
@ApiGroup(group= {ApiGroupConsts.GROUP_UC})
public class TenantManageController extends BaseController<TenantManageManager,TenantManage>{
	@Resource
	TenantManageManager tenantManageManager;
	@Resource
	TenantTypeManager tenantTypeManager;
	@Resource
	TenantAuthManager tenantAuthManager;
	@Resource
	BpmRuntimeFeignService bpmRuntimeFeignService;
	
	/**
	 * 租户管理 列表(分页条件查询)数据
	 * @param request
	 * @return
	 * @throws Exception 
	 * PageJson
	 * @exception 
	 */
	@PostMapping("/listJson")
	@ApiOperation(value="租户管理 数据列表", httpMethod = "POST", notes = "获取租户管理 列表")
	public PageList<TenantManage> list(@ApiParam(name="queryFilter",value="查询对象")@RequestBody QueryFilter queryFilter) throws Exception{
		User user = ContextUtil.getCurrentUser();
		if(!user.isAdmin()){
			//处理管理员数据权限
			List<String> authTypeIds = new ArrayList<String>();
			List<String> authTenantIds = new ArrayList<String>();
			List<TenantAuth> auths = tenantAuthManager.getByUserId(null, null, user.getId());
			if(BeanUtils.isNotEmpty(auths)){
				for (TenantAuth tenantAuth : auths) {
					if(StringUtil.isEmpty(tenantAuth.getTenantId())){
						authTypeIds.add(tenantAuth.getTypeId());
					}else{
						authTenantIds.add(tenantAuth.getTenantId());
					}
				}
			}
			if(BeanUtils.isEmpty(authTypeIds) && BeanUtils.isEmpty(authTenantIds) ){
				authTypeIds.add("0");
				authTenantIds.add("0");
			}
			if(BeanUtils.isNotEmpty(authTypeIds)){
				queryFilter.addFilter("TYPE_ID_", authTypeIds, QueryOP.IN, FieldRelation.OR, "auth");
			}
			if(BeanUtils.isNotEmpty(authTenantIds)){
				queryFilter.addFilter("ID_", authTenantIds, QueryOP.IN, FieldRelation.OR, "auth");
			}
		}
		PageList<TenantManage> pageList = tenantManageManager.queryWithType(queryFilter);
		//处理租户类型名称
//		if(BeanUtils.isNotEmpty(pageList) && pageList.getRows().size()>0){
//			List<TenantType> types = tenantTypeManager.getAll();
//			Map<String,String> typeNameMaps = new HashMap<String,String>();
//			for (TenantType tenantType : types) {
//				typeNameMaps.put(tenantType.getId(), tenantType.getName());
//			}
//			for (TenantManage tenant : pageList.getRows()) {
//				tenant.setTypeName(typeNameMaps.get(tenant.getTypeId()));
//			}
//		}
		return pageList;
	}
	
	/**
	 * 租户管理 明细页面
	 * @param id
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/getJson")
	@ApiOperation(value="租户管理 数据详情",httpMethod = "GET",notes = "租户管理 数据详情")
	public TenantManage get(@ApiParam(name="id",value="业务对象主键", required = true)@RequestParam String id) throws Exception{
		return tenantManageManager.get(id);
	}
	
    /**
	 * 新增租户管理 
	 * @param tenantManage
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@PostMapping(value="save")
	@ApiOperation(value = "新增,更新租户管理 数据", httpMethod = "POST", notes = "新增,更新租户管理 数据")
	public CommonResult<String> save(@ApiParam(name="tenantManage",value="租户管理 业务对象", required = true)@RequestBody TenantManage tenantManage) throws Exception{
		String msg = "添加租户管理 成功";
		if(StringUtil.isEmpty(tenantManage.getId())){
			tenantManageManager.create(tenantManage);
		}else{
			if(TenantManage.STATUS_DISABLED.equals(tenantManage.getStatus())) {
				TenantManage oldManage = tenantManageManager.get(tenantManage.getId());
				if(TenantManage.STATUS_ENABLE.equals(oldManage.getStatus())) {
					//禁用
					String tenantId = tenantManage.getId();
					List<ObjectNode> tasList = bpmRuntimeFeignService.getTaskListByTenantId(tenantId);
					if(BeanUtils.isNotEmpty(tasList)) {
						if(tasList.size() >0) {
							throw new BaseException("该租户仍有任务未完成，不允许禁用！");
						}
					}
				}
			}
			tenantManageManager.update(tenantManage);
			UpdateWrapper wrapper = new UpdateWrapper();
			wrapper.set("type_id_",tenantManage.getTypeId());
			wrapper.eq("tenant_id_",tenantManage.getId());
			tenantAuthManager.update(wrapper);
			msg = "更新租户管理 成功";
		}
		return new CommonResult<String>(true,msg,tenantManage.getId());
	}
	
	/**
	 * 批量删除租户管理 记录
	 * @param ids
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="/remove")
	@ApiOperation(value = "批量删除租户管理 记录", httpMethod = "DELETE", notes = "批量删除租户管理 记录")
	public CommonResult<String> removes(@ApiParam(name="ids",value="业务主键数组,多个业务主键之间用逗号分隔", required = true)@RequestParam String...ids) throws Exception{
		tenantManageManager.removeByIds(ids);
		return new CommonResult<String>(true, "删除成功");
	}
	
	@RequestMapping(value="checkCode", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "检查租户类型编码是否存在", httpMethod = "GET", notes = "检查租户类型编码是否存在")
	public boolean checkCode(@ApiParam(name="code",value="租户类型编码") @RequestParam String code) throws Exception{
		TenantManage tenant = tenantManageManager.getByCode(code);
		return  BeanUtils.isNotEmpty(tenant);
	}
	
	@RequestMapping(value="getTenantByCode", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "通过编码获取租户类型", httpMethod = "GET", notes = "通过编码获取租户类型")
	public TenantManage getTenantByCode(@ApiParam(name="code",value="租户类型编码") @RequestParam String code) throws Exception{
		return tenantManageManager.getByCode(code);
	}
}
