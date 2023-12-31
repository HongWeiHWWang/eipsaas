package com.hotent.form.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.Direction;
import com.hotent.base.query.FieldSort;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bo.instance.BoDataHandler;
import com.hotent.bo.model.BoData;
import com.hotent.bo.model.BoDef;
import com.hotent.bo.model.BoEnt;
import com.hotent.bo.persistence.manager.BoAttributeManager;
import com.hotent.bo.persistence.manager.BoDefManager;
import com.hotent.bo.persistence.manager.BoEntManager;
import com.hotent.form.model.FormMeta;
import com.hotent.form.persistence.manager.FormMetaManager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * bo定义控制器
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月18日
 */
@RestController
@RequestMapping("/bo/def/v1/")
@Api(tags="业务对象定义")
@ApiGroup(group= {ApiGroupConsts.GROUP_FORM})
public class BoDefController extends BaseController<BoDefManager, BoDef> {
	@Resource
	BoEntManager boEntManager;
	@Resource
	BoDataHandler boDataHandler;
	@Resource
	FormMetaManager  bpmFormDefManager;
	@Resource
	BoAttributeManager boAttributeManager;
	
	@RequestMapping(value="list", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "获取bo定义列表（带分页信息）", httpMethod = "POST", notes = "获取bo定义列表")
	public PageList<BoDef> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<BoDef> queryFilter) throws Exception {
		if(BeanUtils.isEmpty(queryFilter.getSorter())){
			queryFilter.getSorter().add(new FieldSort("createTime", Direction.DESC));
		}
		return super.query(queryFilter);
	}
	
	@RequestMapping(value="detail",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取bo定义详情", httpMethod = "GET", notes = "获取bo定义详情")
	public BoDef detail(@ApiParam(name="alias",value="bo定义别名", required = true) @RequestParam String alias) throws Exception{
		return baseService.getPureByAlias(alias);
	}
	
	@RequestMapping(value="getJson",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取bo定义详情", httpMethod = "GET", notes = "获取bo定义详情")
	public ObjectNode getJson(@ApiParam(name="id",value="id", required = true) @RequestParam String id) throws Exception{
		return baseService.getBoDefDetails(id);
	}
	
	@RequestMapping(value="save",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "添加bo定义", httpMethod = "POST", notes = "添加bo定义")
	public CommonResult<String> save(@ApiParam(name="json",value="bo定义的json数据", required = true) @RequestBody String json) throws Exception{
		baseService.save(json);
		return new CommonResult<String>("添加成功");
	}
	
	@RequestMapping(value="remove",method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "删除bo定义", httpMethod = "DELETE", notes = "删除bo定义")
	public CommonResult<Integer> remove(@ApiParam(name="alias",value="bo定义别名", required = true) @RequestParam String alias) throws Exception{
		int removeItems = baseService.removeByAlias(alias);
		if(removeItems > 0) {
			return new CommonResult<Integer>(true, "删除成功", removeItems);
		}
		else {
			return new CommonResult<Integer>(false, "删除失败", removeItems);
		}
	}
	
	@RequestMapping(value="removes",method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除bo定义", httpMethod = "DELETE", notes = "批量删除bo定义")
	public CommonResult<String> batchRemove(@ApiParam(name="ids",value="bo主键集合", required = true) @RequestParam String...ids) throws Exception{
		List<String> errList = removeBoDefDetails(ids);
		if(errList.size() == 0) {
			return new CommonResult<String>(true, "删除成功");
		}
		return new CommonResult<String>(false, "删除失败"+errList.size()+"个,"+StringUtil.join(errList, ","));
	}
	
	@RequestMapping(value="getBoJson",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据bo别名返回bo对应的JSON数据结构", httpMethod = "GET", notes = "根据bo定义返回bo对应的JSON数据结构")
	public JsonNode getBoJson(@ApiParam(name="alias",value="bo定义别名", required = true) @RequestParam String alias) throws Exception {
		BoData boData = boDataHandler.getByBoDefCode(alias);
		return JsonUtil.toJsonNode(boData.toString());
	}
	
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="deploy",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "发布", httpMethod = "GET", notes = "发布")
	public CommonResult deploy(@ApiParam(name="id",value="id", required = true) @RequestParam String id) throws Exception{
		    BoDef boDef = baseService.getByDefId(id);
			boDef.setDeployed(true);
			baseService.update(boDef);
			return new CommonResult(boDef.getDescription() + "发布成功");
	}
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="setStatus",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "修改状态", httpMethod = "GET", notes = "修改状态")
	public CommonResult setStatus(@ApiParam(name="id",value="id", required = true) @RequestParam String id,
			@ApiParam(name="status",value="状态") @RequestParam String status) throws Exception{
		if(status==null || "".equals(status)){
			status="normal";
		}
		BoDef boDef = baseService.getByDefId(id);
		boDef.setStatus(status);
		baseService.update(boDef);
		return new CommonResult(boDef.getDescription() + "更改状态成功");
	}
	

	@RequestMapping(value="getBOTree",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取BO树形数据", httpMethod = "POST", notes = "获取BO树形数据")
	public ObjectNode getBOTree(@ApiParam(name="ids",value="ids", required = true) @RequestBody String ids) throws Exception {
		return baseService.getBoTreeData(ids);
	}
	
	@RequestMapping(value="getAllBos", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "获取所有bo实体列表", httpMethod = "GET", notes = "获取所有bo实体列表")
	public List<BoEnt> getAllBos() throws Exception {
		QueryFilter<BoEnt> queryFilter = QueryFilter.<BoEnt>build().withPage(new PageBean(1,Integer.MAX_VALUE));
		queryFilter.addFilter("is_create_table_", 1, QueryOP.EQUAL);
		PageList<BoEnt> pageList = boEntManager.query(queryFilter);
		return pageList.getRows();
	}
	
	@RequestMapping(value="getBoEntById", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "获取所有bo实体列表", httpMethod = "GET", notes = "获取所有bo实体列表")
	public BoEnt getBoEntById(@ApiParam(name="id",value="id", required = true) @RequestParam String id) throws Exception {
		return boEntManager.getById(id);
	}

	@RequestMapping(value="getSupportDb", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "获取所有bo实体列表", httpMethod = "GET", notes = "获取所有bo实体列表")
	public boolean getSupportDb(@ApiParam(name="alias",value="bo别名", required = true) @RequestParam String alias) throws Exception {
		return baseService.getPureByAlias(alias).isSupportDb();
	}
	
	@RequestMapping(value="getObject", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "获取bodef", httpMethod = "GET", notes = "获取bodef")
	public BoDef getObject(@ApiParam(name="id",value="bodef主键", required = false) @RequestParam(required = false) String id,@ApiParam(name="key",value="bodef key", required = false) @RequestParam(required = false) String key) throws Exception {
		if (StringUtil.isNotEmpty(id)) {
			BoDef boDef = baseService.get(id);
			return boDef;
		}
		if (StringUtil.isNotEmpty(key)) {
			BoDef boDef = baseService.getByAlias(key);
			return boDef;
		}
		return null;
	}

	@RequestMapping(value = "updateCategory", method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "修改bodef分类", httpMethod = "GET", notes = "修改bodef分类")
	public CommonResult<String> updateCategory(@ApiParam(name="ids",value = "ids", required = true) @RequestParam(required = true) String[] ids,@ApiParam(name="categoryId",value = "categoryId",required = true) @RequestParam(required = true) String categoryId,@ApiParam(value = "categoryName",name="categoryName",required = true) @RequestParam(required = true) String categoryName) throws IOException {
		for(int i=0;i<ids.length;i++){
			BoDef boDef = baseService.get(ids[i]);
			boDef.setCategoryId(categoryId);
			boDef.setCategoryName(categoryName);
			baseService.updateCategory(boDef);
		}
		return new CommonResult<>(true,"设置分类成功");
	}

	@RequestMapping(value = "removeAttr", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "修改boAttribute分类", httpMethod = "POST", notes = "修改bodef分类")
	public CommonResult<String> removeAttr(@ApiParam(name="json",value = "json", required = true) @RequestBody(required = true) String json) throws Exception {
		boAttributeManager.updateAttrStatus(json);
		return new CommonResult<>(true,"字段删除成功");
	}
	@RequestMapping(value = "createTableForm", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "创建表单", httpMethod = "POST", notes = "修改bodef分类")
	public CommonResult<String> createTableForm(@ApiParam(name="json",value = "json", required = true) @RequestBody(required = true) String json) throws Exception {
		String defId = baseService.saveFormData(json);
		return new CommonResult<>(true,defId);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "getBindData", method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "获取绑定数据", httpMethod = "GET", notes = "修改bodef分类")
	public String getBindData(@ApiParam(name="id",value = "id", required = true) @RequestParam(required = true) String id,@ApiParam(name="alias",value = "alias", required = true) @RequestParam(required = true) String alias) throws IOException {
		Map result = baseService.getBindData(id,alias);
		String json = JsonUtil.toJson(result);
		return json;
	}

	@RequestMapping(value = "deleteAttr", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "删除attr数据", httpMethod = "POST", notes = "删除attr数据")
	public CommonResult<String> deleteAttr(@ApiParam(name="id",value = "id", required = true) @RequestParam(required = true) String id) throws IOException {
		boAttributeManager.remove(id);
		return new CommonResult<>(true,"字段删除成功");
	}
	@RequestMapping(value = "recovery", method = RequestMethod.POST ,produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "还原删除数据", httpMethod = "POST", notes = "还原删除数据")
	public CommonResult<String> recovery(@ApiParam(name="json",value = "json", required = true) @RequestBody String json) throws Exception {
		boAttributeManager.recovery(json);
		return new CommonResult<>(true,"字段还原成功");
	}

	@RequestMapping(value = "getHideAttr", method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "获取隐藏字段", httpMethod = "GET", notes = "获取隐藏字段")
	public List getHideAttr(@ApiParam(name = "tableName", value = "表名") @RequestParam String tableName) throws Exception {
		return baseService.getHideAttr(tableName);
	}

	private List<String> removeBoDefDetails(String... ids){
		List<String> delIdList=new ArrayList<>();
		List<String> errList=new ArrayList<>();
		for (String id : ids) {
			//判断该bo是否绑定表单
			List<FormMeta> list = bpmFormDefManager.getByBODefId(id);
			if(list.size()==0){
				//没有绑定，放入待删除数组中
				delIdList.add(id);
			}else{
				List<String> formNameList=new ArrayList<>();
				for (FormMeta bpmFormDef : list) {
					formNameList.add(bpmFormDef.getName());
				}
				BoDef bo=baseService.get(id);
				String msg="【"+bo.getDescription()+"】已绑定表单："+StringUtil.join(formNameList, ",");
				errList.add(msg);
			}
		}
		String[] delIds=delIdList.toArray(new String[delIdList.size()]);

		if(errList.size()==0) {
			baseService.removeByIds(delIds);
			return errList;
		}
		return errList;
	}
}
