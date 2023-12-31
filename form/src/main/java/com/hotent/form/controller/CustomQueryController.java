package com.hotent.form.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import com.hotent.base.util.time.DateUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.datasource.DatabaseContext;
import com.hotent.base.datasource.DatabaseSwitchResult;
import com.hotent.base.exception.BaseException;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.Base64;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.form.model.CustomDialog;
import com.hotent.form.model.CustomQuery;
import com.hotent.form.persistence.manager.CustomDialogManager;
import com.hotent.form.persistence.manager.CustomQueryManager;
import com.hotent.form.vo.CustomQueryControllerVo;
import com.hotent.table.meta.impl.BaseTableMeta;
import com.hotent.table.model.Table;
import com.hotent.table.operator.IViewOperator;
import com.hotent.table.util.MetaDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 自定义查询Controller
 *
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月13日
 */
@RestController
@RequestMapping("/form/customQuery/v1")
@Api(tags = "关联数据")
@ApiGroup(group= {ApiGroupConsts.GROUP_FORM})
@SuppressWarnings({"unchecked", "rawtypes"})
public class CustomQueryController extends BaseController<CustomQueryManager, CustomQuery> {
    @Resource
    CustomDialogManager customDialogManager;
    @Resource
    DatabaseContext databaseContext;

    @RequestMapping(value = "list", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "自定义查询信息列表(分页条件查询)", httpMethod = "POST", notes = "自定义查询信息列表(分页条件查询)")
    public PageList listJson(@ApiParam(name = "queryFilter", value = "通用查询对象") @RequestBody Optional<QueryFilter> queryFilter) throws Exception {
        QueryFilter filter = queryFilter.orElse(QueryFilter.build().withPage(new PageBean(1, Integer.MAX_VALUE)));
        if (BeanUtils.isEmpty(filter.getPageBean())){
            filter.withPage(new PageBean(1,Integer.MAX_VALUE));
        }
    	return baseService.query(filter);
    }


    @RequestMapping(value = "getByAlias", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "通过别名获取自定义查询信息", httpMethod = "POST", notes = "通过别名获取自定义查询信息")
    public CustomQuery getByAlias(@ApiParam(name = "alias", value = "别名") @RequestBody String alias) {
        return baseService.getByAlias(alias);
    }


    @RequestMapping(value = "customQueryGet", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "获取自定义查询信息", httpMethod = "POST", notes = "获取自定义查询信息")
    public CustomQuery get(@ApiParam(name = "id", value = "主键") @RequestBody String id) throws Exception {
        return baseService.get(id);
    }

    @RequestMapping(value = "saveDialogByQuery", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "关联查询添加为对话框", httpMethod = "POST", notes = "关联查询添加为对话框")
    public CommonResult saveDialogByQuery(@ApiParam(name = "json", value = "自定义查询信息json对象") @RequestBody String json) throws Exception {
        CustomDialog customDialog =null;
        String resultMsg;
        if(!StringUtil.isEmpty(json)){
            ObjectNode node=(ObjectNode) JsonUtil.toJsonNode(json);
            String displayfield=node.get("resultfield")+"";//因把关联查询数据信息添加为对话框，关联查询没有显示字段，所有对话框的显示字段也用返回字段
            String conditionfield=node.get("conditionfield")+"";
            String resultfield=node.get("resultfield")+"";
            String sortfield=node.get("sortfield")+"";
            node.remove("displayfield");
            node.remove("conditionfield");
            node.remove("resultfield");
            node.remove("sortfield");
            customDialog=JsonUtil.toBean(node, CustomDialog.class);
            customDialog.setDisplayfield(displayfield);
            customDialog.setConditionfield(conditionfield);
            customDialog.setResultfield(resultfield);
            customDialog.setSortfield(sortfield);
            customDialog.setStyle(new Short("0"));//默认列表形式
            customDialog.setSelectNum(1);//默认单选
        }
        CustomDialog entity = new CustomDialog();
        if (StringUtil.isNotEmpty(customDialog.getAlias())) {
            entity = customDialogManager.getByAlias(customDialog.getAlias());
        }
        if (BeanUtils.isEmpty(entity)) {
            if (customDialogManager.getByAlias(customDialog.getAlias()) != null) {
                return new CommonResult(false, customDialog.getAlias() + "，已存在", null);
            }
            customDialog.setId(UniqueIdUtil.getSuid());
            customDialog.setCreateTime(LocalDateTime.now());
            customDialog.setUpdateTime(DateUtil.getCurrentDate());
            customDialogManager.create(customDialog);
            resultMsg = "添加成功";
        } else {
            customDialog.setId(entity.getId());
            customDialog.setUpdateTime(LocalDateTime.now());
            customDialogManager.update(customDialog);
            resultMsg = "更新成功";
        }
        return new CommonResult(true, resultMsg, null);
    }

