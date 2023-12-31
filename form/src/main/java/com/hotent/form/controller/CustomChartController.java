package com.hotent.form.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.datasource.DatabaseContext;
import com.hotent.base.datasource.DatabaseSwitchResult;
import com.hotent.base.exception.BaseException;
import com.hotent.base.feign.PortalFeignService;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.form.model.CustomChart;
import com.hotent.form.persistence.manager.CustomChartManager;
import com.hotent.table.datasource.DataSourceUtil;
import com.hotent.table.meta.impl.BaseTableMeta;
import com.hotent.table.model.Column;
import com.hotent.table.model.Table;
import com.hotent.table.model.impl.DefaultColumn;
import com.hotent.table.model.impl.DefaultTable;
import com.hotent.table.util.MetaDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * <pre> 
 * 描述：自定义对图表 控制器类
 * 构建组：x7
 * 作者:heyf
 * 邮箱:heyf@jee-soft.cn
 * 日期:2019-07-24 10:46:14
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@RestController
@RequestMapping("/form/customChart/v1")
@Api(tags="自定义图表")
@ApiGroup(group= {ApiGroupConsts.GROUP_FORM})
public class CustomChartController extends BaseController<CustomChartManager, CustomChart>{
	@Resource
	PortalFeignService  portalFeignService;
	@Resource
	DatabaseContext databaseContext;
	
	/**
	 * 自定义对图表列表(分页条件查询)数据
	 * @param request
	 * @return
	 * @throws Exception 
	 * PageJson
	 * @exception 
	 */
	@PostMapping("/list")
	@ApiOperation(value="自定义对图表数据列表", httpMethod = "POST", notes = "获取自定义对图表列表")
	public PageList<CustomChart> list(@ApiParam(name="queryFilter",value="查询对象")@RequestBody QueryFilter<CustomChart> queryFilter) throws Exception{
		return super.query(queryFilter);
	}
	
