package com.hotent.portal.controller;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.hotent.base.util.JsonUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryField;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.sys.persistence.manager.SysDataSourceDefManager;
import com.hotent.sys.persistence.manager.SysDataSourceManager;
import com.hotent.sys.persistence.model.SysDataSource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * <pre>
 * 描述：sys_data_source管理
 * 构建组：x7
 * 作者:liyg
 * 邮箱:liygui@jee-soft.cn
 * 日期:2018-07-4-下午3:29:34
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@RestController
@RequestMapping("/sys/sysDataSource/v1")
@Api(tags="数据源管理")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class SysDataSourceController extends BaseController<SysDataSourceManager, SysDataSource> {
	@Resource
	SysDataSourceManager sysDataSourceManager;
	@Resource
	SysDataSourceDefManager sysDataSourceDefManager;

	/**
	 * sys_data_source列表(分页条件查询)数据 TODO方法名称描述
	 * 
	 * @param request
	 * @param reponse
	 * @return
	 * @throws Exception
	 *             PageJson
	 * @exception
	 * @since 1.0.0
	 */
	@RequestMapping(value="listJson", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "数据源列表", httpMethod = "POST", notes = "数据源列表")
	public PageList<SysDataSource> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<SysDataSource> queryFilter) throws Exception {
		return sysDataSourceManager.query(queryFilter);
	}
	
	@RequestMapping(value="getJson", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "数据源信息", httpMethod = "GET", notes = "数据源信息")
	public @ResponseBody SysDataSource getJson(@ApiParam(name="id", value="主键", required = true)@RequestParam String id) throws Exception {
		if(StringUtil.isEmpty(id)){
			return new SysDataSource();
		}
		SysDataSource sysDataSource=sysDataSourceManager.get(id);
		return sysDataSource;
	}


	/**
	 * 保存sys_data_source信息
	 * 
	 * @param request
	 * @param response
	 * @param dataSource
	 * @throws Exception
	 *             void
	 */
	@RequestMapping(value="save", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存数据源信息", httpMethod = "POST", notes = "保存数据源信息")
	public CommonResult<String> save(@ApiParam(name="sysDataSource", value="数据源对象", required = true)@RequestBody SysDataSource sysDataSource) throws Exception {
		
		
		boolean isConnection = sysDataSourceManager.checkConnection(sysDataSource);

		boolean isAliasExist = sysDataSourceManager.isAliasExist(sysDataSource.getAlias());

		if (isAliasExist && StringUtil.isEmpty(sysDataSource.getId())){
			return new CommonResult<String>(false,"别名已存在");
		}

		if(!isConnection){
			return new CommonResult<String>(false, "连接数据库失败，操作失败");
		}
		
		String resultMsg = null;
		if (StringUtil.isEmpty(sysDataSource.getId())) {
			sysDataSource.setId(UniqueIdUtil.getSuid());
			sysDataSourceManager.create(sysDataSource);
			resultMsg = "添加成功,并连接测试通过";
		} else {
			sysDataSourceManager.update(sysDataSource);
			resultMsg = "更新成功,并连接测试通过";
		}
		
		return new CommonResult<String>(resultMsg);
		
	}

	/**
	 * 批量删除sys_data_source记录 TODO方法名称描述
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 *             void
	 * @exception
	 * @since 1.0.0
	 */
	@RequestMapping(value="remove", method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除数据源", httpMethod = "DELETE", notes = "批量删除数据源")
	public CommonResult<String> remove(@ApiParam(name="id", value="主键", required = true)@RequestParam String ids) throws Exception {
		String[] aryIds=StringUtil.getStringAryByStr(ids);
		sysDataSourceManager.removeByIds(Arrays.asList(aryIds));
		return new CommonResult<String>(true, "删除成功");
	}

	/**
	 * 改变当前的数据源
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 *             void
	 * @exception
	 * @since 1.0.0
	 */
	@RequestMapping(value="checkConnection", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "测试连接", httpMethod = "POST", notes = "测试连接")
	public CommonResult<String> checkConnection(@ApiParam(name="sysDataSource", value="数据源对象", required = true)@RequestBody SysDataSource sysDataSource) throws Exception {

		boolean b = sysDataSourceManager.checkConnection(sysDataSource);

		String resultMsg = "";

		if (b) {
			resultMsg = sysDataSource.getName() + ":连接成功";
		} else {
			resultMsg = sysDataSource.getName() + ":连接失败";
		}
		
		return new CommonResult<String>(b,resultMsg);
	}
	
	/**
	 * 获取数据源
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             List<SysDataSource>
	 * @exception
	 * @since 1.0.0
	 */
	@RequestMapping(value="getDataSources", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取数据源", httpMethod = "GET", notes = "获取数据源")
	public List<SysDataSource> getDataSources() throws Exception {
		PageBean pageBean = new PageBean();
		pageBean.setPageSize(Integer.MAX_VALUE);
		QueryFilter queryFilter = QueryFilter.build().withPage(pageBean).withQuery(new QueryField("enabled_", 1, QueryOP.EQUAL));
		PageList<SysDataSource> query = sysDataSourceManager.query(queryFilter);
		// 把默认的数据源也加载进去
		List<SysDataSource> result = query.getRows();
		result.add(sysDataSourceManager.getDefaultDataSource());
		return result;
	}
	
	@RequestMapping(value="getBeanByAlias", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据别名，获取数据库类型", httpMethod = "GET", notes = "根据别名，获取数据库类型")
	public JsonNode getBeanByAlias(@ApiParam(name="alias", value="数据源别名", required = true)@RequestParam String alias) throws Exception {
		return JsonUtil.toJsonNode(sysDataSourceManager.getByAlias(alias));
	}
}