    @RequestMapping(value = "saveQueryByDialog", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "对话框添加为关联查询", httpMethod = "POST", notes = "对话框添加为关联查询")
    public CommonResult saveQueryByDialog(@ApiParam(name = "json", value = "自定义查询信息json对象") @RequestBody String json) throws Exception {
        CustomQuery customQuery = queryByDialog(json);
        String resultMsg = null;
        CustomQuery entity = new CustomQuery();
        if (StringUtil.isNotEmpty(customQuery.getAlias())) {
            entity = baseService.getByAlias(customQuery.getAlias());
        }
        if (BeanUtils.isEmpty(entity)) {
            if (baseService.getByAlias(customQuery.getAlias()) != null) {
                return new CommonResult(false, customQuery.getAlias() + "，已存在", null);
            }
            customQuery.setId(UniqueIdUtil.getSuid());
            baseService.create(customQuery);
            resultMsg = "添加成功";
        } else {
            customQuery.setId(entity.getId());
            baseService.update(customQuery);
            resultMsg = "更新成功";
        }
        return new CommonResult(true, resultMsg, null);
    }

    @RequestMapping(value = "save", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "保存自定义查询信息", httpMethod = "POST", notes = "保存自定义查询信息")
    public CommonResult save(@ApiParam(name = "json", value = "自定义查询信息json对象") @RequestBody String json) throws Exception {
        CustomQuery customQuery = getCustomQuery(json);
        String resultMsg = null;
        if (StringUtil.isEmpty(customQuery.getId())) {
            if (baseService.getByAlias(customQuery.getAlias()) != null) {
                return new CommonResult(false, "别名"+customQuery.getAlias() + "，已存在", null);
            }
            customQuery.setId(UniqueIdUtil.getSuid());
            baseService.create(customQuery);
            resultMsg = "添加成功";
        } else {
            baseService.update(customQuery);
            resultMsg = "更新成功";
        }
        return new CommonResult(true, resultMsg, null);
    }

    @RequestMapping(value = "removes", method = RequestMethod.DELETE, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "批量删除自定义查询信息", httpMethod = "DELETE", notes = "批量删除自定义查询信息")
    public CommonResult removes(@ApiParam(name = "id", value = "主键") @RequestParam String ids) throws Exception {
        String[] aryIds = StringUtil.getStringAryByStr(ids);
        baseService.removeByIds(aryIds);
        return new CommonResult(true, "删除自定义查询信息成功", null);
    }

    @RequestMapping(value = "getByDsObjectName", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "获取表或视图列表", httpMethod = "POST", notes = "获取表或视图列表")
    public ArrayNode getByDsObjectName(@ApiParam(name = "vo", value = "") @RequestBody CustomQueryControllerVo vo) throws Exception {
    	return baseService.getTableOrViewByDsName(vo);
    }

    @RequestMapping(value = "getTable", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "获得表对象", httpMethod = "POST", notes = "获得表对象")
    public ObjectNode getTable(@ApiParam(name = "vo", value = "") @RequestBody CustomQueryControllerVo vo) throws Exception {

        ObjectNode result = JsonUtil.getMapper().createObjectNode();

        try(DatabaseSwitchResult dResult = databaseContext.setDataSource(vo.getDsalias())) {
        	Table table = null;
            // 表
            if (vo.getIsTable().equals("1")) {
                BaseTableMeta baseTableMeta = MetaDataUtil.getBaseTableMetaAfterSetDT(dResult.getDbType());// 获取表操作元

                table = baseTableMeta.getTableByName(vo.getObjName());
            } else {
                IViewOperator iViewOperator = MetaDataUtil.getIViewOperatorAfterSetDT(dResult.getDbType());
                table = iViewOperator.getModelByViewName(vo.getObjName());
            }

            result.set("table", JsonUtil.toJsonNode(table));
            return result;
		} catch(Exception e) {
			throw new BaseException(e.getMessage());
		}
    }

