package com.hotent.form.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bo.context.FormContextThreadUtil;
import com.hotent.bo.model.BoData;
import com.hotent.bo.model.BoDef;
import com.hotent.bo.model.BoEnt;
import com.hotent.bo.model.BoResult;
import com.hotent.bo.persistence.manager.BoDefManager;
import com.hotent.form.model.Form;
import com.hotent.form.model.FormRight;
import com.hotent.form.service.BpmFormRightsService;
import com.hotent.form.service.FormService;
import com.hotent.form.vo.FormRestfulModel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * restful接口控制器类
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月8日
 */
@RestController
@RequestMapping("/form/formServiceController/v1")
@Api(tags="表单服务")
@ApiGroup(group= {ApiGroupConsts.GROUP_FORM})
public class FormServiceController {
	@Resource
	BpmFormRightsService bpmFormRightsService;
	@Resource
	FormService formService;
	@Resource
	BoDefManager boDefManager;
	
	@RequestMapping(value="getMainBOEntByDefAliasOrId", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "根据业务对象别名或id获取主BoEnt", httpMethod = "GET", notes = "根据业务对象别名或id获取主BoEnt")
	public ObjectNode getMainBOEntByDefAliasOrId(@ApiParam(name="alias",value="业务对象别名")@RequestParam String alias,@ApiParam(name="defId",value="业务对象id")@RequestParam String defId) throws IOException{
		return formService.getMainBOEntByDefAliasOrId(alias,defId);
	}
	
	@RequestMapping(value="handlerBoData", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "处理bo数据,boid。空为新增。不为空则更新 ", httpMethod = "POST", notes = "处理bo数据,boid。空为新增。不为空则更新 ")
	public List<BoResult>  handlerBoData(@ApiParam(name="model",value="model")@RequestBody FormRestfulModel model) throws JsonParseException, JsonMappingException, IOException{
        FormContextThreadUtil.putCommonVars("defId", model.getFlowDefId());
        FormContextThreadUtil.putCommonVars("nodeId", model.getNodeId());
        FormContextThreadUtil.putCommonVars("parentDefKey", StringUtil.isEmpty(model.getParentFlowKey())?"":model.getParentFlowKey());
        List<BoResult> handlerBoData = formService.handlerBoData(model.getBoid(), model.getDefId(), model.getBoData(), model.getSaveType());
		return handlerBoData;
	}


	@RequestMapping(value="getBodataByDefCode", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "根据别名获取BoData", httpMethod = "GET", notes = "根据别名获取BoData")
	public BoData getBodataByDefCode(@ApiParam(name="saveMode",value="保存方式")@RequestParam String saveMode, 
			@ApiParam(name="code",value="别名")@RequestParam String code){
		return formService.getBodataByDefCode(saveMode, code);
	}
	
	@RequestMapping(value="getBodataById", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "根据实例ID和bo定义code获取BODATA，只返回两层", httpMethod = "POST", notes = "根据实例ID和bo定义code获取BODATA，只返回两层 ")
	public BoData getBodataById(@ApiParam(name="model",value="model")@RequestBody FormRestfulModel model) throws IOException{
		return formService.getByFormRestfulModel(model);
	}
	
	@RequestMapping(value="getByFormKey", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "根据formkey获取表单", httpMethod = "GET", notes = "根据formkey获取表单")
	public Form getByFormKey(@ApiParam(name="formKey",value="key")@RequestParam String formKey){
		return formService.getByFormKey(formKey);
	}
	
	@RequestMapping(value="getFormExportXml", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "根据formKey 导出表单", httpMethod = "POST", notes = "根据formKey 导出表单")
	public String getFormExportXml(@ApiParam(name="formKeys",value="key集合")@RequestBody String formKeys){
		return formService.getFormExportXml(formKeys);
	}
	
