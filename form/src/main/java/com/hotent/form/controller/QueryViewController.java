package com.hotent.form.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.Base64;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.form.model.QuerySqldef;
import com.hotent.form.model.QueryView;
import com.hotent.form.param.QueryViewEditVo;
import com.hotent.form.persistence.manager.FormTemplateManager;
import com.hotent.form.persistence.manager.QueryMetafieldManager;
import com.hotent.form.persistence.manager.QuerySqldefManager;
import com.hotent.form.persistence.manager.QueryViewManager;
import com.hotent.uc.api.impl.var.IContextVar;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import poi.util.ExcelUtil;

/**
 * 
 * 
 * <pre>
 * 描述：自定义查询视图列表控制器
 * 作者：zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2019年7月31日 上午9:54:06 
 * 版权：广州宏天软件股份有限公司版权所有
 * </pre>
 */
@RestController
@RequestMapping("/form/query/queryView")
@Api(tags="自定义查询")
@ApiGroup(group= {ApiGroupConsts.GROUP_FORM})
public class QueryViewController extends BaseController<QueryViewManager, QueryView> {
	@Resource
	QueryViewManager queryViewManager;
	@Resource
	FormTemplateManager bpmFormTemplateManager;
	@Resource
	QuerySqldefManager querySqldefManager;
	@Resource
	QueryMetafieldManager queryMetafieldManager;

	@RequestMapping(value="listJson", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "自定义SQL视图列表(分页条件查询)数据", httpMethod = "POST", notes = "自定义SQL视图列表(分页条件查询)数据")	
	public @ResponseBody PageList<QueryView> listJson(@ApiParam(name = "queryFilter", value = "通用查询对象") @RequestBody QueryFilter queryFilter,@ApiParam(name = "sqlAlias", value = "sql别名") @RequestParam String sqlAlias) throws Exception {
		queryFilter.addFilter("SQL_ALIAS_", sqlAlias, QueryOP.EQUAL);
		return queryViewManager.query(queryFilter);
	}

