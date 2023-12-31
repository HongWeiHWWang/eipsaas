package com.hotent.portal.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.Base64;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.portal.model.SysIndexColumn;
import com.hotent.portal.model.SysIndexLayout;
import com.hotent.portal.model.SysIndexLayoutManage;
import com.hotent.portal.model.SysIndexMyLayout;
import com.hotent.portal.params.MyLayoutVo;
import com.hotent.portal.persistence.manager.SysIndexColumnManager;
import com.hotent.portal.persistence.manager.SysIndexLayoutManager;
import com.hotent.portal.persistence.manager.SysIndexMyLayoutManager;
import com.hotent.sys.persistence.model.SysMethod;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;



/**
 * 系统首页布局控制器类
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月14日
 */
@RestController
@RequestMapping("/portal/sysIndexMyLayout/v1/")
@Api(tags="我的门户布局")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class SysIndexMyLayoutController extends BaseController<SysIndexMyLayoutManager, SysIndexMyLayout> {
	@Resource
	private SysIndexLayoutManager sysIndexLayoutManager;
	@Resource
	private SysIndexMyLayoutManager sysIndexMyLayoutManager;
	@Resource
	protected SysIndexColumnManager sysIndexColumnManager;
	@Resource
	IUserService ius;
	@Resource
	private SysIndexColumnManager sysIndexColumnService;
	
	@RequestMapping(value="setValid", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "设置一个有效布局", httpMethod = "GET", notes = "设置一个有效布局")
	public CommonResult<String> setValid(@RequestParam String id) throws Exception {
		sysIndexMyLayoutManager.setValid(id);
		return new CommonResult<>(true, "设置成功");
	}
	
	@RequestMapping(value="listJson", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取我的所有布局", httpMethod = "POST", notes = "获取我的所有布局")
	public PageList<SysIndexMyLayout> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<SysIndexMyLayout> queryFilter) throws Exception {
		queryFilter.addFilter("USER_ID", ContextUtil.getCurrentUserId(), QueryOP.EQUAL);
		return sysIndexMyLayoutManager.query(queryFilter);
	}

	@RequestMapping(value="design", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "设计我的首页布局", httpMethod = "GET", notes = "设计我的首页布局")
	public MyLayoutVo design() throws Exception {
		
		IUser user = ContextUtil.getCurrentUser();
		//首页布局
		List<SysIndexLayout> list = sysIndexLayoutManager.list();
		QueryFilter queryFilter= QueryFilter.build().withPage(new PageBean(1, Integer.MAX_VALUE));
		queryFilter.addFilter("IS_PUBLIC", SysIndexColumn.TYPE_PC, QueryOP.EQUAL);
		Map<String,Object>  params  =  new HashMap<String, Object>();
		//首页栏目，true 取出来是已经解析了
		List<SysIndexColumn>  columnList = sysIndexColumnManager.getHashRightColumnList(queryFilter, params, true, SysIndexColumn.TYPE_PC, user);
		//获取展示的布局
		Map<String,List<SysIndexColumn>> columnMap = sysIndexColumnManager.getColumnMap(columnList);
		//获取当前的布局
		SysIndexMyLayout sysIndexMyLayout = sysIndexMyLayoutManager.getLayoutList(user.getUserId(),columnList);
		
		// 解码   columnMap 在sysIndexColumnManager.getColumnMap 中解码
		for (SysIndexLayout sysIndexLayout : list) {
			sysIndexLayout.setTemplateHtml(Base64.getFromBase64(sysIndexLayout.getTemplateHtml()));
		}
		return new MyLayoutVo(list, columnMap, sysIndexMyLayout);
	}
	@RequestMapping(value="designMyLayout", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "设计我的首页布局", httpMethod = "GET", notes = "设计我的首页布局")
	public Map<String,Object> designMyLayout(
			@ApiParam(name="id", value="主键", required = false)@RequestParam String id,
			@ApiParam(name="layoutType", value="部门id", required = false)@RequestParam short layoutType) throws Exception {
		
		Map<String,Object> map = new HashMap<String,Object>();
		IUser user = ContextUtil.getCurrentUser();
		//首页布局
		QueryFilter queryFilter= QueryFilter.build().withPage(new PageBean(1, Integer.MAX_VALUE));
		queryFilter.addFilter("IS_PUBLIC", layoutType, QueryOP.EQUAL);
		Map<String,Object>  params  =  new HashMap<String, Object>();
		//首页栏目，true 取出来是已经解析了
		List<SysIndexColumn>  columnList = sysIndexColumnService.getHashRightColumnList(queryFilter,params, false,layoutType,user);
		//获取展示的布局
		Map<String, List<Map<String, Object>>> columnMap = sysIndexColumnService.getColumnMap2(columnList);
		//获取当前的布局
		SysIndexMyLayout sysIndexMyLayout = sysIndexMyLayoutManager.get(id);
		map.put("columnMap",columnMap);
		map.put("sysIndexLayout", sysIndexMyLayout);
		return map;
	}
	@RequestMapping(value="getSysIndexLayoutList", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取系统首页布局集合", httpMethod = "POST", notes = "获取系统首页布局集合")
	public List<SysIndexLayout> getSysIndexLayoutList() throws Exception {
		List<SysIndexLayout> list = sysIndexLayoutManager.list();
		return list;
	}
	
	@RequestMapping(value="getLayoutList", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取布局集合", httpMethod = "GET", notes = "获取布局集合")
	public List<SysIndexLayout> getLayoutList() throws Exception {
		//首页布局
		List<SysIndexLayout> list = sysIndexLayoutManager.list();
		// 解码   columnMap 在sysIndexColumnManager.getColumnMap 中解码
	for (SysIndexLayout sysIndexLayout : list) {
		sysIndexLayout.setTemplateHtml(Base64.getFromBase64(sysIndexLayout.getTemplateHtml()));
		}
		return list;
	}
	
	@RequestMapping(value="getColumnMap", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取展示的布局", httpMethod = "POST", notes = "获取展示的布局")
	public Map<String, List<SysIndexColumn>> getColumnMap(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<SysIndexColumn> queryFilter,
														  @ApiParam(name="param",value="请求参数")@RequestParam String param
	) throws Exception {
		//获取当前用户
		IUser user = ContextUtil.getCurrentUser();
		// 不限制取值
		Map<String, Object> params =  JsonUtil.toMap(param);
		PageBean pageBean = queryFilter.getPageBean();
		pageBean.setPageSize(Integer.MAX_VALUE);
		queryFilter.setPageBean(pageBean);
		queryFilter.addFilter("IS_PUBLIC", SysIndexColumn.TYPE_PC, QueryOP.EQUAL);
		//首页栏目，true 取出来是已经解析了
		List<SysIndexColumn>  columnList = sysIndexColumnManager.getHashRightColumnList(queryFilter, params, true, SysIndexColumn.TYPE_PC, user);
		//获取展示的布局
		Map<String,List<SysIndexColumn>> columnMap = sysIndexColumnManager.getColumnMap(columnList);
		return columnMap;
	}
	
	@RequestMapping(value="getSysIndexMyLayout", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取当前的布局", httpMethod = "POST", notes = "获取当前的布局")
	public SysIndexMyLayout getSysIndexMyLayout(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<SysIndexColumn> queryFilter,
												@ApiParam(name="param",value="请求参数")@RequestParam String param
	) throws Exception {
		IUser user = ContextUtil.getCurrentUser();
		String userId = user.getUserId();
		// 不限制取值
		Map<String, Object> params =  JsonUtil.toMap(param);
		PageBean pageBean = queryFilter.getPageBean();
		pageBean.setPageSize(Integer.MAX_VALUE);
		queryFilter.setPageBean(pageBean);
		queryFilter.addFilter("IS_PUBLIC", SysIndexColumn.TYPE_PC, QueryOP.EQUAL);
		//首页栏目，true 取出来是已经解析了
		List<SysIndexColumn>  columnList = sysIndexColumnManager.getHashRightColumnList(queryFilter, params, true, SysIndexColumn.TYPE_PC, user);
		//获取当前的布局
		SysIndexMyLayout sysIndexMyLayout = sysIndexMyLayoutManager.getLayoutList(userId,columnList);
		return sysIndexMyLayout;
	}
	
	@RequestMapping(value="saveLayout", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存首页布局", httpMethod = "POST", notes = "保存首页布局")
	public CommonResult<String> saveLayout(@ApiParam(name="designHtml", value="设计模板", required = true)@RequestBody String designHtml	
	) throws Exception {
	try {
			String userId = ContextUtil.getCurrentUserId();
			sysIndexMyLayoutManager.save("", designHtml, userId);
			return new CommonResult<>(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new CommonResult<>(false, e.getMessage());
		}
	}
	
	
	@RequestMapping(value="save", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存我的布局", httpMethod = "POST", notes = "保存首页布局")
	public CommonResult<String> save(@ApiParam(name="designHtml", value="设计模板", required = true)@RequestBody SysIndexMyLayout sysIndexMyLayout	
	) throws Exception {
	try {
			
			sysIndexMyLayout.setUserId(ContextUtil.getCurrentUserId());
			if(StringUtil.isEmpty(sysIndexMyLayout.getId())){
				sysIndexMyLayoutManager.save(sysIndexMyLayout);
				return new CommonResult<>(true, "保存成功");
			}else{
				sysIndexMyLayoutManager.update(sysIndexMyLayout);
				return new CommonResult<>(true, "更新成功");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return new CommonResult<>(false, e.getMessage());
		}
	}
	
	@RequestMapping(value="getMyLayout", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取首页布局模板（默认，base64编码）", httpMethod = "GET", notes = "获取首页布局模板（默认，base64编码）")
	public CommonResult<String> getMyLayout() throws Exception {
		try {
			String currentUserId = ContextUtil.getCurrentUserId();
			SysIndexMyLayout myLayout = sysIndexMyLayoutManager.getByUser(currentUserId);
			if(BeanUtils.isNotEmpty(myLayout)){
				return new CommonResult<String>(true,"获取我的布局成功！",myLayout.getDesignHtml());
			}
		} catch (Exception e) {
			return new CommonResult<String>(false,"获取我的布局失败！","");
		}
		
		return new CommonResult<String>(false,"未设置我的首页布局！","");
	}
	
	@RequestMapping(value="deleteLayout", method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "删除首页布局", httpMethod = "DELETE", notes = "删除首页布局")
	public CommonResult<String> deleteLayout(@ApiParam(name="ids", value="主键", required = true)@RequestParam String ids) throws Exception {
		try {
			sysIndexMyLayoutManager.removeByIds(ids);
			return new CommonResult<>(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new CommonResult<>(false, e.getMessage());
		}
	}
	
	@RequestMapping(value="deleteMyLayout", method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "删除我的布局", httpMethod = "DELETE", notes = "删除我的布局")
	public CommonResult<String> deleteMyLayout() throws Exception {
		try {
			String userId = ContextUtil.getCurrentUserId();
			sysIndexMyLayoutManager.removeByUserId(userId);
			return new CommonResult<>(true, "成功删除我的布局");
		} catch (Exception e) {
			e.printStackTrace();
			return new CommonResult<>(false, e.getMessage());
		}
	}
}