	@RequestMapping(value="getBoDefExportXml", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "根据bodef获得导出用的xml文件", httpMethod = "POST", notes = "根据bodef获得导出用的xml文件")
	public String getBoDefExportXml(@ApiParam(name="bodef",value="bodefJson数据")@RequestBody ObjectNode bodef) throws JsonParseException, JsonMappingException, JAXBException, IOException{
		return formService.getBoDefExportXml(bodef);
	}

	
	@RequestMapping(value="getFormRightExportXml", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "根据FormRight获得导出用的xml文件", httpMethod = "POST", notes = "根据FormRight获得导出用的xml文件")
	public String getFormRightExportXml(@ApiParam(name="bpmFormRights",value="bpmFormRightsJson数据")@RequestBody ObjectNode bpmFormRights) throws JsonParseException, JsonMappingException, JAXBException, IOException{
		return bpmFormRightsService.getFormRightExportXml(bpmFormRights);
	}

	
	@RequestMapping(value="importBo", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "导入 bo", httpMethod = "GET", notes = "导入 bo")
	public CommonResult<String> importBo(@ApiParam(name="bodefXml",value="")@RequestParam String bodefXml){
		  formService.importBo(bodefXml);
		  return new CommonResult<String>("导入成功");
	}

	
	@RequestMapping(value="importBoDef", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "导入bodef对象", httpMethod = "POST", notes = "导入bodef对象")
	public List<BoDef> importBoDef(@ApiParam(name="bos",value="bodef对象")@RequestBody List<ObjectNode> bos) throws JsonParseException, JsonMappingException, IOException{
		List<BoDef> list=new ArrayList<BoDef>();
		for (ObjectNode objectNode : bos) {
			list.add(JsonUtil.toBean(objectNode, BoDef.class));
		}
		return formService.importBoDef(list);
	}

	@RequestMapping(value="importForm", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "导入form", httpMethod = "POST", notes = "导入form")
	public CommonResult<String>  importForm(@ApiParam(name="formfXml",value="formfXml")@RequestBody String formfXml){
		formService.importForm(formfXml);
		return new CommonResult<String>("导入成功");
	}

	
	@RequestMapping(value="importFormRights", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value ="导入formRigths", httpMethod = "POST", notes = "导入formRigths")
	public CommonResult<String> importFormRights(@ApiParam(name="formRightsXml",value="formRightsXml")@RequestBody String formRightsXml){
		bpmFormRightsService.importFormRights(formRightsXml);
		return new CommonResult<String>("导入成功");
	}

	
	@RequestMapping(value="getByFormId", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "根据表单ID取得表单对象。", httpMethod = "GET", notes = "根据表单ID取得表单对象。")
	public Form getByFormId(@ApiParam(name="formId",value="表单ID")@RequestParam String formId){
		return formService.getByFormId(formId);
	}

	
	@RequestMapping(value="getInstPermission", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "获取流程实例表单的权限。", httpMethod = "POST", notes = "获取流程实例表单的权限。")
	public String getInstPermission(@ApiParam(name="model",value="model")@RequestBody FormRestfulModel model){
		return bpmFormRightsService.getInstPermission(model.getFormkey(), model.getUserId(), model.getFlowKey());
	}

	
	@RequestMapping(value="getStartPermission", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "获取流程启动时的表单权限", httpMethod = "POST", notes = "获取流程启动时的表单权限")
	public String getStartPermission(@ApiParam(name="model",value="model")@RequestBody FormRestfulModel model){
		return bpmFormRightsService.getStartPermission(model.getFormkey(), model.getFlowKey(), model.getNodeId(), model.getNextNodeId());
	}

	
	@RequestMapping(value="getPermission", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "获取表单权限", httpMethod = "POST", notes = "获取表单权限")
	public String getPermission(@ApiParam(name="model",value="model")@RequestBody FormRestfulModel model){
		return bpmFormRightsService.getPermission(model.getFormkey(), model.getUserId(), model.getFlowKey(), model.getParentFlowKey(), model.getNodeId(),model.isGlobalPermission());
	}

	

	
	@RequestMapping(value="getFormRigthListByFlowKey", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "根据表单key获得权限列表。", httpMethod = "GET", notes = "根据表单key获得权限列表。")
	public List<FormRight> getFormRigthListByFlowKey(@ApiParam(name="formId",value="表单key")@RequestParam String formId){
		return bpmFormRightsService.getFormRigthListByFlowKey(formId);
	}

	
	@RequestMapping(value="removeFormRights", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "删除表单权限", httpMethod = "GET", notes = "删除表单权限")
	public void removeFormRights(@ApiParam(name="flowKey",value="流程KEY")@RequestParam String flowKey, 
			@ApiParam(name="parentFlowKey",value="parentFlowKey")@RequestParam String  parentFlowKey){
		bpmFormRightsService.removeFormRights(flowKey, parentFlowKey);
	}

	
	@RequestMapping(value="getBodefByAlias", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "通过别名获取bo定义", httpMethod = "GET", notes = "通过别名获取bo定义")
	public BoDef getBodefByAlias(@ApiParam(name="alias",value="别名")@RequestParam String alias) throws IOException{
		return formService.getBoDefByAlias(alias);
	}

