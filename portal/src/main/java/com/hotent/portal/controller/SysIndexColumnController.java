package com.hotent.portal.controller;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.constants.TenantConstant;
import com.hotent.base.context.BaseContext;
import com.hotent.base.controller.BaseController;
import com.hotent.base.handler.MultiTenantHandler;
import com.hotent.base.handler.MultiTenantIgnoreResult;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.ThreadMsgUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.portal.model.SysIndexColumn;
import com.hotent.portal.persistence.manager.SysIndexColumnManager;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.service.IOrgService;
import com.hotent.uc.api.service.IUserGroupService;
import com.hotent.uc.api.service.IUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 首页栏目 控制器类
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月14日
 */
@RestController
@RequestMapping("/portal/sysIndexColumn/sysIndexColumn/v1/")
@Api(tags="门户栏目")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class SysIndexColumnController extends BaseController<SysIndexColumnManager,SysIndexColumn> {
	@Resource
	SysIndexColumnManager sysIndexColumnService;
	@Resource
	IOrgService sysOrgService;
	@Resource
	IUserService ius;
	@Resource
	IUserGroupService ig;
	@Resource
	BaseContext baseContext;
	@Resource
	MultiTenantHandler multiTenantHandler;

	@RequestMapping(value="listJson", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "列表(分页条件查询)数据", httpMethod = "POST", notes = "列表(分页条件查询)数据")
	public PageList<SysIndexColumn> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<SysIndexColumn> queryFilter) throws Exception {
		PageList<SysIndexColumn> result = null;
		try(MultiTenantIgnoreResult setThreadLocalIgnore = MultiTenantHandler.setThreadLocalIgnore()){
			queryFilter.addFilter(multiTenantHandler.getTenantIdColumn(), Arrays.asList(TenantConstant.PLATFORM_TENANT_ID,baseContext.getCurrentTenantId()), QueryOP.IN);
			result = sysIndexColumnService.query(queryFilter);
		}
		return result;
	}

	@RequestMapping(value="getJson", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "明细页面", httpMethod = "GET", notes = "明细页面")
	public @ResponseBody SysIndexColumn getJson(@ApiParam(name="id", value="主键", required = true)@RequestParam String id) throws Exception {
		if (BeanUtils.isEmpty(id)) {
			return null;
		}
        SysIndexColumn sysIndexColumn = null;
        try(MultiTenantIgnoreResult setThreadLocalIgnore = MultiTenantHandler.setThreadLocalIgnore()) {
            sysIndexColumn = sysIndexColumnService.get(id);
        }
		return sysIndexColumn;
	}
	@RequestMapping(value="getByAlias", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "明细页面", httpMethod = "GET", notes = "明细页面")
	public @ResponseBody SysIndexColumn getByAlias(@ApiParam(name="alias", value="主键", required = true)@RequestParam String alias) throws Exception {
		if (BeanUtils.isEmpty(alias)) {
			return null;
		}
		SysIndexColumn sysIndexColumn = null;
		try(MultiTenantIgnoreResult setThreadLocalIgnore = MultiTenantHandler.setThreadLocalIgnore()){
			sysIndexColumn = sysIndexColumnService.getByColumnAlias(alias);
		}
		return sysIndexColumn;
	}

	@RequestMapping(value="remove", method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除记录", httpMethod = "DELETE", notes = "批量删除记录")
	public CommonResult<String> remove(@ApiParam(name="ids", value="主键", required = true)@RequestParam String ids) throws Exception {
		try { 
			String[] aryIds = StringUtil.getStringAryByStr(ids);
			sysIndexColumnService.removeByIds(aryIds);
			return new CommonResult<>(true, ThreadMsgUtil.getMessage(), null);
		} catch (Exception e) {
			e.printStackTrace();
			return new CommonResult<>(false, "删除栏目失败", null);
		}
	}

	@RequestMapping(value="save", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "添加或更新首页栏目", httpMethod = "POST", notes = "添加或更新首页栏目")
	public CommonResult<String> save(@ApiParam(name="sysIndexColumn", value="首页栏目")@RequestBody SysIndexColumn sysIndexColumn) throws Exception {
		String message = null;
		try {
			String alias = sysIndexColumn.getAlias();
			// 判断别名是否存在。
			Boolean isExist = sysIndexColumnService.isExistAlias(sysIndexColumn.getAlias(), sysIndexColumn.getId());
			if (isExist) {
				message = "栏目别名：[" + alias + "]已存在";
				return new CommonResult<>(false, message, null);
			}
			if (!ContextUtil.getCurrentUser().isAdmin()) {// 把自己的组织设置进去
				String orgId = ig.getGroupsByUserIdOrAccount(ContextUtil.getCurrentUserId()).get(0).getGroupId();
				if (BeanUtils.isNotEmpty(orgId))
					sysIndexColumn.setOrgId(orgId);
			}
			if (StringUtil.isZeroEmpty(sysIndexColumn.getId())) {
				sysIndexColumn.setId(UniqueIdUtil.getSuid());
				sysIndexColumn.setCreateTime(LocalDateTime.now());
				sysIndexColumnService.createAndAuth(sysIndexColumn);
				message = "添加首页栏目成功";
			} else {
				sysIndexColumn.setUpdateTime(LocalDateTime.now());
				sysIndexColumnService.update(sysIndexColumn);
				message = "更新首页栏目成功";
			}
			return new CommonResult<>(true, message, null);
		} catch (Exception e) {
			return new CommonResult<>(false, message+ "," + e.getMessage(), null);
		}
	}

	@RequestMapping(value="getTemp", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "预览模版", httpMethod = "POST", notes = "预览模版")
	public String getTemp(@ApiParam(name="id", value="主键", required = true)@RequestParam String id) throws Exception {
		String html = "";
		SysIndexColumn sysIndexColumn = sysIndexColumnService.get(id);
		if(BeanUtils.isNotEmpty(sysIndexColumn)){
			html = "<div template-alias=\"" + sysIndexColumn.getAlias()
			+ "\"></div>";
		}
		return JsonUtil.toJson(html);
	}

	@RequestMapping(value="getData", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "取得首页栏目明细", httpMethod = "POST", notes = "取得首页栏目明细")
	public String getData(@ApiParam(name="alias", value="别名", required = true)@RequestParam String alias,
			@ApiParam(name="params", value="参数", required = true)@RequestParam String params
			) throws Exception {
		Map<String, Object> param = getParameterValueMap(params);
		String data = "";
		try {
			data = sysIndexColumnService.getHtmlByColumnAlias(alias, param);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return data;
	}

	@RequestMapping(value="parseByAlias", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "取得首页栏目明细", httpMethod = "POST", notes = "取得首页栏目明细")
	public String parseByAlias(@ApiParam(name="alias", value="别名", required = true)@RequestParam String alias,
			@ApiParam(name="params", value="参数", required = true)@RequestParam String params
			) throws Exception {
		Map<String, Object> param = getParameterValueMap(params);
		String data = "";
		try {
			data = sysIndexColumnService.getHtmlByColumnAlias(alias, param);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return data;
	}

	@RequestMapping(value="getDatasByAlias", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "取得首页栏目明细（根据别名批量获取）", httpMethod = "POST", notes = "取得首页栏目明细（根据别名批量获取）")
	public List<SysIndexColumn> getDatasByAlias(@ApiParam(name="alias", value="别名(多个用“,”号隔开)", required = true)@RequestBody String alias) throws Exception {
		List<SysIndexColumn> list = null;
		try(MultiTenantIgnoreResult setThreadLocalIgnore = MultiTenantHandler.setThreadLocalIgnore()){
			list = baseService.batchGetColumnAliases(alias);
		}
		return list;
	}

	private Map<String, Object> getParameterValueMap(String params) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		if (BeanUtils.isEmpty(params))
			return map;
		ObjectNode node = (ObjectNode)JsonUtil.toJsonNode(params);
		Iterator<?> it = node.fields();
		while (it.hasNext()) {
			String key = (String) it.next();
			Object value = node.get(key);
			map.put(key, value);
		}
		return map;
	}
}
