package com.hotent.runtime.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.groovy.GroovyScriptEngine;
import com.hotent.base.id.IdGenerator;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.runtime.manager.ScriptManager;
import com.hotent.runtime.model.Script;
import com.hotent.runtime.model.TreeEntity;



/**
 * 
 * <pre> 
 * 描述：sys_script管理
 * 构建组：x5-bpmx-platform
 * 作者:helh
 * 邮箱:helh@jee-soft.cn
 * 日期:2014-05-08 14:47:34
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@RestController
@RequestMapping("/runtime/script/v1/")
@Api(tags="脚本管理")
@ApiGroup(group= {ApiGroupConsts.GROUP_BPM})
public class ScriptController extends BaseController<ScriptManager,Script>{

	@Resource
	private ScriptManager scriptManager;
	@Resource
	private IdGenerator idGenerator;
	@Resource
	GroovyScriptEngine groovyScriptEngine;
	
	@RequestMapping(value = "list", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "系统脚本列表(分页条件查询)数据", httpMethod = "POST", notes = "系统脚本列表(分页条件查询)数据")
	public PageList<Script> listJson(@ApiParam(required = true, name = "queryFilter", value = "查询参数对象") @RequestBody QueryFilter queryFilter) throws Exception{
		return scriptManager.query(queryFilter);
	}
	
	@RequestMapping(value = "getCategoryList", method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "获取所有脚本的分类", httpMethod = "GET", notes = "获取所有脚本的分类")
	public List<String> getCategoryList() throws Exception{
		return scriptManager.getDistinctCategory();
	}
	
	@RequestMapping(value = "get", method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "获取脚本明细", httpMethod = "GET", notes = "获取脚本明细")
	public Script get(@ApiParam(required = true, name = "id", value = "脚本id") @RequestParam String id) throws Exception{
		Script script = null;
		if(StringUtil.isNotEmpty(id)){
			script = scriptManager.get(id);
		}
		return script;
	}
	
	@RequestMapping(value="save", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "保存常用脚本", httpMethod = "POST", notes = "保存常用脚本")	
	public CommonResult<String> save(@ApiParam(required = true, name = "script", value = "脚本信息")@RequestBody Script script) throws Exception{
		String resultMsg = null;
		String id = script.getId();
		try {
			if(StringUtil.isEmpty(id)){
				script.setId(idGenerator.getSuid());
				script.setUpdateTime(LocalDateTime.now());
				scriptManager.create(script);
				resultMsg = "添加系统脚本成功";
			}else{
				scriptManager.update(script);
				resultMsg = "更新系统脚本成功";
			}
			return new CommonResult<String>( true, resultMsg);
		} catch (Exception e) {
			return new CommonResult<String>( false, resultMsg+","+e.getMessage());
		}
	}
	
	@RequestMapping(value="remove",method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除系统脚本", httpMethod = "DELETE", notes = "批量删除系统脚本")
	public CommonResult<String> remove(@ApiParam(name="ids",value="流程的测试用例设置id，多个用“,”号分隔", required = true) @RequestParam String ids) throws Exception{
		try {
			String[] aryIds = null;
			if(!StringUtil.isEmpty(ids)){
				aryIds = ids.split(",");
			}
			scriptManager.removeByIds(aryIds);
			return new CommonResult<String>( true, "删除系统脚本成功");
		} catch (Exception e) {
			return new CommonResult<String>( false, "删除系统脚本失败");
		}
	}
	
	
	

	@RequestMapping(value="executeScript", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "执行脚本", httpMethod = "POST", notes = "执行脚本")	
	public CommonResult<String> executeScript(@ApiParam(name="script",value="脚本内容", required = true) @RequestBody String script) throws Exception{
		try {
			Object obj= groovyScriptEngine.executeObject(script,new HashMap<String, Object>());
			String value = "";
			if (BeanUtils.isNotEmpty(obj)) {
				    value = JsonUtil.toJsonNode(obj).toString();
				if(obj instanceof String){
					value = JsonUtil.toJsonNode(obj).textValue();
				}
			}
			return new CommonResult<String>(true,"执行成功！",value);
		} catch (Exception e) {
			return new CommonResult<String>(false,"执行失败，请检查脚本！");
		}
	}
	
	@RequestMapping("getScriptTreeData")
	@ResponseBody
	public List<TreeEntity> getScriptTreeData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<TreeEntity> treeList = getTree();
		if (BeanUtils.isEmpty(treeList))
			treeList = new ArrayList<TreeEntity>();
		return treeList;
	}
	
	private List<TreeEntity> getTree() {
		List<TreeEntity> listResult = new ArrayList<TreeEntity>();
		List<String> categoryList = scriptManager.getDistinctCategory();
		for (String category : categoryList) {
			TreeEntity entity = new TreeEntity(category,category,category,"",TreeEntity.ICON_COMORG);
			listResult.add(entity);
			 
		}
		List<Script> list= scriptManager.getAll();
		for (Script script : list) {
			TreeEntity entity = new TreeEntity(script.getName(),script.getScript(),script.getId(),script.getCategory(),TreeEntity.ICON_COMORG);
			for (TreeEntity pEntity : listResult) {
				if(pEntity.getId().equals(script.getCategory())){
					List<TreeEntity> childrens = pEntity.getChildren();
					if(BeanUtils.isEmpty(childrens)){
						childrens = new ArrayList<TreeEntity>();
					}
					childrens.add(entity);
					pEntity.setChildren(childrens);
				}
			}
		}
		return listResult;
	}
}
