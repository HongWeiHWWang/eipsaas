package com.hotent.form.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bo.persistence.manager.BoDefManager;
import com.hotent.form.model.Form;
import com.hotent.form.model.FormBusSet;
import com.hotent.form.persistence.dao.FormMetaDao;
import com.hotent.form.persistence.manager.FormBusSetManager;
import com.hotent.form.service.FormService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * 表单业务设置 控制器类
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月8日
 */
@RestController
@RequestMapping("/form/formBusSet/v1")
@Api(tags="表单业务设置")
@ApiGroup(group= {ApiGroupConsts.GROUP_FORM})
@SuppressWarnings({ "unchecked", "rawtypes" })
public class FormBusSetController extends BaseController<FormBusSetManager, FormBusSet>{
	@Resource
	FormBusSetManager formBusSetManager;
	@Resource
	FormService formService;
	@Resource
	FormMetaDao bpmFormDefDao;
	@Resource
	BoDefManager bODefManager;

	@RequestMapping(value="getJson", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "表单业务设置明细页面", httpMethod = "POST", notes = "表单业务设置明细页面")
	public  JsonNode getJson(@ApiParam(name="formKey",value="表单业务设置Key")@RequestBody String formKey) throws Exception{
		if(StringUtil.isEmpty(formKey)){
			return null;
		}
		FormBusSet formBusSet=formBusSetManager.getByFormKey(formKey);

		Map object=new HashMap();
		object.put("formBusSet", formBusSet);

		//获取BO
		Form form = (Form) formService.getByFormKey(formKey);
		List<String> boIds = bpmFormDefDao.getBODefIdByFormId(form.getDefId());
		if(boIds.size()>= 1){
			JsonNode boJson =	bODefManager.getBOJson(boIds.get(0));
			object.put("boJson", boJson);
		}
		return JsonUtil.toJsonNode(object);
	}


	@RequestMapping(value="save", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "保存表单业务设置信息", httpMethod = "POST", notes = "保存表单业务设置信息")
	public CommonResult save(@ApiParam(name="formBusSet",value="表单业务设置对象")@RequestBody   FormBusSet formBusSet) throws Exception{
		String resultMsg=null;
		String id=formBusSet.getId();
		boolean rtn=formBusSetManager.isExist(formBusSet);
		if(rtn){
			return new CommonResult(false, "业务设置已存在!", null);
		}
		if(StringUtil.isEmpty(id)){
			formBusSet.setId(UniqueIdUtil.getSuid());
			formBusSetManager.create(formBusSet);
			resultMsg="添加表单业务设置成功";
		}else{
			formBusSetManager.update(formBusSet);
			resultMsg="更新表单业务设置成功";
		}
		return new CommonResult(true, resultMsg, null);
	}

	@RequestMapping(value="remove", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除表单业务设置记录", httpMethod = "POST", notes = "批量删除表单业务设置记录")
	public CommonResult remove(@ApiParam(name="id",value="表单业务设置ID多个ID用,分割")@RequestBody  String id) throws Exception{
		String[] aryIds=null;
		if(!StringUtil.isEmpty(id)){
			aryIds=id.split(",");
		}
		formBusSetManager.removeByIds(aryIds);
		return new CommonResult(true, "删除表单业务设置成功", null);
	}

	@SuppressWarnings("deprecation")
	@RequestMapping(value="createSqlList", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "", httpMethod = "POST", notes = "")
	public ObjectNode treeList(@ApiParam(name="",value="")@RequestBody  String formKey) throws Exception{
		return baseService.getTreeList(formKey);
	}

}
