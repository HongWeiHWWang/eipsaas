package com.hotent.portal.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.constants.TenantConstant;
import com.hotent.base.controller.BaseController;
import com.hotent.base.handler.MultiTenantHandler;
import com.hotent.base.handler.MultiTenantIgnoreResult;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.FieldRelation;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryField;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.portal.model.SysIndexColumn;
import com.hotent.portal.model.SysIndexLayoutManage;
import com.hotent.portal.persistence.manager.SysIndexColumnManager;
import com.hotent.portal.persistence.manager.SysIndexLayoutManageManager;
import com.hotent.portal.persistence.manager.SysIndexLayoutManager;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IOrgService;
import com.hotent.uc.api.service.IUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 布局管理 控制器类
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月14日
 */
@RestController
@RequestMapping("/portal/sysIndexLayoutManage/sysIndexLayoutManage/v1")
@Api(tags="门户布局管理")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class SysIndexLayoutManageController extends BaseController<SysIndexLayoutManageManager, SysIndexLayoutManage>
{
	@Resource
	private SysIndexLayoutManageManager sysIndexLayoutManageService;
	@Resource
	private SysIndexLayoutManager sysIndexLayoutService;
	@Resource
	private SysIndexColumnManager sysIndexColumnService;
	@Resource
	private IOrgService sysOrgService;
	@Resource
	IUserService ius;

	@RequestMapping(value="listJson", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "取得布局管理分页列表", httpMethod = "POST", notes = "取得布局管理分页列表")
	public PageList<SysIndexLayoutManage> listJson(
			@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<SysIndexLayoutManage> queryFilter)throws Exception {
		//如果是超级管理员
		if (BeanUtils.isNotEmpty(ContextUtil.getCurrentUser()) && ContextUtil.getCurrentUser().isAdmin()) {
			List<QueryField> list = queryFilter.getQuerys();
			for (QueryField queryField : list) {
				if ("orgId".equals(queryField.getProperty())) {
					queryField.setGroup("orgAuthId");
					queryField.setRelation(FieldRelation.OR);
				}
			}
			queryFilter.addFilter("ID", "1", QueryOP.EQUAL, FieldRelation.OR,"orgAuthId");
		}
		
		queryFilter.addFilter("IS_DEF", SysIndexLayoutManage.CANCEL_DEF, QueryOP.EQUAL, FieldRelation.AND);
		return sysIndexLayoutManageService.query(queryFilter);
	}
	/**
	 * 获取系统默认的布局
	 * @param queryFilter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="getSysDefaultLayout", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取系统默认的布局", httpMethod = "POST", notes = "获取系统默认的布局")
	public PageList<SysIndexLayoutManage> getSysDefaultLayout(
			@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<SysIndexLayoutManage> queryFilter)throws Exception {
		PageList<SysIndexLayoutManage> query = null;
		try(MultiTenantIgnoreResult setThreadLocalIgnore = MultiTenantHandler.setThreadLocalIgnore()){
			queryFilter.addFilter("IS_DEF",SysIndexLayoutManage.SET_DEF , QueryOP.EQUAL);
			query = sysIndexLayoutManageService.query(queryFilter);
		}
		return query;
	}
	
	@RequestMapping(value="getJson", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "通过id取得布局管理分页列表", httpMethod = "GET", notes = "通过id取得布局管理分页列表")
	public @ResponseBody SysIndexLayoutManage getJson(@ApiParam(name="id",value="主键id")@RequestParam String id
	)throws Exception {
		return sysIndexLayoutManageService.get(id);
	}

	@RequestMapping(value="remove", method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "删除布局管理", httpMethod = "DELETE", notes = "删除布局管理")
	public CommonResult<String> remove(@ApiParam(name="ids", value="主键", required = true)@RequestParam String ids) throws Exception {
		try{
			String[] aryIds = StringUtil.getStringAryByStr(ids);
			sysIndexLayoutManageService.removeByIds(aryIds);
			return new CommonResult<>(true, "删除布局管理成功!", null);
		}catch(Exception ex){
			return new CommonResult<>(false, "删除失败" + ex.getMessage(), null);
		}
	}
	
	@RequestMapping(value="design", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "设计首页布局", httpMethod = "GET", notes = "设计首页布局")
	public Map<String,Object> design(
			@ApiParam(name="id", value="主键", required = false)@RequestParam String id,
			@ApiParam(name="layoutType", value="部门id", required = false)@RequestParam short layoutType) throws Exception {
		
		Map<String,Object> map = new HashMap<String,Object>();
		IUser user = ContextUtil.getCurrentUser();
		//首页布局
		QueryFilter queryFilter= QueryFilter.build().withPage(new PageBean(1, Integer.MAX_VALUE));
		queryFilter.addFilter("IS_PUBLIC", layoutType, QueryOP.EQUAL);
		
		queryFilter.addFilter("tenant_id_", user.getTenantId(), QueryOP.EQUAL, FieldRelation.OR, "tenantId");
		queryFilter.addFilter("tenant_id_", TenantConstant.PLATFORM_TENANT_ID, QueryOP.EQUAL, FieldRelation.OR, "tenantId");
		
//		Map<String,Object>  params  =  new HashMap<String, Object>();
		//首页栏目，true 取出来是已经解析了SysIndexColumn
//		List<SysIndexColumn>  columnList = sysIndexColumnService.getHashRightColumnList(queryFilter,params, false,layoutType,user);
		
		List<SysIndexColumn> columnList = null;
		try(MultiTenantIgnoreResult setThreadLocalIgnore = MultiTenantHandler.setThreadLocalIgnore()){
			columnList = sysIndexColumnService.getAllByLayoutType(queryFilter);
		}
		//获取展示的布局
		Map<String, List<Map<String, Object>>> columnMap = sysIndexColumnService.getColumnMap2(columnList);
		//获取当前的布局
		SysIndexLayoutManage sysIndexLayoutManage = sysIndexLayoutManageService.get(id);
		map.put("columnMap",columnMap);
		map.put("sysIndexLayout", sysIndexLayoutManage);
		return map;
	}
	

//	@RequestMapping(value="changDefault", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
//	@ApiOperation(value = "根据ID获取内容", httpMethod = "GET", notes = "根据ID获取内容")
//	public CommonResult<String> changDefault(@ApiParam(name="id", value="主键", required = true)@RequestParam String id,
//							 @ApiParam(name="action" , value="动作", required = true)@RequestParam String action,
//							 @ApiParam(name="orgId" , value="组织id", required = true)@RequestParam String orgId,
//							 @ApiParam(name="layoutType" , value="布局类型", required = true)@RequestParam short layoutType
//	) throws Exception {
//		String message = null;
//		try {
//			SysIndexLayoutManage sysIndexLayoutManage = sysIndexLayoutManageService.get(id);
//			if("set".equals(action)){
//				sysIndexLayoutManageService.cancelOrgIsDef(orgId,layoutType);
//				sysIndexLayoutManage.setIsDef(SysIndexLayoutManage.SET_DEF);
//				message="设置默认成功";
//			}else{
//				sysIndexLayoutManage.setIsDef(SysIndexLayoutManage.CANCEL_DEF);
//				message="取消默认成功";
//			}
//			sysIndexLayoutManageService.update(sysIndexLayoutManage);
//			return new CommonResult<>(true, message, null);
//		} catch (Exception e) {
//			e.printStackTrace();
//			if("set".equals(action)){
//				message="设置默认失败";
//			}else{
//				message="取消默认失败";
//			}
//			return new CommonResult<>(false, message, null);
//		}
//	}
	
//	@RequestMapping(value="getSysIndexLayouts", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
//	@ApiOperation(value = "获取系统首页布局实体", httpMethod = "GET", notes = "获取系统首页布局实体")
//	public List<SysIndexLayout> getSysIndexLayout() throws Exception {
//		//首页布局
//		List<SysIndexLayout> layoutList = sysIndexLayoutService.getAll();
//		// 解码   columnMap 在sysIndexColumnManager.getColumnMap 中解码
//		for (SysIndexLayout sysIndexLayout : layoutList) {
//			sysIndexLayout.setTemplateHtml(Base64.getFromBase64(sysIndexLayout.getTemplateHtml()));
//		}
//		return layoutList;
//	}
	
//	@RequestMapping(value="getSysIndexColumns", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
//	@ApiOperation(value = "获取首页栏目", httpMethod = "GET", notes = "获取首页栏目")
//	public Map<String, List<SysIndexColumn>> getSysIndexColumns(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter filter,
//								   								@ApiParam(name="paramsJson",value="请求参数")@RequestParam String paramsJson,
//								   								@ApiParam(name="type",value="类型参数")@RequestParam String type
//	) throws Exception {
//		//获取当前用户
//		IUser user = ius.getUserByAccount(current());
//		short layoutType = 0;
//		if(type.equals("pc")){
//			layoutType = SysIndexColumn.TYPE_PC;
//		}else{
//			layoutType = SysIndexColumn.TYPE_MOBILE;
//		}
//		// 不限制取值
//		PageBean page = filter.getPageBean();
//		page.setPageSize(Integer.MAX_VALUE);
//		filter.setPageBean(page);
//		Map<String,Object>  params  =  JsonUtil.toMap(paramsJson);
//		filter.addFilter("IS_PUBLIC", layoutType, QueryOP.EQUAL);
//		//首页栏目，取出来需要解析
//		List<SysIndexColumn>  columnList = sysIndexColumnService.getHashRightColumnList(filter, params, true, layoutType, user);
//		//获取展示的布局
//		Map<String,List<SysIndexColumn>> columnMap = sysIndexColumnService.getColumnMap(columnList);
//		return columnMap;
//	}
	
//	@RequestMapping(value="getSysIndexLayoutManage", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
//	@ApiOperation(value = "获取当前的布局", httpMethod = "GET", notes = "获取当前的布局")
//	public SysIndexLayoutManage getSysIndexLayoutManage(
//										@ApiParam(name="paramsJson",value="请求参数")@RequestParam String paramsJson,
//										@ApiParam(name="type",value="类型参数")@RequestParam String type,
//										@ApiParam(name="id", value="主键", required = true)@RequestParam String id
//	) throws Exception {
//		//获取当前用户
//		IUser user = ius.getUserByAccount(current());
//		short layoutType = 0;
//		if(type.equals("pc")){
//			layoutType = SysIndexColumn.TYPE_PC;
//		}else{
//			layoutType = SysIndexColumn.TYPE_MOBILE;
//		}
//		QueryFilter filter = QueryFilter.build();
//		// 不限制取值
//		PageBean page = new PageBean();
//		page.setPageSize(Integer.MAX_VALUE);
//		filter.setPageBean(page);
//		Map<String,Object>  params  =  JsonUtil.toMap(paramsJson);
//		filter.addFilter("IS_PUBLIC", layoutType, QueryOP.EQUAL);
//		//首页栏目，取出来需要解析
//		List<SysIndexColumn>  columnList = sysIndexColumnService.getHashRightColumnList(filter, params, true, layoutType, user);
//		//获取当前的布局
//		SysIndexLayoutManage sysIndexLayoutManage = sysIndexLayoutManageService.getLayoutList(id, columnList, layoutType);	
//		return sysIndexLayoutManage;
//	}
	
	
//	@RequestMapping(value="getDefaultLayout", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
//	@ApiOperation(value = "获取首页布局模板（默认，base64编码）", httpMethod = "GET", notes = "获取首页布局模板（默认，base64编码）")
//	public CommonResult<String> getDefaultLayout() throws Exception {
//		QueryFilter filter = QueryFilter.build().withDefaultPage();
//		filter.addFilter("IS_DEF", 1,QueryOP.EQUAL );
//		PageList<SysIndexLayoutManage> page = sysIndexLayoutManageService.query(filter);
//		try {
//			if(page.getTotal()>0){
//				return new CommonResult<String>(true,"获取布局成功！",page.getRows().get(0).getDesignHtml());
//			}
//		} catch (Exception e) {
//			return new CommonResult<String>(false,"获取布局失败！","");
//		}
//		
//		return new CommonResult<String>(false,"未设置首页布局！","");
//	}
	
	@RequestMapping(value = "saveLayout", method = RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存首页布局", httpMethod = "POST", notes = "保存首页布局")
	public CommonResult<String> saveLayout(@ApiParam(name="indexLayout", value="数据源对象", required = true) @RequestBody SysIndexLayoutManage indexLayout)
	throws Exception {
		try {
			if (BeanUtils.isEmpty(indexLayout.getId())) {
				indexLayout.setId(UniqueIdUtil.getSuid());
				sysIndexLayoutManageService.create(indexLayout);
			} else {
				sysIndexLayoutManageService.update(indexLayout);
			}
			return new CommonResult<>(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new CommonResult<>(false, e.getMessage(), null);
		}
	}

//	@RequestMapping(value="dialog", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
//	@ApiOperation(value = "选择首页布局模版", httpMethod = "POST", notes = "选择首页布局模版")
//	public List<SysIndexLayoutManage> dialog() throws Exception {
//		List<SysIndexLayoutManage> list= new ArrayList<SysIndexLayoutManage>();
//		return list;
//	}

//	@RequestMapping(value="getLayoutDef", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
//	@ApiOperation(value = "根据组织id和布局类型获取布局管理", httpMethod = "GET", notes = "根据组织id和布局类型获取布局管理")
//	public CommonResult<String> getLayoutDef(@ApiParam(name="orgId", value="组织id", required = true)@RequestParam String orgId,
//											 @ApiParam(name="layoutType", value="布局类型", required = true)@RequestParam Short layoutType
//	) throws Exception {
//		boolean isMaster = false;
//		if (StringUtil.isNotEmpty(orgId)) {
//			SysIndexLayoutManage sysIndexLayoutManage = sysIndexLayoutManageService.getByOrgIdAndLayoutType(orgId,layoutType);
//			if(BeanUtils.isNotEmpty(sysIndexLayoutManage)){
//				isMaster = true;
//			}
//		}
//		return new CommonResult<>(isMaster, "", null);
//	}
	
	@RequestMapping(value="setShareToSub", method=RequestMethod.GET)
	@ApiOperation(value = "设置是否共享给子部门", httpMethod = "GET", notes = "设置是否共享给子部门")
	public CommonResult<String> setShareToSub(@ApiParam(name="id", value="主键", required = true)@RequestParam String id,@ApiParam(name="shareToSub", required = true)@RequestParam Short shareToSub) throws Exception {
		try{
			SysIndexLayoutManage layout = sysIndexLayoutManageService.get(id);
			layout.setShareToSub(shareToSub);
			sysIndexLayoutManageService.update(layout);
			return new CommonResult<>(true, "操作成功", null);
		}catch(Exception ex){
			return new CommonResult<>(false, "操作失败" + ex.getMessage(), null);
		}
	}
	
	@RequestMapping(value="enable", method=RequestMethod.GET)
	@ApiOperation(value = "设置启用/停用", httpMethod = "GET", notes = "设置启用/停用")
	public CommonResult<String> enable(@ApiParam(name="id", value="主键", required = true)@RequestParam String id,@ApiParam(name="enable", required = true)@RequestParam Short enable) throws Exception {
		sysIndexLayoutManageService.setEnable(id, enable);
		return new CommonResult<>(true, "操作成功", null);
	}
}
