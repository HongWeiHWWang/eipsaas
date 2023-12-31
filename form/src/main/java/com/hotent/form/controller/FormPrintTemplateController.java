package com.hotent.form.controller;


import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.feign.BpmRuntimeFeignService;
import com.hotent.base.feign.PortalFeignService;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.BeanUtils;
import com.hotent.form.model.FormPrintTemplate;
import com.hotent.form.persistence.manager.FormPrintTemplateManager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/form/printTemplate/v1/")
@Api(tags="套打模板")
@ApiGroup(group= {ApiGroupConsts.GROUP_FORM})
public class FormPrintTemplateController  extends BaseController<FormPrintTemplateManager, FormPrintTemplate> {

    @Resource
    BpmRuntimeFeignService bpmRuntimeFeignService;
    @Resource
    FormPrintTemplateManager formPrintTemplateManager;
    @Resource
    PortalFeignService portalFeignService;
    
    @RequestMapping(value="getPrintList", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "打印模板列表", httpMethod = "POST", notes = "打印模板列表")
	@ResponseBody
    public PageList<FormPrintTemplate> getPrintList(@ApiParam(name="queryFilter",value="通用查询对象") @RequestBody QueryFilter<FormPrintTemplate> queryFilter) throws Exception {
    	Page<FormPrintTemplate> list = (Page<FormPrintTemplate>) baseService.getPrintList(queryFilter);
    	return new PageList<FormPrintTemplate>(list);
    }
    
    @PostMapping("/save")
    @ApiOperation("添加实体的接口")
    public CommonResult<String> save(@ApiParam(name="model", value="打印模板") @RequestBody FormPrintTemplate formPrintTemplate) {
        baseService.saveFormPrintTemplate(formPrintTemplate);
        return new CommonResult<>("上传模板成功");
    }


    @RequestMapping(value="removes",method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
    @ApiOperation(value = "批量删除bo定义", httpMethod = "DELETE", notes = "批量删除bo定义")
    public CommonResult<String> batchRemove(@ApiParam(name="ids",value="bo主键集合", required = true) @RequestParam String...ids) throws Exception{
        formPrintTemplateManager.removeByIds(ids);
        return new CommonResult<>("删除模板成功");
    }


    @RequestMapping(value="setDefaultVersion",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
    @ApiOperation(value = "设置为主版本", httpMethod = "GET", notes = "设置为主版本")
    public CommonResult<String> setDefaultVersion(
    		@ApiParam(name="id",value="主键", required = true) @RequestParam String id,
    		@ApiParam(name="formKey",value="表单key", required = true) @RequestParam String formKey,
    		@ApiParam(name="printType",value="打印类型", required = true) @RequestParam String printType) throws Exception {
        baseService.setDefaultVersion(formKey, id, printType);
        return new CommonResult<>("设置主版本成功");
    }


    @RequestMapping(value="print",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
    @ApiOperation(value = "打印模板", httpMethod = "GET", notes = "打印模板")
    public CommonResult<String> print(@ApiParam(name="defId",value="流程实例Id", required = true) @RequestParam String defId,
                      @ApiParam(name="nodeId",value="节点ID", required = true) @RequestParam Optional<String> nodeId,
                      @ApiParam(name="procInstId",value="流程实例Id", required = true) @RequestParam String procInstId) throws Exception {
        ObjectNode objectNode=bpmRuntimeFeignService.printBoAndFormKey(defId,nodeId.orElse(""),procInstId);
        String formKey=objectNode.get("formKey").asText();
        String formName=objectNode.get("formName").asText();
        FormPrintTemplate formPrintTemplate= baseService.getMainFormPrintTemplate(formKey);
        if(BeanUtils.isEmpty(formPrintTemplate)){
            return new CommonResult<>(true, formName+"表单没有设置打印模板", "");
        }else{
            String fileId=formPrintTemplate.getFileId();
            objectNode.put("fileId", fileId);
            String id= portalFeignService.wordPrint(objectNode);
            return new CommonResult<>(true, "", id);
        }
    }
    
    @RequestMapping(value="wordPrint",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
    @ApiOperation(value = "打印word模板", httpMethod = "GET", notes = "打印word模板")
    public CommonResult<String> wordPrint(
    		@ApiParam(name="id",value="id", required = true) @RequestParam String id,
    		@ApiParam(name="defId",value="流程实例Id", required = true) @RequestParam String defId,
            @ApiParam(name="nodeId",value="节点ID", required = true) @RequestParam Optional<String> nodeId,
            @ApiParam(name="procInstId",value="流程实例Id", required = true) @RequestParam String procInstId)  throws Exception {
    	ObjectNode objectNode=bpmRuntimeFeignService.printBoAndFormKey(defId,nodeId.orElse(""),procInstId);
    	FormPrintTemplate formPrintTemplate = baseService.get(id);
    	if(BeanUtils.isEmpty(formPrintTemplate)){
    		return new CommonResult<>(false, "请选择正确的打印模板");
    	}
    	
    	String fileId=formPrintTemplate.getFileId();
        objectNode.put("fileId", fileId);
        String printId= portalFeignService.wordPrint(objectNode);
        return new CommonResult<>(true, "", printId);
    }
    
    @RequestMapping(value="getFormKey",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
    @ApiOperation(value = "获取表单Key", httpMethod = "GET", notes = "获取表单Key")
    public ObjectNode getFormKey(@ApiParam(name="defId",value="流程实例Id", required = true) @RequestParam String defId,
            @ApiParam(name="nodeId",value="节点ID", required = true) @RequestParam Optional<String> nodeId,
            @ApiParam(name="procInstId",value="流程实例Id", required = true) @RequestParam String procInstId) throws Exception {
    	return bpmRuntimeFeignService.printBoAndFormKey(defId,nodeId.orElse(""),procInstId);
    }
    
    @RequestMapping(value="getMainTemlate",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
    @ApiOperation(value = "通过表单key获取列表", httpMethod = "GET", notes = "通过表单key获取列表")
    public FormPrintTemplate getMainTemlate(
    		@ApiParam(name="formKey",value="表单key", required = true) @RequestParam String formKey,
    		@ApiParam(name="printType",value="打印类型", required = true) @RequestParam String printType) throws Exception{
    	return baseService.getMailPrintTemplates(formKey, printType);
    }

}
