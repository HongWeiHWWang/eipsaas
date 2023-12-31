package com.hotent.form.controller;


import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.feign.BpmModelFeignService;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.FieldRelation;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.Base64;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.FileUtil;
import com.hotent.base.util.HttpUtil;
import com.hotent.base.util.JAXBUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.base.util.ZipUtil;
import com.hotent.base.util.time.DateFormatUtil;
import com.hotent.bo.model.BoData;
import com.hotent.bo.model.BoDef;
import com.hotent.bo.model.BoEnt;
import com.hotent.bo.persistence.manager.BoDefManager;
import com.hotent.form.enums.FormType;
import com.hotent.form.model.Form;
import com.hotent.form.model.FormHistory;
import com.hotent.form.model.FormHistoryRecord;
import com.hotent.form.model.FormImportXml;
import com.hotent.form.model.FormMeta;
import com.hotent.form.model.FormXml;
import com.hotent.form.param.FormPreviewDataParam;
import com.hotent.form.persistence.manager.FormDataTemplateManager;
import com.hotent.form.persistence.manager.FormHistoryManager;
import com.hotent.form.persistence.manager.FormHistoryRecordManager;
import com.hotent.form.persistence.manager.FormManager;
import com.hotent.form.persistence.manager.FormMetaManager;
import com.hotent.form.util.JsoupUtil;
import com.hotent.form.vo.BpmFormVo;
import com.hotent.uc.api.impl.util.ContextUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * 表单管理
 * 流程任务表单管理
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月8日
 */
@RestController
@RequestMapping("/form/form/v1")
@Api(tags="表单管理")
@ApiGroup(group= {ApiGroupConsts.GROUP_FORM})
@SuppressWarnings({ "unchecked", "rawtypes" })
public class FormController extends BaseController<FormManager, Form>{
	@Resource
	FormHistoryManager bpmFormHistoryManager;
	@Resource
	BpmModelFeignService bpmModelFeignService;
	@Resource
	BoDefManager bODefManager;
	@Resource
	FormMetaManager formMetaManager;
	@Resource
	FormDataTemplateManager bpmDataTemplateManager;
	@Resource
    FormHistoryRecordManager formHistoryRecordManager;


