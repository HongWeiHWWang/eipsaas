package com.hotent.form.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.context.BaseContext;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.Direction;
import com.hotent.base.query.FieldRelation;
import com.hotent.base.query.FieldSort;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.Dom4jUtil;
import com.hotent.base.util.FileUtil;
import com.hotent.base.util.HttpUtil;
import com.hotent.base.util.JAXBUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.base.util.ZipUtil;
import com.hotent.base.util.time.DateFormatUtil;
import com.hotent.bo.bodef.BoDefService;
import com.hotent.bo.model.BoAttribute;
import com.hotent.bo.model.BoDef;
import com.hotent.bo.model.BoEnt;
import com.hotent.form.model.Form;
import com.hotent.form.model.FormDataTemplate;
import com.hotent.form.model.FormDataTemplateDraft;
import com.hotent.form.model.FormDataTemplateXml;
import com.hotent.form.model.FormDataTemplateXmlList;
import com.hotent.form.model.FormMeta;
import com.hotent.form.param.BpmDataTemplateInfoVo;
import com.hotent.form.param.DataTemplateQueryVo;
import com.hotent.form.persistence.dao.FormDataTemplateDao;
import com.hotent.form.persistence.manager.FormDataTemplateDraftManager;
import com.hotent.form.persistence.manager.FormDataTemplateManager;
import com.hotent.form.persistence.manager.FormManager;
import com.hotent.form.persistence.manager.FormMetaManager;
import com.hotent.form.vo.ExportSubVo;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.impl.var.IContextVar;
import com.hotent.uc.api.model.IUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 *
 * 业务数据模板
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年9月30日
 */
@RestController
@RequestMapping("/form/dataTemplate/v1")
@Api(tags="数据视图接口(业务数据模板)")
@ApiGroup(group= {ApiGroupConsts.GROUP_FORM})
@SuppressWarnings({ "unchecked", "rawtypes" })
public class FormDataTemplateController extends BaseController<FormDataTemplateManager, FormDataTemplate>{

	@Resource
	BoDefService boDefService;
	@Resource
	FormDataTemplateDao bpmDataTemplateDao;
	@Resource
	FormManager formManager;
	@Resource
	FormMetaManager formMetaManager;
	@Resource
	FormDataTemplateDraftManager dataTemplateDraftManager;
	@Resource
	BaseContext baseContext;
	
	/**
	 * 业务数据模板列表(分页条件查询)数据
	 * @return
	 * @throws Exception
	 * PageJson
	 * @exception
	 */
	@RequestMapping(value="listJson", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "业务数据模板列表(分页条件查询)数据", httpMethod = "POST", notes = "业务数据模板列表(分页条件查询)数据")
	public PageList<Map<String, Object>> listJson(
			@ApiParam(name="queryFilter",value="业务数据模板查询对象")@RequestBody DataTemplateQueryVo dataTemplateQueryVo) throws Exception{

		FormDataTemplate template = baseService.get(dataTemplateQueryVo.getTemplateId());

		return baseService.getList(template,dataTemplateQueryVo);
	}