	@RequestMapping(value="getBoJosn", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "通过bo定义id获取bo的json格式定义", httpMethod = "GET", notes = "通过bo定义id获取bo的json格式定义")
	public ObjectNode getBoJosn(@ApiParam(name="id",value="bo定义id")@RequestParam String id) throws IOException{
		return formService.getBoJosn(id);
	}

	
	@RequestMapping(value="getBoEntByName", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "通过bo实例name获取boent", httpMethod = "GET", notes = "通过bo实例name获取boent")
	public BoEnt getBoEntByName(@ApiParam(name="name",value="bo实例name")@RequestParam String name){
		return formService.getBoEntByName(name);
	}
	
	
	@RequestMapping(value="removeFormRightByFlowKey", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "删除表单权限", httpMethod = "POST", notes = "删除表单权限")
	public void removeFormRightByFlowKey (@ApiParam(name="model",value="model")@RequestBody FormRestfulModel model){
		bpmFormRightsService.removeFormRightByFlowKey(model.getFlowKey(), model.getParentFlowKey(), model.getPermissionType());
	}

	
	@RequestMapping(value="createFormRight", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "新增表单权限", httpMethod = "POST", notes = "新增表单权限")
	public void createFormRight (@ApiParam(name="bpmFormRight",value="bpmFormRight")@RequestBody ObjectNode bpmFormRight) throws JsonParseException, JsonMappingException, IOException{
		FormRight right=JsonUtil.toBean(bpmFormRight, FormRight.class);
		bpmFormRightsService.createFormRight(right);
	}

	
	@RequestMapping(value="queryFormRight", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "查询表单权限", httpMethod = "POST", notes = "查询表单权限")
	public List<FormRight> queryFormRight (@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter queryFilter){
		return bpmFormRightsService.queryFormRight(queryFilter);
	}
	
	@RequestMapping(value="getFormBoLists", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "获取表单绑定的bo", httpMethod = "GET", notes = "获取表单绑定的bo")
	public List<BoDef> getFormBoLists (@ApiParam(name="formKey",value="表单key")@RequestParam String  formKey){
		return boDefManager.getByFormKey(formKey);
	}
   
	@RequestMapping(value="getFormAndBoExportXml", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "获取表单导出的xml", httpMethod = "POST", notes = "获取表单导出的xml")
	public  Map<String,String> getFormAndBoExportXml (@ApiParam(name="obj",value="通用查询对象")@RequestBody ObjectNode obj) throws JAXBException{
		 Map<String,String> map= formService.getFormAndBoExportXml(obj);
		 return map;
	}
	
	@RequestMapping(value="importFormAndBo", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "导入表单和Bo", httpMethod = "POST", notes = "导入表单和Bo")
	public  CommonResult<String> importFormAndBo (@ApiParam(name="obj",value="通用查询对象")@RequestBody ObjectNode obj) throws Exception{
		 return formService.importFormAndBo(obj);
	}

	@RequestMapping(value="removeDataByBusLink", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "根据业务数据关联对象清除流程相关数据", httpMethod = "POST", notes = "根据业务数据关联对象清除流程相关数据")
	public void removeDataByBusLink (@ApiParam(name="links",value="业务数据关联对象列表")@RequestBody JsonNode links) throws Exception{
		formService.removeDataByBusLink(links);
	}

}