	/**
	 * 自定义对图表明细页面
	 * @param id
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/get/{id}")
	@ApiOperation(value="自定义对图表数据详情",httpMethod = "GET",notes = "自定义对图表数据详情")
	public CustomChart get(@ApiParam(name="id",value="业务对象主键", required = true)@PathVariable String id) throws Exception{
		return super.getById(id);
	}
	
    /**
	 * 新增自定义对图表
	 * @param customChart
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@PostMapping(value="save")
	@ApiOperation(value = "新增,更新自定义对图表数据", httpMethod = "POST", notes = "新增,更新自定义对图表数据")
	public CommonResult<String> save(@ApiParam(name="customChart",value="自定义对图表业务对象", required = true)@RequestBody CustomChart customChart) throws Exception{
		CustomChart chartByAlias = baseService.getChartByAlias(customChart.getAlias());
		String msg = "添加自定义对图表成功";
		if(StringUtil.isEmpty(customChart.getId())){
            if(chartByAlias != null){
                return new CommonResult<>(false,customChart.getAlias()+"该别名已存在，请更改！");
            }
			baseService.create(customChart);
		}else{
            if(chartByAlias != null && !chartByAlias.getId().equals(customChart.getId())){
                return new CommonResult<>(false,customChart.getAlias()+"该别名已存在，请更改！");
            }
			baseService.update(customChart);
			 msg = "更新自定义对图表成功";
		}
		return new CommonResult<>(msg);
	}
	
	/**
	 * 删除自定义对图表记录
	 * @param id
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="remove/{id}")
	@ApiOperation(value = "删除自定义对图表记录", httpMethod = "DELETE", notes = "删除自定义对图表记录")
	public CommonResult<String>  remove(@ApiParam(name="id",value="业务主键", required = true)@PathVariable String id) throws Exception{
		return super.deleteById(id);
	}
	
	/**
	 * 批量删除自定义对图表记录
	 * @param ids
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="/removes")
	@ApiOperation(value = "批量删除自定义对图表记录", httpMethod = "DELETE", notes = "批量删除自定义对图表记录")
	public CommonResult<String> removes(@ApiParam(name="ids",value="业务主键数组,多个业务主键之间用逗号分隔", required = true)@RequestParam String...ids) throws Exception{
		baseService.removeByIds(ids);
		return new CommonResult<String>(true, "删除成功");
	}
	
	@PostMapping(value="/getListData")
	@ApiOperation(value = "获取自定义图表数据", httpMethod = "POST", notes = "获取自定义图表数据")
	public Object getListData(@ApiParam(name="alias",value="图表别名", required = true)@RequestParam String alias,
			                  @ApiParam(name="filter",value="查询对象")@RequestBody QueryFilter<?> filter) throws Exception {

		CustomChart customDialog = baseService.get(alias);
		Object result = null;
        try(DatabaseSwitchResult dResult = databaseContext.setDataSource(customDialog.getDsalias())) {
        	result = baseService.getListData(customDialog, filter, dResult.getDbType());
		} catch(Exception e) {
			throw new BaseException(e.getMessage());
		}
		return  result;
	}

	@PostMapping(value="getTable")
	@ApiOperation(value = "获取设置列数据", httpMethod = "POST", notes = "获取设置列数据")
	public ObjectNode getTable(@ApiParam(name="param",value="设置列参数", required = true) @RequestBody ObjectNode object) throws Exception {
		String dsalias = object.get("dsalias").asText();
		String isTable = object.get("isTable").asText();
		ObjectNode result = JsonUtil.getMapper().createObjectNode();
		try(DatabaseSwitchResult dResult = databaseContext.setDataSource(dsalias)){
			Table table = null;
			// 表
			if (isTable.equals("1")) {
				String objName = object.get("objName").asText();
				BaseTableMeta baseTableMeta = MetaDataUtil.getBaseTableMetaAfterSetDT(dResult.getDbType());// 获取表操作元
				table = baseTableMeta.getTableByName(objName);
			} else {
				String diySql = object.get("diySql").asText();
				table = initMetafield(dsalias,diySql);
			}
			result.set("table", JsonUtil.toJsonNode(table));
			return result;
		} catch(Exception e) {
			throw new BaseException(e.getMessage());
		}
	}

	/**
	 * 自定义sql返回的字段
	 *
	 * @param querySqldef
	 * @return List<QueryMetafield>
	 * @exception
	 * @since 1.0.0
	 */
	private Table initMetafield(String DsName, String sql) {
		List<Column> list = new ArrayList<Column>();
		Table table =new DefaultTable();
		JdbcTemplate jdbcTemplate = null;
		try {
			jdbcTemplate = DataSourceUtil.getJdbcTempByDsAlias(DsName);
		} catch (Exception e) {
			throw new RuntimeException(e.getCause());
		}
		SqlRowSet srs = jdbcTemplate.queryForRowSet(sql);
		SqlRowSetMetaData srsmd = srs.getMetaData();
		// 列从1开始算
		for (int i = 1; i < srsmd.getColumnCount() + 1; i++) {
			String cn = srsmd.getColumnName(i).toUpperCase();
			String ctn = srsmd.getColumnTypeName(i);
			Column field = new DefaultColumn();
			field.setFieldName(cn);
			field.setComment(cn);
			field.setColumnType(simplifyDataType(ctn));
			list.add(field);
		}
		table.setColumnList(list);
		return table;

	}

	/**
	 * 把数据库对应的字段类型 简化成 四种基本的数据库字段类型（varchar,number,date,text）
	 *
	 * @param type
	 * @return String
	 * @exception
	 * @since 1.0.0
	 */
	private String simplifyDataType(String type) {
		type = type.toLowerCase();

		String number = "int";
		String date = "date";
		String text = "text";
		String varchar = "varchar";

		if (varchar.contains(type)) {
			return Column.COLUMN_TYPE_VARCHAR;
		}
		if (text.contains(type)) {
			return Column.COLUMN_TYPE_VARCHAR;
		}
		if (date.contains(type)) {
			return Column.COLUMN_TYPE_DATE;
		}
		if (number.contains(type)) {
			return Column.COLUMN_TYPE_NUMBER;
		}
		return type;
	}
	
}