    @RequestMapping(value = "doQuery", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "", httpMethod = "POST", notes = "")
    public PageList doQuery(@ApiParam(name = "alias", value = "别名") @RequestParam Optional<String> alias,
                            @ApiParam(name = "queryData", value = "") @RequestBody Optional<String> queryData,
                            @ApiParam(name = "page", value = "") @RequestParam Optional<Integer> page) throws Exception {
        CustomQuery customQuery = baseService.getByAlias(alias.orElse(null));
        if (customQuery == null) {
            return null;
        }
        try (DatabaseSwitchResult dResult = databaseContext.setDataSource(customQuery.getDsalias())){
        	PageList data = baseService.getData(customQuery, queryData.orElse(null), dResult.getDbType(), page.orElse(null), customQuery.getPageSize());
        	return data;
        } catch(Exception e) {
        	throw new BaseException(e.getMessage());
		}
    }
    
    @RequestMapping(value = "doQueryBase64", method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "", httpMethod = "GET", notes = "")
    public PageList doQueryBase64(@ApiParam(name = "alias", value = "别名") @RequestParam String alias,
                            @ApiParam(name = "queryData", value = "") @RequestParam String queryData,
                            @ApiParam(name = "page", value = "") @RequestParam Integer page) throws Exception {
        CustomQuery customQuery = baseService.getByAlias(alias);
        if (customQuery == null) {
            return null;
        }

        if(StringUtil.isNotEmpty(queryData)){
        	queryData = Base64.getFromBase64(queryData);
        	queryData = Base64.getFromBase64(queryData);
        }
        // 切换这次进程的数据源
        try (DatabaseSwitchResult dResult = databaseContext.setDataSource(customQuery.getDsalias())){
        	PageList data = baseService.getData(customQuery, queryData, dResult.getDbType(), page, customQuery.getPageSize());
        	return data;
		} catch(Exception e) {
			throw new BaseException(e.getMessage());
		}
    }


    @RequestMapping(value = "getAll", method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "取得所有的表对象", httpMethod = "GET", notes = "取得所有的表对象")
    public List<CustomQuery> getAll() throws Exception {
        List<CustomQuery> customQuerys = baseService.list();
        return customQuerys;
    }

    @RequestMapping(value = "getQueryPage", method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})
    @ApiOperation(value = "", httpMethod = "GET", notes = "")
    public PageList getQueryPage(@ApiParam(name = "alias", value = "别名") @RequestParam String alias) throws Exception {

        PageList pageList = new PageList<>();
        CustomQuery customQuery = baseService.getByAlias(alias);
        if (customQuery == null) {
            return null;
        }
        try (DatabaseSwitchResult dResult = databaseContext.setDataSource(customQuery.getDsalias())){
            customQuery.setNeedPage(0);
            pageList = (PageList) baseService.getData(customQuery, null, dResult.getDbType(), 1, 20);
        } catch(Exception e) {
        	throw new BaseException(e.getMessage());
        }
        return pageList;
    }
    private CustomQuery queryByDialog(String json) throws Exception{
        CustomQuery customQuery = null;
        ObjectNode node = (ObjectNode) JsonUtil.toJsonNode(json);
        String conditionfield = node.get("conditionfield").toString();
        String resultfield = node.get("displayfield").toString();//拿对话框的显示字段作为关联数据的返回字段
        String sortfield = node.get("sortfield").toString();
        String header = JsonUtil.getString(node, "header");
        node.remove("conditionfield");
        node.remove("resultfield");
        node.remove("sortfield");
        customQuery = JsonUtil.toBean(node, CustomQuery.class);
        customQuery.setConditionfield(conditionfield);
        customQuery.setResultfield(resultfield);
        customQuery.setSortfield(sortfield);
        if(StringUtil.isNotEmpty(header)) {
        	customQuery.setHeader(header);
        }
        customQuery.setPageSize(10);//从对话框添加关联数据默认显示的数据为10个
        return customQuery;
    }

    private CustomQuery getCustomQuery(String json) throws Exception{
        CustomQuery customQuery = null;
        ObjectNode node = (ObjectNode) JsonUtil.toJsonNode(json);
        String conditionfield = node.get("conditionfield").toString();
        String resultfield = node.get("resultfield").toString();
        String sortfield = node.get("sortfield").toString();
        String header = JsonUtil.getString(node, "header");
        node.remove("conditionfield");
        node.remove("resultfield");
        node.remove("sortfield");
        customQuery = JsonUtil.toBean(node, CustomQuery.class);
        customQuery.setConditionfield(conditionfield);
        customQuery.setResultfield(resultfield);
        customQuery.setSortfield(sortfield);
        if(StringUtil.isNotEmpty(header)) {
        	customQuery.setHeader(header);
        }
        return customQuery;
    }
}