	@RequestMapping(value="list", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "流程任务表单列表(分页条件查询)数据 主版本", httpMethod = "POST", notes = "流程任务表单列表(分页条件查询)数据")
	//@FieldAuth("com.hotent.form.model.BpmForm")
	public PageList<Form> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter queryFilter,
                                      @ApiParam(name="formType",value="表单类型")@RequestParam Optional<String> formType,
                                      @ApiParam(name="status",value="是否发布")@RequestParam Optional<String> status) throws Exception{
	    if(StringUtil.isNotEmpty(formType.orElse(null))){
            queryFilter.addFilter("formType", formType.get(), QueryOP.EQUAL, FieldRelation.AND,"isMain");
            queryFilter.addFilter("status", status.get(), QueryOP.EQUAL, FieldRelation.AND,"isMain");
        }
	    queryFilter.addFilter("is_print_", "Y", QueryOP.NOT_EQUAL, FieldRelation.AND, "isMain");
		queryFilter.addFilter("is_main_", "Y", QueryOP.EQUAL, FieldRelation.AND,"isMain");
		IPage<Form> list = baseService.getFormQueryList(queryFilter);
		PageList<Form> pageList = new PageList<Form>(list);
		if(BeanUtils.isNotEmpty(pageList.getRows())){
			Set<String> allFormKeys = bpmDataTemplateManager.getAllFormKeys();
			for (Form bpmForm : pageList.getRows()) {
				if(allFormKeys.contains(bpmForm.getFormKey())){
					bpmForm.setBusDataTemplateCount((short) 1);
				}
			}
		}
		return pageList;
	}

	@RequestMapping(value="formEdit", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "生成表单HTML", httpMethod = "POST", notes = "生成表单HTML")
	public Form edit(@ApiParam(name="defId",value="表单元数据id")@RequestBody String defId,
			@ApiParam(name="formType",value="表单类型")@RequestBody String formType,
			@ApiParam(name="id",value="流程任务表单Id")@RequestBody String id,
			@ApiParam(name="tableNames",value="主模板")@RequestBody String tableNames,
			@ApiParam(name="templateAlias",value="复合字段模板")@RequestBody String templateAlias) throws Exception{
		if(StringUtil.isEmpty(formType)){
			formType=FormType.PC.value();
		}
		Form form=null ;
		if(StringUtil.isNotEmpty(id)){
			form = baseService.get(id);
		}else{
			FormMeta formDef  = formMetaManager.get(defId);
			form = new Form();
			form.setDefId(defId);
			form.setName(formDef.getName());
			form.setFormType(formType);
			form.setTypeName(formDef.getType());
			form.setTypeId(formDef.getTypeId());
			form.setIsMain('Y');
			form.setVersion(1);
			form.setStatus(Form.STATUS_DRAFT);
			String html = baseService.getHtml(defId, tableNames, templateAlias);
			html = JsoupUtil.prettyHtml(html);
			form.setFormHtml(html);
		}
		if(StringUtil.isNotEmpty(formType) && formType.equals(FormType.MOBILE.value())){
			StringBuffer outHtml = new StringBuffer();
			outHtml.append("<div style=\"height: 100%;overflow: auto;\">");
			outHtml.append(form.getFormHtml());
			outHtml.append("</div>");
			form.setFormHtml(outHtml.toString());
		}

		return form;
	}


	@RequestMapping(value="save", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "保存表单html内容", httpMethod = "POST", notes = "保存表单html内容")
	public CommonResult save(@ApiParam(name="form",value="流程任务表单 entity对象")@RequestBody  Form form) throws Exception{
		if(StringUtil.isEmpty(form.getId())){
			List<Form> bpmForm = baseService.getByFormKey(form.getFormKey());
			if(BeanUtils.isNotEmpty(bpmForm)) throw new RuntimeException("KEY【"+form.getFormKey()+"】对应的表单已存在");
			form.setId(UniqueIdUtil.getSuid());
			baseService.create(form);
		}else{
			baseService.update(form);
		}
		FormHistory bpmFormDefHi = new FormHistory(form); // 保持表单的操作记录
		bpmFormHistoryManager.create(bpmFormDefHi);
		return new CommonResult(true, "保存成功", null);
	}

	/**
	 * 保存表单html内容
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="saveDesign", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "保存表单html内容", httpMethod = "POST", notes = "保存表单html内容")
	public CommonResult saveDesign(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String formData = FileUtil.inputStream2String(request.getInputStream());
		baseService.saveDesign(formData);
		String msg = "保存成功";
		return new CommonResult(true, msg, null);
	}

    /**
     * 保存表单html内容
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="saveFormDesign", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
    @ApiOperation(value = "保存表单html内容", httpMethod = "POST", notes = "保存表单html内容")
    public CommonResult saveFormDesign(HttpServletRequest request,HttpServletResponse response) throws Exception{
        String formData = FileUtil.inputStream2String(request.getInputStream());
		baseService.saveDesign(formData);
        String msg = "保存成功";
        return new CommonResult(true, msg, null);
    }

    /**
     * managevue 表单设计器 保存表单元数据
	 * @date 2020-01-07
     * @param bpmFormVo
     * @return
     * @throws Exception
     */
	@RequestMapping(value="saveForm", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "保存表单内容", httpMethod = "POST", notes = "保存表单内容")
	public CommonResult saveForm(@ApiParam(name="bpmFormVo",value="表单设计数据") @RequestBody BpmFormVo bpmFormVo) throws Exception{
		baseService.saveFormDef(bpmFormVo);
		String msg = "生成表单成功";
		if("newForm".equals(bpmFormVo.getNewForm())) {
			 msg = "发布新版本成功";
		}
		Map<String,Object> value = new HashMap<>();
		value.put("rev", bpmFormVo.getBpmFormDef().getRev());
		value.put("formData", bpmFormVo.getBpmForm());
		return new CommonResult(true, msg, value);
	}

	/**
     * managevue pc表单转换为手机表单
	 * @date 2020-02-23
     */
	@RequestMapping(value="pcForm2MobileForm", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "pc表单转换为手机表单", httpMethod = "POST", notes = "pc表单转换为手机表单")
	public CommonResult pcForm2MobileForm(@ApiParam(name="formId",value="表单设计数据") @RequestParam String formId) throws Exception{
		baseService.pcForm2MobileForm(formId);
		String msg = "生成手机表单成功,请在手机表单中查看";
		return new CommonResult(true, msg, null);
	}

	/**
	 * managevue 表单设计器 生成表单html
	 * @date 2020-01-07
	 * @param bpmFormVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="generateFrom", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "重新生成表单", httpMethod = "POST", notes = "重新生成表单")
	public CommonResult generateFrom  (@ApiParam(name="bpmFormVo",value="表单设计数据") @RequestBody BpmFormVo bpmFormVo) throws Exception{
		baseService.generateFrom(bpmFormVo);
		String msg = "保存成功";
		return new CommonResult(true, msg, null);
	}

	@RequestMapping(value="getBpmFormById", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "根据获取form数据", httpMethod = "GET", notes = "根据获取form数据")
	public BpmFormVo generateFrom  (@ApiParam(name="bpmForm",value="表单数据") @RequestParam String id,@ApiParam(name="bpmFormDef",value="表单设计数据") @RequestParam String defId) throws Exception{
		Form bpmForm = baseService.get(id);
		FormMeta bpmFormDef = formMetaManager.get(defId);
		BpmFormVo bpmFormVo = new BpmFormVo();
		bpmFormVo.setBpmForm(bpmForm);
		bpmFormVo.setBpmFormDef(bpmFormDef);
		return bpmFormVo;
	}

    /**
     * 更新表单的自定义脚本
     * @return
     * @throws Exception
     */
    @RequestMapping(value="saveFormJs", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
    @ApiOperation(value = "更新表单的自定义脚本", httpMethod = "POST", notes = "更新表单的自定义脚本")
    public CommonResult<String> saveFormJs(@ApiParam(name="map",value="更新数据") @RequestBody Map<String,Object> map) throws Exception{
        String diyJs = "";
        String formHtml = "";
        if(BeanUtils.isNotEmpty(map.get("diyJs"))){
            diyJs = map.get("diyJs").toString();
        }
        if(BeanUtils.isNotEmpty(map.get("formHtml"))){
        	 formHtml = Base64.getFromBase64(map.get("formHtml").toString());
        }
        CommonResult<String> str = baseService.saveFormJs(map.get("formId").toString(),diyJs,formHtml);
        return str;
    }

	@RequestMapping(value="saveCopy", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "复制表单信息", httpMethod = "POST", notes = "复制表单信息")
	public CommonResult saveCopy(@ApiParam(name="form",value="表单元数据对象")@RequestBody Form form) throws Exception{
    	Form newForm=baseService.get(form.getId());
    	String formDefId=newForm.getDefId();
    	String formKey=form.getFormKey();
    	String formName=form.getName();
    	String typeId=form.getTypeId();
    	String typeName=form.getTypeName();
    	String userId=ContextUtil.getCurrentUserId();
    	String groupId=ContextUtil.getCurrentGroupId();
    	//当前时间
		Date data=new Date();
		if(baseService.getByFormKey(formKey)==null){
			throw new RuntimeException("表单已经存在！key:"+formKey);
		}
		//添加表单元数据
		FormMeta bpmFormDef=formMetaManager.get(formDefId);
		String formNewDefId=UniqueIdUtil.getSuid();
		bpmFormDef.setId(formNewDefId);
		bpmFormDef.setKey(formKey);
		bpmFormDef.setName(formName);
		bpmFormDef.setTypeId(typeId);
		bpmFormDef.setType(typeName);
		bpmFormDef.setCreateBy(userId);
		bpmFormDef.setCreateOrgId(groupId);
		bpmFormDef.setRev(1);
		//清空表单定义更新状态
		bpmFormDef.setUpdateBy(null);
		bpmFormDef.setUpdateTime(LocalDateTime.now());
		formMetaManager.create(bpmFormDef);
		//添加表单信息
		newForm.setId(UniqueIdUtil.getSuid());
		newForm.setDefId(formNewDefId);
		newForm.setFormKey(formKey);
		newForm.setName(formName);
		newForm.setTypeId(typeId);
		newForm.setTypeName(typeName);
		newForm.setStatus(Form.STATUS_DRAFT);
		newForm.setVersion(1);
		newForm.setCreateBy(userId);
		newForm.setCreateOrgId(groupId);
		//清空表单更新状态
		newForm.setUpdateTime(LocalDateTime.now());
		newForm.setUpdateBy(null);
		baseService.create(newForm);
		return new CommonResult(true, "复制表单成功", null);
	}
	
	@RequestMapping(value="savePrintTemplate", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "添加表单打印模板", httpMethod = "POST", notes = "添加表单打印模板")
	public CommonResult<String> savePrintTemplate(@ApiParam(name="form",value="表单元数据对象")@RequestBody Form form) throws Exception{
		return baseService.savePrintTemplate(form);
	}

	@RequestMapping(value="checkKey", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "检查别名是否存在", httpMethod = "GET", notes = "检查别名是否存在")
	public boolean checkKey(@ApiParam(name="key",value="表单key") @RequestParam String key) throws Exception{
		List<Form> bpmForm =baseService.getByFormKey(key);
		return  bpmForm.size()>0;
	}
	
	@RequestMapping(value="checkPrintKey", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "检查表单打印模板别名是否存在", httpMethod = "GET", notes = "检查别名是否存在")
	public boolean checkPrintKey(@ApiParam(name="key",value="表单key") @RequestParam String key) throws Exception {
		List<Form> bpmForm = baseService.getPrintByFormKey(key);
		return  bpmForm.size()>0;
	}

    @GetMapping(value="getFormHistoryRecord")
    @ApiOperation(value="根据主键ID查询表单HTML内容",httpMethod = "GET",notes = "根据主键ID查询表单HTML内容")
    public FormHistoryRecord getFormHistoryRecord(@ApiParam(name="id",value="业务对象主键", required = true)@RequestParam String id) throws Exception{
        return formHistoryRecordManager.get(id);
    }

    @RequestMapping(value="updateFormHistoryRecord", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
    @ApiOperation(value = "恢复到表单历史数据", httpMethod = "GET", notes = "恢复到表单历史数据")
    public CommonResult<String> updateFormHistoryRecord(@ApiParam(name="id",value="主键ID")@RequestParam String id) throws Exception{
        FormHistoryRecord formHistoryRecord = formHistoryRecordManager.get(id);
        CommonResult<String> stringCommonResult = baseService.updateFormHistoryRecord(formHistoryRecord.getFormId(),formHistoryRecord.getFormHtml());
        return stringCommonResult;
    }
    @RequestMapping(value="getFormHtmlByFormId", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
    @ApiOperation(value = "根据表单ID查询表单HTML内容（表单HTML数据历史记录）", httpMethod = "GET", notes = "根据表单ID查询表单HTML内容（表单HTML数据历史记录）")
    public FormHistoryRecord getFormHtmlByFormId(@ApiParam(name="formId",value="表单ID")@RequestParam String formId) throws Exception{
        List<FormHistoryRecord> formHistoryRecord = formHistoryRecordManager.getFormHtmlByFormId(formId);
        return formHistoryRecord.get(0);
    }

    @RequestMapping(value="formHistoryRecordlistJson", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
    @ApiOperation(value = "根据表单ID查询表单HTML内容(分页条件查询)", httpMethod = "POST", notes = "根据表单ID查询表单HTML内容(分页条件查询)")
    public  PageList<FormHistoryRecord> formHistoryRecordlistJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter queryFilter)throws Exception{
        return formHistoryRecordManager.query(queryFilter);
    }

    @DeleteMapping(value="delFormHistoryRecord")
    @ApiOperation(value = "根据表单ID查询表单HTML内容删除记录", httpMethod = "DELETE", notes = "根据表单ID查询表单HTML内容删除记录")
    public  CommonResult<String> delFormHistoryRecord(@ApiParam(name="id",value="业务主键")@RequestParam String id) throws Exception{
        formHistoryRecordManager.remove(id);
        return new CommonResult<String>(true, "删除成功");
    }

    @RequestMapping(value="previewDesignVue", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
    @ApiOperation(value = "Vue表单预览", httpMethod = "GET", notes = "表单预览")
    public Object previewDesignVue(@ApiParam(name="formId",value="表单id") @RequestParam String formId) throws Exception{
        return baseService.getPreviewDesignVueData(formId);
    }


	@RequestMapping(value="previewDesign", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "表单预览", httpMethod = "POST", notes = "表单预览")
	public Object previewDesign(@ApiParam(name="param",value="表单预览") @RequestBody FormPreviewDataParam param) throws Exception{
		return baseService.getPreviewDesignData(param);
	}



	@RequestMapping(value="listVersions", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "查询表单的所有版本", httpMethod = "POST", notes = "查询表单的所有版本")
	public PageList<Form> listVersions(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter queryFilter) throws Exception {
		return baseService.query(queryFilter);
	}

    @RequestMapping(value="getFormById", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
    @ApiOperation(value = "根据表单ID获取表单明细", httpMethod = "GET", notes = "根据表单ID获取表单明细")
    public Form getFormById(@ApiParam(name="id",value="表单ID")@RequestParam String id) throws Exception{
        Form  bpmForm = baseService.get(id);
        return bpmForm;
    }


	@RequestMapping(value="preview", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "表单信息明细", httpMethod = "POST", notes = "表单信息明细")
	public Form preview(@ApiParam(name="id",value="流程任务表单 ID")@RequestBody String id,
			@ApiParam(name="formKey",value="流程任务表单 KEY")@RequestBody String formKey,
			@ApiParam(name="formType",value="流程任务表单类型")@RequestBody String formType,
			@ApiParam(name="formHtml",value=" 表单设计（HTML代码）")@RequestBody String formHtml) throws Exception{
		if(StringUtil.isEmpty(formType)){
			formType=FormType.PC.value();
		}
		if(StringUtil.isEmpty(id) && StringUtil.isNotEmpty(formKey)){
			Form mainByFormKey = baseService.getMainByFormKey(formKey);
			id = mainByFormKey.getId();
		}
		Form bpmForm=new Form();
		if(StringUtil.isNotEmpty(id)){
			bpmForm = baseService.get(id);
			formType =bpmForm.getFormType();
		}

		if(StringUtil.isNotEmpty(formHtml)){
			bpmForm.setFormHtml(formHtml);
		}
		return bpmForm;
	}


	@RequestMapping(value="getRight", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "获取表单权限", httpMethod = "POST", notes = "获取表单权限")
	public JsonNode getRight(@ApiParam(name="id",value="流程任务表单 ID")@RequestBody String id,
			@ApiParam(name="defId",value="表单元数据Id")@RequestBody String defId,
			@ApiParam(name="formType",value="流程任务表单类型")@RequestBody String formType) throws Exception{
		return baseService.getRightData(id, defId, formType);
	}
	@RequestMapping(value="getBoData", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "根据表单元数据ID获取bo数据", httpMethod = "POST", notes = "根据表单元数据ID获取bo数据")
	public ArrayNode getBoData(@ApiParam(name="defId",value="表单元数据Id")@RequestBody String defId) throws Exception{
		List<BoData> boDatas = formMetaManager.getBoDataByFormDefId(defId);
		ArrayNode arrayNode = JsonUtil.getMapper().createArrayNode();
		if (BeanUtils.isNotEmpty(boDatas)) {
			arrayNode = JsonUtil.listToArrayNode(boDatas);
		}
		return arrayNode;
	}


	@RequestMapping(value="getBoJsonByFormKey", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "根据表单key获取boJosn数据", httpMethod = "GET", notes = "根据表单key获取boJosn数据")
	public ArrayNode getBoJsonByFormKey(@ApiParam(name="formkey",value="表单key",required=true)@RequestParam String formkey) throws Exception{
		FormMeta formDef = formMetaManager.getByKey(formkey);
		ArrayNode arrayNode = JsonUtil.getMapper().createArrayNode();
		if (BeanUtils.isNotEmpty(formDef)) {
			List<String> boDefCodes = formMetaManager.getBOCodeByFormId(formDef.getId());
			for (String defCode : boDefCodes) {
				ObjectNode boJson = bODefManager.getBOJsonByBoDefCode(defCode);
				arrayNode.add(boJson);
			}
		}
		return arrayNode;
	}


	@RequestMapping(value="getBOCodes", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "根据表单id获取业务对象编码", httpMethod = "GET", notes = "根据表单id获取业务对象编码")
	public List<String> getBOCodes(@ApiParam(name="formId",value="表单元数据Id")@RequestParam String formId) throws Exception{
		return formMetaManager.getBOCodeByFormId(formId);

	}

	@RequestMapping(value="remove", method=RequestMethod.DELETE, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除表单记录", httpMethod = "DELETE", notes = "批量删除表单记录")
	public CommonResult remove(@ApiParam(name="ids",value="流程任务表单ID!多个ID用,分割") @RequestParam(required=true)  String ids) throws Exception{
    	String[] aryIds=null;
    	if(!StringUtil.isEmpty(ids)){
    		aryIds=ids.split(",");
    	}
    	String bpmNames =checkBpmForm(aryIds);//检查是否绑定了流程
		if(StringUtil.isEmpty(bpmNames)){
			baseService.remove(aryIds);
			return new CommonResult(true, "删除流程任务表单成功", null);
		}else{
			String msg = "删除失败："+bpmNames;
			return new CommonResult(false, msg, null);
		}
	}
	/**
	 * 删除表单时检查是否绑定了流程。
	 * <pre>
	 * 目前是先找表单和业务对象的关系，然后再找业务对象和流程的关系
	 * </pre>
	 * @param aryIds
	 * @return
	 */
	public String checkBpmForm(String[] aryIds) {
		String msgs = "";
		for(String formId : aryIds){//多个表单同时删除
			Form bpmForm = baseService.get(formId);
			List<String> boDefs= formMetaManager.getBODefIdByFormId(bpmForm.getDefId());//获取表单对应的业务对象
			for(String boDefId :boDefs){//业务对象有主对象和从对象
				BoDef boDef = bODefManager.get(boDefId);
				if(boDef==null) continue;

				CommonResult<Boolean> result = bpmModelFeignService.isBoBindFlowCheck(boDef.getAlias(), bpmForm.getFormKey());
				if(result.getValue()){
					msgs +=  "『表单【"+bpmForm.getName()+"】"+result.getMessage()+"』";
				}
			}
		}
		return msgs;
	}





	@RequestMapping(value="chooseDesignTemplate", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "加载编辑器设计模式的模板列表", httpMethod = "POST", notes = "加载编辑器设计模式的模板列表")
	public Map chooseDesignTemplate(@ApiParam(name="subject",value="标题")@RequestBody String subject,
			@ApiParam(name="categoryId",value="")@RequestBody String categoryId,
			@ApiParam(name="formDesc",value="表单描述")@RequestBody String formDesc,
			@ApiParam(name="isSimple",value="true将只允许选择一行")@RequestBody Boolean isSimple) throws Exception {
		return baseService.getChooseDesignTemplate(subject, categoryId, formDesc, isSimple);
	}


	@RequestMapping(value="genByTemplate", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "根据模板产生html。", httpMethod = "POST", notes = "根据模板产生html。")
	public void genByTemplate(@ApiParam(name="formId",value="主键")@RequestBody String formId,
			@ApiParam(name="tableNames",value="主模板")@RequestBody String tableNames,
			@ApiParam(name="templateAlias",value="复合字段模板")@RequestBody String templateAlias,
			@ApiParam(name="formDefId",value="表单元数据ID")@RequestBody String formDefId,
			@ApiParam(name="formType",value="表单类型")@RequestBody String formType,
			HttpServletResponse response) throws Exception {
		baseService.getGenByTemplate(formId, tableNames, templateAlias, formDefId, formType, response);
	}







	@RequestMapping(value="newVersion", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "根据表单定义id创建新的表单版本", httpMethod = "POST", notes = "根据表单定义id创建新的表单版本")
	public CommonResult newVersion(@ApiParam(name="formId",value="表单ID")@RequestBody String formId) throws Exception {
		baseService.newVersion(formId);
		return new CommonResult(true, "新建表单版本成功!", null);
	}


	@RequestMapping(value="setDefaultVersion", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "设置默认版本", httpMethod = "POST", notes = "设置默认版本")
	public CommonResult setDefaultVersion(@ApiParam(name="id",value="表单ID")@RequestParam String id,
			@ApiParam(name="formKey",value="表单key")@RequestParam String formKey) throws Exception {
		baseService.setDefaultVersion(id, formKey);
		baseService.updatePermissionByKey(formKey);
		return new CommonResult(true, "设置默认版本成功", null);
	}


	@RequestMapping(value="publish", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "发布表单", httpMethod = "POST", notes = "发布表单")
	public CommonResult publish(@ApiParam(name="formId",value="表单ID")@RequestParam String formId)
			throws Exception {
		baseService.publish(formId);
		return new CommonResult(true, "发布版本成功", null);
	}


	@RequestMapping(value="genByField", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "生成字段的html", httpMethod = "POST", notes = "生成字段的html")
	public void genByField(@ApiParam(name="defId",value="表单元数据ID")@RequestBody String defId,
			@ApiParam(name="attrId",value="BO属性ID")@RequestBody String attrId,
			@ApiParam(name="formType",value="表单类型")@RequestBody String formType,
			HttpServletResponse response) throws Exception {
		String html = baseService.genByField(defId,attrId,formType);
		html = JsoupUtil.prettyHtml(html);
		PrintWriter out = response.getWriter();
		out.println(html);
	}


	/**
	 * <pre>
	 * 导出格式为*.zip的BO对象，zip文件包含多个xml文件，每一个xml文件都是一个bo业务对象;
	 * <br>
	 * zip文件命名为：boDef_yyyyMMddHHmmss.zip;
	 * <br>
	 * 每个xml文件命名规则为:name_id.xml;
	 * <br>
	 * 完成后，相关生成的文件都会删除.
	 * </pre>
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */

	@RequestMapping(value="exportForm" ,method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "导出表单xml", httpMethod = "GET", notes = "导出表单xml")
	public void exportForm(HttpServletRequest request, HttpServletResponse response, @ApiParam(name="formIds",value="表单id", required = true) @RequestParam String formIds) throws Exception {
		response.setContentType("APPLICATION/OCTET-STREAM");
		if(BeanUtils.isNotEmpty(formIds)) {
			String[] ids = formIds.split(",");
			List<String> idList = Arrays.asList(ids);

			Map map = baseService.exportForms(idList,true); // 输出xml

			String fileName = "ht_form_"+DateFormatUtil.format(LocalDateTime.now(), "yyyy_MMdd_HHmm");

			HttpUtil.downLoadFile(request, response, map, fileName);
		}
	}

	@RequestMapping(value="importSave", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "表单附件上传操作，根据传入的文件id从缓存中取出xml文件导入，并清除缓存", httpMethod = "POST", notes = "附件上传操作")
	public Object importSave(@ApiParam(name="confirmImport",value="确认导入",required=false) @RequestParam Optional<Boolean> confirmImport,
			@ApiParam(name="typeId",value="表单标识",required=false) @RequestParam Optional<String> typeId,
			@ApiParam(name="cacheFileId",value="缓存的流程文件id",required=false) @RequestParam Optional<String> cacheFileId) throws Exception {

		CommonResult<String> message = new CommonResult<>("导入成功");
		if (confirmImport.orElse(false)) {
			String byKey = baseService.getImportFileFromCache(cacheFileId.get());
			if(StringUtil.isEmpty(byKey)) {
				return new CommonResult<>("导入的文件已经过期，请重新导入。");
			}
			ObjectNode objectNode = (ObjectNode) JsonUtil.toJsonNode(byKey);
			baseService.importForms(objectNode,typeId.orElse(""));
		}
		baseService.delImportFileFromCache(cacheFileId.orElse(""));
		return message;
	}

	@RequestMapping(value="importCheck", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "表单导入前校验", httpMethod = "POST", notes = "表单导入前校验")
	public CommonResult<String> importCheck(MultipartHttpServletRequest request, HttpServletResponse response,
			@ApiParam(name="typeId",value="表单标识",required=false) @RequestParam Optional<String> typeId) throws Exception {
		Map<String, MultipartFile> fileMaps = request.getFileMap();
		Iterator<MultipartFile> it = fileMaps.values().iterator();
		List<MultipartFile> files = new ArrayList<MultipartFile>();
		while (it.hasNext()) {
			files.add(it.next());
		}
		MultipartFile fileLoad = files.get(0);
		String unZipFilePath = "";
		CommonResult<String> message = null;
		try {
			String fileDir = StringUtil.substringBeforeLast(fileLoad.getOriginalFilename().toString(), ".");
			String rootRealPath = (FileUtil.getIoTmpdir() +"attachFiles/unZip/").replace("/", File.separator);
			//建立临时文件夹，存放文件
			File folder=new File(rootRealPath);
			if(!folder.exists()) {
				folder.mkdirs();
			}
			// 解压文件
			ZipUtil.unZipFile(fileLoad, rootRealPath);
		    unZipFilePath = rootRealPath + File.separator + fileDir; // 解压后文件的真正路径

		    String formXmlStr = FileUtil.readFile(unZipFilePath + File.separator + "form.xml");
		    String boXmlStr = FileUtil.readFile(unZipFilePath + File.separator + "bo.xml");

		    if (StringUtils.isEmpty(formXmlStr)) throw new Exception("导入的未按指定的格式");

		    ObjectNode obj = JsonUtil.getMapper().createObjectNode();
			obj.put("formXmlStr", formXmlStr);
			obj.put("boXmlStr", boXmlStr);

		    FormImportXml formImportXml =(FormImportXml)JAXBUtil.unmarshall(formXmlStr, FormImportXml.class);
			List<FormXml> formXmlList = formImportXml.getFormXmlList();
			List<String> names = new ArrayList<String>();

			for (FormXml formXml : formXmlList) {
				Form form = formXml.getBpmForm();

				Form oldForm = baseService.getMainByFormKey(form.getFormKey());
				if(oldForm != null){
					names.add(oldForm.getName()+"（"+oldForm.getFormKey()+"）");
				}
			}
			if(BeanUtils.isEmpty(names)){
				baseService.importForms(obj,typeId.orElse(""));
				message = new CommonResult<String>(true, "导入成功");
			}else{
				String cacheFileId = UniqueIdUtil.getSuid();
				baseService.putImportFileInCache(cacheFileId, JsonUtil.toJson(obj));
				message = new CommonResult<String>(false, "导入失败，表单【" + String.join("，",names)+"】在系统中已存在，是否继续为其新增版本？",cacheFileId);
			}
		} catch (Exception e) {
			message = new CommonResult<String>(false, "导入失败:" + e.getMessage());
		} finally {
			File boDir = new File(unZipFilePath);
			if (boDir.exists()) {
				FileUtil.deleteDir(boDir); // 删除解压后的目录
			}
		}
		return message;
	}



	@RequestMapping(value="formDesign", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "获取表单已设置的信息", httpMethod = "GET", notes = "获取表单已设置的信息")
	public Object formDesign(@ApiParam(name="formId",value="表单ID")@RequestParam String formId) throws Exception{
		return baseService.getFormDesign(formId);
	}

	@RequestMapping(value="getSubEntsByFormKey", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "根据组件别名获取所有子实体。", httpMethod = "GET", notes = "根据组件别名获取所有子实体。")
	public List<BoEnt> getSubEntsByFormKey(@ApiParam(name="formKey",value="组件key")@RequestParam String formKey) throws Exception {
		if (StringUtil.isNotEmpty(formKey)) {
			return formMetaManager.getChildrenByFormKey(formKey);
		}
		return null;
	}

	@RequestMapping(value="getBindRelation", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "获取表单绑定关系。", httpMethod = "GET", notes = "根据组件别名获取所有子实体。")
	public Map<String,Object> getBindRelation(@ApiParam(name="defId",value="表单defId")@RequestParam String defId,@ApiParam(name="formKey",value="表单formKey")@RequestParam String formKey) throws Exception {
		return baseService.getBindRelation(defId,formKey);
	}

	@RequestMapping(value = "getFormData", method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "流程获取表单信息", httpMethod = "GET", notes = "取得所有的表对象")
	public Map<String,Object> getFormData(@ApiParam(name = "pcAlias", value = "pc别名") @RequestParam(required = false) String pcAlias, @ApiParam(name = "mobileAlias", value = "mobile别名") @RequestParam(required = false) String mobileAlias) throws Exception {
		return baseService.getFormData(pcAlias, mobileAlias);
	}
}