	@RequestMapping(value="listToJson", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "业务数据模板列表(分页条件查询)数据", httpMethod = "POST", notes = "业务数据模板列表(分页条件查询)数据")
	public PageList<FormDataTemplate> listToJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter queryFilter) throws Exception{
		PageList<FormDataTemplate> bpmDataTemplates = (PageList<FormDataTemplate>)baseService.query(queryFilter);
		return bpmDataTemplates;
	}

	/**
	 * 数据列表  第二次解析模板
	 *
	 * @param request
	 * @param alias
	 * @param queryFilter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="dataList_{alias}", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "数据列表  第二次解析模板", httpMethod = "POST", notes = "数据列表  第二次解析模板")
	public CommonResult<String> dataList(HttpServletRequest request,@PathVariable(value = "alias") String alias,
			@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter queryFilter) throws Exception {
		//取得当前页URL,如有参数则带参数
//		String url = "dataList_" + alias + ".ht";
//		String toReplaceUrl = "getDisplay_" + alias + ".ht";
//		String __baseURL = request.getRequestURI().replace(url, toReplaceUrl);
		//取得传入参数ID
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(FormDataTemplate.PARAMS_KEY_CTX, "");
		params.put("__tic", "bpmDataTemplate");
		params.put(FormDataTemplate.PARAMS_KEY_ALIAS, alias);
		String html = baseService.getDisplay(alias, params, queryFilter.getParams());
 		return new CommonResult<String>(true,"获取成功！",html);
	}

	/**
	 * 业务数据模板明细页面
	 * @param formKey
	 * @return
	 * @throws Exception
	 * ModelAndView
	 */
	@RequestMapping(value="getBpmDataTemplate",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据表单key获取业务数据模板明细", httpMethod = "GET", notes = "根据表单key获取业务数据模板明细")
	public ObjectNode getBpmDataTemplate(@ApiParam(name="formKey",value="表单key", required = true) @RequestParam String formKey,@ApiParam(name="formKey",value="表单key", required = false) @RequestParam String boId) throws Exception{
		return baseService.getByFormKey(formKey, boId);

	}

	@GetMapping(value="getBODefByFormId",produces = { "application/json; charset=utf-8" })
	public List<FormMeta> getBODefByFormId(@ApiParam(name="formId",value="表单Id", required = true) @RequestParam String formId) throws Exception{
		return formMetaManager.getBODefByFormId(formId);
	}

	/**
	 * 业务数据模板明细页面
	 * @param id
	 * @return
	 * @throws Exception
	 * ModelAndView
	 */
	@RequestMapping(value="getByTemplateId",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据id获取业务数据模板明细", httpMethod = "GET", notes = "根据id获取业务数据模板明细")
	public ObjectNode getByTemplateId(@ApiParam(name="id",value="业务数据id", required = true) @RequestParam String id, @ApiParam(name="boId",value="boId", required = false) @RequestParam String boId) throws Exception{
		return baseService.getByTemplateId(id, boId);

	}

	/**
	 * 保存业务数据模板信息
	 * @param json
	 * @throws Exception
	 * void
	 * @exception
	 */
	@RequestMapping(value="save",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存业务数据模板信息。", httpMethod = "POST", notes = "保存业务数据模板信息")
	public CommonResult<String> save(@RequestBody String json) throws Exception{
		String resultMsg=null;
		FormDataTemplate bpmDataTemplate = getFormObject(json);
		//是否初始化模板（是在编辑情况下，是否再初始化模板）
		boolean resetTemp=false;
		if (!StringUtil.isEmpty(json)){
			ObjectNode obj = (ObjectNode) JsonUtil.toJsonNode(json);
			if(BeanUtils.isNotEmpty(obj.get("resetTemp"))){
				String str = obj.get("resetTemp").asText();
				if(str.equals("1")){
					resetTemp=true;
				}
			}
		}
		boolean flag = StringUtil.isEmpty(bpmDataTemplate.getId())?true:false;
		if(flag){
			FormDataTemplate temp = baseService.getByAlias(bpmDataTemplate.getAlias());
			if(BeanUtils.isNotEmpty(temp)){
				return new CommonResult<String>(false, "报表别名："+bpmDataTemplate.getAlias()+"已存在，请输入其他别名！");
			}
		}
		baseService.save(bpmDataTemplate,resetTemp);//在这个过程中进行了第一次模板解释，然后复制到templateHtml上
		resultMsg = flag ? "添加业务数据模板成功" : "更新业务数据模板成功";
		return new CommonResult<String>(true, resultMsg);
	}

	/**
	 * 批量删除业务数据模板记录
	 * @param ids
	 * @throws Exception
	 * void
	 * @exception
	 */
	@RequestMapping(value="remove",method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除业务数据模板记录", httpMethod = "DELETE", notes = "批量删除业务数据模板记录")
	public CommonResult<String> remove(@ApiParam(name="ids",value="流程定义id字符串", required = true) @RequestParam String ids) throws Exception{
		String[] aryIds = ids.split(",");
		baseService.removeByIds(aryIds);
		return new CommonResult<String>(true,"删除业务数据模板成功！","");
	}

	/**
	 * 根据表单Key, boAlias 获取表单html， 权限， bo数据结构
	 * @param id
	 * @param action
	 * @param formKey
	 * @param boAlias
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="getForm/{formKey}/{boAlias}",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据表单Key, boAlias 获取表单html， 权限， bo数据结构", httpMethod = "GET", notes = "获取bo数据结构")
	public Map<String,Object> getForm(
			@PathVariable(value = "formKey") String formKey,
			@PathVariable(value = "boAlias") String boAlias,
			@ApiParam(name="id",value="id", required = true) @RequestParam String id,
			@ApiParam(name="action",value="操作类型") @RequestParam String action,
			@ApiParam(name="recordId",value="表单修改记录id", required = false) @RequestParam(required = false) String recordId) throws Exception{
		return baseService.getFormData(formKey, boAlias, id, action, recordId);
	}
	/**
	 * 获取报表草稿
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="getTempDraftData/{draftId}",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取报表草稿", httpMethod = "GET", notes = "获取bo数据结构")
	public Map<String,Object> getTempDraftData( @PathVariable(value = "draftId") String draftId) throws Exception{
		return baseService.getTempDraftData(draftId);
	}



	@RequestMapping(value="boSave/{boAlias}",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存业务数据信息。", httpMethod = "POST", notes = "保存业务数据信息")
	public CommonResult<String> boSave(@ApiParam(name="params",value="业务数据", required = true)@RequestBody ObjectNode params,
			@PathVariable(value="boAlias") String boAlias,
			@RequestParam(value="delDraftId") String delDraftId
			) throws Exception{
		String resultMsg = "保存成功";
		baseService.boSave(params,boAlias,delDraftId);
		return new CommonResult<String>(true, resultMsg);
	}

	/**
	 * 保存业务模板草稿
	 * @param boData
	 * @param draftId
	 * @param tempAlias
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="boSaveDraft",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存业务模板草稿", httpMethod = "POST", notes = "保存业务模板草稿")
	public CommonResult<String> boSaveDraft(@ApiParam(name="dataTemplateDraft",value="数据报表草稿", required = true)@RequestBody FormDataTemplateDraft dataTemplateDraft) throws Exception{
		String resultMsg = "保存草稿成功";
		baseService.boSaveDraft(dataTemplateDraft);
		return new CommonResult<String>(true, resultMsg);
	}

	@RequestMapping(value="boDel/{boAlias}",method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除业务数据记录", httpMethod = "DELETE", notes = "批量删除业务数据记录")
	public CommonResult<String> boDel(@PathVariable(value="boAlias") String boAlias,
			@ApiParam(name="ids",value="业务数据", required = true)@RequestParam String ids) throws IOException{
		String resultMsg = "删除成功";
		// 获取主键
		String[] idArray = ids.split(",");
		baseService.boDel(idArray,boAlias);
		return new CommonResult<String>(true, resultMsg);
	}

	@RequestMapping(value="editTemplate",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取模板", httpMethod = "GET", notes = "获取模板")
	public FormDataTemplate templateHtmlEdit(@ApiParam(name="id",value="模板id", required = true)@RequestParam String id) throws Exception{
		return baseService.get(id);
	}

	@RequestMapping(value="saveTemplate",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存模板信息。", httpMethod = "POST", notes = "保存模板信息")
	public CommonResult<String> saveTemplate(@ApiParam(name="id",value="模板id", required = true)@RequestParam String id,
			@ApiParam(name="templateHtml",value="模板html", required = true)@RequestBody String templateHtml) throws IOException{
		String resultMsg = "保存成功";
		FormDataTemplate template = baseService.get(id);
		template.setTemplateHtml(templateHtml);
		baseService.update(template);
		return new CommonResult<String>(true,resultMsg);
	}
	/**
	 * 取得 BpmDataTemplate 实体
	 * @param json
	 * @return
	 * @throws Exception
	 */
	protected FormDataTemplate getFormObject(String json) throws Exception {

		if (StringUtil.isEmpty(json))
			return null;
		ObjectNode obj = (ObjectNode) JsonUtil.toJsonNode(json);

		String displayField = obj.get("displayField").asText();
		String conditionField = obj.get("conditionField").asText();
		String sortField = obj.get("sortField").asText();
		String filterField = obj.get("filterField").asText();
		String manageField = obj.get("manageField").asText();
		String exportField = obj.get("exportField").asText();
		String treeField = obj.get("treeField").asText();

		obj.remove("displayField");
		obj.remove("conditionField");
		obj.remove("sortField");
		obj.remove("filterField");
		obj.remove("manageField");
		obj.remove("treeField");

		FormDataTemplate bpmDataTemplate = (FormDataTemplate) JsonUtil.toBean(obj, FormDataTemplate.class);

		bpmDataTemplate.setDisplayField(displayField);
		bpmDataTemplate.setConditionField(conditionField);
		bpmDataTemplate.setSortField(sortField);
		bpmDataTemplate.setFilterField(filterField);
		bpmDataTemplate.setManageField(manageField);
		bpmDataTemplate.setExportField(exportField);
		bpmDataTemplate.setTreeField(treeField);

		return bpmDataTemplate;
	}


	/**
	 * 获取业务数据模板数据
	 *
	 * @param id
	 * @param formKey
	 * @return
	 * @throws Exception
	 *             ModelAndView
	 */
	@RequestMapping(value="getJson",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取业务数据模板数据", httpMethod = "GET", notes = "获取业务数据模板数据")
	public FormDataTemplate getJsonData(@ApiParam(name="id",value="模板id", required = false)@RequestParam Optional<String> id,
			@ApiParam(name="formKey",value="表单key", required = true)@RequestParam String formKey) throws Exception {
		FormDataTemplate bpmDataTemplate = null;
		String templateId = id.orElse("");
		if(StringUtil.isNotEmpty(id.orElse(""))) {
			bpmDataTemplate = baseService.get(templateId);
		}else if (StringUtil.isNotEmpty(formKey)) {
			bpmDataTemplate = baseService.getExportDisplay(formKey);
		}
		return bpmDataTemplate;
	}

	@RequestMapping(value="export",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "导出数据", httpMethod = "POST", notes = "导出数据")
	public void export(HttpServletResponse response,
			@ApiParam(name="formKey",value="表单key", required = true)@RequestParam String formKey,
			@ApiParam(name="getType",value="getType", required = false)@RequestParam String getType,
			@ApiParam(name="filterKey",value="filterKey", required = false)@RequestParam String filterKey,
			@ApiParam(name="expField",value="expField", required = true)@RequestParam String expField,
			@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter queryFilter) throws Exception {
		baseService.exportData(response, formKey, getType, filterKey, expField, queryFilter);
	}
	
	/**
	 * 下载用于导入bo主表的excel模板
	 */
	@RequestMapping(value="downloadMainTempFile/{alias}", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "下载导入的模板", httpMethod = "POST", notes = "下载导入的模板")
	public void downloadMainTempFile(HttpServletResponse response,
			@ApiParam(name="alias",value="模板别名", required = true)@PathVariable String alias) throws Exception {
		baseService.downloadMainTempFile(response,alias);
	}

	@RequestMapping(value="getVarList",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取变量", httpMethod = "GET", notes = "获取当前用户相关变量")
	public List<IContextVar> getVarList() throws Exception{
		List<IContextVar> comVarList = (List<IContextVar>) AppUtil.getBean("queryViewComVarList");
		return comVarList;
	}

	@RequestMapping(value="getSubData",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据实体别名和外键id获取子表数据", httpMethod = "GET", notes = "根据实体别名和外键id获取子表数据")
	public List<Map<String, Object>> getSubData(@ApiParam(name="alias",value="实体别名", required = true) @RequestParam String alias,
												@ApiParam(name="refId",value="外键id", required = true) @RequestParam String refId) throws Exception{
		return baseService.getSubData(alias, refId);
	}

	@PostMapping(value="getSubDataPagination",produces={ "application/json; charset=utf-8" })
	@ApiOperation(value = "根据实体别名和外键id获取子表数据(分页)", httpMethod = "GET", notes = "根据实体别名和外键id获取子表数据（分页）")
	public PageList<Map<String,Object>> getSubDataPagination(@ApiParam(name="queryFilter") @RequestBody QueryFilter queryFilter,
															 @ApiParam(name="alias",value="实体别名", required = true) @RequestParam String alias,
															 @ApiParam(name="refId",value="外键id", required = true) @RequestParam String refId) throws Exception{
		return baseService.getSubDataPagination(queryFilter, alias, refId);
	}

	@PostMapping(value="importSub",produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "导入子表数据", httpMethod = "POST", notes = "导入子表数据")
	public CommonResult<String> importSub(@ApiParam(name = "files", value = "上传的文件流") @RequestBody List<MultipartFile> file,
										  @ApiParam(name = "refId", value = "refId") @RequestParam String refId,
										  @ApiParam(name = "alias", value = "別名") @RequestParam String alias) throws Exception{
		baseService.importData(file,refId,alias);
		return new CommonResult<>("导入子表数据成功");
	}
	@PostMapping(value="importMain",produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "导入主表数据", httpMethod = "POST", notes = "导入主表数据")
	public CommonResult<String> importMain(@ApiParam(name = "files", value = "上传的文件流") @RequestBody List<MultipartFile> file,
			@ApiParam(name = "alias", value = "数据模板別名") @RequestParam String alias) throws Exception{
		baseService.importMain(file,alias);
		return new CommonResult<>("导入主表数据成功");
	}

	@PostMapping(value="exportSub",produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "导出子表数据", httpMethod = "POST", notes = "导出子表数据")
	public void exportSub(HttpServletResponse response, @ApiParam(name="queryFilter",value="通用查询对象")@RequestBody ExportSubVo exportSubVo) throws Exception{
		baseService.exportSub(response, exportSubVo);
	}

	/**
	 * 业务数据模板明细页面
	 * @param alias
	 * @param needDisplayFileds
	 * @return
	 * @throws Exception
	 * ModelAndView
	 */
	@RequestMapping(value="getBpmDataTemplateInfo",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据表单key获取业务数据模板相关信息", httpMethod = "GET", notes = "根据表单key获取业务数据模板相关信息")
	public CommonResult<BpmDataTemplateInfoVo> getBpmDataTemplateInfo(@ApiParam(name="alias",value="数据报表别名", required = true) @RequestParam String alias,
			@ApiParam(name="needDisplayFileds",value="是否需要显示字段")@RequestParam Optional<Boolean> needDisplayFileds) throws Exception{
		FormDataTemplate bpmDataTemplate = null;
		if(needDisplayFileds.orElse(false)){
			bpmDataTemplate = baseService.getExportDisplay(alias);
		}else{
			bpmDataTemplate = bpmDataTemplateDao.getByAlias(alias);
		}
		if(BeanUtils.isEmpty(bpmDataTemplate)){
			return new CommonResult<BpmDataTemplateInfoVo>(false,"根据报表别名【"+alias+"】未找到对应报表数据！");
		}
		Form bpmForm = formManager.getMainByFormKey(bpmDataTemplate.getFormKey());
		if(BeanUtils.isEmpty(bpmForm)){
			return new CommonResult<BpmDataTemplateInfoVo>(false,"根据表单key【"+bpmDataTemplate.getFormKey()+"】未找到对应表单！");
		}
		BpmDataTemplateInfoVo vo = JsonUtil.toBean(JsonUtil.toJson(bpmDataTemplate), BpmDataTemplateInfoVo.class) ;
		vo.setFormId(bpmForm.getId());
		// 获取主键
		BoDef boDef = boDefService.getByAlias(bpmDataTemplate.getBoDefAlias());
		BoEnt boEnt = (BoEnt) boDef.getBoEnt();
		vo.setPkField(boEnt.getPkKey().toLowerCase());
		return new CommonResult<BpmDataTemplateInfoVo>(true,"获取成功！", vo);
	}
	/**
	 * 获取我的报表草稿数据
	 * @param queryFilter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="getMyDraftList", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "获取我的报表草稿数据(分页条件查询)数据", httpMethod = "POST", notes = "获取我的报表草稿数据(分页条件查询)数据")
	public PageList<FormDataTemplateDraft> getMyDraftListy(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter queryFilter) throws Exception{
		IUser currentUser = ContextUtil.getCurrentUser();
		queryFilter.addFilter("CREATE_BY_", currentUser.getUserId(), QueryOP.EQUAL, FieldRelation.AND);
		FieldSort sort = new FieldSort("create_time_", Direction.DESC);
		queryFilter.getSorter().add(sort);
		PageList<FormDataTemplateDraft> query = dataTemplateDraftManager.query(queryFilter);
		return query;
	}
	/**
	 * 批量删除业务数据模板草稿记录
	 * @param ids
	 * @throws Exception
	 * void
	 * @exception
	 */
	@RequestMapping(value="removeTempDraft",method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除业务数据模板草稿记录", httpMethod = "DELETE", notes = "批量删除业务数据模板草稿记录")
	public CommonResult<String> removeTempDraft(@ApiParam(name="ids",value="草稿id", required = true) @RequestParam String ids) throws Exception{
		String[] aryIds = ids.split(",");
		dataTemplateDraftManager.removeByIds(aryIds);
		return new CommonResult<String>(true,"删除成功！","");
	}

    @RequestMapping(value="exportXml" ,method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
    @ApiOperation(value = "导出数据报表xml", httpMethod = "GET", notes = "导出流程定义xml")
    public void exportXml( HttpServletRequest request, HttpServletResponse response,
                           @ApiParam(name="ids",value="数据报表id", required = true) @RequestParam String ids) throws Exception {
        response.setContentType("APPLICATION/OCTET-STREAM");
        if (BeanUtils.isNotEmpty(ids)) {
            String[] stringsIds = ids.split(",");
            List<String> list = Arrays.asList(stringsIds);
            String zipName = "ht_formDataTemplate_"+ DateFormatUtil.format(LocalDateTime.now(), "yyyy_MMdd_HHmmss");
            // 写XML
            Map<String, String> strXml = baseService.exportDef(list);
            HttpUtil.downLoadFile(request, response, strXml,zipName);
        }
    }

    @RequestMapping(value="importSave", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
    @ApiOperation(value = "数据报表导入，根据传入的文件id从缓存中取出xml文件导入，并清除缓存", httpMethod = "POST", notes = "附件上传操作")
    public Object importSave(@ApiParam(name="confirmImport",value="确认导入",required=false) @RequestParam Optional<Boolean> confirmImport,
                             @ApiParam(name="typeId",value="表单标识",required=false) @RequestParam Optional<String> typeId,
                             @ApiParam(name="cacheFileId",value="缓存的数据报表文件id",required=false) @RequestParam Optional<String> cacheFileId) throws Exception {

        CommonResult<String> message = null;
        try {
            if (confirmImport.orElse(false)) {
                String byKey = baseService.getImportFileFromCache(cacheFileId.get());
                if(StringUtil.isEmpty(byKey)) {
                    return new CommonResult<String>(false, "导入失败:上传的文件已从缓存中清除，请重新导入。");
                }
                ObjectNode objectNode = (ObjectNode) JsonUtil.toJsonNode(byKey);
                message = baseService.importDef(objectNode,typeId.orElse(""));
            }
            baseService.delImportFileFromCache(cacheFileId.orElse(""));
        } catch (Exception e) {
            message = new CommonResult<String>(false, "导入失败:" + ExceptionUtils.getRootCauseMessage(e));
        }
        return message;
    }

    @RequestMapping(value="importCheck", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
    @ApiOperation(value = "流程导入前校验,通过校验则直接导入，如有重复流程，则返回流程xml的缓存key，待用户确认覆盖后再次导入", httpMethod = "POST", notes = "流程导入前校验")
    public Object importCheck(MultipartHttpServletRequest request, HttpServletResponse response,
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
            String rootRealPath = (FileUtil.getIoTmpdir() +"/attachFiles/unZip/").replace("/", File.separator);
            FileUtil.createFolder(rootRealPath, true);
            // 解压文件
            ZipUtil.unZipFile(fileLoad, rootRealPath);
            unZipFilePath = rootRealPath + File.separator + fileDir; // 解压后文件的真正路径

            String formDataTemplatesXml = FileUtil.readFile( unZipFilePath + "/formDataTemplates.form.xml");

            if (StringUtils.isEmpty(formDataTemplatesXml)) throw new Exception("导入的未按指定的格式");

            checkXmlFormat(formDataTemplatesXml);

            FormDataTemplateXmlList formDataTemplateXmlList=(FormDataTemplateXmlList) JAXBUtil.unmarshall(formDataTemplatesXml, FormDataTemplateXmlList.class);
            List<FormDataTemplateXml> list= formDataTemplateXmlList.getFormDataTemplateXmlList();
            List<String> names = new ArrayList<String>();
            for(FormDataTemplateXml formDataTemplateXml:list){
                FormDataTemplate formDataTemplate = formDataTemplateXml.getFormDataTemplate();
                if(BeanUtils.isNotEmpty(formDataTemplate)){
                    FormDataTemplate oformDataTemplate = baseService.getByAlias(formDataTemplate.getAlias());
                    if(oformDataTemplate != null){
                        names.add(formDataTemplate.getName()+"（"+formDataTemplate.getAlias()+"）");
                    }
                }
            }
            ObjectNode obj = JsonUtil.getMapper().createObjectNode();
            obj.put("formDataTemplatesXml", formDataTemplatesXml);
            if(BeanUtils.isEmpty(names)){
                message = baseService.importDef(obj,typeId.orElse(""));
            }else{
                String cacheFileId = UniqueIdUtil.getSuid();
                baseService.putImportFileInCache(cacheFileId, JsonUtil.toJson(obj));
                message = new CommonResult<String>(false, "导入失败，数据报表【" + String.join("，",names)+"】在系统中已存在，是否覆盖？",cacheFileId);
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

    @SuppressWarnings("unchecked")
    public static void checkXmlFormat(String xml) throws Exception {
        String firstName = "formDataTemplateXmlList";
        String nextName = "formDataTemplateXml";
        Document doc = Dom4jUtil.loadXml(xml);
        Element root = doc.getRootElement();
        String msg = "导入文件格式不对";
        if (!root.getName().equals(firstName))
            throw new Exception(msg);
        List<Element> itemLists = root.elements();
        for (Element elm : itemLists) {
            if (!elm.getName().equals(nextName))
                throw new Exception(msg);
        }

    }
    
    @PostMapping(value="tImportMain",produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "导入主表数据", httpMethod = "POST", notes = "导入主表数据")
	public CommonResult<String> tImportMain(@ApiParam(name = "files", value = "上传的文件流") @RequestBody List<MultipartFile> file,
			@ApiParam(name = "alias", value = "数据模板別名") @RequestParam String alias,
			@ApiParam(name = "bindFilld", value = "填充的字段名") @RequestParam Optional<String> bindFilld,
			@ApiParam(name = "fillValue", value = "填充的字段值") @RequestParam Optional<String> fillValue) throws Exception{
		
		FormDataTemplate template = baseService.getByAlias(alias);
		BoDef boDef = boDefService.getByDefId(template.getBoDefId());
		BoEnt boEnt = boDef.getBoEnt();
		ArrayNode btns = (ArrayNode) JsonUtil.toJsonNode(template.getManageField());
		int limt = 1000;
		for (JsonNode btn : btns) {
			if ("import".equals(btn.get("name").asText()) && btn.hasNonNull("limit")) {
				limt = btn.get("limit").asInt();
			}
		}
		
		Iterator<MultipartFile> it = file.iterator();
		while (it.hasNext()) {
			MultipartFile f= it.next();
			List<BoAttribute> columnList = boEnt.getColumnList();
			Map<String, BoAttribute> columnMap =new HashMap<>();
			for (BoAttribute boAttribute : columnList) {
				columnMap.put(boAttribute.getDesc(), boAttribute);
			}
			List<Map<String, Object>> rows = baseService.resolutionExcel(f,columnMap,bindFilld.orElse(""));
			if (rows.size() > limt) {
				throw new RuntimeException("超过单次导入最大限制："+limt+"条");
			}
			
			ExecutorService executorService = Executors.newCachedThreadPool();
			IUser currentUser = ContextUtil.getCurrentUser();
			executorService.execute(() -> {
				try {
					ContextUtil.setCurrentUser(currentUser);
					baseContext.setTempTenantId(currentUser.getTenantId());
					long currentTimeMillis = System.currentTimeMillis();
					baseService.checkAndImportData(rows,boEnt,columnMap,bindFilld.orElse(""),fillValue.orElse(""),template);
					System.err.println("导入："+rows.size()+"条数据，用时："+(System.currentTimeMillis()-currentTimeMillis)/1000+"秒");
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
		return new CommonResult<>("上传成功，请稍后查询结果");
	}
}
