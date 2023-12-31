package com.hotent.form.controller;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.util.StringUtil;
import com.hotent.form.model.Form;
import com.hotent.form.model.FormBusSet;
import com.hotent.form.persistence.manager.FormBusManager;
import com.hotent.form.persistence.manager.FormBusSetManager;
import com.hotent.form.persistence.manager.FormManager;
import com.hotent.form.persistence.manager.FormMetaManager;
import com.hotent.form.persistence.manager.FormRightManager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 表单业务保存 控制器类
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月8日
 */
@RestController
@RequestMapping("/form/formBus/v1")
@Api(tags="表单数据处理")
@ApiGroup(group= {ApiGroupConsts.GROUP_FORM})
@SuppressWarnings({ "unchecked", "rawtypes" })
public class FormBusController extends BaseController<FormBusSetManager, FormBusSet>{
	@Resource
	FormManager bpmFormManager;
	@Resource
	FormBusManager formBusManager;
	
	
	@RequestMapping(value="getList", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "获取所有的业务数据", httpMethod = "POST", notes = "获取所有的业务数据")
	public JsonNode getList(@ApiParam(name="formKey",value="表单业务设置Key")@RequestBody   String formKey,
			@ApiParam(name="param",value="上下文的请求")@RequestBody   Map<String,Object> param) throws Exception{
		return formBusManager.getList(formKey,param);
	}
	
	
	@SuppressWarnings("deprecation")
	@RequestMapping(value="getData", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "表单业务设置明细页面", httpMethod = "POST", notes = "表单业务设置明细页面")
	public JsonNode getJson(@ApiParam(name="id",value="表单业务设置Id")@RequestBody String id,
			@ApiParam(name="readonly",value="是否只读")@RequestBody boolean readonly,
			@ApiParam(name="formKey",value="表单业务设置key")@RequestBody String formKey) throws Exception{
		return baseService.getDetail(id, readonly, formKey);
	}
	
	
	@RequestMapping(value="edit", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "编辑页面", httpMethod = "POST", notes = "编辑页面")
	public CommonResult edit(@ApiParam(name="formKey",value="表单业务设置key")@RequestBody String formKey,
			@ApiParam(name="id",value="id")@RequestBody String id,
			@ApiParam(name="parentId_",value="父级ID")@RequestBody String parentId_) throws Exception{
		Map map=new HashMap();
		map.put("url", "/form/form/formBusEdit");
		map.put("id", id);
		map.put("formKey", formKey);
		
		// 树形编辑情况下。添加父ID的值
		if(StringUtil.isNotEmpty(parentId_)){
			String [] parentSet = parentId_.split("\\$");
			if(parentSet.length!=2) return new CommonResult(true, null, map);
			map.put("parentKey",parentSet[0]);
			map.put("parentVal",parentSet[1]);
		}
		
		 return new CommonResult(true, null, map);
	}
	
	@RequestMapping(value="get", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "详细页面", httpMethod = "POST", notes = "详细页面")
	public CommonResult get(@ApiParam(name="formKey",value="表单业务设置key")@RequestBody String formKey,
			@ApiParam(name="id",value="id")@RequestBody String id) throws Exception{
		Map map=new HashMap();
		map.put("url", "/form/form/formBusEdit");
		map.put("id", id);
		map.put("formKey", formKey);
		map.put("readonly", true);
		return new CommonResult(true, null, map);
	}
	
	@RequestMapping("treeList")
	@ResponseBody
	public CommonResult treeList(HttpServletRequest request,HttpServletResponse response,@PathVariable(value = "formKey") String formKey) throws Exception{
		FormBusSet formBusSet = baseService.getByFormKey(formKey);
		Form form = bpmFormManager.getMainByFormKey(formKey);
		if(formBusSet.getIsTreeList()!=1) throw new RuntimeException("改表单不支持树形列表！请修改表单的业务数据保存的设置！");
		Map mv = new HashMap();
		mv.put("url", "/form/form/formBusTreeList");
		mv.put("formName", form.getName());
		mv.put("formKey", formKey);
		mv.put("treeConf", formBusSet.getTreeConf());
		return new CommonResult(true, null, mv);
	}
	
	
	@RequestMapping(value="save", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = " 保存bo数据", httpMethod = "POST", notes = "保存bo数据")
	public CommonResult save(@ApiParam(name="formKey",value="表单业务设置信息Key")@RequestBody  String formKey,
			@ApiParam(name="json",value="boJson数据")@RequestBody  String json) throws Exception{
		formBusManager.saveData(formKey,json);
		return new CommonResult(true, "保存成功", null);
	}
	
	

	@RequestMapping(value="remove", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除表单业务设置记录", httpMethod = "POST", notes = "批量删除表单业务设置记录")
	public CommonResult remove(@ApiParam(name="id",value="主键!多个ID用,分割")@RequestBody String id,
			@ApiParam(name="formKey",value="key")@RequestBody String formKey) throws Exception{
		String[] aryIds=null;
		if(!StringUtil.isEmpty(id)){
			aryIds=id.split(",");
		}
		formBusManager.removeByIds(aryIds,formKey);
		return new CommonResult(true,"删除表单业务设置成功",null);
	}

}