	@RequestMapping(value="getJson", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "自定义SQL视图明细", httpMethod = "GET", notes = "自定义SQL视图明细")
	public QueryView getJson(@ApiParam(name ="id", value = "id") @RequestParam Optional<String> id,
							 @ApiParam(name ="sqlAlias", value = "sql别名") @RequestParam Optional<String> sqlAlias,
							 @ApiParam(name ="alias", value = "alias") @RequestParam Optional<String> alias) throws Exception {
		String id_ = id.orElse("");
		String sqlAlias_ = sqlAlias.orElse("");
		String alias_ = alias.orElse("");
		QueryView queryView = null;
		if (StringUtil.isNotEmpty(id_)) {
			queryView = queryViewManager.get(id_);
		}else if(StringUtil.isNotEmpty(sqlAlias_)&&StringUtil.isNotEmpty(alias_)) {
			queryView = queryViewManager.getBySqlAliasAndAlias(sqlAlias_, alias_);
		}
		return queryView;
	}

	
	@RequestMapping(value="save",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存自定义SQL视图信息", httpMethod = "POST", notes = "保存自定义SQL视图信息")
	public CommonResult<String> save(@ApiParam(name="queryView",value="保存自定义SQL视图信息", required = true) @RequestBody QueryView queryView) throws Exception{
		String resultMsg = null;
		String id = queryView.getId();
		QueryView byAlias = queryViewManager.getByAlias(queryView.getAlias());
		if (queryView.getRebuildTemp() == (short) 1) {
			queryViewManager.handleTemplate(queryView);
		}
		if (StringUtil.isEmpty(id)) {
			if(byAlias != null){
				return new CommonResult<>(false,queryView.getAlias()+"该别名已存在，请更换！");
			}
			queryView.setId(UniqueIdUtil.getSuid());
			queryViewManager.create(queryView);
			resultMsg = "添加自定义SQL视图成功";
		} else {
			if(byAlias != null && !byAlias.getId().equals(queryView.getId())){
				return new CommonResult<>(false,queryView.getAlias()+"该别名已存在，请更换！");
			}
			queryViewManager.update(queryView);
			resultMsg = "更新自定义SQL视图成功";
		}
		return new CommonResult<String>(true, resultMsg);
	}

	
	@RequestMapping(value="remove",method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除自定义SQL视图记录", httpMethod = "DELETE", notes = "批量删除自定义SQL视图记录")
	public CommonResult<String> remove(@ApiParam(name="ids",value="自定义SQL视图记录ID!多个ID用,分割", required = true)@RequestParam String ids) throws Exception{
		String[] aryIds=null;
		if(!StringUtil.isEmpty(ids)){
			aryIds=ids.split(",");
		}
		queryViewManager.removeByIds(aryIds);
		return new CommonResult<String>(true, "删除自定义SQL视图成功");
	}
	
	@RequestMapping(value="getEditInfo", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "获取编辑页面信息", httpMethod = "GET", notes = "获取编辑页面信息")
	public QueryViewEditVo getEditInfo(@ApiParam(name ="sqlAlias", value = "sqlAlias") @RequestParam String sqlAlias) throws Exception {
		QuerySqldef sqldef= querySqldefManager.getByAlias(sqlAlias);
		sqldef.setMetafields(queryMetafieldManager.getBySqlId(sqldef.getId()));
		List<IContextVar> comVarList = (List<IContextVar>) AppUtil.getBean("queryViewComVarList");
		return new QueryViewEditVo("/system/query/queryViewEdit", sqldef, comVarList);
	}
	

	@RequestMapping(value="data_{sqlAlias}/{alias}", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "数据列表", httpMethod = "POST", notes = "数据列表")
	public PageList getShowData(@PathVariable(value = "sqlAlias") String sqlAlias,
			@PathVariable(value = "alias") String alias,
			@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter queryFilter,
			@ApiParam(name="initSearch",value="是否初始化查询")@RequestParam Optional<Boolean> initSearch) throws Exception {
		return queryViewManager.getShowData(sqlAlias, alias, queryFilter, false, initSearch.orElse(true));
	}

	/**
	 * 获取所有模板
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             ModelAndView
	 */
	@RequestMapping("getTempList")
	public @ResponseBody Object getTempList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return bpmFormTemplateManager.getQueryDataTemplate();
	}

	@RequestMapping(value="export",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "导出数据", httpMethod = "POST", notes = "导出数据")
	public void export(HttpServletResponse response,
			@ApiParam(name="sqlAlias",value="sqlAlias", required = true)@RequestParam String sqlAlias,
			@ApiParam(name="alias",value="alias", required = false)@RequestParam String alias,
			@ApiParam(name="getType",value="getType", required = false)@RequestParam String getType,
			@ApiParam(name="expField",value="expField", required = true)@RequestParam String expField,
			@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter queryFilter,
			@ApiParam(name="initSearch",value="是否初始化查询")@RequestParam Optional<Boolean> initSearch) throws Exception {
		QueryView queryView = queryViewManager.getBySqlAliasAndAlias(sqlAlias, alias);
		PageList pageList = queryViewManager.getShowData(sqlAlias, alias, queryFilter, getType.equals("all"),initSearch.orElse(true));
		expField = Base64.getFromBase64(expField);
		// 拼装exprotMaps
		Map<String, String> exportMaps = new LinkedHashMap<String, String>();
		ArrayNode showJA = (ArrayNode) JsonUtil.toJsonNode(queryView.getShows());
		ObjectNode showJO = JsonUtil.arrayToObject(showJA, "fieldName");
		String[] expFields = expField.split(",");
		for (String str : expFields) {
			ObjectNode node = (ObjectNode) showJO.get(str);
			exportMaps.put(str, node.get("fieldDesc").asText());
		}
		HSSFWorkbook book = ExcelUtil.exportExcel(queryView.getName(), 24, exportMaps, pageList.getRows());
		ExcelUtil.downloadExcel(book, queryView.getName(), response);

	}
	
	@RequestMapping(value="isExist", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "判断是否存在", httpMethod = "GET", notes = "判断是否存在")
	public CommonResult<Boolean> isExist(@ApiParam(name ="id", value = "id") @RequestParam String id,
			@ApiParam(name ="key", value = "key") @RequestParam String key,
			@ApiParam(name ="sqlAlias", value = "sqlAlias") @RequestParam String sqlAlias) throws Exception {
		if (StringUtil.isNotEmpty(key)) {
			QueryView temp = queryViewManager.getBySqlAliasAndAlias(sqlAlias, key);
			if (temp == null) {
				return new CommonResult<Boolean>(true, "获取成功", false);
			}
			return new CommonResult<Boolean>(true, "获取成功", !temp.getId().equals(id));// 如果id跟数据库中用这个别名的对象一样就返回false，反之true
		}
		return new CommonResult<Boolean>(true, "获取成功", false);
	}
	
	@RequestMapping(value="saveTemplate", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "保存视图页面html", httpMethod = "POST", notes = "保存视图页面html")
	public CommonResult<String> saveTemplate(@ApiParam(name="id",value="模板id", required = true)@RequestParam String id,
			@ApiParam(name="templateHtml",value="模板html", required = true)@RequestBody String templateHtml) throws IOException{
		String resultMsg = "保存成功";
		QueryView queryView = queryViewManager.get(id);
		queryView.setTemplate(templateHtml);
		queryViewManager.update(queryView);
		return new CommonResult<String>(true,resultMsg);

	}
}
