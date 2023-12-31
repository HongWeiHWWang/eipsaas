package com.hotent.portal.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.*;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.sys.persistence.manager.SysDataSourceDefManager;
import com.hotent.sys.persistence.model.SysDataSourceDef;

/**
 * 
 * <pre>
 * 描述：portal_sys_datasorce_def管理
 * 构建组：x7
 * 作者:liyj_aschs
 * 邮箱:liyj_aschs@jee-soft.cn
 * 日期:2018-07-04 下午3:29:34
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@RestController
@RequestMapping("/sys/sysDataSourceDef/v1")
@Api(tags="数据源连接池管理")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class SysDataSourceDefController extends BaseController<SysDataSourceDefManager, SysDataSourceDef> {
	@Resource
	SysDataSourceDefManager sysDataSourceDefManager;
	

	@RequestMapping(value="getAll", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取所有数据池", httpMethod = "GET", notes = "获取所有数据池")
	public List<SysDataSourceDef> getAll() throws Exception {
		List<SysDataSourceDef> list = sysDataSourceDefManager.list();
		return list;
	}
	
	@RequestMapping(value="listJson", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "数据源列表", httpMethod = "POST", notes = "数据源列表")
	public PageList<SysDataSourceDef> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<SysDataSourceDef> queryFilter) throws Exception {
		return sysDataSourceDefManager.query(queryFilter);
	}

	@RequestMapping(value="get", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据id获取连接池", httpMethod = "GET", notes = "根据id获取连接池")
	public SysDataSourceDef get(@ApiParam(name="id",value="通用查询对象")@RequestParam String id) throws Exception {
		return sysDataSourceDefManager.getById(id);
	}
}
